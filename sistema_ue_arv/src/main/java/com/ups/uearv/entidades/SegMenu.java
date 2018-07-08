package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the seg_menu database table.
 * 
 */
@Entity
@Table(name="seg_menu")
@NamedQuery(name="SegMenu.findAll", query="SELECT s FROM SegMenu s")
public class SegMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_menu")
	private int idMenu;

	private String descripcion;

	private String estado;

	private String ruta;

	//bi-directional many-to-many association to SegPerfil
	@ManyToMany
	@JoinTable(
		name="seg_perfil_menu"
		, joinColumns={
			@JoinColumn(name="id_menu")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_perfil")
			}
		)
	private List<SegPerfil> segPerfils;

	public SegMenu() {
	}

	public int getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
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

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public List<SegPerfil> getSegPerfils() {
		return this.segPerfils;
	}

	public void setSegPerfils(List<SegPerfil> segPerfils) {
		this.segPerfils = segPerfils;
	}

}