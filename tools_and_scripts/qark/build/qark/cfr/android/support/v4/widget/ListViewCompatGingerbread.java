/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.widget.ListView
 */
package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

class ListViewCompatGingerbread {
    ListViewCompatGingerbread() {
    }

    /*
     * Enabled aggressive block sorting
     */
    static void scrollListBy(ListView listView, int n) {
        View view;
        int n2 = listView.getFirstVisiblePosition();
        if (n2 == -1 || (view = listView.getChildAt(0)) == null) {
            return;
        }
        listView.setSelectionFromTop(n2, view.getTop() - n);
    }
}

