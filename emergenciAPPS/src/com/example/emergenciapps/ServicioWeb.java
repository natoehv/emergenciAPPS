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
    	String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicioweb.php";
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
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_busqueda.php";
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
	
	public static String sendNotification(String lat, String lng, String miNumero){
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/index.php?r=api/enviaAlerta";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	String respuesta;
    	String descripcion = "";
    	Log.d("estado_norificacion","enviando"+miNumero);
    	try{
    		List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
			oPostParam.add(new BasicNameValuePair("lat",lat));
			oPostParam.add(new BasicNameValuePair("lng",lng));
			oPostParam.add(new BasicNameValuePair("id_usuario",miNumero));
			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
			
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return "El mensaje no ha podido ser enviado";
    	}
    	
    	Log.d("respuesta",respuesta);
    	
		return respuesta;
		
	}

	public static Usuario verificaLogin(String user, String pass){
		Usuario usuario = new Usuario();
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_inicio_sesion.php";
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
    			usuario.setEstadoAlerta(json.getInt("estadoAlerta"));
    			
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
					String numeroTelefono = aux.getString("numero_telefono");
					String numero  = aux.getString("numero");
					String correo = aux.getString("correo");
					Integer alertaSMS = aux.getInt("alerta_sms");
					Integer alertaGPS = aux.getInt("alerta_gps");
					Integer alertaCorreo = aux.getInt("alerta_correo");
					Integer idContacto = aux.getInt("_id");
					
					contacto.setAlertaCorreo(alertaCorreo);
					contacto.setAlertaGPS(alertaGPS);
					contacto.setAlertaSMS(alertaSMS);
					contacto.setCorreo(correo);
					contacto.setNombre(nombre);
					contacto.setNumeroTelefono(numeroTelefono);
					contacto.setNumero(numero);
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

	public static boolean actualizaConfiguracion(Configuracion conf, String idUsuario){
		boolean guardar = false;
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_configuracion.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	Integer codigo = OK_CONEXION;
    	RespuestaServicioWeb res;
    	String respuesta = "error";
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 10000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("numero_telefono",idUsuario));
    			oPostParam.add(new BasicNameValuePair("numero_carabinero",conf.getNumeroCarabinero()));
    			oPostParam.add(new BasicNameValuePair("numero_bombero",conf.getNumeroBombero()));
    			oPostParam.add(new BasicNameValuePair("numero_centro_medico",conf.getNumeroCentroMedico()));
    			oPostParam.add(new BasicNameValuePair("mensaje_alerta",conf.getMensajeAlerta()));
    			oPostParam.add(new BasicNameValuePair("radio_busqueda",conf.getRadioBusqueda().toString()));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			respuesta = EntityUtils.toString(r_entity);
    			Log.d("emergenciAPPS", "Respuesta Servidor "+respuesta);
    			if(respuesta.equals("true"))
    				return true;
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    			codigo = ERROR_CONEXION;
    			res =  new RespuestaServicioWeb(resultados, codigo);
    	    	return false;
    		}
    			
		return guardar;
	}
	
	public static boolean ingresaContacto(Contacto contacto, Usuario usuario){
		
		boolean guardar = false;
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_administrar_contacto.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	Integer codigo = OK_CONEXION;
    	RespuestaServicioWeb res;
    	String respuesta = "error";
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 10000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("id_contacto",""+contacto.getIdContacto()));
    		    oPostParam.add(new BasicNameValuePair("numero_telefono",contacto.getNumeroTelefono()));
    			oPostParam.add(new BasicNameValuePair("nombre",contacto.getNombre()));
    			oPostParam.add(new BasicNameValuePair("numero",contacto.getNumero()));
    			oPostParam.add(new BasicNameValuePair("correo",contacto.getCorreo()));
    			oPostParam.add(new BasicNameValuePair("alerta_sms",""+contacto.getAlertaSMS()));
    			oPostParam.add(new BasicNameValuePair("alerta_gps",""+contacto.getAlertaGPS()));
    			oPostParam.add(new BasicNameValuePair("alerta_correo",""+contacto.getAlertaCorreo()));
    			oPostParam.add(new BasicNameValuePair("usuario_nombre",""+usuario.getNombre()));
    			oPostParam.add(new BasicNameValuePair("usuario_apellido",""+usuario.getApellido()));
    			oPostParam.add(new BasicNameValuePair("usuario_correo",""+usuario.getCorreo()));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			respuesta = EntityUtils.toString(r_entity);
    			Log.d("emergenciAPPS", "Respuesta Servidor "+respuesta);
    			if(respuesta.equals("true"))
    				return true;
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    			codigo = ERROR_CONEXION;
    			res =  new RespuestaServicioWeb(resultados, codigo);
    	    	return false;
    		}
    			
		return guardar;
	}
	
	public static boolean actualizaContacto(Contacto contacto){
			
			boolean guardar = false;
			String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_administrar_contacto.php";
	    	HttpParams httpParameters = new BasicHttpParams();
	    	Integer codigo = OK_CONEXION;
	    	RespuestaServicioWeb res;
	    	String respuesta = "error";
	    	List resultados = new ArrayList();
	    	int timeoutConnection = 10000;
	    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    	int timeoutSocket = 10000;
	    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    	HttpPost oPost = new HttpPost(URL);
	    	
	    		try{
	    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
	    			oPostParam.add(new BasicNameValuePair("id_contacto",""+contacto.getIdContacto()));
	    			oPostParam.add(new BasicNameValuePair("nombre",contacto.getNombre()));
	    			oPostParam.add(new BasicNameValuePair("numero_telefono",contacto.getNumeroTelefono()));
	    			oPostParam.add(new BasicNameValuePair("nombre",contacto.getNombre()));
	    			oPostParam.add(new BasicNameValuePair("estado",""+contacto.getEstado()));
	    			oPostParam.add(new BasicNameValuePair("numero",contacto.getNumero()));
	    			oPostParam.add(new BasicNameValuePair("correo",contacto.getCorreo()));
	    			oPostParam.add(new BasicNameValuePair("alerta_sms",""+contacto.getAlertaSMS()));
	    			oPostParam.add(new BasicNameValuePair("alerta_gps",""+contacto.getAlertaGPS()));
	    			oPostParam.add(new BasicNameValuePair("alerta_correo",""+contacto.getAlertaCorreo()));
	    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
	    			HttpResponse oResp = httpclient.execute(oPost);
	    			HttpEntity r_entity = oResp.getEntity();
	    			respuesta = EntityUtils.toString(r_entity);
	    			Log.d("emergenciAPPS", "Respuesta Servidor "+respuesta);
	    			if(respuesta.equals("true"))
	    				return true;
	    		}catch(Exception e){
	    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
	    			codigo = ERROR_CONEXION;
	    	    	return false;
	    		}
	    			
			return guardar;
		}
	
	public static boolean eliminaContacto(int id_contacto) {
		boolean guardar = false;
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/servicio_web_administrar_contacto.php";
		HttpParams httpParameters = new BasicHttpParams();
		Integer codigo = OK_CONEXION;
		RespuestaServicioWeb res;
		String respuesta = "error";
		List resultados = new ArrayList();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost oPost = new HttpPost(URL);
		
			try{
				List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
				oPostParam.add(new BasicNameValuePair("id_contacto",""+id_contacto));
				
				oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
				HttpResponse oResp = httpclient.execute(oPost);
				HttpEntity r_entity = oResp.getEntity();
				respuesta = EntityUtils.toString(r_entity);
				Log.d("emergenciAPPS", "Respuesta Servidor "+respuesta);
				
				if(respuesta.equals("true"))
					return true;
			}catch(Exception e){
				Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
				codigo = ERROR_CONEXION;
				res =  new RespuestaServicioWeb(resultados, codigo);
		    	return false;
			}
				
		return guardar;
	}

	public static boolean registraGCMEnServidor(String user, String id){
		boolean guardar = false;
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/protected/views/ws/registro_cliente_gcm.php";
    	HttpParams httpParameters = new BasicHttpParams();
    	Integer codigo = OK_CONEXION;
    	RespuestaServicioWeb res;
    	String respuesta = "error";
    	List resultados = new ArrayList();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 10000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	
    		try{
    			List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    			oPostParam.add(new BasicNameValuePair("id_contacto",user));
    			oPostParam.add(new BasicNameValuePair("regid",id));
    			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
    			HttpResponse oResp = httpclient.execute(oPost);
    			HttpEntity r_entity = oResp.getEntity();
    			respuesta = EntityUtils.toString(r_entity);
    			Log.d("emergenciAPPS", "Respuesta Servidor "+respuesta);
    			if(respuesta.equals("true"))
    				return true;
    		}catch(Exception e){
    			Log.e("emergenciAPPS", "Error al llamar datos desde servicio web: "+URL, e);
    			codigo = ERROR_CONEXION;
    	    	return false;
    		}
    			
		return guardar;
	}

	/**
	 * Metodo encargado de actualizar mi ubicacion por el servicio en la Base de Datos
	 * @param lat
	 * @param lng
	 * @param miNumero
	 * @return
	 */
	public static String actualizaPosicion(String lat, String lng, String miNumero){
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/index.php?r=api/actualizaPosicion";
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
			oPostParam.add(new BasicNameValuePair("id_usuario",miNumero));
			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
			
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return "El mensaje no ha podido ser enviado";
    	}
    	
    	Log.e("respuesta",respuesta);
    	
		return descripcion;
		
	}
	
	public static List<Usuario> getUserInAlert(String miNumero){
		
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/index.php?r=api/alertas";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	String respuesta;
    	List<Usuario> usuarios = new ArrayList<Usuario>();
    	
    	try{
    		List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
			oPostParam.add(new BasicNameValuePair("id_usuario",miNumero));
			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
		    Log.d("Respuesta servicio web",respuesta);
		    JSONArray json = new JSONArray(respuesta);
		    
		    
			for(int i=0; i<json.length(); i++){
				Usuario usuario = new Usuario();
				JSONObject aux = json.getJSONObject(i);
				String nombre = aux.getString("nombre");
				String numero = aux.getString("numero_telefono");
				String lat = aux.getString("lat");
				String lng = aux.getString("lng");
				usuario.setNombre(nombre);
				usuario.setNumeroTelefono(numero);
				usuario.setLat(Float.parseFloat(lat));
				usuario.setLng(Float.parseFloat(lng));
				usuarios.add(usuario);
			}
		    
			
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return null;
    		
    	}
    	
    	
		return usuarios;
	}
	
	

	public static Boolean eliminarRegId(String miNumero) {
		// TODO Auto-generated method stub
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/index.php?r=api/eliminarRegId";
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
			oPostParam.add(new BasicNameValuePair("id_usuario",miNumero));
			
			oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
		    String valor = respuesta.trim();
		    Log.e("respuesta",valor);
		    if(valor.equals("true"))
				return true;
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return false;
    		
    	}
		return false;
	}

	public static List<Contacto> getContactos(String miNumero) {
		String URL = "http://parra.chillan.ubiobio.cl:8070/rhormaza/index.php?r=api/getContactos";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 30000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
    	HttpPost oPost = new HttpPost(URL);
    	String respuesta;
    	
    	List<Contacto> contactos = new ArrayList<Contacto>();
    	try{
    		List<NameValuePair> oPostParam = new ArrayList<NameValuePair>(2);
    		oPostParam.add(new BasicNameValuePair("id_usuario",miNumero));
    		oPost.setEntity(new UrlEncodedFormEntity(oPostParam));
			HttpResponse oResp = httpclient.execute(oPost);
			HttpEntity r_entity = oResp.getEntity();
		    respuesta = EntityUtils.toString(r_entity);
		    JSONArray json = new JSONArray(respuesta);
		    
		    for(int i=0; i<json.length(); i++){
				Contacto contacto = new Contacto();
				String nombre = "";
				JSONObject aux = json.getJSONObject(i);
				int id = aux.getInt("id_contacto");
				String nombre_decode = aux.getString("nombre");
				try{
					nombre = new String(nombre_decode.getBytes("ISO-8859-1"), "UTF-8");
				}catch (java.io.UnsupportedEncodingException e) {
		           Log.d("DECODE_UTFO", "No se pudo decodificar nombre");
		        }
				String numero_telefono = aux.getString("numero_telefono");
				String numero = aux.getString("numero");
				String correo = aux.getString("correo");
				int estado = aux.getInt("estado");
				int alerta_sms = aux.getInt("alerta_sms");
				int alerta_correo = aux.getInt("alerta_correo");
				int alerta_gps = aux.getInt("alerta_gps");
				contacto.setIdContacto(id);
				contacto.setNombre(nombre);
				contacto.setNumeroTelefono(numero_telefono);
				contacto.setNumero(numero);
				contacto.setCorreo(correo);
				contacto.setEstado(estado);
				contacto.setAlertaSMS(alerta_sms);
				contacto.setAlertaCorreo(alerta_correo);
				contacto.setAlertaGPS(alerta_gps);
				contactos.add(contacto);
				
			}
		    
		    
    	}catch(Exception e){
    		Log.e("emergenciAPPS", "Error: "+URL, e);
    		return null;
    		
    	}
    	return contactos;
	}
}

 