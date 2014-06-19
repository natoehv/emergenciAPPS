package com.example.emergenciapps;

public class Item {
	private int idIcon;
	private String titulo;
	
	public Item(String titulo, int idIcon){
		this.titulo = titulo;
		this.idIcon = idIcon;
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
	
}
