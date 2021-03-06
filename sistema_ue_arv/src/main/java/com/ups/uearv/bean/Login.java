package com.ups.uearv.bean;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;

import com.ups.uearv.entidades.SegUsuario;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario;
	private String password;

	private boolean loggedIn;

	@ManagedProperty(value = "#{navegacion}")
	private Navegacion navegacion;

	public String ingresar() {
		try {
			if (usuario.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Debe ingresar el usuario"));
				return navegacion.toLogin();
			}
			if (password.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Debe ingresar la contraseņa"));
				return navegacion.toLogin();
			}

			SegUsuario u = (SegUsuario) DAO.buscarObject(new SegUsuario(),"from SegUsuario c where c.idUsuario = '" + usuario + "'");

			if (u == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "El usuario no existe"));
				usuario = null;
				return navegacion.toLogin();	
			}

			String pass = u.getClave();
			String bloq = u.getSnBloqueado();
			String esta = u.getEstado();

			if (esta.equals("IC")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "El usuario se encuentra inactivo"));
				usuario = null;
				return navegacion.toLogin();
			}
			if (bloq.equals("S")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "El usuario se encuentra bloqueado"));
				usuario = null;
				return navegacion.toLogin();
			}

			password = Util.generaSHA256(password);
			if (password.equals(pass)) {
				HttpSession httpSession = Session.getSession();
				httpSession.setAttribute("username", usuario);

				return navegacion.redirectToInicio();
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Contraseņa incorrecta"));
				return navegacion.toLogin();	
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Contacte al administrador"));
			usuario = null;
			return navegacion.toLogin();
		}
	}

	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		loggedIn = false;
		return navegacion.redirectToLogin();
	}

	public String getIniciales() {
		try {
			return usuario.substring(0, 2).toUpperCase();	
		} catch (Exception e) {
			return "";
		}	
	}

	public String getPerfil() {
		try {						
			return WordUtils.capitalizeFully(DAO.getPerfil(usuario));	
		} catch (Exception e) {
			return "";
		}	
	}

	public String getNombre() {
		try {						
			return WordUtils.capitalizeFully(DAO.getNombre(usuario));	
		} catch (Exception e) {
			return "";
		}	
	}

	// GETTERS AND SETTERS
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public Navegacion getNavegacion() {
		return navegacion;
	}
	public void setNavegacion(Navegacion navegacion) {
		this.navegacion = navegacion;
	}
}