package com.scaha.objects;

import java.io.Serializable;
import java.sql.Blob;
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

public class ClubList extends ListDataModel<Club> implements Serializable, SelectableDataModel<Club> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	

	public ClubList() {  
    }  
	
	/**
	 * This will get all ScahaNews Items and return a newsItemList
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static ClubList NewClubListFactory(Profile _pro, ScahaDatabase _db) throws SQLException {
		
		List<Club> data = new ArrayList<Club>();
		
		ResultSet rs = null;
		if (_db.getData("call scaha.getAllClubs()")) {
			//
			// lets create new News Items.. and Let them rip
			//
			rs = _db.getResultSet();
			while (rs.next()) {
				int i = 1;
				Club cl = new Club(rs.getInt(i++),_pro) ;
				cl.setCahaNumber(rs.getString(i++));
				cl.setTag(rs.getString(i++));
				cl.setSname(rs.getString(i++));
				cl.setClubname(rs.getString(i++));
				cl.setWebSite(rs.getString(i++));
				data.add(cl);
			}
			rs.close();
			}
		
		
		//
		// now lets get the second level stuff..
		//

		for (Club c : data) {
			LOGGER.info("CALL IS: call scaha.getMultiMedia(" + c.ID + ", 'CLUB', 'LOGO')");
			_db.getData("call scaha.getMultiMedia(" + c.ID + ", 'CLUB', 'LOGO')");
			rs = _db.getResultSet();
			while (rs.next()) {
				Blob blob = rs.getBlob(4);
			      // materialize BLOB onto client
				c.setLogoextension(rs.getString(2));
				c.setBlogo(blob.getBytes(1, (int) blob.length()));
				LOGGER.info("blob ext is(" + c.getLogoextension() + ", " + c.getBlogo().length);
			}
			rs.close();
			c.setCal(ClubAdminList.NewClubAdminListFactory(_pro, _db, c));
		}
		return new ClubList(data);
	}

	/**
	 * 
	 * @param data
	 */
    public ClubList(List<Club> data) {  
        super(data);  
    }  
    
    @Override  
    public Club getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<Club> results = (List<Club>) getWrappedData();  
          
        for(Club result : results) {  
            if(Integer.toString(result.ID).equals(rowKey))  
                return result;  
        }  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Club result) {  
        return Integer.toString(result.ID);  
    }

	
}
