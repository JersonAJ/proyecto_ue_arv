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

import com.ups.uearv.entidades.CatalogoCab;
import com.ups.uearv.entidades.CatalogoDet;
import com.ups.uearv.entidades.SegPerfil;
import com.ups.uearv.entidades.SegUsuario;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;


/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "catalogo")
@ViewScoped
public class Catalogo implements Serializable {

	private static final long serialVersionUID = 1L;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<CatalogoCab> catalogoCabList = new ArrayList<CatalogoCab>();
	private List<CatalogoDet> catalogoDetList = new ArrayList<CatalogoDet>();
	
	String descripcion = "";


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
		catalogoCabList.clear();
		List<CatalogoCab> l = DAO.nqCatalogoCab(jpql); 
		for (CatalogoCab in : l) 
			catalogoCabList.add(in);		
	}

	public void buscar() {		
		jpql = "SELECT * FROM catalogo_cab WHERE (grupo LIKE '%" + itBuscar + "%' OR descripcion LIKE '%" + itBuscar
				+ "%') AND estado = 'AC' ORDER BY grupo, descripcion";
		llenarLista(jpql);
	}
	
	
	public List<CatalogoDet> getCatalogoDetalle(String cab) {
		catalogoDetList.clear();
		jpql = "SELECT * FROM catalogo_det WHERE codigo_cab = '"+ cab + "' and estado = 'AC'";
		List<CatalogoDet> l = DAO.nqCatalogoDet(jpql); 
		for (CatalogoDet in : l) 
			catalogoDetList.add(in);	
		
		return catalogoDetList;
	}

	// INGRESO - ACTUALIZACION
	public void closeDialogo() {
		init();
	}

	public boolean getEstado(String estado) {
		boolean ban = true;
		if (estado.equals("IC"))
			ban = false;
		return ban;
	}
	
	// GETTERS AND SETTERS	
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
	public List<CatalogoCab> getCatalogoCabList() {
		return catalogoCabList;
	}
	public void setCatalogoCabList(List<CatalogoCab> catalogoCabList) {
		this.catalogoCabList = catalogoCabList;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<CatalogoDet> getCatalogoDetList() {
		return catalogoDetList;
	}
	public void setCatalogoDetList(List<CatalogoDet> catalogoDetList) {
		this.catalogoDetList = catalogoDetList;
	}
}