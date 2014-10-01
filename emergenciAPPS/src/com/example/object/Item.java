package com.example.object;

public class Item {
	private int idIcon;
	private String titulo;
	private String nombreTabla;
	
	public Item(String titulo, int idIcon, String nombreTabla){
		this.titulo = titulo;
		this.idIcon = idIcon;
		this.nombreTabla = nombreTabla;
	}
	public int getIdIcon() {
		return idIcon;
	}
	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	
}
