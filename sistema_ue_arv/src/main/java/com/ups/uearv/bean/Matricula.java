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
import javax.persistence.Query;

import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;

import com.ups.uearv.entidades.CalAsistencia;
import com.ups.uearv.entidades.CalCalificacion;
import com.ups.uearv.entidades.CalControl;
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
	String soPeriodoAnu = "";
	
	String soMatricula = "";
		
	String itBuscar = "";
	
	String itEstudiante = "";
	String itaObservacion = "";

	private List<Object> generaEstudianteList = new ArrayList<Object>();
	private List<Object> matriculaList = new ArrayList<Object>();
	private List<Object> procesaMatriculaList = new ArrayList<Object>();
	
	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertas = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listMatriculas = new ArrayList<SelectItem>();
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {
		
		listPeriodos = (ArrayList<SelectItem>) llenaComboPeriodo();
		
		soPeriodoEst = "NA";
		soPeriodoMat = "NA";
		soPeriodoAnu = "NA";
		
		itEstudiante = "";
		itaObservacion = "";
		
		onChangePeriodoAnu();
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
				MatOferta oferta = (MatOferta) DAO.buscarObject(new MatOferta(), "from MatOferta c where c.descripcion = '" + ((MatriculaEst) asset).getNomOferta() + "'");
				MatMatricula mat = (MatMatricula) DAO.buscarObject(new MatMatricula(), "from MatMatricula c where c.idMatricula = '" + ((MatriculaEst) asset).getCodMatricula() + "'");
								
				mat.setSnAprobado(((MatriculaEst) asset).isSnAprobada() ? "S" : "N");
				mat.setMatOferta(oferta);
				mat.setObservaciones(((MatriculaEst) asset).getObservacion());
				mat.setUsuarioAct(Session.getUserName());			
				mat.setFechaAct(fecha);			
								
				if (DAO.saveOrUpdate(mat, 1, em)) {
					em.getTransaction().commit();
				} else {
					em.getTransaction().rollback();
				}
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
	
	public void onChangePeriodoAnu() {
		listMatriculas = (ArrayList<SelectItem>) llenaComboEstudiante();
		soMatricula = "NA";
	}
	
	public void onChangeMatricula() {
		itEstudiante = "";
		if(!soMatricula.equals("NA")) 
			itEstudiante = DAO.getEstMatricula(soMatricula);
	}

	// CONSULTA	
	public void llenarListGeneraEstudiantes() {		
		generaEstudianteList.clear();
		if(!soPeriodoEst.equals("NA")) {
			jpql = 
			"SELECT c.* FROM mat_estudiante c \r\n" + 
			"WHERE c.estado = 'AC' \r\n" + 
			"AND c.id_estudiante NOT IN (SELECT m.id_estudiante FROM mat_matricula m WHERE m.id_periodo = '" + soPeriodoEst + "' \r\n" +
			"AND m.estado = 'AC') ORDER BY c.apellidos ";
		
			List<Object> l = DAO.nqObject(new MatEstudiante(), jpql);
					
			for (Object in : l)
				generaEstudianteList.add(in);	
		}		
	}
	
	public void llenarListProcesaMatriculas() {		
		procesaMatriculaList.clear();
		if(!soPeriodoMat.equals("NA")) {
			jpql = 
			"SELECT m.* \r\n" + 
			"FROM mat_matricula m \r\n" + 
			"	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante AND e.estado = 'AC' \r\n" + 
			"WHERE m.id_periodo = '" + soPeriodoMat + "' AND m.id_oferta IS NOT NULL AND m.sn_aprobado = 'S' AND m.estado = 'AC' \r\n" + 
			"AND m.id_matricula NOT IN (SELECT DISTINCT id_matricula FROM ges_pension WHERE estado = 'AC') \r\n" +
			"ORDER BY e.apellidos ";
			
			List<Object> l = DAO.nqObject(new MatMatricula(), jpql);
					
			for (Object in : l)
				procesaMatriculaList.add(in);	
		}		
	}
	
	public void llenarListMatriculas() {
		matriculaList.clear();
		if(!soPeriodo.equals("NA")) {
			jpql = 
			"SELECT IFNULL(LPAD(m.id_matricula,5,'0'), '--'), e.id_estudiante, CONCAT(e.apellidos, ' ', e.nombres), \r\n" + 
			"	IFNULL(o.descripcion, 'Seleccione Oferta'), IFNULL(m.sn_aprobado, 'N'), IFNULL(m.estado, 'IC'), IFNULL(m.observaciones, 'Ninguna') \r\n" + 
			"FROM mat_matricula m \r\n" + 
			"	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante AND e.apellidos LIKE '%" + itBuscar + "%' AND e.estado = 'AC' \r\n" + 
			"	LEFT JOIN mat_oferta o ON o.id_oferta = m.id_oferta AND o.estado = 'AC' \r\n" + 
			"WHERE m.id_periodo = '" + soPeriodo + "' AND m.estado = 'AC' \r\n" +
			"AND m.id_matricula NOT IN (SELECT p.id_matricula FROM ges_pension p WHERE secuencia = 0) \r\n" +			
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
		jpql = 
		"SELECT o.id_oferta, o.descripcion \r\n" + 
		"FROM mat_oferta o \r\n" + 
		"  	INNER JOIN mat_curso c ON c.id_curso = o.id_curso \r\n" + 
		"	INNER JOIN catalogo_det k ON k.codigo_det = c.nivel \r\n" + 
		"WHERE o.id_periodo = '" + soPeriodo + "' AND o.estado = 'AC' ORDER BY k.codigo_det, c.id_curso ";

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
	
	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getMatriculas(), 1);
	}

	public Query getMatriculas() {		
		jpql = 
		"SELECT IFNULL(LPAD(m.id_matricula,5,'0'), '--') \r\n" + 
		"FROM mat_estudiante e \r\n" + 
		"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
		"WHERE m.id_periodo = '" + soPeriodoAnu + "' \r\n" +
		"AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
		"ORDER BY 1";

		Query query = em.createNativeQuery(jpql);
		return query;
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
			mensaje = "Se generó el listado de matrícula(s) para " + generaEstudianteList.size() + " estudiante(s) correctamente";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
			llenarListGeneraEstudiantes();
			llenarListMatriculas();
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
			for (Object ob : procesaMatriculaList) {
				
				int cantPensiones = ((MatMatricula) ob).getMatPeriodo().getCantPensiones();
				BigDecimal precioMatricula = ((MatMatricula) ob).getMatPeriodo().getPrecioMatricula();
				BigDecimal precioPension = ((MatMatricula) ob).getMatPeriodo().getPrecioPension();
				Date fechaInicio = ((MatMatricula) ob).getMatPeriodo().getFechaIni();
				
				Calendar cal = Calendar.getInstance(); 
				cal.setTime(fechaInicio);
				
				// SEC 0 : MATRICULA 
				// SEC 1 a n : PENSIONES				
				for (int sec = 0; sec <= cantPensiones; sec++) {	
					if (sec >= 2) 
						cal.add(Calendar.MONTH, 1);
				
					GesPension pen = new GesPension();
					pen.setMatMatricula((MatMatricula) ob);
					pen.setSecuencia(sec);
					pen.setPrecio((sec == 0 ? precioMatricula : precioPension));
					pen.setAbono(BigDecimal.ZERO);
					pen.setTotalPagar((sec == 0 ? precioMatricula : precioPension));
					pen.setSaldo((sec == 0 ? precioMatricula : precioPension));					
					pen.setFechaVence(cal.getTime());
					pen.setUsuarioIng(Session.getUserName());			
					pen.setFechaIng(fecha);	
					pen.setEstado("AC");
					
					if (!DAO.saveOrUpdate(pen, 0, em)) 
						em.getTransaction().rollback();		
				}
			}
			em.getTransaction().commit();			
			mensaje = "Se procesó " + procesaMatriculaList.size() + " matrícula(s) correctamente";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
			llenarListProcesaMatriculas();
			llenarListMatriculas();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}
	
	public void anularMatricula() {		
		// VALIDACIONES
		if (soMatricula.trim().equals("NA")) {
			mensaje = "Debe seleccionar la matrícula";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (itaObservacion.trim().equals("")) {
			mensaje = "Debe ingresar la observación(motivo de anulación)";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		
		// PROCESO
		Date date = new Date();
		Timestamp fecha = new Timestamp(date.getTime());
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {			
			jpql = "SELECT m.* FROM mat_matricula m WHERE m.id_matricula = '" + soMatricula + "' ";
			List<Object> listMat = DAO.nqObject(new MatMatricula(), jpql);						
			for (Object mat : listMat) {	
				((MatMatricula) mat).setUsuarioAct(Session.getUserName());
				((MatMatricula) mat).setFechaAct(fecha);	
				((MatMatricula) mat).setObservaciones("Anulada: " + itaObservacion);
				((MatMatricula) mat).setEstado("IC");
				if (!DAO.saveOrUpdate(mat, 1, em)) {
					em.getTransaction().rollback();
					return;
				}			
			}
			
			jpql = "SELECT p.* FROM ges_pension p WHERE p.id_matricula = '" + soMatricula + "' ";
			List<Object> listPen = DAO.nqObject(new GesPension(), jpql);						
			for (Object pen : listPen) {
				((GesPension) pen).setUsuarioAct(Session.getUserName());
				((GesPension) pen).setFechaAct(fecha);	
				((GesPension) pen).setEstado("IC");
				if (!DAO.saveOrUpdate(pen, 1, em)) {
					em.getTransaction().rollback();
					return;
				}		
			}			
			
			jpql = "SELECT c.* FROM cal_control c WHERE c.id_matricula = '" + soMatricula + "' ";
			List<Object> listCon = DAO.nqObject(new CalControl(), jpql);						
			for (Object con : listCon) {
				((CalControl) con).setUsuarioAct(Session.getUserName());
				((CalControl) con).setFechaAct(fecha);	
				((CalControl) con).setEstado("IC");
				if (!DAO.saveOrUpdate(con, 1, em)) {
					em.getTransaction().rollback();
					return;
				}
			}
			
			jpql = "SELECT a.* FROM cal_asistencia a WHERE a.id_matricula = '" + soMatricula + "' ";
			List<Object> listAsi = DAO.nqObject(new CalAsistencia(), jpql);						
			for (Object asi : listAsi) {
				((CalAsistencia) asi).setUsuarioAct(Session.getUserName());
				((CalAsistencia) asi).setFechaAct(fecha);	
				((CalAsistencia) asi).setEstado("IC");
				if (!DAO.saveOrUpdate(asi, 1, em)) {
					em.getTransaction().rollback();
					return;
				}		
			}
			
			jpql = "SELECT ca.* FROM cal_calificacion ca WHERE ca.id_matricula = '" + soMatricula + "' ";
			List<Object> listCal = DAO.nqObject(new CalCalificacion(), jpql);						
			for (Object cal : listCal) {
				((CalCalificacion) cal).setUsuarioAct(Session.getUserName());
				((CalCalificacion) cal).setFechaAct(fecha);	
				((CalCalificacion) cal).setEstado("IC");
				if (!DAO.saveOrUpdate(cal, 1, em)) {
					em.getTransaction().rollback();
					return;
				}		
			}
			
			em.getTransaction().commit();
			mensaje = "Anulación exitosa";
			FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
			
			onChangePeriodoAnu();
			llenarListMatriculas();
			
			itEstudiante = "";
			itaObservacion = "";
			
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}				
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

	public String getSoPeriodoAnu() {
		return soPeriodoAnu;
	}
	public void setSoPeriodoAnu(String soPeriodoAnu) {
		this.soPeriodoAnu = soPeriodoAnu;
	}
	public String getSoMatricula() {
		return soMatricula;
	}
	public void setSoMatricula(String soMatricula) {
		this.soMatricula = soMatricula;
	}
	public ArrayList<SelectItem> getListMatriculas() {
		return listMatriculas;
	}
	public void setListMatriculas(ArrayList<SelectItem> listMatriculas) {
		this.listMatriculas = listMatriculas;
	}
	public String getItEstudiante() {
		return itEstudiante;
	}
	public void setItEstudiante(String itEstudiante) {
		this.itEstudiante = itEstudiante;
	}
	public String getItaObservacion() {
		return itaObservacion;
	}
	public void setItaObservacion(String itaObservacion) {
		this.itaObservacion = itaObservacion;
	}	
}