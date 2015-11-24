package com.qiao.EBServer;

import org.apache.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger LOGGER = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	LOGGER.debug("debug");
    	LOGGER.info("info");
    	LOGGER.warn("warn");
    	LOGGER.error("error");
    	LOGGER.fatal("fatal");
    }
}
