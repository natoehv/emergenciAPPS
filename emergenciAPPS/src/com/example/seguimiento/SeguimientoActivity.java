package com.example.seguimiento;

import java.util.ArrayList;
import java.util.List;

import com.example.contactos.DetalleContactoActivity;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Usuario;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SeguimientoActivity extends Activity{
	List<String> myList;
	Spinner spinner;
	Button btn;
	String miNumero;
	private ArrayAdapter<String> myAdapter;
	List<Usuario> lista;
	private ProgressDialog ringProgressDialog;
	Boolean bandera = true;
	private MapView map;
	List<DefaultItemizedOverlay> markers = new ArrayList<DefaultItemizedOverlay>();
	private DefaultItemizedOverlay overlayUserInAlert;
	OverlayItem item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguimiento);
		spinner = (Spinner)findViewById(R.id.spinner);
		myList = new ArrayList<String>();
		inititialize_list();
		myAdapter = new ArrayAdapter<String>(SeguimientoActivity.this, android.R.layout.simple_spinner_item, myList);
		spinner.setAdapter(myAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(markers.size() > 0){
					for(int i=0; i<markers.size(); i++){
						markers.get(i).destroy();
					}
				}
				
				setearPunto(lista.get(arg2).getLat(), lista.get(arg2).getLng(), lista.get(arg2).getNumeroTelefono(),(MapView)SeguimientoActivity.this.findViewById(R.id.mapSegumiento));
				
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

			
			

		});
		
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void inititialize_list(){
		 
		 SharedPreferences prefs = getSharedPreferences("miCuenta", this.MODE_PRIVATE);
		 miNumero = ""+prefs.getString("miNumero", "");
		 AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
			 @Override
				protected void onPreExecute() {
					super.onPreExecute();
					ringProgressDialog = ProgressDialog.show(SeguimientoActivity.this, "Por favor espere ...", "Cargando contactos en peligro ...", true);
					ringProgressDialog.setCancelable(false);
					
				}
			 
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				lista = ServicioWeb.getUserInAlert(miNumero);
				
				if(lista != null){
					myList = new ArrayList<String>();
					 for(int i=0; i<lista.size(); i++){
						 myList.add(lista.get(i).getNombre());
					 }
				}else{
					Log.d("lista", "vacia");
				}
				 
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				ringProgressDialog.dismiss();
				myAdapter = new ArrayAdapter<String>(SeguimientoActivity.this, android.R.layout.simple_spinner_item, myList);
				spinner.setAdapter(myAdapter);
			}};
		 tarea.execute();
		 
		 
		
		 
	}
	
	private void setearPunto(Float lat, Float lng, String numero, MapView map){
		Log.d("map","inicia map");
		
		this.map = map;
		Drawable iconUserInAlert = this.getResources().getDrawable(R.drawable.miposicion);
		overlayUserInAlert = new DefaultItemizedOverlay(iconUserInAlert);
		markers.add(overlayUserInAlert);
		
		GeoPoint latlng = new GeoPoint(lat,lng);
		item = new OverlayItem(latlng, numero, numero);
		
		map.getController().animateTo(latlng);
		overlayUserInAlert.addItem(item);
		map.getOverlays().add(overlayUserInAlert);
		map.getController().setZoom(16);
		map.getController().setCenter(latlng);
		
		
		
	}
	

}
