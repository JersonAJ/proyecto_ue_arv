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
import com.ups.uearv.entidades.MatOferta;
import com.ups.uearv.entidades.MatPeriodo;
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
	private List<Object> procesaNotasList = new ArrayList<Object>();

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
		procesaNotasList.clear();
		
		if (!soPeriodo.equals("NA")) {
			if (!soOferta.equals("NA")) {
				if (!soAsignatura.equals("NA")) {

					jpql = "CALL consulta_control_academico (" + soPeriodo + "," + soOferta + "," + soAsignatura + "," + soQuimestre + "," + soParcial + ")";

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

					jpql = "CALL consulta_control_consolidado (" + soPeriodo + "," + soOferta + "," + soAsignatura  + ")";

					@SuppressWarnings("unchecked")
					List<Object> resultNotas = em.createNativeQuery(jpql).getResultList();
					Iterator<Object> itrNotas = resultNotas.iterator();
					for (int k = 0; k < resultNotas.size(); k++) {
						Object[] obj = (Object[]) itrNotas.next();

						Notas e = new Notas();
						e.setCodMatricula(obj[0].toString());
						e.setNomEstudiante(obj[1].toString());
						e.setLeccionQ1P1(new BigDecimal(obj[2].toString()));
						e.setDeberQ1P1(new BigDecimal(obj[3].toString()));
						e.setTallerQ1P1(new BigDecimal(obj[4].toString()));
						e.setLeccionQ1P2(new BigDecimal(obj[5].toString()));
						e.setDeberQ1P2(new BigDecimal(obj[6].toString()));
						e.setTallerQ1P2(new BigDecimal(obj[7].toString()));
						e.setLeccionQ1P3(new BigDecimal(obj[8].toString()));
						e.setDeberQ1P3(new BigDecimal(obj[9].toString()));
						e.setTallerQ1P3(new BigDecimal(obj[10].toString()));
						e.setExamenQ1(new BigDecimal(obj[11].toString()));
						e.setLeccionQ2P1(new BigDecimal(obj[12].toString()));
						e.setDeberQ2P1(new BigDecimal(obj[13].toString()));
						e.setTallerQ2P1(new BigDecimal(obj[14].toString()));
						e.setLeccionQ2P2(new BigDecimal(obj[15].toString()));
						e.setDeberQ2P2(new BigDecimal(obj[16].toString()));
						e.setTallerQ2P2(new BigDecimal(obj[17].toString()));
						e.setLeccionQ2P3(new BigDecimal(obj[18].toString()));
						e.setDeberQ2P3(new BigDecimal(obj[19].toString()));
						e.setTallerQ2P3(new BigDecimal(obj[20].toString()));
						e.setExamenQ2(new BigDecimal(obj[21].toString()));
						procesaNotasList.add(e);
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
	
	public String getDesPeriodo(String cod) {
		if (!cod.equals("NA")) {
			MatPeriodo ob = new MatPeriodo();
			ob = (MatPeriodo) DAO.buscarObject(new MatPeriodo(), "from MatPeriodo c where c.idPeriodo = '" + cod + "'");
			if (ob == null)
				return "--";
			else
				return ob.getDescripcion().trim();
		} else
			return "--";
	}
	
	public String getDesOferta(String cod) {
		if (!cod.equals("NA")) {
			MatOferta ob = new MatOferta();
			ob = (MatOferta) DAO.buscarObject(new MatOferta(), "from MatOferta c where c.idOferta = '" + cod + "'");
			if (ob == null)
				return "--";
			else
				return ob.getDescripcion().trim();
		} else
			return "--";
	}
	
	public String getDesAsignatura(String cod) {
		if (!cod.equals("NA")) {
			CalAsignatura ob = new CalAsignatura();
			ob = (CalAsignatura) DAO.buscarObject(new CalAsignatura(), "from CalAsignatura c where c.idAsignatura = '" + cod + "'");
			if (ob == null)
				return "--";
			else
				return ob.getNombre().trim();
		} else
			return "--";
	}

	// ACCIONES
	public void procesaNotas() {
		
	}
	
	// CLASES
	public class ControlEst implements Serializable {

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

	public class Notas implements Serializable {

		private static final long serialVersionUID = 1L;

		public String codMatricula = "";
		public String nomEstudiante = "";				
		public BigDecimal leccionQ1P1;
		public BigDecimal deberQ1P1;
		public BigDecimal tallerQ1P1;		
		public BigDecimal leccionQ1P2;
		public BigDecimal deberQ1P2;
		public BigDecimal tallerQ1P2;		
		public BigDecimal leccionQ1P3;
		public BigDecimal deberQ1P3;
		public BigDecimal tallerQ1P3;
		public BigDecimal examenQ1;
		public BigDecimal leccionQ2P1;
		public BigDecimal deberQ2P1;
		public BigDecimal tallerQ2P1;		
		public BigDecimal leccionQ2P2;
		public BigDecimal deberQ2P2;
		public BigDecimal tallerQ2P2;		
		public BigDecimal leccionQ2P3;
		public BigDecimal deberQ2P3;
		public BigDecimal tallerQ2P3;
		public BigDecimal examenQ2;
		
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
		public BigDecimal getLeccionQ1P1() {
			return leccionQ1P1;
		}
		public void setLeccionQ1P1(BigDecimal leccionQ1P1) {
			this.leccionQ1P1 = leccionQ1P1;
		}
		public BigDecimal getDeberQ1P1() {
			return deberQ1P1;
		}
		public void setDeberQ1P1(BigDecimal deberQ1P1) {
			this.deberQ1P1 = deberQ1P1;
		}
		public BigDecimal getTallerQ1P1() {
			return tallerQ1P1;
		}
		public void setTallerQ1P1(BigDecimal tallerQ1P1) {
			this.tallerQ1P1 = tallerQ1P1;
		}
		public BigDecimal getLeccionQ1P2() {
			return leccionQ1P2;
		}
		public void setLeccionQ1P2(BigDecimal leccionQ1P2) {
			this.leccionQ1P2 = leccionQ1P2;
		}
		public BigDecimal getDeberQ1P2() {
			return deberQ1P2;
		}
		public void setDeberQ1P2(BigDecimal deberQ1P2) {
			this.deberQ1P2 = deberQ1P2;
		}
		public BigDecimal getTallerQ1P2() {
			return tallerQ1P2;
		}
		public void setTallerQ1P2(BigDecimal tallerQ1P2) {
			this.tallerQ1P2 = tallerQ1P2;
		}
		public BigDecimal getLeccionQ1P3() {
			return leccionQ1P3;
		}
		public void setLeccionQ1P3(BigDecimal leccionQ1P3) {
			this.leccionQ1P3 = leccionQ1P3;
		}
		public BigDecimal getDeberQ1P3() {
			return deberQ1P3;
		}
		public void setDeberQ1P3(BigDecimal deberQ1P3) {
			this.deberQ1P3 = deberQ1P3;
		}
		public BigDecimal getTallerQ1P3() {
			return tallerQ1P3;
		}
		public void setTallerQ1P3(BigDecimal tallerQ1P3) {
			this.tallerQ1P3 = tallerQ1P3;
		}
		public BigDecimal getExamenQ1() {
			return examenQ1;
		}
		public void setExamenQ1(BigDecimal examenQ1) {
			this.examenQ1 = examenQ1;
		}
		public BigDecimal getLeccionQ2P1() {
			return leccionQ2P1;
		}
		public void setLeccionQ2P1(BigDecimal leccionQ2P1) {
			this.leccionQ2P1 = leccionQ2P1;
		}
		public BigDecimal getDeberQ2P1() {
			return deberQ2P1;
		}
		public void setDeberQ2P1(BigDecimal deberQ2P1) {
			this.deberQ2P1 = deberQ2P1;
		}
		public BigDecimal getTallerQ2P1() {
			return tallerQ2P1;
		}
		public void setTallerQ2P1(BigDecimal tallerQ2P1) {
			this.tallerQ2P1 = tallerQ2P1;
		}
		public BigDecimal getLeccionQ2P2() {
			return leccionQ2P2;
		}
		public void setLeccionQ2P2(BigDecimal leccionQ2P2) {
			this.leccionQ2P2 = leccionQ2P2;
		}
		public BigDecimal getDeberQ2P2() {
			return deberQ2P2;
		}
		public void setDeberQ2P2(BigDecimal deberQ2P2) {
			this.deberQ2P2 = deberQ2P2;
		}
		public BigDecimal getTallerQ2P2() {
			return tallerQ2P2;
		}
		public void setTallerQ2P2(BigDecimal tallerQ2P2) {
			this.tallerQ2P2 = tallerQ2P2;
		}
		public BigDecimal getLeccionQ2P3() {
			return leccionQ2P3;
		}
		public void setLeccionQ2P3(BigDecimal leccionQ2P3) {
			this.leccionQ2P3 = leccionQ2P3;
		}
		public BigDecimal getDeberQ2P3() {
			return deberQ2P3;
		}
		public void setDeberQ2P3(BigDecimal deberQ2P3) {
			this.deberQ2P3 = deberQ2P3;
		}
		public BigDecimal getTallerQ2P3() {
			return tallerQ2P3;
		}
		public void setTallerQ2P3(BigDecimal tallerQ2P3) {
			this.tallerQ2P3 = tallerQ2P3;
		}
		public BigDecimal getExamenQ2() {
			return examenQ2;
		}
		public void setExamenQ2(BigDecimal examenQ2) {
			this.examenQ2 = examenQ2;
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
	public List<Object> getProcesaNotasList() {
		return procesaNotasList;
	}
	public void setProcesaNotasList(List<Object> procesaNotasList) {
		this.procesaNotasList = procesaNotasList;
	}	
}