package com.example.seguimiento;

import com.example.emergenciapps.LocationListenerMensaje;
import com.example.emergenciapps.ServicioWeb;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service{
	LocationManager locManager;
	LocationListener locListener;
	String miNumero ="";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locManager.removeUpdates(locListener);
		 AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					String respuesta = ServicioWeb.actualizaPosicion("0", "0", miNumero);
					Log.d("actualizacion_ubicacion",respuesta);
					return null;
				}}
	        ;
	        tarea.execute();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("MyService","EN EJECUCION");
		SharedPreferences prefs = getSharedPreferences("miCuenta", this.MODE_PRIVATE);
		miNumero = prefs.getString("miNumero", "");
		locManager = (LocationManager)getSystemService(this.LOCATION_SERVICE);
    	locListener = new LocationListenerService(miNumero);
	    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
		return START_STICKY;
	}
	
	
	
	

}
