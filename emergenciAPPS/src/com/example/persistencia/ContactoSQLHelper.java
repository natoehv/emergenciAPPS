package com.example.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class ContactoSQLHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "bdcontacto.db";
	private static final int DATABASE_VERSION = 1;
	
	public ContactoSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table contacto (id_contacto interger primary key, numero_telefono text, nombre text, numero text, correo text, estado interger, alerta_sms interger, alerta_gps interger, alerta_correo interger)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
