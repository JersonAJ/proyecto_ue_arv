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
import com.ups.uearv.entidades.CalReparto;
import com.ups.uearv.entidades.MatCurso;
import com.ups.uearv.entidades.MatDocente;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reparto")
@ViewScoped
public class Reparto implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idReparto = "";
	String soDocente = "";
	String soCurso = "";
	String soNivel = "";
	String soAsignatura = "";
	
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> repartoList = new ArrayList<Object>();

	ArrayList<SelectItem> listNiveles = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listDocentes = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listAsignaturas = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listCursos = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listDocentes = (ArrayList<SelectItem>) llenaComboDocente();
		soDocente = listDocentes.get(0).getValue().toString();
	
		listNiveles = (ArrayList<SelectItem>) llenaComboNiveles();
		soNivel = listNiveles.get(0).getValue().toString();
		
		onChangeNivel();
		
		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		repartoList.clear();
		List<Object> l = DAO.nqObject(new CalReparto(), jpql);
				
		for (Object in : l)
			repartoList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = "SELECT c.* FROM cal_reparto c " + 
					   "INNER JOIN mat_docente d ON d.id_docente = c.id_docente " + 
					   "INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " + 
					   "INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " +
					   "WHERE d.apellidos LIKE '%"+ itBuscar + "%' ORDER BY k.codigo_det, c.id_curso, d.apellidos";
		} else {
			jpql = "SELECT c.* FROM cal_reparto c " + 
					   "INNER JOIN mat_docente d ON d.id_docente = c.id_docente " + 
					   "INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " + 
					   "WHERE d.apellidos LIKE '%"+ itBuscar + "%' AND c.estado = 'AC' ORDER BY k.codigo_det, c.id_curso, d.apellidos";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES		
		if (soDocente.equals("NA")) {
			mensaje = "Debe seleccionar el docente";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soNivel.equals("NA")) {
			mensaje = "Debe seleccionar el nivel";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (soCurso.equals("NA")) {
			mensaje = "Debe seleccionar un curso";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}	
		if (soAsignatura.equals("NA")) {
			mensaje = "Debe seleccionar una asignatura";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (accion == 0) {
			CalReparto val = (CalReparto) DAO.buscarObject(new CalReparto(),
					"from CalReparto c where c.matCurso.idCurso = " + soCurso + " and c.calAsignatura.idAsignatura = "+ soAsignatura + " ");
			if (val != null) {
				mensaje = "Ya existe un docente asignado con los parámetros seleccionados";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
				return;
			}
		}
		
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			CalReparto ob = new CalReparto();
			if (accion == 1) {
				ob = (CalReparto) DAO.buscarObject(new CalReparto(), "from CalReparto c where c.idReparto = " + idReparto);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";
			
			MatDocente docente = (MatDocente) DAO.buscarObject(new MatDocente(), "from MatDocente c where c.idDocente = '" + soDocente + "'");
			CalAsignatura asignatura = (CalAsignatura) DAO.buscarObject(new CalAsignatura(), "from CalAsignatura c where c.idAsignatura = " + soAsignatura);
			MatCurso curso = (MatCurso) DAO.buscarObject(new MatCurso(), "from MatCurso c where c.idCurso = " + soCurso);
			
			ob.setMatDocente(docente);
			ob.setMatCurso(curso);
			ob.setCalAsignatura(asignatura);
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
	
	public List<SelectItem> llenaComboDocente() {
		return Util.llenaCombo(DAO.getDocentes(), 2);
	}
	
	public List<SelectItem> llenaComboAsignaturas() {
		return Util.llenaCombo(DAO.getAsignaturas(soNivel), 2);
	}
	
	public List<SelectItem> llenaComboCursos() {
		return Util.llenaCombo(DAO.getCursos(soNivel), 2);
	}
	
	public void onChangeNivel() {
		listCursos = (ArrayList<SelectItem>) llenaComboCursos();
		soCurso = listCursos.get(0).getValue().toString();
	
		listAsignaturas = (ArrayList<SelectItem>) llenaComboAsignaturas();
		soAsignatura = listAsignaturas.get(0).getValue().toString();
	}
	
	public ArrayList<SelectItem> getCursos() {
		return (ArrayList<SelectItem>) llenaComboCursos();
	}
	
	public ArrayList<SelectItem> getAsignaturas() {
		return (ArrayList<SelectItem>) llenaComboAsignaturas();
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
	public String getidReparto() {
		return idReparto;
	}
	public void setidReparto(String idReparto) {
		Reparto.idReparto = idReparto;
	}
	public String getSoNivel() {
		return soNivel;
	}
	public void setSoNivel(String soNivel) {
		this.soNivel = soNivel;
	}
	public List<Object> getrepartoList() {
		return repartoList;
	}
	public void setrepartoList(List<Object> repartoList) {
		this.repartoList = repartoList;
	}
	public ArrayList<SelectItem> getListNiveles() {
		return listNiveles;
	}
	public void setListNiveles(ArrayList<SelectItem> listNiveles) {
		this.listNiveles = listNiveles;
	}
	public static String getIdReparto() {
		return idReparto;
	}
	public static void setIdReparto(String idReparto) {
		Reparto.idReparto = idReparto;
	}
	public String getSoDocente() {
		return soDocente;
	}
	public void setSoDocente(String soDocente) {
		this.soDocente = soDocente;
	}
	public String getSoCurso() {
		return soCurso;
	}
	public void setSoCurso(String soCurso) {
		this.soCurso = soCurso;
	}
	public String getSoAsignatura() {
		return soAsignatura;
	}
	public void setSoAsignatura(String soAsignatura) {
		this.soAsignatura = soAsignatura;
	}
	public List<Object> getRepartoList() {
		return repartoList;
	}
	public void setRepartoList(List<Object> repartoList) {
		this.repartoList = repartoList;
	}
	public ArrayList<SelectItem> getListDocentes() {
		return listDocentes;
	}
	public void setListDocentes(ArrayList<SelectItem> listDocentes) {
		this.listDocentes = listDocentes;
	}
	public ArrayList<SelectItem> getListAsignaturas() {
		return listAsignaturas;
	}
	public void setListAsignaturas(ArrayList<SelectItem> listAsignaturas) {
		this.listAsignaturas = listAsignaturas;
	}
	public ArrayList<SelectItem> getListCursos() {
		return listCursos;
	}
	public void setListCursos(ArrayList<SelectItem> listCursos) {
		this.listCursos = listCursos;
	}
}