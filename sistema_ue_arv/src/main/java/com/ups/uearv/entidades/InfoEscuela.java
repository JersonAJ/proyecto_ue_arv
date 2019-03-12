package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogo_cab database table.
 * 
 */
@Entity
@Table(name="info_escuela")
@NamedQuery(name="InfoEscuela.findAll", query="SELECT c FROM InfoEscuela c")
public class InfoEscuela implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codigo;

	private String descripcion;

	private String valor;
	

	public InfoEscuela() {
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}