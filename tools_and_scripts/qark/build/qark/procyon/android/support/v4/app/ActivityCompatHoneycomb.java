// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(11)
@RequiresApi(11)
class ActivityCompatHoneycomb
{
    static void dump(final Activity activity, final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        activity.dump(s, fileDescriptor, printWriter, array);
    }
    
    static void invalidateOptionsMenu(final Activity activity) {
        activity.invalidateOptionsMenu();
    }
}
