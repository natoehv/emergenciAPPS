package com.example.emergenciapps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emergenciapps.R.id;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ServiceFragment extends Fragment {
    public static final String SERVICE_NUMBER = "servicio_number";
    public static final int RADIO_BUSQUEDA = 5;
    public static final int ZOOM = 20;
    public MapView map;
    public ListView listaTelefonos;
    public MyLocationExtends myLoc;
    public DefaultItemizedOverlay overlay;
    public DefaultItemizedOverlay overlayCarabinero;
    public DefaultItemizedOverlay overlayBombero;
    public DefaultItemizedOverlay overlayPDI;
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
        	setupMapHospitalView(20, (MapView)rootView.findViewById(R.id.mapHospital));
        	/*
        	 * TODO Generar metodo el cual se conecte al servidor y retorne un arreglo con los puntos m�s cercanos
        	 * al dispositivo
        	 */
        	
        	break;
        	case 2: rootView = inflater.inflate(R.layout.fragment_bombero, container, false);
        			
        	break;
        	case 3: rootView = inflater.inflate(R.layout.fragment_carabinero, container, false);
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroCarabinero);
        	setupMapCarabineroView(20, (MapView)rootView.findViewById(R.id.mapCarabinero));
        	break;        	
        	case 4: rootView = inflater.inflate(R.layout.fragment_pdi, container, false);
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroPdi);
        	setupMapPDIView(20, (MapView)rootView.findViewById(R.id.mapPdi));

        	break;
        	case 5: 
        				
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
    
    private void setupMapHospitalView(int zoom, MapView maps){
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
    /**
     * Metodo encargado de cargar mapa de carabineros
     * @param zoom
     * @param maps
     */
    private void setupMapCarabineroView(int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Bitmap iconNew = EmergenciUTIL.resizeImage(iconMiPosicion, 100, 100);
    	Drawable iconCarabinero = maps.getContext().getResources().getDrawable(R.drawable.marcadorcarabinero);
    	Bitmap carabNuevo = EmergenciUTIL.resizeImage(iconCarabinero, 100, 100);
    	overlay = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), iconNew));
    	overlayCarabinero = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
            List<Carabinero> lista = (List<Carabinero>) ServicioWeb.postCercanos(currentLocation, RADIO_BUSQUEDA, "carabinero");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Carabinero c: lista){
            	item = new OverlayItem(new GeoPoint(c.getX(),c.getY()), c.getNombre(), c.getDireccion());
            	overlayCarabinero.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayCarabinero);
            myLoc.disableMyLocation();
            myLoc.setFollowing(true);
          }
          
        });
    	
    }
    
    private void setupMapBomberoView(int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Bitmap iconNew = EmergenciUTIL.resizeImage(iconMiPosicion, 100, 100);
    	Drawable iconCarabinero = maps.getContext().getResources().getDrawable(R.drawable.marcadorbombero);
    	Bitmap carabNuevo = EmergenciUTIL.resizeImage(iconCarabinero, 100, 100);
    	overlay = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), iconNew));
    	overlayBombero = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
            List<Carabinero> lista = (List<Carabinero>) ServicioWeb.postCercanos(currentLocation, RADIO_BUSQUEDA, "bombero");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Carabinero c: lista){
            	item = new OverlayItem(new GeoPoint(c.getX(),c.getY()), c.getNombre(), c.getDireccion());
            	overlayBombero.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayBombero);
            myLoc.disableMyLocation();
            myLoc.setFollowing(true);
          }
          
        });
    }
    	private void setupMapPDIView(int zoom, MapView maps){
        	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
        	Bitmap iconNew = EmergenciUTIL.resizeImage(iconMiPosicion, 100, 100);
        	Drawable iconCarabinero = maps.getContext().getResources().getDrawable(R.drawable.marcadorpdi);
        	Bitmap carabNuevo = EmergenciUTIL.resizeImage(iconCarabinero, 100, 100);
        	overlay = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), iconNew));
        	overlayPDI = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
                List<PDI> lista = (List<PDI>) ServicioWeb.postCercanos(currentLocation, RADIO_BUSQUEDA, "pdi");
                OverlayItem item;
                overlay.addItem(miPocision);
                for(PDI c: lista){
                	item = new OverlayItem(new GeoPoint(c.getX(),c.getY()), c.getNombre(), c.getDireccion());
                	overlayPDI.addItem(item);
                }
                TareaLlenaNumeros tarea;
    			if(lista.size()>0){
    				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
    				tarea.execute();
    			}
                map.getOverlays().add(myLoc);
                map.getOverlays().add(overlay);
                map.getOverlays().add(overlayPDI);
                myLoc.disableMyLocation();
                myLoc.setFollowing(true);
              }
              
            });
    	
    }
}
