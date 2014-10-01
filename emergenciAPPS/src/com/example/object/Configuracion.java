package com.example.object;

import java.io.Serializable;
import java.util.Date;

public class Configuracion implements Serializable  {
	private Integer idConfiguracion;
	private String numeroPDI;
	private String numeroCarabinero;
	private String numeroBombero;
	private String numeroCentroMedico;
	private Integer radioBusqueda;
	private Date fechaModificacion;
	private String mensajeAlerta;
	public Integer getIdConfiguracion() {
		return idConfiguracion;
	}
	public void setIdConfiguracion(Integer idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}
	public String getNumeroPDI() {
		return numeroPDI;
	}
	public void setNumeroPDI(String numeroPDI) {
		this.numeroPDI = numeroPDI;
	}
	public String getNumeroCarabinero() {
		return numeroCarabinero;
	}
	public void setNumeroCarabinero(String numeroCarabinero) {
		this.numeroCarabinero = numeroCarabinero;
	}
	public String getNumeroBombero() {
		return numeroBombero;
	}
	public void setNumeroBombero(String numeroBombero) {
		this.numeroBombero = numeroBombero;
	}
	public String getNumeroCentroMedico() {
		return numeroCentroMedico;
	}
	public void setNumeroCentroMedico(String numeroCentroMedico) {
		this.numeroCentroMedico = numeroCentroMedico;
	}
	public Integer getRadioBusqueda() {
		return radioBusqueda;
	}
	public void setRadioBusqueda(Integer radioBusqueda) {
		this.radioBusqueda = radioBusqueda;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getMensajeAlerta() {
		return mensajeAlerta;
	}
	public void setMensajeAlerta(String mensajeAlerta) {
		this.mensajeAlerta = mensajeAlerta;
	}
	
	
}
