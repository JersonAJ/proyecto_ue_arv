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

import com.ups.uearv.entidades.SegPerfil;
import com.ups.uearv.entidades.SegPerfilMenu;
import com.ups.uearv.entidades.SegPerfilMenuPK;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Session;


/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "perfil")
@ViewScoped
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	String idPerfil = "";
	String descripcion = "";
	boolean ckEstado = false;

	String itBuscar = "";
	boolean ckMostrarIC = false;

	private List<SegPerfil> perfilList = new ArrayList<SegPerfil>();

	private List<PerfilMenu> menuSegList = new ArrayList<PerfilMenu>();
	private List<PerfilMenu> menuManList = new ArrayList<PerfilMenu>();
	private List<PerfilMenu> menuMatList = new ArrayList<PerfilMenu>();
	private List<PerfilMenu> menuGesList = new ArrayList<PerfilMenu>();
	private List<PerfilMenu> menuCalList = new ArrayList<PerfilMenu>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";


	@PostConstruct
	public void init() {	
		buscar();	
	}

	// CONSULTA
	public void llenarLista(String jpql) {
		perfilList.clear();
		List<SegPerfil> l = DAO.nqSegPerfil(jpql); 
		for (SegPerfil in : l) 
			perfilList.add(in);		
	}

	public void buscar() {
		if (ckMostrarIC) 
			jpql = "SELECT * FROM Seg_Perfil WHERE descripcion LIKE '%" + itBuscar	+ "%' ORDER BY descripcion";
		else 		
			jpql = "SELECT * FROM Seg_Perfil WHERE descripcion LIKE '%" + itBuscar	+ "%' AND estado = 'AC' ORDER BY descripcion";

		llenarLista(jpql);
	}

	// INGRESO - ACTUALIZACION
	public void guardar() {		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			Date date = new Date();
			Timestamp fecha = new Timestamp(date.getTime());
			String estado = "IC";
			if (ckEstado) { estado = "AC"; }				

			SegPerfil ob = new SegPerfil();
			if (accion == 1) {
				ob = DAO.buscarSegPerfil("from SegPerfil c where c.idPerfil = " + idPerfil);
			}

			ob.setDescripcion(descripcion);
			ob.setEstado(estado);
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
				FacesContext.getCurrentInstance().addMessage("msgRegistro", new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeTitulo, mensaje));	
				buscar();		
			} else {
				em.getTransaction().rollback();
				mensaje = "Error al guardar";
				FacesContext.getCurrentInstance().addMessage("msgRegistro", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensajeTitulo, mensaje));
			}		
		} catch (Exception e) {
			em.getTransaction().rollback();
		}	
		em.close();		
	}

	public void closeDialogo() {
		init();
	}

	public boolean getEstado(String estado) {
		boolean ban = true;
		if (estado.equals("IC"))
			ban = false;
		return ban;
	}

	public void habilitaMenu(int perfil, int menu, boolean seleccion) {
		SegPerfilMenuPK pk = new SegPerfilMenuPK();
		pk.setIdPerfil(perfil);
		pk.setIdMenu(menu);
		SegPerfilMenu mp = new SegPerfilMenu();
		mp.setId(pk);
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			if (seleccion) {
				if(DAO.saveOrUpdate(mp, 0, em))
					em.getTransaction().commit();				
			}
			else 
				DAO.deleteSegPerfilMenu(mp);
		} catch (Exception e) {
			em.getTransaction().rollback();
		}	
		em.close();
	}

	public List<PerfilMenu> cargaMenuSeg(String perfil, int padre) {
		return consultaMenu(menuSegList, perfil, padre);		
	}
	
	public List<PerfilMenu> cargaMenuMan(String perfil, int padre) {
		return consultaMenu(menuManList, perfil, padre);
	}
	
	public List<PerfilMenu> cargaMenuMat(String perfil, int padre) {
		return consultaMenu(menuMatList, perfil, padre);
	} 
	
	public List<PerfilMenu> cargaMenuGes(String perfil, int padre) {
		return consultaMenu(menuGesList, perfil, padre);
	} 
	
	public List<PerfilMenu> cargaMenuCal(String perfil, int padre) {
		return consultaMenu(menuCalList, perfil, padre);
	} 	
	
	public List<PerfilMenu> consultaMenu(List<PerfilMenu> list, String perfil, int padre) {
		list.clear();
		List<Object> result = DAO.consultaMenu(perfil, padre);
		Iterator<Object> itr = result.iterator();
		for (int k = 0; k < result.size(); k++) {
			Object[] obj = (Object[]) itr.next();

			PerfilMenu m = new PerfilMenu();
			m.setPerfil(Integer.parseInt(obj[0].toString()));
			m.setMenu(Integer.parseInt(obj[1].toString()));
			m.setDescripcion(obj[2].toString());
			m.setSeleccion(obj[3].toString());
			list.add(m);
		}
		return list;
	}

	public class PerfilMenu {
		int perfil;
		int menu;
		String descripcion;
		String seleccion;

		public int getPerfil() {
			return perfil;
		}
		public void setPerfil(int perfil) {
			this.perfil = perfil;
		}
		public int getMenu() {
			return menu;
		}
		public void setMenu(int menu) {
			this.menu = menu;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getSeleccion() {
			return seleccion;
		}
		public void setSeleccion(String seleccion) {
			this.seleccion = seleccion;
		}		
	}

	// GETTERS AND SETTERS	
	public String getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

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

	public List<SegPerfil> getPerfilList() {
		return perfilList;
	}

	public void setPerfilList(List<SegPerfil> perfilList) {
		this.perfilList = perfilList;
	}

	public List<PerfilMenu> getMenuSegList() {
		return menuSegList;
	}

	public void setMenuSegList(List<PerfilMenu> menuSegList) {
		this.menuSegList = menuSegList;
	}

	public List<PerfilMenu> getMenuManList() {
		return menuManList;
	}

	public void setMenuManList(List<PerfilMenu> menuManList) {
		this.menuManList = menuManList;
	}

	public List<PerfilMenu> getMenuMatList() {
		return menuMatList;
	}

	public void setMenuMatList(List<PerfilMenu> menuMatList) {
		this.menuMatList = menuMatList;
	}

	public List<PerfilMenu> getMenuGesList() {
		return menuGesList;
	}

	public void setMenuGesList(List<PerfilMenu> menuGesList) {
		this.menuGesList = menuGesList;
	}

	public List<PerfilMenu> getMenuCalList() {
		return menuCalList;
	}

	public void setMenuCalList(List<PerfilMenu> menuCalList) {
		this.menuCalList = menuCalList;
	}
}
