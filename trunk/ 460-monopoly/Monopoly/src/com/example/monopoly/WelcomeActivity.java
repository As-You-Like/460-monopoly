package com.example.monopoly;

import com.example.bluetooth.Bluetooth;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class WelcomeActivity extends Activity {
	
	public static WelcomeActivity activity;
	
	private Button btnHost = null;
	private Button btnPlayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome); 
		
		new Bluetooth(this.getApplicationContext());
		
		// Setup this activity as needed
		WelcomeActivity.activity = this;
		this.btnHost   = (Button) this.findViewById(R.id.btnHost);
		this.btnPlayer = (Button) this.findViewById(R.id.btnPlayer);
		
		this.btnHost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.activity, CreateHostActivity.class);
				startActivity(intent);
			}
			
		});
		
		this.btnPlayer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.activity, FindHostActivity.class);
				startActivity(intent);
			}
			
		});
		
		
		// Send player to first menu screen
		//Intent intent = new Intent(this, WebActivity.class);
		//startActivity(intent);
	}

}
