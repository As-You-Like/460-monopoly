package com.example.controllers;

import com.example.monopoly.MapActivity;
import com.example.monopoly.SaveGameActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DatabaseReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {	
		  
          Log.v("RECEIVER", 
                 "Broadcast received");
           
           if(intent.getAction() == "Bentley.action.GOSERVICE"){
        	   Log.i("", "GOSERVICE - starting MapActivity");
        	   Intent intent2 = new Intent(context, MapActivity.class);
               context.startActivity(intent2); 
           }
           else if(intent.getAction() == "Bentley.action.POPULATELISTVIEW"){
        	   Log.i("", "POPULATELISTVIEW - running SaveGameActivity.populateListView()");
        	   SaveGameActivity.populateListView();
           }
           else if(intent.getAction() == "Bentley.action.MAKEGAME"){
        	   Log.i("", "MAKEGAME - constructing Game object");
        	   new Game("");
        	   DatabaseThread.gameMade = true;
           }
           else {
        	   Log.i("", "Database Receiver got nothing");
           }
		   
    }

}
