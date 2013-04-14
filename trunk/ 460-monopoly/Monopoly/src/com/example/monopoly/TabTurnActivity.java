package com.example.monopoly;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class TabTurnActivity extends Activity { 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_turn);
		
		Button btnButton = (Button)findViewById(R.id.btnRoll); 
		btnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HostDevice.host.sendMessage(Message.MOVEMENT_DICE_ROLL, "");
			}
			
		});
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
