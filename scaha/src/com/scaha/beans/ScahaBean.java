package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.internet.InternetAddress;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubAdmin;
import com.scaha.objects.ClubAdminList;
import com.scaha.objects.ClubList;
import com.scaha.objects.GeneralSeasonList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;
import com.scaha.objects.ScahaTeam;
import com.scaha.objects.TeamList;
import com.scaha.objects.TryoutList;

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
	
	private Profile DefaultProfile = null;
	
	
	
	 @PostConstruct
	 public void init() {
		 
		 LOGGER.info("ScahaBean PostConstruct Init: Logger level at:" + LOGGER.getLevel());
		 LOGGER.setLevel(Level.ALL);
		 this.setDefaultProfile(new Profile());
		 refreshBean();

		 
	 }
	 
	 @PreDestroy
	 public void cleanup() {
		 ScahaClubList = null;
		 ScahaSeasonList = null;
		 DefaultProfile = null;
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
		try {
			setScahaClubList(ClubList.NewClubListFactory(this.DefaultProfile, db));
			setScahaSeasonList(GeneralSeasonList.NewClubListFactory(this.DefaultProfile, db, "SCAHA"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		 
	 }
	
	public void refreshClubList() {

		this.resetClubLists();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		try {
			setScahaClubList(ClubList.NewClubListFactory(this.DefaultProfile, db));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
	}

	@SuppressWarnings("unchecked")
	private void resetClubLists() {
		if (this.ScahaClubList != null) {
			List<Club> list = (List<Club>) this.ScahaClubList.getWrappedData();
			for (Club c : list) {
				ClubAdminList cal = c.getCal();
				TeamList tl = c.getScahaTeams();
				if (cal != null) {
					List<ClubAdmin> lca = (List<ClubAdmin>) cal.getWrappedData();
					lca.clear();
				}
				if (tl != null) {
					List<ScahaTeam> lst = (List<ScahaTeam>) tl.getWrappedData();
					lst.clear();
				}
			}
			list.clear();
		}
			
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


	/**
	 * @return the defaultProfile
	 */
	public Profile getDefaultProfile() {
		return DefaultProfile;
	}


	/**
	 * @param defaultProfile the defaultProfile to set
	 */
	public void setDefaultProfile(Profile defaultProfile) {
		DefaultProfile = defaultProfile;
	}

	@Override
	public InternetAddress[] getToMailIAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InternetAddress[] getPreApprovedICC() {
		// TODO Auto-generated method stub
		return null;
	}
}
