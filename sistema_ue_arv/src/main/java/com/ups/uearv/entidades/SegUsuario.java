package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the seg_usuario database table.
 * 
 */
@Entity
@Table(name="seg_usuario")
@NamedQuery(name="SegUsuario.findAll", query="SELECT s FROM SegUsuario s")
public class SegUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_usuario")
	private String idUsuario;
	
	@Column(name="id_perfil")
	private int idPerfil;

	private String apellidos;

	private String clave;

	private int intentos;

	private String nombres;

	@Column(name="sn_bloqueado")
	private String snBloqueado;
	
	private String estado;
	
	@Column(name="sn_nuevo")
	private String snNuevo;

	public SegUsuario() {
	}

	public String getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getIntentos() {
		return this.intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getSnBloqueado() {
		return this.snBloqueado;
	}

	public void setSnBloqueado(String snBloqueado) {
		this.snBloqueado = snBloqueado;
	}

	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSnNuevo() {
		return snNuevo;
	}

	public void setSnNuevo(String snNuevo) {
		this.snNuevo = snNuevo;
	}
}