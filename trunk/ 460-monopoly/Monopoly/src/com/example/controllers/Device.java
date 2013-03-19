package com.example.controllers;

import java.util.HashMap;

public abstract class Device {
	
	public static boolean self = false; //indiciates if the current device is the host
	
	//constants
	public static final String MESSAGE_COMPONENT_RECIEVER = "r";
	public static final String MESSAGE_COMPONENT_SENDER   = "s";
	public static final String MESSAGE_COMPONENT_TYPE     = "t";
	public static final String MESSAGE_COMPONENT_MESSAGE  = "m";
	
	public static final int MESSAGE_TYPE_SYSTEM = 0;
	public static final int MESSAGE_TYPE_CHAT = 1;
	
	//static reference to the host of the game
	//note: certain elements that the current player needs will be static in the HostDevice class.
	public static HostDevice host = null;
	
	//static references to the players in the game
	public static PlayerDevice[] player = new PlayerDevice[4];
	
	//temporary reference to hold the player object until the host sends the assigned player number
	public static PlayerDevice tmpCurrentPlayer = null;
	
	//static reference to the index of the current player for the array above when necessary
	//note: certain elements that the current player needs will be static in the PlayerDevice class.
	public static int currentPlayer = -1;
	
	public static int getCurrentDevice(){
		if (HostDevice.self){ //if host, return -1 (indicator of host)
			return -1;
		} else { //loop through players looking for "self" and return the corresponding index
			for (int i=0; i<Device.player.length; i++){
				if (Device.player[i].self){ 
					return i;
				}
			}
		}
		//return failure
		return -2;
	}
	
	//send message to target device
	public abstract void sendMessage(int type, String message);
	
	//send message to all devices except the current one
	public static void sendMessageToAll(int type, String message){
		if (!HostDevice.self){ //Don't send message to self if host
			HostDevice.host.sendMessage(type, message);
		}
		Device.sendMessageToAllPlayers(type, message);
	}
	
	//send message to all players (but not the host)
	public static void sendMessageToAllPlayers(int type, String message){
		for (int i=0; i<Device.player.length; i++){
			//don't send messages to self if you are a player
			//don't send messages if current player slot is empty
			if (Device.currentPlayer != i && Device.player[i] != null){ //Don't send message to self if player
			//Only send to play if the player is connected
			if (Device.player[i].playerConnectionStatus == PlayerDevice.CONNECTION_ACTIVE){
				Device.player[i].sendMessage(type, message);
			}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
