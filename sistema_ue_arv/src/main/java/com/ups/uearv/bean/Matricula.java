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
import java.util.HashSet;
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

import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;

import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "matricula")
@ViewScoped
public class Matricula implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idMatricula = "";
	String soMatricula = "";
	String soEstudiante = "";	
	String itObservacion = "";
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> matriculaList = new ArrayList<Object>();
	private List<Object> filtroMatricula = new ArrayList<>();

	ArrayList<SelectItem> listEstudiante = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOferta = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {

		buscar();
	}


	public void cellChangeEvent(final SheetEvent event) {  
		final Sheet sheet = event.getSheet();  
		final List<SheetUpdate> updates = sheet.getUpdates();  
		// A SheetUpdate exists for each column updated, the same row may appear more than once. For this reason we will track those we already persisted  
		final HashSet<MatMatricula> processed = new HashSet<MatMatricula>();  
		int rowUpdates = 0;  
		for (final SheetUpdate update : updates) {  
			final Object asset = (Object) update.getRowData();  
			if (processed.contains(asset)) {  
				continue;  
			}  
			System.out.println("Asset " + ((MatMatricula) asset).getIdMatricula() + " updated.");  
			rowUpdates++;  
		}  
		sheet.commitUpdates();  
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Success", Integer.toString(rowUpdates) + " rows updated"));  
	}  


	// CONSULTA
	public void llenarLista(String jpql) {
		matriculaList.clear();
		List<Object> l = DAO.nqObject(new MatMatricula(), jpql);

		for (Object in : l)
			matriculaList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_matricula c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' ORDER BY c.descripcion ";
		} else {
			jpql = " SELECT c.* FROM mat_matricula c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.descripcion ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {

		// VALIDACIONES

		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());

			MatMatricula ob = new MatMatricula();
			if (accion == 1) {
				ob = (MatMatricula) DAO.buscarObject(new MatMatricula(), "from MatMatricula c where c.idMatricula = '" + idMatricula + "'");
			}


			String estado = "IC";
			if (ckEstado) estado = "AC";	

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

	public List<SelectItem> llenaComboEstudiantes() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboCursos() {
		return Util.llenaCombo(DAO.getCursos(), 2);
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
	public String getidMatricula() {
		return idMatricula;
	}
	public void setidMatricula(String idMatricula) {
		Matricula.idMatricula = idMatricula;
	}
	public List<Object> getMatriculaList() {
		return matriculaList;
	}
	public void setMatriculaList(List<Object> matriculaList) {
		this.matriculaList = matriculaList;
	}
	public String getSoMatricula() {
		return soMatricula;
	}
	public void setSoMatricula(String soMatricula) {
		this.soMatricula = soMatricula;
	}
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public String getItObservacion() {
		return itObservacion;
	}
	public void setItObservacion(String itObservacion) {
		this.itObservacion = itObservacion;
	}
	public List<Object> getFiltroMatricula() {
		return filtroMatricula;
	}
	public void setFiltroMatricula(List<Object> filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}
	public ArrayList<SelectItem> getListEstudiante() {
		return listEstudiante;
	}
	public void setListEstudiante(ArrayList<SelectItem> listEstudiante) {
		this.listEstudiante = listEstudiante;
	}
	public ArrayList<SelectItem> getListOferta() {
		return listOferta;
	}
	public void setListOferta(ArrayList<SelectItem> listOferta) {
		this.listOferta = listOferta;
	}
}