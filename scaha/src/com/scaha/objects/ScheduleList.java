package com.scaha.objects;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class ScheduleList extends ListDataModel<Schedule> implements Serializable, SelectableDataModel<Schedule> {
	
	//
	// We need to implement a hashmap so we can directly fetch things we need.
	//
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	public ScheduleList(List<Schedule> _lst) {  
		super(_lst);
    }  
 
	
	 /**
	 * This will get all Schedule and return a ScheduleList
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static ScheduleList ListFactory(Profile _pro, ScahaDatabase _db, GeneralSeason _gs) throws SQLException {
		
		List<Schedule> data = new ArrayList<Schedule>();
	
		//
		// Guts to be filled out here...
		// 
		
		return new ScheduleList(data);

	}
		
		
    
	@Override
	public Schedule getRowData(String _rowkey) {
	    @SuppressWarnings("unchecked")
		List<Schedule> results = (List<Schedule>) getWrappedData();  
	    for(Schedule schedule : results) {  
	    	if(Integer.toString(schedule.ID).equals(_rowkey)) return schedule;  
	    }  
		return null;
	}
	
	@Override
	public Object getRowKey(Schedule arg0) {
		// TODO Auto-generated method stub
		return Integer.toString(arg0.ID);	}

}
