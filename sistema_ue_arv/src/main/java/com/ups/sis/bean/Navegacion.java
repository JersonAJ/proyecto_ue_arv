package com.ups.sis.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Simple navigation bean
 * 
 * @author Jerson Armijos
 *
 */
@ManagedBean
@SessionScoped
public class Navegacion implements Serializable {

	private static final long serialVersionUID = 1520318172495977648L;

	public String redirectToLogin() {
		return "/login.xhtml?faces-redirect=true";
	}
	
	public String toLogin() {
		return "/login";
	}
	
	public String toBase() {
		return "/base";
	}

	public String redirectToInicio() {
		return "/inicio.xhtml?faces-redirect=true";
	}
}