package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.StreamedContent;

import com.gbli.common.SendMailSSL;
import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.MailableObject;
import com.scaha.objects.Person;
import com.scaha.objects.ScahaMember;
import com.scaha.objects.UsaHockeyRegistration;

@ManagedBean(name="familymemberBean")
@RequestScoped
public class FamilyMemberBean  implements Serializable,  MailableObject  {

	//
	// Class Level Variables
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String MergedBody = null;
	//
	// Account Management.. this is when they select a family member to change something
	//
	private FamilyMember currentFM = null;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	@ManagedProperty(value="#{scahaBean}")
	private ScahaBean scaha;
	
	/**
	 * @return the scaha
	 */
	public ScahaBean getScaha() {
		return scaha;
	}

	/**
	 * @param scaha the scaha to set
	 */
	public void setScaha(ScahaBean scaha) {
		this.scaha = scaha;
	}

	/**
	 * @return the pb
	 */
	public ProfileBean getPb() {
		return pb;
	}

	/**
	 * @param pb the pb to set
	 */
	public void setPb(ProfileBean pb) {
		this.pb = pb;
	}	
	
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
		return "online@iscaha.com";
	}
	@Override
	public String getToMailAddress() {
		return pb.getProfile().getUserName();
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
	}

	public void onFamilyMemberRowSelect(SelectEvent  event) {  
		FamilyMember fm = (FamilyMember)event.getObject();
	}  

	public void onFamilyMemberRowUnSelect(UnselectEvent   event) {  
		FamilyMember fm = (FamilyMember)event.getObject();
	}
	
	
	public String resendMemberReceipt() {
		
		LOGGER.info("Resending Member Reg for " + this.currentFM);
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase", pb.getProfile());
		
		Person tper = pb.getProfile().getPerson();
		Person per = (Person)this.currentFM;
		UsaHockeyRegistration usar = this.currentFM.getUsaHockeyRegistration();
		ScahaMember mem = this.currentFM.getSMember();
		this.buildMailBody(db, tper, per, usar, mem);
		SendMailSSL mail = new SendMailSSL(this);
		mail.sendMail();
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);			

		context.addMessage(
				null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Member Registration",
                "We have successfully sent a registration receipt reminder to your address on file.  You will be receiving a confirmation e-mail shortly"));
		
		return "true";
		
	}
	
	private void buildMailBody(ScahaDatabase _db, Person tper, Person per, UsaHockeyRegistration usar2, ScahaMember mem) {
		
		// TODO Auto-generated method stub
		List<String> myTokens = new ArrayList<String>();
		myTokens.add("PFIRST:" + per.getsFirstName());
		myTokens.add("PLAST:" + per.getsLastName());
		myTokens.add("FIRSTNAME:" + tper.getsFirstName());
		myTokens.add("LASTNAME:" + tper.getsLastName());
		myTokens.add("USAHNUM:" + usar2.getUSAHnum());
		myTokens.add("SCAHANUM:" + mem.getSCAHANumber());
		myTokens.add("SEASON:" + scaha.getScahaSeasonList().getCurrentSeason().getDescription());
		try {
			if (_db.isPersonPlayer(per.ID)) {
				if (_db.checkForBC(per.ID)) {
					myTokens.add("BCSENTENCE:" + "We already have the Player's Birth Certificate on file so you do not need to bring a copy to any SCAHA tryout.");
				} else {
					myTokens.add("BCSENTENCE:" + "MAKE SURE TO BRING A COPY OF THE PLAYER'S Birth Certificate to ALL  SCAHA TRYOUTS.  We do not have any BC on file for this player yet.");
				}
			} else {
				myTokens.add("BCSENTENCE:" + "You have not registered this person as a SCAHA player, so no Birth Certificate Needed.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myTokens.add("SEASON:" + scaha.getScahaSeasonList().getCurrentSeason().getDescription());
			
		this.MergedBody =  Utils.mergeTokens(MemberBean.mail_body,myTokens);
		
	}

	@Override
	public InternetAddress[] getToMailIAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InternetAddress[] getPreApprovedICC() {
		// TODO Auto-generated method stub
		return null;
	}  

}
