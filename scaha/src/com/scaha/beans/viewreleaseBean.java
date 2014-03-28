package com.scaha.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Status;

//import com.gbli.common.SendMailSSL;


public class viewreleaseBean implements Serializable, MailableObject {

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	transient private ResultSet rs = null;
	private String type = null;
	private String firstname = null;
	private String lastname = null;
	private String dob = null;
	private String usanumber = null;
	private String parentname = null;
	private String parentemail = null;
	private String reason = null;
	private String suspension = null;
	private String beginningdate = null;
	private String endingdate = null;
	private String releasingclubdivision = null;
	private String acceptingclub = null;
	private String division = null;
	private String skilllevel = null;
	private String financial = null;
	private String comments = null;
	private String subject = null;
	private String textbody = null;
	private String cc = null;
	private String to = null;
	private List<Status> statuses = null;
	private String note = null;
	private Boolean displaypermanent = null;
	private Boolean displaytemporary = null;
	private Integer releaseid = null;
	private String selectedstatus = null;
	
	
	public viewreleaseBean() {  
        
    	//will need to load player profile information for displaying loi
		HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
    	if(hsr.getParameter("releaseid") != null)
        {
    		releaseid = Integer.parseInt(hsr.getParameter("releaseid").toString());
        }
    	
    	loadreleaseProfile();

    	//doing anything else right here
    }  
	
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}
    
    public void settype(String snum){
    	type = snum;
    }
	
	public String getSelectedstatus() {
		// TODO Auto-generated method stub
		return selectedstatus;
	}
    
    public void setSelectedstatus(String snum){
    	selectedstatus = snum;
    }
	
	
	public Integer getReleaseid() {
		// TODO Auto-generated method stub
		return releaseid;
	}
    
    public void setReleasedid(Integer snum){
    	releaseid = snum;
    }
	
	
	public Boolean getDisplaypermanent() {
		// TODO Auto-generated method stub
		return displaypermanent;
	}
    
    public void setDisplaypermanent(Boolean snum){
    	displaypermanent = snum;
    }
	
    public Boolean getDisplaytemporary() {
		// TODO Auto-generated method stub
		return displaytemporary;
	}
    
    public void setDisplaytemporary(Boolean snum){
    	displaytemporary = snum;
    }
	
    
	public String getComments() {
		// TODO Auto-generated method stub
		return comments;
	}
    
    public void setComments(String snum){
    	comments = snum;
    }
    
    public String getNote() {
		// TODO Auto-generated method stub
		return note;
	}
    
    public void setNote(String snum){
    	note = snum;
    }
    
    
    public String getFinancial() {
		// TODO Auto-generated method stub
		return financial;
	}
    
    public void setFinancial(String snum){
    	financial = snum;
    }
    
    public String getSkilllevel() {
		// TODO Auto-generated method stub
		return skilllevel;
	}
    
    public void setSkilllevel(String snum){
    	skilllevel = snum;
    }
    
	public String getDivision() {
		// TODO Auto-generated method stub
		return division;
	}
    
    public void setDivision(String snum){
    	division = snum;
    }
    
	
	public String getAcceptingclub() {
		// TODO Auto-generated method stub
		return acceptingclub;
	}
    
    public void setacceptingclub(String snum){
    	acceptingclub = snum;
    }
    
	
	public String getReleasingclubdivision() {
		// TODO Auto-generated method stub
		return releasingclubdivision;
	}
    
    public void setReleasingclubdivision(String snum){
    	releasingclubdivision = snum;
    }
    
	
	public String getEndingdate() {
		// TODO Auto-generated method stub
		return endingdate;
	}
    
    public void setEndingdate(String snum){
    	endingdate = snum;
    }
    
	
	public String getBeginningdate() {
		// TODO Auto-generated method stub
		return beginningdate;
	}
    
    public void setBeginningdate(String snum){
    	beginningdate = snum;
    }
    
	
	public String getSuspension() {
		// TODO Auto-generated method stub
		return suspension;
	}
    
    public void setSuspension(String snum){
    	suspension = snum;
    }
    
	public String getReason() {
		// TODO Auto-generated method stub
		return reason;
	}
    
    public void setReason(String snum){
    	reason = snum;
    }
    
	public String getParentemail() {
		// TODO Auto-generated method stub
		return parentemail;
	}
    
    public void setParentemail(String snum){
    	parentemail = snum;
    }
    
	public String getParentname() {
		// TODO Auto-generated method stub
		return parentname;
	}
    
    public void setParentname(String snum){
    	parentname = snum;
    }
    
	
	public String getUsanumber() {
		// TODO Auto-generated method stub
		return usanumber;
	}
    
    public void setUsanumber(String snum){
    	usanumber = snum;
    }
    
	
	public String getSubject() {
		// TODO Auto-generated method stub
		return subject;
	}
    
    public void setSubject(String ssubject){
    	subject = ssubject;
    }
    
	public String getTextBody() {
		// TODO Auto-generated method stub
		return textbody;
	}
	
	public void setTextBody(String stextbody){
		textbody = stextbody;
	}
	
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return cc;
	}
	
	public void setPreApprovedCC(String scc){
		cc = scc;
	}
	
	
	
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return to;
	}
    
    public void setToMailAddress(String sto){
    	to = sto;
    }
	
	public void setDob(String sdob){
    	dob = sdob;
    }
    
    public String getDob(){
    	return dob;
    }
    
    public void setLastname(String lname){
    	lastname = lname;
    }
    
    public String getLastname(){
    	return lastname;
    }
    public void setFirstname(String fname){
    	firstname = fname;
    }
    public String getFirstname(){
    	return firstname;
    }
    	
	public List<Status> getListofStatus(){
		List<Status> templist = new ArrayList<Status>();
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			CallableStatement cs = db.prepareCall("CALL scaha.getReleaseStatus()");
    		    rs = cs.executeQuery();
    			
    			if (rs != null){
    				
    				while (rs.next()) {
    					String idstatus = rs.getString("idreleasestatus");
        				String statuslabel = rs.getString("releaselabel");
        				
        				Status status = new Status();
        				status.setStatusid(idstatus);
        				status.setStatusname(statuslabel);
        				
        				templist.add(status);
    				}
    				LOGGER.info("We have results for status list");
    			}
    			db.cleanup();
    		} else {
    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading clubs");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
		
    	setStatuses(templist);
		
		return getStatuses();
	}
	
	public List<Status> getStatuses(){
		return statuses;
	}
	
	public void setStatuses(List<Status> list){
		statuses = list;
	}
	
		
		
	//used to populate form with release information
	public void loadreleaseProfile(){
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		
    			Vector<Integer> v = new Vector<Integer>();
    			v.add(this.releaseid);
    			db.getData("CALL scaha.getReleaseInfo(?)", v);
    		    
    			if (db.getResultSet() != null){
    				//need to add to an array
    				rs = db.getResultSet();
    				
    				while (rs.next()) {
    					this.firstname = rs.getString("fname");
    					this.lastname = rs.getString("lname");
    					this.dob = rs.getString("dob");
    					this.usanumber = rs.getString("usahockeynumber");
    					this.parentname = rs.getString("parentname");
    					this.parentemail = rs.getString("parentemail");
    					this.releasingclubdivision = rs.getString("releasingclubdivision");
        				this.acceptingclub = rs.getString("acceptingclub");
        				this.beginningdate = rs.getString("beginningdate");
        				this.endingdate = rs.getString("endingdate");
        				this.comments = rs.getString("comments");
        				this.division = rs.getString("division");
        				this.financial = rs.getString("financial");
        				this.reason = rs.getString("reason");
        				this.skilllevel = rs.getString("skilllevel");
        				this.suspension = rs.getString("suspension");
        				this.type = rs.getString("type");
        				
        				if (this.type.equals("Permanent")){
        					this.displaypermanent = true;
        					this.displaytemporary = false;
        				} else {
        					this.displaypermanent = false;
        					this.displaytemporary = true;
        				}
        			}
    				LOGGER.info("We have results for player details by player id");
    			}
    			db.cleanup();
    		
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
	
	public void CloseLoi(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{playerreleaseBean}", Object.class );

		playerreleaseBean dpb = (playerreleaseBean) expression.getValue( context.getELContext());
    	dpb.setSearchcriteria("");
		dpb.playerSearch();

		
		try{
			context.getExternalContext().redirect("startplayerrelease.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void sendRelease(){
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
    	
    	try{

    		if (db.setAutoCommit(false)) {
    		
    			CallableStatement cs = db.prepareCall("CALL scaha.addNotestoRelease(?,?,?)");
    			cs.setInt("releaseid", this.releaseid);
    			cs.setInt("status", Integer.parseInt(this.selectedstatus));
    			cs.setString("notes", this.note);
    			
    			cs.executeQuery();
    			db.commit();
    			db.cleanup();
    			
    			//need to send email to club registrars, family, and scaha registrar
    			//first releasing and accepting club registrar
    			LOGGER.info("Sending email to club registrar, family, and scaha registrar");
    			cs = db.prepareCall("CALL scaha.getClubRegistrarEmailsForRelease(?)");
    		    cs.setInt("releaseid", this.releaseid);
    		    rs = cs.executeQuery();
    		    if (rs != null){
    				while (rs.next()) {
    					to = rs.getString("acceptingemail");
    					if (to!=null){
    						to = to + ',';
    					}else {
    						to = "";
    					}
    					to = to + rs.getString("releasingemail");
    				}
    			}
    		    
    		    
    		    
    		    //
    		    //next scaha registrar
    		    cs = db.prepareCall("CALL scaha.getSCAHARegistrarEmail()");
    		    rs = cs.executeQuery();
    		    if (rs != null){
    				while (rs.next()) {
    					to = to + ',' + rs.getString("usercode");
    				}
    			}
    		    
    		    //and now the family email
    		    to = to + ',' + this.getParentemail();
    		    
    		    this.setToMailAddress(to);
    		    this.setTextBody("Player " + this.firstname + " " + this.lastname + " Notes From SCAHA");
    		    this.setSubject(this.firstname + " " + this.lastname + " Released Notes from SCAHA");
    		    
				/*SendMailSSL mail = new SendMailSSL(this);
				LOGGER.info("Finished creating mail object for " + this.getUsername());
				mail.sendMail();
				db.commit();
				return "True";*/
				
    		    FacesContext context = FacesContext.getCurrentInstance();
	    		try{
					context.getExternalContext().redirect("confirmrelease.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		    
    		} else {
    		
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		LOGGER.info("ERROR IN loading clubs");
    		e.printStackTrace();
    		db.rollback();
    	} finally {
    		//
    		// always clean up after yourself..
    		//
    		db.free();
    	}
	
    	
    	
    }
	
	public void setReleaseType(String releasetype){
		
		if (releasetype.equals("p")){
			this.displaypermanent = true;
			this.displaytemporary = false;
		} else {
			this.displaypermanent = false;
			this.displaytemporary = true;
		}
	}
}

