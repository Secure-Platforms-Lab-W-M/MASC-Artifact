// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

class ListViewCompatDonut
{
    static void scrollListBy(final ListView listView, final int n) {
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition != -1) {
            final View child = listView.getChildAt(0);
            if (child != null) {
                listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
            }
        }
    }
}
