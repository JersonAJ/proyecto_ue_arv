/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

import com.ups.uearv.entidades.GesDescuento;
import com.ups.uearv.entidades.GesPension;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Ja�n - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "pension")
@ViewScoped
public class Pension implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soOferta = "";	
	String soEstudiante = "";
	String soTipo = "";		
	String soSecuencia = "";
	
	String olDiaActual = "--";

	String soPeriodoDia = "";
	String soDia = "";
	
	static String idPension = "";
	int olMatricula = 0;
	int olSecuencia = 0;
	String olEstudiante = "";
	String soFormaPago = "";
	String soDescuento = "";
	String soOpcion = "";
	
	boolean ckDescuento = false;
	
	
	BigDecimal inValor = new BigDecimal(0);
	BigDecimal inValorPagar = new BigDecimal(0);
	BigDecimal inTotalPagar = new BigDecimal(0);
	BigDecimal inSaldo = new BigDecimal(0);
	BigDecimal inPorcentaje = new BigDecimal(0);
	
	private List<Object> pensionList = new ArrayList<Object>();

	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertas = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listEstudiantes = new ArrayList<SelectItem>();
	
	ArrayList<SelectItem> listFormaPago = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listDescuentos = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {
		listPeriodos = (ArrayList<SelectItem>) llenaComboPeriodo();				
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		listEstudiantes = (ArrayList<SelectItem>) llenaComboEstudiante();

		soPeriodo = "NA";
		soOferta = "NA";
		soEstudiante = "NA";		
		soTipo = "NA";
		soSecuencia = "NA";
		
		closeDialogo();
		
		listDescuentos = (ArrayList<SelectItem>) llenaComboDescuento();
		soDescuento = listDescuentos.get(0).getValue().toString();
		onChangeDescuento();
		 
		listFormaPago = (ArrayList<SelectItem>) llenaComboFormaPago();
		soFormaPago = listFormaPago.get(0).getValue().toString();
	}

	// EVENTOS		
	public void closeDialogo() {
		soPeriodoDia = "NA";
		soDia = "NA";
		
		olDiaActual = "--";
	}

	public void onChangePeriodo() {		
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";
		onChangeOferta();
	}

	public void onChangeOferta() {
		listEstudiantes = (ArrayList<SelectItem>) llenaComboEstudiante();
		soEstudiante = "NA";
		onChangeEstudiante();	
	}

	public void onChangeEstudiante() {
		buscar();	
	}

	public void onChangeTipo() {
		buscar();	
	}

	public void onChangeSec() {
		buscar();	
	}
	
	public void onChangePeriodoDia() {
		olDiaActual = getDiaVenceActual();	
	}
	
	public void onChangeDescuento() {
		GesDescuento des = (GesDescuento)  DAO.buscarObject(new GesDescuento(), "from GesDescuento c where c.idDescuento = '" + soDescuento +"'");
		if (des == null)
			inPorcentaje = new BigDecimal(0);
		else
			inPorcentaje = des.getPorcentaje();	
	}
	
	// CONSULTA		
	public void buscar() {
		jpql = 
		"SELECT p.* \r\n" + 
		"FROM ges_pension p \r\n" + 
		"	INNER JOIN mat_matricula m ON m.id_matricula = p.id_matricula \r\n" +
		"WHERE m.id_periodo = '" + soPeriodo + "' \r\n";
		if (!soOferta.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOferta + "' \r\n"; }
		if (!soEstudiante.equals("NA")) { jpql = jpql + "AND m.id_estudiante = '" + soEstudiante + "' \r\n"; }
		if (!soTipo.equals("NA")) { 
			if (soTipo.equals("M")) { jpql = jpql + "AND p.secuencia = 0 \r\n"; }
			if (soTipo.equals("P")) { 				
				if (soSecuencia.equals("NA")) { jpql = jpql + "AND p.secuencia <> 0 \r\n"; } 
				if (!soSecuencia.equals("NA")) { jpql = jpql + "AND p.secuencia = '" + soSecuencia + "' \r\n"; }
			}
		}
		jpql = jpql + "AND p.estado = 'AC' \r\n" + 
		"ORDER BY p.secuencia \r\n";

		llenarListPensiones();
	}

	public void llenarListPensiones() {
		pensionList.clear();
		if(!soPeriodo.equals("NA")) {			
			List<Object> l = DAO.nqObject(new GesPension(), jpql);

			for (Object in : l)
				pensionList.add(in);
		}
	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboDescuento() {
		return Util.llenaCombo(DAO.getDescuentos(), 2);
	}
	
	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}

	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getEstudiantesOferta(), 2);
	}
	
	public List<SelectItem> llenaComboFormaPago() {
		return Util.llenaCombo(DAO.getDetCatalogo("FP000"), 2);
	}

	public Query getEstudiantesOferta() {		
		jpql = 
		"SELECT e.id_estudiante, CONCAT(IFNULL(SUBSTRING_INDEX(e.nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(e.apellidos, ' ', 1), '')) nombre \r\n" + 
		"FROM mat_estudiante e \r\n" + 
		"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
		"WHERE m.id_periodo = '" + soPeriodo + "' \r\n";		
		if (!soOferta.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOferta + "' \r\n"; }	
		jpql = jpql + "AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
		"ORDER BY 2";

		Query query = em.createNativeQuery(jpql);
		return query;
	}

	// ACCIONES
	public void cambiarDiaVence() {
		// VALIDACIONES
		if (soDia.trim().equals("NA")) {
			mensaje = "Debe seleccionar un d�a";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soPeriodoDia.trim().equals("NA")) {
			mensaje = "Debe seleccionar el per�odo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soDia.trim().equals(olDiaActual)) {
			mensaje = "Debe seleccionar un d�a diferente al actual";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
				
		// PROCESO
		DAO.spActualizaDiaVence(Integer.parseInt(soDia), Integer.parseInt(soPeriodoDia));
		
		mensaje = "Se cambi� el d�a de vencimiento correctamente";
		FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
		
		olDiaActual = getDiaVenceActual();
		
		buscar();
	}
	
	public void pagar() {
		// VALIDACIONES		
		if (inValorPagar == new BigDecimal(0)) {			
			mensaje = "Debe ingresar el valor a pagar";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
				        
		// PROCESO
		Date date = new Date();
		Timestamp fecha = new Timestamp(date.getTime());

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {						
			GesPension pen = (GesPension)  DAO.buscarObject(new GesPension(), "from GesPension c where c.idPension = '" + idPension +"'");
			GesDescuento des = (GesDescuento)  DAO.buscarObject(new GesDescuento(), "from GesDescuento c where c.idDescuento = '" + soDescuento +"'");
			
			if (ckDescuento)
				pen.setGesDescuento(des);
			
			if (soOpcion.equals("AB"))
				pen.setAbono(inValorPagar);
			else if (soOpcion.equals("PT"))
				pen.setFechaPago(fecha);
			
			pen.setSaldo(inSaldo);
			pen.setFormaPago(soFormaPago);
			pen.setTotalPagar(inTotalPagar);					
			pen.setUsuarioAct(Session.getUserName());			
			pen.setFechaAct(fecha);
				
			if (!DAO.saveOrUpdate(pen, 1, em)) {
				em.getTransaction().rollback();
				return;					
			}			

			em.getTransaction().commit();			
			mensaje = "Pago exitoso";
			if (soOpcion.equals("AB"))
				mensaje = "Abono exitoso";			
			FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));		
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}				
	}	
	
	public String getDiaVenceActual() {
		try {
			jpql = "SELECT DAY(p.fecha_vence) FROM ges_pension p INNER JOIN mat_matricula m ON m.id_matricula = p.id_matricula WHERE m.id_periodo = '" + soPeriodoDia + "' LIMIT 1";
			@SuppressWarnings("unchecked")		
			List<Object> result = em.createNativeQuery(jpql).getResultList();		
			Iterator<Object> itr = result.iterator();
			Object obj = (Object) itr.next();
			String dia = String.valueOf(obj);
			return dia;	
		} catch (Exception e) {
			return "--";
		}		
	}	

	// GETTERS AND SETTERS
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}	
	public String getSoSecuencia() {
		return soSecuencia;
	}
	public void setSoSecuencia(String soSecuencia) {
		this.soSecuencia = soSecuencia;
	}
	public ArrayList<SelectItem> getListPeriodos() {
		return listPeriodos;
	}
	public void setListPeriodos(ArrayList<SelectItem> listPeriodos) {
		this.listPeriodos = listPeriodos;
	}
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
	public List<Object> getPensionList() {
		return pensionList;
	}
	public void setPensionList(List<Object> pensionList) {
		this.pensionList = pensionList;
	}
	public ArrayList<SelectItem> getListOfertas() {
		return listOfertas;
	}
	public void setListOfertas(ArrayList<SelectItem> listOfertas) {
		this.listOfertas = listOfertas;
	}
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public ArrayList<SelectItem> getListEstudiantes() {
		return listEstudiantes;
	}
	public void setListEstudiantes(ArrayList<SelectItem> listEstudiantes) {
		this.listEstudiantes = listEstudiantes;
	}
	public String getSoTipo() {
		return soTipo;
	}
	public void setSoTipo(String soTipo) {
		this.soTipo = soTipo;
	}
	public String getSoPeriodoDia() {
		return soPeriodoDia;
	}
	public void setSoPeriodoDia(String soPeriodoDia) {
		this.soPeriodoDia = soPeriodoDia;
	}
	public String getSoDia() {
		return soDia;
	}
	public void setSoDia(String soDia) {
		this.soDia = soDia;
	}
	public String getOlDiaActual() {
		return olDiaActual;
	}
	public void setOlDiaActual(String olDiaActual) {
		this.olDiaActual = olDiaActual;
	}
	public int getOlMatricula() {
		return olMatricula;
	}
	public void setOlMatricula(int olMatricula) {
		this.olMatricula = olMatricula;
	}
	public int getOlSecuencia() {
		return olSecuencia;
	}
	public void setOlSecuencia(int olSecuencia) {
		this.olSecuencia = olSecuencia;
	}
	public String getOlEstudiante() {
		return olEstudiante;
	}
	public void setOlEstudiante(String olEstudiante) {
		this.olEstudiante = olEstudiante;
	}
	public String getSoFormaPago() {
		return soFormaPago;
	}
	public void setSoFormaPago(String soFormaPago) {
		this.soFormaPago = soFormaPago;
	}
	public String getSoDescuento() {
		return soDescuento;
	}
	public void setSoDescuento(String soDescuento) {
		this.soDescuento = soDescuento;
	}
	public boolean isCkDescuento() {
		return ckDescuento;
	}
	public void setCkDescuento(boolean ckDescuento) {
		this.ckDescuento = ckDescuento;
	}
	public BigDecimal getInValor() {
		return inValor;
	}
	public void setInValor(BigDecimal inValor) {
		this.inValor = inValor;
	}
	public BigDecimal getInValorPagar() {
		return inValorPagar;
	}
	public void setInValorPagar(BigDecimal inValorPagar) {
		this.inValorPagar = inValorPagar;
	}
	public BigDecimal getInTotalPagar() {
		return inTotalPagar;
	}
	public void setInTotalPagar(BigDecimal inTotalPagar) {
		this.inTotalPagar = inTotalPagar;
	}
	public BigDecimal getInSaldo() {
		return inSaldo;
	}
	public void setInSaldo(BigDecimal inSaldo) {
		this.inSaldo = inSaldo;
	}
	public ArrayList<SelectItem> getListFormaPago() {
		return listFormaPago;
	}
	public void setListFormaPago(ArrayList<SelectItem> listFormaPago) {
		this.listFormaPago = listFormaPago;
	}
	public ArrayList<SelectItem> getListDescuentos() {
		return listDescuentos;
	}
	public void setListDescuentos(ArrayList<SelectItem> listDescuentos) {
		this.listDescuentos = listDescuentos;
	}
	public String getIdPension() {
		return idPension;
	}
	public void setIdPension(String idPension) {
		Pension.idPension = idPension;
	}
	public BigDecimal getInPorcentaje() {
		return inPorcentaje;
	}
	public void setInPorcentaje(BigDecimal inPorcentaje) {
		this.inPorcentaje = inPorcentaje;
	}
	public String getSoOpcion() {
		return soOpcion;
	}
	public void setSoOpcion(String soOpcion) {
		this.soOpcion = soOpcion;
	}
}