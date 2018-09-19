package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the cal_escala database table.
 * 
 */
@Entity
@Table(name="cal_escala")
@NamedQuery(name="CalEscala.findAll", query="SELECT c FROM CalEscala c")
public class CalEscala implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_escala")
	private int idEscala;

	private String cualitativa;

	@Column(name="cuantitativa_desde")
	private BigDecimal cuantitativaDesde;
	
	@Column(name="cuantitativa_hasta")
	private BigDecimal cuantitativaHasta;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	public CalEscala() {
	}

	public int getIdEscala() {
		return this.idEscala;
	}

	public void setIdEscala(int idEscala) {
		this.idEscala = idEscala;
	}
	
	public String getCualitativa() {
		return cualitativa;
	}

	public void setCualitativa(String cualitativa) {
		this.cualitativa = cualitativa;
	}

	public BigDecimal getCuantitativaDesde() {
		return cuantitativaDesde;
	}

	public void setCuantitativaDesde(BigDecimal cuantitativaDesde) {
		this.cuantitativaDesde = cuantitativaDesde;
	}

	public BigDecimal getCuantitativaHasta() {
		return cuantitativaHasta;
	}

	public void setCuantitativaHasta(BigDecimal cuantitativaHasta) {
		this.cuantitativaHasta = cuantitativaHasta;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

}