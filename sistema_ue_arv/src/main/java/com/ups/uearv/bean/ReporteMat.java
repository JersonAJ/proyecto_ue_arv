/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import org.primefaces.model.chart.PieChartModel;

import com.ups.uearv.entidades.MatEstudiante;
import com.ups.uearv.entidades.MatMatricula;
import com.ups.uearv.entidades.MatOferta;
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
	
	// LISTADO
	private List<Object> matriculaList = new ArrayList<Object>();
	String olCantAC = "0";
	String olCantIC = "0";	
	
	// GRAFICA	
	PieChartModel porPeriodo = new PieChartModel();
	
	@PostConstruct
	public void init() {		
		listPeriodo = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodo = "NA";
		onChangePeriodo();

		limpiarFicha();
		limpiarGrafica();		
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
	
	public void listarMatriculas() {
		matriculaList.clear();
		
		if (soPeriodo.equals("NA")) {
			mensaje = "Debe seleccionarel período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		
		if(!soPeriodo.equals("NA")) {
			jpql = 
			"SELECT m.* \r\n" + 
			"FROM mat_matricula m \r\n" + 
			"	INNER JOIN mat_estudiante e ON e.id_estudiante = m.id_estudiante \r\n" +
			"WHERE m.id_periodo = " + soPeriodo + " AND m.sn_aprobado = 'S' ORDER BY m.id_oferta, m.estado, e.apellidos ";
		
			List<Object> l = DAO.nqObject(new MatMatricula(), jpql);
			if (!l.isEmpty()) {
				for (Object in : l)
					matriculaList.add(in);	
			}	
			
			jpql = 
			"SELECT * FROM mat_matricula \r\n" + 
			"WHERE id_periodo = " + soPeriodo + " AND sn_aprobado = 'S' AND estado = 'AC' ";
			List<Object> l_AC = DAO.nqObject(new MatMatricula(), jpql);
			olCantAC = String.valueOf(l_AC.size());
			
			jpql = 
			"SELECT * FROM mat_matricula \r\n" + 
			"WHERE id_periodo = " + soPeriodo + " AND sn_aprobado = 'S' AND estado = 'IC' ";
			List<Object> l_IC = DAO.nqObject(new MatMatricula(), jpql);
			olCantIC = String.valueOf(l_IC.size());		
		}	
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
		jpql = "from MatOferta e where e.idOferta = '" + soOferta + "'";		
		MatOferta o = (MatOferta) DAO.buscarObject(new MatOferta(), jpql);
		if (o != null) {
			try { olGrado = o.getMatCurso().getDescripcion(); } catch (Exception ex) {}
			try { olParalelo = o.getMatParalelo().getDescripcion(); } catch (Exception ex) {}
		}
		
		jpql = "from MatEstudiante e where e.idEstudiante = '" + soEstudiante + "'";		
		MatEstudiante e = (MatEstudiante) DAO.buscarObject(new MatEstudiante(), jpql);
		if (e != null) {
			try { itCedula = e.getIdEstudiante(); } catch (Exception ex) {}
			try { itNombres = e.getNombres(); } catch (Exception ex) {}
			try { itApellidos = e.getApellidos(); } catch (Exception ex) {}
			try { soRepresentante = e.getMatRepresentante().getApellidos().trim() + " " + e.getMatRepresentante().getNombres().trim(); } catch (Exception ex) {}
			try { sorGenero = e.getGenero(); } catch (Exception ex) {}
			try { soTipoSangre = e.getTipoSangre(); } catch (Exception ex) {}
			try { itLugarNac = e.getLugarNacimiento(); } catch (Exception ex) {}
			try { itNacionalidad = e.getNacionalidad(); } catch (Exception ex) {}
			try { cFechaNac = e.getFechaNac();
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(cFechaNac); 
			itFechaNac = strDate; 
			itEdad = String.valueOf(calculateAge(cFechaNac, new Date())); } catch (Exception ex) {}
			try { ckDiscapacidad = (e.getSnDiscapacidad().equals("S") ? true : false); } catch (Exception ex) {}
			try { itObservacion = e.getObservacion(); } catch (Exception ex) {}
			try { itImagen = (e.getFoto() == null ? "/sismacc/fotos/FOTO_DEFAULT.png" : e.getFoto()); } catch (Exception ex) {}

			try { itProvincia = e.getProvincia(); } catch (Exception ex) {}
			try { itCanton = e.getCanton(); } catch (Exception ex) {}
			try { itCiudad = e.getCiudad(); } catch (Exception ex) {}
			try { itParroquia = e.getParroquia(); } catch (Exception ex) {}
			try { itTelefono = e.getTelefono(); } catch (Exception ex) {}
			try { itDireccion = e.getDireccion(); } catch (Exception ex) {}
			try { itPersonaEmerg = e.getEmergLlamar(); } catch (Exception ex) {}
			try { itTelefonoEmerg = e.getEmergTelefono(); } catch (Exception ex) {}

			try { itPaNombre = e.getPaNombre(); } catch (Exception ex) {}
			try { itPaCedula = e.getPaCedula(); } catch (Exception ex) {}
			try { soPaEstadoCivil = e.getPaEstadoCivil(); } catch (Exception ex) {}
			try { opPaEstudios = e.getPaEstudios(); } catch (Exception ex) {}
			try { itPaProfesion = e.getPaProfesion(); } catch (Exception ex) {}
			try { opPaOcupacion = e.getPaOcupacion(); } catch (Exception ex) {}
			try { itPaInstitucion = e.getPaInstitucion(); } catch (Exception ex) {}
			try { itPaPuesto = e.getPaPuesto(); } catch (Exception ex) {}
			try { itPaTelefono = e.getPaTelefono(); } catch (Exception ex) {}
			try { itPaCelular = e.getPaCelular(); } catch (Exception ex) {}
			try { itPaCorreo = e.getPaCorreo(); } catch (Exception ex) {}
			try { itMaNombre = e.getMaNombre(); } catch (Exception ex) {}
			try { itMaCedula = e.getMaCedula(); } catch (Exception ex) {}
			try { soMaEstadoCivil = e.getMaEstadoCivil(); } catch (Exception ex) {}
			try { opMaEstudios = e.getMaEstudios(); } catch (Exception ex) {}
			try { itMaProfesion = e.getMaProfesion(); } catch (Exception ex) {}
			try { opMaOcupacion = e.getMaOcupacion(); } catch (Exception ex) {}
			try { itMaInstitucion = e.getMaInstitucion(); } catch (Exception ex) {}
			try { itMaPuesto = e.getMaPuesto(); } catch (Exception ex) {}
			try { itMaTelefono = e.getMaTelefono(); } catch (Exception ex) {}
			try { itMaCelular = e.getMaCelular(); } catch (Exception ex) {}
			try { itMaCorreo = e.getMaCorreo(); } catch (Exception ex) {}
			try { snHermanos = e.getSnHermanos(); } catch (Exception ex) {}
			try { itHermanos = e.getDsHermanos(); } catch (Exception ex) {}

			try { opViveCon = e.getViveCon(); } catch (Exception ex) {}
			try { itSituacionFam = e.getSituacionFamiliar(); } catch (Exception ex) {}
			try { opRelacionPadres = e.getRelacionPadres(); } catch (Exception ex) {}
			try { snClasesPart = e.getSnClasesPart(); } catch (Exception ex) {}
			try { itClasesPart = e.getDsClasesPart(); } catch (Exception ex) {}
			try { snEstudiosFuera = e.getSnEstudiosFuera(); } catch (Exception ex) {}
			try { itEstudiosFuera = e.getDsEstudiosFuera(); } catch (Exception ex) {}
			try { opRendimiento = e.getRendimiento(); } catch (Exception ex) {}
			try { itAsigMas = e.getAsigMas(); } catch (Exception ex) {}
			try { itAsigMenos = e.getAsigMenos(); } catch (Exception ex) {}
			try { itReaccionPadres = e.getReaccionPadres(); } catch (Exception ex) {}
			try { snCumples = e.getSnCumples(); } catch (Exception ex) {}
			try { itCumples = e.getDsCumples(); } catch (Exception ex) {}

			try { opEnfermedad = e.getEnfermedad(); } catch (Exception ex) {}
			try { snAlergiaMed = e.getSnAlergiaMed(); } catch (Exception ex) {}
			try { itAlergiaMed = e.getDsAlergiaMed(); } catch (Exception ex) {}
			try { snAlergiaIns = e.getSnAlergiaIns(); } catch (Exception ex) {}
			try { itAlergiaIns = e.getDsAlergiaIns(); } catch (Exception ex) {}
			try { snAlergiaAli = e.getSnAlergiaAli(); } catch (Exception ex) {}
			try { itAlergiaAli = e.getDsAlergiaAli(); } catch (Exception ex) {}
			try { itAlergOtras = e.getAlergOtras(); } catch (Exception ex) {}
			try { snTratamientoAct = e.getSnTratamiento(); } catch (Exception ex) {}
			try { snTratamientoAnt = e.getSnTratRecibido(); } catch (Exception ex) {}
			try { snHospital = e.getSnHospital(); } catch (Exception ex) {}
			try { itHospital = e.getDsHospital(); } catch (Exception ex) {}
			try { snOperado = e.getSnOperado(); } catch (Exception ex) {}
			try { itOperado = e.getDsOperado(); } catch (Exception ex) {}
			try { snMinusvalia = e.getSnMinusvalia(); } catch (Exception ex) {}
			try { itMinusvalia = e.getDsMinusvalia(); } catch (Exception ex) {}
			try { soGradoMinusvalia = e.getGdMinusvalia(); } catch (Exception ex) {}
		} else {
			limpiarFicha();
			mensaje = "No existen datos del estudiante";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, mensajeTitulo, mensaje));
			return;
		}
	}
	
	public void createPiePeriodo() {
		if (soPeriodo.equals("NA")) {
			mensaje = "Debe seleccionarel período";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		
		List<Object> result = (List<Object>) DAO.getGraficaDepartamento(soPeriodo);
		Iterator<Object> itr = result.iterator();
		int total = 0;
		if (!result.isEmpty()) {			
			porPeriodo = new PieChartModel();
			for (int k = 0; k < result.size(); k++) {
				Object[] obj = (Object[]) itr.next();
				int cant = Integer.parseInt(obj[1].toString());				
				porPeriodo.set(obj[0].toString(), cant);
				total += cant; 
			}
			porPeriodo.setTitle("Cantidad (" + total + ")");	
			porPeriodo.setLegendPosition("w");
			porPeriodo.setExtender("extChart");
			porPeriodo.setSeriesColors("657df9,FF9800,f77070,00bcd4,fb7444,da6fec,64b832,2196f3,ffeb3b,ff6c9e,b9f3bc,4caf50");
		} else
			limpiarGrafica();
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
	
	public String cantMatOferta(String periodo, String  oferta, String  estado) {
		try {
			return DAO.cantMatOferta(periodo, oferta, estado);	
		} catch (Exception e) {
			return "";
		}		
	}
	
	public void limpiarGrafica() {
		porPeriodo = new PieChartModel();
		porPeriodo.set("No hay datos", 0);
		
		porPeriodo.setTitle("Cantidad (0)");	
		porPeriodo.setLegendPosition("w");
		porPeriodo.setExtender("extChart");
		porPeriodo.setSeriesColors("657df9,FF9800,f77070,00bcd4,fb7444,da6fec,64b832,2196f3,ffeb3b,ff6c9e,b9f3bc,4caf50");
	}

	public void limpiarFicha() {
		olGrado = "_____________";		
		olParalelo = "____";
		
		itCedula = "_____________";
		itNombres = "___________________";
		itApellidos = "___________________";
		soRepresentante = "_______________________________________";
		itImagen = "/sismacc/fotos/FOTO_DEFAULT.png";
		soTipoSangre = "__________";

		itLugarNac = "_________________";
		itNacionalidad = "_________________";
		itFechaNac = "_______________________";
		itEdad = "_________";
		itObservacion = "____________________________________________________________________________";

		itProvincia = "_________________";
		itCanton = "_________________";
		itCiudad = "_________________";
		itParroquia = "_________________";
		itTelefono = "_____________";
		itDireccion = "______________________________________";
		itPersonaEmerg = "__________________________";
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
		
		ckDiscapacidad = false;
		sorGenero = "";
		soPaEstadoCivil = "_________________";
		opPaEstudios = "";	
		opPaOcupacion = "";
		soMaEstadoCivil = "_________________";
		opMaEstudios = "";	
		opMaOcupacion = "";
		snHermanos = "";
		opViveCon = "";
		opRelacionPadres = "";
		snClasesPart = "";
		snEstudiosFuera = "";
		opRendimiento = "";
		snCumples = "";
		opEnfermedad = "";
		snAlergiaMed = "";
		snAlergiaIns = "";
		snAlergiaAli = "";
		snTratamientoAct = "";
		snTratamientoAnt = "";
		snHospital = "";
		snOperado = "";
		snMinusvalia = "";
	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodosRep(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodo), 2);
	}

	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getEstudiantesOferta(), 2);
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
	public List<Object> getMatriculaList() {
		return matriculaList;
	}
	public void setMatriculaList(List<Object> matriculaList) {
		this.matriculaList = matriculaList;
	}
	public String getOlCantAC() {
		return olCantAC;
	}
	public void setOlCantAC(String olCantAC) {
		this.olCantAC = olCantAC;
	}
	public String getOlCantIC() {
		return olCantIC;
	}
	public void setOlCantIC(String olCantIC) {
		this.olCantIC = olCantIC;
	}
	public PieChartModel getPorPeriodo() {
		return porPeriodo;
	}
	public void setPorPeriodo(PieChartModel porPeriodo) {
		this.porPeriodo = porPeriodo;
	}	
}