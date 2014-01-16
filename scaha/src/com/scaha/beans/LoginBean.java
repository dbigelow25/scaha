package com.scaha.beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

/**
 * LoginBean.java
 * 
 */

public class LoginBean
{
    private String name = null;
    private String password = null;
	private Boolean isAuth = false;
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
    
    public void login() {

    	ContextManager.getLoggerContext();
    	ScahaDatabase mydb = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase"); 
    	
    	mydb.free();
    	
    	// pull profile into the Login Bean..
    	

    	try {
    		if (name.equals("dave")) {
    			this.isAuth = true;
    			if (origin != null) {
        			FacesContext context = FacesContext.getCurrentInstance();
        			FacesContext.getCurrentInstance().getExternalContext().redirect(origin);
//    				origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
    			}
    			
    		} else {
    			FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Invalid Login!",
                        "Please Try Again!"));

    			name = null;
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
			if(!isAuth){
				this.origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
				context.getExternalContext().redirect("Welcome.xhtml");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
    
    public String logout() {
    	this.name = null;
    	this.password = null;
    	this.isAuth = false;
    	this.origin = null;
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Welcome.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return "login";
     }
}