// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.widget.AbsListView;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import java.lang.reflect.Field;
import android.support.annotation.RestrictTo;
import android.widget.ListView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ListViewCompat extends ListView
{
    public static final int INVALID_POSITION = -1;
    public static final int NO_POSITION = -1;
    private static final int[] STATE_SET_NOTHING;
    private Field mIsChildViewEnabled;
    protected int mMotionPosition;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    private GateKeeperDrawable mSelector;
    final Rect mSelectorRect;
    
    static {
        STATE_SET_NOTHING = new int[] { 0 };
    }
    
    public ListViewCompat(final Context context) {
        this(context, null);
    }
    
    public ListViewCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ListViewCompat(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mSelectorRect = new Rect();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        try {
            (this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled")).setAccessible(true);
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        this.drawSelectorCompat(canvas);
        super.dispatchDraw(canvas);
    }
    
    protected void drawSelectorCompat(final Canvas canvas) {
        if (this.mSelectorRect.isEmpty()) {
            return;
        }
        final Drawable selector = this.getSelector();
        if (selector != null) {
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.setSelectorEnabled(true);
        this.updateSelectorStateCompat();
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
            if (b) {
                for (n = Math.max(0, n); n < count && !adapter.isEnabled(n); ++n) {}
            }
            else {
                for (n = Math.min(n, count - 1); n >= 0 && !adapter.isEnabled(n); --n) {}
            }
            if (n < 0) {
                return -1;
            }
            if (n >= count) {
                return -1;
            }
            return n;
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
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        View view = null;
        final int count = adapter.getCount();
        int n5 = 0;
        int n6 = 0;
        n2 = listPaddingTop + listPaddingBottom;
        int i = 0;
        while (i < count) {
            final int itemViewType = adapter.getItemViewType(i);
            if (itemViewType != n5) {
                view = null;
                n5 = itemViewType;
            }
            final View view2 = adapter.getView(i, view, (ViewGroup)this);
            ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = this.generateDefaultLayoutParams();
                view2.setLayoutParams(layoutParams);
            }
            int n7;
            if (layoutParams.height > 0) {
                n7 = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
            }
            else {
                n7 = View$MeasureSpec.makeMeasureSpec(0, 0);
            }
            view2.measure(n, n7);
            view2.forceLayout();
            if (i > 0) {
                n2 += dividerHeight;
            }
            n2 += view2.getMeasuredHeight();
            if (n2 >= n3) {
                if (n4 >= 0 && i > n4 && n6 > 0 && n2 != n3) {
                    return n6;
                }
                return n3;
            }
            else {
                if (n4 >= 0 && i >= n4) {
                    n6 = n2;
                }
                ++i;
                view = view2;
            }
        }
        return n2;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mMotionPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        return super.onTouchEvent(motionEvent);
    }
    
    protected void positionSelectorCompat(final int n, final View view) {
        while (true) {
            final Rect mSelectorRect = this.mSelectorRect;
            mSelectorRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            mSelectorRect.left -= this.mSelectionLeftPadding;
            mSelectorRect.top -= this.mSelectionTopPadding;
            mSelectorRect.right += this.mSelectionRightPadding;
            mSelectorRect.bottom += this.mSelectionBottomPadding;
            while (true) {
                Label_0149: {
                    try {
                        final boolean boolean1 = this.mIsChildViewEnabled.getBoolean(this);
                        if (view.isEnabled() != boolean1) {
                            final Field mIsChildViewEnabled = this.mIsChildViewEnabled;
                            if (boolean1) {
                                break Label_0149;
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
    
    protected void positionSelectorLikeFocusCompat(final int n, final View view) {
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
    
    protected void positionSelectorLikeTouchCompat(final int n, final View view, final float n2, final float n3) {
        this.positionSelectorLikeFocusCompat(n, view);
        final Drawable selector = this.getSelector();
        if (selector != null && n != -1) {
            DrawableCompat.setHotspot(selector, n2, n3);
        }
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
    
    protected void setSelectorEnabled(final boolean enabled) {
        final GateKeeperDrawable mSelector = this.mSelector;
        if (mSelector != null) {
            mSelector.setEnabled(enabled);
        }
    }
    
    protected boolean shouldShowSelectorCompat() {
        return this.touchModeDrawsInPressedStateCompat() && this.isPressed();
    }
    
    protected boolean touchModeDrawsInPressedStateCompat() {
        return false;
    }
    
    protected void updateSelectorStateCompat() {
        final Drawable selector = this.getSelector();
        if (selector != null && this.shouldShowSelectorCompat()) {
            selector.setState(this.getDrawableState());
        }
    }
    
    private static class GateKeeperDrawable extends DrawableWrapper
    {
        private boolean mEnabled;
        
        public GateKeeperDrawable(final Drawable drawable) {
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
}
