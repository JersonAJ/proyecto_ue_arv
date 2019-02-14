/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporteGes")
@ViewScoped
public class ReporteGes implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	private static final long serialVersionUID = 1L;

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";
	String jpql = "";

	String soPeriodo = "";

	String soOferta = "";
	String soEstudiante = "";

	ArrayList<SelectItem> listPeriodo = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOferta = new ArrayList<SelectItem>();		
			
	
	@PostConstruct
	public void init() {		
		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";
		onChangePeriodo();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}
	
	public void onChangePeriodo() {
		listOferta = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";
	}
	
	public void verEstadoCuentas() {
		
	}
	
	public void createBarEstadoCuentas() {
		
	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodosRep(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}
	

	// SUBCLASES
	public class LibretaCal implements Serializable {

		private static final long serialVersionUID = 1L;

		public String idAsignatura;
		public String asignatura;
		public BigDecimal tarea;
		public BigDecimal actIndividual;
		public BigDecimal actGrupal;
		public BigDecimal leccion;
		public BigDecimal evaluacion;
		public BigDecimal promedio;
		public String escala;


		public String getIdAsignatura() {
			return idAsignatura;
		}
		public void setIdAsignatura(String idAsignatura) {
			this.idAsignatura = idAsignatura;
		}
		public String getAsignatura() {
			return asignatura;
		}
		public void setAsignatura(String asignatura) {
			this.asignatura = asignatura;
		}
		public BigDecimal getTarea() {
			return tarea;
		}
		public void setTarea(BigDecimal tarea) {
			this.tarea = tarea;
		}
		public BigDecimal getActIndividual() {
			return actIndividual;
		}
		public void setActIndividual(BigDecimal actIndividual) {
			this.actIndividual = actIndividual;
		}
		public BigDecimal getActGrupal() {
			return actGrupal;
		}
		public void setActGrupal(BigDecimal actGrupal) {
			this.actGrupal = actGrupal;
		}
		public BigDecimal getLeccion() {
			return leccion;
		}
		public void setLeccion(BigDecimal leccion) {
			this.leccion = leccion;
		}
		public BigDecimal getEvaluacion() {
			return evaluacion;
		}
		public void setEvaluacion(BigDecimal evaluacion) {
			this.evaluacion = evaluacion;
		}
		public BigDecimal getPromedio() {
			return promedio;
		}
		public void setPromedio(BigDecimal promedio) {
			this.promedio = promedio;
		}
		public String getEscala() {
			return escala;
		}
		public void setEscala(String escala) {
			this.escala = escala;
		}
	}

	// GETTERS AND SETTERS
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
	}
	public String getSoOferta() {
		return soOferta;
	}
	public void setSoOferta(String soOferta) {
		this.soOferta = soOferta;
	}
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public ArrayList<SelectItem> getListPeriodo() {
		return listPeriodo;
	}
	public void setListPeriodo(ArrayList<SelectItem> listPeriodo) {
		this.listPeriodo = listPeriodo;
	}
	public ArrayList<SelectItem> getListOferta() {
		return listOferta;
	}
	public void setListOferta(ArrayList<SelectItem> listOferta) {
		this.listOferta = listOferta;
	}
}