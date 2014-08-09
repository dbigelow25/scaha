package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Penalty extends ScahaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private ScahaTeam  team = null;
	private LiveGame livegame = null;
	private LiveGameRosterSpot rosterspot = null;
	private int idroster = 0;
	private String penalty = null;
	private int count = 0;
	private int period = 0;
	private String timeofpenalty = null;
	/**
	 * @return the timeofpenalty
	 */
	public String getTimeofpenalty() {
		return timeofpenalty;
	}

	/**
	 * @param timeofpenalty the timeofpenalty to set
	 */
	public void setTimeofpenalty(String timeofpenalty) {
		this.timeofpenalty = timeofpenalty;
	}

	private int minutes = 0;
	
	public Penalty(int _id, Profile _pro, LiveGame _lg, ScahaTeam _tm) {
		ID = _id;	
		this.setProfile(_pro);
		this.livegame = _lg;
		this.team = _tm;
	}

	public Penalty(int _id, Profile _pro) {
		ID = _id;		
		this.setProfile(_pro);
	}

	/**
	 * @return the team
	 */
	public ScahaTeam getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(ScahaTeam team) {
		this.team = team;
	}

	/**
	 * @return the livegame
	 */
	public LiveGame getLivegame() {
		return livegame;
	}

	/**
	 * @param livegame the livegame to set
	 */
	public void setLivegame(LiveGame livegame) {
		this.livegame = livegame;
	}

	
	/**
	 * @return the period
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	

	public void setCount(int i) {
		this.count = i;
	}
	
	public int getCount() {
		return this.count;
	}

	public void delete(ScahaDatabase db) throws SQLException {

		CallableStatement cs = db.prepareCall("call scaha.deletePenalty(?)");
		cs.setInt(1, this.ID);
		cs.execute();
		cs.close();
		
	}
	
	public void update(ScahaDatabase db) throws SQLException {
		// 
		// is it an object that is not in the database yet..
		//
		
		CallableStatement cs = db.prepareCall("call scaha.updatePenalty(?,?,?,?,?,?,?,?,?,?)");
		
		LOGGER.info("HERE IS THE Starting Penalty ID:" + this.ID);

		int i = 1;
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(i++, this.ID);
		cs.setInt(i++, this.idroster);
		cs.setInt(i++, this.getLivegame().ID);
		cs.setInt(i++, this.getTeam().ID);
		cs.setInt(i++, this.period);
		cs.setString(i++, this.penalty);
		cs.setInt(i++, this.minutes);
		cs.setString(i++, this.timeofpenalty);
		cs.setInt(i++,1);
		cs.setString(i++,null);
		cs.execute();
		
		//
		// Update the new ID from the database...
		//
		this.ID = cs.getInt(1);
		cs.close();

		LOGGER.info("HERE IS THE new Penalty ID:" + this.ID);

		
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the rosterspot
	 */
	public LiveGameRosterSpot getRosterspot() {
		return rosterspot;
	}

	/**
	 * @param rosterspot the rosterspot to set
	 */
	public void setRosterspot(LiveGameRosterSpot rosterspot) {
		this.rosterspot = rosterspot;
		if (this.rosterspot != null) {
			this.idroster = this.rosterspot.ID;
		}
	}

	/**
	 * @return the idroster
	 */
	public int getIdroster() {
		return idroster;
	}

	/**
	 * @param idroster the idroster to set
	 */
	public void setIdroster(int idroster) {
		this.idroster = idroster;
	}

	/**
	 * @return the penalty
	 */
	public String getPenalty() {
		return penalty;
	}

	/**
	 * @param penalty the penalty to set
	 */
	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}


}
