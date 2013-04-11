package com.example.controllers;

public abstract class Event {
	public int expireTurn = -1;
	public String name = null;
	
	public abstract void action();
}
