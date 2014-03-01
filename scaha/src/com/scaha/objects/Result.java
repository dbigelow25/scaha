package com.scaha.objects;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class Result extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String playername = null;
	private String idplayer = null;
	private String address = null;
	private String dob = null;
	
	
	public Result (String sName, String playerid,String saddress, String sdob){ 
		
		playername = sName;
		idplayer = playerid;
		address = saddress;
		dob = sdob;
	
	}
	
	public String getPlayername(){
		return playername;
	}
	
	public void setPlayername(String sName){
		playername = sName;
	}
	
	public String getIdplayer(){
		return idplayer;
	}
	
	public void setIdplayer(String playerid){
		idplayer = playerid;
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setAddress(String saddress){
		address = saddress;
	}
	
	public String getDob(){
		return dob;
	}
	
	public void setDob(String sdob){
		dob = sdob;
	}
	
	
}
