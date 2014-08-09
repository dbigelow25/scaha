package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class Scoring extends ScahaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private ScahaTeam  team = null;
	private LiveGame livegame = null;
	private LiveGameRosterSpot lgrosterspotgoal = null;
	private LiveGameRosterSpot lgrosterspota1 = null;
	private LiveGameRosterSpot lgrosterspota2 = null;
	
	private int idrostergoal = 0;
	private int idrostera1 = 0;
	private int idrostera2 = 0;
	
	private int period = 0;
	private int count = 0;
	
	private String goaltype = null;
	private String timescored = null;
	
	
	public Scoring(int _id, Profile _pro, LiveGame _lg, ScahaTeam _tm) {
		ID = _id;	
		this.setProfile(_pro);
	}

	public Scoring(int _id, Profile _pro) {
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
	 * @return the lgrosterspotgoal
	 */
	public LiveGameRosterSpot getLgrosterspotgoal() {
		return lgrosterspotgoal;
	}

	/**
	 * @param lgrosterspotgoal the lgrosterspotgoal to set
	 */
	public void setLgrosterspotgoal(LiveGameRosterSpot lgrosterspotgoal) {
		this.lgrosterspotgoal = lgrosterspotgoal;
	}

	/**
	 * @return the lgrosterspota1
	 */
	public LiveGameRosterSpot getLgrosterspota1() {
		return lgrosterspota1;
	}

	/**
	 * @param lgrosterspota1 the lgrosterspota1 to set
	 */
	public void setLgrosterspota1(LiveGameRosterSpot lgrosterspota1) {
		this.lgrosterspota1 = lgrosterspota1;
	}

	/**
	 * @return the lgrosterspota2
	 */
	public LiveGameRosterSpot getLgrosterspota2() {
		return lgrosterspota2;
	}

	/**
	 * @param lgrosterspota2 the lgrosterspota2 to set
	 */
	public void setLgrosterspota2(LiveGameRosterSpot lgrosterspota2) {
		this.lgrosterspota2 = lgrosterspota2;
	}

	/**
	 * @return the idrostergoal
	 */
	public int getIdrostergoal() {
		return idrostergoal;
	}

	/**
	 * @param idrostergoal the idrostergoal to set
	 */
	public void setIdrostergoal(int idrostergoal) {
		this.idrostergoal = idrostergoal;
	}

	/**
	 * @return the idrostera1
	 */
	public int getIdrostera1() {
		return idrostera1;
	}

	/**
	 * @param idrostera1 the idrostera1 to set
	 */
	public void setIdrostera1(int idrostera1) {
		this.idrostera1 = idrostera1;
	}

	/**
	 * @return the idrostera2
	 */
	public int getIdrostera2() {
		return idrostera2;
	}

	/**
	 * @param idrostera2 the idrostera2 to set
	 */
	public void setIdrostera2(int idrostera2) {
		this.idrostera2 = idrostera2;
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

	/**
	 * @return the goaltype
	 */
	public String getGoaltype() {
		return goaltype;
	}

	/**
	 * @param goaltype the goaltype to set
	 */
	public void setGoaltype(String goaltype) {
		this.goaltype = goaltype;
	}

	/**
	 * @return the timescored
	 */
	public String getTimescored() {
		return timescored;
	}

	/**
	 * @param timescored the timescored to set
	 */
	public void setTimescored(String timescored) {
		this.timescored = timescored;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Scoring [lgrosterspotgoal=" + lgrosterspotgoal
				+ ", lgrosterspota1=" + lgrosterspota1 + ", lgrosterspota2="
				+ lgrosterspota2 + ", idrostergoal=" + idrostergoal
				+ ", idrostera1=" + idrostera1 + ", idrostera2=" + idrostera2
				+ ", period=" + period + ", goaltype=" + goaltype
				+ ", timescored=" + timescored + "]";
	}

	public void setCount(int i) {
		this.count = i;
	}
	
	public int getCount() {
		return this.count;
	}


}
