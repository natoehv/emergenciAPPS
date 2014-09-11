package com.example.emergenciapps;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ServiceFragment extends Fragment {
    public static final String SERVICE_NUMBER = "servicio_number";
    public static final int RADIO_BUSQUEDA = 5;
    public static  int radioBusqueda;
    public static final int ZOOM = 20;
    public MapView map;
    public ListView listaTelefonos;
    public int progressChanged;
    public MyLocationExtends myLoc;
    public DefaultItemizedOverlay overlay;
    public DefaultItemizedOverlay overlayServicio;
    public AnnotationView annotation;
    public Context contexto;
    
    
    public int progreso_a_guardar = 0;
    
    public boolean ok_bom =  false;
    public boolean ok_hos = false;
    public boolean ok_car = false;
    public boolean ok_corr = false;
    public boolean ok_msj = false;
    public boolean ok_miNom = false;
    public boolean ok_miNum = false;
    
    public boolean guadadoExitoso = false;
    private List listaServicio;
    
    
    public ServiceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    		
        View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
        SharedPreferences prefe = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
        radioBusqueda = prefe.getInt("ratio", 1);
        contexto = rootView.getContext();
        Log.d("radioBusqueda",""+radioBusqueda);
        int i = getArguments().getInt(SERVICE_NUMBER);
        Bundle a = getArguments();
        listaServicio = (List) getArguments().getSerializable("lista");
        switch(i){
        	//case 0
        	case 0: rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        	
        	final TextView numeroCarabinero = (TextView) rootView.findViewById(R.id.textView2);
            final TextView numeroBombero = (TextView) rootView.findViewById(R.id.textView3);
            final TextView numeroHospital = (TextView) rootView.findViewById(R.id.textView4);
            Button btnAyuda = (Button) rootView.findViewById(R.id.btnEmergencia);
            TaskSendMail mail = TaskSendMail.getInstance();
            if(mail.getStatus() == AsyncTask.Status.RUNNING){
            	btnAyuda.setBackgroundResource(R.drawable.ayuda_pulsado);
            }
            SharedPreferences prefs = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
            
        	String carabinero = prefs.getString("numeroCarabinero", "5255");
        	numeroCarabinero.setText(carabinero);
        	
        	String bombero = prefs.getString("numeroBombero", "86575038");
        	numeroBombero.setText(bombero);
        	
        	String hospital = prefs.getString("numeroHospital", "82998988");
        	numeroHospital.setText(hospital);
        	break;
        	//case 1
        	case 1: rootView = inflater.inflate(R.layout.fragment_hospital, container, false);
        	Button btnHospital = (Button)rootView.findViewById(R.id.llamar131);
        	
        	btnHospital.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:131")); 
					v.getContext().startActivity(intent);
				}
        	});
        	 
   		    
   		    
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroHospital);
        	setupMapHospitalView(20, (MapView)rootView.findViewById(R.id.mapHospital));
        	
        	break;
        	// case 2
        	case 2: rootView = inflater.inflate(R.layout.fragment_bombero, container, false);
        	
        	Button btnBombero = (Button)rootView.findViewById(R.id.llamar132);
        	
        	btnBombero.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:132")); 
					v.getContext().startActivity(intent);
				}
        	});
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroBombero);
        	setupMapBomberoView(20, (MapView)rootView.findViewById(R.id.mapBombero));
        	
        	break;
        	
        	// case 3
        	case 3: rootView = inflater.inflate(R.layout.fragment_carabinero, container, false);
        	
        	Button btnCarabinero = (Button)rootView.findViewById(R.id.llamar133);
        	
        	btnCarabinero.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:133")); 
					v.getContext().startActivity(intent);
				}
        	});
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroCarabinero);
        	setupMapCarabineroView(20, (MapView)rootView.findViewById(R.id.mapCarabinero));
        	break;        	
        	
        	// case 4
        	case 4: rootView = inflater.inflate(R.layout.fragment_pdi, container, false);
        	Button btnPDI = (Button)rootView.findViewById(R.id.llamar134);
        	
        	btnPDI.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:134")); 
					v.getContext().startActivity(intent);
				}
        	});
        	
        	listaTelefonos = (ListView) rootView.findViewById(R.id.listaNroPdi);
        	setupMapPDIView(20, (MapView)rootView.findViewById(R.id.mapPdi));

        	break;
        	//case 5
        	case 5: 
        				
        			rootView = inflater.inflate(R.layout.config, container, false);
        			final EditText editBombero = (EditText)rootView.findViewById(R.id.editText1);
        			final EditText editCarabinero = (EditText)rootView.findViewById(R.id.editText2);
        			final EditText editHospital = (EditText)rootView.findViewById(R.id.editText3);
        			final EditText editMiNombre = (EditText)rootView.findViewById(R.id.editText40);
        			final EditText editMiNumero = (EditText)rootView.findViewById(R.id.editText50);
        			final EditText editFavorito = (EditText)rootView.findViewById(R.id.editText4);
        			final EditText editMensaje = (EditText)rootView.findViewById(R.id.editText5);
        			final Button btnGuardar = (Button)rootView.findViewById(R.id.btnGuardar);
        			
                    SharedPreferences pref = rootView.getContext().getSharedPreferences("MisContactos", rootView.getContext().MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    
                    final String bom = pref.getString("numeroBombero", "");
                	editBombero.setText(bom);
                    final String car = pref.getString("numeroCarabinero", "");
                    editCarabinero.setText(car);
                	final String hos = pref.getString("numeroHospital", "");
                	editHospital.setText(hos);
                	final String miNombre = pref.getString("miNombre", "");
                	editMiNombre.setText(miNombre);
                	final String miNumero = pref.getString("miNumero", "");
                	editMiNumero.setText(miNumero);
                	final String fav = pref.getString("correoContacto", "");
                	editFavorito.setText(fav);
                	final String msj = pref.getString("mensaje", "Help!");
                	editMensaje.setText(msj);
                	
                	final int radio = pref.getInt("ratio", 6);
                	
                	
                	
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
								editCarabinero.setError("Ingrese número favorito de Carabinero");
							}
							
							if(!editHospital.getText().toString().equals("")){
								ok_hos = true;
							}else{
								ok_hos = false;
								editHospital.setError("Ingrese número favorito de Hospital");
							}
							
							if(!editMiNombre.getText().toString().equals("")){
								ok_miNom = true;
							}else{
								ok_miNom = false;
								editMiNombre.setError("Ingrese su nombre");
								
							}
							if(!editMiNumero.getText().toString().equals("")){
								ok_miNum = true;
							}else{
								ok_miNum = false;
								editMiNumero.setError("Ingrese su número de contacto");
							}
							
							if(!editFavorito.getText().toString().equals("")){
								ok_corr = true;
							}else{
								ok_corr = false;
								editFavorito.setError("Ingrese correo electrónico para alertar");
							}
							
							if(!editMensaje.getText().toString().equals("")){
								ok_msj = true;
							}else{
								ok_msj = false;
								editMensaje.setError("Ingrese datos");
							}
							
							
							
							if(ok_bom == true && ok_car == true && ok_hos==true && ok_corr == true && ok_msj == true && ok_miNom == true && ok_miNum == true){
								Log.d("true","campos completos");
								editor.putString("numeroBombero",editBombero.getText().toString());
								editor.putString("numeroCarabinero",editCarabinero.getText().toString());
								editor.putString("numeroHospital",editHospital.getText().toString());
								editor.putString("miNombre", editMiNombre.getText().toString());
								editor.putString("miNumero", editMiNumero.getText().toString());
								editor.putString("correoContacto",editFavorito.getText().toString());
								editor.putString("mensaje",editMensaje.getText().toString());
								if(progreso_a_guardar == 0){
									editor.putInt("ratio",radio);
									Log.d("progreso1",""+radio);
								}else{
									editor.putInt("ratio",progreso_a_guardar);
									Log.d("progreso2",""+progreso_a_guardar);
								}
							    
								
								
								editor.commit();
								guadadoExitoso = true;
								Toast.makeText(v.getContext(),
						                  "Se ha guardado la configuración", Toast.LENGTH_SHORT).show();
								
							}else{
								Log.d("false","hay campos vacios");
								
								Toast.makeText(v.getContext(),
					                  "Llene los campos indicados", Toast.LENGTH_SHORT).show();
							}
							
							
							
						}
						 
					});
                	
                	
        	break;
        	//case 6
        	case 6: rootView = inflater.inflate(R.layout.fragmento_tutorial, container, false);
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
    	Drawable iconHospital = maps.getContext().getResources().getDrawable(R.drawable.marcadorhospital);
    	overlay = new DefaultItemizedOverlay(iconMiPosicion);
    	overlayServicio = new DefaultItemizedOverlay(iconHospital);
    	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
    	map = maps;
    	annotation = new AnnotationView(maps);
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
           
           
            int radio  ;
            List<Hospital> lista = (List<Hospital>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "centro_medico");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Hospital h: lista){
            	item = new OverlayItem(new GeoPoint(h.getX(),h.getY()), h.getNombre(), h.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				overlayServicio.setTapListener(new ItemizedOverlay.OverlayTapListener(){
					@Override
					public void onTap(GeoPoint arg0, MapView arg1) {
						int lastTouchedIndex = overlayServicio.getLastFocusedIndex();
						if(lastTouchedIndex > -1){
							OverlayItem tapped =  overlayServicio.getItem(lastTouchedIndex);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
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
    	Drawable iconCarabinero = maps.getContext().getResources().getDrawable(R.drawable.marcadorcarabinero);
    	overlay = new DefaultItemizedOverlay(iconMiPosicion);
    	overlayServicio = new DefaultItemizedOverlay(iconCarabinero);
    	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
    	map = maps;
    	annotation = new AnnotationView(maps);
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
            List<Carabinero> lista;
            if(listaServicio != null){
            	Log.d("emergenciAPPS_TAG", "Cargando ubicacion encontrada por comuna");
            	lista = listaServicio;
            	if(listaServicio.size() > 0){
	            	Float x = ((Carabinero)listaServicio.get(0)).getX();
	            	Float y = ((Carabinero)listaServicio.get(0)).getY();
	            	map.getController().animateTo(new GeoPoint(x,y));
	                map.getController().setCenter(new GeoPoint(x,y));
            	}
            }else{
            	lista = (List<Carabinero>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "carabinero");
            }
            
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Carabinero c: lista){
            	item = new OverlayItem(new GeoPoint(c.getX(),c.getY()), c.getNombre(), c.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				overlayServicio.setTapListener(new ItemizedOverlay.OverlayTapListener(){
					@Override
					public void onTap(GeoPoint arg0, MapView arg1) {
						int lastTouchedIndex = overlayServicio.getLastFocusedIndex();
						if(lastTouchedIndex > -1){
							OverlayItem tapped =  overlayServicio.getItem(lastTouchedIndex);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
				 TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				 if(listaServicio == null)
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
				 else
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna"); 
    			
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayServicio);
            myLoc.disableMyLocation();
            //myLoc.setFollowing(true);
          }
          
        });
    	
    }
    
    private void setupMapBomberoView(int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Drawable iconBombero = maps.getContext().getResources().getDrawable(R.drawable.marcadorbombero);
    	overlay = new DefaultItemizedOverlay(iconMiPosicion);
    	overlayServicio = new DefaultItemizedOverlay(iconBombero);
    	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
    
    	map = maps;
    	annotation = new AnnotationView(maps);
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
			List<Bombero> lista = (List<Bombero>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "bombero");
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Bombero b: lista){
            	item = new OverlayItem(new GeoPoint(b.getX(),b.getY()), b.getNombre(), b.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				overlayServicio.setTapListener(new ItemizedOverlay.OverlayTapListener(){
					
					@Override
					public void onTap(GeoPoint arg0, MapView arg1) {
						int lastTouchedIndex = overlayServicio.getLastFocusedIndex();
						if(lastTouchedIndex > -1){
							OverlayItem tapped =  overlayServicio.getItem(lastTouchedIndex);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
			     TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
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
        	Drawable iconPDI = maps.getContext().getResources().getDrawable(R.drawable.marcadorpdi);
        	overlay = new DefaultItemizedOverlay(iconMiPosicion);
        	overlayServicio = new DefaultItemizedOverlay(iconPDI);
        	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
        	map = maps;
        	annotation = new AnnotationView(maps);
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
                List<PDI> lista = (List<PDI>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "pdi");
                OverlayItem item;
                overlay.addItem(miPocision);
                for(PDI c: lista){
                	item = new OverlayItem(new GeoPoint(c.getX(),c.getY()), c.getNombre(), c.getDireccion());
                	overlayServicio.addItem(item);
                }
                
                TareaLlenaNumeros tarea;
    			if(lista.size()>0){
    				overlayServicio.setTapListener(new ItemizedOverlay.OverlayTapListener(){
    					
						@Override
						public void onTap(GeoPoint arg0, MapView arg1) {
							int lastTouchedIndex = overlayServicio.getLastFocusedIndex();
							if(lastTouchedIndex > -1){
								OverlayItem tapped =  overlayServicio.getItem(lastTouchedIndex);
								annotation.showAnnotationView(tapped);
							}
							
						}
    					
    				});
    				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
    				tarea.execute();
    			}else{
    				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
   				 	mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
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
