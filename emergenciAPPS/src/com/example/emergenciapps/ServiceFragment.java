package com.example.emergenciapps;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactos.DetalleContactoActivity;
import com.example.contactos.ListaContactosActivity;
import com.example.login.LoginActivity;
import com.example.object.Bombero;
import com.example.object.Carabinero;
import com.example.object.Configuracion;
import com.example.object.Hospital;
import com.example.object.PDI;
import com.example.persistencia.ContactoSQLHelper;
import com.example.seguimiento.SeguimientoActivity;
import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ServiceFragment extends Fragment {
    public static final String SERVICE_NUMBER = "servicio_number";
    public static final int RADIO_BUSQUEDA = 5;
    public static  int radioBusqueda;
    public String miNumero;
    public String miNombre;
    private static final int ZOOM = 20;
    private MapView map;
    private ListView listaTelefonos;
    private int progressChanged;
    private MyLocationExtends myLoc;
    private DefaultItemizedOverlay overlay;
    private DefaultItemizedOverlay overlayServicio;
    private AnnotationView annotation;
    private Context contexto;
    private RouteManager routeManager;
    private boolean cercanos;
    private int progreso_a_guardar = 0;
    
    private boolean ok_bom =  false;
    private boolean ok_hos = false;
    private boolean ok_car = false;
    private boolean ok_corr = false;
    private boolean ok_msj = false;
    private boolean ok_miNom = false;
    private boolean ok_miNum = false;
    
    private ProgressDialog ringProgressDialog;
    
    private EditText editBombero;
    private EditText editCarabinero;
    private EditText editHospital;
    private EditText editMensaje;
    
    private boolean guadadoExitoso = false;
    private RespuestaServicioWeb respuestaBusqueda;
    
    
    public ServiceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
        routeManager = new RouteManager(rootView.getContext(),"Fmjtd%7Cluur2g0bn0%2Cb0%3Do5-9at2qu");
        SharedPreferences prefe = rootView.getContext().getSharedPreferences("miCuenta", rootView.getContext().MODE_PRIVATE);
        radioBusqueda = prefe.getInt("radio_busqueda", 1);
        miNumero = prefe.getString("miNumero", "");
        contexto = rootView.getContext();
        miNombre = prefe.getString("miNombre", "");
        Log.d("radioBusqueda",""+radioBusqueda);
        int i = getArguments().getInt(SERVICE_NUMBER);
        Bundle a = getArguments();
        respuestaBusqueda = (RespuestaServicioWeb) getArguments().getSerializable("respuesta");
        cercanos = (respuestaBusqueda == null)? true : false;
        switch(i){
        	//case 0
        	case 0: rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        	
        	final TextView numeroCarabinero = (TextView) rootView.findViewById(R.id.textView2);
            final TextView numeroBombero = (TextView) rootView.findViewById(R.id.textView3);
            final TextView numeroHospital = (TextView) rootView.findViewById(R.id.textView4);
            Button btnAyuda = (Button) rootView.findViewById(R.id.btnEmergencia);
            
            
            SharedPreferences prefs = rootView.getContext().getSharedPreferences("miCuenta", rootView.getContext().MODE_PRIVATE);
            
        	String carabinero = prefs.getString("numero_carabinero", "133");
        	numeroCarabinero.setText(carabinero);
        	
        	String bombero = prefs.getString("numero_bombero", "132");
        	numeroBombero.setText(bombero);
        	
        	String hospital = prefs.getString("numero_centro_medico", "131");
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
        	if(cercanos){
        		setupMapHospitalViewCercanos(20, (MapView)rootView.findViewById(R.id.mapHospital));
        	}else{
        		setupMapHospitalViewComuna(20, (MapView)rootView.findViewById(R.id.mapHospital));
        	}
        	
        	
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
        	if(cercanos){
        		setupMapBomberoViewCercanos(14, (MapView)rootView.findViewById(R.id.mapBombero));
        	}else{
        		setupMapBomberoViewComuna(14, (MapView)rootView.findViewById(R.id.mapBombero));
        	}
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
        	if(cercanos){
        		setupMapCarabineroViewCercanos(14, (MapView)rootView.findViewById(R.id.mapCarabinero));
        	}else{
        		setupMapCarabineroViewComuna(14, (MapView)rootView.findViewById(R.id.mapCarabinero));
        	}
        	
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
        	if(cercanos){
        		setupMapPDIViewCercanos(20, (MapView)rootView.findViewById(R.id.mapPdi));
        	}else{
        		setupMapPDIViewComuna(20, (MapView)rootView.findViewById(R.id.mapPdi));
        	}
        	

        	break;
        	case 5: 
        			
        		break;
        	//case 6
        	case 6: 
        				
        			rootView = inflater.inflate(R.layout.configuracion, container, false);
        			editBombero = (EditText)rootView.findViewById(R.id.editBombero);
        			editCarabinero = (EditText)rootView.findViewById(R.id.editCarabinero);
        			editHospital = (EditText)rootView.findViewById(R.id.editCentroMedico);
        			editMensaje = (EditText)rootView.findViewById(R.id.editMensajeAlerta);
        			final Button btnContacto = (Button)rootView.findViewById(R.id.boton_contactos);
        			
        			btnContacto.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Intent i = new Intent(contexto, ListaContactosActivity.class);
					        startActivity(i);
							
						}
					});
        			
                    SharedPreferences pref = rootView.getContext().getSharedPreferences("miCuenta", rootView.getContext().MODE_PRIVATE);
                    
                    final String bom = pref.getString("numero_bombero", "");
                	editBombero.setText(bom);
                    final String car = pref.getString("numero_carabinero", "");
                    editCarabinero.setText(car);
                	final String hos = pref.getString("numero_centro_medico", "");
                	editHospital.setText(hos);
                	final String msj = pref.getString("mensaje_alerta", "Help!");
                	editMensaje.setText(msj);
                	
                	final int radio = pref.getInt("radio_busqueda", 6);
                	
                	progreso_a_guardar = radio;
                	
                	SeekBar radioControl = (SeekBar) rootView.findViewById(R.id.seekBar1);
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
            		
                	
                	
                	
        	break;
        	//case 7
        	case 7: AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        	builder.create();
        	builder.setTitle("Salir");
        	builder.setMessage("¿ Cerrar sesión ?");
        	builder.setPositiveButton("SI",
        	        new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int which) {
        	            		AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
        	            		
        	            		@Override
        						protected void onPreExecute() {
        							super.onPreExecute();
        							ringProgressDialog = ProgressDialog.show(contexto, "Por favor espere ...", "Cerrando sesión ...", true);
        							ringProgressDialog.setCancelable(false);
        							
        						}
        	            		
								@Override
								protected String doInBackground(String... arg0) {
										Boolean resultado = ServicioWeb.eliminarRegId(miNumero);
										if(resultado){
											return "Sesión cerrada correctamente";
										}else{
											return "No fue posible eliminar identificador";
										}
										
									
									
									
								}
								
								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									ringProgressDialog.dismiss();
									Toast.makeText(contexto, result, Toast.LENGTH_SHORT).show();
									SharedPreferences prefs = contexto.getSharedPreferences("sesion", contexto.MODE_PRIVATE);
									SharedPreferences.Editor editor = prefs.edit();
									editor.clear();
									editor.commit();	
									
									SharedPreferences prefs2 = contexto.getSharedPreferences("miCuenta", contexto.MODE_PRIVATE);
									SharedPreferences.Editor editor2 = prefs2.edit();
									editor2.clear();
									editor2.commit();
									
									SharedPreferences prefs3 = contexto.getSharedPreferences("LoginActivity", contexto.MODE_PRIVATE);
									SharedPreferences.Editor editor3 = prefs3.edit();
									editor3.clear();
									editor3.commit();
									
									ContactoSQLHelper oData = new ContactoSQLHelper(contexto); 
									SQLiteDatabase db = oData.getWritableDatabase();
									db.execSQL("delete from contacto");
									db.close();
									oData.close();
									
									Intent i = new Intent(getActivity(), LoginActivity.class); 
									startActivity(i); 
									getActivity().finish();
									
									
								}
        	            		
        	            	};
        	            	tarea.execute();
        	            	
        	            }
        	        });
        	
        	builder.setNegativeButton("NO",
        	        new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	Intent i = new Intent(getActivity(), EmergenciAPPSActivity.class); 
					startActivity(i); 
					getActivity().finish();
                }
            });
        	builder.show();
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
    private void setupMapHospitalViewCercanos(int zoom, MapView maps){
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
            //List<Hospital> lista = (List<Hospital>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "centro_medico");
            List<Hospital> lista;
        	respuestaBusqueda = (RespuestaServicioWeb) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "centro_medico");
        	lista = (List<Hospital>)respuestaBusqueda.getLista();

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
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
			}
            map.getOverlays().add(myLoc);
            map.getOverlays().add(overlay);
            map.getOverlays().add(overlayServicio);
            myLoc.disableMyLocation();
            myLoc.setFollowing(true);
          }
          
        });
    	
    }
    
    private void setupMapHospitalViewComuna(int zoom, MapView maps){
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
            //List<Hospital> lista = (List<Hospital>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "centro_medico");
            List<Hospital> lista;
        	Log.d("emergenciAPPS_TAG", "Cargando ubicacion encontrada por comuna");
        	lista = respuestaBusqueda.getLista();
        	if(lista.size() > 0){
            	map.getController().animateTo(lista.get(0).getGeoPoint());
                map.getController().setCenter(lista.get(0).getGeoPoint());
        	}
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Hospital h: lista){
            	item = new OverlayItem(h.getGeoPoint(), h.getNombre(), h.getDireccion());
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
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				new TareaLlenaNumeros(lista, listaTelefonos).execute();
			}else{
				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
			}
            
            map.getOverlays().add(overlayServicio);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
          }
          
        });
    	
    }
    /**
     * Metodo encargado de cargar mapa de carabineros
     * @param zoom
     * @param maps
     */
    private void setupMapCarabineroViewCercanos(int zoom, MapView maps){
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
        	respuestaBusqueda = (RespuestaServicioWeb) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "carabinero");
        	lista = (List<Carabinero>)respuestaBusqueda.getLista();
 
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Carabinero c: lista){
            	item = new OverlayItem(c.getGeoPoint(), c.getNombre(), c.getDireccion());
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
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
				 TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				 switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
				 
				 
    			
			}
            map.getOverlays().add(overlayServicio);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
            //myLoc.setFollowing(true);
          }
          
        });
    	
    }
    
    private void setupMapCarabineroViewComuna(int zoom, MapView maps){
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
        	Log.d("emergenciAPPS_TAG", "Cargando ubicacion encontrada por comuna");
        	lista = respuestaBusqueda.getLista();
        	if(lista.size() > 0){
            	map.getController().animateTo(lista.get(0).getGeoPoint());
                map.getController().setCenter(lista.get(0).getGeoPoint());
        	}

            
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Carabinero c: lista){
            	item = new OverlayItem(c.getGeoPoint(), c.getNombre(), c.getDireccion());
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
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				new TareaLlenaNumeros(lista, listaTelefonos).execute();
			}else{
				 TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				 switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
				 
				 
    			
			}
            
            map.getOverlays().add(overlayServicio);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
            //myLoc.setFollowing(true);
          }
          
        });
    	
    }
    /**
     * Metodo encargado de cargar mapa de bomberos cercanos a un punto en el 
     * mapa obtenido desde el GPS
     * @param zoom distancia de visual de mapa
     * @param maps mapa al cual se le cargaran los resultados
     */
    private void setupMapBomberoViewCercanos(int zoom, MapView maps){
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
			//List<Bombero> lista = (List<Bombero>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "bombero");
            List<Bombero> lista;
        	respuestaBusqueda = (RespuestaServicioWeb) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "bombero");
        	lista = (List<Bombero>)respuestaBusqueda.getLista();
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
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
			     TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
			     switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
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
     * Metodo encargado de mostrar resultados al buscar bomberos por comuna
     * @param zoom
     * @param maps
     */
    private void setupMapBomberoViewComuna(int zoom, MapView maps){
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
            
           
            map.getController().setZoom(14);
            OverlayItem miPocision = new OverlayItem(myLoc.getMyLocation(), "Eu estoy aqui", "cr7");
            @SuppressWarnings("unchecked")
			//List<Bombero> lista = (List<Bombero>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "bombero");
            List<Bombero> lista;
        	Log.d("emergenciAPPS_TAG", "Cargando ubicacion encontrada por comuna");
        	lista = respuestaBusqueda.getLista();
        	if(lista.size() > 0){
            	Float x = ((Bombero)lista.get(0)).getX();
            	Float y = ((Bombero)lista.get(0)).getY();
            	map.getController().animateTo(new GeoPoint(x,y));
                map.getController().setCenter(new GeoPoint(x,y));
        	}
            OverlayItem item;
            overlay.addItem(miPocision);
            for(Bombero b: lista){
            	item = new OverlayItem(new GeoPoint(b.getX(),b.getY()), b.getNombre(), b.getDireccion());
            	overlayServicio.addItem(item);
            }
            TareaLlenaNumeros tarea;
			if(lista.size()>0){
				map.getController().setCenter(new GeoPoint(lista.get(0).getX(),lista.get(0).getY()));
				 map.getController().animateTo(new GeoPoint(lista.get(0).getX(),lista.get(0).getY()));
				overlayServicio.setTapListener(new ItemizedOverlay.OverlayTapListener(){
					
					@Override
					public void onTap(GeoPoint arg0, MapView arg1) {
						int lastTouchedIndex = overlayServicio.getLastFocusedIndex();
						if(lastTouchedIndex > -1){
							OverlayItem tapped =  overlayServicio.getItem(lastTouchedIndex);
							init(tapped);
							annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
			     TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
			     switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
				 
				 
				 }
			}
            
            map.getOverlays().add(overlayServicio);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
            //myLoc.setFollowing(true);
          }
          
        });
    }

    /**
     * Metodo encargado de mostrar mapa con PDI cercanos a un punto obtenido
     * por
     * @param zoom
     * @param maps
     */
    private void setupMapPDIViewCercanos(int zoom, MapView maps){
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
                //List<PDI> lista = (List<PDI>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "pdi");
                List<PDI> lista;
            	respuestaBusqueda = (RespuestaServicioWeb) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "pdi");
            	lista = (List<PDI>)respuestaBusqueda.getLista();
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
								init(tapped);
						        annotation.showAnnotationView(tapped);
							}
							
						}
    					
    				});
    				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
    				tarea.execute();
    			}else{
    				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
    				switch(respuestaBusqueda.getCodigo()){
   				 case ServicioWeb.ERROR_CONEXION: 
   					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
   					 break;
   				 case ServicioWeb.ERROR_JSON:
   					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
   					 break;
   				 case ServicioWeb.ERROR_JSON_GPS:
   					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
   					 break;
   				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
					 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
					 break;
   				 
   				 
   				 }
    			}
                map.getOverlays().add(myLoc);
                map.getOverlays().add(overlay);
                map.getOverlays().add(overlayServicio);
                myLoc.disableMyLocation();
                myLoc.setFollowing(true);
              }
              
            });	
    }
    
    private void setupMapPDIViewComuna(final int zoom, MapView maps){
    	Drawable iconMiPosicion = maps.getContext().getResources().getDrawable(R.drawable.miposicion);
    	Drawable iconPDI = maps.getContext().getResources().getDrawable(R.drawable.marcadorpdi);
    	overlay = new DefaultItemizedOverlay(iconMiPosicion);
    	overlayServicio = new DefaultItemizedOverlay(iconPDI);
    	this.myLoc = new MyLocationExtends(maps.getContext(), maps);
    	map = maps;
    	annotation = new AnnotationView(maps);
    	myLoc.enableMyLocation();
    	
    	myLoc.runOnFirstFix(new Runnable() {
          @Override
          public void run() {
            GeoPoint currentLocation = myLoc.getMyLocation();
            map.getController().animateTo(currentLocation);
            map.getController().setCenter(currentLocation);
            map.getController().setZoom(zoom);
            OverlayItem miPocision = new OverlayItem(myLoc.getMyLocation(), "Eu estoy aqui", "cr7");
            //List<PDI> lista = (List<PDI>) ServicioWeb.postCercanos(currentLocation, radioBusqueda, "pdi");
            List<PDI> lista;
        	Log.d("emergenciAPPS_TAG", "Cargando ubicacion encontrada por comuna");
        	lista = respuestaBusqueda.getLista();
        	if(lista.size() > 0){
            	map.getController().animateTo(lista.get(0).getGeoPoint());
                map.getController().setCenter(lista.get(0).getGeoPoint());
        	}
            OverlayItem item;
            overlay.addItem(miPocision);
            for(PDI p: lista){
            	item = new OverlayItem(p.getGeoPoint(), p.getNombre(), p.getDireccion());
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
							init(tapped);
					        annotation.showAnnotationView(tapped);
						}
						
					}
					
				});
				tarea = new TareaLlenaNumeros(lista, listaTelefonos);
				tarea.execute();
			}else{
				TareaMuestraMensaje mensajeNoExistenResultados = new TareaMuestraMensaje(contexto);
				switch(respuestaBusqueda.getCodigo()){
				 case ServicioWeb.ERROR_CONEXION: 
					 	mensajeNoExistenResultados.execute("No se pudo establecer la conexion con el Servidor");
					 break;
				 case ServicioWeb.ERROR_JSON:
					 mensajeNoExistenResultados.execute("No se encontraron resultados para tu comuna");
					 break;
				 case ServicioWeb.ERROR_JSON_GPS:
					 mensajeNoExistenResultados.execute("No existen resultados en el radio de "+radioBusqueda+" km");
					 break;
				 case ServicioWeb.ERROR_NO_EXISTE_COMUNA: 
				 mensajeNoExistenResultados.execute("No existen resultados para la comuna solicitada");
				 break;
				 
				 
				 }
			}
            map.getOverlays().add(overlayServicio);
            map.getOverlays().add(overlay);
            myLoc.disableMyLocation();
          }
          
        });	
}
    /**
     * Metodo encargado de agregar funcion a boton y mostrar ruta en mapa
     * @param item
     */
    protected void init(final OverlayItem item) {
    	AnnotationView customizedAnnotation;
        // initialize the annotation to be shown later 
    	customizedAnnotation = new AnnotationView(map);
    	// customize the annotation
    	float density = map.getContext().getResources().getDisplayMetrics().density;
    	customizedAnnotation.setBubbleRadius((int)(12*density+0.5f));
    	// make the annotation not animate
    	customizedAnnotation.setAnimated(false);
    	
    	// init our custom innerView from an xml file
    	
        
        
        RelativeLayout customInnerView;
		LayoutInflater li = (LayoutInflater)map.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customInnerView = (RelativeLayout) li.inflate(R.layout.annotation, null);
        TextView customTitle = (TextView) customInnerView.findViewById(R.id.title);
        TextView customSnippet = (TextView) customInnerView.findViewById(R.id.snippet);
        Button customButton = (Button) customInnerView.findViewById(R.id.boton);
        if(!cercanos)
        	customButton.setVisibility(View.INVISIBLE);
        customTitle.setText(item.getTitle());
        customSnippet.setText(item.getSnippet());
        customButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ringProgressDialog = ProgressDialog.show(contexto, "Por favor espere ...", "Cargando ruta óptima ...", true);
				ringProgressDialog.setCancelable(false);
			    routeManager.setMapView(map);
			    String myLat = myLoc.getMyLocation().getLatitude()+"";
			    String myLng = myLoc.getMyLocation().getLongitude()+"";
			    String toLat = item.getPoint().getLatitude()+"";
			    String toLng = item.getPoint().getLongitude()+"";
			    
				routeManager.setRouteCallback(new RouteManager.RouteCallback() {
					
					@Override
					public void onSuccess(RouteResponse arg0) {
						ringProgressDialog.dismiss();
						Toast.makeText(contexto, "Ruta creada exitosamente", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onError(RouteResponse arg0) {
						ringProgressDialog.dismiss();
						Toast.makeText(contexto, "No fue posible crear la ruta", Toast.LENGTH_SHORT).show();
					}
				});
			    routeManager.clearRoute();
			    routeManager.createRoute(myLat+","+myLng, toLat+","+toLng);
				//Toast.makeText(map.getContext(), item.getPoint().getLatitude()+ " "+ item.getPoint().getLongitude(), Toast.LENGTH_LONG).show();
			}
		});
        annotation.setInnerView(customInnerView);
        // now use the customInnerView as the annotation's innerView
        
    }

    public Configuracion getConfiguracion(){
    	Configuracion conf = new Configuracion();
    	conf.setMensajeAlerta(editMensaje.getText().toString());
    	conf.setNumeroBombero(editBombero.getText().toString());
    	conf.setNumeroCarabinero(editCarabinero.getText().toString());
    	conf.setNumeroCentroMedico(editHospital.getText().toString());
    	conf.setRadioBusqueda(progreso_a_guardar);
    	return conf;
    }
}
