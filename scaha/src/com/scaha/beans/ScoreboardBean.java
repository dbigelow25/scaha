package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.GeneralSeasonList;
import com.scaha.objects.LiveGameList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.ParticipantList;
import com.scaha.objects.Schedule;
import com.scaha.objects.ScheduleList;

@ManagedBean
@ViewScoped
public class ScoreboardBean implements Serializable,  MailableObject {

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	
	
	private ScheduleList schedules;
	private List<Schedule> schedulelist =  null;
	private GeneralSeasonList seasons = null;

	private Schedule selectedschedule;	
	private int selectedscheduleid;
	private GeneralSeason selectedseason;
	private int selectedseasonid;

	private ParticipantList partlist = null;
	private LiveGameList livegamelist = null;
	
	//
	// Class Level Variables
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// lets go get it!
	//
	public ScoreboardBean() {
	}

	 @PostConstruct
	 public void init() {
		 
		 LOGGER.info(" *************** START :POST INIT FOR SCOREBOARD BEAN *****************");
		 //
		 // for now.. we will borrow what was from scaha for schedule.
		 // in the end.. we want to pull the schedule based upon the season they choose..
		 // which means this list gets personalized... (*for history *)
		 // Currently Very cludgy.. but over time.. we can generalize on the season key as the way to 
		 // look at data from past seasons..
		 //
		 this.setSeasons(scaha.getScahaSeasonList());
		 //
		 // lets refresh all the picklists used in the fiew..
		 //
		 //refreshScheduleList();
		 
		 LOGGER.info(" *************** FINISH :POST INIT FOR SCOREBOARD BEAN *****************");
	 }
	
	
	
	/**
	 * This guy builds up the scoreboard related information..
	 * Then routes the user to the correct page
	 * 
	 * We need to pass schedule and possibly team pointers as URL parms 
	 * 
	 * So that clubs and teams can point to this page from other locations
	 * 
	 * @return
	 */
	public void buildIt() {
		
		//
		// We also will want to pass parms here.. if applicable..

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("scoreboard.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Recalc schedule for season..
	 */
	public void onSeasonChange() {
		LOGGER.info("season change request detected new id is:" + this.selectedseasonid);
		this.selectedseason = this.seasons.getGeneralSeason(this.selectedseasonid);
		refreshScheduleList();
	}
	
	/**
	 * get participants for a schedule..
	 */
	public void onScheduleChange() {
		
		this.selectedschedule = this.schedules.getSchedule(this.selectedscheduleid);
		LOGGER.info("schedule change request detected new id is:" + this.selectedscheduleid + ":" + this.selectedschedule);
		this.partlist = null;
		if (this.selectedschedule != null) {
			this.partlist = this.selectedschedule.getPartlist();
			this.setLivegamelist(this.selectedschedule.getLivegamelist());
		}
	}
	
	private void refreshScheduleList() {
		
		//
		// ok.. lets do the schedules now..
		//
		LOGGER.info("Refreshing Schedule List for season:" + this.selectedseason);
		this.schedulelist = null;	
		this.schedules = null;
		this.partlist = null;
		this.setLivegamelist(null);
		if (this.selectedseason != null) {
			this.schedules = this.selectedseason.getSchedList();
			LOGGER.info("season schedule is: " + schedules);
			if (schedules != null) {
			  if (this.schedules.getRowCount() > 0) {
				  LOGGER.info("Refresh.. setting schedule list to:" + this.schedules.getRowCount());
				  this.schedulelist = this.getScheduleList();
			  }	else {
				LOGGER.info("Refresh.. zero list.. leaving null:" + this.schedules.getRowCount());
			  } 
			}
		}
        
	}
	
	
	public void refreshSeasonList() {

		LOGGER.info("Getting season List");
		//
		// ok.. lets do the seasons now..
		//
		this.seasons = scaha.getScahaSeasonList();
		
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

	/**
	 * @return the scaha
	 */
	public ScahaBean getScaha() {
		return scaha;
	}

	/**
	 * @param scaha the scaha to set
	 */
	public void setScaha(ScahaBean scaha) {
		this.scaha = scaha;
	}

	/**
	 * @return the pb
	 */
	public ProfileBean getPb() {
		return pb;
	}

	/**
	 * @param pb the pb to set
	 */
	public void setPb(ProfileBean pb) {
		this.pb = pb;
	}

	/**
	 * @return the schedules
	 */
	public ScheduleList getSchedules() {
		return schedules;
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void setSchedules(ScheduleList schedules) {
		this.schedules = schedules;
	}

	/**
	 * @return the seasons
	 */
	public GeneralSeasonList getSeasons() {
		return seasons;
	}
	
	@SuppressWarnings("unchecked")
	public List<GeneralSeason> getSeasonlist() {
		return (List<GeneralSeason>)seasons.getWrappedData();
	}

	/**
	 * @param seasons the seasons to set
	 */
	public void setSeasons(GeneralSeasonList seasons) {
		this.seasons = seasons;
		LOGGER.info("Here is our General Season:");
		LOGGER.info(this.seasons.toString());
	}

	
	/**
	 * @return the selectedseason
	 */
	public GeneralSeason getSelectedseason() {
		return selectedseason;
	}

	/**
	 * @param selectedseason the selectedseason to set
	 */
	public void setSelectedseason(GeneralSeason selectedseason) {
		this.selectedseason = selectedseason;
	}

	/**
	 * @return the selectedschedule
	 */
	public Schedule getSelectedschedule() {
		return selectedschedule;
	}

	/**
	 * @param selectedschedule the selectedschedule to set
	 */
	public void setSelectedschedule(Schedule selectedschedule) {
		this.selectedschedule = selectedschedule;
	}

	/**
	 * @return the selectedseasonid
	 */
	public int getSelectedseasonid() {
		return selectedseasonid;
	}

	/**
	 * @param selectedseasonid the selectedseasonid to set
	 */
	public void setSelectedseasonid(int selectedseasonid) {
		this.selectedseasonid = selectedseasonid;
	}

	/**
	 * @return the partlist
	 */
	public ParticipantList getPartlist() {
		return partlist;
	}

	/**
	 * @param partlist the partlist to set
	 */
	public void setPartlist(ParticipantList partlist) {
		this.partlist = partlist;
	}

	/**
	 * @return the selectedscheduleid
	 */
	public int getSelectedscheduleid() {
		return selectedscheduleid;
	}

	/**
	 * @param selectedscheduleid the selectedscheduleid to set
	 */
	public void setSelectedscheduleid(int selectedscheduleid) {
		this.selectedscheduleid = selectedscheduleid;
	}

	/**
	 * @return the schedulelist
	 */
	public List<Schedule> getSchedulelist() {
		return schedulelist;
	}

	/**
	 * @param schedulelist the schedulelist to set
	 */
	public void setSchedulelist(List<Schedule> schedulelist) {
		this.schedulelist = schedulelist;
	}
	
	@SuppressWarnings("unchecked")
	private List<Schedule> getScheduleList() {
		return (List<Schedule>)schedules.getWrappedData();
	}

	/**
	 * @return the livegamelist
	 */
	public LiveGameList getLivegamelist() {
		return livegamelist;
	}

	/**
	 * @param livegamelist the livegamelist to set
	 */
	public void setLivegamelist(LiveGameList livegamelist) {
		this.livegamelist = livegamelist;
	}


	

	
}