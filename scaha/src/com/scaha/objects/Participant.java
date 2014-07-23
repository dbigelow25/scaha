package com.scaha.objects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class Participant extends ScahaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private ScahaTeam  team = null;
	private Schedule schedule = null;
	private int Rank = 0;
	
	private Vector<Slot> SlotsAvail = new Vector<Slot>();
	private Vector<Slot> SlotsPlaying = new Vector<Slot>();
	private Vector<Slot> SlotsMatchup = new Vector<Slot>();
	
	public Participant (int _id, int _irank, Schedule _se, ScahaTeam _team) {
		ID = _id;
		this.setRank(_irank);
		this.setSchedule(_se);
		this.setTeam(_team);
		
	}
	
	public Participant(int _id, Profile _pro) {
		ID = _id;		
		this.setProfile(_pro);
	}

	/**
	 * @return the slotsPlaying
	 */
	public Vector<Slot> getSlotsPlaying() {
		return SlotsPlaying;
	}

	/**
	 * @param slotsPlaying the slotsPlaying to set
	 */
	public void setSlotsPlaying(Vector<Slot> slotsPlaying) {
		SlotsPlaying = slotsPlaying;
	}

	/**
	 * @return the SlotsAvail
	 */
	public Vector<Slot> getSlotsAvail() {
		return SlotsAvail;
	}

	public ScahaTeam getTeam() {
		// TODO Auto-generated method stub
		return this.team;
	}
	public void setTeam(ScahaTeam _team) {
		this.team = _team;
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
	 * @return the rank
	 */
	public int getRank() {
		return Rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		Rank = rank;
	}

	/**
	 * @return the slotsMatchup
	 */
	public Vector<Slot> getSlotsMatchup() {
		return SlotsMatchup;
	}

	/**
	 * @param slotsMatchup the slotsMatchup to set
	 */
	public void setSlotsMatchup(Vector<Slot> slotsMatchup) {
		SlotsMatchup = slotsMatchup;
	}
	
	@Override
	public String toString() {
		return "Participant [Team=" + team + ", Rank=" + Rank + ", ID=" + ID + "]";
	}

	/**
	 * Rules checker to make sure this game can be played...
	 * Given we are running in squeeze mode
	 * @param _sl
	 * @param _pAgainst
	 * @return
	 */
	public boolean canIPlay(Schedule _se, Slot _sl, Participant _pAgainst) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

		try {
			 Date dateAvail = dateFormat.parse(_sl.actDate + " " + _sl.StartTime); 
			 if (this.getSlotsPlaying().isEmpty()) {
			   	LOGGER.info("Team Not playing... so anyslot will do SLOT:" + _sl);
		    	return true;
			 }
			 for (Slot slot: this.getSlotsPlaying()) {
				Date datePlaying = dateFormat.parse(slot.actDate + " " + slot.StartTime); 
			    long diff = dateAvail.getTime() - datePlaying.getTime();
			    long hours = Math.abs(diff / (1000*60*60));
				
				if (_pAgainst.getTeam().ID ==  slot.idTeamAgainst && hours < 24 * (_se.getTeamcount() * 2)) {
			    	LOGGER.info("I cannot play...not enough time between matchups.. needs to be more than:" +(24 * (_se.getTeamcount() * 1.25)));
			    	return false;
			    }
			    if (hours < 8 ) {
			    	LOGGER.info("I cannot play...not enough time between matchups..  already have a game within 8 hours of start time");
			    	return false;
			    }
			    
			    //
			    // lets make sure its not in the middle of a club or team out of town
			    //
			    
			    for (String bodate: _pAgainst.getTeam().getTeamGameInfo().getHmBOD().keySet()) {
			    	Date datebo = dateFormat.parse(bodate + " 00:00:00");
			        long bodiff = dateAvail.getTime() - datebo.getTime();
			    	long bodhours = Math.abs(bodiff / (1000*60*60));
					if (bodhours < 72) {
					   	return false;
					}
			    }
			    
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
    	LOGGER.info("I (" + this.getTeam() + ") can play on this slot:" + _sl);
		
		return true;
	}

	public void resetSlotsMatchup() {
		SlotsMatchup = new Vector<Slot>();
	}
	
	public void resetSlotsAvail() {
		this.SlotsAvail = new Vector<Slot>();
	}
	
	public void resetSlotsPlayed() {
		SlotsPlaying = new Vector<Slot>();
	}
}
