package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.mail.internet.InternetAddress;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.LiveGame;
import com.scaha.objects.LiveGameRosterSpot;
import com.scaha.objects.LiveGameRosterSpotList;
import com.scaha.objects.MailableObject;

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

	private LiveGameRosterSpotList awayteam = null;
	private LiveGameRosterSpotList hometeam = null;
	
	//
	// Class Level Variables
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
			list = LiveGameRosterSpotList.NewListFactory(pb.getProfile(), db, this.getLivegame(), scaha.getScahaTeamList(), "HOME");
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
			list = LiveGameRosterSpotList.NewListFactory(pb.getProfile(), db, this.getLivegame(), scaha.getScahaTeamList(), "AWAY");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.free();
		
		return list;
	}
	
	
	public String getHomeClubId() {
		LOGGER.info("HERE IS HOME TEAM:" + this.getLivegame().getHometeam());
		LOGGER.info("HERE IS HOME Club:" + this.getLivegame().getHometeam().getTeamClub());
		return this.getLivegame().getHometeam().getTeamClub().ID+"";
	}

	public String getAwayClubId() {
		
		LOGGER.info("HERE IS AWAY TEAM:" + this.getLivegame().getAwayteam());
		LOGGER.info("HERE IS AWAY Club:" + this.getLivegame().getAwayteam().getTeamClub());
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
	
	
	

	
}