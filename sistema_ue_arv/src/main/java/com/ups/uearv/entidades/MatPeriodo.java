package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_periodo database table.
 * 
 */
@Entity
@Table(name="mat_periodo")
@NamedQuery(name="MatPeriodo.findAll", query="SELECT m FROM MatPeriodo m")
public class MatPeriodo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_periodo")
	private int idPeriodo;

	@Column(name="cant_pensiones")
	private int cantPensiones;
	
	@Column(name="sn_cerrado")
	private String snCerrado;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ini")
	private Date fechaIni;

	private String jornada;

	@Column(name="precio_matricula")
	private BigDecimal precioMatricula;

	@Column(name="precio_pension")
	private BigDecimal precioPension;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatOferta
	@OneToMany(mappedBy="matPeriodo")
	private List<MatOferta> matOfertas;

	public MatPeriodo() {
	}

	public int getIdPeriodo() {
		return this.idPeriodo;
	}

	public void setIdPeriodo(int idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public int getCantPensiones() {
		return this.cantPensiones;
	}

	public void setCantPensiones(int cantPensiones) {
		this.cantPensiones = cantPensiones;
	}

	public String getSnCerrado() {
		return snCerrado;
	}

	public void setSnCerrado(String snCerrado) {
		this.snCerrado = snCerrado;
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

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaIng() {
		return this.fechaIng;
	}

	public void setFechaIng(Date fechaIng) {
		this.fechaIng = fechaIng;
	}

	public Date getFechaIni() {
		return this.fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public String getJornada() {
		return this.jornada;
	}

	public void setJornada(String jornada) {
		this.jornada = jornada;
	}

	public BigDecimal getPrecioMatricula() {
		return this.precioMatricula;
	}

	public void setPrecioMatricula(BigDecimal precioMatricula) {
		this.precioMatricula = precioMatricula;
	}

	public BigDecimal getPrecioPension() {
		return this.precioPension;
	}

	public void setPrecioPension(BigDecimal precioPension) {
		this.precioPension = precioPension;
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
		matOferta.setMatPeriodo(this);

		return matOferta;
	}

	public MatOferta removeMatOferta(MatOferta matOferta) {
		getMatOfertas().remove(matOferta);
		matOferta.setMatPeriodo(null);

		return matOferta;
	}

}