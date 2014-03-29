package com.scaha.beans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;

@ManagedBean
@ApplicationScoped
public class ClubBean implements Serializable,  MailableObject {
	
	
	private ClubList ScahaClubList  = null;
	private Club selectedclub = null;
	private int currentLevel = 1; 

	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// lets go get it!
	//
	public ClubBean() {
		
		if (ScahaClubList == null) refreshBean();
		
	}
	
	public void handleFileUpload(FileUploadEvent event) {
	    this.selectedclub.getLogo().setMmObject(event.getFile().getContents());
	    this.selectedclub.getLogo().setExtension(event.getFile().getContentType());
	    LOGGER.info("GET THE INFO:" + event.getFile().getFileName() + ":" + event.getFile().getSize());
	}

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
    /**
	 * @return the clubList
	 */
	public ClubList getScahaClubList() {
		
			return ScahaClubList;
	}

	/**
	 * @param clubList the clubList to set
	 */
	public void setScahaClubList(ClubList clubList) {
		ScahaClubList = clubList;
	}

	/**
	 * @return the currentLevel
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * @param currentLevel the currentLevel to set
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
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
	    
	    if (get == null) {
    		return new DefaultStreamedContent();
	    }
    	int id = Integer.parseInt(get);
	    Club myclub  = this.findClubByID(id);
		return getClubLogo(myclub);
		
	}
	
	
	public Club findClubByID (int _id) {
		for (Club c : ScahaClubList) {
			if (c.ID == _id) { 
				return c;
			}
		}
		return null;
	}
	
	public String setClub(Club _cl) {
		
		if (ScahaClubList == null) {
			this.selectedclub = _cl;
			return "";
		}

		for (Club c : ScahaClubList) {
			if (c.ID == _cl.ID) { 
				this.selectedclub = c;
				break;
			}
		}
		
		if (this.selectedclub == null) this.selectedclub = _cl;
		
		return "";
	}

	 public long getDate() {
	    return System.currentTimeMillis();
	 }
	 
	 public String refreshBean() {
		 
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try {
			LOGGER.info("REFRESH BEAN DIP TIME");
			ScahaClubList = ClubList.NewClubListFactory(new Profile(0), db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		return "done";
		 
	 }
}
