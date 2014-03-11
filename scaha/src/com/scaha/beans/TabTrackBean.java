/**
 * 
 */
package com.scaha.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import com.gbli.context.ContextManager;

/**
 * @author dbigelow
 *
 */
@ViewScoped
public class TabTrackBean  implements Serializable {
	
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	private String mytabindex = "0";
	
	public void onTabChange(TabChangeEvent event) {  
		FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());  
		FacesContext.getCurrentInstance().addMessage(null, msg);  
		TabView tv = (TabView) event.getComponent(); 
//        this.tabindex = String.valueOf(tv.getActiveIndex());
		this.mytabindex = String.valueOf(tv.getActiveIndex());
	}  
	
	public String getTabIndex() {
		return this.mytabindex;
	}

	public void setTabIndex(String _i) {
		this.mytabindex = _i;
	}
}
