package com.scaha.beans;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.Role;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import com.scaha.objects.Profile;

/**
 * LoginBean.java
 * 
 */

public class ProfileBean implements Serializable  { 

	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	private String name = null;
    private String password = null;
	private Profile pro = null;  // This holds all the information regarding a person in the system
	private String origin = null;
	
	private boolean EditPerson = false;
	private boolean EditPassword = false;

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
        return password;
    }

    public void setPassword (final String password)
    {
        this.password = password;
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

    	
    	pro = Profile.verify(name, password);
		
    	// pull profile into the Login Bean..
    	try {
	    	if (pro != null) {
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
    			password = null;
    			
	    					
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
    	this.password = null;
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
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotEditPerson() {
	EditPerson =false;
}

/**
 * @param editPerson the editPerson to set
 */
public void setEditPassword() {
	LOGGER.info("About to edit password information..");
	
	//
	// Initialize everything
	//
	this.chgAddress = this.getAddress();
	this.chgFirstName = this.getFirstName();
	this.chgLastName = this.getLastName();
	this.chgAltEmail = this.getAltemail();
	this.chgCity = this.getCity();
	this.chgState = this.getState();

	EditPassword = true;
}

/**
 * @param editPerson the editPerson to set
 */
public void setNotEditPassword() {
	EditPerson =false;
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
 
}