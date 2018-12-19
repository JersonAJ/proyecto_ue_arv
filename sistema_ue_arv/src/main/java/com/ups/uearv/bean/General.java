/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;

import com.ups.uearv.entidades.CatalogoDet;
import com.ups.uearv.entidades.SegUsuario;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;
import com.ups.uearv.servicios.Util;


/**
 * @author Jerson Armijos Jaén
 * @version 1.0
 */

@ManagedBean(name = "general")
@ViewScoped
public class General implements Serializable {

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {	
		validaMenuSistema();
		mostrarCambioClave();

	}

	// VALIDAR SESION
	public String validaSesion() {		 
		if (Session.getUserName() == null) 
			return "/errores/error_sesion.xhtml";	    
		return null;
	}

	// MOSTRAR CAMBIO DE CLAVE
	String displayMensajeClave = "none";

	@SuppressWarnings("deprecation")
	public void mostrarCambioClave() {		
		if (Session.getUserName() != null) {
			SegUsuario ob = (SegUsuario) DAO.buscarObject(new SegUsuario(), "from SegUsuario c where c.idUsuario = '" + Session.getUserName() + "'");
			if (ob.getSnNuevo().equals("S")) {
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dglClave').show();");
				displayMensajeClave ="";	
			}		
		}
	}

	public String getDisplayMensajeClave() {
		return displayMensajeClave;
	}
	public void setDisplayMensajeClave(String displayMensajeClave) {
		this.displayMensajeClave = displayMensajeClave;
	}
	
	// OBTENER ESTADO
	public boolean getEstado(String estado) {
		boolean ban = true;
		if (estado.equals("IC"))
			ban = false;
		return ban;
	}
	
	public boolean getAprobado(String estado) {
		boolean ban = true;
		if (estado.equals("N"))
			ban = false;
		return ban;
	}
	
	// OBTENER DESCRIPCION DEL CATALOGO DETALLE 
	public String getDesCatalogoDet(String cod) {
		CatalogoDet ob = new CatalogoDet();
		ob = (CatalogoDet) DAO.buscarObject(new CatalogoDet(), "from CatalogoDet c where c.codigoDet = '" + cod + "'");

		return ob.getDescripcion().trim();
	}

	// OBTENER FECHA EN FORMATO
	public String getFechaString(Date f) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateFormat.format(f.getTime());
	}
	
	// CAMBIO DE CLAVE
	String clave1 = "";
	String clave2 = "";

	public void cambiarClave() {

		if (!clave1.equals("")) {
			if (clave1.equals(clave2)) {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
				EntityManager em = emf.createEntityManager();
				em.getTransaction().begin();
				try {
					SegUsuario ob = new SegUsuario();
					ob = (SegUsuario) DAO.buscarObject(new SegUsuario(), "from SegUsuario c where c.idUsuario = '" + Session.getUserName() + "'");
					ob.setSnNuevo("N");
					ob.setClave(Util.generaSHA256(clave1));

					if (DAO.saveOrUpdate(ob, 1, em)) {
						em.getTransaction().commit();
						mensaje = "Cambio de clave exitoso";
						FacesContext.getCurrentInstance().addMessage("msgRegistro",
								new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));
						displayMensajeClave = "none";

					} else {
						em.getTransaction().rollback();
						mensaje = "Error al cambiar la clave";
						FacesContext.getCurrentInstance().addMessage("msgRegistro",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
					}
				} catch (Exception e) {
					em.getTransaction().rollback();
					e.printStackTrace();
				}
				em.close();
			} else {
				mensaje = "Las claves ingresadas deben ser iguales";
				FacesContext.getCurrentInstance().addMessage("msgRegistro",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			}
		} else {
			mensaje = "Debe ingresar la nueva clave";
			FacesContext.getCurrentInstance().addMessage("msgRegistro",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
		}
	}

	public String getClave1() {
		return clave1;
	}
	public void setClave1(String clave1) {
		this.clave1 = clave1;
	}
	public String getClave2() {
		return clave2;
	}
	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}

	// VALIDAR MENU		
	boolean seg = false;
	boolean man = false;
	boolean mat = false;
	boolean ges = false;
	boolean cal = false;

	String displaySeguridad = "none";
	String displayPerfiles = "none";
	String displayUsuarios = "none";

	String displayMantenimientos = "none";
	String displayCatalogo = "none";
	String displayCursos = "none";
	String displayDocentes = "none";
	String displayAsignaturas = "none";
	String displayRepartos = "none";
	String displayEscalas = "none";
	String displayComportamientos = "none";
	String displayParalelos = "none";

	String displayMatriculacion = "none";
	String displayPeriodos = "none";
	String displayOfertas = "none";
	String displayRepresentantes = "none";
	String displayEstudiantes = "none";
	String displayMatriculas = "none";
	String displayMatReportes = "none";

	String displayGestion = "none";
	String displayDescuentos = "none";
	String displayPensiones = "none";
	String displayGesReportes = "none";

	String displayCalificaciones = "none";
	String displayControl = "none";
	String displayAsistencias = "none";
	String displayCalReportes = "none";

	public void validaMenuSistema() {
		try {
			List<Object> result = DAO.obtenerMenu(Session.getUserName());
			Iterator<Object> itr = result.iterator();
			if (result.size() != 0) {
				for (int k = 0; k < result.size(); k++) {
					Object obj = (Object) itr.next();

					int idMenu = Integer.parseInt(obj.toString());

					switch (idMenu) {
					case 2:
						displayPerfiles = "";
						seg = true;
						break;
					case 3:
						displayUsuarios = "";
						seg = true;
						break;
					case 5:
						displayCatalogo = "";
						man = true;
						break;
					case 6:
						displayCursos = "";
						man = true;
						break;
					case 7:
						displayDocentes = "";
						man = true;
						break;
					case 8:
						displayAsignaturas = "";
						man = true;
						break;
					case 9:
						displayRepartos = "";
						man = true;
						break;
					case 10:
						displayEscalas = "";
						man = true;
						break;
					case 11:
						displayComportamientos = "";
						man = true;
						break;
					case 12:
						displayParalelos = "";
						man = true;
						break;
					case 14:
						displayPeriodos = "";
						mat = true;
						break;
					case 15:
						displayOfertas = "";
						mat = true;
						break;
					case 16:
						displayRepresentantes = "";
						mat = true;
						break;
					case 17:
						displayEstudiantes = "";
						mat = true;
						break;
					case 18:
						displayMatriculas = "";
						mat = true;
						break;
					case 19:
						displayMatReportes = "";
						mat = true;
						break;
					case 21:
						displayDescuentos = "";
						ges = true;
						break;
					case 22:
						displayPensiones = "";
						ges = true;
						break;
					case 23:
						displayGesReportes = "";
						ges = true;
						break;
					case 25:
						displayControl = "";
						cal = true;
						break;
					case 26:
						displayAsistencias = "";
						cal = true;
						break;
					case 27:
						displayCalReportes = "";
						cal = true;
						break;
					}
				}
				if (seg) { displaySeguridad = ""; } // 1
				if (man) { displayMantenimientos = ""; } // 4
				if (mat) { displayMatriculacion = ""; } // 13
				if (ges) { displayGestion = ""; } // 20
				if (cal) { displayCalificaciones = ""; } // 24		
			}
		} catch (Exception e) {
			System.out.println("Error en validaMenuSistema() de tipo " + e);
		}
	}

	public String getDisplaySeguridad() {
		return displaySeguridad;
	}
	public void setDisplaySeguridad(String displaySeguridad) {
		this.displaySeguridad = displaySeguridad;
	}
	public String getDisplayPerfiles() {
		return displayPerfiles;
	}
	public void setDisplayPerfiles(String displayPerfiles) {
		this.displayPerfiles = displayPerfiles;
	}
	public String getDisplayUsuarios() {
		return displayUsuarios;
	}
	public void setDisplayUsuarios(String displayUsuarios) {
		this.displayUsuarios = displayUsuarios;
	}
	public String getDisplayMantenimientos() {
		return displayMantenimientos;
	}
	public void setDisplayMantenimientos(String displayMantenimientos) {
		this.displayMantenimientos = displayMantenimientos;
	}
	public String getDisplayCatalogo() {
		return displayCatalogo;
	}
	public void setDisplayCatalogo(String displayCatalogo) {
		this.displayCatalogo = displayCatalogo;
	}
	public String getDisplayCursos() {
		return displayCursos;
	}
	public void setDisplayCursos(String displayCursos) {
		this.displayCursos = displayCursos;
	}
	public String getDisplayDocentes() {
		return displayDocentes;
	}
	public void setDisplayDocentes(String displayDocentes) {
		this.displayDocentes = displayDocentes;
	}
	public String getDisplayAsignaturas() {
		return displayAsignaturas;
	}
	public void setDisplayAsignaturas(String displayAsignaturas) {
		this.displayAsignaturas = displayAsignaturas;
	}
	public String getDisplayRepartos() {
		return displayRepartos;
	}
	public void setDisplayRepartos(String displayRepartos) {
		this.displayRepartos = displayRepartos;
	}
	public String getDisplayEscalas() {
		return displayEscalas;
	}
	public void setDisplayEscalas(String displayEscalas) {
		this.displayEscalas = displayEscalas;
	}
	public String getDisplayComportamientos() {
		return displayComportamientos;
	}
	public void setDisplayComportamientos(String displayComportamientos) {
		this.displayComportamientos = displayComportamientos;
	}
	public String getDisplayParalelos() {
		return displayParalelos;
	}
	public void setDisplayParalelos(String displayParalelos) {
		this.displayParalelos = displayParalelos;
	}
	public String getDisplayMatriculacion() {
		return displayMatriculacion;
	}
	public void setDisplayMatriculacion(String displayMatriculacion) {
		this.displayMatriculacion = displayMatriculacion;
	}
	public String getDisplayPeriodos() {
		return displayPeriodos;
	}
	public void setDisplayPeriodos(String displayPeriodos) {
		this.displayPeriodos = displayPeriodos;
	}
	public String getDisplayOfertas() {
		return displayOfertas;
	}
	public void setDisplayOfertas(String displayOfertas) {
		this.displayOfertas = displayOfertas;
	}
	public String getDisplayRepresentantes() {
		return displayRepresentantes;
	}
	public void setDisplayRepresentantes(String displayRepresentantes) {
		this.displayRepresentantes = displayRepresentantes;
	}
	public String getDisplayEstudiantes() {
		return displayEstudiantes;
	}
	public void setDisplayEstudiantes(String displayEstudiantes) {
		this.displayEstudiantes = displayEstudiantes;
	}
	public String getDisplayMatriculas() {
		return displayMatriculas;
	}
	public void setDisplayMatriculas(String displayMatriculas) {
		this.displayMatriculas = displayMatriculas;
	}
	public String getDisplayMatReportes() {
		return displayMatReportes;
	}
	public void setDisplayMatReportes(String displayMatReportes) {
		this.displayMatReportes = displayMatReportes;
	}
	public String getDisplayGestion() {
		return displayGestion;
	}
	public void setDisplayGestion(String displayGestion) {
		this.displayGestion = displayGestion;
	}
	public String getDisplayDescuentos() {
		return displayDescuentos;
	}
	public void setDisplayDescuentos(String displayDescuentos) {
		this.displayDescuentos = displayDescuentos;
	}
	public String getDisplayPensiones() {
		return displayPensiones;
	}
	public void setDisplayPensiones(String displayPensiones) {
		this.displayPensiones = displayPensiones;
	}
	public String getDisplayGesReportes() {
		return displayGesReportes;
	}
	public void setDisplayGesReportes(String displayGesReportes) {
		this.displayGesReportes = displayGesReportes;
	}
	public String getDisplayCalificaciones() {
		return displayCalificaciones;
	}
	public void setDisplayCalificaciones(String displayCalificaciones) {
		this.displayCalificaciones = displayCalificaciones;
	}
	public String getDisplayControl() {
		return displayControl;
	}
	public void setDisplayControl(String displayControl) {
		this.displayControl = displayControl;
	}
	public String getDisplayAsistencias() {
		return displayAsistencias;
	}
	public void setDisplayAsistencias(String displayAsistencias) {
		this.displayAsistencias = displayAsistencias;
	}
	public String getDisplayCalReportes() {
		return displayCalReportes;
	}
	public void setDisplayCalReportes(String displayCalReportes) {
		this.displayCalReportes = displayCalReportes;
	}	
}