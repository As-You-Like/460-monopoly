package com.example.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.controllers.*;
import com.example.monopoly.*;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Bluetooth abstraction layer that manages connections between the host and multiple player devices
 * This class works in conjunction with Device HostDevice and PlayerDevice classes
 * @author VEBER_DMIT
 *
 */
public class Bluetooth {  
	// Variables
	public static String mGameName = "Monopoly";
	public static String originalDeviceName = "";
	
	private static ArrayList<BluetoothEvent> bluetoothEvent = new ArrayList<BluetoothEvent>();
	
	public static Handle mHandler;
	public static BluetoothAdapter mAdapter;
	public static Bluetooth entity;
	public static String NAME = "Monopoly";
	public static Context context;
	
	// Constants
	public static final int MESSAGE_RECIEVE = 0;
	public static final int MESSAGE_ERROR = -1;
	
	public static final int USERTYPE_SERVER = 0;
	public static final int USERTYPE_CLIENT = 1;
	
	//private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final UUID MY_UUID = UUID.fromString("0f1e9a70-9030-11e2-9e96-0800200c9a66");
	
	
	
	// Constructor
	public Bluetooth(Context context){
		Bluetooth.mAdapter = BluetoothAdapter.getDefaultAdapter();
		Bluetooth.originalDeviceName = Bluetooth.mAdapter.getName();
		
		if (mAdapter == null){
			Toast.makeText(context, "Your device does not support Bluetooth, you cannot play this game", Toast.LENGTH_LONG)
			.show();
		}
		
		Bluetooth.mHandler = new Bluetooth.Handle();
		Bluetooth.entity = this;
		Bluetooth.context = context;
	} // End Constructor
	
	public static void registerBluetoothEvent(BluetoothEvent event){
		Bluetooth.bluetoothEvent.add(event);
	}
	/**
	 * Changes the name of the device on the discovery stage
	 * Includes the name of the game, so that this app on other devices can distinguish
	 * devices that are hosting an instance of this game
	 * @param name
	 */
	public static void changeDeviceName(String name){
		Bluetooth.mAdapter.setName("[" + Bluetooth.mGameName + "]" + name);
    }
	
	// Methods
	/**
     * Start AcceptThread to begin a session in listening (server) mode.
	 */
	public synchronized void start(){
		
		
	} //End start()
	
	/**
	 * Stops the AcceptThread and ends the listening session of the server
	 */
	public synchronized void stop(){
		
	}
	
	
	/**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
	public void write(byte[] out){
		
	} //End write()
	
	// Event Callbacks
	/**
     * Indicate course of action if a connection attempt fails
     */
	private void connectionFailed(){
		
	} //End connectionFailed()
	
	/**
     * Indicate course of action if an existing connection is lost
     */
	private void connectionLost(){
		
	} //End connectionLost()
	
	/**
     * Start device discover with the BluetoothAdapter
     */
    public static void startDiscovery() {
        // If we're already discovering, stop it
        if (Bluetooth.mAdapter.isDiscovering()) {
        	Bluetooth.mAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        Bluetooth.mAdapter.startDiscovery();
    }
    
    /**
     * End device discover with the BluetoothAdapter
     */
    public static void endDiscovery(){
    	// Make sure we're not doing discovery anymore
        if (Bluetooth.mAdapter != null) {
            Bluetooth.mAdapter.cancelDiscovery();
        }
    }
    
    /**
     * Makes sure the device is discoverable
     */
    
	
	// Inner Classes
	public class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		public boolean listen = true;
		
		public AcceptThread(){
			BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
            	
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                Log.d("Log1", "Succesfully created listener");
            } catch (Exception e) { 
                //Toast.makeText(context, "AcceptThread failed to begin listening", Toast.LENGTH_SHORT).show();
                Log.e("Log1", "AcceptThread failed to begin listening"); 
                
                e.printStackTrace();
            }
            mmServerSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("AcceptThread");
			BluetoothSocket socket = null;
			
			// Listen to the server socket if still listening
			Log.e("5", "before while");
			while(listen){
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e){
					Log.e("1", e.getMessage());
					break; 
				}
				
				if (socket != null){
					Log.e("5", socket.getRemoteDevice().getName()); 
					synchronized (Bluetooth.this){
						HostDevice.activate(socket, socket.getRemoteDevice()); 
						
						//if the number of connected players is already at its limit, stop listening
						
					}
				}
			}
			Log.e("5", "after while");
			
		} //End run()
		
		public void cancel(){
			try {
				Bluetooth.changeDeviceName(Bluetooth.originalDeviceName);
				listen = false;
				mmServerSocket.close();
			} catch (IOException e) {
				Toast.makeText(context, "AcceptThread failed to close socket", Toast.LENGTH_SHORT).show();
			}
		} //End cancel()
	} //End class AcceptThread
	
	public class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
		
		public ConnectThread(BluetoothDevice device){
			this.mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                
            	//Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                //tmp = (BluetoothSocket) m.invoke(device, 1);
            } catch (IOException e) {
            	Log.e("2", e.getMessage());
            } 
            mmSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("ConnectThread");
			
			mAdapter.cancelDiscovery();
			
			// Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	Log.e("1", "1");
                mmSocket.connect();
                Log.e("1", "2");
            } catch (IOException e) {
                connectionFailed();
                Log.e("1", e.getMessage());
                
                // Close the socket
                try {
                    mmSocket.close();
                    Log.e("1", "4");
                } catch (IOException e2) {
                	Log.e("1", "5");
                }
                // Start the service over to restart listening mode
                Log.e("1", "6");
                Bluetooth.this.start();
                Log.e("1", "7");
                return;
            }
            
            //Reset the ConnectThread because we're done
            synchronized (Bluetooth.this) {
            	HostDevice.connect = null;
            }

            // Start the active connection thread
            PlayerDevice.activate(mmSocket, mmDevice);
		} //End run()
		
		public void cancel(){
			try {
                mmSocket.close();
            } catch (IOException e) {
            	Toast.makeText(context, "ConnectThread unable to close socket on cancel", Toast.LENGTH_SHORT).show();
            }
		} //End cancel()
	} //End class ConnectThread
	
	public class ActiveThread extends Thread {
		public final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ActiveThread(BluetoothSocket socket){
			mmSocket = socket;
			
			InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            	Log.e("ActiveThread", "Failed to make temp sockets, exception: " + e.getMessage());
                connectionFailed();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
		} //End constructor
		
		public void run(){
			byte[] buffer = new byte[1024*100];
            int bytes;

            // Keep listening to the InputStream while connected
          
			int totalBytes = 0;
            
			StringBuilder builder = new StringBuilder();
			while(true){
				try {
					// Read from the InputStream
	            	do {
						bytes = mmInStream.read(buffer,0,1008);
						String readMessage = new String(buffer, 0, bytes);
						builder.append(readMessage);
						totalBytes+=bytes;
					} while (bytes==1008);
	            	
	                // Send the obtained bytes to the UI Activity
	            	if (builder.length() > 0){ 
	            		
	            		String jsonString = builder.toString() + "{";
	            		Log.e("Master Json", jsonString);
	            		
	            		
	            		int index = -1;
	            		int prevIndex;
	            		//do while loop to loop through jammed messages if they are present.
	            		do {
	            			//get next json
	            			prevIndex = index;
	            			index = jsonString.indexOf("}{", prevIndex+1);
	            			if (index != -1){
		            			String json = jsonString.substring(prevIndex+1, index+1);
		            			
		            			Log.e("json", json);
		            			JSONObject obj = null;
			            		try {
									obj = new JSONObject(json);
									obj.put("JSON", json);
								} catch (JSONException e) {
									Log.e("AcceptThread", "JSON IS NULL");
									obj = null;
								}
			            		if (obj != null){
			            			
			            			Message msg = Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_RECIEVE, obj);
			            			if (HostDevice.self == true){
			            				msg.arg1 = 0; //identify which playerdevice has the current activethread
			            				for (int i=0; i<Device.player.length; i++){
			            					if (Device.player[i] != null){
			            						if(Device.player[i].active !=null){
				            						if (Device.player[i].active.getId() == this.getId()){
				            							Log.d("getId()", "i = " + i);
				            							msg.arg1 = i;
				            						}
			            						}
			            					}
			            				}
			            			} else {
			            				msg.arg1 = -1; //All messages are recieved from the host only
			            			}
			            			
			            			msg.sendToTarget();
			            		}
	            			}
	            		} while(index != -1);
	            	}
	            	
	                builder.delete(0, builder.length());
	                
				} catch (IOException e){
					//Toast.makeText(context, "ConnectedThread player " + this.playerNum + " has disconnected", Toast.LENGTH_SHORT).show();
					Log.e("ActiveThread", "A player has disconnected, exception: " + e.getMessage());
					
					Message msg = Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_ERROR, "playerDisconnect");
					if (HostDevice.self == true){
        				msg.arg1 = 0; //identify which playerdevice has the current activethread
        				for (int i=0; i<Device.player.length; i++){
        					if (Device.player[i] != null){
        						if (Device.player[i].active.getId() == this.getId()){
        							Log.d("getId()", "i = " + i);
        							Device.player[i].playerConnectionStatus = PlayerDevice.CONNECTION_DISCONNECTED;
        							msg.arg1 = i;
        						}
        					}
        				}
        			} else {
        				msg.arg1 = -1; //All messages are recieved from the host only
        			}
					
					msg.sendToTarget();
					connectionLost();
					break;
				}
			}
			if (totalBytes == 0){
				// The stream was never used
			}
		} //End run()
		
		public void write(byte[] buffer){
			try {
                mmOutStream.write(buffer);
                mmOutStream.flush();
            } catch (IOException e) {
            	Log.e("ActiveThread", "failed to write: " + e.getMessage());
            }
		} //End write()
		
		public void cancel(){
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e("ActiveThread", "failed to close socket: " + e.getMessage());
			}
		} //End cancel()
	} //End class ConnectedThread
	
	private static class Handle extends Handler {
		public void handleMessage(Message msg){
			switch (msg.what){
			case Bluetooth.MESSAGE_RECIEVE:
				JSONObject obj = (JSONObject)msg.obj;
				try {
					Log.d("Handler", "Message Recieved!");
					int sender = obj.getInt(Device.MESSAGE_COMPONENT_SENDER);
					int reciever = obj.getInt(Device.MESSAGE_COMPONENT_RECIEVER);
					int type = obj.getInt(Device.MESSAGE_COMPONENT_TYPE);
					String message = obj.getString(Device.MESSAGE_COMPONENT_MESSAGE);
					String debugMsg = "[from " + sender 
							+ " to " + reciever
							+ " type: " + type + "] " 
							+ message;
					String debugMsg2 = obj.getString("JSON");
					
					
					Log.d("message", debugMsg);
					Log.d("jsonMessage", debugMsg2);
					/*Toast.makeText(context, 
							
							debugMsg
							
							, Toast.LENGTH_LONG).show();
					*/
					//SYSTEM - Handle system messages
					
					//Identify events that are listening for the current BluetoothMessage type and run them
					
					//if the host has received a message not intended to the host
					if (HostDevice.self && reciever != -1){
						//redirect the message to the appropriate player pretending to be the original sender
						Device.player[reciever].sendMessage(type, message, sender);
					} else {
						boolean found = false;
						for (int i=0; i<Bluetooth.bluetoothEvent.size(); i++){
							BluetoothEvent event = Bluetooth.bluetoothEvent.get(i);
							if (event.typeValid(type) == true){
								if (sender < -1){ //if sender is unknown, set it
									sender = msg.arg1;
								}
								Log.d("BluetoothEvent", "Event succesfully identified");
								found = true;
								event.processMessage(sender, reciever, message);
							}
						}
						if (!found){ //if no event was identified, display an error
							Log.e("BluetoothEvent", "Unable to find event for id: " + type);
						}
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Bluetooth.MESSAGE_ERROR: 
				String message = (String)msg.obj;
				if (message.equals("playerDisconnect")){
					if (MapActivity.activity == null){
						if (HostDevice.self){
							Log.e("arg1", msg.arg1+"");
							/*LobbyActivity
								.activity
								.createAlert("Player " + Device
										.player[msg.arg1]
												.name + " has disconnected");*/
							
							Device.player[msg.arg1] = null;
							
							LobbyActivity.activity.updatePlayerList();
						} else {
							Toast.makeText(context, "You have disconnected from the game", Toast.LENGTH_SHORT).show();
							
							//go back
							if (LobbyActivity.activity != null)
								LobbyActivity.activity.finish();
							
							//flush out existing knowledge of lobby
							for (int i=0; i<Device.player.length; i++){
								if (Device.player[i] != null){
									Device.player[i] = null;
								}
							}
						}
					} else {
						if (HostDevice.self){
							Log.e("arg1", msg.arg1+"");
							MapActivity
								.activity
								.createAlert("Player " + Device
										.player[msg.arg1]
												.name + " has disconnected");
							
							Device.player[msg.arg1].active.cancel();
							Device.player[msg.arg1].active = null;
							
							//Toast.makeText(context, "Player " + msg.arg1 + " has disconnected", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "You have disconnected from the game", Toast.LENGTH_SHORT).show();
							CommandCardActivity
							.activity
							.createAlert("You have disconnected from the game");
							//go back
							
							Device.player[Device.currentPlayer].active.cancel();
							Device.player[Device.currentPlayer].active = null;
							
						}
					}
					
				}
				//Toast.makeText(context, msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	} //End class Handle
} //End class Bluetooth