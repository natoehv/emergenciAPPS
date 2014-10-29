package com.example.seguimiento;

import java.util.ArrayList;
import java.util.List;

import com.example.emergenciapps.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SeguimientoActivity extends Activity{
	List<String> myList;
	Spinner spinner;
	Button btn;
	private ArrayAdapter<String> myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguimiento);
		
		spinner = (Spinner)findViewById(R.id.spinner);
		btn =(Button)findViewById(R.id.button_ver);
		
		inititialize_list();
		
		myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList);
		spinner.setAdapter(myAdapter);
		
		
		
		
		
		
		
		
	}
	
	private void inititialize_list(){
		 myList = new ArrayList<String>();
		 myList.add("Sunday");
		 myList.add("Monday");
		 myList.add("Tuesday");
		 myList.add("Wednesday");
		 myList.add("Thursday");
		 myList.add("Friday");
		 myList.add("Saturday");
	}
	
	

}
