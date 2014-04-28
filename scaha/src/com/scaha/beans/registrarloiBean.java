package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class registrarloiBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private List<Player> players = null;
    private PlayerDataModel PlayerDataModel = null;
    private Player selectedplayer = null;
	private String selectedtabledisplay = null;
	private List<Team> teams = null;
	private Boolean displayteamlist = null;
	private String selectedteam = null;
	private String selectedplayerid = null;
	private Integer clubid = 0;
	private Integer profileid = 0;
	
	
    public registrarloiBean() {  
    	clubid = 1;
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
    	this.setProfid(pb.getProfile().ID);
    	loadClubName();
    	
    	players = new ArrayList<Player>();  
        PlayerDataModel = new PlayerDataModel(players);
        this.setSelectedtabledisplay("1");
        
        playersDisplay(); 
    }  
    
    public Integer getProfid(){
    	return profileid;
    }
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
    }
    
    public Integer getClubid(){
    	return clubid;
    }
    
    public void setClubid(Integer idclub){
    	clubid = idclub;
    }
    
    public String getSelectedteam(){
    	return selectedteam;
    }
    
    public void setSelectedteam(String teamselected){
    	selectedteam=teamselected;
    }
    
    public Boolean getDisplayteamlist(){
    	return displayteamlist;
    }
    
    public void setDisplayteamlist(Boolean team){
    	displayteamlist = team;
    }
    
    public String getSelectedtabledisplay(){
		return selectedtabledisplay;
	}
	
	public void setSelectedtabledisplay(String selectedTabledisplay){
		selectedtabledisplay = selectedTabledisplay;
	}
    
    public Player getSelectedplayer(){
		return selectedplayer;
	}
	
	public void setSelectedplayer(Player selectedPlayer){
		selectedplayer = selectedPlayer;
	}
    
	public List<Player> getPlayers(){
		return players;
	}
	
	public void setPlayers(List<Player> list){
		players = list;
	}
	
	    
    //retrieves list of players
    public void playersDisplay(){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Player> tempresult = new ArrayList<Player>();
    	
    	try{

    		if (db.setAutoCommit(false)) {
    			if (this.selectedteam==null){
    				this.selectedteam = "0";
    			}
    			Integer iteam = Integer.parseInt(this.selectedteam);
    			
				if (iteam.equals(0)){
					CallableStatement cs = db.prepareCall("CALL scaha.getLoiByClub(?)");
					cs.setInt("clubid", this.clubid);
					rs = cs.executeQuery();
				} else {
					CallableStatement cs = db.prepareCall("CALL scaha.getLoiListByTeam(?)");
					cs.setInt("teamid", iteam);
					rs = cs.executeQuery();
				}
				
    			if (rs != null){
    				
    				while (rs.next()) {
    					String idplayer = rs.getString("idperson");
        				String sfirstname = rs.getString("firstname");
        				String slastname = rs.getString("lastname");
        				String scurrentteam = rs.getString("currentteam");
        				String slastyearteam = rs.getString("lastyearteam");
        				if (slastyearteam==null){
        					slastyearteam="N/A";
        				}
        				String sdob = rs.getString("dob");
        				String sloidate = rs.getString("loidate");
        				String scitizenship = rs.getString("citizenship");
        				String scitizenshipexpiredate = rs.getString("citizenshipexpiredate");
        				String scitizenshiptransfer = rs.getString("citizenshiptransfer");
        				if (scitizenshiptransfer==null){
        					scitizenshiptransfer="0";
        				}
        				String sbirthcertificate = rs.getString("birthcertificate");
        				String splayerup = rs.getString("isplayerup");
        				String sindefinite = rs.getString("indefinite");
        				
        				Player oplayer = new Player();
        				oplayer.setIdplayer(idplayer);
        				oplayer.setFirstname(sfirstname);
        				oplayer.setLastname(slastname);
        				oplayer.setCurrentteam(scurrentteam);
        				oplayer.setPreviousteam(slastyearteam);
        				oplayer.setDob(sdob);
        				oplayer.setCitizenship(scitizenship);
        				oplayer.setCitizenshiptransfer(scitizenshiptransfer);
        				oplayer.setCtverified(scitizenshiptransfer);
        				oplayer.setCTExpirationdate(scitizenshipexpiredate);
        				
        				if (scitizenship!=null){
	        				if (!scitizenship.equals("USA")){
		        				if (sindefinite!=null){
		        					if (sindefinite.equals("1")){
		            					oplayer.setCitizenshiplabel("Transfer does not expire");
		            				} else if (sindefinite.equals("1") && scitizenshiptransfer.equals("1")){
		            					oplayer.setCitizenshiplabel("Transfer expires " + scitizenshipexpiredate);
		            				} else if (sindefinite.equals("0") && scitizenshiptransfer.equals("1")){
		            					oplayer.setCitizenshiplabel("Transfer expires " + scitizenshipexpiredate);
		            				} else if (sindefinite.equals("0") && scitizenshiptransfer.equals("0")){
		            					oplayer.setCitizenshiplabel("Transfer is needed.");
		            				} else if (sindefinite.equals("1") && scitizenshiptransfer.equals("0")){
		            					oplayer.setCitizenshiplabel("Transfer is needed.");
		            				} else {
		            					oplayer.setCitizenshiplabel("Transfer is needed.");
		            				}
		        				} else {
		        					oplayer.setCitizenshiplabel("Transfer is needed.");
		        				}
	        				}
        				}
        				
        				oplayer.setBirthcertificate(sbirthcertificate);
        				oplayer.setBcverified(sbirthcertificate);
        				oplayer.setLoidate(sloidate);
        				oplayer.setPlayerup(IsPlayerup(splayerup));
        				tempresult.add(oplayer);
    				}
    				
    				LOGGER.info("We have results for lois for the team: " + selectedteam);
    				
    			}
    				
    			db.cleanup();
    		} else {

    		}
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR Lois for selected team:");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
    	
    	//setResults(tempresult);
    	setPlayers(tempresult);
    	PlayerDataModel = new PlayerDataModel(players);
    }

    public PlayerDataModel getPlayerdatamodel(){
    	return PlayerDataModel;
    }
    
    public void setPlayerdatamodel(PlayerDataModel odatamodel){
    	PlayerDataModel = odatamodel;
    }
    
    public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
		}
    	
    }
    
    public String IsPlayerup(String sname){
    	if (sname.equals("1")){
    		return "Yes";
    	} else {
    		return "";
    	}
    	
    }
    
    public List<Team> getListofTeams(){
		List<Team> templist = new ArrayList<Team>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			//Vector<Integer> v = new Vector<Integer>();
    			//v.add(1);
    			//db.getData("CALL scaha.getTeamsByClub(?)", v);
    		    CallableStatement cs = db.prepareCall("CALL scaha.getTeamsbyClubId(?)");
    		    cs.setInt("pclubid", this.clubid);
    		    rs = cs.executeQuery();
    			
    			if (rs != null){
    				//need to add to an array
    				//rs = db.getResultSet();
    				
    				while (rs.next()) {
    					String idteam = rs.getString("idteams");
        				String teamname = rs.getString("teamname");
        				
        				Team team = new Team(teamname,idteam);
        				team.setIdteam(idteam);
        				team.setTeamName(teamname);
        				
        				
        				templist.add(team);
    				}
    				LOGGER.info("We have results for team list");
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
	
public void loadClubName(){
		
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
		
		
	}
	
}

