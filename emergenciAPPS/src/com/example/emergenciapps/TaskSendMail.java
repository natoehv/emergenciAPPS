package com.example.emergenciapps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TaskSendMail extends AsyncTask<Object, Void, String>{
	EmailEmergencia email;
	View v;
	private static TaskSendMail instancia;
	private TaskSendMail(){
	}
	public static TaskSendMail getInstance(){
		if(instancia == null)
			instancia = new TaskSendMail();
		return instancia;
	}
	@Override
	protected String doInBackground(Object... params) {
		email = (EmailEmergencia) params[2];
		v = (View) params[3];
		return ServicioWeb.sendMail((String)params[0], (String)params[1], email.getCorreo(), email.getMensaje(), email.getMiNombre(), email.getMiNumero());
	}
	@Override
	protected void onPostExecute(String result) {
		Log.d("emergencia", result);
		Toast.makeText(email.getContext(),
               result, Toast.LENGTH_SHORT).show();
		Button button = (Button) v.findViewById(R.id.btnEmergencia);
		button.setBackgroundResource(R.drawable.ayuda);
		instancia = new TaskSendMail();
		super.onPostExecute(result);
	}
}
