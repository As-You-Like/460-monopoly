package com.example.androidtest;

import java.util.UUID;

import android.graphics.Color;
import android.widget.Toast;

public class Player {
	// Constants
	public static final int CONNECTION_NONE = 0;
	public static final int CONNECTION_LISTEN = 1;
	public static final int CONNECTION_DISCONNECTED = 2;
	public static final int CONNECTION_CONNECTING = 3;
	public static final int CONNECTION_CONNECTED = 4;
	
	// Player Settings
	public static int playerMax = 4;
	public static int playerCount = 0;
	
	public static Player[] entities = new Player[playerMax];
	public static int[] colors = {
		Color.rgb(255, 0, 0),   // red 
		Color.rgb(0, 0, 255),   // blue 
		Color.rgb(0, 255, 255), // cyan 
		Color.rgb(85, 00, 136), // purple 
		Color.rgb(255, 255, 0), // yellow 
		Color.rgb(255, 153, 0), // orange
		Color.rgb(0, 255, 0),   // green
		Color.rgb(238, 85, 187) // pink
		};
	public static int currentPlayer;
	public static int[] playerConnectionStatus = new int[playerMax];
	
	// Class Attributes
	public String name;
	public UUID uuid;
	public Color color;
	public Bluetooth.AcceptThread mAccept;
	public Bluetooth.ConnectThread mConnect;
	public Bluetooth.ConnectedThread mConnected;
	
	public void sendMessage(String message){
		byte[] buffer = message.getBytes();
		mConnected.write(buffer);
	}
	
	public void recieveMessage(String message){
		Toast.makeText(MainActivity.CONTEXT, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
