/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.osgi;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.NavUtils;

import org.atalk.android.aTalkApp;
import org.atalk.android.gui.actionbar.ActionBarUtil;
import org.atalk.android.gui.util.ThemeHelper;
import org.atalk.android.plugin.errorhandler.ExceptionHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Copy of <tt>OSGiActivity</tt> that extends <tt>PreferenceActivity</tt>.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class OSGiPreferenceActivity extends PreferenceActivity
{
    private BundleActivator bundleActivator;

    private BundleContext bundleContext;

    private BundleContextHolder service;

    private ServiceConnection serviceConnection;

    /**
     * EXIT action listener that triggers closes the <tt>Activity</tt>
     */
    private final ExitActionListener exitListener = new ExitActionListener();

    /**
     * List of attached {@link OSGiUiPart}.
     */
    private final List<OSGiUiPart> osgiFrgaments = new ArrayList<>();

    /**
     * Starts this osgi activity.
     *
     * @param bundleContext the osgi <tt>BundleContext</tt>
     * @throws Exception
     */
    private void internalStart(BundleContext bundleContext)
            throws Exception
    {
        this.bundleContext = bundleContext;
        boolean start = false;
        try {
            start(bundleContext);
            start = true;
        } finally {
            if (!start && (this.bundleContext == bundleContext))
                this.bundleContext = null;
        }
    }

    /**
     * Stops this osgi activity.
     *
     * @param bundleContext the osgi <tt>BundleContext</tt>
     * @throws Exception
     */
    private void internalStop(BundleContext bundleContext)
            throws Exception
    {
        if (this.bundleContext != null) {
            if (bundleContext == null)
                bundleContext = this.bundleContext;
            if (this.bundleContext == bundleContext)
                this.bundleContext = null;
            stop(bundleContext);
        }
    }

    /**
     * Called when the activity is starting. Initializes the corresponding call interface.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     * Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState)
    {
        // Always call setTheme() method before super.onCreate(savedInstanceState)
        ThemeHelper.setTheme(this);

        // Hooks the exception handler to the UI thread
        ExceptionHandler.checkAndAttachExceptionHandler();

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Disable up arrow on home activity
            Class<?> homeActivity = aTalkApp.getHomeScreenActivityClass();
            if (this.getClass().equals(homeActivity)) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
            }
            ActionBarUtil.setTitle(this, getTitle());
        }

        super.onCreate(savedInstanceState);
        ServiceConnection serviceConnection = new ServiceConnection()
        {
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                if (this == OSGiPreferenceActivity.this.serviceConnection)
                    setService((BundleContextHolder) service);
            }

            public void onServiceDisconnected(ComponentName name)
            {
                if (this == OSGiPreferenceActivity.this.serviceConnection)
                    setService(null);
            }
        };

        this.serviceConnection = serviceConnection;
        boolean bindService = false;

        try {
            bindService = bindService(new Intent(this, OSGiService.class), serviceConnection, BIND_AUTO_CREATE);
        } finally {
            if (!bindService)
                this.serviceConnection = null;
        }

        // Registers exit action listener
        this.registerReceiver(exitListener, new IntentFilter(aTalkApp.ACTION_EXIT));
    }

    /**
     * Called when an activity is destroyed.
     */
    @Override
    protected void onDestroy()
    {
        // Unregisters exit action listener
        unregisterReceiver(exitListener);
        ServiceConnection serviceConnection = this.serviceConnection;

        this.serviceConnection = null;
        try {
            setService(null);
        } finally {
            if (serviceConnection != null)
                unbindService(serviceConnection);
        }

        super.onDestroy();
    }

    protected void onPause()
    {
        // Clear the references to this activity.
        clearReferences();
        super.onPause();
    }

    protected void onResume()
    {
        super.onResume();
        aTalkApp.setCurrentActivity(this);
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        aTalkApp.setCurrentActivity(this);
    }

    private void setService(BundleContextHolder service)
    {
        if (this.service != service) {
            if ((this.service != null) && (bundleActivator != null)) {
                try {
                    this.service.removeBundleActivator(bundleActivator);
                    bundleActivator = null;
                } finally {
                    try {
                        internalStop(null);
                    } catch (Throwable t) {
                        if (t instanceof ThreadDeath)
                            throw (ThreadDeath) t;
                    }
                }
            }

            this.service = service;

            if (this.service != null) {
                if (bundleActivator == null) {
                    bundleActivator = new BundleActivator()
                    {
                        public void start(BundleContext bundleContext)
                                throws Exception
                        {
                            internalStart(bundleContext);
                        }

                        public void stop(BundleContext bundleContext)
                                throws Exception
                        {
                            internalStop(bundleContext);
                        }
                    };
                }
                this.service.addBundleActivator(bundleActivator);
            }
        }
    }

    protected void start(BundleContext bundleContext)
            throws Exception
    {
        // Starts children OSGI fragments.
        for (OSGiUiPart osGiFragment : osgiFrgaments) {
            osGiFragment.start(bundleContext);
        }
    }

    protected void stop(BundleContext bundleContext)
            throws Exception
    {
        // Stops children OSGI fragments.
        for (OSGiUiPart osGiFragment : osgiFrgaments) {
            osGiFragment.stop(bundleContext);
        }
    }

    /**
     * Registers child <tt>OSGiUiPart</tt> to be notified on startup.
     *
     * @param fragment child <tt>OSGiUiPart</tt> contained in this <tt>Activity</tt>.
     */
    public void registerOSGiFragment(OSGiUiPart fragment)
    {
        osgiFrgaments.add(fragment);

        if (bundleContext != null) {
            // If context exists it means we have started already,
            // so start the fragment immediately
            try {
                fragment.start(bundleContext);
            } catch (Exception e) {
                Timber.e(e, "Error starting OSGiFragment");
            }
        }
    }

    /**
     * Unregisters child <tt>OSGiUiPart</tt>.
     *
     * @param fragment the <tt>OSGiUiPart</tt> that will be unregistered.
     */
    public void unregisterOSGiFragment(OSGiUiPart fragment)
    {
        if (bundleContext != null) {
            try {
                fragment.stop(bundleContext);
            } catch (Exception e) {
                Timber.e(e, "Error while trying to stop OSGiFragment");
            }
        }
        osgiFrgaments.remove(fragment);
    }

    /**
     * Convenience method which starts a new activity for given <tt>activityClass</tt> class
     *
     * @param activityClass the activity class
     */
    protected void startActivity(Class<?> activityClass)
    {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    /**
     * Convenience method that switches from one activity to another.
     *
     * @param activityClass the activity class
     */
    protected void switchActivity(Class<?> activityClass)
    {
        startActivity(activityClass);
        finish();
    }

    /**
     * Convenience method that switches from one activity to another.
     *
     * @param activityIntent the next activity <tt>Intent</tt>
     */
    protected void switchActivity(Intent activityIntent)
    {
        startActivity(activityIntent);
        finish();
    }

    /**
     * Handler for home navigator. Use upIntent if parentActivityName defined. Otherwise execute onBackKeyPressed.
     * Account setting must back to its previous menu (BackKey) to properly save changes
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (upIntent != null) {
                    Timber.w("Process UpIntent for: %s", this.getLocalClassName());
                    NavUtils.navigateUpTo(this, upIntent);
                }
                else {
                    Timber.w("Replace Up with BackKeyPress for: %s", this.getLocalClassName());
                    super.onBackPressed();
                    // Class<?> homeActivity = aTalkApp.getHomeScreenActivityClass();
                    // if (!this.getClass().equals(homeActivity)) {
                    //    switchActivity(homeActivity);
                    // }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Returns OSGI <tt>BundleContext</tt>.
     *
     * @return OSGI <tt>BundleContext</tt>.
     */
    protected BundleContext getBundlecontext()
    {
        return bundleContext;
    }

    /**
     * Returns the content <tt>View</tt>.
     *
     * @return the content <tt>View</tt>.
     */
    protected View getContentView()
    {
        return findViewById(android.R.id.content);
    }

    /**
     * Broadcast listener that listens for {@link aTalkApp#ACTION_EXIT} and then finishes this
     * <tt>Activity</tt>
     * .
     */
    class ExitActionListener extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    }

    private void clearReferences()
    {
        Activity currentActivity = aTalkApp.getCurrentActivity();
        if (currentActivity != null && currentActivity.equals(this))
            aTalkApp.setCurrentActivity(null);
    }
}
