/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.app;

import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;

public interface AppCompatCallback {
    public void onSupportActionModeFinished(ActionMode var1);

    public void onSupportActionModeStarted(ActionMode var1);

    @Nullable
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1);
}

