// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;

public interface AppCompatCallback
{
    void onSupportActionModeFinished(final ActionMode p0);
    
    void onSupportActionModeStarted(final ActionMode p0);
    
    @Nullable
    ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback p0);
}
