package com.example.emergenciapps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.adapters.ItemAdapter;
import com.example.contactos.DetalleContactoActivity;
import com.example.contactos.ListaContactosActivity;
import com.example.login.LoginActivity;
import com.example.object.Configuracion;
import com.example.object.Contacto;
import com.example.object.EmailEmergencia;
import com.example.object.Item;
import com.example.object.Usuario;
import com.example.persistencia.ContactoSQLHelper;
import com.example.seguimiento.MyService;
import com.example.seguimiento.SeguimientoActivity;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;


public class EmergenciAPPSActivity extends Activity implements OnQueryTextListener {
    private DrawerLayout mDrawerLayout;
    private SearchView mSearchView;
    private ListView mDrawerList;
    public RespuestaServicioWeb respuestaBusqueda;
    private ActionBarDrawerToggle mDrawerToggle;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> items;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String numero;
    private String miNombre;
    private String correoContacto;
    private static final int NOTIF_ALERTA_ID = 1;
    
    private ProgressDialog ringProgressDialog;
    
    private String miNumero;
    private Integer posicionActual;
    private SharedPreferences prefs;
    private SharedPreferences prefsSesion;
    private Fragment fragment ;
    private static String TAG = "emergenciAPPS";
    private Menu menu;

    LocationManager locManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		prefs =	getSharedPreferences("miCuenta", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
       
		
		prefsSesion =	getSharedPreferences("sesion", LoginActivity.MODE_PRIVATE);
		if(prefsSesion.getBoolean("firstTime",true)){
			Bundle extras = this.getIntent().getExtras();
			if(extras != null){
				Usuario usuario = (Usuario) extras.getSerializable("usuario");
				editor.putString("miNumero", usuario.getNumeroTelefono());
				editor.putString("miNombre", usuario.getNombre());
				editor.putString("miApellido", usuario.getApellido());
				editor.putString("miCorreo", usuario.getCorreo());
				editor.putInt("estadoAlerta", usuario.getEstadoAlerta());
				
				Configuracion configuracion = usuario.getConfiguracion();
				editor.putString("numero_pdi", configuracion.getNumeroPDI());
				editor.putString("numero_carabinero", configuracion.getNumeroCarabinero());
				editor.putString("numero_centro_medico", configuracion.getNumeroCentroMedico());
				editor.putString("numero_bombero", configuracion.getNumeroBombero());
				editor.putInt("radio_busqueda", configuracion.getRadioBusqueda());
				editor.putString("mensaje_alerta", configuracion.getMensajeAlerta());
				
				
				editor.putLong("fecha_modificacion", configuracion.getFechaModificacion().getTime());
				editor.commit();
				
				List<Contacto> misContactos = usuario.getContactos();
				ContactoSQLHelper oData = new ContactoSQLHelper(this); 
				SQLiteDatabase db = oData.getWritableDatabase(); 
				
				for(Contacto c: misContactos){
					ContentValues registro = new ContentValues();
					registro.put("_id", c.getIdContacto());
					registro.put("numero_telefono", c.getNumeroTelefono());
					registro.put("nombre", c.getNombre());
					registro.put("numero", c.getNumero());
					registro.put("correo", c.getCorreo());
					registro.put("estado", c.getEstado());
					registro.put("alerta_sms", c.getAlertaSMS());
					registro.put("alerta_gps", c.getAlertaGPS());
					registro.put("alerta_correo", c.getAlertaCorreo());
					db.insert("contacto", null, registro);
					
				}
				db.close();
				oData.close();
				
			}
			SharedPreferences.Editor editorSesion = prefsSesion.edit();
			editorSesion.putBoolean("firsTime", false);
			editorSesion.commit();
			miNombre = prefs.getString("miNombre", "vacio");
		}
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        this.generaItems();
        itemAdapter = new ItemAdapter(this, R.layout.drawer_list_item, items);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(itemAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
        		selectItem(0);
            
        }
        
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.buscar);
        MenuItem botonItem = menu.findItem(R.id.guardarConf);
        switch(posicionActual){
        // INICIO
        case 0:
        	searchItem.setVisible(false);
        	botonItem.setVisible(false);
        	break;
        // CENTRO MEDICO
        case 1:
        	botonItem.setVisible(false);
        	break;
        // BOMBEROS
        case 2:
        	botonItem.setVisible(false);
        	break;
        case 3:
        // CARABINEROS
        	botonItem.setVisible(false);
        	break;
        // PDI
        case 4:
        	botonItem.setVisible(false);
        	break;
        
        case 5:
        	break;
        	
        // CONFIGURACION	
        case 6:searchItem.setVisible(false);
        	break;
        case 7:
        	botonItem.setVisible(false);
        	searchItem.setVisible(false);
        	break;
        }
       
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Search...");
        mSearchView.setOnQueryTextListener(this);
        
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
    	switch (item.getItemId()) {
	        case R.id.guardarConf:
	            Log.i("ActionBar", "Guardardando!");
	            final String miNumero =  prefs.getString("miNumero", "no encontrado");
	            final Configuracion conf = ((ServiceFragment)fragment).getConfiguracion();
	            /*
	             * Obtengo configuracion guardada en las preferencias para verificar si la nueva configuracion
	             * contiene cambios, si no es así no se realiza una actualización con el servicio web
	             */
	            Configuracion confLocal = new Configuracion();
	            confLocal.setMensajeAlerta(prefs.getString("mensaje_alerta", "mensaje_confLocal"));
	            confLocal.setNumeroBombero(prefs.getString("numero_bombero", "bombero_confLocal"));
	            confLocal.setNumeroCarabinero(prefs.getString("numero_carabinero", "carabinero_confLocal"));
	            confLocal.setNumeroCentroMedico(prefs.getString("numero_centro_medico", "centro_medico_confLocal"));
	            confLocal.setRadioBusqueda(prefs.getInt("radio_busqueda", -1));
	            
	            AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
	            	
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						ringProgressDialog = ProgressDialog.show(EmergenciAPPSActivity.this, "Por favor espere ...", "Guardando información ...", true);
						ringProgressDialog.setCancelable(false);
					}

					@Override
					protected String doInBackground(String... arg0) {
						boolean resultado = ServicioWeb.actualizaConfiguracion(conf, miNumero);
						String respuesta;
						if(resultado){
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("numero_carabinero", conf.getNumeroCarabinero());
							editor.putString("numero_centro_medico", conf.getNumeroCentroMedico());
							editor.putString("numero_bombero", conf.getNumeroBombero());
							editor.putInt("radio_busqueda", conf.getRadioBusqueda());
							editor.putString("mensaje_alerta", conf.getMensajeAlerta());
							editor.commit();
							return "Configuración Actualizada correctamente";
						}else{
							return "No fue posible actualizar la configuracion";
						}
					}

					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						ringProgressDialog.dismiss();
						Toast.makeText(EmergenciAPPSActivity.this, result, Toast.LENGTH_LONG).show();
					}
					
					
	            	
	            };
	            /*
	             * Verifica si ambas configuraciones son iguales 
	             */
	            if(conf.compareTo(confLocal) != 0){
	            	/*
	            	 * Si las configuraciones no son iguales ejecuta tarea para guardar
	            	 * en servidor
	            	 */
	            	if(Utils.isOnline(this)){
	            		tarea.execute();
	            	}else{
	            		Toast.makeText(this, "No podemos realizar tu solicitud, ya que no posees conexión a internet", Toast.LENGTH_SHORT).show();
	            	}
	            	
	            }else{
	            	Toast.makeText(EmergenciAPPSActivity.this, "No existen cambios a guardar", Toast.LENGTH_SHORT).show();
	            }
	            
	            return true;
	        default:
	            return mDrawerToggle.onOptionsItemSelected(item);
	    }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	
        		selectItem(position);
        	
        }
    }
    
    private void generaItems(){
    	items = new ArrayList<Item>();
    	items.add(new Item("Bienvenido "+miNombre, R.drawable.home,""));
    	items.add(new Item("Centros Médicos", R.drawable.hospital,"centro_medico"));
    	items.add(new Item("Bomberos", R.drawable.bombero,"bombero"));
    	items.add(new Item("Carabineros", R.drawable.carabinero,"carabinero"));
    	items.add(new Item("PDI", R.drawable.pdi,"pdi"));
    	items.add(new Item("Seguimiento", R.drawable.ic_action_device_access_location_found,""));
    	items.add(new Item("Configurar", R.drawable.ic_action,""));
    	items.add(new Item("Cerrar Sesión", R.drawable.exit,""));
    	//items.add(new Item("Ayuda", R.drawable.help,""));
    }
    private void selectItem(int position, RespuestaServicioWeb lista) {
    	posicionActual = position;
        // update the main content by replacing fragments
        fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putSerializable("respuesta", (Serializable) lista);
        args.putInt(ServiceFragment.SERVICE_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(items.get(position).getTitulo());
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    private void selectItem(int position){
    	if(position == 5){
    		Intent intent = new Intent(EmergenciAPPSActivity.this, SeguimientoActivity.class); 
			startActivity(intent); 
    	}else{
    		selectItem(position, null);
    	}
    	
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onQueryTextChange(String newText) {
    	//metodo el cual se ejecuta cada vez que se teclea en la busqueda
    	//TODO realizar autocompletar para comunas
        //Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();

        return false;
    }
   @Override
    public boolean onQueryTextSubmit(String text) {
	   // metodo el cual se ejecuta al momento de realizar la busqueda
	   
	   AsyncTask<String, Void, String> busca = new AsyncTask<String, Void, String>() {
		   @Override
			protected void onPreExecute() {
				super.onPreExecute();
				ringProgressDialog = ProgressDialog.show(EmergenciAPPSActivity.this, "Por favor espere ...", "Buscando comuna ...", true);
				ringProgressDialog.setCancelable(false);
			}
			@Override
			protected String doInBackground(String... params) {
				respuestaBusqueda = ServicioWeb.buscaPorComuna(params[0], items.get(posicionActual).getNombreTabla());
				return "";
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				selectItem(posicionActual, respuestaBusqueda);
				ringProgressDialog.dismiss();
				
			}
		};
		if(Utils.isOnline(this)){
			busca.execute(text);
		}else{
			Toast.makeText(this,"No podemos realizar la solicitud ya que no posees conexión a internet", Toast.LENGTH_SHORT).show();
		}
		
	   
       

        return true;
    }
    
    public static void llamada(Activity activity, String numero){ 
		 Intent intent = new Intent(Intent.ACTION_CALL); 
		 intent.setData(Uri.parse(numero)); 
		 activity.startActivity(intent); 
	}
    
    
    public void llamar(View v){
    	SharedPreferences prefs =
				getSharedPreferences("MisContactos", this.MODE_PRIVATE);
    	
    	String llamarA;
    	
    	int id = v.getId();
    	if(id == R.id.btnCarabinero){
    		Log.d("llamada", "llamando a carabinero");
    		String numeroCarabinero = prefs.getString("numeroCarabinero", "86575038");
    		Log.d("numero",numeroCarabinero);
    		llamarA = "tel:"+numeroCarabinero;
    		Intent intent = new Intent(Intent.ACTION_CALL); 
   		 	intent.setData(Uri.parse(llamarA)); 
   		    this.startActivity(intent); 
   		}else{
    		if(id == R.id.btnBombero){
    			Log.d("llamada", "llamando a bombero");
    			String numeroBombero = prefs.getString("numeroBombero", "86575038");
        		llamarA = "tel:"+numeroBombero;
        		Intent intent = new Intent(Intent.ACTION_CALL);
        		intent.setData(Uri.parse(llamarA)); 
       		    this.startActivity(intent); 
        	}else{
        		if(id == R.id.btnHospital){
	        		Log.d("llamada", "llamando a hospital");
	        		String numeroHospital = prefs.getString("numeroHospital", "82998988");
	        		llamarA = "tel:"+numeroHospital;
	        		Intent intent = new Intent(Intent.ACTION_CALL);
	        		intent.setData(Uri.parse(llamarA)); 
	       		    this.startActivity(intent); 
        		}
        	}
    	}
    	
    }
    
    public void enviarAlerta(final View v){
    	/*
    	 * TODO El envio de alerta ha cambiado,
    	 * ahora la notificacion va al servidor y el servidor la procesa para ver
    	 * que hacer
    	 */
    	Log.d(TAG, "Inicia evento enviarAlerta");
    	SharedPreferences prefs = getSharedPreferences("miCuenta", this.MODE_PRIVATE);
	   	final String miNumero = prefs.getString("miNumero", "Sin numero");
	   	final SharedPreferences.Editor editor = prefs.edit();
	   	Integer estadoAlerta = prefs.getInt("estadoAlerta", 0);
	   	if(estadoAlerta == 0){
	   		editor.putInt("estadoAlerta", 1);
	   		editor.commit();
	   		Log.d("EmergenciAPPS","INICIO ENVIO ALERTA");
	    	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	    	builder.create();
	    	builder.setTitle("Estado Alerta");
	    	builder.setMessage("¿ Realmente desea enviar una Alerta ?");
	    	builder.setPositiveButton("SI",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	ArrayList<Contacto> lista = Utils.getContactosSMS(EmergenciAPPSActivity.this);
    	            	SharedPreferences preferencias = getSharedPreferences("miCuenta", EmergenciAPPSActivity.MODE_PRIVATE);
    	            	String mensajeAlerta = preferencias.getString("mensaje_alerta", "");
    	            	Log.d("mensaje_alerta",mensajeAlerta);
    	            	if(!ServiceFragment.verificaConexion(EmergenciAPPSActivity.this)){
    	            		Toast.makeText(EmergenciAPPSActivity.this, "No tiene activado el Internet, por lo tanto solo se enviarán SMS", Toast.LENGTH_LONG).show();
    	            		for(Contacto c : lista){
        	            		sendSMSMessage(mensajeAlerta, c.getNumero().toString());
        	            		Log.d("numero_contacto",c.getNumero());
        	            	}
    	            	}else{
    	            		for(Contacto c : lista){
        	            		sendSMSMessage(mensajeAlerta, c.getNumero().toString());
        	            		Log.d("numero_contacto",c.getNumero());
        	            	}
    	            		
    	            		locManager = (LocationManager)getSystemService(v.getContext().LOCATION_SERVICE);
        	            	
        	            	LocationListener locListener = new LocationListenerMensaje( locManager,v, miNumero);
        	            	
        	        	    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locListener);
    	            	}
    	            	
    	            	
    	            	
    	        	    
    	            	
    	            }
    	        });
    	
	    	builder.setNegativeButton("NO",
	    	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.cancel();
	            }
	    	});
    	builder.show();
	   	}else{
	   		final NotificationManager mNotificationManager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
	   	 AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					String toast;
					String respuesta = ServicioWeb.actualizaPosicion("0", "0", miNumero);
					respuesta = respuesta.trim();
					if(respuesta.equals("true")){
						Log.d("EmergenciAPPS","TERMINA ENVIO ALERTA");
				   		
				   		toast = "La alerta se ha detenido";
				   		mNotificationManager.cancel(NOTIF_ALERTA_ID);
				   		stopService(new Intent(EmergenciAPPSActivity.this, MyService.class));
				   		editor.putInt("estadoAlerta", 0);
				   		editor.commit();
					}else{
						toast =  "No es posible cancelar la alerta activa. Intenta más tarde";
						
					}
					Log.d("actualizacion_ubicacion",respuesta);
					return toast;
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					Toast.makeText(EmergenciAPPSActivity.this, result,Toast.LENGTH_SHORT).show();
				}
				
	   	 }
	        ;
	        tarea.execute();
	   	}
    	 
    	 
    	 
    	 
    	 
    	 
 
     }
    
    protected void sendSMSMessage(String texto, String numero) {
	      Log.i("Send SMS", "");
	      try {
	         SmsManager smsManager = SmsManager.getDefault();
	         smsManager.sendTextMessage(numero, null, texto, null, null);
	        
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
    
    private List<String> mostrarPosicion(Location loc){
    	if(loc != null){
    		List<String> posicion = new ArrayList<String>();
    		posicion.add(String.valueOf(loc.getLatitude()));
    		posicion.add(String.valueOf(loc.getAltitude()));
    		return posicion;
    	}
    	return null;
    }
    
   
    
    
    public void actualizaPantallaInicio(){
        final TextView numeroCarabinero = (TextView) findViewById(R.id.textView2);
        final TextView numeroBombero = (TextView) findViewById(R.id.textView3);
        final TextView numeroHospital = (TextView) findViewById(R.id.textView4);
        
        SharedPreferences prefs =
				getSharedPreferences("miCuenta", this.MODE_PRIVATE);
        
    	String carabinero = prefs.getString("numero_carabinero", "133");
    	numeroCarabinero.setText(carabinero);
    	
    	String bombero = prefs.getString("numero_bombero", "132");
    	numeroBombero.setText(bombero);
    	
    	String hospital = prefs.getString("numero_hospital", "131");
    	numeroHospital.setText(hospital);
    }

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(EmergenciAPPSActivity.this);
    	builder.create();
    	builder.setTitle("Salir");
    	builder.setMessage("¿ Cerrar EmergenciAPPS ?");
    	builder.setPositiveButton("SI",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	EmergenciAPPSActivity.this.finish();
    	            	
    	            }
    	        });
    	
    	builder.setNegativeButton("NO",
    	        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    	builder.show();
	}


    

    
}
