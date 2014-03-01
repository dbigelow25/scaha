package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class SkillLevel extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String SkillLevelName = null;
	private Integer IDskilllevel = null;
	
	public SkillLevel (){ 
		
	}
	
	public String getSkilllevelname(){
		return SkillLevelName;
	}
	
	public void setSkilllevelname(String sName){
		SkillLevelName = sName;
	}
	
	public Integer getIdskilllevel(){
		return IDskilllevel;
	}
	
	public void setIdskilllevel(Integer sid){
		IDskilllevel = sid;
	}
	
	
		
}
