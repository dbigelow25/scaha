package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public class UsaHockeyRegistration extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	
	private String LastName = null;
	private String FirstName = null;
	private String MiddleInit = null;
	private String Address = null;
	private String City = null;
	private String State = null;
	private String Zipcode = null;
	private String DOB = null;
	private String Country = null;
	private	String ForZip = null;
	private	String Citizen = null;
	private String Gender = null;
	private String HPhone = null;
	private String BPhone = null;
	private String PGSLName = null;
	private String PGSFName = null;
	private String PGSMName = null;
	private String Email = null;
	
	
	
	public UsaHockeyRegistration(int _id ) {
		this.setID(_id);
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	/**
	 * @return the middleInit
	 */
	public String getMiddleInit() {
		return MiddleInit;
	}
	/**
	 * @param middleInit the middleInit to set
	 */
	public void setMiddleInit(String middleInit) {
		MiddleInit = middleInit;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return Address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		Address = address;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return City;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		City = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return State;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		State = state;
	}
	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return Zipcode;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		Zipcode = zipcode;
	}
	/**
	 * @return the dOB
	 */
	public String getDOB() {
		return DOB;
	}
	/**
	 * @param dOB the dOB to set
	 */
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return Country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		Country = country;
	}
	/**
	 * @return the forZip
	 */
	public String getForZip() {
		return ForZip;
	}
	/**
	 * @param forZip the forZip to set
	 */
	public void setForZip(String forZip) {
		ForZip = forZip;
	}
	/**
	 * @return the citizen
	 */
	public String getCitizen() {
		return Citizen;
	}
	/**
	 * @param citizen the citizen to set
	 */
	public void setCitizen(String citizen) {
		Citizen = citizen;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return Gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		Gender = gender;
	}
	/**
	 * @return the hPhone
	 */
	public String getHPhone() {
		return HPhone;
	}
	/**
	 * @param hPhone the hPhone to set
	 */
	public void setHPhone(String hPhone) {
		HPhone = hPhone;
	}
	/**
	 * @return the bPhone
	 */
	public String getBPhone() {
		return BPhone;
	}
	/**
	 * @param bPhone the bPhone to set
	 */
	public void setBPhone(String bPhone) {
		BPhone = bPhone;
	}
	/**
	 * @return the pGSLName
	 */
	public String getPGSLName() {
		return PGSLName;
	}
	/**
	 * @param pGSLName the pGSLName to set
	 */
	public void setPGSLName(String pGSLName) {
		PGSLName = pGSLName;
	}
	/**
	 * @return the pGSFName
	 */
	public String getPGSFName() {
		return PGSFName;
	}
	/**
	 * @param pGSFName the pGSFName to set
	 */
	public void setPGSFName(String pGSFName) {
		PGSFName = pGSFName;
	}
	/**
	 * @return the pGSMName
	 */
	public String getPGSMName() {
		return PGSMName;
	}
	/**
	 * @param pGSMName the pGSMName to set
	 */
	public void setPGSMName(String pGSMName) {
		PGSMName = pGSMName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UsaHockeyRegistration [LastName=" + LastName + ", FirstName="
				+ FirstName + ", MiddleInit=" + MiddleInit + ", Address="
				+ Address + ", City=" + City + ", State=" + State
				+ ", Zipcode=" + Zipcode + ", DOB=" + DOB + ", Country="
				+ Country + ", ForZip=" + ForZip + ", Citizen=" + Citizen
				+ ", Gender=" + Gender + ", HPhone=" + HPhone + ", BPhone="
				+ BPhone + ", PGSLName=" + PGSLName + ", PGSFName=" + PGSFName
				+ ", PGSMName=" + PGSMName + ", Email=" + Email + "]";
	}

	
	

}
