package com.example.controllers;

import com.example.monopoly.LoadingActivity;

import android.os.Handler;
import android.os.Message;

public class LoadThread extends Thread {
	
	Handle mHandler = new Handle();
	
	public void run(){
		//Loading code for all devices go here
		
		//Loading code for individual device types
		if (HostDevice.self){
			//loading code for host goes here
		} else {
			//loading code for player goes here
		}
		
		//ping the handler indicating the loading is done for the current device
		mHandler.obtainMessage().sendToTarget();
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
