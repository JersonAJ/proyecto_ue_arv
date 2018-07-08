package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_curso database table.
 * 
 */
@Entity
@Table(name="mat_curso")
@NamedQuery(name="MatCurso.findAll", query="SELECT m FROM MatCurso m")
public class MatCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_curso")
	private int idCurso;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private String nivel;

	private String subnivel;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to CalReparto
	@OneToMany(mappedBy="matCurso")
	private List<CalReparto> calRepartos;

	//bi-directional many-to-one association to MatOferta
	@OneToMany(mappedBy="matCurso")
	private List<MatOferta> matOfertas;

	public MatCurso() {
	}

	public int getIdCurso() {
		return this.idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
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

	public String getNivel() {
		return this.nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getSubnivel() {
		return this.subnivel;
	}

	public void setSubnivel(String subnivel) {
		this.subnivel = subnivel;
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
		calReparto.setMatCurso(this);

		return calReparto;
	}

	public CalReparto removeCalReparto(CalReparto calReparto) {
		getCalRepartos().remove(calReparto);
		calReparto.setMatCurso(null);

		return calReparto;
	}

	public List<MatOferta> getMatOfertas() {
		return this.matOfertas;
	}

	public void setMatOfertas(List<MatOferta> matOfertas) {
		this.matOfertas = matOfertas;
	}

	public MatOferta addMatOferta(MatOferta matOferta) {
		getMatOfertas().add(matOferta);
		matOferta.setMatCurso(this);

		return matOferta;
	}

	public MatOferta removeMatOferta(MatOferta matOferta) {
		getMatOfertas().remove(matOferta);
		matOferta.setMatCurso(null);

		return matOferta;
	}

}