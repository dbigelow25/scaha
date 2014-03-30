package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;
import com.gbli.context.ContextManager;

public class ScahaTeam extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// This holds the Club That owns the team
	//
	private Club TeamClub = null;
	private CoachList Coachs = null;
	private ManagerList Managers = null;
	
	private Division TeamDivision = null;
	private SkillLevel TeamSkillLevel = null;
	
	private String TeamName = null;
	private String TeamGender = null;    
	private String SeasonTag = null;
	private String SkillLevelTag = null;
	private String DivisionTag = null;
	private String ScheduleTags = null;
	private int IsExhibition = 0;
	
	
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
		LOGGER.info("Looking for Head Coach for Team:" + this.getTeamName());
		if (this.Coachs == null) return null;
		for (ScahaCoach hc : Coachs) {
			LOGGER.info("\tLooking at coach:" + hc.getsLastName() + ":" + hc.getGenatt().get("ROSTERTYPE"));
			if (hc.getGenatt().get("ROSTERTYPE").equals("Head Coach")) return hc;
		}
		return  null;
	}
	
	public ScahaManager getManager() {
		LOGGER.info("Looking for Manager for Team:" + this.getTeamName());
		if (this.Managers == null) return null;
		for (ScahaManager hc : Managers) {
			LOGGER.info("\tLooking at Manager:" + hc.getsLastName() + ":" + hc.getGenatt().get("ROSTERTYPE"));
			if (hc.getGenatt().get("ROSTERTYPE").equals("Manager")) return hc;
		}
		return  null;
	}
	/**
	 * @return the teamDivision
	 */
	public Division getTeamDivision() {
		return TeamDivision;
	}

	/**
	 * @param teamDivision the teamDivision to set
	 */
	public void setTeamDivision(Division teamDivision) {
		TeamDivision = teamDivision;
	}

	/**
	 * @return the teamSkillLevel
	 */
	public SkillLevel getTeamSkillLevel() {
		return TeamSkillLevel;
	}

	/**
	 * @param teamSkillLevel the teamSkillLevel to set
	 */
	public void setTeamSkillLevel(SkillLevel teamSkillLevel) {
		TeamSkillLevel = teamSkillLevel;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return TeamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		TeamName = teamName;
	}

	/**
	 * @return the teamGender
	 */
	public String getTeamGender() {
		return TeamGender;
	}

	/**
	 * @param teamGender the teamGender to set
	 */
	public void setTeamGender(String teamGender) {
		TeamGender = teamGender;
	}

	/**
	 * @return the seasonTag
	 */
	public String getSeasonTag() {
		return SeasonTag;
	}

	/**
	 * @param seasonTag the seasonTag to set
	 */
	public void setSeasonTag(String seasonTag) {
		SeasonTag = seasonTag;
	}

	/**
	 * @return the skillLevelTag
	 */
	public String getSkillLevelTag() {
		return SkillLevelTag;
	}

	/**
	 * @param skillLevelTag the skillLevelTag to set
	 */
	public void setSkillLevelTag(String skillLevelTag) {
		SkillLevelTag = skillLevelTag;
	}

	/**
	 * @return the divisionTag
	 */
	public String getDivisionTag() {
		return DivisionTag;
	}

	/**
	 * @param divisionTag the divisionTag to set
	 */
	public void setDivisionTag(String divisionTag) {
		DivisionTag = divisionTag;
	}

	/**
	 * @return the isExhibition
	 */
	public int getIsExhibition() {
		return IsExhibition;
	}

	public String isExhibitionTeam() {
		return (IsExhibition == 1 ? "Yes" : "No");
	}
	/**
	 * @param isExhibition the isExhibition to set
	 */
	public void setIsExhibition(int isExhibition) {
		IsExhibition = isExhibition;
	}

	/**
	 * @return the scheduleTags
	 */
	public String getScheduleTags() {
		return ScheduleTags;
	}

	/**
	 * @param scheduleTags the scheduleTags to set
	 */
	public void setScheduleTags(String scheduleTags) {
		ScheduleTags = scheduleTags;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScahaTeam [id=" + ID + ", TeamClub=" + TeamClub + ", TeamDivision="
				+ TeamDivision + ", TeamSkillLevel=" + TeamSkillLevel
				+ ", TeamName=" + TeamName + ", TeamGender=" + TeamGender
				+ ", SeasonTag=" + SeasonTag + ", SkillLevelTag="
				+ SkillLevelTag + ", DivisionTag=" + DivisionTag
				+ ", ScheduleTags=" + ScheduleTags + ", IsExhibition="
				+ IsExhibition + "]";
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
	
		
}
