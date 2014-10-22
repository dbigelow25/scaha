package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.GeneralSeasonList;
import com.scaha.objects.LiveGame;
import com.scaha.objects.LiveGameList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Participant;
import com.scaha.objects.ParticipantList;
import com.scaha.objects.Schedule;
import com.scaha.objects.ScheduleList;

@ManagedBean
@SessionScoped
public class ScoreboardBean implements Serializable,  MailableObject {

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	
	
	private ScheduleList schedules;
	private List<Schedule> schedulelist =  null;
	private List<Participant> partpicklist =  null;
	
	private GeneralSeasonList seasons = null;
	

	private Schedule selectedschedule;	
	private int selectedscheduleid;
	private GeneralSeason selectedseason;
	private int selectedseasonid;
	private Participant selectedpart;
	private int selectedpartid;

	private ParticipantList partlist = null;
	private LiveGameList livegamelist = null;
	
	private LiveGame selectedlivegame = null;

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
		 this.selectedseason = scaha.getScahaSeasonList().getCurrentSeason();
		 this.selectedseasonid = selectedseason.ID;
		 this.refreshScheduleList();
		 
		 LOGGER.info(" *************** FINISH :POST INIT FOR SCOREBOARD BEAN *****************");
	 }
	
	public String refreshScoreboard() {
		if (this.getPartlist() != null) {
			
			try {
			// Lets get the connections we need
				ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
				this.getPartlist().refreshList(pb.getProfile(), db);
				db.free();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Date and Time is: "  + (new Date());
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
			
			
			//if historical season lets look up the games and standings for the season.
			if(this.selectedseason!=scaha.getScahaSeasonList().getCurrentSeason()){
				ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
				try {
					//generating historical standings and adding them to the partlist.
					this.partlist = ParticipantList.NewListFactory(db, selectedscheduleid);
					this.partpicklist = ParticipantList.getHistoricalParticipantList(db, selectedscheduleid);
					
					//generating the historical schedule of games and adding them to the livegamelist
					this.setLivegamelist(scaha.getScahaLiveGameList().NewListFactory(db, selectedscheduleid));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					db.free();
				}
				
			}else {
				this.partlist = this.selectedschedule.getPartlist();
				this.partpicklist = this.getParticipantpicklist();
				this.setLivegamelist(scaha.getScahaLiveGameList().NewList(scaha.getDefaultProfile(),selectedschedule));
				
			}
		}
	}
	
	
	/**
	 * get participants for a schedule..
	 */
	public void onPartChange() {
		
		if(this.selectedseason!=scaha.getScahaSeasonList().getCurrentSeason()){
			//need to load historical schedule by team name
			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
			try {
				this.setLivegamelist(scaha.getScahaLiveGameList().NewListFactory(db, selectedscheduleid,this.selectedpartid));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				db.free();
			}
			
		} else {
		
			this.selectedpart = this.getPartlist().getByKey(this.selectedpartid);
			LOGGER.info("participant change request detected new id is:" + this.selectedpartid + ":" + this.selectedpart + " for sched:" + this.selectedschedule);
			if (this.selectedpart != null) {
					this.setLivegamelist(this.selectedschedule.getLivegamelist().NewList(scaha.getDefaultProfile(), selectedschedule, selectedpart.getTeam()));
				
			} else {
				this.setLivegamelist(this.selectedschedule.getLivegamelist());
			}
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
		this.partpicklist = null;
		if (this.selectedseason != null) {

			if(this.selectedseason!=scaha.getScahaSeasonList().getCurrentSeason()){
				ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
				try {
					this.schedules = ScheduleList.ListFactory(pb.getProfile(), db,this.selectedseason,scaha.getScahaTeamList());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				this.schedules = this.selectedseason.getSchedList();
			}
			
			LOGGER.info("season schedule is: " + schedules);

			this.schedules = this.selectedseason.getSchedList();
//			LOGGER.info("season schedule is: " + schedules);

			if (schedules != null) {
			  if (this.schedules.getRowCount() > 0) {
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
	
	@SuppressWarnings("unchecked")
	private List<Participant> getParticipantpicklist() {
		return (List<Participant>)partlist.getWrappedData();
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

	/**
	 * @return the partpicklist
	 */
	public List<Participant> getPartpicklist() {
		return partpicklist;
	}

	/**
	 * @param partpicklist the partpicklist to set
	 */
	public void setPartpicklist(List<Participant> partpicklist) {
		this.partpicklist = partpicklist;
	}

	/**
	 * @return the selectedpart
	 */
	public Participant getSelectedpart() {
		return selectedpart;
	}

	/**
	 * @param selectedpart the selectedpart to set
	 */
	public void setSelectedpart(Participant selectedpart) {
		this.selectedpart = selectedpart;
	}

	/**
	 * @return the selectedpartid
	 */
	public int getSelectedpartid() {
		return selectedpartid;
	}

	/**
	 * @param selectedpartid the selectedpartid to set
	 */
	public void setSelectedpartid(int selectedpartid) {
		this.selectedpartid = selectedpartid;
	}

	/**
	 * @return the selectedlivegame
	 */
	public LiveGame getSelectedlivegame() {
		return selectedlivegame;
	}

	/**
	 * @param selectedlivegame the selectedlivegame to set
	 */
	public void setSelectedlivegame(LiveGame selectedlivegame) {
		this.selectedlivegame = selectedlivegame;
	}


	 public void editLiveGame() {  
		 
		 this.selectedlivegame = this.getLivegamelist().getByKey(this.selectedlivegame.ID);
		 pb.setSelectedlivegame(this.selectedlivegame);
		 pb.setLivegameeditreturn("scoreboard.xhtml");
		 
		 LOGGER.info("!!!!! Real Selected Game is" + selectedlivegame);
		  
	     ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
	     try {
	    	 context.redirect("gamesheetcentral.xhtml");
	     } catch (IOException e) {
	    	 // TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	 } 

	
}