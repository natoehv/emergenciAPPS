package com.example.login;

import java.io.Serializable;

import com.example.emergenciapps.EmergenciAPPSActivity;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Usuario;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity  extends Activity{
	private Button iniciarSesion;
	private EditText password;
	private EditText usuario;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	private ProgressDialog ringProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
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
	
}
