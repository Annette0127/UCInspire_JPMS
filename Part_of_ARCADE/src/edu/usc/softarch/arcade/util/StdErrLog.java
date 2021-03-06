package edu.usc.softarch.arcade.util;

import org.apache.log4j.Logger;

import java.io.PrintStream;


public class StdErrLog {

    private static final Logger logger = Logger.getLogger(StdErrLog.class);

    public static void tieSystemErrToLog() {
        System.setErr(createLoggingProxy(System.err));
    }

    public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                realPrintStream.print(string);
                logger.info(string);
            }
        };
    }
}
