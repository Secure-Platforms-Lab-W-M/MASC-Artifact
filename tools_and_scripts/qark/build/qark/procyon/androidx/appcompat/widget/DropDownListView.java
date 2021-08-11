// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import androidx.appcompat.graphics.drawable.DrawableWrapper;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.os.Build$VERSION;
import androidx.core.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.view.View;
import android.widget.AbsListView;
import android.util.AttributeSet;
import androidx.appcompat.R$attr;
import android.content.Context;
import android.graphics.Rect;
import androidx.core.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;
import androidx.core.view.ViewPropertyAnimatorCompat;
import android.widget.ListView;

class DropDownListView extends ListView
{
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
    private int mSelectionBottomPadding;
    private int mSelectionLeftPadding;
    private int mSelectionRightPadding;
    private int mSelectionTopPadding;
    private GateKeeperDrawable mSelector;
    private final Rect mSelectorRect;
    
    DropDownListView(final Context context, final boolean mHijackFocus) {
        super(context, (AttributeSet)null, R$attr.dropDownListViewStyle);
        this.mSelectorRect = new Rect();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mHijackFocus = mHijackFocus;
        this.setCacheColorHint(0);
        try {
            (this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled")).setAccessible(true);
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
    
    private void clearPressedItem() {
        this.setPressed(this.mDrawsInPressedState = false);
        this.drawableStateChanged();
        final View child = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
        if (child != null) {
            child.setPressed(false);
        }
        final ViewPropertyAnimatorCompat mClickAnimation = this.mClickAnimation;
        if (mClickAnimation != null) {
            mClickAnimation.cancel();
            this.mClickAnimation = null;
        }
    }
    
    private void clickPressedItem(final View view, final int n) {
        this.performItemClick(view, n, this.getItemIdAtPosition(n));
    }
    
    private void drawSelectorCompat(final Canvas canvas) {
        if (!this.mSelectorRect.isEmpty()) {
            final Drawable selector = this.getSelector();
            if (selector != null) {
                selector.setBounds(this.mSelectorRect);
                selector.draw(canvas);
            }
        }
    }
    
    private void positionSelectorCompat(final int n, final View view) {
        while (true) {
            final Rect mSelectorRect = this.mSelectorRect;
            mSelectorRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            mSelectorRect.left -= this.mSelectionLeftPadding;
            mSelectorRect.top -= this.mSelectionTopPadding;
            mSelectorRect.right += this.mSelectionRightPadding;
            mSelectorRect.bottom += this.mSelectionBottomPadding;
            while (true) {
                Label_0143: {
                    try {
                        final boolean boolean1 = this.mIsChildViewEnabled.getBoolean(this);
                        if (view.isEnabled() != boolean1) {
                            final Field mIsChildViewEnabled = this.mIsChildViewEnabled;
                            if (boolean1) {
                                break Label_0143;
                            }
                            final boolean b = true;
                            mIsChildViewEnabled.set(this, b);
                            if (n != -1) {
                                this.refreshDrawableState();
                            }
                        }
                        return;
                    }
                    catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    private void positionSelectorLikeFocusCompat(final int n, final View view) {
        final Drawable selector = this.getSelector();
        boolean b = true;
        final boolean b2 = selector != null && n != -1;
        if (b2) {
            selector.setVisible(false, false);
        }
        this.positionSelectorCompat(n, view);
        if (b2) {
            final Rect mSelectorRect = this.mSelectorRect;
            final float exactCenterX = mSelectorRect.exactCenterX();
            final float exactCenterY = mSelectorRect.exactCenterY();
            if (this.getVisibility() != 0) {
                b = false;
            }
            selector.setVisible(b, false);
            DrawableCompat.setHotspot(selector, exactCenterX, exactCenterY);
        }
    }
    
    private void positionSelectorLikeTouchCompat(final int n, final View view, final float n2, final float n3) {
        this.positionSelectorLikeFocusCompat(n, view);
        final Drawable selector = this.getSelector();
        if (selector != null && n != -1) {
            DrawableCompat.setHotspot(selector, n2, n3);
        }
    }
    
    private void setPressedItem(final View view, final int mMotionPosition, final float n, final float n2) {
        this.mDrawsInPressedState = true;
        if (Build$VERSION.SDK_INT >= 21) {
            this.drawableHotspotChanged(n, n2);
        }
        if (!this.isPressed()) {
            this.setPressed(true);
        }
        this.layoutChildren();
        final int mMotionPosition2 = this.mMotionPosition;
        if (mMotionPosition2 != -1) {
            final View child = this.getChildAt(mMotionPosition2 - this.getFirstVisiblePosition());
            if (child != null && child != view && child.isPressed()) {
                child.setPressed(false);
            }
        }
        this.mMotionPosition = mMotionPosition;
        final float n3 = (float)view.getLeft();
        final float n4 = (float)view.getTop();
        if (Build$VERSION.SDK_INT >= 21) {
            view.drawableHotspotChanged(n - n3, n2 - n4);
        }
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        this.positionSelectorLikeTouchCompat(mMotionPosition, view, n, n2);
        this.setSelectorEnabled(false);
        this.refreshDrawableState();
    }
    
    private void setSelectorEnabled(final boolean enabled) {
        final GateKeeperDrawable mSelector = this.mSelector;
        if (mSelector != null) {
            mSelector.setEnabled(enabled);
        }
    }
    
    private boolean touchModeDrawsInPressedStateCompat() {
        return this.mDrawsInPressedState;
    }
    
    private void updateSelectorStateCompat() {
        final Drawable selector = this.getSelector();
        if (selector != null && this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
            selector.setState(this.getDrawableState());
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
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
        return this.mHijackFocus || super.hasFocus();
    }
    
    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }
    
    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }
    
    public boolean isInTouchMode() {
        return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
    }
    
    public int lookForSelectablePosition(int n, final boolean b) {
        final ListAdapter adapter = this.getAdapter();
        if (adapter == null) {
            return -1;
        }
        if (this.isInTouchMode()) {
            return -1;
        }
        final int count = adapter.getCount();
        if (!this.getAdapter().areAllItemsEnabled()) {
            int n2;
            if (b) {
                n = Math.max(0, n);
                while (true) {
                    n2 = n;
                    if (n >= count) {
                        break;
                    }
                    n2 = n;
                    if (adapter.isEnabled(n)) {
                        break;
                    }
                    ++n;
                }
            }
            else {
                n = Math.min(n, count - 1);
                while (true) {
                    n2 = n;
                    if (n < 0) {
                        break;
                    }
                    n2 = n;
                    if (adapter.isEnabled(n)) {
                        break;
                    }
                    --n;
                }
            }
            if (n2 < 0) {
                return -1;
            }
            if (n2 >= count) {
                return -1;
            }
            return n2;
        }
        else {
            if (n < 0) {
                return -1;
            }
            if (n >= count) {
                return -1;
            }
            return n;
        }
    }
    
    public int measureHeightOfChildrenCompat(final int n, int n2, int dividerHeight, final int n3, final int n4) {
        final int listPaddingTop = this.getListPaddingTop();
        final int listPaddingBottom = this.getListPaddingBottom();
        this.getListPaddingLeft();
        this.getListPaddingRight();
        dividerHeight = this.getDividerHeight();
        final Drawable divider = this.getDivider();
        final ListAdapter adapter = this.getAdapter();
        if (adapter == null) {
            return listPaddingTop + listPaddingBottom;
        }
        n2 = listPaddingTop + listPaddingBottom;
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        int n5 = 0;
        View view = null;
        int n6 = 0;
        final int count = adapter.getCount();
        int i = 0;
        while (i < count) {
            final int itemViewType = adapter.getItemViewType(i);
            int n7;
            if (itemViewType != (n7 = n6)) {
                view = null;
                n7 = itemViewType;
            }
            final View view2 = adapter.getView(i, view, (ViewGroup)this);
            ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = this.generateDefaultLayoutParams();
                view2.setLayoutParams(layoutParams);
            }
            int n8;
            if (layoutParams.height > 0) {
                n8 = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
            }
            else {
                n8 = View$MeasureSpec.makeMeasureSpec(0, 0);
            }
            view2.measure(n, n8);
            view2.forceLayout();
            int n9 = n2;
            if (i > 0) {
                n9 = n2 + dividerHeight;
            }
            n2 = n9 + view2.getMeasuredHeight();
            if (n2 >= n3) {
                if (n4 >= 0 && i > n4 && n5 > 0 && n2 != n3) {
                    return n5;
                }
                return n3;
            }
            else {
                int n10 = n5;
                if (n4 >= 0) {
                    n10 = n5;
                    if (i >= n4) {
                        n10 = n2;
                    }
                }
                ++i;
                n5 = n10;
                view = view2;
                n6 = n7;
            }
        }
        return n2;
    }
    
    protected void onDetachedFromWindow() {
        this.mResolveHoverRunnable = null;
        super.onDetachedFromWindow();
    }
    
    public boolean onForwardedEvent(final MotionEvent motionEvent, int n) {
        final boolean b = true;
        boolean b2 = true;
        final int n2 = 0;
        final int actionMasked = motionEvent.getActionMasked();
        Label_0164: {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        b2 = b;
                        n = n2;
                        break Label_0164;
                    }
                    b2 = false;
                    n = n2;
                    break Label_0164;
                }
            }
            else {
                b2 = false;
            }
            final int pointerIndex = motionEvent.findPointerIndex(n);
            if (pointerIndex < 0) {
                b2 = false;
                n = n2;
            }
            else {
                n = (int)motionEvent.getX(pointerIndex);
                final int n3 = (int)motionEvent.getY(pointerIndex);
                final int pointToPosition = this.pointToPosition(n, n3);
                if (pointToPosition == -1) {
                    n = 1;
                }
                else {
                    final View child = this.getChildAt(pointToPosition - this.getFirstVisiblePosition());
                    this.setPressedItem(child, pointToPosition, (float)n, (float)n3);
                    final boolean b3 = b2 = true;
                    n = n2;
                    if (actionMasked == 1) {
                        this.clickPressedItem(child, pointToPosition);
                        n = n2;
                        b2 = b3;
                    }
                }
            }
        }
        if (!b2 || n != 0) {
            this.clearPressedItem();
        }
        if (b2) {
            if (this.mScrollHelper == null) {
                this.mScrollHelper = new ListViewAutoScrollHelper(this);
            }
            this.mScrollHelper.setEnabled(true);
            this.mScrollHelper.onTouch((View)this, motionEvent);
            return b2;
        }
        final ListViewAutoScrollHelper mScrollHelper = this.mScrollHelper;
        if (mScrollHelper != null) {
            mScrollHelper.setEnabled(false);
        }
        return b2;
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        if (Build$VERSION.SDK_INT < 26) {
            return super.onHoverEvent(motionEvent);
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 10 && this.mResolveHoverRunnable == null) {
            (this.mResolveHoverRunnable = new ResolveHoverRunnable()).post();
        }
        final boolean onHoverEvent = super.onHoverEvent(motionEvent);
        if (actionMasked != 9 && actionMasked != 7) {
            this.setSelection(-1);
            return onHoverEvent;
        }
        final int pointToPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        if (pointToPosition != -1 && pointToPosition != this.getSelectedItemPosition()) {
            final View child = this.getChildAt(pointToPosition - this.getFirstVisiblePosition());
            if (child.isEnabled()) {
                this.setSelectionFromTop(pointToPosition, child.getTop() - this.getTop());
            }
            this.updateSelectorStateCompat();
        }
        return onHoverEvent;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mMotionPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        final ResolveHoverRunnable mResolveHoverRunnable = this.mResolveHoverRunnable;
        if (mResolveHoverRunnable != null) {
            mResolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(motionEvent);
    }
    
    void setListSelectionHidden(final boolean mListSelectionHidden) {
        this.mListSelectionHidden = mListSelectionHidden;
    }
    
    public void setSelector(final Drawable drawable) {
        GateKeeperDrawable mSelector;
        if (drawable != null) {
            mSelector = new GateKeeperDrawable(drawable);
        }
        else {
            mSelector = null;
        }
        super.setSelector((Drawable)(this.mSelector = mSelector));
        final Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.mSelectionLeftPadding = rect.left;
        this.mSelectionTopPadding = rect.top;
        this.mSelectionRightPadding = rect.right;
        this.mSelectionBottomPadding = rect.bottom;
    }
    
    private static class GateKeeperDrawable extends DrawableWrapper
    {
        private boolean mEnabled;
        
        GateKeeperDrawable(final Drawable drawable) {
            super(drawable);
            this.mEnabled = true;
        }
        
        @Override
        public void draw(final Canvas canvas) {
            if (this.mEnabled) {
                super.draw(canvas);
            }
        }
        
        void setEnabled(final boolean mEnabled) {
            this.mEnabled = mEnabled;
        }
        
        @Override
        public void setHotspot(final float n, final float n2) {
            if (this.mEnabled) {
                super.setHotspot(n, n2);
            }
        }
        
        @Override
        public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
            if (this.mEnabled) {
                super.setHotspotBounds(n, n2, n3, n4);
            }
        }
        
        @Override
        public boolean setState(final int[] state) {
            return this.mEnabled && super.setState(state);
        }
        
        @Override
        public boolean setVisible(final boolean b, final boolean b2) {
            return this.mEnabled && super.setVisible(b, b2);
        }
    }
    
    private class ResolveHoverRunnable implements Runnable
    {
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
