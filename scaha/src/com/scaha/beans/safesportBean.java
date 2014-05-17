package com.scaha.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.gbli.common.ReturnDataResultSet;
import com.gbli.common.ReturnDataRow;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Player;
import com.scaha.objects.PlayerDataModel;

@ManagedBean
@RequestScoped
public class safesportBean implements Serializable  {


	private PlayerDataModel playerlist = null;
	
    
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());


	 @PostConstruct
	 public void init() {
		 
		 LOGGER.info(" *************** POST INIT FOR safesportBean *****************");
		 
		 ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
   		 List<Player> templist = new ArrayList<Player>();
		 
   		 try {
   			CallableStatement cs = db.prepareCall("CALL scaha.getSafeSportRegisteredPersons()");
			ResultSet rs = cs.executeQuery();
    			
			while (rs.next()) {
				String idplayer = rs.getString("idplayer");
				String sfirstname = rs.getString("fname");
				String slastname = rs.getString("lname");
				String scurrentteam = rs.getString("currentteam");
				
				Player oplayer = new Player();
				oplayer.setIdplayer(idplayer);
				oplayer.setFirstname(sfirstname);
				oplayer.setLastname(slastname);
				oplayer.setCurrentteam(scurrentteam);
				
				templist.add(oplayer);
				
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		db.free();
   		db.cleanup();
   		
   		setPlayerlist(new PlayerDataModel(templist));
	 }

	public PlayerDataModel getPlayerlist() {
			return playerlist;
	}

	/**
	 * @param playerlist the playerlist to set
	 */
	public void setPlayerlist(PlayerDataModel playerlist) {
		this.playerlist = playerlist;
	}

	public void closePage(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		context.getExternalContext().redirect("Welcome.xhtml");
    	} catch (Exception e){
			e.printStackTrace();
    	
    	}
	}
}
