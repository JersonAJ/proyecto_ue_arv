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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporte")
@ViewScoped
public class Reporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	@PostConstruct
	public void init() {

		buscar();
	}

	// CONSULTA	
	public void buscar() {
		
	}

	public void closeDialogo() {
		init();
	}
}