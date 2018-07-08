package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cal_asignatura database table.
 * 
 */
@Entity
@Table(name="cal_asignatura")
@NamedQuery(name="CalAsignatura.findAll", query="SELECT c FROM CalAsignatura c")
public class CalAsignatura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_asignatura")
	private int idAsignatura;

	private String area;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private int horas;

	private String nombre;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to CalCalificacion
	@OneToMany(mappedBy="calAsignatura")
	private List<CalCalificacion> calCalificacions;

	//bi-directional many-to-one association to CalControl
	@OneToMany(mappedBy="calAsignatura")
	private List<CalControl> calControls;

	//bi-directional many-to-one association to CalReparto
	@OneToMany(mappedBy="calAsignatura")
	private List<CalReparto> calRepartos;

	public CalAsignatura() {
	}

	public int getIdAsignatura() {
		return this.idAsignatura;
	}

	public void setIdAsignatura(int idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public int getHoras() {
		return this.horas;
	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public List<CalCalificacion> getCalCalificacions() {
		return this.calCalificacions;
	}

	public void setCalCalificacions(List<CalCalificacion> calCalificacions) {
		this.calCalificacions = calCalificacions;
	}

	public CalCalificacion addCalCalificacion(CalCalificacion calCalificacion) {
		getCalCalificacions().add(calCalificacion);
		calCalificacion.setCalAsignatura(this);

		return calCalificacion;
	}

	public CalCalificacion removeCalCalificacion(CalCalificacion calCalificacion) {
		getCalCalificacions().remove(calCalificacion);
		calCalificacion.setCalAsignatura(null);

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
		calControl.setCalAsignatura(this);

		return calControl;
	}

	public CalControl removeCalControl(CalControl calControl) {
		getCalControls().remove(calControl);
		calControl.setCalAsignatura(null);

		return calControl;
	}

	public List<CalReparto> getCalRepartos() {
		return this.calRepartos;
	}

	public void setCalRepartos(List<CalReparto> calRepartos) {
		this.calRepartos = calRepartos;
	}

	public CalReparto addCalReparto(CalReparto calReparto) {
		getCalRepartos().add(calReparto);
		calReparto.setCalAsignatura(this);

		return calReparto;
	}

	public CalReparto removeCalReparto(CalReparto calReparto) {
		getCalRepartos().remove(calReparto);
		calReparto.setCalAsignatura(null);

		return calReparto;
	}

}