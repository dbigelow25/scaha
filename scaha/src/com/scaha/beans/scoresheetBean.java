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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.ExhibitionGame;
import com.scaha.objects.ExhibitionGameDataModel;
import com.scaha.objects.FileUploadController;
import com.scaha.objects.Scoresheet;
import com.scaha.objects.ScoresheetDataModel;

//import com.gbli.common.SendMailSSL;

@ManagedBean
@SessionScoped
public class scoresheetBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;

	transient private ResultSet rs = null;
	//lists for generated datamodels
	private List<Scoresheet> scoresheets = null;
	
	//bean level properties used by multiple methods
	private Integer teamid = null;
	private Integer idclub = null;
	private Integer profileid = 0;
	private String gametype = null;
	private Integer idgame = null;
	private Scoresheet selectedscoresheet = null;
	
	//datamodels for all of the lists on the page
	private ScoresheetDataModel ScoresheetDataModel = null;
    
	//properties for uploading scoresheet
	private FileUploadController fileuploadcontroller;
	
	@PostConstruct
    public void init() {
		//load defaults
		idclub = 1;  
        this.setProfid(pb.getProfile().ID);
        getClubID();
        
      //will need to load player profile information for displaying loi
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	  	
	  	if(hsr.getParameter("gameid") != null)
	    {
	  		this.idgame = Integer.parseInt(hsr.getParameter("gameid").toString());
	    }
	  	if(hsr.getParameter("gametype") != null)
	    {
	  		this.gametype = hsr.getParameter("gametype").toString();
	    }
        
	  	
	  	this.fileuploadcontroller = new FileUploadController();
	  	getGameScoresheets();
	}
	
    public scoresheetBean() {  
        
    }  
    
    public Scoresheet getSelectedscoresheet(){
    	return selectedscoresheet;
    }
    
    public void setSelectedscoresheet(Scoresheet selected){
    	selectedscoresheet=selected;
    }
    
    public List<Scoresheet> getScoresheets(){
		return scoresheets;
	}
	
	public void setScoresheets(List<Scoresheet> list){
		scoresheets = list;
	}
    
    public String getGametypee(){
    	return gametype;
    }
    
    public void setGametype(String gdate){
    	gametype=gdate;
    }
    
    public ScoresheetDataModel getScoresheetgamedatamodel(){
    	return ScoresheetDataModel;
    }
    
    public void setScoresheetdatamodel(ScoresheetDataModel odatamodel){
    	ScoresheetDataModel = odatamodel;
    }

    
    public FileUploadController getFileuploadcontroller(){
    	return fileuploadcontroller;
    }
    
    public void setFileuploadcontroller(FileUploadController gdate){
    	fileuploadcontroller=gdate;
    }
    
    public Integer getTeamid(){
    	return teamid;
    }
    
    public void setTeamid(Integer id){
    	teamid=id;
    }
    
    public Integer getIdgame(){
    	return idgame;
    }
    
    public void setIdgame(Integer id){
    	idgame=id;
    }
    
    
    public Integer getIdclub(){
    	return idclub;
    }
    
    public void setIdclub(Integer id){
    	idclub=id;
    }
    
    public Integer getProfid(){
    	return profileid;
    }	
    
    public void setProfid(Integer idprofile){
    	profileid = idprofile;
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

	public void handleFileUpload(FileUploadEvent event) {  
        
		Scoresheet scoresheet = new Scoresheet();
		scoresheet.setIdgame(this.idgame);
		scoresheet.setGametype(this.gametype);
		
		if (this.fileuploadcontroller.handleFileUpload(event,scoresheet)){
			
			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
			
			try{

				if (db.setAutoCommit(false)) {
				
					//Need to provide info to the stored procedure to save the scoresheet filename, game type and game id
	 				LOGGER.info("remove tournament game from list");
	 				CallableStatement cs = db.prepareCall("CALL scaha.addScoresheetForGame(?,?,?,?)");
	    		    cs.setInt("gameid", this.idgame);
	    		    cs.setString("infilename", scoresheet.getFilename());
	    		    cs.setString("indisplayname",scoresheet.getFiledisplayname());
	    		    cs.setString("ingametype", this.gametype);
	    		    cs.executeQuery();
	    		    db.commit();
	    			db.cleanup();
	 				
	    			LOGGER.info("You have added the scoresheet :" + scoresheet.getFilename() + " gameid:" + this.idgame.toString());
	    		    //now to reload the scoresheet collection for datatable update
	    			getGameScoresheets();
				} else {
					//add error message here...
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				LOGGER.info("ERROR IN adding scoresheet for game id:" + this.idgame);
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
	
    
	public void getGameScoresheets() {  
		List<Scoresheet> templist = new ArrayList<Scoresheet>();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{
    		//first get team name
    		CallableStatement cs = db.prepareCall("CALL scaha.getScoresheetsForGame(?)");
			cs.setInt("gameid", this.idgame);
			rs = cs.executeQuery();
			
			if (rs != null){
				
				while (rs.next()) {
					Integer idscoresheets = rs.getInt("idscoresheets");
    				String filename = rs.getString("filename");
    				String gametype = rs.getString("gametype");
    				String displayname = rs.getString("displayname");
    				String uploaddate = rs.getString("uploaddate");
    				
    				Scoresheet scoresheet = new Scoresheet();
    				scoresheet.setFilename(filename);
    				scoresheet.setGametype(gametype);
    				scoresheet.setIdscoresheet(idscoresheets);
    				scoresheet.setUploaddate(uploaddate);
    				scoresheet.setFiledisplayname(displayname);
    				templist.add(scoresheet);
				}
				LOGGER.info("We have results for scoresheets for game:" + this.idgame);
			}
			
			
			rs.close();
			db.cleanup();
    		
		} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN getting scoresheet list for game:" + this.idgame);
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setScoresheets(templist);
    	ScoresheetDataModel = new ScoresheetDataModel(scoresheets);
	}
}

