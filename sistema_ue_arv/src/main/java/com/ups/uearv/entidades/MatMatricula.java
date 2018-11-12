package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_matricula database table.
 * 
 */
@Entity
@Table(name="mat_matricula")
@NamedQuery(name="MatMatricula.findAll", query="SELECT m FROM MatMatricula m")
public class MatMatricula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_matricula")
	private int idMatricula;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private String observaciones;

	@Column(name="sn_aprobado")
	private String snAprobado;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to CalAsistencia
	@OneToMany(mappedBy="matMatricula")
	private List<CalAsistencia> calAsistencias;

	//bi-directional many-to-one association to CalCalificacion
	@OneToMany(mappedBy="matMatricula")
	private List<CalCalificacion> calCalificacions;

	//bi-directional many-to-one association to CalControl
	@OneToMany(mappedBy="matMatricula")
	private List<CalControl> calControls;
	
	//bi-directional many-to-one association to GesPension
	@OneToMany(mappedBy="matMatricula")
	private List<GesPension> gesPensions;

	//bi-directional many-to-one association to MatEstudiante
	@ManyToOne
	@JoinColumn(name="id_estudiante")
	private MatEstudiante matEstudiante;

	//bi-directional many-to-one association to MatOferta
	@ManyToOne
	@JoinColumn(name="id_oferta")
	private MatOferta matOferta;

	//bi-directional many-to-one association to MatPeriodo
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private MatPeriodo matPeriodo;

	public MatMatricula() {
	}

	public int getIdMatricula() {
		return this.idMatricula;
	}

	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
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

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getSnAprobado() {
		return this.snAprobado;
	}

	public void setSnAprobado(String snAprobado) {
		this.snAprobado = snAprobado;
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

	public List<CalAsistencia> getCalAsistencias() {
		return this.calAsistencias;
	}

	public void setCalAsistencias(List<CalAsistencia> calAsistencias) {
		this.calAsistencias = calAsistencias;
	}

	public CalAsistencia addCalAsistencia(CalAsistencia calAsistencia) {
		getCalAsistencias().add(calAsistencia);
		calAsistencia.setMatMatricula(this);

		return calAsistencia;
	}

	public CalAsistencia removeCalAsistencia(CalAsistencia calAsistencia) {
		getCalAsistencias().remove(calAsistencia);
		calAsistencia.setMatMatricula(null);

		return calAsistencia;
	}

	public List<CalCalificacion> getCalCalificacions() {
		return this.calCalificacions;
	}

	public void setCalCalificacions(List<CalCalificacion> calCalificacions) {
		this.calCalificacions = calCalificacions;
	}

	public CalCalificacion addCalCalificacion(CalCalificacion calCalificacion) {
		getCalCalificacions().add(calCalificacion);
		calCalificacion.setMatMatricula(this);

		return calCalificacion;
	}

	public CalCalificacion removeCalCalificacion(CalCalificacion calCalificacion) {
		getCalCalificacions().remove(calCalificacion);
		calCalificacion.setMatMatricula(null);

		return calCalificacion;
	}

	public List<CalControl> getCalControls() {
		return this.calControls;
	}

	public void setCalControls(List<CalControl> calControls) {
		this.calControls = calControls;
	}

	public CalControl addCalControl(CalControl calControl) {
		getCalControls().add(calControl);
		calControl.setMatMatricula(this);

		return calControl;
	}

	public CalControl removeCalControl(CalControl calControl) {
		getCalControls().remove(calControl);
		calControl.setMatMatricula(null);

		return calControl;
	}

	public List<GesPension> getGesPensions() {
		return this.gesPensions;
	}

	public void setGesPensions(List<GesPension> gesPensions) {
		this.gesPensions = gesPensions;
	}

	public GesPension addGesPension(GesPension gesPension) {
		getGesPensions().add(gesPension);
		gesPension.setMatMatricula(this);

		return gesPension;
	}

	public GesPension removeGesPension(GesPension gesPension) {
		getGesPensions().remove(gesPension);
		gesPension.setMatMatricula(null);

		return gesPension;
	}

	public MatEstudiante getMatEstudiante() {
		return this.matEstudiante;
	}

	public void setMatEstudiante(MatEstudiante matEstudiante) {
		this.matEstudiante = matEstudiante;
	}

	public MatOferta getMatOferta() {
		return this.matOferta;
	}

	public void setMatOferta(MatOferta matOferta) {
		this.matOferta = matOferta;
	}

	public MatPeriodo getMatPeriodo() {
		return this.matPeriodo;
	}

	public void setMatPeriodo(MatPeriodo matPeriodo) {
		this.matPeriodo = matPeriodo;
	}

}