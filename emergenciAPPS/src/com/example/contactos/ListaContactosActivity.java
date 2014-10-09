package com.example.contactos;

import java.util.ArrayList;

import com.example.adapters.ListaAdapterContacto;
import com.example.emergenciapps.R;
import com.example.emergenciapps.Utils;
import com.example.object.Contacto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ListaContactosActivity extends Activity{
	private ListView listaContactos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactos);
		listaContactos = (ListView) findViewById(R.id.listaNroContactos);
		Log.d("emergenciAPPS",listaContactos.toString());
		
		
		
		ArrayList<Contacto> listaContact = Utils.getContactos(this);
		
		ListaAdapterContacto adapter = new ListaAdapterContacto(this, R.id.lista_boton_llamar, listaContact);
		listaContactos.setAdapter(adapter);
		
		
	}

}
