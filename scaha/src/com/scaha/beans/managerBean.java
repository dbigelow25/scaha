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
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import com.gbli.common.SendMailSSL;
import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.ExhibitionGame;
import com.scaha.objects.ExhibitionGameDataModel;
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
public class managerBean implements Serializable, MailableObject {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;

	transient private ResultSet rs = null;
	//lists for generated datamodels
	private List<RosterEdit> players = null;
	private List<RosterEdit> coaches = null;
	private List<TempGame> games = null;
	private List<TournamentGame> tournamentgames = null;
	private List<Tournament> tournaments = null;
	private List<ExhibitionGame> exhibitiongames = null;
    
	//bean level properties used by multiple methods
	private Integer teamid = null;
	private String teamname = null;
	private Integer idclub = null;
	private Integer profileid = 0;
	private Boolean ishighschool = null;
	
	//datamodels for all of the lists on the page
	private TempGameDataModel TempGameDataModel = null;
    private TournamentDataModel TournamentDataModel = null;
    private TournamentGameDataModel TournamentGameDataModel = null;
    private ExhibitionGameDataModel ExhibitionGameDataModel = null;
    private RosterEditDataModel RosterEditDataModel = null;
    private RosterEditDataModel RosterCoachDataModel = null;
    
    //properties for storing the selected row of each of the datatables
    private RosterEdit selectedplayer = null;
    private TempGame selectedgame = null;
	private TournamentGame selectedtournamentgame = null;
	private ExhibitionGame selectedexhibitiongame = null;
	private Tournament selectedtournament = null;
	private String selectedtournamentforgame = null;
	
	//properties for adding tournaments
	private String tournamentname;
	private String startdate;
	private String enddate;
	private String contact;
	private String phone;
	private String sanction;
	private String location;
	private String website;
	private String levelplayed;
	
	//properties for adding tournament/exhibition games
	private String gamedate=null;
	private String gametime=null;
	private String opponent=null;
	private String tourneygamelocation=null;
	private String exhibitiongamelocation=null;
	
	//properties for emailing to managers, scaha statistician
	private String to = null;
	private String subject = null;
	private String cc = null;
	private static String mail_tournament_body = Utils.getMailTemplateFromFile("/mail/tournament.html");
	private static String mail_tournamentgame_body = Utils.getMailTemplateFromFile("/mail/tournamentgame.html");
	private static String mail_exhibitiongame_body = Utils.getMailTemplateFromFile("/mail/exhibitiongame.html");
	private String todaysdate = null;
	private Boolean addingtournament = null;
	private Boolean addingtournamentgame = null;
	private Boolean addingexhibitiongame = null;
	
		
	
	@PostConstruct
    public void init() {
		games = new ArrayList<TempGame>();  
        TempGameDataModel = new TempGameDataModel(games);
        
        idclub = 1;  
        
        this.setProfid(pb.getProfile().ID);
        
        getClubID();
        isClubHighSchool();
    	setTodaysDate();
    	setAddingflags();
        //teamid = 131;
        this.setTeamid(pb.getProfile().getManagerteamid());
        
        
        //Load team roster
        getRoster();
        
        //Load SCAHA Games
        loadScahaGames();
        
        //Load Tournament and Games
        getTournament();
        getTournamentGames();
        
        //Load Exhibition Games
        getExhibitionGames();
        
        //now lets highlight the areas needing action.
        setAlerts();
	}
	
    public managerBean() {  
        
    }  
    
    public Boolean getAddingtournament(){
    	return addingtournament;
    }
    
    public void setAddingtournament(Boolean in){
    	addingtournament=in;
    }
    
    public Boolean getAddingtournamentgame(){
    	return addingtournamentgame;
    }
    
    public void setAddingtournamentgame(Boolean in){
    	addingtournamentgame=in;
    }
    
    public Boolean getAddingexhibitiongame(){
    	return addingexhibitiongame;
    }
    
    public void setAddingexhibitiongame(Boolean in){
    	addingexhibitiongame=in;
    }
    
    
    public String getTodaysdate(){
    	return todaysdate;
    }
    
    public void setTodaysdate(String tdate){
    	todaysdate=tdate;
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
		String result = "";
		if (this.addingtournament){
			myTokens.add("REQUESTINGTEAM:" + this.teamname);
			myTokens.add("REQUESTDATE:" + this.todaysdate);
			myTokens.add("TOURNAMENTNAME:" + this.tournamentname);
			myTokens.add("LEVELPLAYED:" + this.levelplayed);
			myTokens.add("STARTDATE:" + this.startdate);
			myTokens.add("ENDDATE:" + this.enddate);
			myTokens.add("CONTACT:" + this.contact);
			myTokens.add("PHONE:" + this.phone);
			myTokens.add("SANCTION:" + this.sanction);
			myTokens.add("LOCATION:" + this.location);
			myTokens.add("WEBSITE:" + this.website);
			myTokens.add("STATUS:" + "Pending");
			
			result = Utils.mergeTokens(managerBean.mail_tournament_body,myTokens);
		}
		
		if (this.addingtournamentgame){
			myTokens.add("REQUESTINGTEAM:" + this.teamname);
			myTokens.add("REQUESTDATE:" + this.todaysdate);
			myTokens.add("TOURNAMENTNAME:" + this.tournamentname);
			myTokens.add("GAMEDATE:" + this.gamedate);
			myTokens.add("GAMETIME:" + this.gametime);
			myTokens.add("OPPONENT:" + this.opponent);
			myTokens.add("LOCATION:" + this.tourneygamelocation);
			myTokens.add("STATUS:" + "Pending");
			
			result = Utils.mergeTokens(managerBean.mail_tournamentgame_body,myTokens);
		}
		
		if (this.addingexhibitiongame){
			myTokens.add("REQUESTINGTEAM:" + this.teamname);
			myTokens.add("REQUESTDATE:" + this.todaysdate);
			myTokens.add("GAMEDATE:" + this.gamedate);
			myTokens.add("GAMETIME:" + this.gametime);
			myTokens.add("OPPONENT:" + this.opponent);
			myTokens.add("LOCATION:" + this.exhibitiongamelocation);
			myTokens.add("STATUS:" + "Pending");
			
			result = Utils.mergeTokens(managerBean.mail_exhibitiongame_body,myTokens);
		}
		
		return result;
		
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
    
    public String getGamedate(){
    	return gamedate;
    }
    
    public void setGamedate(String gdate){
    	gamedate=gdate;
    }
    
    public String getGametime(){
    	return gametime;
    }
    
    public void setGametime(String gdate){
    	gametime=gdate;
    }
    
    public String getOpponent(){
    	return opponent;
    }
    
    public void setOpponent(String gdate){
    	opponent=gdate;
    }
    
    
    public String getLevelplayed(){
    	return levelplayed;
    }
    
    public void setLevelplayed(String name){
    	levelplayed=name;
    }
    
    public String getWebsite(){
    	return website;
    }
    
    public void setWebsite(String name){
    	website=name;
    }
    
    
    public String getLocation(){
    	return this.location;
    }
    
    public void setLocation(String name){
    	this.location=name;
    }
    
    public String getTourneygamelocation(){
    	return this.tourneygamelocation;
    }
    
    public void setTourneygamelocation(String name){
    	this.tourneygamelocation=name;
    }
    
    public String getExhibitiongamelocation(){
    	return this.exhibitiongamelocation;
    }
    
    public void setExhibitiongamelocation(String name){
    	this.exhibitiongamelocation=name;
    }
    
    public String getSanction(){
    	return sanction;
    }
    
    public void setSanction(String name){
    	sanction=name;
    }
    
    
    public String getPhone(){
    	return phone;
    }
    
    public void setPhone(String name){
    	phone=name;
    }
    
    
    public String getContact(){
    	return contact;
    }
    
    public void setContact(String name){
    	contact=name;
    }
    
    
    public String getEnddate(){
    	return enddate;
    }
    
    public void setEnddate(String name){
    	enddate=name;
    }
    
    
    public String getStartdate(){
    	return startdate;
    }
    
    public void setStartdate(String name){
    	startdate=name;
    }
    
    
    public String getTournamentname(){
    	return tournamentname;
    }
    
    public void setTournamentname(String name){
    	tournamentname=name;
    }
    
    public RosterEdit getSelectedplayer(){
    	return selectedplayer;
    }
    
    public void setSelectedplayer(RosterEdit name){
    	selectedplayer=name;
    }
    
    
    public String getTeamname(){
    	return teamname;
    }
    
    public void setTeamname(String name){
    	teamname=name;
    }
    
    
    public Integer getTeamid(){
    	return teamid;
    }
    
    public void setTeamid(Integer id){
    	teamid=id;
    }
    
    public Integer getIdclub(){
    	return idclub;
    }
    
    public void setIdclub(Integer id){
    	idclub=id;
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
    
    
    
    public TempGame getSelectedgame(){
		return selectedgame;
	}
	
	public void setSelectedgame(TempGame selectedGame){
		selectedgame = selectedGame;
	}
    
	public TournamentGame getSelectedtournamentgame(){
		return selectedtournamentgame;
	}
	
	public void setSelectedtournamentgame(TournamentGame selectedGame){
		selectedtournamentgame = selectedGame;
	}
	
	public ExhibitionGame getSelectedexhibitiongame(){
		return selectedexhibitiongame;
	}
	
	public void setSelectedexhibitiongame(ExhibitionGame selectedGame){
		selectedexhibitiongame = selectedGame;
	}
    
	public Tournament getSelectedtournament(){
		return selectedtournament;
	}
	
	public void setSelectedtournament(Tournament selectedGame){
		selectedtournament = selectedGame;
	}
	
	public String getSelectedtournamentforgame(){
		return selectedtournamentforgame;
	}
	
	public void setSelectedtournamentforgame(String selected){
		selectedtournamentforgame = selected;
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
    	//dummy data until schedule is built
		TempGame ogame = new TempGame();
		ogame.setIdgame(1);
		ogame.setDate("Sat Sep 2nd, 2014");
		ogame.setTime("12:00 PM");
		ogame.setVisitor("Jr Ducks");
		ogame.setHome("Jr Kings");
		ogame.setLocation("Toyota Sports Center");
		ogame.setAwayscore("");
		ogame.setHomescore("");
		tempresult.add(ogame);
		
		ogame = new TempGame();
		ogame.setIdgame(2);
		ogame.setDate("Sun Sep 3rd, 2014");
		ogame.setTime("9:00 AM");
		ogame.setVisitor("Jr Gulls");
		ogame.setHome("Jr Kings");
		ogame.setLocation("Toyota Sports Center");
		ogame.setAwayscore("");
		ogame.setHomescore("");
		tempresult.add(ogame);
		
		ogame = new TempGame();
		ogame.setIdgame(3);
		ogame.setDate("Sun Sep 3rd, 2014");
		ogame.setTime("1:00 PM");
		ogame.setVisitor("Jr Flyers");
		ogame.setHome("Wave (Ontario)");
		ogame.setLocation("East West Ice Palace");
		ogame.setAwayscore("");
		ogame.setHomescore("");
		tempresult.add(ogame);
		
		setGames(tempresult);
    	TempGameDataModel = new TempGameDataModel(games);
    }

    public TempGameDataModel getTempGamedatamodel(){
    	return TempGameDataModel;
    }
    
    public void setTempgamedatamodel(TempGameDataModel odatamodel){
    	TempGameDataModel = odatamodel;
    }

    
    public TournamentGameDataModel getTournamentgamedatamodel(){
    	return TournamentGameDataModel;
    }
    
    public void setTournamentgamedatamodel(TournamentGameDataModel odatamodel){
    	TournamentGameDataModel = odatamodel;
    }
    
    public ExhibitionGameDataModel getExhibitiongamedatamodel(){
    	return ExhibitionGameDataModel;
    }
    
    public void setExhibitiongamedatamodel(ExhibitionGameDataModel odatamodel){
    	ExhibitionGameDataModel = odatamodel;
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

	
	public RosterEditDataModel getRostereditdatamodel(){
    	return RosterEditDataModel;
    }
    
    public void setRostereditdatamodel(RosterEditDataModel odatamodel){
    	RosterEditDataModel = odatamodel;
    }
    
    public RosterEditDataModel getRostercoachdatamodel(){
    	return RosterCoachDataModel;
    }
    
    public void setRostercoachdatamodel(RosterEditDataModel odatamodel){
    	RosterCoachDataModel = odatamodel;
    }
    
    
	public void getRoster(){
		List<RosterEdit> templist = new ArrayList<RosterEdit>();
		List<RosterEdit> templist2 = new ArrayList<RosterEdit>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getTeamByTeamId(?)");
			cs.setInt("teamid", this.teamid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					this.teamname = rs.getString("teamname");
				}
				LOGGER.info("We have results for team name");
			}
			rs.close();
			db.cleanup();
    		
    		//next get player roster
			cs = db.prepareCall("CALL scaha.getRosterPlayersByTeamID(?)");
			cs.setInt("teamid", this.teamid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String playerid = rs.getString("idroster");
					String fname = rs.getString("fname");
					String lname = rs.getString("lname");
					String jerseynumber = rs.getString("jerseynumber");
					
					
					RosterEdit player = new RosterEdit();
					player.setIdplayer(playerid);
					player.setFirstname(fname);
					player.setLastname(lname);
					player.setOldjerseynumber(jerseynumber);
					player.setJerseynumber(jerseynumber);
					
					templist.add(player);
				}
				LOGGER.info("We have results for team roster");
			}
			rs.close();
			
			cs = db.prepareCall("CALL scaha.getRosterCoachesByTeamID(?)");
			cs.setInt("teamid", this.teamid);
		    rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String playerid = rs.getString("idroster");
					String fname = rs.getString("fname");
					String lname = rs.getString("lname");
					String jerseynumber = rs.getString("jerseynumber");
					
					
					RosterEdit player = new RosterEdit();
					player.setIdplayer(playerid);
					player.setFirstname(fname);
					player.setLastname(lname);
					player.setOldjerseynumber(jerseynumber);
					player.setJerseynumber(jerseynumber);
					
					templist2.add(player);
				}
				LOGGER.info("We have results for team roster");
			}
			rs.close();
			
			db.cleanup();
    		
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
		
    	setPlayers(templist);
    	setCoaches(templist2);
    	RosterEditDataModel = new RosterEditDataModel(players);
    	RosterCoachDataModel = new RosterEditDataModel(coaches);
	}
    
    public List<RosterEdit> getPlayers(){
		return players;
	}
	
	public void setPlayers(List<RosterEdit> list){
		players = list;
	}
	
	public List<RosterEdit> getCoaches(){
		return coaches;
	}
	
	public void setCoaches(List<RosterEdit> list){
		coaches = list;
	}
	
	public void addTournament() {  
    
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.addTournamentForTeam(?,?,?,?,?,?,?,?,?,?)");
			cs.setInt("teamid", this.teamid);
			cs.setString("newtournamentname", this.tournamentname);
			cs.setString("newlevelplayed", this.levelplayed);
			cs.setString("newstartdate", this.startdate);
			cs.setString("newenddate", this.enddate);
			cs.setString("newcontact", this.contact);
			cs.setString("newphone", this.phone);
			cs.setString("newsanction", this.sanction);
			cs.setString("newlocation", this.location);
			cs.setString("newwebsite", this.website);
		    cs.executeQuery();
			db.commit();
			
			LOGGER.info("manager has added tournament:" + this.tournamentname);
    		
			getTournament();
			
			//need to add email to manager and scaha statistician
			to = this.pb.getProfile().getUserName();
			
			cs = db.prepareCall("CALL scaha.getSCAHAStatisticianEmail()");
  		    rs = cs.executeQuery();
  		    if (rs != null){
  				while (rs.next()) {
  					to = to + ',' + rs.getString("usercode");
  				}
  			}
  		    rs.close();
  		    db.cleanup();
  		    
			to = "lahockeyfan2@yahoo.com";
		    this.setToMailAddress(to);
		    this.setPreApprovedCC("");
		    this.setSubject(this.tournamentname + " Added for " + this.teamname);
		    
			SendMailSSL mail = new SendMailSSL(this);
			LOGGER.info("Finished creating mail object for " + this.tournamentname + " Added for " + this.teamname);
			//set flag for getbody to know which template and values to use
			this.addingtournament=true;
			mail.sendMail();
			
			//set flag back to false;
			this.addingtournament=false;
			
			//need to send successful message
			FacesContext.getCurrentInstance().addMessage("tournamentmessages", new FacesMessage(FacesMessage.SEVERITY_INFO,this.tournamentname + " has been added for team: " + this.teamname, ""));
			
			//need to clear values from overlay form after done
			this.tournamentname="";
			this.levelplayed="";
			this.startdate="";
			this.enddate="";
			this.contact="";
			this.phone="";
			this.sanction="";
			this.location="";
			this.website="";
		    
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN adding tournament");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
	}
	
	public void getTournament() {  
		List<Tournament> templist = new ArrayList<Tournament>();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getTournamentsForTeam(?)");
			cs.setInt("teamid", this.teamid);
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String idteam = rs.getString("idteamtournament");
    				String tournamentname = rs.getString("tournamentname");
    				String dates = rs.getString("dates");
    				String contact = rs.getString("contact");
    				String location = rs.getString("location");
    				String status = rs.getString("status");
    				Boolean rendered = rs.getBoolean("rendered");
    				
    				Tournament tournament = new Tournament();
    				tournament.setIdtournament(Integer.parseInt(idteam));
    				tournament.setTournamentname(tournamentname);
    				tournament.setDates(dates);
    				tournament.setContact(contact);
    				tournament.setLocation(location);
    				tournament.setStatus(status);
    				tournament.setRendered(rendered);
    				templist.add(tournament);
				}
				LOGGER.info("We have results for tourney list by team:" + this.teamid);
			}
			
			
			rs.close();
			db.cleanup();
    		
			LOGGER.info("manager has added tournament:" + this.tournamentname);
    		//need to add email sent to scaha statistician and manager
			
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting tournament list for team" + this.teamid);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setTournaments(templist);
    	TournamentDataModel = new TournamentDataModel(tournaments);
	}
	
	public List<Tournament> getTournaments(){
		return tournaments;
	}
	
	public void setTournaments(List<Tournament> list){
		tournaments = list;
	}
	
	public TournamentDataModel getTournamentdatamodel(){
    	return TournamentDataModel;
    }
    
    public void setTournamentdatamodel(TournamentDataModel odatamodel){
    	TournamentDataModel = odatamodel;
    }
	
	public void editGame(TempGame sgame) {  
	        String oldValue = sgame.getHome();
	        
	        
	        String newvalue = oldValue;
	        getRoster();
	}
	
	public void deleteTournament(Tournament tourn){
		//need to set to void
    	Integer tournamentid = tourn.getIdtournament();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("remove tournament from list");
 				CallableStatement cs = db.prepareCall("CALL scaha.deleteTeamTournament(?)");
    		    cs.setInt("teamtournamentid", tournamentid);
    		    cs.executeQuery();
    		    db.commit();
    			db.cleanup();
 				
    		    FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You have deleted the tournament"));
			} else {
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Deleting the Tournament");
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		//now we need to reload the data object for the loi list
		getTournament();
	}
	
	public void editTournamentDetail(Tournament tournament){
		String tourneyid = tournament.getIdtournament().toString();
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			context.getExternalContext().redirect("edittournamentform.xhtml?teamid=" + this.teamid + "&tournamentid=" + tourneyid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void viewTournamentDetail(Tournament tournament){
		String tourneyid = tournament.getIdtournament().toString();
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			context.getExternalContext().redirect("viewtournamentform.xhtml?teamid=" + this.teamid + "&tournamentid=" + tourneyid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getTournamentGames() {  
		List<TournamentGame> templist = new ArrayList<TournamentGame>();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getTournamentGamesForTeam(?)");
			cs.setInt("teamid", this.teamid);
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String idnonscahagame = rs.getString("idnonscahagames");
    				String tournamentname = rs.getString("tournamentname");
    				String gamedate = rs.getString("gamedate");
    				String gametime = rs.getString("gametime");
    				String opponent = rs.getString("opponent");
    				String location = rs.getString("location");
    				String status = rs.getString("status");
    				Boolean rendered = rs.getBoolean("rendered");
    				
    				TournamentGame tournament = new TournamentGame();
    				tournament.setIdgame(Integer.parseInt(idnonscahagame));
    				tournament.setTournamentname(tournamentname);
    				tournament.setDate(gamedate);
    				tournament.setTime(gametime);
    				tournament.setOpponent(opponent);
    				tournament.setLocation(location);
    				tournament.setStatus(status);
    				tournament.setRendered(rendered);
    				templist.add(tournament);
				}
				LOGGER.info("We have results for tourney list by team:" + this.teamid);
			}
			
			
			rs.close();
			db.cleanup();
    		
			LOGGER.info("manager has added tournament:" + this.tournamentname);
    		//need to add email sent to scaha statistician and manager
			
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting tournament list for team" + this.teamid);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setTournamentgames(templist);
    	TournamentGameDataModel = new TournamentGameDataModel(tournamentgames);
	}
	
	public List<TournamentGame> getTournamentgames(){
		return tournamentgames;
	}
	
	public void setTournamentgames(List<TournamentGame> tgames){
		tournamentgames = tgames;
	}
	
	public List<ExhibitionGame> getExhibitiongames(){
		return exhibitiongames;
	}
	
	public void setExhibitiongames(List<ExhibitionGame> tgames){
		exhibitiongames = tgames;
	}
	
	public void addTournamentGame(){
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.addTournamentGameForTeam(?,?,?,?,?,?)");
			cs.setInt("teamid", this.teamid);
			cs.setString("newteamtournamentid", selectedtournamentforgame);
			cs.setString("newgamedate", this.gamedate);
			cs.setString("newgametime", this.gametime);
			cs.setString("newopponent", this.opponent);
			cs.setString("newlocation", this.tourneygamelocation);
			rs=cs.executeQuery();
			if (rs != null){
  				while (rs.next()) {
  					this.tournamentname=rs.getString("tournamentname");
  				}
  			}
  		    db.commit();
			
			LOGGER.info("manager has added tournament game:" + this.gamedate);
    		
			
			//need to add email to manager and scaha statistician
			to = this.pb.getProfile().getUserName();
			
			cs = db.prepareCall("CALL scaha.getSCAHAStatisticianEmail()");
  		    rs = cs.executeQuery();
  		    if (rs != null){
  				while (rs.next()) {
  					to = to + ',' + rs.getString("usercode");
  				}
  			}
  		    rs.close();
  		    db.cleanup();
  		    
			to = "lahockeyfan2@yahoo.com";
		    this.setToMailAddress(to);
		    this.setPreApprovedCC("");
		    this.setSubject("Tournament Game Added vs " + this.opponent);
		    
			SendMailSSL mail = new SendMailSSL(this);
			LOGGER.info("Finished creating mail object for Tournament Game Added vs " + this.opponent);
			//set flag for getbody to know which template and values to use
			this.addingtournamentgame=true;
			mail.sendMail();
			
			//set flag back to false;
			this.addingtournamentgame=false;
			
			//need to clear overlay panel values
			this.selectedtournamentforgame="0";
			this.gamedate="";
			this.gametime="";
			this.opponent="";
			this.tourneygamelocation="";
			
			
			
			getTournamentGames();
			
			//need to send successful message
			FacesContext.getCurrentInstance().addMessage("tournamentgamemessages", new FacesMessage(FacesMessage.SEVERITY_INFO,this.tournamentname + "game has been added for team: " + this.teamname, ""));
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN adding tournament");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
	}
	
	public void deleteTournamentGame(TournamentGame tourn){
		//need to set to void
    	Integer gameid = tourn.getIdgame();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("remove tournament game from list");
 				CallableStatement cs = db.prepareCall("CALL scaha.deleteTeamTournamentGame(?)");
    		    cs.setInt("gameid", gameid);
    		    cs.executeQuery();
    		    db.commit();
    			db.cleanup();
 				
    		    FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You have deleted the tournament game"));
			} else {
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Deleting the Tournament");
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		//now we need to reload the data object for the loi list
		getTournamentGames();
	}

	public void editGameDetail(TournamentGame tournament){
		String gameid = tournament.getIdgame().toString();
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			context.getExternalContext().redirect("edittournamentgameform.xhtml?teamid=" + this.teamid + "&gameid=" + gameid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addExhibitionGame(){
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.addExhibitionGameForTeam(?,?,?,?,?)");
			cs.setInt("teamid", this.teamid);
			cs.setString("newgamedate", this.gamedate);
			cs.setString("newgametime", this.gametime);
			cs.setString("newopponent", this.opponent);
			cs.setString("newlocation", this.exhibitiongamelocation);
			cs.executeQuery();
			db.commit();
			
			LOGGER.info("manager has added exhibition game:" + this.gamedate);
    		
			//need to add email to manager and scaha statistician
			to = this.pb.getProfile().getUserName();
			
			cs = db.prepareCall("CALL scaha.getSCAHAStatisticianEmail()");
  		    rs = cs.executeQuery();
  		    if (rs != null){
  				while (rs.next()) {
  					to = to + ',' + rs.getString("usercode");
  				}
  			}
  		    rs.close();
  		    db.cleanup();
  		    
			to = "lahockeyfan2@yahoo.com";
		    this.setToMailAddress(to);
		    this.setPreApprovedCC("");
		    this.setSubject("Exhibition game Added vs " + this.opponent);
		    
			SendMailSSL mail = new SendMailSSL(this);
			LOGGER.info("Finished creating mail object for exhibition game Added vs " + this.opponent);
			
			//set flag for getbody to know which template and values to use
			this.addingexhibitiongame=true;
			mail.sendMail();
			
			//set flag back to false;
			this.addingexhibitiongame=false;
			
			//need to send successful message
			FacesContext.getCurrentInstance().addMessage("exhibitiongamemessages", new FacesMessage(FacesMessage.SEVERITY_INFO,"The exhibition game against " + this.opponent + " has been added for team: " + this.teamname, ""));
			
			//need to clear fields for overlay panel
			this.gamedate="";
			this.gametime="";
			this.opponent="";
			this.exhibitiongamelocation="";
			
			
			getExhibitionGames();
			
			//need to add email to manager and scaha statistician
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN adding exhibition game");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	
	}
	
	public void getExhibitionGames() {  
		List<ExhibitionGame> templist = new ArrayList<ExhibitionGame>();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getExhibitionGamesForTeam(?)");
			cs.setInt("teamid", this.teamid);
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					String idnonscahagame = rs.getString("idnonscahagames");
    				String gamedate = rs.getString("gamedate");
    				String gametime = rs.getString("gametime");
    				String opponent = rs.getString("opponent");
    				String location = rs.getString("location");
    				String status = rs.getString("status");
    				Boolean rendered = rs.getBoolean("rendered");
    				
    				ExhibitionGame tournament = new ExhibitionGame();
    				tournament.setIdgame(Integer.parseInt(idnonscahagame));
    				tournament.setDate(gamedate);
    				tournament.setTime(gametime);
    				tournament.setOpponent(opponent);
    				tournament.setLocation(location);
    				tournament.setStatus(status);
    				tournament.setRendered(rendered);
    				templist.add(tournament);
				}
				LOGGER.info("We have results for exhibition list by team:" + this.teamid);
			}
			
			
			rs.close();
			db.cleanup();
    		
			//need to add email sent to scaha statistician and manager
			
			
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting tournament list for team" + this.teamid);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setExhibitiongames(templist);
    	ExhibitionGameDataModel = new ExhibitionGameDataModel(exhibitiongames);
	}
	
	public void deleteExhibitionTournamentGame(ExhibitionGame game){
		Integer gameid = game.getIdgame();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("remove tournament game from list");
 				CallableStatement cs = db.prepareCall("CALL scaha.deleteTeamTournamentGame(?)");
    		    cs.setInt("gameid", gameid);
    		    cs.executeQuery();
    		    db.commit();
    			db.cleanup();
 				
    		    FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You have deleted the exhibition game"));
			} else {
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Deleting the Tournament");
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		//now we need to reload the data object for the loi list
		getExhibitionGames();
	}
	
	public void editExhibitionGameDetail(ExhibitionGame game){
		String gameid = game.getIdgame().toString();
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			context.getExternalContext().redirect("editexhibitiongameform.xhtml?teamid=" + this.teamid + "&gameid=" + gameid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void uploadTournamentScoresheet(TournamentGame game){
		String gameid = game.getIdgame().toString();
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			context.getExternalContext().redirect("managegamescoresheet.xhtml?id=" + gameid + "&teamid=" + this.teamid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void uploadExhibitionScoresheet(ExhibitionGame game){
		String gameid = game.getIdgame().toString();
		FacesContext context = FacesContext.getCurrentInstance();
				
		try{
			context.getExternalContext().redirect("managegamescoresheet.xhtml?id=" + gameid + "&teamid=" + this.teamid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAlerts(){
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		Boolean ScahaGames = false;
		Boolean Tournaments = false;
		Boolean TournamentGames = false;
		Boolean ExhibitionGames = false;
		
		try{

			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("get flags for alerts");
 				CallableStatement cs = db.prepareCall("CALL scaha.getManagerFlags(?)");
 				cs.setInt("teamid",this.teamid);
    		    rs = cs.executeQuery();
    		    
    		    if (rs != null){
    				
    				while (rs.next()) {
    					ScahaGames = rs.getBoolean("scahagamesflag");
    					Tournaments = rs.getBoolean("tournamentsflag");
    					TournamentGames = rs.getBoolean("tournamentgamesflag");
    					ExhibitionGames = rs.getBoolean("exhibitiongamesflag");
        			}
    				LOGGER.info("We have results for exhibition list by team:" + this.teamid);
    			}
    			
    			rs.close();
    		    db.commit();
    			db.cleanup();
 				
    		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Deleting the Tournament");
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
        
		
		if (ScahaGames){
			context.addMessage("scahagamesmessages", new FacesMessage(FacesMessage.SEVERITY_WARN,"Action Needed!", "You have SCAHA Games needing scoresheet entry and/or scoresheet upload"));
		}
		
		if (Tournaments){
			context.addMessage("scahagamesmessages", new FacesMessage(FacesMessage.SEVERITY_WARN,"Action Needed!", "A holiday weekend is less than two weeks away, have you submitted your tournmanet notification?"));
		}
		
		if (TournamentGames){
			context.addMessage("scahagamesmessages", new FacesMessage(FacesMessage.SEVERITY_WARN,"Action Needed!", "You have Tournament Games needing the scoresheet uploaded."));
		}
		
		if (ExhibitionGames){
			context.addMessage("scahagamesmessages", new FacesMessage(FacesMessage.SEVERITY_WARN,"Action Needed!", "You have Exhibition Games needing the scoresheet uploaded."));
		}
		
	}
	
	private void setTodaysDate(){
		//need to set todays date for email
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Date date = new Date();
		this.setTodaysdate(dateFormat.format(date).toString());
		
	}

	private void setAddingflags(){
		//need to set todays date for email
		this.addingtournament=false;
		this.addingtournamentgame=false;
		this.addingexhibitiongame=false;
		
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
}

