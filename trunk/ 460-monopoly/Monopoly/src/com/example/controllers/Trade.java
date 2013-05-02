package com.example.controllers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.bluetooth.Message;
import com.example.model.Tile;
import com.example.model.Unit;

public class Trade {
	
	public int initiator;
	public int recipient;
	public static Trade entity;
	public ArrayList <Tile> initTiles;
	public ArrayList <Tile> recTiles;
	public ArrayList <Tile> initOffer;
	public ArrayList <Tile> recOffer;
	public double initCash;
	public double recCash;
	public double initOfferCash=0;
	public double recOfferCash=0;
	public boolean initAccept = false;
	public boolean recAccept = false;
	
	public Trade(int rID){
		entity=this;
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		try {
			json1.put("Player", initiator);
			json1.put("Cash", initCash);
			json1.put("Tiles", initTiles);
			json2.put("Player", recipient);
			json2.put("Cash", recCash);
			json2.put("Tiles", initTiles);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		initiator=Game.currentPlayer;
		recipient=rID;
		getInitProperties();
		getRecProperties();
		Device.player[recipient].sendMessage(Message.TRADE_INFORM, ""+json1);		
		Device.player[initiator].sendMessage(Message.TRADE_INFORM, ""+json2);
	}
	
	public void getInitProperties(){
		initCash=Player.entities[initiator].getBalance();
		for(int i=0; i<Unit.entity.size(); i++)
		{
			Unit u = Unit.entity.get(i);
			if (u instanceof Tile){
				if(u.getOwner()==initiator){
					initTiles.add((Tile)u);
				}
			}
		}
	}
	
	public void getRecProperties(){
		recCash=Player.entities[recipient].getBalance();
		for(int i=0; i<Unit.entity.size(); i++)
		{
			Unit u = Unit.entity.get(i);
			if (u instanceof Tile){
				if(u.getOwner()==recipient){
					recTiles.add((Tile)u);
				}
			}
		}
	}
	
	public void changeDetails(int sender, String m){
		int separator = m.indexOf(":");
		String upDown = m.substring(separator+1);
		int movement = Integer.parseInt(upDown);
		String mProcess;
		int tileId;
		
		if(initAccept){
			initAccept=false;
			Device.player[initiator].sendMessage(Message.TRADE_REJECT_INFORM,"");
			
		}
		else if(recAccept){
			recAccept=false;
			Device.player[recipient].sendMessage(Message.TRADE_REJECT_INFORM,"");
		}
		
		if(movement==1){
			if(sender==initiator){
				if(m.indexOf("$")!=-1){
					mProcess=m.substring(1,separator);
					initOfferCash=Double.parseDouble(mProcess);
					initCash=initCash-initOfferCash;
					
				}
				else{
					mProcess=m.substring(0,separator);
					tileId=Integer.parseInt(mProcess);
					for(int i=initTiles.size()-1; i>=0; i--){					
						if(tileId==initTiles.get(i).getOwner()){
							initOffer.add(initTiles.get(i));
							initTiles.remove(i);
						}
					}
				}
			}
			else{
				if(m.indexOf("$")!=-1){
					mProcess=m.substring(1,separator);
					recOfferCash=Double.parseDouble(mProcess);
					recCash=recCash-recOfferCash;
				}
				else{
					mProcess=m.substring(0,separator);
					tileId=Integer.parseInt(mProcess);
					for(int i=recTiles.size()-1; i>=0; i--){
						if(tileId==recTiles.get(i).getOwner()){
							recOffer.add(recTiles.get(i));
							recTiles.remove(i);
						}
					}
				}
			}
		}
		else{
			if(sender==initiator){
				if(m.indexOf("$")!=-1){
					mProcess=m.substring(1,separator);
					initCash=initOfferCash+initCash;
					initOfferCash=0;
					Log.d(null, "AAAAAAAND ITS GONE");
					
				}
				else{
					mProcess=m.substring(0,separator);
					tileId=Integer.parseInt(mProcess);
					for(int i=initOffer.size()-1; i>=0; i--){
						if(tileId==initOffer.get(i).getOwner()){
							initTiles.add(initOffer.get(i));
							initOffer.remove(i);
						}
					}
				}
					
			}
			else{
				if(m.indexOf("$")!=-1){
					mProcess=m.substring(1,separator);
					recCash=recOfferCash+recCash;
					recOfferCash=0;
					Log.d(null,"AAAAAAAND ITS GONE");
				}
				else{
					mProcess=m.substring(0,separator);
					tileId=Integer.parseInt(mProcess);
					for(int i=recOffer.size()-1; i>=0; i--){
						recTiles.add(recOffer.get(i));
						recOffer.remove(i);
					}
				}
			}
		}
		Device.player[recipient].sendMessage(Message.TRADE_UPDATE_DETAILS,m+":"+sender);
		Device.player[initiator].sendMessage(Message.TRADE_UPDATE_DETAILS,m+":"+sender);
	}
	
	public void acceptTrade(int sender){
		if(sender==initiator){
			initAccept=true;
			Device.player[recipient].sendMessage(Message.TRADE_ACCEPT_INFORM,"");
		}
		else if(sender==recipient){
			recAccept=true;
			Device.player[initiator].sendMessage(Message.TRADE_ACCEPT_INFORM,"");
		}
		
		if(initAccept&&recAccept){
			successfulTrade();
		}
		
	}
	
	public void rejectTrade(int sender){
		if(sender==initiator){
			recAccept=false;
			Device.player[recipient].sendMessage(Message.TRADE_REJECT_INFORM,"");
		}
		else if(sender==recipient){
			initAccept=false;
			Device.player[initiator].sendMessage(Message.TRADE_REJECT_INFORM,"");
		}
	}
	public void successfulTrade(){
		initCash=initCash+(recOfferCash-initOfferCash);
		recCash=recCash+(initOfferCash-recOfferCash);
		
		for(int i=0; i<initOffer.size(); i++){
			initOffer.get(i).setOwner(recipient);
		}
		for(int i=0; i<recOffer.size(); i++){
			recOffer.get(i).setOwner(initiator);
		}
		Device.player[initiator].sendMessage(Message.TRADE_SUCCESS,"");
		Device.player[recipient].sendMessage(Message.TRADE_SUCCESS, "");
	}
	

}
