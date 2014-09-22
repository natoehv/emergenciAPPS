package com.example.emergenciapps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RespuestaServicioWeb implements Serializable{
	private List lista;
	private Integer codigo;
	
	public RespuestaServicioWeb(List lista, Integer codigo) {
		super();
		this.lista = lista;
		this.codigo = codigo;
	}
	
	
	public List getLista() {
		return lista;
	}
	public void setLista(List lista) {
		this.lista = lista;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	
	
	

}
