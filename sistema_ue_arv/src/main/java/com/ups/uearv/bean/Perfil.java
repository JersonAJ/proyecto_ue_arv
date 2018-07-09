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

import com.ups.uearv.entidades.SegPerfil;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;


/**
 * @author Jerson Armijos Jaén
 * @version 1.0
 */

@ManagedBean(name = "perfil")
@ViewScoped
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	String id_perfil = "";
	String descripcion = "";
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;
	
	private List<SegPerfil> perfilList = new ArrayList<SegPerfil>();

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
		perfilList.clear();
		List<SegPerfil> l = DAO.nqSegPerfil(jpql); 
		for (SegPerfil in : l) 
			perfilList.add(in);		
	}

	public void buscar() {
		if (ckMostrarIC) 
			jpql = "SELECT * FROM Seg_Perfil WHERE descripcion LIKE '%" + itBuscar	+ "%' ORDER BY descripcion";
		else 		
			jpql = "select * from Seg_Perfil WHERE descripcion like '%" + itBuscar	+ "%' AND estado = 'AC' ORDER BY descripcion";
		
		llenarLista(jpql);
	}
	
	// INGRESO - ACTUALIZACION
	public void guardar() {		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			String estado = "IC";
			if (ckEstado) { estado = "AC"; }				

			SegPerfil ob = new SegPerfil();
			if (accion == 1) {
				ob = DAO.buscarSegPerfil("from SegPerfil c where c.idPerfil = " + id_perfil);
			}
			
			ob.setDescripcion(descripcion);
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
				FacesContext.getCurrentInstance().addMessage("msgRegistro", new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));	
				buscar();		
			} else {
				em.getTransaction().rollback();
				mensaje = "Error al guardar";
				FacesContext.getCurrentInstance().addMessage("msgRegistro", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			}		
		} catch (Exception e) {
			em.getTransaction().rollback();
		}	
		em.close();		
	}
	
	public void closeDialogo() {
		init();
	}

	public boolean getEstado(String estado) {
		boolean ban = true;
		if (estado.equals("IC"))
			ban = false;
		return ban;
	}

	
	// GETTERS AND SETTERS	
	public String getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(String id_perfil) {
		this.id_perfil = id_perfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public List<SegPerfil> getPerfilList() {
		return perfilList;
	}

	public void setPerfilList(List<SegPerfil> perfilList) {
		this.perfilList = perfilList;
	}
}
