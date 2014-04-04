package com.scaha.objects;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.gbli.common.GeneralAttributes;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
;

public class PersonList extends ListDataModel<Person> implements Serializable, SelectableDataModel<Person> {
	
	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	

	public PersonList() {  
    }  
	
	/**
	 * 
	 * @param data
	 */
    public PersonList(List<Person> data) {  
        super(data);  
    }  

	
	/**
	 * This will get all Coaches for a given Team and Season
	 * @param _db
	 * @return
	 * @throws SQLException 
	 */
	public static PersonList NewPersonListFactory(Profile _pro, ScahaDatabase _db,  UsaHockeyRegistration  _usah) throws SQLException {
	
		
		LOGGER.info ("USAR:" + _usah.toString());

		int loopcount = 0;
		List<Person> data = new ArrayList<Person>();
	
		PreparedStatement ps = _db.prepareStatement("call scaha.getPersonbyUSAHockeyMatchFNLNDOB(?,?,?)");
		LOGGER.info ("USAR:LAST NAME:" + _usah.getFirstName());
		LOGGER.info ("USAR:LAST NAME:" + _usah.getLastName());
		LOGGER.info ("USAR:DOB:" + _usah.getDOB());

		while (true) {
			loopcount++;
			if (loopcount ==1) {
				ps.setString(1,_usah.getFirstName());
				ps.setString(2,_usah.getLastName());
				ps.setString(3,_usah.getDOB());
			} else if (loopcount == 2) {
				ps.setString(1,"NOOP");
				ps.setString(2,_usah.getLastName());
				ps.setString(3,_usah.getDOB());
		//	} else if (loopcount == 3) {
		//		ps.setString(1,_usah.getFirstName());
		//			ps.setString(2,_usah.getLastName());
		//			ps.setString(3,"NOOP");
			} else {
				break;
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int i = 1;
				Person per = new Person(rs.getInt(i++),_pro);
				
				LOGGER.info("Person ID is:" + per.ID);
				per.setsFirstName(rs.getString(i++));
				per.setsLastName(rs.getString(i++));
				per.setsEmail(rs.getString(i++));
				per.setsPhone(rs.getString(i++));
				per.setsAddress1(rs.getString(i++));
				per.setsCity(rs.getString(i++));
				per.setsState(rs.getString(i++));
				per.setiZipCode(rs.getInt(i++));
				per.setGender(rs.getString(i++));
				per.setDob(rs.getString(i++));
				per.setCitizenship(rs.getString(i++));
				String strRelType = rs.getString(i++);
				int tmpFamilyid = rs.getInt(i++);
				String strFamilyName = rs.getString(i++);
				String strIsPlayer = rs.getString(i++);
				String strIsCoach = rs.getString(i++);
				String strIsManager = rs.getString(i++);
				String strIsGoalie = rs.getString(i++);
				
				boolean found = false;
				for(Person result : data) {  
		            if(result.ID == per.ID) {
		            	found=true;
		            	per = result;
		            	break;
			        }  
				}
				
				if (!found) { 
					data.add(per);
					String  strNotes = "";
					if (tmpFamilyid != _pro.getPerson().getFamily().ID) {
						strNotes = strNotes + "The person is <b>*** NOT ***</b> part of your family account<br/>";
						per.getGenatt().put("INFAMILY","false");
					} else {
						per.getGenatt().put("INFAMILY","true");
					}
					if (per.ID == _pro.getPerson().ID) {
						strNotes = strNotes + "This is a selfie (meaning It is you)<br/>";
					}
					per.getGenatt().put("NOTES", strNotes);
					per.getGenatt().put("RELTYPE",strRelType);
					per.getGenatt().put("FAMILYNAME",strFamilyName);
					per.getGenatt().put("ISPLAYER",strIsPlayer);
					per.getGenatt().put("ISCOACH",strIsCoach);
					per.getGenatt().put("ISMANAGER",strIsManager);
					per.getGenatt().put("ISGOALIE",strIsGoalie);
				}
			
			}
			rs.close();
			
			//
			// if there we no matches at all.. 
			// we simply create a new record...
			//
		}
		ps.close();


		if (data.isEmpty()) {
			Person per = new Person(_pro);		// This will be the new person
			per.gleanUSAHinfo(_usah);
			per.getGenatt().put("RELTYPE","New Member");
			per.getGenatt().put("FAMILYNAME",_pro.getPerson().getFamily().getFamilyName());
			per.getGenatt().put("NOTES", "<b>THIS IS A NEW MEMBER TO THE SYSTEM</b><br/>");
			per.getGenatt().put("ISPLAYER","N");
			per.getGenatt().put("ISCOACH","N");
			per.getGenatt().put("ISMANAGER","N");
			per.getGenatt().put("ISGOALIE","N");
			per.getGenatt().put("INFAMILY","true");
			data.add(per);
		}

		return new PersonList(data);
	}

    
    @Override  
    public Person getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        @SuppressWarnings("unchecked")
		List<Person> results = (List<Person>) getWrappedData();  
          
        for(Person result : results) {  
            if(Integer.toString(result.ID).equals(rowKey))  
                return result;  
        }  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Person result) {  
        return Integer.toString(result.ID);
    }

	
}
