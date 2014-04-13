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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Result;
import com.scaha.objects.ResultDataModel;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;

public class DraftCoachesBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private String searchcriteria = null;
	private String selectedteam = null;
	private String selectedgirlsteam = null;
	private Result selectedcoach = null;
	private List<Team> teams = null;
    private List<Result> results = null;
    private ResultDataModel resultDataModel = null;
	
    @PostConstruct
    public void init() {
	    results = new ArrayList<Result>();  
          
        coachSearch(); 
  
        resultDataModel = new ResultDataModel(results);
        
        //will need to load player profile information
    }  
    
    public Result getSelectedcoach(){
		return selectedcoach;
	}
	
	public void setSelectedcoach(Result selectedCoach){
		selectedcoach = selectedCoach;
	}
	
	public String getSelectedteam(){
		return selectedteam;
	}
	
	public void setSelectedteam(String selectedTeam){
		selectedteam = selectedTeam;
	}
	
	public String getSelectedgirlsteam(){
		return selectedgirlsteam;
	}
	
	public void setSelectedgirlsteam(String selectedTeam){
		selectedgirlsteam = selectedTeam;
	}
	
	public List<Team> getListofTeams(String gender){
		List<Team> templist = new ArrayList<Team>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			//Vector<Integer> v = new Vector<Integer>();
    			//v.add(1);
    			//db.getData("CALL scaha.getTeamsByClub(?)", v);
    		    CallableStatement cs = db.prepareCall("CALL scaha.getTeamsByClub(?,?)");
    		    cs.setInt("pclubid", 1);
    		    cs.setString("gender", gender);
    			rs = cs.executeQuery();
    			
    			if (rs != null){
    				//need to add to an array
    				//rs = db.getResultSet();
    				
    				while (rs.next()) {
    					String idteam = rs.getString("idteams");
        				String teamname = rs.getString("teamname");
        				
        				Team team = new Team(teamname,idteam);
        				templist.add(team);
    				}
    				LOGGER.info("We have results for team list by club");
    			}
    			rs.close();
    			db.cleanup();
    		} else {
    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading teams");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setTeams(templist);
		
		return getTeams();
	}
	
	public List<Team> getTeams(){
		return teams;
	}
	
	public void setTeams(List<Team> list){
		teams = list;
	}
	
	public List<Result> getResults(){
		return results;
	}
	
	public void setResults(List<Result> list){
		results = list;
	}
	
	public String getSearchcriteria ()
    {
        return searchcriteria;
    }
    
    public void setSearchcriteria (final String searchcriteria)
    {
        this.searchcriteria = searchcriteria;
    }

    
    //retrieves list of players for registrar to select from based on criteria provided
    public void coachSearch(){
    
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Result> tempresult = new ArrayList<Result>();
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			Vector<String> v = new Vector<String>();
    			v.add(this.searchcriteria);
    			db.getData("CALL scaha.coachsearch(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					String idperson = rs.getString("idperson");
        				String coachname = rs.getString("fname") + " " + db.getResultSet().getString("lname");
        				String address = rs.getString("address");
        				String city = db.getResultSet().getString("city");
        				String state = db.getResultSet().getString("state");
        				String zip = db.getResultSet().getString("zipcode");
        				
        				if (address == null){
        					address = "";
        				}
        				if (city != null){
        					address = address + ", " + city;
        				}
        				if (state != null){
        					address = address + ", " + state;
        				}
        				if (zip != null){
        					address = address + " " + zip;
        				}
        				
        				Result result = new Result("",idperson,address,"");
        				result.setCoachname(coachname);
        				result.setIdcoach(idperson);
        				tempresult.add(result);
    				}
    				
    				LOGGER.info("We have results for search criteria " + this.searchcriteria);
    				
    			}
    			
    			rs.close();
    			db.cleanup();

    			LOGGER.info("We have searched players for " + this.searchcriteria);
    			//return "true";
    		} else {
    			//return "False";
    		}
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR " + this.searchcriteria);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	//setResults(tempresult);
    	resultDataModel = new ResultDataModel(tempresult);
    }

    public ResultDataModel getResultdatamodel(){
    	return resultDataModel;
    }
    
    //Generates the loi for family to confirm info and add registration code.  
    //If player is on delinquency or has previously loi'd message will be displayed.
    public void generateLOI(){
    	Result tempResult = selectedcoach;
    	String selectedCoachid = tempResult.getIdcoach();
    	String selectedCoachname = tempResult.getCoachname();
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", selectedCoachname + "'s LOI is being generated..."));
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("editablecoachloi.xhtml?coachid=" + selectedCoachid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void searchClose(){
    	searchcriteria = "";
    	results = null;

    	FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("Welcome.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

