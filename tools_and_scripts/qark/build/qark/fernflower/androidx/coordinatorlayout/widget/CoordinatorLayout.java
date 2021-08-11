package androidx.coordinatorlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import androidx.coordinatorlayout.R.attr;
import androidx.coordinatorlayout.R.style;
import androidx.coordinatorlayout.R.styleable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingParent3 {
   static final Class[] CONSTRUCTOR_PARAMS;
   static final int EVENT_NESTED_SCROLL = 1;
   static final int EVENT_PRE_DRAW = 0;
   static final int EVENT_VIEW_REMOVED = 2;
   static final String TAG = "CoordinatorLayout";
   static final Comparator TOP_SORTED_CHILDREN_COMPARATOR;
   private static final int TYPE_ON_INTERCEPT = 0;
   private static final int TYPE_ON_TOUCH = 1;
   static final String WIDGET_PACKAGE_NAME;
   static final ThreadLocal sConstructors;
   private static final Pools.Pool sRectPool;
   private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
   private final int[] mBehaviorConsumed;
   private View mBehaviorTouchView;
   private final DirectedAcyclicGraph mChildDag;
   private final List mDependencySortedChildren;
   private boolean mDisallowInterceptReset;
   private boolean mDrawStatusBarBackground;
   private boolean mIsAttachedToWindow;
   private int[] mKeylines;
   private WindowInsetsCompat mLastInsets;
   private boolean mNeedsPreDrawListener;
   private final NestedScrollingParentHelper mNestedScrollingParentHelper;
   private View mNestedScrollingTarget;
   private final int[] mNestedScrollingV2ConsumedCompat;
   OnHierarchyChangeListener mOnHierarchyChangeListener;
   private CoordinatorLayout.OnPreDrawListener mOnPreDrawListener;
   private Paint mScrimPaint;
   private Drawable mStatusBarBackground;
   private final List mTempDependenciesList;
   private final List mTempList1;

   static {
      Package var0 = CoordinatorLayout.class.getPackage();
      String var1;
      if (var0 != null) {
         var1 = var0.getName();
      } else {
         var1 = null;
      }

      WIDGET_PACKAGE_NAME = var1;
      if (VERSION.SDK_INT >= 21) {
         TOP_SORTED_CHILDREN_COMPARATOR = new CoordinatorLayout.ViewElevationComparator();
      } else {
         TOP_SORTED_CHILDREN_COMPARATOR = null;
      }

      CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
      sConstructors = new ThreadLocal();
      sRectPool = new Pools.SynchronizedPool(12);
   }

   public CoordinatorLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CoordinatorLayout(Context var1, AttributeSet var2) {
      this(var1, var2, attr.coordinatorLayoutStyle);
   }

   public CoordinatorLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mDependencySortedChildren = new ArrayList();
      this.mChildDag = new DirectedAcyclicGraph();
      this.mTempList1 = new ArrayList();
      this.mTempDependenciesList = new ArrayList();
      this.mBehaviorConsumed = new int[2];
      this.mNestedScrollingV2ConsumedCompat = new int[2];
      this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
      TypedArray var6;
      if (var3 == 0) {
         var6 = var1.obtainStyledAttributes(var2, styleable.CoordinatorLayout, 0, style.Widget_Support_CoordinatorLayout);
      } else {
         var6 = var1.obtainStyledAttributes(var2, styleable.CoordinatorLayout, var3, 0);
      }

      if (VERSION.SDK_INT >= 29) {
         if (var3 == 0) {
            this.saveAttributeDataForStyleable(var1, styleable.CoordinatorLayout, var2, var6, 0, style.Widget_Support_CoordinatorLayout);
         } else {
            this.saveAttributeDataForStyleable(var1, styleable.CoordinatorLayout, var2, var6, var3, 0);
         }
      }

      var3 = var6.getResourceId(styleable.CoordinatorLayout_keylines, 0);
      if (var3 != 0) {
         Resources var7 = var1.getResources();
         this.mKeylines = var7.getIntArray(var3);
         float var4 = var7.getDisplayMetrics().density;
         int var5 = this.mKeylines.length;

         for(var3 = 0; var3 < var5; ++var3) {
            int[] var8 = this.mKeylines;
            var8[var3] = (int)((float)var8[var3] * var4);
         }
      }

      this.mStatusBarBackground = var6.getDrawable(styleable.CoordinatorLayout_statusBarBackground);
      var6.recycle();
      this.setupForInsets();
      super.setOnHierarchyChangeListener(new CoordinatorLayout.HierarchyChangeListener());
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

   }

   private static Rect acquireTempRect() {
      Rect var1 = (Rect)sRectPool.acquire();
      Rect var0 = var1;
      if (var1 == null) {
         var0 = new Rect();
      }

      return var0;
   }

   private static int clamp(int var0, int var1, int var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   private void constrainChildRect(CoordinatorLayout.LayoutParams var1, Rect var2, int var3, int var4) {
      int var6 = this.getWidth();
      int var5 = this.getHeight();
      var6 = Math.max(this.getPaddingLeft() + var1.leftMargin, Math.min(var2.left, var6 - this.getPaddingRight() - var3 - var1.rightMargin));
      var5 = Math.max(this.getPaddingTop() + var1.topMargin, Math.min(var2.top, var5 - this.getPaddingBottom() - var4 - var1.bottomMargin));
      var2.set(var6, var5, var6 + var3, var5 + var4);
   }

   private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat var1) {
      if (var1.isConsumed()) {
         return var1;
      } else {
         int var2 = 0;
         int var3 = this.getChildCount();

         WindowInsetsCompat var4;
         for(var4 = var1; var2 < var3; var4 = var1) {
            View var5 = this.getChildAt(var2);
            var1 = var4;
            if (ViewCompat.getFitsSystemWindows(var5)) {
               CoordinatorLayout.Behavior var6 = ((CoordinatorLayout.LayoutParams)var5.getLayoutParams()).getBehavior();
               var1 = var4;
               if (var6 != null) {
                  var4 = var6.onApplyWindowInsets(this, var5, var4);
                  var1 = var4;
                  if (var4.isConsumed()) {
                     return var4;
                  }
               }
            }

            ++var2;
         }

         return var4;
      }
   }

   private void getDesiredAnchoredChildRectWithoutConstraints(View var1, int var2, Rect var3, Rect var4, CoordinatorLayout.LayoutParams var5, int var6, int var7) {
      int var8 = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(var5.gravity), var2);
      var2 = GravityCompat.getAbsoluteGravity(resolveGravity(var5.anchorGravity), var2);
      int var10 = var8 & 7;
      int var9 = var8 & 112;
      int var11 = var2 & 7;
      var8 = var2 & 112;
      if (var11 != 1) {
         if (var11 != 5) {
            var2 = var3.left;
         } else {
            var2 = var3.right;
         }
      } else {
         var2 = var3.left + var3.width() / 2;
      }

      if (var8 != 16) {
         if (var8 != 80) {
            var8 = var3.top;
         } else {
            var8 = var3.bottom;
         }
      } else {
         var8 = var3.top + var3.height() / 2;
      }

      if (var10 != 1) {
         if (var10 != 5) {
            var2 -= var6;
         }
      } else {
         var2 -= var6 / 2;
      }

      if (var9 != 16) {
         if (var9 != 80) {
            var8 -= var7;
         }
      } else {
         var8 -= var7 / 2;
      }

      var4.set(var2, var8, var2 + var6, var8 + var7);
   }

   private int getKeyline(int var1) {
      int[] var2 = this.mKeylines;
      StringBuilder var3;
      if (var2 == null) {
         var3 = new StringBuilder();
         var3.append("No keylines defined for ");
         var3.append(this);
         var3.append(" - attempted index lookup ");
         var3.append(var1);
         Log.e("CoordinatorLayout", var3.toString());
         return 0;
      } else if (var1 >= 0 && var1 < var2.length) {
         return var2[var1];
      } else {
         var3 = new StringBuilder();
         var3.append("Keyline index ");
         var3.append(var1);
         var3.append(" out of range for ");
         var3.append(this);
         Log.e("CoordinatorLayout", var3.toString());
         return 0;
      }
   }

   private void getTopSortedChildren(List var1) {
      var1.clear();
      boolean var5 = this.isChildrenDrawingOrderEnabled();
      int var4 = this.getChildCount();

      for(int var2 = var4 - 1; var2 >= 0; --var2) {
         int var3;
         if (var5) {
            var3 = this.getChildDrawingOrder(var4, var2);
         } else {
            var3 = var2;
         }

         var1.add(this.getChildAt(var3));
      }

      Comparator var6 = TOP_SORTED_CHILDREN_COMPARATOR;
      if (var6 != null) {
         Collections.sort(var1, var6);
      }

   }

   private boolean hasDependencies(View var1) {
      return this.mChildDag.hasOutgoingEdges(var1);
   }

   private void layoutChild(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      Rect var4 = acquireTempRect();
      var4.set(this.getPaddingLeft() + var3.leftMargin, this.getPaddingTop() + var3.topMargin, this.getWidth() - this.getPaddingRight() - var3.rightMargin, this.getHeight() - this.getPaddingBottom() - var3.bottomMargin);
      if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this) && !ViewCompat.getFitsSystemWindows(var1)) {
         var4.left += this.mLastInsets.getSystemWindowInsetLeft();
         var4.top += this.mLastInsets.getSystemWindowInsetTop();
         var4.right -= this.mLastInsets.getSystemWindowInsetRight();
         var4.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
      }

      Rect var5 = acquireTempRect();
      GravityCompat.apply(resolveGravity(var3.gravity), var1.getMeasuredWidth(), var1.getMeasuredHeight(), var4, var5, var2);
      var1.layout(var5.left, var5.top, var5.right, var5.bottom);
      releaseTempRect(var4);
      releaseTempRect(var5);
   }

   private void layoutChildWithAnchor(View var1, View var2, int var3) {
      Rect var4 = acquireTempRect();
      Rect var5 = acquireTempRect();

      try {
         this.getDescendantRect(var2, var4);
         this.getDesiredAnchoredChildRect(var1, var3, var4, var5);
         var1.layout(var5.left, var5.top, var5.right, var5.bottom);
      } finally {
         releaseTempRect(var4);
         releaseTempRect(var5);
      }

   }

   private void layoutChildWithKeyline(View var1, int var2, int var3) {
      CoordinatorLayout.LayoutParams var10 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      int var4 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(var10.gravity), var3);
      int var9 = var4 & 7;
      int var8 = var4 & 112;
      int var7 = this.getWidth();
      int var6 = this.getHeight();
      var4 = var1.getMeasuredWidth();
      int var5 = var1.getMeasuredHeight();
      if (var3 == 1) {
         var2 = var7 - var2;
      }

      var2 = this.getKeyline(var2) - var4;
      var3 = 0;
      if (var9 != 1) {
         if (var9 == 5) {
            var2 += var4;
         }
      } else {
         var2 += var4 / 2;
      }

      if (var8 != 16) {
         if (var8 == 80) {
            var3 = 0 + var5;
         }
      } else {
         var3 = 0 + var5 / 2;
      }

      var2 = Math.max(this.getPaddingLeft() + var10.leftMargin, Math.min(var2, var7 - this.getPaddingRight() - var4 - var10.rightMargin));
      var3 = Math.max(this.getPaddingTop() + var10.topMargin, Math.min(var3, var6 - this.getPaddingBottom() - var5 - var10.bottomMargin));
      var1.layout(var2, var3, var2 + var4, var3 + var5);
   }

   private void offsetChildByInset(View var1, Rect var2, int var3) {
      if (ViewCompat.isLaidOut(var1)) {
         if (var1.getWidth() > 0) {
            if (var1.getHeight() > 0) {
               CoordinatorLayout.LayoutParams var9 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
               CoordinatorLayout.Behavior var10 = var9.getBehavior();
               Rect var7 = acquireTempRect();
               Rect var8 = acquireTempRect();
               var8.set(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
               if (var10 != null && var10.getInsetDodgeRect(this, var1, var7)) {
                  if (!var8.contains(var7)) {
                     StringBuilder var11 = new StringBuilder();
                     var11.append("Rect should be within the child's bounds. Rect:");
                     var11.append(var7.toShortString());
                     var11.append(" | Bounds:");
                     var11.append(var8.toShortString());
                     throw new IllegalArgumentException(var11.toString());
                  }
               } else {
                  var7.set(var8);
               }

               releaseTempRect(var8);
               if (var7.isEmpty()) {
                  releaseTempRect(var7);
               } else {
                  int var5 = GravityCompat.getAbsoluteGravity(var9.dodgeInsetEdges, var3);
                  boolean var4 = false;
                  boolean var12 = var4;
                  int var6;
                  if ((var5 & 48) == 48) {
                     var6 = var7.top - var9.topMargin - var9.mInsetOffsetY;
                     var12 = var4;
                     if (var6 < var2.top) {
                        this.setInsetOffsetY(var1, var2.top - var6);
                        var12 = true;
                     }
                  }

                  var4 = var12;
                  if ((var5 & 80) == 80) {
                     var6 = this.getHeight() - var7.bottom - var9.bottomMargin + var9.mInsetOffsetY;
                     var4 = var12;
                     if (var6 < var2.bottom) {
                        this.setInsetOffsetY(var1, var6 - var2.bottom);
                        var4 = true;
                     }
                  }

                  if (!var4) {
                     this.setInsetOffsetY(var1, 0);
                  }

                  var4 = false;
                  var12 = var4;
                  if ((var5 & 3) == 3) {
                     var6 = var7.left - var9.leftMargin - var9.mInsetOffsetX;
                     var12 = var4;
                     if (var6 < var2.left) {
                        this.setInsetOffsetX(var1, var2.left - var6);
                        var12 = true;
                     }
                  }

                  var4 = var12;
                  if ((var5 & 5) == 5) {
                     var5 = this.getWidth() - var7.right - var9.rightMargin + var9.mInsetOffsetX;
                     var4 = var12;
                     if (var5 < var2.right) {
                        this.setInsetOffsetX(var1, var5 - var2.right);
                        var4 = true;
                     }
                  }

                  if (!var4) {
                     this.setInsetOffsetX(var1, 0);
                  }

                  releaseTempRect(var7);
               }
            }
         }
      }
   }

   static CoordinatorLayout.Behavior parseBehavior(Context var0, AttributeSet var1, String var2) {
      if (TextUtils.isEmpty(var2)) {
         return null;
      } else {
         StringBuilder var3;
         if (var2.startsWith(".")) {
            var3 = new StringBuilder();
            var3.append(var0.getPackageName());
            var3.append(var2);
            var2 = var3.toString();
         } else if (var2.indexOf(46) < 0 && !TextUtils.isEmpty(WIDGET_PACKAGE_NAME)) {
            var3 = new StringBuilder();
            var3.append(WIDGET_PACKAGE_NAME);
            var3.append('.');
            var3.append(var2);
            var2 = var3.toString();
         }

         Exception var10000;
         label57: {
            Map var4;
            boolean var10001;
            try {
               var4 = (Map)sConstructors.get();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label57;
            }

            Object var14 = var4;
            if (var4 == null) {
               try {
                  var14 = new HashMap();
                  sConstructors.set(var14);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label57;
               }
            }

            Constructor var5;
            try {
               var5 = (Constructor)((Map)var14).get(var2);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label57;
            }

            Constructor var15 = var5;
            if (var5 == null) {
               try {
                  var15 = Class.forName(var2, false, var0.getClassLoader()).getConstructor(CONSTRUCTOR_PARAMS);
                  var15.setAccessible(true);
                  ((Map)var14).put(var2, var15);
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label57;
               }
            }

            try {
               CoordinatorLayout.Behavior var12 = (CoordinatorLayout.Behavior)var15.newInstance(var0, var1);
               return var12;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var11 = var10000;
         StringBuilder var13 = new StringBuilder();
         var13.append("Could not inflate Behavior subclass ");
         var13.append(var2);
         throw new RuntimeException(var13.toString(), var11);
      }
   }

   private boolean performIntercept(MotionEvent var1, int var2) {
      boolean var8 = false;
      boolean var3 = false;
      MotionEvent var13 = null;
      int var6 = var1.getActionMasked();
      List var15 = this.mTempList1;
      this.getTopSortedChildren(var15);
      int var7 = var15.size();
      int var4 = 0;

      boolean var9;
      while(true) {
         var9 = var8;
         if (var4 >= var7) {
            break;
         }

         View var16 = (View)var15.get(var4);
         CoordinatorLayout.LayoutParams var14 = (CoordinatorLayout.LayoutParams)var16.getLayoutParams();
         CoordinatorLayout.Behavior var17 = var14.getBehavior();
         boolean var5 = true;
         boolean var10;
         MotionEvent var18;
         if ((var8 || var3) && var6 != 0) {
            var10 = var8;
            var5 = var3;
            var18 = var13;
            if (var17 != null) {
               var18 = var13;
               if (var13 == null) {
                  long var11 = SystemClock.uptimeMillis();
                  var18 = MotionEvent.obtain(var11, var11, 3, 0.0F, 0.0F, 0);
               }

               if (var2 != 0) {
                  if (var2 == 1) {
                     var17.onTouchEvent(this, var16, var18);
                  }
               } else {
                  var17.onInterceptTouchEvent(this, var16, var18);
               }

               var10 = var8;
               var5 = var3;
            }
         } else {
            var9 = var8;
            if (!var8) {
               var9 = var8;
               if (var17 != null) {
                  if (var2 != 0) {
                     if (var2 == 1) {
                        var8 = var17.onTouchEvent(this, var16, var1);
                     }
                  } else {
                     var8 = var17.onInterceptTouchEvent(this, var16, var1);
                  }

                  var9 = var8;
                  if (var8) {
                     this.mBehaviorTouchView = var16;
                     var9 = var8;
                  }
               }
            }

            var10 = var14.didBlockInteraction();
            var8 = var14.isBlockingInteractionBelow(this, var16);
            if (var8 && !var10) {
               var3 = var5;
            } else {
               var3 = false;
            }

            var10 = var9;
            var5 = var3;
            var18 = var13;
            if (var8) {
               var10 = var9;
               var5 = var3;
               var18 = var13;
               if (!var3) {
                  break;
               }
            }
         }

         ++var4;
         var8 = var10;
         var3 = var5;
         var13 = var18;
      }

      var15.clear();
      return var9;
   }

   private void prepareChildren() {
      this.mDependencySortedChildren.clear();
      this.mChildDag.clear();
      int var1 = 0;

      for(int var3 = this.getChildCount(); var1 < var3; ++var1) {
         View var4 = this.getChildAt(var1);
         CoordinatorLayout.LayoutParams var5 = this.getResolvedLayoutParams(var4);
         var5.findAnchorView(this, var4);
         this.mChildDag.addNode(var4);

         for(int var2 = 0; var2 < var3; ++var2) {
            if (var2 != var1) {
               View var6 = this.getChildAt(var2);
               if (var5.dependsOn(this, var4, var6)) {
                  if (!this.mChildDag.contains(var6)) {
                     this.mChildDag.addNode(var6);
                  }

                  this.mChildDag.addEdge(var6, var4);
               }
            }
         }
      }

      this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
      Collections.reverse(this.mDependencySortedChildren);
   }

   private static void releaseTempRect(Rect var0) {
      var0.setEmpty();
      sRectPool.release(var0);
   }

   private void resetTouchBehaviors(boolean var1) {
      int var3 = this.getChildCount();

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         View var6 = this.getChildAt(var2);
         CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)var6.getLayoutParams()).getBehavior();
         if (var7 != null) {
            long var4 = SystemClock.uptimeMillis();
            MotionEvent var8 = MotionEvent.obtain(var4, var4, 3, 0.0F, 0.0F, 0);
            if (var1) {
               var7.onInterceptTouchEvent(this, var6, var8);
            } else {
               var7.onTouchEvent(this, var6, var8);
            }

            var8.recycle();
         }
      }

      for(var2 = 0; var2 < var3; ++var2) {
         ((CoordinatorLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).resetTouchBehaviorTracking();
      }

      this.mBehaviorTouchView = null;
      this.mDisallowInterceptReset = false;
   }

   private static int resolveAnchoredChildGravity(int var0) {
      return var0 == 0 ? 17 : var0;
   }

   private static int resolveGravity(int var0) {
      int var1 = var0;
      if ((var0 & 7) == 0) {
         var1 = var0 | 8388611;
      }

      var0 = var1;
      if ((var1 & 112) == 0) {
         var0 = var1 | 48;
      }

      return var0;
   }

   private static int resolveKeylineGravity(int var0) {
      return var0 == 0 ? 8388661 : var0;
   }

   private void setInsetOffsetX(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.mInsetOffsetX != var2) {
         ViewCompat.offsetLeftAndRight(var1, var2 - var3.mInsetOffsetX);
         var3.mInsetOffsetX = var2;
      }

   }

   private void setInsetOffsetY(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.mInsetOffsetY != var2) {
         ViewCompat.offsetTopAndBottom(var1, var2 - var3.mInsetOffsetY);
         var3.mInsetOffsetY = var2;
      }

   }

   private void setupForInsets() {
      if (VERSION.SDK_INT >= 21) {
         if (ViewCompat.getFitsSystemWindows(this)) {
            if (this.mApplyWindowInsetsListener == null) {
               this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     return CoordinatorLayout.this.setWindowInsets(var2);
                  }
               };
            }

            ViewCompat.setOnApplyWindowInsetsListener(this, this.mApplyWindowInsetsListener);
            this.setSystemUiVisibility(1280);
         } else {
            ViewCompat.setOnApplyWindowInsetsListener(this, (OnApplyWindowInsetsListener)null);
         }
      }
   }

   void addPreDrawListener() {
      if (this.mIsAttachedToWindow) {
         if (this.mOnPreDrawListener == null) {
            this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
         }

         this.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      }

      this.mNeedsPreDrawListener = true;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof CoordinatorLayout.LayoutParams && super.checkLayoutParams(var1);
   }

   public void dispatchDependentViewsChanged(View var1) {
      List var3 = this.mChildDag.getIncomingEdges(var1);
      if (var3 != null && !var3.isEmpty()) {
         for(int var2 = 0; var2 < var3.size(); ++var2) {
            View var4 = (View)var3.get(var2);
            CoordinatorLayout.Behavior var5 = ((CoordinatorLayout.LayoutParams)var4.getLayoutParams()).getBehavior();
            if (var5 != null) {
               var5.onDependentViewChanged(this, var4, var1);
            }
         }
      }

   }

   public boolean doViewsOverlap(View var1, View var2) {
      int var3 = var1.getVisibility();
      boolean var6 = false;
      if (var3 == 0 && var2.getVisibility() == 0) {
         Rect var7 = acquireTempRect();
         boolean var5;
         if (var1.getParent() != this) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.getChildRect(var1, var5, var7);
         Rect var20 = acquireTempRect();
         if (var2.getParent() != this) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.getChildRect(var2, var5, var20);
         var5 = var6;

         label221: {
            int var4;
            label220: {
               Throwable var10000;
               label228: {
                  boolean var10001;
                  try {
                     if (var7.left > var20.right) {
                        break label221;
                     }
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label228;
                  }

                  var5 = var6;

                  try {
                     if (var7.top > var20.bottom) {
                        break label221;
                     }
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label228;
                  }

                  var5 = var6;

                  label211:
                  try {
                     if (var7.right < var20.left) {
                        break label221;
                     }

                     var3 = var7.bottom;
                     var4 = var20.top;
                     break label220;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label211;
                  }
               }

               Throwable var21 = var10000;
               releaseTempRect(var7);
               releaseTempRect(var20);
               throw var21;
            }

            var5 = var6;
            if (var3 >= var4) {
               var5 = true;
            }
         }

         releaseTempRect(var7);
         releaseTempRect(var20);
         return var5;
      } else {
         return false;
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      CoordinatorLayout.LayoutParams var7 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
      if (var7.mBehavior != null) {
         float var5 = var7.mBehavior.getScrimOpacity(this, var2);
         if (var5 > 0.0F) {
            if (this.mScrimPaint == null) {
               this.mScrimPaint = new Paint();
            }

            this.mScrimPaint.setColor(var7.mBehavior.getScrimColor(this, var2));
            this.mScrimPaint.setAlpha(clamp(Math.round(255.0F * var5), 0, 255));
            int var6 = var1.save();
            if (var2.isOpaque()) {
               var1.clipRect((float)var2.getLeft(), (float)var2.getTop(), (float)var2.getRight(), (float)var2.getBottom(), Op.DIFFERENCE);
            }

            var1.drawRect((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()), this.mScrimPaint);
            var1.restoreToCount(var6);
         }
      }

      return super.drawChild(var1, var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var3 = this.getDrawableState();
      boolean var2 = false;
      Drawable var4 = this.mStatusBarBackground;
      boolean var1 = var2;
      if (var4 != null) {
         var1 = var2;
         if (var4.isStateful()) {
            var1 = false | var4.setState(var3);
         }
      }

      if (var1) {
         this.invalidate();
      }

   }

   void ensurePreDrawListener() {
      boolean var4 = false;
      int var2 = this.getChildCount();
      int var1 = 0;

      boolean var3;
      while(true) {
         var3 = var4;
         if (var1 >= var2) {
            break;
         }

         if (this.hasDependencies(this.getChildAt(var1))) {
            var3 = true;
            break;
         }

         ++var1;
      }

      if (var3 != this.mNeedsPreDrawListener) {
         if (var3) {
            this.addPreDrawListener();
            return;
         }

         this.removePreDrawListener();
      }

   }

   protected CoordinatorLayout.LayoutParams generateDefaultLayoutParams() {
      return new CoordinatorLayout.LayoutParams(-2, -2);
   }

   public CoordinatorLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new CoordinatorLayout.LayoutParams(this.getContext(), var1);
   }

   protected CoordinatorLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof CoordinatorLayout.LayoutParams) {
         return new CoordinatorLayout.LayoutParams((CoordinatorLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new CoordinatorLayout.LayoutParams((MarginLayoutParams)var1) : new CoordinatorLayout.LayoutParams(var1);
      }
   }

   void getChildRect(View var1, boolean var2, Rect var3) {
      if (!var1.isLayoutRequested() && var1.getVisibility() != 8) {
         if (var2) {
            this.getDescendantRect(var1, var3);
         } else {
            var3.set(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
         }
      } else {
         var3.setEmpty();
      }
   }

   public List getDependencies(View var1) {
      List var2 = this.mChildDag.getOutgoingEdges(var1);
      this.mTempDependenciesList.clear();
      if (var2 != null) {
         this.mTempDependenciesList.addAll(var2);
      }

      return this.mTempDependenciesList;
   }

   final List getDependencySortedChildren() {
      this.prepareChildren();
      return Collections.unmodifiableList(this.mDependencySortedChildren);
   }

   public List getDependents(View var1) {
      List var2 = this.mChildDag.getIncomingEdges(var1);
      this.mTempDependenciesList.clear();
      if (var2 != null) {
         this.mTempDependenciesList.addAll(var2);
      }

      return this.mTempDependenciesList;
   }

   void getDescendantRect(View var1, Rect var2) {
      ViewGroupUtils.getDescendantRect(this, var1, var2);
   }

   void getDesiredAnchoredChildRect(View var1, int var2, Rect var3, Rect var4) {
      CoordinatorLayout.LayoutParams var7 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      int var5 = var1.getMeasuredWidth();
      int var6 = var1.getMeasuredHeight();
      this.getDesiredAnchoredChildRectWithoutConstraints(var1, var2, var3, var4, var7, var5, var6);
      this.constrainChildRect(var7, var4, var5, var6);
   }

   void getLastChildRect(View var1, Rect var2) {
      var2.set(((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).getLastChildRect());
   }

   public final WindowInsetsCompat getLastWindowInsets() {
      return this.mLastInsets;
   }

   public int getNestedScrollAxes() {
      return this.mNestedScrollingParentHelper.getNestedScrollAxes();
   }

   CoordinatorLayout.LayoutParams getResolvedLayoutParams(View var1) {
      CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (!var5.mBehaviorResolved) {
         if (var1 instanceof CoordinatorLayout.AttachedBehavior) {
            CoordinatorLayout.Behavior var8 = ((CoordinatorLayout.AttachedBehavior)var1).getBehavior();
            if (var8 == null) {
               Log.e("CoordinatorLayout", "Attached behavior class is null");
            }

            var5.setBehavior(var8);
            var5.mBehaviorResolved = true;
            return var5;
         }

         Class var3 = var1.getClass();
         CoordinatorLayout.DefaultBehavior var7 = null;

         CoordinatorLayout.DefaultBehavior var2;
         while(true) {
            var2 = var7;
            if (var3 == null) {
               break;
            }

            CoordinatorLayout.DefaultBehavior var4 = (CoordinatorLayout.DefaultBehavior)var3.getAnnotation(CoordinatorLayout.DefaultBehavior.class);
            var7 = var4;
            var2 = var4;
            if (var4 != null) {
               break;
            }

            var3 = var3.getSuperclass();
         }

         if (var2 != null) {
            try {
               var5.setBehavior((CoordinatorLayout.Behavior)var2.value().getDeclaredConstructor().newInstance());
            } catch (Exception var6) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Default behavior class ");
               var9.append(var2.value().getName());
               var9.append(" could not be instantiated. Did you forget a default constructor?");
               Log.e("CoordinatorLayout", var9.toString(), var6);
            }
         }

         var5.mBehaviorResolved = true;
      }

      return var5;
   }

   public Drawable getStatusBarBackground() {
      return this.mStatusBarBackground;
   }

   protected int getSuggestedMinimumHeight() {
      return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
   }

   protected int getSuggestedMinimumWidth() {
      return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
   }

   public boolean isPointInChildBounds(View var1, int var2, int var3) {
      Rect var5 = acquireTempRect();
      this.getDescendantRect(var1, var5);

      boolean var4;
      try {
         var4 = var5.contains(var2, var3);
      } finally {
         releaseTempRect(var5);
      }

      return var4;
   }

   void offsetChildToAnchor(View var1, int var2) {
      CoordinatorLayout.LayoutParams var6 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var6.mAnchorView != null) {
         int var4;
         int var5;
         Rect var7;
         Rect var8;
         Rect var9;
         boolean var11;
         label27: {
            var7 = acquireTempRect();
            var8 = acquireTempRect();
            var9 = acquireTempRect();
            this.getDescendantRect(var6.mAnchorView, var7);
            boolean var3 = false;
            this.getChildRect(var1, false, var8);
            var4 = var1.getMeasuredWidth();
            var5 = var1.getMeasuredHeight();
            this.getDesiredAnchoredChildRectWithoutConstraints(var1, var2, var7, var9, var6, var4, var5);
            if (var9.left == var8.left) {
               var11 = var3;
               if (var9.top == var8.top) {
                  break label27;
               }
            }

            var11 = true;
         }

         this.constrainChildRect(var6, var9, var4, var5);
         int var12 = var9.left - var8.left;
         var4 = var9.top - var8.top;
         if (var12 != 0) {
            ViewCompat.offsetLeftAndRight(var1, var12);
         }

         if (var4 != 0) {
            ViewCompat.offsetTopAndBottom(var1, var4);
         }

         if (var11) {
            CoordinatorLayout.Behavior var10 = var6.getBehavior();
            if (var10 != null) {
               var10.onDependentViewChanged(this, var1, var6.mAnchorView);
            }
         }

         releaseTempRect(var7);
         releaseTempRect(var8);
         releaseTempRect(var9);
      }

   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.resetTouchBehaviors(false);
      if (this.mNeedsPreDrawListener) {
         if (this.mOnPreDrawListener == null) {
            this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
         }

         this.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      }

      if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows(this)) {
         ViewCompat.requestApplyInsets(this);
      }

      this.mIsAttachedToWindow = true;
   }

   final void onChildViewsChanged(int var1) {
      int var4 = ViewCompat.getLayoutDirection(this);
      int var5 = this.mDependencySortedChildren.size();
      Rect var8 = acquireTempRect();
      Rect var9 = acquireTempRect();
      Rect var10 = acquireTempRect();

      for(int var2 = 0; var2 < var5; ++var2) {
         View var11 = (View)this.mDependencySortedChildren.get(var2);
         CoordinatorLayout.LayoutParams var12 = (CoordinatorLayout.LayoutParams)var11.getLayoutParams();
         if (var1 != 0 || var11.getVisibility() != 8) {
            int var3;
            for(var3 = 0; var3 < var2; ++var3) {
               View var13 = (View)this.mDependencySortedChildren.get(var3);
               if (var12.mAnchorDirectChild == var13) {
                  this.offsetChildToAnchor(var11, var4);
               }
            }

            this.getChildRect(var11, true, var9);
            if (var12.insetEdge != 0 && !var9.isEmpty()) {
               var3 = GravityCompat.getAbsoluteGravity(var12.insetEdge, var4);
               int var6 = var3 & 112;
               if (var6 != 48) {
                  if (var6 == 80) {
                     var8.bottom = Math.max(var8.bottom, this.getHeight() - var9.top);
                  }
               } else {
                  var8.top = Math.max(var8.top, var9.bottom);
               }

               var3 &= 7;
               if (var3 != 3) {
                  if (var3 == 5) {
                     var8.right = Math.max(var8.right, this.getWidth() - var9.left);
                  }
               } else {
                  var8.left = Math.max(var8.left, var9.right);
               }
            }

            if (var12.dodgeInsetEdges != 0 && var11.getVisibility() == 0) {
               this.offsetChildByInset(var11, var8, var4);
            }

            if (var1 != 2) {
               this.getLastChildRect(var11, var10);
               if (var10.equals(var9)) {
                  continue;
               }

               this.recordLastChildRect(var11, var9);
            }

            for(var3 = var2 + 1; var3 < var5; ++var3) {
               View var15 = (View)this.mDependencySortedChildren.get(var3);
               CoordinatorLayout.LayoutParams var16 = (CoordinatorLayout.LayoutParams)var15.getLayoutParams();
               CoordinatorLayout.Behavior var14 = var16.getBehavior();
               if (var14 != null && var14.layoutDependsOn(this, var15, var11)) {
                  if (var1 == 0 && var16.getChangedAfterNestedScroll()) {
                     var16.resetChangedAfterNestedScroll();
                  } else {
                     boolean var7;
                     if (var1 != 2) {
                        var7 = var14.onDependentViewChanged(this, var15, var11);
                     } else {
                        var14.onDependentViewRemoved(this, var15, var11);
                        var7 = true;
                     }

                     if (var1 == 1) {
                        var16.setChangedAfterNestedScroll(var7);
                     }
                  }
               }
            }
         }
      }

      releaseTempRect(var8);
      releaseTempRect(var9);
      releaseTempRect(var10);
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.resetTouchBehaviors(false);
      if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
         this.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      }

      View var1 = this.mNestedScrollingTarget;
      if (var1 != null) {
         this.onStopNestedScroll(var1);
      }

      this.mIsAttachedToWindow = false;
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
         WindowInsetsCompat var3 = this.mLastInsets;
         int var2;
         if (var3 != null) {
            var2 = var3.getSystemWindowInsetTop();
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
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.resetTouchBehaviors(true);
      }

      boolean var3 = this.performIntercept(var1, 0);
      if (var2 == 1 || var2 == 3) {
         this.resetTouchBehaviors(true);
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = ViewCompat.getLayoutDirection(this);
      var4 = this.mDependencySortedChildren.size();

      for(var2 = 0; var2 < var4; ++var2) {
         View var6 = (View)this.mDependencySortedChildren.get(var2);
         if (var6.getVisibility() != 8) {
            CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)var6.getLayoutParams()).getBehavior();
            if (var7 == null || !var7.onLayoutChild(this, var6, var3)) {
               this.onLayoutChild(var6, var3);
            }
         }
      }

   }

   public void onLayoutChild(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (!var3.checkAnchorChanged()) {
         if (var3.mAnchorView != null) {
            this.layoutChildWithAnchor(var1, var3.mAnchorView, var2);
         } else if (var3.keyline >= 0) {
            this.layoutChildWithKeyline(var1, var3.keyline, var2);
         } else {
            this.layoutChild(var1, var2);
         }
      } else {
         throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
      }
   }

   protected void onMeasure(int var1, int var2) {
      this.prepareChildren();
      this.ensurePreDrawListener();
      int var14 = this.getPaddingLeft();
      int var15 = this.getPaddingTop();
      int var16 = this.getPaddingRight();
      int var17 = this.getPaddingBottom();
      int var18 = ViewCompat.getLayoutDirection(this);
      boolean var8 = true;
      boolean var7;
      if (var18 == 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var19 = MeasureSpec.getMode(var1);
      int var20 = MeasureSpec.getSize(var1);
      int var9 = MeasureSpec.getMode(var2);
      int var21 = MeasureSpec.getSize(var2);
      int var6 = this.getSuggestedMinimumWidth();
      int var5 = this.getSuggestedMinimumHeight();
      if (this.mLastInsets == null || !ViewCompat.getFitsSystemWindows(this)) {
         var8 = false;
      }

      int var10 = this.mDependencySortedChildren.size();
      int var11 = 0;

      int var4;
      for(var4 = 0; var11 < var10; ++var11) {
         View var24 = (View)this.mDependencySortedChildren.get(var11);
         if (var24.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var25 = (CoordinatorLayout.LayoutParams)var24.getLayoutParams();
            int var3 = 0;
            int var12;
            int var13;
            if (var25.keyline >= 0 && var19 != 0) {
               var12 = this.getKeyline(var25.keyline);
               var13 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(var25.gravity), var18) & 7;
               if ((var13 != 3 || var7) && (var13 != 5 || !var7)) {
                  if (var13 == 5 && !var7 || var13 == 3 && var7) {
                     var3 = Math.max(0, var12 - var14);
                  }
               } else {
                  var3 = Math.max(0, var20 - var16 - var12);
               }
            }

            var13 = var6;
            var12 = var5;
            var6 = var4;
            if (var8 && !ViewCompat.getFitsSystemWindows(var24)) {
               var4 = this.mLastInsets.getSystemWindowInsetLeft();
               int var23 = this.mLastInsets.getSystemWindowInsetRight();
               var5 = this.mLastInsets.getSystemWindowInsetTop();
               int var22 = this.mLastInsets.getSystemWindowInsetBottom();
               var4 = MeasureSpec.makeMeasureSpec(var20 - (var4 + var23), var19);
               var5 = MeasureSpec.makeMeasureSpec(var21 - (var5 + var22), var9);
            } else {
               var4 = var1;
               var5 = var2;
            }

            CoordinatorLayout.Behavior var26 = var25.getBehavior();
            if (var26 == null || !var26.onMeasureChild(this, var24, var4, var3, var5, 0)) {
               this.onMeasureChild(var24, var4, var3, var5, 0);
            }

            var3 = Math.max(var13, var14 + var16 + var24.getMeasuredWidth() + var25.leftMargin + var25.rightMargin);
            var5 = Math.max(var12, var15 + var17 + var24.getMeasuredHeight() + var25.topMargin + var25.bottomMargin);
            var4 = View.combineMeasuredStates(var6, var24.getMeasuredState());
            var6 = var3;
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var6, var1, -16777216 & var4), View.resolveSizeAndState(var5, var2, var4 << 16));
   }

   public void onMeasureChild(View var1, int var2, int var3, int var4, int var5) {
      this.measureChildWithMargins(var1, var2, var3, var4, var5);
   }

   public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
      int var6 = this.getChildCount();
      boolean var7 = false;

      boolean var8;
      for(int var5 = 0; var5 < var6; var7 = var8) {
         View var9 = this.getChildAt(var5);
         if (var9.getVisibility() == 8) {
            var8 = var7;
         } else {
            CoordinatorLayout.LayoutParams var10 = (CoordinatorLayout.LayoutParams)var9.getLayoutParams();
            if (!var10.isNestedScrollAccepted(0)) {
               var8 = var7;
            } else {
               CoordinatorLayout.Behavior var11 = var10.getBehavior();
               var8 = var7;
               if (var11 != null) {
                  var8 = var11.onNestedFling(this, var9, var1, var2, var3, var4) | var7;
               }
            }
         }

         ++var5;
      }

      if (var7) {
         this.onChildViewsChanged(1);
      }

      return var7;
   }

   public boolean onNestedPreFling(View var1, float var2, float var3) {
      boolean var6 = false;
      int var5 = this.getChildCount();

      boolean var7;
      for(int var4 = 0; var4 < var5; var6 = var7) {
         View var8 = this.getChildAt(var4);
         if (var8.getVisibility() == 8) {
            var7 = var6;
         } else {
            CoordinatorLayout.LayoutParams var9 = (CoordinatorLayout.LayoutParams)var8.getLayoutParams();
            if (!var9.isNestedScrollAccepted(0)) {
               var7 = var6;
            } else {
               CoordinatorLayout.Behavior var10 = var9.getBehavior();
               var7 = var6;
               if (var10 != null) {
                  var7 = var6 | var10.onNestedPreFling(this, var8, var1, var2, var3);
               }
            }
         }

         ++var4;
      }

      return var6;
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
      this.onNestedPreScroll(var1, var2, var3, var4, 0);
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4, int var5) {
      int var11 = this.getChildCount();
      int var9 = 0;
      int var7 = 0;
      boolean var6 = false;

      for(int var8 = 0; var8 < var11; ++var8) {
         View var12 = this.getChildAt(var8);
         if (var12.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var13 = (CoordinatorLayout.LayoutParams)var12.getLayoutParams();
            if (var13.isNestedScrollAccepted(var5)) {
               CoordinatorLayout.Behavior var17 = var13.getBehavior();
               if (var17 != null) {
                  int[] var14 = this.mBehaviorConsumed;
                  var14[0] = 0;
                  var14[1] = 0;
                  var17.onNestedPreScroll(this, var12, var1, var2, var3, var14, var5);
                  int[] var16 = this.mBehaviorConsumed;
                  int var15;
                  if (var2 > 0) {
                     var15 = Math.max(var9, var16[0]);
                  } else {
                     var15 = Math.min(var9, var16[0]);
                  }

                  var16 = this.mBehaviorConsumed;
                  if (var3 > 0) {
                     var7 = Math.max(var7, var16[1]);
                  } else {
                     var7 = Math.min(var7, var16[1]);
                  }

                  boolean var10 = true;
                  var9 = var15;
                  var6 = var10;
               }
            }
         }
      }

      var4[0] = var9;
      var4[1] = var7;
      if (var6) {
         this.onChildViewsChanged(1);
      }

   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
      this.onNestedScroll(var1, var2, var3, var4, var5, 0);
   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5, int var6) {
      this.onNestedScroll(var1, var2, var3, var4, var5, 0, this.mNestedScrollingV2ConsumedCompat);
   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      int var11 = this.getChildCount();
      boolean var8 = false;
      int var12 = 0;
      int var9 = 0;

      for(int var10 = 0; var10 < var11; ++var10) {
         View var13 = this.getChildAt(var10);
         if (var13.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var14 = (CoordinatorLayout.LayoutParams)var13.getLayoutParams();
            if (var14.isNestedScrollAccepted(var6)) {
               CoordinatorLayout.Behavior var18 = var14.getBehavior();
               if (var18 != null) {
                  int[] var15 = this.mBehaviorConsumed;
                  var15[0] = 0;
                  var15[1] = 0;
                  var18.onNestedScroll(this, var13, var1, var2, var3, var4, var5, var6, var15);
                  int[] var17 = this.mBehaviorConsumed;
                  int var16;
                  if (var4 > 0) {
                     var16 = Math.max(var12, var17[0]);
                  } else {
                     var16 = Math.min(var12, var17[0]);
                  }

                  var17 = this.mBehaviorConsumed;
                  if (var5 > 0) {
                     var9 = Math.max(var9, var17[1]);
                  } else {
                     var9 = Math.min(var9, var17[1]);
                  }

                  var12 = var16;
                  var8 = true;
               }
            }
         }
      }

      var7[0] += var12;
      var7[1] += var9;
      if (var8) {
         this.onChildViewsChanged(1);
      }

   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.onNestedScrollAccepted(var1, var2, var3, 0);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3, int var4) {
      this.mNestedScrollingParentHelper.onNestedScrollAccepted(var1, var2, var3, var4);
      this.mNestedScrollingTarget = var2;
      int var6 = this.getChildCount();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var7 = this.getChildAt(var5);
         CoordinatorLayout.LayoutParams var8 = (CoordinatorLayout.LayoutParams)var7.getLayoutParams();
         if (var8.isNestedScrollAccepted(var4)) {
            CoordinatorLayout.Behavior var9 = var8.getBehavior();
            if (var9 != null) {
               var9.onNestedScrollAccepted(this, var7, var1, var2, var3, var4);
            }
         }
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof CoordinatorLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         CoordinatorLayout.SavedState var8 = (CoordinatorLayout.SavedState)var1;
         super.onRestoreInstanceState(var8.getSuperState());
         SparseArray var9 = var8.behaviorStates;
         int var2 = 0;

         for(int var3 = this.getChildCount(); var2 < var3; ++var2) {
            View var5 = this.getChildAt(var2);
            int var4 = var5.getId();
            CoordinatorLayout.Behavior var6 = this.getResolvedLayoutParams(var5).getBehavior();
            if (var4 != -1 && var6 != null) {
               Parcelable var7 = (Parcelable)var9.get(var4);
               if (var7 != null) {
                  var6.onRestoreInstanceState(this, var5, var7);
               }
            }
         }

      }
   }

   protected Parcelable onSaveInstanceState() {
      CoordinatorLayout.SavedState var4 = new CoordinatorLayout.SavedState(super.onSaveInstanceState());
      SparseArray var5 = new SparseArray();
      int var1 = 0;

      for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
         View var6 = this.getChildAt(var1);
         int var3 = var6.getId();
         CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)var6.getLayoutParams()).getBehavior();
         if (var3 != -1 && var7 != null) {
            Parcelable var8 = var7.onSaveInstanceState(this, var6);
            if (var8 != null) {
               var5.append(var3, var8);
            }
         }
      }

      var4.behaviorStates = var5;
      return var4;
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3) {
      return this.onStartNestedScroll(var1, var2, var3, 0);
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3, int var4) {
      int var6 = this.getChildCount();
      boolean var7 = false;

      for(int var5 = 0; var5 < var6; ++var5) {
         View var9 = this.getChildAt(var5);
         if (var9.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var10 = (CoordinatorLayout.LayoutParams)var9.getLayoutParams();
            CoordinatorLayout.Behavior var11 = var10.getBehavior();
            if (var11 != null) {
               boolean var8 = var11.onStartNestedScroll(this, var9, var1, var2, var3, var4);
               var10.setNestedScrollAccepted(var4, var8);
               var7 |= var8;
            } else {
               var10.setNestedScrollAccepted(var4, false);
            }
         }
      }

      return var7;
   }

   public void onStopNestedScroll(View var1) {
      this.onStopNestedScroll(var1, 0);
   }

   public void onStopNestedScroll(View var1, int var2) {
      this.mNestedScrollingParentHelper.onStopNestedScroll(var1, var2);
      int var4 = this.getChildCount();

      for(int var3 = 0; var3 < var4; ++var3) {
         View var5 = this.getChildAt(var3);
         CoordinatorLayout.LayoutParams var6 = (CoordinatorLayout.LayoutParams)var5.getLayoutParams();
         if (var6.isNestedScrollAccepted(var2)) {
            CoordinatorLayout.Behavior var7 = var6.getBehavior();
            if (var7 != null) {
               var7.onStopNestedScroll(this, var5, var1, var2);
            }

            var6.resetNestedScroll(var2);
            var6.resetChangedAfterNestedScroll();
         }
      }

      this.mNestedScrollingTarget = null;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2;
      boolean var3;
      boolean var4;
      boolean var5;
      Object var10;
      Object var11;
      label36: {
         boolean var6 = false;
         var4 = false;
         var11 = null;
         var10 = null;
         var2 = var1.getActionMasked();
         if (this.mBehaviorTouchView == null) {
            boolean var7 = this.performIntercept(var1, 1);
            var4 = var7;
            var3 = var6;
            var5 = var7;
            if (!var7) {
               break label36;
            }
         }

         CoordinatorLayout.Behavior var12 = ((CoordinatorLayout.LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
         var3 = var6;
         var5 = var4;
         if (var12 != null) {
            var3 = var12.onTouchEvent(this, this.mBehaviorTouchView, var1);
            var5 = var4;
         }
      }

      if (this.mBehaviorTouchView == null) {
         var4 = var3 | super.onTouchEvent(var1);
         var1 = (MotionEvent)var11;
      } else {
         var4 = var3;
         var1 = (MotionEvent)var11;
         if (var5) {
            var1 = (MotionEvent)var10;
            if (true) {
               long var8 = SystemClock.uptimeMillis();
               var1 = MotionEvent.obtain(var8, var8, 3, 0.0F, 0.0F, 0);
            }

            super.onTouchEvent(var1);
            var4 = var3;
         }
      }

      if (var1 != null) {
         var1.recycle();
      }

      if (var2 == 1 || var2 == 3) {
         this.resetTouchBehaviors(false);
      }

      return var4;
   }

   void recordLastChildRect(View var1, Rect var2) {
      ((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).setLastChildRect(var2);
   }

   void removePreDrawListener() {
      if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null) {
         this.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      }

      this.mNeedsPreDrawListener = false;
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      CoordinatorLayout.Behavior var4 = ((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).getBehavior();
      return var4 != null && var4.onRequestChildRectangleOnScreen(this, var1, var2, var3) ? true : super.requestChildRectangleOnScreen(var1, var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      super.requestDisallowInterceptTouchEvent(var1);
      if (var1 && !this.mDisallowInterceptReset) {
         this.resetTouchBehaviors(false);
         this.mDisallowInterceptReset = true;
      }

   }

   public void setFitsSystemWindows(boolean var1) {
      super.setFitsSystemWindows(var1);
      this.setupForInsets();
   }

   public void setOnHierarchyChangeListener(OnHierarchyChangeListener var1) {
      this.mOnHierarchyChangeListener = var1;
   }

   public void setStatusBarBackground(Drawable var1) {
      Drawable var4 = this.mStatusBarBackground;
      if (var4 != var1) {
         Drawable var3 = null;
         if (var4 != null) {
            var4.setCallback((Callback)null);
         }

         if (var1 != null) {
            var3 = var1.mutate();
         }

         this.mStatusBarBackground = var3;
         if (var3 != null) {
            if (var3.isStateful()) {
               this.mStatusBarBackground.setState(this.getDrawableState());
            }

            DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection(this));
            var1 = this.mStatusBarBackground;
            boolean var2;
            if (this.getVisibility() == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.setVisible(var2, false);
            this.mStatusBarBackground.setCallback(this);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setStatusBarBackgroundColor(int var1) {
      this.setStatusBarBackground(new ColorDrawable(var1));
   }

   public void setStatusBarBackgroundResource(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setStatusBarBackground(var2);
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Drawable var3 = this.mStatusBarBackground;
      if (var3 != null && var3.isVisible() != var2) {
         this.mStatusBarBackground.setVisible(var2, false);
      }

   }

   final WindowInsetsCompat setWindowInsets(WindowInsetsCompat var1) {
      WindowInsetsCompat var4 = var1;
      if (!ObjectsCompat.equals(this.mLastInsets, var1)) {
         this.mLastInsets = var1;
         boolean var3 = true;
         boolean var2;
         if (var1 != null && var1.getSystemWindowInsetTop() > 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mDrawStatusBarBackground = var2;
         if (!var2 && this.getBackground() == null) {
            var2 = var3;
         } else {
            var2 = false;
         }

         this.setWillNotDraw(var2);
         var4 = this.dispatchApplyWindowInsetsToBehaviors(var1);
         this.requestLayout();
      }

      return var4;
   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.mStatusBarBackground;
   }

   public interface AttachedBehavior {
      CoordinatorLayout.Behavior getBehavior();
   }

   public abstract static class Behavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
      }

      public static Object getTag(View var0) {
         return ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).mBehaviorTag;
      }

      public static void setTag(View var0, Object var1) {
         ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).mBehaviorTag = var1;
      }

      public boolean blocksInteractionBelow(CoordinatorLayout var1, View var2) {
         return this.getScrimOpacity(var1, var2) > 0.0F;
      }

      public boolean getInsetDodgeRect(CoordinatorLayout var1, View var2, Rect var3) {
         return false;
      }

      public int getScrimColor(CoordinatorLayout var1, View var2) {
         return -16777216;
      }

      public float getScrimOpacity(CoordinatorLayout var1, View var2) {
         return 0.0F;
      }

      public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
         return false;
      }

      public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout var1, View var2, WindowInsetsCompat var3) {
         return var3;
      }

      public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, View var2, View var3) {
         return false;
      }

      public void onDependentViewRemoved(CoordinatorLayout var1, View var2, View var3) {
      }

      public void onDetachedFromLayoutParams() {
      }

      public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
         return false;
      }

      public boolean onMeasureChild(CoordinatorLayout var1, View var2, int var3, int var4, int var5, int var6) {
         return false;
      }

      public boolean onNestedFling(CoordinatorLayout var1, View var2, View var3, float var4, float var5, boolean var6) {
         return false;
      }

      public boolean onNestedPreFling(CoordinatorLayout var1, View var2, View var3, float var4, float var5) {
         return false;
      }

      @Deprecated
      public void onNestedPreScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int[] var6) {
      }

      public void onNestedPreScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int[] var6, int var7) {
         if (var7 == 0) {
            this.onNestedPreScroll(var1, var2, var3, var4, var5, var6);
         }

      }

      @Deprecated
      public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7) {
      }

      @Deprecated
      public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7, int var8) {
         if (var8 == 0) {
            this.onNestedScroll(var1, var2, var3, var4, var5, var6, var7);
         }

      }

      public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7, int var8, int[] var9) {
         var9[0] += var6;
         var9[1] += var7;
         this.onNestedScroll(var1, var2, var3, var4, var5, var6, var7, var8);
      }

      @Deprecated
      public void onNestedScrollAccepted(CoordinatorLayout var1, View var2, View var3, View var4, int var5) {
      }

      public void onNestedScrollAccepted(CoordinatorLayout var1, View var2, View var3, View var4, int var5, int var6) {
         if (var6 == 0) {
            this.onNestedScrollAccepted(var1, var2, var3, var4, var5);
         }

      }

      public boolean onRequestChildRectangleOnScreen(CoordinatorLayout var1, View var2, Rect var3, boolean var4) {
         return false;
      }

      public void onRestoreInstanceState(CoordinatorLayout var1, View var2, Parcelable var3) {
      }

      public Parcelable onSaveInstanceState(CoordinatorLayout var1, View var2) {
         return BaseSavedState.EMPTY_STATE;
      }

      @Deprecated
      public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5) {
         return false;
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5, int var6) {
         return var6 == 0 ? this.onStartNestedScroll(var1, var2, var3, var4, var5) : false;
      }

      @Deprecated
      public void onStopNestedScroll(CoordinatorLayout var1, View var2, View var3) {
      }

      public void onStopNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4) {
         if (var4 == 0) {
            this.onStopNestedScroll(var1, var2, var3);
         }

      }

      public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         return false;
      }
   }

   @Deprecated
   @Retention(RetentionPolicy.RUNTIME)
   public @interface DefaultBehavior {
      Class value();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface DispatchChangeEvent {
   }

   private class HierarchyChangeListener implements OnHierarchyChangeListener {
      HierarchyChangeListener() {
      }

      public void onChildViewAdded(View var1, View var2) {
         if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
            CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(var1, var2);
         }

      }

      public void onChildViewRemoved(View var1, View var2) {
         CoordinatorLayout.this.onChildViewsChanged(2);
         if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
            CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(var1, var2);
         }

      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int anchorGravity = 0;
      public int dodgeInsetEdges = 0;
      public int gravity = 0;
      public int insetEdge = 0;
      public int keyline = -1;
      View mAnchorDirectChild;
      int mAnchorId = -1;
      View mAnchorView;
      CoordinatorLayout.Behavior mBehavior;
      boolean mBehaviorResolved = false;
      Object mBehaviorTag;
      private boolean mDidAcceptNestedScrollNonTouch;
      private boolean mDidAcceptNestedScrollTouch;
      private boolean mDidBlockInteraction;
      private boolean mDidChangeAfterNestedScroll;
      int mInsetOffsetX;
      int mInsetOffsetY;
      final Rect mLastChildRect = new Rect();

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var4 = var1.obtainStyledAttributes(var2, styleable.CoordinatorLayout_Layout);
         this.gravity = var4.getInteger(styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
         this.mAnchorId = var4.getResourceId(styleable.CoordinatorLayout_Layout_layout_anchor, -1);
         this.anchorGravity = var4.getInteger(styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
         this.keyline = var4.getInteger(styleable.CoordinatorLayout_Layout_layout_keyline, -1);
         this.insetEdge = var4.getInt(styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
         this.dodgeInsetEdges = var4.getInt(styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
         boolean var3 = var4.hasValue(styleable.CoordinatorLayout_Layout_layout_behavior);
         this.mBehaviorResolved = var3;
         if (var3) {
            this.mBehavior = CoordinatorLayout.parseBehavior(var1, var2, var4.getString(styleable.CoordinatorLayout_Layout_layout_behavior));
         }

         var4.recycle();
         CoordinatorLayout.Behavior var5 = this.mBehavior;
         if (var5 != null) {
            var5.onAttachedToLayoutParams(this);
         }

      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(CoordinatorLayout.LayoutParams var1) {
         super(var1);
      }

      private void resolveAnchorView(View var1, CoordinatorLayout var2) {
         View var3 = var2.findViewById(this.mAnchorId);
         this.mAnchorView = var3;
         if (var3 == null) {
            if (var2.isInEditMode()) {
               this.mAnchorDirectChild = null;
               this.mAnchorView = null;
            } else {
               StringBuilder var6 = new StringBuilder();
               var6.append("Could not find CoordinatorLayout descendant view with id ");
               var6.append(var2.getResources().getResourceName(this.mAnchorId));
               var6.append(" to anchor view ");
               var6.append(var1);
               throw new IllegalStateException(var6.toString());
            }
         } else if (var3 == var2) {
            if (var2.isInEditMode()) {
               this.mAnchorDirectChild = null;
               this.mAnchorView = null;
            } else {
               throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
            }
         } else {
            View var4 = this.mAnchorView;

            for(ViewParent var5 = var3.getParent(); var5 != var2 && var5 != null; var5 = var5.getParent()) {
               if (var5 == var1) {
                  if (var2.isInEditMode()) {
                     this.mAnchorDirectChild = null;
                     this.mAnchorView = null;
                     return;
                  }

                  throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
               }

               if (var5 instanceof View) {
                  var4 = (View)var5;
               }
            }

            this.mAnchorDirectChild = var4;
         }
      }

      private boolean shouldDodge(View var1, int var2) {
         int var3 = GravityCompat.getAbsoluteGravity(((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).insetEdge, var2);
         return var3 != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, var2) & var3) == var3;
      }

      private boolean verifyAnchorView(View var1, CoordinatorLayout var2) {
         if (this.mAnchorView.getId() != this.mAnchorId) {
            return false;
         } else {
            View var4 = this.mAnchorView;

            for(ViewParent var3 = this.mAnchorView.getParent(); var3 != var2; var3 = var3.getParent()) {
               if (var3 == null || var3 == var1) {
                  this.mAnchorDirectChild = null;
                  this.mAnchorView = null;
                  return false;
               }

               if (var3 instanceof View) {
                  var4 = (View)var3;
               }
            }

            this.mAnchorDirectChild = var4;
            return true;
         }
      }

      boolean checkAnchorChanged() {
         return this.mAnchorView == null && this.mAnchorId != -1;
      }

      boolean dependsOn(CoordinatorLayout var1, View var2, View var3) {
         if (var3 != this.mAnchorDirectChild && !this.shouldDodge(var3, ViewCompat.getLayoutDirection(var1))) {
            CoordinatorLayout.Behavior var4 = this.mBehavior;
            if (var4 == null || !var4.layoutDependsOn(var1, var2, var3)) {
               return false;
            }
         }

         return true;
      }

      boolean didBlockInteraction() {
         if (this.mBehavior == null) {
            this.mDidBlockInteraction = false;
         }

         return this.mDidBlockInteraction;
      }

      View findAnchorView(CoordinatorLayout var1, View var2) {
         if (this.mAnchorId == -1) {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
            return null;
         } else {
            if (this.mAnchorView == null || !this.verifyAnchorView(var2, var1)) {
               this.resolveAnchorView(var2, var1);
            }

            return this.mAnchorView;
         }
      }

      public int getAnchorId() {
         return this.mAnchorId;
      }

      public CoordinatorLayout.Behavior getBehavior() {
         return this.mBehavior;
      }

      boolean getChangedAfterNestedScroll() {
         return this.mDidChangeAfterNestedScroll;
      }

      Rect getLastChildRect() {
         return this.mLastChildRect;
      }

      void invalidateAnchor() {
         this.mAnchorDirectChild = null;
         this.mAnchorView = null;
      }

      boolean isBlockingInteractionBelow(CoordinatorLayout var1, View var2) {
         boolean var4 = this.mDidBlockInteraction;
         if (var4) {
            return true;
         } else {
            CoordinatorLayout.Behavior var5 = this.mBehavior;
            boolean var3;
            if (var5 != null) {
               var3 = var5.blocksInteractionBelow(var1, var2);
            } else {
               var3 = false;
            }

            var3 |= var4;
            this.mDidBlockInteraction = var3;
            return var3;
         }
      }

      boolean isNestedScrollAccepted(int var1) {
         if (var1 != 0) {
            return var1 != 1 ? false : this.mDidAcceptNestedScrollNonTouch;
         } else {
            return this.mDidAcceptNestedScrollTouch;
         }
      }

      void resetChangedAfterNestedScroll() {
         this.mDidChangeAfterNestedScroll = false;
      }

      void resetNestedScroll(int var1) {
         this.setNestedScrollAccepted(var1, false);
      }

      void resetTouchBehaviorTracking() {
         this.mDidBlockInteraction = false;
      }

      public void setAnchorId(int var1) {
         this.invalidateAnchor();
         this.mAnchorId = var1;
      }

      public void setBehavior(CoordinatorLayout.Behavior var1) {
         CoordinatorLayout.Behavior var2 = this.mBehavior;
         if (var2 != var1) {
            if (var2 != null) {
               var2.onDetachedFromLayoutParams();
            }

            this.mBehavior = var1;
            this.mBehaviorTag = null;
            this.mBehaviorResolved = true;
            if (var1 != null) {
               var1.onAttachedToLayoutParams(this);
            }
         }

      }

      void setChangedAfterNestedScroll(boolean var1) {
         this.mDidChangeAfterNestedScroll = var1;
      }

      void setLastChildRect(Rect var1) {
         this.mLastChildRect.set(var1);
      }

      void setNestedScrollAccepted(int var1, boolean var2) {
         if (var1 != 0) {
            if (var1 == 1) {
               this.mDidAcceptNestedScrollNonTouch = var2;
            }
         } else {
            this.mDidAcceptNestedScrollTouch = var2;
         }
      }
   }

   class OnPreDrawListener implements android.view.ViewTreeObserver.OnPreDrawListener {
      public boolean onPreDraw() {
         CoordinatorLayout.this.onChildViewsChanged(0);
         return true;
      }
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public CoordinatorLayout.SavedState createFromParcel(Parcel var1) {
            return new CoordinatorLayout.SavedState(var1, (ClassLoader)null);
         }

         public CoordinatorLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new CoordinatorLayout.SavedState(var1, var2);
         }

         public CoordinatorLayout.SavedState[] newArray(int var1) {
            return new CoordinatorLayout.SavedState[var1];
         }
      };
      SparseArray behaviorStates;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         int var4 = var1.readInt();
         int[] var5 = new int[var4];
         var1.readIntArray(var5);
         Parcelable[] var6 = var1.readParcelableArray(var2);
         this.behaviorStates = new SparseArray(var4);

         for(int var3 = 0; var3 < var4; ++var3) {
            this.behaviorStates.append(var5[var3], var6[var3]);
         }

      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         SparseArray var5 = this.behaviorStates;
         int var3;
         if (var5 != null) {
            var3 = var5.size();
         } else {
            var3 = 0;
         }

         var1.writeInt(var3);
         int[] var7 = new int[var3];
         Parcelable[] var6 = new Parcelable[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            var7[var4] = this.behaviorStates.keyAt(var4);
            var6[var4] = (Parcelable)this.behaviorStates.valueAt(var4);
         }

         var1.writeIntArray(var7);
         var1.writeParcelableArray(var6, var2);
      }
   }

   static class ViewElevationComparator implements Comparator {
      public int compare(View var1, View var2) {
         float var3 = ViewCompat.getZ(var1);
         float var4 = ViewCompat.getZ(var2);
         if (var3 > var4) {
            return -1;
         } else {
            return var3 < var4 ? 1 : 0;
         }
      }
   }
}
