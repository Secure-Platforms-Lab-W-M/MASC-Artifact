/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.ListView
 */
package androidx.appcompat.view.menu;

import android.widget.ListView;

public interface ShowableListMenu {
    public void dismiss();

    public ListView getListView();

    public boolean isShowing();

    public void show();
}

