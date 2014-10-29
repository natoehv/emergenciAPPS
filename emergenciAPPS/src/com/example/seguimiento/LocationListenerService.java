package com.example.seguimiento;

import com.example.emergenciapps.ServicioWeb;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class LocationListenerService implements LocationListener {
	private String miNumero;
	public LocationListenerService(String miNumero){
		this.miNumero = miNumero;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		final String lat = String.valueOf(location.getLatitude());
        final String lng = String.valueOf(location.getLongitude());
        String respuesta = ServicioWeb.actualizaPosicion(lat, lng, miNumero);
        Log.d("actualizacion_ubicacion",respuesta);
        
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
