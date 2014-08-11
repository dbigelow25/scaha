package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
	private Scoring currentscore =null;
	
	private LiveGameRosterSpotList awayteam = null;
	private LiveGameRosterSpotList hometeam = null;
	
	private ScoringList awayscoring = null;
	private ScoringList homescoring = null;
	
	private List<LiveGameRosterSpot> scoringpicklist = null;
	private ScahaTeam scoringteam = null;
	private LiveGameRosterSpotList scoringroster = null;
	private int selectedgoalroseterid = 0;
	private int selecteda1roseterid = 0;
	private int selecteda2roseterid = 0;
	
	private int goalperiod = 0;
	private String goaltype = null;
	private String goalmin = null;
	private String goalsec = null;
	
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
	
	@SuppressWarnings("unchecked")
	public void newGoal(String _ha) {
		
		if (_ha.equals("H")) { 
			this.scoringteam = this.livegame.getHometeam();
			this.scoringroster = this.getHometeam();
			this.scoringpicklist = (List<LiveGameRosterSpot>) this.getHometeam().getWrappedData();
			this.currentscore = new Scoring(0,pb.getProfile(),this.livegame,this.scoringteam);
		} else {
			this.scoringteam = this.livegame.getAwayteam();
			this.scoringroster = this.getAwayteam();
			this.scoringpicklist = (List<LiveGameRosterSpot>) this.getAwayteam().getWrappedData();
			this.currentscore = new Scoring(0,pb.getProfile(),this.livegame,this.scoringteam);
		}
		
		//
		// reinitialize the info
		//
		this.goalperiod = 0;
		this.goalmin = null;
		this.goalsec = null;
		this.goaltype = null;
		this.selectedgoalroseterid = 0;
		this.selecteda1roseterid = 0;
		this.selecteda2roseterid = 0;

		
	}
	
	@SuppressWarnings("unchecked")
	public void editGoal(String _ha) {
		
		if (_ha.equals("H")) { 
			this.scoringteam = this.livegame.getHometeam();
			this.scoringroster = this.getHometeam();
			this.scoringpicklist = (List<LiveGameRosterSpot>) this.getHometeam().getWrappedData();
			this.currentscore = this.homescoring.getByKey(this.selectedhomescore.ID);
		} else {
			this.scoringteam = this.livegame.getAwayteam();
			this.scoringroster = this.getAwayteam();
			this.scoringpicklist = (List<LiveGameRosterSpot>) this.getAwayteam().getWrappedData();
			this.currentscore = this.awayscoring.getByKey(this.selectedawayscore.ID);
		}

		
		//
		// reinitialize the info to the current selected score
		//
		LOGGER.info("currentscore is: " + currentscore);
		
		this.goalperiod = currentscore.getPeriod();
		String[] ms = currentscore.getTimescored().split(":");
		LOGGER.info("MS:" + ms.length + ":" + ms[0] + ":" + ms[1]);
		this.goalmin = ms[0];
		this.goalsec = ms[1];
		this.goaltype = currentscore.getGoaltype();
		this.selectedgoalroseterid = currentscore.getIdrostergoal();
		this.selecteda1roseterid = currentscore.getIdrostera1();
		this.selecteda2roseterid = currentscore.getIdrostera2();
		
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

	
	public void deleteGoal(String _ha) {
		
		Scoring sc = null;
		if (_ha.equals("H")) {
			sc = this.getHomescoring().getByKey(selectedhomescore.ID);
		} else {
			sc = this.getAwayscoring().getByKey(selectedawayscore.ID);
		}
		LOGGER.info("we need to delete: " + sc);
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try {
			sc.delete(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		this.setAwayscoring(this.refreshAwayScoring());
		this.setHomescoring(this.refreshHomeScoring());
		
	}
	
	
	public void saveGoal() {
		
		LOGGER.info("HERE IS WHERE WE save a GOAL for " + this.scoringteam.getTeamname());
		
		Scoring score = this.currentscore;
		
		score.setPeriod(this.getGoalperiod());
		score.setGoaltype(this.getGoaltype());
		score.setTimescored("00:" + this.getGoalmin() + ":" + this.getGoalsec());
		score.setLgrosterspotgoal(this.scoringroster.getByKey(this.selectedgoalroseterid));
		score.setLgrosterspota1(this.scoringroster.getByKey(this.selecteda1roseterid));
		score.setLgrosterspota2(this.scoringroster.getByKey(this.selecteda2roseterid));
		
		LOGGER.info("updating score for " + score);
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		
		try {
			score.update(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.free();
		this.setAwayscoring(this.refreshAwayScoring());
		this.setHomescoring(this.refreshHomeScoring());
		
	}

	/**
	 * @return the goalperiod
	 */
	public int getGoalperiod() {
		return goalperiod;
	}

	/**
	 * @param goalperiod the goalperiod to set
	 */
	public void setGoalperiod(int goalperiod) {
		this.goalperiod = goalperiod;
	}

	/**
	 * @return the goaltype
	 */
	public String getGoaltype() {
		return goaltype;
	}

	/**
	 * @param goaltype the goaltype to set
	 */
	public void setGoaltype(String goaltype) {
		this.goaltype = goaltype;
	}

	/**
	 * @return the goalmin
	 */
	public String getGoalmin() {
		return goalmin;
	}

	/**
	 * @param goalmin the goalmin to set
	 */
	public void setGoalmin(String goalmin) {
		this.goalmin = goalmin;
	}

	/**
	 * @return the goalsec
	 */
	public String getGoalsec() {
		return goalsec;
	}

	/**
	 * @param goalsec the goalsec to set
	 */
	public void setGoalsec(String goalsec) {
		this.goalsec = goalsec;
	}

	/**
	 * @return the selectedgoalroseterid
	 */
	public int getSelectedgoalroseterid() {
		return selectedgoalroseterid;
	}

	/**
	 * @param selectedgoalroseterid the selectedgoalroseterid to set
	 */
	public void setSelectedgoalroseterid(int selectedgoalroseterid) {
		this.selectedgoalroseterid = selectedgoalroseterid;
	}

	/**
	 * @return the selecteda1roseterid
	 */
	public int getSelecteda1roseterid() {
		return selecteda1roseterid;
	}

	/**
	 * @param selecteda1roseterid the selecteda1roseterid to set
	 */
	public void setSelecteda1roseterid(int selecteda1roseterid) {
		this.selecteda1roseterid = selecteda1roseterid;
	}

	/**
	 * @return the selecteda2roseterid
	 */
	public int getSelecteda2roseterid() {
		return selecteda2roseterid;
	}

	/**
	 * @param selecteda2roseterid the selecteda2roseterid to set
	 */
	public void setSelecteda2roseterid(int selecteda2roseterid) {
		this.selecteda2roseterid = selecteda2roseterid;
	}

	/**
	 * @return the scoringpicklist
	 */
	public List<LiveGameRosterSpot> getScoringpicklist() {
		return scoringpicklist;
	}

	/**
	 * @param scoringpicklist the scoringpicklist to set
	 */
	public void setScoringpicklist(List<LiveGameRosterSpot> scoringpicklist) {
		this.scoringpicklist = scoringpicklist;
	}

	/**
	 * @return the scoringteam
	 */
	public ScahaTeam getScoringteam() {
		return scoringteam;
	}

	/**
	 * @param scoringteam the scoringteam to set
	 */
	public void setScoringteam(ScahaTeam scoringteam) {
		this.scoringteam = scoringteam;
	}

	/**
	 * @return the scoringroster
	 */
	public LiveGameRosterSpotList getScoringroster() {
		return scoringroster;
	}

	/**
	 * @param scoringroster the scoringroster to set
	 */
	public void setScoringroster(LiveGameRosterSpotList scoringroster) {
		this.scoringroster = scoringroster;
	}

	/**
	 * @return the currentscore
	 */
	public Scoring getCurrentscore() {
		return currentscore;
	}

	/**
	 * @param currentscore the currentscore to set
	 */
	public void setCurrentscore(Scoring currentscore) {
		this.currentscore = currentscore;
	}

	
}