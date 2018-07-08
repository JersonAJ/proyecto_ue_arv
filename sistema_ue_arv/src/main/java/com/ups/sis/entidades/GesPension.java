package com.ups.sis.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ges_pension database table.
 * 
 */
@Entity
@Table(name="ges_pension")
@NamedQuery(name="GesPension.findAll", query="SELECT g FROM GesPension g")
public class GesPension implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_pension")
	private int idPension;

	private BigDecimal abono;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_pago")
	private Date fechaPago;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_vence")
	private Date fechaVence;

	@Column(name="forma_pago")
	private String formaPago;

	private BigDecimal precio;

	private BigDecimal saldo;

	private int secuencia;

	@Column(name="total_pagar")
	private BigDecimal totalPagar;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	//bi-directional many-to-one association to MatMatricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private MatMatricula matMatricula;

	//bi-directional many-to-one association to GesDescuento
	@ManyToOne
	@JoinColumn(name="id_descuento")
	private GesDescuento gesDescuento;

	public GesPension() {
	}

	public int getIdPension() {
		return this.idPension;
	}

	public void setIdPension(int idPension) {
		this.idPension = idPension;
	}

	public BigDecimal getAbono() {
		return this.abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
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

	public Date getFechaPago() {
		return this.fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Date getFechaVence() {
		return this.fechaVence;
	}

	public void setFechaVence(Date fechaVence) {
		this.fechaVence = fechaVence;
	}

	public String getFormaPago() {
		return this.formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public int getSecuencia() {
		return this.secuencia;
	}

	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}

	public BigDecimal getTotalPagar() {
		return this.totalPagar;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
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

	public MatMatricula getMatMatricula() {
		return this.matMatricula;
	}

	public void setMatMatricula(MatMatricula matMatricula) {
		this.matMatricula = matMatricula;
	}

	public GesDescuento getGesDescuento() {
		return this.gesDescuento;
	}

	public void setGesDescuento(GesDescuento gesDescuento) {
		this.gesDescuento = gesDescuento;
	}

}