package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cal_falta database table.
 * 
 */
@Entity
@Table(name="cal_falta")
@NamedQuery(name="CalFalta.findAll", query="SELECT c FROM CalFalta c")
public class CalFalta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_falta")
	private int idFalta;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_fal")
	private Date fechaFal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private String motivo;

	@Column(name="sn_justificada")
	private String snJustificada;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatMatricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private MatMatricula matMatricula;

	public CalFalta() {
	}

	public int getIdFalta() {
		return this.idFalta;
	}

	public void setIdFalta(int idFalta) {
		this.idFalta = idFalta;
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

	public Date getFechaFal() {
		return this.fechaFal;
	}

	public void setFechaFal(Date fechaFal) {
		this.fechaFal = fechaFal;
	}

	public Date getFechaIng() {
		return this.fechaIng;
	}

	public void setFechaIng(Date fechaIng) {
		this.fechaIng = fechaIng;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getSnJustificada() {
		return this.snJustificada;
	}

	public void setSnJustificada(String snJustificada) {
		this.snJustificada = snJustificada;
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

}