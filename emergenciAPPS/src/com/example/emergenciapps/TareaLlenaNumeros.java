package com.example.emergenciapps;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.widget.ListView;

public class TareaLlenaNumeros extends AsyncTask<Void, Object, Boolean>{
	private List objects;
	private ListView lista;
	public TareaLlenaNumeros(List objects, ListView lista){
		super();
		this.objects = objects;
		this.lista = lista;
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		publishProgress(objects);
		return true;
	}
	@Override
    protected void onProgressUpdate(Object... values) {
		ArrayList carabineros = ((ArrayList)values[0]);
		if(carabineros.get(0) instanceof Carabinero){
			
			ListaAdapter list = new ListaAdapter(lista.getContext(), R.id.lista_boton_llamar, carabineros);
			lista.setAdapter(list);
		}
    }
}
