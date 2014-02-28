package com.gbli.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.scaha.objects.UsaHockeyRegistration;



public class USAHRegClient {
 
	
	public static void main (String[] args) throws Exception  {
		
		UsaHockeyRegistration reg = USAHRegClient.pullRegistartionRecord("098401125HUMMI");
		System.out.println(reg.toString());
		
	}

	public static UsaHockeyRegistration pullRegistartionRecord(String _strUSAH) throws Exception {
		
		Simple3Des crypt = new Simple3Des();
 
		System.out.println("START:" + crypt.encryptText(_strUSAH));
		
		URL url = new URL("http://scahalookup.azurewebsites.net/ReqUSAH.aspx?usah='" + crypt.encryptText(_strUSAH) + "'");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine = null;
		String answer = new String ();
		while ((inputLine = in.readLine()) != null) {
        	answer = answer + inputLine;
        }	
        in.close();
        
        String[] temp = answer.split("(<textarea name=\"TextArea1\" id=\"TextArea1\" cols=\"200\" rows=\"20\">{1})|(</textarea></body></html>{1})");
        String cyph = crypt.decryptText(temp[1]); 
        
        String [] ans = cyph.split("\\|");
        
        UsaHockeyRegistration myUSAHockey =  new UsaHockeyRegistration(0);
        int i = 2;
        myUSAHockey.setLastName(ans[i++]);
        myUSAHockey.setFirstName(ans[i++]);
        myUSAHockey.setMiddleInit(ans[i++]);
        myUSAHockey.setAddress(ans[i++] + ans[i++]);
        myUSAHockey.setCity(ans[i++]);
        myUSAHockey.setState(ans[i++]);
        myUSAHockey.setZipcode(ans[i++]);
        myUSAHockey.setDOB(ans[i++]);
        myUSAHockey.setCountry(ans[i++]);
        myUSAHockey.setForZip(ans[i++]);
        myUSAHockey.setCitizen(ans[i++]);
        myUSAHockey.setGender(ans[i++]);
        myUSAHockey.setHPhone(ans[i++]);
        myUSAHockey.setBPhone(ans[i++]);
        myUSAHockey.setPGSLName(ans[i++]);
        myUSAHockey.setPGSFName(ans[i++]);
        myUSAHockey.setPGSMName(ans[i++]);
        myUSAHockey.setEmail(ans[i++]);

        return myUSAHockey;
        
		
 
    }
 
}
