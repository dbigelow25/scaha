package com.scaha.beans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;
import com.scaha.objects.TeamList;

@ManagedBean
@SessionScoped
public class ClubBean implements Serializable,  MailableObject {
	
	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	
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

	private int currentLevel = 1;
	
	private Club selectedclub = null;
	private TeamList selectedTeamList = null;
	
	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// lets go get it!
	//
	public ClubBean() {
	}

	 @PostConstruct
	 public void init() {

	 }
	 
	public void handleFileUpload(FileUploadEvent event) {
	    this.selectedclub.getLogo().setMmObject(event.getFile().getContents());
	    this.selectedclub.getLogo().setExtension(event.getFile().getContentType());
	    LOGGER.info("GET THE INFO:" + event.getFile().getFileName() + ":" + event.getFile().getSize());
	}

	/**
	 * This saves the Club object and along with it.. any changes
	 * @return
	 */
	public String save() {  
	        FacesMessage message =  
	            new FacesMessage(FacesMessage.SEVERITY_INFO, "Club " + this.selectedclub.getClubname() + " has been saved", null);  
	        FacesContext.getCurrentInstance().addMessage(null, message);  
	        
			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
	        try {
				this.selectedclub.getLogo().update(db);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        db.free();
	        return "true";
    } 

    public Club getSelectedclub(){
    	return selectedclub;
    }
    
    public void setSelectedclub(Club clubselected){
    	selectedclub=clubselected;
    }


	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * @return the bLogo
	 */
	public StreamedContent getClubLogo() {
		return this.getClubLogo(this.selectedclub);
	}
	
	
	public StreamedContent getClubLogo(Club _cl) {
		 
		boolean bstream = true;
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (_cl == null) {
			bstream = false;
		} else 	if (_cl.getLogo() == null) {
			bstream = false;
		} else if (_cl.getLogo().getMmObject() == null) {
			bstream = false;
		} else { 
			
			//
			// we are good
		}
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
		    // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
			return  new DefaultStreamedContent();
			
		} else {

			if (bstream) return _cl.getLogo().getStreamedContent();
            
			try {
			
			//
			// ok.. through some text up as a defaul ..
			//
				BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);  
	            Graphics2D g2 = bufferedImg.createGraphics();  
	            g2.drawString("NO IMG", 10, 10);  
	            ByteArrayOutputStream os = new ByteArrayOutputStream();  
				ImageIO.write(bufferedImg, "png", os);
				return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");

            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            
            return null;

		}
	}
	
	public StreamedContent getClubLogoByParmId() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String get = context.getExternalContext().getRequestParameterMap().get("target");
	    LOGGER.info("gclbyparm: get =" + get);
	    if (get == null) {
    		return new DefaultStreamedContent();
	    }
    	int id = Integer.parseInt(get);
	    Club myclub  = scaha.findClubByID(id);
		return getClubLogo(myclub);
		
	}

	/**
	 * This sets the call..
	 * @param _id
	 * @return
	 */
	public String setClub(int _id) {
		LOGGER.fine("ClubBean received a Club id of:" + + _id);
		this.selectedclub = scaha.findClubByID(_id);
		LOGGER.fine((this.selectedclub == null ? "setClub found nothing in masterlist!" : "setting to club " + this.selectedclub.getClubname()));
		return "";
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
	 * @return the currentlevel
	 */
	public int getCurrentLevel() {
		return this.currentLevel;
	}
	/**
	 * @param currentlevel the currentlevel to set
	 */
	public void setCurrentLevel(int currentlevel) {
		this.currentLevel = currentlevel;
	}
	
	/**
 	* This gets the current list of teams given the season setting in the ScahaBean Application Session 
 	* 
	*/
	public TeamList getTeams() {

		if (selectedclub == null) return null;
		
		if (selectedclub.getTeams() != null) return selectedclub.getTeams();

		// lets refresh the team base
		GeneralSeason scahags = scaha.getScahaSeasonList().getCurrentSeason();
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try {
			selectedTeamList = TeamList.NewTeamListFactory(pb.getProfile(), db, this.selectedclub, scahags, true, false);
			selectedclub.setTeams(selectedTeamList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		//
		// we are not going to stitch it to the Club just yet.. its a question of needing to at the moment.
		// do we want to get all teams in memory over time as people look?
		//
		// no.. 
		return selectedTeamList;
	}
}
