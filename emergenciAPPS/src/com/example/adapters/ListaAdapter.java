package com.example.adapters;

import java.util.ArrayList;

import com.example.emergenciapps.R;
import com.example.emergenciapps.R.id;
import com.example.emergenciapps.R.layout;
import com.example.emergenciapps.ServicioWeb;
import com.example.object.Bombero;
import com.example.object.Carabinero;
import com.example.object.Hospital;
import com.example.object.PDI;
import com.example.seguimiento.SeguimientoActivity;

import android.R.color;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
	
	public ListaAdapter(Context context, int textViewResourceId,
			ArrayList values) {
		super(context, textViewResourceId, values);
		this.objects = values;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
					final SharedPreferences pref = v.getContext().getSharedPreferences("miCuenta", v.getContext().MODE_PRIVATE);
					llamar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Log.d("emergencia", carabinero.getTelefono());
							Intent intent = new Intent(Intent.ACTION_CALL);
							String llamarA = "tel:"+carabinero.getTelefono();
			        		intent.setData(Uri.parse(llamarA)); 
			       		    context.startActivity(intent); 
							
						}
					});
					numero.setOnTouchListener(new OnTouchListener(){
						long inicio;
						long total;
						@Override
						public boolean onTouch(final View v, MotionEvent arg1) {
							View aView = inflater.inflate(R.layout.lista_telefonos, null);
							final TextView tv  = (TextView) aView.findViewById(R.id.numero);
							switch(arg1.getAction()){
							
								case MotionEvent.ACTION_DOWN: 
									inicio = System.currentTimeMillis();
									
									numero.setTextColor(Color.GREEN);
									Log.d("emergenciapps", "tocaste a las: "+inicio);
								break;
								case MotionEvent.ACTION_UP:
									total = System.currentTimeMillis() - inicio;
									Log.d("emergenciapps", "soltaste con duracion: "+total);
									if(total > 2000){
										vibracion.vibrate(500);
										final String numeroUsuario = pref.getString("miNumero", "");
										AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
											@Override
											protected void onPreExecute() {
												super.onPreExecute();
												ringProgressDialog = ProgressDialog.show(v.getContext(), "Por favor espere ...", "Actualizando ...", true);
												ringProgressDialog.setCancelable(false);
												
											}
											@Override
											protected String doInBackground(String... arg0) {
												// TODO Auto-generated method stub
												Boolean resultado = ServicioWeb.actualizaNumeroCarabinero(numeroUsuario, numero.getText().toString());
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
													tv.setTextColor(Color.WHITE);
													numero.setTextColor(Color.GREEN);
													SharedPreferences.Editor editor = pref.edit();
													editor.putString("numero_carabinero", numero.getText().toString());
													editor.commit();
													Toast.makeText(v.getContext(), "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
												}
											}
											
										};
										tarea.execute();
										
										
									}else{
										numero.setTextColor(Color.WHITE);
									}
								break;
							}
							Log.d("emergenciapps", "evento touch");
							return true;
						}
						
					});
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
							Log.d("emergencia", pdi.getTelefono());
							Intent intent = new Intent(Intent.ACTION_CALL);
							String llamarA = "tel:"+pdi.getTelefono();
			        		intent.setData(Uri.parse(llamarA)); 
			       		    context.startActivity(intent); 
							
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
						final SharedPreferences prefb = v.getContext().getSharedPreferences("miCuenta", v.getContext().MODE_PRIVATE);

						llamar.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d("emergencia", bombero.getTelefono());
								Intent intent = new Intent(Intent.ACTION_CALL);
								String llamarA = "tel:"+bombero.getTelefono();
				        		intent.setData(Uri.parse(llamarA)); 
				       		    context.startActivity(intent); 
							}
						});
						numero.setOnTouchListener(new OnTouchListener(){
							long inicio;
							long total;
							@Override
							public boolean onTouch(final View v, MotionEvent arg1) {
								View aView = inflater.inflate(R.layout.lista_telefonos, null);
								final TextView tv  = (TextView) aView.findViewById(R.id.numero);
								switch(arg1.getAction()){
								
								case MotionEvent.ACTION_DOWN: 
									inicio = System.currentTimeMillis();
									
									numero.setTextColor(Color.GREEN);
									Log.d("emergenciapps", "tocaste a las: "+inicio);
								break;
								case MotionEvent.ACTION_UP:
									total = System.currentTimeMillis() - inicio;
									Log.d("emergenciapps", "soltaste con duracion: "+total);
									if(total > 2000){
										vibracion.vibrate(500);
										final String numeroUsuario = prefb.getString("miNumero", "");
										AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
											@Override
											protected void onPreExecute() {
												super.onPreExecute();
												ringProgressDialog = ProgressDialog.show(v.getContext(), "Por favor espere ...", "Actualizando ...", true);
												ringProgressDialog.setCancelable(false);
												
											}
											@Override
											protected String doInBackground(String... arg0) {
												// TODO Auto-generated method stub
												Boolean resultado = ServicioWeb.actualizaNumeroBombero(numeroUsuario, numero.getText().toString());
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
													tv.setTextColor(Color.WHITE);
													numero.setTextColor(Color.GREEN);
													SharedPreferences.Editor editor = prefb.edit();
													editor.putString("numero_bombero", numero.getText().toString());
													editor.commit();
													Toast.makeText(v.getContext(), "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
												}
											}
											
										};
										tarea.execute();
										
										
									}else{
										numero.setTextColor(Color.WHITE);
									}
								break;
								}
								Log.d("emergenciapps", "evento touch");
								return true;
							}
							
						});
						//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
						
						String numeroPref = prefb.getString("numero_bombero", "");
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
							final SharedPreferences prefh = v.getContext().getSharedPreferences("miCuenta", v.getContext().MODE_PRIVATE);
							llamar.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Log.d("emergencia", hospital.getTelefono());
									Intent intent = new Intent(Intent.ACTION_CALL);
									String llamarA = "tel:"+hospital.getTelefono();
					        		intent.setData(Uri.parse(llamarA)); 
					       		    context.startActivity(intent); 
								}
							});
							
							numero.setOnTouchListener(new OnTouchListener(){
								long inicio;
								long total;
								@Override
								public boolean onTouch(final View v, MotionEvent arg1) {
									View aView = inflater.inflate(R.layout.lista_telefonos, null);
									final TextView tv  = (TextView) aView.findViewById(R.id.numero);
									switch(arg1.getAction()){
									
									case MotionEvent.ACTION_DOWN: 
										inicio = System.currentTimeMillis();
										
										numero.setTextColor(Color.GREEN);
										Log.d("emergenciapps", "tocaste a las: "+inicio);
									break;
									case MotionEvent.ACTION_UP:
										total = System.currentTimeMillis() - inicio;
										Log.d("emergenciapps", "soltaste con duracion: "+total);
										if(total > 2000){
											vibracion.vibrate(500);
											final String numeroUsuario = prefh.getString("miNumero", "");
											AsyncTask<String, Void, String> tarea = new AsyncTask<String, Void, String> (){
												@Override
												protected void onPreExecute() {
													super.onPreExecute();
													ringProgressDialog = ProgressDialog.show(v.getContext(), "Por favor espere ...", "Actualizando ...", true);
													ringProgressDialog.setCancelable(false);
													
												}
												@Override
												protected String doInBackground(String... arg0) {
													// TODO Auto-generated method stub
													Boolean resultado = ServicioWeb.actualizaNumeroCentroMedico(numeroUsuario, numero.getText().toString());
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
														tv.setTextColor(Color.WHITE);
														numero.setTextColor(Color.GREEN);
														SharedPreferences.Editor editor = prefh.edit();
														editor.putString("numero_centro_medico", numero.getText().toString());
														editor.commit();
														Toast.makeText(v.getContext(), "El número "+numero.getText().toString()+" se ha añadido a sus favoritos", Toast.LENGTH_LONG).show();
													}
												}
												
											};
											tarea.execute();
											
											
										}else{
											numero.setTextColor(Color.WHITE);
										}
									break;
									}
									Log.d("emergenciapps", "evento touch");
									return true;
								}
								
							});
							//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
							
							String numeroPref = prefh.getString("numero_centro_medico", "");
							if(numeroPref.equalsIgnoreCase(hospital.getTelefono())){
								numero.setTextColor(Color.GREEN);
							}
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
