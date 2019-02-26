/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
import javax.persistence.Query;

import com.ups.uearv.entidades.CalComportamiento;
import com.ups.uearv.entidades.CalEscala;
import com.ups.uearv.entidades.MatDocente;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Ja�n - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporteCal")
@ViewScoped
public class ReporteCal implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	private static final long serialVersionUID = 1L;
	
	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";
	String jpql = "";

	String soPeriodoCal = "";
	String soOfertaCal = "";
	String soEstudianteCal = "";
	String soDocenteCal = "";

	String soQuimestre = "";
	String soParcial = "";

	ArrayList<SelectItem> listPeriodoCal = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertaCal = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listEstudianteCal = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listDocenteCal = new ArrayList<SelectItem>();

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
	
	// VARIABLES INFORME FINAL
	private List<Object> informeFinalList = new ArrayList<Object>();

	@PostConstruct
	public void init() {
		listPeriodoCal = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodoCal = "NA";
		onChangePeriodoCal();

		listDocenteCal = (ArrayList<SelectItem>) llenaComboDocente();
		soDocenteCal = "NA";
		onChangeDocenteCal();

		soQuimestre = "1";
		soParcial = "1";

		llenarListaComportamiento();
		llenarListaEscala();
	}
	
	// VARIABLES GENERALES
	private List<Object> comportamientoList = new ArrayList<Object>();
	private List<Object> escalaList = new ArrayList<Object>();

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

	public void onChangeDocenteCal() {
		if (!soDocenteCal.equals("NA")) {
			MatDocente docente = (MatDocente) DAO.buscarObject(new MatDocente(), "from MatDocente c where c.idDocente = '" + soDocenteCal + "'");
			olDocente = docente.getApellidos().trim() + " " + docente.getNombres();
		} else {
			olDocente = "____________________________________________________";
		}
	}

	@SuppressWarnings("unchecked")
	public void verLibretaCalifinaciones() {	
		limpiarLibreta();
		
		if (soPeriodoCal.equals("NA")) {
			mensaje = "Debe seleccionarel per�odo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soOfertaCal.equals("NA")) {
			mensaje = "Debe seleccionar una oferta";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soEstudianteCal.equals("NA")) {
			mensaje = "Debe seleccionar el estudiante";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		
		if (soParcial.equals("1")) { olParcial = "PRIMER PARCIAL"; }
		if (soParcial.equals("2")) { olParcial = "SEGUNDO PARCIAL"; }
		if (soParcial.equals("3")) { olParcial = "TERCER PARCIAL"; }					
		if (soQuimestre.equals("1")) { olQuimestre = "PRIMER QUIMESTRE"; }
		if (soQuimestre.equals("2")) { olQuimestre = "SEGUNDO QUIMESTRE"; }

		// DETALLE
		jpql = "CALL consulta_libreta_det (" + soPeriodoCal + "," + soOfertaCal + ",'" + soEstudianteCal + "'," + soQuimestre + "," + soParcial + ")";
		List<Object> result1 = em.createNativeQuery(jpql).getResultList();
		if (!result1.isEmpty()) {
			Iterator<Object> itr1 = result1.iterator();
			for (int k = 0; k < result1.size(); k++) {
				Object[] obj = (Object[]) itr1.next();					
				LibretaCal e = new LibretaCal();
				e.setIdAsignatura(obj[0].toString());
				e.setAsignatura(obj[1].toString());
				e.setTarea(new BigDecimal(obj[2].toString()));
				e.setActIndividual(new BigDecimal(obj[3].toString()));
				e.setActGrupal(new BigDecimal(obj[4].toString()));
				e.setLeccion(new BigDecimal(obj[5].toString()));
				e.setEvaluacion(new BigDecimal(obj[6].toString()));
				e.setPromedio(new BigDecimal(obj[7].toString()));
				e.setEscala(obj[8].toString());
				libretaList.add(e);
				
				olPromedioFinal = obj[9].toString();
				olPromedioEscala = obj[10].toString();
			}
		} else {
			limpiarLibreta();
			mensaje = "Debe completar los datos del control acad�mico";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, mensajeTitulo, mensaje));
			return;
		}

		// CABECERA
		jpql = "CALL consulta_libreta_cab (" + soPeriodoCal + "," + soOfertaCal + ",'" + soEstudianteCal + "'," + soQuimestre + "," + soParcial + ")";
		List<Object> result2 = em.createNativeQuery(jpql).getResultList();
		if (!result2.isEmpty()) {
			Iterator<Object> itr2 = result2.iterator();
			Object[] obj = (Object[]) itr2.next();
			olEstudiante = obj[0].toString();
			olGrado = obj[1].toString();
			olAistencias = obj[2].toString();
			olAtrasos = obj[3].toString();
			olFaltas = obj[4].toString();
			olFaltasJustif = obj[5].toString();
			olComportamiento = obj[6].toString();
			olProyectos = obj[7].toString();
			olParalelo = obj[8].toString();	
			olJornada = obj[9].toString();
			olPeriodo = obj[10].toString();
		} else {
			limpiarLibreta();
			mensaje = "Debe completar los datos del control parcial";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, mensajeTitulo, mensaje));
			return;
		}
	}
	
	public void limpiarLibreta() {
		libretaList.clear();
		olPromedioFinal = "";
		olPromedioEscala = "";
		olEstudiante = "";
		olGrado = "";
		olAistencias = "";
		olAtrasos = "";
		olFaltas = "";
		olFaltasJustif = "";
		olComportamiento = "";
		olProyectos = "";
		olParalelo = "";
	}

	@SuppressWarnings("unchecked")
	public void verInformeFinal() {	
		limpiarInformeFinal();
		
		if (soPeriodoCal.equals("NA")) {
			mensaje = "Debe seleccionarel per�odo";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soOfertaCal.equals("NA")) {
			mensaje = "Debe seleccionar una oferta";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soEstudianteCal.equals("NA")) {
			mensaje = "Debe seleccionar el estudiante";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}

		// DETALLE
		jpql = "CALL consulta_informe_final_det (" + soPeriodoCal + "," + soOfertaCal + ",'" + soEstudianteCal + "')";
		List<Object> result1 = em.createNativeQuery(jpql).getResultList();
		if (!result1.isEmpty()) {
			Iterator<Object> itr1 = result1.iterator();
			for (int k = 0; k < result1.size(); k++) {
				Object[] obj = (Object[]) itr1.next();					
				InformeFinal e = new InformeFinal();
				e.setIdAsignatura(obj[0].toString());
				e.setAsignatura(obj[1].toString());
				
				informeFinalList.add(e);
			}
		} 
	}
	
	public void limpiarInformeFinal() {
		informeFinalList.clear();
	}
	
	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodosRep(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodoCal), 2);
	}

	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getEstudiantesOferta(), 2);
	}

	public List<SelectItem> llenaComboDocente() {
		return Util.llenaCombo(DAO.getDocentes(), 2);
	}

	public Query getEstudiantesOferta() {		
		String jpql = 
				"SELECT e.id_estudiante, CONCAT(IFNULL(e.apellidos, ''), ' ', IFNULL(e.nombres, '')) nombre \r\n" + 
				"FROM mat_estudiante e \r\n" + 
				"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
				"WHERE m.id_periodo = '" + soPeriodoCal + "' \r\n"+
				"AND m.id_oferta = '" + soOfertaCal + "' \r\n"+
				"AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
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

	public class InformeFinal implements Serializable {

		private static final long serialVersionUID = 1L;

		public String idAsignatura;
		public String asignatura;
		
		public BigDecimal q1p1;
		public BigDecimal q1p2;
		public BigDecimal q1p3;
		public BigDecimal q180;
		public BigDecimal q1ex;
		public BigDecimal q120;
		public BigDecimal q1pr;
		public BigDecimal q1Escala;
		
		public BigDecimal q2p1;
		public BigDecimal q2p2;
		public BigDecimal q2p3;
		public BigDecimal q280;
		public BigDecimal q2ex;
		public BigDecimal q220;
		public BigDecimal q2pr;
		public BigDecimal q2Escala;
		
		public BigDecimal notaq1;
		public BigDecimal notaq2;
		
		public BigDecimal promedio;
		public String escala;
		public BigDecimal promedioFinal;
		public String escalaFinal;
		
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
		public BigDecimal getQ1p1() {
			return q1p1;
		}
		public void setQ1p1(BigDecimal q1p1) {
			this.q1p1 = q1p1;
		}
		public BigDecimal getQ1p2() {
			return q1p2;
		}
		public void setQ1p2(BigDecimal q1p2) {
			this.q1p2 = q1p2;
		}
		public BigDecimal getQ1p3() {
			return q1p3;
		}
		public void setQ1p3(BigDecimal q1p3) {
			this.q1p3 = q1p3;
		}
		public BigDecimal getQ180() {
			return q180;
		}
		public void setQ180(BigDecimal q180) {
			this.q180 = q180;
		}
		public BigDecimal getQ1ex() {
			return q1ex;
		}
		public void setQ1ex(BigDecimal q1ex) {
			this.q1ex = q1ex;
		}
		public BigDecimal getQ120() {
			return q120;
		}
		public void setQ120(BigDecimal q120) {
			this.q120 = q120;
		}
		public BigDecimal getQ1pr() {
			return q1pr;
		}
		public void setQ1pr(BigDecimal q1pr) {
			this.q1pr = q1pr;
		}
		public BigDecimal getQ1Escala() {
			return q1Escala;
		}
		public void setQ1Escala(BigDecimal q1Escala) {
			this.q1Escala = q1Escala;
		}
		public BigDecimal getQ2p1() {
			return q2p1;
		}
		public void setQ2p1(BigDecimal q2p1) {
			this.q2p1 = q2p1;
		}
		public BigDecimal getQ2p2() {
			return q2p2;
		}
		public void setQ2p2(BigDecimal q2p2) {
			this.q2p2 = q2p2;
		}
		public BigDecimal getQ2p3() {
			return q2p3;
		}
		public void setQ2p3(BigDecimal q2p3) {
			this.q2p3 = q2p3;
		}
		public BigDecimal getQ280() {
			return q280;
		}
		public void setQ280(BigDecimal q280) {
			this.q280 = q280;
		}
		public BigDecimal getQ2ex() {
			return q2ex;
		}
		public void setQ2ex(BigDecimal q2ex) {
			this.q2ex = q2ex;
		}
		public BigDecimal getQ220() {
			return q220;
		}
		public void setQ220(BigDecimal q220) {
			this.q220 = q220;
		}
		public BigDecimal getQ2pr() {
			return q2pr;
		}
		public void setQ2pr(BigDecimal q2pr) {
			this.q2pr = q2pr;
		}
		public BigDecimal getQ2Escala() {
			return q2Escala;
		}
		public void setQ2Escala(BigDecimal q2Escala) {
			this.q2Escala = q2Escala;
		}
		public BigDecimal getNotaq1() {
			return notaq1;
		}
		public void setNotaq1(BigDecimal notaq1) {
			this.notaq1 = notaq1;
		}
		public BigDecimal getNotaq2() {
			return notaq2;
		}
		public void setNotaq2(BigDecimal notaq2) {
			this.notaq2 = notaq2;
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
		public BigDecimal getPromedioFinal() {
			return promedioFinal;
		}
		public void setPromedioFinal(BigDecimal promedioFinal) {
			this.promedioFinal = promedioFinal;
		}
		public String getEscalaFinal() {
			return escalaFinal;
		}
		public void setEscalaFinal(String escalaFinal) {
			this.escalaFinal = escalaFinal;
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
	public String getSoDocenteCal() {
		return soDocenteCal;
	}
	public void setSoDocenteCal(String soDocenteCal) {
		this.soDocenteCal = soDocenteCal;
	}
	public ArrayList<SelectItem> getListDocenteCal() {
		return listDocenteCal;
	}
	public void setListDocenteCal(ArrayList<SelectItem> listDocenteCal) {
		this.listDocenteCal = listDocenteCal;
	}
	public List<Object> getInformeFinalList() {
		return informeFinalList;
	}
	public void setInformeFinalList(List<Object> informeFinalList) {
		this.informeFinalList = informeFinalList;
	}
}