package com.scaha.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.MailableObject;

public class FamilyMemberBean  implements Serializable,  MailableObject  {

	//
	// Class Level Variables
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	//
	// Account Management.. this is when they select a family member to change something
	//
	private FamilyMember selectedFamilyMember = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTextBody() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPreApprovedCC() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getToMailAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the selectedFamilyMember
	 */
	public FamilyMember getSelectedFamilyMember() {
		return selectedFamilyMember;
	}
	/**
	 * @param selectedFamilyMember the selectedFamilyMember to set
	 */
	public void setSelectedFamilyMember(FamilyMember selectedFamilyMember) {
		this.selectedFamilyMember = selectedFamilyMember;
		if (this.selectedFamilyMember == null) {
			LOGGER.info("We have a null family member coming across ...");
			
		} else {
			LOGGER.info("I am selected Family Member: " + this.selectedFamilyMember.ID);
		}
	}

	public void onFamilyMemberRowSelect(SelectEvent  event) {  
		FamilyMember fm = (FamilyMember)event.getObject();
		LOGGER.info("fm:thisid: " + fm.getFamilyMemberID());
		LOGGER.info("fm:personid: " + fm.getPersonID());
		LOGGER.info("fm:relationship: " + fm.getRelationship());
	}  

	public void onFamilyMemberRowUnSelect(UnselectEvent   event) {  
		FamilyMember fm = (FamilyMember)event.getObject();
	}  

}
