package com.hwrs88.accesowebservice;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyActivity extends Activity implements OnTaskCompleted {

	JSONObject json = null;
	
	
	private ProgressDialog pDialog;
	private static  String url_query;// = "http://miw29.calamar.eui.upm.es/webservice/";
	private WSManager wsConection;
	private WSActions wsAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String server_url = prefs.getString("server_url", "");
		String server_user = prefs.getString("server_user","");
		String server_pass = prefs.getString("server_pass","");
		url_query = server_url;
		updateFields();
		
	}

	public void updateRecord(View v){
		
		
		
		wsConection = new WSManager();
		wsConection.setListener(this);
		wsConection.setCurrentContext(ModifyActivity.this);
		wsConection.setUrlQuery(this.url_query);
		wsConection.setProgressDialog(getString(R.string.progress_title));
		wsConection.setActionEnum(WSActions.UPDATE);
		wsConection.setWsMethod("webService.update.php");

		EditText etDNI = (EditText) findViewById(R.id.dni);
		BasicNameValuePair vdni = new BasicNameValuePair("DNI", etDNI.getText()
				.toString());

		EditText etName = (EditText) findViewById(R.id.name);
		BasicNameValuePair vname = new BasicNameValuePair("Nombre", etName
				.getText().toString());

		EditText etlastName = (EditText) findViewById(R.id.last_name);
		BasicNameValuePair vlastName = new BasicNameValuePair("Apellidos",
				etlastName.getText().toString());

		EditText etAddress = (EditText) findViewById(R.id.address);
		BasicNameValuePair vAddress = new BasicNameValuePair("Direccion",
				etAddress.getText().toString());

		EditText etTel = (EditText) findViewById(R.id.tel);
		BasicNameValuePair vTel = new BasicNameValuePair("Telefono", etTel
				.getText().toString());

		EditText etTeam = (EditText) findViewById(R.id.team);
		BasicNameValuePair vTeam = new BasicNameValuePair("Equipo", etTeam
				.getText().toString());

		BasicNameValuePair arrNameValuePairs[] = { vdni, vname, vlastName,
				vAddress, vTel, vTeam };
		
		Log.w("arrNameValuePairs",arrNameValuePairs.toString());
		
		wsConection.execute(arrNameValuePairs);

	}

	public void updateFields() {

		try {
			json = new JSONObject(getIntent().getStringExtra("json"));

			EditText etDNI = (EditText) findViewById(R.id.dni);
			etDNI.setText(json.getString("DNI"));
			etDNI.setBackgroundColor(color.darker_gray);

			EditText etName = (EditText) findViewById(R.id.name);
			etName.setText(json.getString("Nombre"));
			etName.setFocusableInTouchMode(true);
			etName.setFocusable(true);

			EditText etlastName = (EditText) findViewById(R.id.last_name);
			etlastName.setText(json.getString("Apellidos"));
			etlastName.setFocusable(true);
			etlastName.setFocusableInTouchMode(true);

			EditText etAddress = (EditText) findViewById(R.id.address);
			etAddress.setText(json.getString("Direccion"));
			etAddress.setFocusable(true);
			etAddress.setFocusableInTouchMode(true);

			EditText etTel = (EditText) findViewById(R.id.tel);
			etTel.setText(json.getString("Telefono"));
			etTel.setFocusable(true);
			etTel.setFocusableInTouchMode(true);

			EditText etTeam = (EditText) findViewById(R.id.team);
			etTeam.setText(json.getString("Equipo"));
			etTeam.setFocusable(true);
			etTeam.setFocusableInTouchMode(true);

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
		case UPDATE:
			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    if(Integer.parseInt(str_value) >= 1){
		    	
				i = new Intent();
				i.putExtra("resultMessage", getString(R.string.query_updated));
		   		setResult(RESULT_OK, i);
				super.onBackPressed();
//		    	Toast.makeText(ModifyActivity.this, getString(R.string.query_updated), Toast.LENGTH_LONG).show();
		    }else if(Integer.parseInt(str_value) == 0){
		    	i = new Intent();
				i.putExtra("resultMessage", getString(R.string.query_updated));
		   		setResult(RESULT_OK, i);
				super.onBackPressed();
		    }else{
//		    	i = new Intent(this, SearchActivity.class);
//				i.putExtra("jarray",wsConection.getRecordsResult().toString());
//				startActivityForResult(i, SEARCH_ACTIVITY);
//		    	Toast.makeText(ModifyActivity.this,"Didnt work", Toast.LENGTH_LONG).show();
		    	Log.w("","Update have gonne wrong...");
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
		i.putExtra("resultMessage", getString(R.string.query_updated_cancel));
		setResult(RESULT_CANCELED,i);
		super.onBackPressed();
	}
	

}
