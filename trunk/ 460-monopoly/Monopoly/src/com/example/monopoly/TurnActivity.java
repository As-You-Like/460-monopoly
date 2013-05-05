package com.example.monopoly;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class TurnActivity extends Activity {
	
	TurnActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_turn, menu);
		return true;
	}
	
	public void onResume()
	{
		super.onResume();
		if(SplashActivity.activity.cascadeQuitBool == true){
			activity.terminate();
		}

	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}


}
