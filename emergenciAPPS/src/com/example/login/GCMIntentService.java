package com.example.login;

import com.example.emergenciapps.EmergenciAPPSActivity;
import com.example.emergenciapps.R;
import com.example.seguimiento.SeguimientoActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
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
            		this.mostrarNotification(extras.getString("msg"), extras.getString("nombre")+ " necesita ayuda!");
            		break;
            	}
            }
        }
        
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }
	
	private void mostrarNotification(String msg, String title){
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);	
		long[] pattern = new long[]{2000, 200, 2000};
		Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		NotificationCompat.Builder mBuilder = 
			new NotificationCompat.Builder(this)  
				.setSmallIcon(R.drawable.ic_launcher)  
				.setContentTitle(title)
				.setContentText(msg)
				.setTicker(title)
				.setVibrate(pattern)
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_MAX)
				.setSound(defaultSound);
		
		Intent notIntent =  new Intent(this, SeguimientoActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);   
		
		mBuilder.setContentIntent(contIntent);
		
		mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
}
