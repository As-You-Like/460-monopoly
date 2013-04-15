package com.example.model;

public class Point {
	public double x;
	public double y;
	
	public Point(){
		this(0, 0);
	}
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point getPolarOffset(double distance, double angle){
		Point p = new Point(this.x, this.y);
		p.x = p.x + distance + Math.cos(Math.toRadians(angle));
		p.y = p.y + distance + Math.sin(Math.toRadians(angle));
		return p;
	}
	
	public double getDistance(Point target){
		//get differences
		double x = this.x - target.x;
		double y = this.y - target.y;
		
		//square the differences
		x *= x;
		y *= y;
		
		//do the square root
		return Math.sqrt(x + y);
	}
	
	public double getAngle(Point target){
		double deltaX = target.x - this.x;
		double deltaY = target.y - this.y;
		
		return Math.atan2(deltaX, deltaY) * (180D/Math.PI);
	}
}
