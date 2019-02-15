/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;

import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporteGes")
@ViewScoped
public class ReporteGes implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	private static final long serialVersionUID = 1L;

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";
	String jpql = "";

	String soPeriodo = "";

	String soOferta = "";
	String soEstudiante = "";

	ArrayList<SelectItem> listPeriodo = new ArrayList<SelectItem>();

	private List<EstacoCuentaCab> estadoCuentaList = new ArrayList<EstacoCuentaCab>();

	private LineChartModel lineModel;

	@PostConstruct
	public void init() {		
		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";

		createLineModels();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}	

	public void verEstadoCuentas() {
		estadoCuentaList.clear();
		if (soPeriodo.equals("NA")) {
			mensaje = "Debe seleccionarel período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}

		List<Object> result1 = getEstadoCuentaCab(soPeriodo);
		Iterator<Object> itr1 = result1.iterator();
		for (int k1 = 0; k1 < result1.size(); k1++) {
			Object[] cab = (Object[]) itr1.next();

			EstacoCuentaCab ec = new EstacoCuentaCab();
			ec.setOferta(cab[0].toString());
			ec.setEstudiante(cab[1].toString());
			String idEstudiante = cab[2].toString();
			ec.setCedula(idEstudiante);
			ec.setSumPrecio(new BigDecimal(cab[3].toString()));
			ec.setSumTotal(new BigDecimal(cab[4].toString()));
			ec.setSumSaldo(new BigDecimal(cab[5].toString()));
			ec.setSumPagado(new BigDecimal(cab[6].toString()));

			List<Object> result2 = getEstadoCuentaDet(soPeriodo, idEstudiante);
			Iterator<Object> itr2 = result2.iterator();
			for (int k2 = 0; k2 < result2.size(); k2++) {
				Object[] det = (Object[]) itr2.next();			
				EstacoCuentaDet ed = new EstacoCuentaDet();
				ed.setOferta(det[0].toString());
				ed.setEstudiante(det[1].toString());
				ed.setConcepto(det[2].toString());				
				ed.setPrecio(new BigDecimal(det[3].toString()));
				ed.setDescuento(new BigDecimal(det[4].toString()));
				ed.setTotal(new BigDecimal(det[5].toString()));
				ed.setSaldo(new BigDecimal(det[6].toString()));
				ed.setPagado(new BigDecimal(det[7].toString()));				
				ed.setNomDescuento(det[8].toString());
				ec.detalleList.add(ed);
			}
			estadoCuentaList.add(ec);
		}		
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();
		ChartSeries datos = new ChartSeries();

		if (soPeriodo.equals("NA")) {
			datos.setLabel("No hay datos");
			datos.set("[---------] [--- -----] [-]", 0);
			model.addSeries(datos);

			mensaje = "Debe seleccionarel período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return model;
		}

		List<Object> result = (List<Object>) DAO.getGraficaEstadoCuentas(soPeriodo);
		Iterator<Object> itr = result.iterator();
		if (!result.isEmpty()) {
			ChartSeries t = new ChartSeries();
			ChartSeries p = new ChartSeries();
			t.setLabel("Total a pagar");
			p.setLabel("Pagado");

			for (int k = 0; k < result.size(); k++) {
				Object[] obj = (Object[]) itr.next();
				String oferta = obj[0].toString();
				BigDecimal total = new BigDecimal(obj[1].toString());
				BigDecimal pagad = new BigDecimal(obj[2].toString());
				t.set(oferta, total);				
				p.set(oferta, pagad);				
			}
			model.addSeries(t);
			model.addSeries(p);
		} else {
			datos.setLabel("No hay datos");
			datos.set("[---------] [--- -----] [-]", 0);
			model.addSeries(datos);
		}

		return model;
	}

	public void createLineModels() {
		lineModel = initCategoryModel();
		lineModel.setTitle("Valores Totales/Pagados por Oferta");
		lineModel.setLegendPosition("e");
		lineModel.setLegendPlacement(LegendPlacement.OUTSIDE);
		lineModel.setExtender("extChart2");
		lineModel.setSeriesColors("2b56fb,00a015");
		lineModel.setShowPointLabels(true);
		lineModel.getAxes().put(AxisType.X, new CategoryAxis("Ofertas"));
		
		Axis xAxis = lineModel.getAxis(AxisType.X);
		xAxis.setTickAngle(-40);
		
		Axis yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		yAxis.setMin(0);
		// yAxis.setMax(200);
		yAxis.setTickFormat("%.2f");
	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodosRep(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}

	// ESTADO DE CUENTAS
	@SuppressWarnings("unchecked")
	public static List<Object> getEstadoCuentaCab(String idPeriodo) {
		Query query = em.createNativeQuery(" CALL consulta_estado_cuenta_cab (" + idPeriodo + ") ");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<Object> getEstadoCuentaDet(String idPeriodo, String idEstudiante) {
		Query query = em.createNativeQuery(" CALL consulta_estado_cuenta_det (" + idPeriodo +", '" + idEstudiante +"') ");
		return query.getResultList();
	}

	// SUBCLASES
	public class EstacoCuentaCab implements Serializable {

		private static final long serialVersionUID = 1L;

		public String oferta;
		public String estudiante;
		public String cedula;
		public List<EstacoCuentaDet> detalleList = new ArrayList<EstacoCuentaDet>();;
		public BigDecimal sumPrecio;
		public BigDecimal sumTotal;
		public BigDecimal sumSaldo;
		public BigDecimal sumPagado;		

		public String getCedula() {
			return cedula;
		}
		public void setCedula(String cedula) {
			this.cedula = cedula;
		}
		public String getOferta() {
			return oferta;
		}
		public void setOferta(String oferta) {
			this.oferta = oferta;
		}
		public String getEstudiante() {
			return estudiante;
		}
		public void setEstudiante(String estudiante) {
			this.estudiante = estudiante;
		}
		public List<EstacoCuentaDet> getDetalleList() {
			return detalleList;
		}
		public void setDetalleList(List<EstacoCuentaDet> detalleList) {
			this.detalleList = detalleList;
		}
		public BigDecimal getSumPrecio() {
			return sumPrecio;
		}
		public void setSumPrecio(BigDecimal sumPrecio) {
			this.sumPrecio = sumPrecio;
		}
		public BigDecimal getSumTotal() {
			return sumTotal;
		}
		public void setSumTotal(BigDecimal sumTotal) {
			this.sumTotal = sumTotal;
		}
		public BigDecimal getSumSaldo() {
			return sumSaldo;
		}
		public void setSumSaldo(BigDecimal sumSaldo) {
			this.sumSaldo = sumSaldo;
		}
		public BigDecimal getSumPagado() {
			return sumPagado;
		}
		public void setSumPagado(BigDecimal sumPagado) {
			this.sumPagado = sumPagado;
		}		
	}

	public class EstacoCuentaDet implements Serializable {

		private static final long serialVersionUID = 1L;

		public String oferta;
		public String estudiante;
		public String concepto;
		public String nomDescuento;
		public BigDecimal precio;
		public BigDecimal descuento;
		public BigDecimal total;
		public BigDecimal saldo;
		public BigDecimal pagado;
		public String getOferta() {
			return oferta;
		}
		public void setOferta(String oferta) {
			this.oferta = oferta;
		}
		public String getEstudiante() {
			return estudiante;
		}
		public void setEstudiante(String estudiante) {
			this.estudiante = estudiante;
		}
		public String getConcepto() {
			return concepto;
		}
		public void setConcepto(String concepto) {
			this.concepto = concepto;
		}
		public BigDecimal getPrecio() {
			return precio;
		}
		public void setPrecio(BigDecimal precio) {
			this.precio = precio;
		}
		public BigDecimal getDescuento() {
			return descuento;
		}
		public void setDescuento(BigDecimal descuento) {
			this.descuento = descuento;
		}
		public BigDecimal getTotal() {
			return total;
		}
		public void setTotal(BigDecimal total) {
			this.total = total;
		}
		public BigDecimal getSaldo() {
			return saldo;
		}
		public void setSaldo(BigDecimal saldo) {
			this.saldo = saldo;
		}
		public BigDecimal getPagado() {
			return pagado;
		}
		public void setPagado(BigDecimal pagado) {
			this.pagado = pagado;
		}
		public String getNomDescuento() {
			return nomDescuento;
		}
		public void setNomDescuento(String nomDescuento) {
			this.nomDescuento = nomDescuento;
		}		
	}

	// GETTERS AND SETTERS
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
	}
	public String getSoOferta() {
		return soOferta;
	}
	public void setSoOferta(String soOferta) {
		this.soOferta = soOferta;
	}
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public ArrayList<SelectItem> getListPeriodo() {
		return listPeriodo;
	}
	public void setListPeriodo(ArrayList<SelectItem> listPeriodo) {
		this.listPeriodo = listPeriodo;
	}
	public List<EstacoCuentaCab> getEstadoCuentaList() {
		return estadoCuentaList;
	}
	public void setEstadoCuentaList(List<EstacoCuentaCab> estadoCuentaList) {
		this.estadoCuentaList = estadoCuentaList;
	}
	public LineChartModel getLineModel() {
		return lineModel;
	}
	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}
}