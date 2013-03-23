package com.audio_droid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UrlHistory extends Activity {

	private static final String PREFS = "mypref";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_url_history);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		final ListView listView = (ListView) findViewById(R.id.urlview);

		SharedPreferences urlprefs = getSharedPreferences(PREFS, 0);
		final String[] values = urlprefs.getString("urlprefs", "").split("@");


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);


		listView.setAdapter(adapter); 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			    {
				Intent resultData = new Intent();
				resultData.putExtra("selectedurl", values[position]);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			    }});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_url_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
	        case R.id.clearHistory:
				  SharedPreferences urlprefs = getSharedPreferences(PREFS, 0);
			      SharedPreferences.Editor editor = urlprefs.edit();
				  editor.remove("urlprefs");
			  	  editor.commit();
			  	  Toast.makeText(getApplicationContext(), "History Cleared" , Toast.LENGTH_LONG).show();
			  	  //final ListView listView = (ListView) findViewById(R.id.urlview);
			  	  finish();
			  	  return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    
		}
	}

}
