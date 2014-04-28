package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;
import com.gbli.context.ContextManager;

public class ScahaTeam extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 45L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// This holds the Club That owns the team
	//
	private Club TeamClub = null;
	private CoachList Coachs = null;
	private ManagerList Managers = null;
	
	private Division teamdivision = null;
	private SkillLevel teamskilllevel = null;
	
	private String teamname = null;
	private String teamgender = null;    
	private String seasontag = null;
	private String scheduletags = null;
	private int isexhibition = 0;
	private String skillleveltag = null;
	private String divisiontag = null;


	public ScahaTeam (Profile _pro, int _id) {
		setProfile(_pro);
		this.ID = _id;
	}

	/**
	 * @return the teamClub
	 */
	public Club getTeamClub() {
		return TeamClub;
	}

	/**
	 * @param teamClub the teamClub to set
	 */
	public void setTeamClub(Club teamClub) {
		TeamClub = teamClub;
	}

	public ScahaCoach getHeadCoach() {
		if (this.Coachs == null) return null;
		for (ScahaCoach hc : Coachs) {
			if (hc.getGenatt().get("ROSTERTYPE").equals("Head Coach")) return hc;
		}
		return  null;
	}
	
	public ScahaManager getManager() {
		if (this.Managers == null) return null;
		for (ScahaManager hc : Managers) {
			if (hc.getGenatt().get("ROSTERTYPE").equals("Manager")) return hc;
		}
		return  null;
	}
	
	/**
	 * @return the coachs
	 */
	public CoachList getCoachs() {
		return Coachs;
	}

	/**
	 * @param coachs the coachs to set
	 */
	public void setCoachs(CoachList coachs) {
		Coachs = coachs;
	}

	/**
	 * @return the managers
	 */
	public ManagerList getManagers() {
		return Managers;
	}

	/**
	 * @param managers the managers to set
	 */
	public void setManagers(ManagerList managers) {
		Managers = managers;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getTeamname() + " " + this.getDivisiontag() + this.getSkillleveltag();
	}

	/**
	 * @return the isexhibition
	 */
	public int getIsexhibition() {
		return isexhibition;
	}

	/**
	 * @param isexhibition the isexhibition to set
	 */
	public void setIsexhibition(int isexhibition) {
		this.isexhibition = isexhibition;
	}

	/**
	 * @return the scheduletags
	 */
	public String getScheduletags() {
		return scheduletags;
	}

	/**
	 * @param scheduletags the scheduletags to set
	 */
	public void setScheduletags(String scheduletags) {
		this.scheduletags = scheduletags;
	}

	/**
	 * @return the seasontag
	 */
	public String getSeasontag() {
		return seasontag;
	}

	/**
	 * @param seasontag the seasontag to set
	 */
	public void setSeasontag(String seasontag) {
		this.seasontag = seasontag;
	}

	/**
	 * @return the teamgender
	 */
	public String getTeamgender() {
		return teamgender;
	}

	/**
	 * @param teamgender the teamgender to set
	 */
	public void setTeamgender(String teamgender) {
		this.teamgender = teamgender;
	}

	/**
	 * @return the teamdivision
	 */
	public Division getTeamdivision() {
		return teamdivision;
	}

	/**
	 * @param teamdivision the teamdivision to set
	 */
	public void setTeamdivision(Division teamdivision) {
		this.teamdivision = teamdivision;
	}

	/**
	 * @return the teamskilllevel
	 */
	public SkillLevel getTeamskilllevel() {
		return teamskilllevel;
	}

	/**
	 * @param teamskilllevel the teamskilllevel to set
	 */
	public void setTeamskilllevel(SkillLevel teamskilllevel) {
		this.teamskilllevel = teamskilllevel;
	}

	/**
	 * @return the teamname
	 */
	public String getTeamname() {
		return teamname;
	}

	/**
	 * @param teamname the teamname to set
	 */
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	/**
	 * @return the skillleveltag
	 */
	public String getSkillleveltag() {
		return skillleveltag;
	}

	/**
	 * @param skillleveltag the skillleveltag to set
	 */
	public void setSkillleveltag(String skillleveltag) {
		this.skillleveltag = skillleveltag;
	}

	/**
	 * @return the divisiontag
	 */
	public String getDivisiontag() {
		return divisiontag;
	}

	/**
	 * @param divisiontag the divisiontag to set
	 */
	public void setDivisiontag(String divisiontag) {
		this.divisiontag = divisiontag;
	}
	
		
}
