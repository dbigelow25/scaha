package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Division extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String DivisionName = null;
	private Integer IDdivision = null;
	
	public Division (){ 
		
	}
	
	public String getDivisionname(){
		return DivisionName;
	}
	
	public void setDivisionname(String sName){
		DivisionName = sName;
	}
	
	public Integer getIddivision(){
		return IDdivision;
	}
	
	public void setIddivision(Integer steamid){
		IDdivision = steamid;
	}
	
	
		
}
