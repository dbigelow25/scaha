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
	public FamilyMember (int _id, int _ifmid, String _relationship) {
		super(_id);
		this.ifmID = _ifmid;
		this.Relationship = _relationship;
	}
	
	public String getRelationship() {
		return Relationship;
	}
	
	
}
