// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.view.View;

class ScrollbarHelper
{
    static int computeScrollExtent(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b) {
        if (layoutManager.getChildCount() == 0 || state.getItemCount() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (!b) {
            return Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1;
        }
        return Math.min(orientationHelper.getTotalSpace(), orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view));
    }
    
    static int computeScrollOffset(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b, final boolean b2) {
        if (layoutManager.getChildCount() == 0 || state.getItemCount() == 0 || view == null) {
            return 0;
        }
        if (view2 == null) {
            return 0;
        }
        final int min = Math.min(layoutManager.getPosition(view), layoutManager.getPosition(view2));
        final int max = Math.max(layoutManager.getPosition(view), layoutManager.getPosition(view2));
        int n;
        if (b2) {
            n = Math.max(0, state.getItemCount() - max - 1);
        }
        else {
            n = Math.max(0, min);
        }
        if (!b) {
            return n;
        }
        return Math.round(n * (Math.abs(orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view)) / (float)(Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1)) + (orientationHelper.getStartAfterPadding() - orientationHelper.getDecoratedStart(view)));
    }
    
    static int computeScrollRange(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b) {
        if (layoutManager.getChildCount() == 0 || state.getItemCount() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (!b) {
            return state.getItemCount();
        }
        return (int)((orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view)) / (float)(Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1) * state.getItemCount());
    }
}
