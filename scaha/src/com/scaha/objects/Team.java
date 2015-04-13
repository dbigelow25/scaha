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
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	public String teamname = null;
	private String IDteam = null;
	private String skillname = null;
	private String division_name = null;
	private String activeplayercount = null;
	private String totalplayercount = null;
	private String totalcoachescount = null;
	private String gmcount = null;
	private String gp = null;
	private String minorpenalties = null;
	private String majorpenalties = null;
	private String pims = null;
	private String matchcount = null;
	
	
	//this is used for assigning coach role to teams on the coach loi process.
	private String coachrole = null;
	
	public Team (String sName, String teamid){ 
		teamname = sName;
		IDteam = teamid;
	}
	
	public String getMatchcount(){
		return matchcount;
	}
	
	public void setMatchcount(String sName){
		matchcount = sName;
	}
	
	public String getPims(){
		return pims;
	}
	
	public void setPims(String sName){
		pims = sName;
	}
	
	public String getMajorpenalties(){
		return majorpenalties;
	}
	
	public void setMajorpenalties(String sName){
		majorpenalties = sName;
	}
	
	public String getMinorpenalties(){
		return minorpenalties;
	}
	
	public void setMinorpenalties(String sName){
		minorpenalties = sName;
	}
	
	public String getGp(){
		return gp;
	}
	
	public void setGp(String sName){
		gp = sName;
	}
	
	public String getGmcount(){
		return gmcount;
	}
	
	public void setGmcount(String sName){
		gmcount = sName;
	}
	
	public String getCoachrole(){
		return coachrole;
	}
	
	public void setCoachrole(String sName){
		coachrole = sName;
	}
	
	
	public String getTotalcoachescount(){
		return totalcoachescount;
	}
	
	public void setTotalcoachescount(String sName){
		totalcoachescount = sName;
	}
	
	
	public String getTotalplayercount(){
		return totalplayercount;
	}
	
	public void setTotalplayercount(String sName){
		totalplayercount = sName;
	}
	
	public String getActiveplayercount(){
		return activeplayercount;
	}
	
	public void setActiveplayercount(String sName){
		activeplayercount = sName;
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
		return teamname;
	}
	
	public void setTeamname(String sName){
		teamname = sName;
	}
	
	public String getIdteam(){
		return IDteam;
	}
	
	public void setIdteam(String steamid){
		IDteam = steamid;
	}
	
	
		
}
