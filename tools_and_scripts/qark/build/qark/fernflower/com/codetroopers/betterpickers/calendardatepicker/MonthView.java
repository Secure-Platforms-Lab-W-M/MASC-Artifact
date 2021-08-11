package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.dimen;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.styleable;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public abstract class MonthView extends View {
   protected static int DAY_SELECTED_CIRCLE_SIZE;
   protected static int DAY_SEPARATOR_WIDTH = 1;
   protected static final int DEFAULT_FOCUS_MONTH = -1;
   protected static int DEFAULT_HEIGHT = 32;
   protected static final int DEFAULT_NUM_DAYS = 7;
   protected static final int DEFAULT_NUM_ROWS = 6;
   protected static final int DEFAULT_SELECTED_DAY = -1;
   protected static final int DEFAULT_SHOW_WK_NUM = 0;
   protected static final int DEFAULT_WEEK_START = 1;
   private static final int DISABLED_DAY_SQUARE_ALPHA = 60;
   protected static final int MAX_NUM_ROWS = 6;
   protected static int MINI_DAY_NUMBER_TEXT_SIZE;
   protected static int MIN_HEIGHT = 10;
   protected static int MONTH_DAY_LABEL_TEXT_SIZE;
   protected static int MONTH_HEADER_SIZE;
   protected static int MONTH_LABEL_TEXT_SIZE;
   private static final int SELECTED_CIRCLE_ALPHA = 60;
   private static final String TAG = "MonthView";
   public static final String VIEW_PARAMS_FOCUS_MONTH = "focus_month";
   public static final String VIEW_PARAMS_HEIGHT = "height";
   public static final String VIEW_PARAMS_MONTH = "month";
   public static final String VIEW_PARAMS_NUM_DAYS = "num_days";
   public static final String VIEW_PARAMS_RANGE_MAX = "range_max";
   public static final String VIEW_PARAMS_RANGE_MIN = "range_min";
   public static final String VIEW_PARAMS_SELECTED_DAY = "selected_day";
   public static final String VIEW_PARAMS_SHOW_WK_NUM = "show_wk_num";
   public static final String VIEW_PARAMS_WEEK_START = "week_start";
   public static final String VIEW_PARAMS_YEAR = "year";
   protected static float mScale = 0.0F;
   private final Calendar mCalendar;
   protected int mDayBackgroundColorDisabled;
   private final Calendar mDayLabelCalendar;
   private int mDayOfWeekStart;
   private String mDayOfWeekTypeface;
   protected int mDayTextColorDisabled;
   protected int mDayTextColorEnabled;
   protected Paint mDisabledDaySquarePaint;
   protected SparseArray mDisabledDays;
   protected int mFirstJulianDay = -1;
   protected int mFirstMonth = -1;
   private final Formatter mFormatter;
   protected boolean mHasToday;
   protected int mLastMonth = -1;
   private boolean mLockAccessibilityDelegate;
   protected int mMonth;
   protected Paint mMonthDayLabelPaint;
   protected Paint mMonthNumPaint;
   protected Paint mMonthTitleBGPaint;
   protected int mMonthTitleColor;
   protected Paint mMonthTitlePaint;
   private String mMonthTitleTypeface;
   protected int mNumCells;
   protected int mNumDays;
   private int mNumRows;
   private MonthView.OnDayClickListener mOnDayClickListener;
   protected int mPadding = 0;
   protected int mRangeMax;
   protected int mRangeMin;
   protected int mRowHeight;
   protected Paint mSelectedCirclePaint;
   protected int mSelectedDay;
   protected int mSelectedLeft;
   protected int mSelectedRight;
   private final StringBuilder mStringBuilder;
   protected int mToday;
   protected int mTodayNumberColor;
   private final MonthView.MonthViewTouchHelper mTouchHelper;
   protected int mWeekStart;
   protected int mWidth;
   protected int mYear;

   public MonthView(Context var1) {
      super(var1);
      this.mRowHeight = DEFAULT_HEIGHT;
      this.mHasToday = false;
      this.mSelectedDay = -1;
      this.mToday = -1;
      this.mWeekStart = 1;
      this.mNumDays = 7;
      this.mNumCells = 7;
      this.mSelectedLeft = -1;
      this.mSelectedRight = -1;
      this.mRangeMin = -1;
      this.mRangeMax = -1;
      this.mNumRows = 6;
      this.mDayOfWeekStart = 0;
      Resources var2 = var1.getResources();
      this.mDayLabelCalendar = Calendar.getInstance();
      this.mCalendar = Calendar.getInstance();
      this.mDayOfWeekTypeface = var2.getString(string.day_of_week_label_typeface);
      this.mMonthTitleTypeface = var2.getString(string.sans_serif);
      this.mDayTextColorEnabled = var2.getColor(color.date_picker_text_normal);
      this.mDayTextColorDisabled = var2.getColor(color.date_picker_text_disabled);
      this.mTodayNumberColor = var2.getColor(color.bpBlue);
      this.mDayBackgroundColorDisabled = var2.getColor(color.bpDarker_red);
      this.mMonthTitleColor = var2.getColor(color.date_picker_text_normal);
      this.mStringBuilder = new StringBuilder(50);
      this.mFormatter = new Formatter(this.mStringBuilder, Locale.getDefault());
      MINI_DAY_NUMBER_TEXT_SIZE = var2.getDimensionPixelSize(dimen.day_number_size);
      MONTH_LABEL_TEXT_SIZE = var2.getDimensionPixelSize(dimen.month_label_size);
      MONTH_DAY_LABEL_TEXT_SIZE = var2.getDimensionPixelSize(dimen.month_day_label_text_size);
      MONTH_HEADER_SIZE = var2.getDimensionPixelOffset(dimen.month_list_item_header_height);
      DAY_SELECTED_CIRCLE_SIZE = var2.getDimensionPixelSize(dimen.day_number_select_circle_radius);
      this.mRowHeight = (var2.getDimensionPixelOffset(dimen.date_picker_view_animator_height) - MONTH_HEADER_SIZE) / 6;
      MonthView.MonthViewTouchHelper var3 = new MonthView.MonthViewTouchHelper(this);
      this.mTouchHelper = var3;
      ViewCompat.setAccessibilityDelegate(this, var3);
      ViewCompat.setImportantForAccessibility(this, 1);
      this.mLockAccessibilityDelegate = true;
      this.initView();
   }

   private int calculateNumRows() {
      int var1 = this.findDayOffset();
      int var3 = this.mNumCells;
      int var4 = this.mNumDays;
      int var2 = (var1 + var3) / var4;
      byte var5;
      if ((var3 + var1) % var4 > 0) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      return var5 + var2;
   }

   private void drawMonthDayLabels(Canvas var1) {
      int var3 = MONTH_HEADER_SIZE;
      int var4 = MONTH_DAY_LABEL_TEXT_SIZE / 2;
      int var5 = (this.mWidth - this.mPadding * 2) / (this.mNumDays * 2);
      int var2 = 0;

      while(true) {
         int var6 = this.mNumDays;
         if (var2 >= var6) {
            return;
         }

         int var7 = this.mWeekStart;
         int var8 = this.mPadding;
         this.mDayLabelCalendar.set(7, (var7 + var2) % var6);
         var1.drawText(this.mDayLabelCalendar.getDisplayName(7, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()), (float)((var2 * 2 + 1) * var5 + var8), (float)(var3 - var4), this.mMonthDayLabelPaint);
         ++var2;
      }
   }

   private void drawMonthTitle(Canvas var1) {
      int var2 = (this.mWidth + this.mPadding * 2) / 2;
      int var3 = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2;
      int var4 = MONTH_LABEL_TEXT_SIZE / 3;
      var1.drawText(this.getMonthAndYearString(), (float)var2, (float)(var3 + var4), this.mMonthTitlePaint);
   }

   private int findDayOffset() {
      int var2 = this.mDayOfWeekStart;
      int var1 = var2;
      if (var2 < this.mWeekStart) {
         var1 = var2 + this.mNumDays;
      }

      return var1 - this.mWeekStart;
   }

   private String getMonthAndYearString() {
      this.mStringBuilder.setLength(0);
      long var1 = this.mCalendar.getTimeInMillis();
      String var3 = DateUtils.formatDateRange(this.getContext(), this.mFormatter, var1, var1, 52, Time.getCurrentTimezone()).toString();
      StringBuilder var4 = new StringBuilder();
      var4.append(var3.substring(0, 1).toUpperCase());
      var4.append(var3.substring(1).toLowerCase());
      return var4.toString();
   }

   private boolean isDayInRange(int var1) {
      int var2 = this.mRangeMax;
      if (var2 < 0 || var1 <= var2) {
         var2 = this.mRangeMin;
         if (var2 < 0 || var1 >= var2) {
            return true;
         }
      }

      return false;
   }

   private void onDayClick(int var1) {
      MonthView.OnDayClickListener var2 = this.mOnDayClickListener;
      if (var2 != null) {
         var2.onDayClick(this, new MonthAdapter.CalendarDay(this.mYear, this.mMonth, var1));
      }

      this.mTouchHelper.sendEventForVirtualView(var1, 1);
   }

   private boolean sameDay(int var1, Time var2) {
      return this.mYear == var2.year && this.mMonth == var2.month && var1 == var2.monthDay;
   }

   public void clearAccessibilityFocus() {
      this.mTouchHelper.clearFocusedVirtualView();
   }

   public abstract void drawMonthDay(Canvas var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11);

   protected void drawMonthNums(Canvas var1) {
      int var3 = (this.mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2;
      int var4 = DAY_SEPARATOR_WIDTH;
      int var5 = MONTH_HEADER_SIZE;
      int var7 = (this.mWidth - this.mPadding * 2) / (this.mNumDays * 2);
      int var2 = this.findDayOffset();
      var4 = var3 - var4 + var5;

      for(var3 = 1; var3 <= this.mNumCells; var4 = var5) {
         boolean var10 = true;
         var5 = (var2 * 2 + 1) * var7 + this.mPadding;
         int var6 = this.mRowHeight;
         int var8 = var4 - ((MINI_DAY_NUMBER_TEXT_SIZE + var6) / 2 - DAY_SEPARATOR_WIDTH);
         int var9 = Utils.formatDisabledDayForKey(this.mYear, this.mMonth, var3);
         boolean var11 = this.isDayInRange(var3);
         SparseArray var12 = this.mDisabledDays;
         if (var12 != null) {
            if (!var11 || var12.indexOfKey(var9) >= 0) {
               var10 = false;
            }
         } else {
            var10 = var11;
         }

         this.drawMonthDay(var1, this.mYear, this.mMonth, var3, var5, var4, var5 - var7, var5 + var7, var8, var8 + var6, var10);
         var6 = var2 + 1;
         var5 = var4;
         var2 = var6;
         if (var6 == this.mNumDays) {
            var5 = var4 + this.mRowHeight;
            var2 = 0;
         }

         ++var3;
      }

   }

   public MonthAdapter.CalendarDay getAccessibilityFocus() {
      int var1 = this.mTouchHelper.getFocusedVirtualView();
      return var1 >= 0 ? new MonthAdapter.CalendarDay(this.mYear, this.mMonth, var1) : null;
   }

   public int getDayFromLocation(float var1, float var2) {
      int var3 = this.mPadding;
      if (var1 >= (float)var3) {
         int var4 = this.mWidth;
         int var5 = this.mPadding;
         if (var1 > (float)(var4 - var5)) {
            return -1;
         } else {
            int var6 = (int)(var2 - (float)MONTH_HEADER_SIZE) / this.mRowHeight;
            var3 = (int)((var1 - (float)var3) * (float)this.mNumDays / (float)(var4 - var3 - var5)) - this.findDayOffset() + 1 + this.mNumDays * var6;
            if (var3 >= 1) {
               return var3 > this.mNumCells ? -1 : var3;
            } else {
               return -1;
            }
         }
      } else {
         return -1;
      }
   }

   protected void initView() {
      Paint var1 = new Paint();
      this.mMonthTitlePaint = var1;
      var1.setFakeBoldText(true);
      this.mMonthTitlePaint.setAntiAlias(true);
      this.mMonthTitlePaint.setTextSize((float)MONTH_LABEL_TEXT_SIZE);
      this.mMonthTitlePaint.setTypeface(Typeface.create(this.mMonthTitleTypeface, 1));
      this.mMonthTitlePaint.setColor(this.mDayTextColorEnabled);
      this.mMonthTitlePaint.setTextAlign(Align.CENTER);
      this.mMonthTitlePaint.setStyle(Style.FILL);
      var1 = new Paint();
      this.mMonthTitleBGPaint = var1;
      var1.setFakeBoldText(true);
      this.mMonthTitleBGPaint.setAntiAlias(true);
      this.mMonthTitleBGPaint.setTextAlign(Align.CENTER);
      this.mMonthTitleBGPaint.setStyle(Style.FILL);
      var1 = new Paint();
      this.mSelectedCirclePaint = var1;
      var1.setFakeBoldText(true);
      this.mSelectedCirclePaint.setAntiAlias(true);
      this.mSelectedCirclePaint.setColor(this.mTodayNumberColor);
      this.mSelectedCirclePaint.setTextAlign(Align.CENTER);
      this.mSelectedCirclePaint.setStyle(Style.FILL);
      this.mSelectedCirclePaint.setAlpha(60);
      var1 = new Paint();
      this.mDisabledDaySquarePaint = var1;
      var1.setFakeBoldText(true);
      this.mDisabledDaySquarePaint.setAntiAlias(true);
      this.mDisabledDaySquarePaint.setColor(this.mDayBackgroundColorDisabled);
      this.mDisabledDaySquarePaint.setTextAlign(Align.CENTER);
      this.mDisabledDaySquarePaint.setStyle(Style.FILL);
      this.mDisabledDaySquarePaint.setAlpha(60);
      var1 = new Paint();
      this.mMonthDayLabelPaint = var1;
      var1.setAntiAlias(true);
      this.mMonthDayLabelPaint.setTextSize((float)MONTH_DAY_LABEL_TEXT_SIZE);
      this.mMonthDayLabelPaint.setColor(this.mDayTextColorEnabled);
      this.mMonthDayLabelPaint.setTypeface(Typeface.create(this.mDayOfWeekTypeface, 0));
      this.mMonthDayLabelPaint.setStyle(Style.FILL);
      this.mMonthDayLabelPaint.setTextAlign(Align.CENTER);
      this.mMonthDayLabelPaint.setFakeBoldText(true);
      var1 = new Paint();
      this.mMonthNumPaint = var1;
      var1.setAntiAlias(true);
      this.mMonthNumPaint.setTextSize((float)MINI_DAY_NUMBER_TEXT_SIZE);
      this.mMonthNumPaint.setStyle(Style.FILL);
      this.mMonthNumPaint.setTextAlign(Align.CENTER);
      this.mMonthNumPaint.setFakeBoldText(false);
   }

   protected void onDraw(Canvas var1) {
      this.drawMonthTitle(var1);
      this.drawMonthDayLabels(var1);
      this.drawMonthNums(var1);
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), this.mRowHeight * this.mNumRows + MONTH_HEADER_SIZE);
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.mWidth = var1;
      this.mTouchHelper.invalidateRoot();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getAction() != 1) {
         return true;
      } else {
         int var2 = this.getDayFromLocation(var1.getX(), var1.getY());
         if (var2 >= 0) {
            this.onDayClick(var2);
         }

         return true;
      }
   }

   public boolean restoreAccessibilityFocus(MonthAdapter.CalendarDay var1) {
      if (var1.year == this.mYear && var1.month == this.mMonth && var1.day <= this.mNumCells) {
         this.mTouchHelper.setFocusedVirtualView(var1.day);
         return true;
      } else {
         return false;
      }
   }

   public void reuse() {
      this.mNumRows = 6;
      this.requestLayout();
   }

   public void setAccessibilityDelegate(AccessibilityDelegate var1) {
      if (!this.mLockAccessibilityDelegate && VERSION.SDK_INT >= 14) {
         super.setAccessibilityDelegate(var1);
      }

   }

   public void setDisabledDays(SparseArray var1) {
      this.mDisabledDays = var1;
   }

   public void setMonthParams(HashMap var1) {
      if (!var1.containsKey("month") && !var1.containsKey("year")) {
         throw new InvalidParameterException("You must specify month and year for this view");
      } else {
         this.setTag(var1);
         int var2;
         int var3;
         if (var1.containsKey("height")) {
            var2 = (Integer)var1.get("height");
            this.mRowHeight = var2;
            var3 = MIN_HEIGHT;
            if (var2 < var3) {
               this.mRowHeight = var3;
            }
         }

         if (var1.containsKey("selected_day")) {
            this.mSelectedDay = (Integer)var1.get("selected_day");
         }

         if (var1.containsKey("range_min")) {
            this.mRangeMin = (Integer)var1.get("range_min");
         }

         if (var1.containsKey("range_max")) {
            this.mRangeMax = (Integer)var1.get("range_max");
         }

         this.mMonth = (Integer)var1.get("month");
         this.mYear = (Integer)var1.get("year");
         Time var4 = new Time(Time.getCurrentTimezone());
         var4.setToNow();
         this.mHasToday = false;
         this.mToday = -1;
         this.mCalendar.set(2, this.mMonth);
         this.mCalendar.set(1, this.mYear);
         this.mCalendar.set(5, 1);
         this.mDayOfWeekStart = this.mCalendar.get(7);
         if (var1.containsKey("week_start")) {
            this.mWeekStart = (Integer)var1.get("week_start");
         } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
         }

         this.mNumCells = Utils.getDaysInMonth(this.mMonth, this.mYear);

         for(var2 = 0; var2 < this.mNumCells; ++var2) {
            var3 = var2 + 1;
            if (this.sameDay(var3, var4)) {
               this.mHasToday = true;
               this.mToday = var3;
            }
         }

         this.mNumRows = this.calculateNumRows();
         this.mTouchHelper.invalidateRoot();
      }
   }

   public void setOnDayClickListener(MonthView.OnDayClickListener var1) {
      this.mOnDayClickListener = var1;
   }

   public void setTheme(TypedArray var1) {
      this.mMonthTitleColor = var1.getColor(styleable.BetterPickersDialogs_bpAmPmTextColor, ContextCompat.getColor(this.getContext(), color.ampm_text_color));
      this.mTodayNumberColor = var1.getColor(styleable.BetterPickersDialogs_bpBodySelectedTextColor, ContextCompat.getColor(this.getContext(), color.bpBlue));
      this.mDayTextColorEnabled = var1.getColor(styleable.BetterPickersDialogs_bpBodyUnselectedTextColor, ContextCompat.getColor(this.getContext(), color.date_picker_text_disabled));
      this.mDayTextColorDisabled = var1.getColor(styleable.BetterPickersDialogs_bpDisabledDayTextColor, ContextCompat.getColor(this.getContext(), color.date_picker_text_disabled));
      this.mDayBackgroundColorDisabled = var1.getColor(styleable.BetterPickersDialogs_bpDisabledDayBackgroundColor, ContextCompat.getColor(this.getContext(), color.bpDarker_red));
      this.initView();
   }

   private class MonthViewTouchHelper extends ExploreByTouchHelper {
      private static final String DATE_FORMAT = "dd MMMM yyyy";
      private final Calendar mTempCalendar = Calendar.getInstance();
      private final Rect mTempRect = new Rect();

      public MonthViewTouchHelper(View var2) {
         super(var2);
      }

      private void getItemBounds(int var1, Rect var2) {
         int var6 = MonthView.this.mPadding;
         int var5 = MonthView.MONTH_HEADER_SIZE;
         int var3 = MonthView.this.mRowHeight;
         int var4 = (MonthView.this.mWidth - MonthView.this.mPadding * 2) / MonthView.this.mNumDays;
         int var7 = var1 - 1 + MonthView.this.findDayOffset();
         var1 = var7 / MonthView.this.mNumDays;
         var6 += var7 % MonthView.this.mNumDays * var4;
         var1 = var1 * var3 + var5;
         var2.set(var6, var1, var6 + var4, var1 + var3);
      }

      private CharSequence getItemDescription(int var1) {
         this.mTempCalendar.set(MonthView.this.mYear, MonthView.this.mMonth, var1);
         CharSequence var2 = DateFormat.format("dd MMMM yyyy", this.mTempCalendar.getTimeInMillis());
         return (CharSequence)(var1 == MonthView.this.mSelectedDay ? MonthView.this.getContext().getString(string.item_is_selected, new Object[]{var2}) : var2);
      }

      public void clearFocusedVirtualView() {
         int var1 = this.getFocusedVirtualView();
         if (var1 != Integer.MIN_VALUE) {
            this.getAccessibilityNodeProvider(MonthView.this).performAction(var1, 128, (Bundle)null);
         }

      }

      protected int getVirtualViewAt(float var1, float var2) {
         int var3 = MonthView.this.getDayFromLocation(var1, var2);
         return var3 >= 0 ? var3 : Integer.MIN_VALUE;
      }

      protected void getVisibleVirtualViews(List var1) {
         for(int var2 = 1; var2 <= MonthView.this.mNumCells; ++var2) {
            var1.add(var2);
         }

      }

      protected boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3) {
         if (var2 != 16) {
            return false;
         } else {
            MonthView.this.onDayClick(var1);
            return true;
         }
      }

      protected void onPopulateEventForVirtualView(int var1, AccessibilityEvent var2) {
         var2.setContentDescription(this.getItemDescription(var1));
      }

      protected void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2) {
         this.getItemBounds(var1, this.mTempRect);
         var2.setContentDescription(this.getItemDescription(var1));
         var2.setBoundsInParent(this.mTempRect);
         var2.addAction(16);
         if (var1 == MonthView.this.mSelectedDay) {
            var2.setSelected(true);
         }

      }

      public void setFocusedVirtualView(int var1) {
         this.getAccessibilityNodeProvider(MonthView.this).performAction(var1, 64, (Bundle)null);
      }
   }

   public interface OnDayClickListener {
      void onDayClick(MonthView var1, MonthAdapter.CalendarDay var2);
   }
}
