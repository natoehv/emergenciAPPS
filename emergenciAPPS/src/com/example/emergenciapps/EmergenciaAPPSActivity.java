package com.example.emergenciapps;

import java.util.ArrayList;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class EmergenciaAPPSActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> items;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences prefs =
				getSharedPreferences("MisContactos", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString("numeroCarabinero", "133");
		editor.putString("numeroBombero", "132");
		editor.putString("numeroHospital", "131");
		editor.commit();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_websearch).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
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
    	items.add(new Item("Inicio", R.drawable.home));
    	items.add(new Item("Ambulancia", R.drawable.hospital));
    	items.add(new Item("Bomberos", R.drawable.bombero));
    	items.add(new Item("Carabineros", R.drawable.carabinero));
    	items.add(new Item("Configurar", R.drawable.configuracion));
    }
    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putInt(ServiceFragment.SERVICE_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(items.get(position).getTitulo());
        mDrawerLayout.closeDrawer(mDrawerList);
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