package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the cal_control database table.
 * 
 */
@Entity
@Table(name="cal_control")
@NamedQuery(name="CalControl.findAll", query="SELECT c FROM CalControl c")
public class CalControl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_control")
	private int idControl;

	private BigDecimal deber;

	private String estado;

	private BigDecimal exam;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private BigDecimal leccion;

	private int parcial;

	private int quimestre;

	private BigDecimal taller;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatMatricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private MatMatricula matMatricula;

	//bi-directional many-to-one association to CalAsignatura
	@ManyToOne
	@JoinColumn(name="id_asignatura")
	private CalAsignatura calAsignatura;

	//bi-directional many-to-one association to CalComportamiento
	@ManyToOne
	@JoinColumn(name="id_comportamiento")
	private CalComportamiento calComportamiento;

	//bi-directional many-to-one association to CalReparto
	@ManyToOne
	@JoinColumn(name="id_reparto")
	private CalReparto calReparto;

	public CalControl() {
	}

	public int getIdControl() {
		return this.idControl;
	}

	public void setIdControl(int idControl) {
		this.idControl = idControl;
	}

	public BigDecimal getDeber() {
		return this.deber;
	}

	public void setDeber(BigDecimal deber) {
		this.deber = deber;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public BigDecimal getExam() {
		return this.exam;
	}

	public void setExam(BigDecimal exam) {
		this.exam = exam;
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

	public BigDecimal getLeccion() {
		return this.leccion;
	}

	public void setLeccion(BigDecimal leccion) {
		this.leccion = leccion;
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

	public BigDecimal getTaller() {
		return this.taller;
	}

	public void setTaller(BigDecimal taller) {
		this.taller = taller;
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

	public CalAsignatura getCalAsignatura() {
		return this.calAsignatura;
	}

	public void setCalAsignatura(CalAsignatura calAsignatura) {
		this.calAsignatura = calAsignatura;
	}

	public CalComportamiento getCalComportamiento() {
		return this.calComportamiento;
	}

	public void setCalComportamiento(CalComportamiento calComportamiento) {
		this.calComportamiento = calComportamiento;
	}

	public CalReparto getCalReparto() {
		return this.calReparto;
	}

	public void setCalReparto(CalReparto calReparto) {
		this.calReparto = calReparto;
	}

}