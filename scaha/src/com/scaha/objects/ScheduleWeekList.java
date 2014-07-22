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
public class ScheduleWeekList extends ListDataModel<ScheduleWeek> implements Serializable, SelectableDataModel<Schedule> {

	//
	// We need to implement a hashmap so we can directly fetch things we need.
	//
	
	private HashMap<String, ScheduleWeek> hm = new HashMap<String,ScheduleWeek>();
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	public ScheduleWeekList(List<ScheduleWeek> _lst) {  
		super(_lst);
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
	public ArrayList<ScheduleWeek> getScheduleWeekArray() {
		return (ArrayList<ScheduleWeek>)this.getWrappedData();
	}
	
	public ScheduleWeek getByKey(int _i) {
		return hm.get(_i+"");
	}
	
	@SuppressWarnings("unchecked")
	public void reset() {
		// TODO Auto-generated method stub
		((ArrayList<ScheduleWeek>)this.getWrappedData()).clear();
		hm.clear();
	}  
	
	@SuppressWarnings("unchecked")
	public void add(ScheduleWeek _sw) {
		hm.put(_sw.ID+"", _sw);
		((ArrayList<ScheduleWeek>)this.getWrappedData()).add(_sw);
	}

}
