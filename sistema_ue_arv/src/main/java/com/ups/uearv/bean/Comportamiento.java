/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ups.uearv.entidades.CalComportamiento;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "comportamiento")
@ViewScoped
public class Comportamiento implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idComportamiento = "";
	String itAbreviatura = "";
	String itDescripcion = "";	
	boolean ckEstado = false;
	int inEquivalencia = 0;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> comportamientoList = new ArrayList<Object>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		comportamientoList.clear();
		List<Object> l = DAO.nqObject(new CalComportamiento(), jpql);
				
		for (Object in : l)
			comportamientoList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM cal_comportamiento c WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' ORDER BY c.id_comportamiento ";
		} else {
			jpql = " SELECT c.* FROM cal_comportamiento c WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.id_comportamiento ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itAbreviatura.trim().equals("")) {
			mensaje = "Debe ingresar la abreviatura";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar el comportamiento";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			CalComportamiento ob = new CalComportamiento();
			if (accion == 1) {
				ob = (CalComportamiento) DAO.buscarObject(new CalComportamiento(), "from CalComportamiento c where c.idComportamiento = " + idComportamiento);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			ob.setDescripcion(itDescripcion);
			ob.setAbreviatura(itAbreviatura);
			ob.setEquivalencia(inEquivalencia);
			ob.setEstado(estado);
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
	
	// GETTERS AND SETTERS
	public String getIdComportamiento() {
		return idComportamiento;
	}
	public void setIdComportamiento(String idComportamiento) {
		Comportamiento.idComportamiento = idComportamiento;
	}	
	public String getItAbreviatura() {
		return itAbreviatura;
	}
	public void setItAbreviatura(String itAbreviatura) {
		this.itAbreviatura = itAbreviatura;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
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
	public List<Object> getComportamientoList() {
		return comportamientoList;
	}
	public void setComportamientoList(List<Object> comportamientoList) {
		this.comportamientoList = comportamientoList;
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
	public int getInEquivalencia() {
		return inEquivalencia;
	}
	public void setInEquivalencia(int inEquivalencia) {
		this.inEquivalencia = inEquivalencia;
	}	
}