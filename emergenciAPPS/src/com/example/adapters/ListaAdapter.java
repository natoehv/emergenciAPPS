package com.example.adapters;

import java.util.ArrayList;

import com.example.emergenciapps.EmergenciAPPSActivity;
import com.example.emergenciapps.LocationListenerMensaje;
import com.example.emergenciapps.R;
import com.example.emergenciapps.Utils;
import com.example.emergenciapps.R.id;
import com.example.emergenciapps.R.layout;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Bombero;
import com.example.object.Carabinero;
import com.example.object.Contacto;
import com.example.object.Hospital;
import com.example.object.PDI;
import com.example.seguimiento.SeguimientoActivity;

import android.R.color;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaAdapter  extends ArrayAdapter{
	private ArrayList objects;
	private Context context;
	private ProgressDialog ringProgressDialog;
	private boolean press = false;
	TextView text_numero;
	
	public ListaAdapter(Context context, int textViewResourceId, ArrayList values) {
		super(context, textViewResourceId, values);
		this.objects = values;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final SharedPreferences pref = context.getSharedPreferences("miCuenta", context.MODE_PRIVATE);
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final String numeroUsuario = pref.getString("miNumero", "");
		v = inflater.inflate(R.layout.lista_telefonos, null);
		final Vibrator vibracion = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		 if(objects.get(position) instanceof Carabinero){
			 final Carabinero carabinero = (Carabinero) objects.get(position);
					/*
					 * fila de usuario
					 */
			 		
					final TextView numero = (TextView) v.findViewById(R.id.numero);
					TextView direccion = (TextView) v.findViewById(R.id.direccion);
					Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
					TextView nombre = (TextView) v.findViewById(R.id.nombre);
					
					llamar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							builder.create();
					    	builder.setTitle("Llamada de Retén de Carabineros");
					    	builder.setMessage("¿ Realmente desea llamar a "+carabinero.getNombre()+ " ?");
					    	builder.setPositiveButton("Llamar",
					    	        new DialogInterface.OnClickListener() {
					    	            public void onClick(DialogInterface dialog, int which) {
					    	            	Log.d("emergencia", carabinero.getTelefono());
											Intent intent = new Intent(Intent.ACTION_CALL);
											String llamarA = "tel:"+carabinero.getTelefono();
							        		intent.setData(Uri.parse(llamarA)); 
							       		    context.startActivity(intent); 
					    	            	
					    	            }
					    	        });
					    	
						    	builder.setNegativeButton("Cancelar",
						    	        new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int which) {
						                dialog.cancel();
						            }
						    	});
						    	builder.show();
							
						}
					});
					final Handler handler = new Handler();
					final Runnable runnable = new Runnable(){
					

						@Override
						public void run() {
							// TODO Auto-generated method stub
							vibracion.vibrate(500);
							
							AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
								@Override
								protected void onPreExecute() {
									super.onPreExecute();
									ringProgressDialog = ProgressDialog.show(context, "Por favor espere ...", "Actualizando ...", true);
									ringProgressDialog.setCancelable(false);
									
								}
								@Override
								protected String doInBackground(String... arg0) {
									// TODO Auto-generated method stub
									Boolean resultado = ServicioWeb.actualizaNumeroCarabinero(numeroUsuario,numero.getText().toString());
									if(resultado){
										return "true";
									}else{
										return "false";
									}
									
								}
								@Override
								protected void onPostExecute(String result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);
									ringProgressDialog.dismiss();
									if(result.equals("true")){
										text_numero.setTextColor(Color.WHITE);
										numero.setTextColor(Color.GREEN);
										SharedPreferences.Editor editor = pref.edit();
										editor.putString("numero_carabinero", numero.getText().toString());
										editor.commit();
										Toast.makeText(context, "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
									}
								}
								
							};
							tarea.execute();
							
							
						}};
						numero.setOnTouchListener(new OnTouchListener(){

							

							@Override
							public boolean onTouch(View arg0, MotionEvent arg1) {
								// TODO Auto-generated method stub
								final TextView tv  = (TextView) arg0.findViewById(R.id.numero);
								text_numero =  tv;
								if(arg1.getAction() ==  MotionEvent.ACTION_DOWN){
									handler.postDelayed(runnable, 1500);
									
									press = true;
								}else{
									if(arg1.getAction() ==  MotionEvent.ACTION_UP){
										
										if(press){
											press = false;
											handler.removeCallbacks(runnable);
										}
									}
								}
								return press;
							}});
					//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
					String numeroPref = pref.getString("numero_carabinero", "");
					if(numeroPref.equalsIgnoreCase(carabinero.getTelefono())){
						numero.setTextColor(Color.GREEN);
					}
					numero.setText(carabinero.getTelefono());
					direccion.setText(carabinero.getDireccion());
					nombre.setText(carabinero.getNombre());
		 }else{
			 if(objects.get(position) instanceof PDI){
				 final PDI pdi = (PDI) objects.get(position);
					/*
					 * fila de usuario
					 */
					final TextView numero = (TextView) v.findViewById(R.id.numero);
					TextView direccion = (TextView) v.findViewById(R.id.direccion);
					Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
					TextView nombre = (TextView) v.findViewById(R.id.nombre);
					llamar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							builder.create();
					    	builder.setTitle("Llamada de Policías de Investigaciones");
					    	builder.setMessage("¿ Realmente desea llamar a "+pdi.getNombre()+ " ?");
					    	builder.setPositiveButton("Llamar",
					    	        new DialogInterface.OnClickListener() {
					    	            public void onClick(DialogInterface dialog, int which) {
					    	            	Log.d("emergencia", pdi.getTelefono());
											Intent intent = new Intent(Intent.ACTION_CALL);
											String llamarA = "tel:"+pdi.getTelefono();
							        		intent.setData(Uri.parse(llamarA)); 
							       		    context.startActivity(intent); 
					    	            	
					    	            }
					    	        });
					    	
						    	builder.setNegativeButton("Cancelar",
						    	        new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int which) {
						                dialog.cancel();
						            }
						    	});
						    	builder.show();
							
						}
					});
					
					//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
					numero.setText(pdi.getTelefono());
					direccion.setText(pdi.getDireccion());
					nombre.setText(pdi.getNombre());
			 }else{
				 if(objects.get(position) instanceof Bombero){
					 final Bombero bombero = (Bombero) objects.get(position);
						/*
						 * fila de usuario
						 */
						final TextView numero = (TextView) v.findViewById(R.id.numero);
						TextView direccion = (TextView) v.findViewById(R.id.direccion);
						Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
						TextView nombre = (TextView) v.findViewById(R.id.nombre);

						llamar.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								builder.create();
						    	builder.setTitle("Llamada de Cuerpo de Bomberos");
						    	builder.setMessage("¿ Realmente desea llamar a "+bombero.getNombre()+ " ?");
						    	builder.setPositiveButton("Llamar",
						    	        new DialogInterface.OnClickListener() {
						    	            public void onClick(DialogInterface dialog, int which) {
						    	            	Log.d("emergencia", bombero.getTelefono());
												Intent intent = new Intent(Intent.ACTION_CALL);
												String llamarA = "tel:"+bombero.getTelefono();
								        		intent.setData(Uri.parse(llamarA)); 
								       		    context.startActivity(intent); 
						    	            	
						    	            }
						    	        });
						    	
							    	builder.setNegativeButton("Cancelar",
							    	        new DialogInterface.OnClickListener() {
							            public void onClick(DialogInterface dialog, int which) {
							                dialog.cancel();
							            }
							    	});
							    	builder.show();
							}
						});
						final Handler handler = new Handler();
						final Runnable runnable = new Runnable(){
						

							@Override
							public void run() {
								// TODO Auto-generated method stub
								vibracion.vibrate(500);
								
								AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										ringProgressDialog = ProgressDialog.show(context, "Por favor espere ...", "Actualizando ...", true);
										ringProgressDialog.setCancelable(false);
										
									}
									@Override
									protected String doInBackground(String... arg0) {
										// TODO Auto-generated method stub
										Boolean resultado = ServicioWeb.actualizaNumeroBombero(numeroUsuario,numero.getText().toString());
										if(resultado){
											return "true";
										}else{
											return "false";
										}
										
									}
									@Override
									protected void onPostExecute(String result) {
										// TODO Auto-generated method stub
										super.onPostExecute(result);
										ringProgressDialog.dismiss();
										if(result.equals("true")){
											text_numero.setTextColor(Color.WHITE);
											numero.setTextColor(Color.GREEN);
											SharedPreferences.Editor editor = pref.edit();
											editor.putString("numero_bombero", numero.getText().toString());
											editor.commit();
											Toast.makeText(context, "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
										}
									}
									
								};
								tarea.execute();
								
								
							}};
							numero.setOnTouchListener(new OnTouchListener(){

								

								@Override
								public boolean onTouch(View arg0, MotionEvent arg1) {
									// TODO Auto-generated method stub
									final TextView tv  = (TextView) arg0.findViewById(R.id.numero);
									text_numero =  tv;
									if(arg1.getAction() ==  MotionEvent.ACTION_DOWN){
										handler.postDelayed(runnable, 1500);
										
										press = true;
									}else{
										if(arg1.getAction() ==  MotionEvent.ACTION_UP){
											
											if(press){
												press = false;
												handler.removeCallbacks(runnable);
											}
										}
									}
									return press;
								}});
							
						String numeroPref = pref.getString("numero_bombero", "");
						if(numeroPref.equalsIgnoreCase(bombero.getTelefono())){
							numero.setTextColor(Color.GREEN);
						};
						numero.setText(bombero.getTelefono());
						direccion.setText(bombero.getDireccion());
						nombre.setText(bombero.getNombre());
				 }else{
					 if(objects.get(position) instanceof Hospital){
						 final Hospital hospital = (Hospital) objects.get(position);
							/*
							 * fila de usuario
							 */
							final TextView numero = (TextView) v.findViewById(R.id.numero);
							TextView direccion = (TextView) v.findViewById(R.id.direccion);
							Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
							TextView nombre = (TextView) v.findViewById(R.id.nombre);
							
							llamar.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Log.d("emergencia", "inicia llamada");
									
									builder.create();
							    	builder.setTitle("Llamada de Centro Médico");
							    	builder.setMessage("¿ Realmente desea llamar a "+hospital.getNombre()+ " ?");
							    	builder.setPositiveButton("Llamar",
							    	        new DialogInterface.OnClickListener() {
							    	            public void onClick(DialogInterface dialog, int which) {
							    	            	Log.d("emergencia", hospital.getTelefono());
													Intent intent = new Intent(Intent.ACTION_CALL);
													String llamarA = "tel:"+hospital.getTelefono();
									        		intent.setData(Uri.parse(llamarA)); 
									       		    context.startActivity(intent); 
							    	            	
							    	            }
							    	        });
							    	
								    	builder.setNegativeButton("Cancelar",
								    	        new DialogInterface.OnClickListener() {
								            public void onClick(DialogInterface dialog, int which) {
								                dialog.cancel();
								            }
								    	});
								    	builder.show();
									
								}
							});
							
							
							
							final Handler handler = new Handler();
							final Runnable runnable = new Runnable(){
							

								@Override
								public void run() {
									// TODO Auto-generated method stub
									vibracion.vibrate(500);
									
									AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
										@Override
										protected void onPreExecute() {
											super.onPreExecute();
											ringProgressDialog = ProgressDialog.show(context, "Por favor espere ...", "Actualizando ...", true);
											ringProgressDialog.setCancelable(false);
											
										}
										@Override
										protected String doInBackground(String... arg0) {
											// TODO Auto-generated method stub
											Boolean resultado = ServicioWeb.actualizaNumeroCentroMedico(numeroUsuario,numero.getText().toString());
											if(resultado){
												return "true";
											}else{
												return "false";
											}
											
										}
										@Override
										protected void onPostExecute(String result) {
											// TODO Auto-generated method stub
											super.onPostExecute(result);
											ringProgressDialog.dismiss();
											if(result.equals("true")){
												text_numero.setTextColor(Color.WHITE);
												numero.setTextColor(Color.GREEN);
												SharedPreferences.Editor editor = pref.edit();
												editor.putString("numero_centro_medico", numero.getText().toString());
												editor.commit();
												Toast.makeText(context, "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
											}
										}
										
									};
									tarea.execute();
									
									
								}};
								numero.setOnTouchListener(new OnTouchListener(){

									

									@Override
									public boolean onTouch(View arg0, MotionEvent arg1) {
										// TODO Auto-generated method stub
										final TextView tv  = (TextView) arg0.findViewById(R.id.numero);
										text_numero =  tv;
										if(arg1.getAction() ==  MotionEvent.ACTION_DOWN){
											handler.postDelayed(runnable, 1500);
											
											press = true;
										}else{
											if(arg1.getAction() ==  MotionEvent.ACTION_UP){
												
												if(press){
													press = false;
													handler.removeCallbacks(runnable);
												}
											}
										}
										return press;
									}});
								
								String numeroPref = pref.getString("numero_centro_medico", "");
								if(numeroPref.equalsIgnoreCase(hospital.getTelefono())){
									numero.setTextColor(Color.GREEN);
								};
								numero.setText(hospital.getTelefono());
								direccion.setText(hospital.getDireccion());
								nombre.setText(hospital.getNombre());
							
					 }
				 }
			 }
		 }

		return v;
	}

}
