package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cal_asistencia database table.
 * 
 */
@Entity
@Table(name="cal_asistencia")
@NamedQuery(name="CalAsistencia.findAll", query="SELECT c FROM CalAsistencia c")
public class CalAsistencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_asistencia")
	private int idAsistencia;

	private int asistencias;

	private int atrasos;

	private int faltas;

	private int justificados;

	private String observacion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private int parcial;

	private int quimestre;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="proyectos_esc")
	private String proyectosEsc;

	//bi-directional many-to-one association to MatMatricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private MatMatricula matMatricula;

	//bi-directional many-to-one association to CalComportamiento
	@ManyToOne
	@JoinColumn(name="id_comportamiento")
	private CalComportamiento calComportamiento;

	@Column(name="usuario_ing")
	private String usuarioIng;

	public CalAsistencia() {
	}

	public int getIdAsistencia() {
		return this.idAsistencia;
	}

	public void setIdAsistencia(int idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public int getAsistencias() {
		return this.asistencias;
	}

	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}

	public int getAtrasos() {
		return this.atrasos;
	}

	public void setAtrasos(int atrasos) {
		this.atrasos = atrasos;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaAct() {
		return this.fechaAct;
	}

	public void setFechaAct(Date fechaAct) {
		this.fechaAct = fechaAct;
	}

	public Date getFechaIng() {
		return this.fechaIng;
	}

	public void setFechaIng(Date fechaIng) {
		this.fechaIng = fechaIng;
	}

	public int getParcial() {
		return this.parcial;
	}

	public void setParcial(int parcial) {
		this.parcial = parcial;
	}

	public int getQuimestre() {
		return this.quimestre;
	}

	public void setQuimestre(int quimestre) {
		this.quimestre = quimestre;
	}

	public String getUsuarioAct() {
		return this.usuarioAct;
	}

	public void setUsuarioAct(String usuarioAct) {
		this.usuarioAct = usuarioAct;
	}

	public String getUsuarioIng() {
		return this.usuarioIng;
	}

	public void setUsuarioIng(String usuarioIng) {
		this.usuarioIng = usuarioIng;
	}

	public MatMatricula getMatMatricula() {
		return this.matMatricula;
	}

	public void setMatMatricula(MatMatricula matMatricula) {
		this.matMatricula = matMatricula;
	}

	public int getFaltas() {
		return faltas;
	}

	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}

	public int getJustificados() {
		return justificados;
	}

	public void setJustificados(int justificados) {
		this.justificados = justificados;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public CalComportamiento getCalComportamiento() {
		return this.calComportamiento;
	}

	public void setCalComportamiento(CalComportamiento calComportamiento) {
		this.calComportamiento = calComportamiento;
	}

	public String getProyectosEsc() {
		return proyectosEsc;
	}

	public void setProyectosEsc(String proyectosEsc) {
		this.proyectosEsc = proyectosEsc;
	}
}