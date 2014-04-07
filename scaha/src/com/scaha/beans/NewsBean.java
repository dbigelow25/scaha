package com.scaha.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.FamilyMemberDataModel;
import com.scaha.objects.MailableObject;
import com.scaha.objects.NewsItem;
import com.scaha.objects.NewsItemList;
import com.scaha.objects.Profile;

@ManagedBean
@ViewScoped
public class NewsBean implements Serializable,  MailableObject  {


	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private NewsItem currentnewsitem = null;
	
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

	 /**
	 * @return the currentnewsitem
	 */
	public NewsItem getCurrentNewsItem() {
		return currentnewsitem;
	}
	
	public String testMerge() {
		
		List<String> myTokens = new ArrayList<String>();
		myTokens.add("FIRSTNAME:David Bigelow");
		return Utils.mailMerge("/mail/test.mail",myTokens);
		
	}
	/**
	 * @param currentnewsitem the currentnewsitem to set
	 */
	public void setCurrentNewsItem(NewsItem currentnewsitem) {
		this.currentnewsitem = currentnewsitem;
	}
	public NewsItemList getNewsItemList() {
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		NewsItemList nil = null;
		try {
			nil = NewsItemList.NewsItemListFactory(scaha.getDefaultProfile(), db, "12/12/2013");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		db.free();
		return nil;
		
	 }
	
	public void updateNewsItem(NewsItem current) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression expression = app.getExpressionFactory().createValueExpression(context.getELContext(), "#{profileBean}", Object.class );
		ProfileBean pb = (ProfileBean) expression.getValue( context.getELContext() );
		ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
		try {
			current.setAuthor(pb.getProfile().getPerson().getsFirstName() + " " + pb.getProfile().getPerson().getsLastName());
			current.update(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		db.free();
		LOGGER.info(current.toString());

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
}
