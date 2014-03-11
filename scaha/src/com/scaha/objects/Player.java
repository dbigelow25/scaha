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
	private String firstname = null;
	private String lastname = null;
	private String IDplayer = null;
	private String currentteam = null;
	private String previousteam = null;
	private String citizenship = null;
	private Boolean citizenshiptransfer = null;
	private String citizenshipexpirationdate = null;
	private Boolean ctverified = null;
	private String birthcertificate = null;
	private Boolean bcverified = null;
	private String dob = null;
	private String loidate = null;
	private String playerup = null;
	private String citizenshiplabel = null;
	
	public Player (){ 
		
	}
	
	public String getFirstname(){
    	return firstname;
    }
    
    public void setFirstname(String fname){
    	firstname=fname;
    } 
	
    public String getLastname(){
    	return lastname;
    }
    
    public void setLastname(String lname){
    	lastname=lname;
    }
    
	public String getCitizenshiplabel(){
	    	return citizenshiplabel;
	    }
	    
	    public void setCitizenshiplabel(String selidplayer){
	    	citizenshiplabel=selidplayer;
	    }
	
	public String getCTExpirationdate(){
		return citizenshipexpirationdate;
	}
	
	public void setCTExpirationdate(String sname){
		citizenshipexpirationdate = sname;
	}
	
	public String getPlayerup(){
		return playerup;
	}
	
	public void setPlayerup(String sName){
		playerup = sName;
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
		if (sName.equals("0")){
			birthcertificate = "Needed";
		} else {
			birthcertificate = "";
		}
		
	}
	
	public Boolean getBcverified(){
		return bcverified;
	}
	
	public void setBcverified(String sName){
		if (sName.equals("0")){
			bcverified = false;
		} else {
			bcverified = true;
		}
		
	}
	
	public Boolean getCitizenshiptransfer(){
		return citizenshiptransfer;
	}
	
	public void setCitizenshiptransfer(String sName){
		if (sName!=null){
			if (sName.equals("0")){
				citizenshiptransfer = true;
			} else {
				citizenshiptransfer = false;
			}
		} else {
			citizenshiptransfer = true;
		}
		
		
	}
	
	public Boolean getCtverified(){
		return ctverified;
	}
	
	public void setCtverified(String sName){
		if (sName!=null){
			if (sName.equals("1")){
				ctverified = false;
			} else {
				ctverified = true;
			}
		} else {
			ctverified = false;
		}
		
		
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
