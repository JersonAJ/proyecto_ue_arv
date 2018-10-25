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

import com.ups.uearv.entidades.MatRepresentante;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "representante")
@ViewScoped
public class Representante implements Serializable {

	private static final long serialVersionUID = 1L;

	static String itCedula = "";
	String itNombres = "";
	String itApellidos = "";
	String itTelefono = "";
	String itOcupacion = "";
	String itCorreo = "";
	String itDireccion = "";	
	Date cFechaNac = new Date();
	
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> representanteList = new ArrayList<Object>();

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
		representanteList.clear();
		List<Object> l = DAO.nqObject(new MatRepresentante(), jpql);
				
		for (Object in : l)
			representanteList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_representante c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' ORDER BY c.apellidos ";
		} else {
			jpql = " SELECT c.* FROM mat_representante c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.apellidos ";
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
			MatRepresentante ob =  (MatRepresentante) DAO.buscarObject(new MatRepresentante(), "from MatRepresentante c where c.idRepresentante = '" + itCedula + "'");
			if (ob != null) {
				mensaje = "El representante ya existe";
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
			
			MatRepresentante ob = new MatRepresentante();
			if (accion == 1) {
				ob = (MatRepresentante) DAO.buscarObject(new MatRepresentante(), "from MatRepresentante c where c.idRepresentante = '" + itCedula + "'");
			} else {
				ob.setIdRepresentante(itCedula);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";
			
			ob.setApellidos(itApellidos);
			ob.setNombres(itNombres);
			ob.setCorreo(itCorreo);
			ob.setDireccion(itDireccion);
			ob.setOcupacion(itOcupacion);
			ob.setFechaNac(cFechaNac);
			ob.setTelefono(itTelefono);
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
		return Util.llenaCombo(DAO.getDetCatalogo("EC000"), 2);
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
	public List<Object> getRepresentanteList() {
		return representanteList;
	}
	public void setRepresentanteList(List<Object> representanteList) {
		this.representanteList = representanteList;
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
	public String getItCedula() {
		return itCedula;
	}
	public void setItCedula(String itCedula) {
		Representante.itCedula = itCedula;
	}
	public String getItCorreo() {
		return itCorreo;
	}
	public void setItCorreo(String itCorreo) {
		this.itCorreo = itCorreo;
	}
	public String getItDireccion() {
		return itDireccion;
	}
	public void setItDireccion(String itDireccion) {
		this.itDireccion = itDireccion;
	}
	public Date getcFechaNac() {
		return cFechaNac;
	}
	public void setcFechaNac(Date cFechaNac) {
		this.cFechaNac = cFechaNac;
	}
	public String getItTelefono() {
		return itTelefono;
	}
	public void setItTelefono(String itTelefono) {
		this.itTelefono = itTelefono;
	}
	public String getItOcupacion() {
		return itOcupacion;
	}
	public void setItOcupacion(String itOcupacion) {
		this.itOcupacion = itOcupacion;
	}
}