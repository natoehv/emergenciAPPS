package com.example.emergenciapps;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ServiceFragment extends Fragment {
    public static final String SERVICE_NUMBER = "servicio_number";
    public MapView map;
    public MyLocationExtends myLoc;
    public DefaultItemizedOverlay overlay;
    public ServiceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    		
        View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
        int i = getArguments().getInt(SERVICE_NUMBER);
        switch(i){
        	case 0: rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        	final TextView numeroCarabinero = (TextView) rootView.findViewById(R.id.textView2);
            final TextView numeroBombero = (TextView) rootView.findViewById(R.id.textView3);
            final TextView numeroHospital = (TextView) rootView.findViewById(R.id.textView4);
            
            SharedPreferences prefs = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
            
        	String carabinero = prefs.getString("numeroCarabinero", "x");
        	numeroCarabinero.setText(carabinero);
        	
        	String bombero = prefs.getString("numeroBombero", "86575038");
        	numeroBombero.setText(bombero);
        	
        	String hospital = prefs.getString("numeroHospital", "82998988");
        	numeroHospital.setText(hospital);
        	break;
        	case 1: rootView = inflater.inflate(R.layout.fragment_hospital, container, false);
        	setupMapHospitalView(new GeoPoint(-36.6094, -72.1024), 20, (MapView)rootView.findViewById(R.id.mapHospital));
        	/*
        	 * TODO Generar metodo el cual se conecte al servidor y retorne un arreglo con los puntos m�s cercanos
        	 * al dispositivo
        	 */
        	
        	break;
        	case 2: rootView = inflater.inflate(R.layout.fragment_planet, container, false);
        		
        			
        	break;
        	case 3: rootView = inflater.inflate(R.layout.fragment_planet, container, false);
        	break;
        	case 4: 
        				
        			rootView = inflater.inflate(R.layout.config, container, false);
        			EditText editBombero = (EditText)rootView.findViewById(R.id.editText1);
        			EditText editCarabinero = (EditText)rootView.findViewById(R.id.editText2);
        			EditText editHospital = (EditText)rootView.findViewById(R.id.editText3);
        			EditText editFavorito = (EditText)rootView.findViewById(R.id.editText4);
        			EditText editMensaje = (EditText)rootView.findViewById(R.id.editText5);
        			Button btnGuardar = (Button)rootView.findViewById(R.id.btnGuardar);
                    SharedPreferences pref = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    
                    final String bom = pref.getString("numeroBombero", "132");
                	editBombero.setText(bom);
                    final String car = pref.getString("numeroCarabinero", "133");
                    editCarabinero.setText(car);
                	final String hos = pref.getString("numeroHospital", "131");
                	editHospital.setText(hos);
                	final String fav = pref.getString("numeroFavorito", "911");
                	editFavorito.setText(fav);
                	final String msj = pref.getString("mensaje", "Help!");
                	editMensaje.setText(msj);
                	
                	
                	
                	btnGuardar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							editor.putString("numeroBombero",bom);
							editor.putString("numeroCarabinero",car);
							editor.putString("numeroHospital",hos);
							editor.putString("numeroFavorito",fav);
							editor.putString("mensaje",msj);
							editor.commit();
							
							
							
							
						}
					});
        	break;
        }
//        int i = getArguments().getInt(ARG_PLANET_NUMBER);
//        String planet = getResources().getStringArray(R.array.planets_array)[i];
//
//        int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                        "drawable", getActivity().getPackageName());
//        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//        getActivity().setTitle(planet);
        return rootView;
    }
    
    private void setupMapHospitalView(GeoPoint pt, int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Bitmap iconNew = EmergenciUTIL.resizeImage(iconMiPosicion, 100, 100);
    	overlay = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), iconNew));
    	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
    	map = maps;
    	
    	map.getController().setZoom(zoom);
    	myLoc.enableMyLocation();
    	myLoc.runOnFirstFix(new Runnable() {
          @Override
          public void run() {
            GeoPoint currentLocation = myLoc.getMyLocation();
            map.getController().animateTo(currentLocation);
            map.getController().setCenter(myLoc.getMyLocation());
            map.getController().setZoom(14);
            OverlayItem miPocision = new OverlayItem(myLoc.getMyLocation(), "Eu estoy aqui", "cr7");
            overlay.addItem(miPocision);
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
            myLoc.setFollowing(true);
          }
        });
    	
    }
}
