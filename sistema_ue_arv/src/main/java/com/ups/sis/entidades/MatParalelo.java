package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_paralelo database table.
 * 
 */
@Entity
@Table(name="mat_paralelo")
@NamedQuery(name="MatParalelo.findAll", query="SELECT m FROM MatParalelo m")
public class MatParalelo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_paralelo")
	private int idParalelo;

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

	//bi-directional many-to-one association to MatOferta
	@OneToMany(mappedBy="matParalelo")
	private List<MatOferta> matOfertas;

	public MatParalelo() {
	}

	public int getIdParalelo() {
		return this.idParalelo;
	}

	public void setIdParalelo(int idParalelo) {
		this.idParalelo = idParalelo;
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

	public List<MatOferta> getMatOfertas() {
		return this.matOfertas;
	}

	public void setMatOfertas(List<MatOferta> matOfertas) {
		this.matOfertas = matOfertas;
	}

	public MatOferta addMatOferta(MatOferta matOferta) {
		getMatOfertas().add(matOferta);
		matOferta.setMatParalelo(this);

		return matOferta;
	}

	public MatOferta removeMatOferta(MatOferta matOferta) {
		getMatOfertas().remove(matOferta);
		matOferta.setMatParalelo(null);

		return matOferta;
	}

}