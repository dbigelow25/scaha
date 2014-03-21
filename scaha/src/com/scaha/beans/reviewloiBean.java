package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;

//import com.gbli.common.SendMailSSL;


public class reviewloiBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private List<Player> players = null;
    private PlayerDataModel PlayerDataModel = null;
    private Player selectedplayer = null;
	private String selectedtabledisplay = null;
	private List<Club> clubs = null;
	private Boolean displayclublist = null;
	private String selectedclub = null;
	private String selectedplayerid = null;
	
	
    public reviewloiBean() {  
        players = new ArrayList<Player>();  
        PlayerDataModel = new PlayerDataModel(players);
        this.setSelectedtabledisplay("1");
        
        playersDisplay(); 
    }  
    
   
    
    public String getSelectedplayerid(){
    	return selectedplayerid;
    }
    
    public void setSelectedplayerid(String selidplayer){
    	selectedplayerid=selidplayer;
    }
    
    public String getSelectedclub(){
    	return selectedclub;
    }
    
    public void setSelectedclub(String clubselected){
    	selectedclub=clubselected;
    }
    
    public Boolean getDisplayclublist(){
    	return displayclublist;
    }
    
    public void setDisplayclublist(Boolean club){
    	displayclublist = club;
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
    	java.util.Date date = new java.util.Date();
    	
    	try{

    		if (db.setAutoCommit(false)) {
    			
    			if (selectedtabledisplay.equals("1")){
    				CallableStatement cs = db.prepareCall("CALL scaha.getLoiList(?)");
	    			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	    			cs.setDate("lookupdate", sqlDate);
	    			rs = cs.executeQuery();
    			} else if (selectedtabledisplay.equals("2")){
    				CallableStatement cs = db.prepareCall("CALL scaha.getLoiMissingBirthCertificate()");
	    			rs = cs.executeQuery();
    			} else if (selectedtabledisplay.equals("3")){
    				CallableStatement cs = db.prepareCall("CALL scaha.getLoiMissingTransfer()");
	    			rs = cs.executeQuery();
    			} else if (selectedtabledisplay.equals("4")){
    				CallableStatement cs = db.prepareCall("CALL scaha.getLoiByClub(?)");
	    			cs.setInt("clubid", Integer.parseInt(this.selectedclub));
    				rs = cs.executeQuery();
    			}
    		    
    			if (rs != null){
    				
    				while (rs.next()) {
    					String idplayer = rs.getString("idplayer");
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
    				
    				LOGGER.info("We have results for lois for the lookup date" + date.toString());
    				
    			}
    				
    			db.cleanup();
    		} else {

    		}
    		
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR Lois for lookup date:" + date.toString());
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
    
    public List<Club> getListofClubs(){
		List<Club> templist = new ArrayList<Club>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			//Vector<Integer> v = new Vector<Integer>();
    			//v.add(1);
    			//db.getData("CALL scaha.getTeamsByClub(?)", v);
    		    CallableStatement cs = db.prepareCall("CALL scaha.getClubs()");
    		    rs = cs.executeQuery();
    			
    			if (rs != null){
    				//need to add to an array
    				//rs = db.getResultSet();
    				
    				while (rs.next()) {
    					String idclub = rs.getString("idclubs");
        				String clubname = rs.getString("clubname");
        				
        				Club club = new Club();
        				club.setClubid(idclub);
        				club.setClubname(clubname);
        						
        				templist.add(club);
    				}
    				LOGGER.info("We have results for division list");
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
		
    	setClubs(templist);
		
		return getClubs();
	}
    
    public List<Club> getClubs(){
		return clubs;
	}
	
	public void setClubs(List<Club> list){
		clubs = list;
	}
	
	public void addTransferCitizenship(Player selectedPlayer){
	
		String sidplayer = selectedPlayer.getIdplayer();
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("addtransfercitizenship.xhtml?playerid=" + sidplayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void voidLoi(Player selectedPlayer){
		
		String sidplayer = selectedPlayer.getIdplayer();
		String playname = selectedPlayer.getPlayername();
		
		//need to set to void
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.settoVoid(?)");
    		    cs.setInt("playerid", Integer.parseInt(sidplayer));
    		    rs=cs.executeQuery();
    		    
    		    db.commit();
    			db.cleanup();
 				
    		    //logging 
    			LOGGER.info("We are voiding the loi for player id:" + sidplayer);
    		    
    			FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You have voided the loi for:" + playname));
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
		
		//now we need to reload the data object for the loi list
		playersDisplay();
	}
	
	public void viewLoi(Player selectedPlayer){
		
		String sidplayer = selectedPlayer.getIdplayer();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.getPersonIdbyPlayerId(?)");
    		    cs.setInt("iplayerid", Integer.parseInt(sidplayer));
    		    rs=cs.executeQuery();
    		    
    		    if (rs != null){
    				
    				while (rs.next()) {
    					Integer idplayer = rs.getInt("idperson");
    					sidplayer = idplayer.toString();
        			}
    				LOGGER.info("We have results for division list");
    			}
    			db.commit();
    			db.cleanup();
 			} else {
		
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Retrieving PersonId" + this.selectedplayer);
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("scahaviewloi.xhtml?playerid=" + sidplayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CloseLoi(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("confirmlois.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

