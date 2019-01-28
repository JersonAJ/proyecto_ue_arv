/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;

import com.ups.uearv.entidades.CalAsistencia;
import com.ups.uearv.entidades.CalComportamiento;
import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "asistencia")
@ViewScoped
public class Asistencia implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soOferta = "";
	String soQuimestre = "";
	String soParcial = "";

	private List<Object> asistenciaList = new ArrayList<Object>();

	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertas = new ArrayList<SelectItem>();		

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {

		listPeriodos = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";

		soQuimestre = "1";
		soParcial = "1";

		onChangePeriodo();
	}

	// EVENTOS
	public void cellChangeEvent(final SheetEvent event) {  
		final Sheet sheet = event.getSheet();  
		final List<SheetUpdate> updates = sheet.getUpdates();    
		final HashSet<MatMatricula> processed = new HashSet<MatMatricula>();  

		for (final SheetUpdate update : updates) {  
			final Object asset = (Object) update.getRowData();  
			if (processed.contains(asset)) {  
				continue;  
			}

			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());

			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			try {				
				String codAsistencia = ((AsistenciaEst) asset).getCodAsistencia();
				String codMatricula = ((AsistenciaEst) asset).getCodMatricula();
				int asistencias = ((AsistenciaEst) asset).getAsistencias();
				int atrasos = ((AsistenciaEst) asset).getAtrasos();
				int faltas = ((AsistenciaEst) asset).getFaltas();
				int justificados = ((AsistenciaEst) asset).getJustificados();
				// String observacion = ((AsistenciaEst) asset).getObservacion();
				String nomComportamiento = ((AsistenciaEst) asset).getNomComportamiento();
				String proyectos = ((AsistenciaEst) asset).getProyectos();

				MatMatricula matricula = (MatMatricula) DAO.buscarObject(new MatMatricula(), "from MatMatricula c where c.idMatricula = '" + codMatricula + "'");
				CalComportamiento comportamiento = (CalComportamiento) DAO.buscarObject(new CalComportamiento(),"from CalComportamiento c where c.descripcion = '" + nomComportamiento + "'");

				int op = 0; // NUEVO REGISTRO		
				CalAsistencia asi = new CalAsistencia();

				if (!codAsistencia.equals("--")) {
					op = 1; // ACTUALIZA REGISTRO
					asi = (CalAsistencia) DAO.buscarObject(new CalAsistencia(), "from CalAsistencia c where c.idAsistencia = '" + codAsistencia + "'");

					asi.setUsuarioAct(Session.getUserName());
					asi.setFechaAct(fecha);		
				} else {
					asi.setUsuarioIng(Session.getUserName());
					asi.setFechaIng(fecha);		
					asi.setEstado("AC");
				}

				asi.setMatMatricula(matricula);
				asi.setQuimestre(Integer.parseInt(soQuimestre));
				asi.setParcial(Integer.parseInt(soParcial));
				asi.setAsistencias(asistencias);					
				asi.setAtrasos(atrasos);
				asi.setFaltas(faltas);
				asi.setJustificados(justificados);
				// asi.setObservacion(observacion);
				asi.setCalComportamiento(comportamiento);
				asi.setProyectosEsc(proyectos);

				if (!DAO.saveOrUpdate(asi, op, em)) 
					em.getTransaction().rollback();			
				else
					em.getTransaction().commit();	

				llenarListAsistencia();
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
			}
			em.close();  
		}  
		sheet.commitUpdates();  
	}  

	public void closeDialogo() {
		init();

		asistenciaList.clear();
	}

	public void onChangePeriodo() {
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";

		onChangeOferta();
	}

	public void onChangeOferta() {
		llenarListAsistencia();
	}

	// CONSULTA	
	public void llenarListAsistencia() {
		asistenciaList.clear();
		
		if (!soPeriodo.equals("NA")) {
			if (!soOferta.equals("NA")) {

				jpql = "CALL consulta_asistencias (" + soPeriodo + "," + soOferta  + "," + soQuimestre  + "," + soParcial  + ")";

				@SuppressWarnings("unchecked")
				List<Object> result = em.createNativeQuery(jpql).getResultList();
				Iterator<Object> itr = result.iterator();
				for (int k = 0; k < result.size(); k++) {
					Object[] obj = (Object[]) itr.next();

					AsistenciaEst e = new AsistenciaEst();
					e.setCodAsistencia(obj[0].toString());
					e.setCodMatricula(obj[1].toString());
					e.setNomEstudiante(obj[2].toString());
					e.setAsistencias(Integer.parseInt(obj[3].toString()));
					e.setAtrasos(Integer.parseInt(obj[4].toString()));
					e.setFaltas(Integer.parseInt(obj[5].toString()));
					e.setJustificados(Integer.parseInt(obj[6].toString()));
					e.setObservacion(obj[7].toString());
					e.setNomComportamiento(obj[8].toString());
					e.setProyectos(obj[9].toString());

					asistenciaList.add(e);
				}
			}
		}
	}


	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> llenaComboComportamiento() {
		jpql = " SELECT id_comportamiento, descripcion FROM cal_comportamiento WHERE estado = 'AC' ";

		ArrayList<String> listaOfertas = new ArrayList<String>();
		listaOfertas.add("Seleccione Comportamiento");

		List<Object> result = em.createNativeQuery(jpql).getResultList();
		Iterator<Object> itr = result.iterator();
		for (int k = 0; k < result.size(); k++) {
			Object[] obj = (Object[]) itr.next();
			listaOfertas.add(obj[1].toString());
		}		
		return listaOfertas;
	}
	
	public List<String> llenaComboProyecto() {
		ArrayList<String> listaProyectos = new ArrayList<String>();
		listaProyectos.add("Seleccione");
		listaProyectos.add("Excelente");
		listaProyectos.add("Muy Bueno");
		listaProyectos.add("Bueno");
		listaProyectos.add("Regular");		
		
		return listaProyectos;
	}
	
	// CLASES
	public class AsistenciaEst implements Serializable {

		private static final long serialVersionUID = 1L;

		public String codAsistencia = "";
		public String codMatricula = "";
		public String nomEstudiante = "";
		public String nomComportamiento = "";
		public int asistencias;
		public int atrasos;
		public int faltas;
		public int justificados;
		public String observacion = "";
		public String proyectos = "";

		public String getCodMatricula() {
			return codMatricula;
		}
		public void setCodMatricula(String codMatricula) {
			this.codMatricula = codMatricula;
		}		
		public String getNomEstudiante() {
			return nomEstudiante;
		}
		public void setNomEstudiante(String nomEstudiante) {
			this.nomEstudiante = nomEstudiante;
		}
		public String getCodAsistencia() {
			return codAsistencia;
		}
		public void setCodAsistencia(String codAsistencia) {
			this.codAsistencia = codAsistencia;
		}
		public int getAsistencias() {
			return asistencias;
		}
		public void setAsistencias(int asistencias) {
			this.asistencias = asistencias;
		}
		public int getAtrasos() {
			return atrasos;
		}
		public void setAtrasos(int atrasos) {
			this.atrasos = atrasos;
		}
		public int getFaltas() {
			return faltas;
		}
		public void setFaltas(int faltas) {
			this.faltas = faltas;
		}
		public int getJustificados() {
			return justificados;
		}
		public void setJustificados(int justificados) {
			this.justificados = justificados;
		}
		public String getObservacion() {
			return observacion;
		}
		public void setObservacion(String observacion) {
			this.observacion = observacion;
		}
		public String getNomComportamiento() {
			return nomComportamiento;
		}
		public void setNomComportamiento(String nomComportamiento) {
			this.nomComportamiento = nomComportamiento;
		}
		public String getProyectos() {
			return proyectos;
		}
		public void setProyectos(String proyectos) {
			this.proyectos = proyectos;
		}
	}

	// GETTERS AND SETTERS
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}	
	public ArrayList<SelectItem> getListPeriodos() {
		return listPeriodos;
	}
	public void setListPeriodos(ArrayList<SelectItem> listPeriodos) {
		this.listPeriodos = listPeriodos;
	}
	public List<SelectItem> getListOfertas() {
		return listOfertas;
	}
	public void setListOfertas(ArrayList<SelectItem> listOfertas) {
		this.listOfertas = listOfertas;
	}
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
	public String getSoQuimestre() {
		return soQuimestre;
	}
	public void setSoQuimestre(String soQuimestre) {
		this.soQuimestre = soQuimestre;
	}
	public List<Object> getAsistenciaList() {
		return asistenciaList;
	}
	public void setAsistenciaList(List<Object> asistenciaList) {
		this.asistenciaList = asistenciaList;
	}
	public String getSoParcial() {
		return soParcial;
	}
	public void setSoParcial(String soParcial) {
		this.soParcial = soParcial;
	}	
}