package com.example.emergenciapps;

import com.mapquest.android.maps.GeoPoint;

public class PDI {
	private int id;
	private String nombre;
	private String direccion;
	private String telefono;
	private String comuna;
	private Float x, y;
	private Float distancia;
	
	public PDI(){
	}
	public PDI(int id, String nombre, String direccion, String telefono,
			String comuna, Float x, Float y, Float distancia) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.comuna = comuna;
		this.x = x;
		this.y = y;
		this.distancia = distancia;
	}
	public GeoPoint getGeoPoint(){
		return new GeoPoint(x,y);
	}
	
	public Float getDistancia() {
		return distancia;
	}
	public void setDistancia(Float distancia) {
		this.distancia = distancia;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public Float getX() {
		return x;
	}
	public void setX(Float x) {
		this.x = x;
	}
	public Float getY() {
		return y;
	}
	public void setY(Float y) {
		this.y = y;
	}
	
	
}
