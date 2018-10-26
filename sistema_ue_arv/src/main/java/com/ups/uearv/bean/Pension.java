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
import java.util.Calendar;
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

import com.ups.uearv.entidades.GesDescuento;
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

@ManagedBean(name = "pension")
@ViewScoped
public class Pension implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soPeriodoEst = "";
	String soPeriodoMat = "";
		
	String itBuscar = "";

	
	private List<Object> pensionList = new ArrayList<Object>();
	private List<Object> matriculaList = new ArrayList<Object>();
	
	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	
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
				GesDescuento descuento = (GesDescuento) DAO.buscarObject(new GesDescuento(), "from GesDescuento c where c.nombre = '" + "" + "'");
				GesPension pen = (GesPension) DAO.buscarObject(new GesPension(), "from GesPension c where c.idPension = '" + "" + "'");
								
				pen.setGesDescuento(descuento);
				pen.setUsuarioAct(Session.getUserName());			
				pen.setFechaAct(fecha);			
								
				if (!DAO.saveOrUpdate(pen, 1, em)) 
					em.getTransaction().rollback();			
				else
					em.getTransaction().commit();				
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

	}
	
	public void onChangePeriodo() {
		llenarListMatriculas();
		llenaComboOfertas();	
	}
	
	// CONSULTA		
	public void llenarListMatriculas() {
		matriculaList.clear();
		if(!soPeriodo.equals("NA")) {
			
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
	
	// CLASES	

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
	public ArrayList<SelectItem> getListPeriodos() {
		return listPeriodos;
	}
	public void setListPeriodos(ArrayList<SelectItem> listPeriodos) {
		this.listPeriodos = listPeriodos;
	}
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
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