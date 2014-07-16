package com.example.emergenciapps;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LocationListenerMensaje implements LocationListener {
	private static String TAG = "emergenciAPPS";
	EmailEmergencia mail;
	View v;
	LocationManager locManager;
	TaskSendMail enviarMail;
	public LocationListenerMensaje(LocationManager locManager,EmailEmergencia mail,View v){
		this.enviarMail = TaskSendMail.getInstance();
		this.mail = mail;
		this.v = v;
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
        Log.d("emergenciAPPS",enviarMail.getStatus().toString());
        if(enviarMail.getStatus() == AsyncTask.Status.RUNNING){
        	//new TareaMuestraMensaje(mail.getContext()).execute("Su mensaje ya está siendo enviado");
        }else{
        	enviarMail.execute(lat,lng,mail,v);
        }
        	
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
