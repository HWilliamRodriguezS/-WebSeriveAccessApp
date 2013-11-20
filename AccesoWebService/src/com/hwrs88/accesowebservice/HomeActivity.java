package com.hwrs88.accesowebservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

	Button search,add,modify,delete;
	
	private final int SEARCH_ACTIVITY  = 1;
	private final int ADD_ACTIVITY     = 2;
	private final int MODIFY_ACTIVITY  = 3;
	private final int DELETE_ACTIVITY  = 4;
	private final int PREFERENCES_ACTIVITY  = 5;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // search = (Button) findViewById(R.id.search_button);
        // add = (Button) findViewById(R.id.add_button);
        // modify = (Button) findViewById(R.id.edit_button);
        // delete = (Button) findViewById(R.id.delete_button);
        
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
    		Intent i = new Intent(this, Prefs.class);
       		startActivityForResult(i,PREFERENCES_ACTIVITY);
    		break;
    	}
    	
		return super.onOptionsItemSelected(item);
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
    
}
