package com.example.emergenciapps;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaAdapter  extends ArrayAdapter{
	private ArrayList objects;
	private Context context;
	public ListaAdapter(Context context, int textViewResourceId,
			ArrayList values) {
		super(context, textViewResourceId, values);
		this.objects = values;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.lista_telefonos, null);
		 
		 if(objects.get(position) instanceof Carabinero){
			 Carabinero carabinero = (Carabinero) objects.get(position);
					/*
					 * fila de usuario
					 */
					TextView numero = (TextView) v.findViewById(R.id.numero);
					TextView direccion = (TextView) v.findViewById(R.id.direccion);
					Button llamar = (Button) v.findViewById(R.id.lista_boton_llamar);
					llamar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO llamar a telefono de carabinero
							
						}
					});
					//icon.setImageBitmap(EmergenciUTIL.resizeImage(context.getResources().getDrawable(R.drawable.anonimo), 30, 30));
					numero.setText(carabinero.getTelefono());
					direccion.setText(carabinero.getDireccion());
		 }

		return v;
	}

}
