/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ups.uearv.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.ups.uearv.entidades.GesPension;
import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Ja�n - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "pension")
@ViewScoped
public class Pension implements Serializable {

	private static final long serialVersionUID = 1L;

	String soPeriodo = "";
	String soOferta = "";	
	String soEstudiante = "";
	String soTipo = "";		
	String soSecuencia = "";


	private List<Object> pensionList = new ArrayList<Object>();

	ArrayList<SelectItem> listPeriodos = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertas = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listEstudiantes = new ArrayList<SelectItem>();

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	

	int accion = 0; // 0 = ingresar; 1 = modificar

	String mensaje = "";
	String mensajeTitulo = "Mensaje del sistema";

	String jpql = "";  

	@PostConstruct
	public void init() {
		listPeriodos = (ArrayList<SelectItem>) llenaComboPeriodo();				
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		listEstudiantes = (ArrayList<SelectItem>) llenaComboEstudiante();

		soPeriodo = "NA";
		soOferta = "NA";
		soEstudiante = "NA";		
		soTipo = "NA";
		soSecuencia = "NA";	
	}

	// EVENTOS		
	public void closeDialogo() {
		init();
	}

	public void onChangePeriodo() {		
		listOfertas = (ArrayList<SelectItem>) llenaComboOferta();
		soOferta = "NA";
		onChangeOferta();
	}

	public void onChangeOferta() {
		listEstudiantes = (ArrayList<SelectItem>) llenaComboEstudiante();
		soEstudiante = "NA";
		onChangeEstudiante();	
	}

	public void onChangeEstudiante() {
		buscar();	
	}

	public void onChangeTipo() {
		buscar();	
	}

	public void onChangeSec() {
		buscar();	
	}

	// CONSULTA		
	public void buscar() {
		jpql = 
				"SELECT p.* \r\n" + 
						"FROM ges_pension p \r\n" + 
						"	INNER JOIN mat_matricula m ON m.id_matricula = p.id_matricula \r\n" +
						"WHERE m.id_periodo = '" + soPeriodo + "' \r\n";
		if (!soOferta.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOferta + "' \r\n"; }
		if (!soEstudiante.equals("NA")) { jpql = jpql + "AND m.id_estudiante = '" + soEstudiante + "' \r\n"; }
		if (!soTipo.equals("NA")) { 
			if (soTipo.equals("M")) { jpql = jpql + "AND p.secuencia = 0 \r\n"; }
			if (soTipo.equals("P")) { 				
				if (soSecuencia.equals("NA")) { jpql = jpql + "AND p.secuencia <> 0 \r\n"; } 
				if (!soSecuencia.equals("NA")) { jpql = jpql + "AND p.secuencia = '" + soSecuencia + "' \r\n"; }
			}
		}
		jpql = jpql + "AND p.estado = 'AC' \r\n" + 
				"ORDER BY p.secuencia \r\n";

		llenarListPensiones();
	}

	public void llenarListPensiones() {
		pensionList.clear();
		if(!soPeriodo.equals("NA")) {			
			List<Object> l = DAO.nqObject(new GesPension(), jpql);

			for (Object in : l)
				pensionList.add(in);
		}
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

	public Query getEstudiantesOferta() {		
		jpql = 
				"SELECT e.id_estudiante, CONCAT(IFNULL(SUBSTRING_INDEX(e.nombres, ' ', 1), ''), ' ', IFNULL(SUBSTRING_INDEX(e.apellidos, ' ', 1), '')) nombre \r\n" + 
						"FROM mat_estudiante e \r\n" + 
						"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
						"WHERE m.id_periodo = '" + soPeriodo + "' \r\n";		
		if (!soOferta.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOferta + "' \r\n"; }	
		jpql = jpql + "AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
				"ORDER BY 2";

		Query query = em.createNativeQuery(jpql);
		return query;
	}

	// ACCIONES

	// CLASES	

	// GETTERS AND SETTERS
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}	
	public String getSoSecuencia() {
		return soSecuencia;
	}
	public void setSoSecuencia(String soSecuencia) {
		this.soSecuencia = soSecuencia;
	}
	public ArrayList<SelectItem> getListPeriodos() {
		return listPeriodos;
	}
	public void setListPeriodos(ArrayList<SelectItem> listPeriodos) {
		this.listPeriodos = listPeriodos;
	}
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
	public List<Object> getPensionList() {
		return pensionList;
	}
	public void setPensionList(List<Object> pensionList) {
		this.pensionList = pensionList;
	}
	public ArrayList<SelectItem> getListOfertas() {
		return listOfertas;
	}
	public void setListOfertas(ArrayList<SelectItem> listOfertas) {
		this.listOfertas = listOfertas;
	}
	public String getSoEstudiante() {
		return soEstudiante;
	}
	public void setSoEstudiante(String soEstudiante) {
		this.soEstudiante = soEstudiante;
	}
	public ArrayList<SelectItem> getListEstudiantes() {
		return listEstudiantes;
	}
	public void setListEstudiantes(ArrayList<SelectItem> listEstudiantes) {
		this.listEstudiantes = listEstudiantes;
	}
	public String getSoTipo() {
		return soTipo;
	}
	public void setSoTipo(String soTipo) {
		this.soTipo = soTipo;
	}
}