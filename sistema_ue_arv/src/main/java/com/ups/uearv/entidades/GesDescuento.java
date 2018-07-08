package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ges_descuento database table.
 * 
 */
@Entity
@Table(name="ges_descuento")
@NamedQuery(name="GesDescuento.findAll", query="SELECT g FROM GesDescuento g")
public class GesDescuento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_descuento")
	private int idDescuento;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	private BigDecimal porcentaje;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to GesPension
	@OneToMany(mappedBy="gesDescuento")
	private List<GesPension> gesPensions;

	public GesDescuento() {
	}

	public int getIdDescuento() {
		return this.idDescuento;
	}

	public void setIdDescuento(int idDescuento) {
		this.idDescuento = idDescuento;
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

	public BigDecimal getPorcentaje() {
		return this.porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
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

	public List<GesPension> getGesPensions() {
		return this.gesPensions;
	}

	public void setGesPensions(List<GesPension> gesPensions) {
		this.gesPensions = gesPensions;
	}

	public GesPension addGesPension(GesPension gesPension) {
		getGesPensions().add(gesPension);
		gesPension.setGesDescuento(this);

		return gesPension;
	}

	public GesPension removeGesPension(GesPension gesPension) {
		getGesPensions().remove(gesPension);
		gesPension.setGesDescuento(null);

		return gesPension;
	}

}