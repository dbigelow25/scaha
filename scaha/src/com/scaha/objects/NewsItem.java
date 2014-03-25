/**
 * 
 */
package com.scaha.objects;

import java.io.Serializable;
import java.util.logging.Logger;

import com.gbli.context.ContextManager;

/**
 * @author David
 *
 */
public class NewsItem extends ScahaObject implements Serializable {

	/**
	 * Get the obligatory stuff for serializable
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String title = null;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewsItem [title=" + title + ", subject=" + subject
				+ ", author=" + author + ", updated=" + updated + ", body="
				+ body + ", ID=" + ID + ", vct=" + vct + ", hsh=" + hsh + "]";
	}

	private String subject = null;
	private String author = null;
	private String updated = null;
	private String body = null;
	
	public NewsItem (Profile _pro) {
		this.ID = -1;
		this.setProfile(_pro);
	}

	public NewsItem (int _id, Profile _pro) {
		this.ID = _id;
		this.setProfile(_pro);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

}
