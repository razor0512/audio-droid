package com.audio_droid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spoledge.aacdecoder.AACPlayer;


public class MainActivity extends Activity {
	private static String logtag = "MainActivity";
	MediaPlayer mplayer;
	Pattern ipPattern = Pattern.compile("\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?");   
	Pattern webPattern = Pattern.compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,65}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$");
	AACPlayer aacPlayer;
	String urlpath;
	Toast con;
	boolean is_connect = false;
	String lastinput="";
	String mode="rtsp://";
	int conAttempts = 0;
	private PowerManager.WakeLock wl;
	private static final String PREFS = "mypref";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);  
		mplayer = new MediaPlayer();;
		setContentView(R.layout.activity_main);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.urlhistory:
	        	Intent myIntent = new Intent(this, UrlHistory.class);
                startActivityForResult(myIntent, 0);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
			final Button connectButton = (Button) findViewById(R.id.buttonConnect);
	      if (requestCode == 0) {
	          if (resultCode == RESULT_OK) {
	            String url = data.getStringExtra("selectedurl");
	            	this.lastinput = url;
	            	this.urlpath = url;
	            	 try {
	     				mplayer.setDataSource(mode + urlpath);
	     				mplayer.setOnInfoListener(new MediaPlayer.OnInfoListener(){
	     					public boolean onInfo(MediaPlayer mplayer, int what, int extra) {
	     						return true;
	     					}
	     				});
	     				
	     				mplayer.prepare();
	     				mplayer.start();
	     				connectButton.setText("Disconnect");
	     				is_connect = true;
	     				
	     			  }
	     			  catch(Exception e){
	     				  
	     			  }
	            	 if(mplayer.isPlaying()){
	   				  Toast.makeText(getApplicationContext(), "Connected to: " + urlpath, Toast.LENGTH_LONG).show();
	   				  final Handler h = new Handler();
	   				  Runnable checkplaying = new Runnable()
	   				    {

	   				        @Override
	   				        public void run()
	   				        { 	
	   							if(mplayer.isPlaying())
	   								h.postDelayed(this, 2000);
	   							else {
	   					            Toast note = Toast.makeText(getApplicationContext(), "Host Disconnected", Toast.LENGTH_LONG);
	   								note.show();
	   							    is_connect = false;
	   							    connectButton.setText("Connect");
	   								mplayer.stop();
	   								mplayer.reset();
	   							}
	   								
	   				        }
	   				    };
	   				    h.postDelayed(checkplaying, 2000);
	   			  }
	   			  else
	   			  {
	   				  Toast.makeText(getApplicationContext(), "Could not connect to audio stream", Toast.LENGTH_LONG).show();
	   				  connectButton.setText("Connect");
	   				  mplayer.reset();
	   				  is_connect = false;
	   			  }
	            	
	          }
	      }
	}
	 public void playmusic(View view){
		 Context context = getApplicationContext();
		 CharSequence text = "Playing";
		 int duration = Toast.LENGTH_SHORT;
		 Toast toast = Toast.makeText(context, text + " " + urlpath, duration);
		 toast.show();
		 try {
		 mplayer.setDataSource(urlpath);
		 //AssetFileDescriptor afd = getAssets().openFd("Wake Up.mp3");
		 //mplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		 mplayer.prepare();
		 mplayer.start();
		 }
		 catch(Exception e){
		 }
		// aacPlayer.playAsync(urlpath);		 
	 }
	 
	 public void setMode(View view){
		 final Button modeButton = (Button) view;
		 Toast toast;
		 if(mode.compareTo("rtsp://") == 0){
			 modeButton.setText("Mode: HTTP");
			 mode = "http://";
			 toast = Toast.makeText(getApplicationContext(), "Mode changed to HTTP", 3);
			 
		 }
         else{
        	 modeButton.setText("Mode: RTSP");
        	 mode = "rtsp://";
        	 toast = Toast.makeText(getApplicationContext(), "Mode changed to RTSP", 3); 		 	
        	 
         }
		 toast.show();
	
	 }
	 
	 public void help(View view){
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);
		 alert.setTitle("audio-droid Help");
		 alert.setMessage("To use this app make sure your streaming server is up and running. This app is limited to RTSP connections only. Input the IP address by clicking the Connect button. Alternatively, you can select a url from your URL History in the options menu");
		 alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
		 alert.show();
	 }
	 
	 public void connect(View view){
//		  PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//          wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNjfdhotDimScreen");
//		 	wl.acquire();
		    final Button connectButton = (Button) view;
			conAttempts = 0;
		    if(!is_connect){
		    AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("audio-droid");
			alert.setMessage("Host URL");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			input.setText(lastinput);
		    input.setHint("e.g. 192.168.1.100:8554/stream");
			alert.setView(input);

			alert.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			
			  public void onClick(DialogInterface dialog, int whichButton) {
			  String inpt = input.getText().toString().split("/")[0];
			  Toast.makeText(getApplicationContext(), inpt , Toast.LENGTH_LONG).show();
			  Matcher matcher = ipPattern.matcher(input.getText().toString().split("/")[0]);
			  Matcher matcher2 = webPattern.matcher(input.getText().toString().split("/")[0]);
			  boolean ipcheck = matcher.find() && matcher.group().equals(inpt);
			  boolean webcheck = matcher2.find() && matcher2.group().equals(inpt);
			  if(!(ipcheck || webcheck)) {
				  Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_LONG).show();
				  return;
			  }
			  urlpath = input.getText().toString();
			  SharedPreferences urlprefs = getSharedPreferences(PREFS, 0);
		      SharedPreferences.Editor editor = urlprefs.edit();
			  if(!urlprefs.contains("urlprefs")) {
				  editor.putString("urlprefs", urlpath);
			  	  editor.commit();
			  }
			  else {
				  final Set<String> values = new HashSet<String>(Arrays.asList(
						  urlprefs.getString("urlprefs", "").split("@"))
					);
				  if(values.contains(urlpath) == false)
				  {
				  editor.putString("urlprefs", urlprefs.getString("urlprefs", "") + "@" + urlpath);
				  editor.commit();
				  }
			  }
			  
			  
			  lastinput = urlpath;
			  connectButton.setText("Disconnect");
				is_connect = true;
			  try {
				//mplayer.setDataSource("rtsp://192.168.1.100:8554/stream");
				mplayer.setDataSource(mode + urlpath);
				mplayer.setOnInfoListener(new MediaPlayer.OnInfoListener(){
					public boolean onInfo(MediaPlayer mplayer, int what, int extra) {
						return true;
					}
				}
				);
				mplayer.prepare();
				mplayer.start();
				
			  }
			  catch(Exception e){
				  
			  }
			  if(mplayer.isPlaying()){
				  Toast.makeText(getApplicationContext(), "Connected to: " + urlpath, Toast.LENGTH_LONG).show();
				  final Handler h = new Handler();
				  Runnable checkplaying = new Runnable()
				    {

				        @Override
				        public void run()
				        { 	
							if(mplayer.isPlaying())
								h.postDelayed(this, 2000);
							else {
					            Toast note = Toast.makeText(getApplicationContext(), "Host Disconnected", Toast.LENGTH_LONG);
								note.show();
							    is_connect = false;
							    connectButton.setText("Connect");
								mplayer.stop();
								mplayer.reset();
//								if(conAttempts <= 2) {
//								try {
//									//mplayer.setDataSource("rtsp://192.168.1.100:8554/stream");
//									mplayer.setDataSource(mode + urlpath);
//									mplayer.setOnInfoListener(new MediaPlayer.OnInfoListener(){
//										public boolean onInfo(MediaPlayer mplayer, int what, int extra) {
//											return true;
//										}
//									}
//									);
//									mplayer.prepare();
//									mplayer.start();
//									h.postDelayed(this, 2000);
//									conAttempts = 0;
//									
//								  }
//								  catch(Exception e){
//										h.postDelayed(this, 2000);
//										conAttempts += 1;
//								  }
//								}
							}
								
				        }
				    };
				    h.postDelayed(checkplaying, 2000);
			  }
			  else
			  {
				  Toast.makeText(getApplicationContext(), "Could not connect to audio stream", Toast.LENGTH_LONG).show();
				  connectButton.setText("Connect");
				  mplayer.reset();
				  is_connect = false;
			  }
			  // Do something with value!
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			alert.show();
		    }
		    
		    else{
		    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
		    	alert.setTitle("audio-droid");
				alert.setMessage("Do you really want to disconnect?");

				alert.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

			      is_connect = false;
			      connectButton.setText("Connect");
				  urlpath = null;
				  Toast disco = Toast.makeText(getApplicationContext(), "You have been disconnected", Toast.LENGTH_LONG);
				  disco.show();
				  mplayer.stop();
				  mplayer.reset();
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});
				alert.show();
		    }
		 }
	 
	 
	 }



