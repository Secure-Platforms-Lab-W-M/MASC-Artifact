// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.view.ActionProvider;
import android.content.Context;
import android.widget.ShareActionProvider;
import android.content.Intent;
import android.app.Activity;
import android.view.MenuItem;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
class ShareCompatICS
{
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";
    
    public static void configureMenuItem(final MenuItem menuItem, final Activity activity, final Intent shareIntent) {
        final ActionProvider actionProvider = menuItem.getActionProvider();
        ShareActionProvider actionProvider2;
        if (!(actionProvider instanceof ShareActionProvider)) {
            actionProvider2 = new ShareActionProvider((Context)activity);
        }
        else {
            actionProvider2 = (ShareActionProvider)actionProvider;
        }
        actionProvider2.setShareHistoryFileName(".sharecompat_" + activity.getClass().getName());
        actionProvider2.setShareIntent(shareIntent);
        menuItem.setActionProvider((ActionProvider)actionProvider2);
    }
}
