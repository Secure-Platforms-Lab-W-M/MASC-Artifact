/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.view.ActionProvider
 *  android.view.MenuItem
 *  android.widget.ShareActionProvider
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

@TargetApi(value=14)
@RequiresApi(value=14)
class ShareCompatICS {
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

    ShareCompatICS() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void configureMenuItem(MenuItem menuItem, Activity activity, Intent intent) {
        ActionProvider actionProvider = menuItem.getActionProvider();
        actionProvider = !(actionProvider instanceof ShareActionProvider) ? new ShareActionProvider((Context)activity) : (ShareActionProvider)actionProvider;
        actionProvider.setShareHistoryFileName(".sharecompat_" + activity.getClass().getName());
        actionProvider.setShareIntent(intent);
        menuItem.setActionProvider(actionProvider);
    }
}

