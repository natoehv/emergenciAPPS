package com.example.emergenciapps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TareaMuestraMensaje extends AsyncTask<String, Void, String>{
	Context context;
	public TareaMuestraMensaje(Context context){
		this.context = context;
	}
	@Override
	protected String doInBackground(String... params) {
		
		return params[0];
	}
	@Override
	protected void onPostExecute(String result) {
		Log.d("emergencia", result);
		Toast.makeText(context,
               result, Toast.LENGTH_LONG).show();
		super.onPostExecute(result);
	}
}
