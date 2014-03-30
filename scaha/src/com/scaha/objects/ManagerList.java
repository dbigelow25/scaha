package com.scaha.objects;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;


import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
;

public class ManagerList extends ListDataModel<ScahaManager> implements Serializable, SelectableDataModel<ScahaManager> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	

	public ManagerList() {  
    }  
	
	/**
	 * 
	 * @param data
	 */
    public ManagerList(List<ScahaManager> data) {  
        super(data);  
    }  

	
	/**
	 * This will get all Coaches for a given Team and Season
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static ManagerList NewManagerListFactory(Profile _pro, ScahaDatabase _db,  ScahaTeam _tm) throws SQLException {
		
		List<ScahaManager> data = new ArrayList<ScahaManager>();
		
		Vector<Integer> v = new Vector<Integer>();
		v.add(new Integer(_tm.ID));
		
		ResultSet rs = null;
		if (_db.getData("call scaha.getAllManagerByTeam(?)",v)) {
			//
			// lets create new News Items.. and Let them rip
			//
			rs = _db.getResultSet();
			while (rs.next()) {
				int i = 1;
				ScahaManager sm = new ScahaManager(rs.getInt(i++),_pro);
				sm.setsFirstName(rs.getString(i++));
				sm.setsLastName(rs.getString(i++));
				sm.getGenatt().put("ROSTERTYPE",rs.getString(i++));
				sm.setsEmail(rs.getString(i++));
				sm.setsPhone(rs.getString(i++));
				data.add(sm);
			}
			rs.close();
			}
		
		
		//
		// now lets get the second level stuff..
		//
		LOGGER.info("Finished all the Coach List Loading!!");
		return new ManagerList(data);
	}

    
    @Override  
    public ScahaManager getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<ScahaManager> results = (List<ScahaManager>) getWrappedData();  
          
        for(ScahaManager result : results) {  
            if(Integer.toString(result.ID).equals(rowKey))  
                return result;  
        }  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ScahaManager result) {  
        return Integer.toString(result.ID);  
    }
	
}
