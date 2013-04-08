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
}
