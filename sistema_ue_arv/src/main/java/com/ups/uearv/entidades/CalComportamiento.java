package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cal_comportamiento database table.
 * 
 */
@Entity
@Table(name="cal_comportamiento")
@NamedQuery(name="CalComportamiento.findAll", query="SELECT c FROM CalComportamiento c")
public class CalComportamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_comportamiento")
	private int idComportamiento;

	private String abreviatura;

	private String descripcion;

	private String estado;
	
	private int equivalencia;

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

	//bi-directional many-to-one association to CalControl
	@OneToMany(mappedBy="calComportamiento")
	private List<CalControl> calControls;

	public CalComportamiento() {
	}

	public int getIdComportamiento() {
		return this.idComportamiento;
	}

	public void setIdComportamiento(int idComportamiento) {
		this.idComportamiento = idComportamiento;
	}

	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
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

	public List<CalControl> getCalControls() {
		return this.calControls;
	}

	public void setCalControls(List<CalControl> calControls) {
		this.calControls = calControls;
	}

	public CalControl addCalControl(CalControl calControl) {
		getCalControls().add(calControl);
		calControl.setCalComportamiento(this);

		return calControl;
	}

	public CalControl removeCalControl(CalControl calControl) {
		getCalControls().remove(calControl);
		calControl.setCalComportamiento(null);

		return calControl;
	}

	public int getEquivalencia() {
		return equivalencia;
	}

	public void setEquivalencia(int equivalencia) {
		this.equivalencia = equivalencia;
	}

}