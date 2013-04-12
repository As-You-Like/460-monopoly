package com.example.model;

import java.io.Serializable;

import android.util.Log;

public class TileProperty implements Serializable {

	public static final long serialVersionUID = 1L;

	public String itemName; 
	public String itemOwned; 
	public String itemValue; 
	
	public TileProperty() {
	}

	public String getName() {
		return itemName;
	}

	public void setName(String name) {
		this.itemName = name;
	}

	public String getOwned() {
		return itemOwned;
	}

	public void setOwned(String data) {
		this.itemOwned = data;
	}
	public String getValue() {
		return itemValue;
	}
	
	public void setValue(String data) {
		this.itemValue = data;
	}


}
