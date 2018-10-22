/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.servicios.DAO;
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
	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> matriculaEstList = new ArrayList<Object>();
	
	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	List<SelectItem> listOfertas = new ArrayList<SelectItem>();		

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {
		
		listPeriodos = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = listPeriodos.get(0).getValue().toString();
		
		listOfertas = (ArrayList<SelectItem>) llenaComboOfertas();
		
		generarListadoEst();
	}


	public void cellChangeEvent(final SheetEvent event) {  
		final Sheet sheet = event.getSheet();  
		final List<SheetUpdate> updates = sheet.getUpdates();  
		// A SheetUpdate exists for each column updated, the same row may appear more than once. For this reason we will track those we already persisted  
		final HashSet<MatMatricula> processed = new HashSet<MatMatricula>();  
		int rowUpdates = 0;  
		for (final SheetUpdate update : updates) {  
			final Object asset = (Object) update.getRowData();  
			if (processed.contains(asset)) {  
				continue;  
			}  
			System.out.println("Asset " + ((MatriculaEst) asset).getCodMatricula() + " updated.");  
			rowUpdates++;  
		}  
		sheet.commitUpdates();  
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Success", Integer.toString(rowUpdates) + " rows updated"));  
	}  

	// CONSULTA	
	@SuppressWarnings("unchecked")
	public void generarListadoEst() {		
		try {			
			jpql = "SELECT IFNULL(m.id_matricula, '--'), \n" + 
				   "       e.id_estudiante, \n" + 
				   "       CONCAT(e.apellidos, ' ', e.nombres), \n" + 
				   "       IFNULL(o.id_oferta, '--'), \n" + 
				   "       IFNULL(o.descripcion, '--'), \n" + 
				   "       'N', \n" + 
				   "       'IC', \n" + 
				   "       IFNULL(m.observaciones, 'Ninguna') \n" +
				   "FROM mat_estudiante e \n" + 
				   "	LEFT JOIN mat_matricula m ON e.id_estudiante = m.id_estudiante AND e.estado = 'AC' \n" + 
				   "	LEFT JOIN mat_oferta o ON o.id_oferta = m.id_oferta AND o.estado = 'AC' " + 
				   "ORDER BY 3 ";
						
			List<Object> result = em.createNativeQuery(jpql).getResultList();
			Iterator<Object> itr = result.iterator();
			for (int k = 0; k < result.size(); k++) {
				Object[] obj = (Object[]) itr.next();
							
				MatriculaEst e = new MatriculaEst();				
				e.setCodMatricula(obj[0].toString());
				e.setCodEstudiante(obj[1].toString());
				e.setNomEstudiante(obj[2].toString());
				e.setCodOferta(obj[3].toString());
				e.setNomOferta(obj[4].toString());
				e.setSnAprobada((obj[5].toString().equals("S") ? true : false));
				e.setEstado((obj[6].toString().equals("AC") ? true : false));
				e.setObservacion(obj[7].toString());
							
				matriculaEstList.add(e);
			}
			
		} catch (Exception e) {		
		}				
	}
	
	public void closeDialogo() {
		init();
	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}
	
	public List<SelectItem> llenaComboOfertas() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}
	
	public void onChangePeriodo() {
		listOfertas = (ArrayList<SelectItem>) llenaComboOfertas();
		
		generarListadoEst();
	}
		
	// CLASES
	public class MatriculaEst  implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public String codMatricula = "";
		public String codEstudiante = ""; 
		public String nomEstudiante = "";
		public String codOferta = "";
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
		public String getCodOferta() {
			return codOferta;
		}
		public void setCodOferta(String codOferta) {
			this.codOferta = codOferta;
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
	public List<Object> getMatriculaEstList() {
		return matriculaEstList;
	}
	public void setMatriculaEstList(List<Object> matriculaEstList) {
		this.matriculaEstList = matriculaEstList;
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
	public void setListOfertas(List<SelectItem> listOfertas) {
		this.listOfertas = listOfertas;
	}
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
	}
}