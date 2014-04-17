package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Division;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.SkillLevel;
import com.scaha.objects.Team;
import com.scaha.objects.TeamDataModel;

//import com.gbli.common.SendMailSSL;

@ManagedBean
@SessionScoped
public class teamBean implements Serializable, MailableObject {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;

	private List<Team> teams = null;
    private List<Division> divisions= null;
    private List<SkillLevel> skilllevels= null;
    private TeamDataModel TeamDataModel = null;
	private String selectedskilllevel = null;
	private String selecteddivision = null;
	private String teamname = null;
	private Integer idclub = null;
	private Team selectedteam = null;
	private String teamstabletitle = null;
	private Integer profileid = 0;
	private Boolean ishighschool = null;
	
	@PostConstruct
    public void init() {
		teams = new ArrayList<Team>();  
        TeamDataModel = new TeamDataModel(teams);
        
        idclub = 1;  
    	this.setProfid(pb.getProfile().ID);
        getClubID();
        isClubHighSchool();
    	
        populateTableTitle(idclub);
        teamsForClub(idclub); 

	}
	
    public teamBean() {  
        
    }  
    
    public Boolean getIshighschool(){
    	return ishighschool;
    }
    
    public void setIshighschool(Boolean value){
    	ishighschool = value;
    }
    
    
    public Integer getProfid(){
    	return profileid;
    }
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
    }
    
    
    public String getTeamstabletitle(){
		return teamstabletitle;
	}
	
	public void setTeamstabletitle(String stitle){
		teamstabletitle = stitle;
	}
    
    public Team getSelectedteam(){
		return selectedteam;
	}
	
	public void setSelectedteam(Team selectedTeam){
		selectedteam = selectedTeam;
	}
    
    public String getSelectedskilllevel(){
		return selectedskilllevel;
	}
	
	public void setSelectedskilllevel(String selectedSkill){
		selectedskilllevel = selectedSkill;
	}
	
	public String getSelecteddivision(){
		return selecteddivision;
	}
	
	public void setSelecteddivision(String selectedDivision){
		selecteddivision = selectedDivision;
	}
	
	public String getTeamname(){
		return teamname;
	}
	
	public void setTeamname(String steamname){
		teamname = steamname;
	}
	
	public List<Division> getListofDivisions(){
		List<Division> templist = new ArrayList<Division>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

   		    CallableStatement cs = null;
   		    
		    if (this.ishighschool){
		    	cs = db.prepareCall("CALL scaha.getDivisionsForHighSchool()");
		    } else {
		    	cs = db.prepareCall("CALL scaha.getDivisionsForSCAHA()");
		    }
    		    
		    ResultSet rs = cs.executeQuery();
			while (rs.next()) {
				Integer iddivision = rs.getInt("iddivisions");
    			String divisionname = rs.getString("division_name");
    			
    			Division division = new Division();
    			division.setDivisionname(divisionname);
    			division.setIddivision(iddivision);
    						
    			templist.add(division);
			}
   			LOGGER.info("We have results for division list");
   			rs.close();
   			cs.close();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading teams");
    		e.printStackTrace();
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
	
	public List<SkillLevel> getListofSkillLevels(){
		List<SkillLevel> templist = new ArrayList<SkillLevel>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		
		    CallableStatement cs = null;
		    
		    if (this.ishighschool){
		    	cs = db.prepareCall("CALL scaha.getSkilllevelsForHighSchool()");
		    } else {
		    	cs = db.prepareCall("CALL scaha.getSkilllevelsForSCAHA()");
		    }
		    
		    
		    ResultSet rs = cs.executeQuery();
    			
			if (rs != null){
				//need to add to an array
				//rs = db.getResultSet();
				
				while (rs.next()) {
					Integer idskilllevel = rs.getInt("idskilllevels");
    				String levelsname = rs.getString("levelsname");
    				
    				SkillLevel level = new SkillLevel();
    				level.setSkilllevelname(levelsname);
    				level.setIdskilllevel(idskilllevel);
    						
    				templist.add(level);
				}
				LOGGER.info("We have results for division list");
			}
			rs.close();
			db.cleanup();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading teams");
    		e.printStackTrace();
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
	
	
	
	public List<Team> getTeams(){
		return teams;
	}
	
	public void setTeams(List<Team> list){
		teams = list;
	}
	
	    
    //retrieves list of existing teams for the club
    public void teamsForClub(Integer idclub){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Team> tempresult = new ArrayList<Team>();
    	
    	try{
    			
			CallableStatement cs = db.prepareCall("CALL scaha.getTeamsForRegistrarList(?,?)");
		    cs.setInt("clubid", idclub);
		    cs.setInt("inputyear", 2014);
		    ResultSet rs = cs.executeQuery();
    			
			if (rs != null){
				
				while (rs.next()) {
					String idteam = rs.getString("idteams");
    				String steamname = rs.getString("teamname");
    				String skillname= rs.getString("levelsname");
    				String division_name = rs.getString("division_name");
    				
    				Team oteam = new Team(steamname,idteam);
    				oteam.setDivisionname(division_name);
    				oteam.setSkillname(skillname);
    				tempresult.add(oteam);
				}
				
				LOGGER.info("We have results for teams by club" + idclub);
				
			}
   			rs.close();	
    		cs.close();
   			db.cleanup();

   			LOGGER.info("We have tried to retrieve teams for club" + idclub);
    			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR Teams for club:" + idclub);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	//setResults(tempresult);
    	setTeams(tempresult);
    	TeamDataModel = new TeamDataModel(teams);
    }

    public TeamDataModel getTeamdatamodel(){
    	return TeamDataModel;
    }
    
    public void setTeamdatamodel(TeamDataModel odatamodel){
    	TeamDataModel = odatamodel;
    }

    /**
     * This simply saves a new team to the system.
     * 
     */
    public void saveTeam(){
   		// Log it for test
		LOGGER.info("Here are team Add Parms:" + this.idclub + ":" + this.teamname + ":" + 
				getteamgender() + ":" + this.selectedskilllevel + ":"+ this.selecteddivision + ":2014:");
		if (scaha == null) {
			LOGGER.info("SCAHA IS NULL!!");
		} else if (scaha.getScahaSeasonList() == null) {
			LOGGER.info("SCAHA SEASON LIST IS NULL!!");
		} else if (scaha.getScahaSeasonList().getCurrentSeason() == null) {
			LOGGER.info("SCAHA DEFAULT SEASON IS NULL!!");
		} else {
			LOGGER.info(scaha.getScahaSeasonList().getCurrentSeason().toString());
		}
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
 			CallableStatement cs = db.prepareCall("CALL scaha.addTeam(?,?,?,?,?,?,?)");
			cs.setInt("pclubid", this.idclub);
		    cs.setString("teamname", this.teamname);
		    cs.setString("teamgender", getteamgender());
		    cs.setInt("skilllevelid", Integer.parseInt(this.selectedskilllevel));
		    cs.setInt("divisionsid", Integer.parseInt(this.selecteddivision));
			cs.setInt("currentyear", 2014);
			cs.setString("in_seasontag", scaha.getScahaSeasonList().getCurrentSeason().getTag());
			cs.executeUpdate();
			cs.close();
			LOGGER.info("Saved a new team");
    	} catch (SQLException e) {
    		LOGGER.info("ERROR IN loading teams");
    		e.printStackTrace();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}

    	SendMailSSL mail = new SendMailSSL(this);
		mail.sendMail();
		
    	teamsForClub(idclub);
    	this.setSelecteddivision(null);
    	this.setSelectedskilllevel(null);
    	this.setTeamname(null);
    	
    }
    
    private String getteamgender(){
    	
    	if ((Integer.parseInt(this.selecteddivision) >= 7) && (Integer.parseInt(this.selecteddivision) <= 12)){
    		return "F";
    	} else {
    		return "M";
    	}
    	
    	
    }
    
    public void populateTableTitle(Integer idclub){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

   			CallableStatement cs = db.prepareCall("CALL scaha.getClubNamebyId(?)");
   		    cs.setInt("clubid", idclub);
   		    ResultSet rs = cs.executeQuery();
   			
			while (rs.next()) {
				String sclubname = rs.getString("clubname");
   				this.setTeamstabletitle("2014 " + sclubname + " Teams");
   			}
				
			LOGGER.info("We have results for teams by club" + idclub);
				
			rs.close();	
			cs.close();
			db.cleanup();

			LOGGER.info("We have tried to retrieve teams for club" + idclub);
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR Teams for club:" + idclub);
    		e.printStackTrace();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    }
    
    public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
		}
    	
    }
    
    public void getClubID(){
		
		//first lets get club id for the logged in profile
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{
			Vector<Integer> v = new Vector<Integer>();
			v.add(this.getProfid());
			db.getData("CALL scaha.getClubforPerson(?)", v);
		    ResultSet rs = db.getResultSet();
			while (rs.next()) {
				this.idclub = rs.getInt("idclub");
			}
			rs.close();
			LOGGER.info("We have results for club for a profile");
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading club by profile");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}

    }
	
    public void isClubHighSchool(){
			
			//first lets get club id for the logged in profile
			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
			Integer isschool = 0;
			try{
				Vector<Integer> v = new Vector<Integer>();
				v.add(this.idclub);
				db.getData("CALL scaha.IsClubHighSchool(?)", v);
			    ResultSet rs = db.getResultSet();
				while (rs.next()) {
					isschool = rs.getInt("result");
				}
				LOGGER.info("We have results for club is a high school");
				db.cleanup();
				
				if (isschool.equals(0)){
					this.ishighschool=false;
				}else{
					this.ishighschool=true;
				}
	    	} catch (SQLException e) {
	    		// TODO Auto-generated catch block
	    		LOGGER.info("ERROR IN loading club by profile");
	    		e.printStackTrace();
	    		db.rollback();
	    	} finally {
	    		//
	    		// always clean up after yourself..
	    		//
	    		db.free();
	    	}
	}

    
    /**
	 * @return the scaha
	 */
	public ScahaBean getScaha() {
		return scaha;
	}

	/**
	 * @param scaha the scaha to set
	 */
	public void setScaha(ScahaBean scaha) {
		this.scaha = scaha;
	}

	
	/**
	 * @return the pb
	 */
	public ProfileBean getPb() {
		return pb;
	}

	/**
	 * @param pb the pb to set
	 */
	public void setPb(ProfileBean pb) {
		this.pb = pb;
	}

	@Override
	public String getSubject() {
		return "A New Team was Added for ClubID: " + this.idclub;
	}
	@Override
	public String getTextBody() {
		return "A New team was added to the Club:" + this.teamname + ":"  + getteamgender() + ":" + this.selectedskilllevel + ":" + this.selecteddivision + "<\\p>" +
	            "This team was added by:" + pb.getFirstName() + " " + pb.getLastName();
	}

	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "online@iscaha.com,scheduler@iscaha.com";
	}
	
	@Override
	public String getToMailAddress() {
		//
		// Tmp hardcoded.. we simply need to pull the e-mail for the Scaha Registrar
		//
		return "dux8fan@aol.com";
	
	}

}

