package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogo_det database table.
 * 
 */
@Entity
@Table(name="catalogo_det")
@NamedQuery(name="CatalogoDet.findAll", query="SELECT c FROM CatalogoDet c")
public class CatalogoDet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codigo_det")
	private String codigoDet;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to CatalogoCab
	@ManyToOne
	@JoinColumn(name="codigo_cab")
	private CatalogoCab catalogoCab;

	public CatalogoDet() {
	}

	public String getCodigoDet() {
		return this.codigoDet;
	}

	public void setCodigoDet(String codigoDet) {
		this.codigoDet = codigoDet;
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

	public CatalogoCab getCatalogoCab() {
		return this.catalogoCab;
	}

	public void setCatalogoCab(CatalogoCab catalogoCab) {
		this.catalogoCab = catalogoCab;
	}

}