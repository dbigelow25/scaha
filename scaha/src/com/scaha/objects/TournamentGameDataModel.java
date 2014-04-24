package com.scaha.objects;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TournamentGameDataModel extends ListDataModel<TournamentGame> implements Serializable, SelectableDataModel<TournamentGame> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TournamentGameDataModel() {  
    }  
  
    public TournamentGameDataModel(List<TournamentGame> data) {  
        super(data);  
    }  
      
    @SuppressWarnings("unchecked")
	@Override  
    public TournamentGame getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<TournamentGame> results = (List<TournamentGame>) getWrappedData();  
          
        for(TournamentGame result : results) {  
            if(result.getIdgame().equals(rowKey))  
                return result;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TournamentGame result) {  
        return result.getIdgame();  
    }  
	
}
