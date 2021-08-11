/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.Menu
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewPropertyAnimator
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.widget.OverScroller
 */
package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.OverScroller;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarOverlayLayout
extends ViewGroup
implements DecorContentParent,
NestedScrollingParent {
    static final int[] ATTRS = new int[]{R.attr.actionBarSize, 16842841};
    private static final String TAG = "ActionBarOverlayLayout";
    private final int ACTION_BAR_ANIMATE_DELAY = 600;
    private int mActionBarHeight;
    ActionBarContainer mActionBarTop;
    private ActionBarVisibilityCallback mActionBarVisibilityCallback;
    private final Runnable mAddActionBarHideOffset;
    boolean mAnimatingForFling;
    private final Rect mBaseContentInsets = new Rect();
    private final Rect mBaseInnerInsets = new Rect();
    private ContentFrameLayout mContent;
    private final Rect mContentInsets = new Rect();
    ViewPropertyAnimator mCurrentActionBarTopAnimator;
    private DecorToolbar mDecorToolbar;
    private OverScroller mFlingEstimator;
    private boolean mHasNonEmbeddedTabs;
    private boolean mHideOnContentScroll;
    private int mHideOnContentScrollReference;
    private boolean mIgnoreWindowContentOverlay;
    private final Rect mInnerInsets = new Rect();
    private final Rect mLastBaseContentInsets = new Rect();
    private final Rect mLastBaseInnerInsets = new Rect();
    private final Rect mLastInnerInsets = new Rect();
    private int mLastSystemUiVisibility;
    private boolean mOverlayMode;
    private final NestedScrollingParentHelper mParentHelper;
    private final Runnable mRemoveActionBarHideOffset;
    final AnimatorListenerAdapter mTopAnimatorListener;
    private Drawable mWindowContentOverlay;
    private int mWindowVisibility = 0;

    public ActionBarOverlayLayout(Context context) {
        this(context, null);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTopAnimatorListener = new AnimatorListenerAdapter(){

            public void onAnimationCancel(Animator object) {
                object = ActionBarOverlayLayout.this;
                object.mCurrentActionBarTopAnimator = null;
                object.mAnimatingForFling = false;
            }

            public void onAnimationEnd(Animator object) {
                object = ActionBarOverlayLayout.this;
                object.mCurrentActionBarTopAnimator = null;
                object.mAnimatingForFling = false;
            }
        };
        this.mRemoveActionBarHideOffset = new Runnable(){

            @Override
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY(0.0f).setListener((Animator.AnimatorListener)ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        this.mAddActionBarHideOffset = new Runnable(){

            @Override
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY((float)(- ActionBarOverlayLayout.this.mActionBarTop.getHeight())).setListener((Animator.AnimatorListener)ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        this.init(context);
        this.mParentHelper = new NestedScrollingParentHelper(this);
    }

    private void addActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.mAddActionBarHideOffset.run();
    }

    private boolean applyInsets(View object, Rect rect, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        boolean bl5 = false;
        object = (LayoutParams)object.getLayoutParams();
        if (bl && object.leftMargin != rect.left) {
            bl = true;
            object.leftMargin = rect.left;
        } else {
            bl = bl5;
        }
        if (bl2 && object.topMargin != rect.top) {
            bl = true;
            object.topMargin = rect.top;
        }
        if (bl4 && object.rightMargin != rect.right) {
            bl = true;
            object.rightMargin = rect.right;
        }
        if (bl3 && object.bottomMargin != rect.bottom) {
            object.bottomMargin = rect.bottom;
            return true;
        }
        return bl;
    }

    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar)view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar)view).getWrapper();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't make a decor toolbar out of ");
        stringBuilder.append(view.getClass().getSimpleName());
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void init(Context context) {
        TypedArray typedArray = this.getContext().getTheme().obtainStyledAttributes(ATTRS);
        boolean bl = false;
        this.mActionBarHeight = typedArray.getDimensionPixelSize(0, 0);
        this.mWindowContentOverlay = typedArray.getDrawable(1);
        boolean bl2 = this.mWindowContentOverlay == null;
        this.setWillNotDraw(bl2);
        typedArray.recycle();
        bl2 = bl;
        if (context.getApplicationInfo().targetSdkVersion < 19) {
            bl2 = true;
        }
        this.mIgnoreWindowContentOverlay = bl2;
        this.mFlingEstimator = new OverScroller(context);
    }

    private void postAddActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.postDelayed(this.mAddActionBarHideOffset, 600L);
    }

    private void postRemoveActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.postDelayed(this.mRemoveActionBarHideOffset, 600L);
    }

    private void removeActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.mRemoveActionBarHideOffset.run();
    }

    private boolean shouldHideActionBarOnFling(float f, float f2) {
        this.mFlingEstimator.fling(0, 0, 0, (int)f2, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (this.mFlingEstimator.getFinalY() > this.mActionBarTop.getHeight()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canShowOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.canShowOverflowMenu();
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    public void dismissPopups() {
        this.pullChildren();
        this.mDecorToolbar.dismissPopupMenus();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mWindowContentOverlay != null && !this.mIgnoreWindowContentOverlay) {
            int n = this.mActionBarTop.getVisibility() == 0 ? (int)((float)this.mActionBarTop.getBottom() + this.mActionBarTop.getTranslationY() + 0.5f) : 0;
            this.mWindowContentOverlay.setBounds(0, n, this.getWidth(), this.mWindowContentOverlay.getIntrinsicHeight() + n);
            this.mWindowContentOverlay.draw(canvas);
            return;
        }
    }

    protected boolean fitSystemWindows(Rect rect) {
        this.pullChildren();
        if ((ViewCompat.getWindowSystemUiVisibility((View)this) & 256) != 0) {
            // empty if block
        }
        boolean bl = this.applyInsets((View)this.mActionBarTop, rect, true, true, false, true);
        this.mBaseInnerInsets.set(rect);
        ViewUtils.computeFitSystemWindows((View)this, this.mBaseInnerInsets, this.mBaseContentInsets);
        if (!this.mLastBaseInnerInsets.equals((Object)this.mBaseInnerInsets)) {
            bl = true;
            this.mLastBaseInnerInsets.set(this.mBaseInnerInsets);
        }
        if (!this.mLastBaseContentInsets.equals((Object)this.mBaseContentInsets)) {
            bl = true;
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (bl) {
            this.requestLayout();
            return true;
        }
        return true;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            return - (int)actionBarContainer.getTranslationY();
        }
        return 0;
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    @Override
    public CharSequence getTitle() {
        this.pullChildren();
        return this.mDecorToolbar.getTitle();
    }

    void haltActionBarHideOffsetAnimations() {
        this.removeCallbacks(this.mRemoveActionBarHideOffset);
        this.removeCallbacks(this.mAddActionBarHideOffset);
        ViewPropertyAnimator viewPropertyAnimator = this.mCurrentActionBarTopAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            return;
        }
    }

    @Override
    public boolean hasIcon() {
        this.pullChildren();
        return this.mDecorToolbar.hasIcon();
    }

    @Override
    public boolean hasLogo() {
        this.pullChildren();
        return this.mDecorToolbar.hasLogo();
    }

    @Override
    public boolean hideOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.hideOverflowMenu();
    }

    @Override
    public void initFeature(int n) {
        this.pullChildren();
        if (n != 2) {
            if (n != 5) {
                if (n != 109) {
                    return;
                }
                this.setOverlayMode(true);
                return;
            }
            this.mDecorToolbar.initIndeterminateProgress();
            return;
        }
        this.mDecorToolbar.initProgress();
    }

    public boolean isHideOnContentScrollEnabled() {
        return this.mHideOnContentScroll;
    }

    public boolean isInOverlayMode() {
        return this.mOverlayMode;
    }

    @Override
    public boolean isOverflowMenuShowPending() {
        this.pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowPending();
    }

    @Override
    public boolean isOverflowMenuShowing() {
        this.pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowing();
    }

    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.init(this.getContext());
        ViewCompat.requestApplyInsets((View)this);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.haltActionBarHideOffsetAnimations();
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = this.getChildCount();
        n3 = this.getPaddingLeft();
        this.getPaddingRight();
        n4 = this.getPaddingTop();
        this.getPaddingBottom();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n5 = view.getMeasuredWidth();
            int n6 = view.getMeasuredHeight();
            int n7 = layoutParams.leftMargin + n3;
            int n8 = layoutParams.topMargin + n4;
            view.layout(n7, n8, n7 + n5, n8 + n6);
        }
    }

    protected void onMeasure(int n, int n2) {
        this.pullChildren();
        int n3 = 0;
        this.measureChildWithMargins((View)this.mActionBarTop, n, 0, n2, 0);
        LayoutParams layoutParams = (LayoutParams)this.mActionBarTop.getLayoutParams();
        int n4 = Math.max(0, this.mActionBarTop.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int n5 = Math.max(0, this.mActionBarTop.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        int n6 = View.combineMeasuredStates((int)0, (int)this.mActionBarTop.getMeasuredState());
        int n7 = (ViewCompat.getWindowSystemUiVisibility((View)this) & 256) != 0 ? 1 : 0;
        if (n7 != 0) {
            n3 = this.mActionBarHeight;
            if (this.mHasNonEmbeddedTabs && this.mActionBarTop.getTabContainer() != null) {
                n3 += this.mActionBarHeight;
            }
        } else if (this.mActionBarTop.getVisibility() != 8) {
            n3 = this.mActionBarTop.getMeasuredHeight();
        }
        this.mContentInsets.set(this.mBaseContentInsets);
        this.mInnerInsets.set(this.mBaseInnerInsets);
        if (!this.mOverlayMode && n7 == 0) {
            layoutParams = this.mContentInsets;
            layoutParams.top += n3;
            layoutParams = this.mContentInsets;
            layoutParams.bottom += 0;
        } else {
            layoutParams = this.mInnerInsets;
            layoutParams.top += n3;
            layoutParams = this.mInnerInsets;
            layoutParams.bottom += 0;
        }
        this.applyInsets((View)this.mContent, this.mContentInsets, true, true, true, true);
        if (!this.mLastInnerInsets.equals((Object)this.mInnerInsets)) {
            this.mLastInnerInsets.set(this.mInnerInsets);
            this.mContent.dispatchFitSystemWindows(this.mInnerInsets);
        }
        this.measureChildWithMargins((View)this.mContent, n, 0, n2, 0);
        layoutParams = (LayoutParams)this.mContent.getLayoutParams();
        n3 = Math.max(n4, this.mContent.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        n7 = Math.max(n5, this.mContent.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        n6 = View.combineMeasuredStates((int)n6, (int)this.mContent.getMeasuredState());
        n5 = this.getPaddingLeft();
        n4 = this.getPaddingRight();
        n7 = Math.max(n7 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(View.resolveSizeAndState((int)Math.max(n3 + (n5 + n4), this.getSuggestedMinimumWidth()), (int)n, (int)n6), View.resolveSizeAndState((int)n7, (int)n2, (int)(n6 << 16)));
    }

    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        if (this.mHideOnContentScroll && bl) {
            if (this.shouldHideActionBarOnFling(f, f2)) {
                this.addActionBarHideOffset();
            } else {
                this.removeActionBarHideOffset();
            }
            this.mAnimatingForFling = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View view, int n, int n2, int[] arrn) {
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        this.mHideOnContentScrollReference += n2;
        this.setActionBarHideOffset(this.mHideOnContentScrollReference);
    }

    @Override
    public void onNestedScrollAccepted(View object, View view, int n) {
        this.mParentHelper.onNestedScrollAccepted((View)object, view, n);
        this.mHideOnContentScrollReference = this.getActionBarHideOffset();
        this.haltActionBarHideOffsetAnimations();
        object = this.mActionBarVisibilityCallback;
        if (object != null) {
            object.onContentScrollStarted();
            return;
        }
    }

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        if ((n & 2) != 0 && this.mActionBarTop.getVisibility() == 0) {
            return this.mHideOnContentScroll;
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(View object) {
        if (this.mHideOnContentScroll && !this.mAnimatingForFling) {
            if (this.mHideOnContentScrollReference <= this.mActionBarTop.getHeight()) {
                this.postRemoveActionBarHideOffset();
            } else {
                this.postAddActionBarHideOffset();
            }
        }
        if ((object = this.mActionBarVisibilityCallback) != null) {
            object.onContentScrollStopped();
            return;
        }
    }

    public void onWindowSystemUiVisibilityChanged(int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            super.onWindowSystemUiVisibilityChanged(n);
        }
        this.pullChildren();
        int n2 = this.mLastSystemUiVisibility;
        this.mLastSystemUiVisibility = n;
        boolean bl = true;
        boolean bl2 = (n & 4) == 0;
        boolean bl3 = (n & 256) != 0;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            if (bl3) {
                bl = false;
            }
            actionBarVisibilityCallback.enableContentAnimations(bl);
            if (!bl2 && bl3) {
                this.mActionBarVisibilityCallback.hideForSystem();
            } else {
                this.mActionBarVisibilityCallback.showForSystem();
            }
        }
        if (((n2 ^ n) & 256) != 0) {
            if (this.mActionBarVisibilityCallback != null) {
                ViewCompat.requestApplyInsets((View)this);
                return;
            }
            return;
        }
    }

    protected void onWindowVisibilityChanged(int n) {
        super.onWindowVisibilityChanged(n);
        this.mWindowVisibility = n;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onWindowVisibilityChanged(n);
            return;
        }
    }

    void pullChildren() {
        if (this.mContent == null) {
            this.mContent = (ContentFrameLayout)this.findViewById(R.id.action_bar_activity_content);
            this.mActionBarTop = (ActionBarContainer)this.findViewById(R.id.action_bar_container);
            this.mDecorToolbar = this.getDecorToolbar(this.findViewById(R.id.action_bar));
            return;
        }
    }

    @Override
    public void restoreToolbarHierarchyState(SparseArray<Parcelable> sparseArray) {
        this.pullChildren();
        this.mDecorToolbar.restoreHierarchyState(sparseArray);
    }

    @Override
    public void saveToolbarHierarchyState(SparseArray<Parcelable> sparseArray) {
        this.pullChildren();
        this.mDecorToolbar.saveHierarchyState(sparseArray);
    }

    public void setActionBarHideOffset(int n) {
        this.haltActionBarHideOffsetAnimations();
        n = Math.max(0, Math.min(n, this.mActionBarTop.getHeight()));
        this.mActionBarTop.setTranslationY((float)(- n));
    }

    public void setActionBarVisibilityCallback(ActionBarVisibilityCallback actionBarVisibilityCallback) {
        this.mActionBarVisibilityCallback = actionBarVisibilityCallback;
        if (this.getWindowToken() != null) {
            this.mActionBarVisibilityCallback.onWindowVisibilityChanged(this.mWindowVisibility);
            if (this.mLastSystemUiVisibility != 0) {
                this.onWindowSystemUiVisibilityChanged(this.mLastSystemUiVisibility);
                ViewCompat.requestApplyInsets((View)this);
                return;
            }
            return;
        }
    }

    public void setHasNonEmbeddedTabs(boolean bl) {
        this.mHasNonEmbeddedTabs = bl;
    }

    public void setHideOnContentScrollEnabled(boolean bl) {
        if (bl != this.mHideOnContentScroll) {
            this.mHideOnContentScroll = bl;
            if (!bl) {
                this.haltActionBarHideOffsetAnimations();
                this.setActionBarHideOffset(0);
                return;
            }
            return;
        }
    }

    @Override
    public void setIcon(int n) {
        this.pullChildren();
        this.mDecorToolbar.setIcon(n);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.pullChildren();
        this.mDecorToolbar.setIcon(drawable2);
    }

    @Override
    public void setLogo(int n) {
        this.pullChildren();
        this.mDecorToolbar.setLogo(n);
    }

    @Override
    public void setMenu(Menu menu, MenuPresenter.Callback callback) {
        this.pullChildren();
        this.mDecorToolbar.setMenu(menu, callback);
    }

    @Override
    public void setMenuPrepared() {
        this.pullChildren();
        this.mDecorToolbar.setMenuPrepared();
    }

    public void setOverlayMode(boolean bl) {
        this.mOverlayMode = bl;
        bl = bl && this.getContext().getApplicationInfo().targetSdkVersion < 19;
        this.mIgnoreWindowContentOverlay = bl;
    }

    public void setShowingForActionMode(boolean bl) {
    }

    @Override
    public void setUiOptions(int n) {
    }

    @Override
    public void setWindowCallback(Window.Callback callback) {
        this.pullChildren();
        this.mDecorToolbar.setWindowCallback(callback);
    }

    @Override
    public void setWindowTitle(CharSequence charSequence) {
        this.pullChildren();
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    public boolean showOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.showOverflowMenu();
    }

    public static interface ActionBarVisibilityCallback {
        public void enableContentAnimations(boolean var1);

        public void hideForSystem();

        public void onContentScrollStarted();

        public void onContentScrollStopped();

        public void onWindowVisibilityChanged(int var1);

        public void showForSystem();
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

}

