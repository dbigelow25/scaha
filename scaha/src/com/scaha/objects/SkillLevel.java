package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class SkillLevel extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	
	private String SkillLevelName = null;
	private Integer IDskilllevel = null;
	private String Tag = null;
	
	public SkillLevel (){ 
		
	}
	
	public SkillLevel (Profile _pro, int _id) {
		ID = _id;
		setProfile(_pro);
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

	/**
	 * @return the tag
	 */
	public String getTag() {
		return Tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		Tag = tag;
	}
	
	
		
}
