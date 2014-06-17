package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
;

public class YearList extends ListDataModel<Year> implements Serializable, SelectableDataModel<Year> {
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	

	public YearList() {  
    }  
	
	/**
	 * 
	 * @param data
	 */
    public YearList(List<Year> data) {  
        super(data);  
    }  

	
	public static YearList NewYearListFactory(ScahaDatabase _db) throws SQLException {
		
		LOGGER.info ("Getting list of years containing meeting minutes");

		List<Year> data = new ArrayList<Year>();
		
		PreparedStatement ps = _db.prepareStatement("call scaha.getAllSeasonsByType('SCAHA')");
		CallableStatement psminutes = _db.prepareCall("call scaha.getMinutesBySeason(?)");
		ResultSet rs = ps.executeQuery();
		ResultSet rs2 = null;
		while (rs.next()) {
			String yearname = rs.getString("Description");
			Integer seasonid = rs.getInt("idmembershipcontrol");
			
			Year year = new Year();
			year.setYearname(yearname);
			
			psminutes.setInt("seasonid", seasonid);
			rs2 = psminutes.executeQuery();
			List<Minute> mdata = new ArrayList<Minute>();
			
			while (rs2.next()) {
				
				String filename = rs2.getString("filename");
				String meetingdate = rs2.getString("displaymeetingdate");
				
				Minute minute = new Minute();
				minute.setFilename(filename);
				minute.setMeetingdate(meetingdate);
				
				mdata.add(minute);
			}
			year.setMinutes(mdata);
			data.add(year);
		}
		
		rs.close();
		ps.close();

		return new YearList(data);
	}

	
	
    @Override  
    public Year getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<Year> results = (List<Year>) getWrappedData();  
          
        for(Year result : results) {  
            if(Integer.toString(result.ID).equals(rowKey))  
                return result;  
        }  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Year result) {  
        return Integer.toString(result.ID);
    }

}
