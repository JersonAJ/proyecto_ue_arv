package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogo_cab database table.
 * 
 */
@Entity
@Table(name="catalogo_cab")
@NamedQuery(name="CatalogoCab.findAll", query="SELECT c FROM CatalogoCab c")
public class CatalogoCab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codigo_cab")
	private String codigoCab;

	private String descripcion;

	private String estado;

	private String grupo;

	//bi-directional many-to-one association to CatalogoDet
	@OneToMany(mappedBy="catalogoCab")
	private List<CatalogoDet> catalogoDets;

	public CatalogoCab() {
	}

	public String getCodigoCab() {
		return this.codigoCab;
	}

	public void setCodigoCab(String codigoCab) {
		this.codigoCab = codigoCab;
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

	public String getGrupo() {
		return this.grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public List<CatalogoDet> getCatalogoDets() {
		return this.catalogoDets;
	}

	public void setCatalogoDets(List<CatalogoDet> catalogoDets) {
		this.catalogoDets = catalogoDets;
	}

	public CatalogoDet addCatalogoDet(CatalogoDet catalogoDet) {
		getCatalogoDets().add(catalogoDet);
		catalogoDet.setCatalogoCab(this);

		return catalogoDet;
	}

	public CatalogoDet removeCatalogoDet(CatalogoDet catalogoDet) {
		getCatalogoDets().remove(catalogoDet);
		catalogoDet.setCatalogoCab(null);

		return catalogoDet;
	}

}