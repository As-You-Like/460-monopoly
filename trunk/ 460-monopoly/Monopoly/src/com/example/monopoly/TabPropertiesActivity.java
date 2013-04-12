package com.example.monopoly;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class TabPropertiesActivity extends Activity { 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_properties);
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
