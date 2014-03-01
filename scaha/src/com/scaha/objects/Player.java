package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Player extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String PlayerName = null;
	private String IDplayer = null;
	private String currentteam = null;
	private String previousteam = null;
	private String citizenship = null;
	private String birthcertificate = null;
	private String dob = null;
	private String loidate = null;
	
	public Player (){ 
		
	}
	
	public String getLoidate(){
		return loidate;
	}
	
	public void setLoidate(String sName){
		loidate = sName;
	}
	
	public String getDob(){
		return dob;
	}
	
	public void setDob(String sName){
		dob = sName;
	}
	
	public String getBirthcertificate(){
		return birthcertificate;
	}
	
	public void setBirthcertificate(String sName){
		birthcertificate = sName;
	}
	
	public String getCitizenship(){
		return citizenship;
	}
	
	public void setCitizenship(String sName){
		citizenship = sName;
	}
	
	
	public String getIdplayer(){
		return IDplayer;
	}
	
	public void setIdplayer(String sName){
		IDplayer = sName;
	}
	
	public String getPlayername(){
		return PlayerName;
	}
	
	public void setPlayername(String sName){
		PlayerName = sName;
	}
	
	public String getCurrentteam(){
		return currentteam;
	}
	
	public void setCurrentteam(String sName){
		currentteam = sName;
	}
	
	public String getPreviousteam(){
		return previousteam;
	}
	
	public void setPreviousteam(String steamid){
		previousteam = steamid;
	}
	
	
		
}
