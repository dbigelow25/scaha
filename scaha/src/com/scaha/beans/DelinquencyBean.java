package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;

//import com.gbli.common.SendMailSSL;

@ManagedBean(name="delinquencyBean")
@ViewScoped
public class DelinquencyBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

    private PlayerDataModel playerlist = null;
    private Player selectedplayer = null;
	private String selectedtabledisplay = null;
	private Boolean displayclublist = null;
	private String selectedclub = null;
	private String selectedplayerid = null;
	private Boolean displayshortlist = null;
	private Integer displayrecordcount = null;
	private Integer totalrecordcount = null;
	
	@ManagedProperty(value = "#{profileBean}")
	private ProfileBean pb = null;
	@ManagedProperty(value = "#{scahaBean}")
	private ScahaBean scaha = null;
	
	
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

	@PostConstruct
	public void init() {
		LOGGER.info("**************** DelinquencyBean Has Been Init-ed ********************");
		playersDisplay(); 
    }  
    
	public Integer getTotalrecordcount(){
    	return totalrecordcount;
    }
    
    public void setTotalrecordcount(Integer count){
    	totalrecordcount=count;
    }
	
	public Integer getDisplayrecordcount(){
    	return displayrecordcount;
    }
    
    public void setDisplayrecordcount(Integer count){
    	displayrecordcount=count;
    }
    
	
	public Boolean getDisplayshortlist(){
    	return displayshortlist;
    }
    
    public void setDisplayshortlist(Boolean flag){
    	displayshortlist=flag;
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
    
	    
    //retrieves list of players
    public void playersDisplay(){
    	ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	List<Player> tempresult = new ArrayList<Player>();
    	Integer recordcount = 0;
    	
    	//need to set default record count and list format boolean for displaying the list either in pagination format
        //or long list printable format
        this.displayshortlist = true;
        this.displayrecordcount = 10;
        this.totalrecordcount = 0;
        
    	
    	try{

			CallableStatement cs = db.prepareCall("CALL scaha.getDelinquencyList()");
			ResultSet rs = cs.executeQuery();
    			
			while (rs.next()) {
				String idplayer = rs.getString("idplayer");
				String sfirstname = rs.getString("fname");
				String slastname = rs.getString("lname");
				String scurrentteam = rs.getString("currentteam");
				String sdob = rs.getString("dob");
				
				Player oplayer = new Player();
				oplayer.setIdplayer(idplayer);
				oplayer.setFirstname(sfirstname);
				oplayer.setLastname(slastname);
				oplayer.setCurrentteam(scurrentteam);
				oplayer.setDob(sdob);
				
				tempresult.add(oplayer);
				
				recordcount++;
			}
    				
   			this.totalrecordcount=recordcount;
   			rs.close();	
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN Searching FOR delinquency list");
    		e.printStackTrace();
    	} finally {
    		db.free();
    	}
    	
    	setPlayerlist(new PlayerDataModel(tempresult));
    }

    public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
		}
    	
    }
    
    public void removePlayer(Player selectedPlayer){
		
		for (Player p : playerlist) {
			if (p.getIdplayer().equals(selectedPlayer.getIdplayer())) {
				selectedPlayer = p;
				break;
			}
		}
		
		String sidplayer = selectedPlayer.getIdplayer();
		String playname = selectedPlayer.getFirstname() + " " + selectedPlayer.getLastname();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			LOGGER.info("update player in delinquency list");
			CallableStatement cs = db.prepareCall("CALL scaha.removeDelinquency(?)");
		    cs.setInt("iplayerid", Integer.parseInt(sidplayer));
		    cs.executeUpdate();
			LOGGER.info("We are remove the delinquency for player id:" + sidplayer);
		    cs.close();
			FacesContext context = FacesContext.getCurrentInstance();  
            context.addMessage(null, new FacesMessage("Successful", "You have removed the delinquency for :" + playname));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN removing the delinquency for " + this.selectedplayer);
			e.printStackTrace();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		List<Player> pl = (List<Player>) playerlist.getWrappedData();
		pl.remove(selectedPlayer);
	}
    
    public void displayLongList(){
    	this.displayshortlist=false;
    	this.displayrecordcount=0;
    }
    
    public void displayShortList(){
    	this.displayshortlist=true;
    	this.displayrecordcount=10;
    }

	/**
	 * @return the playerlist
	 */
	public PlayerDataModel getPlayerlist() {
		return playerlist;
	}

	/**
	 * @param playerlist the playerlist to set
	 */
	public void setPlayerlist(PlayerDataModel playerlist) {
		this.playerlist = playerlist;
	}
}

