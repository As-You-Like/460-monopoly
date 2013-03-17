package com.example.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.controllers.Device;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Bluetooth {
	// Variables
	public static String mGameName = "Monopoly";
	
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
	
	// Constructor
	public Bluetooth(Context context){
		Bluetooth.mAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mAdapter == null){
			Toast.makeText(context, "Your device does not support Bluetooth, you cannot play this game", Toast.LENGTH_LONG)
			.show();
		}
		
		Bluetooth.mHandler = new Bluetooth.Handle();
		Bluetooth.entity = this;
		Bluetooth.context = context;
	} // End Constructor
	
	/**
	 * Changes the name of the device on the discovery stage
	 * Includes the name of the game, so that this app on other devices can distinguish
	 * devices that are hosting an instance of this game
	 * @param name
	 */
	public static void ChangeDeviceName(String name){
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
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
	public synchronized void connect(BluetoothDevice device){
		
	} //End connect()
	
	/**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
	public synchronized int connected(int p, BluetoothSocket socket, BluetoothDevice device){
		return 0;
	} //End connected()
	
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
	
	// Inner Classes
	public class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		public boolean listen = true;
		
		public AcceptThread(){
			BluetoothServerSocket tmp = null;

            // Create a new listening server socket
			//UUID uuid = Player.entities[Player.currentPlayer].uuid; 
            try {
            	
                //tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
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
			while(listen){
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e){
					Toast.makeText(context, "AcceptThread server socket error", Toast.LENGTH_SHORT).show();
					break; 
				}
				
				if (socket != null){
					synchronized (Bluetooth.this){
						HostDevice.activate(socket, socket.getRemoteDevice()); 
						
						//if the number of connected players is already at its limit, stop listening
						
					}
				}
			}
			
		} //End run()
		
		public void cancel(){
			try {
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
            //try {
            	//UUID uuid = Player.entities[Player.currentPlayer].uuid;
                //tmp = device.createRfcommSocketToServiceRecord(uuid);
            //} catch (IOException e) {
            	//Toast.makeText(context, "ConnectedThread create failed", Toast.LENGTH_SHORT).show();
            //}
            mmSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("ConnectThread");
			
			// Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                	Toast.makeText(context, "ConnectThread unable to close socket during connection failure", Toast.LENGTH_SHORT).show();
                }
                // Start the service over to restart listening mode
                Bluetooth.this.start();
                return;
            }
            
            //Reset the ConnectThread because we're done
            synchronized (Bluetooth.this) {
            	HostDevice.connect = null;
            }

            // Start the connected thread
            connected(0, mmSocket, mmDevice);
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
		private PlayerDevice player;
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ActiveThread(BluetoothSocket socket, PlayerDevice player){
			this.player = player;
			mmSocket = socket;
			
			InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(context, "ConnectedThread failed to make temp sockets", Toast.LENGTH_SHORT).show();
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
	            		String json = builder.toString();
	            		JSONObject obj = null;
	            		try {
							obj = new JSONObject(json);
						} catch (JSONException e) {
							obj = null;
						}
	            		if (obj != null){
	            			
	            			Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_RECIEVE, obj).sendToTarget();
	            		}
	            		
	            	}
	            	
	                builder.delete(0, builder.length());
	                
				} catch (IOException e){
					//Toast.makeText(context, "ConnectedThread player " + this.playerNum + " has disconnected", Toast.LENGTH_SHORT).show();
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
            	Toast.makeText(context, "ConnectedThread failed to write", Toast.LENGTH_SHORT).show();
            }
		} //End write()
		
		public void cancel(){
			try {
				mmSocket.close();
			} catch (IOException e) {
				Toast.makeText(context, "ConnectedThread failed to close socket", Toast.LENGTH_SHORT).show();
			}
		} //End cancel()
	} //End class ConnectedThread
	
	private static class Handle extends Handler {
		public void handleMessage(Message msg){
			JSONObject obj = (JSONObject)msg.obj;
			switch (msg.what){
			case Bluetooth.MESSAGE_RECIEVE:
				try {
					Log.d("Handler", "Message Recieved!");
					Toast.makeText(context, 
							
							"[from " + obj.getInt(Device.MESSAGE_COMPONENT_SENDER) 
							+ " to " + obj.getInt(Device.MESSAGE_COMPONENT_RECIEVER) 
							+ " type: " + obj.getInt(Device.MESSAGE_COMPONENT_TYPE) + "] " 
							+ obj.getString(Device.MESSAGE_COMPONENT_MESSAGE)
							
							, Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Bluetooth.MESSAGE_ERROR:
				Toast.makeText(context, msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	} //End class Handle
} //End class Bluetooth