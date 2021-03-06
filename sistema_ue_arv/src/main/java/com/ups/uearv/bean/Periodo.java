/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

import com.ups.uearv.entidades.MatPeriodo;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Ja�n - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "periodo")
@ViewScoped
public class Periodo implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idPeriodo = "";
	String itDescripcion = "";	
	String soJornada = "";
	Date cFechaIni = new Date();
	Date cFechaFin = new Date();	
	int inCantPension = 0;	
	BigDecimal inValorMatricula = new BigDecimal(0);
	BigDecimal inValorPension = new BigDecimal(0);

	boolean ckEstado = false;
	boolean ckCerrado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> periodoList = new ArrayList<Object>();

	ArrayList<SelectItem> listJornada = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listJornada = (ArrayList<SelectItem>) llenaComboJornada();
		soJornada = listJornada.get(0).getValue().toString();

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		periodoList.clear();
		List<Object> l = DAO.nqObject(new MatPeriodo(), jpql);

		for (Object in : l)
			periodoList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_periodo c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' ORDER BY c.descripcion ";
		} else {
			jpql = " SELECT c.* FROM mat_periodo c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.descripcion ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {

		// VALIDACIONES
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar el per�odo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (cFechaIni == null) {
			mensaje = "Debe seleccionar la fecha inicio";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (cFechaFin == null) {
			mensaje = "Debe seleccionar la fecha fin";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (inCantPension == 0) {
			mensaje = "Debe ingresar la cantidad de pensiones";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}				
		if (inValorMatricula == BigDecimal.ZERO) {
			mensaje = "Debe ingresar el valor de la matr�cula";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}			
		if (inValorPension == BigDecimal.ZERO) {
			mensaje = "Debe ingresar el valor de las pensiones";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());

			MatPeriodo ob = new MatPeriodo();
			if (accion == 1) {
				ob = (MatPeriodo) DAO.buscarObject(new MatPeriodo(), "from MatPeriodo c where c.idPeriodo = '" + idPeriodo + "'");
			}

			ob.setDescripcion(itDescripcion);
			ob.setCantPensiones(inCantPension);
			ob.setPrecioMatricula(inValorMatricula);
			ob.setPrecioPension(inValorPension);
			ob.setFechaIni(cFechaIni);
			ob.setFechaFin(cFechaFin);
			ob.setJornada(soJornada);

			String estado = "IC";
			if (ckEstado) estado = "AC";	
			
			String cerrado = "NO";
			if (ckCerrado) cerrado = "SI";

			ob.setEstado(estado);
			ob.setSnCerrado(cerrado);
			
			if (accion == 0) {
				ob.setUsuarioIng(Session.getUserName());			
				ob.setFechaIng(fecha);				
			}			
			if (accion == 1) {
				ob.setUsuarioAct(Session.getUserName());			
				ob.setFechaAct(fecha);			
			}			

			if (DAO.saveOrUpdate(ob, accion, em)) {
				em.getTransaction().commit();
				mensaje = "Guardado exitoso";
				FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
				buscar();
			} else {
				em.getTransaction().rollback();
				mensaje = "Error al guardar";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			}

		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}

	public void closeDialogo() {
		init();
	}

	public List<SelectItem> llenaComboJornada() {
		return Util.llenaCombo(DAO.getDetCatalogo("JO000"), 2);
	}

	public void onDateSelect() {		
		try {
			int no = Util.diferenciaEnMeses(cFechaIni, cFechaFin);
			inCantPension = no + 1;
		} catch (ParseException e) {
			inCantPension = 0;			
		}		
	}

	// GETTERS AND SETTERS
	public boolean isCkEstado() {
		return ckEstado;
	}
	public void setCkEstado(boolean ckEstado) {
		this.ckEstado = ckEstado;
	}
	public String getItBuscar() {
		return itBuscar;
	}
	public void setItBuscar(String itBuscar) {
		this.itBuscar = itBuscar;
	}
	public boolean isCkMostrarIC() {
		return ckMostrarIC;
	}
	public void setCkMostrarIC(boolean ckMostrarIC) {
		this.ckMostrarIC = ckMostrarIC;
	}
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}	
	public String getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(String idPeriodo) {
		Periodo.idPeriodo = idPeriodo;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
	public String getSoJornada() {
		return soJornada;
	}
	public void setSoJornada(String soJornada) {
		this.soJornada = soJornada;
	}
	public Date getcFechaIni() {
		return cFechaIni;
	}
	public void setcFechaIni(Date cFechaIni) {
		this.cFechaIni = cFechaIni;
	}
	public Date getcFechaFin() {
		return cFechaFin;
	}
	public void setcFechaFin(Date cFechaFin) {
		this.cFechaFin = cFechaFin;
	}
	public int getInCantPension() {
		return inCantPension;
	}
	public void setInCantPension(int inCantPension) {
		this.inCantPension = inCantPension;
	}
	public BigDecimal getInValorMatricula() {
		return inValorMatricula;
	}
	public void setInValorMatricula(BigDecimal inValorMatricula) {
		this.inValorMatricula = inValorMatricula;
	}
	public BigDecimal getInValorPension() {
		return inValorPension;
	}
	public void setInValorPension(BigDecimal inValorPension) {
		this.inValorPension = inValorPension;
	}
	public List<Object> getPeriodoList() {
		return periodoList;
	}
	public void setPeriodoList(List<Object> periodoList) {
		this.periodoList = periodoList;
	}
	public ArrayList<SelectItem> getListJornada() {
		return listJornada;
	}
	public void setListJornada(ArrayList<SelectItem> listJornada) {
		this.listJornada = listJornada;
	}
	public boolean isCkCerrado() {
		return ckCerrado;
	}
	public void setCkCerrado(boolean ckCerrado) {
		this.ckCerrado = ckCerrado;
	}	
}