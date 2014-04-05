package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyRow;
import com.scaha.objects.Team;

//import com.gbli.common.SendMailSSL;


public class adminnavigationBean implements Serializable {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private Boolean clubregistrar = null;
	private Boolean scaharegistrar = null;
	private String pagecode = null;
	
    public adminnavigationBean() {  
        //need to get the roles and setup the navigation permissions
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
    	setRoleflags(pb);

    	
    	
    	
    }  
 
    public void setPagecode(String scode){
    	pagecode = scode;
    }
    
    public String getPagecode(){
    	return pagecode;
    }
    
    public void setRoleflags(ProfileBean pb){
    	
    	//set initial values to false
    	setClubregistrar(false);
    	
    	//iterate thru the roles
    	for (int i = 0; i < pb.getRoles().size()-1; i++) {
	    	
    		if (!(pb.getRoles().get(i).getName()==null)){
    			if (pb.getRoles().get(i).getName().equals("C-REG")){
    	    		setClubregistrar(true);
    	    	}
    			if (pb.getRoles().get(i).getName().equals("S-REG")){
    	    		setScaharegistrar(true);
    	    	}
    		}
    		
		}
    }
    
    public void setClubregistrar(Boolean flag){
    	clubregistrar = flag;
    }
    
    public Boolean getClubregistrar(){
    	return clubregistrar;
    }
    
    public void setScaharegistrar(Boolean flag){
    	scaharegistrar = flag;
    }
    
    public Boolean getScaharegistrar(){
    	return scaharegistrar;
    }
    
    public void addTeam(){
        
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("addteams.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void addLoi(){
    
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("addplayerstoteam.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void addcoachLoi(){
        
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("addcoachestoteam.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void clubreviewLoi(){
        
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("registrarviewlois.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void confirmLoi(){
        
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("confirmlois.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void confirmcoachLoi(){
        
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("confirmcoachlois.xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void viewDelinquency(){
        
    	//need to reload delinquency list before redirecting due to session object issues.
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{delinquencyBean}", Object.class );

		delinquencyBean db = (delinquencyBean) expression.getValue( context.getELContext() );
    	db.playersDisplay();

    	try{
    		context.getExternalContext().redirect(this.loadPageCode() + ".xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	    	
    }
    
    public void loadNavigation(){
        
    	//need to reload player and coaches loi list before redirecting due to session object issues.
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{reviewloiBean}", Object.class );

		reviewloiBean lb = (reviewloiBean) expression.getValue( context.getELContext() );
    	lb.setSelectedclub("0");
    	lb.setSelectedplayerid("0");
    	lb.setSelectedtabledisplay("0");
    	lb.playersDisplay();
		
    	expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{reviewcoachloiBean}", Object.class );

		reviewcoachloiBean clb = (reviewcoachloiBean) expression.getValue( context.getELContext() );
    	clb.setSelectedclub("0");
    	clb.setSelectedtabledisplay("0");
    	clb.coachesDisplay();
    	
    	expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{draftplayersBean}", Object.class );

    	DraftPlayersBean dpb = (DraftPlayersBean) expression.getValue( context.getELContext() );
    	dpb.setSearchcriteria("");
    	dpb.playerSearch();
    	
    	expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{draftcoachesBean}", Object.class );

    	DraftCoachesBean dcb = (DraftCoachesBean) expression.getValue( context.getELContext() );
    	dcb.setSearchcriteria("");
    	dcb.coachSearch();
    	
    	
    	try{
    		context.getExternalContext().redirect(this.loadPageCode() + ".xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	    	
    }
    
        	    	
    private String loadPageCode(){
    	//get page to navigate to
    	HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("pagecode") != null)
        {
    		pagecode = hsr.getParameter("pagecode").toString();
        }
    	
    	return pagecode;
    }
    
    public void reviewreleaseNavigation(){
        
    	//need to reload player release list before redirecting due to session object issues.
    	FacesContext context = FacesContext.getCurrentInstance();
    	Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{playerreleaseBean}", Object.class );

		playerreleaseBean prb = (playerreleaseBean) expression.getValue( context.getELContext() );
    	prb.setSearchcriteria("");
    	prb.playerSearch();
    	
    	try{
    		context.getExternalContext().redirect(this.loadPageCode() + ".xhtml");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	    	
    }

}