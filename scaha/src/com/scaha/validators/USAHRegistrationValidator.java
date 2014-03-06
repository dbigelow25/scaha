package com.scaha.validators;

import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.gbli.connectors.ScahaDatabase;
import com.gbli.context.ContextManager;

public class USAHRegistrationValidator implements Validator {
	
	//
	// Class Level Variables
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	

	public USAHRegistrationValidator() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		if (value == null) {
            return; // Let required="true" handle, if any.
        }

		String sUSAHReg = (String) value;

        if (sUSAHReg.length() != 14) {
        	FacesMessage message = new FacesMessage();
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
        	message.setSummary("USA Hockey Registration.");
        	message.setDetail("This is a 14 Character Registartion Number.. you enter only " + sUSAHReg.length() + " characters");
            throw new ValidatorException(message);
        }
     
    	//
		// ok.. lets check the database to make sure its unique..
		// 

        ScahaDatabase db = (ScahaDatabase) ContextManager.getDatabase("ScahaDatabase");
        
        Vector<String> v = new Vector<String>();
		v.add(sUSAHReg);

		db.getData("CALL scaha.checkForExistingUSAHNum(?)", v);
        
		//
		// iF THE COUNT COMES BACK > 0 THEN SOMEONE ALREADY HAS THAT USA Hockey In play
		// 

		//
		// iF THE COUNT COMES BACK > 0 THEN SOMEONE ALREADY HAS THAT USERNAME
		// 
		try {
			if (db.getResultSet() != null && db.getResultSet().next()){
				if (db.getResultSet().getInt(1) > 0) {
					FacesMessage message = new FacesMessage();
		        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
		        	message.setSummary("USA Hockey Registration.");
		        	message.setDetail("The reg number " + sUSAHReg + " has already been claimed by another member.  You can not reuse this number for more than one member");
		            throw new ValidatorException(message);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage message = new FacesMessage();
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
        	message.setSummary("Database Error.");
        	message.setDetail("The Database is not available at this moment.  Please try back again in a few minutes");
            throw new ValidatorException(message);
		}  finally {
			LOGGER.info("THREE FREE");
			db.free();
		}
	}
}
