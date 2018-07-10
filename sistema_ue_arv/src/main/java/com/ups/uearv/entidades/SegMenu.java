package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;


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

	@Column(name="id_padre")
	private int idPadre;

	
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

	public int getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(int idPadre) {
		this.idPadre = idPadre;
	}
}