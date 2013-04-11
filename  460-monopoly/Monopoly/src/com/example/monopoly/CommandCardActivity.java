package com.example.monopoly;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class CommandCardActivity extends TabActivity {
	public TabHost tabBar;
	public static CommandCardActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commandcard);
		
		if (isTablet(this)) { // isTablet : Move to MapActivty
			Intent i = new Intent(this, MapActivity.class);
			startActivity(i);
		}

		tabBar = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// Home Tab
		intent = new Intent().setClass(this, TabHomeActivity.class);
		spec = tabBar.newTabSpec("Home").setIndicator("Home").setContent(intent);
		tabBar.addTab(spec);

		// Stat Tab
		intent = new Intent().setClass(this, TabStatActivity.class);
		spec = tabBar.newTabSpec("Stat").setIndicator("Stat").setContent(intent);
		tabBar.addTab(spec);

		// Properties Tab
		intent = new Intent().setClass(this, TabPropertiesActivity.class);
		spec = tabBar.newTabSpec("Properties").setIndicator("Properties").setContent(intent);
		tabBar.addTab(spec);

		// Interact Tab
		intent = new Intent().setClass(this, TabInteractActivity.class);
		spec = tabBar.newTabSpec("Interact").setIndicator("Interact").setContent(intent);
		tabBar.addTab(spec);
	}
	
	@Override
	public void onBackPressed() {
		if(tabBar.getCurrentTab() == 0) // If Home Tab, Finish Activity
			finish();
		else 							// else Another Tab, move to Home Tab
			tabBar.setCurrentTab(0); 
	}

	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}


}