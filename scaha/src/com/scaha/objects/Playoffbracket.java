package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class Playoffbracket extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String teamname = null;
	private String game1 = null;
	private String game2 = null;
	private String game3 = null;
	private String gametotal = null;
	private String place = null;
	
	public Playoffbracket (){ 
		
	}
	
	
	public String getTeamname(){
		return teamname;
	}
	
	public void setTeamname(String sName){
		teamname = sName;
	}
	
	public String getGame1(){
		return game1;
	}
	
	public void setGame1(String sid){
		game1 = sid;
	}

	public String getGame2() {
		return game2;
	}

	public void setGame2(String _tag) {
		game2 = _tag;
	}
	
	public void setGame3(String _tag) {
		game3 = _tag;
	}
	
	public String getGame3() {
		return game3;
	}
	
	public void setGametotal(String _tag) {
		gametotal = _tag;
	}
	
	public String getGametotal() {
		return gametotal;
	}
	
	public void setPlace(String _tag) {
		place = _tag;
	}
	
	public String getPlace() {
		return place;
	}
	
}
