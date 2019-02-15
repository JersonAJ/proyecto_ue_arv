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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
	ArrayList<SelectItem> listOferta = new ArrayList<SelectItem>();		
			
	private List<EstacoCuentaCab> estadoCuentaList = new ArrayList<EstacoCuentaCab>();
	
	@PostConstruct
	public void init() {		
		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";
		onChangePeriodo();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}
	
	public void onChangePeriodo() {
		listOferta = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";
	}
	
	public void verEstadoCuentas() {
		List<Object> result1 = getEstadoCuentaCab(soPeriodo);
		Iterator<Object> itr1 = result1.iterator();
		for (int k1 = 0; k1 < result1.size(); k1++) {
			Object[] cab = (Object[]) itr1.next();

			EstacoCuentaCab ec = new EstacoCuentaCab();
			ec.setOferta(cab[0].toString());
			ec.setEstudiante(cab[1].toString());
			
			String idEstudiante = cab[2].toString();						
			List<Object> result2 = getEstadoCuentaDet(soPeriodo, idEstudiante);
			Iterator<Object> itr2 = result2.iterator();
			for (int k2 = 0; k2 < result2.size(); k2++) {
				Object[] det = (Object[]) itr2.next();			
				EstacoCuentaDet ed = new EstacoCuentaDet();
				ed.setOferta(det[0].toString());
				ed.setEstudiante(det[1].toString());
				ed.setConcepto(det[2].toString());
				ec.detalleList.add(ed);
			}
			estadoCuentaList.add(ec);
		}		
	}
	
	public void createBarEstadoCuentas() {
		
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
		public List<EstacoCuentaDet> detalleList = new ArrayList<EstacoCuentaDet>();;
		
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
	}
	
	public class EstacoCuentaDet implements Serializable {

		private static final long serialVersionUID = 1L;

		public String oferta;
		public String estudiante;
		public String concepto;
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
	public ArrayList<SelectItem> getListOferta() {
		return listOferta;
	}
	public void setListOferta(ArrayList<SelectItem> listOferta) {
		this.listOferta = listOferta;
	}

	public List<EstacoCuentaCab> getEstadoCuentaList() {
		return estadoCuentaList;
	}
	public void setEstadoCuentaList(List<EstacoCuentaCab> estadoCuentaList) {
		this.estadoCuentaList = estadoCuentaList;
	}
}