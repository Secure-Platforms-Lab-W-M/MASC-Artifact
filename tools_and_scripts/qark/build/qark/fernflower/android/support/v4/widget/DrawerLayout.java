package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup {
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
   private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
   private final DrawerLayout.ChildAccessibilityDelegate mChildAccessibilityDelegate;
   private boolean mChildrenCanceledTouch;
   private boolean mDisallowInterceptRequested;
   private boolean mDrawStatusBarBackground;
   private float mDrawerElevation;
   private int mDrawerState;
   private boolean mFirstLayout;
   private boolean mInLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private Object mLastInsets;
   private final DrawerLayout.ViewDragCallback mLeftCallback;
   private final ViewDragHelper mLeftDragger;
   @Nullable
   private DrawerLayout.DrawerListener mListener;
   private List mListeners;
   private int mLockModeEnd;
   private int mLockModeLeft;
   private int mLockModeRight;
   private int mLockModeStart;
   private int mMinDrawerMargin;
   private final ArrayList mNonDrawerViews;
   private final DrawerLayout.ViewDragCallback mRightCallback;
   private final ViewDragHelper mRightDragger;
   private int mScrimColor;
   private float mScrimOpacity;
   private Paint mScrimPaint;
   private Drawable mShadowEnd;
   private Drawable mShadowLeft;
   private Drawable mShadowLeftResolved;
   private Drawable mShadowRight;
   private Drawable mShadowRightResolved;
   private Drawable mShadowStart;
   private Drawable mStatusBarBackground;
   private CharSequence mTitleLeft;
   private CharSequence mTitleRight;

   static {
      boolean var1 = true;
      THEME_ATTRS = new int[]{16843828};
      LAYOUT_ATTRS = new int[]{16842931};
      boolean var0;
      if (VERSION.SDK_INT >= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      CAN_HIDE_DESCENDANTS = var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = var1;
      } else {
         var0 = false;
      }

      SET_DRAWER_SHADOW_FROM_ELEVATION = var0;
   }

   public DrawerLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DrawerLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public DrawerLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mChildAccessibilityDelegate = new DrawerLayout.ChildAccessibilityDelegate();
      this.mScrimColor = -1728053248;
      this.mScrimPaint = new Paint();
      this.mFirstLayout = true;
      this.mLockModeLeft = 3;
      this.mLockModeRight = 3;
      this.mLockModeStart = 3;
      this.mLockModeEnd = 3;
      this.mShadowStart = null;
      this.mShadowEnd = null;
      this.mShadowLeft = null;
      this.mShadowRight = null;
      this.setDescendantFocusability(262144);
      float var4 = this.getResources().getDisplayMetrics().density;
      this.mMinDrawerMargin = (int)(64.0F * var4 + 0.5F);
      float var5 = 400.0F * var4;
      this.mLeftCallback = new DrawerLayout.ViewDragCallback(3);
      this.mRightCallback = new DrawerLayout.ViewDragCallback(5);
      this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
      this.mLeftDragger.setEdgeTrackingEnabled(1);
      this.mLeftDragger.setMinVelocity(var5);
      this.mLeftCallback.setDragger(this.mLeftDragger);
      this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
      this.mRightDragger.setEdgeTrackingEnabled(2);
      this.mRightDragger.setMinVelocity(var5);
      this.mRightCallback.setDragger(this.mRightDragger);
      this.setFocusableInTouchMode(true);
      ViewCompat.setImportantForAccessibility(this, 1);
      ViewCompat.setAccessibilityDelegate(this, new DrawerLayout.AccessibilityDelegate());
      ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
      if (ViewCompat.getFitsSystemWindows(this)) {
         if (VERSION.SDK_INT >= 21) {
            this.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
               @TargetApi(21)
               public WindowInsets onApplyWindowInsets(View var1, WindowInsets var2) {
                  DrawerLayout var4 = (DrawerLayout)var1;
                  boolean var3;
                  if (var2.getSystemWindowInsetTop() > 0) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  var4.setChildInsets(var2, var3);
                  return var2.consumeSystemWindowInsets();
               }
            });
            this.setSystemUiVisibility(1280);
            TypedArray var8 = var1.obtainStyledAttributes(THEME_ATTRS);

            try {
               this.mStatusBarBackground = var8.getDrawable(0);
            } finally {
               var8.recycle();
            }
         } else {
            this.mStatusBarBackground = null;
         }
      }

      this.mDrawerElevation = 10.0F * var4;
      this.mNonDrawerViews = new ArrayList();
   }

   static String gravityToString(int var0) {
      if ((var0 & 3) == 3) {
         return "LEFT";
      } else {
         return (var0 & 5) == 5 ? "RIGHT" : Integer.toHexString(var0);
      }
   }

   private static boolean hasOpaqueBackground(View var0) {
      Drawable var2 = var0.getBackground();
      boolean var1 = false;
      if (var2 != null) {
         if (var2.getOpacity() == -1) {
            var1 = true;
         }

         return var1;
      } else {
         return false;
      }
   }

   private boolean hasPeekingDrawer() {
      int var2 = this.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         if (((DrawerLayout.LayoutParams)this.getChildAt(var1).getLayoutParams()).isPeeking) {
            return true;
         }
      }

      return false;
   }

   private boolean hasVisibleDrawer() {
      return this.findVisibleDrawer() != null;
   }

   static boolean includeChildForAccessibility(View var0) {
      return ViewCompat.getImportantForAccessibility(var0) != 4 && ViewCompat.getImportantForAccessibility(var0) != 2;
   }

   private boolean mirror(Drawable var1, int var2) {
      if (var1 != null && DrawableCompat.isAutoMirrored(var1)) {
         DrawableCompat.setLayoutDirection(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   private Drawable resolveLeftShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if (var1 == 0) {
         var2 = this.mShadowStart;
         if (var2 != null) {
            this.mirror(var2, var1);
            return this.mShadowStart;
         }
      } else {
         var2 = this.mShadowEnd;
         if (var2 != null) {
            this.mirror(var2, var1);
            return this.mShadowEnd;
         }
      }

      return this.mShadowLeft;
   }

   private Drawable resolveRightShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if (var1 == 0) {
         var2 = this.mShadowEnd;
         if (var2 != null) {
            this.mirror(var2, var1);
            return this.mShadowEnd;
         }
      } else {
         var2 = this.mShadowStart;
         if (var2 != null) {
            this.mirror(var2, var1);
            return this.mShadowStart;
         }
      }

      return this.mShadowRight;
   }

   private void resolveShadowDrawables() {
      if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         this.mShadowLeftResolved = this.resolveLeftShadow();
         this.mShadowRightResolved = this.resolveRightShadow();
      }
   }

   private void updateChildrenImportantForAccessibility(View var1, boolean var2) {
      int var4 = this.getChildCount();

      for(int var3 = 0; var3 < var4; ++var3) {
         View var5 = this.getChildAt(var3);
         if ((var2 || this.isDrawerView(var5)) && (!var2 || var5 != var1)) {
            ViewCompat.setImportantForAccessibility(var5, 4);
         } else {
            ViewCompat.setImportantForAccessibility(var5, 1);
         }
      }

   }

   public void addDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if (var1 != null) {
         if (this.mListeners == null) {
            this.mListeners = new ArrayList();
         }

         this.mListeners.add(var1);
      }
   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      if (this.getDescendantFocusability() != 393216) {
         int var6 = this.getChildCount();
         boolean var5 = false;

         int var4;
         View var7;
         for(var4 = 0; var4 < var6; ++var4) {
            var7 = this.getChildAt(var4);
            if (this.isDrawerView(var7)) {
               if (this.isDrawerOpen(var7)) {
                  var5 = true;
                  var7.addFocusables(var1, var2, var3);
               }
            } else {
               this.mNonDrawerViews.add(var7);
            }
         }

         if (!var5) {
            int var8 = this.mNonDrawerViews.size();

            for(var4 = 0; var4 < var8; ++var4) {
               var7 = (View)this.mNonDrawerViews.get(var4);
               if (var7.getVisibility() == 0) {
                  var7.addFocusables(var1, var2, var3);
               }
            }
         }

         this.mNonDrawerViews.clear();
      }
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (this.findOpenDrawer() == null && !this.isDrawerView(var1)) {
         ViewCompat.setImportantForAccessibility(var1, 1);
      } else {
         ViewCompat.setImportantForAccessibility(var1, 4);
      }

      if (!CAN_HIDE_DESCENDANTS) {
         ViewCompat.setAccessibilityDelegate(var1, this.mChildAccessibilityDelegate);
      }

   }

   void cancelChildViewTouch() {
      if (!this.mChildrenCanceledTouch) {
         long var3 = SystemClock.uptimeMillis();
         MotionEvent var5 = MotionEvent.obtain(var3, var3, 3, 0.0F, 0.0F, 0);
         int var2 = this.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            this.getChildAt(var1).dispatchTouchEvent(var5);
         }

         var5.recycle();
         this.mChildrenCanceledTouch = true;
      }

   }

   boolean checkDrawerViewAbsoluteGravity(View var1, int var2) {
      return (this.getDrawerViewAbsoluteGravity(var1) & var2) == var2;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof DrawerLayout.LayoutParams && super.checkLayoutParams(var1);
   }

   public void closeDrawer(int var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if (var3 != null) {
         this.closeDrawer(var3, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No drawer view found with gravity ");
         var4.append(gravityToString(var1));
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public void closeDrawer(View var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(View var1, boolean var2) {
      if (this.isDrawerView(var1)) {
         DrawerLayout.LayoutParams var4 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if (this.mFirstLayout) {
            var4.onScreen = 0.0F;
            var4.openState = 0;
         } else if (var2) {
            var4.openState |= 4;
            if (this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, -var1.getWidth(), var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 0.0F);
            this.updateDrawerState(var4.gravity, 0, var1);
            var1.setVisibility(4);
         }

         this.invalidate();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("View ");
         var3.append(var1);
         var3.append(" is not a sliding drawer");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void closeDrawers() {
      this.closeDrawers(false);
   }

   void closeDrawers(boolean var1) {
      boolean var2 = false;
      int var5 = this.getChildCount();

      boolean var4;
      for(int var3 = 0; var3 < var5; var2 = var4) {
         View var6 = this.getChildAt(var3);
         DrawerLayout.LayoutParams var7 = (DrawerLayout.LayoutParams)var6.getLayoutParams();
         var4 = var2;
         if (this.isDrawerView(var6)) {
            if (var1 && !var7.isPeeking) {
               var4 = var2;
            } else {
               int var8 = var6.getWidth();
               if (this.checkDrawerViewAbsoluteGravity(var6, 3)) {
                  var2 |= this.mLeftDragger.smoothSlideViewTo(var6, -var8, var6.getTop());
               } else {
                  var2 |= this.mRightDragger.smoothSlideViewTo(var6, this.getWidth(), var6.getTop());
               }

               var7.isPeeking = false;
               var4 = var2;
            }
         }

         ++var3;
      }

      this.mLeftCallback.removeCallbacks();
      this.mRightCallback.removeCallbacks();
      if (var2) {
         this.invalidate();
      }

   }

   public void computeScroll() {
      int var3 = this.getChildCount();
      float var1 = 0.0F;

      for(int var2 = 0; var2 < var3; ++var2) {
         var1 = Math.max(var1, ((DrawerLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).onScreen);
      }

      this.mScrimOpacity = var1;
      boolean var4 = this.mLeftDragger.continueSettling(true);
      boolean var5 = this.mRightDragger.continueSettling(true);
      if (var4 || var5) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void dispatchOnDrawerClosed(View var1) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if ((var3.openState & 1) == 1) {
         var3.openState = 0;
         List var4 = this.mListeners;
         if (var4 != null) {
            for(int var2 = var4.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerClosed(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, false);
         if (this.hasWindowFocus()) {
            var1 = this.getRootView();
            if (var1 != null) {
               var1.sendAccessibilityEvent(32);
            }
         }
      }

   }

   void dispatchOnDrawerOpened(View var1) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if ((var3.openState & 1) == 0) {
         var3.openState = 1;
         List var4 = this.mListeners;
         if (var4 != null) {
            for(int var2 = var4.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerOpened(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, true);
         if (this.hasWindowFocus()) {
            this.sendAccessibilityEvent(32);
         }
      }

   }

   void dispatchOnDrawerSlide(View var1, float var2) {
      List var4 = this.mListeners;
      if (var4 != null) {
         for(int var3 = var4.size() - 1; var3 >= 0; --var3) {
            ((DrawerLayout.DrawerListener)this.mListeners.get(var3)).onDrawerSlide(var1, var2);
         }
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      int var13 = this.getHeight();
      boolean var15 = this.isContentView(var2);
      int var6 = 0;
      int var7 = this.getWidth();
      int var12 = var1.save();
      int var8;
      int var9;
      if (var15) {
         int var14 = this.getChildCount();

         int var10;
         for(var9 = 0; var9 < var14; var7 = var10) {
            View var17 = this.getChildAt(var9);
            var8 = var6;
            var10 = var7;
            if (var17 != var2) {
               var8 = var6;
               var10 = var7;
               if (var17.getVisibility() == 0) {
                  var8 = var6;
                  var10 = var7;
                  if (hasOpaqueBackground(var17)) {
                     var8 = var6;
                     var10 = var7;
                     if (this.isDrawerView(var17)) {
                        if (var17.getHeight() < var13) {
                           var8 = var6;
                           var10 = var7;
                        } else if (this.checkDrawerViewAbsoluteGravity(var17, 3)) {
                           var10 = var17.getRight();
                           var8 = var6;
                           if (var10 > var6) {
                              var8 = var10;
                           }

                           var10 = var7;
                        } else {
                           int var11 = var17.getLeft();
                           var8 = var6;
                           var10 = var7;
                           if (var11 < var7) {
                              var10 = var11;
                              var8 = var6;
                           }
                        }
                     }
                  }
               }
            }

            ++var9;
            var6 = var8;
         }

         var1.clipRect(var6, 0, var7, this.getHeight());
      } else {
         var6 = 0;
      }

      boolean var16 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var12);
      float var5 = this.mScrimOpacity;
      if (var5 > 0.0F && var15) {
         var8 = this.mScrimColor;
         var9 = (int)((float)((-16777216 & var8) >>> 24) * var5);
         this.mScrimPaint.setColor(var9 << 24 | var8 & 16777215);
         var1.drawRect((float)var6, 0.0F, (float)var7, (float)this.getHeight(), this.mScrimPaint);
         return var16;
      } else {
         if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 3)) {
            var6 = this.mShadowLeftResolved.getIntrinsicWidth();
            var7 = var2.getRight();
            var8 = this.mLeftDragger.getEdgeSize();
            var5 = Math.max(0.0F, Math.min((float)var7 / (float)var8, 1.0F));
            this.mShadowLeftResolved.setBounds(var7, var2.getTop(), var7 + var6, var2.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0F * var5));
            this.mShadowLeftResolved.draw(var1);
         } else if (this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 5)) {
            var6 = this.mShadowRightResolved.getIntrinsicWidth();
            var7 = var2.getLeft();
            var8 = this.getWidth();
            var9 = this.mRightDragger.getEdgeSize();
            var5 = Math.max(0.0F, Math.min((float)(var8 - var7) / (float)var9, 1.0F));
            this.mShadowRightResolved.setBounds(var7 - var6, var2.getTop(), var7, var2.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0F * var5));
            this.mShadowRightResolved.draw(var1);
            return var16;
         }

         return var16;
      }
   }

   View findDrawerWithGravity(int var1) {
      int var2 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      int var3 = this.getChildCount();

      for(var1 = 0; var1 < var3; ++var1) {
         View var4 = this.getChildAt(var1);
         if ((this.getDrawerViewAbsoluteGravity(var4) & 7) == (var2 & 7)) {
            return var4;
         }
      }

      return null;
   }

   View findOpenDrawer() {
      int var2 = this.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         View var3 = this.getChildAt(var1);
         if ((((DrawerLayout.LayoutParams)var3.getLayoutParams()).openState & 1) == 1) {
            return var3;
         }
      }

      return null;
   }

   View findVisibleDrawer() {
      int var2 = this.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         View var3 = this.getChildAt(var1);
         if (this.isDrawerView(var3) && this.isDrawerVisible(var3)) {
            return var3;
         }
      }

      return null;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new DrawerLayout.LayoutParams(-1, -1);
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new DrawerLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof DrawerLayout.LayoutParams) {
         return new DrawerLayout.LayoutParams((DrawerLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new DrawerLayout.LayoutParams((MarginLayoutParams)var1) : new DrawerLayout.LayoutParams(var1);
      }
   }

   public float getDrawerElevation() {
      return SET_DRAWER_SHADOW_FROM_ELEVATION ? this.mDrawerElevation : 0.0F;
   }

   public int getDrawerLockMode(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      if (var1 != 3) {
         if (var1 != 5) {
            if (var1 != 8388611) {
               if (var1 == 8388613) {
                  var1 = this.mLockModeEnd;
                  if (var1 != 3) {
                     return var1;
                  }

                  if (var2 == 0) {
                     var1 = this.mLockModeRight;
                  } else {
                     var1 = this.mLockModeLeft;
                  }

                  if (var1 != 3) {
                     return var1;
                  }
               }
            } else {
               var1 = this.mLockModeStart;
               if (var1 != 3) {
                  return var1;
               }

               if (var2 == 0) {
                  var1 = this.mLockModeLeft;
               } else {
                  var1 = this.mLockModeRight;
               }

               if (var1 != 3) {
                  return var1;
               }
            }
         } else {
            var1 = this.mLockModeRight;
            if (var1 != 3) {
               return var1;
            }

            if (var2 == 0) {
               var1 = this.mLockModeEnd;
            } else {
               var1 = this.mLockModeStart;
            }

            if (var1 != 3) {
               return var1;
            }
         }
      } else {
         var1 = this.mLockModeLeft;
         if (var1 != 3) {
            return var1;
         }

         if (var2 == 0) {
            var1 = this.mLockModeStart;
         } else {
            var1 = this.mLockModeEnd;
         }

         if (var1 != 3) {
            return var1;
         }
      }

      return 0;
   }

   public int getDrawerLockMode(View var1) {
      if (this.isDrawerView(var1)) {
         return this.getDrawerLockMode(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("View ");
         var2.append(var1);
         var2.append(" is not a drawer");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   @Nullable
   public CharSequence getDrawerTitle(int var1) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      if (var1 == 3) {
         return this.mTitleLeft;
      } else {
         return var1 == 5 ? this.mTitleRight : null;
      }
   }

   int getDrawerViewAbsoluteGravity(View var1) {
      return GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
   }

   float getDrawerViewOffset(View var1) {
      return ((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen;
   }

   public Drawable getStatusBarBackgroundDrawable() {
      return this.mStatusBarBackground;
   }

   boolean isContentView(View var1) {
      return ((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity == 0;
   }

   public boolean isDrawerOpen(int var1) {
      View var2 = this.findDrawerWithGravity(var1);
      return var2 != null ? this.isDrawerOpen(var2) : false;
   }

   public boolean isDrawerOpen(View var1) {
      if (this.isDrawerView(var1)) {
         return (((DrawerLayout.LayoutParams)var1.getLayoutParams()).openState & 1) == 1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("View ");
         var2.append(var1);
         var2.append(" is not a drawer");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   boolean isDrawerView(View var1) {
      int var2 = GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(var1));
      if ((var2 & 3) != 0) {
         return true;
      } else {
         return (var2 & 5) != 0;
      }
   }

   public boolean isDrawerVisible(int var1) {
      View var2 = this.findDrawerWithGravity(var1);
      return var2 != null ? this.isDrawerVisible(var2) : false;
   }

   public boolean isDrawerVisible(View var1) {
      if (this.isDrawerView(var1)) {
         return ((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen > 0.0F;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("View ");
         var2.append(var1);
         var2.append(" is not a drawer");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   void moveDrawerToOffset(View var1, float var2) {
      float var3 = this.getDrawerViewOffset(var1);
      int var4 = var1.getWidth();
      int var5 = (int)((float)var4 * var3);
      var4 = (int)((float)var4 * var2) - var5;
      if (!this.checkDrawerViewAbsoluteGravity(var1, 3)) {
         var4 = -var4;
      }

      var1.offsetLeftAndRight(var4);
      this.setDrawerViewOffset(var1, var2);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
         int var2;
         if (VERSION.SDK_INT >= 21) {
            Object var3 = this.mLastInsets;
            if (var3 != null) {
               var2 = ((WindowInsets)var3).getSystemWindowInsetTop();
            } else {
               var2 = 0;
            }
         } else {
            var2 = 0;
         }

         if (var2 > 0) {
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), var2);
            this.mStatusBarBackground.draw(var1);
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var4 = var1.getActionMasked();
      boolean var9 = this.mLeftDragger.shouldInterceptTouchEvent(var1);
      boolean var10 = this.mRightDragger.shouldInterceptTouchEvent(var1);
      boolean var6 = false;
      boolean var5 = false;
      boolean var8 = true;
      boolean var12;
      if (var4 != 0) {
         label40: {
            if (var4 != 1) {
               if (var4 == 2) {
                  var12 = var6;
                  if (this.mLeftDragger.checkTouchSlop(3)) {
                     this.mLeftCallback.removeCallbacks();
                     this.mRightCallback.removeCallbacks();
                     var12 = var6;
                  }
                  break label40;
               }

               if (var4 != 3) {
                  var12 = var6;
                  break label40;
               }
            }

            this.closeDrawers(true);
            this.mDisallowInterceptRequested = false;
            this.mChildrenCanceledTouch = false;
            var12 = var6;
         }
      } else {
         float var2 = var1.getX();
         float var3 = var1.getY();
         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         var12 = var5;
         if (this.mScrimOpacity > 0.0F) {
            View var11 = this.mLeftDragger.findTopChildUnder((int)var2, (int)var3);
            var12 = var5;
            if (var11 != null) {
               var12 = var5;
               if (this.isContentView(var11)) {
                  var12 = true;
               }
            }
         }

         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
      }

      boolean var7 = var8;
      if (!(var9 | var10)) {
         var7 = var8;
         if (!var12) {
            var7 = var8;
            if (!this.hasPeekingDrawer()) {
               if (this.mChildrenCanceledTouch) {
                  return true;
               }

               var7 = false;
            }
         }
      }

      return var7;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (var1 == 4 && this.hasVisibleDrawer()) {
         var2.startTracking();
         return true;
      } else {
         return super.onKeyDown(var1, var2);
      }
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      if (var1 == 4) {
         View var3 = this.findVisibleDrawer();
         if (var3 != null && this.getDrawerLockMode(var3) == 0) {
            this.closeDrawers();
         }

         return var3 != null;
      } else {
         return super.onKeyUp(var1, var2);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mInLayout = true;
      int var10 = var4 - var2;
      int var11 = this.getChildCount();

      for(var4 = 0; var4 < var11; ++var4) {
         View var15 = this.getChildAt(var4);
         if (var15.getVisibility() != 8) {
            DrawerLayout.LayoutParams var16 = (DrawerLayout.LayoutParams)var15.getLayoutParams();
            if (this.isContentView(var15)) {
               var15.layout(var16.leftMargin, var16.topMargin, var16.leftMargin + var15.getMeasuredWidth(), var16.topMargin + var15.getMeasuredHeight());
            } else {
               int var12 = var15.getMeasuredWidth();
               int var13 = var15.getMeasuredHeight();
               float var6;
               int var7;
               if (this.checkDrawerViewAbsoluteGravity(var15, 3)) {
                  var7 = -var12 + (int)((float)var12 * var16.onScreen);
                  var6 = (float)(var12 + var7) / (float)var12;
               } else {
                  var7 = var10 - (int)((float)var12 * var16.onScreen);
                  var6 = (float)(var10 - var7) / (float)var12;
               }

               boolean var8;
               if (var6 != var16.onScreen) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var2 = var16.gravity & 112;
               if (var2 != 16) {
                  if (var2 != 80) {
                     var15.layout(var7, var16.topMargin, var7 + var12, var16.topMargin + var13);
                  } else {
                     var2 = var5 - var3;
                     var15.layout(var7, var2 - var16.bottomMargin - var15.getMeasuredHeight(), var7 + var12, var2 - var16.bottomMargin);
                  }
               } else {
                  int var14 = var5 - var3;
                  int var9 = (var14 - var13) / 2;
                  if (var9 < var16.topMargin) {
                     var2 = var16.topMargin;
                  } else {
                     var2 = var9;
                     if (var9 + var13 > var14 - var16.bottomMargin) {
                        var2 = var14 - var16.bottomMargin - var13;
                     }
                  }

                  var15.layout(var7, var2, var7 + var12, var2 + var13);
               }

               if (var8) {
                  this.setDrawerViewOffset(var15, var6);
               }

               byte var17;
               if (var16.onScreen > 0.0F) {
                  var17 = 0;
               } else {
                  var17 = 4;
               }

               if (var15.getVisibility() != var17) {
                  var15.setVisibility(var17);
               }
            }
         }
      }

      this.mInLayout = false;
      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var10;
      int var11;
      int var12;
      int var13;
      label116: {
         int var9 = MeasureSpec.getMode(var1);
         var13 = MeasureSpec.getMode(var2);
         int var8 = MeasureSpec.getSize(var1);
         var12 = MeasureSpec.getSize(var2);
         if (var9 == 1073741824) {
            var10 = var8;
            var11 = var12;
            if (var13 == 1073741824) {
               break label116;
            }
         }

         if (!this.isInEditMode()) {
            IllegalArgumentException var17 = new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            throw var17;
         }

         int var6;
         if (var9 == Integer.MIN_VALUE) {
            var6 = 1073741824;
         } else if (var9 == 0) {
            var6 = 1073741824;
            var8 = 300;
         }

         int var5;
         if (var13 == Integer.MIN_VALUE) {
            var5 = 1073741824;
            var10 = var8;
            var11 = var12;
         } else {
            var10 = var8;
            var11 = var12;
            if (var13 == 0) {
               var5 = 1073741824;
               var11 = 300;
               var10 = var8;
            }
         }
      }

      this.setMeasuredDimension(var10, var11);
      boolean var23;
      if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this)) {
         var23 = true;
      } else {
         var23 = false;
      }

      int var14 = ViewCompat.getLayoutDirection(this);
      boolean var24 = false;
      boolean var22 = false;
      int var15 = this.getChildCount();

      for(var12 = 0; var12 < var15; ++var12) {
         View var20 = this.getChildAt(var12);
         if (var20.getVisibility() != 8) {
            DrawerLayout.LayoutParams var21 = (DrawerLayout.LayoutParams)var20.getLayoutParams();
            if (var23) {
               var13 = GravityCompat.getAbsoluteGravity(var21.gravity, var14);
               WindowInsets var19;
               WindowInsets var26;
               if (ViewCompat.getFitsSystemWindows(var20)) {
                  if (VERSION.SDK_INT >= 21) {
                     var19 = (WindowInsets)this.mLastInsets;
                     if (var13 == 3) {
                        var26 = var19.replaceSystemWindowInsets(var19.getSystemWindowInsetLeft(), var19.getSystemWindowInsetTop(), 0, var19.getSystemWindowInsetBottom());
                     } else {
                        var26 = var19;
                        if (var13 == 5) {
                           var26 = var19.replaceSystemWindowInsets(0, var19.getSystemWindowInsetTop(), var19.getSystemWindowInsetRight(), var19.getSystemWindowInsetBottom());
                        }
                     }

                     var20.dispatchApplyWindowInsets(var26);
                  }
               } else if (VERSION.SDK_INT >= 21) {
                  var19 = (WindowInsets)this.mLastInsets;
                  if (var13 == 3) {
                     var26 = var19.replaceSystemWindowInsets(var19.getSystemWindowInsetLeft(), var19.getSystemWindowInsetTop(), 0, var19.getSystemWindowInsetBottom());
                  } else {
                     var26 = var19;
                     if (var13 == 5) {
                        var26 = var19.replaceSystemWindowInsets(0, var19.getSystemWindowInsetTop(), var19.getSystemWindowInsetRight(), var19.getSystemWindowInsetBottom());
                     }
                  }

                  var21.leftMargin = var26.getSystemWindowInsetLeft();
                  var21.topMargin = var26.getSystemWindowInsetTop();
                  var21.rightMargin = var26.getSystemWindowInsetRight();
                  var21.bottomMargin = var26.getSystemWindowInsetBottom();
               }
            }

            if (this.isContentView(var20)) {
               var20.measure(MeasureSpec.makeMeasureSpec(var10 - var21.leftMargin - var21.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(var11 - var21.topMargin - var21.bottomMargin, 1073741824));
            } else {
               StringBuilder var27;
               if (!this.isDrawerView(var20)) {
                  var27 = new StringBuilder();
                  var27.append("Child ");
                  var27.append(var20);
                  var27.append(" at index ");
                  var27.append(var12);
                  var27.append(" does not have a valid layout_gravity - must be Gravity.LEFT, ");
                  var27.append("Gravity.RIGHT or Gravity.NO_GRAVITY");
                  throw new IllegalStateException(var27.toString());
               }

               if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
                  float var3 = ViewCompat.getElevation(var20);
                  float var4 = this.mDrawerElevation;
                  if (var3 != var4) {
                     ViewCompat.setElevation(var20, var4);
                  }
               }

               int var16 = this.getDrawerViewAbsoluteGravity(var20) & 7;
               boolean var25;
               if (var16 == 3) {
                  var25 = true;
               } else {
                  var25 = false;
               }

               if (var25 && var24 || !var25 && var22) {
                  var27 = new StringBuilder();
                  var27.append("Child drawer has absolute gravity ");
                  var27.append(gravityToString(var16));
                  var27.append(" but this ");
                  var27.append("DrawerLayout");
                  var27.append(" already has a ");
                  var27.append("drawer view along that edge");
                  throw new IllegalStateException(var27.toString());
               }

               if (var25) {
                  var24 = true;
               } else {
                  var22 = true;
               }

               var20.measure(getChildMeasureSpec(var1, this.mMinDrawerMargin + var21.leftMargin + var21.rightMargin, var21.width), getChildMeasureSpec(var2, var21.topMargin + var21.bottomMargin, var21.height));
            }
         }
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof DrawerLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         DrawerLayout.SavedState var3 = (DrawerLayout.SavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         if (var3.openDrawerGravity != 0) {
            View var2 = this.findDrawerWithGravity(var3.openDrawerGravity);
            if (var2 != null) {
               this.openDrawer(var2);
            }
         }

         if (var3.lockModeLeft != 3) {
            this.setDrawerLockMode(var3.lockModeLeft, 3);
         }

         if (var3.lockModeRight != 3) {
            this.setDrawerLockMode(var3.lockModeRight, 5);
         }

         if (var3.lockModeStart != 3) {
            this.setDrawerLockMode(var3.lockModeStart, 8388611);
         }

         if (var3.lockModeEnd != 3) {
            this.setDrawerLockMode(var3.lockModeEnd, 8388613);
         }

      }
   }

   public void onRtlPropertiesChanged(int var1) {
      this.resolveShadowDrawables();
   }

   protected Parcelable onSaveInstanceState() {
      DrawerLayout.SavedState var5 = new DrawerLayout.SavedState(super.onSaveInstanceState());
      int var4 = this.getChildCount();

      for(int var1 = 0; var1 < var4; ++var1) {
         DrawerLayout.LayoutParams var6 = (DrawerLayout.LayoutParams)this.getChildAt(var1).getLayoutParams();
         int var2 = var6.openState;
         boolean var3 = false;
         boolean var7;
         if (var2 == 1) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var6.openState == 2) {
            var3 = true;
         }

         if (var7 || var3) {
            var5.openDrawerGravity = var6.gravity;
            break;
         }
      }

      var5.lockModeLeft = this.mLockModeLeft;
      var5.lockModeRight = this.mLockModeRight;
      var5.lockModeStart = this.mLockModeStart;
      var5.lockModeEnd = this.mLockModeEnd;
      return var5;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.mLeftDragger.processTouchEvent(var1);
      this.mRightDragger.processTouchEvent(var1);
      int var4 = var1.getAction() & 255;
      float var2;
      float var3;
      if (var4 != 0) {
         boolean var6 = true;
         if (var4 != 1) {
            if (var4 != 3) {
               return true;
            } else {
               this.closeDrawers(true);
               this.mDisallowInterceptRequested = false;
               this.mChildrenCanceledTouch = false;
               return true;
            }
         } else {
            var3 = var1.getX();
            var2 = var1.getY();
            boolean var7 = true;
            View var8 = this.mLeftDragger.findTopChildUnder((int)var3, (int)var2);
            boolean var5 = var7;
            if (var8 != null) {
               var5 = var7;
               if (this.isContentView(var8)) {
                  var3 -= this.mInitialMotionX;
                  var2 -= this.mInitialMotionY;
                  var4 = this.mLeftDragger.getTouchSlop();
                  var5 = var7;
                  if (var3 * var3 + var2 * var2 < (float)(var4 * var4)) {
                     var8 = this.findOpenDrawer();
                     var5 = var7;
                     if (var8 != null) {
                        if (this.getDrawerLockMode(var8) == 2) {
                           var5 = var6;
                        } else {
                           var5 = false;
                        }
                     }
                  }
               }
            }

            this.closeDrawers(var5);
            this.mDisallowInterceptRequested = false;
            return true;
         }
      } else {
         var2 = var1.getX();
         var3 = var1.getY();
         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         return true;
      }
   }

   public void openDrawer(int var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if (var3 != null) {
         this.openDrawer(var3, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No drawer view found with gravity ");
         var4.append(gravityToString(var1));
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public void openDrawer(View var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(View var1, boolean var2) {
      if (this.isDrawerView(var1)) {
         DrawerLayout.LayoutParams var4 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if (this.mFirstLayout) {
            var4.onScreen = 1.0F;
            var4.openState = 1;
            this.updateChildrenImportantForAccessibility(var1, true);
         } else if (var2) {
            var4.openState |= 2;
            if (this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, 0, var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth() - var1.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 1.0F);
            this.updateDrawerState(var4.gravity, 0, var1);
            var1.setVisibility(0);
         }

         this.invalidate();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("View ");
         var3.append(var1);
         var3.append(" is not a sliding drawer");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void removeDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if (var1 != null) {
         List var2 = this.mListeners;
         if (var2 != null) {
            var2.remove(var1);
         }
      }
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      super.requestDisallowInterceptTouchEvent(var1);
      this.mDisallowInterceptRequested = var1;
      if (var1) {
         this.closeDrawers(true);
      }

   }

   public void requestLayout() {
      if (!this.mInLayout) {
         super.requestLayout();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setChildInsets(Object var1, boolean var2) {
      this.mLastInsets = var1;
      this.mDrawStatusBarBackground = var2;
      if (!var2 && this.getBackground() == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.requestLayout();
   }

   public void setDrawerElevation(float var1) {
      this.mDrawerElevation = var1;

      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         View var3 = this.getChildAt(var2);
         if (this.isDrawerView(var3)) {
            ViewCompat.setElevation(var3, this.mDrawerElevation);
         }
      }

   }

   @Deprecated
   public void setDrawerListener(DrawerLayout.DrawerListener var1) {
      DrawerLayout.DrawerListener var2 = this.mListener;
      if (var2 != null) {
         this.removeDrawerListener(var2);
      }

      if (var1 != null) {
         this.addDrawerListener(var1);
      }

      this.mListener = var1;
   }

   public void setDrawerLockMode(int var1) {
      this.setDrawerLockMode(var1, 3);
      this.setDrawerLockMode(var1, 5);
   }

   public void setDrawerLockMode(int var1, int var2) {
      int var3 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      if (var2 != 3) {
         if (var2 != 5) {
            if (var2 != 8388611) {
               if (var2 == 8388613) {
                  this.mLockModeEnd = var1;
               }
            } else {
               this.mLockModeStart = var1;
            }
         } else {
            this.mLockModeRight = var1;
         }
      } else {
         this.mLockModeLeft = var1;
      }

      if (var1 != 0) {
         ViewDragHelper var4;
         if (var3 == 3) {
            var4 = this.mLeftDragger;
         } else {
            var4 = this.mRightDragger;
         }

         var4.cancel();
      }

      View var5;
      if (var1 != 1) {
         if (var1 != 2) {
            return;
         }

         var5 = this.findDrawerWithGravity(var3);
         if (var5 != null) {
            this.openDrawer(var5);
            return;
         }
      } else {
         var5 = this.findDrawerWithGravity(var3);
         if (var5 != null) {
            this.closeDrawer(var5);
         }
      }

   }

   public void setDrawerLockMode(int var1, View var2) {
      if (this.isDrawerView(var2)) {
         this.setDrawerLockMode(var1, ((DrawerLayout.LayoutParams)var2.getLayoutParams()).gravity);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("View ");
         var3.append(var2);
         var3.append(" is not a ");
         var3.append("drawer with appropriate layout_gravity");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void setDrawerShadow(@DrawableRes int var1, int var2) {
      this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), var1), var2);
   }

   public void setDrawerShadow(Drawable var1, int var2) {
      if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         if ((var2 & 8388611) == 8388611) {
            this.mShadowStart = var1;
         } else if ((var2 & 8388613) == 8388613) {
            this.mShadowEnd = var1;
         } else if ((var2 & 3) == 3) {
            this.mShadowLeft = var1;
         } else {
            if ((var2 & 5) != 5) {
               return;
            }

            this.mShadowRight = var1;
         }

         this.resolveShadowDrawables();
         this.invalidate();
      }
   }

   public void setDrawerTitle(int var1, CharSequence var2) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      if (var1 == 3) {
         this.mTitleLeft = var2;
      } else {
         if (var1 == 5) {
            this.mTitleRight = var2;
         }

      }
   }

   void setDrawerViewOffset(View var1, float var2) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if (var2 != var3.onScreen) {
         var3.onScreen = var2;
         this.dispatchOnDrawerSlide(var1, var2);
      }
   }

   public void setScrimColor(@ColorInt int var1) {
      this.mScrimColor = var1;
      this.invalidate();
   }

   public void setStatusBarBackground(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.mStatusBarBackground = var2;
      this.invalidate();
   }

   public void setStatusBarBackground(Drawable var1) {
      this.mStatusBarBackground = var1;
      this.invalidate();
   }

   public void setStatusBarBackgroundColor(@ColorInt int var1) {
      this.mStatusBarBackground = new ColorDrawable(var1);
      this.invalidate();
   }

   void updateDrawerState(int var1, int var2, View var3) {
      var1 = this.mLeftDragger.getViewDragState();
      int var4 = this.mRightDragger.getViewDragState();
      byte var6;
      if (var1 != 1 && var4 != 1) {
         if (var1 != 2 && var4 != 2) {
            var6 = 0;
         } else {
            var6 = 2;
         }
      } else {
         var6 = 1;
      }

      if (var3 != null && var2 == 0) {
         DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var3.getLayoutParams();
         if (var5.onScreen == 0.0F) {
            this.dispatchOnDrawerClosed(var3);
         } else if (var5.onScreen == 1.0F) {
            this.dispatchOnDrawerOpened(var3);
         }
      }

      if (var6 != this.mDrawerState) {
         this.mDrawerState = var6;
         List var7 = this.mListeners;
         if (var7 != null) {
            for(var2 = var7.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerStateChanged(var6);
            }
         }
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

      private void addChildrenForAccessibility(AccessibilityNodeInfoCompat var1, ViewGroup var2) {
         int var4 = var2.getChildCount();

         for(int var3 = 0; var3 < var4; ++var3) {
            View var5 = var2.getChildAt(var3);
            if (DrawerLayout.includeChildForAccessibility(var5)) {
               var1.addChild(var5);
            }
         }

      }

      private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat var1, AccessibilityNodeInfoCompat var2) {
         Rect var3 = this.mTmpRect;
         var2.getBoundsInParent(var3);
         var1.setBoundsInParent(var3);
         var2.getBoundsInScreen(var3);
         var1.setBoundsInScreen(var3);
         var1.setVisibleToUser(var2.isVisibleToUser());
         var1.setPackageName(var2.getPackageName());
         var1.setClassName(var2.getClassName());
         var1.setContentDescription(var2.getContentDescription());
         var1.setEnabled(var2.isEnabled());
         var1.setClickable(var2.isClickable());
         var1.setFocusable(var2.isFocusable());
         var1.setFocused(var2.isFocused());
         var1.setAccessibilityFocused(var2.isAccessibilityFocused());
         var1.setSelected(var2.isSelected());
         var1.setLongClickable(var2.isLongClickable());
         var1.addAction(var2.getActions());
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         if (var2.getEventType() == 32) {
            List var4 = var2.getText();
            View var5 = DrawerLayout.this.findVisibleDrawer();
            if (var5 != null) {
               int var3 = DrawerLayout.this.getDrawerViewAbsoluteGravity(var5);
               CharSequence var6 = DrawerLayout.this.getDrawerTitle(var3);
               if (var6 != null) {
                  var4.add(var6);
               }
            }

            return true;
         } else {
            return super.dispatchPopulateAccessibilityEvent(var1, var2);
         }
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(DrawerLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
         } else {
            AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain(var2);
            super.onInitializeAccessibilityNodeInfo(var1, var3);
            var2.setSource(var1);
            ViewParent var4 = ViewCompat.getParentForAccessibility(var1);
            if (var4 instanceof View) {
               var2.setParent((View)var4);
            }

            this.copyNodeInfoNoChildren(var2, var3);
            var3.recycle();
            this.addChildrenForAccessibility(var2, (ViewGroup)var1);
         }

         var2.setClassName(DrawerLayout.class.getName());
         var2.setFocusable(false);
         var2.setFocused(false);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return !DrawerLayout.CAN_HIDE_DESCENDANTS && !DrawerLayout.includeChildForAccessibility(var2) ? false : super.onRequestSendAccessibilityEvent(var1, var2, var3);
      }
   }

   static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         if (!DrawerLayout.includeChildForAccessibility(var1)) {
            var2.setParent((View)null);
         }

      }
   }

   public interface DrawerListener {
      void onDrawerClosed(View var1);

      void onDrawerOpened(View var1);

      void onDrawerSlide(View var1, float var2);

      void onDrawerStateChanged(int var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface EdgeGravity {
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int FLAG_IS_CLOSING = 4;
      private static final int FLAG_IS_OPENED = 1;
      private static final int FLAG_IS_OPENING = 2;
      public int gravity;
      boolean isPeeking;
      float onScreen;
      int openState;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.gravity = 0;
      }

      public LayoutParams(int var1, int var2, int var3) {
         this(var1, var2);
         this.gravity = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         this.gravity = 0;
         TypedArray var3 = var1.obtainStyledAttributes(var2, DrawerLayout.LAYOUT_ATTRS);
         this.gravity = var3.getInt(0, 0);
         var3.recycle();
      }

      public LayoutParams(DrawerLayout.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface LockMode {
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public DrawerLayout.SavedState createFromParcel(Parcel var1) {
            return new DrawerLayout.SavedState(var1, (ClassLoader)null);
         }

         public DrawerLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new DrawerLayout.SavedState(var1, var2);
         }

         public DrawerLayout.SavedState[] newArray(int var1) {
            return new DrawerLayout.SavedState[var1];
         }
      };
      int lockModeEnd;
      int lockModeLeft;
      int lockModeRight;
      int lockModeStart;
      int openDrawerGravity = 0;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.openDrawerGravity = var1.readInt();
         this.lockModeLeft = var1.readInt();
         this.lockModeRight = var1.readInt();
         this.lockModeStart = var1.readInt();
         this.lockModeEnd = var1.readInt();
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.openDrawerGravity);
         var1.writeInt(this.lockModeLeft);
         var1.writeInt(this.lockModeRight);
         var1.writeInt(this.lockModeStart);
         var1.writeInt(this.lockModeEnd);
      }
   }

   public abstract static class SimpleDrawerListener implements DrawerLayout.DrawerListener {
      public void onDrawerClosed(View var1) {
      }

      public void onDrawerOpened(View var1) {
      }

      public void onDrawerSlide(View var1, float var2) {
      }

      public void onDrawerStateChanged(int var1) {
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface State {
   }

   private class ViewDragCallback extends ViewDragHelper.Callback {
      private final int mAbsGravity;
      private ViewDragHelper mDragger;
      private final Runnable mPeekRunnable = new Runnable() {
         public void run() {
            ViewDragCallback.this.peekDrawer();
         }
      };

      ViewDragCallback(int var2) {
         this.mAbsGravity = var2;
      }

      private void closeOtherDrawer() {
         int var2 = this.mAbsGravity;
         byte var1 = 3;
         if (var2 == 3) {
            var1 = 5;
         }

         View var3 = DrawerLayout.this.findDrawerWithGravity(var1);
         if (var3 != null) {
            DrawerLayout.this.closeDrawer(var3);
         }

      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            return Math.max(-var1.getWidth(), Math.min(var2, 0));
         } else {
            var3 = DrawerLayout.this.getWidth();
            return Math.max(var3 - var1.getWidth(), Math.min(var2, var3));
         }
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         return DrawerLayout.this.isDrawerView(var1) ? var1.getWidth() : 0;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         View var3;
         if ((var1 & 1) == 1) {
            var3 = DrawerLayout.this.findDrawerWithGravity(3);
         } else {
            var3 = DrawerLayout.this.findDrawerWithGravity(5);
         }

         if (var3 != null && DrawerLayout.this.getDrawerLockMode(var3) == 0) {
            this.mDragger.captureChildView(var3, var2);
         }

      }

      public boolean onEdgeLock(int var1) {
         return false;
      }

      public void onEdgeTouched(int var1, int var2) {
         DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
      }

      public void onViewCaptured(View var1, int var2) {
         ((DrawerLayout.LayoutParams)var1.getLayoutParams()).isPeeking = false;
         this.closeOtherDrawer();
      }

      public void onViewDragStateChanged(int var1) {
         DrawerLayout.this.updateDrawerState(this.mAbsGravity, var1, this.mDragger.getCapturedView());
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         var3 = var1.getWidth();
         float var6;
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            var6 = (float)(var3 + var2) / (float)var3;
         } else {
            var6 = (float)(DrawerLayout.this.getWidth() - var2) / (float)var3;
         }

         DrawerLayout.this.setDrawerViewOffset(var1, var6);
         byte var7;
         if (var6 == 0.0F) {
            var7 = 4;
         } else {
            var7 = 0;
         }

         var1.setVisibility(var7);
         DrawerLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         var3 = DrawerLayout.this.getDrawerViewOffset(var1);
         int var5 = var1.getWidth();
         int var4;
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            if (var2 > 0.0F || var2 == 0.0F && var3 > 0.5F) {
               var4 = 0;
            } else {
               var4 = -var5;
            }
         } else {
            var4 = DrawerLayout.this.getWidth();
            if (var2 < 0.0F || var2 == 0.0F && var3 > 0.5F) {
               var4 -= var5;
            }
         }

         this.mDragger.settleCapturedViewAt(var4, var1.getTop());
         DrawerLayout.this.invalidate();
      }

      void peekDrawer() {
         int var3 = this.mDragger.getEdgeSize();
         int var1 = this.mAbsGravity;
         int var2 = 0;
         boolean var6;
         if (var1 == 3) {
            var6 = true;
         } else {
            var6 = false;
         }

         View var4;
         if (var6) {
            var4 = DrawerLayout.this.findDrawerWithGravity(3);
            if (var4 != null) {
               var2 = -var4.getWidth();
            }

            var2 += var3;
         } else {
            var4 = DrawerLayout.this.findDrawerWithGravity(5);
            var2 = DrawerLayout.this.getWidth() - var3;
         }

         if (var4 != null && (var6 && var4.getLeft() < var2 || !var6 && var4.getLeft() > var2) && DrawerLayout.this.getDrawerLockMode(var4) == 0) {
            DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var4.getLayoutParams();
            this.mDragger.smoothSlideViewTo(var4, var2, var4.getTop());
            var5.isPeeking = true;
            DrawerLayout.this.invalidate();
            this.closeOtherDrawer();
            DrawerLayout.this.cancelChildViewTouch();
         }

      }

      public void removeCallbacks() {
         DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
      }

      public void setDragger(ViewDragHelper var1) {
         this.mDragger = var1;
      }

      public boolean tryCaptureView(View var1, int var2) {
         return DrawerLayout.this.isDrawerView(var1) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(var1) == 0;
      }
   }
}
