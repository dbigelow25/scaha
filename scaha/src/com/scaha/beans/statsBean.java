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

import org.primefaces.model.LazyDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Division;
import com.scaha.objects.Stat;

//import com.gbli.common.SendMailSSL;

@ManagedBean
@ViewScoped
public class statsBean implements Serializable{

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	transient private ResultSet rs = null;
	//list for divisions to select from
	private List<Division> divisions = null;
	
	//list of leader groups to display
	private List<Stat> playergoals = null;
	private List<Stat> playerpoints = null;
	private List<Stat> playerassists = null;
	private List<Stat> playergaa = null;
	private List<Stat> playersavepercentage = null;
	private List<Stat> playerwins = null;
	private List<Stat> completeplayers = null;
	private List<Stat> completegoalies = null;
	
	//value of the selected division and year drop downs
	private String selecteddivision = null;
	private String selectedyear = null;
	private String selectedgametype = null;
	private String selectedcount = null;
	private String displaycompletetitle = null;
	
    @PostConstruct
    public void init() {
	    this.selectedcount="25";
	    this.selectedgametype="2";
    	this.selecteddivision="5";
    	this.selectedyear="2014";
    	
    	//load divisions to select from
    	getListofDivisions();
    	
        //Load leaders
        loadLeaders();
        
        
	}
	
    public statsBean() {  
        
    }  
    
    public String getDisplaycompletetitle(){
    	return displaycompletetitle;
    }
    
    public void setDisplaycompletetitle(String value){
    	displaycompletetitle=value;
    }
    
    public List<Stat> getPlayergoals(){
    	return playergoals;
    }
    
    public void setPlayergoals(List<Stat> list){
    	playergoals = list;
    }
	
    public List<Stat> getPlayerpoints(){
    	return playerpoints;
    }
    
    public void setPlayerpoints(List<Stat> list){
    	playerpoints = list;
    }
	
    public List<Stat> getPlayerassists(){
    	return playerassists;
    }
    
    public void setPlayerassists(List<Stat> list){
    	playerassists = list;
    }
    
    public List<Stat> getPlayergaa(){
    	return playergaa;
    }
    
    public void setPlayergaa(List<Stat> list){
    	playergaa = list;
    }
	
    public List<Stat> getPlayersavepercentage(){
    	return playersavepercentage;
    }
    
    public void setPlayersavepercentage(List<Stat> list){
    	playersavepercentage = list;
    }
	
    public List<Stat> getPlayerwins(){
    	return playerwins;
    }
    
    public void setPlayerwins(List<Stat> list){
    	playerwins = list;
    }
	
    public List<Stat> getCompleteplayers(){
    	return completeplayers;
    }
    
    public void setCompleteplayers(List<Stat> list){
    	completeplayers = list;
    }
	
    public List<Stat> getCompletegoalies(){
    	return completegoalies;
    }
    
    public void setCompletegoalies(List<Stat> list){
    	completegoalies = list;
    }
	
    //retrieves list of existing teams for the club
    public void loadLeaders(){
    	List<Stat> tempresult = new ArrayList<Stat>();
    	List<Stat> tempgoals = new ArrayList<Stat>();
    	List<Stat> tempassists = new ArrayList<Stat>();
    	List<Stat> tempgaa = new ArrayList<Stat>();
    	List<Stat> tempsave = new ArrayList<Stat>();
    	
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get top 5 goal scorers
    		CallableStatement cs = db.prepareCall("CALL scaha.getGoalleaders(?,?)");
    		cs.setInt("division", Integer.parseInt(this.selecteddivision));
    		cs.setInt("inyear", Integer.parseInt(this.selectedyear));
			rs = cs.executeQuery();
			Integer count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String goals = rs.getString("goals");
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setGoals(goals);
    				tempgoals.add(stat);
    				
				}
				LOGGER.info("We have results for goal leaders:");
			}
	
			this.setPlayergoals(tempgoals);
			
			
			//get points leaders
			cs = db.prepareCall("CALL scaha.getPointsleaders(?,?)");
			cs.setInt("division", Integer.parseInt(this.selecteddivision));
			cs.setInt("inyear", Integer.parseInt(this.selectedyear));
			rs = cs.executeQuery();
			count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String points = rs.getString("points");
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setPoints(points);
    				tempresult.add(stat);
    				
				}
				LOGGER.info("We have results for leaders:");
			}
			
			this.setPlayerpoints(tempresult);
			
			//get assists leaders
			cs = db.prepareCall("CALL scaha.getAssistsleaders(?,?)");
			cs.setInt("division", Integer.parseInt(this.selecteddivision));
			cs.setInt("inyear", Integer.parseInt(this.selectedyear));
			rs = cs.executeQuery();
			count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String assists = rs.getString("assists");
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setAssists(assists);
    				tempassists.add(stat);
    				
				}
				LOGGER.info("We have results for leaders:");
			}
			
			this.setPlayerassists(tempassists);
			
			
			//get gaa leaders
			cs = db.prepareCall("CALL scaha.getGaaleaders(?,?)");
			cs.setInt("division", Integer.parseInt(this.selecteddivision));
			cs.setInt("inyear", Integer.parseInt(this.selectedyear));
			rs = cs.executeQuery();
			count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String gaa = rs.getString("gaa");
    				if (gaa==null){
    					gaa = "0.00";
    				}
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setGaa(gaa);
    				tempgaa.add(stat);
    				
				}
				LOGGER.info("We have results for leaders:");
			}
			
			this.setPlayergaa(tempgaa);
			
			//get sv % leaders
			cs = db.prepareCall("CALL scaha.getSavepercentageleaders(?,?)");
			cs.setInt("division", Integer.parseInt(this.selecteddivision));
			cs.setInt("inyear", Integer.parseInt(this.selectedyear));
			rs = cs.executeQuery();
			count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String savepercentage = rs.getString("savepercentage");
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setSavepercentage(savepercentage);
    				tempsave.add(stat);
    				
				}
				LOGGER.info("We have results for leaders:");
			}
			
			this.setPlayersavepercentage(tempsave);
			
			
			rs.close();
			db.cleanup();
    		
			
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting leaders list");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	
    }

    public List<Division> getListofDivisions(){
		List<Division> templist = new ArrayList<Division>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			CallableStatement cs = db.prepareCall("CALL scaha.getStatsDivisions()");
    		    rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					Integer iddivision = rs.getInt("iddivisions");
        				String divisionname = rs.getString("division_name");
        				
        				Division division = new Division();
        				division.setIddivision(iddivision);
        				division.setDivisionname(divisionname);
        				templist.add(division);
    				}
    				LOGGER.info("We have results for division list");
    			}
    			rs.close();
    			db.cleanup();
    		} else {
    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading clubs");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setDivisions(templist);
		
		return getDivisions();
	}
    
    public List<Division> getDivisions(){
		return divisions;
	}
	
	public void setDivisions(List<Division> list){
		divisions = list;
	}

	public String getSelectedgametype(){
		return selectedgametype;
	}
	
	public void setSelectedgametype(String value){
		selectedgametype=value;
	}
	
	public String getSelectedcount(){
		return selectedcount;
	}
	
	public void setSelectedcount(String value){
		selectedcount=value;
	}
	
	
	public String getSelecteddivision(){
		return selecteddivision;
	}
	
	public void setSelecteddivision(String value){
		selecteddivision=value;
	}
	
	public String getSelectedyear(){
		return selectedyear;
	}
	
	public void setSelectedyear(String value){
		selectedyear=value;
	}
	
	public void loadCompletePlayerStats(String sortby){
		List<Stat> tempsave = new ArrayList<Stat>();
		setDisplaycompletetitle(sortby);
		
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		CallableStatement cs = db.prepareCall("CALL scaha.getPlayerstats(?,?,?,?,?)");
    		cs.setInt("division", Integer.parseInt(this.selecteddivision));
    		cs.setInt("inyear", Integer.parseInt(this.selectedyear));
    		cs.setString("sorting", sortby);
    		cs.setInt("ingametype", Integer.parseInt(this.selectedgametype));
    		cs.setInt("count", Integer.parseInt(this.selectedcount));
			rs = cs.executeQuery();
			Integer count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String goals = rs.getString("goals");
    				String assists = rs.getString("assists");
    				String points = rs.getString("points");
    				String pims = rs.getString("pims");
    				if (pims==null){
    					pims = "0";
    				}
    				
    				String gp=rs.getString("gp");
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setGoals(goals);
    				stat.setAssists(assists);
    				stat.setPoints(points);
    				stat.setPims(pims);
    				stat.setGp(gp);
    				tempsave.add(stat);
    				
				}
				LOGGER.info("We have results for goal leaders:");
			} else {
	    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading clubs");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
			
    	this.setCompleteplayers(tempsave);
		
    }
	
	
	public void loadCompleteGoalieStats(String sortby){
		List<Stat> tempsave = new ArrayList<Stat>();
    	
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get top 5 goal scorers
    		CallableStatement cs = db.prepareCall("CALL scaha.getGoaliestats(?,?,?,?,?)");
    		cs.setInt("division", Integer.parseInt(this.selecteddivision));
    		cs.setInt("inyear", Integer.parseInt(this.selectedyear));
    		cs.setString("sorting", sortby);
    		cs.setInt("ingametype", Integer.parseInt(this.selectedgametype));
    		cs.setInt("count", Integer.parseInt(this.selectedcount));
			
    		rs = cs.executeQuery();
			Integer count = 1;
			
			if (rs != null){
				
				while (rs.next()) {
					Integer rank = count++;
    				String playername = rs.getString("playername");
    				String teamname = rs.getString("teamname");
    				String mins = rs.getString("mins");
    				if (mins==null){
    					mins = "0";
    				}
    				
    				String shots = rs.getString("shots");
    				if (shots==null){
    					shots = "0";
    				}
    				
    				String saves = rs.getString("saves");
    				if (saves==null){
    					saves = "0";
    				}
    				
    				String percentage = rs.getString("percentage");
    				if (percentage==null){
    					percentage = "0.000";
    				}
    				
    				String gaa=rs.getString("gaa");
    				if (gaa==null){
    					gaa = "0.00";
    				}
    				
    				String gp=rs.getString("gp");
    				if (gp==null){
    					gp = "0";
    				}
    				
    				
    				Stat stat = new Stat();
    				stat.setRank(rank.toString());
    				stat.setPlayername(playername);
    				stat.setTeamname(teamname);
    				stat.setMins(mins);
    				stat.setShots(shots);
    				stat.setSaves(saves);
    				stat.setSavepercentage(percentage);
    				stat.setGp(gp);
    				stat.setGaa(gaa);
    				tempsave.add(stat);
    				
				}
				LOGGER.info("We have results for goal leaders:");
			} else {
	    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading clubs");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	//LazyStatDataModel temp = new LazyStatDataModel(tempsave); 
    	this.setCompletegoalies(tempsave);
		
    }
}

