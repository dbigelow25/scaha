package com.scaha.objects;

public class Schedule extends ScahaObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String scheduleweektag = null;
	private String seasontag = null;
	private String tag = null;
	private String divsname = null;
	private String gametag = null;
	private String description = null;
	private int gamecount = 0;
	private int teamcount = 0;
	private int byeteamcount = 0;
	private boolean playonce = false;
	private boolean locked = false;
	private int rank = 0;
	private String startdate = null;
	private String enddate = null;
	private int mingamecnt = 0;
	private int maxgamecnt = 0;
	private int maxbyecnt = 0;
	private int maxawaycnt = 0;
	

	/**
	 * Basic Constructor.
	 * s
	 * @param _pro
	 * @param _id
	 */
	public Schedule (Profile _pro, int _id) {
		setProfile(_pro);
		this.ID = _id;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	/**
	 * @return the scheduleweektag
	 */
	public String getScheduleweektag() {
		return scheduleweektag;
	}


	/**
	 * @param scheduleweektag the scheduleweektag to set
	 */
	public void setScheduleweektag(String scheduleweektag) {
		this.scheduleweektag = scheduleweektag;
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
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}


	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}


	/**
	 * @return the divsname
	 */
	public String getDivsname() {
		return divsname;
	}


	/**
	 * @param divsname the divsname to set
	 */
	public void setDivsname(String divsname) {
		this.divsname = divsname;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the gamecount
	 */
	public int getGamecount() {
		return gamecount;
	}


	/**
	 * @param gamecount the gamecount to set
	 */
	public void setGamecount(int gamecount) {
		this.gamecount = gamecount;
	}


	/**
	 * @return the teamcount
	 */
	public int getTeamcount() {
		return teamcount;
	}


	/**
	 * @param teamcount the teamcount to set
	 */
	public void setTeamcount(int teamcount) {
		this.teamcount = teamcount;
	}


	/**
	 * @return the byeteamcount
	 */
	public int getByeteamcount() {
		return byeteamcount;
	}


	/**
	 * @param byeteamcount the byeteamcount to set
	 */
	public void setByeteamcount(int byeteamcount) {
		this.byeteamcount = byeteamcount;
	}


	/**
	 * @return the playonce
	 */
	public boolean isPlayonce() {
		return playonce;
	}


	/**
	 * @param playonce the playonce to set
	 */
	public void setPlayonce(boolean playonce) {
		this.playonce = playonce;
	}


	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}


	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}


	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}


	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
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
	 * @return the enddate
	 */
	public String getEnddate() {
		return enddate;
	}


	/**
	 * @param enddate the enddate to set
	 */
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}


	/**
	 * @return the mingamecnt
	 */
	public int getMingamecnt() {
		return mingamecnt;
	}


	/**
	 * @param mingamecnt the mingamecnt to set
	 */
	public void setMingamecnt(int mingamecnt) {
		this.mingamecnt = mingamecnt;
	}


	/**
	 * @return the maxbyecnt
	 */
	public int getMaxbyecnt() {
		return maxbyecnt;
	}


	/**
	 * @param maxbyecnt the maxbyecnt to set
	 */
	public void setMaxbyecnt(int maxbyecnt) {
		this.maxbyecnt = maxbyecnt;
	}


	/**
	 * @return the maxawaycnt
	 */
	public int getMaxawaycnt() {
		return maxawaycnt;
	}


	/**
	 * @param maxawaycnt the maxawaycnt to set
	 */
	public void setMaxawaycnt(int maxawaycnt) {
		this.maxawaycnt = maxawaycnt;
	}


	/**
	 * @return the gametag
	 */
	public String getGametag() {
		return gametag;
	}


	/**
	 * @param gametag the gametag to set
	 */
	public void setGametag(String gametag) {
		this.gametag = gametag;
	}
	
	public String toString() {
		return this.getDescription();
	}
	public void setMaxbyecount(int int1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @return the maxgamecnt
	 */
	public int getMaxgamecnt() {
		return maxgamecnt;
	}
	/**
	 * @param maxgamecnt the maxgamecnt to set
	 */
	public void setMaxgamecnt(int maxgamecnt) {
		this.maxgamecnt = maxgamecnt;
	}

}
