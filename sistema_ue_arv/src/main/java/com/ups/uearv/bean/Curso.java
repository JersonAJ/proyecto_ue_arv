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

import com.ups.uearv.entidades.MatCurso;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "curso")
@ViewScoped
public class Curso implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idCurso = "";
	String itDescripcion = "";
	String soNivel = "";
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> cursoList = new ArrayList<Object>();

	ArrayList<SelectItem> listNiveles = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listNiveles = (ArrayList<SelectItem>) llenaComboNiveles();
		soNivel = listNiveles.get(0).getValue().toString();		
		
		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		cursoList.clear();
		List<Object> l = DAO.nqObject(new MatCurso(), jpql);
				
		for (Object in : l)
			cursoList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_curso c INNER JOIN catalogo_det k ON k.codigo_det = c.nivel WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' ORDER BY k.codigo_det, c.id_curso ";
		} else {
			jpql = " SELECT c.* FROM mat_curso c INNER JOIN catalogo_det k ON k.codigo_det = c.nivel WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' AND c.estado = 'AC' ORDER BY k.codigo_det, c.id_curso ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar el curso";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			MatCurso ob = new MatCurso();
			if (accion == 1) {
				ob = (MatCurso) DAO.buscarObject(new MatCurso(), "from MatCurso c where c.idCurso = " + idCurso);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			ob.setDescripcion(itDescripcion);
			ob.setNivel(soNivel);
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
	public String getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(String idCurso) {
		Curso.idCurso = idCurso;
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
	public List<Object> getCursoList() {
		return cursoList;
	}
	public void setCursoList(List<Object> cursoList) {
		this.cursoList = cursoList;
	}
	public ArrayList<SelectItem> getListNiveles() {
		return listNiveles;
	}
	public void setListNiveles(ArrayList<SelectItem> listNiveles) {
		this.listNiveles = listNiveles;
	}
}