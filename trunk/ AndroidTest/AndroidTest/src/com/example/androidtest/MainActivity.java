package com.example.androidtest;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	
	public static Context CONTEXT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Bluetooth(this.getApplicationContext());
		
		CONTEXT = this.getApplicationContext();
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Bluetooth.mAdapter != null){
			if (!Bluetooth.mAdapter.isEnabled()) {
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, 2);
			} else {
				message();
			}
		}
	}
	
	public void message(){
		String tablet = "";
		String phone = "";
		String currentAddress = Bluetooth.mAdapter.getAddress();
		
		if (currentAddress.equals(tablet)){
			Player.currentPlayer = 0;
			Player p = new Player(); //create server player
			Player.entities[0] = p;
			Bluetooth.mBluetooth.start();
		}
		
		if (currentAddress.equals(phone)){
			Player.currentPlayer = 1;
			Player p = new Player();
			Player.entities[1] = p;
			BluetoothDevice device = Bluetooth.mAdapter.getRemoteDevice(tablet);
			Bluetooth.mBluetooth.connect(device);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu); 
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
		case 2:
			message();
			break;
		}
	}
}
