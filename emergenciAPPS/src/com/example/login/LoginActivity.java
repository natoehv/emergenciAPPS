package com.example.login;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.emergenciapps.EmergenciAPPSActivity;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity  extends Activity{
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    private static final String TAG = "emergenciAPPS_GCM";
    
    String SENDER_ID = "473045304241";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    
	private Button iniciarSesion;
	private EditText password;
	private EditText usuario;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private GoogleCloudMessaging gcm;
	private Context context;
    private String regid;
	
	
	private ProgressDialog ringProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		context = getApplicationContext();
		prefs =		getSharedPreferences("sesion", this.MODE_PRIVATE);
        editor = prefs.edit();
        Boolean inicio = prefs.getBoolean("login", false);
        if(inicio){
        	Intent i = new Intent(LoginActivity.this, EmergenciAPPSActivity.class);
	        startActivity(i);
	        finish();
        }
		setContentView(R.layout.activity_login);
		iniciarSesion = (Button) this.findViewById(R.id.login);
		usuario = (EditText) this.findViewById(R.id.usuario);
		password = (EditText) this.findViewById(R.id.password);
		
		iniciarSesion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AsyncTask<String, Void, String> inicioSesion = new AsyncTask<String, Void,String >() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Por favor espere ...", "Verificando usuario ...", true);
						ringProgressDialog.setCancelable(false);
					}
					
					@Override
					protected String doInBackground(String... params) {
						if(validarCampos()){
							Usuario user;
							user = ServicioWeb.verificaLogin(usuario.getText().toString(), password.getText().toString());
							if(user == null){
								//no existe usuario
								Log.d("emergenciAPPS", "no se encuentra a usuario");
								return "error";
							}else{
								editor.putBoolean("login", true);
								editor.putBoolean("firstTime", true);
								editor.commit();
								registraGCM(usuario.getText().toString());
								Intent i = new Intent(LoginActivity.this, EmergenciAPPSActivity.class);
						         i.putExtra("usuario",(Serializable) user);
						        startActivity(i);
						        finish();
							}
						}
						return "";
					}

					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						ringProgressDialog.dismiss();
						if(result.equals("error"))
							Toast.makeText(usuario.getContext(), "Telefono y/o contraseña incorrecta", Toast.LENGTH_LONG).show();
						
					}
					
					
				};
				inicioSesion.execute("");
				
				
				
			}
		});
	}
	
	public boolean validarCampos(){
		if(usuario.getText().toString().equals("")){
			Toast.makeText(this, "El campo usuario no puede estar vacio", Toast.LENGTH_LONG).show();
			return false;
		}
		if(password.getText().toString().equals("")){
			Toast.makeText(this, "El campo password no puede estar vacio", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS)
	    {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
	        {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        }
	        else
	        {
	            Log.i(TAG, "Dispositivo no soportado.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	public void registraGCM(String numero){
//		if(checkPlayServices())
	        //{
	                gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
	 
	                //Obtenemos el Registration ID guardado
	                regid = getRegistrationId(context);
	 
	                //Si no disponemos de Registration ID comenzamos el registro
	                if (regid.equals("")) {
	                	String msg = "";
	                	 
	                    try
	                    {
	                        if (gcm == null)
	                        {
	                            gcm = GoogleCloudMessaging.getInstance(context);
	                        }
	         
	                        //Nos registramos en los servidores de GCM
	                        regid = gcm.register(SENDER_ID);
	         
	                        Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
	         
	                        //Nos registramos en nuestro servidor
	                        boolean registrado = ServicioWeb.registraGCMEnServidor(usuario.getText().toString(), regid);
	         
	                        //Guardamos los datos del registro
	                        if(registrado)
	                        {
	                            setRegistrationId(context, usuario.getText().toString(), regid);
	                        }
	                    }
	                    catch (IOException ex)
	                    {
	                        Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
	                    }
	                }
	        //}
	        //else
	        //{
	            //    Log.i(TAG, "No se ha encontrado Google Play Services.");
	            //}
	}
	
	private String getRegistrationId(Context context) 
	{
	    SharedPreferences prefs = getSharedPreferences(
	    		LoginActivity.class.getSimpleName(), 
	            Context.MODE_PRIVATE);
	    
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    
	    if (registrationId.length() == 0) 
	    {
	        Log.d(TAG, "Registro GCM no encontrado.");
	        return "";
	    }
	    
	    String registeredUser = 
	    		prefs.getString(PROPERTY_USER, "user");
	    
	    int registeredVersion = 
	    		prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    
	    long expirationTime =
	            prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
	    String expirationDate = sdf.format(new Date(expirationTime));
	    
	    Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser + 
	    		", version=" + registeredVersion + 
	    		", expira=" + expirationDate + ")");
	    
	    int currentVersion = getAppVersion(context);
	    
	    if (registeredVersion != currentVersion) 
	    {
	        Log.d(TAG, "Nueva versión de la aplicación.");
	        return "";
	    }
	    else if (System.currentTimeMillis() > expirationTime)
	    {
	    	Log.d(TAG, "Registro GCM expirado.");
	        return "";
	    }
	    else if (usuario.getText().toString().equals(registeredUser))
	    {
	    	Log.d(TAG, "Nuevo nombre de usuario.");
	        return "";
	    }
	    
	    return registrationId;
	}
	
	private static int getAppVersion(Context context) 
	{
	    try
	    {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        
	        return packageInfo.versionCode;
	    } 
	    catch (NameNotFoundException e) 
	    {
	        throw new RuntimeException("Error al obtener versión: " + e);
	    }
	}
	
	private void setRegistrationId(Context context, String user, String regId) 
	{
	    SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(), 
	            Context.MODE_PRIVATE);    
	    int appVersion = getAppVersion(context);
	    
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_USER, user);
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.putLong(PROPERTY_EXPIRATION_TIME,System.currentTimeMillis() + EXPIRATION_TIME_MS);
	    
	    editor.commit();
	}
}
