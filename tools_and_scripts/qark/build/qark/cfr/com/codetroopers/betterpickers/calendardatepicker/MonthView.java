/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.format.DateFormat
 *  android.text.format.DateUtils
 *  android.text.format.Time
 *  android.util.SparseArray
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.View$MeasureSpec
 *  android.view.accessibility.AccessibilityEvent
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$dimen
 *  com.codetroopers.betterpickers.R$string
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public abstract class MonthView
extends View {
    protected static int DAY_SELECTED_CIRCLE_SIZE = 0;
    protected static int DAY_SEPARATOR_WIDTH = 0;
    protected static final int DEFAULT_FOCUS_MONTH = -1;
    protected static int DEFAULT_HEIGHT = 0;
    protected static final int DEFAULT_NUM_DAYS = 7;
    protected static final int DEFAULT_NUM_ROWS = 6;
    protected static final int DEFAULT_SELECTED_DAY = -1;
    protected static final int DEFAULT_SHOW_WK_NUM = 0;
    protected static final int DEFAULT_WEEK_START = 1;
    private static final int DISABLED_DAY_SQUARE_ALPHA = 60;
    protected static final int MAX_NUM_ROWS = 6;
    protected static int MINI_DAY_NUMBER_TEXT_SIZE = 0;
    protected static int MIN_HEIGHT = 0;
    protected static int MONTH_DAY_LABEL_TEXT_SIZE = 0;
    protected static int MONTH_HEADER_SIZE = 0;
    protected static int MONTH_LABEL_TEXT_SIZE = 0;
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
    protected static float mScale;
    private final Calendar mCalendar;
    protected int mDayBackgroundColorDisabled;
    private final Calendar mDayLabelCalendar;
    private int mDayOfWeekStart = 0;
    private String mDayOfWeekTypeface;
    protected int mDayTextColorDisabled;
    protected int mDayTextColorEnabled;
    protected Paint mDisabledDaySquarePaint;
    protected SparseArray<MonthAdapter.CalendarDay> mDisabledDays;
    protected int mFirstJulianDay = -1;
    protected int mFirstMonth = -1;
    private final Formatter mFormatter;
    protected boolean mHasToday = false;
    protected int mLastMonth = -1;
    private boolean mLockAccessibilityDelegate;
    protected int mMonth;
    protected Paint mMonthDayLabelPaint;
    protected Paint mMonthNumPaint;
    protected Paint mMonthTitleBGPaint;
    protected int mMonthTitleColor;
    protected Paint mMonthTitlePaint;
    private String mMonthTitleTypeface;
    protected int mNumCells = 7;
    protected int mNumDays = 7;
    private int mNumRows = 6;
    private OnDayClickListener mOnDayClickListener;
    protected int mPadding = 0;
    protected int mRangeMax = -1;
    protected int mRangeMin = -1;
    protected int mRowHeight = DEFAULT_HEIGHT;
    protected Paint mSelectedCirclePaint;
    protected int mSelectedDay = -1;
    protected int mSelectedLeft = -1;
    protected int mSelectedRight = -1;
    private final StringBuilder mStringBuilder;
    protected int mToday = -1;
    protected int mTodayNumberColor;
    private final MonthViewTouchHelper mTouchHelper;
    protected int mWeekStart = 1;
    protected int mWidth;
    protected int mYear;

    static {
        DEFAULT_HEIGHT = 32;
        MIN_HEIGHT = 10;
        DAY_SEPARATOR_WIDTH = 1;
        mScale = 0.0f;
    }

    public MonthView(Context object) {
        super((Context)object);
        object = object.getResources();
        this.mDayLabelCalendar = Calendar.getInstance();
        this.mCalendar = Calendar.getInstance();
        this.mDayOfWeekTypeface = object.getString(R.string.day_of_week_label_typeface);
        this.mMonthTitleTypeface = object.getString(R.string.sans_serif);
        this.mDayTextColorEnabled = object.getColor(R.color.date_picker_text_normal);
        this.mDayTextColorDisabled = object.getColor(R.color.date_picker_text_disabled);
        this.mTodayNumberColor = object.getColor(R.color.bpBlue);
        this.mDayBackgroundColorDisabled = object.getColor(R.color.bpDarker_red);
        this.mMonthTitleColor = object.getColor(R.color.date_picker_text_normal);
        this.mStringBuilder = new StringBuilder(50);
        this.mFormatter = new Formatter(this.mStringBuilder, Locale.getDefault());
        MINI_DAY_NUMBER_TEXT_SIZE = object.getDimensionPixelSize(R.dimen.day_number_size);
        MONTH_LABEL_TEXT_SIZE = object.getDimensionPixelSize(R.dimen.month_label_size);
        MONTH_DAY_LABEL_TEXT_SIZE = object.getDimensionPixelSize(R.dimen.month_day_label_text_size);
        MONTH_HEADER_SIZE = object.getDimensionPixelOffset(R.dimen.month_list_item_header_height);
        DAY_SELECTED_CIRCLE_SIZE = object.getDimensionPixelSize(R.dimen.day_number_select_circle_radius);
        this.mRowHeight = (object.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height) - MONTH_HEADER_SIZE) / 6;
        this.mTouchHelper = object = new MonthViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, (AccessibilityDelegateCompat)object);
        ViewCompat.setImportantForAccessibility(this, 1);
        this.mLockAccessibilityDelegate = true;
        this.initView();
    }

    private int calculateNumRows() {
        int n = this.findDayOffset();
        int n2 = this.mNumCells;
        int n3 = this.mNumDays;
        int n4 = (n + n2) / n3;
        n = (n2 + n) % n3 > 0 ? 1 : 0;
        return n + n4;
    }

    private void drawMonthDayLabels(Canvas canvas) {
        int n;
        int n2 = MONTH_HEADER_SIZE;
        int n3 = MONTH_DAY_LABEL_TEXT_SIZE / 2;
        int n4 = (this.mWidth - this.mPadding * 2) / (this.mNumDays * 2);
        for (int i = 0; i < (n = this.mNumDays); ++i) {
            int n5 = this.mWeekStart;
            int n6 = this.mPadding;
            this.mDayLabelCalendar.set(7, (n5 + i) % n);
            canvas.drawText(this.mDayLabelCalendar.getDisplayName(7, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()), (float)((i * 2 + 1) * n4 + n6), (float)(n2 - n3), this.mMonthDayLabelPaint);
        }
    }

    private void drawMonthTitle(Canvas canvas) {
        int n = (this.mWidth + this.mPadding * 2) / 2;
        int n2 = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2;
        int n3 = MONTH_LABEL_TEXT_SIZE / 3;
        canvas.drawText(this.getMonthAndYearString(), (float)n, (float)(n2 + n3), this.mMonthTitlePaint);
    }

    private int findDayOffset() {
        int n;
        int n2 = n = this.mDayOfWeekStart;
        if (n < this.mWeekStart) {
            n2 = n + this.mNumDays;
        }
        return n2 - this.mWeekStart;
    }

    private String getMonthAndYearString() {
        this.mStringBuilder.setLength(0);
        long l = this.mCalendar.getTimeInMillis();
        String string2 = DateUtils.formatDateRange((Context)this.getContext(), (Formatter)this.mFormatter, (long)l, (long)l, (int)52, (String)Time.getCurrentTimezone()).toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.substring(0, 1).toUpperCase());
        stringBuilder.append(string2.substring(1).toLowerCase());
        return stringBuilder.toString();
    }

    private boolean isDayInRange(int n) {
        int n2 = this.mRangeMax;
        if (n2 >= 0 && n > n2 || (n2 = this.mRangeMin) >= 0 && n < n2) {
            return false;
        }
        return true;
    }

    private void onDayClick(int n) {
        OnDayClickListener onDayClickListener = this.mOnDayClickListener;
        if (onDayClickListener != null) {
            onDayClickListener.onDayClick(this, new MonthAdapter.CalendarDay(this.mYear, this.mMonth, n));
        }
        this.mTouchHelper.sendEventForVirtualView(n, 1);
    }

    private boolean sameDay(int n, Time time) {
        if (this.mYear == time.year && this.mMonth == time.month && n == time.monthDay) {
            return true;
        }
        return false;
    }

    public void clearAccessibilityFocus() {
        this.mTouchHelper.clearFocusedVirtualView();
    }

    public abstract void drawMonthDay(Canvas var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11);

    protected void drawMonthNums(Canvas canvas) {
        int n = (this.mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2;
        int n2 = DAY_SEPARATOR_WIDTH;
        int n3 = MONTH_HEADER_SIZE;
        int n4 = (this.mWidth - this.mPadding * 2) / (this.mNumDays * 2);
        int n5 = this.findDayOffset();
        n2 = n - n2 + n3;
        for (n = 1; n <= this.mNumCells; ++n) {
            boolean bl = true;
            n3 = (n5 * 2 + 1) * n4 + this.mPadding;
            int n6 = this.mRowHeight;
            int n7 = n2 - ((MINI_DAY_NUMBER_TEXT_SIZE + n6) / 2 - DAY_SEPARATOR_WIDTH);
            int n8 = Utils.formatDisabledDayForKey(this.mYear, this.mMonth, n);
            boolean bl2 = this.isDayInRange(n);
            SparseArray<MonthAdapter.CalendarDay> sparseArray = this.mDisabledDays;
            if (sparseArray != null) {
                if (!bl2 || sparseArray.indexOfKey(n8) >= 0) {
                    bl = false;
                }
            } else {
                bl = bl2;
            }
            this.drawMonthDay(canvas, this.mYear, this.mMonth, n, n3, n2, n3 - n4, n3 + n4, n7, n7 + n6, bl);
            n6 = n5 + 1;
            n3 = n2;
            n5 = n6;
            if (n6 == this.mNumDays) {
                n3 = n2 + this.mRowHeight;
                n5 = 0;
            }
            n2 = n3;
        }
    }

    public MonthAdapter.CalendarDay getAccessibilityFocus() {
        int n = this.mTouchHelper.getFocusedVirtualView();
        if (n >= 0) {
            return new MonthAdapter.CalendarDay(this.mYear, this.mMonth, n);
        }
        return null;
    }

    public int getDayFromLocation(float f, float f2) {
        int n = this.mPadding;
        if (f >= (float)n) {
            int n2 = this.mWidth;
            int n3 = this.mPadding;
            if (f > (float)(n2 - n3)) {
                return -1;
            }
            int n4 = (int)(f2 - (float)MONTH_HEADER_SIZE) / this.mRowHeight;
            if ((n = (int)((f - (float)n) * (float)this.mNumDays / (float)(n2 - n - n3)) - this.findDayOffset() + 1 + this.mNumDays * n4) >= 1) {
                if (n > this.mNumCells) {
                    return -1;
                }
                return n;
            }
            return -1;
        }
        return -1;
    }

    protected void initView() {
        Paint paint;
        this.mMonthTitlePaint = paint = new Paint();
        paint.setFakeBoldText(true);
        this.mMonthTitlePaint.setAntiAlias(true);
        this.mMonthTitlePaint.setTextSize((float)MONTH_LABEL_TEXT_SIZE);
        this.mMonthTitlePaint.setTypeface(Typeface.create((String)this.mMonthTitleTypeface, (int)1));
        this.mMonthTitlePaint.setColor(this.mDayTextColorEnabled);
        this.mMonthTitlePaint.setTextAlign(Paint.Align.CENTER);
        this.mMonthTitlePaint.setStyle(Paint.Style.FILL);
        this.mMonthTitleBGPaint = paint = new Paint();
        paint.setFakeBoldText(true);
        this.mMonthTitleBGPaint.setAntiAlias(true);
        this.mMonthTitleBGPaint.setTextAlign(Paint.Align.CENTER);
        this.mMonthTitleBGPaint.setStyle(Paint.Style.FILL);
        this.mSelectedCirclePaint = paint = new Paint();
        paint.setFakeBoldText(true);
        this.mSelectedCirclePaint.setAntiAlias(true);
        this.mSelectedCirclePaint.setColor(this.mTodayNumberColor);
        this.mSelectedCirclePaint.setTextAlign(Paint.Align.CENTER);
        this.mSelectedCirclePaint.setStyle(Paint.Style.FILL);
        this.mSelectedCirclePaint.setAlpha(60);
        this.mDisabledDaySquarePaint = paint = new Paint();
        paint.setFakeBoldText(true);
        this.mDisabledDaySquarePaint.setAntiAlias(true);
        this.mDisabledDaySquarePaint.setColor(this.mDayBackgroundColorDisabled);
        this.mDisabledDaySquarePaint.setTextAlign(Paint.Align.CENTER);
        this.mDisabledDaySquarePaint.setStyle(Paint.Style.FILL);
        this.mDisabledDaySquarePaint.setAlpha(60);
        this.mMonthDayLabelPaint = paint = new Paint();
        paint.setAntiAlias(true);
        this.mMonthDayLabelPaint.setTextSize((float)MONTH_DAY_LABEL_TEXT_SIZE);
        this.mMonthDayLabelPaint.setColor(this.mDayTextColorEnabled);
        this.mMonthDayLabelPaint.setTypeface(Typeface.create((String)this.mDayOfWeekTypeface, (int)0));
        this.mMonthDayLabelPaint.setStyle(Paint.Style.FILL);
        this.mMonthDayLabelPaint.setTextAlign(Paint.Align.CENTER);
        this.mMonthDayLabelPaint.setFakeBoldText(true);
        this.mMonthNumPaint = paint = new Paint();
        paint.setAntiAlias(true);
        this.mMonthNumPaint.setTextSize((float)MINI_DAY_NUMBER_TEXT_SIZE);
        this.mMonthNumPaint.setStyle(Paint.Style.FILL);
        this.mMonthNumPaint.setTextAlign(Paint.Align.CENTER);
        this.mMonthNumPaint.setFakeBoldText(false);
    }

    protected void onDraw(Canvas canvas) {
        this.drawMonthTitle(canvas);
        this.drawMonthDayLabels(canvas);
        this.drawMonthNums(canvas);
    }

    protected void onMeasure(int n, int n2) {
        this.setMeasuredDimension(View.MeasureSpec.getSize((int)n), this.mRowHeight * this.mNumRows + MONTH_HEADER_SIZE);
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        this.mWidth = n;
        this.mTouchHelper.invalidateRoot();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 1) {
            return true;
        }
        int n = this.getDayFromLocation(motionEvent.getX(), motionEvent.getY());
        if (n >= 0) {
            this.onDayClick(n);
        }
        return true;
    }

    public boolean restoreAccessibilityFocus(MonthAdapter.CalendarDay calendarDay) {
        if (calendarDay.year == this.mYear && calendarDay.month == this.mMonth && calendarDay.day <= this.mNumCells) {
            this.mTouchHelper.setFocusedVirtualView(calendarDay.day);
            return true;
        }
        return false;
    }

    public void reuse() {
        this.mNumRows = 6;
        this.requestLayout();
    }

    public void setAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate) {
        if (!this.mLockAccessibilityDelegate && Build.VERSION.SDK_INT >= 14) {
            super.setAccessibilityDelegate(accessibilityDelegate);
        }
    }

    public void setDisabledDays(SparseArray<MonthAdapter.CalendarDay> sparseArray) {
        this.mDisabledDays = sparseArray;
    }

    public void setMonthParams(HashMap<String, Integer> hashMap) {
        int n;
        int n2;
        if (!hashMap.containsKey("month") && !hashMap.containsKey("year")) {
            throw new InvalidParameterException("You must specify month and year for this view");
        }
        this.setTag(hashMap);
        if (hashMap.containsKey("height")) {
            this.mRowHeight = n2 = hashMap.get("height").intValue();
            n = MIN_HEIGHT;
            if (n2 < n) {
                this.mRowHeight = n;
            }
        }
        if (hashMap.containsKey("selected_day")) {
            this.mSelectedDay = hashMap.get("selected_day");
        }
        if (hashMap.containsKey("range_min")) {
            this.mRangeMin = hashMap.get("range_min");
        }
        if (hashMap.containsKey("range_max")) {
            this.mRangeMax = hashMap.get("range_max");
        }
        this.mMonth = hashMap.get("month");
        this.mYear = hashMap.get("year");
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        this.mHasToday = false;
        this.mToday = -1;
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        this.mDayOfWeekStart = this.mCalendar.get(7);
        this.mWeekStart = hashMap.containsKey("week_start") ? hashMap.get("week_start").intValue() : this.mCalendar.getFirstDayOfWeek();
        this.mNumCells = Utils.getDaysInMonth(this.mMonth, this.mYear);
        for (n2 = 0; n2 < this.mNumCells; ++n2) {
            n = n2 + 1;
            if (!this.sameDay(n, time)) continue;
            this.mHasToday = true;
            this.mToday = n;
        }
        this.mNumRows = this.calculateNumRows();
        this.mTouchHelper.invalidateRoot();
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.mOnDayClickListener = onDayClickListener;
    }

    public void setTheme(TypedArray typedArray) {
        this.mMonthTitleColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpAmPmTextColor, ContextCompat.getColor(this.getContext(), R.color.ampm_text_color));
        this.mTodayNumberColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpBodySelectedTextColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue));
        this.mDayTextColorEnabled = typedArray.getColor(R.styleable.BetterPickersDialogs_bpBodyUnselectedTextColor, ContextCompat.getColor(this.getContext(), R.color.date_picker_text_disabled));
        this.mDayTextColorDisabled = typedArray.getColor(R.styleable.BetterPickersDialogs_bpDisabledDayTextColor, ContextCompat.getColor(this.getContext(), R.color.date_picker_text_disabled));
        this.mDayBackgroundColorDisabled = typedArray.getColor(R.styleable.BetterPickersDialogs_bpDisabledDayBackgroundColor, ContextCompat.getColor(this.getContext(), R.color.bpDarker_red));
        this.initView();
    }

    private class MonthViewTouchHelper
    extends ExploreByTouchHelper {
        private static final String DATE_FORMAT = "dd MMMM yyyy";
        private final Calendar mTempCalendar;
        private final Rect mTempRect;

        public MonthViewTouchHelper(View view) {
            super(view);
            this.mTempRect = new Rect();
            this.mTempCalendar = Calendar.getInstance();
        }

        private void getItemBounds(int n, Rect rect) {
            int n2 = MonthView.this.mPadding;
            int n3 = MonthView.MONTH_HEADER_SIZE;
            int n4 = MonthView.this.mRowHeight;
            int n5 = (MonthView.this.mWidth - MonthView.this.mPadding * 2) / MonthView.this.mNumDays;
            int n6 = n - 1 + MonthView.this.findDayOffset();
            n = n6 / MonthView.this.mNumDays;
            n2 = n6 % MonthView.this.mNumDays * n5 + n2;
            n = n * n4 + n3;
            rect.set(n2, n, n2 + n5, n + n4);
        }

        private CharSequence getItemDescription(int n) {
            this.mTempCalendar.set(MonthView.this.mYear, MonthView.this.mMonth, n);
            CharSequence charSequence = DateFormat.format((CharSequence)"dd MMMM yyyy", (long)this.mTempCalendar.getTimeInMillis());
            if (n == MonthView.this.mSelectedDay) {
                return MonthView.this.getContext().getString(R.string.item_is_selected, new Object[]{charSequence});
            }
            return charSequence;
        }

        public void clearFocusedVirtualView() {
            int n = this.getFocusedVirtualView();
            if (n != Integer.MIN_VALUE) {
                this.getAccessibilityNodeProvider(MonthView.this).performAction(n, 128, null);
            }
        }

        @Override
        protected int getVirtualViewAt(float f, float f2) {
            int n = MonthView.this.getDayFromLocation(f, f2);
            if (n >= 0) {
                return n;
            }
            return Integer.MIN_VALUE;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i = 1; i <= MonthView.this.mNumCells; ++i) {
                list.add(i);
            }
        }

        @Override
        protected boolean onPerformActionForVirtualView(int n, int n2, Bundle bundle) {
            if (n2 != 16) {
                return false;
            }
            MonthView.this.onDayClick(n);
            return true;
        }

        @Override
        protected void onPopulateEventForVirtualView(int n, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(this.getItemDescription(n));
        }

        @Override
        protected void onPopulateNodeForVirtualView(int n, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.getItemBounds(n, this.mTempRect);
            accessibilityNodeInfoCompat.setContentDescription(this.getItemDescription(n));
            accessibilityNodeInfoCompat.setBoundsInParent(this.mTempRect);
            accessibilityNodeInfoCompat.addAction(16);
            if (n == MonthView.this.mSelectedDay) {
                accessibilityNodeInfoCompat.setSelected(true);
            }
        }

        public void setFocusedVirtualView(int n) {
            this.getAccessibilityNodeProvider(MonthView.this).performAction(n, 64, null);
        }
    }

    public static interface OnDayClickListener {
        public void onDayClick(MonthView var1, MonthAdapter.CalendarDay var2);
    }

}

