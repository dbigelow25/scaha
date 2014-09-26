package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import com.gbli.common.SendMailSSL;
import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.ExhibitionGame;
import com.scaha.objects.ExhibitionGameDataModel;
import com.scaha.objects.LiveGame;
import com.scaha.objects.MailableObject;
import com.scaha.objects.RosterEdit;
import com.scaha.objects.RosterEditDataModel;
import com.scaha.objects.TempGame;
import com.scaha.objects.TempGameDataModel;
import com.scaha.objects.Tournament;
import com.scaha.objects.TournamentDataModel;
import com.scaha.objects.TournamentGame;
import com.scaha.objects.TournamentGameDataModel;

//import com.gbli.common.SendMailSSL;

@ManagedBean
@ViewScoped
public class mobileBean implements Serializable{

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	transient private ResultSet rs = null;
	//lists for generated datamodels
	private List<TempGame> games = null;
	
    
	//bean level properties used by multiple methods
	private Integer profileid = 0;
	
	//datamodels for all of the lists on the page
	private TempGameDataModel TempGameDataModel = null;
    
    //properties for storing the selected row of each of the datatables or drop downs
    private TempGame selectedgame = null;
    private Integer selectedschedule = null;
	private String divisionstring = null;
	private String weeklabel = null;
	
	@PostConstruct
    public void init() {
		games = new ArrayList<TempGame>();  
        TempGameDataModel = new TempGameDataModel(games);
        
        //need to grab division
      
  		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      	
      	if(hsr.getParameter("playerid") != null){
      		divisionstring = hsr.getParameter("division").toString();
      	}
          	
        
        //Load SCAHA Games
        loadScahaGames();
        
        
	}
	
    public mobileBean() {  
        
    }  
    
    public String getWeeklabel(){
    	return weeklabel;
    }
    
    public void setWeeklabel(String value){
    	weeklabel=value;
    }
    
    public String getDivisionstring(){
    	return divisionstring;
    }
    
    public void setDivisionstring(String value){
    	divisionstring=value;
    }
    
    public Integer getProfid(){
    	return profileid;
    }	
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
    }
    
    
    
    public TempGame getSelectedgame(){
		return selectedgame;
	}
	
	public void setSelectedgame(TempGame selectedGame){
		selectedgame = selectedGame;
	}
    
	public Integer getSelectedschedule(){
		return selectedschedule;
	}
	
	public void setSelectedschedule(Integer selectedSchedule){
		selectedschedule = selectedSchedule;
	}
    
	public List<TempGame> getGames(){
		return games;
	}
	
	public void setGames(List<TempGame> list){
		games = list;
	}
	
	    
    //retrieves list of existing teams for the club
    public void loadScahaGames(){
    	List<TempGame> tempresult = new ArrayList<TempGame>();
    	
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getSCAHAGamesForMobile(?)");
			cs.setString("divisionstring", divisionstring + " Regular Season");
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String idgame = rs.getString("idlivegame");
    				String hometeam = rs.getString("hometeam");
    				String awayteam = rs.getString("awayteam");
    				String homescore = rs.getString("homescore");
    				String awayscore = rs.getString("awayscore");
    				String dates = rs.getString("date");
    				String time = rs.getString("time");
    				String location = rs.getString("location");
    				String status = rs.getString("status");
    				
    				TempGame ogame = new TempGame();
    				ogame.setIdgame(Integer.parseInt(idgame));
    				ogame.setDate(dates);
    				ogame.setTime(time);
    				ogame.setVisitor(awayteam);
    				ogame.setHome(hometeam);
    				ogame.setLocation(location);
    				ogame.setAwayscore(awayscore);
    				ogame.setHomescore(homescore);
    				ogame.setStatus(status);
    				ogame.setRinkaddress("address");
    				ogame.setHometeamimage("kings");
    				ogame.setAwayteamimage("ducks");
    				tempresult.add(ogame);
    				
				}
				LOGGER.info("We have results for scaha games schedule for review by statistician for schedule:" + this.selectedschedule);
			}
			
			
			rs.close();
			db.cleanup();
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting scaha games schedule for review by statistician for schedule:" + this.selectedschedule);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	this.setWeeklabel("<games><game><id>1</id></game></games>");
    	
    	setGames(tempresult);
    	TempGameDataModel = new TempGameDataModel(games);
    }

    public TempGameDataModel getTempGamedatamodel(){
    	return TempGameDataModel;
    }
    
    public void setTempgamedatamodel(TempGameDataModel odatamodel){
    	TempGameDataModel = odatamodel;
    }

    
    
	
	
		
		
	
	
	
}

