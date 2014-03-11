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

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyRow;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class coachloiBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private List<String> selectedteams = null;
	private List<String> selectedgirlsteams = null;
	private List<String> cepmodulesselected = null;
	private Integer selectedcoach = 0;
	private List<Team> teams = null;
	private String firstname = null;
	private String lastname = null;
	private String address = null;
	private String state = null;
	private String city = null;
	private String zip = null;
	private String homenumber = null;
	private String email = null;
	private String loicode = null;
	private Integer clubid = null;
	private String displayselectedteam = null;
	private String displayselectedgirlsteam = null;
	private String currentdate = null;
	private Integer profileid = 0;
	private String screeningexpires = null;
	private String cepnumber = null;
	private String ceplevel = null;
	private String cepexpires = null;
	private String boysteams = null;
	private String girlsteams = null;
	private String cepmoduledisplaystring = null;
	
    
    public coachloiBean() {  
        
    	//hard code value until we load session variable
    	clubid = 1;
    	
    	//load profile id from which we can get club id later
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
    	this.setProfid(pb.getProfile().getID());
    	
		//will need to load player profile information for displaying loi
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("coachid") != null)
        {
    		selectedcoach = Integer.parseInt(hsr.getParameter("coachid").toString());
        }
    	
    	loadCoachProfile(selectedcoach);

    	//doing anything else right here
    }  
    
    
    public void setCepmoduledisplaystring(String snumber){
    	cepmoduledisplaystring = snumber;
    }
    
    public String getCepmoduledisplaystring(){
    	return cepmoduledisplaystring;
    }
    
    
    public void setGirlsteams(String snumber){
    	girlsteams = snumber;
    }
    
    public String getGirlsteams(){
    	return girlsteams;
    }
    
    public void setBoysteams(String snumber){
    	boysteams = snumber;
    }
    
    public String getBoysteams(){
    	return boysteams;
    }
    
    public void setCepexpires(String snumber){
    	cepexpires = snumber;
    }
    
    public String getCepexpires(){
    	return cepexpires;
    }
    
    public void setCeplevel(String snumber){
    	ceplevel = snumber;
    }
    
    public String getCeplevel(){
    	return ceplevel;
    }
    
    public void setCepnumber(String snumber){
    	cepnumber = snumber;
    }
    
    public String getCepnumber(){
    	return cepnumber;
    }
    
    public void setScreeningexpires(String snumber){
    	screeningexpires = snumber;
    }
    
    public String getScreeningexpires(){
    	return screeningexpires;
    }
    
    public void setHomenumber(String snumber){
    	homenumber = snumber;
    }
    
    public String getHomenumber(){
    	return homenumber;
    }
    
    
    public void setEmail(String snumber){
    	email = snumber;
    }
    
    public String getEmail(){
    	return email;
    }
    
    public Integer getProfid(){
    	return profileid;
    }
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
    }
    
    public void setCurrentdate(String scode){
    	currentdate = scode;
    }
    
    public String getCurrentdate(){
    	return currentdate;
    }
    
    public void setDisplayselectedgirlsteam(String scode){
    	displayselectedgirlsteam = scode;
    }
    
    public String getDisplayselectedgirlsteam(){
    	return displayselectedgirlsteam;
    }
    
    public void setDisplayselectedteam(String scode){
    	displayselectedteam = scode;
    }
    
    public String getDisplayselectedteam(){
    	return displayselectedteam;
    }
    
    
    public void setLoicode(String scode){
    	loicode = scode;
    }
    
    public String getLoicode(){
    	return loicode;
    }
    
    public Integer getClubid(){
    	return clubid;
    }
    
    public void setClubid(Integer sclub){
    	clubid = sclub;
    }
    
    
    public void setZip(String szip){
    	zip = szip;
    }
    
    public String getZip(){
    	return zip;
    }
    
    public void setState(String sstate){
    	state = sstate;
    }
    
    public String getState(){
    	return state;
    }
    
    public void setCity(String scity){
    	city = scity;
    }
    
    public String getCity(){
    	return city;
    }
    
    public void setAddress(String saddress){
    	address = saddress;
    }
    
    public String getAddress(){
    	return address;
    }
    
    public void setLastname(String lname){
    	lastname = lname;
    }
    
    public String getLastname(){
    	return lastname;
    }
    public void setFirstname(String fname){
    	firstname = fname;
    }
    public String getFirstname(){
    	return firstname;
    }
    public Integer getSelectedcoach(){
		return selectedcoach;
	}
	
	public void setSelectedcoach(Integer selectedCoach){
		selectedcoach = selectedCoach;
	}
	
	public List<String> getSelectedteams(){
		return selectedteams;
	}
	
	public void setSelectedteams(List<String> selectedTeams){
		selectedteams = selectedTeams;
	}
	
	public List<String> getCepmodulesselected(){
		return cepmodulesselected;
	}
	
	public void setCepmodulesselected(List<String> selectedTeams){
		cepmodulesselected = selectedTeams;
	}
	
	public List<String> getSelectedgirlsteams(){
		return selectedgirlsteams;
	}
	
	public void setSelectedgirlsteams(List<String> selectedTeams){
		selectedgirlsteams = selectedTeams;
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
    		    cs.setInt("pclubid", clubid);
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
	
	//used to populate loi form with player information
	public void loadCoachProfile(Integer selectedcoach){
		//first get player detail information then get family members
		Integer personID = 0;
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		currentdate = dateFormat.format(date);
		
		try{

    		if (!selectedcoach.equals("")) {
    		
    			Vector<Integer> v = new Vector<Integer>();
    			v.add(selectedcoach);
    			db.getData("CALL scaha.getCoachInfoByPersonId(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					firstname = rs.getString("fname");
    					lastname = rs.getString("lname");
        				address = rs.getString("address");
        				city = rs.getString("city");
        				state = rs.getString("state");
        				zip = rs.getString("zipcode");
        				personID = rs.getInt("idperson");
        				homenumber = rs.getString("phone");
        				screeningexpires = rs.getString("screeningexpires");
        				cepnumber = rs.getString("cepnumber");
        				ceplevel = rs.getString("ceplevel");
        				
        				if (ceplevel.equals("1")){
        					ceplevel = "Level 1";
        				}
        				if (ceplevel.equals("2")){
        					ceplevel = "Level 2";
        				}
        				if (ceplevel.equals("3")){
        					ceplevel = "Level 3";
        				}
        				if (ceplevel.equals("4")){
        					ceplevel = "Level 4";
        				}
        				if (ceplevel.equals("5")){
        					ceplevel = "Level 5";
        				}
        				if (ceplevel.equals("0")){
        					ceplevel = "";
        				}
        				cepexpires = rs.getString("cepexpires");
        				
        			}
    				LOGGER.info("We have results for player details by player id");
    			}
    			db.cleanup();
    			
    			//need to get list of boys teams signed for
    			v = new Vector<Integer>();
    			v.add(selectedcoach);
    			db.getData("CALL scaha.getCoachTeams(?)", v);
    		    
    			String teamname = null;
    			
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					teamname = rs.getString("teamname") + "</br>" ;
    				}
    				LOGGER.info("We have results for teams for the coach");
    			}
    			this.boysteams = teamname;
    			teamname = "";
    			db.cleanup();
    			
    			//need to get list of girls teams signed for
    			v = new Vector<Integer>();
    			v.add(selectedcoach);
    			db.getData("CALL scaha.getCoachGirlsTeams(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					teamname = teamname + rs.getString("teamname") + "</br>" ;
    				}
    				LOGGER.info("We have results for teams for the coach");
    			}
    			this.girlsteams = teamname;
    			teamname = "";
    			db.cleanup();
    			
    			
    		} else {
    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading player details");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	
    	
		
		
   }
	
	public void completeLOI(){

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to check loi code from family first
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.validateMemberNumber(?,?)");
 				cs.setString("memnumber", this.loicode);
 				cs.setInt("personid", this.selectedcoach);
    		    
    		    rs = cs.executeQuery();
    			
    		    Integer resultcount = 0;
    		    if (rs != null){
    				
    				while (rs.next()) {
    					resultcount = rs.getInt("idmember");
    				}
    				LOGGER.info("We have player up code validation results for player details by player id");
    			}
    			db.cleanup();
 				
    		    
    			if (resultcount > 0){
    		    	
	    		    //if good save info to person table, then add record to roster then email
	 				LOGGER.info("updating person record");
	 				cs = db.prepareCall("CALL scaha.updatePersonInfoAddress(?,?,?,?,?)");
	    		    cs.setInt("ipersonid", this.selectedcoach);
	    		    cs.setString("iaddress", this.address);
	    		    cs.setString("icity", this.city);
	    		    cs.setString("istate", this.state);
	    		    cs.setString("izipcode", this.zip);
	    			rs = cs.executeQuery();
	    			
	    			//need to save coaches screening and cep stuff
	    			LOGGER.info("updating coach record");
	 				cs = db.prepareCall("CALL scaha.updateCoach(?,?,?,?,?,?,?,?,?,?,?)");
	    		    cs.setInt("coachid", this.selectedcoach);
	    		    cs.setString("screenexpires", this.screeningexpires);
	    		    cs.setString("cepnum", this.cepnumber);
	    		    cs.setString("levelcep", this.ceplevel);
	    		    cs.setString("cepexpire", this.cepexpires);
	    		    
	    		    //need to set values for modules
	    		    Integer u8 = 0;
	    		    Integer u10 = 0;
	    		    Integer u12 = 0;
	    		    Integer u14 = 0;
	    		    Integer u18 = 0;
	    		    Integer ugirls = 0;
	    		    		
	    		    for (int i = 0; i < this.cepmodulesselected.size(); i++) {
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("8U")){
	    		    		u8 = 1;
	    		    	}
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("10U")){
	    		    		u10 = 1;
	    		    	}
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("12U")){
	    		    		u12 = 1;
	    		    	}
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("14U")){
	    		    		u14 = 1;
	    		    	}
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("18U")){
	    		    		u18 = 1;
	    		    	}
	    		    	if (this.cepmodulesselected.get(i).equalsIgnoreCase("Girls")){
	    		    		ugirls = 1;
	    		    	}
					}
	    		    
	    		    cs.setInt("u8", u8);
	    		    cs.setInt("u10", u10);
	    		    cs.setInt("u12", u12);
	    		    cs.setInt("u14", u14);
	    		    cs.setInt("u18", u18);
	    		    cs.setInt("ugirls", ugirls);
	    		    rs = cs.executeQuery();
	    			
	    			
					
	    		    //need to add to the coach roster table for each boys team
	    			for (int i = 0; i < this.selectedteams.size(); i++) {
	    		    	LOGGER.info("updating coach roster record for:" + this.selectedteams.get(i));
						cs = db.prepareCall("CALL scaha.addCoachRoster(?,?,?)");
		    		    cs.setInt("ipersonid", this.selectedcoach);
		    		    cs.setInt("iteamid", Integer.parseInt(this.selectedteams.get(i)));
		    		    cs.setInt("setyear", 2014);
		    		    rs = cs.executeQuery();
					}
	    		    
	    		    //need to add to the coach roster table for each girls team selected
	    			for (int i = 0; i < this.selectedgirlsteams.size(); i++) {
		    		    	LOGGER.info("updating coach roster record for:" + this.selectedgirlsteams.get(i));
						cs = db.prepareCall("CALL scaha.addCoachRoster(?,?,?)");
						cs.setInt("ipersonid", this.selectedcoach);
		    		    cs.setInt("iteamid", Integer.parseInt(this.selectedgirlsteams.get(i)));
		    		    cs.setInt("setyear", 2014);
		    		    rs = cs.executeQuery();
					}
	    			
	    		    
	    		    
					LOGGER.info("Sending email to club registrar, family, and nancy");
					
					
					/*SendMailSSL mail = new SendMailSSL(this);
					LOGGER.info("Finished creating mail object for " + this.getUsername());
					mail.sendMail();
					db.commit();
					return "True";*/
					
					FacesContext context = FacesContext.getCurrentInstance();
		    		try{
						context.getExternalContext().redirect("printablecoachloi.xhtml?coachid=" + selectedcoach);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
    		    } else {
    		    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The provided LOI signature code is invalid."));
    		    }
    		   
			} else {
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN LOI Generation Process" + this.selectedcoach);
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
    }
	
	public String getClubName(){
		
		//first lets get club id for the logged in profile
		Integer clubid = 0;
		String clubname = "";
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

    			
			Vector<Integer> v = new Vector<Integer>();
			v.add(this.getProfid());
			db.getData("CALL scaha.getClubforPerson(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				
				while (rs.next()) {
					clubid = rs.getInt("idclub");
					
					}
				LOGGER.info("We have results for club for a profile");
			}
			
			db.cleanup();
    		
			//now lets retrieve club name
			v = new Vector<Integer>();
			v.add(clubid);
			db.getData("CALL scaha.getClubNamebyId(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				
				while (rs.next()) {
					clubname = rs.getString("clubname");
				}
				LOGGER.info("We have results for club name");
			}
			
			db.cleanup();
			
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
		
		return clubname;
	}
	
	public void CloseLoi(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("addplayerstoteam.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getThisYearBoysTeam(){
		//lets load the team name for the current year
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		String teamname = "";
		try{

    			
			Vector<Integer> v = new Vector<Integer>();
			v.add(selectedcoach);
			db.getData("CALL scaha.getBoyTeamForPlayer(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				
				while (rs.next()) {
					teamname = rs.getString("teamname");
				}
				LOGGER.info("We have results for Team name for a person");
			}
			
			db.cleanup();
    		
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
		
		return teamname;
		
	}
	
	public String getThisYearGirlsTeam(){
		//lets load the team name for the current year
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		String teamname = "";
		try{

    			
			Vector<Integer> v = new Vector<Integer>();
			v.add(selectedcoach);
			db.getData("CALL scaha.getGirlsTeamForPlayer(?)", v);
		    
			if (db.getResultSet() != null){
				//need to add to an array
				rs = db.getResultSet();
				
				while (rs.next()) {
					teamname = rs.getString("teamname");
				}
				LOGGER.info("We have results for Team name for a person");
			}
			
			db.cleanup();
    		
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
		
		return teamname;
	}
}

