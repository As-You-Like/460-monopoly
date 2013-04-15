package com.example.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

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
	
	public void update(){
		//Movement
		if (moving){
			Log.e("update", "moving");
			
			//temporary instant teleport code
			this.setPosition(this.targetPoint);
			this.moving = false;
			// ================== DISABLED MOVEMENT CODE DUE TO INNACURACIES
			/*Point tmpCurrent;
			Point tmpTarget;
			
			synchronized (this){
				tmpCurrent = this.getPosition();
				tmpTarget = this.targetPoint;
			}
			
			double angle = tmpCurrent.getAngle(tmpTarget);
			double distance = tmpCurrent.getDistance(tmpTarget);
			
			double displacement;
			if (distance < this.moveSpeed){
				displacement = distance;
				this.moving = false;
			} else {
				displacement = this.moveSpeed;
			}
			
			Point newPosition = tmpCurrent.getPolarOffset(displacement, angle);
			this.setPosition(newPosition);*/
		}
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
