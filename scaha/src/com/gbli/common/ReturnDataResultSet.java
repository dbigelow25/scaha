package com.gbli.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.scaha.objects.Club;
import com.scaha.objects.Tryout;
import com.scaha.objects.TryoutList;

public class ReturnDataResultSet extends ListDataModel<ReturnDataRow> implements Serializable, SelectableDataModel<ReturnDataRow> {

	ReturnDataRow rheader = null;
	
	/**
	 * 
	 * @param data
	 */
    public ReturnDataResultSet(List<ReturnDataRow> data, ReturnDataRow _header) {  
        super(data);  
        rheader = _header;
    }  
    
    
	
	
	/**
	 * Lets gobble up a result set into this object.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static ReturnDataResultSet NewReturnDataResultSetFactory(ResultSet rs) throws SQLException {
		
		List<ReturnDataRow> data = new ArrayList<ReturnDataRow>();
		
		ReturnDataRow rdrheader = new ReturnDataRow(-1);
		int iColCount = rs.getMetaData().getColumnCount();
		for (int y=1; y<=iColCount; y++) rdrheader.add(rs.getMetaData().getColumnLabel(y));
		
		int i = 0;
		while (rs.next()) {
			ReturnDataRow rdr = new ReturnDataRow(i++);
			for (int y=1; y<=iColCount; y++) rdr.add(rs.getObject(y));
			data.add(rdr);
			
		}
		
		return new ReturnDataResultSet(data, rdrheader);
	}
	
	
	
	@Override
	public ReturnDataRow getRowData (String rowKey)  {
        @SuppressWarnings("unchecked")
		List<ReturnDataRow> results = (List<ReturnDataRow>) getWrappedData();  
          
        for(ReturnDataRow result : results) {  
            if(result.getRowkey().equals(rowKey))  
                return result;  
        }  
        return null;
	}

	@Override
	public Object getRowKey(ReturnDataRow rdr) {
		return rdr.getRowkey();  
	}




	/**
	 * @return the rheader
	 */
	public ReturnDataRow getRheader() {
		return rheader;
	}




	/**
	 * @param rheader the rheader to set
	 */
	public void setRheader(ReturnDataRow rheader) {
		this.rheader = rheader;
	}

}
