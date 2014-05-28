package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.mail.internet.InternetAddress;

import com.gbli.context.ContextManager;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.GeneralSeasonList;
import com.scaha.objects.MailableObject;
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
	private GeneralSeasonList seasons = null;
	
	private List<SelectItem>schedlist;
	private List<SelectItem>seasonlist;
	
	private Schedule schedule;	
	private GeneralSeason season;
	
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
		 this.setSchedules(scaha.getScahaschedule());
		 this.setSeasons(scaha.getScahaSeasonList());
		 //
		 // lets refresh all the picklists used in the fiew..
		 //
		 refreshSelectedItems();
		 
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
	
	
	private void refreshSelectedItems() {
		
		//
		// ok.. lets do the schedules now..
		//
		schedlist = new ArrayList<SelectItem>();
        SelectItemGroup sgpre = new SelectItemGroup("Pre Season");
        SelectItemGroup sgreg  = new SelectItemGroup("Regular Season");
        SelectItemGroup sgpst = new SelectItemGroup("Post Season");
        SelectItemGroup sgoth = new SelectItemGroup("Other");
        
        List<SelectItem> lpre = new ArrayList<SelectItem>();
        List<SelectItem> lreg = new ArrayList<SelectItem>();
        List<SelectItem> lpst = new ArrayList<SelectItem>();
        List<SelectItem> loth = new ArrayList<SelectItem>();
        
        for (Schedule s : this.getSchedules()) {
        	if (s.getDescription().contains("Pre Season")) {
        		lpre.add(new SelectItem(s, s.toString()));
        	} else if (s.getDescription().contains("Regular")) {
        		lreg.add(new SelectItem(s, s.toString()));
        	} else if (s.getDescription().contains("Post Season")) {
        		lpst.add(new SelectItem(s, s.toString()));
        	} else { 
        		loth.add(new SelectItem(s, s.toString()));
        		
        	}
        }
        if (lpre.size() > 0) {
        	sgpre.setSelectItems(lpre.toArray(new SelectItem[lpre.size()]));
        	schedlist.add(sgpre);
        }
        if (lreg.size() > 0) {
            sgreg.setSelectItems(lreg.toArray(new SelectItem[lreg.size()]));
        	schedlist.add(sgreg);
        }
        if (lpst.size() > 0) {
            sgpst.setSelectItems(lpst.toArray(new SelectItem[lpst.size()]));
        }
        if (loth.size() > 0) {
            sgoth.setSelectItems(loth.toArray(new SelectItem[loth.size()]));
        	schedlist.add(sgoth);
        }
        
        //
		// ok.. lets do the seasons now..
		//
        this.seasonlist =  new ArrayList<SelectItem>();
        
        for (GeneralSeason s : this.getSeasons()) {
        	if (s.getMembershipType().equals("SCAHA")) seasonlist.add(new SelectItem(s, s.toString()));
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

	/**
	 * @param seasons the seasons to set
	 */
	public void setSeasons(GeneralSeasonList seasons) {
		this.seasons = seasons;
	}

	/**
	 * @return the schedlist
	 */
	public List<SelectItem> getSchedlist() {
		return schedlist;
	}

	/**
	 * @param schedlist the schedlist to set
	 */
	public void setSchedlist(List<SelectItem> schedlist) {
		this.schedlist = schedlist;
	}

	/**
	 * @return the seasonlist
	 */
	public List<SelectItem> getSeasonlist() {
		return seasonlist;
	}

	/**
	 * @param seasonlist the seasonlist to set
	 */
	public void setSeasonlist(List<SelectItem> seasonlist) {
		this.seasonlist = seasonlist;
	}

	/**
	 * @return the schedule
	 */
	public Schedule getSchedule() {
		return schedule;
	}

	/**
	 * @param schedule the schedule to set
	 */
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * @return the genseason
	 */
	public GeneralSeason getSeason() {
		return season;
	}

	/**
	 * @param genseason the genseason to set
	 */
	public void setSeason(GeneralSeason season) {
		this.season = season;
	}

	
	
}