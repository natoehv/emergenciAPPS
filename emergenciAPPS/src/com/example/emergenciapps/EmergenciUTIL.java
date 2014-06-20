package com.example.emergenciapps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Clase con metodos de ayuda
 * @author Renato
 *
 */
public class EmergenciUTIL {
	
	public static Bitmap getRoundedCornerBitmap( Drawable drawable, boolean square) {
	     int width = 0;
	     int height = 0;
	     
	     Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap() ;
	     
	     if(square){
	      if(bitmap.getWidth() < bitmap.getHeight()){
	       width = bitmap.getWidth();
	       height = bitmap.getWidth();
	      } else {
	       width = bitmap.getHeight();
	          height = bitmap.getHeight();
	      }
	     } else {
	      height = bitmap.getHeight();
	      width = bitmap.getWidth();
	     }
	     
	        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);

	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, width, height);
	        final RectF rectF = new RectF(rect);
	        final float roundPx = 90; 

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	        return output;
	}
	/**
	 * Metodo encargado de cambiar tamaño imagen
	 * @param ctx

	 * @param w
	 * @param h
	 * @return Bitmap
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap resizeImage(Drawable drawable, int w, int h) {
		Bitmap BitmapOrg = ((BitmapDrawable) drawable).getBitmap() ;
        // cargamos la imagen de origen
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen 
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                                                   width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return resizedBitmap;

      }
}
