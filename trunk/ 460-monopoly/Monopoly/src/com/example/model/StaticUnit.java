package com.example.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public class StaticUnit extends Unit {
	
	public StaticUnit(Point position, int owner, double radius){
		super(position, owner, radius);
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimer() {
		// TODO Auto-generated method stub
		
	}

}
