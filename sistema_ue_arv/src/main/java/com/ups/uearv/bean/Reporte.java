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
import javax.persistence.Query;

import com.ups.uearv.entidades.CalComportamiento;
import com.ups.uearv.entidades.CalEscala;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporte")
@ViewScoped
public class Reporte implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	
	
	private static final long serialVersionUID = 1L;

	String soPeriodoCal = "";
	String soOfertaCal = "";
	String soEstudianteCal = "";
	
	String soQuimestre = "";
	String soParcial = "";

	ArrayList<SelectItem> listPeriodoCal = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertaCal = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listEstudianteCal = new ArrayList<SelectItem>();
	
	// VARIABLES REPORTE LIBRETA CAL
	String olPeriodo = "";
	String olJornada = "";
	String olParcial = "";
	String olQuimestre = "";
	String olEstudiante = "";
	String olDocente = "";
	String olGrado = "";
	String olParalelo = "";
	private List<Object> libretaList = new ArrayList<Object>();
	String olComportamiento = "";
	String olPromedioFinal = "";
	String olPromedioEscala = "";
	String olAistencias = "";
	String olAtrasos = "";
	String olFaltas = "";
	String olFaltasJustif = "";
	String olProyectos = "";
	private List<Object> comportamientoList = new ArrayList<Object>();
	private List<Object> escalaList = new ArrayList<Object>();

	@PostConstruct
	public void init() {		
		listPeriodoCal = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodoCal = "NA";

		soQuimestre = "1";
		soParcial = "1";
		
		llenarListaComportamiento();
		llenarListaEscala();
		
		onChangePeriodoCal();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}

	public void onChangePeriodoCal() {
		listOfertaCal = (ArrayList<SelectItem>) llenaComboOferta();
		soOfertaCal = "NA";

		onChangeOfertaCal();
	}

	public void onChangeOfertaCal() {
		listEstudianteCal = (ArrayList<SelectItem>) llenaComboEstudiante();
		soEstudianteCal = "NA";
	}

	public void verLibretaCalifinaciones() {

	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodoCal), 2);
	}

	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getEstudiantesOferta(), 2);
	}
	
	public Query getEstudiantesOferta() {		
		String jpql = 
		"SELECT e.id_estudiante, CONCAT(IFNULL(e.apellidos, ''), ' ', IFNULL(e.nombres, '')) nombre \r\n" + 
		"FROM mat_estudiante e \r\n" + 
		"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
		"WHERE m.id_periodo = '" + soPeriodoCal + "' \r\n";		
		if (!soOfertaCal.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOfertaCal + "' \r\n"; }	
		jpql = jpql + "AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
		"ORDER BY 2";

		Query query = em.createNativeQuery(jpql);
		return query;
	}
	
	public void llenarListaComportamiento() {
		String jpql = " SELECT c.* FROM cal_comportamiento c WHERE c.estado = 'AC' ORDER BY c.id_comportamiento ";
		comportamientoList.clear();
		List<Object> l = DAO.nqObject(new CalComportamiento(), jpql);
				
		for (Object in : l)
			comportamientoList.add(in);
	}
	
	public void llenarListaEscala() {
		String jpql = " SELECT c.* FROM cal_escala c WHERE c.estado = 'AC' ORDER BY c.id_escala ";
		escalaList.clear();
		List<Object> l = DAO.nqObject(new CalEscala(), jpql);
				
		for (Object in : l)
			escalaList.add(in);
	}
	// SUBCLASES
	
	public class LibretaCal implements Serializable {

		private static final long serialVersionUID = 1L;

		public String area;
		public BigDecimal tarea;
		public BigDecimal actIndividual;
		public BigDecimal actGrupal;
		public BigDecimal leccion;
		public BigDecimal evaluacion;
		public BigDecimal promedio;
		public String escala;
		
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
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
	public String getSoPeriodoCal() {
		return soPeriodoCal;
	}
	public void setSoPeriodoCal(String soPeriodoCal) {
		this.soPeriodoCal = soPeriodoCal;
	}
	public String getSoOfertaCal() {
		return soOfertaCal;
	}
	public void setSoOfertaCal(String soOfertaCal) {
		this.soOfertaCal = soOfertaCal;
	}
	public String getSoEstudianteCal() {
		return soEstudianteCal;
	}
	public void setSoEstudianteCal(String soEstudianteCal) {
		this.soEstudianteCal = soEstudianteCal;
	}
	public ArrayList<SelectItem> getListPeriodoCal() {
		return listPeriodoCal;
	}
	public void setListPeriodoCal(ArrayList<SelectItem> listPeriodoCal) {
		this.listPeriodoCal = listPeriodoCal;
	}
	public ArrayList<SelectItem> getListOfertaCal() {
		return listOfertaCal;
	}
	public void setListOfertaCal(ArrayList<SelectItem> listOfertaCal) {
		this.listOfertaCal = listOfertaCal;
	}
	public ArrayList<SelectItem> getListEstudianteCal() {
		return listEstudianteCal;
	}
	public void setListEstudianteCal(ArrayList<SelectItem> listEstudianteCal) {
		this.listEstudianteCal = listEstudianteCal;
	}
	public String getSoQuimestre() {
		return soQuimestre;
	}
	public void setSoQuimestre(String soQuimestre) {
		this.soQuimestre = soQuimestre;
	}
	public String getSoParcial() {
		return soParcial;
	}
	public void setSoParcial(String soParcial) {
		this.soParcial = soParcial;
	}
	public String getOlPeriodo() {
		return olPeriodo;
	}
	public void setOlPeriodo(String olPeriodo) {
		this.olPeriodo = olPeriodo;
	}
	public String getOlJornada() {
		return olJornada;
	}
	public void setOlJornada(String olJornada) {
		this.olJornada = olJornada;
	}
	public String getOlParcial() {
		return olParcial;
	}
	public void setOlParcial(String olParcial) {
		this.olParcial = olParcial;
	}
	public String getOlQuimestre() {
		return olQuimestre;
	}
	public void setOlQuimestre(String olQuimestre) {
		this.olQuimestre = olQuimestre;
	}
	public String getOlEstudiante() {
		return olEstudiante;
	}
	public void setOlEstudiante(String olEstudiante) {
		this.olEstudiante = olEstudiante;
	}
	public String getOlDocente() {
		return olDocente;
	}
	public void setOlDocente(String olDocente) {
		this.olDocente = olDocente;
	}
	public String getOlGrado() {
		return olGrado;
	}
	public void setOlGrado(String olGrado) {
		this.olGrado = olGrado;
	}
	public String getOlParalelo() {
		return olParalelo;
	}
	public void setOlParalelo(String olParalelo) {
		this.olParalelo = olParalelo;
	}
	public List<Object> getLibretaList() {
		return libretaList;
	}
	public void setLibretaList(List<Object> libretaList) {
		this.libretaList = libretaList;
	}
	public String getOlComportamiento() {
		return olComportamiento;
	}
	public void setOlComportamiento(String olComportamiento) {
		this.olComportamiento = olComportamiento;
	}
	public String getOlPromedioFinal() {
		return olPromedioFinal;
	}
	public void setOlPromedioFinal(String olPromedioFinal) {
		this.olPromedioFinal = olPromedioFinal;
	}
	public String getOlPromedioEscala() {
		return olPromedioEscala;
	}
	public void setOlPromedioEscala(String olPromedioEscala) {
		this.olPromedioEscala = olPromedioEscala;
	}
	public String getOlAistencias() {
		return olAistencias;
	}
	public void setOlAistencias(String olAistencias) {
		this.olAistencias = olAistencias;
	}
	public String getOlAtrasos() {
		return olAtrasos;
	}
	public void setOlAtrasos(String olAtrasos) {
		this.olAtrasos = olAtrasos;
	}
	public String getOlFaltas() {
		return olFaltas;
	}
	public void setOlFaltas(String olFaltas) {
		this.olFaltas = olFaltas;
	}
	public String getOlFaltasJustif() {
		return olFaltasJustif;
	}
	public void setOlFaltasJustif(String olFaltasJustif) {
		this.olFaltasJustif = olFaltasJustif;
	}
	public String getOlProyectos() {
		return olProyectos;
	}
	public void setOlProyectos(String olProyectos) {
		this.olProyectos = olProyectos;
	}
	public List<Object> getComportamientoList() {
		return comportamientoList;
	}
	public void setComportamientoList(List<Object> comportamientoList) {
		this.comportamientoList = comportamientoList;
	}
	public List<Object> getEscalaList() {
		return escalaList;
	}
	public void setEscalaList(List<Object> escalaList) {
		this.escalaList = escalaList;
	}
}