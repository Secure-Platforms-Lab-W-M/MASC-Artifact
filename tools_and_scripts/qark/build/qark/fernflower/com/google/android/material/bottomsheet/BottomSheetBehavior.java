package com.google.android.material.bottomsheet;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior extends CoordinatorLayout.Behavior {
   private static final int CORNER_ANIMATION_DURATION = 500;
   private static final int DEF_STYLE_RES;
   private static final float HIDE_FRICTION = 0.1F;
   private static final float HIDE_THRESHOLD = 0.5F;
   public static final int PEEK_HEIGHT_AUTO = -1;
   public static final int SAVE_ALL = -1;
   public static final int SAVE_FIT_TO_CONTENTS = 2;
   public static final int SAVE_HIDEABLE = 4;
   public static final int SAVE_NONE = 0;
   public static final int SAVE_PEEK_HEIGHT = 1;
   public static final int SAVE_SKIP_COLLAPSED = 8;
   public static final int STATE_COLLAPSED = 4;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_EXPANDED = 3;
   public static final int STATE_HALF_EXPANDED = 6;
   public static final int STATE_HIDDEN = 5;
   public static final int STATE_SETTLING = 2;
   private static final String TAG = "BottomSheetBehavior";
   int activePointerId;
   private final ArrayList callbacks = new ArrayList();
   int collapsedOffset;
   private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         return var1.getLeft();
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         int var4 = BottomSheetBehavior.this.getExpandedOffset();
         if (BottomSheetBehavior.this.hideable) {
            var3 = BottomSheetBehavior.this.parentHeight;
         } else {
            var3 = BottomSheetBehavior.this.collapsedOffset;
         }

         return MathUtils.clamp(var2, var4, var3);
      }

      public int getViewVerticalDragRange(View var1) {
         return BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset;
      }

      public void onViewDragStateChanged(int var1) {
         if (var1 == 1) {
            BottomSheetBehavior.this.setStateInternal(1);
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         BottomSheetBehavior.this.dispatchOnSlide(var3);
      }

      public void onViewReleased(View var1, float var2, float var3) {
         int var4;
         byte var5;
         if (var3 < 0.0F) {
            if (BottomSheetBehavior.this.fitToContents) {
               var4 = BottomSheetBehavior.this.fitToContentsOffset;
               var5 = 3;
            } else if (var1.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
               var4 = BottomSheetBehavior.this.halfExpandedOffset;
               var5 = 6;
            } else {
               var4 = BottomSheetBehavior.this.expandedOffset;
               var5 = 3;
            }
         } else if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(var1, var3) && (var1.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(var2) < Math.abs(var3))) {
            var4 = BottomSheetBehavior.this.parentHeight;
            var5 = 5;
         } else if (var3 != 0.0F && Math.abs(var2) <= Math.abs(var3)) {
            if (BottomSheetBehavior.this.fitToContents) {
               var4 = BottomSheetBehavior.this.collapsedOffset;
               var5 = 4;
            } else {
               var4 = var1.getTop();
               if (Math.abs(var4 - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(var4 - BottomSheetBehavior.this.collapsedOffset)) {
                  var4 = BottomSheetBehavior.this.halfExpandedOffset;
                  var5 = 6;
               } else {
                  var4 = BottomSheetBehavior.this.collapsedOffset;
                  var5 = 4;
               }
            }
         } else {
            var4 = var1.getTop();
            if (BottomSheetBehavior.this.fitToContents) {
               if (Math.abs(var4 - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(var4 - BottomSheetBehavior.this.collapsedOffset)) {
                  var4 = BottomSheetBehavior.this.fitToContentsOffset;
                  var5 = 3;
               } else {
                  var4 = BottomSheetBehavior.this.collapsedOffset;
                  var5 = 4;
               }
            } else if (var4 < BottomSheetBehavior.this.halfExpandedOffset) {
               if (var4 < Math.abs(var4 - BottomSheetBehavior.this.collapsedOffset)) {
                  var4 = BottomSheetBehavior.this.expandedOffset;
                  var5 = 3;
               } else {
                  var4 = BottomSheetBehavior.this.halfExpandedOffset;
                  var5 = 6;
               }
            } else if (Math.abs(var4 - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(var4 - BottomSheetBehavior.this.collapsedOffset)) {
               var4 = BottomSheetBehavior.this.halfExpandedOffset;
               var5 = 6;
            } else {
               var4 = BottomSheetBehavior.this.collapsedOffset;
               var5 = 4;
            }
         }

         BottomSheetBehavior.this.startSettlingAnimation(var1, var5, var4, true);
      }

      public boolean tryCaptureView(View var1, int var2) {
         if (BottomSheetBehavior.this.state == 1) {
            return false;
         } else if (BottomSheetBehavior.this.touchingScrollingChild) {
            return false;
         } else {
            if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == var2) {
               View var3;
               if (BottomSheetBehavior.this.nestedScrollingChildRef != null) {
                  var3 = (View)BottomSheetBehavior.this.nestedScrollingChildRef.get();
               } else {
                  var3 = null;
               }

               if (var3 != null && var3.canScrollVertically(-1)) {
                  return false;
               }
            }

            return BottomSheetBehavior.this.viewRef != null && BottomSheetBehavior.this.viewRef.get() == var1;
         }
      }
   };
   float elevation = -1.0F;
   int expandedOffset;
   private boolean fitToContents = true;
   int fitToContentsOffset;
   int halfExpandedOffset;
   float halfExpandedRatio = 0.5F;
   boolean hideable;
   private boolean ignoreEvents;
   private Map importantForAccessibilityMap;
   private int initialY;
   private ValueAnimator interpolatorAnimator;
   private boolean isShapeExpanded;
   private int lastNestedScrollDy;
   private MaterialShapeDrawable materialShapeDrawable;
   private float maximumVelocity;
   private boolean nestedScrolled;
   WeakReference nestedScrollingChildRef;
   int parentHeight;
   int parentWidth;
   private int peekHeight;
   private boolean peekHeightAuto;
   private int peekHeightMin;
   private int saveFlags = 0;
   private BottomSheetBehavior.SettleRunnable settleRunnable = null;
   private ShapeAppearanceModel shapeAppearanceModelDefault;
   private boolean shapeThemingEnabled;
   private boolean skipCollapsed;
   int state = 4;
   boolean touchingScrollingChild;
   private VelocityTracker velocityTracker;
   ViewDragHelper viewDragHelper;
   WeakReference viewRef;

   static {
      DEF_STYLE_RES = style.Widget_Design_BottomSheet_Modal;
   }

   public BottomSheetBehavior() {
   }

   public BottomSheetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var4 = var1.obtainStyledAttributes(var2, styleable.BottomSheetBehavior_Layout);
      this.shapeThemingEnabled = var4.hasValue(styleable.BottomSheetBehavior_Layout_shapeAppearance);
      boolean var3 = var4.hasValue(styleable.BottomSheetBehavior_Layout_backgroundTint);
      if (var3) {
         this.createMaterialShapeDrawable(var1, var2, var3, MaterialResources.getColorStateList(var1, var4, styleable.BottomSheetBehavior_Layout_backgroundTint));
      } else {
         this.createMaterialShapeDrawable(var1, var2, var3);
      }

      this.createShapeValueAnimator();
      if (VERSION.SDK_INT >= 21) {
         this.elevation = var4.getDimension(styleable.BottomSheetBehavior_Layout_android_elevation, -1.0F);
      }

      TypedValue var5 = var4.peekValue(styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
      if (var5 != null && var5.data == -1) {
         this.setPeekHeight(var5.data);
      } else {
         this.setPeekHeight(var4.getDimensionPixelSize(styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
      }

      this.setHideable(var4.getBoolean(styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
      this.setFitToContents(var4.getBoolean(styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
      this.setSkipCollapsed(var4.getBoolean(styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
      this.setSaveFlags(var4.getInt(styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
      this.setHalfExpandedRatio(var4.getFloat(styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5F));
      this.setExpandedOffset(var4.getInt(styleable.BottomSheetBehavior_Layout_behavior_expandedOffset, 0));
      var4.recycle();
      this.maximumVelocity = (float)ViewConfiguration.get(var1).getScaledMaximumFlingVelocity();
   }

   private void addAccessibilityActionForState(View var1, AccessibilityNodeInfoCompat.AccessibilityActionCompat var2, final int var3) {
      ViewCompat.replaceAccessibilityAction(var1, var2, (CharSequence)null, new AccessibilityViewCommand() {
         public boolean perform(View var1, AccessibilityViewCommand.CommandArguments var2) {
            BottomSheetBehavior.this.setState(var3);
            return true;
         }
      });
   }

   private void calculateCollapsedOffset() {
      int var1;
      if (this.peekHeightAuto) {
         var1 = Math.max(this.peekHeightMin, this.parentHeight - this.parentWidth * 9 / 16);
      } else {
         var1 = this.peekHeight;
      }

      if (this.fitToContents) {
         this.collapsedOffset = Math.max(this.parentHeight - var1, this.fitToContentsOffset);
      } else {
         this.collapsedOffset = this.parentHeight - var1;
      }
   }

   private void calculateHalfExpandedOffset() {
      this.halfExpandedOffset = (int)((float)this.parentHeight * (1.0F - this.halfExpandedRatio));
   }

   private void createMaterialShapeDrawable(Context var1, AttributeSet var2, boolean var3) {
      this.createMaterialShapeDrawable(var1, var2, var3, (ColorStateList)null);
   }

   private void createMaterialShapeDrawable(Context var1, AttributeSet var2, boolean var3, ColorStateList var4) {
      if (this.shapeThemingEnabled) {
         this.shapeAppearanceModelDefault = ShapeAppearanceModel.builder(var1, var2, attr.bottomSheetStyle, DEF_STYLE_RES).build();
         MaterialShapeDrawable var5 = new MaterialShapeDrawable(this.shapeAppearanceModelDefault);
         this.materialShapeDrawable = var5;
         var5.initializeElevationOverlay(var1);
         if (var3 && var4 != null) {
            this.materialShapeDrawable.setFillColor(var4);
            return;
         }

         TypedValue var6 = new TypedValue();
         var1.getTheme().resolveAttribute(16842801, var6, true);
         this.materialShapeDrawable.setTint(var6.data);
      }

   }

   private void createShapeValueAnimator() {
      ValueAnimator var1 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
      this.interpolatorAnimator = var1;
      var1.setDuration(500L);
      this.interpolatorAnimator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = (Float)var1.getAnimatedValue();
            if (BottomSheetBehavior.this.materialShapeDrawable != null) {
               BottomSheetBehavior.this.materialShapeDrawable.setInterpolation(var2);
            }

         }
      });
   }

   public static BottomSheetBehavior from(View var0) {
      LayoutParams var1 = var0.getLayoutParams();
      if (var1 instanceof CoordinatorLayout.LayoutParams) {
         CoordinatorLayout.Behavior var2 = ((CoordinatorLayout.LayoutParams)var1).getBehavior();
         if (var2 instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior)var2;
         } else {
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
         }
      } else {
         throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
      }
   }

   private int getExpandedOffset() {
      return this.fitToContents ? this.fitToContentsOffset : this.expandedOffset;
   }

   private float getYVelocity() {
      VelocityTracker var1 = this.velocityTracker;
      if (var1 == null) {
         return 0.0F;
      } else {
         var1.computeCurrentVelocity(1000, this.maximumVelocity);
         return this.velocityTracker.getYVelocity(this.activePointerId);
      }
   }

   private void reset() {
      this.activePointerId = -1;
      VelocityTracker var1 = this.velocityTracker;
      if (var1 != null) {
         var1.recycle();
         this.velocityTracker = null;
      }

   }

   private void restoreOptionalState(BottomSheetBehavior.SavedState var1) {
      int var2 = this.saveFlags;
      if (var2 != 0) {
         if (var2 == -1 || (var2 & 1) == 1) {
            this.peekHeight = var1.peekHeight;
         }

         var2 = this.saveFlags;
         if (var2 == -1 || (var2 & 2) == 2) {
            this.fitToContents = var1.fitToContents;
         }

         var2 = this.saveFlags;
         if (var2 == -1 || (var2 & 4) == 4) {
            this.hideable = var1.hideable;
         }

         var2 = this.saveFlags;
         if (var2 == -1 || (var2 & 8) == 8) {
            this.skipCollapsed = var1.skipCollapsed;
         }

      }
   }

   private void settleToStatePendingLayout(final int var1) {
      final View var2 = (View)this.viewRef.get();
      if (var2 != null) {
         ViewParent var3 = var2.getParent();
         if (var3 != null && var3.isLayoutRequested() && ViewCompat.isAttachedToWindow(var2)) {
            var2.post(new Runnable() {
               public void run() {
                  BottomSheetBehavior.this.settleToState(var2, var1);
               }
            });
         } else {
            this.settleToState(var2, var1);
         }
      }
   }

   private void updateAccessibilityActions() {
      WeakReference var3 = this.viewRef;
      if (var3 != null) {
         View var4 = (View)var3.get();
         if (var4 != null) {
            ViewCompat.removeAccessibilityAction(var4, 524288);
            ViewCompat.removeAccessibilityAction(var4, 262144);
            ViewCompat.removeAccessibilityAction(var4, 1048576);
            if (this.hideable && this.state != 5) {
               this.addAccessibilityActionForState(var4, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, 5);
            }

            int var2 = this.state;
            byte var1 = 6;
            if (var2 != 3) {
               if (var2 != 4) {
                  if (var2 == 6) {
                     this.addAccessibilityActionForState(var4, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, 4);
                     this.addAccessibilityActionForState(var4, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, 3);
                  }
               } else {
                  if (this.fitToContents) {
                     var1 = 3;
                  }

                  this.addAccessibilityActionForState(var4, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, var1);
               }
            } else {
               if (this.fitToContents) {
                  var1 = 4;
               }

               this.addAccessibilityActionForState(var4, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, var1);
            }
         }
      }
   }

   private void updateDrawableForTargetState(int var1) {
      if (var1 != 2) {
         boolean var3;
         if (var1 == 3) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (this.isShapeExpanded != var3) {
            this.isShapeExpanded = var3;
            if (this.materialShapeDrawable != null) {
               ValueAnimator var4 = this.interpolatorAnimator;
               if (var4 != null) {
                  if (var4.isRunning()) {
                     this.interpolatorAnimator.reverse();
                     return;
                  }

                  float var2;
                  if (var3) {
                     var2 = 0.0F;
                  } else {
                     var2 = 1.0F;
                  }

                  this.interpolatorAnimator.setFloatValues(new float[]{1.0F - var2, var2});
                  this.interpolatorAnimator.start();
               }
            }
         }

      }
   }

   private void updateImportantForAccessibility(boolean var1) {
      WeakReference var4 = this.viewRef;
      if (var4 != null) {
         ViewParent var7 = ((View)var4.get()).getParent();
         if (var7 instanceof CoordinatorLayout) {
            CoordinatorLayout var8 = (CoordinatorLayout)var7;
            int var3 = var8.getChildCount();
            if (VERSION.SDK_INT >= 16 && var1) {
               if (this.importantForAccessibilityMap != null) {
                  return;
               }

               this.importantForAccessibilityMap = new HashMap(var3);
            }

            for(int var2 = 0; var2 < var3; ++var2) {
               View var5 = var8.getChildAt(var2);
               if (var5 != this.viewRef.get()) {
                  if (!var1) {
                     Map var6 = this.importantForAccessibilityMap;
                     if (var6 != null && var6.containsKey(var5)) {
                        ViewCompat.setImportantForAccessibility(var5, (Integer)this.importantForAccessibilityMap.get(var5));
                     }
                  } else {
                     if (VERSION.SDK_INT >= 16) {
                        this.importantForAccessibilityMap.put(var5, var5.getImportantForAccessibility());
                     }

                     ViewCompat.setImportantForAccessibility(var5, 4);
                  }
               }
            }

            if (!var1) {
               this.importantForAccessibilityMap = null;
            }

         }
      }
   }

   public void addBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback var1) {
      if (!this.callbacks.contains(var1)) {
         this.callbacks.add(var1);
      }

   }

   public void disableShapeAnimations() {
      this.interpolatorAnimator = null;
   }

   void dispatchOnSlide(int var1) {
      View var4 = (View)this.viewRef.get();
      if (var4 != null && !this.callbacks.isEmpty()) {
         int var3 = this.collapsedOffset;
         float var2 = (float)(var3 - var1);
         if (var1 > var3) {
            var2 /= (float)(this.parentHeight - var3);
         } else {
            var2 /= (float)(var3 - this.getExpandedOffset());
         }

         for(var1 = 0; var1 < this.callbacks.size(); ++var1) {
            ((BottomSheetBehavior.BottomSheetCallback)this.callbacks.get(var1)).onSlide(var4, var2);
         }
      }

   }

   View findScrollingChild(View var1) {
      if (ViewCompat.isNestedScrollingEnabled(var1)) {
         return var1;
      } else {
         if (var1 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var1;
            int var2 = 0;

            for(int var3 = var5.getChildCount(); var2 < var3; ++var2) {
               View var4 = this.findScrollingChild(var5.getChildAt(var2));
               if (var4 != null) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public float getHalfExpandedRatio() {
      return this.halfExpandedRatio;
   }

   public int getPeekHeight() {
      return this.peekHeightAuto ? -1 : this.peekHeight;
   }

   int getPeekHeightMin() {
      return this.peekHeightMin;
   }

   public int getSaveFlags() {
      return this.saveFlags;
   }

   public boolean getSkipCollapsed() {
      return this.skipCollapsed;
   }

   public int getState() {
      return this.state;
   }

   public boolean isFitToContents() {
      return this.fitToContents;
   }

   public boolean isHideable() {
      return this.hideable;
   }

   public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
      super.onAttachedToLayoutParams(var1);
      this.viewRef = null;
      this.viewDragHelper = null;
   }

   public void onDetachedFromLayoutParams() {
      super.onDetachedFromLayoutParams();
      this.viewRef = null;
      this.viewDragHelper = null;
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (!var2.isShown()) {
         this.ignoreEvents = true;
         return false;
      } else {
         int var4 = var3.getActionMasked();
         if (var4 == 0) {
            this.reset();
         }

         if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
         }

         this.velocityTracker.addMovement(var3);
         Object var8 = null;
         WeakReference var7;
         if (var4 != 0) {
            if (var4 == 1 || var4 == 3) {
               this.touchingScrollingChild = false;
               this.activePointerId = -1;
               if (this.ignoreEvents) {
                  this.ignoreEvents = false;
                  return false;
               }
            }
         } else {
            int var5 = (int)var3.getX();
            this.initialY = (int)var3.getY();
            if (this.state != 2) {
               var7 = this.nestedScrollingChildRef;
               View var10;
               if (var7 != null) {
                  var10 = (View)var7.get();
               } else {
                  var10 = null;
               }

               if (var10 != null && var1.isPointInChildBounds(var10, var5, this.initialY)) {
                  this.activePointerId = var3.getPointerId(var3.getActionIndex());
                  this.touchingScrollingChild = true;
               }
            }

            boolean var6;
            if (this.activePointerId == -1 && !var1.isPointInChildBounds(var2, var5, this.initialY)) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.ignoreEvents = var6;
         }

         if (!this.ignoreEvents) {
            ViewDragHelper var9 = this.viewDragHelper;
            if (var9 != null && var9.shouldInterceptTouchEvent(var3)) {
               return true;
            }
         }

         var7 = this.nestedScrollingChildRef;
         var2 = (View)var8;
         if (var7 != null) {
            var2 = (View)var7.get();
         }

         return var4 == 2 && var2 != null && !this.ignoreEvents && this.state != 1 && !var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY()) && this.viewDragHelper != null && Math.abs((float)this.initialY - var3.getY()) > (float)this.viewDragHelper.getTouchSlop();
      }
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      if (ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
         var2.setFitsSystemWindows(true);
      }

      if (this.viewRef == null) {
         this.peekHeightMin = var1.getResources().getDimensionPixelSize(dimen.design_bottom_sheet_peek_height_min);
         this.viewRef = new WeakReference(var2);
         MaterialShapeDrawable var7;
         if (this.shapeThemingEnabled) {
            var7 = this.materialShapeDrawable;
            if (var7 != null) {
               ViewCompat.setBackground(var2, var7);
            }
         }

         var7 = this.materialShapeDrawable;
         if (var7 != null) {
            float var4 = this.elevation;
            if (var4 == -1.0F) {
               var4 = ViewCompat.getElevation(var2);
            }

            var7.setElevation(var4);
            boolean var6;
            if (this.state == 3) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.isShapeExpanded = var6;
            var7 = this.materialShapeDrawable;
            if (var6) {
               var4 = 0.0F;
            } else {
               var4 = 1.0F;
            }

            var7.setInterpolation(var4);
         }

         this.updateAccessibilityActions();
         if (ViewCompat.getImportantForAccessibility(var2) == 0) {
            ViewCompat.setImportantForAccessibility(var2, 1);
         }
      }

      if (this.viewDragHelper == null) {
         this.viewDragHelper = ViewDragHelper.create(var1, this.dragCallback);
      }

      int var5 = var2.getTop();
      var1.onLayoutChild(var2, var3);
      this.parentWidth = var1.getWidth();
      var3 = var1.getHeight();
      this.parentHeight = var3;
      this.fitToContentsOffset = Math.max(0, var3 - var2.getHeight());
      this.calculateHalfExpandedOffset();
      this.calculateCollapsedOffset();
      var3 = this.state;
      if (var3 == 3) {
         ViewCompat.offsetTopAndBottom(var2, this.getExpandedOffset());
      } else if (var3 == 6) {
         ViewCompat.offsetTopAndBottom(var2, this.halfExpandedOffset);
      } else if (this.hideable && var3 == 5) {
         ViewCompat.offsetTopAndBottom(var2, this.parentHeight);
      } else {
         var3 = this.state;
         if (var3 == 4) {
            ViewCompat.offsetTopAndBottom(var2, this.collapsedOffset);
         } else if (var3 == 1 || var3 == 2) {
            ViewCompat.offsetTopAndBottom(var2, var5 - var2.getTop());
         }
      }

      this.nestedScrollingChildRef = new WeakReference(this.findScrollingChild(var2));
      return true;
   }

   public boolean onNestedPreFling(CoordinatorLayout var1, View var2, View var3, float var4, float var5) {
      WeakReference var6 = this.nestedScrollingChildRef;
      if (var6 == null) {
         return false;
      } else {
         return var3 == var6.get() && (this.state != 3 || super.onNestedPreFling(var1, var2, var3, var4, var5));
      }
   }

   public void onNestedPreScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int[] var6, int var7) {
      if (var7 != 1) {
         WeakReference var9 = this.nestedScrollingChildRef;
         View var10;
         if (var9 != null) {
            var10 = (View)var9.get();
         } else {
            var10 = null;
         }

         if (var3 == var10) {
            var4 = var2.getTop();
            var7 = var4 - var5;
            if (var5 > 0) {
               if (var7 < this.getExpandedOffset()) {
                  var6[1] = var4 - this.getExpandedOffset();
                  ViewCompat.offsetTopAndBottom(var2, -var6[1]);
                  this.setStateInternal(3);
               } else {
                  var6[1] = var5;
                  ViewCompat.offsetTopAndBottom(var2, -var5);
                  this.setStateInternal(1);
               }
            } else if (var5 < 0 && !var3.canScrollVertically(-1)) {
               int var8 = this.collapsedOffset;
               if (var7 > var8 && !this.hideable) {
                  var6[1] = var4 - var8;
                  ViewCompat.offsetTopAndBottom(var2, -var6[1]);
                  this.setStateInternal(4);
               } else {
                  var6[1] = var5;
                  ViewCompat.offsetTopAndBottom(var2, -var5);
                  this.setStateInternal(1);
               }
            }

            this.dispatchOnSlide(var2.getTop());
            this.lastNestedScrollDy = var5;
            this.nestedScrolled = true;
         }
      }
   }

   public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7, int var8, int[] var9) {
   }

   public void onRestoreInstanceState(CoordinatorLayout var1, View var2, Parcelable var3) {
      BottomSheetBehavior.SavedState var4 = (BottomSheetBehavior.SavedState)var3;
      super.onRestoreInstanceState(var1, var2, var4.getSuperState());
      this.restoreOptionalState(var4);
      if (var4.state != 1 && var4.state != 2) {
         this.state = var4.state;
      } else {
         this.state = 4;
      }
   }

   public Parcelable onSaveInstanceState(CoordinatorLayout var1, View var2) {
      return new BottomSheetBehavior.SavedState(super.onSaveInstanceState(var1, var2), this);
   }

   public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5, int var6) {
      boolean var7 = false;
      this.lastNestedScrollDy = 0;
      this.nestedScrolled = false;
      if ((var5 & 2) != 0) {
         var7 = true;
      }

      return var7;
   }

   public void onStopNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4) {
      if (var2.getTop() == this.getExpandedOffset()) {
         this.setStateInternal(3);
      } else {
         WeakReference var6 = this.nestedScrollingChildRef;
         if (var6 != null && var3 == var6.get()) {
            if (this.nestedScrolled) {
               byte var5;
               if (this.lastNestedScrollDy > 0) {
                  var4 = this.getExpandedOffset();
                  var5 = 3;
               } else if (this.hideable && this.shouldHide(var2, this.getYVelocity())) {
                  var4 = this.parentHeight;
                  var5 = 5;
               } else if (this.lastNestedScrollDy == 0) {
                  var4 = var2.getTop();
                  if (this.fitToContents) {
                     if (Math.abs(var4 - this.fitToContentsOffset) < Math.abs(var4 - this.collapsedOffset)) {
                        var4 = this.fitToContentsOffset;
                        var5 = 3;
                     } else {
                        var4 = this.collapsedOffset;
                        var5 = 4;
                     }
                  } else {
                     int var7 = this.halfExpandedOffset;
                     if (var4 < var7) {
                        if (var4 < Math.abs(var4 - this.collapsedOffset)) {
                           var4 = this.expandedOffset;
                           var5 = 3;
                        } else {
                           var4 = this.halfExpandedOffset;
                           var5 = 6;
                        }
                     } else if (Math.abs(var4 - var7) < Math.abs(var4 - this.collapsedOffset)) {
                        var4 = this.halfExpandedOffset;
                        var5 = 6;
                     } else {
                        var4 = this.collapsedOffset;
                        var5 = 4;
                     }
                  }
               } else if (this.fitToContents) {
                  var4 = this.collapsedOffset;
                  var5 = 4;
               } else {
                  var4 = var2.getTop();
                  if (Math.abs(var4 - this.halfExpandedOffset) < Math.abs(var4 - this.collapsedOffset)) {
                     var4 = this.halfExpandedOffset;
                     var5 = 6;
                  } else {
                     var4 = this.collapsedOffset;
                     var5 = 4;
                  }
               }

               this.startSettlingAnimation(var2, var5, var4, false);
               this.nestedScrolled = false;
            }
         }
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (!var2.isShown()) {
         return false;
      } else {
         int var4 = var3.getActionMasked();
         if (this.state == 1 && var4 == 0) {
            return true;
         } else {
            ViewDragHelper var5 = this.viewDragHelper;
            if (var5 != null) {
               var5.processTouchEvent(var3);
            }

            if (var4 == 0) {
               this.reset();
            }

            if (this.velocityTracker == null) {
               this.velocityTracker = VelocityTracker.obtain();
            }

            this.velocityTracker.addMovement(var3);
            if (var4 == 2 && !this.ignoreEvents && Math.abs((float)this.initialY - var3.getY()) > (float)this.viewDragHelper.getTouchSlop()) {
               this.viewDragHelper.captureChildView(var2, var3.getPointerId(var3.getActionIndex()));
            }

            return this.ignoreEvents ^ true;
         }
      }
   }

   public void removeBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback var1) {
      this.callbacks.remove(var1);
   }

   @Deprecated
   public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback var1) {
      Log.w("BottomSheetBehavior", "BottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes all existing callbacks, including ones set internally by library authors, which may result in unintended behavior. This may change in the future. Please use `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your own callbacks.");
      this.callbacks.clear();
      if (var1 != null) {
         this.callbacks.add(var1);
      }

   }

   public void setExpandedOffset(int var1) {
      if (var1 >= 0) {
         this.expandedOffset = var1;
      } else {
         throw new IllegalArgumentException("offset must be greater than or equal to 0");
      }
   }

   public void setFitToContents(boolean var1) {
      if (this.fitToContents != var1) {
         this.fitToContents = var1;
         if (this.viewRef != null) {
            this.calculateCollapsedOffset();
         }

         int var2;
         if (this.fitToContents && this.state == 6) {
            var2 = 3;
         } else {
            var2 = this.state;
         }

         this.setStateInternal(var2);
         this.updateAccessibilityActions();
      }
   }

   public void setHalfExpandedRatio(float var1) {
      if (var1 > 0.0F && var1 < 1.0F) {
         this.halfExpandedRatio = var1;
      } else {
         throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
      }
   }

   public void setHideable(boolean var1) {
      if (this.hideable != var1) {
         this.hideable = var1;
         if (!var1 && this.state == 5) {
            this.setState(4);
         }

         this.updateAccessibilityActions();
      }

   }

   public void setPeekHeight(int var1) {
      this.setPeekHeight(var1, false);
   }

   public final void setPeekHeight(int var1, boolean var2) {
      boolean var3 = false;
      if (var1 == -1) {
         if (!this.peekHeightAuto) {
            this.peekHeightAuto = true;
            var3 = true;
         }
      } else if (this.peekHeightAuto || this.peekHeight != var1) {
         this.peekHeightAuto = false;
         this.peekHeight = Math.max(0, var1);
         var3 = true;
      }

      if (var3 && this.viewRef != null) {
         this.calculateCollapsedOffset();
         if (this.state == 4) {
            View var4 = (View)this.viewRef.get();
            if (var4 != null) {
               if (var2) {
                  this.settleToStatePendingLayout(this.state);
                  return;
               }

               var4.requestLayout();
            }
         }
      }

   }

   public void setSaveFlags(int var1) {
      this.saveFlags = var1;
   }

   public void setSkipCollapsed(boolean var1) {
      this.skipCollapsed = var1;
   }

   public void setState(int var1) {
      if (var1 != this.state) {
         if (this.viewRef != null) {
            this.settleToStatePendingLayout(var1);
         } else {
            if (var1 == 4 || var1 == 3 || var1 == 6 || this.hideable && var1 == 5) {
               this.state = var1;
            }

         }
      }
   }

   void setStateInternal(int var1) {
      if (this.state != var1) {
         this.state = var1;
         WeakReference var3 = this.viewRef;
         if (var3 != null) {
            View var4 = (View)var3.get();
            if (var4 != null) {
               if (var1 != 6 && var1 != 3) {
                  if (var1 == 5 || var1 == 4) {
                     this.updateImportantForAccessibility(false);
                  }
               } else {
                  this.updateImportantForAccessibility(true);
               }

               this.updateDrawableForTargetState(var1);

               for(int var2 = 0; var2 < this.callbacks.size(); ++var2) {
                  ((BottomSheetBehavior.BottomSheetCallback)this.callbacks.get(var2)).onStateChanged(var4, var1);
               }

               this.updateAccessibilityActions();
            }
         }
      }
   }

   void settleToState(View var1, int var2) {
      int var3;
      int var4;
      if (var2 == 4) {
         var3 = this.collapsedOffset;
         var4 = var2;
      } else if (var2 == 6) {
         int var5 = this.halfExpandedOffset;
         var3 = var5;
         var4 = var2;
         if (this.fitToContents) {
            var3 = var5;
            var4 = var2;
            if (var5 <= this.fitToContentsOffset) {
               var4 = 3;
               var3 = this.fitToContentsOffset;
            }
         }
      } else if (var2 == 3) {
         var3 = this.getExpandedOffset();
         var4 = var2;
      } else {
         if (!this.hideable || var2 != 5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Illegal state argument: ");
            var6.append(var2);
            throw new IllegalArgumentException(var6.toString());
         }

         var3 = this.parentHeight;
         var4 = var2;
      }

      this.startSettlingAnimation(var1, var4, var3, false);
   }

   boolean shouldHide(View var1, float var2) {
      if (this.skipCollapsed) {
         return true;
      } else if (var1.getTop() < this.collapsedOffset) {
         return false;
      } else {
         return Math.abs((float)var1.getTop() + 0.1F * var2 - (float)this.collapsedOffset) / (float)this.peekHeight > 0.5F;
      }
   }

   void startSettlingAnimation(View var1, int var2, int var3, boolean var4) {
      if (var4) {
         var4 = this.viewDragHelper.settleCapturedViewAt(var1.getLeft(), var3);
      } else {
         var4 = this.viewDragHelper.smoothSlideViewTo(var1, var1.getLeft(), var3);
      }

      if (var4) {
         this.setStateInternal(2);
         this.updateDrawableForTargetState(var2);
         if (this.settleRunnable == null) {
            this.settleRunnable = new BottomSheetBehavior.SettleRunnable(var1, var2);
         }

         if (!this.settleRunnable.isPosted) {
            this.settleRunnable.targetState = var2;
            ViewCompat.postOnAnimation(var1, this.settleRunnable);
            this.settleRunnable.isPosted = true;
         } else {
            this.settleRunnable.targetState = var2;
         }
      } else {
         this.setStateInternal(var2);
      }
   }

   public abstract static class BottomSheetCallback {
      public abstract void onSlide(View var1, float var2);

      public abstract void onStateChanged(View var1, int var2);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface SaveFlags {
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1) {
            return new BottomSheetBehavior.SavedState(var1, (ClassLoader)null);
         }

         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomSheetBehavior.SavedState(var1, var2);
         }

         public BottomSheetBehavior.SavedState[] newArray(int var1) {
            return new BottomSheetBehavior.SavedState[var1];
         }
      };
      boolean fitToContents;
      boolean hideable;
      int peekHeight;
      boolean skipCollapsed;
      final int state;

      public SavedState(Parcel var1) {
         this((Parcel)var1, (ClassLoader)null);
      }

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.state = var1.readInt();
         this.peekHeight = var1.readInt();
         int var3 = var1.readInt();
         boolean var5 = false;
         boolean var4;
         if (var3 == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.fitToContents = var4;
         if (var1.readInt() == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.hideable = var4;
         var4 = var5;
         if (var1.readInt() == 1) {
            var4 = true;
         }

         this.skipCollapsed = var4;
      }

      @Deprecated
      public SavedState(Parcelable var1, int var2) {
         super(var1);
         this.state = var2;
      }

      public SavedState(Parcelable var1, BottomSheetBehavior var2) {
         super(var1);
         this.state = var2.state;
         this.peekHeight = var2.peekHeight;
         this.fitToContents = var2.fitToContents;
         this.hideable = var2.hideable;
         this.skipCollapsed = var2.skipCollapsed;
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }

   private class SettleRunnable implements Runnable {
      private boolean isPosted;
      int targetState;
      private final View view;

      SettleRunnable(View var2, int var3) {
         this.view = var2;
         this.targetState = var3;
      }

      public void run() {
         if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
            ViewCompat.postOnAnimation(this.view, this);
         } else {
            BottomSheetBehavior.this.setStateInternal(this.targetState);
         }

         this.isPosted = false;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface State {
   }
}
