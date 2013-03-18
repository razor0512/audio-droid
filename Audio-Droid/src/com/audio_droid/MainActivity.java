package com.audio_droid;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.spoledge.aacdecoder.AACPlayer;


public class MainActivity extends Activity {
	private static String logtag = "MainActivity";
	MediaPlayer mplayer;
	AACPlayer aacPlayer;
	String urlpath;
	Toast con;
	boolean is_connect = false;
	String lastinput="";
	String mode="rtsp://";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Button buttonStart = (Button)findViewById(R.id.buttonConnect);        
	     buttonStart.setOnClickListener(startListener); // Register the onClick listener with the implementation above
	       
	     Button buttonStop = (Button)findViewById(R.id.buttonDisconnect);        
	     buttonStop.setOnClickListener(stopListener); // Register the onClick listener with the implementation above
	     
*/		
		mplayer = new MediaPlayer();
		//aacPlayer = new AACPlayer();
		//urlpath = "rtsp://v2.cache7.c.youtube.com/CjYLENy73wIaLQnIH7D0dZO9IhMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYIaU5_fj_qyZUQw=/0/0/0/video.3gp";
		
		Toast toast = Toast.makeText(getApplicationContext(), "URL: " + urlpath, Toast.LENGTH_LONG);
	 	toast.show();
		setContentView(R.layout.activity_main);
	}
	
	/*//Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
          Log.d(logtag,"onClick() called - start button");              
          Toast.makeText(MainActivity.this, "The Connect button was clicked.", Toast.LENGTH_LONG).show();
          Log.d(logtag,"onClick() ended - start button");
        }
    };
     
    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
         Log.d(logtag,"onClick() called - stop button"); 
         Toast.makeText(MainActivity.this, "The Disconnect button was clicked.", Toast.LENGTH_LONG).show();
          Log.d(logtag,"onClick() ended - stop button");
        } 
    };*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
		 alert.setMessage("To use this app make sure your VLC server is up and running. You can choose either RTSP or HTTP in the mode button. Input the IP address by clicking the Connect button.");
		 alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
		 alert.show();
	 }
	 
	 public void connect(View view){
		    final Button connectButton = (Button) view;
		    if(!is_connect){
		    AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("audio-droid");
			alert.setMessage("Host URL");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			input.setText(lastinput);
		    input.setHint("Enter url");
			alert.setView(input);

			alert.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			
			  public void onClick(DialogInterface dialog, int whichButton) {
			  urlpath = input.getText().toString();
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
							}
								
				        }
				    };
				    h.postDelayed(checkplaying, 1000);
			  }
			  else
			  {
				  Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
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
	 /*@Override
	 protected void onStart() {//activity is started and visible to the user
	  Log.d(logtag,"onStart() called");
	  super.onStart();  
	 }
	 @Override
	 protected void onResume() {//activity was resumed and is visible again
	  Log.d(logtag,"onResume() called");
	  super.onResume();
	   
	 }
	 @Override
	 protected void onPause() { //device goes to sleep or another activity appears
	  Log.d(logtag,"onPause() called");//another activity is currently running (or user has pressed Home)
	  super.onPause();
	   
	 }
	 @Override
	 protected void onStop() { //the activity is not visible anymore
	  Log.d(logtag,"onStop() called");
	  super.onStop();
	   
	 }
	 @Override
	 protected void onDestroy() {//android has killed this activity
	   Log.d(logtag,"onDestroy() called");
	   super.onDestroy();
	 }*/


