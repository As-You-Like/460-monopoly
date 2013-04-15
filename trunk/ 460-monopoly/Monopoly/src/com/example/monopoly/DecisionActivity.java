package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class DecisionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision);
		
	}
	
	public void clickEventLeft(View v){
		
	}
	public void clickEventRight(View v){
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_decision, menu);
		return true;
	}

}
