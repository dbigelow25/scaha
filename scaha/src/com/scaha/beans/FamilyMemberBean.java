package com.scaha.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.StreamedContent;

import com.gbli.common.Utils;
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
	private FamilyMember currentFM = null;
	
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
	public FamilyMember getCurrentFM() {
		return currentFM;
	}
	/**
	 * @param selectedFamilyMember the selectedFamilyMember to set
	 */
	public void setCurrentFM(FamilyMember selectedFamilyMember) {
		this.currentFM = selectedFamilyMember;
		if (this.currentFM == null) {
			LOGGER.info("We have a null family member coming across ...");
			
		} else {
			LOGGER.info("I am selected Family Member: " + this.currentFM.ID);
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
