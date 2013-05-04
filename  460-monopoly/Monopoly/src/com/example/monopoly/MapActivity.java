package com.example.monopoly;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetooth.Bluetooth;
import com.example.content.BoardSetup;
import com.example.content.Image;
import com.example.controllers.DatabaseThread;
import com.example.controllers.Game;
import com.example.controllers.GameThread;
import com.example.controllers.HostDevice;
import com.example.controllers.Player;
import com.example.controllers.SQLHelper;
import com.example.controllers.TickerObject;
import com.example.model.Tile;
import com.example.model.Unit;
import com.example.monopoly.PanAndZoomListener;
import com.example.monopoly.PanAndZoomListener.Anchor;


public class MapActivity extends Activity { 
	
	public static MapActivity activity;
	public CircleView c;
	public static Resources resources;
	public Canvas canvas= new Canvas();
	public RelativeLayout view;
	public DatabaseThread dbt;
	
	public MenuItem menuExit;
	public MenuItem menuListen;
	public MenuItem menuQuit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		//ticker=(TextView)findViewById(R.id.tick);
		activity = this;
		
		/*ticker=new TextView(this.getApplicationContext());
		ticker.setText("Testing");*/
		  RelativeLayout.LayoutParams fp = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, 
                  FrameLayout.LayoutParams.MATCH_PARENT);
		
		view = new RelativeLayout (this);
		setContentView (view);
		
		Context context = this.getApplicationContext();
		c = new CircleView(context);
		
		view.addView(c);
		//view.addView(ticker);
		
		resources = c.getResources();
		
		
		/*Image.HEXAGON_TEXTURE = BitmapFactory.decodeResource(MapActivity.activity.getResources(), R.drawable.hexagon_blue);
		Image.HEXAGON_BOTTOM = BitmapFactory.decodeResource(MapActivity.activity.getResources(), R.drawable.hexagon_layer_bot);
		Image.HEXAGON_REGION = BitmapFactory.decodeResource(MapActivity.activity.getResources(), R.drawable.hexagon_layer_rgn);
		Image.HEXAGON_PLAYER = BitmapFactory.decodeResource(MapActivity.activity.getResources(), R.drawable.hexagon_layer_plr);
		BoardSetup.setupBoard();*/
		GameThread.gt.start();
		
		
		TickerObject.entity.add(new TickerObject("A fine elm hall girl was found " +
				"projectile vomiting all over the bathroom walls",3));
		TickerObject.entity.add(new TickerObject("Bentley center for business " +
				"ethics reports receiving large donation from Charles Taylor",7));
		TickerObject.entity.add(new TickerObject("Reports of glass bottles " +
				"being thrown from the 4th floor of The Trees",4));
		TickerObject.entity.add(new TickerObject("Alarm clock reported to have" +
				" been going off all morning in Trees",1));
		TickerObject.entity.add(new TickerObject("AZP Fraternity has been " +
				"kicked off campus for poor behavior.",6));
		TickerObject.entity.add(new TickerObject("Bentley English professor " +
				"embarasses himself at spelling be",5));
		TickerObject.entity.add(new TickerObject("All incoming freshmen will " +
				"be required to take GB320, says Academic coordinators",5));
		TickerObject.entity.add(new TickerObject("Super fast wi-fi to open" +
				" new doors for business consumers",5));
		TickerObject.entity.add(new TickerObject("Falcon Awards honors community " +
				"achievements",5));
		TickerObject.entity.add(new TickerObject("Everyone's a winner: No " +
				"competition for student leader positions",5));
		TickerObject.entity.add(new TickerObject("Bentley men's hockey obtains " +
				"new home arena at Babson College",5));
		TickerObject.entity.add(new TickerObject("The CLIC lab has begun offering " +
				"English classes",5));
		TickerObject.entity.add(new TickerObject("\"Bonjour\" from the " +
				"CLIC lab",5));
		TickerObject.entity.add(new TickerObject("Sodexo begins drone delivery " +
				"service from Currito",5));
		TickerObject.entity.add(new TickerObject("Bentley student reported stabbed " +
				"by a \"woman of the night\"",5));
		TickerObject.entity.add(new TickerObject("The CIS sandbox will now offer " +
				"tutoring in sandcastle architecture",5));
		TickerObject.entity.add(new TickerObject("$165 million Powerball Tonight",5));
		TickerObject.entity.add(new TickerObject("Bentley begins offering major " +
				"in Art History",5));
		TickerObject.entity.add(new TickerObject("Students siege campus in " +
				"snowball war",5));
		TickerObject.entity.add(new TickerObject("Large paint party thrown on " +
				"GreenSpace, now known as RainbowSpace",5));
		TickerObject.entity.add(new TickerObject("Andy Fastow reportedly is " +
				"the new head of the Ethics Department",5));
		TickerObject.entity.add(new TickerObject("Bentley gains fire department " +
				"to deal with the recent firework problem at Collins",5));
		TickerObject.entity.add(new TickerObject("Bentley set to audit Babson " +
				"College",5));
		TickerObject.entity.add(new TickerObject("Marketing department to " +
				"build bridge from North Campus to Dana Center",5));
		TickerObject.entity.add(new TickerObject("All books eliminated by bonfire " +
				"from Bentley Library.  Put on one shareable disc",5));
		TickerObject.entity.add(new TickerObject("Ilya Bryzgalov hired as new " +
				"astronomy professor",5));
		TickerObject.entity.add(new TickerObject("White walkers reportedly seen " +
				"on Bentley campus",5));
		TickerObject.entity.add(new TickerObject("Winter is coming",5));
		TickerObject.entity.add(new TickerObject("Spring is here, attendance" +
				" drops",5));
		TickerObject.entity.add(new TickerObject("North Campus announces its " +
				"secession from the Resident Hall Association",5));
		TickerObject.entity.add(new TickerObject("Bentley announces Water Park " +
				"plans on RainbowSpace",5));
		TickerObject.entity.add(new TickerObject("Bentley loop arrives 24 hours " +
				"late, campus in outrage",5));
		TickerObject.entity.add(new TickerObject("Harvard shuttle arrives on " +
				"schedule, Today Only, July 5th",5));
		TickerObject.entity.add(new TickerObject("Joffrey Baratheon elected " +
				"as Class of 2015 President",5));
		TickerObject.entity.add(new TickerObject("Bentley to add a Los Pollos" +
				" Hermanos restaurant on campus",5));
		TickerObject.entity.add(new TickerObject("ISIS Agents arrive on Bentley " +
				"campus to investigate toilet paper theft",5));
		TickerObject.entity.add(new TickerObject("Microsoft unexpectedly forces " +
				"update of all computers-immediately",5));
		TickerObject.entity.add(new TickerObject("Google found on campus " +
				"recruiting Accounting majors.  CIS majors distraught",5));
		TickerObject.entity.add(new TickerObject("Bacteria found growing in " +
				"Trees-students forced to move",5));
		TickerObject.entity.add(new TickerObject("Bentley to air commercials " +
				"during Super Bowl-on the Food Network",5));
		TickerObject.entity.add(new TickerObject("Alligators found in pond," +
				" swimmers beware!",5));
		TickerObject.entity.add(new TickerObject("Bentley announces new high " +
				"class hair salon, located in Smith",5));
		TickerObject.entity.add(new TickerObject("Registrar announces MK 402: " +
				"Rebuilding the Bentley Brand",5));
		TickerObject.entity.add(new TickerObject("Campus police investigating " +
				"a wide-scale computer mouse theft",5));
		TickerObject.entity.add(new TickerObject("Bentley Police investigating " +
				"license plate theft-Out of staters beware",5));
		TickerObject.entity.add(new TickerObject("Tires being stolen from cars in" +
				" all parking areas",5));
		TickerObject.entity.add(new TickerObject("Tyga: Spring Day no show",5));
		TickerObject.entity.add(new TickerObject("Help desk announces campus-wide " +
				"reimaging on Reading Day",5));
		TickerObject.entity.add(new TickerObject("Campus-wide lockdown: Campus police " +
				"dogs going wild",5));
		TickerObject.entity.add(new TickerObject("Bentley Police to use Segways, " +
				"stairs an issue",5));
		TickerObject.entity.add(new TickerObject("Campus board decides on spending " +
				"$10,000 on new escalator system on Smith stairs",5));
		TickerObject.entity.add(new TickerObject("Monsoon coming in- Graduation " +
				"postponed",5));
		TickerObject.entity.add(new TickerObject("Spiritual life center announces " +
				"hiring of new Scientology Chaplain",5));
		
		Thread t = new Thread(background);
		t.start();
		
		
		
	}
	
	public void onStart()
	{
		super.onStart();
		view.setOnTouchListener(new PanAndZoomListener(view, c, Anchor.TOPLEFT));

	}
	
	public void onDestroy()
	{
		super.onDestroy();
		Log.i("", "DESTROYING MAP ACTIVITY (oh my)");

	}
	
	Runnable background = new Runnable() {
		public void run() {
			try {
				for(int i=0; i<TickerObject.entity.size(); i++)
				{
					//scroller.setText(events.get(i).getEventText());
					Message msg = handler.obtainMessage(i);
					handler.sendMessage(msg);
					
					if(i==TickerObject.entity.size()-1)
					{
						i=-1;
					}
					Thread.sleep(3000);
					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	Handler handler = new Handler() {
		public synchronized void handleMessage(Message m){
			
			int i=m.what;
			setTitle(TickerObject.entity.get(i).getEventText());
					
		}
		
	};
	
	private void ensureDiscoverable(boolean checkStatus) {
	       // if(D) Log.d(TAG, "ensure discoverable");
	        if (Bluetooth.mAdapter.getScanMode() !=
	            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE || checkStatus == false) {
	            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
	            this.startActivity(discoverableIntent);
	        }
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menuExit = menu.add(0, 1, Menu.NONE, "Rules");
		menuListen = menu.add(0, 2, Menu.NONE, "Listen for Joining Players");
		menuQuit = menu.add(0, 3, Menu.NONE, "Quit Game");
		
		menuExit.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return true;
			}
			
		});
		
		menuListen.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				HostDevice.host.listenStart(true, Game.name);
				MapActivity.activity.ensureDiscoverable(false);
				
				return true;
			}
			
		});
		
		menuQuit.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				SplashActivity.activity.cascadeQuitBool = true;
				Player.entities = null;
				Game.instance = null;
				Unit.entity = null;
				Tile.entity = null;
				MapActivity.activity.terminate();				
				return true;
			}
			
		});
		return true;
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
	
	public void openDatabase(){
		SQLHelper helper = new SQLHelper(MapActivity.activity);
		dbt = new DatabaseThread();
		dbt.db = helper.getWritableDatabase();
		if(dbt.db == null){
			Log.e("", "NULL");
		}
		dbt.start();
		
	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}
	
}