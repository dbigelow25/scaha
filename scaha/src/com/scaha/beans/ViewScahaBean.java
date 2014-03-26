package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Logger;


import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubList;
import com.scaha.objects.Profile;

public class ViewScahaBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private ClubList ScahaClubList  = null;
	private String selectedclub = null;
	
    public String getSelectedclub(){
    	return selectedclub;
    }
    
    public void setSelectedclub(String clubselected){
    	selectedclub=clubselected;
    }

    /**
	 * @return the clubList
	 */
	public ClubList getScahaClubList() {
		
		//
		// lets go get it!
		//
		if (ScahaClubList == null) {

			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
			
			try {
				ScahaClubList = ClubList.NewClubListFactory(new Profile(0), db);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.free();
		}
		return ScahaClubList;
	}

	/**
	 * @param clubList the clubList to set
	 */
	public void setScahaClubList(ClubList clubList) {
		ScahaClubList = clubList;
	}
    

}

