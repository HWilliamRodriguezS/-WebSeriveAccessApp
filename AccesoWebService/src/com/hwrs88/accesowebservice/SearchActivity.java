package com.hwrs88.accesowebservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

public class SearchActivity extends Activity {

	private JSONArray jarray;
	private int currentRecord = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		try {
			JSONArray jarray = new JSONArray(getIntent().getStringExtra(
					"jarray"));
//			Toast.makeText(SearchActivity.this, jarray.toString(),Toast.LENGTH_LONG).show();
			this.jarray = jarray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentRecord = 1;
		updateFields();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void updateFields() {
		
		JSONObject json ;

		try {
			json = jarray.getJSONObject(currentRecord);
			
			String msg = getString(R.string.record) +" "+ currentRecord + " "  + getString(R.string.of) + " " + (jarray.length() - 1) ;
			TextView tvFoundRecords = (TextView) findViewById(R.id.total_query_results);
			tvFoundRecords.setText(msg );
//			tvFoundRecords.
			
			EditText etDNI = (EditText) findViewById(R.id.dni);
			etDNI.setText(json.getString("DNI"));
			
			EditText etName = (EditText) findViewById(R.id.name);
			etName.setText(json.getString("Nombre"));
			
			EditText etlastName = (EditText) findViewById(R.id.last_name);
			etlastName.setText(json.getString("Apellidos"));
			
			EditText etAddress = (EditText) findViewById(R.id.address);
			etAddress.setText(json.getString("Direccion"));
			
			EditText etTel = (EditText) findViewById(R.id.tel);
			etTel.setText(json.getString("Telefono"));
			
			EditText etTeam = (EditText) findViewById(R.id.team);
			etTeam.setText(json.getString("Equipo"));
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void lastRecord(View v){
		currentRecord = jarray.length() - 1;
		updateFields();
	}
	
	public void firstRecord(View v){
		
		currentRecord =1;
		updateFields();
	}
	
	public void nextRecord(View v){
		if(currentRecord < (jarray.length() - 1) )
		currentRecord++;
		updateFields();
	}
	
	public void previousRecord(View v){
		if(currentRecord > 1) 
		currentRecord--;
		updateFields();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra("resultMessage", getString(R.string.query_finished));
   		setResult(RESULT_OK, i);
		super.onBackPressed();
	}
	
	
}

