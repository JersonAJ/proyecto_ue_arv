package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_representante database table.
 * 
 */
@Entity
@Table(name="mat_representante")
@NamedQuery(name="MatRepresentante.findAll", query="SELECT m FROM MatRepresentante m")
public class MatRepresentante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_representante")
	private String idRepresentante;

	private String apellidos;

	private String correo;

	private String direccion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_nac")
	private Date fechaNac;

	private String nombres;

	private String ocupacion;

	private String telefono;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatEstudiante
	@OneToMany(mappedBy="matRepresentante")
	private List<MatEstudiante> matEstudiantes;

	public MatRepresentante() {
	}

	public String getIdRepresentante() {
		return this.idRepresentante;
	}

	public void setIdRepresentante(String idRepresentante) {
		this.idRepresentante = idRepresentante;
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

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getOcupacion() {
		return this.ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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

	public List<MatEstudiante> getMatEstudiantes() {
		return this.matEstudiantes;
	}

	public void setMatEstudiantes(List<MatEstudiante> matEstudiantes) {
		this.matEstudiantes = matEstudiantes;
	}

	public MatEstudiante addMatEstudiante(MatEstudiante matEstudiante) {
		getMatEstudiantes().add(matEstudiante);
		matEstudiante.setMatRepresentante(this);

		return matEstudiante;
	}

	public MatEstudiante removeMatEstudiante(MatEstudiante matEstudiante) {
		getMatEstudiantes().remove(matEstudiante);
		matEstudiante.setMatRepresentante(null);

		return matEstudiante;
	}

}