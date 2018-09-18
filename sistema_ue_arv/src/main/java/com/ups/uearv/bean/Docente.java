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

import com.ups.uearv.entidades.CatalogoDet;
import com.ups.uearv.entidades.MatDocente;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "docente")
@ViewScoped
public class Docente implements Serializable {

	private static final long serialVersionUID = 1L;

	static String itCedula = "";
	String itNombres = "";
	String itApellidos = "";
	String itTelefono1 = "";
	String itTelefono2 = "";
	String itCorreo = "";
	String itTitulo = "";
	String itDireccion = "";
	String soEstadoCivil = "";
	String sorGenero = "";
	
	Date cFechaNac = new Date();
	
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> docenteList = new ArrayList<Object>();

	ArrayList<SelectItem> listEstadoCivil = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listEstadoCivil = (ArrayList<SelectItem>) llenaComboEstadoCivil();
		soEstadoCivil = listEstadoCivil.get(0).getValue().toString();

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		docenteList.clear();
		List<Object> l = DAO.nqObject(new MatDocente(), jpql);
				
		for (Object in : l)
			docenteList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_docente c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' ORDER BY c.apellidos ";
		} else {
			jpql = " SELECT c.* FROM mat_docente c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.apellidos ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itCedula.trim().equals("")) {
			mensaje = "Debe ingresar la cédula";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}				
		String msgValCedula = Util.validaCedula(itCedula);
		if (!msgValCedula.equals("OK")) {
			mensaje = msgValCedula;
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (accion == 0) {
			MatDocente ob =  (MatDocente) DAO.buscarObject(new MatDocente(), "from MatDocente c where c.idDocente = '" + itCedula + "'");
			if (ob != null) {
				mensaje = "El docente ya existe";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
				return;
			}
		}		
		if (itNombres.trim().equals("")) {
			mensaje = "Debe ingresar al menos un nombre";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (itApellidos.trim().equals("")) {
			mensaje = "Debe ingresar al menos un apellido";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		String msgValCorreo = Util.validarCorreo(itCorreo);
		if (!msgValCorreo.equals("OK")) {
			mensaje = msgValCorreo;
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			MatDocente ob = new MatDocente();
			if (accion == 1) {
				ob = (MatDocente) DAO.buscarObject(new MatDocente(), "from MatDocente c where c.idDocente = " + itCedula);
			} else {
				ob.setIdDocente(itCedula);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";
			
			ob.setApellidos(itApellidos);
			ob.setNombres(itNombres);
			ob.setEstadoCivil(soEstadoCivil);
			ob.setCorreo(itCorreo);
			ob.setDireccion(itDireccion);
			ob.setFechaNac(cFechaNac);
			ob.setGenero(sorGenero);
			ob.setTelefono1(itTelefono1);
			ob.setTelefono2(itTelefono2);
			ob.setTitulo(itTitulo);
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
	
	public List<SelectItem> llenaComboEstadoCivil() {
		return Util.llenaCombo(DAO.getDetCatalogo("CA005"), 2);
	}
	
	public String getEstadoCivil(String cod) {
		CatalogoDet ob = new CatalogoDet();
		ob = (CatalogoDet) DAO.buscarObject(new CatalogoDet(), "from CatalogoDet c where c.codigoDet = '" + cod + "'");

		return ob.getDescripcion().trim();
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
	public List<Object> getdocenteList() {
		return docenteList;
	}
	public void setdocenteList(List<Object> docenteList) {
		this.docenteList = docenteList;
	}
	public String getItNombres() {
		return itNombres;
	}
	public void setItNombres(String itNombres) {
		this.itNombres = itNombres;
	}
	public String getItApellidos() {
		return itApellidos;
	}
	public void setItApellidos(String itApellidos) {
		this.itApellidos = itApellidos;
	}
	public String getSoEstadoCivil() {
		return soEstadoCivil;
	}
	public void setSoEstadoCivil(String soEstadoCivil) {
		this.soEstadoCivil = soEstadoCivil;
	}
	public List<Object> getDocenteList() {
		return docenteList;
	}
	public void setDocenteList(List<Object> docenteList) {
		this.docenteList = docenteList;
	}
	public ArrayList<SelectItem> getListEstadoCivil() {
		return listEstadoCivil;
	}
	public void setListEstadoCivil(ArrayList<SelectItem> listEstadoCivil) {
		this.listEstadoCivil = listEstadoCivil;
	}
	public String getItCedula() {
		return itCedula;
	}
	public void setItCedula(String itCedula) {
		Docente.itCedula = itCedula;
	}
	public String getItTelefono1() {
		return itTelefono1;
	}
	public void setItTelefono1(String itTelefono1) {
		this.itTelefono1 = itTelefono1;
	}
	public String getItTelefono2() {
		return itTelefono2;
	}
	public void setItTelefono2(String itTelefono2) {
		this.itTelefono2 = itTelefono2;
	}
	public String getItCorreo() {
		return itCorreo;
	}
	public void setItCorreo(String itCorreo) {
		this.itCorreo = itCorreo;
	}
	public String getItTitulo() {
		return itTitulo;
	}
	public void setItTitulo(String itTitulo) {
		this.itTitulo = itTitulo;
	}
	public String getItDireccion() {
		return itDireccion;
	}
	public void setItDireccion(String itDireccion) {
		this.itDireccion = itDireccion;
	}
	public String getSorGenero() {
		return sorGenero;
	}
	public void setSorGenero(String sorGenero) {
		this.sorGenero = sorGenero;
	}
	public Date getcFechaNac() {
		return cFechaNac;
	}
	public void setcFechaNac(Date cFechaNac) {
		this.cFechaNac = cFechaNac;
	}
}