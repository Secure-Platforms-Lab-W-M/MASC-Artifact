/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnApplyWindowInsetsListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.WindowInsets
 *  android.view.accessibility.AccessibilityEvent
 */
package androidx.drawerlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout
extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final int[] THEME_ATTRS;
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
    private Rect mChildHitRect;
    private Matrix mChildInvertedMatrix;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd = 3;
    private int mLockModeLeft = 3;
    private int mLockModeRight = 3;
    private int mLockModeStart = 3;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor = -1728053248;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();
    private Drawable mShadowEnd = null;
    private Drawable mShadowLeft = null;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight = null;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart = null;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    static {
        boolean bl = true;
        THEME_ATTRS = new int[]{16843828};
        LAYOUT_ATTRS = new int[]{16842931};
        boolean bl2 = Build.VERSION.SDK_INT >= 19;
        CAN_HIDE_DESCENDANTS = bl2;
        bl2 = Build.VERSION.SDK_INT >= 21 ? bl : false;
        SET_DRAWER_SHADOW_FROM_ELEVATION = bl2;
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawerLayout(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        this.setDescendantFocusability(262144);
        float f = this.getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int)(64.0f * f + 0.5f);
        float f2 = 400.0f * f;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = object = ViewDragHelper.create(this, 1.0f, this.mLeftCallback);
        object.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(f2);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = object = ViewDragHelper.create(this, 1.0f, this.mRightCallback);
        object.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(f2);
        this.mRightCallback.setDragger(this.mRightDragger);
        this.setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility((View)this, 1);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        this.setMotionEventSplittingEnabled(false);
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            if (Build.VERSION.SDK_INT >= 21) {
                this.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(){

                    public WindowInsets onApplyWindowInsets(View object, WindowInsets windowInsets) {
                        object = (DrawerLayout)((Object)object);
                        boolean bl = windowInsets.getSystemWindowInsetTop() > 0;
                        object.setChildInsets((Object)windowInsets, bl);
                        return windowInsets.consumeSystemWindowInsets();
                    }
                });
                this.setSystemUiVisibility(1280);
                context = context.obtainStyledAttributes(THEME_ATTRS);
                try {
                    this.mStatusBarBackground = context.getDrawable(0);
                }
                finally {
                    context.recycle();
                }
            } else {
                this.mStatusBarBackground = null;
            }
        }
        this.mDrawerElevation = 10.0f * f;
        this.mNonDrawerViews = new ArrayList();
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent motionEvent, View view) {
        if (!view.getMatrix().isIdentity()) {
            motionEvent = this.getTransformedMotionEvent(motionEvent, view);
            boolean bl = view.dispatchGenericMotionEvent(motionEvent);
            motionEvent.recycle();
            return bl;
        }
        float f = this.getScrollX() - view.getLeft();
        float f2 = this.getScrollY() - view.getTop();
        motionEvent.offsetLocation(f, f2);
        boolean bl = view.dispatchGenericMotionEvent(motionEvent);
        motionEvent.offsetLocation(- f, - f2);
        return bl;
    }

    private MotionEvent getTransformedMotionEvent(MotionEvent motionEvent, View view) {
        float f = this.getScrollX() - view.getLeft();
        float f2 = this.getScrollY() - view.getTop();
        motionEvent = MotionEvent.obtain((MotionEvent)motionEvent);
        motionEvent.offsetLocation(f, f2);
        view = view.getMatrix();
        if (!view.isIdentity()) {
            if (this.mChildInvertedMatrix == null) {
                this.mChildInvertedMatrix = new Matrix();
            }
            view.invert(this.mChildInvertedMatrix);
            motionEvent.transform(this.mChildInvertedMatrix);
        }
        return motionEvent;
    }

    static String gravityToString(int n) {
        if ((n & 3) == 3) {
            return "LEFT";
        }
        if ((n & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(n);
    }

    private static boolean hasOpaqueBackground(View view) {
        view = view.getBackground();
        boolean bl = false;
        if (view != null) {
            if (view.getOpacity() == -1) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private boolean hasPeekingDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            if (!((LayoutParams)this.getChildAt((int)i).getLayoutParams()).isPeeking) continue;
            return true;
        }
        return false;
    }

    private boolean hasVisibleDrawer() {
        if (this.findVisibleDrawer() != null) {
            return true;
        }
        return false;
    }

    static boolean includeChildForAccessibility(View view) {
        if (ViewCompat.getImportantForAccessibility(view) != 4 && ViewCompat.getImportantForAccessibility(view) != 2) {
            return true;
        }
        return false;
    }

    private boolean isInBoundsOfChild(float f, float f2, View view) {
        if (this.mChildHitRect == null) {
            this.mChildHitRect = new Rect();
        }
        view.getHitRect(this.mChildHitRect);
        return this.mChildHitRect.contains((int)f, (int)f2);
    }

    private boolean mirror(Drawable drawable2, int n) {
        if (drawable2 != null && DrawableCompat.isAutoMirrored(drawable2)) {
            DrawableCompat.setLayoutDirection(drawable2, n);
            return true;
        }
        return false;
    }

    private Drawable resolveLeftShadow() {
        int n = ViewCompat.getLayoutDirection((View)this);
        if (n == 0) {
            Drawable drawable2 = this.mShadowStart;
            if (drawable2 != null) {
                this.mirror(drawable2, n);
                return this.mShadowStart;
            }
        } else {
            Drawable drawable3 = this.mShadowEnd;
            if (drawable3 != null) {
                this.mirror(drawable3, n);
                return this.mShadowEnd;
            }
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int n = ViewCompat.getLayoutDirection((View)this);
        if (n == 0) {
            Drawable drawable2 = this.mShadowEnd;
            if (drawable2 != null) {
                this.mirror(drawable2, n);
                return this.mShadowEnd;
            }
        } else {
            Drawable drawable3 = this.mShadowStart;
            if (drawable3 != null) {
                this.mirror(drawable3, n);
                return this.mShadowStart;
            }
        }
        return this.mShadowRight;
    }

    private void resolveShadowDrawables() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        this.mShadowLeftResolved = this.resolveLeftShadow();
        this.mShadowRightResolved = this.resolveRightShadow();
    }

    private void updateChildrenImportantForAccessibility(View view, boolean bl) {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view2 = this.getChildAt(i);
            if (!bl && !this.isDrawerView(view2) || bl && view2 == view) {
                ViewCompat.setImportantForAccessibility(view2, 1);
                continue;
            }
            ViewCompat.setImportantForAccessibility(view2, 4);
        }
    }

    public void addDrawerListener(DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<DrawerListener>();
        }
        this.mListeners.add(drawerListener);
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        int n3;
        View view;
        if (this.getDescendantFocusability() == 393216) {
            return;
        }
        int n4 = this.getChildCount();
        int n5 = 0;
        for (n3 = 0; n3 < n4; ++n3) {
            view = this.getChildAt(n3);
            if (this.isDrawerView(view)) {
                if (!this.isDrawerOpen(view)) continue;
                n5 = 1;
                view.addFocusables(arrayList, n, n2);
                continue;
            }
            this.mNonDrawerViews.add(view);
        }
        if (n5 == 0) {
            n5 = this.mNonDrawerViews.size();
            for (n3 = 0; n3 < n5; ++n3) {
                view = this.mNonDrawerViews.get(n3);
                if (view.getVisibility() != 0) continue;
                view.addFocusables(arrayList, n, n2);
            }
        }
        this.mNonDrawerViews.clear();
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n, layoutParams);
        if (this.findOpenDrawer() == null && !this.isDrawerView(view)) {
            ViewCompat.setImportantForAccessibility(view, 1);
        } else {
            ViewCompat.setImportantForAccessibility(view, 4);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(view, this.mChildAccessibilityDelegate);
        }
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                this.getChildAt(i).dispatchTouchEvent(motionEvent);
            }
            motionEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View view, int n) {
        if ((this.getDrawerViewAbsoluteGravity(view) & n) == n) {
            return true;
        }
        return false;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    public void closeDrawer(int n) {
        this.closeDrawer(n, true);
    }

    public void closeDrawer(int n, boolean bl) {
        Object object = this.findDrawerWithGravity(n);
        if (object != null) {
            this.closeDrawer((View)object, bl);
            return;
        }
        object = new StringBuilder();
        object.append("No drawer view found with gravity ");
        object.append(DrawerLayout.gravityToString(n));
        throw new IllegalArgumentException(object.toString());
    }

    public void closeDrawer(View view) {
        this.closeDrawer(view, true);
    }

    public void closeDrawer(View view, boolean bl) {
        if (this.isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 0.0f;
                layoutParams.openState = 0;
            } else if (bl) {
                layoutParams.openState = 4 | layoutParams.openState;
                if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, - view.getWidth(), view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop());
                }
            } else {
                this.moveDrawerToOffset(view, 0.0f);
                this.updateDrawerState(layoutParams.gravity, 0, view);
                view.setVisibility(4);
            }
            this.invalidate();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a sliding drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void closeDrawers() {
        this.closeDrawers(false);
    }

    void closeDrawers(boolean bl) {
        int n = 0;
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = n;
            if (this.isDrawerView(view)) {
                if (bl && !layoutParams.isPeeking) {
                    n3 = n;
                } else {
                    n3 = view.getWidth();
                    n = this.checkDrawerViewAbsoluteGravity(view, 3) ? (n |= this.mLeftDragger.smoothSlideViewTo(view, - n3, view.getTop())) : (n |= this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop()));
                    layoutParams.isPeeking = false;
                    n3 = n;
                }
            }
            n = n3;
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (n != 0) {
            this.invalidate();
        }
    }

    public void computeScroll() {
        int n = this.getChildCount();
        float f = 0.0f;
        for (int i = 0; i < n; ++i) {
            f = Math.max(f, ((LayoutParams)this.getChildAt((int)i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        boolean bl = this.mLeftDragger.continueSettling(true);
        boolean bl2 = this.mRightDragger.continueSettling(true);
        if (bl || bl2) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() != 10 && this.mScrimOpacity > 0.0f) {
            int n = this.getChildCount();
            if (n != 0) {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                --n;
                while (n >= 0) {
                    View view = this.getChildAt(n);
                    if (this.isInBoundsOfChild(f, f2, view) && !this.isContentView(view) && this.dispatchTransformedGenericPointerEvent(motionEvent, view)) {
                        return true;
                    }
                    --n;
                }
            }
            return false;
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    void dispatchOnDrawerClosed(View view) {
        List<DrawerListener> list = (LayoutParams)view.getLayoutParams();
        if ((list.openState & 1) == 1) {
            list.openState = 0;
            list = this.mListeners;
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerClosed(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, false);
            if (this.hasWindowFocus() && (view = this.getRootView()) != null) {
                view.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View view) {
        List<DrawerListener> list = (LayoutParams)view.getLayoutParams();
        if ((list.openState & 1) == 0) {
            list.openState = 1;
            list = this.mListeners;
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerOpened(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, true);
            if (this.hasWindowFocus()) {
                this.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerSlide(View view, float f) {
        List<DrawerListener> list = this.mListeners;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onDrawerSlide(view, f);
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        int n;
        int n2;
        int n3 = this.getHeight();
        boolean bl = this.isContentView(view);
        int n4 = 0;
        int n5 = this.getWidth();
        int n6 = canvas.save();
        if (bl) {
            int n7 = this.getChildCount();
            for (n2 = 0; n2 < n7; ++n2) {
                View view2 = this.getChildAt(n2);
                n = n4;
                int n8 = n5;
                if (view2 != view) {
                    n = n4;
                    n8 = n5;
                    if (view2.getVisibility() == 0) {
                        n = n4;
                        n8 = n5;
                        if (DrawerLayout.hasOpaqueBackground(view2)) {
                            n = n4;
                            n8 = n5;
                            if (this.isDrawerView(view2)) {
                                if (view2.getHeight() < n3) {
                                    n = n4;
                                    n8 = n5;
                                } else if (this.checkDrawerViewAbsoluteGravity(view2, 3)) {
                                    n8 = view2.getRight();
                                    n = n4;
                                    if (n8 > n4) {
                                        n = n8;
                                    }
                                    n8 = n5;
                                } else {
                                    int n9 = view2.getLeft();
                                    n = n4;
                                    n8 = n5;
                                    if (n9 < n5) {
                                        n8 = n9;
                                        n = n4;
                                    }
                                }
                            }
                        }
                    }
                }
                n4 = n;
                n5 = n8;
            }
            canvas.clipRect(n4, 0, n5, this.getHeight());
        } else {
            n4 = 0;
        }
        boolean bl2 = super.drawChild(canvas, view, l);
        canvas.restoreToCount(n6);
        float f = this.mScrimOpacity;
        if (f > 0.0f && bl) {
            n = this.mScrimColor;
            n2 = (int)((float)((-16777216 & n) >>> 24) * f);
            this.mScrimPaint.setColor(n2 << 24 | n & 16777215);
            canvas.drawRect((float)n4, 0.0f, (float)n5, (float)this.getHeight(), this.mScrimPaint);
            return bl2;
        }
        if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n4 = this.mShadowLeftResolved.getIntrinsicWidth();
            n5 = view.getRight();
            n = this.mLeftDragger.getEdgeSize();
            f = Math.max(0.0f, Math.min((float)n5 / (float)n, 1.0f));
            this.mShadowLeftResolved.setBounds(n5, view.getTop(), n5 + n4, view.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0f * f));
            this.mShadowLeftResolved.draw(canvas);
            return bl2;
        }
        if (this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(view, 5)) {
            n4 = this.mShadowRightResolved.getIntrinsicWidth();
            n5 = view.getLeft();
            n = this.getWidth();
            n2 = this.mRightDragger.getEdgeSize();
            f = Math.max(0.0f, Math.min((float)(n - n5) / (float)n2, 1.0f));
            this.mShadowRightResolved.setBounds(n5 - n4, view.getTop(), n5, view.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0f * f));
            this.mShadowRightResolved.draw(canvas);
        }
        return bl2;
    }

    View findDrawerWithGravity(int n) {
        int n2 = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this));
        int n3 = this.getChildCount();
        for (n = 0; n < n3; ++n) {
            View view = this.getChildAt(n);
            if ((this.getDrawerViewAbsoluteGravity(view) & 7) != (n2 & 7)) continue;
            return view;
        }
        return null;
    }

    View findOpenDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if ((((LayoutParams)view.getLayoutParams()).openState & 1) != 1) continue;
            return view;
        }
        return null;
    }

    View findVisibleDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (!this.isDrawerView(view) || !this.isDrawerVisible(view)) continue;
            return view;
        }
        return null;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    public int getDrawerLockMode(int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        if (n != 3) {
            if (n != 5) {
                if (n != 8388611) {
                    if (n == 8388613) {
                        n = this.mLockModeEnd;
                        if (n != 3) {
                            return n;
                        }
                        n = n2 == 0 ? this.mLockModeRight : this.mLockModeLeft;
                        if (n != 3) {
                            return n;
                        }
                    }
                } else {
                    n = this.mLockModeStart;
                    if (n != 3) {
                        return n;
                    }
                    n = n2 == 0 ? this.mLockModeLeft : this.mLockModeRight;
                    if (n != 3) {
                        return n;
                    }
                }
            } else {
                n = this.mLockModeRight;
                if (n != 3) {
                    return n;
                }
                n = n2 == 0 ? this.mLockModeEnd : this.mLockModeStart;
                if (n != 3) {
                    return n;
                }
            }
        } else {
            n = this.mLockModeLeft;
            if (n != 3) {
                return n;
            }
            n = n2 == 0 ? this.mLockModeStart : this.mLockModeEnd;
            if (n != 3) {
                return n;
            }
        }
        return 0;
    }

    public int getDrawerLockMode(View view) {
        if (this.isDrawerView(view)) {
            return this.getDrawerLockMode(((LayoutParams)view.getLayoutParams()).gravity);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public CharSequence getDrawerTitle(int n) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            return this.mTitleLeft;
        }
        if (n == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    int getDrawerViewAbsoluteGravity(View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
    }

    float getDrawerViewOffset(View view) {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    boolean isContentView(View view) {
        if (((LayoutParams)view.getLayoutParams()).gravity == 0) {
            return true;
        }
        return false;
    }

    public boolean isDrawerOpen(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerOpen(view);
        }
        return false;
    }

    public boolean isDrawerOpen(View view) {
        if (this.isDrawerView(view)) {
            if ((((LayoutParams)view.getLayoutParams()).openState & 1) == 1) {
                return true;
            }
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    boolean isDrawerView(View view) {
        int n = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
        if ((n & 3) != 0) {
            return true;
        }
        if ((n & 5) != 0) {
            return true;
        }
        return false;
    }

    public boolean isDrawerVisible(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerVisible(view);
        }
        return false;
    }

    public boolean isDrawerVisible(View view) {
        if (this.isDrawerView(view)) {
            if (((LayoutParams)view.getLayoutParams()).onScreen > 0.0f) {
                return true;
            }
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    void moveDrawerToOffset(View view, float f) {
        float f2 = this.getDrawerViewOffset(view);
        int n = view.getWidth();
        int n2 = (int)((float)n * f2);
        n = (int)((float)n * f) - n2;
        if (!this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n = - n;
        }
        view.offsetLeftAndRight(n);
        this.setDrawerViewOffset(view, f);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            Object object;
            int n = Build.VERSION.SDK_INT >= 21 ? ((object = this.mLastInsets) != null ? ((WindowInsets)object).getSystemWindowInsetTop() : 0) : 0;
            if (n > 0) {
                this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), n);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean bl;
        boolean bl2;
        int n;
        boolean bl3;
        block11 : {
            int n2;
            block8 : {
                int n3;
                block9 : {
                    block10 : {
                        n = motionEvent.getActionMasked();
                        bl = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent);
                        bl3 = this.mRightDragger.shouldInterceptTouchEvent(motionEvent);
                        n3 = 0;
                        n2 = 0;
                        bl2 = true;
                        if (n == 0) break block8;
                        if (n == 1) break block9;
                        if (n == 2) break block10;
                        if (n == 3) break block9;
                        n = n3;
                        break block11;
                    }
                    n = n3;
                    if (this.mLeftDragger.checkTouchSlop(3)) {
                        this.mLeftCallback.removeCallbacks();
                        this.mRightCallback.removeCallbacks();
                        n = n3;
                    }
                    break block11;
                }
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                n = n3;
                break block11;
            }
            float f = motionEvent.getX();
            float f2 = motionEvent.getY();
            this.mInitialMotionX = f;
            this.mInitialMotionY = f2;
            n = n2;
            if (this.mScrimOpacity > 0.0f) {
                motionEvent = this.mLeftDragger.findTopChildUnder((int)f, (int)f2);
                n = n2;
                if (motionEvent != null) {
                    n = n2;
                    if (this.isContentView((View)motionEvent)) {
                        n = 1;
                    }
                }
            }
            this.mDisallowInterceptRequested = false;
            this.mChildrenCanceledTouch = false;
        }
        boolean bl4 = bl2;
        if (!(bl | bl3)) {
            bl4 = bl2;
            if (n == 0) {
                bl4 = bl2;
                if (!this.hasPeekingDrawer()) {
                    if (this.mChildrenCanceledTouch) {
                        return true;
                    }
                    bl4 = false;
                }
            }
        }
        return bl4;
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4 && this.hasVisibleDrawer()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 4) {
            keyEvent = this.findVisibleDrawer();
            if (keyEvent != null && this.getDrawerLockMode((View)keyEvent) == 0) {
                this.closeDrawers();
            }
            if (keyEvent != null) {
                return true;
            }
            return false;
        }
        return super.onKeyUp(n, keyEvent);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.mInLayout = true;
        int n5 = n3 - n;
        int n6 = this.getChildCount();
        for (n3 = 0; n3 < n6; ++n3) {
            int n7;
            float f;
            View view = this.getChildAt(n3);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.isContentView(view)) {
                view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
                continue;
            }
            int n8 = view.getMeasuredWidth();
            int n9 = view.getMeasuredHeight();
            if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n7 = - n8 + (int)((float)n8 * layoutParams.onScreen);
                f = (float)(n8 + n7) / (float)n8;
            } else {
                n7 = n5 - (int)((float)n8 * layoutParams.onScreen);
                f = (float)(n5 - n7) / (float)n8;
            }
            boolean bl2 = f != layoutParams.onScreen;
            n = layoutParams.gravity & 112;
            if (n != 16) {
                if (n != 80) {
                    view.layout(n7, layoutParams.topMargin, n7 + n8, layoutParams.topMargin + n9);
                } else {
                    n = n4 - n2;
                    view.layout(n7, n - layoutParams.bottomMargin - view.getMeasuredHeight(), n7 + n8, n - layoutParams.bottomMargin);
                }
            } else {
                int n10 = n4 - n2;
                int n11 = (n10 - n9) / 2;
                if (n11 < layoutParams.topMargin) {
                    n = layoutParams.topMargin;
                } else {
                    n = n11;
                    if (n11 + n9 > n10 - layoutParams.bottomMargin) {
                        n = n10 - layoutParams.bottomMargin - n9;
                    }
                }
                view.layout(n7, n, n7 + n8, n + n9);
            }
            if (bl2) {
                this.setDrawerViewOffset(view, f);
            }
            n = layoutParams.onScreen > 0.0f ? 0 : 4;
            if (view.getVisibility() == n) continue;
            view.setVisibility(n);
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    protected void onMeasure(int n, int n2) {
        block29 : {
            block26 : {
                int n3;
                int n4;
                int n5;
                int n6;
                int n7;
                View view;
                int n8;
                int n9;
                Object object;
                block28 : {
                    int n10;
                    int n11;
                    block27 : {
                        object = this;
                        n8 = View.MeasureSpec.getMode((int)n);
                        n3 = View.MeasureSpec.getMode((int)n2);
                        n6 = View.MeasureSpec.getSize((int)n);
                        n5 = View.MeasureSpec.getSize((int)n2);
                        if (n8 != 1073741824) break block27;
                        n10 = n8;
                        n11 = n3;
                        n7 = n6;
                        n9 = n5;
                        if (n3 == 1073741824) break block28;
                    }
                    if (!this.isInEditMode()) break block29;
                    if (n8 == Integer.MIN_VALUE) {
                        n4 = 1073741824;
                    } else {
                        n4 = n8;
                        if (n8 == 0) {
                            n4 = 1073741824;
                            n6 = 300;
                        }
                    }
                    if (n3 == Integer.MIN_VALUE) {
                        n11 = 1073741824;
                        n10 = n4;
                        n7 = n6;
                        n9 = n5;
                    } else {
                        n10 = n4;
                        n11 = n3;
                        n7 = n6;
                        n9 = n5;
                        if (n3 == 0) {
                            n11 = 1073741824;
                            n9 = 300;
                            n7 = n6;
                            n10 = n4;
                        }
                    }
                }
                object.setMeasuredDimension(n7, n9);
                n8 = object.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) ? 1 : 0;
                int n12 = ViewCompat.getLayoutDirection((View)this);
                n6 = 0;
                n4 = 0;
                int n13 = this.getChildCount();
                n5 = 0;
                do {
                    DrawerLayout drawerLayout = this;
                    if (n5 >= n13) break block26;
                    view = drawerLayout.getChildAt(n5);
                    if (view.getVisibility() != 8) {
                        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                        if (n8 != 0) {
                            WindowInsets windowInsets;
                            n3 = GravityCompat.getAbsoluteGravity(layoutParams.gravity, n12);
                            if (ViewCompat.getFitsSystemWindows(view)) {
                                if (Build.VERSION.SDK_INT >= 21) {
                                    windowInsets = (WindowInsets)drawerLayout.mLastInsets;
                                    if (n3 == 3) {
                                        object = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                                    } else {
                                        object = windowInsets;
                                        if (n3 == 5) {
                                            object = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                                        }
                                    }
                                    view.dispatchApplyWindowInsets((WindowInsets)object);
                                }
                            } else if (Build.VERSION.SDK_INT >= 21) {
                                windowInsets = (WindowInsets)drawerLayout.mLastInsets;
                                if (n3 == 3) {
                                    object = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                                } else {
                                    object = windowInsets;
                                    if (n3 == 5) {
                                        object = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                                    }
                                }
                                layoutParams.leftMargin = object.getSystemWindowInsetLeft();
                                layoutParams.topMargin = object.getSystemWindowInsetTop();
                                layoutParams.rightMargin = object.getSystemWindowInsetRight();
                                layoutParams.bottomMargin = object.getSystemWindowInsetBottom();
                            }
                        }
                        if (drawerLayout.isContentView(view)) {
                            view.measure(View.MeasureSpec.makeMeasureSpec((int)(n7 - layoutParams.leftMargin - layoutParams.rightMargin), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n9 - layoutParams.topMargin - layoutParams.bottomMargin), (int)1073741824));
                        } else {
                            float f;
                            float f2;
                            int n14;
                            if (!drawerLayout.isDrawerView(view)) break;
                            if (SET_DRAWER_SHADOW_FROM_ELEVATION && (f2 = ViewCompat.getElevation(view)) != (f = drawerLayout.mDrawerElevation)) {
                                ViewCompat.setElevation(view, f);
                            }
                            n3 = (n14 = drawerLayout.getDrawerViewAbsoluteGravity(view) & 7) == 3 ? 1 : 0;
                            if (n3 != 0 && n6 != 0 || n3 == 0 && n4 != 0) {
                                object = new StringBuilder();
                                object.append("Child drawer has absolute gravity ");
                                object.append(DrawerLayout.gravityToString(n14));
                                object.append(" but this ");
                                object.append("DrawerLayout");
                                object.append(" already has a ");
                                object.append("drawer view along that edge");
                                throw new IllegalStateException(object.toString());
                            }
                            if (n3 != 0) {
                                n6 = 1;
                            } else {
                                n4 = 1;
                            }
                            view.measure(DrawerLayout.getChildMeasureSpec((int)n, (int)(drawerLayout.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin), (int)layoutParams.width), DrawerLayout.getChildMeasureSpec((int)n2, (int)(layoutParams.topMargin + layoutParams.bottomMargin), (int)layoutParams.height));
                        }
                    }
                    ++n5;
                } while (true);
                object = new StringBuilder();
                object.append("Child ");
                object.append((Object)view);
                object.append(" at index ");
                object.append(n5);
                object.append(" does not have a valid layout_gravity - must be Gravity.LEFT, ");
                object.append("Gravity.RIGHT or Gravity.NO_GRAVITY");
                throw new IllegalStateException(object.toString());
            }
            return;
        }
        throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        View view;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        if (parcelable.openDrawerGravity != 0 && (view = this.findDrawerWithGravity(parcelable.openDrawerGravity)) != null) {
            this.openDrawer(view);
        }
        if (parcelable.lockModeLeft != 3) {
            this.setDrawerLockMode(parcelable.lockModeLeft, 3);
        }
        if (parcelable.lockModeRight != 3) {
            this.setDrawerLockMode(parcelable.lockModeRight, 5);
        }
        if (parcelable.lockModeStart != 3) {
            this.setDrawerLockMode(parcelable.lockModeStart, 8388611);
        }
        if (parcelable.lockModeEnd != 3) {
            this.setDrawerLockMode(parcelable.lockModeEnd, 8388613);
        }
    }

    public void onRtlPropertiesChanged(int n) {
        this.resolveShadowDrawables();
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            int n2 = layoutParams.openState;
            boolean bl = false;
            n2 = n2 == 1 ? 1 : 0;
            if (layoutParams.openState == 2) {
                bl = true;
            }
            if (n2 == 0 && !bl) {
                continue;
            }
            savedState.openDrawerGravity = layoutParams.gravity;
            break;
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mLeftDragger.processTouchEvent(motionEvent);
        this.mRightDragger.processTouchEvent(motionEvent);
        int n = motionEvent.getAction() & 255;
        if (n != 0) {
            boolean bl = true;
            if (n != 1) {
                if (n != 3) {
                    return true;
                }
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                return true;
            }
            float f = motionEvent.getX();
            float f2 = motionEvent.getY();
            boolean bl2 = true;
            motionEvent = this.mLeftDragger.findTopChildUnder((int)f, (int)f2);
            boolean bl3 = bl2;
            if (motionEvent != null) {
                bl3 = bl2;
                if (this.isContentView((View)motionEvent)) {
                    n = this.mLeftDragger.getTouchSlop();
                    bl3 = bl2;
                    if (f * (f -= this.mInitialMotionX) + f2 * (f2 -= this.mInitialMotionY) < (float)(n * n)) {
                        motionEvent = this.findOpenDrawer();
                        bl3 = bl2;
                        if (motionEvent != null) {
                            bl3 = this.getDrawerLockMode((View)motionEvent) == 2 ? bl : false;
                        }
                    }
                }
            }
            this.closeDrawers(bl3);
            this.mDisallowInterceptRequested = false;
            return true;
        }
        float f = motionEvent.getX();
        float f3 = motionEvent.getY();
        this.mInitialMotionX = f;
        this.mInitialMotionY = f3;
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        return true;
    }

    public void openDrawer(int n) {
        this.openDrawer(n, true);
    }

    public void openDrawer(int n, boolean bl) {
        Object object = this.findDrawerWithGravity(n);
        if (object != null) {
            this.openDrawer((View)object, bl);
            return;
        }
        object = new StringBuilder();
        object.append("No drawer view found with gravity ");
        object.append(DrawerLayout.gravityToString(n));
        throw new IllegalArgumentException(object.toString());
    }

    public void openDrawer(View view) {
        this.openDrawer(view, true);
    }

    public void openDrawer(View view, boolean bl) {
        if (this.isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 1.0f;
                layoutParams.openState = 1;
                this.updateChildrenImportantForAccessibility(view, true);
            } else if (bl) {
                layoutParams.openState |= 2;
                if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, 0, view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, this.getWidth() - view.getWidth(), view.getTop());
                }
            } else {
                this.moveDrawerToOffset(view, 1.0f);
                this.updateDrawerState(layoutParams.gravity, 0, view);
                view.setVisibility(0);
            }
            this.invalidate();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a sliding drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void removeDrawerListener(DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        List<DrawerListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        list.remove(drawerListener);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        super.requestDisallowInterceptTouchEvent(bl);
        this.mDisallowInterceptRequested = bl;
        if (bl) {
            this.closeDrawers(true);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void setChildInsets(Object object, boolean bl) {
        this.mLastInsets = object;
        this.mDrawStatusBarBackground = bl;
        bl = !bl && this.getBackground() == null;
        this.setWillNotDraw(bl);
        this.requestLayout();
    }

    public void setDrawerElevation(float f) {
        this.mDrawerElevation = f;
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            if (!this.isDrawerView(view)) continue;
            ViewCompat.setElevation(view, this.mDrawerElevation);
        }
    }

    @Deprecated
    public void setDrawerListener(DrawerListener drawerListener) {
        DrawerListener drawerListener2 = this.mListener;
        if (drawerListener2 != null) {
            this.removeDrawerListener(drawerListener2);
        }
        if (drawerListener != null) {
            this.addDrawerListener(drawerListener);
        }
        this.mListener = drawerListener;
    }

    public void setDrawerLockMode(int n) {
        this.setDrawerLockMode(n, 3);
        this.setDrawerLockMode(n, 5);
    }

    public void setDrawerLockMode(int n, int n2) {
        ViewDragHelper viewDragHelper;
        int n3 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this));
        if (n2 != 3) {
            if (n2 != 5) {
                if (n2 != 8388611) {
                    if (n2 == 8388613) {
                        this.mLockModeEnd = n;
                    }
                } else {
                    this.mLockModeStart = n;
                }
            } else {
                this.mLockModeRight = n;
            }
        } else {
            this.mLockModeLeft = n;
        }
        if (n != 0) {
            viewDragHelper = n3 == 3 ? this.mLeftDragger : this.mRightDragger;
            viewDragHelper.cancel();
        }
        if (n != 1) {
            if (n != 2) {
                return;
            }
            viewDragHelper = this.findDrawerWithGravity(n3);
            if (viewDragHelper != null) {
                this.openDrawer((View)viewDragHelper);
                return;
            }
        } else {
            viewDragHelper = this.findDrawerWithGravity(n3);
            if (viewDragHelper != null) {
                this.closeDrawer((View)viewDragHelper);
            }
        }
    }

    public void setDrawerLockMode(int n, View view) {
        if (this.isDrawerView(view)) {
            this.setDrawerLockMode(n, ((LayoutParams)view.getLayoutParams()).gravity);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append((Object)view);
        stringBuilder.append(" is not a ");
        stringBuilder.append("drawer with appropriate layout_gravity");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setDrawerShadow(int n, int n2) {
        this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), n), n2);
    }

    public void setDrawerShadow(Drawable drawable2, int n) {
        block8 : {
            block5 : {
                block7 : {
                    block6 : {
                        block4 : {
                            if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
                                return;
                            }
                            if ((n & 8388611) != 8388611) break block4;
                            this.mShadowStart = drawable2;
                            break block5;
                        }
                        if ((n & 8388613) != 8388613) break block6;
                        this.mShadowEnd = drawable2;
                        break block5;
                    }
                    if ((n & 3) != 3) break block7;
                    this.mShadowLeft = drawable2;
                    break block5;
                }
                if ((n & 5) != 5) break block8;
                this.mShadowRight = drawable2;
            }
            this.resolveShadowDrawables();
            this.invalidate();
            return;
        }
    }

    public void setDrawerTitle(int n, CharSequence charSequence) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            this.mTitleLeft = charSequence;
            return;
        }
        if (n == 5) {
            this.mTitleRight = charSequence;
        }
    }

    void setDrawerViewOffset(View view, float f) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (f == layoutParams.onScreen) {
            return;
        }
        layoutParams.onScreen = f;
        this.dispatchOnDrawerSlide(view, f);
    }

    public void setScrimColor(int n) {
        this.mScrimColor = n;
        this.invalidate();
    }

    public void setStatusBarBackground(int n) {
        Drawable drawable2 = n != 0 ? ContextCompat.getDrawable(this.getContext(), n) : null;
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackground(Drawable drawable2) {
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackgroundColor(int n) {
        this.mStatusBarBackground = new ColorDrawable(n);
        this.invalidate();
    }

    void updateDrawerState(int n, int n2, View list) {
        n = this.mLeftDragger.getViewDragState();
        int n3 = this.mRightDragger.getViewDragState();
        n = n != 1 && n3 != 1 ? (n != 2 && n3 != 2 ? 0 : 2) : 1;
        if (list != null && n2 == 0) {
            LayoutParams layoutParams = (LayoutParams)list.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                this.dispatchOnDrawerClosed((View)list);
            } else if (layoutParams.onScreen == 1.0f) {
                this.dispatchOnDrawerOpened((View)list);
            }
        }
        if (n != this.mDrawerState) {
            this.mDrawerState = n;
            list = this.mListeners;
            if (list != null) {
                for (n2 = list.size() - 1; n2 >= 0; --n2) {
                    this.mListeners.get(n2).onDrawerStateChanged(n);
                }
            }
        }
    }

    class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;

        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = viewGroup.getChildAt(i);
                if (!DrawerLayout.includeChildForAccessibility(view)) continue;
                accessibilityNodeInfoCompat.addChild(view);
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(View object, AccessibilityEvent object2) {
            if (object2.getEventType() == 32) {
                object = object2.getText();
                object2 = DrawerLayout.this.findVisibleDrawer();
                if (object2 != null) {
                    int n = DrawerLayout.this.getDrawerViewAbsoluteGravity((View)object2);
                    object2 = DrawerLayout.this.getDrawerTitle(n);
                    if (object2 != null) {
                        object.add(object2);
                    }
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent((View)object, (AccessibilityEvent)object2);
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)DrawerLayout.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            } else {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat.setSource(view);
                ViewParent viewParent = ViewCompat.getParentForAccessibility(view);
                if (viewParent instanceof View) {
                    accessibilityNodeInfoCompat.setParent((View)viewParent);
                }
                this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat2.recycle();
                this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)view);
            }
            accessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
            accessibilityNodeInfoCompat.setFocusable(false);
            accessibilityNodeInfoCompat.setFocused(false);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (!DrawerLayout.CAN_HIDE_DESCENDANTS && !DrawerLayout.includeChildForAccessibility(view)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    static final class ChildAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DrawerLayout.includeChildForAccessibility(view)) {
                accessibilityNodeInfoCompat.setParent(null);
            }
        }
    }

    public static interface DrawerListener {
        public void onDrawerClosed(View var1);

        public void onDrawerOpened(View var1);

        public void onDrawerSlide(View var1, float var2);

        public void onDrawerStateChanged(int var1);
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity = 0;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(int n, int n2, int n3) {
            this(n, n2);
            this.gravity = n3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = context.getInt(0, 0);
            context.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.gravity = layoutParams.gravity;
        }
    }

    protected static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity = 0;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.openDrawerGravity = parcel.readInt();
            this.lockModeLeft = parcel.readInt();
            this.lockModeRight = parcel.readInt();
            this.lockModeStart = parcel.readInt();
            this.lockModeEnd = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.openDrawerGravity);
            parcel.writeInt(this.lockModeLeft);
            parcel.writeInt(this.lockModeRight);
            parcel.writeInt(this.lockModeStart);
            parcel.writeInt(this.lockModeEnd);
        }

    }

    public static abstract class SimpleDrawerListener
    implements DrawerListener {
        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerSlide(View view, float f) {
        }

        @Override
        public void onDrawerStateChanged(int n) {
        }
    }

    private class ViewDragCallback
    extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;

        ViewDragCallback(int n) {
            this.mPeekRunnable = new Runnable(){

                @Override
                public void run() {
                    ViewDragCallback.this.peekDrawer();
                }
            };
            this.mAbsGravity = n;
        }

        private void closeOtherDrawer() {
            View view;
            int n = this.mAbsGravity;
            int n2 = 3;
            if (n == 3) {
                n2 = 5;
            }
            if ((view = DrawerLayout.this.findDrawerWithGravity(n2)) != null) {
                DrawerLayout.this.closeDrawer(view);
            }
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max(- view.getWidth(), Math.min(n, 0));
            }
            n2 = DrawerLayout.this.getWidth();
            return Math.max(n2 - view.getWidth(), Math.min(n, n2));
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            if (DrawerLayout.this.isDrawerView(view)) {
                return view.getWidth();
            }
            return 0;
        }

        @Override
        public void onEdgeDragStarted(int n, int n2) {
            View view = (n & 1) == 1 ? DrawerLayout.this.findDrawerWithGravity(3) : DrawerLayout.this.findDrawerWithGravity(5);
            if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, n2);
            }
        }

        @Override
        public boolean onEdgeLock(int n) {
            return false;
        }

        @Override
        public void onEdgeTouched(int n, int n2) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }

        @Override
        public void onViewCaptured(View view, int n) {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            this.closeOtherDrawer();
        }

        @Override
        public void onViewDragStateChanged(int n) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, n, this.mDragger.getCapturedView());
        }

        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            n2 = view.getWidth();
            float f = DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3) ? (float)(n2 + n) / (float)n2 : (float)(DrawerLayout.this.getWidth() - n) / (float)n2;
            DrawerLayout.this.setDrawerViewOffset(view, f);
            n = f == 0.0f ? 4 : 0;
            view.setVisibility(n);
            DrawerLayout.this.invalidate();
        }

        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            f2 = DrawerLayout.this.getDrawerViewOffset(view);
            int n2 = view.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n = f <= 0.0f && (f != 0.0f || f2 <= 0.5f) ? - n2 : 0;
            } else {
                n = DrawerLayout.this.getWidth();
                if (f < 0.0f || f == 0.0f && f2 > 0.5f) {
                    n -= n2;
                }
            }
            this.mDragger.settleCapturedViewAt(n, view.getTop());
            DrawerLayout.this.invalidate();
        }

        void peekDrawer() {
            View view;
            int n = this.mDragger.getEdgeSize();
            int n2 = this.mAbsGravity;
            int n3 = 0;
            n2 = n2 == 3 ? 1 : 0;
            if (n2 != 0) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
                if (view != null) {
                    n3 = - view.getWidth();
                }
                n3 += n;
            } else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
                n3 = DrawerLayout.this.getWidth() - n;
            }
            if (view != null && (n2 != 0 && view.getLeft() < n3 || n2 == 0 && view.getLeft() > n3) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                this.mDragger.smoothSlideViewTo(view, n3, view.getTop());
                layoutParams.isPeeking = true;
                DrawerLayout.this.invalidate();
                this.closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper viewDragHelper) {
            this.mDragger = viewDragHelper;
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            if (DrawerLayout.this.isDrawerView(view) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                return true;
            }
            return false;
        }

    }

}

