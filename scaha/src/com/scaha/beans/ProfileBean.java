package com.scaha.beans;

import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.Profile;
import com.scaha.objects.Role;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
    
    public void login() {

    	
    	pro = Profile.verify(name, password);
		
    	// pull profile into the Login Bean..
    	try {
	    	if (pro != null) {
    			if (origin != null) {
        			//FacesContext context = FacesContext.getCurrentInstance();
        			FacesContext.getCurrentInstance().getExternalContext().redirect(origin);
//    				origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
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
    	// Redirect to the Login Screen
    	//
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Welcome.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return "login";
     }
    
    public String getCompleteName() {
    	
    	if (pro != null)  {
    		return pro.getPerson().getsFirstName() + " " + pro.getPerson().getsLastName();
    	} else {
    		return "No Name Found!";
    	}
    }
    
 public String getCompleteAddress() {
    	
    	if (pro != null)  {
    		return pro.getPerson().getsAddress1()  + " " + pro.getPerson().getsAddress2() +", " + pro.getPerson().getsCity() + ", " + pro.getPerson().getsState() + " " + pro.getPerson().getiZipCode();
    	} else {
    		return "No Address Found!";
    	}
    }
 
 public String getEmail() {
 	if (pro != null)  {
		return pro.getPerson().getsEmail();
 	} else {
		return "No Address Found!";
	}
	 
 }
 
 public String getPhoneNumber() {
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
 
}