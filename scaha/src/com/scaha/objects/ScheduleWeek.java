package com.scaha.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;



public class ScheduleWeek extends ScahaObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private boolean ScheduleComplete = false;
	
	private Schedule schedule = null;
	
	//
	// used to help scheduling objects..
	//
	//
	// used to help scheduling objects..
	//
	ParticipantList pProc = ParticipantList.NewListFactory();
	ParticipantList pBump = ParticipantList.NewListFactory();

	int iBC = 0;
	public HashMap<String, String> hmPattern = new HashMap<String, String>(); 
	
	ArrayList<Integer> AvauilToPlayKeys = new ArrayList<Integer>();
	ArrayList<Integer> MatchUpKeys = new ArrayList<Integer>();

	
	/**
	 * @return the scheduleComplete
	 */
	public boolean isScheduleComplete() {
		return ScheduleComplete;
	}

	/**
	 * @param scheduleComplete the scheduleComplete to set
	 */
	public void setScheduleComplete(boolean scheduleComplete) {
		ScheduleComplete = scheduleComplete;
	}
	
	public void resetBumpList() {
		pBump.reset();
	}

	public void resetProcessList() {
		pProc.reset();
	}

	public int getBumpCount () {
		return iBC;
	}
	
	public Participant getBumpListPartAtID(int _i) {
		return pBump.getByKey(_i);
	}

	public Participant getProcessListPartAtID(int _i) {
		return pProc.getByKey(_i);
	}

	public void addBumpListPart(Participant _p) {
		pBump.add(_p);
	}

	public void addPrcoessListPart(Participant _p) {
		pProc.add(_p);
	}

	/**
	 * @return the avauilToPlayKeys
	 */
	public ArrayList<Integer> getAvailToPlayKeys() {
		return AvauilToPlayKeys;
	}

	/**
	 * @param avauilToPlayKeys the avauilToPlayKeys to set
	 */
	public void setAvailToPlayKeys(ArrayList<Integer> availToPlayKeys) {
		AvauilToPlayKeys = availToPlayKeys;
	}
	
	
	
	/**
	 * @return the matchUpKeys
	 */
	public ArrayList<Integer> getMatchUpKeys() {
		return MatchUpKeys;
	}

	/**
	 * @param matchUpKeys the matchUpKeys to set
	 */
	public void setMatchUpKeys(ArrayList<Integer> matchUpKeys) {
		MatchUpKeys = matchUpKeys;
	}

	public boolean isAlreadyScheduled(Participant _p) {
		return pProc.getByKey(_p.ID) != null;
	}
	
	public void setAvailableMatchups(ScahaDatabase _db, Participant _p, Schedule _se) {
		setMatchUpKeys(_db.getAvailableMatchups(_p,_se, this, false));
	}

	public void setAvailableMatchupsSqueeze(ScahaDatabase _db, Participant _p, Schedule _se) {
		setMatchUpKeys(_db.getAvailableMatchups(_p,_se, this, true));
		weedOutMaxGamers(_db);
	}

	
	public boolean noMatchups() {
		
		return this.getMatchUpKeys().isEmpty();
	}
	
	public boolean alreadyProcessed(Participant _p) {
		return pProc.getByKey(_p.ID) != null;
	}
	
	public  Participant calcHomeParticipant(Participant _pMain, Participant  _pMatch, ArrayList<Integer> _aMain, ArrayList<Integer> _aMatch) {
		
	// TODO int iMain = _pMain.getTeam().getTeamGameInfo().getHomeGames();
	// TODO int iMatch = _pMatch.getTeam().getTeamGameInfo().getHomeGames();
		int iMain = 0;
		int iMatch = 0;
		
		if (_aMain.isEmpty() && !_aMatch.isEmpty()) {
			return _pMatch;
		} else 	if (!_aMain.isEmpty() && _aMatch.isEmpty()) {
			return _pMain;
		} else if (_aMain.size() < 2 && iMain < iMatch + 1) {
			return _pMain;
		} else if (_aMatch.size() < 2 && iMatch < iMain + 1) {
			return _pMatch;
		} else 	if (iMain <= iMatch && !_aMain.isEmpty()) {
			return _pMain;
		} else if (iMatch <= iMain && !_aMatch.isEmpty()) {
			return _pMatch;
		}
		return null;
		
	}
	
	public void backoutSchedule(ScahaDatabase _db, Schedule _se, Participant _p) {
		
		this.iBC++;
		this.pBump.add(_p);
		// if bump list grew.. then we have a case where we need to put this at the top top.. blow everything away
		//
		if (pBump.getRowCount() > 1) {
			LOGGER.info("  Initiate Hard Bump Reset...");
			//_db.backoutSchedule(_se, this, true);
		} else {
			LOGGER.info("  Initiate Soft Bump Reset...");
			//_db.backoutSchedule(_se, this, false);
		}
		
		//
		// we are not done
		//
		this.resetProcessList();
		this.setScheduleComplete(false);
	}

	
	public void backoutSchedule (ScahaDatabase _db, Schedule _se) {

		// if bump list grew.. then we have a case where we need to put this at the top top.. blow everything away
		//
		if (pBump.getRowCount() > 1) {
			LOGGER.info("  Initiate Hard Bump Reset...");
		//	_db.backoutSchedule(_se, this, true);
		} else {
			LOGGER.info("  Initiate Soft Bump Reset...");
		//	_db.backoutSchedule(_se, this, true);
		}
		this.resetProcessList();
		this.setScheduleComplete(false);
	}
	
	public void setAvailableToPlay(ScahaDatabase _db) {
		setAvailToPlayKeys(_db.getAvailableParticipants(this));
	}
	
	
	public boolean isBumpOn () {
		return pBump.getRowCount() > 0;
	}
	
	
	public void weedOutMaxGamers(ScahaDatabase _db) {
		ArrayList<Integer> am = new ArrayList<Integer>();
		for (Integer match : getMatchUpKeys()) {
			Participant pMatch = schedule.getParticipantAtID(match.intValue());
		//	pMatch.getTeam().getTeamGameInfo().refreshInfo(_db,schedule);
		//	if (pMatch.getTeam().getTotalGames() > schedule.getGameCount()) {
		// TODO		am.add(match);
				LOGGER.info("schedule:Removing team from matchup.. too many games:" + pMatch);
		//	}

		}
		
		//
		// Now lets weed them out man.
		//
		for (Integer match : am) {
			getMatchUpKeys().remove(match);
		}

	}
	
}
