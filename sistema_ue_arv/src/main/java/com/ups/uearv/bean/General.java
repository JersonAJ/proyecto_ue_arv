/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ups.uearv.servicios.DAO;
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
		validaMenuSistema();
	}
	
	// 
	public String validaSesion() {		 
	    if (Session.getUserName() == null) 
	        return "/errores/error_sesion.xhtml";	    
	    return null;
	}
	
	// VALIDAR MENU		
	boolean seg = false;
	boolean man = false;
	boolean mat = false;
	boolean ges = false;
	boolean cal = false;
	
	String displaySeguridad = "none";
	String displayPerfiles = "none";
	String displayUsuarios = "none";
	
	String displayMantenimientos = "none";
	String displayCatalogo = "none";
	String displayCursos = "none";
	String displayDocentes = "none";
	String displayAsignaturas = "none";
	String displayRepartos = "none";
	String displayEscalas = "none";
	String displayComportamientos = "none";
	String displayParalelos = "none";

	String displayMatriculacion = "none";
	String displayPeriodos = "none";
	String displayOfertas = "none";
	String displayRepreentantes = "none";
	String displayEstudiantes = "none";
	String displayMatriculas = "none";
	String displayMatReportes = "none";
	
	String displayGestion = "none";
	String displayDescuentos = "none";
	String displayPensiones = "none";
	String displayGesReportes = "none";
	
	String displayCalificaciones = "none";
	String displayControl = "none";
	String displayAsistencias = "none";
	String displayCalReportes = "none";
	
	public void validaMenuSistema() {
		List<Object> result = DAO.obtenerMenu(Session.getUserName());
		Iterator<Object> itr = result.iterator();
		if (result.size() != 0) {
			for (int k = 0; k < result.size(); k++) {
				Object obj = (Object) itr.next();

				int idMenu = Integer.parseInt(obj.toString());

				switch (idMenu) {
				case 2:
					displayPerfiles = "";
					seg = true;
					break;
				case 3:
					displayUsuarios = "";
					seg = true;
					break;
				case 5:
					displayCatalogo = "";
					man = true;
					break;
				case 6:
					displayCursos = "";
					man = true;
					break;
				case 7:
					displayDocentes = "";
					man = true;
					break;
				case 8:
					displayAsignaturas = "";
					man = true;
					break;
				case 9:
					displayRepartos = "";
					man = true;
					break;
				case 10:
					displayEscalas = "";
					man = true;
					break;
				case 11:
					displayComportamientos = "";
					man = true;
					break;
				case 12:
					displayParalelos = "";
					man = true;
					break;
				case 14:
					displayPeriodos = "";
					seg = true;
					break;
				case 15:
					displayOfertas = "";
					mat = true;
					break;
				case 16:
					displayRepreentantes = "";
					mat = true;
					break;
				case 17:
					displayEstudiantes = "";
					mat = true;
					break;
				case 18:
					displayMatriculas = "";
					mat = true;
					break;
				case 19:
					displayMatReportes = "";
					mat = true;
					break;
				case 21:
					displayDescuentos = "";
					ges = true;
					break;
				case 22:
					displayPensiones = "";
					ges = true;
					break;
				case 23:
					displayGesReportes = "";
					ges = true;
					break;
				case 25:
					displayControl = "";
					cal = true;
					break;
				case 26:
					displayAsistencias = "";
					cal = true;
					break;
				case 27:
					displayCalReportes = "";
					cal = true;
					break;
				}
			}
		if (seg) { displaySeguridad = ""; }
		if (man) { displayMantenimientos = ""; }
		if (mat) { displayMatriculacion = ""; }
		if (ges) { displayGestion = ""; }
		if (cal) { displayCalificaciones = ""; }		
		}
	}
	
	public String getDisplaySeguridad() {
		return displaySeguridad;
	}

	public void setDisplaySeguridad(String displaySeguridad) {
		this.displaySeguridad = displaySeguridad;
	}

	public String getDisplayPerfiles() {
		return displayPerfiles;
	}

	public void setDisplayPerfiles(String displayPerfiles) {
		this.displayPerfiles = displayPerfiles;
	}

	public String getDisplayUsuarios() {
		return displayUsuarios;
	}

	public void setDisplayUsuarios(String displayUsuarios) {
		this.displayUsuarios = displayUsuarios;
	}

	public String getDisplayMantenimientos() {
		return displayMantenimientos;
	}

	public void setDisplayMantenimientos(String displayMantenimientos) {
		this.displayMantenimientos = displayMantenimientos;
	}

	public String getDisplayCatalogo() {
		return displayCatalogo;
	}

	public void setDisplayCatalogo(String displayCatalogo) {
		this.displayCatalogo = displayCatalogo;
	}

	public String getDisplayCursos() {
		return displayCursos;
	}

	public void setDisplayCursos(String displayCursos) {
		this.displayCursos = displayCursos;
	}

	public String getDisplayDocentes() {
		return displayDocentes;
	}

	public void setDisplayDocentes(String displayDocentes) {
		this.displayDocentes = displayDocentes;
	}

	public String getDisplayAsignaturas() {
		return displayAsignaturas;
	}

	public void setDisplayAsignaturas(String displayAsignaturas) {
		this.displayAsignaturas = displayAsignaturas;
	}

	public String getDisplayRepartos() {
		return displayRepartos;
	}

	public void setDisplayRepartos(String displayRepartos) {
		this.displayRepartos = displayRepartos;
	}

	public String getDisplayEscalas() {
		return displayEscalas;
	}

	public void setDisplayEscalas(String displayEscalas) {
		this.displayEscalas = displayEscalas;
	}

	public String getDisplayComportamientos() {
		return displayComportamientos;
	}

	public void setDisplayComportamientos(String displayComportamientos) {
		this.displayComportamientos = displayComportamientos;
	}

	public String getDisplayParalelos() {
		return displayParalelos;
	}

	public void setDisplayParalelos(String displayParalelos) {
		this.displayParalelos = displayParalelos;
	}

	public String getDisplayMatriculacion() {
		return displayMatriculacion;
	}

	public void setDisplayMatriculacion(String displayMatriculacion) {
		this.displayMatriculacion = displayMatriculacion;
	}

	public String getDisplayPeriodos() {
		return displayPeriodos;
	}

	public void setDisplayPeriodos(String displayPeriodos) {
		this.displayPeriodos = displayPeriodos;
	}

	public String getDisplayOfertas() {
		return displayOfertas;
	}

	public void setDisplayOfertas(String displayOfertas) {
		this.displayOfertas = displayOfertas;
	}

	public String getDisplayRepreentantes() {
		return displayRepreentantes;
	}

	public void setDisplayRepreentantes(String displayRepreentantes) {
		this.displayRepreentantes = displayRepreentantes;
	}

	public String getDisplayEstudiantes() {
		return displayEstudiantes;
	}

	public void setDisplayEstudiantes(String displayEstudiantes) {
		this.displayEstudiantes = displayEstudiantes;
	}

	public String getDisplayMatriculas() {
		return displayMatriculas;
	}

	public void setDisplayMatriculas(String displayMatriculas) {
		this.displayMatriculas = displayMatriculas;
	}

	public String getDisplayMatReportes() {
		return displayMatReportes;
	}

	public void setDisplayMatReportes(String displayMatReportes) {
		this.displayMatReportes = displayMatReportes;
	}

	public String getDisplayGestion() {
		return displayGestion;
	}

	public void setDisplayGestion(String displayGestion) {
		this.displayGestion = displayGestion;
	}

	public String getDisplayDescuentos() {
		return displayDescuentos;
	}

	public void setDisplayDescuentos(String displayDescuentos) {
		this.displayDescuentos = displayDescuentos;
	}

	public String getDisplayPensiones() {
		return displayPensiones;
	}

	public void setDisplayPensiones(String displayPensiones) {
		this.displayPensiones = displayPensiones;
	}

	public String getDisplayGesReportes() {
		return displayGesReportes;
	}

	public void setDisplayGesReportes(String displayGesReportes) {
		this.displayGesReportes = displayGesReportes;
	}

	public String getDisplayCalificaciones() {
		return displayCalificaciones;
	}

	public void setDisplayCalificaciones(String displayCalificaciones) {
		this.displayCalificaciones = displayCalificaciones;
	}

	public String getDisplayControl() {
		return displayControl;
	}

	public void setDisplayControl(String displayControl) {
		this.displayControl = displayControl;
	}

	public String getDisplayAsistencias() {
		return displayAsistencias;
	}

	public void setDisplayAsistencias(String displayAsistencias) {
		this.displayAsistencias = displayAsistencias;
	}

	public String getDisplayCalReportes() {
		return displayCalReportes;
	}

	public void setDisplayCalReportes(String displayCalReportes) {
		this.displayCalReportes = displayCalReportes;
	}	
}