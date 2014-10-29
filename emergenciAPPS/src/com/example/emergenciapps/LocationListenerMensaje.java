package com.example.emergenciapps;

import java.util.List;

import com.example.contactos.DetalleContactoActivity;
import com.example.contactos.ListaContactosActivity;
import com.example.object.EmailEmergencia;
import com.example.seguimiento.MyService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LocationListenerMensaje implements LocationListener {
	private ProgressDialog ringProgressDialog;
	private static String TAG = "emergenciAPPS";
	
	View v;
	LocationManager locManager;
	String miNumero;
	public LocationListenerMensaje(LocationManager locManager,View v, String miNumero){
		this.miNumero = miNumero;
		this.v = v;
		this.locManager = locManager;
		
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "se captura localizacion");
        final String lat = String.valueOf(location.getLatitude());
        final String lng = String.valueOf(location.getLongitude());
        /*
         * Se crea hilo para enviar mensaje
         *  
         */
        AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
    		
    		@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ringProgressDialog = ProgressDialog.show(v.getContext(), "Por favor espere ...", "Enviando Alerta", true);
				ringProgressDialog.setCancelable(false);
				
			}
    		
			@Override
			protected String doInBackground(String... arg0) {
				ServicioWeb.sendNotification(lat, lng, miNumero);
				
				return "";
				
				
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Intent i = new Intent(v.getContext(), MyService.class);
            	i.putExtra("miNumero", miNumero);
            	v.getContext().startService(i);
            	
            	
            	
				ringProgressDialog.dismiss();
				
			}
    		
    	};
    	tarea.execute();
        locManager.removeUpdates(this);
		
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	

}
