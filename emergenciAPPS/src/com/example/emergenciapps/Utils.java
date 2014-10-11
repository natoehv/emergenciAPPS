package com.example.emergenciapps;

import java.util.ArrayList;

import com.example.object.Contacto;
import com.example.persistencia.ContactoSQLHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static ArrayList<Contacto> getContactos(Context context) {
		
		ArrayList<Contacto> lista = new ArrayList<Contacto>();
		ContactoSQLHelper oData = new ContactoSQLHelper(context); 
		SQLiteDatabase db = oData.getWritableDatabase(); 
		
		Cursor oLoop = db.rawQuery("select * from contacto ", null);
		
		while(oLoop.moveToNext()){
			Contacto contacto = new Contacto();
			contacto.setIdContacto(oLoop.getInt(0));
			contacto.setNumeroTelefono(oLoop.getString(1));
			contacto.setNombre(oLoop.getString(2));
			contacto.setNumero(oLoop.getString(3));
			contacto.setCorreo(oLoop.getString(4));
			contacto.setEstado(oLoop.getInt(5));
			contacto.setAlertaSMS(oLoop.getInt(6));
			contacto.setAlertaGPS(oLoop.getInt(7));
			contacto.setAlertaCorreo(oLoop.getInt(8));
			lista.add(contacto);
		}
		
		oLoop.close();
		db.close();
		oData.close();
		
		return lista;
	}
	
	public static void insertContacto(Contacto contacto, Context context){
		ContactoSQLHelper oData = new ContactoSQLHelper(context); 
		SQLiteDatabase db = oData.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("numero_telefono", contacto.getNumeroTelefono());
		values.put("numero", contacto.getNumero());
		values.put("nombre", contacto.getNombre());
		values.put("correo", contacto.getCorreo());
		values.put("alerta_sms", contacto.getAlertaSMS());
		values.put("alerta_gps", contacto.getAlertaGPS());
		values.put("alerta_correo", contacto.getAlertaCorreo());
		values.put("estado",0);
		
		try {
			db.insertOrThrow("contacto", null, values);
		} catch (Exception e) {
			//manejar la excepción
		}
		
		db.close();
		oData.close();
	}
	
	public static void updateContacto(Contacto contacto, Context context){
		ContactoSQLHelper oData = new ContactoSQLHelper(context); 
		SQLiteDatabase db = oData.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("numero_telefono", contacto.getNumeroTelefono());
		values.put("numero", contacto.getNumero());
		values.put("nombre", contacto.getNombre());
		values.put("correo", contacto.getCorreo());
		values.put("alerta_sms", contacto.getAlertaSMS());
		values.put("alerta_gps", contacto.getAlertaGPS());
		values.put("alerta_correo", contacto.getAlertaCorreo());
		values.put("estado",0);
		
		try {
			db.update("contacto", values, "id_contacto " + "=" + contacto.getIdContacto(), null);
		} catch (Exception e) {
			//manejar la excepción
		}
		
		db.close();
		oData.close();
	}
}
