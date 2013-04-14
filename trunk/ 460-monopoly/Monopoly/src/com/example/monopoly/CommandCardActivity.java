package com.example.monopoly;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;

@SuppressWarnings("deprecation")
public class CommandCardActivity extends TabActivity { 
	public TabHost tabBar;
	public static CommandCardActivity activity;

	final static int TAB_TURN = 0;
	final static int TAB_HOME = 1;
	final static int TAB_PROPERTIES = 2;
	final static int TAB_INTERACT = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commandcard);
		
		if (isTablet(this)) { // isTablet : Move to MapActivty
			Intent i = new Intent(this, MapActivity.class);
			startActivity(i);
		}
		
		setTab();
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				//I don't know how it would fit to do so..
				if(type == Message.START_PLAYER_TURN) // if my turn, set Turn tab
					tabBar.setCurrentTab(TAB_TURN);
				else if(type == Message.YOUR_TURN_IS_OVER) // my turn is over, set Home Tab 
					tabBar.setCurrentTab(TAB_HOME);
				
				return type == Message.START_PLAYER_TURN;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void setTab(){
		tabBar = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// Turn Tab (Not Clickable) : 0
		intent = new Intent().setClass(this, TabTurnActivity.class);
		spec = tabBar.newTabSpec("Turn").setIndicator("Turn").setContent(intent);
		tabBar.addTab(spec);

		// Home Tab : 1
		intent = new Intent().setClass(this, TabHomeActivity.class);
		spec = tabBar.newTabSpec("Home").setIndicator("Home").setContent(intent);
		tabBar.addTab(spec);

		// Properties Tab : 2
		intent = new Intent().setClass(this, TabPropertiesActivity.class);
		spec = tabBar.newTabSpec("Properties").setIndicator("Properties").setContent(intent);
		tabBar.addTab(spec);

		// Interact Tab : 3
		intent = new Intent().setClass(this, TabInteractActivity.class);
		spec = tabBar.newTabSpec("Interact").setIndicator("Interact").setContent(intent);
		tabBar.addTab(spec);
		
				
		tabBar.setCurrentTab(TAB_HOME);
		tabBar.getTabWidget().getChildTabViewAt(0).setEnabled(false); // Turn Tab not clickable..
		
	}
	
	@Override
	public void onBackPressed() {
		if(tabBar.getCurrentTab() == TAB_HOME) // If Home Tab, Finish Activity
			finish();
		else 							// else Another Tab, move to Home Tab
			tabBar.setCurrentTab(TAB_HOME); 
	}

	
	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}


}