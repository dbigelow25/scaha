/**
 * 
 */
package com.gbli.connectors;

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
	private static String c_sp_profile = "Call scaha.profile(?,?)";
	private static String c_sp_actionlist = "Call scaha.getActionTree(?)";

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


}


