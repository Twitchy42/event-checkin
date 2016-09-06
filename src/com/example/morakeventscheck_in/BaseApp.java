package com.example.morakeventscheck_in;

import android.app.Application;

public class BaseApp extends Application{

	private static BaseApp instance = null;
	private String user;
	
	@Override
	public void onCreate(){
		super.onCreate();
		instance = this;
	}
	
	public static BaseApp getInstance(){
		return instance;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String u){
		user = u;
	}
}
