package com.scaha.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.context.ContextManager;

/**
* This holds all the smarts for a given Schedule.  There are many combinations of schedules between teams and seasons
* In this system
* @author David
*
*/
public class ParticipantList extends ListDataModel<Participant> implements Serializable, SelectableDataModel<Schedule> {

	//
	// We need to implement a hashmap so we can directly fetch things we need.
	//
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private HashMap<String, Participant> hm = new HashMap<String,Participant>();
	
	public ParticipantList(List<Participant> _lst) {  
		super(_lst);
   }
	
	public static ParticipantList NewListFactory() {
		return new ParticipantList(new ArrayList<Participant>());
	}

	@Override
	public Schedule getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(Schedule arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	


	@SuppressWarnings("unchecked")
	public ArrayList<Participant> getScheduleWeekArray() {
		return (ArrayList<Participant>)this.getWrappedData();
	}
	
	public Participant getByKey(int _i) {
		return hm.get(_i+"");
	}
	
	@SuppressWarnings("unchecked")
	public void reset() {
		// TODO Auto-generated method stub
		((ArrayList<Participant>)this.getWrappedData()).clear();
		hm.clear();
	}  
	
	@SuppressWarnings("unchecked")
	public void add(Participant _p) {
		hm.put(_p.ID+"", _p);
		((ArrayList<Participant>)this.getWrappedData()).add(_p);
	}

}