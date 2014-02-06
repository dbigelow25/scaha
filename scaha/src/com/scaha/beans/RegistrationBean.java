package com.scaha.beans;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.common.SendMailSSL;
import com.gbli.connectors.Database;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.sun.org.apache.bcel.internal.generic.DDIV;

public class RegistrationBean implements Serializable, MailableObject  {
	

	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String username = null;
	private String password = null;
	private String reppassword = null;
	private String firstname = null;
	private String lastname = null;
	private String nickname = null;
	private String phone = null;
	private String email = null;
	private String address = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the reppassword
	 */
	public String getReppassword() {
		return reppassword;
	}
	/**
	 * @param reppassword the reppassword to set
	 */
	public void setReppassword(String reppassword) {
		this.reppassword = reppassword;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	
	/**
	 * 
	 */
	public void setDefaultUserName() {
	    username = email;
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * This method will create all the required database records to create a "registered User".
	 * 
	 * Typically, the database maintenance is done in business objects.. this one touches a few tables..
	 * so we want to make sure its tight...
	 * 
	 */
	public String createRegistration() {
		
		
		//
		// Lets check how unique and clean the data is first.
		// if we pass everything.. then we insert everyrthing..
		
		// The screen checked the uniqueness of the user.. 
		// we just need to check in once again prior to creating the basic profile
		//
		//
		// We create a profile record
		// we create a person record
		// and we create a default roleset record that gives this person the default role of FAMILY
		//
		//
		// Then at the end of this.. we fire off an e-mail.  we have to pull all the profiles that have register admin roles...
		//  and make sure they are in the blind carbon copy section of the e-mail.
		//
		
		//
		// If everything works.. then we return a "True" back.. so the faces can reroute to a succesffully registered page..
		//
		// otherwise.. we send back a false.. which will keep the user parked on the page with an error message.. 
		//
		// we will need the general msg to fill out that there was some sort of error.. and that if it continues.. to call support.
		//
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try{

			if (db.setAutoCommit(false)) {
			
				
				Vector<String> v = new Vector<String>();
				v.add(this.username);
 				db.getData("CALL scaha.checkforuser(?)", v);
			        
 				if (db.getResultSet() != null && db.getResultSet().next()){
					if (db.getResultSet().getInt(1) > 0) {
						// We already have this username.. 
						// we need to exit and fill the message in the context on the screen.
						return "False";
					}
				}
 				
 				db.cleanup();

				//
				// we are good to go here..
				//

				Profile pro = new Profile(this.username,this.password, this.nickname);
				Person per = new Person(0,pro);
				per.setsAddress1(this.address);
				per.setsFirstName(this.firstname);
				per.setsCity(city);
				per.setsState(this.state);
				per.setiZipCode(Integer.valueOf(this.zip));
				per.setsPhone(this.phone);
				per.setsEmail(this.email);
				pro.update(db);
				//per.update(db);
				
				// We want to create a family called the <lastname> family...
				LOGGER.info("HERE IS WHERE WE SAVE EVERYTHING COLLECTED FROM REGISTRATION..");
				LOGGER.info("Sending Test mail here...");
				SendMailSSL mail = new SendMailSSL(this);
				LOGGER.info("Finished creating mail object for " + this.getUsername());
				mail.sendMail();
				db.commit();
				return "True";
			
			} else {
				return "False";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.rollback();
		} finally {
			//
			// always clean up after yourself..
			//
			db.setAutoCommit(true);
			db.free();
		}
		
		return "False";
		
	}
		

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return "SCAHA iSite Wecomes you as a new registered user";
	}
	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return "You have succesfully registered.." + this.getFirstname() + " " + this.getLastname();
	}
	
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return this.username + "," + ((this.email == null || this.email.isEmpty()) ? "" : this.email);
	}
}
