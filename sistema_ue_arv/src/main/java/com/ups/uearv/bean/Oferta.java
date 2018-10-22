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
import com.ups.uearv.entidades.MatOferta;
import com.ups.uearv.entidades.MatParalelo;
import com.ups.uearv.entidades.MatPeriodo;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "oferta")
@ViewScoped
public class Oferta implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idOferta = "";
	String soPeriodo = "";
	String soCurso = "";
	String soParalelo = "";
	String itDescripcion = "";
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> ofertaList = new ArrayList<Object>();

	ArrayList<SelectItem> listPeriodo = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listCurso = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listParalelo = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = listPeriodo.get(0).getValue().toString();

		listCurso = (ArrayList<SelectItem>) llenaComboCursos();
		soPeriodo = listCurso.get(0).getValue().toString();
				
		listParalelo = (ArrayList<SelectItem>) llenaComboParalelos();
		soParalelo = listParalelo.get(0).getValue().toString();

		onChangeCombos();
		
		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		ofertaList.clear();
		List<Object> l = DAO.nqObject(new MatOferta(), jpql);

		for (Object in : l)
			ofertaList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = "SELECT o.* " +
				   "FROM mat_oferta o " +
				   "	INNER JOIN mat_curso c ON c.id_curso = o.id_curso " +
				   "	INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " +
				   "WHERE o.descripcion LIKE '%" + itBuscar + "%' ORDER BY k.codigo_det, c.id_curso ";
			
		} else {
			jpql = "SELECT o.* " +
				   "FROM mat_oferta o " +
				   "	INNER JOIN mat_curso c ON c.id_curso = o.id_curso " +
				   "	INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " +
				   "WHERE o.descripcion LIKE '%" + itBuscar + "%' AND c.estado = 'AC' ORDER BY k.codigo_det, c.id_curso ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {

		// VALIDACIONES
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar la oferta";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soPeriodo.equals("NA")) {
			mensaje = "Debe seleccionar un período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}			
		if (soCurso.equals("NA")) {
			mensaje = "Debe seleccionar un curso";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}	
		if (soParalelo.equals("NA")) {
			mensaje = "Debe seleccionar un paralelo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}	
		
		if (accion == 0) {
			MatOferta ob = (MatOferta) DAO.buscarObject(new MatOferta(),
					"from MatOferta c where c.matPeriodo.idPeriodo = " + soPeriodo + " and c.matCurso.idCurso = "
							+ soCurso + " and c.matParalelo.idParalelo = " + soParalelo + " and c.estado = 'AC'");
			if (ob != null) {
				mensaje = "Ya existe una oferta con los datos seleccionados";
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

			MatOferta ob = new MatOferta();
			if (accion == 1) {
				ob = (MatOferta) DAO.buscarObject(new MatOferta(), "from MatOferta c where c.idOferta = '" + idOferta + "'");
			}
			
			MatPeriodo periodo = (MatPeriodo) DAO.buscarObject(new MatPeriodo(), "from MatPeriodo c where c.idPeriodo = " + soPeriodo );
			MatCurso curso = (MatCurso) DAO.buscarObject(new MatCurso(), "from MatCurso c where c.idCurso = " + soCurso);
			MatParalelo paraleo = (MatParalelo) DAO.buscarObject(new MatParalelo(), "from MatParalelo c where c.idParalelo = " + soParalelo);
			
			ob.setDescripcion(itDescripcion);
			ob.setMatPeriodo(periodo);
			ob.setMatCurso(curso);
			ob.setMatParalelo(paraleo);
					
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
			
	public void onChangeCombos() {
		itDescripcion = descripcionOferta();
	}
	
	public String descripcionOferta() {
		try {
			String periodo = ((MatPeriodo) DAO.buscarObject(new MatPeriodo(), "from MatPeriodo c where c.idPeriodo = " + soPeriodo )).getDescripcion();
			String curso = ((MatCurso) DAO.buscarObject(new MatCurso(), "from MatCurso c where c.idCurso = " + soCurso)).getDescripcion();
			String paraleo = ((MatParalelo) DAO.buscarObject(new MatParalelo(), "from MatParalelo c where c.idParalelo = " + soParalelo)).getDescripcion();
			
			return "[" + periodo + "] [" + curso + "] [" + paraleo + "]";
		} catch (Exception e) {
			return "";
		}
	}
	
	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboCursos() {
		return Util.llenaCombo(DAO.getCursos(), 2);
	}
	
	public List<SelectItem> llenaComboParalelos() {
		return Util.llenaCombo(DAO.getParalelos(), 2);
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
	public String getidOferta() {
		return idOferta;
	}
	public void setidOferta(String idOferta) {
		Oferta.idOferta = idOferta;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
	}
	public String getSoCurso() {
		return soCurso;
	}
	public void setSoCurso(String soCurso) {
		this.soCurso = soCurso;
	}
	public List<Object> getOfertaList() {
		return ofertaList;
	}
	public void setOfertaList(List<Object> ofertaList) {
		this.ofertaList = ofertaList;
	}
	public ArrayList<SelectItem> getListPeriodo() {
		return listPeriodo;
	}
	public void setListPeriodo(ArrayList<SelectItem> listPeriodo) {
		this.listPeriodo = listPeriodo;
	}
	public ArrayList<SelectItem> getListCurso() {
		return listCurso;
	}
	public void setListCurso(ArrayList<SelectItem> listCurso) {
		this.listCurso = listCurso;
	}
	public ArrayList<SelectItem> getListParalelo() {
		return listParalelo;
	}
	public void setListParalelo(ArrayList<SelectItem> listParalelo) {
		this.listParalelo = listParalelo;
	}
	public String getSoParalelo() {
		return soParalelo;
	}
	public void setSoParalelo(String soParalelo) {
		this.soParalelo = soParalelo;
	}
}