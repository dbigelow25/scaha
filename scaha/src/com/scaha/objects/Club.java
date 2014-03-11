package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Club extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String clubname = null;
	private String idclub = null;
	
	public Club (){ 
		
		
	}
	
	public String getClubname(){
		return clubname;
	}
	
	public void setClubname(String sName){
		clubname = sName;
	}
	
	public String getClubid(){
		return idclub;
	}
	
	public void setClubid(String sName){
		idclub = sName;
	}
	
	
}
