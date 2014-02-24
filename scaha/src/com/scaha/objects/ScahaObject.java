package com.scaha.objects;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

public abstract class ScahaObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private Profile m_pro = null;
	
	//
	// Scaha Object have Inherent Collections..
	//
	Vector<ScahaObject> vct = new Vector<ScahaObject>();
	Hashtable<String,ScahaObject> hsh = new Hashtable<String, ScahaObject>();
	
	
	//
	//  This represents the Key for the object
	//
	String m_sKey = null;
	int ID = -1;
	
	/**
	 * @return the m_ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param m_ID the m_ID to set
	 */
	public void setID(int _iID) {
		this.ID = _iID;
	}

	/**
	 * This represents the unique Identifier of this object to the whole system
	 * set it carefully...
	 * @param _sKey
	 */
	public void setKey(String _sKey) {
		m_sKey = _sKey;
	}

	/**
	 * Retrieves the unique identifier of this object to the whole system
	 * @return
	 */
	public String getKey() {
		return m_sKey;
	}
	

	public ScahaObject get(int _i, String _strObjClass) {
		return hsh.get(_i + ":" + _strObjClass);
	}
	
	public ScahaObject getAt(int _index) {
		return vct.elementAt(_index);
	}
	
	public int getSize() {
		return vct.size();
	}
	
	public void put(ScahaObject _so) {
		hsh.put(_so.getID() + ":" + _so.getClass().getSimpleName(), _so);
		vct.add(_so);
	}
	
	public void remove (int _id,String _strObjClass) {
		ScahaObject so = hsh.remove(_id + ":" + _strObjClass);
		vct.removeElement(so);
	}

	public void removeAt (int _index) {
		ScahaObject so = vct.elementAt(_index);
		vct.removeElement(so);
		hsh.remove(so.getID() + ":" +so.getClass().getSimpleName());
	}
	
	public List getList() {
		return new ArrayList(vct);
	}

	public void initCollection() {
		
		if (vct != null) {
			vct.removeAllElements();
		}
		
		if (hsh != null) {
			hsh.clear();
		}

		vct = new Vector<ScahaObject>();
		hsh = new Hashtable<String, ScahaObject>();
	}
	
	public void setProfile (Profile _pro) {
		m_pro = _pro;
	}

	public Profile getProfile () {
		return m_pro;
	}

}
