package com.example.monopoly;

import android.app.Activity;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controllers.GameThread;
import com.example.controllers.TickerObject;
import com.example.monopoly.PanAndZoomListener;
import com.example.monopoly.PanAndZoomListener.Anchor;


public class MapActivity extends Activity { 
	
	public static MapActivity activity;
	public CircleView c;
	public static Resources resources;
	public Canvas canvas= new Canvas();
	public RelativeLayout view;
	
	final int PICK1 = Menu.FIRST + 1;
	final int PICK2 = Menu.FIRST + 2;
	final int PICK3 = Menu.FIRST + 3;
	final int PICK4 = Menu.FIRST + 4;
	final int PICK5 = Menu.FIRST + 5;
	
	
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem item1 = menu.add(0, PICK1, Menu.NONE, "Save List");
		MenuItem item2 = menu.add(0, PICK2, Menu.NONE, "Close App");
		MenuItem item3 = menu.add(0, PICK3, Menu.NONE, "Add Entry");
		MenuItem item4 = menu.add(0, PICK4, Menu.NONE, "Delete Entry");
		MenuItem item5 = menu.add(0, PICK5, Menu.NONE, "Update Entry");
		return true;
	}
}