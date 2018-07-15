/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

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

			SegUsuario u = DAO.buscarSegUsuario("from SegUsuario c where c.idUsuario = '" + usuario + "'");

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
			if (pass != null) {
				if (password.equals(pass)) {
					HttpSession httpSession = Session.getSession();
					httpSession.setAttribute("username", usuario);

					return navegacion.redirectToInicio();
				} 
			}			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Usuario o contraseña incorrecta"));
			usuario = null;
			return navegacion.toLogin();

		} catch (Exception e) {
			e.printStackTrace();
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
	
	// GNS
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
