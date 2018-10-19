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

import com.ups.uearv.entidades.CalAsignatura;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "asignatura")
@ViewScoped
public class Asignatura implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idAsignatura = "";
	String itNombre = "";
	String itDescripcion = "";
	String soArea = "";
	String soNivel = "";
	boolean ckEstado = false;
	int inHoras = 0;
	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> asignaturaList = new ArrayList<Object>();

	ArrayList<SelectItem> listAreas = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listNiveles = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listAreas = (ArrayList<SelectItem>) llenaComboAreas();
		soArea = listAreas.get(0).getValue().toString();
		
		listNiveles = (ArrayList<SelectItem>) llenaComboNiveles();
		soNivel = listNiveles.get(0).getValue().toString();

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		asignaturaList.clear();
		List<Object> l = DAO.nqObject(new CalAsignatura(), jpql);
				
		for (Object in : l)
			asignaturaList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM cal_asignatura c WHERE c.nombre LIKE '%"
					+ itBuscar + "%' ORDER BY c.nivel, c.id_asignatura ";
		} else {
			jpql = " SELECT c.* FROM cal_asignatura c WHERE c.nombre LIKE '%"
					+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.nivel, c.id_asignatura  ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itNombre.trim().equals("")) {
			mensaje = "Debe ingresar la asignatura";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			CalAsignatura ob = new CalAsignatura();
			if (accion == 1) {
				ob = (CalAsignatura) DAO.buscarObject(new CalAsignatura(), "from CalAsignatura c where c.idAsignatura = " + idAsignatura);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			ob.setNombre(itNombre);
			ob.setDescripcion(itDescripcion);
			ob.setNivel(soNivel);
			ob.setArea(soArea);
			ob.setHoras(inHoras);
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
	
	public List<SelectItem> llenaComboAreas() {
		return Util.llenaCombo(DAO.getDetCatalogo("CA002"), 2);
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
	public String getIdAsignatura() {
		return idAsignatura;
	}
	public void setIdAsignatura(String idAsignatura) {
		Asignatura.idAsignatura = idAsignatura;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
	public String getSoNivel() {
		return soNivel;
	}
	public void setSoNivel(String soNivel) {
		this.soNivel = soNivel;
	}
	public List<Object> getAsignaturaList() {
		return asignaturaList;
	}
	public void setAsignaturaList(List<Object> asignaturaList) {
		this.asignaturaList = asignaturaList;
	}
	public ArrayList<SelectItem> getListNiveles() {
		return listNiveles;
	}
	public void setListNiveles(ArrayList<SelectItem> listNiveles) {
		this.listNiveles = listNiveles;
	}
	public String getItNombre() {
		return itNombre;
	}
	public void setItNombre(String itNombre) {
		this.itNombre = itNombre;
	}
	public String getSoArea() {
		return soArea;
	}
	public void setSoArea(String soArea) {
		this.soArea = soArea;
	}
	public int getInHoras() {
		return inHoras;
	}
	public void setInHoras(int inHoras) {
		this.inHoras = inHoras;
	}
	public ArrayList<SelectItem> getListAreas() {
		return listAreas;
	}
	public void setListAreas(ArrayList<SelectItem> listAreas) {
		this.listAreas = listAreas;
	}
}