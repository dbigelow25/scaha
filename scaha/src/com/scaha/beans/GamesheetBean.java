package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.InternetAddress;

import org.primefaces.event.RowEditEvent;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Division;
import com.scaha.objects.LiveGame;
import com.scaha.objects.LiveGameRosterSpot;
import com.scaha.objects.LiveGameRosterSpotList;
import com.scaha.objects.MailableObject;
import com.scaha.objects.ScahaTeam;
import com.scaha.objects.Scoring;
import com.scaha.objects.ScoringList;
import com.scaha.objects.SkillLevel;

@ManagedBean
@ViewScoped
public class GamesheetBean implements Serializable,  MailableObject {

	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	
	@ManagedProperty(value="#{scoreboardBean}")
	private ScoreboardBean sb;
	
	private LiveGame livegame = null;
	private LiveGameRosterSpot selectedhomerosterspot;	
	private LiveGameRosterSpot selectedawayrosterspot;	
	
	private Scoring selectedhomescore = null;
	private Scoring selectedawayscore = null;
	
	private LiveGameRosterSpotList awayteam = null;
	private LiveGameRosterSpotList hometeam = null;
	
	private ScoringList awayscoring = null;
	private ScoringList homescoring = null;
	
	//
	// Class Level Variables
	//
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// lets go get it!
	//
	public GamesheetBean() {
	}

	 @PostConstruct
	 public void init() {
		 
		 LOGGER.info(" *************** START :POST INIT FOR GAMESHEET  BEAN *****************");
		 this.setLivegame(sb.getSelectedlivegame());
		 LOGGER.info("/// here is selected live game.." + this.getLivegame());
		 this.setHometeam(this.refreshHomeRoster());
		 this.setAwayteam(this.refreshAwayRoster());
		 this.setAwayscoring(this.refreshAwayScoring());
		 this.setHomescoring(this.refreshHomeScoring());
	
		 LOGGER.info(" *************** FINISH :POST INIT FOR GAMESHEET BEAN *****************");
	 }
	
	
	/**
	 * @return the selectedhomerosterspot
	 */
	public LiveGameRosterSpot getSelectedhomerosterspot() {
		return selectedhomerosterspot;
	}

	/**
	 * @param selectedhomerosterspot the selectedhomerosterspot to set
	 */
	public void setSelectedhomerosterspot(LiveGameRosterSpot selectedhomerosterspot) {
		this.selectedhomerosterspot = selectedhomerosterspot;
	}

	/**
	 * @return the selectedawayrosterspot
	 */
	public LiveGameRosterSpot getSelectedawayrosterspot() {
		return selectedawayrosterspot;
	}

	/**
	 * @param selectedawayrosterspot the selectedawayrosterspot to set
	 */
	public void setSelectedawayrosterspot(LiveGameRosterSpot selectedawayrosterspot) {
		this.selectedawayrosterspot = selectedawayrosterspot;
	}

	/**
	 * @return the awayteam
	 */
	public LiveGameRosterSpotList getAwayteam() {
		return awayteam;
	}

	/**
	 * @param awayteam the awayteam to set
	 */
	public void setAwayteam(LiveGameRosterSpotList awayteam) {
		this.awayteam = awayteam;
	}

	public void onHomeScoreCancel(RowEditEvent event) { 
		Scoring score = (Scoring) event.getObject();
		LOGGER.info("Cencelling Edited Score:" + score);
	}

	public void onHomeScoreEdit(RowEditEvent event) { 
	
		//
		// lets assemble the object .. we can only change three things right now..
		// 1) Team Name
		//
		// These changs below can only happen when there are NO Players assigned to the team
		
		// 2) Team Division
		// 3) Team Skill Level
		
		Scoring score = (Scoring) event.getObject();
		LOGGER.info("HERE IS MY Edited Score:" + score);
		
		
	} 
	       
	public void onAwayScoreCancel(RowEditEvent event) { 
		Scoring score = (Scoring) event.getObject();
		LOGGER.info("Cencelling Edited Score:" + score);
	}
	
	public void onAwayScoreEdit(RowEditEvent event) { 
		
		//
		// lets assemble the object .. we can only change three things right now..
		// 1) Team Name
		//
		// These changs below can only happen when there are NO Players assigned to the team
		
		// 2) Team Division
		// 3) Team Skill Level
		
		Scoring score = (Scoring) event.getObject();
		LOGGER.info("HERE IS MY Edited Score:" + score);
		
		
	} 
	       
	/**
	 * @return the hometeam
	 */
	public LiveGameRosterSpotList getHometeam() {
		return hometeam;
	}

	/**
	 * @param hometeam the hometeam to set
	 */
	public void setHometeam(LiveGameRosterSpotList hometeam) {
		this.hometeam = hometeam;
	}


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
	 * @return the sb
	 */
	public ScoreboardBean getSb() {
		return sb;
	}

	/**
	 * @param sb the sb to set
	 */
	public void setSb(ScoreboardBean sb) {
		this.sb = sb;
	}

	/**
	 * @return the livegame
	 */
	public LiveGame getLivegame() {
		return livegame;
	}

	/**
	 * @param livegame the livegame to set
	 */
	public void setLivegame(LiveGame livegame) {
		this.livegame = livegame;
	}

	public LiveGameRosterSpotList refreshHomeRoster() {
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		LiveGameRosterSpotList list = null;
		try {
			list = LiveGameRosterSpotList.NewListFactory(pb.getProfile(), db, this.getLivegame(), scaha.getScahaTeamList(), "H");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		return list;
	}

	public LiveGameRosterSpotList refreshAwayRoster() {

		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		LiveGameRosterSpotList list = null;
		try {
			list = LiveGameRosterSpotList.NewListFactory(pb.getProfile(), db, this.getLivegame(), scaha.getScahaTeamList(), "A");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		return list;
	}



	private ScoringList refreshHomeScoring() {
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		ScoringList list = null;
		try {
			list = ScoringList.NewListFactory(pb.getProfile(), db, this.getLivegame(), this.getLivegame().getHometeam(), this.getHometeam());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		return list;
	}

	private ScoringList refreshAwayScoring() {
		
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		ScoringList list = null;
		try {
			list = ScoringList.NewListFactory(pb.getProfile(), db, this.getLivegame(), this.getLivegame().getAwayteam(), this.getAwayteam());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		return list;
	}
	/**
	 * For the selected Player.. we need to toggle his MIA...
	 * Then refresh the list
	 */
	public void toggleMIA(String _ha) {
		
		LiveGameRosterSpot spot = null;
		
		if (_ha.equals("H")) {
			spot = this.getHometeam().getByKey(this.selectedhomerosterspot.ID);
		} else {
			spot = this.getAwayteam().getByKey(this.selectedawayrosterspot.ID);
		}
		
		LOGGER.info("toggling MIA for " + spot);
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		try {
			CallableStatement pc = db.prepareCall("call scaha.toggleMIA(?,?,?)");
			pc.setInt(1,this.livegame.ID);
			pc.setInt(2, spot.ID);
			pc.setInt(3, (spot.isMia() ? 1 : 0));
			pc.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		spot.setMia(!spot.isMia());
//		if (_ha.equals("H")) {
//			refreshHomeRoster();
//		} else {
//			refreshAwayRoster();
//		}
		
	}
	
	public String getHomeClubId() {
		return this.getLivegame().getHometeam().getTeamClub().ID+"";
	}

	public String getAwayClubId() {
		
		return this.getLivegame().getAwayteam().getTeamClub().ID+"";
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

	/**
	 * @return the awayscoring
	 */
	public ScoringList getAwayscoring() {
		return awayscoring;
	}

	/**
	 * @param awayscoring the awayscoring to set
	 */
	public void setAwayscoring(ScoringList awayscoring) {
		this.awayscoring = awayscoring;
	}

	/**
	 * @return the homescoring
	 */
	public ScoringList getHomescoring() {
		return homescoring;
	}
	
	public int getDerivedHomeScore() {
		return this.homescoring.getRowCount();
	}

	public int getDerivedAwayScore() {
		return this.awayscoring.getRowCount();
	}

	
	/**
	 * @param homescoring the homescoring to set
	 */
	public void setHomescoring(ScoringList homescoring) {
		this.homescoring = homescoring;
	}

	/**
	 * @return the selectedhomescore
	 */
	public Scoring getSelectedhomescore() {
		return selectedhomescore;
	}

	/**
	 * @param selectedhomescore the selectedhomescore to set
	 */
	public void setSelectedhomescore(Scoring selectedhomescore) {
		this.selectedhomescore = selectedhomescore;
	}

	/**
	 * @return the selectedawayscore
	 */
	public Scoring getSelectedawayscore() {
		return selectedawayscore;
	}

	/**
	 * @param selectedawayscore the selectedawayscore to set
	 */
	public void setSelectedawayscore(Scoring selectedawayscore) {
		this.selectedawayscore = selectedawayscore;
	}
	
	
	

	
}