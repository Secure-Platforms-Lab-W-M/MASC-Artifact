package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R.id;
import java.util.Calendar;
import java.util.Iterator;

final class MaterialCalendarGridView extends GridView {
   private final Calendar dayCompute;

   public MaterialCalendarGridView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialCalendarGridView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public MaterialCalendarGridView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.dayCompute = UtcDates.getUtcCalendar();
      if (MaterialDatePicker.isFullscreen(this.getContext())) {
         this.setNextFocusLeftId(id.cancel_button);
         this.setNextFocusRightId(id.confirm_button);
      }

      ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setCollectionInfo((Object)null);
         }
      });
   }

   private void gainFocus(int var1, Rect var2) {
      if (var1 == 33) {
         this.setSelection(this.getAdapter().lastPositionInMonth());
      } else if (var1 == 130) {
         this.setSelection(this.getAdapter().firstPositionInMonth());
      } else {
         super.onFocusChanged(true, var1, var2);
      }
   }

   private static int horizontalMidPoint(View var0) {
      return var0.getLeft() + var0.getWidth() / 2;
   }

   private static boolean skipMonth(Long var0, Long var1, Long var2, Long var3) {
      boolean var4 = true;
      if (var0 != null && var1 != null && var2 != null) {
         if (var3 == null) {
            return true;
         } else {
            if (var2 <= var1) {
               if (var3 < var0) {
                  return true;
               }

               var4 = false;
            }

            return var4;
         }
      } else {
         return true;
      }
   }

   public MonthAdapter getAdapter() {
      return (MonthAdapter)super.getAdapter();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.getAdapter().notifyDataSetChanged();
   }

   protected final void onDraw(Canvas var1) {
      super.onDraw(var1);
      MonthAdapter var23 = this.getAdapter();
      DateSelector var24 = var23.dateSelector;
      CalendarStyle var26 = var23.calendarStyle;
      Long var22 = var23.getItem(var23.firstPositionInMonth());
      Long var21 = var23.getItem(var23.lastPositionInMonth());
      Iterator var20 = var24.getSelectedRanges().iterator();

      while(true) {
         Pair var27;
         do {
            do {
               if (!var20.hasNext()) {
                  return;
               }

               var27 = (Pair)var20.next();
            } while(var27.first == null);
         } while(var27.second == null);

         long var16 = (Long)var27.first;
         long var18 = (Long)var27.second;
         if (skipMonth(var22, var21, var16, var18)) {
            return;
         }

         int var2;
         int var4;
         if (var16 < var22) {
            var4 = var23.firstPositionInMonth();
            if (var23.isFirstInRow(var4)) {
               var2 = 0;
            } else {
               var2 = this.getChildAt(var4 - 1).getRight();
            }
         } else {
            this.dayCompute.setTimeInMillis(var16);
            var4 = var23.dayToPosition(this.dayCompute.get(5));
            var2 = horizontalMidPoint(this.getChildAt(var4));
         }

         int var3;
         int var5;
         if (var18 > var21) {
            var5 = var23.lastPositionInMonth();
            if (var23.isLastInRow(var5)) {
               var3 = this.getWidth();
            } else {
               var3 = this.getChildAt(var5).getRight();
            }
         } else {
            this.dayCompute.setTimeInMillis(var18);
            var5 = var23.dayToPosition(this.dayCompute.get(5));
            var3 = horizontalMidPoint(this.getChildAt(var5));
         }

         int var6 = (int)var23.getItemId(var4);
         int var10 = (int)var23.getItemId(var5);

         for(int var7 = var6; var7 <= var10; ++var7) {
            int var9 = var7 * this.getNumColumns();
            int var15 = this.getNumColumns();
            View var25 = this.getChildAt(var9);
            int var11 = var25.getTop();
            int var12 = var26.day.getTopInset();
            int var13 = var25.getBottom();
            int var14 = var26.day.getBottomInset();
            int var8;
            if (var9 > var4) {
               var8 = 0;
            } else {
               var8 = var2;
            }

            if (var5 > var9 + var15 - 1) {
               var9 = this.getWidth();
            } else {
               var9 = var3;
            }

            var1.drawRect((float)var8, (float)(var11 + var12), (float)var9, (float)(var13 - var14), var26.rangeFill);
         }
      }
   }

   protected void onFocusChanged(boolean var1, int var2, Rect var3) {
      if (var1) {
         this.gainFocus(var2, var3);
      } else {
         super.onFocusChanged(false, var2, var3);
      }
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (!super.onKeyDown(var1, var2)) {
         return false;
      } else if (this.getSelectedItemPosition() != -1) {
         if (this.getSelectedItemPosition() >= this.getAdapter().firstPositionInMonth()) {
            return true;
         } else if (19 == var1) {
            this.setSelection(this.getAdapter().firstPositionInMonth());
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public final void setAdapter(ListAdapter var1) {
      if (var1 instanceof MonthAdapter) {
         super.setAdapter(var1);
      } else {
         throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName()));
      }
   }

   public void setSelection(int var1) {
      if (var1 < this.getAdapter().firstPositionInMonth()) {
         super.setSelection(this.getAdapter().firstPositionInMonth());
      } else {
         super.setSelection(var1);
      }
   }
}
