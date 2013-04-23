
package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;

public class DecisionActivity extends Activity {
	
	public static DecisionActivity activity;
	
	Button btnLeft;
	Button btnRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision);
		
		activity = this;
		
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		
		setButtonName("Boyston Appartments", "Police Station");
	}

	public void setButtonName(String left, String right) {
		btnLeft.setText(left);
		btnRight.setText(right);
	}

	public void clickEventLeft(View v) {
		HostDevice.host.sendMessage(Message.RECEIVE_FORK_PATH, 0 + "");
	}

	public void clickEventRight(View v) {
		HostDevice.host.sendMessage(Message.RECEIVE_FORK_PATH, 1 + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_decision, menu);
		return true;
	}

}
