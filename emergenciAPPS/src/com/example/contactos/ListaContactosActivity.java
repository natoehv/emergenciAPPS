package com.example.contactos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.adapters.ListaAdapterContacto;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.emergenciapps.Utils;
import com.example.login.LoginActivity;
import com.example.object.Contacto;
import com.example.persistencia.ContactoSQLHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class ListaContactosActivity extends Activity{
	private ListView listaContactos;
	ArrayList<Contacto> listaContact;
	ListaAdapterContacto adapter;
	private ProgressDialog ringProgressDialog;
	String miNumero;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactos);
		listaContactos = (ListView) findViewById(R.id.listaNroContactos);
		Log.d("emergenciAPPS",listaContactos.toString());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listaContact = Utils.getContactos(this);
		
		adapter = new ListaAdapterContacto(this, R.id.lista_boton_llamar, listaContact);
		listaContactos.setAdapter(adapter);
		SharedPreferences prefe = this.getSharedPreferences("miCuenta", ListaContactosActivity.MODE_PRIVATE);
        miNumero = prefe.getString("miNumero", "");
		
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
        case R.id.actulizarListaContactos:
	        	
				
				AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void,String >() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						ringProgressDialog = ProgressDialog.show(ListaContactosActivity.this, "Por favor espere ...", "Actualizando lista ...", true);
						ringProgressDialog.setCancelable(false);
					}
					
					@Override
					protected String doInBackground(String... arg0) {
						List<Contacto> contactos = new ArrayList<Contacto>();
						contactos = ServicioWeb.getContactos(miNumero);
						
						if(contactos != null){
							ContactoSQLHelper oData = new ContactoSQLHelper(ListaContactosActivity.this); 
							SQLiteDatabase db = oData.getWritableDatabase();
							db.execSQL("delete from contacto");
							db.close();
							oData.close();
						}
						
						Boolean resultado = Utils.insertContactos(contactos, ListaContactosActivity.this);
						
						if(resultado){
							return "Lista cargada exitosamente";
						}else{
							return "No se pudo actualizar la lista";
						}
						
						
					}
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						ringProgressDialog.dismiss();
						
						listaContact = Utils.getContactos(ListaContactosActivity.this);
						adapter = new ListaAdapterContacto(ListaContactosActivity.this, R.id.lista_boton_llamar, listaContact);
						listaContactos.setAdapter(adapter);
						
						Toast.makeText(ListaContactosActivity.this, result, Toast.LENGTH_LONG).show();
						
					}
				
				};
				tarea.execute();
			
			
			
        	
        		
        	break;
        case android.R.id.home:
        	finish();
        	break;
		}
		
		return true;
	}

}
