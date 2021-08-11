// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.view.View;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.widget.ListView;

public final class ListViewCompat
{
    private ListViewCompat() {
    }
    
    public static boolean canScrollList(@NonNull final ListView listView, int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            return listView.canScrollList(n);
        }
        final int childCount = listView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (n > 0) {
            n = listView.getChildAt(childCount - 1).getBottom();
            return firstVisiblePosition + childCount < listView.getCount() || n > listView.getHeight() - listView.getListPaddingBottom();
        }
        n = listView.getChildAt(0).getTop();
        return firstVisiblePosition > 0 || n < listView.getListPaddingTop();
    }
    
    public static void scrollListBy(@NonNull final ListView listView, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            listView.scrollListBy(n);
            return;
        }
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition == -1) {
            return;
        }
        final View child = listView.getChildAt(0);
        if (child == null) {
            return;
        }
        listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
    }
}
