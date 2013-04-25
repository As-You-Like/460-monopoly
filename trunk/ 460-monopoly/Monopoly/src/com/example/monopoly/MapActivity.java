package com.example.monopoly;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.controllers.GameThread;
import com.example.monopoly.PanAndZoomListener;
import com.example.monopoly.PanAndZoomListener.Anchor;


public class MapActivity extends Activity { 
	
	public static MapActivity activity;
	public CircleView c;
	public static Resources resources;
	public Canvas canvas= new Canvas();
	public FrameLayout view;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		activity = this;
		
		  FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams (FrameLayout.LayoutParams.MATCH_PARENT, 
                  FrameLayout.LayoutParams.MATCH_PARENT, 
                  Gravity.TOP | Gravity.LEFT);
		
		view = new FrameLayout (this);
		setContentView (view);
		
		//Canvas canvas; // Creates the canvas used to draw the map 
		//ImageView imageView = new ImageView(this);
		//view.addView(imageView, fp);
		Context context = this.getApplicationContext();
		c = new CircleView(context);
		
		view.addView(c);
		resources = c.getResources();
		
		//Toast.makeText(this, "This is Map!!", Toast.LENGTH_SHORT).show();
		
		
		
		//PanAndZoomListener testing = new PanAndZoomListener(view, c, Anchor.TOPLEFT);
		
		//testing.onTouch(view, motionEvent);

		//GameThread.gt.start();
		

	}
	
	public void onStart()
	{
		//Log.e(null, "MapActivity");
		super.onStart();
		view.setOnTouchListener(new PanAndZoomListener(view, c, Anchor.TOPLEFT));

		//onTouch(c, motionEvent);
		
		
	}
	
	/*public void drawOnCanvas (Canvas canvas) {
	    Paint paint = new Paint();
	    paint.setColor(Color.BLUE);
	    Point p1 = new Point (200, 200);
	    canvas.drawCircle (p1.x, p1.y, 100, paint);
	}*/
	
	

}