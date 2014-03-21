/**
 * 
 */
package com.gbli.common;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.RandomStringUtils;

import com.gbli.connectors.DatabasePool;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;
import com.scaha.objects.Family;
import com.scaha.objects.FamilyMember;
import com.scaha.objects.Person;
import com.scaha.objects.Profile;
import com.scaha.objects.ScahaCoach;
import com.scaha.objects.ScahaManager;
import com.scaha.objects.ScahaMember;
import com.scaha.objects.ScahaPlayer;
import com.scaha.objects.UsaHockeyRegistration;


/**
 * @author dbigelow
 *
 */
public class Utils {
	
	
	public static void main (String[] args) throws Exception  {
		
		genAccountsParentPlayer();
		
	}
	
	public static String properCase(String s) {
		Pattern p = Pattern.compile("(^|\\W)([a-z])");
		Matcher m = p.matcher(s.toLowerCase());
		StringBuffer sb = new StringBuffer(s.length());
		while(m.find()) {
			m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase() );
		}
		m.appendTail(sb);
		return sb.toString();
	}

	
	
	public static String getRandom5CharStringUpper() {
		return RandomStringUtils.random(5, true, true).toUpperCase();
	}
	
	
	//
	// All this guy does is generate accounts from the import file given by USAHockey
	//
	public static void genAccountsParentPlayer() throws SQLException {
		
		
		//
		// Get a privat database pool going.
		//
		
		ContextManager.setLogger("LoadUSAInfo");
		
		Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
		
		DatabasePool dbp2 = new DatabasePool(ScahaDatabase.class.getSimpleName(),2);
		Thread th2 =  new Thread(dbp2);
		dbp2.setMyThread(th2);
		th2.start();
		
		ScahaDatabase db = (ScahaDatabase)dbp2.getDatabase();  // Outer loop
		ScahaDatabase db2 = (ScahaDatabase)dbp2.getDatabase(); // Inner Loop
		db2.setAutoCommit(false);

		String mySQL = 
				" select distinct " + 
				" ipf.prim_email, " +
				" ipf.pg1_first, " +
				" ipf.pg1_last, " +
				" concat(ipf.address1,' ', ipf.address2) as address, " +
				" ipf.city, " +
				" ipf.st, " +
				" ipf.zip_code as zip, " +
				" ipf.forzip as forzip, " +
				" ipf.prim_phone, " +
				" ipf.sec_phone, " +
				" ipf.part_email, " +
				" ipf.first_name as first_name, " +
				" ipf.last_name as last_name, " +
				" ipf.mi as mi, " + 
				" DATE_FORMAT(ipf.dob, '%m/%d/%Y') as dob, " +
				" ipf.ver_dob, " +
				" ipf.country as country, " +
				" ipf.gender as gender, " +
				" ipf.citz as citizenship, " +
				" ipf.conf_no as usahockeynumber, " +
				" substring(ipf.conf_no,4,1) as usayear " +
				" from scaha.USADataFile ipf " +
				" where  YEAR(now()) - YEAR(ipf.dob) <= 18 order by 3,2,1,4 ";
		
		db.getData(mySQL);
		int i=1;
		
		//
		// ok.. we are here to create new profiles.. persons, family records.. player records.. scaha records.. and 
		//
		
		Profile pro = null;
		
		while (db.getResultSet().next()) {
			int y=1;
			String email = db.getResultSet().getString(y++).toLowerCase().trim();
			String fname = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String lname = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String addr = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String city = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String st = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String zip = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String forzip = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String phone = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String secphone = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String altemail = db.getResultSet().getString(y++).toLowerCase().trim();
			String kidfirst =Utils.properCase(db.getResultSet().getString(y++)).trim();
			String kidlast = Utils.properCase(db.getResultSet().getString(y++)).trim();
			String kidmi = db.getResultSet().getString(y++).toUpperCase().trim();
			String kiddob = db.getResultSet().getString(y++);
			String verdob = db.getResultSet().getString(y++);
			String country = db.getResultSet().getString(y++);
			String gender = db.getResultSet().getString(y++);
			String citizen = db.getResultSet().getString(y++);
			String usahockey = db.getResultSet().getString(y++);
			String usayear = db.getResultSet().getString(y++);
			
			System.out.println("Here is a row:" + (i++) + " "+ 
			email + ", " +
			fname + ", " +
			lname + ", " +
			addr + ", " +
			city + ", " +
			st + ", " +
			zip + ", " +
			forzip + ", " +
			phone + ", " +
			secphone + ", " + 
			altemail + ", " +
			kidfirst + ", " +
			kidlast + ", " +
			kidmi + ", " +
			kiddob + ", " +
			verdob + ", " +
			country + ", " +
			gender + ", " +
			citizen + ", " +
			usahockey + ", " +
			usayear
			);
			
			int iProfileid = 0;
			String struser = null;
			String strpwd = null;
			String strnn = null;
			
			Vector<String> v = new Vector<String>();
			v.add(email);
			db2.getData("CALL scaha.checkforuser(?)", v);
		        
			//
			// This should always be true..
			//
			if (db2.getResultSet() != null && db2.getResultSet().next()) {
				iProfileid = db2.getResultSet().getInt(1);
				struser = db2.getResultSet().getString(2);
				strpwd = db2.getResultSet().getString(3);
				strnn = db2.getResultSet().getString(4);
			} 

			db2.getResultSet().close();
			db2.cleanup();

			//
			// if profile is still 0 then we need to make a new one.. else.. we need to fetch the existing one..
			//
			
			if (iProfileid > 0 ) {
				
				 LOGGER.info("Profile already exists.. loading it (and Person, family");
				 pro =  new Profile (iProfileid, db2, strnn, struser, strpwd, false);

			} else {

				LOGGER.info("Creating new Profile.. person");

				pro = new Profile(email, email, fname + " " + lname);
				pro.update(db2);
				Person per = new Person(pro);
				pro.setPerson(per);
				per.setsFirstName(fname);
				per.setsLastName(lname);
				per.setsAddress1(addr);
				per.setsCity(city);
				per.setsState(st);
				per.setiZipCode(Integer.parseInt(zip.substring(0,5)));
				per.setsPhone(phone);
				per.setsEmail(altemail);
				per.setGender("U");

				// Lets update now ..
				pro.update(db2);
				per.update(db2);

				Family fam = new Family(-1, pro, per);
				
				//
				// now update the family..  Lets give it the default name!!
				//
				
				LOGGER.info("Creating new Family.");
				fam.setFamilyName("The " + per.getsLastName() + " Family");
				fam.update(db2, false);
				
				//
				// Make sure the person is always them selves
				//
				LOGGER.info("Creating new FamilyMember (self).");
				FamilyMember fm = new FamilyMember(pro,fam, per);
				fm.setRelationship("Self");
				fm.updateFamilyMemberStructure(db2);
			
			}
			
			//
			// ok.. now we have a top solid person.. lets grab all the things we need here...
			//
			
			//
			//  The person already may be there.. lets see if usa hockey # already there.
			//

			v = new Vector<String>();
	        v.add(usahockey);
			db2.getData("CALL scaha.checkForExistingUSAHNum(?)", v);
			
    		//
			// iF THE COUNT COMES BACK > 0 THEN SOMEONE ALREADY HAS THAT USA Hockey In play
			// 
			if (db2.getResultSet() != null && db2.getResultSet().next()){
				if (db2.getResultSet().getInt(1) > 0) {
					LOGGER.info("!!USA HOCKEY NUMBER ALREADY IN SYSTEM.. SKIPPING: " + usahockey);
					db2.commit();
					continue;
				}
			}
			
			Person tper = pro.getPerson();   //  This is their person record
			Family tfam = tper.getFamily();  //  This is their Family Structure
			
			Person per = new Person(pro);		// This will be the new person
			ScahaPlayer sp = null;
			ScahaMember mem = null;
			UsaHockeyRegistration usar = new UsaHockeyRegistration(-1,usahockey);
 			
			usar.setAddress(addr);
			usar.setBPhone(phone);
			usar.setCitizen(citizen);
			usar.setCity(city);
			usar.setCountry(country);
			usar.setDOB(kiddob);
			usar.setEmail(email);
			usar.setFirstName(kidfirst);
			usar.setForZip(forzip);
			usar.setGender(gender);
			usar.setHPhone(secphone);
			usar.setLastName(kidlast);
			usar.setMiddleInit(kidmi);
			usar.setPGSFName(fname);
			usar.setPGSLName(lname);
			usar.setPGSMName("");
			usar.setState(st);
			usar.setUSAHnum(usahockey);
			usar.setZipcode(zip);
			
			//
			// here we make the USAHockey Information available..
			//
			//
			// lets always make a new one!
			//
		
			per.gleanUSAHinfo(usar);
			per.update(db2);
			usar.update(db2, per);
				
			mem = new ScahaMember(pro,per);
			mem.setSCAHAYear(usar.getMemberShipYear());
			mem.generateMembership(db2);
			mem.setTopPerson(tper);
			sp = new ScahaPlayer(pro, per);
			sp.gleanUSAHinfo(usar);
			sp.update(db2);

			FamilyMember fm = new FamilyMember(pro, tfam, per);
			fm.setRelationship("Child");
			fm.updateFamilyMemberStructure(db2);
			db2.commit();

		}
		
		db2.free();
		db.free();
		
		Thread th = dbp2.getMyThread();
		dbp2.shutdown();
		th.interrupt();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
