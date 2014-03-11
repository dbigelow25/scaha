package com.scaha.objects;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ResultDataModel extends ListDataModel<Result> implements Serializable, SelectableDataModel<Result> {

	public ResultDataModel() {  
    }  
  
    public ResultDataModel(List<Result> data) {  
        super(data);  
    }  
      
    @Override  
    public Result getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Result> results = (List<Result>) getWrappedData();  
          
        for(Result result : results) {  
            if(result.getIdplayer().equals(rowKey))  
                return result;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Result result) {  
        return result.getIdplayer();  
    }  
	
}
