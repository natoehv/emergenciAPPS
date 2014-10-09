package com.example.contactos;

import com.example.emergenciapps.R;
import com.example.object.Contacto;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Switch;
import android.widget.TextView;

public class DetalleContacto extends Activity{
	private TextView nombre;
	private TextView numero;
	private TextView correo;
	private Contacto contacto;
	private Switch gps;
	private Switch sms;
	private Switch email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_contacto);
		
		nombre = (TextView) this.findViewById(R.id.nombreContacto);
		numero = (TextView) this.findViewById(R.id.numeroContacto);
		correo = (TextView) this.findViewById(R.id.correoContacto);
		gps = (Switch) this.findViewById(R.id.switchGPS);
		sms = (Switch) this.findViewById(R.id.switchSMS);
		email = (Switch) this.findViewById(R.id.switchEmail);
		
		Bundle extras = this.getIntent().getExtras();
		
		if(extras != null){
			contacto = (Contacto) extras.getSerializable("contacto");
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
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacto, menu);
        
		return super.onCreateOptionsMenu(menu);
		
	}
	
	

}
