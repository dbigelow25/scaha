package com.scaha.objects;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

/**
 * Ok.. This is a great way for 
 * @author David
 *
 */
public class TeamList extends ListDataModel<ScahaTeam> implements Serializable, SelectableDataModel<ScahaTeam> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	public TeamList() {  
    }  
  
    public TeamList(List<ScahaTeam> data) {  
        super(data);  
    }  
    
    
    /**
	 * This will get all ScahaNews Items and return a newsItemList
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static TeamList NewTeamListFactory(Profile _pro, ScahaDatabase _db, Club _cl, GeneralSeason _gs,boolean _badmin, boolean _roster) throws SQLException {
		
		List<ScahaTeam> data = new ArrayList<ScahaTeam>();
		//https://www.google.com/maps/@34.160681,-118.3113211,17z
		
		PreparedStatement ps = _db.prepareStatement("call scaha.getAllCoachByTeam(?)");
		PreparedStatement ps2 = _db.prepareStatement("call scaha.getAllManagerByTeam(?)");
		
		Vector<Object> v = new Vector<Object>();
		v.add(new Integer(_cl.ID));
		v.add( _gs.getTag());
		
		if (_db.getData("call scaha.getAllTeamsByClubAndSeason(?,?)",v)) {
			
			int y = 1;
			ResultSet rs = _db.getResultSet();
			while (rs.next()) {
				int i = 1;
				ScahaTeam tm = new ScahaTeam(_pro,rs.getInt(i++));
				tm.setTeamname(rs.getString(i++));
				tm.setTeamgender(rs.getString(i++));
				int idSkill = rs.getInt(i++);
				tm.setSkillleveltag(rs.getString(i++));
				int idDivs = rs.getInt(i++);
				tm.setDivisiontag(rs.getString(i++));
				tm.setIsexhibition(rs.getInt(i++));
				tm.setSeasontag(rs.getString(i++));
				tm.setScheduletags(rs.getString(i++));
				int idSkill2 = rs.getInt(i++);
				SkillLevel sl = new SkillLevel(_pro, idSkill);
				sl.setSkilllevelname(rs.getString(i++));
				sl.setTag(rs.getString(i++));
				Division div = new Division(_pro,idDivs);
				int idiv2 = rs.getInt(i++);
				div.setDivisionname(rs.getString(i++));
				div.setTag(rs.getString(i++));
				tm.setSdivision(div.getDivisionname());
				tm.setSskilllevel(sl.getSkilllevelname());
				//
				// We will worry about creating the other objects later.. they are reference objects
				// and we need to be carefull not to create a ton of copies floating around
				//
				// they are the skilllevel, divisions, and seasons.
				tm.setTeamdivision(div);
				tm.setTeamskilllevel(sl);
				data.add(tm);
			}
			rs.close();
			
			//
			// ok.. if we need to get all the admin staff.. then list go get this right now!
			//
			// For any team.. the admin staff is coaches.. assistant Coaches.. and Managers
			// for any team..
			if (_badmin) {
				for (ScahaTeam tm : data) {
					tm.setCoachs(CoachList.NewCoachListFactory(_pro, ps, tm));
					tm.setManagers(ManagerList.NewManagerListFactory(_pro, ps2, tm));
				}
			}
				
		}

		ps.close();
		ps2.close();
		LOGGER.info("Finished all Team List Loading!!");
		return new TeamList(data);
	}

      
    @Override  
    public ScahaTeam getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<ScahaTeam> results = (List<ScahaTeam>) getWrappedData();  
        for(ScahaTeam result : results) {  
        	if(Integer.toString(result.ID).equals(rowKey)) return result;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ScahaTeam result) {  
        return Integer.toString(result.ID);  
    }  
	
}