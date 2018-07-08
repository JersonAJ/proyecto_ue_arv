package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the seg_perfil_menu database table.
 * 
 */
@Entity
@Table(name="seg_perfil_menu")
@NamedQuery(name="SegPerfilMenu.findAll", query="SELECT s FROM SegPerfilMenu s")
public class SegPerfilMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private SegPerfilMenuPK id;

	public SegPerfilMenu() {
	}

	public SegPerfilMenuPK getId() {
		return this.id;
	}

	public void setId(SegPerfilMenuPK id) {
		this.id = id;
	}

}