package com.example.monopoly;

import java.util.ArrayList;

import com.example.bluetooth.Bluetooth;
import com.example.controllers.Game;
import com.example.controllers.Player;
import com.example.model.Tile;
import com.example.model.Unit;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class SplashActivity extends Activity { 
	 
	public static SplashActivity activity;
	public boolean cascadeQuitBool;
	public static final int REQUEST_ENABLE_BT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Unit.entity = new ArrayList<Unit>();
		Tile.entity = new Tile[400][400];
		this.cascadeQuitBool = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		SplashActivity.activity = this;
		
		
		new Bluetooth(this.getApplicationContext());
		
		
		Intent intent = new Intent(SplashActivity.activity, WelcomeActivity.class);
		startActivity(intent); 
	}
	
	protected void onStart(){
		super.onStart();
		
		if (!Bluetooth.mAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
		case REQUEST_ENABLE_BT:
			if (resultCode != Activity.RESULT_OK) {
                // User did not enable Bluetooth or an error occured
                Toast.makeText(this, "Bluetooth is required, exiting", Toast.LENGTH_SHORT).show();
                finish();
            }
			break;
		}
	}
	
	public void onResume()
	{
		super.onResume();
		if(SplashActivity.activity.cascadeQuitBool == true){
			activity.terminate();
		}

	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}

}
