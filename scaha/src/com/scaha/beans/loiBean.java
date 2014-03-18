package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.scaha.objects.MailableObject;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class loiBean implements Serializable, MailableObject {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private String selectedteam = null;
	private String selectedgirlsteam = null;
	private Integer selectedplayer = 0;
	private List<Team> teams = null;
	private List<FamilyRow> parents = null;
	private String firstname = null;
	private String lastname = null;
	private String dob = null;
	private String address = null;
	private String state = null;
	private String city = null;
	private String zip = null;
	private String citizenship = null;
	private String gender = null;
	private String lastyearteam = null;
	private String lastyearclub = null;
	private String loicode = null;
	private String playerupcode = null;
	private Integer clubid = null;
	private String origin = null;
	private Boolean displaygirlteam = null;
	private String displaygender = null;
	private String displayselectedteam = null;
	private String displayselectedgirlsteam = null;
	private String currentdate = null;
	private Integer profileid = 0;
	private String to = null;
	private String subject = null;
	private String cc = null;
	private String textbody = null;
	
	public loiBean() {  
        
    	//hard code value until we load session variable
    	clubid = 1;
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
    	this.setProfid(pb.getProfile().ID);
    	
		//will need to load player profile information for displaying loi
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("playerid") != null)
        {
    		selectedplayer = Integer.parseInt(hsr.getParameter("playerid").toString());
        }
    	
    	loadPlayerProfile(selectedplayer);

    	//doing anything else right here
    }  
    
	public String getSubject() {
		// TODO Auto-generated method stub
		return subject;
	}
    
    public void setSubject(String ssubject){
    	subject = ssubject;
    }
    
	public String getTextBody() {
		// TODO Auto-generated method stub
		return textbody;
	}
	
	public void setTextBody(String stextbody){
		textbody = stextbody;
	}
	
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return cc;
	}
	
	public void setPreApprovedCC(String scc){
		cc = scc;
	}
	
	
	
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return to;
	}
    
    public void setToMailAddress(String sto){
    	to = sto;
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
    
    public void setDisplaygirlteam(Boolean scode){
    	displaygirlteam = scode;
    }
    
    public Boolean getDisplaygirlteam(){
    	return displaygirlteam;
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
    
    public Integer getClubid(){
    	return clubid;
    }
    
    public void setClubid(Integer sclub){
    	clubid = sclub;
    }
    
    
    public String getLoicode(){
    	return loicode;
    }
    
    public void setPlayerupcode(String scode){
    	playerupcode = scode;
    }
    
    public String getPlayerupcode(){
    	return playerupcode;
    }
    
    public void setLastyearclub(String sclub){
    	lastyearclub = sclub;
    }
    
    public String getLastyearclub(){
    	return lastyearclub;
    }
    
    public void setLastyearteam(String steam){
    	lastyearteam = steam;
    }
    
    public String getLastyearteam(){
    	return lastyearteam;
    }
    
    public void setDisplaygender(String sgender){
    	displaygender = sgender;
    }
    
    public String getDisplaygender(){
    	return displaygender;
    }
    
    
    public void setGender(String sgender){
    	gender = sgender;
    }
    
    public String getGender(){
    	return gender;
    }
    
    public void setCitizenship(String scitizenship){
    	citizenship = scitizenship;
    }
    
    public String getCitizenship(){
    	return citizenship;
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
    
    public void setDob(String sdob){
    	dob = sdob;
    }
    
    public String getDob(){
    	return dob;
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
    public Integer getSelectedplayer(){
		return selectedplayer;
	}
	
	public void setSelectedplayer(Integer selectedPlayer){
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
	
	public List<FamilyRow> getParents(){
		return parents;
	}
	
	public void setParents(List<FamilyRow> list){
		parents = list;
	}
	
	
	//used to populate loi form with player information
	public void loadPlayerProfile(Integer selectedplayer){
		//first get player detail information then get family members
		Integer personID = 0;
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (!selectedplayer.equals("")) {
    		
    			
    			
    			Vector<Integer> v = new Vector<Integer>();
    			v.add(selectedplayer);
    			db.getData("CALL scaha.getPlayerInfoByPersonId(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					firstname = rs.getString("fname");
    					lastname = rs.getString("lname");
        				dob = rs.getString("dob");
        				address = rs.getString("address");
        				city = rs.getString("city");
        				state = rs.getString("state");
        				zip = rs.getString("zipcode");
        				gender = rs.getString("gender");
        				personID = rs.getInt("idperson");
        				lastyearteam = rs.getString("teamname");
        				lastyearclub = rs.getString("clubname");
        				
        				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        				Date date = new Date();
        				currentdate = dateFormat.format(date);
        				
        				//need to set the boolean used to display the girl team option or not
        				if (gender.equals("F")){
        					displaygirlteam = true;
        				} else {
        					displaygirlteam = false;
        				}
        				
        				//need to set the display value for gender
        				if (gender.equals("F")){
        		    		displaygender = "Female";
        		    	} else {
        		    		displaygender = "Male";
        		    	}
        			}
    				LOGGER.info("We have results for player details by player id");
    			}
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
    	
    	
    	
    	//ok now need to get family members that are parents
    	List<FamilyRow> tempparent = new ArrayList<FamilyRow>();
    	try{

    		if (personID>0) {
    			
    			Vector<Integer> v = new Vector<Integer>();
    			v.add(personID);
    			db.getData("CALL scaha.getParentsByPersonId(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					String pfirstname = rs.getString("fname");
    					String plastname = rs.getString("lname");
    					String pemail = rs.getString("usercode");
    					String pphone = rs.getString("phone");
    					String prelation = rs.getString("reltype");
    					
    					/*String areacode = pphone.substring(0,3);
    					String prefix = pphone.substring(3,6);
    					String suffix = pphone.substring(6,10);
    					
    					pphone = "(" + areacode + ") " + prefix + "-" + suffix;*/
    					
    					FamilyRow row = new FamilyRow();
    					row.setFirstname(pfirstname);
    					row.setLastname(plastname);
    					row.setEmail(pemail);
    					row.setPhone(pphone);
    					row.setRelation(prelation);
    					
    					tempparent.add(row);
    					}
    				LOGGER.info("We have results for parents list by person id");
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
		
    	setParents(tempparent);
   }
	
	public void completeLOI(){

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to check loi code from family first
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.validateMemberNumber(?,?)");
 				cs.setString("memnumber", this.loicode);
 				cs.setInt("personid", this.selectedplayer);
    		    
    		    rs = cs.executeQuery();
    			
    		    Integer resultcount = 0;
    		    if (rs != null){
    				
    				while (rs.next()) {
    					resultcount = rs.getInt("idmember");
    				}
    				LOGGER.info("We have player up code validation results for player details by player id");
    			}
    			db.cleanup();
 				
    		    //need to verify player up code if user provided it.
    			if (this.playerupcode!=null && this.playerupcode!=""){
    				//Need to check player up code from family next
	 				LOGGER.info("verify family code provided for player up");
	 				cs = db.prepareCall("CALL scaha.validateMemberNumber(?,?)");
	 				cs.setString("memnumber", this.loicode);
	 				cs.setInt("personid", this.selectedplayer);
	    		    rs = cs.executeQuery();
	    			resultcount = 0;
	    		    
	    		    if (rs != null){
	    				
	    				while (rs.next()) {
	    					resultcount = rs.getInt("idmember");
	    				}
	    				LOGGER.info("We have code validation results for player details by player id");
	    			}
	    			db.cleanup();
	    			
	    			if (resultcount.equals(0)){
	    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The provided Player Up signature code is invalid."));
	    			}
    			}
	    		
    			
    			//need to verify if player is playing up and if player up code is needed if not provided
    			if (this.playerupcode==null || this.playerupcode==""){
    				//Need to check player up code from family next
	 				LOGGER.info("verify if user needs to enter player up code");
	 				cs = db.prepareCall("CALL scaha.IsPlayerUpNeeded(?,?)");
	 				String year = this.dob.substring(0,4);
	 				cs.setInt("birthyear",Integer.parseInt(year));
	 				cs.setInt("selectedteam", Integer.parseInt(this.selectedteam));
	    		    rs = cs.executeQuery();
	    			resultcount = 0;
	    		    
	    		    if (rs != null){
	    				
	    				while (rs.next()) {
	    					resultcount = rs.getInt("divisioncount");
	    				}
	    				LOGGER.info("We have validation whether player needs player up code or not");
	    			}
	    			db.cleanup();
	    			
	    			if (resultcount.equals(0)){
	    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The Player Up Code is required for this player for the division selected."));
	    			}
    			}
    			
    		    if (resultcount > 0){
    		    	
	    		    //if good save info to person table, then add record to roster then email
	 				LOGGER.info("updating person record");
	 				cs = db.prepareCall("CALL scaha.updatePersonInfoAddress(?,?,?,?,?)");
	    		    cs.setInt("ipersonid", this.selectedplayer);
	    		    cs.setString("iaddress", this.address);
	    		    cs.setString("icity", this.city);
	    		    cs.setString("istate", this.state);
	    		    cs.setString("izipcode", this.zip);
	    			rs = cs.executeQuery();
	    			
					LOGGER.info("updating roster record");
					cs = db.prepareCall("CALL scaha.addRoster(?,?)");
	    		    cs.setInt("ipersonid", this.selectedplayer);
	    		    
	    		    
	    		    if ((selectedgirlsteam!=null) && (!selectedgirlsteam.equals(""))){
	    		    	cs.setInt("iteamid", Integer.parseInt(this.selectedgirlsteam));
	    		    }else{
	    		    	cs.setInt("iteamid", Integer.parseInt(this.selectedteam));
	    		    }
	    		    rs = cs.executeQuery();
					
	    		    //need to get team name for the newly selected team
	    			if (this.selectedteam!=null && !this.selectedteam.equals("")){
		    			Vector<Integer> v = new Vector<Integer>();
		    			v.add(Integer.parseInt(this.selectedteam));
		    			db.getData("CALL scaha.getTeamByTeamId(?)", v);
		    			
		    			if (db.getResultSet() != null){
		    				//need to add to an array
		    				rs = db.getResultSet();
		    			
			    			if (rs != null){
			    				
			    				while (rs.next()) {
			    					displayselectedteam = rs.getString("teamname");
			    				}
			    				LOGGER.info("We have loaded the team name for printable loi");
			    			}
			    			db.cleanup();
		    			}
	    			}
	    			
	    			//need to get team name for the newly selected team
	    			if (this.selectedgirlsteam!=null && !this.selectedgirlsteam.equals("")){
		    			Vector<Integer> v = new Vector<Integer>();
		    			v.add(Integer.parseInt(this.selectedgirlsteam));
		    			db.getData("CALL scaha.getTeamByTeamId(?)", v);
		    			
		    			if (db.getResultSet() != null){
		    				//need to add to an array
		    				rs = db.getResultSet();
		    			
			    			if (rs != null){
			    				
			    				while (rs.next()) {
			    					displayselectedgirlsteam = rs.getString("teamname");
			    				}
			    				LOGGER.info("We have loaded the girls team name for printable loi");
			    			}
			    			db.cleanup();
		    			}
	    			}
	    		    
	    		    
	    			LOGGER.info("Sending email to club registrar, family, and scaha registrar");
	    			cs = db.prepareCall("CALL scaha.getClubRegistrarEmail(?)");
	    		    cs.setInt("iclubid", this.clubid);
	    		    rs = cs.executeQuery();
	    		    if (rs != null){
	    				while (rs.next()) {
	    					to = rs.getString("usercode");
	    				}
	    			}
					
	    		    
	    			//need to check if scaha registrar has received an email today
	    		    //set value to 0 and then check if the current date is less than 8/1/2014 if it is then we want
	    		    //to check if an email has been sent today, if = or > than 8/1/2014 then always send email to scaha
	    		    //registrar
	    		    Integer emailsenttoday = 0;
	    		    Date curdate = new Date();
	    		    Calendar cal = Calendar.getInstance();
	    		    cal.set(2014, Calendar.AUGUST, 1); //Year, month and day of month
	    		    Date targetdate = cal.getTime();
	    		    if (curdate.compareTo(targetdate)<0){
	    		        cs = db.prepareCall("CALL scaha.HasReceivedEmail()");
		    		    rs = cs.executeQuery();
		    		    if (rs != null){
		    				while (rs.next()) {
		    					emailsenttoday = rs.getInt("emailcount");
		    				}
		    			}
	    		    }
	    		    
	    		    if (emailsenttoday.equals(0)){
		    		    cs = db.prepareCall("CALL scaha.getSCAHARegistrarEmail()");
		    		    rs = cs.executeQuery();
		    		    if (rs != null){
		    				while (rs.next()) {
		    					to = to + ',' + rs.getString("usercode");
		    				}
		    			}
		    		    
		    		    cs = db.prepareCall("CALL scaha.setSCAHARegistrarEmail()");
		    		    rs = cs.executeQuery();
		    		    db.commit();
	    		    }
	    		    
	    		    cs = db.prepareCall("CALL scaha.getFamilyEmail(?)");
	    		    cs.setInt("iplayerid", this.selectedplayer);
	    		    rs = cs.executeQuery();
	    		    if (rs != null){
	    				while (rs.next()) {
	    					to = to + ',' + rs.getString("usercode");
	    				}
	    			}
					
	    		    
	    		    this.setToMailAddress(to);
	    		    this.setTextBody("Player " + this.firstname + " " + this.lastname + " signed an loi for club " + this.getClubName());
	    		    this.setSubject(this.firstname + " " + this.lastname + " LOI with " + this.getClubName());
	    		    
					/*SendMailSSL mail = new SendMailSSL(this);
					LOGGER.info("Finished creating mail object for " + this.getUsername());
					mail.sendMail();
					db.commit();
					return "True";*/
					
					FacesContext context = FacesContext.getCurrentInstance();
		    		origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
					try{
						context.getExternalContext().redirect("printableloi.xhtml?playerid=" + selectedplayer);
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
			LOGGER.info("ERROR IN LOI Generation Process" + this.selectedplayer);
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
		origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
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
			v.add(selectedplayer);
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
			v.add(selectedplayer);
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

