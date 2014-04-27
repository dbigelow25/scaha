package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class TournamentGame extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private String date = null;
	private String opponent = null;
	private String location = null;
	private Integer idgame = null;
	private String time = null;
	private String tournamentname = null;
	private String status = null;
	private Boolean rendered = null;
	
	public TournamentGame (){ 
		
	}
	
	public Boolean getRendered(){
    	return rendered;
    }
    
    public void setRendered(Boolean fname){
    	rendered=fname;
    }
	
	public String getDate(){
    	return date;
    }
    
    public void setDate(String fname){
    	date=fname;
    } 
	
	
	public String getOpponent(){
    	return opponent;
    }
    
    public void setOpponent(String fname){
    	opponent=fname;
    } 
	
	public String getTournamentname(){
    	return tournamentname;
    }
    
    public void setTournamentname(String fname){
    	tournamentname=fname;
    } 
	
    public String getStatus(){
    	return status;
    }
    
    public void setStatus(String fname){
    	status=fname;
    } 
	
    
	
	public String getLocation(){
    	return location;
    }
    
    public void setLocation(String fname){
    	location=fname;
    } 
	
	
	public Integer getIdgame(){
    	return idgame;
    }
    
    public void setIdgame(Integer fname){
    	idgame=fname;
    } 
	
	
	public String getTime(){
    	return time;
    }
    
    public void setTime(String fname){
    	time=fname;
    } 
	
		
}
