package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.gbli.common.SendMailSSL;
import com.gbli.common.USAHRegClient;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.ScahaPlayer;
import com.scaha.objects.UsaHockeyRegistration;

public class UsaHockeyBean implements Serializable, MailableObject {
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	/**
	 * 
	 */
	
	private UsaHockeyRegistration usar = null;
	private String regnumber = null;
	private String dob = null;
	private String membertype = null;
	private String relationship = null;
	private boolean datagood = false;
	
	/**
	 * @return the usar
	 */
	public UsaHockeyRegistration getUsar() {
		return usar;
	}

	/**
	 * @param usar the usar to set
	 */
	public void setUsar(UsaHockeyRegistration usar) {
		this.usar = usar;
	}

	
	/**
	 * @return the regnumber
	 */
	public String getRegnumber() {
		return regnumber;
	}

	/**
	 * @param regnumber the regnumber to set
	 */
	public void setRegnumber(String regnumber) {
		this.regnumber = regnumber;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	
	/**
	 * 
	 * @return
	 */
	public String fetchUSAHockey() {
		
		//
		// clear it out first!!
		//
		this.usar = null;
		try {
			
			this.usar = USAHRegClient.pullRegistartionRecord(this.getRegnumber());

			if (usar.getFirstName().length()== 0) {
				FacesContext.getCurrentInstance().addMessage(
					"mp-form:usah-reg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "USA Hockey Info Not Found",
                    "Could Not find the USA Registration record."));
				return "false";
			} 
			if (!this.dob.equals(usar.getDOB())) {
				FacesContext.getCurrentInstance().addMessage(
					"mp-form:usah-dob",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Date of Birth Match", "The DOB you provided does not match what was pulled."));
					this.usar = null;
				return "false";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(
				"mp-form:finish-add-mem",
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Successfull Lookup",
                "Data looks good, CLICK Save Now to create a new Member."));
		this.datagood = true;
		
		//
		// We need to set Member Type to Manager.. because thats what the XX is for
		if (this.usar.getUSAHnum().contains("XX")) {
			this.membertype = "Manager";
		}
		return "true";
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		//return this.email + "," + ((this.altemail == null || this.altemail.isEmpty()) ? "" : this.altemail);
		return "";
		
	}

	/**
	 * @return the membertype
	 */
	public String getMembertype() {
		return membertype;
	}

	/**
	 * @param membertype the membertype to set
	 */
	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

	/**
	 * @return the regtype
	 */
	public String getRelationship() {
		return relationship;
	}

	/**
	 * @param regtype the regtype to set
	 */
	public void setRelationship(String regtype) {
		this.relationship = regtype;
	}
	

	/**
	 * @return the datagood
	 */
	public boolean isDatagood() {
		return datagood;
	}

	/**
	 * @param datagood the datagood to set
	 */
	public void setDatagood(boolean datagood) {
		this.datagood = datagood;
	}

	/**
	 * This guy adds a new member via a USAHockey pull request...
	 * @return
	 */
	public String addNewMember() {
		
		//
		// OK.. We know this guy is a new person.. but it is a family member first and forement...
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();

		ValueExpression expression = app.getExpressionFactory().createValueExpression( context.getELContext(),
				"#{profileBean}", Object.class );

		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
		
		ScahaPlayer sp = new ScahaPlayer(pb.getProfile());
		
		sp.gleanUSAHinfo(this.usar);

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		try{
			sp.update(db);
			usar.update(db, sp);
			db.free();
            context.addMessage(null, new FacesMessage("Successful", "I think we saved something.."));  
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return "true";

	}
	
	public void reset() {
		LOGGER.info("usaHockeyBean:reseting member variables...");
		usar = null;
		regnumber = null;
		dob = null;
		membertype = null;
		relationship = null;
		datagood = false;
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UsaHockeyBean [usar=" + usar + ", regnumber=" + regnumber
				+ ", dob=" + dob + ", membertype=" + membertype
				+ ", relationship=" + relationship + "]";
	}

}
