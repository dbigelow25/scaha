package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import org.primefaces.event.CellEditEvent;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class RosterEdit extends ScahaObject implements Serializable {
	
	//this class is used for the rosteredit by team managers and has a special method for setting property on jerseynumber
	//the method checks to see if new value being set is different than the value loaded from the db in the oldjerseynumber param
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String firstname = null;
	private String lastname = null;
	private String IDplayer = null;
	private String oldjerseynumber = null;
	private String jerseynumber = null;
	
	public RosterEdit (){ 
		
	}
	
	public String getJerseynumber(){
    	return jerseynumber;
    }
    
    public void setJerseynumber(String fname){
    	if (fname!=oldjerseynumber){
    		jerseynumber=fname;
    	}	
    		//need to set and execute db call here
    		 else {
    		jerseynumber=fname;
    	}
    	
    } 
	
    public String getOldjerseynumber(){
    	return oldjerseynumber;
    }
    
    public void setOldjerseynumber(String fname){
    	oldjerseynumber=fname;
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
    
	public String getIdplayer(){
		return IDplayer;
	}
	
	public void setIdplayer(String sName){
		IDplayer = sName;
	}
	
		
		
}
