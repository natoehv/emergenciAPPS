package com.example.emergenciapps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.object.Bombero;
import com.example.object.Carabinero;
import com.example.object.Configuracion;
import com.example.object.Contacto;
import com.example.object.Hospital;
import com.example.object.PDI;
import com.example.object.Usuario;
import com.mapquest.android.maps.GeoPoint;

public class ServicioWeb {
	public static final int OK_CONEXION = 0;
	public static final int ERROR_CONEXION = 1;
	public static final int ERROR_JSON = 2;
	public static final int ERROR_JSON_GPS = 3;
	public static final int ERROR_NO_EXISTE_COMUNA = 4;
	
	public static RespuestaServicioWeb postCercanos(GeoPoint punto, int distancia, String tabla){
    	String URL = "http://colvin.chillan.ubiobio.cl:8070/rhormaza/servicioweb.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	Integer codigo = OK_CONEXION;
    	RespuestaServicioWeb res;
    	String jsonReturnText="";
    	String respuesta;
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 10000;
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
    			codigo = ERROR_CONEXION;
    			res =  new RespuestaServicioWeb(resultados, codigo);
    	    	return res;
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
					
	    		}catch(JSONException e){
	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
	    			codigo = ERROR_JSON_GPS;
	    			
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
    					
    	    		}catch(JSONException e){
    	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
    	    			codigo = ERROR_JSON_GPS;
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
        					
        	    		}catch(JSONException e){
        	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
        	    			codigo = ERROR_JSON_GPS;
        	    		}
    				}else{
    					if(tabla.equalsIgnoreCase("centro_medico")){
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
            					
            	    		}catch(JSONException e){
            	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
            	    			codigo = ERROR_JSON_GPS;
            	    		}
    					}
    				}
    			}
    		}
    		res =  new RespuestaServicioWeb(resultados, codigo);
        	return res;
    }
	
	public static RespuestaServicioWeb buscaPorComuna(String comunaBuscar, String tabla){
		RespuestaServicioWeb res;
		Integer codigo = OK_CONEXION;
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
    			codigo = ERROR_CONEXION;
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    			res =  new RespuestaServicioWeb(resultados, codigo);
    	    	return res;
    		}
    		if(tabla.equalsIgnoreCase("carabinero")){	
	    		try{
	    			JSONObject json = new JSONObject(jsonReturnText);
	    			JSONArray jArray = json.getJSONArray(tabla);
	    			if(jArray.length() > 0){
	    				Log.d("comuna1", "Cantidad de resultados carabineros: "+jArray.length());
	    				Carabinero carabinero;
						for(int i=0; i<jArray.length(); i++){
							carabinero = new Carabinero();
							JSONObject aux = jArray.getJSONObject(i);
							String nombre = aux.getString("nombre");
							String lat  = aux.getString("lat");
							String lng = aux.getString("lng");
							String direccion = aux.getString("direccion");
							String telefono = aux.getString("telefono");
							int id = aux.getInt("id");
							
							carabinero.setDireccion(direccion);
							carabinero.setId(id);
							carabinero.setNombre(nombre);
							carabinero.setTelefono(telefono);
							carabinero.setX(Float.valueOf(lat));
							carabinero.setY(Float.valueOf(lng));
						    resultados.add(carabinero);
						    
						}
	    			}else{
	    				Log.d("comuna2", "Cantidad de resultados: "+jArray.length());
	    				codigo = ERROR_NO_EXISTE_COMUNA;
	    				
	    			}
	    			
					
	    		}catch(JSONException e){
	    			codigo = ERROR_JSON;
	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
	    		}
    		
    		}else{
    			if(tabla.equalsIgnoreCase("pdi")){
    				try{
    	    			JSONObject json = new JSONObject(jsonReturnText);
    	    			JSONArray jArray = json.getJSONArray(tabla);
    	    			if(jArray.length() > 0){
    	    				PDI pdi;
        					for(int i=0; i<jArray.length(); i++){
        						pdi = new PDI();
        						JSONObject aux = jArray.getJSONObject(i);
        						String nombre = aux.getString("nombre");
        						String lat  = aux.getString("lat");
        						String lng = aux.getString("lng");
        						String direccion = aux.getString("direccion");
        						String telefono = aux.getString("telefono");
        						int id = aux.getInt("id");
        						
        						pdi.setDireccion(direccion);
        						pdi.setId(id);
        						pdi.setNombre(nombre);
        						pdi.setTelefono(telefono);
        						pdi.setX(Float.valueOf(lat));
        						pdi.setY(Float.valueOf(lng));
        					    resultados.add(pdi);
        					    
        					}
    	    			}else{
    	    				codigo = ERROR_NO_EXISTE_COMUNA;
    	    			}
    	    			
    					
    	    		}catch(JSONException e){
    	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
    	    			codigo = ERROR_JSON;
    	    		}
    			}else{
    				if(tabla.equalsIgnoreCase("bombero")){
    					try{
        	    			JSONObject json = new JSONObject(jsonReturnText);
        	    			JSONArray jArray = json.getJSONArray(tabla);
        	    			if(jArray.length() > 0){
        	    				Bombero bombero;
            					for(int i=0; i<jArray.length(); i++){
            						bombero = new Bombero();
            						JSONObject aux = jArray.getJSONObject(i);
            						String nombre = aux.getString("nombre");
            						String lat  = aux.getString("lat");
            						String lng = aux.getString("lng");
            						String direccion = aux.getString("direccion");
            						String telefono = aux.getString("telefono");
            						int id = aux.getInt("id");
            						
            						bombero.setDireccion(direccion);
            						bombero.setId(id);
            						bombero.setNombre(nombre);
            						bombero.setTelefono(telefono);
            						bombero.setX(Float.valueOf(lat));
            						bombero.setY(Float.valueOf(lng));
            					    resultados.add(bombero);
            					    
            					}
        	    			}else{
        	    				codigo = ERROR_NO_EXISTE_COMUNA;
        	    			}
        	    			
        					
        	    		}catch(JSONException e){
        	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
        	    			codigo = ERROR_JSON;
        	    		}
    				}else{
    					if(tabla.equalsIgnoreCase("centro_medico")){
        					try{
            	    			JSONObject json = new JSONObject(jsonReturnText);
            	    			JSONArray jArray = json.getJSONArray(tabla);
            	    			if(jArray.length() > 0){
            	    				Hospital hospital;
                					for(int i=0; i<jArray.length(); i++){
                						hospital = new Hospital();
                						JSONObject aux = jArray.getJSONObject(i);
                						String nombre = aux.getString("nombre");
                						String lat  = aux.getString("lat");
                						String lng = aux.getString("lng");
                						String direccion = aux.getString("direccion");
                						String telefono = aux.getString("telefono");
                						int id = aux.getInt("id");
                						

                						hospital.setDireccion(direccion);
                						hospital.setId(id);
                						hospital.setNombre(nombre);
                						hospital.setTelefono(telefono);
                						hospital.setX(Float.valueOf(lat));
                						hospital.setY(Float.valueOf(lng));
                					    resultados.add(hospital);
                					    
                					}
            	    			}else{
            	    				codigo = ERROR_NO_EXISTE_COMUNA;
            	    			}
            	    			
            					
            	    		}catch(JSONException e){
            	    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
            	    			codigo = ERROR_JSON;
            	    		}
    					}
    				}
    			}
    		}
        res =  new RespuestaServicioWeb(resultados, codigo);
    	return res;
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

	public static Usuario verificaLogin(String user, String pass){
		Usuario usuario = new Usuario();
		String URL = "http://colvin.chillan.ubiobio.cl:8070/rhormaza/servicio_web_inicio_sesion.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	Integer codigo = OK_CONEXION;
    	RespuestaServicioWeb res;
    	String jsonReturnText="";
    	String respuesta;
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 10000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("telefono",user));
    			oPostParam.add(new BasicNameValuePair("password",pass));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			jsonReturnText = EntityUtils.toString(r_entity);
    			
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    			codigo = ERROR_CONEXION;
    			res =  new RespuestaServicioWeb(resultados, codigo);
    	    	return null;
    		}
    		try{
    			JSONObject json = new JSONObject(jsonReturnText);
    			usuario.setApellido(json.getString("apellido"));
    			usuario.setCorreo(json.getString("correo"));
    			usuario.setNombre(json.getString("nombre"));
    			usuario.setNumeroTelefono(json.getString("numero"));
    			Configuracion conf = new Configuracion();
    			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    			JSONObject confJSON = json.getJSONObject("configuracion");
    			try{
    				conf.setFechaModificacion(formato.parse(confJSON.getString("fecha_modificacion")));
    			}catch(ParseException e){
    				Log.e("emergenciAPPS","formato de fecha invalido: "+confJSON.getString("fecha_modificacion") , e);
    			}
    			
    			conf.setIdConfiguracion(confJSON.getInt("id_configuracion"));
    			conf.setMensajeAlerta(confJSON.getString("mensaje_alerta"));
    			conf.setNumeroBombero(confJSON.getString("numero_bombero"));
    			conf.setNumeroCarabinero(confJSON.getString("numero_carabinero"));
    			conf.setNumeroCentroMedico(confJSON.getString("numero_centro_medico"));
    			conf.setNumeroPDI(confJSON.getString("numero_pdi"));
    			conf.setRadioBusqueda(confJSON.getInt("radio_busqueda"));
    			usuario.setConfiguracion(conf);
    			JSONArray jArrayContactos = json.getJSONArray("contactos");
    			Contacto contacto;
    			List<Contacto> contactos = new ArrayList();
				for(int i=0; i<jArrayContactos.length(); i++){
					contacto = new Contacto();
					JSONObject aux = jArrayContactos.getJSONObject(i);
					String nombre = aux.getString("nombre");
					String numero  = aux.getString("numero");
					String correo = aux.getString("correo");
					Integer alertaSMS = aux.getInt("alerta_sms");
					Integer alertaGPS = aux.getInt("alerta_gps");
					Integer alertaCorreo = aux.getInt("alerta_correo");
					Integer idContacto = aux.getInt("id_contacto");
					
					contacto.setAlertaCorreo(alertaCorreo);
					contacto.setAlertaGPS(alertaGPS);
					contacto.setAlertaSMS(alertaSMS);
					contacto.setCorreo(correo);
					contacto.setNombre(nombre);
					contacto.setNumeroTelefono(numero);
					contacto.setIdContacto(idContacto);
					
				    contactos.add(contacto);
				    
				}
				usuario.setContactos(contactos);
				
    		}catch(JSONException e){
    			Log.e("emergenciAPPS", "Al obtener datos de json: "+jsonReturnText, e);
    			codigo = ERROR_JSON_GPS;
    			return null;
    			
    		}
 
		
		return usuario;
	}

}

 