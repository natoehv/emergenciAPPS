package com.example.emergenciapps;

import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

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
        ServicioWeb.sendMail(lat, lng, correo, msje, miNombre, miNumero);
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
