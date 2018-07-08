package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_docente database table.
 * 
 */
@Entity
@Table(name="mat_docente")
@NamedQuery(name="MatDocente.findAll", query="SELECT m FROM MatDocente m")
public class MatDocente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_docente")
	private String idDocente;

	private String apellidos;

	private String correo;

	private String direccion;

	private String estado;

	@Column(name="estado_civil")
	private String estadoCivil;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_nac")
	private Date fechaNac;

	private String genero;

	private String nombres;

	private String telefono1;

	private String telefono2;

	private String titulo;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to CalReparto
	@OneToMany(mappedBy="matDocente")
	private List<CalReparto> calRepartos;

	public MatDocente() {
	}

	public String getIdDocente() {
		return this.idDocente;
	}

	public void setIdDocente(String idDocente) {
		this.idDocente = idDocente;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
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

	public Date getFechaNac() {
		return this.fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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

	public List<CalReparto> getCalRepartos() {
		return this.calRepartos;
	}

	public void setCalRepartos(List<CalReparto> calRepartos) {
		this.calRepartos = calRepartos;
	}

	public CalReparto addCalReparto(CalReparto calReparto) {
		getCalRepartos().add(calReparto);
		calReparto.setMatDocente(this);

		return calReparto;
	}

	public CalReparto removeCalReparto(CalReparto calReparto) {
		getCalRepartos().remove(calReparto);
		calReparto.setMatDocente(null);

		return calReparto;
	}

}