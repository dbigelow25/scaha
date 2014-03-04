package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.gbli.common.SendMailSSL;
import com.gbli.common.USAHRegClient;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
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
					null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "USA Hockey Info Not Found",
                    "Could Not find any USA Registration based upon the number you provided! Please check to see if your USA Hockey#."));
			}
			if (!this.dob.equals(usar.getDOB())) {
				FacesContext.getCurrentInstance().addMessage(
					null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Date of Birth Match", usar.getDOB() + ":" + this.dob + "!.  The DOB you provided does not match what was pulled from USA Hockey.  Please check to see if your USA Hockey # and/or DOB is correct."));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	

}
