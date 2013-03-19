package com.example.monopoly;

import com.example.bluetooth.Bluetooth;
import com.example.controllers.HostDevice;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LobbyActivity extends Activity {
	
	public static LobbyActivity activity = null;
	
	private Button btnBack = null;
	private Button btnStart = null;
	private ListView lstPlayers = null;
	private TextView txtGameName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby); 
		
		//this.btnBack    = (Button) this.findViewById(R.id.btnBack);
		this.btnStart = (Button) this.findViewById(R.id.btnStart);
		this.lstPlayers   = (ListView) this.findViewById(R.id.lstPlayers);
		this.txtGameName   = (TextView) this.findViewById(R.id.txtGameName);
		
		Bundle extras = this.getIntent().getExtras();
		
		this.txtGameName.setText("Game Name: " + extras.getString("gn"));
		
		Bluetooth.changeDeviceName(extras.getString("gn"));
		
		this.btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				
			}
			
		});
		
		
		if (HostDevice.self == true){
			//if the device is a host,
			this.ensureDiscoverable();
		} else {
			//if the device is a client
		}
	}
	
	private void ensureDiscoverable() {
       // if(D) Log.d(TAG, "ensure discoverable");
        if (Bluetooth.mAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            this.startActivity(discoverableIntent);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 

}
