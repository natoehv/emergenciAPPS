package com.example.emergenciapps;

import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationListenerMensaje implements LocationListener {
	private static String TAG = "emergenciAPPS";
	String correo;
	String msje;
	String miNombre;
	String miNumero;
	LocationManager locManager;
	
	public LocationListenerMensaje(String correo, String msje, String miNombre, String miNumero, LocationManager locManager){
		
		this.correo = correo;
		this.msje = msje;
		this.miNombre = miNombre;
		this.miNumero = miNumero;
		this.locManager = locManager;
		
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "se captura localizacion");
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        /*
         * Se crea hilo para enviar mensaje
         *  
         */
        AsyncTask<String, Void, String> enviarMail = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {	
				return ServicioWeb.sendMail(params[0], params[1], correo, msje, miNombre, miNumero);
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				//Toast.makeText,
		         //         "El correo ha llegado a su destinatario", Toast.LENGTH_SHORT).show();
			}
			
		};
        enviarMail.execute(lat,lng);
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
