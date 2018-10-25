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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;

import com.ups.uearv.entidades.GesPension;
import com.ups.uearv.entidades.MatEstudiante;
import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.entidades.MatOferta;
import com.ups.uearv.entidades.MatPeriodo;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "matricula")
@ViewScoped
public class Matricula implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soPeriodoEst = "";
	String soPeriodoMat = "";
		
	String itBuscar = "";

	private List<Object> generaEstudianteList = new ArrayList<Object>();
	private List<Object> matriculaList = new ArrayList<Object>();
	private List<Object> procesaMatriculaList = new ArrayList<Object>();
	
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
	}

	// EVENTOS
	public void cellChangeEvent(final SheetEvent event) {  
		final Sheet sheet = event.getSheet();  
		final List<SheetUpdate> updates = sheet.getUpdates();    
		final HashSet<MatMatricula> processed = new HashSet<MatMatricula>();  
		int rowUpdates = 0;  

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
				MatOferta oferta = (MatOferta) DAO.buscarObject(new MatOferta(), "from MatOferta c where c.descripcion = '" + ((MatriculaEst) asset).getNomOferta() + "'");
				MatMatricula mat = (MatMatricula) DAO.buscarObject(new MatMatricula(), "from MatMatricula c where c.idMatricula = '" + ((MatriculaEst) asset).getCodMatricula() + "'");
								
				mat.setSnAprobado(((MatriculaEst) asset).isSnAprobada() ? "S" : "N");
				mat.setMatOferta(oferta);
				mat.setObservaciones(((MatriculaEst) asset).getObservacion());
				mat.setUsuarioAct(Session.getUserName());			
				mat.setFechaAct(fecha);			
								
				if (!DAO.saveOrUpdate(mat, 1, em)) 
					em.getTransaction().rollback();			
				else
					em.getTransaction().commit();				
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
			}
			em.close();
			
			rowUpdates++;  
		}  
		sheet.commitUpdates();  
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Success", Integer.toString(rowUpdates) + " rows updated"));  
	}  
	
	public void closeDialogo() {
		init();
		
		generaEstudianteList.clear();
		procesaMatriculaList.clear();
	}
	
	public void onChangePeriodo() {
		llenarListMatriculas();
		llenaComboOfertas();	
	}
	
	public void onChangePeriodoEst() {
		llenarListGeneraEstudiantes();
	}
	
	public void onChangePeriodoMat() {
		llenarListProcesaMatriculas();
	}

	// CONSULTA	
	public void llenarListGeneraEstudiantes() {		
		generaEstudianteList.clear();
		if(!soPeriodoEst.equals("NA")) {
			jpql = "SELECT c.* FROM mat_estudiante c " + 
					   "WHERE c.estado = 'AC' " + 
					   "AND c.id_estudiante NOT IN (SELECT m.id_estudiante FROM mat_matricula m WHERE m.id_periodo = '" + soPeriodoEst + "' " +
					   " AND m.estado = 'AC') ORDER BY c.apellidos ";
				List<Object> l = DAO.nqObject(new MatEstudiante(), jpql);
						
				for (Object in : l)
					generaEstudianteList.add(in);	
		}		
	}
	
	public void llenarListProcesaMatriculas() {		
		procesaMatriculaList.clear();
		if(!soPeriodoMat.equals("NA")) {
			jpql = "SELECT m.* " + 
					"FROM mat_matricula m " + 
					"	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante AND e.estado = 'AC' " + 
					"WHERE m.id_periodo = '" + soPeriodoMat + "' AND m.id_oferta IS NOT NULL AND m.sn_aprobado = 'S' AND m.estado = 'AC' " + 
					"ORDER BY e.apellidos ";
				List<Object> l = DAO.nqObject(new MatMatricula(), jpql);
						
				for (Object in : l)
					procesaMatriculaList.add(in);	
		}		
	}
	
	public void llenarListMatriculas() {
		matriculaList.clear();
		if(!soPeriodo.equals("NA")) {
			jpql = "SELECT IFNULL(LPAD(m.id_matricula,5,'0'), '--'), e.id_estudiante, CONCAT(e.apellidos, ' ', e.nombres), " +
					"	IFNULL(o.descripcion, 'Seleccione Oferta'), IFNULL(m.sn_aprobado, 'N'), IFNULL(m.estado, 'IC'), IFNULL(m.observaciones, 'Ninguna') " +
					"FROM mat_matricula m " + 
					"	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante AND e.apellidos LIKE '%" + itBuscar + "%' AND e.estado = 'AC' " + 
					"	LEFT JOIN mat_oferta o ON o.id_oferta = m.id_oferta AND o.estado = 'AC' " + 
					"WHERE m.id_periodo = '" + soPeriodo + "' AND m.estado = 'AC' " +
					"ORDER BY 3 ";

			@SuppressWarnings("unchecked")
			List<Object> result = em.createNativeQuery(jpql).getResultList();
			Iterator<Object> itr = result.iterator();
			for (int k = 0; k < result.size(); k++) {
				Object[] obj = (Object[]) itr.next();

				MatriculaEst e = new MatriculaEst();				
				e.setCodMatricula(obj[0].toString());
				e.setCodEstudiante(obj[1].toString());
				e.setNomEstudiante(obj[2].toString());					
				e.setNomOferta(obj[3].toString());
				e.setSnAprobada((obj[4].toString().equals("S") ? true : false));
				e.setEstado((obj[5].toString().equals("AC") ? true : false));
				e.setObservacion(obj[6].toString());	

				matriculaList.add(e);
			}
		}			
	}
	
	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> llenaComboOfertas() {
		jpql = "SELECT o.id_oferta, o.descripcion " + 
			   "FROM mat_oferta o " + 
			   "  	INNER JOIN mat_curso c ON c.id_curso = o.id_curso " + 
			   "	INNER JOIN catalogo_det k ON k.codigo_det = c.nivel " + 
			   "WHERE o.id_periodo = '" + soPeriodo + "'  ORDER BY k.codigo_det, c.id_curso ";

		ArrayList<String> listaOfertas = new ArrayList<String>();
		listaOfertas.add("Seleccione Oferta");

		List<Object> result = em.createNativeQuery(jpql).getResultList();
		Iterator<Object> itr = result.iterator();
		for (int k = 0; k < result.size(); k++) {
			Object[] obj = (Object[]) itr.next();
			listaOfertas.add(obj[1].toString());
		}		
		return listaOfertas;
	}

	// ACCIONES
	public void generaListadoEst() {
		Date date = new Date();
		Timestamp fecha = new Timestamp(date.getTime());

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			for (Object object : generaEstudianteList) {
				MatPeriodo periodo = (MatPeriodo) DAO.buscarObject(new MatPeriodo(), "from MatPeriodo c where c.idPeriodo = '" + soPeriodoEst +"'");

				MatMatricula mat = new MatMatricula();
				mat.setMatPeriodo(periodo);
				mat.setMatEstudiante((MatEstudiante) object);
				mat.setUsuarioIng(Session.getUserName());			
				mat.setFechaIng(fecha);			
				mat.setEstado("AC");
				if (!DAO.saveOrUpdate(mat, 0, em)) 
					em.getTransaction().rollback();			
			}
			em.getTransaction().commit();
			llenarListGeneraEstudiantes();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}
	
	public void procesaMatriculas() {
		Date date = new Date();
		Timestamp fecha = new Timestamp(date.getTime());
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			for (Object object : procesaMatriculaList) {
				
				
				
				GesPension pen = new GesPension(); 
				
			}			
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}
	
	// CLASES
	public class MatriculaEst  implements Serializable {

		private static final long serialVersionUID = 1L;

		public String codMatricula = "";
		public String codEstudiante = ""; 
		public String nomEstudiante = "";
		public String nomOferta = "";
		public String observacion = "";
		public boolean snAprobada = true;		
		public boolean estado = true;

		public String getCodMatricula() {
			return codMatricula;
		}
		public void setCodMatricula(String codMatricula) {
			this.codMatricula = codMatricula;
		}
		public String getCodEstudiante() {
			return codEstudiante;
		}
		public void setCodEstudiante(String codEstudiante) {
			this.codEstudiante = codEstudiante;
		}
		public String getNomEstudiante() {
			return nomEstudiante;
		}
		public void setNomEstudiante(String nomEstudiante) {
			this.nomEstudiante = nomEstudiante;
		}	
		public String getNomOferta() {
			return nomOferta;
		}
		public void setNomOferta(String nomOferta) {
			this.nomOferta = nomOferta;
		}
		public boolean isSnAprobada() {
			return snAprobada;
		}
		public void setSnAprobada(boolean snAprobada) {
			this.snAprobada = snAprobada;
		}
		public boolean isEstado() {
			return estado;
		}
		public void setEstado(boolean estado) {
			this.estado = estado;
		}
		public String getObservacion() {
			return observacion;
		}
		public void setObservacion(String observacion) {
			this.observacion = observacion;
		}		
	}

	// GETTERS AND SETTERS
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}	
	public String getItBuscar() {
		return itBuscar;
	}
	public void setItBuscar(String itBuscar) {
		this.itBuscar = itBuscar;
	}
	public List<Object> getMatriculaList() {
		return matriculaList;
	}
	public void setMatriculaList(List<Object> matriculaList) {
		this.matriculaList = matriculaList;
	}
	public List<Object> getProcesaMatriculaList() {
		return procesaMatriculaList;
	}
	public void setProcesaMatriculaList(List<Object> procesaMatriculaList) {
		this.procesaMatriculaList = procesaMatriculaList;
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
	public List<Object> getGeneraEstudianteList() {
		return generaEstudianteList;
	}
	public void setGeneraEstudianteList(List<Object> generaEstudianteList) {
		this.generaEstudianteList = generaEstudianteList;
	}
	public String getSoPeriodoEst() {
		return soPeriodoEst;
	}
	public void setSoPeriodoEst(String soPeriodoEst) {
		this.soPeriodoEst = soPeriodoEst;
	}

	public String getSoPeriodoMat() {
		return soPeriodoMat;
	}
	public void setSoPeriodoMat(String soPeriodoMat) {
		this.soPeriodoMat = soPeriodoMat;
	}	
}