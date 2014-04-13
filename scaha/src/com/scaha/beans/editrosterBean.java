package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class editrosterBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private List<Player> players = null;
	private Player selectedplayer = null;
	private Integer teamid = null;
	private String teamname = null;
	
	@PostConstruct
    public void init() {
        // In @PostConstruct (will be invoked immediately after construction and dependency/property injection).
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("teamid") != null)
        {
    		this.teamid = Integer.parseInt(hsr.getParameter("teamid").toString());
        }
    	
    	getRoster();

    }
	
    public editrosterBean() {  
    	    	
		
    }  
    
    public String getTeamname(){
    	return teamname;
    }
    
    public void setTeamname(String name){
    	teamname=name;
    }
    
    public Integer getTeamid(){
    	return teamid;
    }
    
    public void setTeamid(Integer id){
    	teamid=id;
    }
    
    public Player getSelectedplayer(){
		return selectedplayer;
	}
	
	public void setSelectedplayer(Player splayer){
		selectedplayer = splayer;
	}
    
    
    public void getRoster(){
		List<Player> templist = new ArrayList<Player>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getTeamByTeamId(?)");
			cs.setInt("teamid", this.teamid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					this.teamname = rs.getString("teamname");
				}
				LOGGER.info("We have results for team name");
			}
			rs.close();
			db.cleanup();
    		
    		//next get roster
			cs = db.prepareCall("CALL scaha.getRosterByTeamId(?)");
			cs.setInt("teamid", this.teamid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String playerid = rs.getString("idroster");
					String fname = rs.getString("fname");
					String lname = rs.getString("lname");
					String eligibility = rs.getString("eligibility");
					String drop = rs.getString("dropped");
					String added = rs.getString("added");
					String active = rs.getString("active");
					String updated = rs.getString("updated");
					String jerseynumber = rs.getString("jerseynumber");
					
					
					Player player = new Player();
					player.setIdplayer(playerid);
					player.setFirstname(fname);
					player.setLastname(lname);
					player.setEligibility(eligibility);
					player.setDrop(drop);
					player.setAdded(added);
					player.setActive(active);
					player.setUpdated(updated);
					player.setJerseynumber(jerseynumber);
					
					templist.add(player);
				}
				LOGGER.info("We have results for team roster");
			}
			rs.close();
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
		
    	setPlayers(templist);
		
	}
    
    public List<Player> getPlayers(){
		return players;
	}
	
	public void setPlayers(List<Player> list){
		players = list;
	}
	
	public void editrosterdetail(Player splayer){
		String idplayer = splayer.getIdplayer();
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("editrosterdetail.xhtml?playerid=" + idplayer + "&teamid=" + this.teamid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

