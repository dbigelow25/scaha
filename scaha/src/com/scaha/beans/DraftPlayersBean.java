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

public class DraftPlayersBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private String searchcriteria = null;
	private String selectedteam = null;
	private String selectedgirlsteam = null;
	private Result selectedplayer = null;
	private List<Team> teams = null;
    private List<Result> results = null;
    private ResultDataModel resultDataModel = null;
	private String origin = null;
    
    public DraftPlayersBean() {  
        results = new ArrayList<Result>();  
          
        playerSearch(); 
  
        resultDataModel = new ResultDataModel(results);
        
        //will need to load player profile information
    }  
    
    public Result getSelectedplayer(){
		return selectedplayer;
	}
	
	public void setSelectedplayer(Result selectedPlayer){
		selectedplayer = selectedPlayer;
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
    public void playerSearch(){
    
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Result> tempresult = new ArrayList<Result>();
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			Vector<String> v = new Vector<String>();
    			v.add(this.searchcriteria);
    			db.getData("CALL scaha.playersearch(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    						
    				
    				while (rs.next()) {
    					String idperson = rs.getString("idperson");
        				String playername = rs.getString("fname") + " " + db.getResultSet().getString("lname");
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
        				
        				
        				
        				String dob = rs.getString("dob");
        		
        				Result result = new Result(playername,idperson,address,dob);
        				tempresult.add(result);
    				}
    				
    				LOGGER.info("We have results for search criteria " + this.searchcriteria);
    				
    			}
    				
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
    	Result tempResult = selectedplayer;
    	String selectedPlayerid = tempResult.getIdplayer();
    	String selectedPlayername = tempResult.getPlayername();
    	
    	//need to check if selected player is on delinquent list 
    	Boolean delinquent = verifyDelinquency(selectedPlayerid);
    	Boolean previousloi = verifyNoPreviousLOI(selectedPlayerid);
    	
    	//display message that player is delinquent
    	if (delinquent){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", selectedPlayername + " is delinquent on a prior financial commitment.  An LOI can't be generated."));
    	}
    	
    	//display message that player is already loi'd
    	if (previousloi){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", selectedPlayername + " has rostered with another club.  An LOI can't be generated."));
    	}
    	
    	//we are successful lets display the loi page
    	if (!delinquent && !previousloi){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", selectedPlayername + "'s LOI is being generated..."));
    		
    		FacesContext context = FacesContext.getCurrentInstance();
    		origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
			try{
				context.getExternalContext().redirect("editableloi.xhtml?playerid=" + selectedPlayerid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private Boolean verifyDelinquency(String playerid){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	String isdelinquent = "";
    	
    	try{

    		
			Vector<Integer> v = new Vector<Integer>();
			v.add(Integer.parseInt(playerid));
			db.getData("CALL scaha.getDelinquencyByPlayerId(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				if (rs.next()){
					isdelinquent = rs.getString("delinquencycleared");
	    			
					LOGGER.info("We have matching delinquency for:" + playerid);
				}
			}
    				
    		db.cleanup();

    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR " + this.searchcriteria);
    		e.printStackTrace();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    
    	if (isdelinquent.equals("N")){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    private Boolean verifyNoPreviousLOI(String playerid){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	Integer previousTeamID = 0;
    	
    	try{

    		
			Vector<Integer> v = new Vector<Integer>();
			v.add(Integer.parseInt(playerid));
			db.getData("CALL scaha.isPlayerRostered(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				if (rs.next()){
					previousTeamID = rs.getInt("idteam");
	    			
					LOGGER.info("We have matching delinquency for:" + playerid);
				}
			}
    				
    			db.cleanup();

    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR " + this.searchcriteria);
    		e.printStackTrace();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    
    	if (previousTeamID>0){
    		return true;
    	}else{
    		return false;
    	}
    			
    }
    
    public void searchClose(){
    	searchcriteria = "";
    	results = null;

    	FacesContext context = FacesContext.getCurrentInstance();
		origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
		try{
			context.getExternalContext().redirect("Welcome.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
