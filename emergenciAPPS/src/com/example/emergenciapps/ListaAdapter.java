package com.example.emergenciapps;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaAdapter  extends ArrayAdapter{
	private ArrayList objects;
	private Context context;
	public ListaAdapter(Context context, int textViewResourceId,
			ArrayList values) {
		super(context, textViewResourceId, values);
		this.objects = values;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.lista_telefonos, null);
		 
		 if(objects.get(position) instanceof Carabinero){
			 final Carabinero carabinero = (Carabinero) objects.get(position);
					/*
					 * fila de usuario
					 */
					final TextView numero = (TextView) v.findViewById(R.id.numero);
					TextView direccion = (TextView) v.findViewById(R.id.direccion);
					Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
					final SharedPreferences pref = v.getContext().getSharedPreferences("MisContactos", v.getContext().MODE_PRIVATE);
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
						public boolean onTouch(View v, MotionEvent arg1) {					
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
									Log.d("emergenciapps","tiempo capturado mayor a 2 segundos: "+total);
									//TODO guardar numero
									
									SharedPreferences.Editor editor = pref.edit();
									editor.putString("numeroCarabinero", numero.getText().toString());
									editor.commit();
									Toast.makeText(v.getContext(), "El n�mero "+numero.getText().toString()+" se ha a�adido a sus favoritos", Toast.LENGTH_LONG).show();
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
					String numeroPref = pref.getString("numeroCarabinero", "");
					if(numeroPref.equalsIgnoreCase(carabinero.getTelefono())){
						numero.setTextColor(Color.GREEN);
					}
					numero.setText(carabinero.getTelefono());
					direccion.setText(carabinero.getDireccion());
		 }else{
			 if(objects.get(position) instanceof PDI){
				 final PDI pdi = (PDI) objects.get(position);
					/*
					 * fila de usuario
					 */
					final TextView numero = (TextView) v.findViewById(R.id.numero);
					TextView direccion = (TextView) v.findViewById(R.id.direccion);
					Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
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
			 }else{
				 if(objects.get(position) instanceof Bombero){
					 final Bombero bombero = (Bombero) objects.get(position);
						/*
						 * fila de usuario
						 */
						TextView numero = (TextView) v.findViewById(R.id.numero);
						TextView direccion = (TextView) v.findViewById(R.id.direccion);
						Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
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
						//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
						numero.setText(bombero.getTelefono());
						direccion.setText(bombero.getDireccion());
				 }else{
					 if(objects.get(position) instanceof Hospital){
						 final Hospital hospital = (Hospital) objects.get(position);
							/*
							 * fila de usuario
							 */
							TextView numero = (TextView) v.findViewById(R.id.numero);
							TextView direccion = (TextView) v.findViewById(R.id.direccion);
							Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
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
							//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
							numero.setText(hospital.getTelefono());
							direccion.setText(hospital.getDireccion());
					 }
				 }
			 }
		 }

		return v;
	}

}
