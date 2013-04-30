package com.example.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLHelper extends SQLiteOpenHelper {

	    public SQLHelper(Context c) {
	      super(c, DatabaseThread.DATABASE_NAME, null, 1);
	    }

	    // Called when no database exists and the helper class needs
	    // to create a new one. 

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}

	}