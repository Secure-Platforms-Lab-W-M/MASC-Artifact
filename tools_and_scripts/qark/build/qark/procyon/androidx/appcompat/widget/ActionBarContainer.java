// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.ViewGroup$LayoutParams;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable$Callback;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.widget.FrameLayout$LayoutParams;
import android.content.res.TypedArray;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

public class ActionBarContainer extends FrameLayout
{
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;
    
    public ActionBarContainer(final Context context) {
        this(context, null);
    }
    
    public ActionBarContainer(final Context context, final AttributeSet set) {
        super(context, set);
        ViewCompat.setBackground((View)this, new ActionBarBackgroundDrawable(this));
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.ActionBar);
        this.mBackground = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_background);
        this.mStackedBackground = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_backgroundStacked);
        this.mHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionBar_height, -1);
        final int id = this.getId();
        final int split_action_bar = R$id.split_action_bar;
        boolean willNotDraw = true;
        if (id == split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        Label_0137: {
            Label_0134: {
                if (this.mIsSplit) {
                    if (this.mSplitBackground != null) {
                        break Label_0134;
                    }
                }
                else if (this.mBackground != null || this.mStackedBackground != null) {
                    break Label_0134;
                }
                break Label_0137;
            }
            willNotDraw = false;
        }
        this.setWillNotDraw(willNotDraw);
    }
    
    private int getMeasuredHeightWithMargins(final View view) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)view.getLayoutParams();
        return view.getMeasuredHeight() + frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin;
    }
    
    private boolean isCollapsed(final View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable mBackground = this.mBackground;
        if (mBackground != null && mBackground.isStateful()) {
            this.mBackground.setState(this.getDrawableState());
        }
        final Drawable mStackedBackground = this.mStackedBackground;
        if (mStackedBackground != null && mStackedBackground.isStateful()) {
            this.mStackedBackground.setState(this.getDrawableState());
        }
        final Drawable mSplitBackground = this.mSplitBackground;
        if (mSplitBackground != null && mSplitBackground.isStateful()) {
            this.mSplitBackground.setState(this.getDrawableState());
        }
    }
    
    public View getTabContainer() {
        return this.mTabContainer;
    }
    
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        final Drawable mBackground = this.mBackground;
        if (mBackground != null) {
            mBackground.jumpToCurrentState();
        }
        final Drawable mStackedBackground = this.mStackedBackground;
        if (mStackedBackground != null) {
            mStackedBackground.jumpToCurrentState();
        }
        final Drawable mSplitBackground = this.mSplitBackground;
        if (mSplitBackground != null) {
            mSplitBackground.jumpToCurrentState();
        }
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = this.findViewById(R$id.action_bar);
        this.mContextView = this.findViewById(R$id.action_context_bar);
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }
    
    public void onLayout(final boolean b, int n, int measuredHeight, final int n2, final int n3) {
        super.onLayout(b, n, measuredHeight, n2, n3);
        final View mTabContainer = this.mTabContainer;
        final boolean mIsStacked = mTabContainer != null && mTabContainer.getVisibility() != 8;
        if (mTabContainer != null && mTabContainer.getVisibility() != 8) {
            measuredHeight = this.getMeasuredHeight();
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)mTabContainer.getLayoutParams();
            mTabContainer.layout(n, measuredHeight - mTabContainer.getMeasuredHeight() - frameLayout$LayoutParams.bottomMargin, n2, measuredHeight - frameLayout$LayoutParams.bottomMargin);
        }
        n = 0;
        measuredHeight = 0;
        if (this.mIsSplit) {
            final Drawable mSplitBackground = this.mSplitBackground;
            if (mSplitBackground != null) {
                mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                n = 1;
            }
        }
        else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                }
                else {
                    final View mContextView = this.mContextView;
                    if (mContextView != null && mContextView.getVisibility() == 0) {
                        this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
                    }
                    else {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    }
                }
                measuredHeight = 1;
            }
            this.mIsStacked = mIsStacked;
            n = measuredHeight;
            if (mIsStacked) {
                final Drawable mStackedBackground = this.mStackedBackground;
                n = measuredHeight;
                if (mStackedBackground != null) {
                    mStackedBackground.setBounds(mTabContainer.getLeft(), mTabContainer.getTop(), mTabContainer.getRight(), mTabContainer.getBottom());
                    n = 1;
                }
            }
        }
        if (n != 0) {
            this.invalidate();
        }
    }
    
    public void onMeasure(int n, int n2) {
        int measureSpec = n2;
        if (this.mActionBarView == null) {
            measureSpec = n2;
            if (View$MeasureSpec.getMode(n2) == Integer.MIN_VALUE) {
                final int mHeight = this.mHeight;
                measureSpec = n2;
                if (mHeight >= 0) {
                    measureSpec = View$MeasureSpec.makeMeasureSpec(Math.min(mHeight, View$MeasureSpec.getSize(n2)), Integer.MIN_VALUE);
                }
            }
        }
        super.onMeasure(n, measureSpec);
        if (this.mActionBarView == null) {
            return;
        }
        n2 = View$MeasureSpec.getMode(measureSpec);
        final View mTabContainer = this.mTabContainer;
        if (mTabContainer != null && mTabContainer.getVisibility() != 8 && n2 != 1073741824) {
            if (!this.isCollapsed(this.mActionBarView)) {
                n = this.getMeasuredHeightWithMargins(this.mActionBarView);
            }
            else if (!this.isCollapsed(this.mContextView)) {
                n = this.getMeasuredHeightWithMargins(this.mContextView);
            }
            else {
                n = 0;
            }
            if (n2 == Integer.MIN_VALUE) {
                n2 = View$MeasureSpec.getSize(measureSpec);
            }
            else {
                n2 = Integer.MAX_VALUE;
            }
            this.setMeasuredDimension(this.getMeasuredWidth(), Math.min(this.getMeasuredHeightWithMargins(this.mTabContainer) + n, n2));
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }
    
    public void setPrimaryBackground(final Drawable mBackground) {
        final Drawable mBackground2 = this.mBackground;
        if (mBackground2 != null) {
            mBackground2.setCallback((Drawable$Callback)null);
            this.unscheduleDrawable(this.mBackground);
        }
        if ((this.mBackground = mBackground) != null) {
            mBackground.setCallback((Drawable$Callback)this);
            final View mActionBarView = this.mActionBarView;
            if (mActionBarView != null) {
                this.mBackground.setBounds(mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        final boolean mIsSplit = this.mIsSplit;
        boolean willNotDraw = true;
        Label_0120: {
            Label_0118: {
                if (mIsSplit) {
                    if (this.mSplitBackground != null) {
                        break Label_0118;
                    }
                }
                else if (this.mBackground != null || this.mStackedBackground != null) {
                    break Label_0118;
                }
                break Label_0120;
            }
            willNotDraw = false;
        }
        this.setWillNotDraw(willNotDraw);
        this.invalidate();
        if (Build$VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }
    
    public void setSplitBackground(Drawable mSplitBackground) {
        final Drawable mSplitBackground2 = this.mSplitBackground;
        if (mSplitBackground2 != null) {
            mSplitBackground2.setCallback((Drawable$Callback)null);
            this.unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = mSplitBackground;
        final boolean b = false;
        if (mSplitBackground != null) {
            mSplitBackground.setCallback((Drawable$Callback)this);
            if (this.mIsSplit) {
                mSplitBackground = this.mSplitBackground;
                if (mSplitBackground != null) {
                    mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                }
            }
        }
        boolean willNotDraw = false;
        Label_0113: {
            if (this.mIsSplit) {
                willNotDraw = b;
                if (this.mSplitBackground != null) {
                    break Label_0113;
                }
            }
            else {
                willNotDraw = b;
                if (this.mBackground != null) {
                    break Label_0113;
                }
                willNotDraw = b;
                if (this.mStackedBackground != null) {
                    break Label_0113;
                }
            }
            willNotDraw = true;
        }
        this.setWillNotDraw(willNotDraw);
        this.invalidate();
        if (Build$VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }
    
    public void setStackedBackground(Drawable mStackedBackground) {
        final Drawable mStackedBackground2 = this.mStackedBackground;
        if (mStackedBackground2 != null) {
            mStackedBackground2.setCallback((Drawable$Callback)null);
            this.unscheduleDrawable(this.mStackedBackground);
        }
        if ((this.mStackedBackground = mStackedBackground) != null) {
            mStackedBackground.setCallback((Drawable$Callback)this);
            if (this.mIsStacked) {
                mStackedBackground = this.mStackedBackground;
                if (mStackedBackground != null) {
                    mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
                }
            }
        }
        final boolean mIsSplit = this.mIsSplit;
        boolean willNotDraw = true;
        Label_0127: {
            Label_0125: {
                if (mIsSplit) {
                    if (this.mSplitBackground != null) {
                        break Label_0125;
                    }
                }
                else if (this.mBackground != null || this.mStackedBackground != null) {
                    break Label_0125;
                }
                break Label_0127;
            }
            willNotDraw = false;
        }
        this.setWillNotDraw(willNotDraw);
        this.invalidate();
        if (Build$VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }
    
    public void setTabContainer(final ScrollingTabContainerView mTabContainer) {
        final View mTabContainer2 = this.mTabContainer;
        if (mTabContainer2 != null) {
            this.removeView(mTabContainer2);
        }
        if ((this.mTabContainer = (View)mTabContainer) != null) {
            this.addView((View)mTabContainer);
            final ViewGroup$LayoutParams layoutParams = mTabContainer.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            mTabContainer.setAllowCollapse(false);
        }
    }
    
    public void setTransitioning(final boolean mIsTransitioning) {
        this.mIsTransitioning = mIsTransitioning;
        int descendantFocusability;
        if (mIsTransitioning) {
            descendantFocusability = 393216;
        }
        else {
            descendantFocusability = 262144;
        }
        this.setDescendantFocusability(descendantFocusability);
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        final boolean b = visibility == 0;
        final Drawable mBackground = this.mBackground;
        if (mBackground != null) {
            mBackground.setVisible(b, false);
        }
        final Drawable mStackedBackground = this.mStackedBackground;
        if (mStackedBackground != null) {
            mStackedBackground.setVisible(b, false);
        }
        final Drawable mSplitBackground = this.mSplitBackground;
        if (mSplitBackground != null) {
            mSplitBackground.setVisible(b, false);
        }
    }
    
    public ActionMode startActionModeForChild(final View view, final ActionMode$Callback actionMode$Callback) {
        return null;
    }
    
    public ActionMode startActionModeForChild(final View view, final ActionMode$Callback actionMode$Callback, final int n) {
        if (n != 0) {
            return super.startActionModeForChild(view, actionMode$Callback, n);
        }
        return null;
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || (drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable);
    }
}
