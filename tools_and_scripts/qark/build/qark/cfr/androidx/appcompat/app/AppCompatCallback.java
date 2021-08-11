/*
 * Decompiled with CFR 0_124.
 */
package androidx.appcompat.app;

import androidx.appcompat.view.ActionMode;

public interface AppCompatCallback {
    public void onSupportActionModeFinished(ActionMode var1);

    public void onSupportActionModeStarted(ActionMode var1);

    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1);
}

