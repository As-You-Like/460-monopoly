package com.example.controllers;

import java.util.ArrayList;

import com.example.content.BluetoothListeners;
import com.example.content.BoardSetup;
import com.example.content.EventSetup;
import com.example.content.Image;
import com.example.model.Die;
import com.example.monopoly.LoadingActivity;
import com.example.monopoly.R;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LoadThread extends Thread { 
	
	Handle mHandler = new Handle();
	
	//constants for initial settings
	public static final double INITIAL_PLAYER_BALANCE = 2000;
	public static ArrayList<Integer> LIST_COLORS = new ArrayList<Integer>();
	public static ArrayList<String> LIST_COLORS_NAMES = new ArrayList<String>();
	
	public LoadThread(){
		LIST_COLORS.add(Color.rgb(255, 0, 0)); LIST_COLORS_NAMES.add("Red");
		LIST_COLORS.add(Color.rgb(0, 255, 0)); LIST_COLORS_NAMES.add("Green");
		LIST_COLORS.add(Color.rgb(0, 0, 255)); LIST_COLORS_NAMES.add("Blue");
		LIST_COLORS.add(Color.rgb(0, 255, 255)); LIST_COLORS_NAMES.add("Cyan");
	}
	
	public void run(){
		//Loading code for all devices go here
		
		//Loading code for individual device types
		if (HostDevice.self){
			//loading code for host goes here
			this.setupImages();
			this.setupPlayers();
			BluetoothListeners.setup();
			BoardSetup.setupBoard();
			EventSetup.setupEvents();
			//initialize the main game thread (it gets started later)
			new GameThread();
		} else {
			//loading code for player goes here
		}
		
		//ping the handler indicating the loading is done for the current device
		mHandler.obtainMessage().sendToTarget();
	}
	
	private void setupImages() {
		//b=BitmapFactory.decodeResource(getResources(), R.drawable.bruintest);
		Image.HEXAGON_TEXTURE = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_blue);
		Image.HEXAGON_BOTTOM = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_layer_bot);
		Image.HEXAGON_REGION = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_layer_rgn);
		Image.HEXAGON_PLAYER = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_layer_plr);
		
		Image.DIE[0] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_1);
		Image.DIE[1] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_2);
		Image.DIE[2] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_3);
		Image.DIE[3] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_4);
		Image.DIE[4] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_5);
		Image.DIE[5] = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.die_6);
	}

	private void setupPlayers() {
		Player.entities = new Player[Device.player.length];
		for (int i=0; i<Device.player.length; i++){
			if (Device.player[i] != null){
				int colorIndex = (int) (Math.random()*LIST_COLORS.size());
				int color = LIST_COLORS.get(colorIndex);
				LIST_COLORS.remove(colorIndex);
				Player.COLOR_NAMES[i] = LIST_COLORS_NAMES.get(colorIndex);
				LIST_COLORS_NAMES.remove(colorIndex);
				
				Player p = new Player(Device.player[i], i, color);
				p.setBalance(INITIAL_PLAYER_BALANCE);
				Player.entities[i] = p;
			}
		}
	}
	
	private static class Handle extends Handler {
		public void handleMessage(Message msg){
			if(HostDevice.self){
				LoadingActivity.hostReady = true;
				LoadingActivity.startGameModule();
				//Toast.makeText(this, "Host Finished Loading", Toast.LENGTH_SHORT).show();
			} else {
				HostDevice.host.sendMessage(com.example.bluetooth.Message.PLAYER_READY, "");
				//Toast.makeText(this, "Player Finished Loading", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
