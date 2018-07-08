package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the seg_perfil_menu database table.
 * 
 */
@Embeddable
public class SegPerfilMenuPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_perfil")
	private int idPerfil;

	@Column(name="id_menu")
	private int idMenu;

	public SegPerfilMenuPK() {
	}
	public int getIdPerfil() {
		return this.idPerfil;
	}
	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}
	public int getIdMenu() {
		return this.idMenu;
	}
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}
}