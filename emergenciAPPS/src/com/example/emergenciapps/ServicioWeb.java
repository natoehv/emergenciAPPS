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
    	if(tabla.equalsIgnoreCase("carabinero")){
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
    		
    	}
    	return null;
    }
}
