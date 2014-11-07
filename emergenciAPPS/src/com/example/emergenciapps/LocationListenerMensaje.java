package com.example.emergenciapps;

import java.util.List;

import com.example.contactos.DetalleContactoActivity;
import com.example.contactos.ListaContactosActivity;
import com.example.object.EmailEmergencia;
import com.example.seguimiento.MyService;
import com.example.seguimiento.SeguimientoActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LocationListenerMensaje implements LocationListener {
	private ProgressDialog ringProgressDialog;
	private static String TAG = "emergenciAPPS";
	private static final int NOTIF_ALERTA_ID = 1;
	
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
				return ServicioWeb.sendNotification(lat, lng, miNumero).trim();
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.d("respuesta_servidor",""+result);
				if(result.equals("true")){
					mostrarNotification("Alerta activa, selecciona para desactivar","Alerta activada");
					Intent i = new Intent(v.getContext(), MyService.class);
	            	
					if(v.getContext().startService(i) == null){
	            		Log.d("ESTADO SERVICIO","NO EJECUTADO");
	            	}else{
	            		Log.d("ESTADO SERVICIO","EJECUTADO");
	            	}
					
					
					
	            	
	            	
				}
				
            	
            	
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
	
	private void mostrarNotification(String msg, String title){

		NotificationManager mNotificationManager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = 
			new NotificationCompat.Builder(v.getContext())  
				.setSmallIcon(R.drawable.ic_launcher)  
				.setContentTitle(title)
				.setContentText(msg)
				.setTicker(title)
				.setOngoing(true);
		
		Intent notIntent =  new Intent(v.getContext(),EmergenciAPPSActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(v.getContext(), 0, notIntent, 0);   
		
		mBuilder.setContentIntent(contIntent);
		
		mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
	
	

}
