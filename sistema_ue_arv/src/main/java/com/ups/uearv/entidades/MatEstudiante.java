package com.ups.uearv.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mat_estudiante database table.
 * 
 */
@Entity
@Table(name="mat_estudiante")
@NamedQuery(name="MatEstudiante.findAll", query="SELECT m FROM MatEstudiante m")
public class MatEstudiante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_estudiante")
	private String idEstudiante;

	@Column(name="alerg_otras")
	private String alergOtras;

	private String apellidos;

	@Column(name="asig_mas")
	private String asigMas;

	@Column(name="asig_menos")
	private String asigMenos;

	private String canton;

	private String ciudad;

	private String direccion;

	@Column(name="ds_alergia_ali")
	private String dsAlergiaAli;

	@Column(name="ds_alergia_ins")
	private String dsAlergiaIns;

	@Column(name="ds_alergia_med")
	private String dsAlergiaMed;

	@Column(name="ds_clases_part")
	private String dsClasesPart;

	@Column(name="ds_cumples")
	private String dsCumples;

	@Column(name="ds_estudios_fuera")
	private String dsEstudiosFuera;

	@Column(name="ds_hermanos")
	private String dsHermanos;

	@Column(name="ds_hospital")
	private String dsHospital;

	@Column(name="ds_minusvalia")
	private String dsMinusvalia;

	@Column(name="ds_operado")
	private String dsOperado;

	@Column(name="emerg_llamar")
	private String emergLlamar;

	@Column(name="emerg_telefono")
	private String emergTelefono;

	private String enfermedad;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_act")
	private Date fechaAct;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ing")
	private Date fechaIng;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_nac")
	private Date fechaNac;

	private String foto;

	@Column(name="gd_minusvalia")
	private String gdMinusvalia;

	private String genero;

	@Column(name="lugar_nacimiento")
	private String lugarNacimiento;

	@Column(name="ma_cedula")
	private String maCedula;

	@Column(name="ma_celular")
	private String maCelular;

	@Column(name="ma_correo")
	private String maCorreo;

	@Column(name="ma_estado_civil")
	private String maEstadoCivil;

	@Column(name="ma_estudios")
	private String maEstudios;

	@Column(name="ma_institucion")
	private String maInstitucion;

	@Column(name="ma_nombre")
	private String maNombre;

	@Column(name="ma_ocupacion")
	private String maOcupacion;

	@Column(name="ma_profesion")
	private String maProfesion;

	@Column(name="ma_puesto")
	private String maPuesto;

	@Column(name="ma_telefono")
	private String maTelefono;

	private String nacionalidad;

	private String nombres;

	private String observacion;

	@Column(name="pa_cedula")
	private String paCedula;

	@Column(name="pa_celular")
	private String paCelular;

	@Column(name="pa_correo")
	private String paCorreo;

	@Column(name="pa_estado_civil")
	private String paEstadoCivil;

	@Column(name="pa_estudios")
	private String paEstudios;

	@Column(name="pa_institucion")
	private String paInstitucion;

	@Column(name="pa_nombre")
	private String paNombre;

	@Column(name="pa_ocupacion")
	private String paOcupacion;

	@Column(name="pa_profesion")
	private String paProfesion;

	@Column(name="pa_puesto")
	private String paPuesto;

	@Column(name="pa_telefono")
	private String paTelefono;

	private String parroquia;

	private String provincia;

	@Column(name="reaccion_padres")
	private String reaccionPadres;

	@Column(name="relacion_padres")
	private String relacionPadres;

	private String rendimiento;

	@Column(name="situacion_familiar")
	private String situacionFamiliar;

	@Column(name="sn_alergia_ali")
	private String snAlergiaAli;

	@Column(name="sn_alergia_ins")
	private String snAlergiaIns;

	@Column(name="sn_alergia_med")
	private String snAlergiaMed;

	@Column(name="sn_clases_part")
	private String snClasesPart;

	@Column(name="sn_cumples")
	private String snCumples;

	@Column(name="sn_discapacidad")
	private String snDiscapacidad;

	@Column(name="sn_estudios_fuera")
	private String snEstudiosFuera;

	@Column(name="sn_hermanos")
	private String snHermanos;

	@Column(name="sn_hospital")
	private String snHospital;

	@Column(name="sn_minusvalia")
	private String snMinusvalia;

	@Column(name="sn_operado")
	private String snOperado;

	@Column(name="sn_trat_recibido")
	private String snTratRecibido;

	@Column(name="sn_tratamiento")
	private String snTratamiento;

	private String telefono;

	@Column(name="tipo_sangre")
	private String tipoSangre;

	@Column(name="usuario_act")
	private String usuarioAct;

	@Column(name="usuario_ing")
	private String usuarioIng;

	@Column(name="vive_con")
	private String viveCon;

	//bi-directional many-to-one association to MatRepresentante
	@ManyToOne
	@JoinColumn(name="id_representante")
	private MatRepresentante matRepresentante;

	//bi-directional many-to-one association to MatMatricula
	@OneToMany(mappedBy="matEstudiante")
	private List<MatMatricula> matMatriculas;

	public MatEstudiante() {
	}

	public String getIdEstudiante() {
		return this.idEstudiante;
	}

	public void setIdEstudiante(String idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

	public String getAlergOtras() {
		return this.alergOtras;
	}

	public void setAlergOtras(String alergOtras) {
		this.alergOtras = alergOtras;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getAsigMas() {
		return this.asigMas;
	}

	public void setAsigMas(String asigMas) {
		this.asigMas = asigMas;
	}

	public String getAsigMenos() {
		return this.asigMenos;
	}

	public void setAsigMenos(String asigMenos) {
		this.asigMenos = asigMenos;
	}

	public String getCanton() {
		return this.canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDsAlergiaAli() {
		return this.dsAlergiaAli;
	}

	public void setDsAlergiaAli(String dsAlergiaAli) {
		this.dsAlergiaAli = dsAlergiaAli;
	}

	public String getDsAlergiaIns() {
		return this.dsAlergiaIns;
	}

	public void setDsAlergiaIns(String dsAlergiaIns) {
		this.dsAlergiaIns = dsAlergiaIns;
	}

	public String getDsAlergiaMed() {
		return this.dsAlergiaMed;
	}

	public void setDsAlergiaMed(String dsAlergiaMed) {
		this.dsAlergiaMed = dsAlergiaMed;
	}

	public String getDsClasesPart() {
		return this.dsClasesPart;
	}

	public void setDsClasesPart(String dsClasesPart) {
		this.dsClasesPart = dsClasesPart;
	}

	public String getDsCumples() {
		return this.dsCumples;
	}

	public void setDsCumples(String dsCumples) {
		this.dsCumples = dsCumples;
	}

	public String getDsEstudiosFuera() {
		return this.dsEstudiosFuera;
	}

	public void setDsEstudiosFuera(String dsEstudiosFuera) {
		this.dsEstudiosFuera = dsEstudiosFuera;
	}

	public String getDsHermanos() {
		return this.dsHermanos;
	}

	public void setDsHermanos(String dsHermanos) {
		this.dsHermanos = dsHermanos;
	}

	public String getDsHospital() {
		return this.dsHospital;
	}

	public void setDsHospital(String dsHospital) {
		this.dsHospital = dsHospital;
	}

	public String getDsMinusvalia() {
		return this.dsMinusvalia;
	}

	public void setDsMinusvalia(String dsMinusvalia) {
		this.dsMinusvalia = dsMinusvalia;
	}

	public String getDsOperado() {
		return this.dsOperado;
	}

	public void setDsOperado(String dsOperado) {
		this.dsOperado = dsOperado;
	}

	public String getEmergLlamar() {
		return this.emergLlamar;
	}

	public void setEmergLlamar(String emergLlamar) {
		this.emergLlamar = emergLlamar;
	}

	public String getEmergTelefono() {
		return this.emergTelefono;
	}

	public void setEmergTelefono(String emergTelefono) {
		this.emergTelefono = emergTelefono;
	}

	public String getEnfermedad() {
		return this.enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
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

	public Date getFechaNac() {
		return this.fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getGdMinusvalia() {
		return this.gdMinusvalia;
	}

	public void setGdMinusvalia(String gdMinusvalia) {
		this.gdMinusvalia = gdMinusvalia;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getLugarNacimiento() {
		return this.lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getMaCedula() {
		return this.maCedula;
	}

	public void setMaCedula(String maCedula) {
		this.maCedula = maCedula;
	}

	public String getMaCelular() {
		return this.maCelular;
	}

	public void setMaCelular(String maCelular) {
		this.maCelular = maCelular;
	}

	public String getMaCorreo() {
		return this.maCorreo;
	}

	public void setMaCorreo(String maCorreo) {
		this.maCorreo = maCorreo;
	}

	public String getMaEstadoCivil() {
		return this.maEstadoCivil;
	}

	public void setMaEstadoCivil(String maEstadoCivil) {
		this.maEstadoCivil = maEstadoCivil;
	}

	public String getMaEstudios() {
		return this.maEstudios;
	}

	public void setMaEstudios(String maEstudios) {
		this.maEstudios = maEstudios;
	}

	public String getMaInstitucion() {
		return this.maInstitucion;
	}

	public void setMaInstitucion(String maInstitucion) {
		this.maInstitucion = maInstitucion;
	}

	public String getMaNombre() {
		return this.maNombre;
	}

	public void setMaNombre(String maNombre) {
		this.maNombre = maNombre;
	}

	public String getMaOcupacion() {
		return this.maOcupacion;
	}

	public void setMaOcupacion(String maOcupacion) {
		this.maOcupacion = maOcupacion;
	}

	public String getMaProfesion() {
		return this.maProfesion;
	}

	public void setMaProfesion(String maProfesion) {
		this.maProfesion = maProfesion;
	}

	public String getMaPuesto() {
		return this.maPuesto;
	}

	public void setMaPuesto(String maPuesto) {
		this.maPuesto = maPuesto;
	}

	public String getMaTelefono() {
		return this.maTelefono;
	}

	public void setMaTelefono(String maTelefono) {
		this.maTelefono = maTelefono;
	}

	public String getNacionalidad() {
		return this.nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getPaCedula() {
		return this.paCedula;
	}

	public void setPaCedula(String paCedula) {
		this.paCedula = paCedula;
	}

	public String getPaCelular() {
		return this.paCelular;
	}

	public void setPaCelular(String paCelular) {
		this.paCelular = paCelular;
	}

	public String getPaCorreo() {
		return this.paCorreo;
	}

	public void setPaCorreo(String paCorreo) {
		this.paCorreo = paCorreo;
	}

	public String getPaEstadoCivil() {
		return this.paEstadoCivil;
	}

	public void setPaEstadoCivil(String paEstadoCivil) {
		this.paEstadoCivil = paEstadoCivil;
	}

	public String getPaEstudios() {
		return this.paEstudios;
	}

	public void setPaEstudios(String paEstudios) {
		this.paEstudios = paEstudios;
	}

	public String getPaInstitucion() {
		return this.paInstitucion;
	}

	public void setPaInstitucion(String paInstitucion) {
		this.paInstitucion = paInstitucion;
	}

	public String getPaNombre() {
		return this.paNombre;
	}

	public void setPaNombre(String paNombre) {
		this.paNombre = paNombre;
	}

	public String getPaOcupacion() {
		return this.paOcupacion;
	}

	public void setPaOcupacion(String paOcupacion) {
		this.paOcupacion = paOcupacion;
	}

	public String getPaProfesion() {
		return this.paProfesion;
	}

	public void setPaProfesion(String paProfesion) {
		this.paProfesion = paProfesion;
	}

	public String getPaPuesto() {
		return this.paPuesto;
	}

	public void setPaPuesto(String paPuesto) {
		this.paPuesto = paPuesto;
	}

	public String getPaTelefono() {
		return this.paTelefono;
	}

	public void setPaTelefono(String paTelefono) {
		this.paTelefono = paTelefono;
	}

	public String getParroquia() {
		return this.parroquia;
	}

	public void setParroquia(String parroquia) {
		this.parroquia = parroquia;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getReaccionPadres() {
		return this.reaccionPadres;
	}

	public void setReaccionPadres(String reaccionPadres) {
		this.reaccionPadres = reaccionPadres;
	}

	public String getRelacionPadres() {
		return this.relacionPadres;
	}

	public void setRelacionPadres(String relacionPadres) {
		this.relacionPadres = relacionPadres;
	}

	public String getRendimiento() {
		return this.rendimiento;
	}

	public void setRendimiento(String rendimiento) {
		this.rendimiento = rendimiento;
	}

	public String getSituacionFamiliar() {
		return this.situacionFamiliar;
	}

	public void setSituacionFamiliar(String situacionFamiliar) {
		this.situacionFamiliar = situacionFamiliar;
	}

	public String getSnAlergiaAli() {
		return this.snAlergiaAli;
	}

	public void setSnAlergiaAli(String snAlergiaAli) {
		this.snAlergiaAli = snAlergiaAli;
	}

	public String getSnAlergiaIns() {
		return this.snAlergiaIns;
	}

	public void setSnAlergiaIns(String snAlergiaIns) {
		this.snAlergiaIns = snAlergiaIns;
	}

	public String getSnAlergiaMed() {
		return this.snAlergiaMed;
	}

	public void setSnAlergiaMed(String snAlergiaMed) {
		this.snAlergiaMed = snAlergiaMed;
	}

	public String getSnClasesPart() {
		return this.snClasesPart;
	}

	public void setSnClasesPart(String snClasesPart) {
		this.snClasesPart = snClasesPart;
	}

	public String getSnCumples() {
		return this.snCumples;
	}

	public void setSnCumples(String snCumples) {
		this.snCumples = snCumples;
	}

	public String getSnDiscapacidad() {
		return this.snDiscapacidad;
	}

	public void setSnDiscapacidad(String snDiscapacidad) {
		this.snDiscapacidad = snDiscapacidad;
	}

	public String getSnEstudiosFuera() {
		return this.snEstudiosFuera;
	}

	public void setSnEstudiosFuera(String snEstudiosFuera) {
		this.snEstudiosFuera = snEstudiosFuera;
	}

	public String getSnHermanos() {
		return this.snHermanos;
	}

	public void setSnHermanos(String snHermanos) {
		this.snHermanos = snHermanos;
	}

	public String getSnHospital() {
		return this.snHospital;
	}

	public void setSnHospital(String snHospital) {
		this.snHospital = snHospital;
	}

	public String getSnMinusvalia() {
		return this.snMinusvalia;
	}

	public void setSnMinusvalia(String snMinusvalia) {
		this.snMinusvalia = snMinusvalia;
	}

	public String getSnOperado() {
		return this.snOperado;
	}

	public void setSnOperado(String snOperado) {
		this.snOperado = snOperado;
	}

	public String getSnTratRecibido() {
		return this.snTratRecibido;
	}

	public void setSnTratRecibido(String snTratRecibido) {
		this.snTratRecibido = snTratRecibido;
	}

	public String getSnTratamiento() {
		return this.snTratamiento;
	}

	public void setSnTratamiento(String snTratamiento) {
		this.snTratamiento = snTratamiento;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
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

	public String getViveCon() {
		return this.viveCon;
	}

	public void setViveCon(String viveCon) {
		this.viveCon = viveCon;
	}

	public MatRepresentante getMatRepresentante() {
		return this.matRepresentante;
	}

	public void setMatRepresentante(MatRepresentante matRepresentante) {
		this.matRepresentante = matRepresentante;
	}

	public List<MatMatricula> getMatMatriculas() {
		return this.matMatriculas;
	}

	public void setMatMatriculas(List<MatMatricula> matMatriculas) {
		this.matMatriculas = matMatriculas;
	}

	public MatMatricula addMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().add(matMatricula);
		matMatricula.setMatEstudiante(this);

		return matMatricula;
	}

	public MatMatricula removeMatMatricula(MatMatricula matMatricula) {
		getMatMatriculas().remove(matMatricula);
		matMatricula.setMatEstudiante(null);

		return matMatricula;
	}

}