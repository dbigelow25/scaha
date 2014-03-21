package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class ScahaMember extends ScahaObject implements Serializable, MailableObject {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String SCAHANumber = null;			// This is the 6 digit automatically generated number..
	private String SCAHAYear = null;		// This is they year (single digit) its valid in..
	private Person MyPerson = null;				// ok.. new strat.. this owns the member object
	private Person TopPerson = null; 	// This represents the Top owner of this object.. Usually the account holder
	
	
	/**
	 * @return the topPerson
	 */
	public Person getTopPerson() {
		return TopPerson;
	}
	/**
	 * @param topPerson the topPerson to set
	 */
	public void setTopPerson(Person topPerson) {
		TopPerson = topPerson;
	}
	protected int ID = -1;
	
	/**
	 * A basic object to hold all the USAH Information coming back from a call..
	 * 
	 * @param _id
	 * @param _sUSAHnum
	 */
	
	
	public ScahaMember(Profile _pro, Person _per) {
		this.setProfile(_pro);
		this.setMyPerson(_per);
	}
	/**
	
	
	/**
	 * @return the myPerson
	 */
	public Person getMyPerson() {
		return MyPerson;
	}
	/**
	 * @param myPerson the myPerson to set
	 */
	public void setMyPerson(Person myPerson) {
		MyPerson = myPerson;
	}
	/**
	 * @return the sCAHANumber
	 */
	public String getSCAHANumber() {
		return SCAHANumber;
	}
	/**
	 * @param sCAHANumber the sCAHANumber to set
	 */
	public void setSCAHANumber(String sCAHANumber) {
		SCAHANumber = sCAHANumber;
	}
	/**
	 * @return the sCAHAYear
	 */
	public String getSCAHAYear() {
		return SCAHAYear;
	}
	/**
	 * @param sCAHAYear the sCAHAYear to set
	 */
	public void setSCAHAYear(String sCAHAYear) {
		SCAHAYear = sCAHAYear;
	}
	/**
	 *  This guy really just cranks out a membership record.
	 *  The membership object should really never be updated progromatically.
	 *  Because the number has to be unique.. we need to:
	 *  
	 *  1) Lock the table
	 *  2) Genereate the Unique Number (by gen and check against DB)
	 *  3) then create the table
	 *  4) The unlock the table.
	 */
	public void generateMembership(ScahaDatabase _db) throws SQLException {
		
		
		if (this.ID > 0 ) {
			LOGGER.info("Blocking an Update Call to a Member Object that has a positive ID");
			return;
		}

		// 
		// is it an object that is not in the database yet..
		//
		//

		int ians = 1;
		
		while (ians > 0 ) {
		
			this.setSCAHANumber(Utils.getRandom5CharStringUpper());
			Vector<String> v = new Vector<String>();
			v.add(this.getSCAHAYear());
			v.add(this.getSCAHANumber());
		
			//
			// Lets check to see if it is a Unique Number
			// 
		
			_db.getData("call scaha.checkMemberNumberUnique(?,?)", v);
			_db.getResultSet().next();
			ians = _db.getResultSet().getInt(1);

		}
		
		// We should have a unique number by now..
		
		_db.cleanup();
		
		CallableStatement cs = _db.prepareCall("call scaha.updateMember(?,?,?,?,?,?)");
				
		int i = 1;
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(i++, this.ID);
		cs.setInt(i++,this.getMyPerson().ID);			
		cs.setString(i++, this.getSCAHAYear());
		cs.setString(i++, this.getSCAHANumber());
		cs.setInt(i++, 1);										//	IN in_isactive tinyint,
		cs.setString(i++, null);								//	IN in_updated timestamp
		
		cs.execute();
		
		//
		// Update the new ID from the database...
		//
		this.ID = cs.getInt(1);
		cs.close();
		_db.cleanup();

	}
	
	@Override
	public String getSubject() {
		return "SCAHA Membership Information For " + this.MyPerson.getsFirstName() + " " + this.MyPerson.getsLastName();
	}
	@Override
	public String getTextBody() {
		
		String strBody = "Hello There: your USA Hockey Number is: " + this.MyPerson.getUsaHockeyRegistration().getUSAHnum();
		
		strBody = strBody + "/nHere is your SCAHA Membership number: " + this.getSCAHANumber();
		
		return strBody;
		
		
	}
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String getToMailAddress() {
		/**
		 *  The mailto address is going to two people.. if different.
		 *  
		 *  The owner of the account..
		 *  and the e-mail on the usahockeyregistration..
		 */
		
		/**
		 *  The top person has both an e-mail in the profile and an e-mail (alt if their person record)
		 */
		
		LOGGER.info("SendMail To:" +this.getProfile().getUserName() + (TopPerson.getsEmail() != null ? "," + TopPerson.getsEmail() : ""));
		
		return this.getProfile().getUserName() + (TopPerson.getsEmail() != null ? "," + TopPerson.getsEmail() : "");
		
		//+ ", " + this.getMyPerson().getsEmail();
		
	
	}
	

}

