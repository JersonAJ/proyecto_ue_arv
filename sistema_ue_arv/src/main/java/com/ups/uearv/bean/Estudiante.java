/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Timestamp;
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

import org.primefaces.model.UploadedFile;

import com.ups.uearv.entidades.MatEstudiante;
import com.ups.uearv.entidades.MatRepresentante;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "estudiante")
@ViewScoped
public class Estudiante {

	// PERSONAL
	static String itCedula = "";
	String itNombres = "";
	String itApellidos = "";
	String soRepresentante = "";
	String sorGenero = "";
	String soTipoSangre = "";
	String itLugarNac = "";
	String itNacionalidad = "";	
	Date cFechaNac = new Date();
	boolean ckDiscapacidad = false;
	String itObservacion = "";
	boolean ckEstado = false;
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
	
	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> estudianteList = new ArrayList<Object>();

	ArrayList<SelectItem> listTipoSangre = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listRepresentante = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listEstadoCivil = new ArrayList<SelectItem>();
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";

	UploadedFile fuFoto;

	@PostConstruct
	public void init() {
		
		listTipoSangre = (ArrayList<SelectItem>) llenaComboTipoSangre();
		soTipoSangre = listTipoSangre.get(0).getValue().toString();

		listRepresentante = (ArrayList<SelectItem>) llenaComboRepresentante();
		soRepresentante = listRepresentante.get(0).getValue().toString();

		listEstadoCivil = (ArrayList<SelectItem>) llenaComboEstadoCivil();

		buscar();
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		estudianteList.clear();
		List<Object> l = DAO.nqObject(new MatEstudiante(), jpql);

		for (Object in : l)
			estudianteList.add(in);
	}

	public void buscar() {
		if (ckMostrarIC) {
			jpql = " SELECT c.* FROM mat_estudiante c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' ORDER BY c.apellidos ";
		} else {
			jpql = " SELECT c.* FROM mat_estudiante c WHERE c.apellidos LIKE '%"	+ itBuscar + "%' AND c.estado = 'AC' ORDER BY c.apellidos ";
		}
		llenarLista(jpql);
	}

	public void upload() {
		if(fuFoto != null) {
			FacesMessage message = new FacesMessage("Succesful", fuFoto.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public List<SelectItem> llenaComboEstadoCivil() {
		return Util.llenaCombo(DAO.getDetCatalogo("EC000"), 2);
	}
		
	// INGRESO - ACTUALIZACION		
	public void guardar() throws IOException {
		// VALICADIONES FOTO
		String base64 = itImagen;
		base64 = base64.replace("data:image/jpeg;base64,", "");
		base64 = base64.replace("data:image/jpg;base64,", "");
		BufferedImage foto = Util.decodeToImage(base64);
		
		if (foto == null) {
			mensaje = "Debe seleccionar un archivo válido (imagen tipo JPG o JPEG)";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}			
		if (foto.getWidth() > 200 && foto.getHeight() > 250) {
			mensaje = "Las dimensiones de la imagen no deben ser mayor a 200(ancho) y 250(alto)";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
	
		// VALIDACIONES	
		if (itCedula.trim().equals("")) {
			mensaje = "Debe ingresar la cédula";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}				
		String msgValCedula = Util.validaCedula(itCedula);
		if (!msgValCedula.equals("OK")) {
			mensaje = msgValCedula;
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}		
		if (accion == 0) {
			MatEstudiante ob =  (MatEstudiante) DAO.buscarObject(new MatEstudiante(), "from MatEstudiante c where c.idEstudiante = '" + itCedula + "'");
			if (ob != null) {
				mensaje = "El estudiante ya existe";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
				return;
			}
		}		
		if (itNombres.trim().equals("")) {
			mensaje = "Debe ingresar al menos un nombre";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}
		if (itApellidos.trim().equals("")) {
			mensaje = "Debe ingresar al menos un apellido";
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			return;
		}

		// PROCESO		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {		
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());

			MatEstudiante ob = new MatEstudiante();
			if (accion == 1) {
				ob = (MatEstudiante) DAO.buscarObject(new MatEstudiante(), "from MatEstudiante c where c.idEstudiante = '" + itCedula + "'");
			} else {
				ob.setIdEstudiante(itCedula);
			}

			String estado = "IC";
			if (ckEstado) estado = "AC";
			
			String discapacidad = "NO";
			if (ckDiscapacidad) discapacidad = "SI";

			MatRepresentante representante = (MatRepresentante) DAO.buscarObject(new MatRepresentante(), "from MatRepresentante c where c.idRepresentante = '" + soRepresentante + "'");

			ob.setMatRepresentante(representante);
			ob.setApellidos(itApellidos);
			ob.setNombres(itNombres);
			ob.setLugarNacimiento(itLugarNac);
			ob.setNacionalidad(itNacionalidad);			
			ob.setObservacion(itObservacion);
			ob.setFechaNac(cFechaNac);			
			ob.setGenero(sorGenero);
			ob.setTipoSangre(soTipoSangre);
			ob.setEstado(estado);
			ob.setSnDiscapacidad(discapacidad);
			ob.setFoto(itImagen);							
			
			ob.setProvincia(itProvincia);
			ob.setCanton(itCanton);
			ob.setCiudad(itCiudad);
			ob.setParroquia(itParroquia);
			ob.setTelefono(itTelefono);
			ob.setDireccion(itDireccion);
			ob.setEmergLlamar(itPersonaEmerg);
			ob.setEmergTelefono(itTelefonoEmerg);
			
			ob.setPaCedula(itPaCedula);
			ob.setPaNombre(itPaNombre);
			ob.setPaEstadoCivil(soPaEstadoCivil);
			ob.setPaEstudios(opPaEstudios);
			ob.setPaProfesion(itPaProfesion);
			ob.setPaOcupacion(opPaOcupacion);
			ob.setPaPuesto(itPaPuesto);
			ob.setPaInstitucion(itPaInstitucion);
			ob.setPaTelefono(itPaTelefono);
			ob.setPaCelular(itPaCelular);
			ob.setPaCorreo(itPaCorreo);
			ob.setMaCedula(itMaCedula);
			ob.setMaNombre(itMaNombre);
			ob.setMaEstadoCivil(soMaEstadoCivil);
			ob.setMaEstudios(opMaEstudios);
			ob.setMaProfesion(itMaProfesion);
			ob.setMaOcupacion(opMaOcupacion);
			ob.setMaPuesto(itMaPuesto);
			ob.setMaInstitucion(itMaInstitucion);
			ob.setMaTelefono(itMaTelefono);
			ob.setMaCelular(itMaCelular);
			ob.setMaCorreo(itMaCorreo);
			ob.setSnHermanos(snHermanos);
			ob.setDsHermanos(itHermanos);
			
			ob.setViveCon(opViveCon);
			ob.setSituacionFamiliar(itSituacionFam);
			ob.setRelacionPadres(opRelacionPadres);
			ob.setSnClasesPart(snClasesPart);
			ob.setDsClasesPart(itClasesPart);
			ob.setSnEstudiosFuera(snEstudiosFuera);
			ob.setDsEstudiosFuera(itEstudiosFuera);
			ob.setRendimiento(opRendimiento);
			ob.setAsigMas(itAsigMas);
			ob.setAsigMenos(itAsigMenos);
			ob.setReaccionPadres(itReaccionPadres);
			ob.setSnCumples(snCumples);
			ob.setDsCumples(itCumples);
			
			ob.setEnfermedad(opEnfermedad);
			ob.setSnAlergiaMed(snAlergiaMed);
			ob.setDsAlergiaMed(itAlergiaMed);
			ob.setSnAlergiaIns(snAlergiaIns);
			ob.setDsAlergiaIns(itAlergiaIns);
			ob.setSnAlergiaAli(snAlergiaAli);
			ob.setDsAlergiaAli(itAlergiaAli);
			ob.setAlergOtras(itAlergOtras);
			ob.setSnTratamiento(snTratamientoAct);
			ob.setSnTratRecibido(snTratamientoAnt);
			ob.setSnHospital(snHospital);
			ob.setDsHospital(itHospital);
			ob.setSnOperado(snOperado);
			ob.setDsOperado(itOperado);
			ob.setSnMinusvalia(snMinusvalia);
			ob.setDsMinusvalia(itMinusvalia);
			ob.setGdMinusvalia(soGradoMinusvalia);
			
			if (accion == 0) {
				ob.setUsuarioIng(Session.getUserName());			
				ob.setFechaIng(fecha);				
			}			
			if (accion == 1) {
				ob.setUsuarioAct(Session.getUserName());			
				ob.setFechaAct(fecha);			
			}			
			
			if (DAO.saveOrUpdate(ob, accion, em)) {
				em.getTransaction().commit();
				mensaje = "Guardado exitoso";
				FacesContext.getCurrentInstance().addMessage("growl",	new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
				buscar();
			} else {
				em.getTransaction().rollback();
				mensaje = "Error al guardar";
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			}

		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.close();		
	}

	public void closeDialogo() {
		init();
	}

	public List<SelectItem> llenaComboTipoSangre() {
		return Util.llenaCombo(DAO.getDetCatalogo("TS000"), 2);
	}

	public List<SelectItem> llenaComboRepresentante() {
		return Util.llenaCombo(DAO.getRepresentantes(), 2);
	}

	// GETTERS AND SETTERS
	public boolean isCkEstado() {
		return ckEstado;
	}
	public void setCkEstado(boolean ckEstado) {
		this.ckEstado = ckEstado;
	}
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
	public List<Object> getEstudianteList() {
		return estudianteList;
	}
	public void setEstudianteList(List<Object> estudianteList) {
		this.estudianteList = estudianteList;
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
	public String getItCedula() {
		return itCedula;
	}
	public void setItCedula(String itCedula) {
		Estudiante.itCedula = itCedula;
	}
	public String getItDireccion() {
		return itDireccion;
	}
	public void setItDireccion(String itDireccion) {
		this.itDireccion = itDireccion;
	}
	public Date getcFechaNac() {
		return cFechaNac;
	}
	public void setcFechaNac(Date cFechaNac) {
		this.cFechaNac = cFechaNac;
	}
	public String getItTelefono() {
		return itTelefono;
	}
	public void setItTelefono(String itTelefono) {
		this.itTelefono = itTelefono;
	}	
	public String getSoRepresentante() {
		return soRepresentante;
	}
	public void setSoRepresentante(String soRepresentante) {
		this.soRepresentante = soRepresentante;
	}
	public String getSoTipoSangre() {
		return soTipoSangre;
	}
	public void setSoTipoSangre(String soTipoSangre) {
		this.soTipoSangre = soTipoSangre;
	}
	public String getSorGenero() {
		return sorGenero;
	}
	public void setSorGenero(String sorGenero) {
		this.sorGenero = sorGenero;
	}
	public ArrayList<SelectItem> getListRepresentante() {
		return listRepresentante;
	}
	public void setListRepresentante(ArrayList<SelectItem> listRepresentante) {
		this.listRepresentante = listRepresentante;
	}
	public ArrayList<SelectItem> getListTipoSangre() {
		return listTipoSangre;
	}
	public void setListTipoSangre(ArrayList<SelectItem> listTipoSangre) {
		this.listTipoSangre = listTipoSangre;
	}
	public UploadedFile getFuFoto() {
		return fuFoto;
	}
	public void setFuFoto(UploadedFile fuFoto) {
		this.fuFoto = fuFoto;
	}
	public String getItImagen() {
		return itImagen;
	}
	public void setItImagen(String itImagen) {
		this.itImagen = itImagen;
	}
	public String getItObservacion() {
		return itObservacion;
	}
	public void setItObservacion(String itObservacion) {
		this.itObservacion = itObservacion;
	}
	public String getItAlergOtras() {
		return itAlergOtras;
	}
	public void setItAlergOtras(String itAlergOtras) {
		this.itAlergOtras = itAlergOtras;
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
	public boolean isCkDiscapacidad() {
		return ckDiscapacidad;
	}
	public void setCkDiscapacidad(boolean ckDiscapacidad) {
		this.ckDiscapacidad = ckDiscapacidad;
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
	public ArrayList<SelectItem> getListEstadoCivil() {
		return listEstadoCivil;
	}
	public void setListEstadoCivil(ArrayList<SelectItem> listEstadoCivil) {
		this.listEstadoCivil = listEstadoCivil;
	}
}