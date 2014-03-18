/**
 * 
 */
package com.scaha.objects;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

/**
 * @author David
 *
 */
public class ScahaManager extends Person {

	public ScahaManager(int _id, Profile _pro) {
		super(_id, _pro);
		// TODO Auto-generated constructor stub
	}

	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	//
	// Lets hold the playerID here.. and allow the super to hold the person...
	//
	protected int ID = -1;
	
	/**
	 * A basic constructor that glues a person to a player given the relationship.
	 * This assumes new record status.. 
	 * @param _id
	 * @param _per
	 * @param _relationship
	 */
	public ScahaManager (Profile _pro) {
		
		//
		// Here we are starting with basically an empty shell of an object..
		// all id's are zero
		//
		super(-1, _pro);
		ID = -1;
	}
	
	public ScahaManager(Profile _pro, Person _per) {
		super( _pro, _per);

	}
	

	public int getID() {
		return ID;
	}
	
	public void update(ScahaDatabase _db) throws SQLException {
		
		//
		// Lets update the person here.
		//
		super.update(_db);
		
		//
		// now.. we have to update the pertinat parts of the player table 
		// to add the player extension data..
		
		// 
		// is it an object that is not in the database yet..
		//
		//
		CallableStatement cs = _db.prepareCall("call scaha.updateManager(?,?,?,?)");
		
		LOGGER.info("HERE IS THE PERSON ID for manager:" + super.ID);
		LOGGER.info("HERE IS THE manager  ID for manager:" + this.ID);
		
		int i = 1;
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(i++, this.ID);
		cs.setInt(i++, super.ID);
		cs.setInt(i++,1);
		cs.setString(i++,null);
		cs.execute();
				
		//
		// Update the new ID from the database...
		//
		this.ID = cs.getInt(1);
		cs.close();
		LOGGER.info("HERE IS THE New  Manager ID:" + this.ID);
		
	}

}
