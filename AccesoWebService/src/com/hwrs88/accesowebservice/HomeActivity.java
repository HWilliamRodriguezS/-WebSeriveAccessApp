package com.hwrs88.accesowebservice;

import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.Toast;

public class HomeActivity extends Activity {

	Button search,add,modify,delete;
	
//	private final int MAIN_ACTIVITY  = 0;
	private final int SEARCH_ACTIVITY  = 1;
	private final int ADD_ACTIVITY     = 2;
	private final int MODIFY_ACTIVITY  = 3;
	private final int DELETE_ACTIVITY  = 4;
	private final int PREFERENCES_ACTIVITY  = 5;
	
	private ProgressDialog pDialog;
	private static final String url_query = "http://miw29.calamar.eui.upm.es/webservice/";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // search = (Button) findViewById(R.id.search_button);
        // add = (Button) findViewById(R.id.add_button);
        // modify = (Button) findViewById(R.id.edit_button);
        // delete = (Button) findViewById(R.id.delete_button);
       // url_query = "http://demo.calamar.eui.upm.es/webservice/webService.query.php";
        this.conectar(null);
        
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
    	
    	switch(item.getItemId()){
    	case R.id.configuration_settings :
    		openPreferencessActivity((View) item);
    		break;
    	}
    	
		return super.onOptionsItemSelected(item);
	}
    
    public void openPreferencessActivity(View v){
    	Intent i = new Intent(this, Prefs.class);
   		startActivityForResult(i,PREFERENCES_ACTIVITY);
    	
    }


	public void openAddActivity(View v){
    	
    	Intent i = new Intent(this, AddActivity.class);
   		startActivityForResult(i,ADD_ACTIVITY);
    	
    }
    
    public void openModifyActivity(View v){
    	
    	Intent i = new Intent(this, ModifyActivity.class);
   		startActivityForResult(i,MODIFY_ACTIVITY);
    	
    }
    
    public void openSearchActivity(View v){
    	
    	Intent i = new Intent(this, SearchActivity.class);
   		startActivityForResult(i,SEARCH_ACTIVITY);
    	
    }
    
    public void openDeleteActivity(View v){
    	
    	Intent i = new Intent(this, DeleteActivity.class);
   		startActivityForResult(i,DELETE_ACTIVITY);
    	
    }
   
    //Webservice Mannage Connection
    
    public void conectar (View view){
    	// env’o de informaci—n v’a par‡metros
    	BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI","04864868");
    	// env’o de informaci—n v’a entidad
    	// String content = "[{\"DNI\":\"04864868\"}]";
    	QueryDNI connection = new QueryDNI();
//    	connetion.set
//    	connection.execute(nameValuePairs);
    	
    	for(int i = 0;connection.getStatus() != AsyncTask.Status.FINISHED;i++){
    		
    		try {
				Thread.sleep(1000);
				
				if(i >= 120){
					new TimeoutException("Timeout HomeActivity");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
    	
//    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
    
    /**
     * Background Async Task to Query 
     * */
    class QueryDNI extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
    	
    	private JSONArray recordsResult =  null;
    	private boolean connection = false;
    	private String wsMethod = null;
    	private Context currentContext;
    	
    	
    	public boolean getConnection(){
    		
    		return connection;
    	}
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
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
        		openPreferencessActivity(null);
        		
        	}
            
        }
 
    }
    
    
}
