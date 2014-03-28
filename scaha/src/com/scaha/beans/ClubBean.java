package com.scaha.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.sql.rowset.serial.SerialBlob;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;

@ManagedBean
@SessionScoped
public class ClubBean implements Serializable,  MailableObject {
	
	private byte[] logo = null;
	
	private UploadedFile uploadedLogo;
	
	private StreamedContent scLogo;
	
	
	private ClubList ScahaClubList  = null;
	private Club selectedclub = null;
	private int currentLevel = 1; 

	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	//
	
	//
	// lets go get it!
	//
	public ClubBean() {

		if (ScahaClubList == null) {

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try {
			ScahaClubList = ClubList.NewClubListFactory(new Profile(0), db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		}

	}
	
	public void handleFileUpload(FileUploadEvent event) {
	    this.uploadedLogo = event.getFile();
	    logo = event.getFile().getContents();
	    LOGGER.info("GET THE INFO:" + event.getFile().getFileName() + ":" + event.getFile().getSize() +  ":" + uploadedLogo.getFileName().substring(uploadedLogo.getFileName().lastIndexOf(".")+1));
		try {
			setSCLogo(new DefaultStreamedContent(uploadedLogo.getInputstream(),"image/" + uploadedLogo.getFileName().substring(uploadedLogo.getFileName().lastIndexOf("."))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the content
	 */
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * @param content the content to set
	 */
	public void setLogo(byte[] content) {
		this.logo = content;
	}	
	
	
	public String save(Club _club) {  
	        FacesMessage message =  
	            new FacesMessage(FacesMessage.SEVERITY_INFO, "Club " + _club.getClubname() + " has been saved", null);  
	        FacesContext.getCurrentInstance().addMessage(null, message);  
	        updateLogo(_club);
	        
	        return "true";
    } 

	public String updateLogo(Club _club) {

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");

		try {
				CallableStatement cs = db.prepareCall("call scaha.updateMultiMedia(?,?,?,?,?,?,?)");
			int i=1;
			cs.setInt(i++, _club.ID);
			cs.setString(i++, "CLUB");
			cs.setString(i++, "LOGO");
			cs.setString(i++, "png");
			cs.setInt(i++,1);
			cs.setString(i++,null);
			cs.setBlob(i++, new SerialBlob(this.logo));
			cs.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
	
		return "True";
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
	 * @return the uploadedLogo
	 */
	public UploadedFile getUploadedLogo() {
		return uploadedLogo;
	}

	/**
	 * @param uploadedLogo the uploadedLogo to set
	 */
	public void setUploadedLogo(UploadedFile uploadedLogo) {
		this.uploadedLogo = uploadedLogo;
	}

	/**
	 * @return the bLogo
	 */
	public StreamedContent getSCLogo() {
		LOGGER.info("HERE IS CLUB:" + this.selectedclub.getClubname() + ":" + this.selectedclub.getBlogo().length);
		try {
			InputStream is = new ByteArrayInputStream(selectedclub.getBlogo());
			setSCLogo(new DefaultStreamedContent(is, selectedclub.getLogoextension()));
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scLogo;
	}

	/**
	 * @param bLogo the bLogo to set
	 */
	public void setSCLogo(StreamedContent bLogo) {
		this.scLogo = bLogo;
	}
	public String setClub(Club _cl) {
		this.selectedclub = _cl;
		return _cl.getClubname();
	}
    
}
