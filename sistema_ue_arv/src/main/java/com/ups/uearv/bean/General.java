/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ups.uearv.servicios.Session;


/**
 * @author Jerson Armijos Jaén
 * @version 1.0
 */

@ManagedBean(name = "general")
@ViewScoped
public class General implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {	
		
	}
	
	public String validaSesion() {		 
	    if (Session.getUserName() == null) 
	        return "/errores/error_sesion.xhtml";	    
	    return null;
	}
}
