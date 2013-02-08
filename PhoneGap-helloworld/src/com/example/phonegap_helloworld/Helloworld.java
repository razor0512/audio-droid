package com.example.phonegap_helloworld;

import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;



public class Helloworld extends DroidGap {

    @Override
    //protected void onCreate(Bundle savedInstanceState) {
      	//super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_helloworld);
    //}
    
    public void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState); 
    	super.loadUrl("file:///android_asset/www/index.html"); 
    	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_helloworld, menu);
        return true;
    }
    
}
