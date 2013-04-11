package com.example.controllers;

import java.util.ArrayList;

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
	public static final double INITIAL_PLAYER_BALANCE = 200;
	public static ArrayList<Integer> LIST_COLORS = new ArrayList<Integer>();
	
	public LoadThread(){
		LIST_COLORS.add(Color.rgb(255, 0, 0));
		LIST_COLORS.add(Color.rgb(0, 255, 0));
		LIST_COLORS.add(Color.rgb(0, 0, 255));
		LIST_COLORS.add(Color.rgb(0, 255, 255));
	}
	
	public void run(){
		//Loading code for all devices go here
		
		//Loading code for individual device types
		if (HostDevice.self){
			//loading code for host goes here
			this.setupImages();
			this.setupPlayers();
			BoardSetup.setupBoard();
			this.registerEvents();
			//initialize the main game thread (it gets started later)
			new GameThread();
			new Game("");
		} else {
			//loading code for player goes here
		}
		
		//ping the handler indicating the loading is done for the current device
		mHandler.obtainMessage().sendToTarget();
	}
	
	private void setupImages() {
		//b=BitmapFactory.decodeResource(getResources(), R.drawable.bruintest);
		Image.HEXAGON_TEXTURE = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_blue);
		Image.HEXAGON_REGION = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_darkblue);
		Image.HEXAGON_PLAYER = BitmapFactory.decodeResource(LoadingActivity.activity.getResources(), R.drawable.hexagon_orange);
	}

	private void setupPlayers() {
		Player.entities = new Player[Device.player.length];
		for (int i=0; i<Device.player.length; i++){
			if (Device.player[i] != null){
				int colorIndex = (int) (Math.random()*LIST_COLORS.size());
				int color = LIST_COLORS.get(colorIndex);
				LIST_COLORS.remove(colorIndex);
				
				Player p = new Player(Device.player[i], i, color);
				p.setBalance(INITIAL_PLAYER_BALANCE);
				Player.entities[i] = p;
			}
		}
	}

	private void registerEvents(){
		
		//Random Event - Being yelled at by the dean
		EventGenerator.registerEvent("residential", new RandomEvent(){
			public void action() {
				Log.e("RandomEvent", "YOU SUCK!");
			}
		});
		
		//Triggered Event - Being put to jail for rolling a double once
		EventGenerator.registerEvent("gameRules", new TriggeredEvent(){
			@Override
			public boolean condition() {
				return Die.isDouble();
			}
			
			public void action() {
				Log.e("TriggeredEvent", "You have been sent to jail");
			}

			
		});
		
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
