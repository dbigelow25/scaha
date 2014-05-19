package com.scaha.objects;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.gbli.common.Utils;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class Member extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String membername = null;
	private String memberrole = null;
	private String memberemail = null;
	private String memberphone = null;
	private String memberaddress = null;
	private Boolean renderaddress = null;
	private Boolean renderphone = null;
	
	public Boolean getRenderaddress() {
		return renderaddress;
	}

	public void setRenderaddress(Boolean fam) {
		this.renderaddress = fam;
	}
	
	public Boolean getRenderphone() {
		return renderphone;
	}

	public void setRenderphone(Boolean fam) {
		this.renderphone = fam;
	}
	
	public String getMembername() {
		return membername;
	}

	public void setMembername(String fam) {
		this.membername = fam;
	}

	public String getMemberemail() {
		return memberemail;
	}

	public void setMemberemail(String fam) {
		this.memberemail = fam;
	}
	
	public String getMemberrole() {
		return memberrole;
	}

	public void setMemberrole(String fam) {
		this.memberrole = fam;
	}
	
	public String getMemberphone() {
		return memberphone;
	}

	public void setMemberphone(String fam) {
		this.memberphone = fam;
	}
	
	public String getMemberaddress() {
		return memberaddress;
	}

	public void setMemberaddress(String fam) {
		this.memberaddress = fam;
	}
}
