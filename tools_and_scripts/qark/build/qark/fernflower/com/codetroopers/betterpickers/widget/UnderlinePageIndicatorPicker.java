package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.BaseSavedState;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.viewpager.widget.ViewPager;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.styleable;

public class UnderlinePageIndicatorPicker extends View implements PageIndicator {
   private static final int INVALID_POINTER = -1;
   private int mActivePointerId;
   private int mColorUnderline;
   private int mCurrentPage;
   private boolean mIsDragging;
   private float mLastMotionX;
   private ViewPager.OnPageChangeListener mListener;
   private final Paint mPaint;
   private float mPositionOffset;
   private int mScrollState;
   private PickerLinearLayout mTitleView;
   private int mTouchSlop;
   private ViewPager mViewPager;
   private Paint rectPaint;

   public UnderlinePageIndicatorPicker(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public UnderlinePageIndicatorPicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mPaint = new Paint(1);
      this.mLastMotionX = -1.0F;
      this.mActivePointerId = -1;
      this.mTitleView = null;
   }

   public UnderlinePageIndicatorPicker(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mPaint = new Paint(1);
      this.mLastMotionX = -1.0F;
      this.mActivePointerId = -1;
      this.mTitleView = null;
      this.mColorUnderline = this.getResources().getColor(color.dialog_text_color_holo_dark);
      TypedArray var5 = var1.obtainStyledAttributes(var2, styleable.BetterPickersDialogFragment, var3, 0);
      this.mColorUnderline = var5.getColor(styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor, this.mColorUnderline);
      Paint var4 = new Paint();
      this.rectPaint = var4;
      var4.setAntiAlias(true);
      this.rectPaint.setStyle(Style.FILL);
      var5.recycle();
      this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(var1));
   }

   public int getSelectedColor() {
      return this.mPaint.getColor();
   }

   public void notifyDataSetChanged() {
      this.invalidate();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      int var7 = this.mViewPager.getAdapter().getCount();
      if (!this.isInEditMode()) {
         if (var7 != 0) {
            PickerLinearLayout var9 = this.mTitleView;
            if (var9 != null) {
               View var10 = var9.getViewAt(this.mCurrentPage);
               float var5 = (float)var10.getLeft();
               float var4 = (float)var10.getRight();
               float var3 = var5;
               float var2 = var4;
               if (this.mPositionOffset > 0.0F) {
                  int var8 = this.mCurrentPage;
                  var3 = var5;
                  var2 = var4;
                  if (var8 < var7 - 1) {
                     var10 = this.mTitleView.getViewAt(var8 + 1);
                     var3 = (float)var10.getLeft();
                     var2 = (float)var10.getRight();
                     float var6 = this.mPositionOffset;
                     var3 = var6 * var3 + (1.0F - var6) * var5;
                     var2 = var6 * var2 + (1.0F - var6) * var4;
                  }
               }

               var1.drawRect(var3, (float)this.getPaddingBottom(), var2, (float)(this.getHeight() - this.getPaddingBottom()), this.mPaint);
            }

         }
      }
   }

   public void onPageScrollStateChanged(int var1) {
      this.mScrollState = var1;
      ViewPager.OnPageChangeListener var2 = this.mListener;
      if (var2 != null) {
         var2.onPageScrollStateChanged(var1);
      }

   }

   public void onPageScrolled(int var1, float var2, int var3) {
      this.mCurrentPage = var1;
      this.mPositionOffset = var2;
      this.invalidate();
      ViewPager.OnPageChangeListener var4 = this.mListener;
      if (var4 != null) {
         var4.onPageScrolled(var1, var2, var3);
      }

   }

   public void onPageSelected(int var1) {
      if (this.mScrollState == 0) {
         this.mCurrentPage = var1;
         this.mPositionOffset = 0.0F;
         this.invalidate();
      }

      ViewPager.OnPageChangeListener var2 = this.mListener;
      if (var2 != null) {
         var2.onPageSelected(var1);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      UnderlinePageIndicatorPicker.SavedState var2 = (UnderlinePageIndicatorPicker.SavedState)var1;
      super.onRestoreInstanceState(var2.getSuperState());
      this.mCurrentPage = var2.currentPage;
      this.requestLayout();
   }

   public Parcelable onSaveInstanceState() {
      UnderlinePageIndicatorPicker.SavedState var1 = new UnderlinePageIndicatorPicker.SavedState(super.onSaveInstanceState());
      var1.currentPage = this.mCurrentPage;
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (super.onTouchEvent(var1)) {
         return true;
      } else {
         ViewPager var7 = this.mViewPager;
         byte var4 = 0;
         if (var7 != null) {
            if (var7.getAdapter().getCount() == 0) {
               return false;
            } else {
               int var5 = var1.getAction() & 255;
               if (var5 != 0) {
                  float var2;
                  float var3;
                  int var8;
                  if (var5 != 1) {
                     if (var5 == 2) {
                        var2 = MotionEventCompat.getX(var1, MotionEventCompat.findPointerIndex(var1, this.mActivePointerId));
                        var3 = var2 - this.mLastMotionX;
                        if (!this.mIsDragging && Math.abs(var3) > (float)this.mTouchSlop) {
                           this.mIsDragging = true;
                        }

                        if (!this.mIsDragging) {
                           return true;
                        }

                        this.mLastMotionX = var2;
                        if (!this.mViewPager.isFakeDragging() && !this.mViewPager.beginFakeDrag()) {
                           return true;
                        }

                        this.mViewPager.fakeDragBy(var3);
                        return true;
                     }

                     if (var5 != 3) {
                        if (var5 != 5) {
                           if (var5 != 6) {
                              return true;
                           }

                           var5 = MotionEventCompat.getActionIndex(var1);
                           if (MotionEventCompat.getPointerId(var1, var5) == this.mActivePointerId) {
                              if (var5 == 0) {
                                 var4 = 1;
                              }

                              this.mActivePointerId = MotionEventCompat.getPointerId(var1, var4);
                           }

                           this.mLastMotionX = MotionEventCompat.getX(var1, MotionEventCompat.findPointerIndex(var1, this.mActivePointerId));
                           return true;
                        }

                        var8 = MotionEventCompat.getActionIndex(var1);
                        this.mLastMotionX = MotionEventCompat.getX(var1, var8);
                        this.mActivePointerId = MotionEventCompat.getPointerId(var1, var8);
                        return true;
                     }
                  }

                  if (!this.mIsDragging) {
                     var8 = this.mViewPager.getAdapter().getCount();
                     int var6 = this.getWidth();
                     var2 = (float)var6 / 2.0F;
                     var3 = (float)var6 / 6.0F;
                     if (this.mCurrentPage > 0 && var1.getX() < var2 - var3) {
                        if (var5 != 3) {
                           this.mViewPager.setCurrentItem(this.mCurrentPage - 1);
                        }

                        return true;
                     }

                     if (this.mCurrentPage < var8 - 1 && var1.getX() > var2 + var3) {
                        if (var5 != 3) {
                           this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                        }

                        return true;
                     }
                  }

                  this.mIsDragging = false;
                  this.mActivePointerId = -1;
                  if (this.mViewPager.isFakeDragging()) {
                     this.mViewPager.endFakeDrag();
                     return true;
                  }
               } else {
                  this.mActivePointerId = MotionEventCompat.getPointerId(var1, 0);
                  this.mLastMotionX = var1.getX();
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void setCurrentItem(int var1) {
      ViewPager var2 = this.mViewPager;
      if (var2 != null) {
         var2.setCurrentItem(var1);
         this.mCurrentPage = var1;
         this.invalidate();
      } else {
         throw new IllegalStateException("ViewPager has not been bound.");
      }
   }

   public void setOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      this.mListener = var1;
   }

   public void setSelectedColor(int var1) {
      this.mPaint.setColor(var1);
      this.invalidate();
   }

   public void setTitleView(PickerLinearLayout var1) {
      this.mTitleView = var1;
      this.invalidate();
   }

   public void setViewPager(ViewPager var1) {
      ViewPager var2 = this.mViewPager;
      if (var2 != var1) {
         if (var2 != null) {
            var2.setOnPageChangeListener((ViewPager.OnPageChangeListener)null);
         }

         if (var1.getAdapter() != null) {
            this.mViewPager = var1;
            var1.setOnPageChangeListener(this);
            this.invalidate();
         } else {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
         }
      }
   }

   public void setViewPager(ViewPager var1, int var2) {
      this.setViewPager(var1);
      this.setCurrentItem(var2);
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public UnderlinePageIndicatorPicker.SavedState createFromParcel(Parcel var1) {
            return new UnderlinePageIndicatorPicker.SavedState(var1);
         }

         public UnderlinePageIndicatorPicker.SavedState[] newArray(int var1) {
            return new UnderlinePageIndicatorPicker.SavedState[var1];
         }
      };
      int currentPage;

      private SavedState(Parcel var1) {
         super(var1);
         this.currentPage = var1.readInt();
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.currentPage);
      }
   }
}
