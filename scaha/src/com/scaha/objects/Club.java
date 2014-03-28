package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Club extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String Tag = null;
	private String Sname = null;
	private String clubname = null;
	private String CahaNumber = null;
	private String idclub = null;
	private String WebSite = null;
	private byte[] blogo = null;
	private String logoextension = null;
	private ClubAdminList cal = null;
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
		return cal;
	}

	/**
	 * @param cal the cal to set
	 */
	public void setCal(ClubAdminList cal) {
		this.cal = cal;
	}

	/**
	 * @return the blogo
	 */
	public byte[] getBlogo() {
		return blogo;
	}

	/**
	 * @param blogo the blogo to set
	 */
	public void setBlogo(byte[] blogo) {
		this.blogo = blogo;
	}

	/**
	 * @return the logoextension
	 */
	public String getLogoextension() {
		return logoextension;
	}

	/**
	 * @param logoextension the logoextension to set
	 */
	public void setLogoextension(String logoextension) {
		this.logoextension = logoextension;
	}
	
	
}
