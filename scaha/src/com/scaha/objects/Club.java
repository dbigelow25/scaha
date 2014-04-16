package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Club extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// Multi Media Objects Meta
	//
	public static final String MM_ENTITYTYPE = "CLUB";
	public static final String MM_ATTTYPE = "LOGO";
	
	private String Tag = null;
	private String Sname = null;
	private String clubname = null;
	private String CahaNumber = null;
	private String idclub = null;
	private String WebSite = null;
	private MultiMedia Logo = null;
	private ClubAdminList cal = null;
	private List<Team> teams = null; // This is from LOI side
	private TeamList Teams = null;
	private VenueList Venues = null;
	
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

	/**
	 * This is basically an empty shell of a club
	 * 
	 */
	public Club (){ 
		this.ID = -1;
	}
	
	/**
	 * Another way to generalize a new Club..
	 * @param _id
	 * @param _pro
	 */
	public Club(int _id, Profile _pro) {
		this.ID  = _id;
		this.setProfile(_pro);
	}
	
	/**
	 * This is to pull club detail.. given the ID
	 * @param _db
	 * @param _pro
	 * @param _id
	 */
	public Club(ScahaDatabase _db, Profile _pro, int _id) {
		
	}
	
	
	public List<Team> getTeams(){
		return teams;
	}
	
	public void setTeams(List<Team> list){
		teams = list;
	}
	
	public String getClubname(){
		return clubname;
	}
	
	public void setClubname(String sName){
		clubname = sName;
	}
	
	public String getClubid(){
		return idclub;
	}
	
	public void setClubid(String sName){
		idclub = sName;
	}

	/**
	 * @return the cahaNumber
	 */
	public String getCahaNumber() {
		return CahaNumber;
	}

	/**
	 * @param cahaNumber the cahaNumber to set
	 */
	public void setCahaNumber(String cahaNumber) {
		CahaNumber = cahaNumber;
	}


	/**
	 * @return the sname
	 */
	public String getSname() {
		return Sname;
	}

	/**
	 * @param sname the sname to set
	 */
	public void setSname(String sname) {
		Sname = sname;
	}

	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return WebSite;
	}

	/**
	 * @param webSite the webSite to set
	 */
	public void setWebSite(String webSite) {
		WebSite = webSite;
	}

	/**
	 * @return the cal
	 */
	public ClubAdminList getCal() {
		LOGGER.fine("Requesting Club Admin List from Club:" + this.ID + ":" + this.getClubname());
		return cal;
	}

	/**
	 * @param cal the cal to set
	 */
	public void setCal(ClubAdminList cal) {
		this.cal = cal;
	}

	/**
	 * @return the logo
	 */
	public MultiMedia getLogo() {
		return Logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(MultiMedia logo) {
		Logo = logo;
	}
	
	public int getClubID () {
		return this.ID;
	}

	/**
	 * @return the teams
	 */
	public TeamList getScahaTeams() {
		return Teams;
	}

	/**
	 * @param teams the teams to set
	 */
	public void setScahaTeams(TeamList teams) {
		LOGGER.info("Setting Team List for Club:" + this.getClubname());
		Teams = teams;
	}

	/**
	 * @return the venues
	 */
	public VenueList getVenues() {
		return Venues;
	}

	/**
	 * @param venues the venues to set
	 */
	public void setVenues(VenueList venues) {
		Venues = venues;
	}
	
}
