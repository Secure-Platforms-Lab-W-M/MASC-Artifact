// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper
{
    private final ListView mTarget;
    
    public ListViewAutoScrollHelper(final ListView mTarget) {
        super((View)mTarget);
        this.mTarget = mTarget;
    }
    
    @Override
    public boolean canTargetScrollHorizontally(final int n) {
        return false;
    }
    
    @Override
    public boolean canTargetScrollVertically(final int n) {
        final ListView mTarget = this.mTarget;
        final int count = mTarget.getCount();
        if (count == 0) {
            return false;
        }
        final int childCount = mTarget.getChildCount();
        final int firstVisiblePosition = mTarget.getFirstVisiblePosition();
        if (n > 0) {
            if (firstVisiblePosition + childCount >= count) {
                if (mTarget.getChildAt(childCount - 1).getBottom() <= mTarget.getHeight()) {
                    return false;
                }
            }
        }
        else {
            if (n >= 0) {
                return false;
            }
            if (firstVisiblePosition <= 0) {
                if (mTarget.getChildAt(0).getTop() >= 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void scrollTargetBy(final int n, final int n2) {
        ListViewCompat.scrollListBy(this.mTarget, n2);
    }
}
