/**
 * 
 */
package com.gbli.connectors;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import com.gbli.context.ContextManager;
import java.sql.PreparedStatement;
import org.w3c.dom.ls.LSException;

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
		LOGGER.info("here are the password credentials:" + _sUser + ":" + _sPass);
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
}


