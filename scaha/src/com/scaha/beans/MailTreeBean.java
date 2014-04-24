package com.scaha.beans;

import java.io.Serializable;  
import java.sql.SQLException;
import java.util.logging.Logger;  

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;  
import javax.faces.view.ViewScoped;
import org.primefaces.model.TreeNode;  
import org.primefaces.model.CheckboxTreeNode;  

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Club;
import com.scaha.objects.ClubAdmin;
import com.scaha.objects.ClubList;
import com.scaha.objects.GeneralSeason;
import com.scaha.objects.ScahaTeam;
import com.scaha.objects.TeamList;

@ManagedBean
@ViewScoped
public class MailTreeBean implements Serializable {  
      
	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());  

	
	@ManagedProperty(value="#{scahaBean}")
    private ScahaBean scaha;
	
	@ManagedProperty(value="#{profileBean}")
	private ProfileBean pb;
	
	//
	// The basic constructs here!
	//
    private TreeNode root;  
    private TreeNode[] selectedNodes;  
      
    public MailTreeBean() {  
	
    }  

    @PostConstruct
    public void init() {
    	//
		// Now lets get all the 
		//

		root = new CheckboxTreeNode("iSCAHA Mailing Tree Widget", null);  
		TreeNode clubTop = new CheckboxTreeNode("All Clubs", root); 
		TreeNode adminTop = new CheckboxTreeNode("All Admin", root); 
		ClubList cl = scaha.getScahaClubList();
		
		//
		// Now we focus on all the roles without any parent..
		//
		for (Club c : cl) {
			//
			// we add the club..
			//
			this.addClubChild(c,clubTop);
			
		}
			
    }

    /**
     * Recursive method to add nodes to a tree
     * @param _rl
     * @param _node
     */
    public void addClubChild(Club _cl, TreeNode _node) {
    	
    	TreeNode club = new CheckboxTreeNode(_cl, _node); 
    	TreeNode staffTop = new CheckboxTreeNode("All Staff", club); 
    	TreeNode teamTop = new CheckboxTreeNode("All Teams", club); 
    	
    	for (ClubAdmin ca : _cl.getCal()) {
    		TreeNode node = new CheckboxTreeNode(ca, staffTop); 
    	}
    	
    	//
    	// chances are.. teams may or may not be set yet.. 
    	//
		if (_cl.getScahaTeams() == null) {
			LOGGER.info("The Selected Club Has no Teams Set!:" + _cl);

			GeneralSeason scahags = scaha.getScahaSeasonList().getCurrentSeason();
			ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
			try {
				_cl.setScahaTeams(TeamList.NewTeamListFactory(pb.getProfile(), db, _cl, scahags, true, false));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.free();
		}
    	for (ScahaTeam t : _cl.getScahaTeams()) {
    		TreeNode node = new CheckboxTreeNode(t, teamTop); 
    	}
    }

    public TreeNode getRoot() {  
        return root;  
    }  
  
    public void setRoot(TreeNode root2) {  
        this.root = root2;  
    }  
      
    public TreeNode[] getSelectedNodes() {  
        return selectedNodes;  
    }  
  
    public void setSelectedNodes(TreeNode[] selectedNodes) {  
        this.selectedNodes = selectedNodes;  
    }  
      
    public void displaySelectedMultiple() {  
        if(selectedNodes != null && selectedNodes.length > 0) {  
            StringBuilder builder = new StringBuilder();  
  
            for(TreeNode node : selectedNodes) {  
                builder.append(node.getData().toString());  
                builder.append("<br />");  
            }  
  
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", builder.toString());  
  
            FacesContext.getCurrentInstance().addMessage(null, message);  
        }  
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


	/**
	 * @return the pb
	 */
	public ProfileBean getPb() {
		return pb;
	}


	/**
	 * @param pb the pb to set
	 */
	public void setPb(ProfileBean pb) {
		this.pb = pb;
	}  
}  