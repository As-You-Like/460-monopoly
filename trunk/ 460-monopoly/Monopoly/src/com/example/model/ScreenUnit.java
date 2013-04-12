package com.example.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ScreenUnit extends MobileUnit{ 

	//Commands
	public void move(double x, double y){
		this.move(new Point(x, y));
	}
	public void move(Point target){
		//stub method
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		// TODO Auto-generated method stub
		
	}
	
	public void onTimer(){
		
	}

}
