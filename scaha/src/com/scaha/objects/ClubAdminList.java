package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
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

public class ClubAdminList extends ListDataModel<ClubAdmin> implements Serializable, SelectableDataModel<ClubAdmin> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	public ClubAdminList() {  
    }  
	
	/**
	 * This will get all ScahaNews Items and return a newsItemList
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static ClubAdminList NewClubAdminListFactory(Profile _pro, ScahaDatabase _db, Club _cl) throws SQLException {
		
			List<ClubAdmin> data = new ArrayList<ClubAdmin>();
			
			Vector<Integer> v = new Vector<Integer>();
			v.add(_cl.ID);
			_db.getData("CALL scaha.getClubAdminInfo(?)", v);
			ResultSet rs = _db.getResultSet();
			while (rs.next()) {
				int i = 1;
				Role rl = new Role(rs.getInt(i++),rs.getString(i++), rs.getString(i++),false);
				ClubAdmin ca = new ClubAdmin(rs.getInt(i++), _pro, _cl, rl);
				ca.setsFirstName(rs.getString(i++));
				ca.setsLastName(rs.getString(i++));
				ca.setsPhone(rs.getString(i++));
				ca.setsEmail(rs.getString(i++));
				data.add(ca);
			}
			rs.close();
		
			return new ClubAdminList(data);
	}
	
	
	/**
	 * 
	 * @param data
	 */
    public ClubAdminList(List<ClubAdmin> data) {  
        super(data);  
    }  
    
    @Override  
    public ClubAdmin getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<ClubAdmin> results = (List<ClubAdmin>) getWrappedData();  
          
        for(ClubAdmin result : results) {  
            if(Integer.toString(result.ID).equals(rowKey))  
                return result;  
        }  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ClubAdmin result) {  
        return Integer.toString(result.ID);  
    }

	
}
