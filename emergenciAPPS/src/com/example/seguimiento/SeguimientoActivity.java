package com.example.seguimiento;

import java.util.ArrayList;
import java.util.List;

import com.example.contactos.DetalleContactoActivity;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Usuario;
import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SeguimientoActivity extends Activity{
	List<String> myList;
	Spinner spinner;
	Integer itemActual;
	Usuario usuarioActual;
	AsyncTask<Integer, Integer, String> tarea_actualizar;
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
	private AnnotationView annotation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_seguimiento);
		spinner = (Spinner)findViewById(R.id.spinner);
		myList = new ArrayList<String>();
		map = (MapView)SeguimientoActivity.this.findViewById(R.id.mapSegumiento);
		map.getController().setZoom(15);
		inititialize_list();
		myAdapter = new ArrayAdapter<String>(SeguimientoActivity.this, android.R.layout.simple_spinner_item, myList);
		spinner.setAdapter(myAdapter);
		
		/*
		 * Se inicia tarea
		 */
		
		tarea_actualizar = new TareaSeguimiento();
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,final int arg2, long arg3) {
				if(lista != null){
					setearPunto(lista.get(arg2).getLat(), lista.get(arg2).getLng(), lista.get(arg2).getNumeroTelefono(),lista.get(arg2).getNombre());
					itemActual = arg2;
					usuarioActual = lista.get(arg2);
					if(tarea_actualizar.getStatus() == AsyncTask.Status.FINISHED || tarea_actualizar.getStatus() == AsyncTask.Status.PENDING ){
						tarea_actualizar.execute(arg2);
					}else{
						if(tarea_actualizar.getStatus() == AsyncTask.Status.RUNNING){
							tarea_actualizar.cancel(true);
							tarea_actualizar = new TareaSeguimiento();
							tarea_actualizar.execute(arg2);
						}
					}
				}else{
					/*
					 * Se vacia mapa
					 */
					vaciarMapa();
				}
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
		tarea_actualizar.cancel(true);
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
		tarea_actualizar.cancel(true);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		tarea_actualizar = new TareaSeguimiento();
		tarea_actualizar.execute(itemActual);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tarea_actualizar.cancel(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
	        	finish();
	        	break;
			}
		return true;
		
		
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
	private void vaciarMapa(){
		for(com.mapquest.android.maps.Overlay o : map.getOverlays()){
			map.getOverlays().remove(o);
		}
		
	}
	private void setearPunto(Float lat, Float lng, final String numero, final String nombre){
		Log.d("map","inicia map");
		if(markers.size() > 0){
			for(int i=0; i<markers.size(); i++){
				markers.get(i).destroy();
				
			}
		}
		
		Drawable iconUserInAlert = this.getResources().getDrawable(R.drawable.miposicion);
		overlayUserInAlert = new DefaultItemizedOverlay(iconUserInAlert);
		markers.add(overlayUserInAlert);
		
		GeoPoint latlng = new GeoPoint(lat,lng);
		
		item = new OverlayItem(latlng, numero, nombre);
		
		map.getController().animateTo(latlng);
		overlayUserInAlert.addItem(item);
		
		overlayUserInAlert.setTapListener(new ItemizedOverlay.OverlayTapListener(){
			
			@Override
			public void onTap(GeoPoint arg0, MapView arg1) {
				int lastTouchedIndex = overlayUserInAlert.getLastFocusedIndex();
				if(lastTouchedIndex > -1){
					Toast.makeText(SeguimientoActivity.this, "Siguiendo a "+nombre+" ("+numero+")" , Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
		map.getOverlays().add(overlayUserInAlert);
		
		map.getController().setCenter(latlng);
		map.setBuiltInZoomControls(true);
		
		
		
	}
	
	private class TareaSeguimiento extends AsyncTask<Integer, Integer, String>{

		@Override
		protected String doInBackground(Integer... arg0) {
			while(!isCancelled()){
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			lista = ServicioWeb.getUserInAlert(miNumero);
			/*
			 * si el usuario ya no se encuentra en alerta
			 */
			Log.d("seguimiento","nro usuario: "+usuarioActual.getNumeroTelefono());
			if(lista != null && lista.contains(usuarioActual) ){
				Log.d("seguimiento","todo sigue igual");
				myList = new ArrayList<String>();
				 for(int i=0; i<lista.size(); i++){
					 myList.add(lista.get(i).getNombre());
				 }
				 publishProgress(arg0[0]);
			}else{
				if(lista != null){
					Log.d("seguimiento","el usuario ya no se encuentra en alerta");
					myList = new ArrayList<String>();
					 for(int i=0; i<lista.size(); i++){
						 myList.add(lista.get(i).getNombre());
					 }
					 
					 publishProgress(0);
					 
				}else{
					Log.d("seguimiento","ya no existen alertas");
					myList = new ArrayList<String>();
					
					publishProgress();
					
					Log.d("lista", "vacia");
				}
			}
			
			}
			return null;
		}



		@Override
		protected void onProgressUpdate(Integer... result) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(result);
			//ringProgressDialog.dismiss();
			myAdapter = new ArrayAdapter<String>(SeguimientoActivity.this, android.R.layout.simple_spinner_item, myList);
			spinner.setAdapter(myAdapter);
			for(Integer i : result)
				spinner.setSelection(i);
				
		}
		
	}
}

