package com.example.monopoly;

import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;
import com.example.model.Tile;

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
	static final int UPGRADE = 1;
	static final int PAYFREE = 2;
	static final int UNOWNABLE = 2;
	
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
	
	//remembers how many properties are owned in the region
	//this is here for property count updating purposes when properties are purchased
	public int count;
	public int totalCount;
	
	public static TileActivity activity;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tile);
		
		activity = this;

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
		//setLandInfo(image_resId, "Slade Hall", "$500", "Ultraman Dorms", "Not Owned", "0/5");
		
		
		
		// Purchase Button Clicked
		btnPurchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(type){
				case PURCHASE:
					purchase();
					break;
				case UPGRADE:
					upgrade();
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
				HostDevice.host.sendMessage(Message.TILE_ACTIVITY_END_TURN, "");
				CommandCardActivity.activity.tabBar.setCurrentTab(CommandCardActivity.TAB_HOME);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TURN).setVisibility(View.VISIBLE);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TILE).setVisibility(View.GONE);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TURN).setEnabled(false);
			}
		});
	}
	
	public void setPurchaseButtonStatus(int isOwned, boolean isMine){
		/** Set land' status */ 
		this.isOwned = isOwned;
		this.isMine = isMine;
		
		btnPurchase.setVisibility(View.VISIBLE);
		if (this.isOwned == -1) {
			type = PURCHASE;
			btnPurchase.setText("Purchase");
		} else if (this.isMine) {
			type = UPGRADE;
			btnPurchase.setText("Upgrade");
		} else {
			if (isOwned == Tile.OWNER_UNOWNABLE){
				type = UNOWNABLE;
				btnPurchase.setVisibility(View.INVISIBLE);
			} else {
				type = PAYFREE;
				btnPurchase.setText("Pay Free");
			}
			
		}
	}
	
	
	// TODO purchase
	private void purchase(){
		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_PURCHASE_PROPERTY, "");
	}
	
	// TODO update
	private void upgrade(){
		//display upgrade activity
		CommandCardActivity.activity.tabBar.setCurrentTab(CommandCardActivity.TAB_UPGRADE);
		CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_UPGRADE).setVisibility(View.VISIBLE); // Upgrade tab hidden away
		CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TILE).setVisibility(View.GONE); //
		//HostDevice.host.sendMessage(Message.TILE_ACTIVITY_UPGRADE_PROPERTY, "");
	}
	
	// TODO payfree
	private void payfree(){
		HostDevice.host.sendMessage(Message.TILE_ACTIVITY_PAY_RENT, "");
	}

	
	public void setLandInfo(int res, String landed, String value, String region, String status, int count, int countTotal) {
		image.setImageResource(res);
		txtLandedOn.setText("Landed On : " + landed);
		txtValue.setText("Value : " + value);
		txtRegion.setText("Region : " + region);
		txtStatus.setText("Status : " + status);
		txtNotice.setText("You Own " + count + "/" + countTotal + " Properties in This Region");
		this.count = count;
		this.totalCount = countTotal;
	}
	
	public void setCashInfo(String value){
		txtValue.setText("Value : " + value);
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
	  public void onBackPressed() {
	    this.getParent().onBackPressed();   
	  }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tile, menu);
		return true;
	}

}
