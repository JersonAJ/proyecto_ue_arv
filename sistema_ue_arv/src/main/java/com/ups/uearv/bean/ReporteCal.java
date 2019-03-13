/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
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
 * @author Jerson Armijos Jaén - Raysa Solano
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

	String olDocente = "";
	// VARIABLES REPORTE LIBRETA CAL
	String olPeriodo = "";
	String olEstudiante = "";	
	String olJornada = "";
	String olParcial = "";
	String olQuimestre = "";
	String olGrado = "";
	String olParalelo = "";
	
	private List<Object> libretaList = new ArrayList<Object>();
	String olLibComportamiento = "";
	String olLibPromedioFinal = "";
	String olLibEscalaFinal = "";
	String olLibAistencias = "";
	String olLibAtrasos = "";
	String olLibFaltas = "";
	String olLibFaltasJustif = "";
	String olLibProyectos = "";	
	
	// VARIABLES INFORME FINAL
	String olInfPeriodo = "";
	String olInfEstudiante = "";
	String olFotoEst = "/sistema_ue_arv/fotos/FOTO_DEFAULT.png";
	String olDesAprovechamiento = "";
	String olDesComportamiento = "";
	String olInfComportamiento = "";
	String olSumaTotal = "";
	String olInfPromedioFinal = "";
	String olInfEscalaFinal = "";
	String olCurso = "";
		
	private List<Object> informeFinalList = new ArrayList<Object>();
	
	/*Q1*/
	String olQ1Comportamiento = "";
	String olQ1PromedioFinal = "";
	String olQ1EscalaFinal = "";
	
	String olQ1p1Comportamiento = "";
	String olQ1p2Comportamiento = "";
	String olQ1p3Comportamiento = "";
			
	String olQ1p1Aistencias = "";
	String olQ1p2Aistencias = "";
	String olQ1p3Aistencias = "";
	
	String olQ1p1Atrasos = "";
	String olQ1p2Atrasos = "";
	String olQ1p3Atrasos = "";
	
	String olQ1p1Faltas = "";
	String olQ1p2Faltas = "";
	String olQ1p3Faltas = "";
	
	String olQ1p1FaltasJustif = "";
	String olQ1p2FaltasJustif = "";
	String olQ1p3FaltasJustif = "";
	
	String olQ1p1Proyectos = "";	
	String olQ1p2Proyectos = "";
	String olQ1p3Proyectos = "";
	/*Q2*/
	String olQ2Comportamiento = "";
	String olQ2PromedioFinal = "";
	String olQ2EscalaFinal = "";
	
	String olQ2p1Comportamiento = "";
	String olQ2p2Comportamiento = "";
	String olQ2p3Comportamiento = "";
			
	String olQ2p1Aistencias = "";
	String olQ2p2Aistencias = "";
	String olQ2p3Aistencias = "";
	
	String olQ2p1Atrasos = "";
	String olQ2p2Atrasos = "";
	String olQ2p3Atrasos = "";
	
	String olQ2p1Faltas = "";
	String olQ2p2Faltas = "";
	String olQ2p3Faltas = "";
	
	String olQ2p1FaltasJustif = "";
	String olQ2p2FaltasJustif = "";
	String olQ2p3FaltasJustif = "";
	
	String olQ2p1Proyectos = "";	
	String olQ2p2Proyectos = "";
	String olQ2p3Proyectos = "";

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
			mensaje = "Debe seleccionarel período";
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
				e.setIdAsignatura(obj[0] == null ? "" : obj[0].toString());
				e.setAsignatura(obj[1] == null ? "" : obj[1].toString());
				e.setTarea(obj[2] == null ? "" : obj[2].toString());
				e.setActIndividual(obj[3] == null ? "" : obj[3].toString());
				e.setActGrupal(obj[4] == null ? "" : obj[4].toString());
				e.setLeccion(obj[5] == null ? "" : obj[5].toString());
				e.setEvaluacion(obj[6] == null ? "" : obj[6].toString());
				e.setPromedio(obj[7] == null ? "" : obj[7].toString());
				e.setEscala(obj[8] == null ? "" : obj[8].toString());
				libretaList.add(e);
				
				olLibPromedioFinal = obj[9] == null ? "" : obj[9].toString();
				olLibEscalaFinal = obj[10] == null ? "" : obj[10].toString();
			}
		} else {
			limpiarLibreta();
			mensaje = "Debe completar los datos del control académico";
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
			olLibAistencias = obj[2].toString();
			olLibAtrasos = obj[3].toString();
			olLibFaltas = obj[4].toString();
			olLibFaltasJustif = obj[5].toString();
			olLibComportamiento = obj[6].toString();
			olLibProyectos = obj[7].toString();
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
		olEstudiante = "";
		olGrado = "";
		olLibAistencias = "";
		olLibAtrasos = "";
		olLibFaltas = "";
		olLibFaltasJustif = "";
		olLibComportamiento = "";
		olLibProyectos = "";
		olParalelo = "";
		olJornada = "";
		olPeriodo = "";
		olLibPromedioFinal = "";
		olLibEscalaFinal = "";
	}

	@SuppressWarnings("unchecked")
	public void verInformeFinal() {	
		limpiarInformeFinal();
		
		if (soPeriodoCal.equals("NA")) {
			mensaje = "Debe seleccionarel período";
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
				
				e.setIdAsignatura(obj[0] == null ? "" : obj[0].toString());
				e.setAsignatura(obj[1] == null ? "" : obj[1].toString());
				
				e.setQ1p1(obj[2] == null ? "" : obj[2].toString());
				e.setQ1p2(obj[3] == null ? "" : obj[3].toString());
				e.setQ1p3(obj[4] == null ? "" : obj[4].toString());
				e.setQ1pp(obj[5] == null ? "" : obj[5].toString());				
				e.setQ180(obj[6] == null ? "" : obj[6].toString());
				e.setQ1ex(obj[7] == null ? "" : obj[7].toString());
				e.setQ120(obj[8] == null ? "" : obj[7].toString());
				e.setQ1pr(obj[9] == null ? "" : obj[8].toString());
				e.setQ1Escala(obj[10] == null ? "" : obj[10].toString());
				
				e.setQ2p1(obj[11] == null ? "" : obj[11].toString());
				e.setQ2p2(obj[12] == null ? "" : obj[12].toString());
				e.setQ2p3(obj[13] == null ? "" : obj[13].toString());
				e.setQ2pp(obj[14] == null ? "" : obj[14].toString());				
				e.setQ280(obj[15] == null ? "" : obj[15].toString());
				e.setQ2ex(obj[16] == null ? "" : obj[16].toString());
				e.setQ220(obj[17] == null ? "" : obj[17].toString());
				e.setQ2pr(obj[18] == null ? "" : obj[18].toString());
				e.setQ2Escala(obj[19] == null ? "" : obj[19].toString());
				
				e.setNotaq1(obj[20] == null ? "" : obj[20].toString());
				e.setNotaq2(obj[21] == null ? "" : obj[21].toString());
				
				e.setPromedio(obj[22] == null ? "" : obj[22].toString());
				e.setEscala(obj[23] == null ? "" : obj[23].toString());
				
				olQ1PromedioFinal = 	obj[24] == null ? "" : obj[24].toString();
				olQ1EscalaFinal = 		obj[25] == null ? "" : obj[25].toString();
				
				olQ2PromedioFinal =		obj[26] == null ? "" : obj[26].toString();
				olQ2EscalaFinal = 		obj[27] == null ? "" : obj[27].toString();
								
				olSumaTotal = 			obj[28] == null ? "" : obj[28].toString();
				olInfPromedioFinal = 	obj[29] == null ? "" : obj[29].toString();
				olInfEscalaFinal = 		obj[30] == null ? "" : obj[30].toString();
				olDesAprovechamiento = 	obj[31] == null ? "" : obj[31].toString();
				
				informeFinalList.add(e);
			}
			
			// CABECERA
			jpql = "CALL consulta_informe_final_cab (" + soPeriodoCal + "," + soOfertaCal + ",'" + soEstudianteCal + "')";
			List<Object> result2 = em.createNativeQuery(jpql).getResultList();
			if (!result2.isEmpty()) {
				Iterator<Object> itr2 = result2.iterator();
				Object[] obj = (Object[]) itr2.next();
				
				olInfEstudiante = 		obj[0] == null ? "" : obj[0].toString();
				olCurso = 				obj[1] == null ? "" : obj[1].toString();
				olInfPeriodo =			obj[2] == null ? "" : obj[2].toString();
				
				olQ1p1Aistencias = 		obj[3] == null ? "" : obj[3].toString();
				olQ1p1Atrasos = 		obj[4] == null ? "" : obj[4].toString();
				olQ1p1Faltas = 			obj[5] == null ? "" : obj[5].toString();
				olQ1p1FaltasJustif = 	obj[6] == null ? "" : obj[6].toString();
				olQ1p1Comportamiento = 	obj[7] == null ? "" : obj[7].toString();
				olQ1p1Proyectos = 		obj[8] == null ? "" : obj[8].toString();
				
				olQ1p2Aistencias = 		obj[9] == null ? "" : obj[9].toString();
				olQ1p2Atrasos = 		obj[10] == null ? "" : obj[10].toString();
				olQ1p2Faltas = 			obj[11] == null ? "" : obj[11].toString();
				olQ1p2FaltasJustif = 	obj[12] == null ? "" : obj[12].toString();
				olQ1p2Comportamiento = 	obj[13] == null ? "" : obj[13].toString();
				olQ1p2Proyectos = 		obj[14] == null ? "" : obj[14].toString();
				
				olQ1p3Aistencias = 		obj[15] == null ? "" : obj[15].toString();
				olQ1p3Atrasos = 		obj[16] == null ? "" : obj[16].toString();
				olQ1p3Faltas = 			obj[17] == null ? "" : obj[17].toString();
				olQ1p3FaltasJustif = 	obj[18] == null ? "" : obj[18].toString();
				olQ1p3Comportamiento = 	obj[19] == null ? "" : obj[19].toString();
				olQ1p3Proyectos = 		obj[20] == null ? "" : obj[20].toString();
				
				olQ2p1Aistencias = 		obj[21] == null ? "" : obj[21].toString();
				olQ2p1Atrasos = 		obj[22] == null ? "" : obj[22].toString();
				olQ2p1Faltas = 			obj[23] == null ? "" : obj[23].toString();
				olQ2p1FaltasJustif = 	obj[24] == null ? "" : obj[24].toString();
				olQ2p1Comportamiento = 	obj[25] == null ? "" : obj[25].toString();
				olQ2p1Proyectos = 		obj[26] == null ? "" : obj[26].toString();
				
				olQ2p2Aistencias = 		obj[27] == null ? "" : obj[27].toString();
				olQ2p2Atrasos = 		obj[28] == null ? "" : obj[28].toString();
				olQ2p2Faltas = 			obj[29] == null ? "" : obj[29].toString();
				olQ2p2FaltasJustif = 	obj[30] == null ? "" : obj[30].toString();
				olQ2p2Comportamiento = 	obj[31] == null ? "" : obj[31].toString();
				olQ2p2Proyectos = 		obj[32] == null ? "" : obj[32].toString();
				
				olQ2p3Aistencias = 		obj[33] == null ? "" : obj[33].toString();
				olQ2p3Atrasos = 		obj[34] == null ? "" : obj[34].toString();
				olQ2p3Faltas = 			obj[35] == null ? "" : obj[35].toString();
				olQ2p3FaltasJustif = 	obj[36] == null ? "" : obj[36].toString();
				olQ2p3Comportamiento = 	obj[37] == null ? "" : obj[37].toString();
				olQ2p3Proyectos = 		obj[38] == null ? "" : obj[38].toString();
				
				olQ1Comportamiento = 	obj[39] == null ? "" : obj[39].toString();
				olQ2Comportamiento = 	obj[40] == null ? "" : obj[40].toString();
				
				olInfComportamiento = 	obj[41] == null ? "" : obj[41].toString();
				olDesComportamiento = 	obj[42] == null ? "" : obj[42].toString();
				
				olFotoEst = 			obj[43] == null ? "" : obj[43].toString();
			} 
		} 
	}
	
	public void limpiarInformeFinal() {		
		informeFinalList.clear();		
		olInfPeriodo = "";
		olInfEstudiante = "";		
		olFotoEst = "/sistema_ue_arv/fotos/FOTO_DEFAULT.png";
		olDesAprovechamiento = "";
		olDesComportamiento = "";
		olInfComportamiento = "";
		olSumaTotal = "";
		olInfPromedioFinal = "";
		olInfEscalaFinal = "";
		olCurso = "";	
		
		/*Q1*/
		olQ1Comportamiento = "";
		olQ1PromedioFinal = "";
		olQ1EscalaFinal = "";
		
		olQ1p1Comportamiento = "";
		olQ1p2Comportamiento = "";
		olQ1p3Comportamiento = "";
				
		olQ1p1Aistencias = "";
		olQ1p2Aistencias = "";
		olQ1p3Aistencias = "";
		
		olQ1p1Atrasos = "";
		olQ1p2Atrasos = "";
		olQ1p3Atrasos = "";
		
		olQ1p1Faltas = "";
		olQ1p2Faltas = "";
		olQ1p3Faltas = "";
		
		olQ1p1FaltasJustif = "";
		olQ1p2FaltasJustif = "";
		olQ1p3FaltasJustif = "";
		
		olQ1p1Proyectos = "";	
		olQ1p2Proyectos = "";
		olQ1p3Proyectos = "";
		
		/*Q2*/
		olQ2Comportamiento = "";
		olQ2PromedioFinal = "";
		olQ2EscalaFinal = "";
		
		olQ2p1Comportamiento = "";
		olQ2p2Comportamiento = "";
		olQ2p3Comportamiento = "";
				
		olQ2p1Aistencias = "";
		olQ2p2Aistencias = "";
		olQ2p3Aistencias = "";
		
		olQ2p1Atrasos = "";
		olQ2p2Atrasos = "";
		olQ2p3Atrasos = "";
		
		olQ2p1Faltas = "";
		olQ2p2Faltas = "";
		olQ2p3Faltas = "";
		
		olQ2p1FaltasJustif = "";
		olQ2p2FaltasJustif = "";
		olQ2p3FaltasJustif = "";
		
		olQ2p1Proyectos = "";	
		olQ2p2Proyectos = "";
		olQ2p3Proyectos = "";

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
		public String tarea;
		public String actIndividual;
		public String actGrupal;
		public String leccion;
		public String evaluacion;
		public String promedio;
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
		public String getTarea() {
			return tarea;
		}
		public void setTarea(String tarea) {
			this.tarea = tarea;
		}
		public String getActIndividual() {
			return actIndividual;
		}
		public void setActIndividual(String actIndividual) {
			this.actIndividual = actIndividual;
		}
		public String getActGrupal() {
			return actGrupal;
		}
		public void setActGrupal(String actGrupal) {
			this.actGrupal = actGrupal;
		}
		public String getLeccion() {
			return leccion;
		}
		public void setLeccion(String leccion) {
			this.leccion = leccion;
		}
		public String getEvaluacion() {
			return evaluacion;
		}
		public void setEvaluacion(String evaluacion) {
			this.evaluacion = evaluacion;
		}
		public String getPromedio() {
			return promedio;
		}
		public void setPromedio(String promedio) {
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
		
		public String q1p1;
		public String q1p2;
		public String q1p3;
		public String q1pp;
		public String q180;
		public String q1ex;
		public String q120;
		public String q1pr;
		public String q1Escala;
		
		public String q2p1;
		public String q2p2;
		public String q2p3;
		public String q2pp;
		public String q280;
		public String q2ex;
		public String q220;
		public String q2pr;
		public String q2Escala;
		
		public String notaq1;
		public String notaq2;
		
		public String promedio;
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
		public String getQ1p1() {
			return q1p1;
		}
		public void setQ1p1(String q1p1) {
			this.q1p1 = q1p1;
		}
		public String getQ1p2() {
			return q1p2;
		}
		public void setQ1p2(String q1p2) {
			this.q1p2 = q1p2;
		}
		public String getQ1p3() {
			return q1p3;
		}
		public void setQ1p3(String q1p3) {
			this.q1p3 = q1p3;
		}
		public String getQ1pp() {
			return q1pp;
		}
		public void setQ1pp(String q1pp) {
			this.q1pp = q1pp;
		}
		public String getQ180() {
			return q180;
		}
		public void setQ180(String q180) {
			this.q180 = q180;
		}
		public String getQ1ex() {
			return q1ex;
		}
		public void setQ1ex(String q1ex) {
			this.q1ex = q1ex;
		}
		public String getQ120() {
			return q120;
		}
		public void setQ120(String q120) {
			this.q120 = q120;
		}
		public String getQ1pr() {
			return q1pr;
		}
		public void setQ1pr(String q1pr) {
			this.q1pr = q1pr;
		}
		public String getQ1Escala() {
			return q1Escala;
		}
		public void setQ1Escala(String q1Escala) {
			this.q1Escala = q1Escala;
		}
		public String getQ2p1() {
			return q2p1;
		}
		public void setQ2p1(String q2p1) {
			this.q2p1 = q2p1;
		}
		public String getQ2p2() {
			return q2p2;
		}
		public void setQ2p2(String q2p2) {
			this.q2p2 = q2p2;
		}
		public String getQ2p3() {
			return q2p3;
		}
		public void setQ2p3(String q2p3) {
			this.q2p3 = q2p3;
		}
		public String getQ2pp() {
			return q2pp;
		}
		public void setQ2pp(String q2pp) {
			this.q2pp = q2pp;
		}
		public String getQ280() {
			return q280;
		}
		public void setQ280(String q280) {
			this.q280 = q280;
		}
		public String getQ2ex() {
			return q2ex;
		}
		public void setQ2ex(String q2ex) {
			this.q2ex = q2ex;
		}
		public String getQ220() {
			return q220;
		}
		public void setQ220(String q220) {
			this.q220 = q220;
		}
		public String getQ2pr() {
			return q2pr;
		}
		public void setQ2pr(String q2pr) {
			this.q2pr = q2pr;
		}
		public String getQ2Escala() {
			return q2Escala;
		}
		public void setQ2Escala(String q2Escala) {
			this.q2Escala = q2Escala;
		}
		public String getNotaq1() {
			return notaq1;
		}
		public void setNotaq1(String notaq1) {
			this.notaq1 = notaq1;
		}
		public String getNotaq2() {
			return notaq2;
		}
		public void setNotaq2(String notaq2) {
			this.notaq2 = notaq2;
		}
		public String getPromedio() {
			return promedio;
		}
		public void setPromedio(String promedio) {
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
	public String getOlLibComportamiento() {
		return olLibComportamiento;
	}
	public void setOlLibComportamiento(String olLibComportamiento) {
		this.olLibComportamiento = olLibComportamiento;
	}
	public String getOlLibPromedioFinal() {
		return olLibPromedioFinal;
	}
	public void setOlLibPromedioFinal(String olLibPromedioFinal) {
		this.olLibPromedioFinal = olLibPromedioFinal;
	}
	public String getOlLibEscalaFinal() {
		return olLibEscalaFinal;
	}
	public void setOlLibEscalaFinal(String olLibEscalaFinal) {
		this.olLibEscalaFinal = olLibEscalaFinal;
	}
	public String getOlLibAistencias() {
		return olLibAistencias;
	}
	public void setOlLibAistencias(String olLibAistencias) {
		this.olLibAistencias = olLibAistencias;
	}
	public String getOlLibAtrasos() {
		return olLibAtrasos;
	}
	public void setOlLibAtrasos(String olLibAtrasos) {
		this.olLibAtrasos = olLibAtrasos;
	}
	public String getOlLibFaltas() {
		return olLibFaltas;
	}
	public void setOlLibFaltas(String olLibFaltas) {
		this.olLibFaltas = olLibFaltas;
	}
	public String getOlLibFaltasJustif() {
		return olLibFaltasJustif;
	}
	public void setOlLibFaltasJustif(String olLibFaltasJustif) {
		this.olLibFaltasJustif = olLibFaltasJustif;
	}
	public String getOlLibProyectos() {
		return olLibProyectos;
	}
	public void setOlLibProyectos(String olLibProyectos) {
		this.olLibProyectos = olLibProyectos;
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
	public String getOlQ1PromedioFinal() {
		return olQ1PromedioFinal;
	}
	public void setOlQ1PromedioFinal(String olQ1PromedioFinal) {
		this.olQ1PromedioFinal = olQ1PromedioFinal;
	}
	public String getOlQ1EscalaFinal() {
		return olQ1EscalaFinal;
	}
	public void setOlQ1EscalaFinal(String olQ1EscalaFinal) {
		this.olQ1EscalaFinal = olQ1EscalaFinal;
	}
	public String getOlQ1p1Comportamiento() {
		return olQ1p1Comportamiento;
	}
	public void setOlQ1p1Comportamiento(String olQ1p1Comportamiento) {
		this.olQ1p1Comportamiento = olQ1p1Comportamiento;
	}
	public String getOlQ1p2Comportamiento() {
		return olQ1p2Comportamiento;
	}
	public void setOlQ1p2Comportamiento(String olQ1p2Comportamiento) {
		this.olQ1p2Comportamiento = olQ1p2Comportamiento;
	}
	public String getOlQ1p3Comportamiento() {
		return olQ1p3Comportamiento;
	}
	public void setOlQ1p3Comportamiento(String olQ1p3Comportamiento) {
		this.olQ1p3Comportamiento = olQ1p3Comportamiento;
	}
	public String getOlQ1p1Aistencias() {
		return olQ1p1Aistencias;
	}
	public void setOlQ1p1Aistencias(String olQ1p1Aistencias) {
		this.olQ1p1Aistencias = olQ1p1Aistencias;
	}
	public String getOlQ1p2Aistencias() {
		return olQ1p2Aistencias;
	}
	public void setOlQ1p2Aistencias(String olQ1p2Aistencias) {
		this.olQ1p2Aistencias = olQ1p2Aistencias;
	}
	public String getOlQ1p3Aistencias() {
		return olQ1p3Aistencias;
	}
	public void setOlQ1p3Aistencias(String olQ1p3Aistencias) {
		this.olQ1p3Aistencias = olQ1p3Aistencias;
	}
	public String getOlQ1p1Atrasos() {
		return olQ1p1Atrasos;
	}
	public void setOlQ1p1Atrasos(String olQ1p1Atrasos) {
		this.olQ1p1Atrasos = olQ1p1Atrasos;
	}
	public String getOlQ1p2Atrasos() {
		return olQ1p2Atrasos;
	}
	public void setOlQ1p2Atrasos(String olQ1p2Atrasos) {
		this.olQ1p2Atrasos = olQ1p2Atrasos;
	}
	public String getOlQ1p3Atrasos() {
		return olQ1p3Atrasos;
	}
	public void setOlQ1p3Atrasos(String olQ1p3Atrasos) {
		this.olQ1p3Atrasos = olQ1p3Atrasos;
	}
	public String getOlQ1p1Faltas() {
		return olQ1p1Faltas;
	}
	public void setOlQ1p1Faltas(String olQ1p1Faltas) {
		this.olQ1p1Faltas = olQ1p1Faltas;
	}
	public String getOlQ1p2Faltas() {
		return olQ1p2Faltas;
	}
	public void setOlQ1p2Faltas(String olQ1p2Faltas) {
		this.olQ1p2Faltas = olQ1p2Faltas;
	}
	public String getOlQ1p3Faltas() {
		return olQ1p3Faltas;
	}
	public void setOlQ1p3Faltas(String olQ1p3Faltas) {
		this.olQ1p3Faltas = olQ1p3Faltas;
	}
	public String getOlQ1p1FaltasJustif() {
		return olQ1p1FaltasJustif;
	}
	public void setOlQ1p1FaltasJustif(String olQ1p1FaltasJustif) {
		this.olQ1p1FaltasJustif = olQ1p1FaltasJustif;
	}
	public String getOlQ1p2FaltasJustif() {
		return olQ1p2FaltasJustif;
	}
	public void setOlQ1p2FaltasJustif(String olQ1p2FaltasJustif) {
		this.olQ1p2FaltasJustif = olQ1p2FaltasJustif;
	}
	public String getOlQ1p3FaltasJustif() {
		return olQ1p3FaltasJustif;
	}
	public void setOlQ1p3FaltasJustif(String olQ1p3FaltasJustif) {
		this.olQ1p3FaltasJustif = olQ1p3FaltasJustif;
	}
	public String getOlQ1p1Proyectos() {
		return olQ1p1Proyectos;
	}
	public void setOlQ1p1Proyectos(String olQ1p1Proyectos) {
		this.olQ1p1Proyectos = olQ1p1Proyectos;
	}
	public String getOlQ1p2Proyectos() {
		return olQ1p2Proyectos;
	}
	public void setOlQ1p2Proyectos(String olQ1p2Proyectos) {
		this.olQ1p2Proyectos = olQ1p2Proyectos;
	}
	public String getOlQ1p3Proyectos() {
		return olQ1p3Proyectos;
	}
	public void setOlQ1p3Proyectos(String olQ1p3Proyectos) {
		this.olQ1p3Proyectos = olQ1p3Proyectos;
	}
	public String getOlQ2PromedioFinal() {
		return olQ2PromedioFinal;
	}
	public void setOlQ2PromedioFinal(String olQ2PromedioFinal) {
		this.olQ2PromedioFinal = olQ2PromedioFinal;
	}
	public String getOlQ2EscalaFinal() {
		return olQ2EscalaFinal;
	}
	public void setOlQ2EscalaFinal(String olQ2EscalaFinal) {
		this.olQ2EscalaFinal = olQ2EscalaFinal;
	}
	public String getOlQ2p1Comportamiento() {
		return olQ2p1Comportamiento;
	}
	public void setOlQ2p1Comportamiento(String olQ2p1Comportamiento) {
		this.olQ2p1Comportamiento = olQ2p1Comportamiento;
	}
	public String getOlQ2p2Comportamiento() {
		return olQ2p2Comportamiento;
	}
	public void setOlQ2p2Comportamiento(String olQ2p2Comportamiento) {
		this.olQ2p2Comportamiento = olQ2p2Comportamiento;
	}
	public String getOlQ2p3Comportamiento() {
		return olQ2p3Comportamiento;
	}
	public void setOlQ2p3Comportamiento(String olQ2p3Comportamiento) {
		this.olQ2p3Comportamiento = olQ2p3Comportamiento;
	}
	public String getOlQ2p1Aistencias() {
		return olQ2p1Aistencias;
	}
	public void setOlQ2p1Aistencias(String olQ2p1Aistencias) {
		this.olQ2p1Aistencias = olQ2p1Aistencias;
	}
	public String getOlQ2p2Aistencias() {
		return olQ2p2Aistencias;
	}
	public void setOlQ2p2Aistencias(String olQ2p2Aistencias) {
		this.olQ2p2Aistencias = olQ2p2Aistencias;
	}
	public String getOlQ2p3Aistencias() {
		return olQ2p3Aistencias;
	}
	public void setOlQ2p3Aistencias(String olQ2p3Aistencias) {
		this.olQ2p3Aistencias = olQ2p3Aistencias;
	}
	public String getOlQ2p1Atrasos() {
		return olQ2p1Atrasos;
	}
	public void setOlQ2p1Atrasos(String olQ2p1Atrasos) {
		this.olQ2p1Atrasos = olQ2p1Atrasos;
	}
	public String getOlQ2p2Atrasos() {
		return olQ2p2Atrasos;
	}
	public void setOlQ2p2Atrasos(String olQ2p2Atrasos) {
		this.olQ2p2Atrasos = olQ2p2Atrasos;
	}
	public String getOlQ2p3Atrasos() {
		return olQ2p3Atrasos;
	}
	public void setOlQ2p3Atrasos(String olQ2p3Atrasos) {
		this.olQ2p3Atrasos = olQ2p3Atrasos;
	}
	public String getOlQ2p1Faltas() {
		return olQ2p1Faltas;
	}
	public void setOlQ2p1Faltas(String olQ2p1Faltas) {
		this.olQ2p1Faltas = olQ2p1Faltas;
	}
	public String getOlQ2p2Faltas() {
		return olQ2p2Faltas;
	}
	public void setOlQ2p2Faltas(String olQ2p2Faltas) {
		this.olQ2p2Faltas = olQ2p2Faltas;
	}
	public String getOlQ2p3Faltas() {
		return olQ2p3Faltas;
	}
	public void setOlQ2p3Faltas(String olQ2p3Faltas) {
		this.olQ2p3Faltas = olQ2p3Faltas;
	}
	public String getOlQ2p1FaltasJustif() {
		return olQ2p1FaltasJustif;
	}
	public void setOlQ2p1FaltasJustif(String olQ2p1FaltasJustif) {
		this.olQ2p1FaltasJustif = olQ2p1FaltasJustif;
	}
	public String getOlQ2p2FaltasJustif() {
		return olQ2p2FaltasJustif;
	}
	public void setOlQ2p2FaltasJustif(String olQ2p2FaltasJustif) {
		this.olQ2p2FaltasJustif = olQ2p2FaltasJustif;
	}
	public String getOlQ2p3FaltasJustif() {
		return olQ2p3FaltasJustif;
	}
	public void setOlQ2p3FaltasJustif(String olQ2p3FaltasJustif) {
		this.olQ2p3FaltasJustif = olQ2p3FaltasJustif;
	}
	public String getOlQ2p1Proyectos() {
		return olQ2p1Proyectos;
	}
	public void setOlQ2p1Proyectos(String olQ2p1Proyectos) {
		this.olQ2p1Proyectos = olQ2p1Proyectos;
	}
	public String getOlQ2p2Proyectos() {
		return olQ2p2Proyectos;
	}
	public void setOlQ2p2Proyectos(String olQ2p2Proyectos) {
		this.olQ2p2Proyectos = olQ2p2Proyectos;
	}
	public String getOlQ2p3Proyectos() {
		return olQ2p3Proyectos;
	}
	public void setOlQ2p3Proyectos(String olQ2p3Proyectos) {
		this.olQ2p3Proyectos = olQ2p3Proyectos;
	}
	public String getOlSumaTotal() {
		return olSumaTotal;
	}
	public void setOlSumaTotal(String olSumaTotal) {
		this.olSumaTotal = olSumaTotal;
	}
	public String getOlInfPromedioFinal() {
		return olInfPromedioFinal;
	}
	public void setOlInfPromedioFinal(String olInfPromedioFinal) {
		this.olInfPromedioFinal = olInfPromedioFinal;
	}
	public String getOlInfEscalaFinal() {
		return olInfEscalaFinal;
	}
	public void setOlInfEscalaFinal(String olInfEscalaFinal) {
		this.olInfEscalaFinal = olInfEscalaFinal;
	}
	public String getOlFotoEst() {
		return olFotoEst;
	}
	public void setOlFotoEst(String olFotoEst) {
		this.olFotoEst = olFotoEst;
	}
	public String getOlDesAprovechamiento() {
		return olDesAprovechamiento;
	}
	public void setOlDesAprovechamiento(String olDesAprovechamiento) {
		this.olDesAprovechamiento = olDesAprovechamiento;
	}
	public String getOlDesComportamiento() {
		return olDesComportamiento;
	}
	public void setOlDesComportamiento(String olDesComportamiento) {
		this.olDesComportamiento = olDesComportamiento;
	}
	public String getOlInfComportamiento() {
		return olInfComportamiento;
	}
	public void setOlInfComportamiento(String olInfComportamiento) {
		this.olInfComportamiento = olInfComportamiento;
	}
	public String getOlCurso() {
		return olCurso;
	}
	public void setOlCurso(String olCurso) {
		this.olCurso = olCurso;
	}
	public String getOlQ1Comportamiento() {
		return olQ1Comportamiento;
	}	
	public void setOlQ1Comportamiento(String olQ1Comportamiento) {
		this.olQ1Comportamiento = olQ1Comportamiento;
	}
	public String getOlQ2Comportamiento() {
		return olQ2Comportamiento;
	}
	public void setOlQ2Comportamiento(String olQ2Comportamiento) {
		this.olQ2Comportamiento = olQ2Comportamiento;
	}
	public String getOlInfPeriodo() {
		return olInfPeriodo;
	}
	public void setOlInfPeriodo(String olInfPeriodo) {
		this.olInfPeriodo = olInfPeriodo;
	}
	public String getOlInfEstudiante() {
		return olInfEstudiante;
	}
	public void setOlInfEstudiante(String olInfEstudiante) {
		this.olInfEstudiante = olInfEstudiante;
	}
}