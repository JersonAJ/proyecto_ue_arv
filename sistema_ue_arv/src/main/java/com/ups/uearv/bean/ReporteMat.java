/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import com.ups.uearv.entidades.MatEstudiante;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporteMat")
@ViewScoped
public class ReporteMat implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	private static final long serialVersionUID = 1L;

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";
	String jpql = "";

	String soPeriodo = "";

	String soOferta = "";
	String soEstudiante = "";

	ArrayList<SelectItem> listPeriodo = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOferta = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listEstudiante = new ArrayList<SelectItem>();

	String olGrado = "";
	String olParalelo = "";
	
	// PERSONALES
	String itCedula = "";
	String itNombres = "";
	String itApellidos = "";
	String soRepresentante = "";
	String sorGenero = "";
	String soTipoSangre = "";
	String itLugarNac = "";
	String itNacionalidad = "";	
	Date cFechaNac = new Date();
	String itFechaNac = "";
	String itEdad = "";
	boolean ckDiscapacidad = false;
	String itObservacion = "";	
	String itImagen = "";

	// RESIDENCIA
	String itProvincia = "";
	String itCanton = "";
	String itCiudad = "";
	String itParroquia = "";
	String itTelefono = "";
	String itDireccion = "";
	String itPersonaEmerg = "";
	String itTelefonoEmerg = "";

	// FAMILIARES
	String itPaNombre = "";
	String itPaCedula = "";
	String soPaEstadoCivil = "";
	String opPaEstudios = "";	
	String itPaProfesion = "";
	String opPaOcupacion = "";
	String itPaInstitucion = "";
	String itPaPuesto = "";
	String itPaTelefono = "";
	String itPaCelular = "";
	String itPaCorreo = "";
	String itMaNombre = "";
	String itMaCedula = "";
	String soMaEstadoCivil = "";
	String opMaEstudios = "";	
	String itMaProfesion = "";
	String opMaOcupacion = "";
	String itMaInstitucion = "";
	String itMaPuesto = "";
	String itMaTelefono = "";
	String itMaCelular = "";
	String itMaCorreo = "";
	String snHermanos = "";
	String itHermanos = "";

	// BI-PSICO SOCIALES
	String opViveCon = "";
	String itSituacionFam = "";
	String opRelacionPadres = "";
	String snClasesPart = "";
	String itClasesPart = "";
	String snEstudiosFuera = "";
	String itEstudiosFuera = "";
	String opRendimiento = "";
	String itAsigMas = "";
	String itAsigMenos = "";
	String itReaccionPadres = "";
	String snCumples = "";
	String itCumples = "";

	// MEDICOS	
	String opEnfermedad = "";
	String snAlergiaMed = "";
	String itAlergiaMed = "";
	String snAlergiaIns = "";
	String itAlergiaIns = "";
	String snAlergiaAli = "";
	String itAlergiaAli = "";
	String itAlergOtras = "";
	String snTratamientoAct = "";
	String snTratamientoAnt = "";
	String snHospital = "";
	String itHospital = "";
	String snOperado = "";
	String itOperado = "";
	String snMinusvalia = "";
	String itMinusvalia = "";
	String soGradoMinusvalia = "";	

	@PostConstruct
	public void init() {		
		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";
		onChangePeriodo();

		limpiarFicha();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}

	public void onChangePeriodo() {
		listOferta = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";

		onChangeOferta();
	}

	public void onChangeOferta() {
		listEstudiante = (ArrayList<SelectItem>) llenaComboEstudiante();
		soEstudiante = "NA";
	}

	public void verFichaEstudiante() {	
		limpiarFicha();

		if (soPeriodo.equals("NA")) {
			mensaje = "Debe seleccionarel período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soOferta.equals("NA")) {
			mensaje = "Debe seleccionar una oferta";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (soEstudiante.equals("NA")) {
			mensaje = "Debe seleccionar el estudiante";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}

		// DETALLE
		jpql = "from MatEstudiante e where e.idEstudiante = '" + soEstudiante + "'";		
		MatEstudiante e = (MatEstudiante) DAO.buscarObject(new MatEstudiante(), jpql);
		if (e != null) {
			itCedula = e.getIdEstudiante();
			itNombres = e.getNombres();
			itApellidos = e.getApellidos();
			soRepresentante = e.getMatRepresentante().getApellidos().trim() + " " + e.getMatRepresentante().getNombres().trim();
			sorGenero = e.getGenero();
			soTipoSangre = e.getTipoSangre();
			itLugarNac = e.getLugarNacimiento();
			itNacionalidad = e.getNacionalidad();
			cFechaNac = e.getFechaNac();
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(cFechaNac);
			itFechaNac = strDate;
			itEdad = String.valueOf(calculateAge(cFechaNac, new Date()));
			ckDiscapacidad = (e.getSnDiscapacidad().equals("S") ? true : false);
			itObservacion = e.getObservacion();
			itImagen = (e.getFoto() == null ? "/sistema_ue_arv/fotos/FOTO_DEFAULT.png" : e.getFoto());

			itProvincia = e.getProvincia();
			itCanton = e.getCanton();
			itCiudad = e.getCiudad();
			itParroquia = e.getParroquia();
			itTelefono = e.getTelefono();
			itDireccion = e.getDireccion();
			itPersonaEmerg = e.getEmergLlamar();
			itTelefonoEmerg = e.getEmergTelefono();

			itPaNombre = e.getPaNombre();
			itPaCedula = e.getPaCedula();
			soPaEstadoCivil = e.getPaEstadoCivil();
			opPaEstudios = e.getPaEstudios();
			itPaProfesion = e.getPaProfesion();
			opPaOcupacion = e.getPaOcupacion();
			itPaInstitucion = e.getPaInstitucion();
			itPaPuesto = e.getPaPuesto();
			itPaTelefono = e.getPaTelefono();
			itPaCelular = e.getPaCelular();
			itPaCorreo = e.getPaCorreo();
			itMaNombre = e.getMaNombre();
			itMaCedula = e.getMaCedula();
			soMaEstadoCivil = e.getMaEstadoCivil();
			opMaEstudios = e.getMaEstudios();
			itMaProfesion = e.getMaProfesion();
			opMaOcupacion = e.getMaOcupacion();
			itMaInstitucion = e.getMaInstitucion();
			itMaPuesto = e.getMaPuesto();
			itMaTelefono = e.getMaTelefono();
			itMaCelular = e.getMaCelular();
			itMaCorreo = e.getMaCorreo();
			snHermanos = e.getSnHermanos();
			itHermanos = e.getDsHermanos();

			opViveCon = e.getViveCon();
			itSituacionFam = e.getSituacionFamiliar();
			opRelacionPadres = e.getRelacionPadres();
			snClasesPart = e.getSnClasesPart();
			itClasesPart = e.getDsClasesPart();
			snEstudiosFuera = e.getSnEstudiosFuera();
			itEstudiosFuera = e.getDsEstudiosFuera();
			opRendimiento = e.getRendimiento();
			itAsigMas = e.getAsigMas();
			itAsigMenos = e.getAsigMenos();
			itReaccionPadres = e.getReaccionPadres();
			snCumples = e.getSnCumples();
			itCumples = e.getDsCumples();

			opEnfermedad = e.getEnfermedad();
			snAlergiaMed = e.getSnAlergiaMed();
			itAlergiaMed = e.getDsAlergiaMed();
			snAlergiaIns = e.getSnAlergiaIns();
			itAlergiaIns = e.getDsAlergiaIns();
			snAlergiaAli = e.getSnAlergiaAli();
			itAlergiaAli = e.getDsAlergiaAli();
			itAlergOtras = e.getAlergOtras();
			snTratamientoAct = e.getSnTratamiento();
			snTratamientoAnt = e.getSnTratRecibido();
			snHospital = e.getSnHospital();
			itHospital = e.getDsHospital();
			snOperado = e.getSnOperado();
			itOperado = e.getDsOperado();
			snMinusvalia = e.getSnMinusvalia();
			itMinusvalia = e.getDsMinusvalia();
			soGradoMinusvalia = e.getGdMinusvalia();
		} else {
			limpiarFicha();
			mensaje = "No existen datos del estudiante";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, mensajeTitulo, mensaje));
			return;
		}
	}

	public static int calculateAge(Date nac, Date act) {
		LocalDate birthDate = nac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = act.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if ((birthDate != null) && (currentDate != null)) {
			return Period.between(birthDate, currentDate).getYears();
		} else {
			return 0;
		}
	}

	public void limpiarFicha() {		
		olGrado = "_____________";		
		olParalelo = "____";
		
		itCedula = "_____________";
		itNombres = "___________________";
		itApellidos = "___________________";
		soRepresentante = "______________________________________";
		itImagen = "/sistema_ue_arv/fotos/FOTO_DEFAULT.png";

		itLugarNac = "_________________";
		itNacionalidad = "_________________";
		itFechaNac = "_______________________________";
		itEdad = "_________";
		itObservacion = "____________________________________________________________________________";

		itProvincia = "_________________";
		itCanton = "_________________";
		itCiudad = "_________________";
		itParroquia = "_________________";
		itTelefono = "_____________";
		itDireccion = "______________________________________";
		itPersonaEmerg = "______________________________________";
		itTelefonoEmerg = "_____________";

		itPaNombre = "______________________________________";
		itPaCedula = "_____________";
		itPaProfesion = "_________________";
		itPaInstitucion = "______________________________________";
		itPaPuesto = "_________________";
		itPaTelefono = "_____________";
		itPaCelular = "_____________";
		itPaCorreo = "______________________________________";
		itMaNombre = "______________________________________";
		itMaCedula = "_____________";
		itMaProfesion = "_________________";
		itMaInstitucion = "______________________________________";
		itMaPuesto = "_________________";
		itMaTelefono = "_____________";
		itMaCelular = "_____________";
		itMaCorreo = "______________________________________";
		itHermanos = "______________________________________";

		itSituacionFam = "______________________________________";
		itClasesPart = "______________________________________";
		itEstudiosFuera = "______________________________________";
		itAsigMas = "______________________________________";
		itAsigMenos = "______________________________________";
		itReaccionPadres = "______________________________________";
		itCumples = "______________________________________";

		itAlergiaMed = "______________________________________";
		itAlergiaIns = "______________________________________";
		itAlergiaAli = "______________________________________";
		itAlergOtras = "______________________________________";
		itHospital = "______________________________________";
		itOperado = "______________________________________";
		itMinusvalia = "_________________________";
		soGradoMinusvalia = "_______";

	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
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
						"WHERE m.id_periodo = '" + soPeriodo + "' \r\n"+
						"AND m.id_oferta = '" + soOferta + "' \r\n"+
						"AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
						"ORDER BY 2";

		Query query = em.createNativeQuery(jpql);
		return query;
	}

	// SUBCLASES
	public class LibretaCal implements Serializable {

		private static final long serialVersionUID = 1L;

		public String idAsignatura;
		public String asignatura;
		public BigDecimal tarea;
		public BigDecimal actIndividual;
		public BigDecimal actGrupal;
		public BigDecimal leccion;
		public BigDecimal evaluacion;
		public BigDecimal promedio;
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
		public BigDecimal getTarea() {
			return tarea;
		}
		public void setTarea(BigDecimal tarea) {
			this.tarea = tarea;
		}
		public BigDecimal getActIndividual() {
			return actIndividual;
		}
		public void setActIndividual(BigDecimal actIndividual) {
			this.actIndividual = actIndividual;
		}
		public BigDecimal getActGrupal() {
			return actGrupal;
		}
		public void setActGrupal(BigDecimal actGrupal) {
			this.actGrupal = actGrupal;
		}
		public BigDecimal getLeccion() {
			return leccion;
		}
		public void setLeccion(BigDecimal leccion) {
			this.leccion = leccion;
		}
		public BigDecimal getEvaluacion() {
			return evaluacion;
		}
		public void setEvaluacion(BigDecimal evaluacion) {
			this.evaluacion = evaluacion;
		}
		public BigDecimal getPromedio() {
			return promedio;
		}
		public void setPromedio(BigDecimal promedio) {
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
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public ArrayList<SelectItem> getListPeriodo() {
		return listPeriodo;
	}
	public void setListPeriodo(ArrayList<SelectItem> listPeriodo) {
		this.listPeriodo = listPeriodo;
	}
	public ArrayList<SelectItem> getListOferta() {
		return listOferta;
	}
	public void setListOferta(ArrayList<SelectItem> listOferta) {
		this.listOferta = listOferta;
	}
	public ArrayList<SelectItem> getListEstudiante() {
		return listEstudiante;
	}
	public void setListEstudiante(ArrayList<SelectItem> listEstudiante) {
		this.listEstudiante = listEstudiante;
	}
	public String getItCedula() {
		return itCedula;
	}
	public void setItCedula(String itCedula) {
		this.itCedula = itCedula;
	}
	public String getItNombres() {
		return itNombres;
	}
	public void setItNombres(String itNombres) {
		this.itNombres = itNombres;
	}
	public String getItApellidos() {
		return itApellidos;
	}
	public void setItApellidos(String itApellidos) {
		this.itApellidos = itApellidos;
	}
	public String getSoRepresentante() {
		return soRepresentante;
	}

	public void setSoRepresentante(String soRepresentante) {
		this.soRepresentante = soRepresentante;
	}
	public String getSorGenero() {
		return sorGenero;
	}
	public void setSorGenero(String sorGenero) {
		this.sorGenero = sorGenero;
	}
	public String getSoTipoSangre() {
		return soTipoSangre;
	}
	public void setSoTipoSangre(String soTipoSangre) {
		this.soTipoSangre = soTipoSangre;
	}
	public String getItLugarNac() {
		return itLugarNac;
	}
	public void setItLugarNac(String itLugarNac) {
		this.itLugarNac = itLugarNac;
	}
	public String getItNacionalidad() {
		return itNacionalidad;
	}
	public void setItNacionalidad(String itNacionalidad) {
		this.itNacionalidad = itNacionalidad;
	}
	public Date getcFechaNac() {
		return cFechaNac;
	}
	public void setcFechaNac(Date cFechaNac) {
		this.cFechaNac = cFechaNac;
	}
	public boolean isCkDiscapacidad() {
		return ckDiscapacidad;
	}
	public void setCkDiscapacidad(boolean ckDiscapacidad) {
		this.ckDiscapacidad = ckDiscapacidad;
	}
	public String getItObservacion() {
		return itObservacion;
	}
	public void setItObservacion(String itObservacion) {
		this.itObservacion = itObservacion;
	}
	public String getItImagen() {
		return itImagen;
	}
	public void setItImagen(String itImagen) {
		this.itImagen = itImagen;
	}
	public String getItProvincia() {
		return itProvincia;
	}
	public void setItProvincia(String itProvincia) {
		this.itProvincia = itProvincia;
	}
	public String getItCanton() {
		return itCanton;
	}
	public void setItCanton(String itCanton) {
		this.itCanton = itCanton;
	}
	public String getItCiudad() {
		return itCiudad;
	}
	public void setItCiudad(String itCiudad) {
		this.itCiudad = itCiudad;
	}
	public String getItParroquia() {
		return itParroquia;
	}
	public void setItParroquia(String itParroquia) {
		this.itParroquia = itParroquia;
	}
	public String getItTelefono() {
		return itTelefono;
	}
	public void setItTelefono(String itTelefono) {
		this.itTelefono = itTelefono;
	}
	public String getItDireccion() {
		return itDireccion;
	}
	public void setItDireccion(String itDireccion) {
		this.itDireccion = itDireccion;
	}
	public String getItPersonaEmerg() {
		return itPersonaEmerg;
	}
	public void setItPersonaEmerg(String itPersonaEmerg) {
		this.itPersonaEmerg = itPersonaEmerg;
	}
	public String getItTelefonoEmerg() {
		return itTelefonoEmerg;
	}
	public void setItTelefonoEmerg(String itTelefonoEmerg) {
		this.itTelefonoEmerg = itTelefonoEmerg;
	}
	public String getItPaNombre() {
		return itPaNombre;
	}
	public void setItPaNombre(String itPaNombre) {
		this.itPaNombre = itPaNombre;
	}
	public String getItPaCedula() {
		return itPaCedula;
	}
	public void setItPaCedula(String itPaCedula) {
		this.itPaCedula = itPaCedula;
	}
	public String getSoPaEstadoCivil() {
		return soPaEstadoCivil;
	}
	public void setSoPaEstadoCivil(String soPaEstadoCivil) {
		this.soPaEstadoCivil = soPaEstadoCivil;
	}
	public String getOpPaEstudios() {
		return opPaEstudios;
	}
	public void setOpPaEstudios(String opPaEstudios) {
		this.opPaEstudios = opPaEstudios;
	}
	public String getItPaProfesion() {
		return itPaProfesion;
	}
	public void setItPaProfesion(String itPaProfesion) {
		this.itPaProfesion = itPaProfesion;
	}
	public String getOpPaOcupacion() {
		return opPaOcupacion;
	}
	public void setOpPaOcupacion(String opPaOcupacion) {
		this.opPaOcupacion = opPaOcupacion;
	}
	public String getItPaInstitucion() {
		return itPaInstitucion;
	}
	public void setItPaInstitucion(String itPaInstitucion) {
		this.itPaInstitucion = itPaInstitucion;
	}
	public String getItPaPuesto() {
		return itPaPuesto;
	}
	public void setItPaPuesto(String itPaPuesto) {
		this.itPaPuesto = itPaPuesto;
	}
	public String getItPaTelefono() {
		return itPaTelefono;
	}
	public void setItPaTelefono(String itPaTelefono) {
		this.itPaTelefono = itPaTelefono;
	}
	public String getItPaCelular() {
		return itPaCelular;
	}
	public void setItPaCelular(String itPaCelular) {
		this.itPaCelular = itPaCelular;
	}
	public String getItPaCorreo() {
		return itPaCorreo;
	}
	public void setItPaCorreo(String itPaCorreo) {
		this.itPaCorreo = itPaCorreo;
	}
	public String getItMaNombre() {
		return itMaNombre;
	}
	public void setItMaNombre(String itMaNombre) {
		this.itMaNombre = itMaNombre;
	}
	public String getItMaCedula() {
		return itMaCedula;
	}
	public void setItMaCedula(String itMaCedula) {
		this.itMaCedula = itMaCedula;
	}
	public String getSoMaEstadoCivil() {
		return soMaEstadoCivil;
	}
	public void setSoMaEstadoCivil(String soMaEstadoCivil) {
		this.soMaEstadoCivil = soMaEstadoCivil;
	}
	public String getOpMaEstudios() {
		return opMaEstudios;
	}
	public void setOpMaEstudios(String opMaEstudios) {
		this.opMaEstudios = opMaEstudios;
	}
	public String getItMaProfesion() {
		return itMaProfesion;
	}
	public void setItMaProfesion(String itMaProfesion) {
		this.itMaProfesion = itMaProfesion;
	}
	public String getOpMaOcupacion() {
		return opMaOcupacion;
	}
	public void setOpMaOcupacion(String opMaOcupacion) {
		this.opMaOcupacion = opMaOcupacion;
	}
	public String getItMaInstitucion() {
		return itMaInstitucion;
	}
	public void setItMaInstitucion(String itMaInstitucion) {
		this.itMaInstitucion = itMaInstitucion;
	}
	public String getItMaPuesto() {
		return itMaPuesto;
	}
	public void setItMaPuesto(String itMaPuesto) {
		this.itMaPuesto = itMaPuesto;
	}
	public String getItMaTelefono() {
		return itMaTelefono;
	}
	public void setItMaTelefono(String itMaTelefono) {
		this.itMaTelefono = itMaTelefono;
	}
	public String getItMaCelular() {
		return itMaCelular;
	}
	public void setItMaCelular(String itMaCelular) {
		this.itMaCelular = itMaCelular;
	}
	public String getItMaCorreo() {
		return itMaCorreo;
	}
	public void setItMaCorreo(String itMaCorreo) {
		this.itMaCorreo = itMaCorreo;
	}
	public String getSnHermanos() {
		return snHermanos;
	}
	public void setSnHermanos(String snHermanos) {
		this.snHermanos = snHermanos;
	}
	public String getItHermanos() {
		return itHermanos;
	}
	public void setItHermanos(String itHermanos) {
		this.itHermanos = itHermanos;
	}
	public String getOpViveCon() {
		return opViveCon;
	}
	public void setOpViveCon(String opViveCon) {
		this.opViveCon = opViveCon;
	}
	public String getItSituacionFam() {
		return itSituacionFam;
	}
	public void setItSituacionFam(String itSituacionFam) {
		this.itSituacionFam = itSituacionFam;
	}
	public String getOpRelacionPadres() {
		return opRelacionPadres;
	}
	public void setOpRelacionPadres(String opRelacionPadres) {
		this.opRelacionPadres = opRelacionPadres;
	}
	public String getSnClasesPart() {
		return snClasesPart;
	}
	public void setSnClasesPart(String snClasesPart) {
		this.snClasesPart = snClasesPart;
	}
	public String getItClasesPart() {
		return itClasesPart;
	}
	public void setItClasesPart(String itClasesPart) {
		this.itClasesPart = itClasesPart;
	}
	public String getSnEstudiosFuera() {
		return snEstudiosFuera;
	}
	public void setSnEstudiosFuera(String snEstudiosFuera) {
		this.snEstudiosFuera = snEstudiosFuera;
	}
	public String getItEstudiosFuera() {
		return itEstudiosFuera;
	}
	public void setItEstudiosFuera(String itEstudiosFuera) {
		this.itEstudiosFuera = itEstudiosFuera;
	}
	public String getOpRendimiento() {
		return opRendimiento;
	}
	public void setOpRendimiento(String opRendimiento) {
		this.opRendimiento = opRendimiento;
	}
	public String getItAsigMas() {
		return itAsigMas;
	}
	public void setItAsigMas(String itAsigMas) {
		this.itAsigMas = itAsigMas;
	}
	public String getItAsigMenos() {
		return itAsigMenos;
	}
	public void setItAsigMenos(String itAsigMenos) {
		this.itAsigMenos = itAsigMenos;
	}
	public String getItReaccionPadres() {
		return itReaccionPadres;
	}
	public void setItReaccionPadres(String itReaccionPadres) {
		this.itReaccionPadres = itReaccionPadres;
	}
	public String getSnCumples() {
		return snCumples;
	}
	public void setSnCumples(String snCumples) {
		this.snCumples = snCumples;
	}
	public String getItCumples() {
		return itCumples;
	}
	public void setItCumples(String itCumples) {
		this.itCumples = itCumples;
	}
	public String getOpEnfermedad() {
		return opEnfermedad;
	}
	public void setOpEnfermedad(String opEnfermedad) {
		this.opEnfermedad = opEnfermedad;
	}
	public String getSnAlergiaMed() {
		return snAlergiaMed;
	}
	public void setSnAlergiaMed(String snAlergiaMed) {
		this.snAlergiaMed = snAlergiaMed;
	}
	public String getItAlergiaMed() {
		return itAlergiaMed;
	}
	public void setItAlergiaMed(String itAlergiaMed) {
		this.itAlergiaMed = itAlergiaMed;
	}
	public String getSnAlergiaIns() {
		return snAlergiaIns;
	}
	public void setSnAlergiaIns(String snAlergiaIns) {
		this.snAlergiaIns = snAlergiaIns;
	}
	public String getItAlergiaIns() {
		return itAlergiaIns;
	}
	public void setItAlergiaIns(String itAlergiaIns) {
		this.itAlergiaIns = itAlergiaIns;
	}
	public String getSnAlergiaAli() {
		return snAlergiaAli;
	}
	public void setSnAlergiaAli(String snAlergiaAli) {
		this.snAlergiaAli = snAlergiaAli;
	}
	public String getItAlergiaAli() {
		return itAlergiaAli;
	}
	public void setItAlergiaAli(String itAlergiaAli) {
		this.itAlergiaAli = itAlergiaAli;
	}
	public String getItAlergOtras() {
		return itAlergOtras;
	}
	public void setItAlergOtras(String itAlergOtras) {
		this.itAlergOtras = itAlergOtras;
	}
	public String getSnTratamientoAct() {
		return snTratamientoAct;
	}
	public void setSnTratamientoAct(String snTratamientoAct) {
		this.snTratamientoAct = snTratamientoAct;
	}
	public String getSnTratamientoAnt() {
		return snTratamientoAnt;
	}
	public void setSnTratamientoAnt(String snTratamientoAnt) {
		this.snTratamientoAnt = snTratamientoAnt;
	}
	public String getSnHospital() {
		return snHospital;
	}
	public void setSnHospital(String snHospital) {
		this.snHospital = snHospital;
	}
	public String getItHospital() {
		return itHospital;
	}
	public void setItHospital(String itHospital) {
		this.itHospital = itHospital;
	}
	public String getSnOperado() {
		return snOperado;
	}
	public void setSnOperado(String snOperado) {
		this.snOperado = snOperado;
	}
	public String getItOperado() {
		return itOperado;
	}
	public void setItOperado(String itOperado) {
		this.itOperado = itOperado;
	}
	public String getSnMinusvalia() {
		return snMinusvalia;
	}
	public void setSnMinusvalia(String snMinusvalia) {
		this.snMinusvalia = snMinusvalia;
	}
	public String getItMinusvalia() {
		return itMinusvalia;
	}
	public void setItMinusvalia(String itMinusvalia) {
		this.itMinusvalia = itMinusvalia;
	}
	public String getSoGradoMinusvalia() {
		return soGradoMinusvalia;
	}
	public void setSoGradoMinusvalia(String soGradoMinusvalia) {
		this.soGradoMinusvalia = soGradoMinusvalia;
	}
	public String getItEdad() {
		return itEdad;
	}
	public void setItEdad(String itEdad) {
		this.itEdad = itEdad;
	}
	public String getItFechaNac() {
		return itFechaNac;
	}
	public void setItFechaNac(String itFechaNac) {
		this.itFechaNac = itFechaNac;
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
}