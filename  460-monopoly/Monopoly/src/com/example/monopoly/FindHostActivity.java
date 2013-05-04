package com.example.monopoly;

import java.util.ArrayList;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class FindHostActivity extends Activity {
	
	public static FindHostActivity activity = null;
	public static String tmpGameName = "";
	
	//private Button btnBack = null;
	private Button btnRefresh = null;
	private ListView lstHosts = null;
	private TextView txtTitle = null;
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> listItems = new ArrayList<String>();
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findhost); 
		
		// Setup this activity as needed
		FindHostActivity.activity = this;
		
		//this.btnBack    = (Button) this.findViewById(R.id.btnBack);
		this.btnRefresh = (Button) this.findViewById(R.id.btnRefresh);
		this.lstHosts   = (ListView) this.findViewById(R.id.lstHosts);
		this.txtTitle   = (TextView) this.findViewById(R.id.txtTitle);
		
		this.txtTitle.setText("Scanning for games ...");
		
		//Setup list adapter
		adapter=new ArrayAdapter<String>(this.lstHosts.getContext(),
	            android.R.layout.simple_list_item_1,
	            listItems);
		this.lstHosts.setAdapter(adapter);
		
		this.lstHosts.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
				//Ask player to type in a username (if we stay with the old design)
				if (HostDevice.connectionStatus == HostDevice.CONNECTION_CONNECTING){
					return;
				}
				
				//Attempt to connect the player
				BluetoothDevice host = FindHostActivity.activity.devices.get(index);
				PlayerDevice.connect(host);
			}
			
		});
		
		//setup click listener for the refresh button
		this.btnRefresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				
			}
			
		});
		
		//register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(this.mReceiver, filter);
		
		//register for broadcasts when a discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(this.mReceiver, filter);
		
		//register LOBBY_NEW_PLAYER event
				Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
					public boolean typeValid(int type) {
						return type == Message.LOBBY_NEW_PLAYER; 
					}

					@Override
					public void processMessage(int sender, int reciever, String message) {
						int playerNum = Integer.parseInt(message.substring(11,12));
						String name = message.substring(12);
						
						PlayerDevice p = new PlayerDevice(false, playerNum);
						p.name = name;
						
						if (LobbyActivity.activity != null){
							LobbyActivity.activity.updatePlayerList();
						}
					}
					
				});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.RECONNECT_START;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				PlayerDevice.currentPlayerConnectionStatus = PlayerDevice.CONNECTION_ACTIVE;
				//Device.tmpCurrentPlayer.;
				Device.currentPlayer = reciever;
				
				int divider1 = message.indexOf(":");
				int divider2 = message.indexOf(":", divider1+1);
				int divider3 = message.indexOf(":", divider2+1);
				String[] players = new String[]{
						message.substring(27, divider1), //get first name
						message.substring(divider1+1, divider2), //get second name
						message.substring(divider2+1, divider3), //get third name
						message.substring(divider3+1) //get fourth name
				};
				//if the game has already started (a reconnect has occurred)
				dialogChoosePlayer(players);
			}
			
		});
		
		//register LOBBY_SLOT event
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
			public boolean typeValid(int type) {
				return type == Message.LOBBY_SLOT;
			}
			
			public void processMessage(int sender, int reciever, String message) {
				//extract player data
				int playerNum = Integer.parseInt(message.substring(9, 10));
				String deviceMac = message.substring(10, 27);
				
				
				
				if (deviceMac.equals(Bluetooth.mAdapter.getAddress())){
					//the new player is the current device
					PlayerDevice.currentPlayerConnectionStatus = PlayerDevice.CONNECTION_ACTIVE;
					Device.player[playerNum] = Device.tmpCurrentPlayer;
					Device.currentPlayer = playerNum;
					FindHostActivity.goToLobby();
					
				} else {
					//the new player is not the current device
					
					//create player object
					PlayerDevice p = new PlayerDevice(false, playerNum);
					p.name = "Player " + playerNum + " is connecting ...";
					
					//update lobby UI
					if (LobbyActivity.activity != null){
						LobbyActivity.activity.updatePlayerList();
					}
				}
			}
			
		});
		
		//Register RECONNECT_CHOICE_ACCEPT event
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.RECONNECT_CHOICE_ACCEPT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Register RECONNECT_CHOICE_REJECT event
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.RECONNECT_CHOICE_REJECT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				int divider1 = message.indexOf(":");
				int divider2 = message.indexOf(":", divider1+1);
				int divider3 = message.indexOf(":", divider2+1);
				String[] players = new String[]{
						message.substring(0, divider1), //get first name
						message.substring(divider1+1, divider2), //get second name
						message.substring(divider2+1, divider3), //get third name
						message.substring(divider3+1) //get fourth name
				};
				dialogChoosePlayer(players);
				createAlert("The player you requested was already taken, please choose another");
			}
			
		});
		
		//Register RECONNECT_CHOICE event (to be moved to GameThread)
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.RECONNECT_CHOICE;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				int divider = message.indexOf(":");
				int source = Integer.parseInt(message.substring(divider+1));
				int which = Integer.parseInt(message.substring(0, divider));
				
				if (Device.player[which].playerConnectionStatus == PlayerDevice.CONNECTION_DISCONNECTED){
					//accept player request
					Device.connectingPlayer.get(sender).sendMessage(Message.RECONNECT_CHOICE_ACCEPT, "");
					Device.player[which].playerConnectionStatus = PlayerDevice.CONNECTION_CONNECTING;
				} else {
					//reject player request
					Device.connectingPlayer.get(sender).sendMessage(Message.RECONNECT_CHOICE_REJECT, "");
				}
			}
			
		});
	}
	
	private void dialogChoosePlayer(String[] players){
		LayoutInflater inflater = getLayoutInflater();
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FindHostActivity.activity);
		
		alertBuilder.setTitle("Who are you?");
		alertBuilder.setItems(players, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (HostDevice.host == null){
					Log.e("requestName()", "Host is not defined, unable to send message");
					createAlert("There was a problem, please try again");
					return;
				}
				HostDevice.host.sendMessage(Message.RECONNECT_CHOICE, ""+which);
			}
		});
	}
	
	protected void onStart(){
		super.onStart();
		
		Bluetooth.startDiscovery();
	}
	
	protected void onDestroy() {
        super.onDestroy();
        
     // Make sure we're not doing discovery anymore
        Bluetooth.endDiscovery();
        
     // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
	
	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItem(BluetoothDevice device, String text) {
        listItems.add(text);
        adapter.notifyDataSetChanged();
        
        devices.add(device);
    }
    
    public static void goToLobby(){
    	Intent intent = new Intent(FindHostActivity.activity, LobbyActivity.class);
    	intent.putExtra("gn",tmpGameName);
    	FindHostActivity.activity.startActivity(intent);
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    //addItem(device.getName() + "\n" + device.getAddress());
                    
                    //game detection code on standby !!!!!!
                    String name = device.getName();
                    int gameNameLength = Bluetooth.mGameName.length()+2;
                    if (name != null){
	                    if (name.length() > gameNameLength){
		                    if (name.substring(0, gameNameLength).equals("["+Bluetooth.mGameName+"]")){
		                    	addItem(device, name.substring(gameNameLength) + "\n" + device.getAddress());
		                    	FindHostActivity.tmpGameName = name.substring(gameNameLength);
		                    }
	                    }
                    }
               // }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                //setTitle(R.string.select_device);
                //if (mNewDevicesArrayAdapter.getCount() == 0) {
                    //String noDevices = getResources().getText(R.string.none_found).toString();
                    //mNewDevicesArrayAdapter.add(noDevices);
                //}
            }
        }
    };
    
    public void createAlert(String msg){
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
				// TODO
			}
		})
		.show();		
	}

}
