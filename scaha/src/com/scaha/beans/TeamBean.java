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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import org.primefaces.event.RowEditEvent;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Division;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.MailableObject;
import com.scaha.objects.ScahaTeam;
import com.scaha.objects.SkillLevel;
import com.scaha.objects.TeamList;

//import com.gbli.common.SendMailSSL;

@ManagedBean
@SessionScoped
public class TeamBean implements Serializable, MailableObject {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;

    private List<Division> divisions= null;
    private String[] sdivisions = null;
    private List<SkillLevel> skilllevels= null;
    private String[] sskilllevels = null;
	private String selectedskilllevel = null;
	private String selecteddivision = null;
	private String teamname = null;
	private Integer idclub = null;
	private ScahaTeam selectedteam = null;
	private String teamstabletitle = null;
	private Integer profileid = 0;
	private Boolean ishighschool = null;
	private String TargetTeamID = null;
	private TeamList MyTeamList = null;
	
	@PostConstruct
    public void init() {
        idclub = 1;  
    	this.setProfid(pb.getProfile().ID);
        getClubID();
        isClubHighSchool();
        populateTableTitle(idclub);
        getTeamsForClub(idclub); 
        getListofDivisions();
        this.getListofSkillLevels();
	}
	
    public TeamBean() {  
        
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
    
    public ScahaTeam getSelectedteam(){
		return selectedteam;
	}
	
	public void setSelectedteam(ScahaTeam selectedTeam){
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
	
	public String[] getSListOfDivisions() {
		return this.divisions.toArray(new String[this.divisions.size()]);
	}
	
	public List<Division> getListofDivisions(){
		List<Division> templist = new ArrayList<Division>();
		List<String> sdivs = new ArrayList<String>();
		
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
    			Division div = new Division();
    			div.setDivisionname(divisionname);
    			div.setIddivision(iddivision);
    			templist.add(div);
    			sdivs.add(divisionname);
			}
   			rs.close();
   			cs.close();
    	} catch (SQLException e) {
    		LOGGER.info("ERROR IN loading teams");
    		e.printStackTrace();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setDivisions(templist);
    	this.setSdivisions(sdivs.toArray(new String[sdivs.size()]));
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
		List<String> sdivs = new ArrayList<String>();
	
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
    				sdivs.add(levelsname);
    				templist.add(level);
				}
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
    	this.setSskilllevels(sdivs.toArray(new String[sdivs.size()]));
		return getSkilllevels();
	}
	
	public List<SkillLevel> getSkilllevels(){
		return skilllevels;
	}
	
	public void setSkilllevels(List<SkillLevel> list){
		skilllevels = list;
	}
	    
    //retrieves list of existing teams for the club
    public void getTeamsForClub(Integer idclub){
    	
		GeneralSeason scahags = scaha.getScahaSeasonList().getCurrentSeason();
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
  			
		try {
			setMyTeamList(TeamList.NewTeamListFactory(pb.getProfile(), db, scaha.getScahaClubList().getRowData(idclub+"") , scahags, true, false));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();

    }


    /**
     * This simply saves a new team to the system.
     * 
     */
    public void saveTeam(){
   
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
		
		FacesMessage msg = new FacesMessage("Team Added", this.teamname);  
		FacesContext.getCurrentInstance().addMessage(null, msg); 
		
    	getTeamsForClub(idclub);
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
   				this.setTeamstabletitle(sclubname + " Teams for the " + scaha.getScahaSeasonList().getCurrentSeason());
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
		
		String strSkillLevel = "UNK";
		String strDivision = "UNK";
		Club cl = scaha.getScahaClubList().getRowData(this.idclub+"");
		
		for (SkillLevel sk : this.skilllevels) {
			if (sk.getIdskilllevel().toString().equals(this.selectedskilllevel)) {
				strSkillLevel = sk.getSkilllevelname();
				break;
			}
		}
		for (Division d : this.divisions) {
			if (d.getIddivision().toString().equals(this.selecteddivision)) {
				strDivision = d.getDivisionname();
				break;
			}
		}
		return "Team " + this.teamname + " (" + strDivision + " " +  strSkillLevel + ") was Added to club " + cl;
	}
	@Override
	public String getTextBody() {
		String strSkillLevel = "UNK";
		String strDivision = "UNK";
		Club cl = scaha.getScahaClubList().getRowData(this.idclub+"");
		
		for (SkillLevel sk : this.skilllevels) {
			if (sk.getIdskilllevel().toString().equals(this.selectedskilllevel)) {
				strSkillLevel = sk.getSkilllevelname();
				break;
			}
		}
		for (Division d : this.divisions) {
			if (d.getIddivision().toString().equals(this.selecteddivision)) {
				strDivision = d.getDivisionname();
				break;
			}
		}
		return "A team was added to the club: " + cl + ".  <br/>"  + "The team name is " + this.teamname + "(" + strDivision + " " +  strSkillLevel + ")<p/>Thanks,<p/>The iScaha Web Site";
	}

	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "online@iscaha.com";
	}
	
	@Override
	public String getToMailAddress() {
		//
		// Tmp hardcoded.. we simply need to pull the e-mail for the Scaha Registrar
		//
		return "dux8fan@aol.com";
	
	}

	@Override
	public InternetAddress[] getToMailIAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InternetAddress[] getPreApprovedICC() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the targetTeamID
	 */
	public String getTargetTeamID() {
		return TargetTeamID;
	}

	/**
	 * @param targetTeamID the targetTeamID to set
	 */
	public void setTargetTeamID(String targetTeamID) {
		TargetTeamID = targetTeamID;
	}
	
	/**
	 * @return the sdivisions
	 */
	public String[] getSdivisions() {
		return sdivisions;
	}

	/**
	 * @param sdivisions the sdivisions to set
	 */
	public void setSdivisions(String[] sdivisions) {
		this.sdivisions = sdivisions;
	}
	
	/**
	 * @return the myTeamList
	 */
	public TeamList getMyTeamList() {
		return MyTeamList;
	}

	/**
	 * @param myTeamList the myTeamList to set
	 */
	public void setMyTeamList(TeamList myTeamList) {
		MyTeamList = myTeamList;
	}
	
	public void onEdit(RowEditEvent event) { 
		FacesMessage msg = new FacesMessage("Item Edited",((ScahaTeam) event.getObject()).toString()); 
		FacesContext.getCurrentInstance().addMessage(null, msg); 
		
		//
		// lets assemble the object .. we can only change three things right now..
		// 1) Team Name
		//
		// These changs below can only happen when there are NO Players assigned to the team
		
		// 2) Team Division
		// 3) Team Skill Level
		
		ScahaTeam myteam = (ScahaTeam) event.getObject();
		LOGGER.info("HERE IS MY TEAM:" + myteam + ":retire:" + myteam.isRetire() + myteam.getSskilllevel() + ":" + myteam.getSdivision());
		for (ScahaTeam t : MyTeamList) {
			if (t.ID  ==  myteam.ID) {
				LOGGER.info("HERE IS MY TEAM:" + t);
				t.setTeamname(myteam.getTeamname());
				
				//
				// Lets assume that we have no players yet.. will need a check from the DB here..
				//
				for (SkillLevel sk : this.skilllevels) {
					if (sk.getSkilllevelname().equals(myteam.getSskilllevel())) {
						t.setTeamskilllevel(sk);
						break;
					}
				}
				
				for (Division d : this.divisions) {
					if (d.getDivisionname().equals(myteam.getSdivision())) {
						t.setTeamdivision(d);
						break;
					}
				}

				ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		    	try {
					t.update(db);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	db.free();
				break;
			}
			

		}

		//
		// refresh the list..
		//

		LOGGER.info("HERE IS MY TEAM:" + myteam);
		
		
	} 
	       
	public void onCancel(RowEditEvent event) { 
		FacesMessage msg = new FacesMessage("Team Edit Has Been Cancelled", ((ScahaTeam) event.getObject()).toString());  
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	/**
	 * This will delete a team as long as it has no roster info on it and it is marked as perminent.
	 * 
	 */
    public void deleteTeam() {  
		

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");

		try {
			if (!db.isTeamEmpty(selectedteam.ID)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
			            new FacesMessage(FacesMessage.SEVERITY_ERROR,
			            "THIS TEAM CANNOT BE REMOVED",
			            "There currently exists players and or staff assigned to this team.. you cannot remove this team from your club."));
				db.free();
				return;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (ScahaTeam t : MyTeamList) {
			if (t.ID  ==  this.selectedteam.ID) {
				LOGGER.info("HERE IS MY Deleted TEAM:" + t);
				t.setRetire(true);
		    	try {
					t.update(db);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
    	db.free();
		@SuppressWarnings("unchecked")
		List<ScahaTeam> ml = (List<ScahaTeam>) MyTeamList.getWrappedData();
		ml.remove(this.selectedteam);
		FacesMessage msg = new FacesMessage("Team Deleted", selectedteam.toString());  
		FacesContext.getCurrentInstance().addMessage(null, msg); 
		
    } 

	/**
	 * @return the sskilllevels
	 */
	public String[] getSskilllevels() {
		return sskilllevels;
	}

	/**
	 * @param sskilllevels the sskilllevels to set
	 */
	public void setSskilllevels(String[] sskilllevels) {
		this.sskilllevels = sskilllevels;
	}  

}

