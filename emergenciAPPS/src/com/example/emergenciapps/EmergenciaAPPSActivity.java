package com.example.emergenciapps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.object.Configuracion;
import com.example.object.Contacto;
import com.example.object.EmailEmergencia;
import com.example.object.Item;
import com.example.object.Usuario;
import com.example.persistencia.ContactoSQLHelper;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ContentValues;
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


public class EmergenciaAPPSActivity extends Activity implements OnQueryTextListener {
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
    private String correoContacto;
    
    private String miNumero;
    private Integer posicionActual;
    private SharedPreferences prefs;
    private SharedPreferences prefsSesion;
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
        
        //correoContacto = prefs.getString("correoContacto", "");
        //String numeroCarabinero = prefs.getString("numeroCarabinero", "133");
        //String numeroBombero = prefs.getString("numeroBombero", "132");
        //String numeroHospital = prefs.getString("numeroHospital", "131");
        
		//editor.putString("numeroCarabinero", numeroCarabinero);
		//editor.putString("numeroBombero", numeroBombero);
		//editor.putString("numeroHospital", numeroHospital);
		//editor.commit();
		
		prefsSesion =	getSharedPreferences("sesion", this.MODE_PRIVATE);
		if(prefsSesion.getBoolean("firstTime",false)){
			Bundle extras = this.getIntent().getExtras();
			if(extras != null){
				Usuario usuario = (Usuario) extras.getSerializable("user");
				editor.putString("miNumero", usuario.getNumeroTelefono());
				editor.putString("miNombre", usuario.getNombre());
				editor.putString("miApellido", usuario.getApellido());
				editor.putString("miCorreo", usuario.getCorreo());
				
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
					registro.put("id_contacto", c.getIdContacto());
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
			SharedPreferences.Editor editorSesion = prefs.edit();
			editorSesion.putBoolean("firsTime", false);
			editorSesion.commit();
			
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
        	//Se oculta buscar para inicio
        	posicionActual = 0;
        	if(correoContacto.equals("")){
        		//selectItem(5);
        		selectItem(0);
        	}else{
        		//selectItem(0);
        		selectItem(0);
        	}
            
        }
        
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.buscar);
       
        if(posicionActual == 0 || posicionActual == 5 || posicionActual == 6){
        	searchItem.setVisible(false);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        return false;
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
    	items.add(new Item("Inicio", R.drawable.home,""));
    	items.add(new Item("Centros M�dicos", R.drawable.hospital,"centro_medico"));
    	items.add(new Item("Bomberos", R.drawable.bombero,"bombero"));
    	items.add(new Item("Carabineros", R.drawable.carabinero,"carabinero"));
    	items.add(new Item("PDI", R.drawable.pdi,"pdi"));
    	//items.add(new Item("Configurar", R.drawable.configuracion,""));
    	//items.add(new Item("Ayuda", R.drawable.help,""));
    }
    private void selectItem(int position, RespuestaServicioWeb lista) {
    	posicionActual = position;
        // update the main content by replacing fragments
        Fragment fragment = new ServiceFragment();
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
    	selectItem(position, null);
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
		protected String doInBackground(String... params) {
			respuestaBusqueda = ServicioWeb.buscaPorComuna(params[0], items.get(posicionActual).getNombreTabla());
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			selectItem(posicionActual, respuestaBusqueda);
			super.onPostExecute(result);
		}
	};
	busca.execute(text);
	   
       

        return false;
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
    
    public void enviarAlerta(View v){
    	Log.d(TAG, "Inicia eventeo enviarAlerta");
    	 SharedPreferences prefs = getSharedPreferences("MisContactos", this.MODE_PRIVATE);
    	 String correo = prefs.getString("correoContacto", "");
    	 String msje = prefs.getString("mensaje", "Help!");
    	 String miNombre = prefs.getString("miNombre", "Unknow");
    	 String miNumero = prefs.getString("miNumero", "Sin numero");
    	 String lat;
    	 String lng;
    	 
    	 locManager = (LocationManager)getSystemService(this.LOCATION_SERVICE);
    	 EmailEmergencia email = new EmailEmergencia();
    	 email.setContext(getApplicationContext());
    	 email.setCorreo(correo);
    	 email.setMensaje(msje);
    	 email.setMiNombre(miNombre);
    	 email.setMiNumero(miNumero);
    	 Button button = (Button) v.findViewById(R.id.btnEmergencia);
    	 
    	 button.setBackgroundResource(R.drawable.ayuda_pulsado);
    	 LocationListener locListener = new LocationListenerMensaje( locManager, email,v);
    	    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
    	    Toast.makeText(v.getContext(),
	                  "Enviando Alerta!", Toast.LENGTH_SHORT).show();
 
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
    	//setContentView(R.layout.fragment_inicio);
        final TextView numeroCarabinero = (TextView) findViewById(R.id.textView2);
        final TextView numeroBombero = (TextView) findViewById(R.id.textView3);
        final TextView numeroHospital = (TextView) findViewById(R.id.textView4);
        
        SharedPreferences prefs =
				getSharedPreferences("MisContactos", this.MODE_PRIVATE);
        
    	String carabinero = prefs.getString("numeroCarabinero", "82806080");
    	numeroCarabinero.setText(carabinero);
    	
    	String bombero = prefs.getString("numeroBombero", "86575038");
    	numeroBombero.setText(bombero);
    	
    	String hospital = prefs.getString("numeroHospital", "82998988");
    	numeroHospital.setText(hospital);
    }


    
}
