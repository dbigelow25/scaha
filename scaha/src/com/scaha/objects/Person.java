package com.scaha.objects;

import java.io.Serializable;
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
	private String sAddress2 = null;
	private String sCity = null;
	private String sState = null;
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
					this.setsAddress2(rs.getString(i++));
					this.setsCity(rs.getString(i++));
					this.setsState(rs.getString(i++));
					this.setiZipCode(rs.getInt(i++));
					LOGGER.info("Successfully Created the Person...");

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
	 * @return the sAddress2
	 */
	public String getsAddress2() {
		return sAddress2;
	}


	/**
	 * @param sAddress2 the sAddress2 to set
	 */
	public void setsAddress2(String sAddress2) {
		this.sAddress2 = sAddress2;
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

	
}
