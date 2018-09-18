/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

	String itUsuario = "";
	String soPerfil = "";
	String itNombres = "";
	String itApellidos = "";
	String olClave = "";
	int inIntentos = 0;
	boolean ckBloqueado = false;
	boolean ckEstado = false;
	boolean ckNuevo = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> usuarioList = new ArrayList<Object>();

	ArrayList<SelectItem> listPerfiles = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	@PostConstruct
	public void init() {

		listPerfiles = (ArrayList<SelectItem>) llenaComboPerfiles();
		soPerfil = listPerfiles.get(0).getValue().toString();

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		usuarioList.clear();
		List<Object> l = DAO.nqObject(new SegUsuario(), jpql);
		for (Object in : l)
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

	public String nuevaClave() {
		DateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String yy = sdf.format(Calendar.getInstance().getTime());
		olClave = "Usuario" + yy;
		return olClave;
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (accion == 0) {
			SegUsuario ob =  (SegUsuario) DAO.buscarObject(new SegUsuario(), "from SegUsuario c where c.idUsuario = '" + itUsuario + "'");
			if (ob != null) {
				mensaje = "El usuario ya existe";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
				return;
			}
		}				
		if (itUsuario.trim().equals("")) {
			mensaje = "Debe ingresar el usuario";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
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
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {			
			SegUsuario ob = new SegUsuario();
			if (accion == 1) {
				ob = (SegUsuario) DAO.buscarObject(new SegUsuario(), "from SegUsuario c where c.idUsuario = '" + itUsuario + "'");
			} else {				
				ob.setIdUsuario(itUsuario);
				ob.setClave(Util.generaSHA256(olClave));
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			String bloqueado = "N";
			if (ckBloqueado) bloqueado = "S";

			String nuevo = "N";
			if (ckNuevo) nuevo = "S";

			ob.setNombres(itNombres);
			ob.setApellidos(itApellidos);
			ob.setIdPerfil(Integer.parseInt(soPerfil));
			ob.setIntentos(inIntentos);
			ob.setSnBloqueado(bloqueado);
			ob.setEstado(estado);
			ob.setSnNuevo(nuevo);

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

	public void resetearClave(String usuario) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			accion = 1;
			SegUsuario ob = (SegUsuario) DAO.buscarObject(new SegUsuario(), "from SegUsuario c where c.idUsuario = '" + usuario + "'");
			ob.setClave(Util.generaSHA256(nuevaClave()));
			ob.setSnNuevo("S");
			if (DAO.saveOrUpdate(ob, accion, em)) {
				em.getTransaction().commit();
				mensaje = "Se reseteo la clave correctamente";
				FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
				buscar();
			} else {
				em.getTransaction().rollback();
				mensaje = "Error al resetear clave";
				FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
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
		perfil = (SegPerfil) DAO.buscarObject(new SegPerfil(), "from SegPerfil c where c.idPerfil = " + cod);

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
	public String getItUsuario() {
		return itUsuario;
	}
	public void setItUsuario(String itUsuario) {
		this.itUsuario = itUsuario;
	}
	public String getSoPerfil() {
		return soPerfil;
	}
	public void setSoPerfil(String soPerfil) {
		this.soPerfil = soPerfil;
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
	public String getOlClave() {
		return olClave;
	}
	public void setOlClave(String olClave) {
		this.olClave = olClave;
	}
	public int getInIntentos() {
		return inIntentos;
	}
	public void setInIntentos(int inIntentos) {
		this.inIntentos = inIntentos;
	}
	public boolean isCkBloqueado() {
		return ckBloqueado;
	}
	public void setCkBloqueado(boolean ckBloqueado) {
		this.ckBloqueado = ckBloqueado;
	}
	public List<Object> getUsuarioList() {
		return usuarioList;
	}
	public void setUsuarioList(List<Object> usuarioList) {
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