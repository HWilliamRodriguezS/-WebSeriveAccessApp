package com.hwrs88.accesowebservice;

import java.io.IOException;
import java.util.ArrayList;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Prefs extends PreferenceActivity  implements OnTaskCompleted{
	
	private ProgressDialog pDialog;
	private static String url_query ;
	private WSManager wsConection;
	private WSActions wsAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String server_url = prefs.getString("server_url", "");
		String server_user = prefs.getString("server_user","");
		String server_pass = prefs.getString("server_pass","");
		
//		BasicNameValuePair vUser = new BasicNameValuePair("Equipo", etTeam
//				.getText().toString());
//
//		BasicNameValuePair arrNameValuePairs[] = { vPass, vTeam };
		
//		Log.w("arrNameValuePairs",arrNameValuePairs.toString());
		url_query = server_url;
	}
	
	
	private void testConnection(View view) {
		// TODO Auto-generated method stub
		

		wsConection = new WSManager();
		wsConection.setListener(this);
		wsConection.setCurrentContext(Prefs.this);
		wsConection.setUrlQuery(this.url_query);
		wsConection.setProgressDialog(getString(R.string.progress_title));
		this.wsAction = WSActions.CONNECTION;
		wsConection.setActionEnum(WSActions.CONNECTION); 
		wsConection.setWsMethod("webService.connect.php");
		wsConection.execute();
		
	}
	
	
	
	@Override
	public void onBackPressed() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String server_url = prefs.getString("server_url", "");
		String server_user = prefs.getString("server_user","");
		String server_pass = prefs.getString("server_pass","");
		url_query = server_url;
		testConnection(null);
		
	}


	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
//Log.w("","actualizado : " + wsConection.getRecordsResult().toString());
		
		JSONArray jarrayResponse = wsConection.getRecordsResult();
		Intent i;
		JSONObject json_obj;
		String str_value = "";
		
		switch (wsConection.getActionEnum()) {
		case CONNECTION:
					    
		    if(wsConection.isConnected()){
		    	
				i = new Intent();
				i.putExtra("resultMessage", getString(R.string.service_connected));
		   		setResult(RESULT_OK, i);
				super.onBackPressed();
//		    	Toast.makeText(ModifyActivity.this, getString(R.string.query_updated), Toast.LENGTH_LONG).show();
		    }else {
		    	Toast.makeText(Prefs.this,getString(R.string.errorHTTP), Toast.LENGTH_LONG).show();
//		    	Log.w("","Connection have gonne wrong...");
		    }
		    
		    break;
		default:
			break;
		
		}
		
		
	}
	

}
