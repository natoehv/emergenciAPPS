package com.example.emergenciapps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TaskSendMail extends AsyncTask<String, Void, String>{
	EmailEmergencia email;
	public TaskSendMail(EmailEmergencia email){
		this.email = email;
	}
	@Override
	protected String doInBackground(String... params) {
		return ServicioWeb.sendMail(params[0], params[1], email.getCorreo(), email.getMensaje(), email.getMiNombre(), email.getMiNumero());
	}
	@Override
	protected void onPostExecute(String result) {
		Log.d("emergencia", result);
		Toast.makeText(email.getContext(),
               result, Toast.LENGTH_SHORT).show();
		super.onPostExecute(result);
	}
}
