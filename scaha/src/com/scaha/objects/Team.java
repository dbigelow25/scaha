package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Team extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String TeamName = null;
	private String IDteam = null;
	private String skillname = null;
	private String division_name = null;
	
	public Team (String sName, String teamid){ 
		
		TeamName = sName;
		IDteam = teamid;
		
	
	}
	
	public String getSkillname(){
		return skillname;
	}
	
	public void setSkillname(String sName){
		skillname = sName;
	}
	
	public String getDivisionname(){
		return division_name;
	}
	
	public void setDivisionname(String sName){
		division_name = sName;
	}
	
	
	public String getTeamname(){
		return TeamName;
	}
	
	public void setTeamname(String sName){
		TeamName = sName;
	}
	
	public String getIdteam(){
		return IDteam;
	}
	
	public void setIdteam(String steamid){
		IDteam = steamid;
	}
	
	
		
}
