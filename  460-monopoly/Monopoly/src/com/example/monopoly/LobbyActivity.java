package com.example.monopoly;

import java.util.ArrayList;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LobbyActivity extends Activity {
	
	public static LobbyActivity activity = null;
	
	ArrayAdapter<String> adapter;
	String[] playerList = new String[4];
	
	private Button btnStart = null;
	private Button btnResetDiscovery = null;
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
		this.btnResetDiscovery = (Button) this.findViewById(R.id.btnResetDiscovery);
		this.lstPlayers   = (ListView) this.findViewById(R.id.lstPlayers);
		this.txtGameName   = (TextView) this.findViewById(R.id.txtGameName);
		this.pName = (EditText) this.findViewById(R.id.playerName);
		
		adapter = new ArrayAdapter<String>(this.lstPlayers.getContext(), android.R.layout.simple_list_item_1, playerList);
		this.lstPlayers.setAdapter(adapter);
		this.updatePlayerList();
		
		Bundle extras = this.getIntent().getExtras();
		
		this.txtGameName.setText("Game Name: " + extras.getString("gn"));
		
		// Register CHAT event
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
			public boolean typeValid(int type) {
				return type == Message.CHAT;
			}

			public void processMessage(int sender, int reciever, String message) {
				if (HostDevice.self == true){
					if (reciever != -1){
						//device is a host, must relay the message to the appropriate player
						Device.player[reciever].sendMessage(Message.CHAT, message, sender);
					} else {
						//message was directed to host
						String player = Device.player[sender].name + " (player " + sender + ")";
						Toast.makeText(LobbyActivity.activity, "Message from " + player + ": " + message, Toast.LENGTH_LONG).show();
					}
				} else {
					//device is a player, the player has received a message addressed to the current player
					String player = sender == -1 ? "host" : Device.player[sender].name + " (player " + sender + ")";
					if (sender != -1 && sender == Device.currentPlayer){
						player = "yourself";
					}
					Toast.makeText(LobbyActivity.activity, "Message from " + player + ": " + message, Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		this.lstPlayers.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				
				//if target player exists, ping him
				if (Device.player[index] != null){
					Device.player[index].sendMessage(Message.CHAT, "This is just a ping");
				}
			}
		});
		
		
		this.btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//dummy test code
				if (HostDevice.self == true){
					//if the device is a host,
					Device.sendMessageToAllPlayers(Message.CHAT, "Host Chatting");
				} else {
					//if the device is a client, ask for name
					HostDevice.host.sendMessage(Message.CHAT, "User " + PlayerDevice.currentPlayer + " Chatting");
				}
				
				
			}
			
		});
		
		this.btnResetDiscovery.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				LobbyActivity.activity.ensureDiscoverable(false);
			}
			
		});
		
		
		if (HostDevice.self == true){
			//if the device is a host,
			this.btnStart.setVisibility(View.VISIBLE);
			this.btnResetDiscovery.setVisibility(View.VISIBLE);
			
			//register NAME_REQUEST event
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
				public boolean typeValid(int type) {
					return type == Message.NAME_REQUEST;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					if (reciever == -1 && message.substring(0, 11).equals("nameRequest")){
						int playerNum = sender;
						String playerName = message.substring(11);
						
						//check if name is available
						String error = PlayerDevice.isNameAvailable(playerName);
						
						if (error == null){
							//update data
							PlayerDevice.player[playerNum].name = playerName;
							
							//update UI
							LobbyActivity.activity.updatePlayerList();
							
							//send message to player indicating name is accepted
							Log.d("System Message", "Name Accepted: " + playerName + " for Player " + playerNum);
							//PlayerDevice.player[playerNum].sendMessage(Device.MESSAGE_TYPE_SYSTEM, "nameRequestAccepted" + playerName);
							Device.sendMessageToAllPlayers(Message.NAME_REQUEST_ACCEPTED, "nameRequestAccepted" + playerNum + playerName);
						} else {
							//send message to player indicating name is rejected
							Log.e("System Message", "Name Rejected: " + playerName + " for Player " + playerNum);
							PlayerDevice.player[playerNum].sendMessage(Message.NAME_REQUEST_REJECTED, "nameRequestRejected" + error);
						}
					}
				}
				
			});
			
			this.ensureDiscoverable(true);
		} else {
			//if the device is a client
			this.btnStart.setVisibility(View.INVISIBLE);
			this.btnResetDiscovery.setVisibility(View.INVISIBLE);
			
			
			
			//register NAME_REQUEST_ACCEPTED event
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
				public boolean typeValid(int type) {
					return type == Message.NAME_REQUEST_ACCEPTED;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					int playerNum = Integer.parseInt(message.substring(19, 20));
					String name = message.substring(20);
					//Display accepted message
					Log.d("Name Request", "Name Accepted: " + name);
					
					if (PlayerDevice.player[playerNum] != null){
						PlayerDevice.player[playerNum].name = name;
					} else {
						Log.e("nameRequestAccepted", "Player " + playerNum + " does not exist");
					}
					//change name
					//if (reciever == Device.currentPlayer){
					//	PlayerDevice.player[reciever].name = name;
					//} else {
					//	Log.e("PlayerRequest", reciever + " != " + Device.currentPlayer);
					//}
					
					//update lobby UI
					LobbyActivity.activity.updatePlayerList();
				}
				
			});
			
			//register NAME_REQUEST_REJECTED event
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
				public boolean typeValid(int type) {
					return type == Message.NAME_REQUEST_REJECTED;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					//Display error message
					String error = message.substring(19);
					Log.d("Name Request", "Name Rejected: " + error);
					
					//ask for name again
					LobbyActivity.activity.requestName();
				}
				
			});
			
			//register LOBBY_NEW_PLAYER event
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
				public boolean typeValid(int type) {
					return type == Message.LOBBY_NEW_PLAYER;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					int playerNum = Integer.parseInt(message.substring(11,12));
					String name = message.substring(12);
					
					PlayerDevice p = new PlayerDevice(false, playerNum);
					p.name = name;
					
					if (LobbyActivity.activity != null){
						LobbyActivity.activity.updatePlayerList();
					}
				}
				
			});
			
			requestName();
			//name requests are done in Bluetooth.mHandler.handleMessage(msg) in the part that handles the receiving of the player number from the host
			
		}
	}
	
	public void onBackPressed(){
		super.onBackPressed();
		if (HostDevice.self == true){
			
			//stop listening for incoming connections
			HostDevice.host.listenStop();
			
			//flush out all existing players
			for (int i=0; i<Device.player.length; i++){
				if (Device.player[i] != null){
					Device.player[i].active.cancel();
					Device.player[i] = null;
				}
			}
		} else {
			HostDevice.active.cancel(); //simply kill the connection to the host if you are the player leaving
		}
		
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
		HostDevice.host.sendMessage(Message.NAME_REQUEST, "nameRequest" + name);
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
	
	private void ensureDiscoverable(boolean checkStatus) {
       // if(D) Log.d(TAG, "ensure discoverable");
        if (Bluetooth.mAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE || checkStatus == false) {
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
