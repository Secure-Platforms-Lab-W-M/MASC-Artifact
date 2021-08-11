package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import com.codetroopers.betterpickers.Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class DayPickerView extends ListView implements OnScrollListener, CalendarDatePickerDialogFragment.OnDateChangedListener {
   public static final int DAYS_PER_WEEK = 7;
   protected static final int GOTO_SCROLL_DURATION = 250;
   public static final int LIST_TOP_OFFSET = -1;
   protected static final int SCROLL_CHANGE_DELAY = 40;
   protected static final int SCROLL_HYST_WEEKS = 2;
   private static final String TAG = "MonthFragment";
   private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
   protected MonthAdapter mAdapter;
   protected Context mContext;
   private CalendarDatePickerController mController;
   protected int mCurrentMonthDisplayed;
   protected int mCurrentScrollState = 0;
   protected int mDaysPerWeek = 7;
   protected int mFirstDayOfWeek;
   protected float mFriction = 1.0F;
   protected Handler mHandler;
   protected int mNumWeeks = 6;
   private boolean mPerformingScroll;
   protected CharSequence mPrevMonthName;
   protected long mPreviousScrollPosition;
   protected int mPreviousScrollState = 0;
   protected DayPickerView.ScrollStateRunnable mScrollStateChangedRunnable = new DayPickerView.ScrollStateRunnable();
   protected MonthAdapter.CalendarDay mSelectedDay = new MonthAdapter.CalendarDay();
   protected boolean mShowWeekNumber = false;
   protected MonthAdapter.CalendarDay mTempDay = new MonthAdapter.CalendarDay();

   public DayPickerView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var1);
   }

   public DayPickerView(Context var1, CalendarDatePickerController var2) {
      super(var1);
      this.init(var1);
      this.setController(var2);
   }

   private MonthAdapter.CalendarDay findAccessibilityFocus() {
      int var2 = this.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         View var3 = this.getChildAt(var1);
         if (var3 instanceof MonthView) {
            MonthAdapter.CalendarDay var4 = ((MonthView)var3).getAccessibilityFocus();
            if (var4 != null) {
               if (VERSION.SDK_INT == 17) {
                  ((MonthView)var3).clearAccessibilityFocus();
               }

               return var4;
            }
         }
      }

      return null;
   }

   private String getMonthAndYearString(MonthAdapter.CalendarDay var1) {
      Calendar var2 = Calendar.getInstance();
      var2.set(var1.year, var1.month, var1.day);
      StringBuffer var3 = new StringBuffer();
      var3.append(var2.getDisplayName(2, 2, Locale.getDefault()));
      var3.append(" ");
      var3.append(YEAR_FORMAT.format(var2.getTime()));
      return var3.toString();
   }

   private boolean restoreAccessibilityFocus(MonthAdapter.CalendarDay var1) {
      if (var1 == null) {
         return false;
      } else {
         int var3 = this.getChildCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            View var4 = this.getChildAt(var2);
            if (var4 instanceof MonthView && ((MonthView)var4).restoreAccessibilityFocus(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public abstract MonthAdapter createMonthAdapter(Context var1, CalendarDatePickerController var2);

   public int getMostVisiblePosition() {
      int var7 = this.getFirstVisiblePosition();
      int var8 = this.getHeight();
      int var2 = 0;
      int var3 = 0;
      int var1 = 0;

      int var5;
      for(int var4 = 0; var4 < var8; var4 = var5) {
         View var9 = this.getChildAt(var1);
         if (var9 == null) {
            break;
         }

         var5 = var9.getBottom();
         int var6 = Math.min(var5, var8) - Math.max(0, var9.getTop());
         var4 = var2;
         if (var6 > var2) {
            var3 = var1;
            var4 = var6;
         }

         ++var1;
         var2 = var4;
      }

      return var7 + var3;
   }

   public boolean goTo(MonthAdapter.CalendarDay var1, boolean var2, boolean var3, boolean var4) {
      if (var3) {
         this.mSelectedDay.set(var1);
      }

      this.mTempDay.set(var1);
      int var7 = (var1.year - this.mController.getMinDate().year) * 12 + (var1.month - this.mController.getMinDate().month);
      int var5 = 0;

      View var9;
      while(true) {
         int var6 = var5 + 1;
         var9 = this.getChildAt(var5);
         if (var9 == null) {
            break;
         }

         var5 = var9.getTop();
         if (Log.isLoggable("MonthFragment", 3)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("child at ");
            var8.append(var6 - 1);
            var8.append(" has top ");
            var8.append(var5);
            Log.d("MonthFragment", var8.toString());
         }

         if (var5 >= 0) {
            break;
         }

         var5 = var6;
      }

      if (var9 != null) {
         var5 = this.getPositionForView(var9);
      } else {
         var5 = 0;
      }

      if (var3) {
         this.mAdapter.setSelectedDay(this.mSelectedDay);
      }

      if (Log.isLoggable("MonthFragment", 3)) {
         StringBuilder var10 = new StringBuilder();
         var10.append("GoTo position ");
         var10.append(var7);
         Log.d("MonthFragment", var10.toString());
      }

      if (var7 == var5 && !var4) {
         if (var3) {
            this.setMonthDisplayed(this.mSelectedDay);
         }
      } else {
         this.setMonthDisplayed(this.mTempDay);
         this.mPreviousScrollState = 2;
         if (var2 && VERSION.SDK_INT >= 11) {
            this.smoothScrollToPositionFromTop(var7, -1, 250);
            return true;
         }

         this.postSetSelection(var7);
      }

      return false;
   }

   public void init(Context var1) {
      this.mHandler = new Handler();
      this.setLayoutParams(new LayoutParams(-1, -1));
      this.setDrawSelectorOnTop(false);
      this.mContext = var1;
      this.setUpListView();
   }

   protected void layoutChildren() {
      MonthAdapter.CalendarDay var1 = this.findAccessibilityFocus();
      super.layoutChildren();
      if (this.mPerformingScroll) {
         this.mPerformingScroll = false;
      } else {
         this.restoreAccessibilityFocus(var1);
      }
   }

   public void onChange() {
      this.refreshAdapter();
   }

   public void onDateChanged() {
      this.goTo(this.mController.getSelectedDay(), false, true, true);
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      var1.setItemCount(-1);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.addAction(4096);
      var1.addAction(8192);
   }

   public void onScroll(AbsListView var1, int var2, int var3, int var4) {
      MonthView var5 = (MonthView)var1.getChildAt(0);
      if (var5 != null) {
         this.mPreviousScrollPosition = (long)(var1.getFirstVisiblePosition() * var5.getHeight() - var5.getBottom());
         this.mPreviousScrollState = this.mCurrentScrollState;
      }
   }

   public void onScrollStateChanged(AbsListView var1, int var2) {
      this.mScrollStateChangedRunnable.doScrollStateChange(var1, var2);
   }

   public boolean performAccessibilityAction(int var1, Bundle var2) {
      if (var1 != 4096 && var1 != 8192) {
         return super.performAccessibilityAction(var1, var2);
      } else {
         int var3 = this.getFirstVisiblePosition();
         MonthAdapter.CalendarDay var5 = new MonthAdapter.CalendarDay(var3 / 12 + this.mController.getMinDate().year, var3 % 12, 1);
         if (var1 == 4096) {
            ++var5.month;
            if (var5.month == 12) {
               var5.month = 0;
               ++var5.year;
            }
         } else if (var1 == 8192) {
            View var4 = this.getChildAt(0);
            if (var4 != null && var4.getTop() >= -1) {
               --var5.month;
               if (var5.month == -1) {
                  var5.month = 11;
                  --var5.year;
               }
            }
         }

         Utils.tryAccessibilityAnnounce(this, this.getMonthAndYearString(var5));
         this.goTo(var5, true, false, true);
         this.mPerformingScroll = true;
         return true;
      }
   }

   public void postSetSelection(final int var1) {
      this.clearFocus();
      this.post(new Runnable() {
         public void run() {
            DayPickerView.this.setSelection(var1);
         }
      });
      this.onScrollStateChanged(this, 0);
   }

   protected void refreshAdapter() {
      MonthAdapter var1 = this.mAdapter;
      if (var1 == null) {
         this.mAdapter = this.createMonthAdapter(this.getContext(), this.mController);
      } else {
         var1.setSelectedDay(this.mSelectedDay);
      }

      this.setAdapter(this.mAdapter);
   }

   public void setController(CalendarDatePickerController var1) {
      this.mController = var1;
      var1.registerOnDateChangedListener(this);
      this.refreshAdapter();
      this.onDateChanged();
   }

   protected void setMonthDisplayed(MonthAdapter.CalendarDay var1) {
      this.mCurrentMonthDisplayed = var1.month;
      this.invalidateViews();
   }

   public void setTheme(TypedArray var1) {
      MonthAdapter var2 = this.mAdapter;
      if (var2 != null) {
         var2.setTheme(var1);
      }

   }

   protected void setUpListView() {
      this.setCacheColorHint(0);
      this.setDivider((Drawable)null);
      this.setItemsCanFocus(true);
      this.setFastScrollEnabled(false);
      this.setVerticalScrollBarEnabled(false);
      this.setOnScrollListener(this);
      this.setFadingEdgeLength(0);
      if (VERSION.SDK_INT >= 11) {
         this.setFriction(ViewConfiguration.getScrollFriction() * this.mFriction);
      }

   }

   protected class ScrollStateRunnable implements Runnable {
      private int mNewState;

      public void doScrollStateChange(AbsListView var1, int var2) {
         DayPickerView.this.mHandler.removeCallbacks(this);
         this.mNewState = var2;
         DayPickerView.this.mHandler.postDelayed(this, 40L);
      }

      public void run() {
         DayPickerView.this.mCurrentScrollState = this.mNewState;
         if (Log.isLoggable("MonthFragment", 3)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("new scroll state: ");
            var5.append(this.mNewState);
            var5.append(" old state: ");
            var5.append(DayPickerView.this.mPreviousScrollState);
            Log.d("MonthFragment", var5.toString());
         }

         if (this.mNewState == 0 && DayPickerView.this.mPreviousScrollState != 0) {
            int var1 = DayPickerView.this.mPreviousScrollState;
            boolean var2 = true;
            if (var1 != 1) {
               DayPickerView.this.mPreviousScrollState = this.mNewState;
               var1 = 0;

               View var8;
               DayPickerView var9;
               for(var8 = DayPickerView.this.getChildAt(0); var8 != null && var8.getBottom() <= 0; var8 = var9.getChildAt(var1)) {
                  var9 = DayPickerView.this;
                  ++var1;
               }

               if (var8 == null) {
                  return;
               }

               var1 = DayPickerView.this.getFirstVisiblePosition();
               int var3 = DayPickerView.this.getLastVisiblePosition();
               boolean var6;
               if (var1 != 0 && var3 != DayPickerView.this.getCount() - 1) {
                  var6 = var2;
               } else {
                  var6 = false;
               }

               int var7 = var8.getTop();
               var3 = var8.getBottom();
               int var4 = DayPickerView.this.getHeight() / 2;
               if (var6 && var7 < -1) {
                  if (var3 > var4) {
                     DayPickerView.this.smoothScrollBy(var7, 250);
                  } else {
                     DayPickerView.this.smoothScrollBy(var3, 250);
                  }
               }

               return;
            }
         }

         DayPickerView.this.mPreviousScrollState = this.mNewState;
      }
   }
}
