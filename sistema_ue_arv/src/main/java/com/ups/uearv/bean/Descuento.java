/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ups.uearv.entidades.GesDescuento;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "descuento")
@ViewScoped
public class Descuento implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idDescuento = "";
	String itNombre = "";
	String itDescripcion = "";	
	BigDecimal inPorcentaje = new BigDecimal(0);

	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> descuentoList = new ArrayList<Object>();

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
		descuentoList.clear();
		List<Object> l = DAO.nqObject(new GesDescuento(), jpql);

		for (Object in : l)
			descuentoList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM ges_descuento c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' ORDER BY c.descripcion ";
		} else {
			jpql = " SELECT c.* FROM ges_descuento c WHERE c.descripcion LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.descripcion ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {

		// VALIDACIONES
		if (itNombre.trim().equals("")) {
			mensaje = "Debe ingresar la abreviatura";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (inPorcentaje == BigDecimal.ZERO) {
			mensaje = "Debe ingresar el porcentaje a descontar";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());

			GesDescuento ob = new GesDescuento();
			if (accion == 1) {
				ob = (GesDescuento) DAO.buscarObject(new GesDescuento(), "from GesDescuento c where c.idDescuento = '" + idDescuento + "'");
			}

			ob.setNombre(itNombre);
			ob.setDescripcion(itDescripcion);
			ob.setPorcentaje(inPorcentaje);
	
			String estado = "IC";
			if (ckEstado) estado = "AC";	

			ob.setEstado(estado);
			if (accion == 0) {
				ob.setUsuarioIng(Session.getUserName());			
				ob.setFechaIng(fecha);				
			}			
			if (accion == 1) {
				ob.setUsuarioAct(Session.getUserName());			
				ob.setFechaAct(fecha);			
			}			

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

	public void closeDialogo() {
		init();
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
	public String getIdDescuento() {
		return idDescuento;
	}
	public void setIdDescuento(String idDescuento) {
		Descuento.idDescuento = idDescuento;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
	public BigDecimal getInPorcentaje() {
		return inPorcentaje;
	}
	public void setInPorcentaje(BigDecimal inPorcentaje) {
		this.inPorcentaje = inPorcentaje;
	}
	public List<Object> getDescuentoList() {
		return descuentoList;
	}
	public void setDescuentoList(List<Object> descuentoList) {
		this.descuentoList = descuentoList;
	}
	public String getItNombre() {
		return itNombre;
	}
	public void setItNombre(String itNombre) {
		this.itNombre = itNombre;
	}
}