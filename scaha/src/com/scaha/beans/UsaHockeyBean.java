package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
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
import com.scaha.objects.Family;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.ScahaCoach;
import com.scaha.objects.ScahaManager;
import com.scaha.objects.ScahaMember;
import com.scaha.objects.ScahaPlayer;
import com.scaha.objects.UsaHockeyRegistration;

public class UsaHockeyBean implements Serializable, MailableObject {
	
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	/**
	 * 
	 */
	
	private UsaHockeyRegistration usar = null;
	private String regnumber = null;
	private List<String> membertype;  
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
		// We need to set Member Type to Manager.  Because thats what the XX is for
		//
		
		if (this.usar == null) {
			LOGGER.info("usaHockeyBean:Did NOT Get any USAH Number info Back!! BAD SERVICE CALL");
			
		} else if (this.usar.getUSAHnum().contains("XX")) {
			if (!membertype.contains("Manager")) {
				this.membertype.add("Manager");
			}
		}
		
		//
		// We are not going anywhere.. so simply pass true vs routing..
		//
		return "true";
	}

	
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return "Congradulations, you have successfully registered";
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
	public List<String>  getMembertype() {
		return membertype;
	}

	/**
	 * @param membertype the  to set
	 */
	public void setMembertype(List<String> selectedOptions2) {
		this.membertype = selectedOptions2;
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
		//
		// We have to be really clear here.  First off.. this person can either be brand new.. or and existing member.
		//
		// This current case only deals with a Brand New Person.
		//
		// In this case.. we need to
		//
		// 1) Create or update the person..
		// 2) Need to create an object.. for each type they are (player, manager, coach)
		// 3) Hook them up to the existing family..
		// 4) If they have not already registered.. we need to create a SCAHA registration
		//
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression expression = app.getExpressionFactory().createValueExpression(context.getELContext(), "#{profileBean}", Object.class );
		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");

		//
		// Lets start off with the basics person is always implied in the extended object.
		// we are really just saving off alot of stuff through the objects.. then reloading the family .. once done
		// 
		
		//
		// First.. if the added person is looks just like another person in the family.. then we have trouble brewing..
		// We must stop this add in its tracks and tell them to use an update existing family member function
		//
		try {
			if (db.checkForPersonByFLDOB(usar.getFirstName(), usar.getLastName(), usar.getDOB())) {
				
				FacesContext.getCurrentInstance().addMessage(
						"mp-form:usah-reg",
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "USA Hockey Reg",
	                    "You cannot create a member.  There is already a member with the same first name, last name and date of birth!"));
				
			} else {
	
				//
				// Lets check to see if this person .. "looks" like another person
				// if it does.. we need to route them to a confirmation screen that they are indeed going to add 
				//
			
				Profile pro = pb.getProfile();   // This is the profile of the user
				Person tper = pro.getPerson();   //  This is their person record
				Family tfam = tper.getFamily();  //  This is their Family Structure
				
				Person per = new Person(pro);		// This will be the new person
				ScahaManager sm = null;
				ScahaPlayer sp = null;
				ScahaCoach sc = null;
				ScahaMember mem = null;
				
		
				// And we need to create the membership record as well..
				//
				// Here is where we update everything at once.
				// we have auto commit off so its an all or nothing approach.
				//
				db.setAutoCommit(false);
				//
		
				per.gleanUSAHinfo(this.usar);
				per.update(db);
				usar.update(db, per);
				
				mem = new ScahaMember(pro,per);
				mem.setSCAHAYear(this.usar.getMemberShipYear());
				LOGGER.info("Time to Create the Membership.. ");
				mem.generateMembership(db);
				mem.setTopPerson(tper);
				
				LOGGER.info("Member Type is" + membertype.toString());
				
				//
				// ok.. lets update the trifecta and see if it sticks!!
				//
				if (membertype.contains("Manager")) {
					sm = new ScahaManager(pro,per);
					sm.update(db);
				}
				if (membertype.contains("Coach")) {
					sc = new ScahaCoach(pro,per);
					sc.update(db);
	
				}
				if (membertype.contains("Player-Goalie") || membertype.contains("Player-Skater")) {
					sp = new ScahaPlayer(pro, per);
					sp.gleanUSAHinfo(this.usar);
					if (membertype.contains("Player-Goalie")) {
						sp.setGoalie(true);
					}
					sp.update(db);
				}
		
				//
				// Now we need to get the Family Object from the Person in the Profile..
				// and add this person to the database.. and the object
				//
				// no matter how many types of people we have.. they all point to the same person...
				FamilyMember fm = new FamilyMember(pro, tfam, per);
				fm.setRelationship(this.getRelationship());
				fm.updateFamilyMemberStructure(db);
				
				// ok.. since we are good.. lets get a new family structure for the pic.
				
				tper.setFam(null);
				tper.setFam(new Family(db, tper));
				
				db.free();
	            context.addMessage(null, new FacesMessage("Successful", "I think we saved something.."));  
	            
	            //
	            // Lets go back to view mode.. i know .. its a bad name.. but we are in a hurry
	            //
	            pb.cancelAddMember();
	        
	            
	            //
	            //  We also have to create an e-mail record.. possibly formated in HTML..
	            //
	        	// We want to create a family called the <lastname> family...
				SendMailSSL mail = new SendMailSSL(mem);
				mail.sendMail();
			}
            
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		db.free();
		
		return "true";

	}
	
	public void reset() {
		LOGGER.info("usaHockeyBean:reseting member variables...");
		usar = null;
		regnumber = null;
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
				+ ", dob=" + ", membertype=" + membertype
				+ ", relationship=" + relationship + "]";
	}

}
