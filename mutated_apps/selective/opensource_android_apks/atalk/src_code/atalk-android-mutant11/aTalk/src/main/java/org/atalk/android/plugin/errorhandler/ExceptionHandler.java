/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.plugin.errorhandler;

import android.content.*;

import net.java.sip.communicator.util.Logger;

import org.atalk.android.aTalkApp;
import org.atalk.service.fileaccess.FileCategory;

import java.io.File;

import timber.log.Timber;

/**
 * The <tt>ExceptionHandler</tt> is used to catch unhandled exceptions which occur on the UI
 * <tt>Thread</tt>. Those exceptions normally cause current <tt>Activity</tt> to freeze and the
 * process usually must be killed after the Application Not Responding dialog is displayed. This
 * handler kills Jitsi process at the moment when the exception occurs, so that user don't have
 * to wait for ANR dialog. It also marks in <tt>SharedPreferences</tt> that such crash has
 * occurred. Next time the Jitsi is started it will ask the user if he wants to send the logs.<br/>
 * <p>
 * Usually system restarts Jitsi and it's service automatically after the process was killed.
 * That's because the service was still bound to some <tt>Activities</tt> at the moment when the
 * exception occurred.<br/>
 * <p>
 * The handler is bound to the <tt>Thread</tt> in every <tt>OSGiActivity</tt>.
 *
 * @author Pawel Domas
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler
{
	/**
	 * Parent exception handler(system default).
	 */
	private final Thread.UncaughtExceptionHandler parent;

	/**
	 * Creates new instance of <tt>ExceptionHandler</tt> bound to given <tt>Thread</tt>.
	 *
	 * @param t
	 * 		the <tt>Thread</tt> which will be handled.
	 */
	private ExceptionHandler(Thread t)
	{
		parent = t.getUncaughtExceptionHandler();
		t.setUncaughtExceptionHandler(this);
	}

	/**
	 * Checks and attaches the <tt>ExceptionHandler</tt> if it hasn't been bound already.
	 */
	public static void checkAndAttachExceptionHandler()
	{
		Thread current = Thread.currentThread();
		if (current.getUncaughtExceptionHandler() instanceof ExceptionHandler) {
			return;
		}
		// Creates and binds new handler instance
		new ExceptionHandler(current);
	}

	/**
	 * Marks the crash in <tt>SharedPreferences</tt> and kills the process.
	 * Storage: /data/data/org.atalk.android/files/log/atalk-crash-logcat.txt
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		markCrashedEvent();
		parent.uncaughtException(thread, ex);

		Timber.e(ex, "uncaughtException occurred, killing the process...");

		// Save logcat for more information.
		File logcatFile;
		String logcatFN = new File("log", "atalk-crash-logcat.txt").toString();
		try {
			logcatFile = ExceptionHandlerActivator.getFileAccessService()
					.getPrivatePersistentFile(logcatFN, FileCategory.LOG);
			Runtime.getRuntime().exec("logcat -v time -f " + logcatFile.getAbsolutePath());
		}
		catch (Exception e) {
			Timber.e("Couldn't save crash logcat file.");
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

	/**
	 * Returns <tt>SharedPreferences</tt> used to mark the crash event.
	 *
	 * @return <tt>SharedPreferences</tt> used to mark the crash event.
	 */
	private static SharedPreferences getStorage()
	{
		return aTalkApp.getGlobalContext().getSharedPreferences("crash", Context.MODE_PRIVATE);
	}

	/**
	 * Marks that the crash has occurred in <tt>SharedPreferences</tt>.
	 */
	private static void markCrashedEvent()
	{
		getStorage().edit().putBoolean("crash", true).apply();
	}

	/**
	 * Returns <tt>true</tt> if Jitsi crash was detected.
	 *
	 * @return <tt>true</tt> if Jitsi crash was detected.
	 */
	public static boolean hasCrashed()
	{
		return getStorage().getBoolean("crash", false);
	}

	/**
	 * Clears the "crashed" flag.
	 */
	public static void resetCrashedStatus()
	{
		getStorage().edit().putBoolean("crash", false).apply();
	}
}
