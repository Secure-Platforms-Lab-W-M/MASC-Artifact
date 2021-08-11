package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
   public static final int HORIZONTAL = 0;
   private static final int INDEX_BOTTOM = 2;
   private static final int INDEX_CENTER_VERTICAL = 0;
   private static final int INDEX_FILL = 3;
   private static final int INDEX_TOP = 1;
   public static final int SHOW_DIVIDER_BEGINNING = 1;
   public static final int SHOW_DIVIDER_END = 4;
   public static final int SHOW_DIVIDER_MIDDLE = 2;
   public static final int SHOW_DIVIDER_NONE = 0;
   public static final int VERTICAL = 1;
   private static final int VERTICAL_GRAVITY_COUNT = 4;
   private boolean mBaselineAligned;
   private int mBaselineAlignedChildIndex;
   private int mBaselineChildTop;
   private Drawable mDivider;
   private int mDividerHeight;
   private int mDividerPadding;
   private int mDividerWidth;
   private int mGravity;
   private int[] mMaxAscent;
   private int[] mMaxDescent;
   private int mOrientation;
   private int mShowDividers;
   private int mTotalLength;
   private boolean mUseLargestChild;
   private float mWeightSum;

   public LinearLayoutCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mBaselineAligned = true;
      this.mBaselineAlignedChildIndex = -1;
      this.mBaselineChildTop = 0;
      this.mGravity = 8388659;
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.LinearLayoutCompat, var3, 0);
      var3 = var5.getInt(R$styleable.LinearLayoutCompat_android_orientation, -1);
      if (var3 >= 0) {
         this.setOrientation(var3);
      }

      var3 = var5.getInt(R$styleable.LinearLayoutCompat_android_gravity, -1);
      if (var3 >= 0) {
         this.setGravity(var3);
      }

      boolean var4 = var5.getBoolean(R$styleable.LinearLayoutCompat_android_baselineAligned, true);
      if (!var4) {
         this.setBaselineAligned(var4);
      }

      this.mWeightSum = var5.getFloat(R$styleable.LinearLayoutCompat_android_weightSum, -1.0F);
      this.mBaselineAlignedChildIndex = var5.getInt(R$styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
      this.mUseLargestChild = var5.getBoolean(R$styleable.LinearLayoutCompat_measureWithLargestChild, false);
      this.setDividerDrawable(var5.getDrawable(R$styleable.LinearLayoutCompat_divider));
      this.mShowDividers = var5.getInt(R$styleable.LinearLayoutCompat_showDividers, 0);
      this.mDividerPadding = var5.getDimensionPixelSize(R$styleable.LinearLayoutCompat_dividerPadding, 0);
      var5.recycle();
   }

   private void forceUniformHeight(int var1, int var2) {
      int var4 = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);

      for(int var3 = 0; var3 < var1; ++var3) {
         View var6 = this.getVirtualChildAt(var3);
         if (var6.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            if (var7.height == -1) {
               int var5 = var7.width;
               var7.width = var6.getMeasuredWidth();
               this.measureChildWithMargins(var6, var2, 0, var4, 0);
               var7.width = var5;
            }
         }
      }

   }

   private void forceUniformWidth(int var1, int var2) {
      int var4 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);

      for(int var3 = 0; var3 < var1; ++var3) {
         View var6 = this.getVirtualChildAt(var3);
         if (var6.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            if (var7.width == -1) {
               int var5 = var7.height;
               var7.height = var6.getMeasuredHeight();
               this.measureChildWithMargins(var6, var4, 0, var2, 0);
               var7.height = var5;
            }
         }
      }

   }

   private void setChildFrame(View var1, int var2, int var3, int var4, int var5) {
      var1.layout(var2, var3, var2 + var4, var3 + var5);
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof LinearLayoutCompat.LayoutParams;
   }

   void drawDividersHorizontal(Canvas var1) {
      int var4 = this.getVirtualChildCount();
      boolean var5 = ViewUtils.isLayoutRtl(this);

      int var2;
      View var6;
      LinearLayoutCompat.LayoutParams var7;
      for(var2 = 0; var2 < var4; ++var2) {
         var6 = this.getVirtualChildAt(var2);
         if (var6 != null && var6.getVisibility() != 8 && this.hasDividerBeforeChildAt(var2)) {
            var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            int var3;
            if (var5) {
               var3 = var6.getRight() + var7.rightMargin;
            } else {
               var3 = var6.getLeft() - var7.leftMargin - this.mDividerWidth;
            }

            this.drawVerticalDivider(var1, var3);
         }
      }

      if (this.hasDividerBeforeChildAt(var4)) {
         var6 = this.getVirtualChildAt(var4 - 1);
         if (var6 == null) {
            if (var5) {
               var2 = this.getPaddingLeft();
            } else {
               var2 = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
            }
         } else {
            var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            if (var5) {
               var2 = var6.getLeft() - var7.leftMargin - this.mDividerWidth;
            } else {
               var2 = var6.getRight() + var7.rightMargin;
            }
         }

         this.drawVerticalDivider(var1, var2);
      }

   }

   void drawDividersVertical(Canvas var1) {
      int var3 = this.getVirtualChildCount();

      int var2;
      View var4;
      LinearLayoutCompat.LayoutParams var5;
      for(var2 = 0; var2 < var3; ++var2) {
         var4 = this.getVirtualChildAt(var2);
         if (var4 != null && var4.getVisibility() != 8 && this.hasDividerBeforeChildAt(var2)) {
            var5 = (LinearLayoutCompat.LayoutParams)var4.getLayoutParams();
            this.drawHorizontalDivider(var1, var4.getTop() - var5.topMargin - this.mDividerHeight);
         }
      }

      if (this.hasDividerBeforeChildAt(var3)) {
         var4 = this.getVirtualChildAt(var3 - 1);
         if (var4 == null) {
            var2 = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
         } else {
            var5 = (LinearLayoutCompat.LayoutParams)var4.getLayoutParams();
            var2 = var4.getBottom() + var5.bottomMargin;
         }

         this.drawHorizontalDivider(var1, var2);
      }

   }

   void drawHorizontalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, var2, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + var2);
      this.mDivider.draw(var1);
   }

   void drawVerticalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(var2, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + var2, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
      this.mDivider.draw(var1);
   }

   protected LinearLayoutCompat.LayoutParams generateDefaultLayoutParams() {
      int var1 = this.mOrientation;
      if (var1 == 0) {
         return new LinearLayoutCompat.LayoutParams(-2, -2);
      } else {
         return var1 == 1 ? new LinearLayoutCompat.LayoutParams(-1, -2) : null;
      }
   }

   public LinearLayoutCompat.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new LinearLayoutCompat.LayoutParams(this.getContext(), var1);
   }

   protected LinearLayoutCompat.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new LinearLayoutCompat.LayoutParams(var1);
   }

   public int getBaseline() {
      if (this.mBaselineAlignedChildIndex < 0) {
         return super.getBaseline();
      } else {
         int var1 = this.getChildCount();
         int var2 = this.mBaselineAlignedChildIndex;
         if (var1 > var2) {
            View var5 = this.getChildAt(var2);
            int var3 = var5.getBaseline();
            if (var3 == -1) {
               if (this.mBaselineAlignedChildIndex == 0) {
                  return -1;
               } else {
                  throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
               }
            } else {
               var2 = this.mBaselineChildTop;
               var1 = var2;
               if (this.mOrientation == 1) {
                  int var4 = this.mGravity & 112;
                  var1 = var2;
                  if (var4 != 48) {
                     if (var4 != 16) {
                        if (var4 != 80) {
                           var1 = var2;
                        } else {
                           var1 = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                        }
                     } else {
                        var1 = var2 + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                     }
                  }
               }

               return ((LinearLayoutCompat.LayoutParams)var5.getLayoutParams()).topMargin + var1 + var3;
            }
         } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
         }
      }
   }

   public int getBaselineAlignedChildIndex() {
      return this.mBaselineAlignedChildIndex;
   }

   int getChildrenSkipCount(View var1, int var2) {
      return 0;
   }

   public Drawable getDividerDrawable() {
      return this.mDivider;
   }

   public int getDividerPadding() {
      return this.mDividerPadding;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getDividerWidth() {
      return this.mDividerWidth;
   }

   public int getGravity() {
      return this.mGravity;
   }

   int getLocationOffset(View var1) {
      return 0;
   }

   int getNextLocationOffset(View var1) {
      return 0;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getShowDividers() {
      return this.mShowDividers;
   }

   View getVirtualChildAt(int var1) {
      return this.getChildAt(var1);
   }

   int getVirtualChildCount() {
      return this.getChildCount();
   }

   public float getWeightSum() {
      return this.mWeightSum;
   }

   protected boolean hasDividerBeforeChildAt(int var1) {
      boolean var3 = false;
      boolean var2 = false;
      if (var1 == 0) {
         if ((this.mShowDividers & 1) != 0) {
            var2 = true;
         }

         return var2;
      } else if (var1 == this.getChildCount()) {
         var2 = var3;
         if ((this.mShowDividers & 4) != 0) {
            var2 = true;
         }

         return var2;
      } else if ((this.mShowDividers & 2) != 0) {
         --var1;

         while(var1 >= 0) {
            if (this.getChildAt(var1).getVisibility() != 8) {
               return true;
            }

            --var1;
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean isBaselineAligned() {
      return this.mBaselineAligned;
   }

   public boolean isMeasureWithLargestChildEnabled() {
      return this.mUseLargestChild;
   }

   void layoutHorizontal(int var1, int var2, int var3, int var4) {
      boolean var20 = ViewUtils.isLayoutRtl(this);
      int var11 = this.getPaddingTop();
      int var12 = var4 - var2;
      int var13 = this.getPaddingBottom();
      int var14 = this.getPaddingBottom();
      int var9 = this.getVirtualChildCount();
      int var15 = this.mGravity;
      boolean var21 = this.mBaselineAligned;
      int[] var22 = this.mMaxAscent;
      int[] var23 = this.mMaxDescent;
      int var8 = ViewCompat.getLayoutDirection(this);
      var2 = GravityCompat.getAbsoluteGravity(var15 & 8388615, var8);
      if (var2 != 1) {
         if (var2 != 5) {
            var1 = this.getPaddingLeft();
         } else {
            var1 = this.getPaddingLeft() + var3 - var1 - this.mTotalLength;
         }
      } else {
         var1 = this.getPaddingLeft() + (var3 - var1 - this.mTotalLength) / 2;
      }

      int var6;
      byte var7;
      if (var20) {
         var6 = var9 - 1;
         var7 = -1;
      } else {
         var6 = 0;
         var7 = 1;
      }

      var2 = 0;
      var3 = var11;

      for(var4 = var1; var2 < var9; ++var2) {
         int var17 = var6 + var7 * var2;
         View var24 = this.getVirtualChildAt(var17);
         if (var24 == null) {
            var4 += this.measureNullChild(var17);
         } else if (var24.getVisibility() != 8) {
            int var16 = var24.getMeasuredWidth();
            int var18 = var24.getMeasuredHeight();
            LinearLayoutCompat.LayoutParams var25 = (LinearLayoutCompat.LayoutParams)var24.getLayoutParams();
            if (var21 && var25.height != -1) {
               var1 = var24.getBaseline();
            } else {
               var1 = -1;
            }

            int var10 = var25.gravity;
            if (var10 < 0) {
               var10 = var15 & 112;
            }

            var10 &= 112;
            if (var10 != 16) {
               if (var10 != 48) {
                  if (var10 != 80) {
                     var1 = var3;
                  } else {
                     var10 = var12 - var13 - var18 - var25.bottomMargin;
                     if (var1 != -1) {
                        int var19 = var24.getMeasuredHeight();
                        var1 = var10 - (var23[2] - (var19 - var1));
                     } else {
                        var1 = var10;
                     }
                  }
               } else {
                  var10 = var25.topMargin + var3;
                  if (var1 != -1) {
                     var1 = var10 + (var22[1] - var1);
                  } else {
                     var1 = var10;
                  }
               }
            } else {
               var1 = (var12 - var11 - var14 - var18) / 2 + var3 + var25.topMargin - var25.bottomMargin;
            }

            var10 = var4;
            if (this.hasDividerBeforeChildAt(var17)) {
               var10 = var4 + this.mDividerWidth;
            }

            var4 = var10 + var25.leftMargin;
            this.setChildFrame(var24, var4 + this.getLocationOffset(var24), var1, var16, var18);
            var1 = var25.rightMargin;
            var10 = this.getNextLocationOffset(var24);
            var2 += this.getChildrenSkipCount(var24, var17);
            var4 += var16 + var1 + var10;
         }
      }

   }

   void layoutVertical(int var1, int var2, int var3, int var4) {
      int var6 = this.getPaddingLeft();
      int var7 = var3 - var1;
      int var8 = this.getPaddingRight();
      int var9 = this.getPaddingRight();
      int var10 = this.getVirtualChildCount();
      int var11 = this.mGravity;
      var1 = var11 & 112;
      if (var1 != 16) {
         if (var1 != 80) {
            var1 = this.getPaddingTop();
         } else {
            var1 = this.getPaddingTop() + var4 - var2 - this.mTotalLength;
         }
      } else {
         var1 = this.getPaddingTop() + (var4 - var2 - this.mTotalLength) / 2;
      }

      var2 = 0;
      var3 = var6;

      while(true) {
         var4 = var3;
         if (var2 >= var10) {
            return;
         }

         View var14 = this.getVirtualChildAt(var2);
         if (var14 == null) {
            var1 += this.measureNullChild(var2);
         } else if (var14.getVisibility() != 8) {
            int var13 = var14.getMeasuredWidth();
            int var12 = var14.getMeasuredHeight();
            LinearLayoutCompat.LayoutParams var15 = (LinearLayoutCompat.LayoutParams)var14.getLayoutParams();
            var3 = var15.gravity;
            if (var3 < 0) {
               var3 = var11 & 8388615;
            }

            var3 = GravityCompat.getAbsoluteGravity(var3, ViewCompat.getLayoutDirection(this)) & 7;
            if (var3 != 1) {
               if (var3 != 5) {
                  var3 = var15.leftMargin + var4;
               } else {
                  var3 = var7 - var8 - var13 - var15.rightMargin;
               }
            } else {
               var3 = (var7 - var6 - var9 - var13) / 2 + var4 + var15.leftMargin - var15.rightMargin;
            }

            int var5 = var1;
            if (this.hasDividerBeforeChildAt(var2)) {
               var5 = var1 + this.mDividerHeight;
            }

            var1 = var5 + var15.topMargin;
            this.setChildFrame(var14, var3, var1 + this.getLocationOffset(var14), var13, var12);
            var3 = var15.bottomMargin;
            var5 = this.getNextLocationOffset(var14);
            var2 += this.getChildrenSkipCount(var14, var2);
            var1 += var12 + var3 + var5;
         }

         ++var2;
         var3 = var3;
      }
   }

   void measureChildBeforeLayout(View var1, int var2, int var3, int var4, int var5, int var6) {
      this.measureChildWithMargins(var1, var3, var4, var5, var6);
   }

   void measureHorizontal(int var1, int var2) {
      this.mTotalLength = 0;
      int var16 = this.getVirtualChildCount();
      int var10 = MeasureSpec.getMode(var1);
      int var21 = MeasureSpec.getMode(var2);
      if (this.mMaxAscent == null || this.mMaxDescent == null) {
         this.mMaxAscent = new int[4];
         this.mMaxDescent = new int[4];
      }

      int[] var27 = this.mMaxAscent;
      int[] var28 = this.mMaxDescent;
      var27[3] = -1;
      var27[2] = -1;
      var27[1] = -1;
      var27[0] = -1;
      var28[3] = -1;
      var28[2] = -1;
      var28[1] = -1;
      var28[0] = -1;
      boolean var23 = this.mBaselineAligned;
      boolean var13 = false;
      boolean var24 = this.mUseLargestChild;
      boolean var15;
      if (var10 == 1073741824) {
         var15 = true;
      } else {
         var15 = false;
      }

      int var7 = 0;
      int var5 = Integer.MIN_VALUE;
      boolean var12 = false;
      boolean var9 = true;
      int var14 = 0;
      float var3 = 0.0F;
      int var11 = 0;
      int var6 = 0;

      int var8;
      int var17;
      int var18;
      int var19;
      int var20;
      for(var8 = 0; var11 < var16; var7 = var17) {
         View var29 = this.getVirtualChildAt(var11);
         if (var29 == null) {
            this.mTotalLength += this.measureNullChild(var11);
            var17 = var7;
            var7 = var6;
            var6 = var17;
         } else if (var29.getVisibility() == 8) {
            var11 += this.getChildrenSkipCount(var29, var11);
            var17 = var7;
            var7 = var6;
            var6 = var17;
         } else {
            if (this.hasDividerBeforeChildAt(var11)) {
               this.mTotalLength += this.mDividerWidth;
            }

            LinearLayoutCompat.LayoutParams var25 = (LinearLayoutCompat.LayoutParams)var29.getLayoutParams();
            var3 += var25.weight;
            if (var10 == 1073741824 && var25.width == 0 && var25.weight > 0.0F) {
               if (var15) {
                  this.mTotalLength += var25.leftMargin + var25.rightMargin;
               } else {
                  var17 = this.mTotalLength;
                  this.mTotalLength = Math.max(var17, var25.leftMargin + var17 + var25.rightMargin);
               }

               if (var23) {
                  var17 = MeasureSpec.makeMeasureSpec(0, 0);
                  var29.measure(var17, var17);
               } else {
                  var13 = true;
               }
            } else {
               if (var25.width == 0 && var25.weight > 0.0F) {
                  var25.width = -2;
                  var17 = 0;
               } else {
                  var17 = Integer.MIN_VALUE;
               }

               if (var3 == 0.0F) {
                  var18 = this.mTotalLength;
               } else {
                  var18 = 0;
               }

               this.measureChildBeforeLayout(var29, var11, var1, var18, var2, 0);
               if (var17 != Integer.MIN_VALUE) {
                  var25.width = var17;
               }

               var17 = var29.getMeasuredWidth();
               if (var15) {
                  this.mTotalLength += var25.leftMargin + var17 + var25.rightMargin + this.getNextLocationOffset(var29);
               } else {
                  var18 = this.mTotalLength;
                  this.mTotalLength = Math.max(var18, var18 + var17 + var25.leftMargin + var25.rightMargin + this.getNextLocationOffset(var29));
               }

               if (var24) {
                  var5 = Math.max(var17, var5);
               }
            }

            var19 = var11;
            boolean var34 = false;
            boolean var37 = var34;
            boolean var30 = var12;
            if (var21 != 1073741824) {
               var37 = var34;
               var30 = var12;
               if (var25.height == -1) {
                  var30 = true;
                  var37 = true;
               }
            }

            var11 = var25.topMargin + var25.bottomMargin;
            int var35 = var29.getMeasuredHeight() + var11;
            var20 = View.combineMeasuredStates(var7, var29.getMeasuredState());
            if (var23) {
               int var22 = var29.getBaseline();
               if (var22 != -1) {
                  if (var25.gravity < 0) {
                     var7 = this.mGravity;
                  } else {
                     var7 = var25.gravity;
                  }

                  var7 = ((var7 & 112) >> 4 & -2) >> 1;
                  var27[var7] = Math.max(var27[var7], var22);
                  var28[var7] = Math.max(var28[var7], var35 - var22);
               }
            }

            var14 = Math.max(var14, var35);
            boolean var32;
            if (var9 && var25.height == -1) {
               var32 = true;
            } else {
               var32 = false;
            }

            int var33;
            if (var25.weight > 0.0F) {
               if (!var37) {
                  var11 = var35;
               }

               var33 = Math.max(var8, var11);
            } else {
               if (!var37) {
                  var11 = var35;
               }

               var6 = Math.max(var6, var11);
               var33 = var8;
            }

            var35 = this.getChildrenSkipCount(var29, var19);
            var7 = var6;
            var6 = var20;
            var18 = var19 + var35;
            var12 = var30;
            var9 = var32;
            var11 = var18;
            var8 = var33;
         }

         ++var11;
         var17 = var6;
         var6 = var7;
      }

      var17 = var8;
      var8 = var14;
      var14 = var5;
      if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var16)) {
         this.mTotalLength += this.mDividerWidth;
      }

      if (var27[1] == -1 && var27[0] == -1 && var27[2] == -1 && var27[3] == -1) {
         var5 = var8;
      } else {
         var5 = Math.max(var8, Math.max(var27[3], Math.max(var27[0], Math.max(var27[1], var27[2]))) + Math.max(var28[3], Math.max(var28[0], Math.max(var28[1], var28[2]))));
      }

      var8 = var7;
      LinearLayoutCompat.LayoutParams var26;
      View var40;
      if (var24 && (var10 == Integer.MIN_VALUE || var10 == 0)) {
         this.mTotalLength = 0;

         for(var7 = 0; var7 < var16; var7 = var5 + 1) {
            var40 = this.getVirtualChildAt(var7);
            if (var40 == null) {
               this.mTotalLength += this.measureNullChild(var7);
               var5 = var7;
            } else if (var40.getVisibility() == 8) {
               var5 = var7 + this.getChildrenSkipCount(var40, var7);
            } else {
               var26 = (LinearLayoutCompat.LayoutParams)var40.getLayoutParams();
               if (var15) {
                  var18 = this.mTotalLength;
                  var19 = var26.leftMargin;
                  var5 = var7;
                  this.mTotalLength = var18 + var19 + var14 + var26.rightMargin + this.getNextLocationOffset(var40);
               } else {
                  var5 = var7;
                  var7 = this.mTotalLength;
                  this.mTotalLength = Math.max(var7, var7 + var14 + var26.leftMargin + var26.rightMargin + this.getNextLocationOffset(var40));
               }
            }
         }

         var5 = var5;
      }

      var11 = var10;
      this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
      var10 = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), var1, 0);
      var7 = var10 & 16777215;
      var19 = var7 - this.mTotalLength;
      if (var13 || var19 != 0 && var3 > 0.0F) {
         float var4 = this.mWeightSum;
         if (var4 > 0.0F) {
            var3 = var4;
         }

         var27[3] = -1;
         var27[2] = -1;
         var27[1] = -1;
         var27[0] = -1;
         var28[3] = -1;
         var28[2] = -1;
         var28[1] = -1;
         var28[0] = -1;
         byte var38 = -1;
         this.mTotalLength = 0;
         byte var31 = 0;
         var18 = var6;
         var6 = var8;
         var7 = var16;
         var16 = var31;
         var8 = var10;
         var5 = var19;
         var14 = var11;
         var11 = var38;

         for(var10 = var18; var16 < var7; ++var16) {
            var40 = this.getVirtualChildAt(var16);
            if (var40 != null && var40.getVisibility() != 8) {
               var26 = (LinearLayoutCompat.LayoutParams)var40.getLayoutParams();
               var4 = var26.weight;
               if (var4 > 0.0F) {
                  var18 = (int)((float)var5 * var4 / var3);
                  var20 = getChildMeasureSpec(var2, this.getPaddingTop() + this.getPaddingBottom() + var26.topMargin + var26.bottomMargin, var26.height);
                  if (var26.width == 0 && var14 == 1073741824) {
                     if (var18 > 0) {
                        var17 = var18;
                     } else {
                        var17 = 0;
                     }

                     var40.measure(MeasureSpec.makeMeasureSpec(var17, 1073741824), var20);
                  } else {
                     var19 = var40.getMeasuredWidth() + var18;
                     var17 = var19;
                     if (var19 < 0) {
                        var17 = 0;
                     }

                     var40.measure(MeasureSpec.makeMeasureSpec(var17, 1073741824), var20);
                  }

                  var6 = View.combineMeasuredStates(var6, var40.getMeasuredState() & -16777216);
                  var3 -= var4;
                  var5 -= var18;
               }

               if (var15) {
                  this.mTotalLength += var40.getMeasuredWidth() + var26.leftMargin + var26.rightMargin + this.getNextLocationOffset(var40);
               } else {
                  var17 = this.mTotalLength;
                  this.mTotalLength = Math.max(var17, var40.getMeasuredWidth() + var17 + var26.leftMargin + var26.rightMargin + this.getNextLocationOffset(var40));
               }

               boolean var39;
               if (var21 != 1073741824 && var26.height == -1) {
                  var39 = true;
               } else {
                  var39 = false;
               }

               var20 = var26.topMargin + var26.bottomMargin;
               var19 = var40.getMeasuredHeight() + var20;
               var18 = Math.max(var11, var19);
               if (var39) {
                  var11 = var20;
               } else {
                  var11 = var19;
               }

               var11 = Math.max(var10, var11);
               if (var9 && var26.height == -1) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               if (var23) {
                  var17 = var40.getBaseline();
                  if (var17 != -1) {
                     if (var26.gravity < 0) {
                        var10 = this.mGravity;
                     } else {
                        var10 = var26.gravity;
                     }

                     var10 = ((var10 & 112) >> 4 & -2) >> 1;
                     var27[var10] = Math.max(var27[var10], var17);
                     var28[var10] = Math.max(var28[var10], var19 - var17);
                  }
               }

               var10 = var11;
               var11 = var18;
            }
         }

         label220: {
            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            if (var27[1] == -1 && var27[0] == -1 && var27[2] == -1) {
               var5 = var11;
               if (var27[3] == -1) {
                  break label220;
               }
            }

            var5 = Math.max(var11, Math.max(var27[3], Math.max(var27[0], Math.max(var27[1], var27[2]))) + Math.max(var28[3], Math.max(var28[0], Math.max(var28[1], var28[2]))));
         }

         int var36 = var10;
         var11 = var7;
         var10 = var8;
         var7 = var36;
         var8 = var6;
      } else {
         var6 = Math.max(var6, var17);
         if (var24 && var11 != 1073741824) {
            for(var11 = 0; var11 < var16; ++var11) {
               var40 = this.getVirtualChildAt(var11);
               if (var40 != null && var40.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var40.getLayoutParams()).weight > 0.0F) {
                  var40.measure(MeasureSpec.makeMeasureSpec(var14, 1073741824), MeasureSpec.makeMeasureSpec(var40.getMeasuredHeight(), 1073741824));
               }
            }
         }

         var11 = var16;
         var7 = var6;
      }

      var6 = var5;
      if (!var9) {
         var6 = var5;
         if (var21 != 1073741824) {
            var6 = var7;
         }
      }

      this.setMeasuredDimension(var10 | -16777216 & var8, View.resolveSizeAndState(Math.max(var6 + this.getPaddingTop() + this.getPaddingBottom(), this.getSuggestedMinimumHeight()), var2, var8 << 16));
      if (var12) {
         this.forceUniformHeight(var11, var1);
      }
   }

   int measureNullChild(int var1) {
      return 0;
   }

   void measureVertical(int var1, int var2) {
      this.mTotalLength = 0;
      int var11 = 0;
      float var3 = 0.0F;
      int var13 = this.getVirtualChildCount();
      int var22 = MeasureSpec.getMode(var1);
      int var8 = MeasureSpec.getMode(var2);
      int var19 = this.mBaselineAlignedChildIndex;
      boolean var25 = this.mUseLargestChild;
      boolean var12 = false;
      int var10 = 0;
      int var9 = 0;
      int var5 = 0;
      int var6 = Integer.MIN_VALUE;
      boolean var15 = false;
      int var14 = 0;

      boolean var7;
      int var16;
      int var17;
      int var18;
      int var20;
      int var21;
      int var34;
      for(var7 = true; var14 < var13; ++var14) {
         View var27 = this.getVirtualChildAt(var14);
         if (var27 == null) {
            this.mTotalLength += this.measureNullChild(var14);
         } else if (var27.getVisibility() == 8) {
            var14 += this.getChildrenSkipCount(var27, var14);
         } else {
            if (this.hasDividerBeforeChildAt(var14)) {
               this.mTotalLength += this.mDividerHeight;
            }

            LinearLayoutCompat.LayoutParams var26 = (LinearLayoutCompat.LayoutParams)var27.getLayoutParams();
            var3 += var26.weight;
            boolean var35;
            if (var8 == 1073741824 && var26.height == 0 && var26.weight > 0.0F) {
               var34 = this.mTotalLength;
               this.mTotalLength = Math.max(var34, var26.topMargin + var34 + var26.bottomMargin);
               var35 = true;
            } else {
               if (var26.height == 0 && var26.weight > 0.0F) {
                  var26.height = -2;
                  var16 = 0;
               } else {
                  var16 = Integer.MIN_VALUE;
               }

               if (var3 == 0.0F) {
                  var18 = this.mTotalLength;
               } else {
                  var18 = 0;
               }

               var17 = var6;
               this.measureChildBeforeLayout(var27, var14, var1, 0, var2, var18);
               if (var16 != Integer.MIN_VALUE) {
                  var26.height = var16;
               }

               var18 = var27.getMeasuredHeight();
               var6 = this.mTotalLength;
               this.mTotalLength = Math.max(var6, var6 + var18 + var26.topMargin + var26.bottomMargin + this.getNextLocationOffset(var27));
               var6 = var17;
               var35 = var15;
               if (var25) {
                  var6 = Math.max(var18, var17);
                  var35 = var15;
               }
            }

            if (var19 >= 0 && var19 == var14 + 1) {
               this.mBaselineChildTop = this.mTotalLength;
            }

            if (var14 < var19 && var26.weight > 0.0F) {
               throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
            }

            boolean var37 = false;
            boolean var36 = var37;
            boolean var29 = var12;
            if (var22 != 1073741824) {
               var36 = var37;
               var29 = var12;
               if (var26.width == -1) {
                  var29 = true;
                  var36 = true;
               }
            }

            int var33 = var26.leftMargin + var26.rightMargin;
            var18 = var27.getMeasuredWidth() + var33;
            var20 = Math.max(var9, var18);
            var21 = View.combineMeasuredStates(var11, var27.getMeasuredState());
            if (var7 && var26.width == -1) {
               var7 = true;
            } else {
               var7 = false;
            }

            if (var26.weight > 0.0F) {
               if (!var36) {
                  var33 = var18;
               }

               var11 = Math.max(var5, var33);
               var9 = var10;
               var10 = var11;
            } else {
               if (!var36) {
                  var33 = var18;
               }

               var9 = Math.max(var10, var33);
               var10 = var5;
            }

            var14 += this.getChildrenSkipCount(var27, var14);
            var18 = var10;
            var12 = var29;
            var15 = var35;
            var10 = var9;
            var5 = var18;
            var9 = var20;
            var11 = var21;
         }
      }

      var14 = var11;
      var11 = var9;
      var16 = var5;
      if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var13)) {
         this.mTotalLength += this.mDividerHeight;
      }

      var20 = var13;
      View var39;
      LinearLayoutCompat.LayoutParams var40;
      if (var25 && (var8 == Integer.MIN_VALUE || var8 == 0)) {
         this.mTotalLength = 0;

         for(var5 = 0; var5 < var20; ++var5) {
            var39 = this.getVirtualChildAt(var5);
            if (var39 == null) {
               this.mTotalLength += this.measureNullChild(var5);
            } else if (var39.getVisibility() == 8) {
               var5 += this.getChildrenSkipCount(var39, var5);
            } else {
               var40 = (LinearLayoutCompat.LayoutParams)var39.getLayoutParams();
               var13 = this.mTotalLength;
               this.mTotalLength = Math.max(var13, var13 + var6 + var40.topMargin + var40.bottomMargin + this.getNextLocationOffset(var39));
            }
         }
      }

      this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
      int var23 = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), var2, 0);
      var13 = var23 & 16777215;
      var5 = var13 - this.mTotalLength;
      if (var15 || var5 != 0 && var3 > 0.0F) {
         float var4 = this.mWeightSum;
         if (var4 > 0.0F) {
            var3 = var4;
         }

         this.mTotalLength = 0;
         byte var38 = 0;
         var17 = var5;
         var18 = var9;
         var5 = var14;
         var6 = var10;
         var14 = var8;
         var34 = var38;
         var9 = var17;

         for(var8 = var18; var34 < var20; ++var34) {
            var39 = this.getVirtualChildAt(var34);
            if (var39.getVisibility() != 8) {
               var40 = (LinearLayoutCompat.LayoutParams)var39.getLayoutParams();
               var4 = var40.weight;
               if (var4 > 0.0F) {
                  var17 = (int)((float)var9 * var4 / var3);
                  var18 = this.getPaddingLeft();
                  var19 = this.getPaddingRight();
                  var3 -= var4;
                  var21 = var40.leftMargin;
                  int var24 = var40.rightMargin;
                  var16 = var9 - var17;
                  var18 = getChildMeasureSpec(var1, var18 + var19 + var21 + var24, var40.width);
                  if (var40.height == 0 && var14 == 1073741824) {
                     if (var17 > 0) {
                        var9 = var17;
                     } else {
                        var9 = 0;
                     }

                     var39.measure(var18, MeasureSpec.makeMeasureSpec(var9, 1073741824));
                  } else {
                     var17 += var39.getMeasuredHeight();
                     var9 = var17;
                     if (var17 < 0) {
                        var9 = 0;
                     }

                     var39.measure(var18, MeasureSpec.makeMeasureSpec(var9, 1073741824));
                  }

                  var5 = View.combineMeasuredStates(var5, var39.getMeasuredState() & -256);
                  var9 = var16;
               }

               var17 = var40.leftMargin + var40.rightMargin;
               var18 = var39.getMeasuredWidth() + var17;
               var16 = Math.max(var8, var18);
               boolean var32;
               if (var22 != 1073741824 && var40.width == -1) {
                  var32 = true;
               } else {
                  var32 = false;
               }

               if (var32) {
                  var8 = var17;
               } else {
                  var8 = var18;
               }

               var17 = Math.max(var6, var8);
               boolean var30;
               if (var7 && var40.width == -1) {
                  var30 = true;
               } else {
                  var30 = false;
               }

               int var31 = this.mTotalLength;
               this.mTotalLength = Math.max(var31, var31 + var39.getMeasuredHeight() + var40.topMargin + var40.bottomMargin + this.getNextLocationOffset(var39));
               var8 = var16;
               var7 = var30;
               var6 = var17;
            }
         }

         this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
         var14 = var5;
      } else {
         var10 = Math.max(var10, var16);
         if (!var25) {
            var5 = var9;
         } else if (var8 == 1073741824) {
            var5 = var9;
         } else {
            for(var9 = 0; var9 < var20; ++var9) {
               var39 = this.getVirtualChildAt(var9);
               if (var39 != null && var39.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var39.getLayoutParams()).weight > 0.0F) {
                  var39.measure(MeasureSpec.makeMeasureSpec(var39.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var6, 1073741824));
               }
            }

            var5 = var11;
         }

         var6 = var10;
         var8 = var5;
      }

      var5 = var8;
      if (!var7) {
         var5 = var8;
         if (var22 != 1073741824) {
            var5 = var6;
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(Math.max(var5 + this.getPaddingLeft() + this.getPaddingRight(), this.getSuggestedMinimumWidth()), var1, var14), var23);
      if (var12) {
         this.forceUniformWidth(var20, var2);
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.mDivider != null) {
         if (this.mOrientation == 1) {
            this.drawDividersVertical(var1);
         } else {
            this.drawDividersHorizontal(var1);
         }
      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.mOrientation == 1) {
         this.layoutVertical(var2, var3, var4, var5);
      } else {
         this.layoutHorizontal(var2, var3, var4, var5);
      }
   }

   protected void onMeasure(int var1, int var2) {
      if (this.mOrientation == 1) {
         this.measureVertical(var1, var2);
      } else {
         this.measureHorizontal(var1, var2);
      }
   }

   public void setBaselineAligned(boolean var1) {
      this.mBaselineAligned = var1;
   }

   public void setBaselineAlignedChildIndex(int var1) {
      if (var1 >= 0 && var1 < this.getChildCount()) {
         this.mBaselineAlignedChildIndex = var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("base aligned child index out of range (0, ");
         var2.append(this.getChildCount());
         var2.append(")");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void setDividerDrawable(Drawable var1) {
      if (var1 != this.mDivider) {
         this.mDivider = var1;
         boolean var2 = false;
         if (var1 != null) {
            this.mDividerWidth = var1.getIntrinsicWidth();
            this.mDividerHeight = var1.getIntrinsicHeight();
         } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
         }

         if (var1 == null) {
            var2 = true;
         }

         this.setWillNotDraw(var2);
         this.requestLayout();
      }
   }

   public void setDividerPadding(int var1) {
      this.mDividerPadding = var1;
   }

   public void setGravity(int var1) {
      if (this.mGravity != var1) {
         int var2 = var1;
         if ((8388615 & var1) == 0) {
            var2 = var1 | 8388611;
         }

         var1 = var2;
         if ((var2 & 112) == 0) {
            var1 = var2 | 48;
         }

         this.mGravity = var1;
         this.requestLayout();
      }

   }

   public void setHorizontalGravity(int var1) {
      var1 &= 8388615;
      int var2 = this.mGravity;
      if ((8388615 & var2) != var1) {
         this.mGravity = -8388616 & var2 | var1;
         this.requestLayout();
      }

   }

   public void setMeasureWithLargestChildEnabled(boolean var1) {
      this.mUseLargestChild = var1;
   }

   public void setOrientation(int var1) {
      if (this.mOrientation != var1) {
         this.mOrientation = var1;
         this.requestLayout();
      }

   }

   public void setShowDividers(int var1) {
      if (var1 != this.mShowDividers) {
         this.requestLayout();
      }

      this.mShowDividers = var1;
   }

   public void setVerticalGravity(int var1) {
      var1 &= 112;
      int var2 = this.mGravity;
      if ((var2 & 112) != var1) {
         this.mGravity = var2 & -113 | var1;
         this.requestLayout();
      }

   }

   public void setWeightSum(float var1) {
      this.mWeightSum = Math.max(0.0F, var1);
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface DividerMode {
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int gravity = -1;
      public float weight;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.weight = 0.0F;
      }

      public LayoutParams(int var1, int var2, float var3) {
         super(var1, var2);
         this.weight = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.LinearLayoutCompat_Layout);
         this.weight = var3.getFloat(R$styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
         this.gravity = var3.getInt(R$styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
         var3.recycle();
      }

      public LayoutParams(LinearLayoutCompat.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface OrientationMode {
   }
}
