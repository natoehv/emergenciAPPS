package com.example.contactos;

import java.util.ArrayList;

import com.example.adapters.ListaAdapterContacto;
import com.example.emergenciapps.EmergenciAPPSActivity;
import com.example.emergenciapps.R;
import com.example.emergenciapps.ServicioWeb;
import com.example.emergenciapps.Utils;
import com.example.object.Contacto;
import com.example.persistencia.ContactoSQLHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleContactoActivity extends Activity{
	private int id_contacto = -1;
	private TextView nombre;
	private TextView numero;
	private TextView correo;
	private Contacto contacto;
	private String numero_telefono;
	private int estado;
	private Switch gps;
	private Switch sms;
	private Switch email;
	private  boolean ingresar = false;
	private  boolean actualizar = false;
	private ProgressDialog ringProgressDialog;
	private boolean ok_nombre =  false;
    private boolean ok_numero = false;
    private boolean ok_correo = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_contacto);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		nombre = (TextView) this.findViewById(R.id.nombreContacto);
		numero = (TextView) this.findViewById(R.id.numeroContacto);
		correo = (TextView) this.findViewById(R.id.correoContacto);
		gps = (Switch) this.findViewById(R.id.switchGPS);
		sms = (Switch) this.findViewById(R.id.switchSMS);
		email = (Switch) this.findViewById(R.id.switchEmail);
		
		SharedPreferences prefs =	getSharedPreferences("miCuenta", this.MODE_PRIVATE);
		numero_telefono = ""+prefs.getString("miNumero", "");
		Log.d("miNumero", ""+prefs.getString("miNumero", ""));
		Bundle extras = this.getIntent().getExtras();
		
		
		
		if(extras != null){
			actualizar = true;
			contacto = (Contacto) extras.getSerializable("contacto");
			id_contacto = contacto.getIdContacto();
			estado = contacto.getEstado();
			nombre.setText(contacto.getNombre());
			numero.setText(contacto.getNumero());
			correo.setText(contacto.getCorreo());
			
			if(contacto.getAlertaGPS() == 1)
				gps.setChecked(true);
			else
				gps.setChecked(false);
			
			if(contacto.getAlertaSMS() == 1)
				sms.setChecked(true);
			else
				sms.setChecked(false);
			
			if(contacto.getAlertaCorreo() == 1)
				email.setChecked(true);
			else
				email.setChecked(false);
		}else{
			ingresar = true;
			email.setChecked(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contacto, menu);
		MenuItem deleteItem = menu.findItem(R.id.eliminarContacto);
		
		if(ingresar){
			 deleteItem.setVisible(false);
			 
			 
		}else{
			if(actualizar){
				deleteItem.setVisible(true);
			}
		}
        
        
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.guardarConf:
        		final Contacto contacto = new Contacto();
        		contacto.setNumeroTelefono(numero_telefono);
        		contacto.setIdContacto(id_contacto);
        		if(!nombre.getText().toString().equals("")){
        			contacto.setNombre(nombre.getText().toString());
        			ok_nombre = true;
        		}else{
        			ok_nombre = false;
        		}
        		
        		if(!numero.getText().toString().equals("")){
        			contacto.setNumero(numero.getText().toString());
        			ok_numero = true;
        		}else{
        			ok_numero = false;
        		}
        		
        		if(!correo.getText().toString().equals("")){
        			contacto.setCorreo(correo.getText().toString());
        			ok_correo = true;
        		}else{
        			ok_correo = false;
        		}
        			
        		
        		if(gps.isChecked())
        			contacto.setAlertaGPS(1);
        		else
        			contacto.setAlertaGPS(0);
        		
        		if(email.isChecked())
        			contacto.setAlertaCorreo(1);
        		else
        			contacto.setAlertaCorreo(0);
        		
        		if(sms.isChecked())
        			contacto.setAlertaSMS(1);
        		else
        			contacto.setAlertaSMS(0);
        		
        		if(ok_nombre == true && ok_numero == true && ok_correo == true){
        			
	        		AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
		            	
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							ringProgressDialog = ProgressDialog.show(DetalleContactoActivity.this, "Por favor espere ...", "Guardando información ...", true);
							ringProgressDialog.setCancelable(false);
							
						}
	
						@Override
						protected String doInBackground(String... arg0) {
							String respuesta;
							if(id_contacto == -1){
								Log.d("accion", "Ingresar");
								boolean resultadoIngreso = ServicioWeb.ingresaContacto(contacto);
								if(resultadoIngreso){
									Utils.insertContacto(contacto, DetalleContactoActivity.this);
									return "Contacto creado correctamente";
								}else{
									return "No fue posible crear el Contacto";
								}
							}else{
								Log.d("accion", "Actualizar");
								Log.d("id_contacto", ""+id_contacto);
								boolean resultadoActualizacion = ServicioWeb.actualizaContacto(contacto);
								if(resultadoActualizacion){
									Utils.updateContacto(contacto, DetalleContactoActivity.this);
									return "Contacto actualizado correctamente";
								}else{
									return "No fue posible actualizar el Contacto";
								}
							}
							
						}
	
						@Override
						protected void onPostExecute(String result) {
							super.onPostExecute(result);
							ringProgressDialog.dismiss();
							Toast.makeText(DetalleContactoActivity.this, result, Toast.LENGTH_LONG).show();
							
							Intent i = new Intent(DetalleContactoActivity.this, ListaContactosActivity.class); 
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i); 
							finish();
						}
						
						
		            	
		            };
	        		tarea.execute();
	        		
	        		
        		}else{
        			Toast.makeText(DetalleContactoActivity.this, "Complete todos los campos antes de guardar", Toast.LENGTH_LONG).show();
        		}
        	break;
        case R.id.eliminarContacto:
        	AlertDialog.Builder builder = new AlertDialog.Builder(DetalleContactoActivity.this);
        	builder.create();
        	builder.setTitle("Eliminar");
        	builder.setMessage("¿ Eliminar a "+nombre.getText().toString()+ " ?");
        	builder.setPositiveButton("SI",
        	        new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int which) {
        	            	Log.d("id_contacto_eliminar",""+id_contacto);
        	            	AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
        	            		
        	            		@Override
        						protected void onPreExecute() {
        							super.onPreExecute();
        							ringProgressDialog = ProgressDialog.show(DetalleContactoActivity.this, "Por favor espere ...", "Eliminando ...", true);
        							ringProgressDialog.setCancelable(false);
        							
        						}
        	            		
								@Override
								protected String doInBackground(String... arg0) {
									boolean resultadoEliminacion = ServicioWeb.eliminaContacto(id_contacto);
									if(resultadoEliminacion){
										Utils.deleteContacto(id_contacto, DetalleContactoActivity.this);
										return "Contacto eliminado exitosamente";
									}else{
										return "No fue posible eliminar el contacto";
									}
									
									
								}
								
								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									ringProgressDialog.dismiss();
									Toast.makeText(DetalleContactoActivity.this, result, Toast.LENGTH_LONG).show();
									
									Intent i = new Intent(DetalleContactoActivity.this, ListaContactosActivity.class); 
									i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(i); 
									finish();;
								}
        	            		
        	            	};
        	            	tarea.execute();
        	            }
        	        });
        	
        	builder.setNegativeButton("NO",
        	        new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        	builder.show();
        	
        	break;
        case android.R.id.home:
        	finish();
        	
        			
        	break;
		}
		return true;
	}
	
	

}
