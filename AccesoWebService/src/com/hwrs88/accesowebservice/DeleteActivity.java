package com.hwrs88.accesowebservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.R.color;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class DeleteActivity extends Activity {
	
	private JSONObject json;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		updateFields();
	}
	
	public void deleteRecord(){
		
		EditText etDNI = (EditText) findViewById(R.id.dni);
		String dni = etDNI.getText().toString();
		
	}

	public void updateFields() {

		try {
			json = new JSONObject(getIntent().getStringExtra("json"));

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
