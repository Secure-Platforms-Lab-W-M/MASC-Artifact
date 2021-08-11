package com.codetroopers.betterpickers.calendardatepicker;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.codetroopers.betterpickers.HapticFeedbackController;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.style;
import com.codetroopers.betterpickers.R.styleable;
import com.nineoldandroids.animation.ObjectAnimator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class CalendarDatePickerDialogFragment extends DialogFragment implements OnClickListener, CalendarDatePickerController {
   private static final int ANIMATION_DELAY = 500;
   private static final int ANIMATION_DURATION = 300;
   private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());
   private static final MonthAdapter.CalendarDay DEFAULT_END_DATE = new MonthAdapter.CalendarDay(2100, 11, 31);
   private static final MonthAdapter.CalendarDay DEFAULT_START_DATE = new MonthAdapter.CalendarDay(1900, 0, 1);
   private static final String KEY_CURRENT_VIEW = "current_view";
   private static final String KEY_DATE_END = "date_end";
   private static final String KEY_DATE_START = "date_start";
   private static final String KEY_DISABLED_DAYS = "disabled_days";
   private static final String KEY_LIST_POSITION = "list_position";
   private static final String KEY_LIST_POSITION_OFFSET = "list_position_offset";
   private static final String KEY_SELECTED_DAY = "day";
   private static final String KEY_SELECTED_MONTH = "month";
   private static final String KEY_SELECTED_YEAR = "year";
   private static final String KEY_THEME = "theme";
   private static final String KEY_WEEK_START = "week_start";
   private static final int MONTH_AND_DAY_VIEW = 0;
   private static final String TAG = "DatePickerDialog";
   private static final int UNINITIALIZED = -1;
   private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
   private static final int YEAR_VIEW = 1;
   private AccessibleDateAnimator mAnimator;
   private final Calendar mCalendar = Calendar.getInstance();
   private CalendarDatePickerDialogFragment.OnDateSetListener mCallBack;
   private String mCancelText;
   private int mCurrentView = -1;
   private TextView mDayOfWeekView;
   private String mDayPickerDescription;
   private DayPickerView mDayPickerView;
   private boolean mDelayAnimation;
   private OnDialogDismissListener mDimissCallback;
   private SparseArray mDisabledDays;
   private String mDoneText;
   private HapticFeedbackController mHapticFeedbackController;
   private HashSet mListeners = new HashSet();
   private MonthAdapter.CalendarDay mMaxDate;
   private MonthAdapter.CalendarDay mMinDate;
   private LinearLayout mMonthAndDayView;
   private String mSelectDay;
   private String mSelectYear;
   private int mSelectedColor;
   private LinearLayout mSelectedDateLayout;
   private TextView mSelectedDayTextView;
   private TextView mSelectedMonthTextView;
   private int mStyleResId;
   private int mUnselectedColor;
   private int mWeekStart;
   private String mYearPickerDescription;
   private YearPickerView mYearPickerView;
   private TextView mYearView;

   public CalendarDatePickerDialogFragment() {
      this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
      this.mMinDate = DEFAULT_START_DATE;
      this.mMaxDate = DEFAULT_END_DATE;
      this.mDelayAnimation = true;
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_PrimaryColor;
   }

   private void adjustDayInMonthIfNeeded(int var1, int var2) {
      int var3 = this.mCalendar.get(5);
      var1 = Utils.getDaysInMonth(var1, var2);
      if (var3 > var1) {
         this.mCalendar.set(5, var1);
      }

   }

   private void setCurrentView(int var1) {
      long var2 = this.mCalendar.getTimeInMillis();
      ObjectAnimator var4;
      AccessibleDateAnimator var5;
      StringBuilder var6;
      String var7;
      if (var1 != 0) {
         if (var1 == 1) {
            var4 = Utils.getPulseAnimator(this.mYearView, 0.85F, 1.1F);
            if (this.mDelayAnimation) {
               var4.setStartDelay(500L);
               this.mDelayAnimation = false;
            }

            this.mYearPickerView.onDateChanged();
            if (this.mCurrentView != var1) {
               this.mMonthAndDayView.setSelected(false);
               this.mYearView.setSelected(true);
               this.mSelectedDayTextView.setTextColor(this.mUnselectedColor);
               this.mSelectedMonthTextView.setTextColor(this.mUnselectedColor);
               this.mYearView.setTextColor(this.mSelectedColor);
               this.mAnimator.setDisplayedChild(1);
               this.mCurrentView = var1;
            }

            var4.start();
            var7 = YEAR_FORMAT.format(var2);
            var5 = this.mAnimator;
            var6 = new StringBuilder();
            var6.append(this.mYearPickerDescription);
            var6.append(": ");
            var6.append(var7);
            var5.setContentDescription(var6.toString());
            Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectYear);
         }
      } else {
         var4 = Utils.getPulseAnimator(this.mMonthAndDayView, 0.9F, 1.05F);
         if (this.mDelayAnimation) {
            var4.setStartDelay(500L);
            this.mDelayAnimation = false;
         }

         this.mDayPickerView.onDateChanged();
         if (this.mCurrentView != var1) {
            this.mMonthAndDayView.setSelected(true);
            this.mYearView.setSelected(false);
            this.mSelectedDayTextView.setTextColor(this.mSelectedColor);
            this.mSelectedMonthTextView.setTextColor(this.mSelectedColor);
            this.mYearView.setTextColor(this.mUnselectedColor);
            this.mAnimator.setDisplayedChild(0);
            this.mCurrentView = var1;
         }

         var4.start();
         var7 = DateUtils.formatDateTime(this.getActivity(), var2, 16);
         var5 = this.mAnimator;
         var6 = new StringBuilder();
         var6.append(this.mDayPickerDescription);
         var6.append(": ");
         var6.append(var7);
         var5.setContentDescription(var6.toString());
         Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectDay);
      }
   }

   private void updateDisplay(boolean var1) {
      TextView var4 = this.mDayOfWeekView;
      if (var4 != null) {
         var4.setText(this.mCalendar.getDisplayName(7, 2, Locale.getDefault()).toUpperCase(Locale.getDefault()));
      }

      this.mSelectedMonthTextView.setText(this.mCalendar.getDisplayName(2, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()));
      this.mSelectedDayTextView.setText(DAY_FORMAT.format(this.mCalendar.getTime()));
      this.mYearView.setText(YEAR_FORMAT.format(this.mCalendar.getTime()));
      long var2 = this.mCalendar.getTimeInMillis();
      this.mAnimator.setDateMillis(var2);
      String var5 = DateUtils.formatDateTime(this.getActivity(), var2, 24);
      this.mMonthAndDayView.setContentDescription(var5);
      if (var1) {
         var5 = DateUtils.formatDateTime(this.getActivity(), var2, 20);
         Utils.tryAccessibilityAnnounce(this.mAnimator, var5);
      }

   }

   private void updatePickers() {
      Iterator var1 = this.mListeners.iterator();

      while(var1.hasNext()) {
         ((CalendarDatePickerDialogFragment.OnDateChangedListener)var1.next()).onDateChanged();
      }

   }

   public SparseArray getDisabledDays() {
      return this.mDisabledDays;
   }

   public int getFirstDayOfWeek() {
      return this.mWeekStart;
   }

   public MonthAdapter.CalendarDay getMaxDate() {
      return this.mMaxDate;
   }

   public MonthAdapter.CalendarDay getMinDate() {
      return this.mMinDate;
   }

   public MonthAdapter.CalendarDay getSelectedDay() {
      return new MonthAdapter.CalendarDay(this.mCalendar);
   }

   public boolean isThemeDark() {
      return this.mStyleResId == style.BetterPickersRadialTimePickerDialog_Dark;
   }

   public void onClick(View var1) {
      this.tryVibrate();
      if (var1.getId() == id.date_picker_year) {
         this.setCurrentView(1);
      } else {
         if (var1.getId() == id.date_picker_month_and_day) {
            this.setCurrentView(0);
         }

      }
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.getActivity().getWindow().setSoftInputMode(3);
      if (var1 != null) {
         this.mCalendar.set(1, var1.getInt("year"));
         this.mCalendar.set(2, var1.getInt("month"));
         this.mCalendar.set(5, var1.getInt("day"));
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Log.d("DatePickerDialog", "onCreateView: ");
      if (this.getShowsDialog()) {
         this.getDialog().getWindow().requestFeature(1);
      }

      View var14 = var1.inflate(layout.calendar_date_picker_dialog, var2, false);
      this.mSelectedDateLayout = (LinearLayout)var14.findViewById(id.day_picker_selected_date_layout);
      this.mDayOfWeekView = (TextView)var14.findViewById(id.date_picker_header);
      LinearLayout var15 = (LinearLayout)var14.findViewById(id.date_picker_month_and_day);
      this.mMonthAndDayView = var15;
      var15.setOnClickListener(this);
      this.mSelectedMonthTextView = (TextView)var14.findViewById(id.date_picker_month);
      this.mSelectedDayTextView = (TextView)var14.findViewById(id.date_picker_day);
      TextView var16 = (TextView)var14.findViewById(id.date_picker_year);
      this.mYearView = var16;
      var16.setOnClickListener(this);
      int var4 = -1;
      int var5 = 0;
      int var6 = 0;
      if (var3 != null) {
         this.mWeekStart = var3.getInt("week_start");
         this.mMinDate = new MonthAdapter.CalendarDay(var3.getLong("date_start"));
         this.mMaxDate = new MonthAdapter.CalendarDay(var3.getLong("date_end"));
         var6 = var3.getInt("current_view");
         var4 = var3.getInt("list_position");
         var5 = var3.getInt("list_position_offset");
         this.mStyleResId = var3.getInt("theme");
         this.mDisabledDays = var3.getSparseParcelableArray("disabled_days");
      }

      FragmentActivity var17 = this.getActivity();
      this.mDayPickerView = new SimpleDayPickerView(var17, this);
      this.mYearPickerView = new YearPickerView(var17, this);
      Resources var12 = this.getResources();
      TypedArray var18 = this.getActivity().obtainStyledAttributes(this.mStyleResId, styleable.BetterPickersDialogs);
      this.mDayPickerDescription = var12.getString(string.day_picker_description);
      this.mSelectDay = var12.getString(string.select_day);
      this.mYearPickerDescription = var12.getString(string.year_picker_description);
      this.mSelectYear = var12.getString(string.select_year);
      int var7 = var18.getColor(styleable.BetterPickersDialogs_bpHeaderBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var8 = var18.getColor(styleable.BetterPickersDialogs_bpPreHeaderBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var9 = var18.getColor(styleable.BetterPickersDialogs_bpBodyBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var10 = var18.getColor(styleable.BetterPickersDialogs_bpButtonsBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var11 = var18.getColor(styleable.BetterPickersDialogs_bpButtonsTextColor, ContextCompat.getColor(this.getActivity(), color.bpBlue));
      this.mSelectedColor = var18.getColor(styleable.BetterPickersDialogs_bpHeaderSelectedTextColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      this.mUnselectedColor = var18.getColor(styleable.BetterPickersDialogs_bpHeaderUnselectedTextColor, ContextCompat.getColor(this.getActivity(), color.radial_gray_light));
      AccessibleDateAnimator var19 = (AccessibleDateAnimator)var14.findViewById(id.animator);
      this.mAnimator = var19;
      var19.addView(this.mDayPickerView);
      this.mAnimator.addView(this.mYearPickerView);
      this.mAnimator.setDateMillis(this.mCalendar.getTimeInMillis());
      AlphaAnimation var20 = new AlphaAnimation(0.0F, 1.0F);
      var20.setDuration(300L);
      this.mAnimator.setInAnimation(var20);
      var20 = new AlphaAnimation(1.0F, 0.0F);
      var20.setDuration(300L);
      this.mAnimator.setOutAnimation(var20);
      Button var21 = (Button)var14.findViewById(id.done_button);
      String var13 = this.mDoneText;
      if (var13 != null) {
         var21.setText(var13);
      }

      var21.setTextColor(var11);
      var21.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            CalendarDatePickerDialogFragment.this.tryVibrate();
            if (CalendarDatePickerDialogFragment.this.mCallBack != null) {
               CalendarDatePickerDialogFragment.OnDateSetListener var3 = CalendarDatePickerDialogFragment.this.mCallBack;
               CalendarDatePickerDialogFragment var2 = CalendarDatePickerDialogFragment.this;
               var3.onDateSet(var2, var2.mCalendar.get(1), CalendarDatePickerDialogFragment.this.mCalendar.get(2), CalendarDatePickerDialogFragment.this.mCalendar.get(5));
            }

            CalendarDatePickerDialogFragment.this.dismiss();
         }
      });
      var21 = (Button)var14.findViewById(id.cancel_button);
      var13 = this.mCancelText;
      if (var13 != null) {
         var21.setText(var13);
      }

      var21.setTextColor(var11);
      var21.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            CalendarDatePickerDialogFragment.this.tryVibrate();
            CalendarDatePickerDialogFragment.this.dismiss();
         }
      });
      var14.findViewById(id.ok_cancel_buttons_layout).setBackgroundColor(var10);
      this.updateDisplay(false);
      this.setCurrentView(var6);
      if (var4 != -1) {
         if (var6 == 0) {
            this.mDayPickerView.postSetSelection(var4);
         } else if (var6 == 1) {
            this.mYearPickerView.postSetSelectionFromTop(var4, var5);
         }
      }

      this.mHapticFeedbackController = new HapticFeedbackController(var17);
      this.mDayPickerView.setTheme(var18);
      this.mYearPickerView.setTheme(var18);
      this.mSelectedDateLayout.setBackgroundColor(var7);
      this.mYearView.setBackgroundColor(var7);
      this.mMonthAndDayView.setBackgroundColor(var7);
      var16 = this.mDayOfWeekView;
      if (var16 != null) {
         var16.setBackgroundColor(var8);
      }

      var14.setBackgroundColor(var9);
      this.mYearPickerView.setBackgroundColor(var9);
      this.mDayPickerView.setBackgroundColor(var9);
      return var14;
   }

   public void onDayOfMonthSelected(int var1, int var2, int var3) {
      this.mCalendar.set(1, var1);
      this.mCalendar.set(2, var2);
      this.mCalendar.set(5, var3);
      this.updatePickers();
      this.updateDisplay(true);
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      OnDialogDismissListener var2 = this.mDimissCallback;
      if (var2 != null) {
         var2.onDialogDismiss(var1);
      }

   }

   public void onPause() {
      super.onPause();
      this.mHapticFeedbackController.stop();
   }

   public void onResume() {
      super.onResume();
      this.mHapticFeedbackController.start();
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putInt("year", this.mCalendar.get(1));
      var1.putInt("month", this.mCalendar.get(2));
      var1.putInt("day", this.mCalendar.get(5));
      var1.putInt("week_start", this.mWeekStart);
      var1.putLong("date_start", this.mMinDate.getDateInMillis());
      var1.putLong("date_end", this.mMaxDate.getDateInMillis());
      var1.putInt("current_view", this.mCurrentView);
      var1.putInt("theme", this.mStyleResId);
      int var2 = -1;
      int var3 = this.mCurrentView;
      if (var3 == 0) {
         var2 = this.mDayPickerView.getMostVisiblePosition();
      } else if (var3 == 1) {
         var2 = this.mYearPickerView.getFirstVisiblePosition();
         var1.putInt("list_position_offset", this.mYearPickerView.getFirstPositionOffset());
      }

      var1.putInt("list_position", var2);
      var1.putSparseParcelableArray("disabled_days", this.mDisabledDays);
   }

   public void onYearSelected(int var1) {
      this.adjustDayInMonthIfNeeded(this.mCalendar.get(2), var1);
      this.mCalendar.set(1, var1);
      this.updatePickers();
      this.setCurrentView(0);
      this.updateDisplay(true);
   }

   public void registerOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1) {
      this.mListeners.add(var1);
   }

   public CalendarDatePickerDialogFragment setCancelText(String var1) {
      this.mCancelText = var1;
      return this;
   }

   public CalendarDatePickerDialogFragment setDateRange(MonthAdapter.CalendarDay var1, MonthAdapter.CalendarDay var2) {
      if (var1 == null) {
         this.mMinDate = DEFAULT_START_DATE;
      } else {
         this.mMinDate = var1;
      }

      if (var2 == null) {
         this.mMaxDate = DEFAULT_END_DATE;
      } else {
         this.mMaxDate = var2;
      }

      if (this.mMaxDate.compareTo(this.mMinDate) >= 0) {
         DayPickerView var3 = this.mDayPickerView;
         if (var3 != null) {
            var3.onChange();
         }

         return this;
      } else {
         throw new IllegalArgumentException("End date must be larger than start date");
      }
   }

   public CalendarDatePickerDialogFragment setDisabledDays(SparseArray var1) {
      this.mDisabledDays = var1;
      DayPickerView var2 = this.mDayPickerView;
      if (var2 != null) {
         var2.onChange();
      }

      return this;
   }

   public CalendarDatePickerDialogFragment setDoneText(String var1) {
      this.mDoneText = var1;
      return this;
   }

   public CalendarDatePickerDialogFragment setFirstDayOfWeek(int var1) {
      if (var1 >= 1 && var1 <= 7) {
         this.mWeekStart = var1;
         DayPickerView var2 = this.mDayPickerView;
         if (var2 != null) {
            var2.onChange();
         }

         return this;
      } else {
         throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and Calendar.SATURDAY");
      }
   }

   public CalendarDatePickerDialogFragment setOnDateSetListener(CalendarDatePickerDialogFragment.OnDateSetListener var1) {
      this.mCallBack = var1;
      return this;
   }

   public CalendarDatePickerDialogFragment setOnDismissListener(OnDialogDismissListener var1) {
      this.mDimissCallback = var1;
      return this;
   }

   public CalendarDatePickerDialogFragment setPreselectedDate(int var1, int var2, int var3) {
      this.mCalendar.set(1, var1);
      this.mCalendar.set(2, var2);
      this.mCalendar.set(5, var3);
      return this;
   }

   public CalendarDatePickerDialogFragment setThemeCustom(int var1) {
      this.mStyleResId = var1;
      return this;
   }

   public CalendarDatePickerDialogFragment setThemeDark() {
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_Dark;
      return this;
   }

   public CalendarDatePickerDialogFragment setThemeLight() {
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_Light;
      return this;
   }

   public void tryVibrate() {
      this.mHapticFeedbackController.tryVibrate();
   }

   public void unregisterOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1) {
      this.mListeners.remove(var1);
   }

   public interface OnDateChangedListener {
      void onDateChanged();
   }

   public interface OnDateSetListener {
      void onDateSet(CalendarDatePickerDialogFragment var1, int var2, int var3, int var4);
   }
}
