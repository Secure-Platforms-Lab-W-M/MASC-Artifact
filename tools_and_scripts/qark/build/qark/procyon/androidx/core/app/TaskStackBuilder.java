// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import androidx.core.content.ContextCompat;
import java.util.Iterator;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.app.PendingIntent;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import java.util.ArrayList;
import android.content.Intent;

public final class TaskStackBuilder implements Iterable<Intent>
{
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents;
    private final Context mSourceContext;
    
    private TaskStackBuilder(final Context mSourceContext) {
        this.mIntents = new ArrayList<Intent>();
        this.mSourceContext = mSourceContext;
    }
    
    public static TaskStackBuilder create(final Context context) {
        return new TaskStackBuilder(context);
    }
    
    @Deprecated
    public static TaskStackBuilder from(final Context context) {
        return create(context);
    }
    
    public TaskStackBuilder addNextIntent(final Intent intent) {
        this.mIntents.add(intent);
        return this;
    }
    
    public TaskStackBuilder addNextIntentWithParentStack(final Intent intent) {
        ComponentName componentName;
        if ((componentName = intent.getComponent()) == null) {
            componentName = intent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (componentName != null) {
            this.addParentStack(componentName);
        }
        this.addNextIntent(intent);
        return this;
    }
    
    public TaskStackBuilder addParentStack(final Activity activity) {
        Intent supportParentActivityIntent = null;
        if (activity instanceof SupportParentable) {
            supportParentActivityIntent = ((SupportParentable)activity).getSupportParentActivityIntent();
        }
        Intent parentActivityIntent;
        if ((parentActivityIntent = supportParentActivityIntent) == null) {
            parentActivityIntent = NavUtils.getParentActivityIntent(activity);
        }
        if (parentActivityIntent != null) {
            ComponentName componentName;
            if ((componentName = parentActivityIntent.getComponent()) == null) {
                componentName = parentActivityIntent.resolveActivity(this.mSourceContext.getPackageManager());
            }
            this.addParentStack(componentName);
            this.addNextIntent(parentActivityIntent);
        }
        return this;
    }
    
    public TaskStackBuilder addParentStack(final ComponentName componentName) {
        final int size = this.mIntents.size();
        try {
            for (Intent intent = NavUtils.getParentActivityIntent(this.mSourceContext, componentName); intent != null; intent = NavUtils.getParentActivityIntent(this.mSourceContext, intent.getComponent())) {
                this.mIntents.add(size, intent);
            }
            return this;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException((Throwable)ex);
        }
    }
    
    public TaskStackBuilder addParentStack(final Class<?> clazz) {
        return this.addParentStack(new ComponentName(this.mSourceContext, (Class)clazz));
    }
    
    public Intent editIntentAt(final int n) {
        return this.mIntents.get(n);
    }
    
    @Deprecated
    public Intent getIntent(final int n) {
        return this.editIntentAt(n);
    }
    
    public int getIntentCount() {
        return this.mIntents.size();
    }
    
    public Intent[] getIntents() {
        final Intent[] array = new Intent[this.mIntents.size()];
        if (array.length == 0) {
            return array;
        }
        array[0] = new Intent((Intent)this.mIntents.get(0)).addFlags(268484608);
        for (int i = 1; i < array.length; ++i) {
            array[i] = new Intent((Intent)this.mIntents.get(i));
        }
        return array;
    }
    
    public PendingIntent getPendingIntent(final int n, final int n2) {
        return this.getPendingIntent(n, n2, null);
    }
    
    public PendingIntent getPendingIntent(final int n, final int n2, final Bundle bundle) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
        }
        final ArrayList<Intent> mIntents = this.mIntents;
        final Intent[] array = mIntents.toArray(new Intent[mIntents.size()]);
        array[0] = new Intent(array[0]).addFlags(268484608);
        if (Build$VERSION.SDK_INT >= 16) {
            return PendingIntent.getActivities(this.mSourceContext, n, array, n2, bundle);
        }
        return PendingIntent.getActivities(this.mSourceContext, n, array, n2);
    }
    
    @Deprecated
    @Override
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }
    
    public void startActivities() {
        this.startActivities(null);
    }
    
    public void startActivities(final Bundle bundle) {
        if (!this.mIntents.isEmpty()) {
            final ArrayList<Intent> mIntents = this.mIntents;
            final Intent[] array = mIntents.toArray(new Intent[mIntents.size()]);
            array[0] = new Intent(array[0]).addFlags(268484608);
            if (!ContextCompat.startActivities(this.mSourceContext, array, bundle)) {
                final Intent intent = new Intent(array[array.length - 1]);
                intent.addFlags(268435456);
                this.mSourceContext.startActivity(intent);
            }
            return;
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }
    
    public interface SupportParentable
    {
        Intent getSupportParentActivityIntent();
    }
}
