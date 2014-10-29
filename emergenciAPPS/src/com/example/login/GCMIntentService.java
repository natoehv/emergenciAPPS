package com.example.login;

import com.example.emergenciapps.EmergenciAPPSActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends IntentService 
{
	private static final int NOTIF_CONFIGURACION = 0;
	private static final int NOTIF_CONTACTOS = 1;
	private static final int NOTIF_CUENTA = 2;
	private static final int NOTIF_SEGUIMIENTO = 3;
	
	private static final int NOTIF_ALERTA_ID = 1;

	public GCMIntentService() {
        super("GCMIntentService");
    }
	
	@Override
    protected void onHandleIntent(Intent intent) 
	{
		Log.d("NOTIFICACION_SEGUIMIENTO","NOTIFICACION EN INICIO");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        
        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) 
        {  
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
            {
            	//mostrarNotification(extras.getString("msg"));
            	Log.d("NOTIFICACION_SEGUIMIENTO","Valor opcion: "+extras.getString("opcion"));
            	switch(Integer.parseInt(extras.getString("opcion"))){
            	case NOTIF_CONFIGURACION: 
            		/*
            		 * TODO cargar json en las preferencias 
            		 */
            		SharedPreferences prefs = getSharedPreferences("miCuenta", this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    
            		
            		break;
            	case NOTIF_CONTACTOS:
            		break;
            	case NOTIF_CUENTA:
            		break;
            	case NOTIF_SEGUIMIENTO:
            		Log.d("NOTIFICACION_SEGUIMIENTO","NOTIFICACION EN PROCESO");
            		break;
            	}
            }
        }
        
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }
	
	private void mostrarNotification(String msg){
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);	
		NotificationCompat.Builder mBuilder = 
			new NotificationCompat.Builder(this)  
				.setSmallIcon(android.R.drawable.stat_sys_warning)  
				.setContentTitle("Notificación GCM")  
				.setContentText(msg);
		
		Intent notIntent =  new Intent(this, EmergenciAPPSActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);   
		
		mBuilder.setContentIntent(contIntent);
		
		mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
}
