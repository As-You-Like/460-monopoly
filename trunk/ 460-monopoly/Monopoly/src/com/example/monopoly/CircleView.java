package com.example.monopoly;

import com.example.content.Image;
import com.example.model.Unit;
import com.example.monopoly.PanZoomView;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;

/**
 * This view displays a few rectangles that can be panned and zoomed.
 * It is a subclass of PanZoomView, which provides most of the code to
 * support zooming and panning.
 */

public class CircleView extends PanZoomView { 
/**
 */
public CircleView (Context context) {
    super (context);
    Log.d(null, "CircleView Constructor1");
}

public CircleView (Context context, AttributeSet attrs) {
    super (context, attrs);
    Log.d(null, "CV Constructor2");
   
}

public CircleView (Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    Log.d(null, "CV Constructor3");

}

/**
 * Do whatever drawing is appropriate for this view.
 * The canvas object is already set up to be drawn on. That means that all translations and scaling
 * operations have already been done.
 * 
 * @param canvas Canvas
 * @return void
 */

public void drawOnCanvas (Canvas canvas) {
	//Ensure it doesn't attempt to render before MapActivity is ready
	
	//Log.d(null, "CV drawOnCanvas");
	
	if (MapActivity.activity == null){
		return;
	}

	//Initialize paint
    Paint paint = new Paint();
    //paint.setColor(Color.BLUE);
    
    //canvas.drawBitmap(b, 0, 0, paint);
    Unit[] tmp;
    synchronized (Unit.entity){
    	tmp = Unit.entity.toArray(new Unit[]{});
    	
    }
    //canvas.drawText("Unit Count: " + tmp.length, 0, 0, paint);
    for (Unit u : tmp){
    	u.draw(canvas, paint);
    	
    }
    
    this.invalidate();
   // Log.e(null, "CircleView drawOnCanvas");
	//super.onDraw(canvas);
    

}


/**
 * Return the resource id of the sample image. Note that this class always returns 0, indicating
 * that there is no sample drawable.
 * 
 * @return int
 */

public int sampleDrawableId () {
	  Log.d(null, "CV.sampleDrawableID");
	return 0;
}

/**
 * Return true if panning is supported.
 * 
 * @return boolean
 */

public boolean supportsPan () {
	  Log.d(null, "Cv.supportsPan");
	return true;
}

/**
 * Return true if scaling is done around the focus point of the pinch.
 * 
 * @return boolean
 */

public boolean supportsScaleAtFocusPoint () {
	  Log.d(null, "CV.supportsScaleAtFocusPoint");
	return true;
}

/**
 * Return true if pinch zooming is supported.
 * 
 * @return boolean
 */

public boolean supportsZoom () {
	  Log.d(null, "CV.supportsZooms");
	return true;
}



} // end class
