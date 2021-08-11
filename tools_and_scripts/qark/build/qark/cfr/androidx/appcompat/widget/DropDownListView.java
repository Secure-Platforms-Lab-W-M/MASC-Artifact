/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.AbsListView
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  androidx.appcompat.R
 *  androidx.appcompat.R$attr
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.AutoScrollHelper;
import androidx.core.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;

class DropDownListView
extends ListView {
    public static final int INVALID_POSITION = -1;
    public static final int NO_POSITION = -1;
    private ViewPropertyAnimatorCompat mClickAnimation;
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private Field mIsChildViewEnabled;
    private boolean mListSelectionHidden;
    private int mMotionPosition;
    ResolveHoverRunnable mResolveHoverRunnable;
    private ListViewAutoScrollHelper mScrollHelper;
    private int mSelectionBottomPadding = 0;
    private int mSelectionLeftPadding = 0;
    private int mSelectionRightPadding = 0;
    private int mSelectionTopPadding = 0;
    private GateKeeperDrawable mSelector;
    private final Rect mSelectorRect = new Rect();

    DropDownListView(Context object, boolean bl) {
        super((Context)object, null, R.attr.dropDownListViewStyle);
        this.mHijackFocus = bl;
        this.setCacheColorHint(0);
        try {
            this.mIsChildViewEnabled = object = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
            object.setAccessible(true);
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            return;
        }
    }

    private void clearPressedItem() {
        this.mDrawsInPressedState = false;
        this.setPressed(false);
        this.drawableStateChanged();
        Object object = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
        if (object != null) {
            object.setPressed(false);
        }
        if ((object = this.mClickAnimation) != null) {
            object.cancel();
            this.mClickAnimation = null;
        }
    }

    private void clickPressedItem(View view, int n) {
        this.performItemClick(view, n, this.getItemIdAtPosition(n));
    }

    private void drawSelectorCompat(Canvas canvas) {
        Drawable drawable2;
        if (!this.mSelectorRect.isEmpty() && (drawable2 = this.getSelector()) != null) {
            drawable2.setBounds(this.mSelectorRect);
            drawable2.draw(canvas);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void positionSelectorCompat(int n, View object) {
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

    private void positionSelectorLikeFocusCompat(int n, View view) {
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
        }
    }

    private void positionSelectorLikeTouchCompat(int n, View view, float f, float f2) {
        this.positionSelectorLikeFocusCompat(n, view);
        view = this.getSelector();
        if (view != null && n != -1) {
            DrawableCompat.setHotspot((Drawable)view, f, f2);
        }
    }

    private void setPressedItem(View view, int n, float f, float f2) {
        View view2;
        this.mDrawsInPressedState = true;
        if (Build.VERSION.SDK_INT >= 21) {
            this.drawableHotspotChanged(f, f2);
        }
        if (!this.isPressed()) {
            this.setPressed(true);
        }
        this.layoutChildren();
        int n2 = this.mMotionPosition;
        if (n2 != -1 && (view2 = this.getChildAt(n2 - this.getFirstVisiblePosition())) != null && view2 != view && view2.isPressed()) {
            view2.setPressed(false);
        }
        this.mMotionPosition = n;
        float f3 = view.getLeft();
        float f4 = view.getTop();
        if (Build.VERSION.SDK_INT >= 21) {
            view.drawableHotspotChanged(f - f3, f2 - f4);
        }
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        this.positionSelectorLikeTouchCompat(n, view, f, f2);
        this.setSelectorEnabled(false);
        this.refreshDrawableState();
    }

    private void setSelectorEnabled(boolean bl) {
        GateKeeperDrawable gateKeeperDrawable = this.mSelector;
        if (gateKeeperDrawable != null) {
            gateKeeperDrawable.setEnabled(bl);
        }
    }

    private boolean touchModeDrawsInPressedStateCompat() {
        return this.mDrawsInPressedState;
    }

    private void updateSelectorStateCompat() {
        Drawable drawable2 = this.getSelector();
        if (drawable2 != null && this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
            drawable2.setState(this.getDrawableState());
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        this.drawSelectorCompat(canvas);
        super.dispatchDraw(canvas);
    }

    protected void drawableStateChanged() {
        if (this.mResolveHoverRunnable != null) {
            return;
        }
        super.drawableStateChanged();
        this.setSelectorEnabled(true);
        this.updateSelectorStateCompat();
    }

    public boolean hasFocus() {
        if (!this.mHijackFocus && !super.hasFocus()) {
            return false;
        }
        return true;
    }

    public boolean hasWindowFocus() {
        if (!this.mHijackFocus && !super.hasWindowFocus()) {
            return false;
        }
        return true;
    }

    public boolean isFocused() {
        if (!this.mHijackFocus && !super.isFocused()) {
            return false;
        }
        return true;
    }

    public boolean isInTouchMode() {
        if (this.mHijackFocus && this.mListSelectionHidden || super.isInTouchMode()) {
            return true;
        }
        return false;
    }

    public int lookForSelectablePosition(int n, boolean bl) {
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter != null) {
            if (this.isInTouchMode()) {
                return -1;
            }
            int n2 = listAdapter.getCount();
            if (!this.getAdapter().areAllItemsEnabled()) {
                int n3;
                if (bl) {
                    n = Math.max(0, n);
                    do {
                        n3 = n;
                        if (n < n2) {
                            n3 = n;
                            if (!listAdapter.isEnabled(n)) {
                                ++n;
                                continue;
                            }
                        }
                        break;
                    } while (true);
                } else {
                    n = Math.min(n, n2 - 1);
                    do {
                        n3 = n;
                        if (n < 0) break;
                        n3 = n;
                        if (listAdapter.isEnabled(n)) break;
                        --n;
                    } while (true);
                }
                if (n3 >= 0) {
                    if (n3 >= n2) {
                        return -1;
                    }
                    return n3;
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
        n2 = n6 + n7;
        if (n3 <= 0 || drawable2 == null) {
            n3 = 0;
        }
        int n8 = 0;
        drawable2 = null;
        int n9 = 0;
        int n10 = listAdapter.getCount();
        for (int i = 0; i < n10; ++i) {
            int n11 = listAdapter.getItemViewType(i);
            int n12 = n9;
            if (n11 != n9) {
                drawable2 = null;
                n12 = n11;
            }
            View view = listAdapter.getView(i, (View)drawable2, (ViewGroup)this);
            drawable2 = view.getLayoutParams();
            if (drawable2 == null) {
                drawable2 = this.generateDefaultLayoutParams();
                view.setLayoutParams((ViewGroup.LayoutParams)drawable2);
            }
            n9 = drawable2.height > 0 ? View.MeasureSpec.makeMeasureSpec((int)drawable2.height, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
            view.measure(n, n9);
            view.forceLayout();
            n9 = n2;
            if (i > 0) {
                n9 = n2 + n3;
            }
            if ((n2 = n9 + view.getMeasuredHeight()) >= n4) {
                if (n5 >= 0 && i > n5 && n8 > 0 && n2 != n4) {
                    return n8;
                }
                return n4;
            }
            n9 = n8;
            if (n5 >= 0) {
                n9 = n8;
                if (i >= n5) {
                    n9 = n2;
                }
            }
            n8 = n9;
            drawable2 = view;
            n9 = n12;
        }
        return n2;
    }

    protected void onDetachedFromWindow() {
        this.mResolveHoverRunnable = null;
        super.onDetachedFromWindow();
    }

    public boolean onForwardedEvent(MotionEvent object, int n) {
        boolean bl;
        block13 : {
            int n2;
            boolean bl2;
            int n3;
            int n4;
            block12 : {
                block11 : {
                    bl2 = true;
                    bl = true;
                    n3 = 0;
                    n2 = object.getActionMasked();
                    if (n2 == 1) break block11;
                    if (n2 == 2) break block12;
                    if (n2 != 3) {
                        bl = bl2;
                        n = n3;
                    } else {
                        bl = false;
                        n = n3;
                    }
                    break block13;
                }
                bl = false;
            }
            if ((n4 = object.findPointerIndex(n)) < 0) {
                bl = false;
                n = n3;
            } else {
                n = (int)object.getX(n4);
                int n5 = (int)object.getY(n4);
                n4 = this.pointToPosition(n, n5);
                if (n4 == -1) {
                    n = 1;
                } else {
                    View view = this.getChildAt(n4 - this.getFirstVisiblePosition());
                    this.setPressedItem(view, n4, n, n5);
                    bl = bl2 = true;
                    n = n3;
                    if (n2 == 1) {
                        this.clickPressedItem(view, n4);
                        n = n3;
                        bl = bl2;
                    }
                }
            }
        }
        if (!bl || n != 0) {
            this.clearPressedItem();
        }
        if (bl) {
            if (this.mScrollHelper == null) {
                this.mScrollHelper = new ListViewAutoScrollHelper(this);
            }
            this.mScrollHelper.setEnabled(true);
            this.mScrollHelper.onTouch((View)this, (MotionEvent)object);
            return bl;
        }
        object = this.mScrollHelper;
        if (object != null) {
            object.setEnabled(false);
        }
        return bl;
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT < 26) {
            return super.onHoverEvent(motionEvent);
        }
        int n = motionEvent.getActionMasked();
        if (n == 10 && this.mResolveHoverRunnable == null) {
            ResolveHoverRunnable resolveHoverRunnable;
            this.mResolveHoverRunnable = resolveHoverRunnable = new ResolveHoverRunnable();
            resolveHoverRunnable.post();
        }
        boolean bl = super.onHoverEvent(motionEvent);
        if (n != 9 && n != 7) {
            this.setSelection(-1);
            return bl;
        }
        n = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        if (n != -1 && n != this.getSelectedItemPosition()) {
            motionEvent = this.getChildAt(n - this.getFirstVisiblePosition());
            if (motionEvent.isEnabled()) {
                this.setSelectionFromTop(n, motionEvent.getTop() - this.getTop());
            }
            this.updateSelectorStateCompat();
        }
        return bl;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mMotionPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
        if (resolveHoverRunnable != null) {
            resolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(motionEvent);
    }

    void setListSelectionHidden(boolean bl) {
        this.mListSelectionHidden = bl;
    }

    public void setSelector(Drawable drawable2) {
        GateKeeperDrawable gateKeeperDrawable = drawable2 != null ? new GateKeeperDrawable(drawable2) : null;
        this.mSelector = gateKeeperDrawable;
        super.setSelector((Drawable)gateKeeperDrawable);
        gateKeeperDrawable = new Rect();
        if (drawable2 != null) {
            drawable2.getPadding((Rect)gateKeeperDrawable);
        }
        this.mSelectionLeftPadding = gateKeeperDrawable.left;
        this.mSelectionTopPadding = gateKeeperDrawable.top;
        this.mSelectionRightPadding = gateKeeperDrawable.right;
        this.mSelectionBottomPadding = gateKeeperDrawable.bottom;
    }

    private static class GateKeeperDrawable
    extends DrawableWrapper {
        private boolean mEnabled = true;

        GateKeeperDrawable(Drawable drawable2) {
            super(drawable2);
        }

        @Override
        public void draw(Canvas canvas) {
            if (this.mEnabled) {
                super.draw(canvas);
            }
        }

        void setEnabled(boolean bl) {
            this.mEnabled = bl;
        }

        @Override
        public void setHotspot(float f, float f2) {
            if (this.mEnabled) {
                super.setHotspot(f, f2);
            }
        }

        @Override
        public void setHotspotBounds(int n, int n2, int n3, int n4) {
            if (this.mEnabled) {
                super.setHotspotBounds(n, n2, n3, n4);
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

    private class ResolveHoverRunnable
    implements Runnable {
        ResolveHoverRunnable() {
        }

        public void cancel() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.removeCallbacks((Runnable)this);
        }

        public void post() {
            DropDownListView.this.post((Runnable)this);
        }

        @Override
        public void run() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.drawableStateChanged();
        }
    }

}

