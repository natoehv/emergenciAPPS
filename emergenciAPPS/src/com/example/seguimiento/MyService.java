package com.example.seguimiento;

import com.example.emergenciapps.LocationListenerMensaje;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class MyService extends Service{
	LocationManager locManager;
	LocationListener locListener;
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
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		String miNumero ="";
		if(extras != null){
			miNumero = extras.getString("miNumero");
		}
		locManager = (LocationManager)getSystemService(this.LOCATION_SERVICE);
    	locListener = new LocationListenerService(miNumero);
	    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	

}
