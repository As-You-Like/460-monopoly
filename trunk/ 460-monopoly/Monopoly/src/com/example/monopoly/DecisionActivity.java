
package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;

public class DecisionActivity extends Activity {
	HostDevice hostDevice;
	Button btnLeft;
	Button btnRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision);
		hostDevice = new HostDevice(false);
		
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		
		setButtonName("Boyston Appartments", "Police Station");
	}

	private void setButtonName(String left, String right) {
		btnLeft.setText("Left : " + left);
		btnRight.setText("Right : " + right);
	}

	public void clickEventLeft(View v) {
		hostDevice.sendMessage(Message.RECEIVE_FORK_PATH, 0 + "");
	}

	public void clickEventRight(View v) {
		hostDevice.sendMessage(Message.RECEIVE_FORK_PATH, 1 + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_decision, menu);
		return true;
	}

}
