package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Suspension;

//import com.gbli.common.SendMailSSL;
@ManagedBean
@ViewScoped
public class suspensionBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private List<Suspension> suspensions = null;
	transient private ResultSet rs = null;
	
	
	@PostConstruct
    public void init() {
		
        loadSuspensions();
        
        
	}
	
    public suspensionBean() {  
        
    }  
    
    public List<Suspension> getSuspensions(){
    	return suspensions;
    }
    
    public void setSuspensions(List<Suspension> list){
    	suspensions=list;
    }
    
    
    public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
		}
    	
    }
    
    
	public void loadSuspensions() {  
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		List<Suspension> tempresult = new ArrayList<Suspension>();
		
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getSuspensions()");
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					Integer idsuspension = rs.getInt("idsuspensions");
					String playername = rs.getString("playername");
					String team = rs.getString("team");
					String infraction = rs.getString("infraction");
					String nogames = rs.getString("numberofgames");
					String eligibility = rs.getString("eligibility");
					String match = rs.getString("matchpenalty");
					String suspensiondate = rs.getString("suspensiondate");
					
					Suspension susp = new Suspension();
					susp.setIdsuspension(idsuspension);
					susp.setPlayername(playername);
					susp.setTeam(team);
					susp.setInfraction(infraction);
					susp.setGames(nogames);
					susp.setEligibility(eligibility);
					susp.setMatch(match);
					susp.setSuspensiondate(suspensiondate);
					
					tempresult.add(susp);
    					}
				LOGGER.info("We have results for suspension list");
			}
			
			
			rs.close();
			db.cleanup();
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting suspension list");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	this.setSuspensions(tempresult);
		
    }
	
}

