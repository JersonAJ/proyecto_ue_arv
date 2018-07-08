package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cal_reparto database table.
 * 
 */
@Entity
@Table(name="cal_reparto")
@NamedQuery(name="CalReparto.findAll", query="SELECT c FROM CalReparto c")
public class CalReparto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_reparto")
	private int idReparto;

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

	//bi-directional many-to-one association to CalControl
	@OneToMany(mappedBy="calReparto")
	private List<CalControl> calControls;

	//bi-directional many-to-one association to MatCurso
	@ManyToOne
	@JoinColumn(name="id_curso")
	private MatCurso matCurso;

	//bi-directional many-to-one association to MatDocente
	@ManyToOne
	@JoinColumn(name="id_docente")
	private MatDocente matDocente;

	//bi-directional many-to-one association to CalAsignatura
	@ManyToOne
	@JoinColumn(name="id_asignatura")
	private CalAsignatura calAsignatura;

	public CalReparto() {
	}

	public int getIdReparto() {
		return this.idReparto;
	}

	public void setIdReparto(int idReparto) {
		this.idReparto = idReparto;
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
		calControl.setCalReparto(this);

		return calControl;
	}

	public CalControl removeCalControl(CalControl calControl) {
		getCalControls().remove(calControl);
		calControl.setCalReparto(null);

		return calControl;
	}

	public MatCurso getMatCurso() {
		return this.matCurso;
	}

	public void setMatCurso(MatCurso matCurso) {
		this.matCurso = matCurso;
	}

	public MatDocente getMatDocente() {
		return this.matDocente;
	}

	public void setMatDocente(MatDocente matDocente) {
		this.matDocente = matDocente;
	}

	public CalAsignatura getCalAsignatura() {
		return this.calAsignatura;
	}

	public void setCalAsignatura(CalAsignatura calAsignatura) {
		this.calAsignatura = calAsignatura;
	}

}