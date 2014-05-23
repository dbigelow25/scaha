
package com.scaha.beans;

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Division;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;
import com.scaha.objects.SkillLevel;

@ManagedBean
@ViewScoped
public class safesportBean implements Serializable  {


	private PlayerDataModel playerlist = null;
	private PlayerDataModel personlist = null;
	
    
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private Boolean isscahareg = null;
	private List<Club> clubs = null;
	private List<Division> divisions = null;
	private List<SkillLevel> skilllevels = null;
	private String fname = null;
	private String lname = null;
	private String email = null;
	private String selectedclub = null;
	private String selecteddivision = null;
	private String selectedskilllevel = null;
	

	 @PostConstruct
	 public void init() {
		 
		LOGGER.info(" *************** POST INIT FOR safesportBean *****************");
		FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
		setIsscahareg(pb.hasRoleList("S-REG"));
		 
		LoadSelectionToAddList();
		LoadList();
	 }

	 public String getSelecteddivision(){
			return selecteddivision;
		}
		
		public void setSelecteddivision(String value){
			selecteddivision=value;
		}
	 
		public String getSelectedskilllevel(){
			return selectedskilllevel;
		}
		
		public void setSelectedskilllevel(String value){
			selectedskilllevel=value;
		}	
		
	 public String getSelectedclub(){
			return selectedclub;
		}
		
		public void setSelectedclub(String value){
			selectedclub=value;
		}
	 
	 public String getEmail(){
		return email;
	}
	
	public void setEmail(String value){
		email=value;
	}
		
	public String getFname(){
		return fname;
	}
	
	public void setFname(String value){
		fname=value;
	}
	 
	public String getLname(){
		return lname;
	}
	
	public void setLname(String value){
		lname=value;
	}
	 
	 public Boolean getIsscahareg(){
		return isscahareg;
	}
	
	public void setIsscahareg(Boolean value){
		isscahareg=value;
	}
	 
	public PlayerDataModel getPersonlist() {
		return personlist;
}

public void setPersonlist(PlayerDataModel playerlist) {
	this.personlist = playerlist;
}
	
	public PlayerDataModel getPlayerlist() {
			return playerlist;
	}

	/**
	 * @param playerlist the playerlist to set
	 */
	public void setPlayerlist(PlayerDataModel playerlist) {
		this.playerlist = playerlist;
	}

	public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
    	
    	}
	}
	
	public void setSafesport(Player inplayer,Integer flagstatus){
		String coachid = inplayer.getIdplayer();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		 
		try {
			CallableStatement cs = db.prepareCall("CALL scaha.setSafesportStatusByCoach(?,?)");
			cs.setInt("safesportstatusin", flagstatus);
			cs.setInt("incoachid",Integer.parseInt(coachid));
			ResultSet rs = cs.executeQuery();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		db.cleanup();
		
		LoadList();
	 }
	
	public void LoadList(){
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
  		 List<Player> templist = new ArrayList<Player>();
		 
  		 try {
  			CallableStatement cs = db.prepareCall("CALL scaha.getSafeSportRegisteredPersons()");
			ResultSet rs = cs.executeQuery();
   			
			while (rs.next()) {
				String idplayer = rs.getString("idplayer");
				String sfirstname = rs.getString("fname");
				String slastname = rs.getString("lname");
				String scurrentteam = rs.getString("currentteam");
				String status = rs.getString("safesportstatus");
				
				Player oplayer = new Player();
				oplayer.setIdplayer(idplayer);
				oplayer.setFirstname(sfirstname);
				oplayer.setLastname(slastname);
				oplayer.setCurrentteam(scurrentteam);
				oplayer.setEligibility(status);
				
				templist.add(oplayer);
				
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		db.free();
  		db.cleanup();
  		
  		setPlayerlist(new PlayerDataModel(templist));
	}
	
	
	//load list of persons for scaha reg to select from to add to the list.
	public void LoadSelectionToAddList(){
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
  		 List<Player> templist = new ArrayList<Player>();
		 
  		 try {
  			CallableStatement cs = db.prepareCall("CALL scaha.getSafeSportCandidates()");
			ResultSet rs = cs.executeQuery();
   			
			while (rs.next()) {
				String idplayer = rs.getString("idplayer");
				String sfirstname = rs.getString("fname");
				String slastname = rs.getString("lname");
				
				
				Player oplayer = new Player();
				oplayer.setIdplayer(idplayer);
				oplayer.setFirstname(sfirstname);
				oplayer.setLastname(slastname);
				
				templist.add(oplayer);
				
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		db.free();
  		db.cleanup();
  		
  		setPersonlist(new PlayerDataModel(templist));
	}
	
	public List<Club> getListofClubs(){
		List<Club> templist = new ArrayList<Club>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			CallableStatement cs = db.prepareCall("CALL scaha.getClubs()");
    			ResultSet rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					String idclub = rs.getString("idclubs");
        				String clubname = rs.getString("clubname");
        				
        				Club club = new Club();
        				club.setClubid(idclub);
        				club.setClubname(clubname);
        				
        				templist.add(club);
    				}
    				LOGGER.info("We have results for club list");
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
		
    	setClubs(templist);
		
		return getClubs();
	}
	
	public List<Club> getClubs(){
		return clubs;
	}
	
	public void setClubs(List<Club> list){
		clubs = list;
	}
	
	public List<Division> getListofDivisions(){
		List<Division> templist = new ArrayList<Division>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			//Vector<Integer> v = new Vector<Integer>();
    			//v.add(1);
    			//db.getData("CALL scaha.getTeamsByClub(?)", v);
    		    CallableStatement cs = db.prepareCall("CALL scaha.getDivisions()");
    		    ResultSet rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					Integer iddivision = rs.getInt("iddivisions");
        				String divisionname = rs.getString("division_name");
        				
        				Division division = new Division();
        				division.setIddivision(iddivision);
        				division.setDivisionname(divisionname);
        				templist.add(division);
    				}
    				LOGGER.info("We have results for club list");
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
	
	public List<SkillLevel> getListofSkillLevel(){
		List<SkillLevel> templist = new ArrayList<SkillLevel>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			CallableStatement cs = db.prepareCall("CALL scaha.getSkillLevels()");
    			ResultSet rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					Integer idskill = rs.getInt("idskilllevels");
        				String skilllevelname = rs.getString("levelsname");
        				
        				SkillLevel skill = new SkillLevel();
        				skill.setIdskilllevel(idskill);
        				skill.setSkilllevelname(skilllevelname);
        				templist.add(skill);
    				}
    				LOGGER.info("We have results for club list");
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
		
    	setSkilllevels(templist);
		
		return getSkilllevels();
	}
	
	public List<SkillLevel> getSkilllevels(){
		return skilllevels;
	}
	
	public void setSkilllevels(List<SkillLevel> list){
		skilllevels = list;
	}
	
	public void addPersontolist(){
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		//first need to create person then member then membership
	}
}
