package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;

//import com.gbli.common.SendMailSSL;


public class reviewloiBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private ResultSet rs = null;
	private List<Player> players = null;
    private PlayerDataModel PlayerDataModel = null;
    private Player selectedplayer = null;
	private Integer idclub = null;
	
    public reviewloiBean() {  
        players = new ArrayList<Player>();  
        PlayerDataModel = new PlayerDataModel(players);
        
        playersDisplay(); 
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
    			
    			CallableStatement cs = db.prepareCall("CALL scaha.getLoiList(?)");
    			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    			cs.setDate("lookupdate", sqlDate);
    		    rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					String idplayer = rs.getString("idplayer");
        				String splayername = rs.getString("playername");
        				String scurrentteam = rs.getString("currentteam");
        				String slastyearteam = rs.getString("lastyearteam");
        				String sdob = rs.getString("dob");
        				String sloidate = rs.getString("loidate");
        				String scitizenship = "";
        				String sbirthcertificate = "";
        				
        				Player oplayer = new Player();
        				oplayer.setIdplayer(idplayer);
        				oplayer.setPlayername(splayername);
        				oplayer.setCurrentteam(scurrentteam);
        				oplayer.setPreviousteam(slastyearteam);
        				oplayer.setDob(sdob);
        				oplayer.setCitizenship(scitizenship);
        				oplayer.setBirthcertificate(sbirthcertificate);
        				oplayer.setLoidate(sloidate);
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
}

