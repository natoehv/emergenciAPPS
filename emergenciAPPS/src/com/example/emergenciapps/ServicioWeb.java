package com.example.emergenciapps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

public class ServicioWeb {
	public static List postCercanos(GeoPoint punto, int distancia, String tabla){
    	String URL = "http://colvin.chillan.ubiobio.cl:8070/rhormaza/servicioweb.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	String jsonReturnText="";
    	String respuesta;
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("tabla",tabla));
    			oPostParam.add(new BasicNameValuePair("lat",punto.getLatitude()+""));
    			oPostParam.add(new BasicNameValuePair("lng",punto.getLongitude()+""));
    			oPostParam.add(new BasicNameValuePair("distancia",distancia+""));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			jsonReturnText = EntityUtils.toString(r_entity);
    			
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    		}
    		if(tabla.equalsIgnoreCase("carabinero")){	
	    		try{
	    			JSONObject json = new JSONObject(jsonReturnText);
	    			JSONArray jArray = json.getJSONArray(tabla);
	    			Carabinero carabinero;
					for(int i=0; i<jArray.length(); i++){
						carabinero = new Carabinero();
						JSONObject aux = jArray.getJSONObject(i);
						String nombre = aux.getString("nombre");
						String lat  = aux.getString("lat");
						String lng = aux.getString("lng");
						String direccion = aux.getString("direccion");
						String telefono = aux.getString("telefono");
						String distancia2 = aux.getString("distancia");
						int id = aux.getInt("id");
						String comuna = aux.getString("comuna");
						
						carabinero.setComuna(comuna);
						carabinero.setDireccion(direccion);
						carabinero.setId(id);
						carabinero.setNombre(nombre);
						carabinero.setTelefono(telefono);
						carabinero.setX(Float.valueOf(lat));
						carabinero.setY(Float.valueOf(lng));
						carabinero.setDistancia(Float.valueOf(distancia2));
					    resultados.add(carabinero);
					    
					}
					return resultados;
	    		}catch(JSONException e){
	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
	    		}
    		
    		}else{
    			if(tabla.equalsIgnoreCase("pdi")){
    				try{
    	    			JSONObject json = new JSONObject(jsonReturnText);
    	    			JSONArray jArray = json.getJSONArray(tabla);
    	    			PDI pdi;
    					for(int i=0; i<jArray.length(); i++){
    						pdi = new PDI();
    						JSONObject aux = jArray.getJSONObject(i);
    						String nombre = aux.getString("nombre");
    						String lat  = aux.getString("lat");
    						String lng = aux.getString("lng");
    						String direccion = aux.getString("direccion");
    						String telefono = aux.getString("telefono");
    						String distancia2 = aux.getString("distancia");
    						int id = aux.getInt("id");
    						String comuna = aux.getString("comuna");
    						
    						pdi.setComuna(comuna);
    						pdi.setDireccion(direccion);
    						pdi.setId(id);
    						pdi.setNombre(nombre);
    						pdi.setTelefono(telefono);
    						pdi.setX(Float.valueOf(lat));
    						pdi.setY(Float.valueOf(lng));
    						pdi.setDistancia(Float.valueOf(distancia2));
    					    resultados.add(pdi);
    					    
    					}
    					return resultados;
    	    		}catch(JSONException e){
    	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
    	    		}
    			}else{
    				if(tabla.equalsIgnoreCase("bombero")){
    					try{
        	    			JSONObject json = new JSONObject(jsonReturnText);
        	    			JSONArray jArray = json.getJSONArray(tabla);
        	    			Bombero bombero;
        					for(int i=0; i<jArray.length(); i++){
        						bombero = new Bombero();
        						JSONObject aux = jArray.getJSONObject(i);
        						String nombre = aux.getString("nombre");
        						String lat  = aux.getString("lat");
        						String lng = aux.getString("lng");
        						String direccion = aux.getString("direccion");
        						String telefono = aux.getString("telefono");
        						String distancia2 = aux.getString("distancia");
        						int id = aux.getInt("id");
        						String comuna = aux.getString("comuna");
        						
        						bombero.setComuna(comuna);
        						bombero.setDireccion(direccion);
        						bombero.setId(id);
        						bombero.setNombre(nombre);
        						bombero.setTelefono(telefono);
        						bombero.setX(Float.valueOf(lat));
        						bombero.setY(Float.valueOf(lng));
        						bombero.setDistancia(Float.valueOf(distancia2));
        					    resultados.add(bombero);
        					    
        					}
        					return resultados;
        	    		}catch(JSONException e){
        	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
        	    		}
    				}else{
    					if(tabla.equalsIgnoreCase("hospital")){
        					try{
            	    			JSONObject json = new JSONObject(jsonReturnText);
            	    			JSONArray jArray = json.getJSONArray(tabla);
            	    			Hospital hospital;
            					for(int i=0; i<jArray.length(); i++){
            						hospital = new Hospital();
            						JSONObject aux = jArray.getJSONObject(i);
            						String nombre = aux.getString("nombre");
            						String lat  = aux.getString("lat");
            						String lng = aux.getString("lng");
            						String direccion = aux.getString("direccion");
            						String telefono = aux.getString("telefono");
            						String distancia2 = aux.getString("distancia");
            						int id = aux.getInt("id");
            						String comuna = aux.getString("comuna");
            						
            						hospital.setComuna(comuna);
            						hospital.setDireccion(direccion);
            						hospital.setId(id);
            						hospital.setNombre(nombre);
            						hospital.setTelefono(telefono);
            						hospital.setX(Float.valueOf(lat));
            						hospital.setY(Float.valueOf(lng));
            						hospital.setDistancia(Float.valueOf(distancia2));
            					    resultados.add(hospital);
            					    
            					}
            					return resultados;
            	    		}catch(JSONException e){
            	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
            	    		}
    					}
    				}
    			}
    		}
    	return resultados;
    }
	
	public static List buscaPorComuna(String comunaBuscar, String tabla){
		String URL = "http://colvin.chillan.ubiobio.cl:8070/rhormaza/servicio_web_busqueda.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	String jsonReturnText="";
    	String respuesta;
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("tabla",tabla));
    			oPostParam.add(new BasicNameValuePair("comuna",comunaBuscar));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			jsonReturnText = EntityUtils.toString(r_entity);
    			
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    		}
    		if(tabla.equalsIgnoreCase("carabinero")){	
	    		try{
	    			JSONObject json = new JSONObject(jsonReturnText);
	    			JSONArray jArray = json.getJSONArray(tabla);
	    			Carabinero carabinero;
					for(int i=0; i<jArray.length(); i++){
						carabinero = new Carabinero();
						JSONObject aux = jArray.getJSONObject(i);
						String nombre = aux.getString("nombre");
						String lat  = aux.getString("lat");
						String lng = aux.getString("lng");
						String direccion = aux.getString("direccion");
						String telefono = aux.getString("telefono");
						String distancia2 = aux.getString("distancia");
						int id = aux.getInt("id");
						String comuna = aux.getString("comuna");
						
						carabinero.setComuna(comuna);
						carabinero.setDireccion(direccion);
						carabinero.setId(id);
						carabinero.setNombre(nombre);
						carabinero.setTelefono(telefono);
						carabinero.setX(Float.valueOf(lat));
						carabinero.setY(Float.valueOf(lng));
						carabinero.setDistancia(Float.valueOf(distancia2));
					    resultados.add(carabinero);
					    
					}
					return resultados;
	    		}catch(JSONException e){
	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
	    		}
    		
    		}else{
    			if(tabla.equalsIgnoreCase("pdi")){
    				try{
    	    			JSONObject json = new JSONObject(jsonReturnText);
    	    			JSONArray jArray = json.getJSONArray(tabla);
    	    			PDI pdi;
    					for(int i=0; i<jArray.length(); i++){
    						pdi = new PDI();
    						JSONObject aux = jArray.getJSONObject(i);
    						String nombre = aux.getString("nombre");
    						String lat  = aux.getString("lat");
    						String lng = aux.getString("lng");
    						String direccion = aux.getString("direccion");
    						String telefono = aux.getString("telefono");
    						String distancia2 = aux.getString("distancia");
    						int id = aux.getInt("id");
    						String comuna = aux.getString("comuna");
    						
    						pdi.setComuna(comuna);
    						pdi.setDireccion(direccion);
    						pdi.setId(id);
    						pdi.setNombre(nombre);
    						pdi.setTelefono(telefono);
    						pdi.setX(Float.valueOf(lat));
    						pdi.setY(Float.valueOf(lng));
    						pdi.setDistancia(Float.valueOf(distancia2));
    					    resultados.add(pdi);
    					    
    					}
    					return resultados;
    	    		}catch(JSONException e){
    	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
    	    		}
    			}else{
    				if(tabla.equalsIgnoreCase("bombero")){
    					try{
        	    			JSONObject json = new JSONObject(jsonReturnText);
        	    			JSONArray jArray = json.getJSONArray(tabla);
        	    			Bombero bombero;
        					for(int i=0; i<jArray.length(); i++){
        						bombero = new Bombero();
        						JSONObject aux = jArray.getJSONObject(i);
        						String nombre = aux.getString("nombre");
        						String lat  = aux.getString("lat");
        						String lng = aux.getString("lng");
        						String direccion = aux.getString("direccion");
        						String telefono = aux.getString("telefono");
        						String distancia2 = aux.getString("distancia");
        						int id = aux.getInt("id");
        						String comuna = aux.getString("comuna");
        						
        						bombero.setComuna(comuna);
        						bombero.setDireccion(direccion);
        						bombero.setId(id);
        						bombero.setNombre(nombre);
        						bombero.setTelefono(telefono);
        						bombero.setX(Float.valueOf(lat));
        						bombero.setY(Float.valueOf(lng));
        						bombero.setDistancia(Float.valueOf(distancia2));
        					    resultados.add(bombero);
        					    
        					}
        					return resultados;
        	    		}catch(JSONException e){
        	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
        	    		}
    				}else{
    					if(tabla.equalsIgnoreCase("hospital")){
        					try{
            	    			JSONObject json = new JSONObject(jsonReturnText);
            	    			JSONArray jArray = json.getJSONArray(tabla);
            	    			Hospital hospital;
            					for(int i=0; i<jArray.length(); i++){
            						hospital = new Hospital();
            						JSONObject aux = jArray.getJSONObject(i);
            						String nombre = aux.getString("nombre");
            						String lat  = aux.getString("lat");
            						String lng = aux.getString("lng");
            						String direccion = aux.getString("direccion");
            						String telefono = aux.getString("telefono");
            						String distancia2 = aux.getString("distancia");
            						int id = aux.getInt("id");
            						String comuna = aux.getString("comuna");
            						
            						hospital.setComuna(comuna);
            						hospital.setDireccion(direccion);
            						hospital.setId(id);
            						hospital.setNombre(nombre);
            						hospital.setTelefono(telefono);
            						hospital.setX(Float.valueOf(lat));
            						hospital.setY(Float.valueOf(lng));
            						hospital.setDistancia(Float.valueOf(distancia2));
            					    resultados.add(hospital);
            					    
            					}
            					return resultados;
            	    		}catch(JSONException e){
            	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
            	    		}
    					}
    				}
    			}
    		}
    	return resultados;
	}
	public static String sendMail(String lat, String lng, String correo, String msj, String miNombre, String miNumero){
		String URL = "http://colvin.chillan.ubiobio.cl:8070/rhormaza/sendMail.php";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	String respuesta;
    	String descripcion = "";
    	
    	try{
    		List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
			oPostParam.add(new BasicNameValuePair("lat",lat));
			oPostParam.add(new BasicNameValuePair("lng",lng));
			oPostParam.add(new BasicNameValuePair("correo",correo));
			oPostParam.add(new BasicNameValuePair("mensaje",msj));
			oPostParam.add(new BasicNameValuePair("miNombre",miNombre));
			oPostParam.add(new BasicNameValuePair("miNumero",miNumero));
			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
			
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return "El mensaje no ha podido ser enviado";
    	}
    	
    	try{
    		JSONObject reader = new JSONObject(respuesta);
    		JSONObject sys  = reader.getJSONObject("respuesta");
    		descripcion = sys.getString("descripcion");
    	}catch(JSONException e){
    		Log.e("emergenciAPPS", "Error al parsear "+respuesta, e);
    	}
    	
		return descripcion;
		
	}
}
