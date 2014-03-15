package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

//import com.gbli.common.SendMailSSL;


public class rosteractionBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private String selectedplayer = null;
	private String playername = null;
	private Integer transfer = null;
	private Integer transferindefinite = null;
	private String expirationdate = null;
	private Integer transferid = null;
	private Integer birthcertificate = null;
	private String gotoTransferInformation = null;
	private String gotoCitizenship = null;
	
    public rosteractionBean() {  
        //will need to load player profile information for displaying citienship and transfer informatoin
    	transfer = 0;
    	transferindefinite = 0;
    	transferid = 0;
    	
    	
    	HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("playerid") != null)
        {
    		setSelectedplayer(hsr.getParameter("playerid").toString());
        }
    	
    	loadPlayerProfile(selectedplayer);

    	//doing anything else right here
    	gotoTransferInformation = "addtransfercitizenship.xhtml?playerid=" + this.selectedplayer;
    	gotoCitizenship = "managecitizenship.xhtml?playerid=" + this.selectedplayer;
    }  
    
    public void setGotoTransferInformation(String url){
    	gotoTransferInformation = url;
    }
    
    public String getGotoTransferInformation(){
    	return gotoTransferInformation;
    }
    
    public void setGotoCitizenship(String url){
    	gotoCitizenship = url;
    }
    
    public String getGotoCitizenship(){
    	return gotoCitizenship;
    }
    
    public Integer getBirthcertificate(){
    	return birthcertificate;
    }
    
    public void setBirthcertificate(Integer certificate){
    	birthcertificate = certificate;
    }
    
    public String getSelectedplayer(){
    	return selectedplayer;
    }
    
    public void setSelectedplayer(String selectedPlayer){
    	selectedplayer = selectedPlayer;
    }
    
    public String getPlayername(){
    	return playername;
    }
    
    public void setPlayername(String playerName){
    	playername = playerName;
    }
    
    public Integer getTransfer(){
    	return transfer;
    }
    
    public void setTransfer(Integer Transfer){
    	transfer = Transfer;
    }
    
    public Integer getTransferindefinite(){
    	return transferindefinite;
    }
    
    public String getExpirationdate(){
    	return expirationdate;
    }
    
    public void setExpirationdate(String playerName){
    	expirationdate = playerName;
    }
    
    public void setTransferindefinite(Integer Transfer){
    	transferindefinite = Transfer;
    }
	
    public Integer getTransferid(){
    	return transferid;
    }
    
    public void setTransferid(Integer Transfer){
    	transferid = Transfer;
    }
    
    
    
	//used to populate loi form with player information
	public void loadPlayerProfile(String selectedplayer){
		//first get player detail information then get family members
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (!selectedplayer.equals("")) {
    		
    			Vector<Integer> v = new Vector<Integer>();
    			v.add(Integer.parseInt(selectedplayer));
    			db.getData("CALL scaha.getTransferbyPlayerId(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					playername = rs.getString("playername");
    					transferid = rs.getInt("idcitizenshiptransfers");
        				transfer = rs.getInt("citizenshiptransfers");
        				transferindefinite = rs.getInt("indefinite");
        				birthcertificate = rs.getInt("birthcertificate");
        				
        				Date dexpirationdate = rs.getDate("expirationdate");
        				
        				DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        				if (dexpirationdate!=null){
        					expirationdate = dateformat.format(dexpirationdate).toString();
        				} else {
        					expirationdate = "";
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
    	
   }
	
	public void completeTransfer(){

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.saveTransfer(?,?,?,?,?)");
    		    cs.setInt("transferid", this.transferid);
    		    cs.setInt("playerid", Integer.parseInt(this.selectedplayer));
    		    cs.setInt("transfer", this.transfer);
    		    cs.setInt("transferindefinite", this.transferindefinite);
    		    if (this.expirationdate.equals("")){
    		    	cs.setString("sexpirationdate",null);
    		    }else{
    		    	cs.setString("sexpirationdate",this.expirationdate);
    		    }
    		    rs = cs.executeQuery();
    			
    		    db.commit();
    			db.cleanup();
 				
    		    //logging 
    			LOGGER.info("We are updating transfer info for player id:" + this.selectedplayer);
    		    
    			FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You ave updated the Transfer"));
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
	
	public void completeCertificate(){

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				//Need to provide info to the stored procedure to save or update
 				LOGGER.info("verify loi code provided");
 				CallableStatement cs = db.prepareCall("CALL scaha.saveCertificate(?,?)");
    		    cs.setInt("playerid", Integer.parseInt(this.selectedplayer));
    		    cs.setInt("certificate", this.birthcertificate);
    		    
    		    rs = cs.executeQuery();
    		    
    		    db.commit();
    			db.cleanup();
 				
    		    //logging 
    			LOGGER.info("We are updating birth certificate for player id:" + this.selectedplayer);
    		    
    			FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Successful", "You ave updated the Birth Certificate"));
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
	
	public void Close(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			context.getExternalContext().redirect("confirmlois.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}

