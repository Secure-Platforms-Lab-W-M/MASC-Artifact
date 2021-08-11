package androidx.slidingpanelayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
   private static final int DEFAULT_FADE_COLOR = -858993460;
   private static final int DEFAULT_OVERHANG_SIZE = 32;
   private static final int MIN_FLING_VELOCITY = 400;
   private static final String TAG = "SlidingPaneLayout";
   private boolean mCanSlide;
   private int mCoveredFadeColor;
   private boolean mDisplayListReflectionLoaded;
   final ViewDragHelper mDragHelper;
   private boolean mFirstLayout;
   private Method mGetDisplayList;
   private float mInitialMotionX;
   private float mInitialMotionY;
   boolean mIsUnableToDrag;
   private final int mOverhangSize;
   private SlidingPaneLayout.PanelSlideListener mPanelSlideListener;
   private int mParallaxBy;
   private float mParallaxOffset;
   final ArrayList mPostedRunnables;
   boolean mPreservedOpenState;
   private Field mRecreateDisplayList;
   private Drawable mShadowDrawableLeft;
   private Drawable mShadowDrawableRight;
   float mSlideOffset;
   int mSlideRange;
   View mSlideableView;
   private int mSliderFadeColor;
   private final Rect mTmpRect;

   public SlidingPaneLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSliderFadeColor = -858993460;
      this.mFirstLayout = true;
      this.mTmpRect = new Rect();
      this.mPostedRunnables = new ArrayList();
      float var4 = var1.getResources().getDisplayMetrics().density;
      this.mOverhangSize = (int)(32.0F * var4 + 0.5F);
      this.setWillNotDraw(false);
      ViewCompat.setAccessibilityDelegate(this, new SlidingPaneLayout.AccessibilityDelegate());
      ViewCompat.setImportantForAccessibility(this, 1);
      ViewDragHelper var5 = ViewDragHelper.create(this, 0.5F, new SlidingPaneLayout.DragHelperCallback());
      this.mDragHelper = var5;
      var5.setMinVelocity(400.0F * var4);
   }

   private boolean closePane(View var1, int var2) {
      if (!this.mFirstLayout && !this.smoothSlideTo(0.0F, var2)) {
         return false;
      } else {
         this.mPreservedOpenState = false;
         return true;
      }
   }

   private void dimChildView(View var1, float var2, int var3) {
      SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
      if (var2 > 0.0F && var3 != 0) {
         int var4 = (int)((float)((-16777216 & var3) >>> 24) * var2);
         if (var5.dimPaint == null) {
            var5.dimPaint = new Paint();
         }

         var5.dimPaint.setColorFilter(new PorterDuffColorFilter(var4 << 24 | 16777215 & var3, Mode.SRC_OVER));
         if (var1.getLayerType() != 2) {
            var1.setLayerType(2, var5.dimPaint);
         }

         this.invalidateChildRegion(var1);
      } else if (var1.getLayerType() != 0) {
         if (var5.dimPaint != null) {
            var5.dimPaint.setColorFilter((ColorFilter)null);
         }

         SlidingPaneLayout.DisableLayerRunnable var6 = new SlidingPaneLayout.DisableLayerRunnable(var1);
         this.mPostedRunnables.add(var6);
         ViewCompat.postOnAnimation(this, var6);
         return;
      }

   }

   private boolean openPane(View var1, int var2) {
      if (!this.mFirstLayout && !this.smoothSlideTo(1.0F, var2)) {
         return false;
      } else {
         this.mPreservedOpenState = true;
         return true;
      }
   }

   private void parallaxOtherViews(float var1) {
      boolean var8;
      boolean var10;
      label40: {
         var8 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var9 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         if (var9.dimWhenOffset) {
            int var3;
            if (var8) {
               var3 = var9.rightMargin;
            } else {
               var3 = var9.leftMargin;
            }

            if (var3 <= 0) {
               var10 = true;
               break label40;
            }
         }

         var10 = false;
      }

      int var6 = this.getChildCount();

      for(int var4 = 0; var4 < var6; ++var4) {
         View var11 = this.getChildAt(var4);
         if (var11 != this.mSlideableView) {
            float var2 = this.mParallaxOffset;
            int var5 = this.mParallaxBy;
            int var7 = (int)((1.0F - var2) * (float)var5);
            this.mParallaxOffset = var1;
            var5 = var7 - (int)((1.0F - var1) * (float)var5);
            if (var8) {
               var5 = -var5;
            }

            var11.offsetLeftAndRight(var5);
            if (var10) {
               var2 = this.mParallaxOffset;
               if (var8) {
                  --var2;
               } else {
                  var2 = 1.0F - var2;
               }

               this.dimChildView(var11, var2, this.mCoveredFadeColor);
            }
         }
      }

   }

   private static boolean viewIsOpaque(View var0) {
      if (var0.isOpaque()) {
         return true;
      } else if (VERSION.SDK_INT >= 18) {
         return false;
      } else {
         Drawable var1 = var0.getBackground();
         if (var1 != null) {
            return var1.getOpacity() == -1;
         } else {
            return false;
         }
      }
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var9 = (ViewGroup)var1;
         int var7 = var1.getScrollX();
         int var8 = var1.getScrollY();

         for(int var6 = var9.getChildCount() - 1; var6 >= 0; --var6) {
            View var10 = var9.getChildAt(var6);
            if (var4 + var7 >= var10.getLeft() && var4 + var7 < var10.getRight() && var5 + var8 >= var10.getTop() && var5 + var8 < var10.getBottom() && this.canScroll(var10, true, var3, var4 + var7 - var10.getLeft(), var5 + var8 - var10.getTop())) {
               return true;
            }
         }
      }

      if (var2) {
         if (!this.isLayoutRtlSupport()) {
            var3 = -var3;
         }

         if (var1.canScrollHorizontally(var3)) {
            return true;
         }
      }

      return false;
   }

   @Deprecated
   public boolean canSlide() {
      return this.mCanSlide;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof SlidingPaneLayout.LayoutParams && super.checkLayoutParams(var1);
   }

   public boolean closePane() {
      return this.closePane(this.mSlideableView, 0);
   }

   public void computeScroll() {
      if (this.mDragHelper.continueSettling(true)) {
         if (!this.mCanSlide) {
            this.mDragHelper.abort();
            return;
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void dispatchOnPanelClosed(View var1) {
      SlidingPaneLayout.PanelSlideListener var2 = this.mPanelSlideListener;
      if (var2 != null) {
         var2.onPanelClosed(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelOpened(View var1) {
      SlidingPaneLayout.PanelSlideListener var2 = this.mPanelSlideListener;
      if (var2 != null) {
         var2.onPanelOpened(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelSlide(View var1) {
      SlidingPaneLayout.PanelSlideListener var2 = this.mPanelSlideListener;
      if (var2 != null) {
         var2.onPanelSlide(var1, this.mSlideOffset);
      }

   }

   public void draw(Canvas var1) {
      super.draw(var1);
      Drawable var7;
      if (this.isLayoutRtlSupport()) {
         var7 = this.mShadowDrawableRight;
      } else {
         var7 = this.mShadowDrawableLeft;
      }

      View var8;
      if (this.getChildCount() > 1) {
         var8 = this.getChildAt(1);
      } else {
         var8 = null;
      }

      if (var8 != null) {
         if (var7 != null) {
            int var4 = var8.getTop();
            int var5 = var8.getBottom();
            int var6 = var7.getIntrinsicWidth();
            int var2;
            int var3;
            if (this.isLayoutRtlSupport()) {
               var2 = var8.getRight();
               var3 = var2 + var6;
            } else {
               var3 = var8.getLeft();
               var2 = var3 - var6;
            }

            var7.setBounds(var2, var4, var3, var5);
            var7.draw(var1);
         }
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      SlidingPaneLayout.LayoutParams var7 = (SlidingPaneLayout.LayoutParams)var2.getLayoutParams();
      int var5 = var1.save();
      if (this.mCanSlide && !var7.slideable && this.mSlideableView != null) {
         var1.getClipBounds(this.mTmpRect);
         Rect var8;
         if (this.isLayoutRtlSupport()) {
            var8 = this.mTmpRect;
            var8.left = Math.max(var8.left, this.mSlideableView.getRight());
         } else {
            var8 = this.mTmpRect;
            var8.right = Math.min(var8.right, this.mSlideableView.getLeft());
         }

         var1.clipRect(this.mTmpRect);
      }

      boolean var6 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var5);
      return var6;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new SlidingPaneLayout.LayoutParams();
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new SlidingPaneLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new SlidingPaneLayout.LayoutParams((MarginLayoutParams)var1) : new SlidingPaneLayout.LayoutParams(var1);
   }

   public int getCoveredFadeColor() {
      return this.mCoveredFadeColor;
   }

   public int getParallaxDistance() {
      return this.mParallaxBy;
   }

   public int getSliderFadeColor() {
      return this.mSliderFadeColor;
   }

   void invalidateChildRegion(View var1) {
      if (VERSION.SDK_INT >= 17) {
         ViewCompat.setLayerPaint(var1, ((SlidingPaneLayout.LayoutParams)var1.getLayoutParams()).dimPaint);
      } else {
         label42: {
            if (VERSION.SDK_INT >= 16) {
               Field var2;
               if (!this.mDisplayListReflectionLoaded) {
                  try {
                     this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
                  } catch (NoSuchMethodException var5) {
                     Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", var5);
                  }

                  try {
                     var2 = View.class.getDeclaredField("mRecreateDisplayList");
                     this.mRecreateDisplayList = var2;
                     var2.setAccessible(true);
                  } catch (NoSuchFieldException var4) {
                     Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", var4);
                  }

                  this.mDisplayListReflectionLoaded = true;
               }

               if (this.mGetDisplayList == null) {
                  break label42;
               }

               var2 = this.mRecreateDisplayList;
               if (var2 == null) {
                  break label42;
               }

               try {
                  var2.setBoolean(var1, true);
                  this.mGetDisplayList.invoke(var1, (Object[])null);
               } catch (Exception var3) {
                  Log.e("SlidingPaneLayout", "Error refreshing display list state", var3);
               }
            }

            ViewCompat.postInvalidateOnAnimation(this, var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
            return;
         }

         var1.invalidate();
      }
   }

   boolean isDimmed(View var1) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         SlidingPaneLayout.LayoutParams var4 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
         boolean var2 = var3;
         if (this.mCanSlide) {
            var2 = var3;
            if (var4.dimWhenOffset) {
               var2 = var3;
               if (this.mSlideOffset > 0.0F) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }

   boolean isLayoutRtlSupport() {
      return ViewCompat.getLayoutDirection(this) == 1;
   }

   public boolean isOpen() {
      return !this.mCanSlide || this.mSlideOffset == 1.0F;
   }

   public boolean isSlideable() {
      return this.mCanSlide;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
      int var1 = 0;

      for(int var2 = this.mPostedRunnables.size(); var1 < var2; ++var1) {
         ((SlidingPaneLayout.DisableLayerRunnable)this.mPostedRunnables.get(var1)).run();
      }

      this.mPostedRunnables.clear();
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var4 = var1.getActionMasked();
      boolean var7 = this.mCanSlide;
      boolean var6 = true;
      if (!var7 && var4 == 0 && this.getChildCount() > 1) {
         View var8 = this.getChildAt(1);
         if (var8 != null) {
            this.mPreservedOpenState = this.mDragHelper.isViewUnder(var8, (int)var1.getX(), (int)var1.getY()) ^ true;
         }
      }

      if (this.mCanSlide && (!this.mIsUnableToDrag || var4 == 0)) {
         if (var4 != 3 && var4 != 1) {
            boolean var5 = false;
            float var2;
            float var3;
            boolean var9;
            if (var4 != 0) {
               if (var4 != 2) {
                  var9 = var5;
               } else {
                  var3 = var1.getX();
                  var2 = var1.getY();
                  var3 = Math.abs(var3 - this.mInitialMotionX);
                  var2 = Math.abs(var2 - this.mInitialMotionY);
                  var9 = var5;
                  if (var3 > (float)this.mDragHelper.getTouchSlop()) {
                     var9 = var5;
                     if (var2 > var3) {
                        this.mDragHelper.cancel();
                        this.mIsUnableToDrag = true;
                        return false;
                     }
                  }
               }
            } else {
               this.mIsUnableToDrag = false;
               var2 = var1.getX();
               var3 = var1.getY();
               this.mInitialMotionX = var2;
               this.mInitialMotionY = var3;
               var9 = var5;
               if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)var2, (int)var3)) {
                  var9 = var5;
                  if (this.isDimmed(this.mSlideableView)) {
                     var9 = true;
                  }
               }
            }

            if (!this.mDragHelper.shouldInterceptTouchEvent(var1)) {
               if (var9) {
                  return true;
               }

               var6 = false;
            }

            return var6;
         } else {
            this.mDragHelper.cancel();
            return false;
         }
      } else {
         this.mDragHelper.cancel();
         return super.onInterceptTouchEvent(var1);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      boolean var15 = this.isLayoutRtlSupport();
      if (var15) {
         this.mDragHelper.setEdgeTrackingEnabled(2);
      } else {
         this.mDragHelper.setEdgeTrackingEnabled(1);
      }

      int var10 = var4 - var2;
      if (var15) {
         var3 = this.getPaddingRight();
      } else {
         var3 = this.getPaddingLeft();
      }

      if (var15) {
         var5 = this.getPaddingLeft();
      } else {
         var5 = this.getPaddingRight();
      }

      int var12 = this.getPaddingTop();
      int var11 = this.getChildCount();
      var2 = var3;
      if (this.mFirstLayout) {
         float var6;
         if (this.mCanSlide && this.mPreservedOpenState) {
            var6 = 1.0F;
         } else {
            var6 = 0.0F;
         }

         this.mSlideOffset = var6;
      }

      int var7 = 0;

      for(int var8 = var3; var7 < var11; var8 = var3) {
         View var16 = this.getChildAt(var7);
         if (var16.getVisibility() == 8) {
            var3 = var8;
         } else {
            SlidingPaneLayout.LayoutParams var17 = (SlidingPaneLayout.LayoutParams)var16.getLayoutParams();
            int var13 = var16.getMeasuredWidth();
            byte var9 = 0;
            if (var17.slideable) {
               var3 = var17.leftMargin;
               int var14 = var17.rightMargin;
               var14 = Math.min(var2, var10 - var5 - this.mOverhangSize) - var8 - (var3 + var14);
               this.mSlideRange = var14;
               if (var15) {
                  var3 = var17.rightMargin;
               } else {
                  var3 = var17.leftMargin;
               }

               if (var8 + var3 + var14 + var13 / 2 > var10 - var5) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var17.dimWhenOffset = var1;
               var14 = (int)((float)var14 * this.mSlideOffset);
               var3 = var8 + var14 + var3;
               this.mSlideOffset = (float)var14 / (float)this.mSlideRange;
               var8 = var9;
            } else {
               label82: {
                  if (this.mCanSlide) {
                     var3 = this.mParallaxBy;
                     if (var3 != 0) {
                        var8 = (int)((1.0F - this.mSlideOffset) * (float)var3);
                        var3 = var2;
                        break label82;
                     }
                  }

                  var3 = var2;
                  var8 = var9;
               }
            }

            int var18;
            if (var15) {
               var8 += var10 - var3;
               var18 = var8 - var13;
            } else {
               var18 = var3 - var8;
               var8 = var18 + var13;
            }

            var16.layout(var18, var12, var8, var16.getMeasuredHeight() + var12);
            var2 += var16.getWidth();
         }

         ++var7;
      }

      if (this.mFirstLayout) {
         if (this.mCanSlide) {
            if (this.mParallaxBy != 0) {
               this.parallaxOtherViews(this.mSlideOffset);
            }

            if (((SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
               this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
            }
         } else {
            for(var2 = 0; var2 < var11; ++var2) {
               this.dimChildView(this.getChildAt(var2), 0.0F, this.mSliderFadeColor);
            }
         }

         this.updateObscuredViewsVisibility(this.mSlideableView);
      }

      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var5 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var9 = MeasureSpec.getMode(var2);
      int var10 = MeasureSpec.getSize(var2);
      int var6;
      int var7;
      int var8;
      if (var5 != 1073741824) {
         if (!this.isInEditMode()) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
         }

         if (var5 == Integer.MIN_VALUE) {
            var2 = 1073741824;
            var8 = var1;
            var6 = var9;
            var7 = var10;
         } else {
            var8 = var1;
            var6 = var9;
            var7 = var10;
            if (var5 == 0) {
               var2 = 1073741824;
               var8 = 300;
               var6 = var9;
               var7 = var10;
            }
         }
      } else {
         var8 = var1;
         var6 = var9;
         var7 = var10;
         if (var9 == 0) {
            if (!this.isInEditMode()) {
               throw new IllegalStateException("Height must not be UNSPECIFIED");
            }

            var8 = var1;
            var6 = var9;
            var7 = var10;
            if (var9 == 0) {
               var6 = Integer.MIN_VALUE;
               var7 = 300;
               var8 = var1;
            }
         }
      }

      var5 = 0;
      var1 = 0;
      if (var6 != Integer.MIN_VALUE) {
         if (var6 == 1073741824) {
            var5 = var7 - this.getPaddingTop() - this.getPaddingBottom();
            var1 = var5;
         }
      } else {
         var1 = var7 - this.getPaddingTop() - this.getPaddingBottom();
      }

      float var4 = 0.0F;
      boolean var16 = false;
      int var13 = var8 - this.getPaddingLeft() - this.getPaddingRight();
      var9 = var13;
      int var14 = this.getChildCount();
      if (var14 > 2) {
         Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
      }

      this.mSlideableView = null;

      int var15;
      View var18;
      SlidingPaneLayout.LayoutParams var19;
      for(var10 = 0; var10 < var14; var5 = var2) {
         var18 = this.getChildAt(var10);
         var19 = (SlidingPaneLayout.LayoutParams)var18.getLayoutParams();
         if (var18.getVisibility() == 8) {
            var19.dimWhenOffset = false;
            var2 = var5;
         } else {
            label194: {
               float var3 = var4;
               if (var19.weight > 0.0F) {
                  var4 += var19.weight;
                  var3 = var4;
                  if (var19.width == 0) {
                     var2 = var5;
                     break label194;
                  }
               }

               var2 = var19.leftMargin + var19.rightMargin;
               if (var19.width == -2) {
                  var2 = MeasureSpec.makeMeasureSpec(var13 - var2, Integer.MIN_VALUE);
               } else if (var19.width == -1) {
                  var2 = MeasureSpec.makeMeasureSpec(var13 - var2, 1073741824);
               } else {
                  var2 = MeasureSpec.makeMeasureSpec(var19.width, 1073741824);
               }

               if (var19.height == -2) {
                  var7 = MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE);
               } else if (var19.height == -1) {
                  var7 = MeasureSpec.makeMeasureSpec(var1, 1073741824);
               } else {
                  var7 = MeasureSpec.makeMeasureSpec(var19.height, 1073741824);
               }

               var18.measure(var2, var7);
               var7 = var18.getMeasuredWidth();
               var15 = var18.getMeasuredHeight();
               var2 = var5;
               if (var6 == Integer.MIN_VALUE) {
                  var2 = var5;
                  if (var15 > var5) {
                     var2 = Math.min(var15, var1);
                  }
               }

               var9 -= var7;
               boolean var17;
               if (var9 < 0) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               var19.slideable = var17;
               if (var19.slideable) {
                  this.mSlideableView = var18;
               }

               var16 |= var17;
               var4 = var3;
            }
         }

         ++var10;
      }

      if (var16 || var4 > 0.0F) {
         int var12 = var13 - this.mOverhangSize;
         int var11 = 0;
         var7 = var14;
         var2 = var1;

         for(var6 = var12; var11 < var7; ++var11) {
            var18 = this.getChildAt(var11);
            if (var18.getVisibility() != 8) {
               var19 = (SlidingPaneLayout.LayoutParams)var18.getLayoutParams();
               if (var18.getVisibility() != 8) {
                  boolean var20;
                  if (var19.width == 0 && var19.weight > 0.0F) {
                     var20 = true;
                  } else {
                     var20 = false;
                  }

                  if (var20) {
                     var12 = 0;
                  } else {
                     var12 = var18.getMeasuredWidth();
                  }

                  if (var16 && var18 != this.mSlideableView) {
                     if (var19.width < 0 && (var12 > var6 || var19.weight > 0.0F)) {
                        if (var20) {
                           if (var19.height == -2) {
                              var1 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
                           } else if (var19.height == -1) {
                              var1 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
                           } else {
                              var1 = MeasureSpec.makeMeasureSpec(var19.height, 1073741824);
                           }
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var18.getMeasuredHeight(), 1073741824);
                        }

                        var18.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), var1);
                     }
                  } else if (var19.weight > 0.0F) {
                     if (var19.width == 0) {
                        if (var19.height == -2) {
                           var1 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
                        } else if (var19.height == -1) {
                           var1 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var19.height, 1073741824);
                        }
                     } else {
                        var1 = MeasureSpec.makeMeasureSpec(var18.getMeasuredHeight(), 1073741824);
                     }

                     if (var16) {
                        var14 = var13 - (var19.leftMargin + var19.rightMargin);
                        var15 = MeasureSpec.makeMeasureSpec(var14, 1073741824);
                        if (var12 != var14) {
                           var18.measure(var15, var1);
                        }
                     } else {
                        var14 = Math.max(0, var9);
                        var18.measure(MeasureSpec.makeMeasureSpec(var12 + (int)(var19.weight * (float)var14 / var4), 1073741824), var1);
                     }
                  }
               }
            }
         }
      }

      this.setMeasuredDimension(var8, this.getPaddingTop() + var5 + this.getPaddingBottom());
      this.mCanSlide = var16;
      if (this.mDragHelper.getViewDragState() != 0 && !var16) {
         this.mDragHelper.abort();
      }

   }

   void onPanelDragged(int var1) {
      if (this.mSlideableView == null) {
         this.mSlideOffset = 0.0F;
      } else {
         boolean var5 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var6 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         int var3 = this.mSlideableView.getWidth();
         if (var5) {
            var1 = this.getWidth() - var1 - var3;
         }

         if (var5) {
            var3 = this.getPaddingRight();
         } else {
            var3 = this.getPaddingLeft();
         }

         int var4;
         if (var5) {
            var4 = var6.rightMargin;
         } else {
            var4 = var6.leftMargin;
         }

         float var2 = (float)(var1 - (var3 + var4)) / (float)this.mSlideRange;
         this.mSlideOffset = var2;
         if (this.mParallaxBy != 0) {
            this.parallaxOtherViews(var2);
         }

         if (var6.dimWhenOffset) {
            this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
         }

         this.dispatchOnPanelSlide(this.mSlideableView);
      }
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof SlidingPaneLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         SlidingPaneLayout.SavedState var2 = (SlidingPaneLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if (var2.isOpen) {
            this.openPane();
         } else {
            this.closePane();
         }

         this.mPreservedOpenState = var2.isOpen;
      }
   }

   protected Parcelable onSaveInstanceState() {
      SlidingPaneLayout.SavedState var2 = new SlidingPaneLayout.SavedState(super.onSaveInstanceState());
      boolean var1;
      if (this.isSlideable()) {
         var1 = this.isOpen();
      } else {
         var1 = this.mPreservedOpenState;
      }

      var2.isOpen = var1;
      return var2;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         this.mFirstLayout = true;
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.mCanSlide) {
         return super.onTouchEvent(var1);
      } else {
         this.mDragHelper.processTouchEvent(var1);
         int var6 = var1.getActionMasked();
         float var2;
         float var3;
         if (var6 != 0) {
            if (var6 != 1) {
               return true;
            }

            if (this.isDimmed(this.mSlideableView)) {
               var2 = var1.getX();
               var3 = var1.getY();
               float var4 = var2 - this.mInitialMotionX;
               float var5 = var3 - this.mInitialMotionY;
               var6 = this.mDragHelper.getTouchSlop();
               if (var4 * var4 + var5 * var5 < (float)(var6 * var6) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)var2, (int)var3)) {
                  this.closePane(this.mSlideableView, 0);
                  return true;
               }
            }
         } else {
            var2 = var1.getX();
            var3 = var1.getY();
            this.mInitialMotionX = var2;
            this.mInitialMotionY = var3;
         }

         return true;
      }
   }

   public boolean openPane() {
      return this.openPane(this.mSlideableView, 0);
   }

   public void requestChildFocus(View var1, View var2) {
      super.requestChildFocus(var1, var2);
      if (!this.isInTouchMode() && !this.mCanSlide) {
         boolean var3;
         if (var1 == this.mSlideableView) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mPreservedOpenState = var3;
      }

   }

   void setAllChildrenVisible() {
      int var1 = 0;

      for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
         View var3 = this.getChildAt(var1);
         if (var3.getVisibility() == 4) {
            var3.setVisibility(0);
         }
      }

   }

   public void setCoveredFadeColor(int var1) {
      this.mCoveredFadeColor = var1;
   }

   public void setPanelSlideListener(SlidingPaneLayout.PanelSlideListener var1) {
      this.mPanelSlideListener = var1;
   }

   public void setParallaxDistance(int var1) {
      this.mParallaxBy = var1;
      this.requestLayout();
   }

   @Deprecated
   public void setShadowDrawable(Drawable var1) {
      this.setShadowDrawableLeft(var1);
   }

   public void setShadowDrawableLeft(Drawable var1) {
      this.mShadowDrawableLeft = var1;
   }

   public void setShadowDrawableRight(Drawable var1) {
      this.mShadowDrawableRight = var1;
   }

   @Deprecated
   public void setShadowResource(int var1) {
      this.setShadowDrawable(this.getResources().getDrawable(var1));
   }

   public void setShadowResourceLeft(int var1) {
      this.setShadowDrawableLeft(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setShadowResourceRight(int var1) {
      this.setShadowDrawableRight(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setSliderFadeColor(int var1) {
      this.mSliderFadeColor = var1;
   }

   @Deprecated
   public void smoothSlideClosed() {
      this.closePane();
   }

   @Deprecated
   public void smoothSlideOpen() {
      this.openPane();
   }

   boolean smoothSlideTo(float var1, int var2) {
      if (!this.mCanSlide) {
         return false;
      } else {
         boolean var5 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var6 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         if (var5) {
            var2 = this.getPaddingRight();
            int var3 = var6.rightMargin;
            int var4 = this.mSlideableView.getWidth();
            var2 = (int)((float)this.getWidth() - ((float)(var2 + var3) + (float)this.mSlideRange * var1 + (float)var4));
         } else {
            var2 = (int)((float)(this.getPaddingLeft() + var6.leftMargin) + (float)this.mSlideRange * var1);
         }

         ViewDragHelper var8 = this.mDragHelper;
         View var7 = this.mSlideableView;
         if (var8.smoothSlideViewTo(var7, var2, var7.getTop())) {
            this.setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
         } else {
            return false;
         }
      }
   }

   void updateObscuredViewsVisibility(View var1) {
      boolean var17 = this.isLayoutRtlSupport();
      int var2;
      if (var17) {
         var2 = this.getWidth() - this.getPaddingRight();
      } else {
         var2 = this.getPaddingLeft();
      }

      int var3;
      if (var17) {
         var3 = this.getPaddingLeft();
      } else {
         var3 = this.getWidth() - this.getPaddingRight();
      }

      int var10 = this.getPaddingTop();
      int var11 = this.getHeight();
      int var12 = this.getPaddingBottom();
      int var4;
      int var5;
      int var6;
      int var7;
      if (var1 != null && viewIsOpaque(var1)) {
         var4 = var1.getLeft();
         var5 = var1.getRight();
         var6 = var1.getTop();
         var7 = var1.getBottom();
      } else {
         var4 = 0;
         var7 = 0;
         var6 = 0;
         var5 = 0;
      }

      int var8 = 0;

      for(int var13 = this.getChildCount(); var8 < var13; ++var8) {
         View var18 = this.getChildAt(var8);
         if (var18 == var1) {
            return;
         }

         if (var18.getVisibility() != 8) {
            int var9;
            if (var17) {
               var9 = var3;
            } else {
               var9 = var2;
            }

            int var14 = Math.max(var9, var18.getLeft());
            int var15 = Math.max(var10, var18.getTop());
            if (var17) {
               var9 = var2;
            } else {
               var9 = var3;
            }

            var9 = Math.min(var9, var18.getRight());
            int var16 = Math.min(var11 - var12, var18.getBottom());
            byte var19;
            if (var14 >= var4 && var15 >= var6 && var9 <= var5 && var16 <= var7) {
               var19 = 4;
            } else {
               var19 = 0;
            }

            var18.setVisibility(var19);
         }
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

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
         var1.setMovementGranularities(var2.getMovementGranularities());
      }

      public boolean filter(View var1) {
         return SlidingPaneLayout.this.isDimmed(var1);
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(SlidingPaneLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         AccessibilityNodeInfoCompat var5 = AccessibilityNodeInfoCompat.obtain(var2);
         super.onInitializeAccessibilityNodeInfo(var1, var5);
         this.copyNodeInfoNoChildren(var2, var5);
         var5.recycle();
         var2.setClassName(SlidingPaneLayout.class.getName());
         var2.setSource(var1);
         ViewParent var6 = ViewCompat.getParentForAccessibility(var1);
         if (var6 instanceof View) {
            var2.setParent((View)var6);
         }

         int var4 = SlidingPaneLayout.this.getChildCount();

         for(int var3 = 0; var3 < var4; ++var3) {
            var1 = SlidingPaneLayout.this.getChildAt(var3);
            if (!this.filter(var1) && var1.getVisibility() == 0) {
               ViewCompat.setImportantForAccessibility(var1, 1);
               var2.addChild(var1);
            }
         }

      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return !this.filter(var2) ? super.onRequestSendAccessibilityEvent(var1, var2, var3) : false;
      }
   }

   private class DisableLayerRunnable implements Runnable {
      final View mChildView;

      DisableLayerRunnable(View var2) {
         this.mChildView = var2;
      }

      public void run() {
         if (this.mChildView.getParent() == SlidingPaneLayout.this) {
            this.mChildView.setLayerType(0, (Paint)null);
            SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
         }

         SlidingPaneLayout.this.mPostedRunnables.remove(this);
      }
   }

   private class DragHelperCallback extends ViewDragHelper.Callback {
      DragHelperCallback() {
      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
         int var4;
         if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
            var3 = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + var5.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
            var4 = SlidingPaneLayout.this.mSlideRange;
            return Math.max(Math.min(var2, var3), var3 - var4);
         } else {
            var3 = SlidingPaneLayout.this.getPaddingLeft() + var5.leftMargin;
            var4 = SlidingPaneLayout.this.mSlideRange;
            return Math.min(Math.max(var2, var3), var4 + var3);
         }
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         return SlidingPaneLayout.this.mSlideRange;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, var2);
      }

      public void onViewCaptured(View var1, int var2) {
         SlidingPaneLayout.this.setAllChildrenVisible();
      }

      public void onViewDragStateChanged(int var1) {
         if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
            SlidingPaneLayout var2;
            if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
               var2 = SlidingPaneLayout.this;
               var2.updateObscuredViewsVisibility(var2.mSlideableView);
               var2 = SlidingPaneLayout.this;
               var2.dispatchOnPanelClosed(var2.mSlideableView);
               SlidingPaneLayout.this.mPreservedOpenState = false;
               return;
            }

            var2 = SlidingPaneLayout.this;
            var2.dispatchOnPanelOpened(var2.mSlideableView);
            SlidingPaneLayout.this.mPreservedOpenState = true;
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         SlidingPaneLayout.this.onPanelDragged(var2);
         SlidingPaneLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         SlidingPaneLayout.LayoutParams var6 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
         int var4;
         if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
            int var5;
            label28: {
               var5 = SlidingPaneLayout.this.getPaddingRight() + var6.rightMargin;
               if (var2 >= 0.0F) {
                  var4 = var5;
                  if (var2 != 0.0F) {
                     break label28;
                  }

                  var4 = var5;
                  if (SlidingPaneLayout.this.mSlideOffset <= 0.5F) {
                     break label28;
                  }
               }

               var4 = var5 + SlidingPaneLayout.this.mSlideRange;
            }

            var5 = SlidingPaneLayout.this.mSlideableView.getWidth();
            var4 = SlidingPaneLayout.this.getWidth() - var4 - var5;
         } else {
            var4 = SlidingPaneLayout.this.getPaddingLeft() + var6.leftMargin;
            if (var2 > 0.0F || var2 == 0.0F && SlidingPaneLayout.this.mSlideOffset > 0.5F) {
               var4 += SlidingPaneLayout.this.mSlideRange;
            }
         }

         SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(var4, var1.getTop());
         SlidingPaneLayout.this.invalidate();
      }

      public boolean tryCaptureView(View var1, int var2) {
         return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)var1.getLayoutParams()).slideable;
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int[] ATTRS = new int[]{16843137};
      Paint dimPaint;
      boolean dimWhenOffset;
      boolean slideable;
      public float weight = 0.0F;

      public LayoutParams() {
         super(-1, -1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, ATTRS);
         this.weight = var3.getFloat(0, 0.0F);
         var3.recycle();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(SlidingPaneLayout.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
      }
   }

   public interface PanelSlideListener {
      void onPanelClosed(View var1);

      void onPanelOpened(View var1);

      void onPanelSlide(View var1, float var2);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public SlidingPaneLayout.SavedState createFromParcel(Parcel var1) {
            return new SlidingPaneLayout.SavedState(var1, (ClassLoader)null);
         }

         public SlidingPaneLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new SlidingPaneLayout.SavedState(var1, (ClassLoader)null);
         }

         public SlidingPaneLayout.SavedState[] newArray(int var1) {
            return new SlidingPaneLayout.SavedState[var1];
         }
      };
      boolean isOpen;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isOpen = var3;
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }

   public static class SimplePanelSlideListener implements SlidingPaneLayout.PanelSlideListener {
      public void onPanelClosed(View var1) {
      }

      public void onPanelOpened(View var1) {
      }

      public void onPanelSlide(View var1, float var2) {
      }
   }
}
