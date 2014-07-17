/**
 * 
 */
package com.gbli.connectors;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.common.ReturnDataResultSet;
import com.gbli.common.ReturnDataRow;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.Schedule;

import java.sql.PreparedStatement;

import javax.mail.internet.InternetAddress;

/**
 * @author dbigelow
 * 
 */
public class ScahaDatabase extends Database {

	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// Stored Procedure Strings.
	//
	public static String c_sp_profile = "Call scaha.profile(?,?)";
	public static String c_sp_actionlist = "Call scaha.getActionTree(?)";
	public static String c_PS_CHECK_FOR_USER = "Call scaha.checkForUser(?)";
	public static String cs_UPDATE_CLUB_STAFFER = "call scaha.updateClubStaffer(?,?,?,?)";
	
	
	public ScahaDatabase(int _iId, String _sDriver, String _sURL, String _sUser, String _sPwd) {
		super(_iId, _sDriver, _sURL, _sUser, _sPwd);
	}

	public ScahaDatabase(String _sDriver, String _sURL, String _sUser, String _sPwd) {
		super(_sDriver, _sURL, _sUser, _sPwd);
	}

	/**
	 * This will check to see if the user has the proper credentials and will
	 * set up the result set for interrogation if true
	 * 
	 * @param _strUser
	 * @param _strPass
	 * @return
	 */
	public final boolean verify(String _sUser, String _sPass) {

		Vector<String> v = new Vector<String>();
		v.add(_sUser);
		v.add(_sPass);
		LOGGER.info("db.verify: Parms are:" + _sUser + ":" + _sPass);
		return super.getData(c_sp_profile, v);

	}

	/**
	 * This will check to see if the user has the proper credentials and will
	 * set up the result set for interrogation if true
	 * 
	 * @param _strUser
	 * @param _strPass
	 * @return
	 */
	public final boolean getActionData(int _ipid) {

		Vector<Integer> v = new Vector<Integer>();
		v.add(Integer.valueOf(_ipid));

		return getData(c_sp_actionlist, v);

	}
	
	/**
	 *  General utility to check to see if  this person already exists in the database as a person..
	 *  
	 * @param _sfname
	 * @param _slname
	 * @param _sDOB
	 * @return
	 * @throws SQLException 
	 */
	public boolean checkForPersonByFLDOB(String _sfname, String _slname, String _sDOB) throws SQLException {

		String strAnswer = "N";
		CallableStatement cs = this.prepareCall("call scaha.checkforPersonByFLDOB(?,?,?,?)");
		
		LOGGER.info("FLDOB:" + _sfname + ":" + _slname + ":" + _sDOB); 
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(1, strAnswer);
		cs.setString(2,  _sfname);
		cs.setString(3,  _slname);
		cs.setString(4,  _sDOB);
		cs.execute();
		strAnswer = cs.getString(1);
		cs.close();
		LOGGER.info("FLDOB:" + _sfname + ":" + _slname + ":" + _sDOB + ":returns=" + strAnswer); 
		return strAnswer.equals("Y");
		
	}
	
	/**
	 *  General utility to check to see if  this person has yet to verify his bc
	 *  
	 * @param _sfname
	 * @param _slname
	 * @param _sDOB
	 * @return
	 * @throws SQLException 
	 */
	public boolean checkForBC(int _idPerson) throws SQLException {

		String strAnswer = "N";
		CallableStatement cs = this.prepareCall("call scaha.checkForBC(?,?)");
		cs.setInt(1, _idPerson);
		cs.registerOutParameter(2, java.sql.Types.VARCHAR);
		cs.setString(2, strAnswer);
		cs.execute();
		strAnswer = cs.getString(2);
		cs.close();
		return strAnswer.equals("Y");
		
	}
	
	/**
	 *  General utility to check to see if  this person has yet to verify his bc
	 *  
	 * @param _sfname
	 * @param _slname
	 * @param _sDOB
	 * @return
	 * @throws SQLException 
	 */
	public boolean isPersonPlayer(int _idPerson) throws SQLException {

		String strAnswer = "N";
		CallableStatement cs = this.prepareCall("call scaha.isPersonPlayer(?,?)");
		cs.setInt(1, _idPerson);
		cs.registerOutParameter(2, java.sql.Types.VARCHAR);
		cs.setString(2, strAnswer);
		cs.execute();
		strAnswer = cs.getString(2);
		cs.close();
		return strAnswer.equals("Y");
		
	}
	
	/**
	 *  General utility to check to see if  this person has yet to verify his bc
	 *  
	 * @param _sfname
	 * @param _slname
	 * @param _sDOB
	 * @return
	 * @throws SQLException 
	 */
	public boolean isTeamEmpty(int _idTeam) throws SQLException {

		String strAnswer = "N";
		CallableStatement cs = this.prepareCall("call scaha.isTeamEmpty(?,?)");
		cs.setInt(1, _idTeam);
		cs.registerOutParameter(2, java.sql.Types.VARCHAR);
		cs.setString(2, strAnswer);
		cs.execute();
		strAnswer = cs.getString(2);
		cs.close();
		return strAnswer.equals("Y");
		
	}

	public void updateClubPresident(Profile profile, Club _c, Person _oldPrez, Person _newPrez) throws SQLException {
		CallableStatement cs = this.prepareCall(ScahaDatabase.cs_UPDATE_CLUB_STAFFER);
		cs.setInt(1, _c.ID);
		cs.setInt(2, (_oldPrez == null ? 0 :_oldPrez.ID));
		cs.setInt(3, _newPrez.ID);
		cs.setString(4,"C-PRES");
		cs.execute();
		cs.close();
	}

	public void updateClubRegistrar(Profile profile, Club _c, Person _oldPrez, Person _newPrez) throws SQLException {
		CallableStatement cs = this.prepareCall(ScahaDatabase.cs_UPDATE_CLUB_STAFFER);
		cs.setInt(1, _c.ID);
		cs.setInt(2, (_oldPrez == null ? 0 :_oldPrez.ID));
		cs.setInt(3, _newPrez.ID);
		cs.setString(4,"C-REG");
		cs.execute();
		cs.close();
	}

	public void updateClubIceConvenor(Profile profile, Club _c, Person _oldPrez, Person _newPrez) throws SQLException {
		CallableStatement cs = this.prepareCall(ScahaDatabase.cs_UPDATE_CLUB_STAFFER);
		cs.setInt(1, _c.ID);
		cs.setInt(2, (_oldPrez == null ? 0 :_oldPrez.ID));
		cs.setInt(3, _newPrez.ID);
		cs.setString(4,"C-ICE");
		cs.execute();
		cs.close();
	}

	public List<InternetAddress> getClubFamilyEmails(Club c,	GeneralSeason _cs)  throws SQLException {
		List<InternetAddress> tmp = new ArrayList<InternetAddress>();
		PreparedStatement ps = this.prepareStatement("call scaha.getAllMemberEmailsByClubAndSeason(?,?)");
		ps.setInt(1, c.ID);
		ps.setString(2,_cs.getTag()); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			LOGGER.info("getCLubFamilyEmails:" + rs.getString(1) + ":" +  rs.getString(2));
			try {
				tmp.add(new InternetAddress(rs.getString(2),rs.getString(1)));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.info(e.getMessage());
			}
		}
		rs.close();
		ps.close();

		return tmp;
	}

	public List<InternetAddress> getRenegadeFamilyEmails(GeneralSeason currentSeason) throws SQLException, UnsupportedEncodingException {
		
		List<InternetAddress> tmp = new ArrayList<InternetAddress>();
		
		PreparedStatement ps = this.prepareStatement("call scaha.getAllMemberEmailsByNoTeamAndSeason(?)");
		// TODO
		ps.setString(1,currentSeason.getTag()); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			LOGGER.info("getRenegadeFamilyEmails:" + rs.getString(1) + ":" +  rs.getString(2));
			tmp.add(new InternetAddress(rs.getString(2),rs.getString(1)));
		}
		rs.close();
		ps.close();

		return tmp;
	}

	public List<InternetAddress> getOutstandingMemberSignupEmails() throws SQLException, UnsupportedEncodingException {

		List<InternetAddress> tmp = new ArrayList<InternetAddress>();
		
		PreparedStatement ps = this.prepareStatement("call scaha.getOutstandingMemberSignupEmails()");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			LOGGER.info("getOutstandingMemberSignupEmails:" + rs.getString(1) + ":" +  rs.getString(2));
			tmp.add(new InternetAddress(rs.getString(2),rs.getString(1)));
		}
		rs.close();
		ps.close();

		return tmp;
	}
	
	
	/**
	 * This is a DB manipulation Method that syncs all the Teams to a Certain Schedule
	 * Based upon what they have declared.. 
	 * 
	 * @param _sc
	 */
	public void syncTeamsToSchedule (Schedule _sc, GeneralSeason _gs) throws SQLException {
		
		LOGGER.info("Searching for new teams and defunct teams for Schedule " + _sc);
		
		PreparedStatement ps = this.prepareStatement("call scaha.genParticipantChanges(?,?,?)");
		CallableStatement csAdd = this.prepareCall("call scaha.addParticipantToSchedule(?,?)");
		CallableStatement csRemove = this.prepareCall("call scaha.removeParticipantFromSchedule(?,?)");
		ps.setInt(1, _sc.ID);
		ps.setString(2,_sc.getTag());
		ps.setString(3,_gs.getTag());
		ResultSet rs = ps.executeQuery();
		
		ReturnDataResultSet rdrs = ReturnDataResultSet.NewReturnDataResultSetFactory(rs);
		rs.close();

		for (ReturnDataRow row : rdrs) {
			String sCode = (String)row.elementAt(0);
			int idTeam = Integer.parseInt(row.elementAt(1).toString());
			String sName = (String)row.elementAt(2);
			LOGGER.info("Hello There:" + sCode + ":" + idTeam + ":" + sName);
			if (sCode.equals("D")) {
				LOGGER.info("Found a defunct team id[" + idTeam + "] cleaning up for schedule " + _sc);
				csRemove.setInt(1,idTeam);
				csRemove.setInt(2,_sc.ID);
				csRemove.execute();
			} else if (sCode.equals("A")) {
				LOGGER.info("Adding Team [" + sName + "] to the participants for schedule: " + _sc.getTag());
				csAdd.setInt(1,idTeam);
				csAdd.setInt(2,_sc.ID);
				csAdd.execute();
			}
		}
		
		ps.close();
		csAdd.close();
		csRemove.close();
		this.cleanup();
		
		LOGGER.info("sychTeamSeeding: Reviewing rankings to ensure they are sequential for Schedule: " + _sc);
		//
		// We just keep reexecuting this until we are good.
		//
		PreparedStatement ps1 = this.prepareStatement("call scaha.getParticipantKeysAndRankBySchedule(?)");
		CallableStatement csFixGap = this.prepareCall("call scaha.updateParticipantRankingForSchedule(?,?,?,?)");
		ps1.setInt(1,_sc.ID);
		
		boolean bwork = true;
		while (bwork) {
			bwork = false;

			int itargetrank = 0;
			int irank = 1;
			int ioldrank = 0;
			int ikey = 0;
			
			//
			// rerun sql..
			//
			rs = ps1.executeQuery();
			while (rs.next()) {
				ioldrank = rs.getInt(2);
				if (irank != ioldrank && itargetrank == 0 ) {
					itargetrank = irank;
					LOGGER.info("syncTeamSeeding: Found a ranking gap in Schedule " + _sc  + ". " + rs.getInt(1) + ":" + rs.getInt(2) + ": should be:" + irank);
					bwork = true;
				}
				ikey = rs.getInt(1);
				irank++;
			}	
			rs.close();

			//
			// If there is work to do.. then we do it.
			//
			if (bwork) {
				LOGGER.info("Re-Gapping Participate " + ikey + "] from rank [" + ioldrank + ":" + itargetrank + "] for schedule: " + _sc);
				csFixGap.setInt(1,ikey);
				csFixGap.setInt(2,_sc.ID);
				csFixGap.setInt(3,ioldrank);
				csFixGap.setInt(4,itargetrank);
				csFixGap.execute();
			}
		}
		
		ps1.close();
		csFixGap.close();

		CallableStatement csTcs = this.prepareCall("call scaha.updateScheduleTeamCounts(?)");
		csTcs.setInt(1,_sc.ID);
		csTcs.execute();
		csTcs.close();
		this.cleanup();
		
	}
	
	
	
	/**
	 * genGames
	 * 
	 * This will take a look at the season.. how many teams.. and how many games they have to play.. and generate 
	 * enough games to cover the season through iteration...
	 * 
	 * if the iteration of games are smaller than the number of teams based upon the round robin matrix
	 * then we generate another iteration.. this makes sure we have enough games to pull from to piece the matchups 
	 * together.
	 * 
	 * We always want more games to choose from then possible games for the weekend.
	 * @param _se
	 */
	public void genGames(Schedule _sc) throws SQLException {
		
		LOGGER.info("genGames: Starting to Gen games for Schedule "  + _sc);
		CallableStatement csDelete = this.prepareCall("call scaha.removeGamesByScheduleIteration(?,?)");
		CallableStatement csInsert = this.prepareCall("call scaha.insertGamesByScheduleIteration(?,?)");
		
		// We will always have at least one iteration.
		int iCount = 0;
		int iGames = _sc.getMingamecnt();
		int iGamesPerIteration = _sc.getTeamcount() - 1 ;
		
		LOGGER.info("genGames: Check for " + _sc + ". iCount=" + iCount + ": iGames=" + _sc.getMingamecnt() + ": Team Count-1=" + (_sc.getTeamcount() - 1));
			
		//
		// Lets calculate the number of iterations now..
		//
		while (iGames > (iGamesPerIteration*iCount)) {
			iCount++;
			int i=1;
			csInsert.setInt(i++, _sc.ID);
			csInsert.setInt(i++, iCount);
			csInsert.execute();
			LOGGER.info("genGames: Inserted missing games for iteration :" + iCount + ": for schedule "  + _sc);
		}
		
		csDelete.close();
		csInsert.close();
		
		LOGGER.info("genGames: Inserted missing games finished for season " + _sc);
						
	}	
}
	

