package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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


public class editrosterdetailBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private Integer rosterid = null;
	private String teamid = null;
	private String playername = null;
	private String eligibility = null;
	private String dropdate = null;
	private String selectedrelation = null;
	private String active = null;
	private String closeurl = null;
	
	
    public editrosterdetailBean() {  
    	//will need to load player profile information for displaying loi
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	if(hsr.getParameter("playerid") != null)
        {
    		this.rosterid = Integer.parseInt(hsr.getParameter("playerid").toString());
        }
    	if(hsr.getParameter("teamid") != null)
        {
    		this.teamid = hsr.getParameter("teamid").toString();
        }
    	getRosterDetail();
    	this.closeurl = "editroster.xhtml?teamid=" + this.teamid;
    }  
    
    public String getCloseurl(){
    	return closeurl;
    }
    
    public void setCloseurl(String date){
    	closeurl=date;
    }
    
    
    public String getTeamid(){
    	return teamid;
    }
    
    public void setTeamid(String date){
    	teamid=date;
    }
    
    public String getActive(){
    	return active;
    }
    
    public void setActive(String date){
    	active=date;
    }
    
    public String getSelectedrelation(){
    	return selectedrelation;
    }
    
    public void setSelectedrelation(String date){
    	selectedrelation=date;
    }
    
    
    public String getDropdate(){
    	return dropdate;
    }
    
    public void setDropdate(String date){
    	dropdate=date;
    }
    
    public String getEligibility(){
    	return eligibility;
    }
    
    public void setEligibility(String eligible){
    	eligibility=eligible;
    }
    
    public String getPlayername(){
    	return playername;
    }
    
    public void setPlayername(String name){
    	playername=name;
    }
    
    public Integer getRosterid(){
    	return rosterid;
    }
    
    public void setRosterid(Integer id){
    	rosterid=id;
    }
    
    public void getRosterDetail(){
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getRosterDetail(?)");
			cs.setInt("rosterid", this.rosterid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					this.eligibility = rs.getString("eligibility");
					this.selectedrelation = rs.getString("rostertype");
					this.active = rs.getString("active");
					this.dropdate = rs.getString("dropped");
					this.playername = rs.getString("playername");
				}
				LOGGER.info("We have results for roster detail");
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
		
    }
    
    public void updateRoster(){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.updateRosterDetail(?,?,?,?,?)");
			cs.setInt("rosterid", this.rosterid);
			cs.setString("seligible", this.eligibility);
			if (this.dropdate.equals("")){
				cs.setString("dropdate", "12/31/2099");
			} else {
				cs.setString("dropdate", this.dropdate);
			}
			cs.setInt("active", Integer.parseInt(this.active));
			cs.setString("rstrtype", this.selectedrelation);
			
		    rs = cs.executeQuery();
			db.commit();
			db.cleanup();
    		
			FacesContext context = FacesContext.getCurrentInstance();
			try{
				context.getExternalContext().redirect("editroster.xhtml?teamid=" + teamid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
    }
    
	public void editrosterdetail(Player splayer){
		String idplayer = splayer.getIdplayer();
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("editrosterdetail.xhtml?playerid=" + idplayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

