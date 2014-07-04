package com.example.emergenciapps;

import android.content.Context;

import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.RouteResponse.Location;

public class MyLocationExtends extends MyLocationOverlay{
	MapView map;
	Location lastFix = null;
	
	public MyLocationExtends(Context context, MapView maps) {
		super(context, maps);
		this.map = maps;
		enableMyLocation();
	}

	

	
}
