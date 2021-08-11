/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.AbsListView
 *  android.widget.ListAdapter
 *  android.widget.ListView
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class ListViewCompat
extends ListView {
    public static final int INVALID_POSITION = -1;
    public static final int NO_POSITION = -1;
    private static final int[] STATE_SET_NOTHING = new int[]{0};
    private Field mIsChildViewEnabled;
    protected int mMotionPosition;
    int mSelectionBottomPadding = 0;
    int mSelectionLeftPadding = 0;
    int mSelectionRightPadding = 0;
    int mSelectionTopPadding = 0;
    private GateKeeperDrawable mSelector;
    final Rect mSelectorRect = new Rect();

    public ListViewCompat(Context context) {
        this(context, null);
    }

    public ListViewCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ListViewCompat(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        try {
            this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
            this.mIsChildViewEnabled.setAccessible(true);
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            return;
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        this.drawSelectorCompat(canvas);
        super.dispatchDraw(canvas);
    }

    protected void drawSelectorCompat(Canvas canvas) {
        if (!this.mSelectorRect.isEmpty()) {
            Drawable drawable2 = this.getSelector();
            if (drawable2 != null) {
                drawable2.setBounds(this.mSelectorRect);
                drawable2.draw(canvas);
                return;
            }
            return;
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.setSelectorEnabled(true);
        this.updateSelectorStateCompat();
    }

    public int lookForSelectablePosition(int n, boolean bl) {
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter != null) {
            if (this.isInTouchMode()) {
                return -1;
            }
            int n2 = listAdapter.getCount();
            if (!this.getAdapter().areAllItemsEnabled()) {
                if (bl) {
                    for (n = java.lang.Math.max((int)0, (int)n); n < n2 && !listAdapter.isEnabled(n); ++n) {
                    }
                } else {
                    for (n = java.lang.Math.min((int)n, (int)(n2 - 1)); n >= 0 && !listAdapter.isEnabled(n); --n) {
                    }
                }
                if (n >= 0) {
                    if (n >= n2) {
                        return -1;
                    }
                    return n;
                }
                return -1;
            }
            if (n >= 0) {
                if (n >= n2) {
                    return -1;
                }
                return n;
            }
            return -1;
        }
        return -1;
    }

    public int measureHeightOfChildrenCompat(int n, int n2, int n3, int n4, int n5) {
        int n6 = this.getListPaddingTop();
        int n7 = this.getListPaddingBottom();
        this.getListPaddingLeft();
        this.getListPaddingRight();
        n3 = this.getDividerHeight();
        Drawable drawable2 = this.getDivider();
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter == null) {
            return n6 + n7;
        }
        if (n3 <= 0 || drawable2 == null) {
            n3 = 0;
        }
        drawable2 = null;
        int n8 = listAdapter.getCount();
        int n9 = 0;
        int n10 = 0;
        n2 = n6 + n7;
        for (int i = 0; i < n8; ++i) {
            int n11 = listAdapter.getItemViewType(i);
            if (n11 != n9) {
                drawable2 = null;
                n9 = n11;
            }
            View view = listAdapter.getView(i, (View)drawable2, (ViewGroup)this);
            drawable2 = view.getLayoutParams();
            if (drawable2 == null) {
                drawable2 = this.generateDefaultLayoutParams();
                view.setLayoutParams((ViewGroup.LayoutParams)drawable2);
            }
            n11 = drawable2.height > 0 ? View.MeasureSpec.makeMeasureSpec((int)drawable2.height, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
            view.measure(n, n11);
            view.forceLayout();
            if (i > 0) {
                n2 += n3;
            }
            if ((n2 += view.getMeasuredHeight()) >= n4) {
                if (n5 >= 0 && i > n5 && n10 > 0 && n2 != n4) {
                    return n10;
                }
                return n4;
            }
            if (n5 >= 0 && i >= n5) {
                n10 = n2;
            }
            drawable2 = view;
        }
        return n2;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mMotionPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        return super.onTouchEvent(motionEvent);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void positionSelectorCompat(int n, View object) {
        boolean bl;
        Rect rect = this.mSelectorRect;
        rect.set(object.getLeft(), object.getTop(), object.getRight(), object.getBottom());
        rect.left -= this.mSelectionLeftPadding;
        rect.top -= this.mSelectionTopPadding;
        rect.right += this.mSelectionRightPadding;
        rect.bottom += this.mSelectionBottomPadding;
        try {
            bl = this.mIsChildViewEnabled.getBoolean((Object)this);
            if (object.isEnabled() == bl) return;
            {
                object = this.mIsChildViewEnabled;
                bl = !bl;
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return;
        }
        object.set((Object)this, bl);
        if (n == -1) return;
        {
            this.refreshDrawableState();
        }
    }

    protected void positionSelectorLikeFocusCompat(int n, View view) {
        Drawable drawable2 = this.getSelector();
        boolean bl = true;
        boolean bl2 = drawable2 != null && n != -1;
        if (bl2) {
            drawable2.setVisible(false, false);
        }
        this.positionSelectorCompat(n, view);
        if (bl2) {
            view = this.mSelectorRect;
            float f = view.exactCenterX();
            float f2 = view.exactCenterY();
            if (this.getVisibility() != 0) {
                bl = false;
            }
            drawable2.setVisible(bl, false);
            DrawableCompat.setHotspot(drawable2, f, f2);
            return;
        }
    }

    protected void positionSelectorLikeTouchCompat(int n, View view, float f, float f2) {
        this.positionSelectorLikeFocusCompat(n, view);
        view = this.getSelector();
        if (view != null && n != -1) {
            DrawableCompat.setHotspot((Drawable)view, f, f2);
            return;
        }
    }

    public void setSelector(Drawable drawable2) {
        GateKeeperDrawable gateKeeperDrawable = drawable2 != null ? new GateKeeperDrawable(drawable2) : null;
        this.mSelector = gateKeeperDrawable;
        super.setSelector((Drawable)this.mSelector);
        gateKeeperDrawable = new Rect();
        if (drawable2 != null) {
            drawable2.getPadding((Rect)gateKeeperDrawable);
        }
        this.mSelectionLeftPadding = gateKeeperDrawable.left;
        this.mSelectionTopPadding = gateKeeperDrawable.top;
        this.mSelectionRightPadding = gateKeeperDrawable.right;
        this.mSelectionBottomPadding = gateKeeperDrawable.bottom;
    }

    protected void setSelectorEnabled(boolean bl) {
        GateKeeperDrawable gateKeeperDrawable = this.mSelector;
        if (gateKeeperDrawable != null) {
            gateKeeperDrawable.setEnabled(bl);
            return;
        }
    }

    protected boolean shouldShowSelectorCompat() {
        if (this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
            return true;
        }
        return false;
    }

    protected boolean touchModeDrawsInPressedStateCompat() {
        return false;
    }

    protected void updateSelectorStateCompat() {
        Drawable drawable2 = this.getSelector();
        if (drawable2 != null && this.shouldShowSelectorCompat()) {
            drawable2.setState(this.getDrawableState());
            return;
        }
    }

    private static class GateKeeperDrawable
    extends DrawableWrapper {
        private boolean mEnabled = true;

        public GateKeeperDrawable(Drawable drawable2) {
            super(drawable2);
        }

        @Override
        public void draw(Canvas canvas) {
            if (this.mEnabled) {
                super.draw(canvas);
                return;
            }
        }

        void setEnabled(boolean bl) {
            this.mEnabled = bl;
        }

        @Override
        public void setHotspot(float f, float f2) {
            if (this.mEnabled) {
                super.setHotspot(f, f2);
                return;
            }
        }

        @Override
        public void setHotspotBounds(int n, int n2, int n3, int n4) {
            if (this.mEnabled) {
                super.setHotspotBounds(n, n2, n3, n4);
                return;
            }
        }

        @Override
        public boolean setState(int[] arrn) {
            if (this.mEnabled) {
                return super.setState(arrn);
            }
            return false;
        }

        @Override
        public boolean setVisible(boolean bl, boolean bl2) {
            if (this.mEnabled) {
                return super.setVisible(bl, bl2);
            }
            return false;
        }
    }

}

