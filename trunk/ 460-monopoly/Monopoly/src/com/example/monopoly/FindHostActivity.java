package com.example.monopoly;

import java.util.ArrayList;

import com.example.bluetooth.Bluetooth;
import com.example.controllers.PlayerDevice;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FindHostActivity extends Activity {
	
	public static FindHostActivity activity = null;
	public static String tmpGameName = "";
	
	private Button btnBack = null;
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
		
		//Setup list adapter
		adapter=new ArrayAdapter<String>(this.lstHosts.getContext(),
	            android.R.layout.simple_list_item_1,
	            listItems);
		this.lstHosts.setAdapter(adapter);
		
		this.lstHosts.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
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
                    if (name.length() > gameNameLength){
	                    if (name.substring(0, gameNameLength).equals("["+Bluetooth.mGameName+"]")){
	                    	addItem(device, name.substring(gameNameLength) + "\n" + device.getAddress());
	                    	FindHostActivity.tmpGameName = name.substring(gameNameLength);
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

}
