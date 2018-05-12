/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.sis.bean;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 * @author Jerson Armijos Jaén
 * @version 1.0
 */

@ManagedBean(name = "inicio")
@ViewScoped
public class Inicio implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {	
		
	}
}
