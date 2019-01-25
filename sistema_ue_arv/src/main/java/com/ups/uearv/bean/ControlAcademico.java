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
				
				BigDecimal leccion1 = ((ControlEst) asset).getLeccion1();
				BigDecimal leccion2 = ((ControlEst) asset).getLeccion2();
				BigDecimal leccion3 = ((ControlEst) asset).getLeccion3();
				BigDecimal leccion4 = ((ControlEst) asset).getLeccion4();
				BigDecimal leccion5 = ((ControlEst) asset).getLeccion5();
				
				BigDecimal tarea1 = ((ControlEst) asset).getTarea1();
				BigDecimal tarea2 = ((ControlEst) asset).getTarea2();
				BigDecimal tarea3 = ((ControlEst) asset).getTarea3();
				BigDecimal tarea4 = ((ControlEst) asset).getTarea4();
				BigDecimal tarea5 = ((ControlEst) asset).getTarea5();
				
				BigDecimal actIndividual1 = ((ControlEst) asset).getActIndividual1();
				BigDecimal actIndividual2 = ((ControlEst) asset).getActIndividual2();
				BigDecimal actIndividual3 = ((ControlEst) asset).getActIndividual3();
				BigDecimal actIndividual4 = ((ControlEst) asset).getActIndividual4();
				BigDecimal actIndividual5 = ((ControlEst) asset).getActIndividual5();
				
				BigDecimal actGrupal1 = ((ControlEst) asset).getActGrupal1();
				BigDecimal actGrupal2 = ((ControlEst) asset).getActGrupal2();
				BigDecimal actGrupal3 = ((ControlEst) asset).getActGrupal3();
				BigDecimal actGrupal4 = ((ControlEst) asset).getActGrupal4();
				BigDecimal actGrupal5 = ((ControlEst) asset).getActGrupal5();
				
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
					
					con.setLeccion1(leccion1);
					con.setLeccion2(leccion2);
					con.setLeccion3(leccion3);
					con.setLeccion4(leccion4);
					con.setLeccion5(leccion5);

					con.setTarea1(tarea1);
					con.setTarea2(tarea2);
					con.setTarea3(tarea3);
					con.setTarea4(tarea4);
					con.setTarea5(tarea5);
					
					con.setActIndividual1(actIndividual1);
					con.setActIndividual2(actIndividual2);
					con.setActIndividual3(actIndividual3);
					con.setActIndividual4(actIndividual4);
					con.setActIndividual5(actIndividual5);
					
					con.setActGrupal1(actGrupal1);
					con.setActGrupal2(actGrupal2);
					con.setActGrupal3(actGrupal3);
					con.setActGrupal4(actGrupal4);
					con.setActGrupal5(actGrupal5);										
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
						
						e.setLeccion1(new BigDecimal(obj[4].toString()));
						e.setLeccion2(new BigDecimal(obj[5].toString()));
						e.setLeccion3(new BigDecimal(obj[6].toString()));
						e.setLeccion4(new BigDecimal(obj[7].toString()));
						e.setLeccion5(new BigDecimal(obj[8].toString()));
						
						e.setTarea1(new BigDecimal(obj[9].toString()));
						e.setTarea2(new BigDecimal(obj[10].toString()));
						e.setTarea3(new BigDecimal(obj[11].toString()));
						e.setTarea4(new BigDecimal(obj[12].toString()));
						e.setTarea5(new BigDecimal(obj[13].toString()));
						
						e.setActIndividual1(new BigDecimal(obj[14].toString()));
						e.setActIndividual2(new BigDecimal(obj[15].toString()));
						e.setActIndividual3(new BigDecimal(obj[16].toString()));
						e.setActIndividual4(new BigDecimal(obj[17].toString()));
						e.setActIndividual5(new BigDecimal(obj[18].toString()));
												
						e.setActGrupal1(new BigDecimal(obj[19].toString()));
						e.setActGrupal2(new BigDecimal(obj[20].toString()));
						e.setActGrupal3(new BigDecimal(obj[21].toString()));
						e.setActGrupal4(new BigDecimal(obj[22].toString()));
						e.setActGrupal5(new BigDecimal(obj[23].toString()));
												
						e.setExamen(new BigDecimal(obj[24].toString()));

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
						e.setTareaQ1P1(new BigDecimal(obj[3].toString()));
						e.setActIndividualQ1P1(new BigDecimal(obj[4].toString()));
						e.setActGrupalQ1P1(new BigDecimal(obj[5].toString()));
						
						e.setLeccionQ1P2(new BigDecimal(obj[6].toString()));
						e.setTareaQ1P2(new BigDecimal(obj[7].toString()));
						e.setActIndividualQ1P2(new BigDecimal(obj[8].toString()));
						e.setActGrupalQ1P2(new BigDecimal(obj[9].toString()));
						
						e.setLeccionQ1P3(new BigDecimal(obj[10].toString()));
						e.setTareaQ1P3(new BigDecimal(obj[11].toString()));
						e.setActIndividualQ1P3(new BigDecimal(obj[12].toString()));
						e.setActGrupalQ1P3(new BigDecimal(obj[13].toString()));
						e.setExamenQ1(new BigDecimal(obj[14].toString()));
						
						e.setLeccionQ2P1(new BigDecimal(obj[15].toString()));
						e.setTareaQ2P1(new BigDecimal(obj[16].toString()));
						e.setActIndividualQ2P1(new BigDecimal(obj[17].toString()));
						e.setActGrupalQ2P1(new BigDecimal(obj[18].toString()));
						
						e.setLeccionQ2P2(new BigDecimal(obj[19].toString()));
						e.setTareaQ2P2(new BigDecimal(obj[20].toString()));
						e.setActIndividualQ2P2(new BigDecimal(obj[21].toString()));
						e.setActGrupalQ2P2(new BigDecimal(obj[22].toString()));
						
						e.setLeccionQ2P3(new BigDecimal(obj[23].toString()));
						e.setTareaQ2P3(new BigDecimal(obj[24].toString()));
						e.setActIndividualQ2P3(new BigDecimal(obj[25].toString()));
						e.setActGrupalQ2P3(new BigDecimal(obj[26].toString()));
						e.setExamenQ2(new BigDecimal(obj[27].toString()));
						
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
		
	// CLASES
	public class ControlEst implements Serializable {

		private static final long serialVersionUID = 1L;

		public String codControl = "";
		public String codMatricula = "";
		public String nomEstudiante = "";
		public String nomComportamiento = "";		
		public BigDecimal leccion1;
		public BigDecimal leccion2;
		public BigDecimal leccion3;
		public BigDecimal leccion4;
		public BigDecimal leccion5;
		public BigDecimal tarea1;
		public BigDecimal tarea2;
		public BigDecimal tarea3;
		public BigDecimal tarea4;
		public BigDecimal tarea5;
		public BigDecimal actIndividual1;
		public BigDecimal actIndividual2;
		public BigDecimal actIndividual3;
		public BigDecimal actIndividual4;
		public BigDecimal actIndividual5;
		public BigDecimal actGrupal1;
		public BigDecimal actGrupal2;
		public BigDecimal actGrupal3;
		public BigDecimal actGrupal4;
		public BigDecimal actGrupal5;
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
		public BigDecimal getLeccion1() {
			return leccion1;
		}
		public void setLeccion1(BigDecimal leccion1) {
			this.leccion1 = leccion1;
		}
		public BigDecimal getLeccion2() {
			return leccion2;
		}
		public void setLeccion2(BigDecimal leccion2) {
			this.leccion2 = leccion2;
		}
		public BigDecimal getLeccion3() {
			return leccion3;
		}
		public void setLeccion3(BigDecimal leccion3) {
			this.leccion3 = leccion3;
		}
		public BigDecimal getLeccion4() {
			return leccion4;
		}
		public void setLeccion4(BigDecimal leccion4) {
			this.leccion4 = leccion4;
		}
		public BigDecimal getLeccion5() {
			return leccion5;
		}
		public void setLeccion5(BigDecimal leccion5) {
			this.leccion5 = leccion5;
		}
		public BigDecimal getTarea1() {
			return tarea1;
		}
		public void setTarea1(BigDecimal tarea1) {
			this.tarea1 = tarea1;
		}
		public BigDecimal getTarea2() {
			return tarea2;
		}
		public void setTarea2(BigDecimal tarea2) {
			this.tarea2 = tarea2;
		}
		public BigDecimal getTarea3() {
			return tarea3;
		}
		public void setTarea3(BigDecimal tarea3) {
			this.tarea3 = tarea3;
		}
		public BigDecimal getTarea4() {
			return tarea4;
		}
		public void setTarea4(BigDecimal tarea4) {
			this.tarea4 = tarea4;
		}
		public BigDecimal getTarea5() {
			return tarea5;
		}
		public void setTarea5(BigDecimal tarea5) {
			this.tarea5 = tarea5;
		}
		public BigDecimal getActIndividual1() {
			return actIndividual1;
		}
		public void setActIndividual1(BigDecimal actIndividual1) {
			this.actIndividual1 = actIndividual1;
		}
		public BigDecimal getActIndividual2() {
			return actIndividual2;
		}
		public void setActIndividual2(BigDecimal actIndividual2) {
			this.actIndividual2 = actIndividual2;
		}
		public BigDecimal getActIndividual3() {
			return actIndividual3;
		}
		public void setActIndividual3(BigDecimal actIndividual3) {
			this.actIndividual3 = actIndividual3;
		}
		public BigDecimal getActIndividual4() {
			return actIndividual4;
		}
		public void setActIndividual4(BigDecimal actIndividual4) {
			this.actIndividual4 = actIndividual4;
		}
		public BigDecimal getActIndividual5() {
			return actIndividual5;
		}
		public void setActIndividual5(BigDecimal actIndividual5) {
			this.actIndividual5 = actIndividual5;
		}
		public BigDecimal getActGrupal1() {
			return actGrupal1;
		}
		public void setActGrupal1(BigDecimal actGrupal1) {
			this.actGrupal1 = actGrupal1;
		}
		public BigDecimal getActGrupal2() {
			return actGrupal2;
		}
		public void setActGrupal2(BigDecimal actGrupal2) {
			this.actGrupal2 = actGrupal2;
		}
		public BigDecimal getActGrupal3() {
			return actGrupal3;
		}
		public void setActGrupal3(BigDecimal actGrupal3) {
			this.actGrupal3 = actGrupal3;
		}
		public BigDecimal getActGrupal4() {
			return actGrupal4;
		}
		public void setActGrupal4(BigDecimal actGrupal4) {
			this.actGrupal4 = actGrupal4;
		}
		public BigDecimal getActGrupal5() {
			return actGrupal5;
		}
		public void setActGrupal5(BigDecimal actGrupal5) {
			this.actGrupal5 = actGrupal5;
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
		public BigDecimal tareaQ1P1;
		public BigDecimal actIndividualQ1P1;		
		public BigDecimal actGrupalQ1P1;		
		public BigDecimal leccionQ1P2;		
		public BigDecimal tareaQ1P2;
		public BigDecimal actIndividualQ1P2;	
		public BigDecimal actGrupalQ1P2;		
		public BigDecimal leccionQ1P3;
		public BigDecimal tareaQ1P3;
		public BigDecimal actIndividualQ1P3;
		public BigDecimal actGrupalQ1P3;
		public BigDecimal examenQ1;		
		public BigDecimal leccionQ2P1;
		public BigDecimal tareaQ2P1;
		public BigDecimal actIndividualQ2P1;
		public BigDecimal actGrupalQ2P1;		
		public BigDecimal leccionQ2P2;
		public BigDecimal tareaQ2P2;
		public BigDecimal actIndividualQ2P2;	
		public BigDecimal actGrupalQ2P2;		
		public BigDecimal leccionQ2P3;
		public BigDecimal tareaQ2P3;
		public BigDecimal actIndividualQ2P3;
		public BigDecimal actGrupalQ2P3;
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
		public BigDecimal getTareaQ1P1() {
			return tareaQ1P1;
		}
		public void setTareaQ1P1(BigDecimal tareaQ1P1) {
			this.tareaQ1P1 = tareaQ1P1;
		}
		public BigDecimal getActIndividualQ1P1() {
			return actIndividualQ1P1;
		}
		public void setActIndividualQ1P1(BigDecimal actIndividualQ1P1) {
			this.actIndividualQ1P1 = actIndividualQ1P1;
		}
		public BigDecimal getActGrupalQ1P1() {
			return actGrupalQ1P1;
		}
		public void setActGrupalQ1P1(BigDecimal actGrupalQ1P1) {
			this.actGrupalQ1P1 = actGrupalQ1P1;
		}
		public BigDecimal getLeccionQ1P2() {
			return leccionQ1P2;
		}
		public void setLeccionQ1P2(BigDecimal leccionQ1P2) {
			this.leccionQ1P2 = leccionQ1P2;
		}
		public BigDecimal getTareaQ1P2() {
			return tareaQ1P2;
		}
		public void setTareaQ1P2(BigDecimal tareaQ1P2) {
			this.tareaQ1P2 = tareaQ1P2;
		}
		public BigDecimal getActIndividualQ1P2() {
			return actIndividualQ1P2;
		}
		public void setActIndividualQ1P2(BigDecimal actIndividualQ1P2) {
			this.actIndividualQ1P2 = actIndividualQ1P2;
		}
		public BigDecimal getActGrupalQ1P2() {
			return actGrupalQ1P2;
		}
		public void setActGrupalQ1P2(BigDecimal actGrupalQ1P2) {
			this.actGrupalQ1P2 = actGrupalQ1P2;
		}
		public BigDecimal getLeccionQ1P3() {
			return leccionQ1P3;
		}
		public void setLeccionQ1P3(BigDecimal leccionQ1P3) {
			this.leccionQ1P3 = leccionQ1P3;
		}
		public BigDecimal getTareaQ1P3() {
			return tareaQ1P3;
		}
		public void setTareaQ1P3(BigDecimal tareaQ1P3) {
			this.tareaQ1P3 = tareaQ1P3;
		}
		public BigDecimal getActIndividualQ1P3() {
			return actIndividualQ1P3;
		}
		public void setActIndividualQ1P3(BigDecimal actIndividualQ1P3) {
			this.actIndividualQ1P3 = actIndividualQ1P3;
		}
		public BigDecimal getActGrupalQ1P3() {
			return actGrupalQ1P3;
		}
		public void setActGrupalQ1P3(BigDecimal actGrupalQ1P3) {
			this.actGrupalQ1P3 = actGrupalQ1P3;
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
		public BigDecimal getTareaQ2P1() {
			return tareaQ2P1;
		}
		public void setTareaQ2P1(BigDecimal tareaQ2P1) {
			this.tareaQ2P1 = tareaQ2P1;
		}
		public BigDecimal getActIndividualQ2P1() {
			return actIndividualQ2P1;
		}
		public void setActIndividualQ2P1(BigDecimal actIndividualQ2P1) {
			this.actIndividualQ2P1 = actIndividualQ2P1;
		}
		public BigDecimal getActGrupalQ2P1() {
			return actGrupalQ2P1;
		}
		public void setActGrupalQ2P1(BigDecimal actGrupalQ2P1) {
			this.actGrupalQ2P1 = actGrupalQ2P1;
		}
		public BigDecimal getLeccionQ2P2() {
			return leccionQ2P2;
		}
		public void setLeccionQ2P2(BigDecimal leccionQ2P2) {
			this.leccionQ2P2 = leccionQ2P2;
		}
		public BigDecimal getTareaQ2P2() {
			return tareaQ2P2;
		}
		public void setTareaQ2P2(BigDecimal tareaQ2P2) {
			this.tareaQ2P2 = tareaQ2P2;
		}
		public BigDecimal getActIndividualQ2P2() {
			return actIndividualQ2P2;
		}
		public void setActIndividualQ2P2(BigDecimal actIndividualQ2P2) {
			this.actIndividualQ2P2 = actIndividualQ2P2;
		}
		public BigDecimal getActGrupalQ2P2() {
			return actGrupalQ2P2;
		}
		public void setActGrupalQ2P2(BigDecimal actGrupalQ2P2) {
			this.actGrupalQ2P2 = actGrupalQ2P2;
		}
		public BigDecimal getLeccionQ2P3() {
			return leccionQ2P3;
		}
		public void setLeccionQ2P3(BigDecimal leccionQ2P3) {
			this.leccionQ2P3 = leccionQ2P3;
		}
		public BigDecimal getTareaQ2P3() {
			return tareaQ2P3;
		}
		public void setTareaQ2P3(BigDecimal tareaQ2P3) {
			this.tareaQ2P3 = tareaQ2P3;
		}
		public BigDecimal getActIndividualQ2P3() {
			return actIndividualQ2P3;
		}
		public void setActIndividualQ2P3(BigDecimal actIndividualQ2P3) {
			this.actIndividualQ2P3 = actIndividualQ2P3;
		}
		public BigDecimal getActGrupalQ2P3() {
			return actGrupalQ2P3;
		}
		public void setActGrupalQ2P3(BigDecimal actGrupalQ2P3) {
			this.actGrupalQ2P3 = actGrupalQ2P3;
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