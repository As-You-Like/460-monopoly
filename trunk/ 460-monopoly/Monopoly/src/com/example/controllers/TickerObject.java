package com.example.controllers;

import java.util.ArrayList;

import android.util.Log;

public class TickerObject {

	private String eventText; // String for the message that will scroll on the ticker
	private int turnCount; // Amount of turns left in displaying message on ticker
	public static ArrayList<TickerObject> entity = new ArrayList<TickerObject>();
	
	
	public TickerObject(String name, int count) // Constructor gets passed string name and number of turns to display
	{
		eventText=name;
		turnCount=count;
		
		entity.add(this);
	}
	
	// Mutators and accessors
	public void setEventText(String e)
	{
		eventText=e;
	}
	public String getEventText()
	{
		return eventText;
	}
	
	public void setTurnCount(int t)
	{
		turnCount=t;
	}
	public int getTurnCount()
	{
		return turnCount;
	}
	
	public void decreaseCount() // Method to decrease number of turns left to display
	{
		turnCount--;
	}

	public static void passTurn(ArrayList<TickerObject> events) {
		for (int i=0; i<events.size(); i++)
		{
			events.get(i).decreaseCount();
		}
		
	}
	
	public static ArrayList<TickerObject> checkOld(ArrayList<TickerObject> e) {
		ArrayList<TickerObject> updatedList = new ArrayList<TickerObject>();
		
		for(int i=0; i<e.size(); i++)
		{
			if(e.get(i).turnCount>0)
			{
				updatedList.add(e.get(i));
			}

		}
		
		return updatedList;
	}
	
}
