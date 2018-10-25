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

	String soPeriodoEst = "";
	String soPeriodo = "";
	
	String itBuscar = "";

	private List<Object> estudianteList = new ArrayList<Object>();
	private List<Object> matriculaEstList = new ArrayList<Object>();
	
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
			
			System.out.println(((MatriculaEst) asset).getCodMatricula() + " updated.");
			
			rowUpdates++;  
		}  
		sheet.commitUpdates();  
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Success", Integer.toString(rowUpdates) + " rows updated"));  
	}  

	// CONSULTA	
	public void llenarListEstudiantes() {		
		estudianteList.clear();
		if(!soPeriodoEst.equals("NA")) {
			jpql = "SELECT c.* FROM mat_estudiante c " + 
					   "WHERE c.estado = 'AC' " + 
					   "AND c.id_estudiante NOT IN (SELECT m.id_estudiante FROM mat_matricula m WHERE m.id_periodo = '" + soPeriodoEst + "' " +
					   " AND m.estado = 'AC') ORDER BY c.apellidos ";
				List<Object> l = DAO.nqObject(new MatEstudiante(), jpql);
						
				for (Object in : l)
					estudianteList.add(in);	
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void llenarLista() {
		try {		
			matriculaEstList.clear();
			if(!soPeriodo.equals("NA")) {
				jpql = "SELECT IFNULL(LPAD(m.id_matricula,5,'0'), '--'), e.id_estudiante, CONCAT(e.apellidos, ' ', e.nombres), " +
					   "	IFNULL(o.descripcion, 'Seleccione Oferta'), IFNULL(m.sn_aprobado, 'N'), IFNULL(m.estado, 'IC'), IFNULL(m.observaciones, 'Ninguna') " +
					   "FROM mat_matricula m " + 
					   "	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante AND e.apellidos LIKE '%" + itBuscar + "%' AND e.estado = 'AC' " + 
					   "	LEFT JOIN mat_oferta o ON o.id_oferta = m.id_oferta AND o.estado = 'AC' " + 
					   "WHERE m.id_periodo = '" + soPeriodo + "' AND m.estado = 'AC' " +
					   "ORDER BY 3 ";

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
					
					matriculaEstList.add(e);
				}
			}			
		} catch (Exception e) {		
		}				
	}
	
	public void generaListadoEst() {
		Date date = new Date();
		Timestamp fecha = new Timestamp(date.getTime());

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			for (Object object : estudianteList) {
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
			llenarListEstudiantes();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}

	public void closeDialogo() {
		init();
		
		estudianteList.clear();
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

	public void onChangePeriodo() {
		llenarLista();
		llenaComboOfertas();	
	}
	
	public void onChangePeriodoEst() {
		llenarListEstudiantes();
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
	public void setListOfertas(ArrayList<SelectItem> listOfertas) {
		this.listOfertas = listOfertas;
	}
	public String getSoPeriodo() {
		return soPeriodo;
	}
	public void setSoPeriodo(String soPeriodo) {
		this.soPeriodo = soPeriodo;
	}
	public List<Object> getEstudianteList() {
		return estudianteList;
	}
	public void setEstudianteList(List<Object> estudianteList) {
		this.estudianteList = estudianteList;
	}
	public String getSoPeriodoEst() {
		return soPeriodoEst;
	}
	public void setSoPeriodoEst(String soPeriodoEst) {
		this.soPeriodoEst = soPeriodoEst;
	}	
}