// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.app;

import androidx.appcompat.view.ActionMode;

public interface AppCompatCallback
{
    void onSupportActionModeFinished(final ActionMode p0);
    
    void onSupportActionModeStarted(final ActionMode p0);
    
    ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback p0);
}
