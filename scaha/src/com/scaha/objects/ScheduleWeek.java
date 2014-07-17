package com.scaha.objects;

import java.io.Serializable;
import java.util.ArrayList;


public class ScheduleWeek extends ScahaObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean ScheduleComplete = false;
	
	//
	// used to help scheduling objects..
	//
	//
	// used to help scheduling objects..
	//
	ArrayList<Participant> pProc = new ArrayList<Participant>();
	ArrayList<Participant> pBump = new ArrayList<Participant>();

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


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
		pBump.clear();
	}

	public void resetProcessList() {
		pProc.clear();
	}

}
