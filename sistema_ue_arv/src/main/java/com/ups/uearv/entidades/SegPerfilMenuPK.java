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

	@Column(name="id_perfil", insertable=true, updatable=false)
	private int idPerfil;

	@Column(name="id_menu", insertable=true, updatable=false)
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SegPerfilMenuPK)) {
			return false;
		}
		SegPerfilMenuPK castOther = (SegPerfilMenuPK)other;
		return 
			(this.idPerfil == castOther.idPerfil)
			&& (this.idMenu == castOther.idMenu);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idPerfil;
		hash = hash * prime + this.idMenu;
		
		return hash;
	}
}