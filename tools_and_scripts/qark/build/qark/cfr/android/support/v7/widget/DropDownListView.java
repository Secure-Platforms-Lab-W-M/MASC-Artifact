/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.widget.ListView
 */
package android.support.v7.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

class DropDownListView
extends ListViewCompat {
    private ViewPropertyAnimatorCompat mClickAnimation;
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private boolean mListSelectionHidden;
    private ListViewAutoScrollHelper mScrollHelper;

    public DropDownListView(Context context, boolean bl) {
        super(context, null, R.attr.dropDownListViewStyle);
        this.mHijackFocus = bl;
        this.setCacheColorHint(0);
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
            return;
        }
    }

    private void clickPressedItem(View view, int n) {
        this.performItemClick(view, n, this.getItemIdAtPosition(n));
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
        if (this.mMotionPosition != -1 && (view2 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition())) != null && view2 != view && view2.isPressed()) {
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

    public boolean onForwardedEvent(MotionEvent object, int n) {
        boolean bl;
        block15 : {
            boolean bl2 = true;
            bl = true;
            int n2 = 0;
            int n3 = object.getActionMasked();
            switch (n3) {
                default: {
                    bl = bl2;
                    n = n2;
                    break block15;
                }
                case 3: {
                    bl = false;
                    n = n2;
                    break block15;
                }
                case 2: {
                    break;
                }
                case 1: {
                    bl = false;
                }
            }
            int n4 = object.findPointerIndex(n);
            if (n4 < 0) {
                bl = false;
                n = n2;
            } else {
                n = (int)object.getX(n4);
                int n5 = this.pointToPosition(n, n4 = (int)object.getY(n4));
                if (n5 == -1) {
                    n = 1;
                } else {
                    View view = this.getChildAt(n5 - this.getFirstVisiblePosition());
                    this.setPressedItem(view, n5, n, n4);
                    bl = true;
                    if (n3 == 1) {
                        this.clickPressedItem(view, n5);
                        n = n2;
                    } else {
                        n = n2;
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
            return bl;
        }
        return bl;
    }

    void setListSelectionHidden(boolean bl) {
        this.mListSelectionHidden = bl;
    }

    @Override
    protected boolean touchModeDrawsInPressedStateCompat() {
        if (!this.mDrawsInPressedState && !super.touchModeDrawsInPressedStateCompat()) {
            return false;
        }
        return true;
    }
}

