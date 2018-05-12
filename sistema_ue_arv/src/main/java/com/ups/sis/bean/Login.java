/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.sis.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import com.ups.sis.servicios.Session;

/**
 * @author Jerson Armijos Jaén
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
			String pass = "admin";
			
			if (pass != null) {
				String valor = password;
				if (valor.equals(pass) && usuario.equals("admin")) {
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
