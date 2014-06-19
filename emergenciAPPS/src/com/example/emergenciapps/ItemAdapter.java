package com.example.emergenciapps;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Item>{
	private ArrayList<Item> objects;
	private Context context;
	public ItemAdapter(Context context, int textViewResourceId,
			ArrayList<Item> objects) {
		super(context, textViewResourceId, objects);
		
		this.context = context;
		this.objects = objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.drawer_list_item, null);
		Item item = objects.get(position);
		TextView titulo = (TextView) v.findViewById(R.id.titulo);
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		titulo.setText(item.getTitulo());
		icon.setImageResource(item.getIdIcon());
		return v;
	}
	
	
}
