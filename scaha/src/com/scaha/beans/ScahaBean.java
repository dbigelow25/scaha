package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubList;
import com.scaha.objects.GeneralSeasonList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;

@ManagedBean
@ApplicationScoped
public class ScahaBean implements Serializable,  MailableObject {
		
	
	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
		//
	// An appliation wide listing of all the clubs.
	//
	private ClubList ScahaClubList  = null;
	private GeneralSeasonList ScahaSeasonList = null;
	
	 @PostConstruct
	 public void init() {
		 
		 LOGGER.info("ScahaBean PostConstruct Init: Logger level at:" + LOGGER.getLevel());
		 LOGGER.setLevel(Level.ALL);
		 refreshBean();
		 
	 }
	

	 public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * This refreshes everything for this singular App Bean
	 * 
	 */
	public void refreshBean() {
		 
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		Profile pro = new Profile(0);
		try {
			setScahaClubList(ClubList.NewClubListFactory(new Profile(0), db));
			setScahaSeasonList(GeneralSeasonList.NewClubListFactory(pro, db, "SCAHA"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		 
	 }
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the scahaClubList
	 */
	public ClubList getScahaClubList() {
		return ScahaClubList;
	}
	/**
	 * @param scahaClubList the scahaClubList to set
	 */
	public void setScahaClubList(ClubList scahaClubList) {
		ScahaClubList = scahaClubList;
	}
	
	 public long getDate() {
	    return System.currentTimeMillis();
	 }

	/**
	 * find a club object given the club id
	 * @param _id
	 * @return
	 */
	public Club findClubByID (int _id) {
		for (Club c : ScahaClubList) {
			if (c.ID == _id) { 
				return c;
			}
		}
		return null;
	}


	/**
	 * @return the scahaSeasonList
	 */
	public GeneralSeasonList getScahaSeasonList() {
		return ScahaSeasonList;
	}


	/**
	 * @param scahaSeasonList the scahaSeasonList to set
	 */
	public void setScahaSeasonList(GeneralSeasonList scahaSeasonList) {
		ScahaSeasonList = scahaSeasonList;
	}
}
