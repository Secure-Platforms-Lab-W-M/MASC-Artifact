/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.widget.ListView
 */
package android.support.v4.widget;

import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewCompat;
import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper
extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView listView) {
        super((View)listView);
        this.mTarget = listView;
    }

    @Override
    public boolean canTargetScrollHorizontally(int n) {
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean canTargetScrollVertically(int n) {
        ListView listView = this.mTarget;
        int n2 = listView.getCount();
        if (n2 == 0) {
            return false;
        }
        int n3 = listView.getChildCount();
        int n4 = listView.getFirstVisiblePosition();
        if (n > 0) {
            if (n4 + n3 < n2 || listView.getChildAt(n3 - 1).getBottom() > listView.getHeight()) return true;
            return false;
        }
        if (n >= 0) return false;
        if (n4 > 0 || listView.getChildAt(0).getTop() < 0) return true;
        return false;
    }

    @Override
    public void scrollTargetBy(int n, int n2) {
        ListViewCompat.scrollListBy(this.mTarget, n2);
    }
}

