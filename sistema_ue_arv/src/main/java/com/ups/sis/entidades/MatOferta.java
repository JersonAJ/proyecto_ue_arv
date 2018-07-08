package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_oferta database table.
 * 
 */
@Entity
@Table(name="mat_oferta")
@NamedQuery(name="MatOferta.findAll", query="SELECT m FROM MatOferta m")
public class MatOferta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_oferta")
	private int idOferta;

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

	//bi-directional many-to-one association to MatMatricula
	@OneToMany(mappedBy="matOferta")
	private List<MatMatricula> matMatriculas;

	//bi-directional many-to-one association to MatPeriodo
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private MatPeriodo matPeriodo;

	//bi-directional many-to-one association to MatCurso
	@ManyToOne
	@JoinColumn(name="id_curso")
	private MatCurso matCurso;

	//bi-directional many-to-one association to MatParalelo
	@ManyToOne
	@JoinColumn(name="id_paralelo")
	private MatParalelo matParalelo;

	public MatOferta() {
	}

	public int getIdOferta() {
		return this.idOferta;
	}

	public void setIdOferta(int idOferta) {
		this.idOferta = idOferta;
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

	public List<MatMatricula> getMatMatriculas() {
		return this.matMatriculas;
	}

	public void setMatMatriculas(List<MatMatricula> matMatriculas) {
		this.matMatriculas = matMatriculas;
	}

	public MatMatricula addMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().add(matMatricula);
		matMatricula.setMatOferta(this);

		return matMatricula;
	}

	public MatMatricula removeMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().remove(matMatricula);
		matMatricula.setMatOferta(null);

		return matMatricula;
	}

	public MatPeriodo getMatPeriodo() {
		return this.matPeriodo;
	}

	public void setMatPeriodo(MatPeriodo matPeriodo) {
		this.matPeriodo = matPeriodo;
	}

	public MatCurso getMatCurso() {
		return this.matCurso;
	}

	public void setMatCurso(MatCurso matCurso) {
		this.matCurso = matCurso;
	}

	public MatParalelo getMatParalelo() {
		return this.matParalelo;
	}

	public void setMatParalelo(MatParalelo matParalelo) {
		this.matParalelo = matParalelo;
	}

}