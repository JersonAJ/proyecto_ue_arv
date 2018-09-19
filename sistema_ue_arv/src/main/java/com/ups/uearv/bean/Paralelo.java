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
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ups.uearv.entidades.MatParalelo;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "paralelo")
@ViewScoped
public class Paralelo implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idParalelo = "";
	String itDescripcion = "";	
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> paraleloList = new ArrayList<Object>();

	ArrayList<SelectItem> listNiveles = new ArrayList<SelectItem>();

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
		paraleloList.clear();
		List<Object> l = DAO.nqObject(new MatParalelo(), jpql);
				
		for (Object in : l)
			paraleloList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_paralelo c WHERE c.descripcion LIKE '%" + itBuscar + "%' ORDER BY c.descripcion ";
		} else {
			jpql = " SELECT c.* FROM mat_paralelo c WHERE c.descripcion LIKE '%" + itBuscar + "%' AND c.estado = 'AC' ORDER BY c.descripcion ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar la descripción";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			MatParalelo ob = new MatParalelo();
			if (accion == 1) {
				ob = (MatParalelo) DAO.buscarObject(new MatParalelo(), "from MatParalelo c where c.idParalelo = " + idParalelo);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			ob.setDescripcion(itDescripcion);
			
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
	
	public List<SelectItem> llenaComboNiveles() {
		return Util.llenaCombo(DAO.getDetCatalogo("CA004"), 2);
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
	public String getidParalelo() {
		return idParalelo;
	}
	public void setidParalelo(String idParalelo) {
		Paralelo.idParalelo = idParalelo;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
	public List<Object> getParaleloList() {
		return paraleloList;
	}
	public void setParaleloList(List<Object> paraleloList) {
		this.paraleloList = paraleloList;
	}
	public ArrayList<SelectItem> getListNiveles() {
		return listNiveles;
	}
	public void setListNiveles(ArrayList<SelectItem> listNiveles) {
		this.listNiveles = listNiveles;
	}
}