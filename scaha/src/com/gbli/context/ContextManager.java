/**
 * 
 */
package com.gbli.context;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import com.gbli.common.SendMailSSL;
import com.gbli.connectors.Database;
import com.gbli.connectors.DatabasePool;
import com.gbli.connectors.ScahaDatabase;
import com.gbli.logging.MyLogger;

/**
 * @author dbigelow
 * 
 */
public class ContextManager implements ServletContextListener {

	
	//
	// Class Info
	//
	private static final long serialVersionUID = 1L;
	private static final String ATTRIBUTE_NAME = "config";
	private static boolean c_bLoaded = false;
	private static String c_sPath = null;
	private static ServletContext c_context;
	private static String c_sLoggerContext = null;	// Used to determine logger Name
	private static Logger c_Logger = null;  // Used to initailize the logger
	private static Hashtable<String, DatabasePool> c_hDBPools = new Hashtable<String, DatabasePool>();  // Used to hold a map of database pools
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent _contextEvent) {
		// TODO Auto-generated method stub
		
		 synchronized (this) {

			if (!c_bLoaded) {

				
				// Lets get this object into the context
				c_context = _contextEvent.getServletContext();
				c_context.setAttribute(ATTRIBUTE_NAME, this);
				// Lets get the context class variables all squared away
				c_sPath = c_context.getRealPath("/");
				c_sLoggerContext = "scaha";

				
				//
				// STEP 1 Lets Get the Logger Established right now
				//
				c_Logger = Logger.getLogger(c_sLoggerContext);

				try {
					MyLogger.setup();
				} catch (Exception e) {
					e.printStackTrace();
				}

				c_Logger.info("Context Created...");
				c_Logger.info("AbsolutePath is: " + c_context.getRealPath("/"));
				c_Logger.info("Check List Initiated...");

				// Set up Scaha Database Database Pool 
				// Lets always default to two connections in the pool..
				//
				Integer ipc = 2;
				try {
					Context ic;
					ic = new InitialContext();
					ipc = (Integer) ic.lookup("java:comp/env/scaha/dbpoolcount");
					c_Logger.info("**** Pool Count is..." + ipc);
					
					String sMailAuth = (String)ic.lookup("java:comp/env/scaha/emailauth");
					String[] saMailAuth = sMailAuth.split(":");
					SendMailSSL.setUsername(saMailAuth[0]);
					SendMailSSL.setPassword(saMailAuth[1]);
					c_Logger.info("***** Mail Info is:" + SendMailSSL.getUsername() + ", " + SendMailSSL.getPassword());
					ic.close();
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//
				DatabasePool dbp2 = new DatabasePool(ScahaDatabase.class.getSimpleName(),ipc.intValue());
				c_hDBPools.put(ScahaDatabase.class.getSimpleName(),dbp2);
				Thread th2 =  new Thread(dbp2);
				dbp2.setMyThread(th2);
				th2.start();
			
				c_Logger.info("Check List Completed...");
				c_bLoaded = true;
				
			} else {
				
				c_Logger.info("Systems already Initialized...");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent _contextEvent) {
		// TODO Auto-generated method stub

		//
		// Shut down all the databasebpool Thread
		
		for (Enumeration<DatabasePool> en = c_hDBPools.elements(); en.hasMoreElements();) {
			DatabasePool dp = en.nextElement();
			c_Logger.info("Found a Database Pool to Shut Down!");
			Thread th = dp.getMyThread();
			dp.shutdown();
			th.interrupt();
			try {
				c_Logger.info("Joining Database Pool Thread for shutdown wait...");
				th.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		c_Logger.info("Context Destroyed...");

	}

	
	//
	// Returns the ContextManagerObject
	//
	public static ContextManager getInstance() {
		return (ContextManager) c_context.getAttribute(ATTRIBUTE_NAME);
	}

	public static String getRealPath() {
		return c_sPath;
	}
	
	public static String getLoggerContext() {
		return c_sLoggerContext;
	}
	
	//
	// This hands out a good database connection
	//
	public static Database getDatabase(String _sClassName) {
		return c_hDBPools.get(_sClassName).getDatabase();
	}
	

}
