package com.scaha.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

import com.gbli.context.ContextManager;
  
public class FileUploadController extends ScahaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());
	private HttpServletRequest servletRequest;
	
	public FileUploadController (){ 
		
	}
	
	public Boolean handleFileUpload(FileUploadEvent event,Scoresheet scoresheet) {  
        
		InputStream stream = null;
        FileOutputStream output = null;
        
        String prefix = FilenameUtils.getBaseName(event.getFile().getFileName().replace("#", "")); 
        String suffix = FilenameUtils.getExtension(event.getFile().getFileName());
        
        scoresheet.setFilename(scoresheet.getIdgame() + prefix + "." + suffix);
        scoresheet.setFiledisplayname(prefix + "." + suffix);
        
        try {
        	String destPath = "/var/scaha/scoresheets/" + scoresheet.getGametype() + "_" + scoresheet.getIdgame() + prefix + "." + suffix;
        	
            File destFile = new File(destPath);
        	stream = event.getFile().getInputstream();
        	output = new FileOutputStream(destFile);
        	
        	IOUtils.copy(stream, output);
        	return true;
        }catch (IOException e){
			e.printStackTrace();
			return false;
		} finally {
			IOUtils.closeQuietly(output);
		    IOUtils.closeQuietly(stream);
        }
        
        
    }	
		
}
	