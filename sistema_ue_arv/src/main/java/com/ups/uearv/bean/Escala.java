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

import com.ups.uearv.entidades.CalEscala;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "escala")
@ViewScoped
public class Escala implements Serializable {

	private static final long serialVersionUID = 1L;

	static String idEscala = "";
	String itCualitativa = "";
	String itDescripcion = "";	
	boolean ckEstado = false;
	BigDecimal inCuantitativaDsd = new BigDecimal(0);
	BigDecimal inCuantitativaHst = new BigDecimal(0);

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> escalaList = new ArrayList<Object>();

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
		escalaList.clear();
		List<Object> l = DAO.nqObject(new CalEscala(), jpql);
				
		for (Object in : l)
			escalaList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM cal_escala c WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' ORDER BY c.id_escala ";
		} else {
			jpql = " SELECT c.* FROM cal_escala c WHERE c.descripcion LIKE '%"
					+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.id_escala ";
		}
		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {
		
		// VALIDACIONES
		if (itCualitativa.trim().equals("")) {
			mensaje = "Debe ingresar el valor cualitativo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}					
		if (itDescripcion.trim().equals("")) {
			mensaje = "Debe ingresar el nombre de la escala";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}			
			
		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			
			CalEscala ob = new CalEscala();
			if (accion == 1) {
				ob = (CalEscala) DAO.buscarObject(new CalEscala(), "from CalEscala c where c.idEscala = " + idEscala);
			}
			
			String estado = "IC";
			if (ckEstado) estado = "AC";

			ob.setDescripcion(itDescripcion);
			ob.setCualitativa(itCualitativa);
			ob.setCuantitativaDesde((inCuantitativaDsd == null ? BigDecimal.ZERO : inCuantitativaDsd));
			ob.setCuantitativaHasta((inCuantitativaHst == null ? BigDecimal.ZERO : inCuantitativaHst));
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
	public String getIdEscala() {
		return idEscala;
	}
	public void setIdEscala(String idEscala) {
		Escala.idEscala = idEscala;
	}	
		public String getItCualitativa() {
		return itCualitativa;
	}
	public void setItCualitativa(String itCualitativa) {
		this.itCualitativa = itCualitativa;
	}
	public BigDecimal getInCuantitativaDsd() {
		return inCuantitativaDsd;
	}
	public void setInCuantitativaDsd(BigDecimal inCuantitativaDsd) {
		this.inCuantitativaDsd = inCuantitativaDsd;
	}
	public BigDecimal getInCuantitativaHst() {
		return inCuantitativaHst;
	}
	public void setInCuantitativaHst(BigDecimal inCuantitativaHst) {
		this.inCuantitativaHst = inCuantitativaHst;
	}
	public String getItDescripcion() {
		return itDescripcion;
	}
	public void setItDescripcion(String itDescripcion) {
		this.itDescripcion = itDescripcion;
	}
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
	public List<Object> getEscalaList() {
		return escalaList;
	}
	public void setEscalaList(List<Object> escalaList) {
		this.escalaList = escalaList;
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
}