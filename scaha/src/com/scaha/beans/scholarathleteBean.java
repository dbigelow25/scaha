package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Player;
import com.scaha.objects.Result;
import com.scaha.objects.ResultDataModel;

//import com.gbli.common.SendMailSSL;
@ManagedBean
@ViewScoped

public class scholarathleteBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private Result selectedplayer = null;
	private List<Result> results = null;
    private Integer clubid = null;
    private Integer profileid = null;
    private ResultDataModel listofplayers = null;
	
    @PostConstruct
    public void init() {
    	//lets load the list
    	getScholarAthletes();
    }  
    
    public Integer getProfid(){
    	return profileid;
    }
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
    }
    
    
    public Integer getClubid(){
    	return clubid;
    }
    
    public void setClubid(Integer sclub){
    	clubid = sclub;
    }
    
    /**
	 * @return the listofplayers
	 */
	public ResultDataModel getListofplayers() {
		return listofplayers;
	}

	/**
	 * @param listofplayers the listofplayers to set
	 */
	public void setListofplayers(ResultDataModel listofplayers) {
		this.listofplayers = listofplayers;
	}
	    
    //retrieves list of players for registrar to select from based on criteria provided
    public void getScholarAthletes(){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Result> tempresult = new ArrayList<Result>();
    	
    	try{

   			Vector<String> v = new Vector<String>();
   			db.getData("CALL scaha.getScholarathletes()", v);
   			ResultSet rs = db.getResultSet();
   
   			while (rs.next()) {
   				String playername = rs.getString("name");
        		String division = rs.getString("division");
        		String skilllevel = db.getResultSet().getString("skilllevel");
        		String club = db.getResultSet().getString("club");
        			
        		Result result = new Result(playername,"0",skilllevel,division);
        		result.setCurrentteam(club);
        		tempresult.add(result);
    		}
    				
    		LOGGER.info("We have results for scholar athletes ");
    		rs.close();
    		db.cleanup();
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting results for scholar athletes");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		db.free();
    	}
    	
    	listofplayers = new ResultDataModel(tempresult);
    }
    
}

    


