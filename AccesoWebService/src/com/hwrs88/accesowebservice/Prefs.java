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
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
	
	
	private void openHomeActivity(View view) {
		// TODO Auto-generated method stub
		
		
		
	}
	
	class TestConnection extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
		private ProgressDialog pDialog;
		private static final String url_query = "http://miw29.calamar.eui.upm.es/webservice/";
    	
    	private boolean connection = false;
    	
    	public boolean getConnection(){
    		
    		return connection;
    	}
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Prefs.this);
            pDialog.setMessage(getString(R.string.progress_title));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Obtaining info
         * */
        protected HttpResponse doInBackground(BasicNameValuePair... params) {
        	HttpResponse response = null;
        	
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
        	for(int i=0; i<params.length; i++){
        		nameValuePairs.add(params[i]);
        	}
       	
        	try{
    	        //HttpClient httpclient = new DefaultHttpClient();
    	        AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost httppost = new HttpPost(url_query+"webService.connect.php");
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        //httppost.setEntity(new StringEntity(content));
    	        response = httpclient.execute(httppost);
        	}catch(Exception e){
    	        Log.e(getString(R.string.app_name), R.string.errorHTTP+": "+e.toString());
        	}    	
	        return response;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(HttpResponse response) {
        	String message = "";
        	
            // dismiss the dialog once done
            pDialog.dismiss();
            
            int responseCode = response.getStatusLine().getStatusCode();
            String responseMessage = response.getStatusLine().getReasonPhrase();
            
	        HttpEntity entity = response.getEntity();
	        
            if (entity != null) {
            	//InputStream is = entity.getContent();
                String responseString;
				try {
					responseString = EntityUtils.toString(entity);
					message = responseString;
					
					JSONArray jarray = new JSONArray(message);
					
//					JSONObject json = new JSONObject(message);
//					Log.w("Json ",json.toString());
					
				    JSONObject json_obj = jarray.getJSONObject(0);
				    
				    String str_value=json_obj.getString("NUMREG");
				    
				    if(Integer.parseInt(str_value) >= 0){
				    	this.connection = true;
				    }else{
				    	this.connection = false;
				    }
				    
				} 
				catch (ParseException e) {}
				catch (IOException e) {} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
            	message = responseCode+": "+responseMessage;
            	this.connection = false;
            }
            
        	if(this.getConnection()){
        		
        		Toast.makeText(getApplicationContext(), R.string.service_connected , Toast.LENGTH_LONG).show();
        		
        	}else{
        		
        		Toast.makeText(getApplicationContext(), R.string.errorHTTP , Toast.LENGTH_LONG).show();
        		openHomeActivity(null);
        		
        	}
            
        }
 
    }
	

}
