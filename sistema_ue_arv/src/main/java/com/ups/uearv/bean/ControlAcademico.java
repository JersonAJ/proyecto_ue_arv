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

import com.ups.uearv.entidades.CalAsignatura;
import com.ups.uearv.entidades.CalComportamiento;
import com.ups.uearv.entidades.CalControl;
import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "control")
@ViewScoped
public class ControlAcademico implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soOferta = "";
	String soAsignatura = "";
	String soQuimestre = "";
	String soParcial = "";

	private List<Object> controlList = new ArrayList<Object>();

	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertas = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listAsignaturas = new ArrayList<SelectItem>();

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
				String codControl = ((ControlEst) asset).getCodControl();
				String codMatricula = ((ControlEst) asset).getCodMatricula();
				String nomComportamiento = ((ControlEst) asset).getNomComportamiento();				
				BigDecimal leccion = ((ControlEst) asset).getLeccion();
				BigDecimal deber = ((ControlEst) asset).getDeber();
				BigDecimal taller = ((ControlEst) asset).getTaller();
				BigDecimal examen = ((ControlEst) asset).getExamen();
				
				MatMatricula matricula = (MatMatricula) DAO.buscarObject(new MatMatricula(), "from MatMatricula c where c.idMatricula = '" + codMatricula + "'");
				CalAsignatura asignatura = (CalAsignatura) DAO.buscarObject(new CalAsignatura(), "from CalAsignatura c where c.idAsignatura = '" + soAsignatura + "'");
				CalComportamiento comportamiento = (CalComportamiento) DAO.buscarObject(new CalComportamiento(),"from CalComportamiento c where c.descripcion = '" + nomComportamiento + "'");
				
				int op = 0; // NUEVO REGISTRO		
				CalControl con = new CalControl();
								
				if (!codControl.equals("--")) {
					op = 1; // ACTUALIZA REGISTRO
					con = (CalControl) DAO.buscarObject(new CalControl(), "from CalControl c where c.idControl = '" + codControl + "'");
					
					con.setUsuarioAct(Session.getUserName());
					con.setFechaAct(fecha);		
				} else {
					con.setUsuarioIng(Session.getUserName());
					con.setFechaIng(fecha);		
					con.setEstado("AC");
				}
					
				con.setMatMatricula(matricula);
				con.setCalAsignatura(asignatura);				
				con.setQuimestre(Integer.parseInt(soQuimestre));
				con.setParcial(Integer.parseInt(soParcial));
				
				if (!soParcial.equals("0")) {								
					con.setCalComportamiento(comportamiento);					
					con.setLeccion(leccion);
					con.setDeber(deber);
					con.setTaller(taller);					
				} else {
					con.setExam(examen);
				}		
					

				if (!DAO.saveOrUpdate(con, op, em)) 
					em.getTransaction().rollback();			
				else
					em.getTransaction().commit();	
				
				llenarListControl();
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

		controlList.clear();
	}

	public void onChangePeriodo() {
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";

		onChangeOferta();
		
		llenaComboComportamiento();
	}

	public void onChangeOferta() {
		listAsignaturas = (ArrayList<SelectItem>) llenaComboAsignaturas();
		soAsignatura = "NA";

		onChangeAsignatura();
	}

	public void onChangeAsignatura() {
		llenarListControl();
	}

	// CONSULTA	
	public void llenarListControl() {
		controlList.clear();
		if (!soPeriodo.equals("NA")) {
			if (!soOferta.equals("NA")) {
				if (!soAsignatura.equals("NA")) {
					
					jpql = "CALL consulta_control_academico (" + soPeriodo + "," + soOferta + "," + soAsignatura + ","
							+ soQuimestre + "," + soParcial + ")";

					@SuppressWarnings("unchecked")
					List<Object> result = em.createNativeQuery(jpql).getResultList();
					Iterator<Object> itr = result.iterator();
					for (int k = 0; k < result.size(); k++) {
						Object[] obj = (Object[]) itr.next();

						ControlEst e = new ControlEst();
						e.setCodControl(obj[0].toString());
						e.setCodMatricula(obj[1].toString());
						e.setNomEstudiante(obj[2].toString());
						e.setNomComportamiento(obj[3].toString());
						e.setLeccion(new BigDecimal(obj[4].toString()));
						e.setDeber(new BigDecimal(obj[5].toString()));
						e.setTaller(new BigDecimal(obj[6].toString()));
						e.setExamen(new BigDecimal(obj[7].toString()));

						controlList.add(e);
					}
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

	public List<SelectItem> llenaComboAsignaturas() {
		return Util.llenaCombo(DAO.getAsignaturasControl(soOferta), 2);
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

	// ACCIONES

	// CLASES
	public class ControlEst  implements Serializable {

		private static final long serialVersionUID = 1L;

		public String codControl = "";
		public String codMatricula = "";
		public String nomEstudiante = "";
		public String nomComportamiento = "";		
		public BigDecimal leccion;
		public BigDecimal deber;
		public BigDecimal taller;
		public BigDecimal examen;

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
		public String getCodControl() {
			return codControl;
		}
		public void setCodControl(String codControl) {
			this.codControl = codControl;
		}
		public String getNomComportamiento() {
			return nomComportamiento;
		}
		public void setNomComportamiento(String nomComportamiento) {
			this.nomComportamiento = nomComportamiento;
		}
		public BigDecimal getLeccion() {
			return leccion;
		}
		public void setLeccion(BigDecimal leccion) {
			this.leccion = leccion;
		}
		public BigDecimal getDeber() {
			return deber;
		}
		public void setDeber(BigDecimal deber) {
			this.deber = deber;
		}
		public BigDecimal getTaller() {
			return taller;
		}
		public void setTaller(BigDecimal taller) {
			this.taller = taller;
		}
		public BigDecimal getExamen() {
			return examen;
		}
		public void setExamen(BigDecimal examen) {
			this.examen = examen;
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
	public List<Object> getControlList() {
		return controlList;
	}
	public void setControlList(List<Object> controlList) {
		this.controlList = controlList;
	}
	public String getSoOferta() {
		return soOferta;
	}
	public void setSoOferta(String soOferta) {
		this.soOferta = soOferta;
	}
	public String getSoAsignatura() {
		return soAsignatura;
	}
	public void setSoAsignatura(String soAsignatura) {
		this.soAsignatura = soAsignatura;
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
	public ArrayList<SelectItem> getListAsignaturas() {
		return listAsignaturas;
	}
	public void setListAsignaturas(ArrayList<SelectItem> listAsignaturas) {
		this.listAsignaturas = listAsignaturas;
	}
}