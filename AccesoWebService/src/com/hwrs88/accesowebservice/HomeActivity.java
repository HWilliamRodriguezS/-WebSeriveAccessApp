package com.hwrs88.accesowebservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnTaskCompleted {

	Button search, add, modify, delete;

	// private final int MAIN_ACTIVITY = 0;
	private final int SEARCH_ACTIVITY = 1;
	private final int ADD_ACTIVITY = 2;
	private final int MODIFY_ACTIVITY = 3;
	private final int DELETE_ACTIVITY = 4;
	private final int PREFERENCES_ACTIVITY = 5;

	private ProgressDialog pDialog;
	private static final String url_query = "http://miw29.calamar.eui.upm.es/webservice/";
	private WSManager wsConection;
	private WSActions wsAction;
//	private View callerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// search = (Button) findViewById(R.id.search_button);
		// add = (Button) findViewById(R.id.add_button);
		// modify = (Button) findViewById(R.id.edit_button);
		// delete = (Button) findViewById(R.id.delete_button);
		// url_query =
		// "http://demo.calamar.eui.upm.es/webservice/webService.query.php";

		this.wsAction = WSActions.CONNECTION;
		this.callWS(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.configuration_settings:
			openPreferencessActivity((View) item);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void openPreferencessActivity(View v) {
		Log.w("", "View ID : " + v.getId());
		// this.wsAction = WSActions.SEARCH;
		callWS(v);
		
//		Intent i = new Intent(this, Prefs.class);
//		startActivityForResult(i, PREFERENCES_ACTIVITY);

	}

	public void openAddActivity(View v) {
		
		Log.w("", "View ID : " + v.getId());
		// this.wsAction = WSActions.SEARCH;
		callWS(v);
//		Intent i = new Intent(this, AddActivity.class);
//		startActivityForResult(i, ADD_ACTIVITY);

	}

	public void openModifyActivity(View v) {
		
		Log.w("", "View ID : " + v.getId());
		// this.wsAction = WSActions.SEARCH;
		callWS(v);
//		Intent i = new Intent(this, ModifyActivity.class);
//		startActivityForResult(i, MODIFY_ACTIVITY);

	}

	public void openSearchActivity(View v) {
		v.getId();
		Log.w("", "View ID : " + v.getId());
		// this.wsAction = WSActions.SEARCH;
		callWS(v);
		// Intent i = new Intent(this, SearchActivity.class);
		// startActivityForResult(i,SEARCH_ACTIVITY);

	}

	public void openDeleteActivity(View v) {

		Log.w("", "View ID : " + v.getId());
		// this.wsAction = WSActions.SEARCH;
		callWS(v);
		
//		Intent i = new Intent(this, DeleteActivity.class);
//		startActivityForResult(i, DELETE_ACTIVITY);

	}

	// Webservice Mannage Connection
	public void callWS(View view) {

		wsConection = new WSManager();
		wsConection.setListener(this);
		wsConection.setCurrentContext(HomeActivity.this);
		wsConection.setUrlQuery(url_query);
		wsConection.setProgressDialog(getString(R.string.progress_title));
		
		EditText etDni =  (EditText) findViewById(R.id.dni_search);
		String dni = etDni.getText().toString();
		
		if (view == null) {
			
			wsConection.setActionEnum(WSActions.CONNECTION);
			wsConection.setWsMethod("webService.connect.php");
			
		} else if (view.getId() == R.id.search_button) {

			this.wsAction = WSActions.SEARCH;
			wsConection.setActionEnum(WSActions.SEARCH);
			wsConection.setWsMethod("webService.query.php");

		} else if (view.getId() == R.id.add_button) {
			Log.w("","dni_noempty +'"+dni+"'");
			this.wsAction = WSActions.ADD;
			wsConection.setActionEnum(WSActions.SEARCH);
			wsConection.setWsMethod("webService.query.php");
			
			if(dni != null && dni.length() == 0){
				Log.w("","dni_noempty");
				Toast.makeText(HomeActivity.this, getString(R.string.dni_noempty), Toast.LENGTH_LONG).show();
				return;
			}
			
		} else if (view.getId() == R.id.edit_button) {

			this.wsAction = WSActions.UPDATE;
			wsConection.setActionEnum(WSActions.SEARCH);
			wsConection.setWsMethod("webService.query.php");

		} else if (view.getId() == R.id.delete_button) {

			this.wsAction = WSActions.DELETE;
			wsConection.setActionEnum(WSActions.SEARCH);
			wsConection.setWsMethod("webService.query.php");

		} else {
			Log.w("", "View ID : " + getString(view.getId()));
			wsConection.setActionEnum(WSActions.CONNECTION);
			wsConection.setWsMethod("webService.connect.php");
		}
		
		if(dni != null && dni.length() == 0){
			Log.w("","wsConection.execute()");
			wsConection.execute();
			
		}else{
			BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",dni.toString());
			wsConection.execute(nameValuePairs);	
		}
		
	}

	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub

		JSONArray jarrayResponse = wsConection.getRecordsResult();
		Intent i;
		JSONObject json_obj;
		String str_value = "";
		
		EditText etDni =  (EditText) findViewById(R.id.dni_search);
		String dni = etDni.getText().toString();
		
		switch (wsAction) {
		case CONNECTION:

			if (wsConection.isConnected()) {

				Toast.makeText(wsConection.getCurrentContext(),R.string.service_connected, Toast.LENGTH_LONG).show();
				Log.w("", "Array : " + jarrayResponse.length());

			} else {

				Toast.makeText(wsConection.getCurrentContext(),
						R.string.errorHTTP, Toast.LENGTH_LONG).show();
				openPreferencessActivity(null);

			}
			break;

		case SEARCH:
			
			
			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    
		    if(Integer.parseInt(str_value) == 0){
		    	Toast.makeText(HomeActivity.this, getString(R.string.query_noexist), Toast.LENGTH_LONG).show();
		    }else{
		    	i = new Intent(this, SearchActivity.class);
				i.putExtra("jarray",wsConection.getRecordsResult().toString());
				startActivityForResult(i, SEARCH_ACTIVITY);
		    }
			
			break;

		case ADD:

			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    if(Integer.parseInt(str_value) == 0){
		    	i = new Intent(this, AddActivity.class);
				i.putExtra("DNI",dni);
				startActivityForResult(i, ADD_ACTIVITY);
		    }else{
				
				Toast.makeText(HomeActivity.this, getString(R.string.query_exists), Toast.LENGTH_LONG).show();
		    }
			
			
			break;
		
		case UPDATE:
			
			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    
		    if(Integer.parseInt(str_value) == 0){
		    	Toast.makeText(HomeActivity.this, getString(R.string.query_noexist), Toast.LENGTH_LONG).show();
		    }else{
		    	i = new Intent(this, ModifyActivity.class);
				try {
					i.putExtra("json",wsConection.getRecordsResult().getJSONObject(1).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivityForResult(i, MODIFY_ACTIVITY);
		    }

			break;
		
		case DELETE:
			
			try {
				json_obj = wsConection.getRecordsResult().getJSONObject(0);
				str_value=json_obj.getString("NUMREG");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    
		    if(Integer.parseInt(str_value) == 0){
		    	Toast.makeText(HomeActivity.this, getString(R.string.query_noexist), Toast.LENGTH_LONG).show();
		    }else{
		    	i = new Intent(this, DeleteActivity.class);
				try {
					i.putExtra("json",wsConection.getRecordsResult().getJSONObject(1).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivityForResult(i, DELETE_ACTIVITY);
		    }
			
			

			break;	
	
		default:
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case SEARCH_ACTIVITY:
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					String resultMessage = extras.getString("resultMessage");
					Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();
				}
			}
			
			break;
		
		}
		
	}
	
}
