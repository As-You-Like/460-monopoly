package com.example.monopoly;

import com.example.bluetooth.Bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashActivity extends Activity {
	
	public static SplashActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SplashActivity.activity = this;
		setContentView(R.layout.activity_splash);
		new Bluetooth(this.getApplicationContext());
		Intent intent = new Intent(SplashActivity.activity, WelcomeActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash, menu);
		return true;
	}

}
