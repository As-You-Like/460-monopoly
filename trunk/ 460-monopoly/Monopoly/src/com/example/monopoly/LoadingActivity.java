package com.example.monopoly;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.HostDevice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class LoadingActivity extends Activity {
	
	public static LoadingActivity activity = null;
	public boolean playersReady[];
	public static boolean hostReady = false;
	public static boolean done = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		activity = this;
		playersReady = new boolean[Device.player.length];
		
		//Bluetooth messaging
		if(HostDevice.self){
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

				@Override
				public boolean typeValid(int type) {
					return type == Message.PLAYER_READY;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					playersReady[sender] = true;
					done = true;
					for(int i = 0; i > playersReady.length; i++){
						if(playersReady[i] == false && Device.player[i] != null){
							done = false;
						}
					}
					
					LoadingActivity.startGameModule();
				}
				
			});
			
		}
		
		else {
			int p = Device.currentPlayer;
			HostDevice.host.sendMessage(Message.PLAYER_READY, "");
			
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
			     public boolean typeValid(int type) {
			              return type == Message.ALL_READY;
			       }

			       @Override
			       public void processMessage(int sender, int reciever, String message) {
			    	   //Intent intent = new Intent(LoadingActivity.activity, CommandCardActivity.class);
			           //startActivity(intent);
			    	   Toast.makeText(activity, "Intent -> to CommandCardActivity", Toast.LENGTH_LONG)
						.show();
			       }
			});

		}
		
		/*Load everything needed for game module
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*/
		
		//Loading has finished. Informing Host of completion.
		if(HostDevice.self){
			LoadingActivity.startGameModule();
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_loading, menu);
		return true;
	}
	
	//
	public static void startGameModule(){
		if(done == true && hostReady == true){
			Device.sendMessageToAllPlayers(Message.ALL_READY, "");
			//Intent intent = new Intent(LoadingActivity.activity, MapActivity.class);
			//startActivity(intent);
			Toast.makeText(activity, "Intent -> to MapActivity", Toast.LENGTH_LONG)
			.show();
		}
	}

}
