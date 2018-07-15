/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.ups.uearv.entidades.SegPerfil;
import com.ups.uearv.entidades.SegUsuario;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;


/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "usuario")
@ViewScoped
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	String idUsuario = "";
	String idPerfil = "";
	String nombres = "";
	String apellidos = "";
	String clave = "";
	int intentos = 0;
	boolean ckBloqueado = false;
	boolean ckEstado = false;
	boolean ckNuevo = false;
	
	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<SegUsuario> usuarioList = new ArrayList<SegUsuario>();

	ArrayList<SelectItem> listPerfiles = new ArrayList<SelectItem>();
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";


	@PostConstruct
	public void init() {	
		
		listPerfiles = (ArrayList<SelectItem>) llenaComboPerfiles();
		idPerfil = listPerfiles.get(0).getValue().toString();
		
		buscar();	
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		usuarioList.clear();
		List<SegUsuario> l = DAO.nqSegUsuario(jpql); 
		for (SegUsuario in : l) 
			usuarioList.add(in);		
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = "SELECT * FROM seg_usuario WHERE nombres LIKE '%" + itBuscar + "%' OR apellidos LIKE '%" + itBuscar
					+ "%' ORDER BY nombres, apellidos";
		} else {
			jpql = "SELECT * FROM seg_usuario WHERE (nombres LIKE '%" + itBuscar + "%' OR apellidos LIKE '%" + itBuscar
					+ "%') AND estado = 'AC' ORDER BY nombres, apellidos";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String estado = "IC";
			if (ckEstado) { estado = "AC"; }
			
			String bloqueado = "N";
			if (ckBloqueado) { bloqueado = "S"; }
			
			String nuevo = "N";
			if (ckNuevo) { nuevo = "S"; }	

			SegUsuario ob = new SegUsuario();
			if (accion == 1) {
				ob = DAO.buscarSegUsuario("from SegUsuario c where c.idUsuario = '" + idUsuario + "'");
			}

			ob.setNombres(nombres);
			ob.setApellidos(apellidos);
			ob.setIdPerfil(Integer.parseInt(idPerfil));
			ob.setClave(clave);
			ob.setIntentos(intentos);
			ob.setSnBloqueado(bloqueado);			
			ob.setEstado(estado);
			ob.setSnNuevo(nuevo);
			
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
			e.printStackTrace();
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
	
	public boolean getBloqueado(String bloqueado) {
		boolean ban = true;
		if (bloqueado.equals("N"))
			ban = false;
		return ban;
	}
	
	public boolean getNuevo(String nuevo) {
		boolean ban = true;
		if (nuevo.equals("N"))
			ban = false;
		return ban;
	}
	
	public List<SelectItem> llenaComboPerfiles() {		
		return Util.llenaCombo(DAO.getPerfiles(), 2);
	}
	
	public String getPerfil(String cod) {
		SegPerfil perfil = new SegPerfil();
		perfil = DAO.buscarSegPerfil("from SegPerfil c where c.idPerfil = " + cod);
		
		return perfil.getDescripcion();
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

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public boolean isCkBloqueado() {
		return ckBloqueado;
	}

	public void setCkBloqueado(boolean ckBloqueado) {
		this.ckBloqueado = ckBloqueado;
	}

	public List<SegUsuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<SegUsuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	public ArrayList<SelectItem> getListPerfiles() {
		return listPerfiles;
	}

	public void setListPerfiles(ArrayList<SelectItem> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}

	public boolean isCkNuevo() {
		return ckNuevo;
	}

	public void setCkNuevo(boolean ckNuevo) {
		this.ckNuevo = ckNuevo;
	}
}