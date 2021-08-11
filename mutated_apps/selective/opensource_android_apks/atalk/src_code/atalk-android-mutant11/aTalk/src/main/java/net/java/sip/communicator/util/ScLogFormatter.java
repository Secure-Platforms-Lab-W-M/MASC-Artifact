/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the LogRecord in a human readable. The summary will
 * typically be on a single line (unless it's too long :) ... what I meant to
 * say is that we don't add any line breaks).
 *
 * @author Emil Ivov
 * @author Eng Chong Meng
 */

public class ScLogFormatter extends java.util.logging.Formatter
{

    /**
     * Program name logging property name
     */
    private static final String PROGRAM_NAME_PROPERTY = ".programname";

    /**
     * Disable timestamp logging property name.
     */
    private static final String DISABLE_TIMESTAMP_PROPERTY = ".disableTimestamp";

    /**
     * Line separator used by current platform
     */
    private static String lineSeparator = System.getProperty("line.separator");

    /**
     * Two digit <tt>DecimalFormat</tt> instance, used to format datetime
     */
    private static DecimalFormat twoDigFmt = new DecimalFormat("00");

    /**
     * Three digit <tt>DecimalFormat</tt> instance, used to format datetime
     */
    private static DecimalFormat threeDigFmt = new DecimalFormat("000");

    /**
     * The application name used to generate this log
     */
    private static String programName;

    /**
     * Whether logger will add date to the logs, enabled by default.
     */
    private static boolean timestampDisabled = false;

    /**
     * The default constructor for <tt>ScLogFormatter</tt> which loads
     * program name property from logging.properties file, if it exists
     */
    public ScLogFormatter()
    {
        loadConfigProperties();
    }

    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    @Override
    public synchronized String format(LogRecord record)
    {
        StringBuilder sb = new StringBuilder();
        if (programName != null) {
            // Program name
            sb.append(programName);
            sb.append(' ');
        }

        if (!timestampDisabled) {
            //current time
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int seconds = cal.get(Calendar.SECOND);
            int millis = cal.get(Calendar.MILLISECOND);

            sb.append(year).append('-');
            sb.append(twoDigFmt.format(month)).append('-');
            sb.append(twoDigFmt.format(day)).append(' ');
            sb.append(twoDigFmt.format(hour)).append(':');
            sb.append(twoDigFmt.format(minutes)).append(':');
            sb.append(twoDigFmt.format(seconds)).append('.');
            sb.append(threeDigFmt.format(millis)).append(' ');
        }

        //log level
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");

        // Thread ID
        sb.append("[" + record.getThreadID() + "] ");

        //caller method
        int lineNumber = inferCaller(record);
        String loggerName = record.getLoggerName();

        if (loggerName == null)
            loggerName = record.getSourceClassName();

        if (loggerName.startsWith("net.java.sip.communicator.")) {
            sb.append(loggerName.substring("net.java.sip.communicator.".length()));
        }
        else
            sb.append(record.getLoggerName());

        if (record.getSourceMethodName() != null) {
            sb.append(".");
            sb.append(record.getSourceMethodName());

            //include the line number if we have it.
            if (lineNumber != -1)
                sb.append("().").append(Integer.toString(lineNumber));
            else
                sb.append("()");
        }
        sb.append(" ");
        sb.append(record.getMessage());
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }

    /**
     * Try to extract the name of the class and method that called the current log statement.
     *
     * @param record the logrecord where class and method name should be stored.
     * @return the line number that the call was made from in the caller.
     */
    private int inferCaller(LogRecord record)
    {
        // Get the stack trace.
        StackTraceElement stack[] = (new Throwable()).getStackTrace();

        //the line number that the caller made the call from
        int lineNumber = -1;

        // First, search back to a method in the SIP Communicator Logger class.
        int ix = 0;
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();
            if (cname.equals("net.java.sip.communicator.util.Logger")) {
                break;
            }
            ix++;
        }
        // Now search for the first frame before the SIP Communicator Logger class.
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            lineNumber = stack[ix].getLineNumber();
            String cname = frame.getClassName();
            if (!cname.equals("net.java.sip.communicator.util.Logger")) {
                // We've found the relevant frame.
                record.setSourceClassName(cname);
                record.setSourceMethodName(frame.getMethodName());
                break;
            }
            ix++;
        }

        return lineNumber;
    }

    /**
     * Loads all config properties.
     */
    private void loadConfigProperties()
    {
        loadProgramNameProperty();
        loadTimestampDisabledProperty();
    }

    /**
     * Checks and loads timestamp disabled property if any.
     */
    private static void loadTimestampDisabledProperty()
    {
        LogManager manager = LogManager.getLogManager();
        String cname = ScLogFormatter.class.getName();
        timestampDisabled = Boolean.parseBoolean(manager.getProperty(cname + DISABLE_TIMESTAMP_PROPERTY));
    }

    /**
     * Load the programname property to be used in logs to identify Jitsi-based
     * application which produced the logs
     */
    private static void loadProgramNameProperty()
    {
        LogManager manager = LogManager.getLogManager();
        String cname = ScLogFormatter.class.getName();
        programName = manager.getProperty(cname + PROGRAM_NAME_PROPERTY);
    }

}
