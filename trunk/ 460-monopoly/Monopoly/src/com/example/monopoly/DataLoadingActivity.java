package com.example.monopoly;

import com.example.bluetooth.Message;
import com.example.controllers.DatabaseThread;
import com.example.controllers.Device;
import com.example.controllers.Game;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class DataLoadingActivity extends Activity {
	
	public static DataLoadingActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_loading);
		activity = this;
		
		DatabaseThread dbt = new DatabaseThread();
		dbt.isLoad = true;
		dbt.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_loading, menu);
		return true;
	}
	
	public static void startGameModule(){
		Intent intent = new Intent(LoadingActivity.activity, MapActivity.class);
		DataLoadingActivity.activity.startActivity(intent);
	}

}
