package com.hwrs88.accesowebservice;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class DeleteActivity extends Activity implements OnTaskCompleted {
	
	private JSONObject json;
	
	private ProgressDialog pDialog;
	private static final String url_query = "http://miw29.calamar.eui.upm.es/webservice/";
	private WSManager wsConection;
	private WSActions wsAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		updateFields();
	}
	
	public void deleteRecord(View v){
		
		
		wsConection = new WSManager();
		wsConection.setListener(this);
		wsConection.setCurrentContext(DeleteActivity.this);
		wsConection.setUrlQuery(url_query);
		wsConection.setProgressDialog(getString(R.string.progress_title));
		
		EditText etDni =  (EditText) findViewById(R.id.dni);
		String dni = etDni.getText().toString();
		
		wsConection.setActionEnum(WSActions.DELETE);
		wsConection.setWsMethod("webService.delete.php");
		
		BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",dni.toString());
		wsConection.execute(nameValuePairs);
		
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

	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
		Log.w("","actualizado : " + wsConection.getRecordsResult().toString());
		
		JSONArray jarrayResponse = wsConection.getRecordsResult();
		Intent i;
		JSONObject json_obj;
		String str_value = "";
		
		switch (wsConection.getActionEnum()) {
		case DELETE:
			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    if(Integer.parseInt(str_value) >= 1){
		    	
				i = new Intent();
				i.putExtra("resultMessage", getString(R.string.query_deleted));
		   		setResult(RESULT_OK, i);
				super.onBackPressed();
//		    	Toast.makeText(ModifyActivity.this, getString(R.string.query_updated), Toast.LENGTH_LONG).show();
		    }else if(Integer.parseInt(str_value) == 0){
		    	i = new Intent();
				i.putExtra("resultMessage", getString(R.string.query_deleted));
		   		setResult(RESULT_OK, i);
				super.onBackPressed();
		    }else{
//		    	i = new Intent(this, SearchActivity.class);
//				i.putExtra("jarray",wsConection.getRecordsResult().toString());
//				startActivityForResult(i, SEARCH_ACTIVITY);
//		    	Toast.makeText(ModifyActivity.this,"Didnt work", Toast.LENGTH_LONG).show();
		    	Log.w("","Delete have gonne wrong...");
		    }
		    
		    break;
		default:
			break;
		
		}
		
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra("resultMessage", getString(R.string.query_deleted_cancel));
		setResult(RESULT_CANCELED,i);
		super.onBackPressed();
	}
}
