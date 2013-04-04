package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;

public class TabInteractActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_interact);
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
