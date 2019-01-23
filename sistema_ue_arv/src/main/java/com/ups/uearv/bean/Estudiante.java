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

	static String itCedula = "";
	String itNombres = "";
	String itApellidos = "";
	String itTelefono = "";
	String itInstitucionAnt = "";	
	String itAlergias = "";	
	String itDireccion = "";	
	String soRepresentante = "";
	String soTipoSangre = "";
	String sorGenero = "";
	Date cFechaNac = new Date();

	boolean ckEstado = false;

	String itImagen = "";
	
	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<Object> estudianteList = new ArrayList<Object>();

	ArrayList<SelectItem> listTipoSangre = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listRepresentante = new ArrayList<SelectItem>();

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

			MatRepresentante representante = (MatRepresentante) DAO.buscarObject(new MatRepresentante(), "from MatRepresentante c where c.idRepresentante = '" + soRepresentante + "'");

			ob.setMatRepresentante(representante);
			ob.setApellidos(itApellidos);
			ob.setNombres(itNombres);
			ob.setAlergias(itAlergias);
			ob.setDireccion(itDireccion);
			ob.setInstitucionAnt(itInstitucionAnt);
			ob.setFechaNac(cFechaNac);
			ob.setTelefono(itTelefono);
			ob.setGenero(sorGenero);
			ob.setTipoSangre(soTipoSangre);
			ob.setEstado(estado);
				
			ob.setFoto(itImagen);							
			
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
	public String getItInstitucionAnt() {
		return itInstitucionAnt;
	}
	public void setItInstitucionAnt(String itInstitucionAnt) {
		this.itInstitucionAnt = itInstitucionAnt;
	}
	public String getItAlergias() {
		return itAlergias;
	}
	public void setItAlergias(String itAlergias) {
		this.itAlergias = itAlergias;
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
}