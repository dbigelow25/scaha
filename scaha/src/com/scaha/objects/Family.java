package com.scaha.objects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Family extends ScahaObject implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private Person per = null;
	private List<FamilyMember> FamilyMembers; 
	private String FamilyName = null;
	
	
	public Family (ScahaDatabase _db, Person _per) {

		//
		// since we are using a passed database.. we do not want to free it..
		// we just want to clean up after ourselves..
		//
		
		LOGGER.info("Attempting to create the Family...");
		// who owns the family?
		this.per = _per;
		
		FamilyMembers = new ArrayList<FamilyMember>();
		
		//
		// ok.. lets pull the family info here..
		//
		Vector<Integer> v = new Vector<Integer>();
		v.add(_per.getID());
		ResultSet rs = null;
		try {
			if (_db.getData("call scaha.getFamilyByPersonID(?)", v)) {
				rs = _db.getResultSet();
				boolean bfirst = true;
				while (rs.next()) {
					if (bfirst) {
						this.setID(rs.getInt(1));
						FamilyName = rs.getString(2); // we only need this once
						bfirst = false;
					}
					//
					// ok.. now lets make a family member .. by sticking a person and a responsibility together..
					//
					FamilyMember mem = new FamilyMember(this.getProfile(), rs.getInt(5), rs.getInt(3), rs.getString(4));
					
					mem.setsFirstName(rs.getString(6));
					mem.setsLastName(rs.getString(7));
					mem.setsEmail(rs.getString(8));
					mem.setsPhone(rs.getString(9));
					mem.setsAddress1(rs.getString(10));
					mem.setsCity(rs.getString(11));
					mem.setsState(rs.getString(12));
					mem.setiZipCode(rs.getInt(13));
					mem.setGender(rs.getString(14));
					mem.setDob(rs.getString(15));
					FamilyMembers.add(mem);
				}	
					
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			_db.cleanup();
		}
		
		LOGGER.info("Fam Members are:" + FamilyMembers);
	}
	
	public List<FamilyMember> getFamilyMembers() {
		return FamilyMembers;
	}
}
