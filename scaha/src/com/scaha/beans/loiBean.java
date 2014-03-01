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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyRow;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class loiBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private ResultSet rs = null;
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
    
    public loiBean() {  
        //will need to load player profile information for displaying loi
    	//hard code value until we load session variable
    	clubid = 1;
    	
    	
    	HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("playerid") != null)
        {
    		selectedplayer = Integer.parseInt(hsr.getParameter("playerid").toString());
        }
    	
    	loadPlayerProfile(selectedplayer);

    	//doing anything else right here
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
    					
    					String areacode = pphone.substring(0,3);
    					String prefix = pphone.substring(3,6);
    					String suffix = pphone.substring(6,10);
    					
    					pphone = "(" + areacode + ") " + prefix + "-" + suffix;
    					
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
 				CallableStatement cs = db.prepareCall("CALL scaha.validateCode(?,?)");
    		    cs.setInt("ipersonid", this.selectedplayer);
    		    cs.setString("iloicode", this.loicode);
    		    rs = cs.executeQuery();
    			
    		    Integer resultcount = 0;
    		    if (rs != null){
    				
    				while (rs.next()) {
    					resultcount = rs.getInt("codecount");
    				}
    				LOGGER.info("We have player up code validation results for player details by player id");
    			}
    			db.cleanup();
 				
    		    
    			if (this.playerupcode!=null && this.playerupcode!=""){
    				//Need to check player up code from family next
	 				LOGGER.info("verify family code provided for player up");
	 				cs = db.prepareCall("CALL scaha.validateCode(?,?)");
	    		    cs.setInt("ipersonid", this.selectedplayer);
	    		    cs.setString("iloicode", this.playerupcode);
	    		    rs = cs.executeQuery();
	    			resultcount = 0;
	    		    
	    		    if (rs != null){
	    				
	    				while (rs.next()) {
	    					resultcount = rs.getInt("codecount");
	    				}
	    				LOGGER.info("We have code validation results for player details by player id");
	    			}
	    			db.cleanup();
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
	    		    
	    		    
					LOGGER.info("Sending email to club registrar, family, and nancy");
					
					
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
    		    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "Provided signature code for LOI is invalid."));
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
	
	
}

