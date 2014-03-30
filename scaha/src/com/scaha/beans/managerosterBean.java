package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class managerosterBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	transient private ResultSet rssub = null;
	private List<Club> clubs = null;
	private Team selectedteam = null;
	
	
    public managerosterBean() {  
        
    }  
    
    public Team getSelectedteam(){
		return selectedteam;
	}
	
	public void setSelectedteam(Team steam){
		selectedteam = steam;
	}
    
    
    public List<Club> getListofClubs(){
		List<Club> templist = new ArrayList<Club>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

			CallableStatement cs = db.prepareCall("CALL scaha.getClubs()");
		    rs = cs.executeQuery();
			
			if (rs != null){
				//need to add to an array
				//rs = db.getResultSet();
				
				while (rs.next()) {
					String idclub = rs.getString("idclubs");
					String clubname = rs.getString("clubname");
					
					Club club = new Club();
					club.setClubid(idclub);
					club.setClubname(clubname);
	
					//need to get list of teams for the club
					cs = db.prepareCall("CALL scaha.getTeamCountsbyClubId(?)");
	    		    cs.setInt("pclubid", Integer.parseInt(idclub));
	    		    rssub = cs.executeQuery();
	    		    List<Team> tempteamlist = new ArrayList<Team>();
	    		    
	    			if (rssub != null){
	    				
	    				while (rssub.next()) {
	    					String idteam = rssub.getString("idteams");
	        				String teamname = rssub.getString("teamname");
	        				String activeplayercount = rssub.getString("activeplayercount");
	        				String totalplayercount = rssub.getString("totalplayercount");
	        				String totalcoachcount = rssub.getString("totalcoachcount");
	        				
	        				Team team = new Team(teamname,idteam);
	        				team.setIdteam(idteam);
	        				team.setTeamname(teamname);
	        				team.setActiveplayercount(activeplayercount);
	        				team.setTotalplayercount(totalplayercount);
	        				team.setTotalcoachescount(totalcoachcount);
	        				
	        				tempteamlist.add(team);
	        			}
	    			}
	    			
	    			club.setTeams(tempteamlist);
					templist.add(club);
				}
				LOGGER.info("We have results for division list");
			}
			db.cleanup();
    		
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
		
    	setClubs(templist);
		
		return getClubs();
	}
    
    public List<Club> getClubs(){
		return clubs;
	}
	
	public void setClubs(List<Club> list){
		clubs = list;
	}
	
	public void viewroster(Team steam){
		String idteam = steam.getIdteam();
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("editroster.xhtml?teamid=" + idteam);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

