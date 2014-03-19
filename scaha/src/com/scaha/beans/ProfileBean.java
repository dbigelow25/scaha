package com.scaha.beans;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Profile;
import com.scaha.objects.Role;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


/**
 * LoginBean.java
 * 
 */

public class ProfileBean implements Serializable,  MailableObject  { 

	//
	// Class Level Variables
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private String name = null;
    private String live_password = null;  // This is the live password they used to login..
	private Profile pro = null;  // This holds all the information regarding a person in the system
	private String origin = null;
	
	private String cur_password = null;   // Holds the screen info for a current password
	private String new_password = null;   // Holds the screen info for the new password
	private String con_password = null;   // Holds the screen info for confirming the new password
	
	
	private boolean EditPerson = false;
	private boolean EditPassword = false;
	private boolean EditMember = false;
	private boolean AddMember = false;
	
	//
	// Very archaic way to track changes.. 
	// profiles are tricky because they are rarely changed..
	//
	private String  chgFirstName = null;
	private String  chgLastName = null;
	private String  chgNickName = null;
	private String  chgAltEmail = null;
	private String  chgPassword = null;
	private String  chgPhone = null;
	private String  chgAddress= null;
	private String  chgCity= null;
	private String  chgState= null;
	private String  chgZip= null;
	private String  chgDOB= null;
	private String  chgGender= null;
	private String  currentUSAHockeySeason = "Needs call to DB";
	private String  currentSCAHAHockeySeason = "Needs call to DB";
	
	
	/**
	 * 
	 */
	public ProfileBean () {
		
		
	}
    public String getName ()
    {
        return name;
    }


    public void setName (final String name)
    {
        this.name = name;
    }

    public String getPassword ()
    {
        return live_password;
    }

    public void setPassword (final String password)
    {
        this.live_password = password;
    }
    
    public Profile getProfile() {
    	return pro;
    }
    
    public String getNickName() {
    	if (pro == null) return "Not Logged In";
    	return pro.getNickName();
    }

    public String getFirstName() {
    	if (pro == null) return "Not Logged In";
    	return pro.getPerson().getsFirstName();
    }

    public String getLastName() {
    	if (pro == null) return "Not Logged In";
    	return pro.getPerson().getsLastName();
    }

 

    public void login() {

    	
    	pro = Profile.verify(name, live_password);
		
    	// pull profile into the Login Bean..
    	try {
	    	if (pro != null) {
	    		
	    		//
	    		// Here we want to default some session stuff right here.
	    		// What is the current USAHockey Season..
	    		//
	    		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
	    		try{
	    		
	    			if (db.getData("call scaha.getActiveMemberShipByType('USAH')")) {
	    				while (db.getResultSet().next()) {
	    					this.setCurrentUSAHockeySeason(db.getResultSet().getString(2));
	    				}
	    			}

	    			if (db.getData("call scaha.getActiveMemberShipByType('SCAHA')")) {
	    				while (db.getResultSet().next()) {
	    					this.setCurrentSCAHAHockeySeason(db.getResultSet().getString(2));
	    				}
	    			}
  			
	    			
	    			db.free();
	    		} catch (SQLException ex) {
	    			ex.printStackTrace();
	    			db.free();
	    		}
	    		
	    				
    			if (origin != null) {
        			FacesContext.getCurrentInstance().getExternalContext().redirect(origin);
    			}
    			
    		} else {
    			FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Invalid Login!",
                        "Please Try Again!"));

    			// blank out the password
    			live_password = null;
    			
	    					
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    }
    
    
    public void verifyUserLogin(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			if(pro == null){
				this.origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
				context.getExternalContext().redirect("Welcome.xhtml");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
    
    public String logout() {
    	this.pro = null;
    	this.live_password = null;
    	this.origin = null;
    	
    	//
    	// Lets reset the bean here..
    	// maybe we distroy the bean when done?
    	//
    	this.setNotEditPassword();
    	this.setNotEditPerson();
    	
    	//
    	// Redirect to the Login Screen
    	//
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Welcome.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";
     }
    
    public String getCompleteName() {
    	
    	if (pro != null)  {
    		return pro.getPerson().getsFirstName() + " " + pro.getPerson().getsLastName();
    	} else {
    		return "No Name Found!";
    	}
    }
    
    public String getAltemail () {
    	if (pro != null)  {
    		return pro.getPerson().getsEmail();
    	} else {
    		return "No Name Found!";
    	}
    	
    }
    
 public String getCompleteAddress() {
    	
    	if (pro != null)  {
    		return pro.getPerson().getsAddress1()  + ", " + pro.getPerson().getsCity() + ", " + pro.getPerson().getsState() + " " + pro.getPerson().getiZipCode();
    	} else {
    		return "No Address Found!";
    	}
    }
 
 public String getUserName() {
 	if (pro != null)  {
		return pro.getUserName();
 	} else {
		return "No UserName Found!";
	}
	 
 }
 
 public String getPhoneNumber() {
 	if (pro != null)  {
		return pro.getPerson().getsPhone();
 	} else {
		return "No Phone Found!";
	}
 }
 
 public String getAddress() {
 	if (pro != null)  {
		return pro.getPerson().getsAddress1();
 	} else {
		return "No Address Found!";
	}
 }

 public String getCity() {
 	if (pro != null)  {
		return pro.getPerson().getsCity();
 	} else {
		return "No City Found!";
	}
 }
 
 public String getState() {
 	if (pro != null)  {
		return pro.getPerson().getsState();
 	} else {
		return "No State Found!";
	}
 }

 public String getGender() {
 	if (pro != null)  {
		return String.valueOf(pro.getPerson().getGender());
 	} else {
		return "U";
	}
 }
 
 public String getDOB () {
 	if (pro != null)  {
		return String.valueOf(pro.getPerson().getDob());
 	} else {
		return "1965/07/14";
	}
 }

 
 public String getZipCode() {
 	if (pro != null)  {
		return String.valueOf(pro.getPerson().getiZipCode());
 	} else {
		return "NOTFOUND";
	}
 }

 public String getPhone() {
	 	if (pro != null)  {
			return pro.getPerson().getsPhone();
	 	} else {
			return "No Phone Found!";
		}
	 }

 public List<FamilyMember> getFamilyMembers() {
	 if (pro != null) {
		 return pro.getPerson().getFamily().getFamilyMembers();
	 } 
	 return null;
 }
 
public List<Role> getRoles() {
	 return pro.getRoles();
 }


/**
 * @return the editPerson
 */
public boolean isEditPerson() {
	return EditPerson;
}


/**
 * @return 
 */
public boolean isAddMember() {
	return AddMember;
}

/**
 * @return 
 */
public boolean isEditMember() {
	return EditMember;
}


/**
 * @return the editPerson
 */
public boolean isViewPerson() {
	return !EditPerson && !EditPassword;
}

/**
 * @return the editPerson
 */
public boolean isViewMembers() {
	return !EditMember && !AddMember;
}


/**
 * @return the editPerson
 */
public boolean isEditPassword() {
	return EditPassword;
}


/**
 * @param editPerson the editPerson to set
 */
public void setEditPerson() {
	LOGGER.info("About to edit person information..");
	//
	// Initialize everything
	//
	this.chgAddress = this.getAddress();
	this.chgFirstName = this.getFirstName();
	this.chgLastName = this.getLastName();
	this.chgAltEmail = this.getAltemail();
	this.chgNickName = this.getNickName();
	this.chgCity = this.getCity();
	this.chgState = this.getState();
	this.chgPhone = this.getPhone();
	this.chgZip = this.getZipCode();
	this.chgDOB = this.getDOB();
	this.chgGender = this.getGender();
	
	EditPerson = true;
	EditPassword = false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotEditPerson() {
	EditPerson = false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setEditPassword() {
	LOGGER.info("About to edit password information..");
	
	EditPassword = true;
	EditPerson = false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotEditPassword() {
	EditPassword =false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setEditMembers() {
	LOGGER.info("About to edit Member information..");
	
	EditMember = true;
	AddMember = false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotEditMembers() {
	EditMember =false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setAddMembers() {
	LOGGER.info("About to Add Member information..");
	
	AddMember = true;
	EditMember = false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotAddMembers() {
	AddMember =false;
	EditMember = false;
}

public void cancelAddMember() {

	this.setNotAddMembers();
	FacesContext context = FacesContext.getCurrentInstance();
	Application app = context.getApplication();

	ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
			"#{usahBean}", Object.class );
	UsaHockeyBean usah = (UsaHockeyBean) expression.getValue( context.getELContext() );
	usah.reset();
	
}



	/**
	 * here we want to change complete the person info changes
	 * 
	 * They can change: 
	 * 
	 * 		Here is what they can change
	 * 			1) First Name
	 * 			2) Last Name
	 * 			3) Phone Number
	 * 			4) alternate e-mail
	 * 			5) Address
	 * 
	 */
	public void updatePersonInfo() {
	
		//
		// in the end.. we simply turn off the edit so that edit screen will dissappear
		
		if (!this.chgDOB.isEmpty()) pro.getPerson().setDob(this.chgDOB);
		if (!this.chgGender.isEmpty()) pro.getPerson().setGender(this.chgGender);
		if (!this.chgAddress.isEmpty()) pro.getPerson().setsAddress1(this.chgAddress);
		if (!this.chgCity.isEmpty()) pro.getPerson().setsCity(this.chgCity);
		if (!this.chgState.isEmpty()) pro.getPerson().setsState(this.chgState);
		if (!this.chgZip.isEmpty()) pro.getPerson().setiZipCode(Integer.parseInt(this.chgZip));
		if (!this.chgFirstName.isEmpty()) pro.getPerson().setsFirstName(this.chgFirstName);
		if (!this.chgLastName.isEmpty()) pro.getPerson().setsLastName(this.chgLastName);
		if (!this.chgNickName.isEmpty()) pro.setNickName(this.chgNickName);
		if (!this.chgAltEmail.isEmpty()) pro.getPerson().setsEmail(this.chgAltEmail);
		if (!this.chgPhone.isEmpty()) pro.getPerson().setsPhone(this.chgPhone);
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{
	
			if (db.setAutoCommit(false)) {
			
				pro.update(db);
				pro.getPerson().update(db);
				db.commit();
				db.free();
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Successful", "Your Changes have been successfully posted and saved..."));  
				LOGGER.info("HERE IS WHERE WE SAVE EVERYTHING COLLECTED FROM the manage Profile Page..");
				LOGGER.info("Sending Test mail here...");
				//SendMailSSL mail = new SendMailSSL(this);
				//LOGGER.info("Finished creating mail object for " + this.getUsername());
				//mail.sendMail();
			}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Profile Change User Attributes PROCESS FOR " + this.getCompleteName());
			e.printStackTrace();
			db.rollback();
			db.free();
		}
	
		
		setNotEditPerson();
		
	}
	
	
	/**
	 * here we want to change complete the person info changes
	 * 
	 * They can change: 
	 * 
	 * 		Here is what they can change
	 * 			1) First Name
	 * 			2) Last Name
	 * 			3) Phone Number
	 * 			4) alternate e-mail
	 * 			5) Address
	 * 
	 */
	public String updatePasswordInfo() {
	
		//
		// in the end.. we simply turn off the edit so that edit screen will dissappear
		//

		if (!this.new_password.equals(this.con_password)) {
					
			FacesContext.getCurrentInstance().addMessage(
					"password:con-pass",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Change Password Error",
                    "new passwords does not match... Please Try Again!"));
			return "fail";
		}

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{
			pro.setLivePassword(this.new_password);
			pro.update(db);
			this.setLive_password(this.new_password);
			db.free();

			LOGGER.info("HERE IS WHERE WE SAVE EVERYTHING COLLECTED FROM the manage Profile Page..");
			LOGGER.info("Sending Test mail here...");
			SendMailSSL mail = new SendMailSSL(this);
			LOGGER.info("Finished creating mail object for " + pro.getUserName());
			mail.sendMail();
			
			//
			// This keeps the message alive between redirects!
			//
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);			
			

			context.addMessage(
					null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO,
	                "Change Password Success",
	                "You have successfully changed your password.  You will be receiving a confirmation e-mail shortly"));
					
			return "Welcome.xhtml?faces-redirect=true";
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			LOGGER.info("ERROR IN Profile Change User Attributes PROCESS FOR " + this.getCompleteName());
            FacesContext context = FacesContext.getCurrentInstance();  
            context.addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR,"SQL Error", "There was an SQL Error please try again"));  
			e.printStackTrace();
			db.rollback();
			db.free();
		}
		
		return "fail";
		
		
	}

/**
 * here we want to simply change the password
 * 
 */
public void updatePassword() {

	//
	// in the end.. we simply turn off the edit so that edit screen will dissappear
	//
	setNotEditPassword();
	
}


/**
 * @return the chgFirstName
 */
public String getChgFirstName() {
	return chgFirstName;
}


/**
 * @param chgFirstName the chgFirstName to set
 */
public void setChgFirstName(String chgFirstName) {
	this.chgFirstName = chgFirstName;
}


/**
 * @return the chgLastName
 */
public String getChgLastName() {
	return chgLastName;
}


/**
 * @param chgLastName the chgLastName to set
 */
public void setChgLastName(String chgLastName) {
	this.chgLastName = chgLastName;
}


/**
 * @return the chgNickName
 */
public String getChgNickName() {
	return chgNickName;
}


/**
 * @param chgNickName the chgNickName to set
 */
public void setChgNickName(String chgNickName) {
	this.chgNickName = chgNickName;
}


/**
 * @return the chgAltEmail
 */
public String getChgAltEmail() {
	return chgAltEmail;
}


/**
 * @param chgAltEmail the chgAltEmail to set
 */
public void setChgAltEmail(String chgAltEmail) {
	this.chgAltEmail = chgAltEmail;
}


/**
 * @return the chgPassword
 */
public String getChgPassword() {
	return chgPassword;
}


/**
 * @param chgPassword the chgPassword to set
 */
public void setChgPassword(String chgPassword) {
	this.chgPassword = chgPassword;
}


/**
 * @return the chgPhone
 */
public String getChgPhone() {
	return chgPhone;
}


/**
 * @param chgPhone the chgPhone to set
 */
public void setChgPhone(String chgPhone) {
	this.chgPhone = chgPhone;
}


/**
 * @return the chgAddress
 */
public String getChgAddress() {
	return chgAddress;
}


/**
 * @param chgAddress the chgAddress to set
 */
public void setChgAddress(String chgAddress) {
	this.chgAddress = chgAddress;
}


/**
 * @return the chgCity
 */
public String getChgCity() {
	return chgCity;
}


/**
 * @param chgCity the chgCity to set
 */
public void setChgCity(String chgCity) {
	this.chgCity = chgCity;
}


/**
 * @return the chgState
 */
public String getChgState() {
	return chgState;
}


/**
 * @param chgState the chgState to set
 */
public void setChgState(String chgState) {
	this.chgState = chgState;
}


/**
 * @return the chgZip
 */
public String getChgZip() {
	return chgZip;
}


/**
 * @param chgZip the chgZip to set
 */
public void setChgZip(String chgZip) {
	this.chgZip = chgZip;
}


/**
 * @return the chgDOB
 */
public String getChgDOB() {
	return chgDOB;
}


/**
 * @param chgDOB the chgDOB to set
 */
public void setChgDOB(String chgDOB) {
	this.chgDOB = chgDOB;
}


/**
 * @return the chgGender
 */
public String getChgGender() {
	return chgGender;
}


/**
 * @param chgGender the chgGender to set
 */
public void setChgGender(String chgGender) {
	this.chgGender = chgGender;
}


/**
 * @return the cur_password
 */
public String getCur_password() {
	return cur_password;
}


/**
 * @param cur_password the cur_password to set
 */
public void setCur_password(String cur_password) {
	this.cur_password = cur_password;
}


/**
 * @return the new_password
 */
public String getNew_password() {
	return new_password;
}


/**
 * @param new_password the new_password to set
 */
public void setNew_password(String new_password) {
	this.new_password = new_password;
}


/**
 * @return the con_passwrod
 */
public String getCon_password() {
	return con_password;
}


/**
 * @param con_passwrod the con_passwrod to set
 */
public void setCon_password(String con_password) {
	this.con_password = con_password;
}


/**
 * @return the live_password
 */
public String getLive_password() {
	return live_password;
}


/**
 * @param live_password the live_password to set
 */
public void setLive_password(String live_password) {
	this.live_password = live_password;
}
 

/**
 *  Will need to be able to understand that overtime this has to know the context of the mailing
 *  
 */
public String getSubject()  {
	//
	// right now.. we just mail for a password change..
	//
	return "An account message from iscaha";
	
}
public String getTextBody() {
	return "You have recently changed your password.  If this was not you.. please get in contact with a SCAHA rep ASAP!!";
}

public String getPreApprovedCC() {
	return "";
}

public String getToMailAddress() {
	return this.pro.getUserName();
}


/**
 * @return the currentUSAHockeySeason
 */
public String getCurrentUSAHockeySeason() {
	return currentUSAHockeySeason;
}

/**
 * @param currentUSAHockeySeason the currentUSAHockeySeason to set
 */
public void setCurrentUSAHockeySeason(String currentUSAHockeySeason) {
	this.currentUSAHockeySeason = currentUSAHockeySeason;
}
/**
 * @return the currentSCAHAHockeySeason
 */
public String getCurrentSCAHAHockeySeason() {
	return currentSCAHAHockeySeason;
}
/**
 * @param currentSCAHAHockeySeason the currentSCAHAHockeySeason to set
 */
public void setCurrentSCAHAHockeySeason(String currentSCAHAHockeySeason) {
	this.currentSCAHAHockeySeason = currentSCAHAHockeySeason;
}


}