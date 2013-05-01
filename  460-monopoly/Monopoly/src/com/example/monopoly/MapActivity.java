package com.example.monopoly;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.controllers.Game;
import com.example.controllers.GameThread;
import com.example.controllers.HostDevice;
import com.example.controllers.TickerObject;
import com.example.monopoly.PanAndZoomListener;
import com.example.monopoly.PanAndZoomListener.Anchor;


public class MapActivity extends Activity { 
	
	public static MapActivity activity;
	public CircleView c;
	public static Resources resources;
	public Canvas canvas= new Canvas();
	public RelativeLayout view;
	
	public MenuItem menuExit;
	public MenuItem menuListen;
	
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
		
		GameThread.gt.start();
		
		TickerObject.entity.add(new TickerObject("A",3));
		TickerObject.entity.add(new TickerObject("B",7));
		TickerObject.entity.add(new TickerObject("C",4));
		TickerObject.entity.add(new TickerObject("D",1));
		TickerObject.entity.add(new TickerObject("E",6));
		
		Thread t = new Thread(background);
		t.start();
		

	}
	
	public void onStart()
	{
		super.onStart();
		view.setOnTouchListener(new PanAndZoomListener(view, c, Anchor.TOPLEFT));

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
					Thread.sleep(2000);
					
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
}