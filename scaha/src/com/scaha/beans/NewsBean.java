package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMemberDataModel;
import com.scaha.objects.MailableObject;
import com.scaha.objects.NewsItemList;
import com.scaha.objects.Profile;

public class NewsBean implements Serializable,  MailableObject  {

	//
	// Class Level Variables
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	//
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

	 public NewsItemList getNewsItemList() {
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		NewsItemList nil = null;
		try {
			nil = NewsItemList.NewsItemListFactory(new Profile(-1), db, "12/12/2013");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		db.free();
		return nil;
		
	 }
}
