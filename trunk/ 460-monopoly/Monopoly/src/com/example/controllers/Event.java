package com.example.controllers;

public abstract class Event {
	public int expireTurn = -1;
	public int eventNumber = -1;
	public int player = -1;
	
	public abstract void action();
}
