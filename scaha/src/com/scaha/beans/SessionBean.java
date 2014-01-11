package com.scaha.beans;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
 
import java.io.Serializable;

@ManagedBean(name = "SessionBean")
@SessionScoped
public class SessionBean implements Serializable {
	 
	private static final long serialVersionUID = 1L;
 
	private String username;
	private Boolean isAuth = false;
	private String origin = null;
 
	public void verifyUserLogin(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			if(!isAuth){
				this.origin = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURL().toString();
				context.getExternalContext().redirect("login.xhtml");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
 
	public String checkLoginForm(){
		try{
			if(username.equals("ciccio")){
 
			username = "ciccio";
			isAuth = true;
			if(origin != null)
				FacesContext.getCurrentInstance().getExternalContext().redirect(origin);
				return "";
 
			}
		}catch( Exception e){
			e.printStackTrace();
		}
		return "";
	}
 
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}
 
	public Boolean getIsAuth() {
		return isAuth;
	}
 
	public void setIsAuth(Boolean isAuth) {
		this.isAuth = isAuth;
	}
 
}