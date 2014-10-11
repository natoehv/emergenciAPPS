package com.example.contactos;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.adapters.ListaAdapterContacto;
import com.example.emergenciapps.R;
import com.example.emergenciapps.Utils;
import com.example.object.Contacto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class ListaContactosActivity extends Activity{
	private ListView listaContactos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactos);
		listaContactos = (ListView) findViewById(R.id.listaNroContactos);
		Log.d("emergenciAPPS",listaContactos.toString());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ArrayList<Contacto> listaContact = Utils.getContactos(this);
		
		ListaAdapterContacto adapter = new ListaAdapterContacto(this, R.id.lista_boton_llamar, listaContact);
		listaContactos.setAdapter(adapter);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		//MenuItem deleteItem = menu.findItem(R.id.eliminarContacto);
		inflater.inflate(R.menu.lista_contacto, menu);
			 
		
        
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.agregarContacto:
        	
        	Intent intent = new Intent(ListaContactosActivity.this, DetalleContactoActivity.class);
			this.startActivity(intent);
			
        	//Toast.makeText(ListaContactosActivity.this, "Agregar",Toast.LENGTH_LONG).show();

        	break;
        case android.R.id.home:
        	finish();
        	break;
		}
		
		return true;
	}

}
