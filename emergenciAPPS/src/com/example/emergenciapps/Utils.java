package com.example.emergenciapps;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.contactos.DetalleContactoActivity;
import com.example.object.Contacto;
import com.example.persistencia.ContactoSQLHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static boolean validateEmail(String email) {
		 
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
 
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
 
    }
	
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
		
		Cursor oLoop = db.rawQuery("select * from contacto order by nombre asc", null);
		
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
	
public static ArrayList<Contacto> getContactosSMS(Context context) {
		
		ArrayList<Contacto> lista = new ArrayList<Contacto>();
		ContactoSQLHelper oData = new ContactoSQLHelper(context); 
		SQLiteDatabase db = oData.getWritableDatabase(); 
		
		Cursor oLoop = db.rawQuery("select * from contacto where alerta_sms = 1", null);
		
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
		
		String maxID = "select * from contacto";
		final SQLiteStatement stmt = db.compileStatement("SELECT MAX(_id) FROM contacto");
		
		int cnt = (int) stmt.simpleQueryForLong();
		Log.d("cantidad de registros",""+cnt);
		int indice;
		if(cnt != 0){
			indice = cnt + 1;
		}else{
			indice = 1;
		}
	    
	    stmt.close();
		
		ContentValues values = new ContentValues();
		values.put("_id", indice);
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
			Toast.makeText(context, "No fue posible agregar contacto en Base de Datos interna", Toast.LENGTH_LONG).show();
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
			Log.d("_id",""+contacto.getIdContacto());
			db.update("contacto", values, "_id " + "=" + contacto.getIdContacto(), null);
		} catch (Exception e) {
			Toast.makeText(context, "No fue posible actualizar contacto en Base de Datos interna", Toast.LENGTH_LONG).show();
		}
		
		db.close();
		oData.close();
	}

	public static void deleteContacto(int id_contacto, Context context) {
		ContactoSQLHelper oData = new ContactoSQLHelper(context); 
		SQLiteDatabase db = oData.getWritableDatabase();
		try{
			db.delete("contacto", "_id" + "=" + id_contacto, null);
		}catch(Exception e){
			Toast.makeText(context, "No fue posible eliminar contacto de Base de Datos interna", Toast.LENGTH_LONG).show();
		}
		db.close();
		oData.close();
		
		
		
	}
	
	public static boolean insertContactos(List<Contacto> contactos, Context context){
		
		if(contactos != null){
			ContactoSQLHelper oData = new ContactoSQLHelper(context); 
			SQLiteDatabase db = oData.getWritableDatabase();
			ContentValues values = new ContentValues();
			for(Contacto c: contactos){
				values.put("_id", c.getIdContacto());
				values.put("numero_telefono", c.getNumeroTelefono());
				values.put("numero",c.getNumero());
				values.put("nombre", c.getNombre());
				values.put("correo", c.getCorreo());
				values.put("alerta_sms", c.getAlertaSMS());
				values.put("alerta_gps", c.getAlertaGPS());
				values.put("alerta_correo", c.getAlertaCorreo());
				values.put("estado",c.getEstado());
				try {
					db.insertOrThrow("contacto", null, values);
				} catch (Exception e) {
					Log.d("contactos_actializados","no se pudo actualizar lista");
					return false;
					
				}
			}
			db.close();
			oData.close();
			
			return true;
			
		}else{
			return false;
		}
		
		
	}
}
