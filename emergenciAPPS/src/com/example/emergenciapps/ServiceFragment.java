package com.example.emergenciapps;

import java.util.List;

import android.app.Fragment;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
    public int progressChanged;
    public MyLocationExtends myLoc;
    public DefaultItemizedOverlay overlay;
    public DefaultItemizedOverlay overlayServicio;
    
    
    
    public int progreso_a_guardar = 0;
    
    public boolean ok_bom =  false;
    public boolean ok_hos = false;
    public boolean ok_car = false;
    public boolean ok_corr = false;
    public boolean ok_msj = false;
   ;
   
    
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
        	
        	break;
        	case 2: rootView = inflater.inflate(R.layout.fragment_bombero, container, false);
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroBombero);
        	setupMapBomberoView(20, (MapView)rootView.findViewById(R.id.mapBombero));
        	
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
        			final EditText editBombero = (EditText)rootView.findViewById(R.id.editText1);
        			final EditText editCarabinero = (EditText)rootView.findViewById(R.id.editText2);
        			final EditText editHospital = (EditText)rootView.findViewById(R.id.editText3);
        			final EditText editFavorito = (EditText)rootView.findViewById(R.id.editText4);
        			final EditText editMensaje = (EditText)rootView.findViewById(R.id.editText5);
        			final Button btnGuardar = (Button)rootView.findViewById(R.id.btnGuardar);
                    SharedPreferences pref = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    
                    final String bom = pref.getString("numeroBombero", "132");
                	editBombero.setText(bom);
                    final String car = pref.getString("numeroCarabinero", "133");
                    editCarabinero.setText(car);
                	final String hos = pref.getString("numeroHospital", "131");
                	editHospital.setText(hos);
                	final String fav = pref.getString("correoContacto", "");
                	editFavorito.setText(fav);
                	final String msj = pref.getString("mensaje", "Help!");
                	editMensaje.setText(msj);
                	
                	int radio = pref.getInt("ratio", 1);
                	
                	
                	
                	SeekBar radioControl = null;
                	radioControl = (SeekBar) rootView.findViewById(R.id.volume_bar);
                	radioControl.setProgress(radio);
                	radioControl.setMax(20);
                	
                	
                    radioControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            			int progressChanged = 0;
            			
            			@Override
            			public void onStartTrackingTouch(SeekBar seekBar) {
            				// TODO Auto-generated method stub
            			}
            			@Override
            			public void onStopTrackingTouch(SeekBar seekBar) {
            				//Log.d("llamada", ""+progressChanged);
            			}
						@Override
						public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser) {
							Log.d("llamada", ""+progressChanged);
							progressChanged = progress;
							progreso_a_guardar = progress;
							
							
						}
            		});
            		
                	
                	
                	btnGuardar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(!editBombero.getText().toString().equals("")){
								ok_bom = true;
							}else{
								ok_bom = false;
								editBombero.setError("Ingrese datos");
							}
							
							if(!editCarabinero.getText().toString().equals("")){
								ok_car = true;
							}else{
								ok_car = false;
								editCarabinero.setError("Ingrese datos");
							}
							
							if(!editHospital.getText().toString().equals("")){
								ok_hos = true;
							}else{
								ok_hos = false;
								editHospital.setError("Ingrese datos");
							}
							
							if(!editFavorito.getText().toString().equals("")){
								ok_corr = true;
							}else{
								ok_corr = false;
								editFavorito.setError("Ingrese datos");
							}
							
							if(!editMensaje.getText().toString().equals("")){
								ok_msj = true;
							}else{
								ok_msj = false;
								editMensaje.setError("Ingrese datos");
							}
							
							
							
							if(ok_bom == true && ok_car == true && ok_hos==true && ok_corr == true && ok_msj == true ){
								Log.d("true","campos completos");
								editor.putString("numeroBombero",editBombero.getText().toString());
								editor.putString("numeroCarabinero",editCarabinero.getText().toString());
								editor.putString("numeroHospital",editHospital.getText().toString());
								editor.putString("correoContacto",editFavorito.getText().toString());
								editor.putString("mensaje",editMensaje.getText().toString());
								if(progreso_a_guardar == 0){
									editor.putInt("ratio",1);
								}else{
									editor.putInt("ratio",progreso_a_guardar);
								}
								
								editor.commit();
								
							}else{
								Log.d("false","hay campos vacios");
								
								//Toast.makeText(rootView.getContext(),
					             //      "Llene los campos indicados", Toast.LENGTH_SHORT).show();
							}
						
							
							
							
							
							
							
							
							
							
							
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
    /**
     * Metodo encargado de crear mapa para hospital
     * @param zoom
     * @param maps
     */
    private void setupMapHospitalView(int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Bitmap iconNew = EmergenciUTIL.resizeImage(iconMiPosicion, 100, 100);
    	Drawable iconHospital = maps.getContext().getResources().getDrawable(R.drawable.marcadorhospital);
    	Bitmap hospNuevo = EmergenciUTIL.resizeImage(iconHospital, 100, 100);
    	overlay = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), iconNew));
    	overlayServicio = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), hospNuevo));;
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
            List<Hospital> lista = (List<Hospital>) ServicioWeb.postCercanos(currentLocation, RADIO_BUSQUEDA, "hospital");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Hospital h: lista){
            	item = new OverlayItem(new GeoPoint(h.getX(),h.getY()), h.getNombre(), h.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayServicio);
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
    	overlayServicio = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayServicio);
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
    	overlayServicio = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
            @SuppressWarnings("unchecked")
			List<Bombero> lista = (List<Bombero>) ServicioWeb.postCercanos(currentLocation, RADIO_BUSQUEDA, "bombero");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Bombero b: lista){
            	item = new OverlayItem(new GeoPoint(b.getX(),b.getY()), b.getNombre(), b.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayServicio);
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
        	overlayServicio = new DefaultItemizedOverlay(new BitmapDrawable(maps.getContext().getResources(), carabNuevo));;
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
                	overlayServicio.addItem(item);
                }
                TareaLlenaNumeros tarea;
    			if(lista.size()>0){
    				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
    				tarea.execute();
    			}
                map.getOverlays().add(myLoc);
                map.getOverlays().add(overlay);
                map.getOverlays().add(overlayServicio);
                myLoc.disableMyLocation();
                myLoc.setFollowing(true);
              }
              
            });	
    }
}
