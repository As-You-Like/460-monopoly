package com.example.monopoly;

import java.util.ArrayList;

import com.example.bluetooth.Bluetooth;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;
import com.example.controllers.Device;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LobbyActivity extends Activity {
	
	public static LobbyActivity activity = null;
	
	ArrayAdapter<String> adapter;
	String[] playerList = new String[4];
	
	private Button btnBack = null;
	private Button btnStart = null;
	private ListView lstPlayers = null;
	private TextView txtGameName = null;
	private EditText pName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby); 
		
		activity = this;
		
		//this.btnBack    = (Button) this.findViewById(R.id.btnBack);
		this.btnStart = (Button) this.findViewById(R.id.btnStart);
		this.lstPlayers   = (ListView) this.findViewById(R.id.lstPlayers);
		this.txtGameName   = (TextView) this.findViewById(R.id.txtGameName);
		this.pName = (EditText) this.findViewById(R.id.playerName);
		
		adapter = new ArrayAdapter<String>(this.lstPlayers.getContext(), android.R.layout.simple_list_item_1, playerList);
		this.lstPlayers.setAdapter(adapter);
		this.updatePlayerList();
		
		Bundle extras = this.getIntent().getExtras();
		
		this.txtGameName.setText("Game Name: " + extras.getString("gn"));
		
		this.lstPlayers.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				
				//if target player exists, ping him
				if (Device.player[index] != null){
					Device.player[index].sendMessage(Device.MESSAGE_TYPE_CHAT, "This is just a ping");
				}
			}
		});
		
		
		this.btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//dummy test code
				if (HostDevice.self == true){
					//if the device is a host,
					Device.sendMessageToAllPlayers(Device.MESSAGE_TYPE_CHAT, "Host Chatting");
				} else {
					//if the device is a client, ask for name
					HostDevice.host.sendMessage(Device.MESSAGE_TYPE_CHAT, "User " + PlayerDevice.currentPlayer + " Chatting");
				}
				
				
			}
			
		});
		
		
		if (HostDevice.self == true){
			//if the device is a host,
			this.ensureDiscoverable();
		} else {
			//if the device is a client
			requestName();
			//name requests are done in Bluetooth.mHandler.handleMessage(msg) in the part that handles the receiving of the player number from the host
			
		}
	}
	
	public void onBackPressed(){
		super.onBackPressed();
		
		HostDevice.accept.listen = false; 
		HostDevice.accept.cancel();
	}
	
	public void updatePlayerList(){
		for (int i=0; i<this.playerList.length; i++){
			PlayerDevice p = Device.player[i];
			if (p != null){
				this.playerList[i] = p.name;
			} else {
				this.playerList[i] = "";
			}
		}
		this.adapter.notifyDataSetChanged();
	}
	
	public void requestName(){
		String name = this.activity_fillInName();
		Log.d("requestName()", name);
		if (HostDevice.host == null){
			Log.e("requestName()", "Host is not defined, unable to send message");
			return;
		}
		HostDevice.host.sendMessage(Device.MESSAGE_TYPE_SYSTEM, "nameRequest" + name);
	}
	
	/**
	 * Starts an activity for result in which the player types in their desired name
	 * @return
	 */
	private String activity_fillInName(){
		
		/*String newPlayer = "";
		
		AlertDialog.Builder builder= new AlertDialog.Builder(this);
		LayoutInflater inflater= getLayoutInflater();
		final View myView= inflater.inflate(R.layout.dialoglayout, null);
		builder.setTitle("Name");
		builder.setMessage("Enter your name.");
		builder.setView(myView);
		AlertDialog alert=builder.create();
		
		newPlayer = pName.getText().toString();
		
		
		return newPlayer;*/ //stub code
		
		return "Player " + Device.currentPlayer;
	}
	
	private void ensureDiscoverable() {
       // if(D) Log.d(TAG, "ensure discoverable");
        if (Bluetooth.mAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1800);
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
