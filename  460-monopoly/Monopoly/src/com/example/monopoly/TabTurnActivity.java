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
import android.widget.TextView;

public class TabTurnActivity extends Activity { 
	
	public static TabTurnActivity activity;
	Button btnTurn;
	TextView txtYourTurn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_turn);
		
		activity = this;
		
		txtYourTurn = (TextView)findViewById(R.id.txtYourTurn);
		txtYourTurn.setText("It's your turn");
		
		btnTurn = (Button)findViewById(R.id.btnRoll); 
		btnTurn.setVisibility(View.INVISIBLE);
		btnTurn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HostDevice.host.sendMessage(Message.MOVEMENT_DICE_ROLL, "");
				btnTurn.setVisibility(View.INVISIBLE);
				txtYourTurn.setText("Please Wait");
			}
			
		});
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
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
