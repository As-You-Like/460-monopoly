package com.example.monopoly;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpgradeActivity extends Activity {
	
	public static UpgradeActivity activity;

	TextView txtTopCash;
	TextView txtTopRent;
	TextView txtRegion;
	TextView txtValueElectical;
	TextView txtValuePlumbing;
	TextView txtValueVending;
	TextView txtValueHVAC;

	Button btnElectical;
	Button btnPlumbing;
	Button btnVending;
	Button btnHVAC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade);
		
		activity = this;

		txtTopCash = (TextView) findViewById(R.id.txt_cash);
		txtTopRent = (TextView) findViewById(R.id.txt_rent);
		txtRegion = (TextView) findViewById(R.id.txt_region);

		txtValueElectical = (TextView) findViewById(R.id.txt_value_electical);
		txtValuePlumbing = (TextView) findViewById(R.id.txt_value_plumbing);
		txtValueVending = (TextView) findViewById(R.id.txt_value_vending);
		txtValueHVAC = (TextView) findViewById(R.id.txt_value_hvac);

		btnElectical = (Button) findViewById(R.id.btn_electical);
		btnPlumbing = (Button) findViewById(R.id.btn_plumbing);
		btnVending = (Button) findViewById(R.id.btn_vending);
		btnHVAC = (Button) findViewById(R.id.btn_hvac);

		// Setting Values
		setRegionName("Freshman Dooooorms");
		setCashValues(14500, 200);
		setButtonValue(300, 250, 1100, 3200);
		setRentValue(500, 240, 1500, 4400);
	}

	// Click Electical Button
	public void clickEventElectical(View v) {
		//String text = btnElectical.getText().toString();
		//String value = text.substring(1, text.length());
		//Log.i("value", value);
		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_UPGRADE_PROPERTY, ""+0);
	}

	// Click Plumbing Button
	public void clickEventPlumbing(View v) {
		//String text = btnPlumbing.getText().toString();
		//String value = text.substring(1, text.length());
		//Log.i("value", value);
		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_UPGRADE_PROPERTY, ""+1);
	}

	// Click Vending Button
	public void clickEventVending(View v) {
		//String text = btnVending.getText().toString();
		//String value = text.substring(1, text.length());
		//Log.i("value", value);
		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_UPGRADE_PROPERTY, ""+2);
	}

	// Click HVAC Button
	public void clickEventHvac(View v) {
		//String text = btnHVAC.getText().toString();
		//String value = text.substring(1, text.length());
		//Log.i("value", value);

		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_UPGRADE_PROPERTY, ""+3);
	}

	public void setRegionName(String name) {
		txtRegion.setText("Region Name : " + name);
	}

	public void setCashValues(double currentBalance, double tilePrice) {
		txtTopCash.setText("Cash : $" + String.valueOf(currentBalance));
		txtTopRent.setText("Rent : $" + String.valueOf(tilePrice));
	}

	public void setButtonValue(double upgrade, double upgrade2, double upgrade3, double upgrade4) {
		btnElectical.setText("$" + String.valueOf(upgrade));
		btnPlumbing.setText("$" + String.valueOf(upgrade2));
		btnVending.setText("$" + String.valueOf(upgrade3));
		btnHVAC.setText("$" + String.valueOf(upgrade4));
	}
	
	public void enableUpgradeButtons(boolean electrical, boolean plumbing, boolean vending, boolean hvac){
		btnElectical.setEnabled(!electrical);
		btnPlumbing.setEnabled(!plumbing);
		btnVending.setEnabled(!vending);
		btnHVAC.setEnabled(!hvac);
	}

	public void setRentValue(double electical, double plumbing, double vending, double hvac) {
		txtValueElectical.setText("$" + String.valueOf(electical));
		txtValuePlumbing.setText("$" + String.valueOf(plumbing));
		txtValueVending.setText("$" + String.valueOf(vending));
		txtValueHVAC.setText("$" + String.valueOf(hvac));
	}
	
	@Override
	  public void onBackPressed() {
	    this.getParent().onBackPressed();   
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_upgrade, menu);
		return true;
	}

}
