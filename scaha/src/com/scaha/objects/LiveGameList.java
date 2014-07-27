package com.scaha.objects;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

/**
* This holds all the smarts for a given Schedule.  There are many combinations of schedules between teams and seasons
* In this system
* @author David
*
*/
public class LiveGameList extends ListDataModel<LiveGame> implements Serializable, SelectableDataModel<LiveGame> {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	
	//
	// We need to implement a hashmap so we can directly fetch things we need.
	//
	private HashMap<String, LiveGame> hm = new HashMap<String,LiveGame>();
	
	public LiveGameList(List<LiveGame> _lst,HashMap<String, LiveGame> _hm ) {  
		super(_lst);
		hm = _hm;
   }
	
	public static LiveGameList NewListFactory() {
		return new LiveGameList(new ArrayList<LiveGame>(),new HashMap<String,LiveGame>());
	}

	/**
	 * Here we have to fetch the a list of participants.. (who in turn will be associated with a given Team
	 * Which needs to be retrieved as well.
	 * 
	 * @param _pro
	 * @param _db
	 * @param _sch
	 * @return
	 * @throws SQLException
	 */
	public static LiveGameList NewListFactory (Profile _pro, ScahaDatabase _db, Schedule _sch, TeamList _tl) throws SQLException {
		
		List<LiveGame> data = new ArrayList<LiveGame>();
		HashMap<String, LiveGame> hm = new HashMap<String,LiveGame>();
		
		PreparedStatement ps = _db.prepareStatement("call scaha.getLiveGamesBySchedule(?)");
		ps.setInt(1,_sch.ID);
		ResultSet rs = ps.executeQuery();
		int y = 1;
		while (rs.next()) {
			int i = 1;
			LiveGame live = new LiveGame(rs.getInt(i++),_pro,_sch);
			live.setTypetag(rs.getString(i++));
			live.setStatetag(rs.getString(i++));
			live.setHometeam(_tl.getScahaTeamAt(rs.getInt(i++)));
			live.setHometeamname(rs.getString(i++));
			live.setHomescore(rs.getInt(i++));
			live.setAwayteam(_tl.getScahaTeamAt(rs.getInt(i++)));
			live.setAwayteamname(rs.getString(i++));
			live.setAwayscore(rs.getInt(i++));
			live.setVenuetag(rs.getString(i++));
			live.setSheetname(rs.getString(i++));
			live.setStartdate(rs.getString(i++));
			live.setStarttime(rs.getString(i++));
			data.add(live);
			hm.put(live.ID+"", live);
		}
		rs.close();
		ps.close();
		return new LiveGameList(data,hm);
		
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Participant> getParticipantArray() {
		return (ArrayList<Participant>)this.getWrappedData();
	}
	
	public LiveGame getByKey(int _i) {
		return hm.get(_i+"");
	}
	
	@SuppressWarnings("unchecked")
	public void reset() {
		// TODO Auto-generated method stub
		LOGGER.info("resetting Participant List..");
		((ArrayList<LiveGame>)this.getWrappedData()).clear();
		hm.clear();
	}  
	
	@SuppressWarnings("unchecked")
	public void add(LiveGame _p) {
		LOGGER.info("Adding Participant to List:" + _p);
		((ArrayList<LiveGame>)this.getWrappedData()).add(_p);
		hm.put(_p.ID+"", _p);
	}
	
	public String toString() {
		String answer = "";
		@SuppressWarnings("unchecked")
		List<LiveGame> results = (List<LiveGame>) getWrappedData();  
      for(LiveGame result : results) {  
    	  answer = answer +  "LiveGame: " + result + ContextManager.NEW_LINE;
      }  
      
      return answer;
      
	}

    @Override  
    public LiveGame getRowData(String rowKey) {  
        @SuppressWarnings("unchecked")
		List<LiveGame> results = (List<LiveGame>) getWrappedData();  
        for(LiveGame result : results) {  
        	if(Integer.toString(result.ID).equals(rowKey)) return result;  
        }  
        return null;  
    }  
 
    public LiveGame getLiveGameAtAt(int rowKey) {  
          
        @SuppressWarnings("unchecked")
		List<LiveGame> results = (List<LiveGame>) getWrappedData();  
        for(LiveGame result : results) {  
        	if(result.ID == rowKey) return result;  
        }  
          
        return null;  
    }  
    @Override  
    public Object getRowKey(LiveGame result) {  
        return Integer.toString(result.ID);  
    }


}