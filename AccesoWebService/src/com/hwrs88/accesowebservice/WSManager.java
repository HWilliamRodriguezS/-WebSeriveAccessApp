package com.hwrs88.accesowebservice;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
//import android.widget.Toast;

public class WSManager extends AsyncTask<BasicNameValuePair, Void, String> {
	
	private JSONArray recordsResult =  null;
	private boolean connected = false;
	private String wsMethod = null;
	private Context currentContext = null;
	private WSActions actionEnum = null;
	private ProgressDialog pDialog=null; 
	private String progressDialog = null;
	private String urlQuery = null;
	private OnTaskCompleted listener = null;
//	private String message = null;
	
	
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
	protected String doInBackground(BasicNameValuePair... params) {
		HttpResponse response = null;
		JSONObject jsonObj = new JSONObject();
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
    	for(int i=0; i<params.length; i++){
    		nameValuePairs.add(params[i]);
    		NameValuePair localv = params[i];
    		try {
				jsonObj.put( params[i].getName() , params[i].getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
   	
    	try{
    		
    		switch(actionEnum){
    		
    		case ADD:
    		case UPDATE:
    			Log.w("","JSONobj : " + jsonObj.toString());
    			HttpClient httpclient = new DefaultHttpClient();
    	        HttpPost httppost = new HttpPost(urlQuery+wsMethod);
    	        httppost.setEntity(new StringEntity(jsonObj.toString()));
    	        response = httpclient.execute(httppost);
    			break;
    		
    		default:
    			AndroidHttpClient androidHttpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost androidHttppost = new HttpPost(urlQuery+wsMethod);
    	        androidHttppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        response = androidHttpclient.execute(androidHttppost);
    	        androidHttpclient.close();
    			break;
    		
    		}
    		
	        
    	}catch(Exception e){
	        Log.w("Error", "Error connection"+": "+e.toString());
	        this.connected = false;
	        this.cancel(true);
	        
    	}    	
       // return response;
        
        int responseCode = response.getStatusLine().getStatusCode();
        String responseMessage = response.getStatusLine().getReasonPhrase();
        HttpEntity entity = response.getEntity();
        
        String responseString = null;
        
        
        try {
			responseString = EntityUtils.toString(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return responseString;
	}

	@Override
	protected void onPostExecute(String response) { //e(HttpResponse response)
		//super.onPostExecute(response);
		
		String message = "";
    	
        // dismiss the dialog once done
        pDialog.dismiss();
        
        if(response == null){
        	listener.onTaskCompleted();
        	return;
        }
        
        int responseCode = 0;
        String responseMessage = response;
        
//        int responseCode = response.getStatusLine().getStatusCode();
//        String responseMessage = response.getStatusLine().getReasonPhrase();
        
 //       HttpEntity entity = response.getEntity();
        
        if (response != null) {
        	//InputStream is = entity.getContent();
            String responseString;
			try {
				
				
				//responseString = EntityUtils.toString(entity);
				//message = responseString;
				message = responseMessage;
				
//				Log.w("","responseString : " + responseString);
				Log.w("","responseString : " + message);
				
				JSONArray jarray = new JSONArray(message);
				
			    JSONObject json_obj = jarray.getJSONObject(0);
//			    Log.w("Json ",json_obj.toString());
			    
			    String str_value=json_obj.getString("NUMREG");
			    
			    setRecordsResult(jarray);
			    
			    if(Integer.parseInt(str_value) >= 0){
			    	this.connected = true;
			    }else{
			    	this.connected = false;
			    }
			    
			} 
			catch (ParseException e) {}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	message = responseCode+": "+responseMessage;
        	this.connected = false;
        }
        
//    	if(this.isConnected()){
//    		
//   		Toast.makeText(currentContext, R.string.service_connected , Toast.LENGTH_LONG).show();
//    		
//    	}else{
//    		
//    		Toast.makeText(currentContext, R.string.errorHTTP , Toast.LENGTH_LONG).show();
//    		
//    	}
    	
    	listener.onTaskCompleted();
		
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

	public OnTaskCompleted getListener() {
		return listener;
	}

	public void setListener(OnTaskCompleted listener) {
		this.listener = listener;
	}
		
}
