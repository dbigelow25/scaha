package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;

public class ForgotPasswordBean implements Serializable, MailableObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private String email = null;
	private String password = null;
	private String altemail = null;
	
	public String sendPassword() {
		
		// Lets check how unique and clean the data is first.
		// if we pass everything.. then we can send the e-mail...
		
		// we will need to fish out the password from the table ...
		//
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			Vector<String> v = new Vector<String>();
			v.add(this.email);
			db.getData("CALL scaha.checkforuser(?)", v);
		        
			if (db.getResultSet() != null && db.getResultSet().next()){
				if (db.getResultSet().getInt(1) == 0) {
					// We already have this username.. 
					// we need to exit and fill the message in the context on the screen.
					return "False";
				}
			}

			db.cleanup();
			v.clear();
			v.add(this.email);
			db.getData("CALL scaha.getPasswordByUser(?)", v);
			if (db.getResultSet() != null && db.getResultSet().next()){
				this.password = db.getResultSet().getString(1);  // Password 
				this.altemail = db.getResultSet().getString(2);  // Alt-email 
			} else { 
				return "False";
			}
			SendMailSSL mail = new SendMailSSL(this);
			mail.sendMail();
			return "True";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("ERROR IN SEND PASSWORD PROCESS FOR " + this.getEmail());
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.free();
		}
		
		return "False";

	}
	
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return "SCAHA iSite Password Reminder";
	}
	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return "Your password is " + this.password;
	}
	
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return this.email + "," + ((this.altemail == null || this.altemail.isEmpty()) ? "" : this.altemail);
	}
	

}