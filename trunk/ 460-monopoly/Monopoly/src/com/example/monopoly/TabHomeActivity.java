package com.example.monopoly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class TabHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_home);
	}

	@Override
	public void onBackPressed() { 
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
