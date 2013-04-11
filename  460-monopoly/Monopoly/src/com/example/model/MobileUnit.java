package com.example.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MobileUnit extends Unit {
	
	private Point targetPoint;
	private boolean moving = false;
	private double moveSpeed;
	private Tile currentTile;
	
	public MobileUnit(){
		this(new Point(), Unit.DEFAULT_OWNER);
	}
	
	public MobileUnit(Point position, int owner){
		this(position, owner, Unit.DEFAULT_RADIUS);
	}
	
	public MobileUnit(Point position, int owner, double radius){
		this(position, owner, radius, Unit.DEFAULT_MOVESPEED);
	}
	
	public MobileUnit(Point position, int owner, double radius, double moveSpeed){
		super(position, owner, radius);
		this.moveSpeed = moveSpeed;
	}
	
	//Commands
	public void move(double x, double y){
		this.move(new Point(x, y));
	}
	
	public void move(Point target){
		this.targetPoint = target;
		this.moving = true;
	}
	
	public void stop(){
		this.targetPoint = null;
		this.moving = false;
	}
	
	public void setMoveSpeed(double speed){
		if (speed > 0){
			this.moveSpeed = speed;
		} else {
			this.moveSpeed = Unit.DEFAULT_MOVESPEED;
		}
	}
	
	public double getMoveSpeed(){
		return this.moveSpeed;
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
