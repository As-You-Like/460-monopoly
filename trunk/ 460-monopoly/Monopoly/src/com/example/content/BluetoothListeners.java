package com.example.content;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Trade;

public class BluetoothListeners {
	
	
	public static void setup(){
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_START;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				int p2 = Integer.parseInt(message);
				Trade.entity = new Trade(p2);
				
				
			}
			
		});
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
			return type == Message.TRADE_CHANGE_DETAILS;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				Trade.entity.changeDetails(sender, message);
				
			}
			
		});
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_ACCEPT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				Trade.entity.acceptTrade(sender);
				
			}
			
		});
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_REJECT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				Trade.entity.rejectTrade(sender);
				
			}
			
		});
		

	}
}
