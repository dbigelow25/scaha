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

public class Tournament extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private Integer idtournament = null;
	private String tournamentname = null;
	private String contact = null;
	private String dates = null;
	private String phone = null;
	private String location = null;
	private String website = null;
	private String status = null;
	
	public Tournament (){ 
		
	}
	
	public Integer getIdtournament(){
    	return idtournament;
    }
    
    public void setIdtournament(Integer fname){
    	idtournament=fname;
    } 
	
    public String getTournamentname(){
    	return tournamentname;
    }
    
    public void setTournamentname(String fname){
    	tournamentname=fname;
    } 
	
	public String getContact(){
    	return contact;
    }
    
    public void setContact(String fname){
    	contact=fname;
    } 
	
	
	public String getDates(){
    	return dates;
    }
    
    public void setDates(String fname){
    	dates=fname;
    } 
	
	
	public String getPhone(){
    	return phone;
    }
    
    public void setPhone(String fname){
    	phone=fname;
    } 
	
	
	public String getLocation(){
    	return location;
    }
    
    public void setLocation(String fname){
    	location=fname;
    } 
	
	
	public String getWebsite(){
    	return website;
    }
    
    public void setWebsite(String fname){
    	website=fname;
    }
    
    public String getStatus(){
    	return status;
    }
    
    public void setStatus(String fname){
    	status=fname;
    }
	    	
}
