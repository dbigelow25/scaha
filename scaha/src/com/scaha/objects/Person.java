package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Person extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String m_strDB = "Call scaha.getPersonByProfileID(?)";
	
	private String sFirstName = null;
	private String sLastName = null;
	private String sEmail = null;
	private String sPhone = null;
	private String sAddress1 = null;
	private String sCity = null;
	private String sState = null;
	private String gender = null;
	private String dob = null;
	
	private int iZipCode = 0;
	private Family fam = null;
	
	public Person (ScahaDatabase _db, Profile _pro) {
		
		LOGGER.info("Attempting to Create the Person...");
		//
		//  Lets pull from the database now..
		// 
		this.setProfile(_pro);
		
		ResultSet rs = _db.getResultSet();
		Vector<Integer> v = new Vector<Integer>();
		v.add(_pro.getID());

		try {
			if (_db.getData(m_strDB, v)) {
				rs = _db.getResultSet();
				if (rs.next()) {
					int i = 1;
					this.setID(rs.getInt(i++));
					this.setsFirstName(rs.getString(i++));
					this.setsLastName(rs.getString(i++));
					this.setsEmail(rs.getString(i++));
					this.setsPhone(rs.getString(i++));
					this.setsAddress1(rs.getString(i++));
					this.setsCity(rs.getString(i++));
					this.setsState(rs.getString(i++));
					this.setiZipCode(rs.getInt(i++));
					this.setGender(rs.getString(i++));
					this.setDob(rs.getString(i++));
					LOGGER.info("Successfully Created the Person Object...");

				}
			}
		} catch (SQLException ex) {
				ex.printStackTrace();
		} finally {
			_db.cleanup();
		}
		
		//
		// Lets round out the family here..
		//
		this.fam = new Family (_db, this);
		
	}
	
	/**
	 * Here we build one from scratch w/o any DB Support
	 * @param _id
	 * @param _pro
	 */
	public Person (int _id, Profile _pro) {
		
		this.setID(_id);
		this.setProfile(_pro);
	}

	
	
	/**
	 * @return the sPhone
	 */
	public String getsPhone() {
		return sPhone;
	}



	/**
	 * @param sPhone the sPhone to set
	 */
	public void setsPhone(String sPhone) {
		this.sPhone = sPhone;
	}



	/**
	 * @return the sFirstName
	 */
	public String getsFirstName() {
		return sFirstName;
	}


	/**
	 * @param sFirstName the sFirstName to set
	 */
	public void setsFirstName(String sFirstName) {
		this.sFirstName = sFirstName;
	}


	/**
	 * @return the sLastName
	 */
	public String getsLastName() {
		return sLastName;
	}


	/**
	 * @param sLastName the sLastName to set
	 */
	public void setsLastName(String sLastName) {
		this.sLastName = sLastName;
	}


	/**
	 * @return the sEmail
	 */
	public String getsEmail() {
		return sEmail;
	}


	/**
	 * @param sEmail the sEmail to set
	 */
	public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}



	/**
	 * @return the sAddress1
	 */
	public String getsAddress1() {
		return sAddress1;
	}


	/**
	 * @param sAddress1 the sAddress1 to set
	 */
	public void setsAddress1(String sAddress1) {
		this.sAddress1 = sAddress1;
	}

	
	/**
	 * @return the sCity
	 */
	public String getsCity() {
		return sCity;
	}


	/**
	 * @param sCity the sCity to set
	 */
	public void setsCity(String sCity) {
		this.sCity = sCity;
	}


	/**
	 * @return the sState
	 */
	public String getsState() {
		return sState;
	}


	/**
	 * @param sState the sState to set
	 */
	public void setsState(String sState) {
		this.sState = sState;
	}


	/**
	 * @return the iZipCpde
	 */
	public int getiZipCode() {
		return iZipCode;
	}


	/**
	 * @param iZipCpde the iZipCpde to set
	 */
	public void setiZipCode(int iZipCode) {
		this.iZipCode = iZipCode;
	}

	/**
	 * This returns the family that was constructed when the person was created..
	 * @return
	 */
	public Family getFamily() {
		return this.fam;
	}

	/**
	 * This guy will take an existing database connection and update and or insert the record..
	 * 
	 * @param db
	 */
	public void update(ScahaDatabase db) throws SQLException {
		
		// 
		// is it an object that is not in the database yet..
		//
		
		CallableStatement cs = db.prepareCall("call scaha.updatePerson(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		//		INOUT in_idPerson INT(10),
		//		IN in_idProfile INT(10),
		//	    IN in_fname VARCHAR(50),
		//	    IN in_lname VARCHAR(50),
		//	    IN in_altemail VARCHAR(75),
		//	    IN in_phone VARCHAR(10),
		//	    IN in_address VARCHAR(75),
		//	    IN in_city VARCHAR(50),
		//	    IN in_state VARCHAR(2),
		//	    IN in_zipcode INT(10),
		//	    IN in_gender VARCHAR(1),
		//	    IN in_dob VARCHAR(10),
		//		IN in_isactive tinyint,
		//		IN in_updated timestamp,

		int i = 1;
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(i++, getID());
		cs.setInt(i++, getProfile().getID());
		cs.setString(i++, this.sFirstName);
		cs.setString(i++, this.sLastName);
		cs.setString(i++, this.sEmail);
		cs.setString(i++, this.sPhone);
		cs.setString(i++, this.sAddress1);
		cs.setString(i++, this.sCity);
		cs.setString(i++, this.sState);
		cs.setInt(i++, this.iZipCode);
		cs.setString(i++, this.gender);
		cs.setString(i++, this.dob);
		cs.setInt(i++,1);
		cs.setString(i++,null);
		cs.execute();
		
		//
		// Update the new ID from the database...
		//
		this.setID(cs.getInt(1));
		cs.close();

		LOGGER.info("HERE IS THE NEW ID:" + this.getID());

	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	
}
