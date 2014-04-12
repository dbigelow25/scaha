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

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.common.SendMailSSL;
import com.gbli.common.Utils;
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
	private static String mail_reg_body = Utils.getMailTemplateFromFile("/mail/loireceipt.html");
	private static String girlsmail_reg_body = Utils.getMailTemplateFromFile("/mail/girlsloireceipt.html");
	private static String playerupmail_reg_body = Utils.getMailTemplateFromFile("/mail/playeruploireceipt.html");
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
	private Integer parentid = 0;
	private Boolean bplayerup = null;
	private Boolean displayplayerup = null;
	
	@PostConstruct
    public void init() {
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
    	setDisplayplayerup(false);
    }
	
	
	public loiBean() {  
        
    }  
    
	public Boolean getDisplayplayerup(){
		return displayplayerup;
	}
	
	public void setDisplayplayerup(Boolean bplay){
		displayplayerup=bplay;
	}

	
	public Boolean getBplayerup(){
		return bplayerup;
	}
	
	public void setBplayerup(Boolean bplay){
		bplayerup=bplay;
	}
	
	public Integer getParentid(){
		return parentid;
	}
	
	public void setParentid(Integer id){
		parentid = id;
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
		List<String> myTokens = new ArrayList<String>();
		myTokens.add("LOIDATE:" + this.currentdate);
		myTokens.add("FIRSTNAME:" + this.firstname);
		myTokens.add("LASTNAME:" + this.lastname);
		myTokens.add("CLUBNAME:" + this.getClubName());
		myTokens.add("SELECTEDBOYSTEAM:" + this.displayselectedteam + " ");
		if (this.displayselectedgirlsteam==null){
			myTokens.add("SELECTEDGRLSTEAM: ");
		}else {
			myTokens.add("SELECTEDGRLSTEAM:" + this.displayselectedgirlsteam + " ");
		}
		
		myTokens.add("DOB:" + this.dob);
		myTokens.add("CITIZENSHIP:" + this.citizenship);
		myTokens.add("GENDER:" + this.displaygender);
		myTokens.add("ADDRESS:" + this.address);
		myTokens.add("CITY:" + this.city);
		myTokens.add("STATE:" + this.state);
		myTokens.add("ZIP:" + this.zip);
		if (this.lastyearclub==null){
			myTokens.add("LASTYEARCLUB:  ");
		}else {
			myTokens.add("LASTYEARCLUB:" + this.lastyearclub);
		}
		if (this.lastyearteam==null){
			myTokens.add("LASTYEARTEAM:  ");
		}else {
			myTokens.add("LASTYEARTEAM:" + this.lastyearteam);
		}
		
		if (this.displayselectedgirlsteam==null){
			if (bplayerup){
				return Utils.mergeTokens(loiBean.playerupmail_reg_body,myTokens);
			} else {
				return Utils.mergeTokens(loiBean.mail_reg_body,myTokens);
			}
		} else {
			return Utils.mergeTokens(loiBean.girlsmail_reg_body,myTokens);
		}
		
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
        				parentid = rs.getInt("parentid");
        				citizenship = rs.getString("citizenship");
        				
        				if (citizenship.equals("CAN")){
        					citizenship="Canada";
        				} else if (citizenship.equals("OTH")){
        					citizenship="Other";
        				} 
        				
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
    				if (resultcount.equals(0)){
    					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The provided LOI signature code is invalid."));
    				}
    			}
    			db.cleanup();
 				
    		    
    			bplayerup = false;
    			Integer plupresultcount = 0;
    			
    			//need to verify if player is playing up and if player up code is needed if not provided
    			if (this.playerupcode==null || this.playerupcode==""){
    				//Need to check player up code from family next
	 				LOGGER.info("verify if user needs to enter player up code");
	 				cs = db.prepareCall("CALL scaha.IsPlayerUpNeeded(?,?)");
	 				String year = this.dob.substring(0,4);
	 				cs.setInt("birthyear",Integer.parseInt(year));
	 				cs.setInt("selectedteam", Integer.parseInt(this.selectedteam));
	    		    rs = cs.executeQuery();
	    			
	    		    
	    		    if (rs != null){
	    				
	    				while (rs.next()) {
	    					plupresultcount = rs.getInt("divisioncount");
	    				}
	    				LOGGER.info("We have validation whether player needs player up code or not");
	    			}
	    			db.cleanup();
	    			
	    			if (plupresultcount.equals(0) && this.selectedgirlsteam==null){
	    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The Player Up Code is required for this player for the division selected."));
	    				bplayerup=true;
	    			} else {
	    				if (!(this.selectedgirlsteam==null)){
	    					resultcount=1;
	    				}
	    			}
	    			
    			}
    			
    			//need to verify player up code if user provided it.
    			if (plupresultcount.equals(0) && this.selectedgirlsteam==null){
    				//Need to check player up code from family next
	 				LOGGER.info("verify family code provided for player up");
	 				cs = db.prepareCall("CALL scaha.validateMemberNumber(?,?)");
	 				cs.setString("memnumber", this.playerupcode);
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
	    			
	    			if (resultcount.equals(0) && this.selectedgirlsteam==null){
	    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The provided Player Up signature code is invalid."));
	    			} else {
	    				bplayerup=true;
	    				
	    			}
    			}
	    		
    			
    			
    			
    			//lets make sure a team was selected
    			if (this.selectedteam.equals("0") && (this.selectedgirlsteam==null || this.selectedgirlsteam.equals("0"))){
    				resultcount=0;
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "A team must be selected for the player"));
    			}
    			
    			
    		    if (resultcount > 0){
    		    	
	    		    //if good save info to person table, then add record to roster then email
	 				LOGGER.info("updating person record");
	 				cs = db.prepareCall("CALL scaha.updatePersonInfoAddress(?,?,?,?,?)");
	    		    cs.setInt("ipersonid", this.parentid);
	    		    cs.setString("iaddress", this.address);
	    		    cs.setString("icity", this.city);
	    		    cs.setString("istate", this.state);
	    		    cs.setString("izipcode", this.zip);
	    			rs = cs.executeQuery();
	    			
					LOGGER.info("updating roster record");
					cs = db.prepareCall("CALL scaha.addRoster(?,?,?)");
	    		    cs.setInt("ipersonid", this.selectedplayer);
	    		    
	    		    if (bplayerup){
	    		    	cs.setInt("playerup",1);
	    		    } else {
	    		    	cs.setInt("playerup",0);
	    		    }
	    		    
	    		    
	    		    
	    		    
	    		    if ((!selectedgirlsteam.equals("0")) && (selectedgirlsteam!=null) && (!selectedgirlsteam.equals(""))){
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
					
	    		    
	    		    //hard my email address for testing purposes
	    		    to = "lahockeyfan2@yahoo.com";
	    		    this.setToMailAddress(to);
	    		    this.setPreApprovedCC("");
	    		    this.setSubject(this.firstname + " " + this.lastname + " LOI with " + this.getClubName());
	    		    
					SendMailSSL mail = new SendMailSSL(this);
					LOGGER.info("Finished creating mail object for " + this.firstname + " " + this.lastname + " LOI with " + this.getClubName());
					mail.sendMail();
					
					db.commit();
					rs.close();
					db.cleanup();
					//return "True";
					
					FacesContext context = FacesContext.getCurrentInstance();
		    		origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
					try{
						context.getExternalContext().redirect("printableloi.xhtml?playerid=" + selectedplayer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
    		    } else {
    		    	
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
					this.clubid = rs.getInt("idclub");
					
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
		Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{draftplayersBean}", Object.class );

		DraftPlayersBean dpb = (DraftPlayersBean) expression.getValue( context.getELContext());
    	dpb.setSearchcriteria("");
		dpb.playerSearch();

		
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
					Integer playerup = rs.getInt("isplayerup");
					if (playerup.equals(0)){
						this.setDisplayplayerup(false);
					}else {
						this.setDisplayplayerup(true);
					}
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
	
	public void checkDOB(String sourceteam){

		//perform logic to check if team selected is for player up
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{
			
			//first lets check if the team selected is too young for the players dob
			Integer resultcount = 0;
			Integer ageoldercount = 0;
			Integer pwtobtmcount = 0;
			if (sourceteam.equals("M")){
				CallableStatement cs = db.prepareCall("CALL scaha.IsPlayerOlderThanLevel(?,?)");
				String year = this.dob.substring(0,4);
				cs.setInt("birthyear",Integer.parseInt(year));
				cs.setInt("selectedteam", Integer.parseInt(this.selectedteam));
				
			    rs = cs.executeQuery();
				
			    
			    if (rs != null){
					
					while (rs.next()) {
						ageoldercount = rs.getInt("isolder");
					}
					LOGGER.info("We have validation whether player needs player up code or not");
				}
				db.cleanup();
				
				if (ageoldercount.equals(1)){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "The player is too old for the team selected"));
				}
				
				
				//need to check if player up code is needed
				LOGGER.info("verify if user needs to enter player up code");
				cs = db.prepareCall("CALL scaha.IsPlayerUpNeeded(?,?)");
				cs.setInt("birthyear",Integer.parseInt(year));
				if (sourceteam.equals("M")){
					cs.setInt("selectedteam", Integer.parseInt(this.selectedteam));
				}else {
					cs.setInt("selectedteam", Integer.parseInt(this.selectedgirlsteam));
				}
				
			    rs = cs.executeQuery();
				
			    
			    if (rs != null){
					
					while (rs.next()) {
						resultcount = rs.getInt("divisioncount");
					}
					LOGGER.info("We have validation whether player needs player up code or not");
				}
			    
			    //need to check if player is peewee trying to play up in bantam b
			    LOGGER.info("verify if user is player up for pw to bantam");
				cs = db.prepareCall("CALL scaha.IsPlayerUPPeeweeToBantam(?,?)");
				cs.setInt("birthyear",Integer.parseInt(year));
				if (sourceteam.equals("M")){
					cs.setInt("selectedteam", Integer.parseInt(this.selectedteam));
				}else {
					cs.setInt("selectedteam", Integer.parseInt(this.selectedgirlsteam));
				}
				
			    rs = cs.executeQuery();
				
			    if (rs != null){
					
					while (rs.next()) {
						pwtobtmcount = rs.getInt("divisioncount");
					}
					LOGGER.info("We have validation whether player needs player up code or not");
				}
				db.cleanup();
			    
			} else {
				resultcount = 1;
			}
			
			
			if (resultcount.equals(0) && ageoldercount.equals(0) && pwtobtmcount.equals(0)){
				this.setDisplayplayerup(true);
			} else {
				this.setDisplayplayerup(false);
				if (pwtobtmcount.equals(1)){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", "Player up from peewee to bantams is not allowed."));
					this.selectedteam=null;
				}
			}
		
			if (sourceteam.equals("M")){
				this.selectedgirlsteam=null;
			} else {
				this.selectedteam=null;
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
		
		
	
}

