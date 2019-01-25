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

	private String estado;

	private BigDecimal evaluacion;
	
	private BigDecimal exam;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private int parcial;

	private int quimestre;

	private BigDecimal leccion1;
	private BigDecimal leccion2;
	private BigDecimal leccion3;
	private BigDecimal leccion4;
	private BigDecimal leccion5;
	
	private BigDecimal tarea1;
	private BigDecimal tarea2;
	private BigDecimal tarea3;
	private BigDecimal tarea4;
	private BigDecimal tarea5;
	
	@Column(name="act_individual1")
	private BigDecimal actIndividual1;
	@Column(name="act_individual2")
	private BigDecimal actIndividual2;
	@Column(name="act_individual3")
	private BigDecimal actIndividual3;
	@Column(name="act_individual4")
	private BigDecimal actIndividual4;
	@Column(name="act_individual5")
	private BigDecimal actIndividual5;
	
	@Column(name="act_grupal1")
	private BigDecimal actGrupal1;
	@Column(name="act_grupal2")
	private BigDecimal actGrupal2;
	@Column(name="act_grupal3")
	private BigDecimal actGrupal3;
	@Column(name="act_grupal4")
	private BigDecimal actGrupal4;
	@Column(name="act_grupal5")
	private BigDecimal actGrupal5;	

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
		
	public BigDecimal getLeccion1() {
		return leccion1;
	}

	public void setLeccion1(BigDecimal leccion1) {
		this.leccion1 = leccion1;
	}

	public BigDecimal getLeccion2() {
		return leccion2;
	}

	public void setLeccion2(BigDecimal leccion2) {
		this.leccion2 = leccion2;
	}

	public BigDecimal getLeccion3() {
		return leccion3;
	}

	public void setLeccion3(BigDecimal leccion3) {
		this.leccion3 = leccion3;
	}

	public BigDecimal getLeccion4() {
		return leccion4;
	}

	public void setLeccion4(BigDecimal leccion4) {
		this.leccion4 = leccion4;
	}

	public BigDecimal getLeccion5() {
		return leccion5;
	}

	public void setLeccion5(BigDecimal leccion5) {
		this.leccion5 = leccion5;
	}

	public BigDecimal getTarea1() {
		return tarea1;
	}

	public void setTarea1(BigDecimal tarea1) {
		this.tarea1 = tarea1;
	}

	public BigDecimal getTarea2() {
		return tarea2;
	}

	public void setTarea2(BigDecimal tarea2) {
		this.tarea2 = tarea2;
	}

	public BigDecimal getTarea3() {
		return tarea3;
	}

	public void setTarea3(BigDecimal tarea3) {
		this.tarea3 = tarea3;
	}

	public BigDecimal getTarea4() {
		return tarea4;
	}

	public void setTarea4(BigDecimal tarea4) {
		this.tarea4 = tarea4;
	}

	public BigDecimal getTarea5() {
		return tarea5;
	}

	public void setTarea5(BigDecimal tarea5) {
		this.tarea5 = tarea5;
	}

	
	public BigDecimal getActIndividual1() {
		return actIndividual1;
	}

	public void setActIndividual1(BigDecimal actIndividual1) {
		this.actIndividual1 = actIndividual1;
	}

	public BigDecimal getActIndividual2() {
		return actIndividual2;
	}

	public void setActIndividual2(BigDecimal actIndividual2) {
		this.actIndividual2 = actIndividual2;
	}

	public BigDecimal getActIndividual3() {
		return actIndividual3;
	}

	public void setActIndividual3(BigDecimal actIndividual3) {
		this.actIndividual3 = actIndividual3;
	}

	public BigDecimal getActIndividual4() {
		return actIndividual4;
	}

	public void setActIndividual4(BigDecimal actIndividual4) {
		this.actIndividual4 = actIndividual4;
	}

	public BigDecimal getActIndividual5() {
		return actIndividual5;
	}

	public void setActIndividual5(BigDecimal actIndividual5) {
		this.actIndividual5 = actIndividual5;
	}

	public BigDecimal getActGrupal1() {
		return actGrupal1;
	}

	public void setActGrupal1(BigDecimal actGrupal1) {
		this.actGrupal1 = actGrupal1;
	}

	public BigDecimal getActGrupal2() {
		return actGrupal2;
	}

	public void setActGrupal2(BigDecimal actGrupal2) {
		this.actGrupal2 = actGrupal2;
	}

	public BigDecimal getActGrupal3() {
		return actGrupal3;
	}

	public void setActGrupal3(BigDecimal actGrupal3) {
		this.actGrupal3 = actGrupal3;
	}

	public BigDecimal getActGrupal4() {
		return actGrupal4;
	}

	public void setActGrupal4(BigDecimal actGrupal4) {
		this.actGrupal4 = actGrupal4;
	}

	public BigDecimal getActGrupal5() {
		return actGrupal5;
	}

	public void setActGrupal5(BigDecimal actGrupal5) {
		this.actGrupal5 = actGrupal5;
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

	public BigDecimal getEvaluacion() {
		return evaluacion;
	}

	public void setEvaluacion(BigDecimal evaluacion) {
		this.evaluacion = evaluacion;
	}
}