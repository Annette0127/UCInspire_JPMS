package edu.usc.softarch.arcade.util;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.util.Enumeration;

public class LogUtil {
	public static void printLogFiles() {
		Enumeration e = Logger.getRootLogger().getAllAppenders();
	    while ( e.hasMoreElements() ){
	      Appender app = (Appender)e.nextElement();
	      if ( app instanceof FileAppender ){
	        System.out.println("File: " + ((FileAppender)app).getFile());
	      }
	    }
	}
}
