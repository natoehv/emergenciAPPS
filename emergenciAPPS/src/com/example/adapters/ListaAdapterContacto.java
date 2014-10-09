package com.example.adapters;

import java.util.ArrayList;

import com.example.emergenciapps.R;
import com.example.object.Configuracion;
import com.example.object.Contacto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaAdapterContacto extends ArrayAdapter{
	private ArrayList<Contacto> objects;
	private Context context;
	
	public ListaAdapterContacto(Context context, int textViewResourceId,ArrayList values) {
		super(context, textViewResourceId, values);
		
		this.objects = values;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.lista_contactos, null);
		TextView nombre = (TextView) v.findViewById(R.id.nombre);
		TextView numero = (TextView) v.findViewById(R.id.numero);
		ImageView gps = (ImageView) v.findViewById(R.id.imagenGps);
		ImageView email = (ImageView) v.findViewById(R.id.imagenEmail);
		ImageView sms = (ImageView) v.findViewById(R.id.imagenSMS);
		
		Button edit = (Button) v.findViewById(R.id.btnEdit);
		
		nombre.setText(objects.get(position).getNombre());
		numero.setText(objects.get(position).getNumero());
		
		return v;
		
	}

}
