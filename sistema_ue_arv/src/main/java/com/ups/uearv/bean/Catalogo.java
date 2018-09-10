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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.event.RowEditEvent;

import com.ups.uearv.entidades.CatalogoCab;
import com.ups.uearv.entidades.CatalogoDet;
import com.ups.uearv.servicios.DAO;


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
	
	String codigoCab = "";
	String descripcionDet = "";
	boolean ckEstadoDet = false;

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
	
	public void llenarListaDet(String jpql) {
		catalogoDetList.clear();
		List<CatalogoDet> l = DAO.nqCatalogoDet(jpql); 
		for (CatalogoDet in : l) 
			catalogoDetList.add(in);	
	}

	public void buscar() {		
		jpql = "SELECT * FROM catalogo_cab WHERE (grupo LIKE '%" + itBuscar + "%' OR descripcion LIKE '%" + itBuscar
				+ "%') AND estado = 'AC' ORDER BY grupo, descripcion";
		llenarLista(jpql);
	}

	public List<CatalogoDet> getCatalogoDetalle(String cab) {
		catalogoDetList.clear();
		
		if (ckMostrarIC) 
			jpql = "SELECT * FROM catalogo_det WHERE codigo_cab = '"+ cab + "' ORDER BY descripcion";
		else 		
			jpql = "SELECT * FROM catalogo_det WHERE codigo_cab = '"+ cab + "' and estado = 'AC' ORDER BY descripcion";
				
		llenarListaDet(jpql);
		return catalogoDetList;
	}
	
	public void buscarDet() {		
		if (ckMostrarIC) 
			jpql = "SELECT * FROM catalogo_det WHERE codigo_cab = '"+ codigoCab + "' ORDER BY descripcion";
		else 		
			jpql = "SELECT * FROM catalogo_det WHERE codigo_cab = '"+ codigoCab + "' and estado = 'AC' ORDER BY descripcion";
		
		llenarListaDet(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void closeDialogo() {
		init();
	}
		
	public void agregar() {
		CatalogoDet new_ob = new CatalogoDet();
		
		CatalogoCab cab = DAO.buscarCatalogoCab("from CatalogoCab c where c.codigoCab = '" + codigoCab + "'");
		new_ob.setCatalogoCab(cab);
		new_ob.setCodigoDet(generaCodigo());
		new_ob.setDescripcion(descripcionDet);
		new_ob.setEstado("AC");	
		
		accion = 0;
		guardar(new_ob);
		buscarDet();
	}

	public String generaCodigo() {
		int num = 1;
		String abv = "";
		String codigo = "";

		try {
			List<CatalogoDet> l = DAO.nqCatalogoDet("SELECT * FROM catalogo_det WHERE codigo_cab = '"+ codigoCab +"' ORDER BY 1 DESC LIMIT 1 ");
			for (CatalogoDet ca : l) 
				codigo = ca.getCodigoDet();
										
			num = Integer.parseInt(codigo.substring(2, 5)) + 1;
			abv = codigo.substring(0, 2);
		} catch (Exception e) {
			num = 1;
		}	
		
		String c = abv + String.format("%03d", num);		
		return c;
	}
	
	public void onRowEdit(RowEditEvent event) {
		CatalogoDet edt_ob = (CatalogoDet) event.getObject();
		accion = 1;
		edt_ob.setEstado((ckEstadoDet ? "AC" : "IC"));
		guardar(edt_ob);
		buscarDet();
	}

	public void onRowCancel(RowEditEvent event) {
	}
	
	public void guardar(CatalogoDet ob) {		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			if (DAO.saveOrUpdate(ob, accion, em)) {
				em.getTransaction().commit();								
			} else {
				em.getTransaction().rollback();				
			}		
		} catch (Exception e) {
			em.getTransaction().rollback();
		}	
		em.close();		
	}

	public boolean getEstadoDet(String valor) {
		boolean ban = false;
		ckEstadoDet = false;
		if(valor.equals("AC")) {
			ban = true;
			ckEstadoDet = true;
		}
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
	public String getDescripcionDet() {
		return descripcionDet;
	}
	public void setDescripcionDet(String descripcionDet) {
		this.descripcionDet = descripcionDet;
	}
	public String getCodigoCab() {
		return codigoCab;
	}
	public void setCodigoCab(String codigoCab) {
		this.codigoCab = codigoCab;
	}
	public boolean isCkEstadoDet() {
		return ckEstadoDet;
	}
	public void setCkEstadoDet(boolean ckEstadoDet) {
		this.ckEstadoDet = ckEstadoDet;
	}
}