package com.scaha.objects;

import java.io.Serializable;

public class FamilyMember extends Person implements Serializable {
	
	private int ifmID = 0;
	private String Relationship = null;
	
	/**
	 * A basic constructor that glues a person to a relationship for a given family.
	 * 
	 * @param _id
	 * @param _per
	 * @param _relationship
	 */
	public FamilyMember ( Profile _pro, int _personid, int _ifamilyid) {
		super(_personid, _pro);
		this.ifmID = _ifamilyid;
	}
	
	public String getRelationship() {
		return Relationship;
	}
	
	public void setRelationship(String _s) {
		this.Relationship = _s;
	}
	
	public void setID(int _i) {
		this.ifmID = _i;
	}
	
	public int getID() {
		return this.ifmID;
	}

}
