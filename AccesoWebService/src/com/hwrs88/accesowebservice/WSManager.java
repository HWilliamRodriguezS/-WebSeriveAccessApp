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
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WSManager extends AsyncTask<BasicNameValuePair, Void, HttpResponse> {
	
	private JSONArray recordsResult =  null;
	private boolean connected = false;
	private String wsMethod = null;
	private Context currentContext = null;
	private WSActions actionEnum = null;
	private ProgressDialog pDialog=null; 
	private String progressDialog = null;
	private String urlQuery = null;
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(currentContext);
        pDialog.setMessage(progressDialog);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
	}
	
	@Override
	protected HttpResponse doInBackground(BasicNameValuePair... params) {
		HttpResponse response = null;
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
    	for(int i=0; i<params.length; i++){
    		nameValuePairs.add(params[i]);
    	}
   	
    	try{
	        AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
	        HttpPost httppost = new HttpPost(urlQuery+"webService.connect.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        response = httpclient.execute(httppost);
    	}catch(Exception e){
	        Log.w("Error", R.string.errorHTTP+": "+e.toString());
    	}    	
        return response;
	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		super.onPostExecute(response);
		
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
				
//				JSONObject json = new JSONObject(message);
//				Log.w("Json ",json.toString());
				
			    JSONObject json_obj = jarray.getJSONObject(0);
			    
			    String str_value=json_obj.getString("NUMREG");
			    
			    if(Integer.parseInt(str_value) >= 0){
			    	this.connected = true;
			    }else{
			    	this.connected = false;
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
        	this.connected = false;
        }
        
    	if(this.isConnected()){
    		
    		Toast.makeText(currentContext, R.string.service_connected , Toast.LENGTH_LONG).show();
    		
    	}else{
    		
    		Toast.makeText(currentContext, R.string.errorHTTP , Toast.LENGTH_LONG).show();
    		
    	}
		
	}
	
	
	public ProgressDialog getpDialog() {
		return pDialog;
	}

	public void setpDialog(ProgressDialog pDialog) {
		this.pDialog = pDialog;
	}

	public String getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(String progressDialog) {
		this.progressDialog = progressDialog;
	}

	public String getUrlQuery() {
		return urlQuery;
	}

	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}

	public JSONArray getRecordsResult() {
		return recordsResult;
	}

	public void setRecordsResult(JSONArray recordsResult) {
		this.recordsResult = recordsResult;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connection) {
		this.connected = connection;
	}

	public String getWsMethod() {
		return wsMethod;
	}

	public void setWsMethod(String wsMethod) {
		this.wsMethod = wsMethod;
	}

	public Context getCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(Context currentContext) {
		this.currentContext = currentContext;
	}

	public WSActions getActionEnum() {
		return actionEnum;
	}

	public void setActionEnum(WSActions actionEnum) {
		this.actionEnum = actionEnum;
	}
		
}
