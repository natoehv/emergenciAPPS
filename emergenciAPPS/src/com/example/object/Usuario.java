package com.example.object;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
	private String nombre;
	private String numeroTelefono;
	private String apellido;
	private String password;
	private String correo;
	private Integer estadoAlerta;
	private Float lat;
	private Float lng;
	private List<Contacto> contactos;
	private Configuracion configuracion;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Integer getEstadoAlerta() {
		return estadoAlerta;
	}
	public void setEstadoAlerta(Integer estadoAlerta) {
		this.estadoAlerta = estadoAlerta;
	}
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Float getLng() {
		return lng;
	}
	public void setLng(Float lng) {
		this.lng = lng;
	}
	public List<Contacto> getContactos() {
		return contactos;
	}
	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	public Configuracion getConfiguracion() {
		return configuracion;
	}
	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Usuario))return false;
	    Usuario oUsuario = (Usuario)o;
	    if(oUsuario == this) return true;
	    if(oUsuario.getNumeroTelefono().equals(this.numeroTelefono))
	    	return true;
	    return false;
	}
	
	
	
}
