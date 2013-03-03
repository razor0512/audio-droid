package com.audio_droid;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.spoledge.aacdecoder.AACPlayer;


public class MainActivity extends Activity {
	MediaPlayer mplayer;
	AACPlayer aacPlayer;
	String urlpath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mplayer = new MediaPlayer();
		//aacPlayer = new AACPlayer();
		//urlpath = "rtsp://v2.cache7.c.youtube.com/CjYLENy73wIaLQnIH7D0dZO9IhMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYIaU5_fj_qyZUQw=/0/0/0/video.3gp";
		urlpath = "rtsp://192.168.1.100:8554/stream";
		Toast toast = Toast.makeText(getApplicationContext(), "URL: " + urlpath, Toast.LENGTH_LONG);
	 	toast.show();
		setContentView(R.layout.activity_main);
	}

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

}
