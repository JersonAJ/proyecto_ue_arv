package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the cal_calificacion database table.
 * 
 */
@Entity
@Table(name="cal_calificacion")
@NamedQuery(name="CalCalificacion.findAll", query="SELECT c FROM CalCalificacion c")
public class CalCalificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_calificacion")
	private int idCalificacion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Column(name="q1_exam")
	private BigDecimal q1Exam;

	@Column(name="q1_exam_rec")
	private BigDecimal q1ExamRec;

	@Column(name="q1_parcial1")
	private BigDecimal q1Parcial1;

	@Column(name="q1_parcial2")
	private BigDecimal q1Parcial2;

	@Column(name="q1_parcial3")
	private BigDecimal q1Parcial3;

	@Column(name="q1_sn_rec")
	private String q1SnRec;

	@Column(name="q2_exam")
	private BigDecimal q2Exam;

	@Column(name="q2_exam_rec")
	private BigDecimal q2ExamRec;

	@Column(name="q2_parcial1")
	private BigDecimal q2Parcial1;

	@Column(name="q2_parcial2")
	private BigDecimal q2Parcial2;

	@Column(name="q2_parcial3")
	private BigDecimal q2Parcial3;

	@Column(name="q2_sn_rec")
	private String q2SnRec;

	private String recomendaciones;

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

	public CalCalificacion() {
	}

	public int getIdCalificacion() {
		return this.idCalificacion;
	}

	public void setIdCalificacion(int idCalificacion) {
		this.idCalificacion = idCalificacion;
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

	public BigDecimal getQ1Exam() {
		return this.q1Exam;
	}

	public void setQ1Exam(BigDecimal q1Exam) {
		this.q1Exam = q1Exam;
	}

	public BigDecimal getQ1ExamRec() {
		return this.q1ExamRec;
	}

	public void setQ1ExamRec(BigDecimal q1ExamRec) {
		this.q1ExamRec = q1ExamRec;
	}

	public BigDecimal getQ1Parcial1() {
		return this.q1Parcial1;
	}

	public void setQ1Parcial1(BigDecimal q1Parcial1) {
		this.q1Parcial1 = q1Parcial1;
	}

	public BigDecimal getQ1Parcial2() {
		return this.q1Parcial2;
	}

	public void setQ1Parcial2(BigDecimal q1Parcial2) {
		this.q1Parcial2 = q1Parcial2;
	}

	public BigDecimal getQ1Parcial3() {
		return this.q1Parcial3;
	}

	public void setQ1Parcial3(BigDecimal q1Parcial3) {
		this.q1Parcial3 = q1Parcial3;
	}

	public String getQ1SnRec() {
		return this.q1SnRec;
	}

	public void setQ1SnRec(String q1SnRec) {
		this.q1SnRec = q1SnRec;
	}

	public BigDecimal getQ2Exam() {
		return this.q2Exam;
	}

	public void setQ2Exam(BigDecimal q2Exam) {
		this.q2Exam = q2Exam;
	}

	public BigDecimal getQ2ExamRec() {
		return this.q2ExamRec;
	}

	public void setQ2ExamRec(BigDecimal q2ExamRec) {
		this.q2ExamRec = q2ExamRec;
	}

	public BigDecimal getQ2Parcial1() {
		return this.q2Parcial1;
	}

	public void setQ2Parcial1(BigDecimal q2Parcial1) {
		this.q2Parcial1 = q2Parcial1;
	}

	public BigDecimal getQ2Parcial2() {
		return this.q2Parcial2;
	}

	public void setQ2Parcial2(BigDecimal q2Parcial2) {
		this.q2Parcial2 = q2Parcial2;
	}

	public BigDecimal getQ2Parcial3() {
		return this.q2Parcial3;
	}

	public void setQ2Parcial3(BigDecimal q2Parcial3) {
		this.q2Parcial3 = q2Parcial3;
	}

	public String getQ2SnRec() {
		return this.q2SnRec;
	}

	public void setQ2SnRec(String q2SnRec) {
		this.q2SnRec = q2SnRec;
	}

	public String getRecomendaciones() {
		return this.recomendaciones;
	}

	public void setRecomendaciones(String recomendaciones) {
		this.recomendaciones = recomendaciones;
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

}