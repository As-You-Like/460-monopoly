package com.example.monopoly;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class DecisionActivity extends Activity {
	HostDevice hostDevice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision);
		hostDevice =new HostDevice(false);
	}
	
	public void clickEventLeft(View v){
		hostDevice.sendMessage(Message.RECEIVE_FORK_PATH, 0 + "");
	}
	public void clickEventRight(View v){
		hostDevice.sendMessage(Message.RECEIVE_FORK_PATH, 1 + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_decision, menu);
		return true;
	}

}
