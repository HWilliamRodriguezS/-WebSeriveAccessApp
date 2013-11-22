package com.hwrs88.accesowebservice;

import android.os.Bundle;
import android.R.color;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

 public class AddActivity extends Activity {
	 
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add);
	        enableFields();
	    }
	 	
	 
	 public void enableFields() {
			
//          JSONObject json 
//			json = jarray.getJSONObject(currentRecord);				
//			String msg = getString(R.string.record) +" "+ currentRecord + " "  + getString(R.string.of) + " " + (jarray.length() - 1) ;
//			TextView tvFoundRecords = (TextView) findViewById(R.id.total_query_results);
//			tvFoundRecords.setText(msg );
//			tvFoundRecords.
	 		
	 		String dni = getIntent().getStringExtra("DNI");
			EditText etDNI = (EditText) findViewById(R.id.dni);
			etDNI.setText(dni.toString());
			etDNI.setBackgroundColor(color.darker_gray);
			
			EditText etName = (EditText) findViewById(R.id.name);
			etName.setFocusableInTouchMode(true);
			etName.setFocusable(true);

			EditText etlastName = (EditText) findViewById(R.id.last_name);
			etlastName.setFocusable(true);
			etlastName.setFocusableInTouchMode(true);

			EditText etAddress = (EditText) findViewById(R.id.address);
			etAddress.setFocusable(true);
			etAddress.setFocusableInTouchMode(true);
			
			EditText etTel = (EditText) findViewById(R.id.tel);
			etTel.setFocusable(true);
			etTel.setFocusableInTouchMode(true);
			
			EditText etTeam = (EditText) findViewById(R.id.team);
			etTeam.setFocusable(true);
			etTeam.setFocusableInTouchMode(true);
			
			
	}

	public void createRecord(){
		
		EditText etDNI = (EditText) findViewById(R.id.dni);
		
		EditText etName = (EditText) findViewById(R.id.name);
		
		EditText etlastName = (EditText) findViewById(R.id.last_name);
		
		EditText etAddress = (EditText) findViewById(R.id.address);
		
		EditText etTel = (EditText) findViewById(R.id.tel);
		
		EditText etTeam = (EditText) findViewById(R.id.team);
		
	}

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.home, menu);
	        return true;
	    }
	 
}
