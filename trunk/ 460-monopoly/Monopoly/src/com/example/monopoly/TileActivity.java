package com.example.monopoly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TileActivity extends Activity {
	static final int PURCHASE = 0;
	static final int UPDATE = 1;
	static final int PAYFREE = 2;
	
	int type; // 0:Purchase, 1:Update, 2:Pay Free
	int isOwned; // -1:noboy owns, 0:somebody owns
	boolean isMine; // true:Owner is self
	
	ImageView image;
	TextView txtLandedOn;
	TextView txtValue;
	TextView txtRegion;
	TextView txtStatus;
	TextView txtNotice;
	Button btnPurchase;
	Button btnEndTurn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tile);

		image = (ImageView) findViewById(R.id.image);
		txtLandedOn = (TextView) findViewById(R.id.txt_landedon);
		txtValue = (TextView) findViewById(R.id.txt_value);
		txtRegion = (TextView) findViewById(R.id.txt_region);
		txtStatus = (TextView) findViewById(R.id.txt_status);
		txtNotice = (TextView) findViewById(R.id.txt_notice);
		btnPurchase = (Button) findViewById(R.id.btn_left);
		btnEndTurn = (Button) findViewById(R.id.btn_right);

		/** Set view' contents */
		final int image_resId = R.drawable.sample_house; // R.drawable.sample_hotel;
		setLandInfo(image_resId, "Slade Hall", "$500", "Ultraman Dorms", "Not Owned", "0/5");
		
		/** Set land' status */ 
		isOwned = -1;
		isMine = true;
		
		
		if (isOwned == -1) {
			type = PURCHASE;
			btnPurchase.setText("Purchase");
		} else if (isMine) {
			type = UPDATE;
			btnPurchase.setText("Update");
		} else {
			type = PAYFREE;
			btnPurchase.setText("Pay Free");
		}
		
		// Purchase Button Clicked
		btnPurchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(type){
				case PURCHASE:
					purchase();
					break;
				case UPDATE:
					update();
					break;
				case PAYFREE:
					payfree();
					break;
				}
			}
		});
		
		// End Turn Button Clicked
		btnEndTurn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	// TODO purchase
	private void purchase(){
		createToast("Successfully purchased");
		createAlert("Successfully purchased");
	}
	
	// TODO update
	private void update(){
		createToast("Successfully updated");
		createAlert("Successfully updated");
	}
	
	// TODO payfree
	private void payfree(){
		createToast("Successfully payfree");
		createAlert("Successfully payfree");
	}

	
	public void setLandInfo(int res, String landed, String value, String region, String status, String count) {
		image.setImageResource(res);
		txtLandedOn.setText("Landed On : " + landed);
		txtValue.setText("Value : " + value);
		txtRegion.setText("Region : " + region);
		txtStatus.setText("Status : " + status);
		txtNotice.setText("You Own " + count + " Properties in This Region");
	}

	
	public void createToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void createAlert(String msg){
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
				// TODO
			}
		})
		.show();		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tile, menu);
		return true;
	}

}
