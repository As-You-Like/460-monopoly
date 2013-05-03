package com.example.monopoly;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.Game;
import com.example.controllers.GameThread;
import com.example.controllers.HostDevice;
import com.example.controllers.LoadThread;
import com.example.monopoly.PanAndZoomListener.Anchor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
					//Toast.makeText(LoadingActivity.activity, "PLAYER_READY recieved, everybody is " + (done ? "done" : "not done"), Toast.LENGTH_SHORT).show();
					
					LoadingActivity.startGameModule();
				}
				
			});
			
		}
		
		else {
			int p = Device.currentPlayer;
			
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
			     public boolean typeValid(int type) {
			              return type == Message.ALL_READY;
			       }

			       @Override
			       public void processMessage(int sender, int reciever, String message) {
			    	   Intent intent = new Intent(LoadingActivity.activity, CommandCardActivity.class);
			    	   LoadingActivity.activity.startActivity(intent);
			    	  // Toast.makeText(activity, "Intent -> to CommandCardActivity", Toast.LENGTH_LONG)
						//.show();
			       }
			});

		}
		
		//Load everything for the game module
		LoadThread lt = new LoadThread();
		lt.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_loading, menu);
		return true;
	}
	
	public void onResume()
	{
		super.onResume();
		if(SplashActivity.activity.cascadeQuitBool == true){
			activity.terminate();
		}

	}
	
	//
	public static void startGameModule(){
		boolean playersPresent = false;
		for (int i = 0; i<Device.player.length; i++){
			if (Device.player[i] != null) playersPresent = true;
		}
		if((playersPresent == false || done == true) && hostReady == true){
			Device.sendMessageToAllPlayers(Message.ALL_READY, "");
			Game.start();
			Intent intent = new Intent(LoadingActivity.activity, MapActivity.class);
			LoadingActivity.activity.startActivity(intent);
			//Toast.makeText(activity, "Intent -> to MapActivity", Toast.LENGTH_LONG)
			//.show();
			
			
			
			
		}
	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}

}
