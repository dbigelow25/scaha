package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class LiveGame extends ScahaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private Schedule sched = null;
	private ScahaTeam  hometeam = null;
	private ScahaTeam  awayteam = null;
	private Participant homepart = null;  // Not sure if we really need this
	private Participant awaypart = null;  // Not sure if we really need this

	private String hometeamname = null;
	private String awayteamname = null;
	private String startdate = null;
	private String starttime = null;
	private String typetag = null;
	private String statetag = null;
	private int homescore = 0;
	private int awayscore = 0;
	private String venuetag = null;
	private String sheetname = null;
	
	public LiveGame(int _id, Profile _pro, Schedule _sc) {
		ID = _id;		
		this.setProfile(_pro);
	}

	/**
	 * @return the sched
	 */
	public Schedule getSched() {
		return sched;
	}
	
	public int getGameId() {
		return ID;
	}

	/**
	 * @param sched the sched to set
	 */
	public void setSched(Schedule sched) {
		this.sched = sched;
	}

	/**
	 * @return the hometeam
	 */
	public ScahaTeam getHometeam() {
		return hometeam;
	}

	/**
	 * @param hometeam the hometeam to set
	 */
	public void setHometeam(ScahaTeam hometeam) {
		this.hometeam = hometeam;
	}

	/**
	 * @return the awayteam
	 */
	public ScahaTeam getAwayteam() {
		return awayteam;
	}

	/**
	 * @param awayteam the awayteam to set
	 */
	public void setAwayteam(ScahaTeam awayteam) {
		this.awayteam = awayteam;
	}

	/**
	 * @return the homepart
	 */
	public Participant getHomepart() {
		return homepart;
	}

	/**
	 * @param homepart the homepart to set
	 */
	public void setHomepart(Participant homepart) {
		this.homepart = homepart;
	}

	/**
	 * @return the awaypart
	 */
	public Participant getAwaypart() {
		return awaypart;
	}

	/**
	 * @param awaypart the awaypart to set
	 */
	public void setAwaypart(Participant awaypart) {
		this.awaypart = awaypart;
	}

	/**
	 * @return the hometeamname
	 */
	public String getHometeamname() {
		return hometeamname;
	}

	/**
	 * @param hometeamname the hometeamname to set
	 */
	public void setHometeamname(String hometeamname) {
		this.hometeamname = hometeamname;
	}

	/**
	 * @return the awayteamname
	 */
	public String getAwayteamname() {
		return awayteamname;
	}

	/**
	 * @param awayteamname the awayteamname to set
	 */
	public void setAwayteamname(String awayteamname) {
		this.awayteamname = awayteamname;
	}

	/**
	 * @return the startdate
	 */
	public String getStartdate() {
		return startdate;
	}

	/**
	 * @param startdate the startdate to set
	 */
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @param starttime the starttime to set
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * @return the typetag
	 */
	public String getTypetag() {
		return typetag;
	}

	/**
	 * @param typetag the typetag to set
	 */
	public void setTypetag(String typetag) {
		this.typetag = typetag;
	}

	/**
	 * @return the statetag
	 */
	public String getStatetag() {
		return statetag;
	}

	/**
	 * @param statetag the statetag to set
	 */
	public void setStatetag(String statetag) {
		this.statetag = statetag;
	}

	/**
	 * @return the homescore
	 */
	public int getHomescore() {
		return homescore;
	}

	/**
	 * @param homescore the homescore to set
	 */
	public void setHomescore(int homescore) {
		this.homescore = homescore;
	}

	/**
	 * @return the awayscore
	 */
	public int getAwayscore() {
		return awayscore;
	}

	/**
	 * @param awayscore the awayscore to set
	 */
	public void setAwayscore(int awayscore) {
		this.awayscore = awayscore;
	}

	/**
	 * @return the venuetag
	 */
	public String getVenuetag() {
		return venuetag;
	}

	/**
	 * @param venuetag the venuetag to set
	 */
	public void setVenuetag(String venuetag) {
		this.venuetag = venuetag;
	}

	/**
	 * @return the sheetname
	 */
	public String getSheetname() {
		return sheetname;
	}

	/**
	 * @param sheetname the sheetname to set
	 */
	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}

	public String toString() {
		return this.getHometeamname() + "vs" + this.getAwayteamname() + " @ " + this.venuetag + " " + this.startdate + ":" + this.starttime;
	}

}