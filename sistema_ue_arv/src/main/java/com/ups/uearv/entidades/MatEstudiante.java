package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_estudiante database table.
 * 
 */
@Entity
@Table(name="mat_estudiante")
@NamedQuery(name="MatEstudiante.findAll", query="SELECT m FROM MatEstudiante m")
public class MatEstudiante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_estudiante")
	private String idEstudiante;

	private String alergias;

	private String apellidos;

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

	private String genero;

	@Column(name="institucion_ant")
	private String institucionAnt;

	private String nombres;

	private String telefono;

	@Column(name="tipo_sangre")
	private String tipoSangre;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatRepresentante
	@ManyToOne
	@JoinColumn(name="id_representante")
	private MatRepresentante matRepresentante;

	//bi-directional many-to-one association to MatMatricula
	@OneToMany(mappedBy="matEstudiante")
	private List<MatMatricula> matMatriculas;

	public MatEstudiante() {
	}

	public String getIdEstudiante() {
		return this.idEstudiante;
	}

	public void setIdEstudiante(String idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

	public String getAlergias() {
		return this.alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
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

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getInstitucionAnt() {
		return this.institucionAnt;
	}

	public void setInstitucionAnt(String institucionAnt) {
		this.institucionAnt = institucionAnt;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
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

	public MatRepresentante getMatRepresentante() {
		return this.matRepresentante;
	}

	public void setMatRepresentante(MatRepresentante matRepresentante) {
		this.matRepresentante = matRepresentante;
	}

	public List<MatMatricula> getMatMatriculas() {
		return this.matMatriculas;
	}

	public void setMatMatriculas(List<MatMatricula> matMatriculas) {
		this.matMatriculas = matMatriculas;
	}

	public MatMatricula addMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().add(matMatricula);
		matMatricula.setMatEstudiante(this);

		return matMatricula;
	}

	public MatMatricula removeMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().remove(matMatricula);
		matMatricula.setMatEstudiante(null);

		return matMatricula;
	}

}