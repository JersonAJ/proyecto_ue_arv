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

import com.ups.uearv.servicios.DAO;
import com.ups.uearv.servicios.Util;

/**
 * @author Jerson Armijos Jaén - Raysa Solano
 * @version 1.0
 */

@ManagedBean(name = "reporte")
@ViewScoped
public class Reporte implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sismacc");
	private static EntityManager em = emf.createEntityManager();	
	
	private static final long serialVersionUID = 1L;

	String soPeriodoCal = "";
	String soOfertaCal = "";
	String soEstudianteCal = "";
	
	String soQuimestre = "";
	String soParcial = "";

	ArrayList<SelectItem> listPeriodoCal = new ArrayList<SelectItem>();
	ArrayList<SelectItem> listOfertaCal = new ArrayList<SelectItem>();		
	ArrayList<SelectItem> listEstudianteCal = new ArrayList<SelectItem>();

	@PostConstruct
	public void init() {		
		listPeriodoCal = (ArrayList<SelectItem>) llenaComboPeriodo();
		soPeriodoCal = "NA";

		soQuimestre = "1";
		soParcial = "1";
		
		onChangePeriodoCal();
	}

	// CONSULTA	
	public void closeDialogo() {
		init();
	}

	public void onChangePeriodoCal() {
		listOfertaCal = (ArrayList<SelectItem>) llenaComboOferta();
		soOfertaCal = "NA";

		onChangeOfertaCal();
	}

	public void onChangeOfertaCal() {
		listEstudianteCal = (ArrayList<SelectItem>) llenaComboEstudiante();
		soEstudianteCal = "NA";
	}

	public void verLibretaCalifinaciones() {

	}

	public List<SelectItem> llenaComboPeriodo() {
		return Util.llenaCombo(DAO.getPeriodos(), 2);
	}

	public List<SelectItem> llenaComboOferta() {
		return Util.llenaCombo(DAO.getOfertas(soPeriodoCal), 2);
	}

	public List<SelectItem> llenaComboEstudiante() {
		return Util.llenaCombo(getEstudiantesOferta(), 2);
	}
	
	public Query getEstudiantesOferta() {		
		String jpql = 
		"SELECT e.id_estudiante, CONCAT(IFNULL(e.apellidos, ''), ' ', IFNULL(e.nombres, '')) nombre \r\n" + 
		"FROM mat_estudiante e \r\n" + 
		"	INNER JOIN mat_matricula m ON m.id_estudiante = e.id_estudiante \r\n" + 
		"WHERE m.id_periodo = '" + soPeriodoCal + "' \r\n";		
		if (!soOfertaCal.equals("NA")) { jpql = jpql + "AND m.id_oferta = '" + soOfertaCal + "' \r\n"; }	
		jpql = jpql + "AND m.estado = 'AC' AND e.estado = 'AC' AND m.sn_aprobado = 'S' \r\n" + 
		"ORDER BY 2";

		Query query = em.createNativeQuery(jpql);
		return query;
	}
		
	// GETTERS AND SETTERS
	public String getSoPeriodoCal() {
		return soPeriodoCal;
	}
	public void setSoPeriodoCal(String soPeriodoCal) {
		this.soPeriodoCal = soPeriodoCal;
	}
	public String getSoOfertaCal() {
		return soOfertaCal;
	}
	public void setSoOfertaCal(String soOfertaCal) {
		this.soOfertaCal = soOfertaCal;
	}
	public String getSoEstudianteCal() {
		return soEstudianteCal;
	}
	public void setSoEstudianteCal(String soEstudianteCal) {
		this.soEstudianteCal = soEstudianteCal;
	}
	public ArrayList<SelectItem> getListPeriodoCal() {
		return listPeriodoCal;
	}
	public void setListPeriodoCal(ArrayList<SelectItem> listPeriodoCal) {
		this.listPeriodoCal = listPeriodoCal;
	}
	public ArrayList<SelectItem> getListOfertaCal() {
		return listOfertaCal;
	}
	public void setListOfertaCal(ArrayList<SelectItem> listOfertaCal) {
		this.listOfertaCal = listOfertaCal;
	}
	public ArrayList<SelectItem> getListEstudianteCal() {
		return listEstudianteCal;
	}
	public void setListEstudianteCal(ArrayList<SelectItem> listEstudianteCal) {
		this.listEstudianteCal = listEstudianteCal;
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
}