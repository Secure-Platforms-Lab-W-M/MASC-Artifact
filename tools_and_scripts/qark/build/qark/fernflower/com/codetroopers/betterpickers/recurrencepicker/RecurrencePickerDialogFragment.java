package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout.LayoutParams;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.R.array;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.plurals;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

public class RecurrencePickerDialogFragment extends DialogFragment implements OnItemSelectedListener, OnCheckedChangeListener, OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener, CalendarDatePickerDialogFragment.OnDateSetListener {
   private static final String BUNDLE_END_COUNT_HAS_FOCUS = "bundle_end_count_has_focus";
   public static final String BUNDLE_HIDE_SWITCH_BUTTON = "bundle_hide_switch_button";
   private static final String BUNDLE_MODEL = "bundle_model";
   public static final String BUNDLE_RRULE = "bundle_event_rrule";
   public static final String BUNDLE_START_TIME_MILLIS = "bundle_event_start_time";
   public static final String BUNDLE_TIME_ZONE = "bundle_event_time_zone";
   private static final int COUNT_DEFAULT = 5;
   private static final int COUNT_MAX = 730;
   private static final int FIFTH_WEEK_IN_A_MONTH = 5;
   private static final String FRAG_TAG_DATE_PICKER = "tag_date_picker_frag";
   private static final int INTERVAL_DEFAULT = 1;
   private static final int INTERVAL_MAX = 99;
   public static final int LAST_NTH_DAY_OF_WEEK = -1;
   private static final int MIN_SCREEN_WIDTH_FOR_SINGLE_ROW_WEEK = 450;
   private static final String TAG = "RecurrencePickerDialogFragment";
   private static final int[] mFreqModelToEventRecurrence = new int[]{3, 4, 5, 6, 7};
   private final int[] TIME_DAY_TO_CALENDAR_DAY = new int[]{1, 2, 3, 4, 5, 6, 7};
   private CalendarDatePickerDialogFragment mDatePickerDialog;
   private OnDialogDismissListener mDismissCallback;
   private Button mDoneButton;
   private EditText mEndCount;
   private String mEndCountLabel;
   private String mEndDateLabel;
   private TextView mEndDateTextView;
   private String mEndNeverStr;
   private Spinner mEndSpinner;
   private RecurrencePickerDialogFragment.EndSpinnerAdapter mEndSpinnerAdapter;
   private ArrayList mEndSpinnerArray = new ArrayList(3);
   private Spinner mFreqSpinner;
   private boolean mHidePostEndCount;
   private EditText mInterval;
   private TextView mIntervalPostText;
   private TextView mIntervalPreText;
   private int mIntervalResId = -1;
   private RecurrencePickerDialogFragment.RecurrenceModel mModel = new RecurrencePickerDialogFragment.RecurrenceModel();
   private LinearLayout mMonthGroup;
   private String mMonthRepeatByDayOfWeekStr;
   private String[][] mMonthRepeatByDayOfWeekStrs;
   private RadioGroup mMonthRepeatByRadioGroup;
   private TextView mPostEndCount;
   private EventRecurrence mRecurrence = new EventRecurrence();
   private RecurrencePickerDialogFragment.OnRecurrenceSetListener mRecurrenceSetListener;
   private RadioButton mRepeatMonthlyByNthDayOfMonth;
   private RadioButton mRepeatMonthlyByNthDayOfWeek;
   private SwitchCompat mRepeatSwitch;
   private Resources mResources;
   private Time mTime = new Time();
   private Toast mToast;
   private View mView;
   private ToggleButton[] mWeekByDayButtons = new ToggleButton[7];
   private LinearLayout mWeekGroup;
   private LinearLayout mWeekGroup2;

   // $FF: synthetic method
   static void access$300(RecurrencePickerDialogFragment var0) {
      var0.togglePickerOptions();
   }

   public static boolean canHandleRecurrenceRule(EventRecurrence var0) {
      int var1 = var0.freq;
      if (var1 != 3 && var1 != 4 && var1 != 5 && var1 != 6 && var1 != 7) {
         return false;
      } else if (var0.count > 0 && !TextUtils.isEmpty(var0.until)) {
         return false;
      } else {
         int var2 = 0;

         int var3;
         for(var1 = 0; var1 < var0.bydayCount; var2 = var3) {
            var3 = var2;
            if (isSupportedMonthlyByNthDayOfWeek(var0.bydayNum[var1])) {
               var3 = var2 + 1;
            }

            ++var1;
         }

         if (var2 > 1) {
            return false;
         } else if (var2 > 0 && var0.freq != 6) {
            return false;
         } else if (var0.bymonthdayCount > 1) {
            return false;
         } else {
            if (var0.freq == 6) {
               if (var0.bydayCount > 1) {
                  return false;
               }

               if (var0.bydayCount > 0 && var0.bymonthdayCount > 0) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private static void copyEventRecurrenceToModel(EventRecurrence var0, RecurrencePickerDialogFragment.RecurrenceModel var1) {
      int var2 = var0.freq;
      StringBuilder var8;
      if (var2 != 3) {
         if (var2 != 4) {
            if (var2 != 5) {
               if (var2 != 6) {
                  if (var2 != 7) {
                     var8 = new StringBuilder();
                     var8.append("freq=");
                     var8.append(var0.freq);
                     throw new IllegalStateException(var8.toString());
                  }

                  var1.freq = 4;
               } else {
                  var1.freq = 3;
               }
            } else {
               var1.freq = 2;
            }
         } else {
            var1.freq = 1;
         }
      } else {
         var1.freq = 0;
      }

      if (var0.interval > 0) {
         var1.interval = var0.interval;
      }

      var1.endCount = var0.count;
      if (var1.endCount > 0) {
         var1.end = 2;
      }

      if (!TextUtils.isEmpty(var0.until)) {
         if (var1.endDate == null) {
            var1.endDate = new Time();
         }

         try {
            var1.endDate.parse(var0.until);
         } catch (TimeFormatException var7) {
            var1.endDate = null;
         }

         if (var1.end == 2 && var1.endDate != null) {
            var8 = new StringBuilder();
            var8.append("freq=");
            var8.append(var0.freq);
            throw new IllegalStateException(var8.toString());
         }

         var1.end = 1;
      }

      Arrays.fill(var1.weeklyByDayOfWeek, false);
      if (var0.bydayCount > 0) {
         int var3 = 0;

         int var4;
         for(var2 = 0; var2 < var0.bydayCount; var3 = var4) {
            int var5 = EventRecurrence.day2TimeDay(var0.byday[var2]);
            var1.weeklyByDayOfWeek[var5] = true;
            var4 = var3;
            if (var1.freq == 3) {
               var4 = var3;
               if (isSupportedMonthlyByNthDayOfWeek(var0.bydayNum[var2])) {
                  var1.monthlyByDayOfWeek = var5;
                  var1.monthlyByNthDayOfWeek = var0.bydayNum[var2];
                  var1.monthlyRepeat = 1;
                  var4 = var3 + 1;
               }
            }

            ++var2;
         }

         if (var1.freq == 3) {
            if (var0.bydayCount != 1) {
               throw new IllegalStateException("Can handle only 1 byDayOfWeek in monthly");
            }

            if (var3 != 1) {
               throw new IllegalStateException("Didn't specify which nth day of week to repeat for a monthly");
            }
         }
      }

      if (var1.freq == 3) {
         if (var0.bymonthdayCount == 1) {
            if (var1.monthlyRepeat != 1) {
               var1.monthlyByMonthDay = var0.bymonthday[0];
               var1.monthlyRepeat = 0;
            } else {
               throw new IllegalStateException("Can handle only by monthday or by nth day of week, not both");
            }
         } else if (var0.bymonthCount > 1) {
            throw new IllegalStateException("Can handle only one bymonthday");
         }
      }
   }

   private static void copyModelToEventRecurrence(RecurrencePickerDialogFragment.RecurrenceModel var0, EventRecurrence var1) {
      if (var0.recurrenceState != 0) {
         var1.freq = mFreqModelToEventRecurrence[var0.freq];
         if (var0.interval <= 1) {
            var1.interval = 0;
         } else {
            var1.interval = var0.interval;
         }

         int var2 = var0.end;
         if (var2 != 1) {
            if (var2 != 2) {
               var1.count = 0;
               var1.until = null;
            } else {
               var1.count = var0.endCount;
               var1.until = null;
               if (var1.count <= 0) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("count is ");
                  var6.append(var1.count);
                  throw new IllegalStateException(var6.toString());
               }
            }
         } else {
            if (var0.endDate == null) {
               throw new IllegalStateException("end = END_BY_DATE but endDate is null");
            }

            var0.endDate.switchTimezone("UTC");
            var0.endDate.normalize(false);
            var1.until = var0.endDate.format2445();
            var1.count = 0;
         }

         var1.bydayCount = 0;
         var1.bymonthdayCount = 0;
         var2 = var0.freq;
         if (var2 != 2) {
            if (var2 == 3) {
               if (var0.monthlyRepeat == 0) {
                  if (var0.monthlyByMonthDay > 0) {
                     if (var1.bymonthday == null || var1.bymonthdayCount < 1) {
                        var1.bymonthday = new int[1];
                     }

                     var1.bymonthday[0] = var0.monthlyByMonthDay;
                     var1.bymonthdayCount = 1;
                  }
               } else if (var0.monthlyRepeat == 1) {
                  if (!isSupportedMonthlyByNthDayOfWeek(var0.monthlyByNthDayOfWeek)) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("month repeat by nth week but n is ");
                     var7.append(var0.monthlyByNthDayOfWeek);
                     throw new IllegalStateException(var7.toString());
                  }

                  if (var1.bydayCount < 1 || var1.byday == null || var1.bydayNum == null) {
                     var1.byday = new int[1];
                     var1.bydayNum = new int[1];
                  }

                  var1.bydayCount = 1;
                  var1.byday[0] = EventRecurrence.timeDay2Day(var0.monthlyByDayOfWeek);
                  var1.bydayNum[0] = var0.monthlyByNthDayOfWeek;
               }
            }
         } else {
            var2 = 0;

            int var3;
            int var4;
            for(var3 = 0; var3 < 7; var2 = var4) {
               var4 = var2;
               if (var0.weeklyByDayOfWeek[var3]) {
                  var4 = var2 + 1;
               }

               ++var3;
            }

            if (var1.bydayCount < var2 || var1.byday == null || var1.bydayNum == null) {
               var1.byday = new int[var2];
               var1.bydayNum = new int[var2];
            }

            var1.bydayCount = var2;

            for(var3 = 6; var3 >= 0; var2 = var4) {
               var4 = var2;
               if (var0.weeklyByDayOfWeek[var3]) {
                  int[] var5 = var1.bydayNum;
                  var4 = var2 - 1;
                  var5[var4] = 0;
                  var1.byday[var4] = EventRecurrence.timeDay2Day(var3);
               }

               --var3;
            }
         }

         if (!canHandleRecurrenceRule(var1)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("UI generated recurrence that it can't handle. ER:");
            var8.append(var1.toString());
            var8.append(" Model: ");
            var8.append(var0.toString());
            throw new IllegalStateException(var8.toString());
         }
      } else {
         throw new IllegalStateException("There's no recurrence");
      }
   }

   private void doToast() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Model = ");
      var1.append(this.mModel.toString());
      Log.e("RecurrencePickerDialogFragment", var1.toString());
      String var3;
      if (this.mModel.recurrenceState == 0) {
         var3 = "Not repeating";
      } else {
         copyModelToEventRecurrence(this.mModel, this.mRecurrence);
         var3 = this.mRecurrence.toString();
      }

      Toast var2 = this.mToast;
      if (var2 != null) {
         var2.cancel();
      }

      Toast var4 = Toast.makeText(this.getActivity(), var3, 1);
      this.mToast = var4;
      var4.show();
   }

   public static boolean isSupportedMonthlyByNthDayOfWeek(int var0) {
      return var0 > 0 && var0 <= 5 || var0 == -1;
   }

   private void setEndSpinnerEndDateStr(String var1) {
      this.mEndSpinnerArray.set(1, var1);
      this.mEndSpinnerAdapter.notifyDataSetChanged();
   }

   private void togglePickerOptions() {
      int var2 = this.mModel.recurrenceState;
      int var1 = 0;
      ToggleButton[] var3;
      if (var2 == 0) {
         this.mFreqSpinner.setEnabled(false);
         this.mEndSpinner.setEnabled(false);
         this.mIntervalPreText.setEnabled(false);
         this.mInterval.setEnabled(false);
         this.mIntervalPostText.setEnabled(false);
         this.mMonthRepeatByRadioGroup.setEnabled(false);
         this.mEndCount.setEnabled(false);
         this.mPostEndCount.setEnabled(false);
         this.mEndDateTextView.setEnabled(false);
         this.mRepeatMonthlyByNthDayOfWeek.setEnabled(false);
         this.mRepeatMonthlyByNthDayOfMonth.setEnabled(false);
         var3 = this.mWeekByDayButtons;
         var2 = var3.length;

         for(var1 = 0; var1 < var2; ++var1) {
            var3[var1].setEnabled(false);
         }
      } else {
         this.mView.findViewById(id.options).setEnabled(true);
         this.mFreqSpinner.setEnabled(true);
         this.mEndSpinner.setEnabled(true);
         this.mIntervalPreText.setEnabled(true);
         this.mInterval.setEnabled(true);
         this.mIntervalPostText.setEnabled(true);
         this.mMonthRepeatByRadioGroup.setEnabled(true);
         this.mEndCount.setEnabled(true);
         this.mPostEndCount.setEnabled(true);
         this.mEndDateTextView.setEnabled(true);
         this.mRepeatMonthlyByNthDayOfWeek.setEnabled(true);
         this.mRepeatMonthlyByNthDayOfMonth.setEnabled(true);
         var3 = this.mWeekByDayButtons;

         for(var2 = var3.length; var1 < var2; ++var1) {
            var3[var1].setEnabled(true);
         }
      }

      this.updateDoneButtonState();
   }

   private void updateDoneButtonState() {
      if (this.mModel.recurrenceState == 0) {
         this.mDoneButton.setEnabled(true);
      } else if (this.mInterval.getText().toString().length() == 0) {
         this.mDoneButton.setEnabled(false);
      } else if (this.mEndCount.getVisibility() == 0 && this.mEndCount.getText().toString().length() == 0) {
         this.mDoneButton.setEnabled(false);
      } else if (this.mModel.freq == 2) {
         ToggleButton[] var3 = this.mWeekByDayButtons;
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (var3[var1].isChecked()) {
               this.mDoneButton.setEnabled(true);
               return;
            }
         }

         this.mDoneButton.setEnabled(false);
      } else {
         this.mDoneButton.setEnabled(true);
      }
   }

   private void updateEndCountText() {
      String var3 = this.mResources.getQuantityString(plurals.recurrence_end_count, this.mModel.endCount);
      int var1 = var3.indexOf("%d");
      if (var1 != -1) {
         if (var1 == 0) {
            Log.e("RecurrencePickerDialogFragment", "No text to put in to recurrence's end spinner.");
            return;
         }

         int var2 = "%d".length();
         this.mPostEndCount.setText(var3.substring(var2 + var1, var3.length()).trim());
      }

   }

   private void updateIntervalText() {
      int var1 = this.mIntervalResId;
      if (var1 != -1) {
         String var3 = this.mResources.getQuantityString(var1, this.mModel.interval);
         var1 = var3.indexOf("%d");
         if (var1 != -1) {
            int var2 = "%d".length();
            this.mIntervalPostText.setText(var3.substring(var2 + var1, var3.length()).trim());
            this.mIntervalPreText.setText(var3.substring(0, var1).trim());
         }

      }
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      CalendarDatePickerDialogFragment var2 = (CalendarDatePickerDialogFragment)this.getFragmentManager().findFragmentByTag("tag_date_picker_frag");
      this.mDatePickerDialog = var2;
      if (var2 != null) {
         var2.setOnDateSetListener(this);
      }

   }

   public void onCheckedChanged(CompoundButton var1, boolean var2) {
      int var4 = -1;

      int var5;
      for(int var3 = 0; var3 < 7; var4 = var5) {
         var5 = var4;
         if (var4 == -1) {
            var5 = var4;
            if (var1 == this.mWeekByDayButtons[var3]) {
               var5 = var3;
               this.mModel.weeklyByDayOfWeek[var3] = var2;
            }
         }

         ++var3;
      }

      this.updateDialog();
   }

   public void onCheckedChanged(RadioGroup var1, int var2) {
      if (var2 == id.repeatMonthlyByNthDayOfMonth) {
         this.mModel.monthlyRepeat = 0;
      } else if (var2 == id.repeatMonthlyByNthDayOfTheWeek) {
         this.mModel.monthlyRepeat = 1;
      }

      this.updateDialog();
   }

   public void onClick(View var1) {
      if (this.mEndDateTextView == var1) {
         CalendarDatePickerDialogFragment var3 = this.mDatePickerDialog;
         if (var3 != null) {
            var3.dismiss();
         }

         var3 = new CalendarDatePickerDialogFragment();
         this.mDatePickerDialog = var3;
         var3.setOnDateSetListener(this);
         this.mDatePickerDialog.setPreselectedDate(this.mModel.endDate.year, this.mModel.endDate.month, this.mModel.endDate.monthDay);
         this.mDatePickerDialog.setFirstDayOfWeek(Utils.getFirstDayOfWeekAsCalendar(this.getActivity()));
         this.mDatePickerDialog.show(this.getFragmentManager(), "tag_date_picker_frag");
      } else {
         if (this.mDoneButton == var1) {
            String var2;
            if (this.mModel.recurrenceState == 0) {
               var2 = null;
            } else {
               copyModelToEventRecurrence(this.mModel, this.mRecurrence);
               var2 = this.mRecurrence.toString();
            }

            this.mRecurrenceSetListener.onRecurrenceSet(var2);
            this.dismiss();
         }

      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.mRecurrence.wkst = EventRecurrence.timeDay2Day(Utils.getFirstDayOfWeek(this.getActivity()));
      this.getDialog().getWindow().requestFeature(1);
      boolean var9;
      if (var3 != null) {
         RecurrencePickerDialogFragment.RecurrenceModel var11 = (RecurrencePickerDialogFragment.RecurrenceModel)var3.get("bundle_model");
         if (var11 != null) {
            this.mModel = var11;
         }

         var9 = var3.getBoolean("bundle_end_count_has_focus");
      } else {
         var3 = this.getArguments();
         if (var3 != null) {
            this.mTime.set(var3.getLong("bundle_event_start_time"));
            String var28 = var3.getString("bundle_event_time_zone");
            if (!TextUtils.isEmpty(var28)) {
               this.mTime.timezone = var28;
            }

            this.mTime.normalize(false);
            this.mModel.weeklyByDayOfWeek[this.mTime.weekDay] = true;
            var28 = var3.getString("bundle_event_rrule");
            if (!TextUtils.isEmpty(var28)) {
               this.mModel.recurrenceState = 1;
               this.mRecurrence.parse(var28);
               copyEventRecurrenceToModel(this.mRecurrence, this.mModel);
               if (this.mRecurrence.bydayCount == 0) {
                  this.mModel.weeklyByDayOfWeek[this.mTime.weekDay] = true;
               }
            }

            this.mModel.forceHideSwitchButton = var3.getBoolean("bundle_hide_switch_button", false);
         } else {
            this.mTime.setToNow();
         }

         var9 = false;
      }

      this.mResources = this.getResources();
      this.mView = var1.inflate(layout.recurrencepicker, var2, true);
      this.getActivity().getResources().getConfiguration();
      this.mRepeatSwitch = (SwitchCompat)this.mView.findViewById(id.repeat_switch);
      if (this.mModel.forceHideSwitchButton) {
         this.mRepeatSwitch.setChecked(true);
         this.mRepeatSwitch.setVisibility(8);
         this.mModel.recurrenceState = 1;
      } else {
         SwitchCompat var12 = this.mRepeatSwitch;
         boolean var10;
         if (this.mModel.recurrenceState == 1) {
            var10 = true;
         } else {
            var10 = false;
         }

         var12.setChecked(var10);
         this.mRepeatSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton var1, boolean var2) {
               throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            }
         });
      }

      Spinner var13 = (Spinner)this.mView.findViewById(id.freqSpinner);
      this.mFreqSpinner = var13;
      var13.setOnItemSelectedListener(this);
      ArrayAdapter var14 = ArrayAdapter.createFromResource(this.getActivity(), array.recurrence_freq, layout.recurrencepicker_freq_item);
      var14.setDropDownViewResource(layout.recurrencepicker_freq_item);
      this.mFreqSpinner.setAdapter(var14);
      EditText var15 = (EditText)this.mView.findViewById(id.interval);
      this.mInterval = var15;
      var15.addTextChangedListener(new RecurrencePickerDialogFragment.minMaxTextWatcher(1, 1, 99) {
         void onChange(int var1) {
            if (RecurrencePickerDialogFragment.this.mIntervalResId != -1 && RecurrencePickerDialogFragment.this.mInterval.getText().toString().length() > 0) {
               RecurrencePickerDialogFragment.this.mModel.interval = var1;
               RecurrencePickerDialogFragment.this.updateIntervalText();
               RecurrencePickerDialogFragment.this.mInterval.requestLayout();
            }

         }
      });
      this.mIntervalPreText = (TextView)this.mView.findViewById(id.intervalPreText);
      this.mIntervalPostText = (TextView)this.mView.findViewById(id.intervalPostText);
      this.mEndNeverStr = this.mResources.getString(string.recurrence_end_continously);
      this.mEndDateLabel = this.mResources.getString(string.recurrence_end_date_label);
      this.mEndCountLabel = this.mResources.getString(string.recurrence_end_count_label);
      this.mEndSpinnerArray.add(this.mEndNeverStr);
      this.mEndSpinnerArray.add(this.mEndDateLabel);
      this.mEndSpinnerArray.add(this.mEndCountLabel);
      var13 = (Spinner)this.mView.findViewById(id.endSpinner);
      this.mEndSpinner = var13;
      var13.setOnItemSelectedListener(this);
      RecurrencePickerDialogFragment.EndSpinnerAdapter var17 = new RecurrencePickerDialogFragment.EndSpinnerAdapter(this.getActivity(), this.mEndSpinnerArray, layout.recurrencepicker_freq_item, layout.recurrencepicker_end_text);
      this.mEndSpinnerAdapter = var17;
      var17.setDropDownViewResource(layout.recurrencepicker_freq_item);
      this.mEndSpinner.setAdapter(this.mEndSpinnerAdapter);
      var15 = (EditText)this.mView.findViewById(id.endCount);
      this.mEndCount = var15;
      var15.addTextChangedListener(new RecurrencePickerDialogFragment.minMaxTextWatcher(1, 5, 730) {
         void onChange(int var1) {
            if (RecurrencePickerDialogFragment.this.mModel.endCount != var1) {
               RecurrencePickerDialogFragment.this.mModel.endCount = var1;
               RecurrencePickerDialogFragment.this.updateEndCountText();
               RecurrencePickerDialogFragment.this.mEndCount.requestLayout();
            }

         }
      });
      this.mPostEndCount = (TextView)this.mView.findViewById(id.postEndCount);
      TextView var18 = (TextView)this.mView.findViewById(id.endDate);
      this.mEndDateTextView = var18;
      var18.setOnClickListener(this);
      if (this.mModel.endDate == null) {
         this.mModel.endDate = new Time(this.mTime);
         int var5 = this.mModel.freq;
         Time var20;
         if (var5 != 0 && var5 != 1 && var5 != 2) {
            if (var5 != 3) {
               if (var5 == 4) {
                  var20 = this.mModel.endDate;
                  var20.year += 3;
               }
            } else {
               var20 = this.mModel.endDate;
               var20.month += 3;
            }
         } else {
            var20 = this.mModel.endDate;
            ++var20.month;
         }

         this.mModel.endDate.normalize(false);
      }

      this.mWeekGroup = (LinearLayout)this.mView.findViewById(id.weekGroup);
      this.mWeekGroup2 = (LinearLayout)this.mView.findViewById(id.weekGroup2);
      (new DateFormatSymbols()).getWeekdays();
      String[][] var21 = new String[7][];
      this.mMonthRepeatByDayOfWeekStrs = var21;
      var21[0] = this.mResources.getStringArray(array.repeat_by_nth_sun);
      this.mMonthRepeatByDayOfWeekStrs[1] = this.mResources.getStringArray(array.repeat_by_nth_mon);
      this.mMonthRepeatByDayOfWeekStrs[2] = this.mResources.getStringArray(array.repeat_by_nth_tues);
      this.mMonthRepeatByDayOfWeekStrs[3] = this.mResources.getStringArray(array.repeat_by_nth_wed);
      this.mMonthRepeatByDayOfWeekStrs[4] = this.mResources.getStringArray(array.repeat_by_nth_thurs);
      this.mMonthRepeatByDayOfWeekStrs[5] = this.mResources.getStringArray(array.repeat_by_nth_fri);
      this.mMonthRepeatByDayOfWeekStrs[6] = this.mResources.getStringArray(array.repeat_by_nth_sat);
      int var7 = Utils.getFirstDayOfWeek(this.getActivity());
      String[] var22 = (new DateFormatSymbols()).getShortWeekdays();
      byte var6;
      byte var23;
      if (VERSION.SDK_INT < 13) {
         Display var16 = this.getActivity().getWindowManager().getDefaultDisplay();
         DisplayMetrics var19 = new DisplayMetrics();
         var16.getMetrics(var19);
         float var4 = this.getResources().getDisplayMetrics().density;
         if ((float)var19.widthPixels / var4 > 450.0F) {
            var23 = 0;
            this.mWeekGroup2.setVisibility(8);
            this.mWeekGroup2.getChildAt(3).setVisibility(8);
            var6 = 7;
         } else {
            this.mWeekGroup2.setVisibility(0);
            this.mWeekGroup2.getChildAt(3).setVisibility(4);
            var6 = 4;
            var23 = 3;
         }
      } else if (this.mResources.getConfiguration().screenWidthDp > 450) {
         var6 = 7;
         var23 = 0;
         this.mWeekGroup2.setVisibility(8);
         this.mWeekGroup2.getChildAt(3).setVisibility(8);
      } else {
         var6 = 4;
         this.mWeekGroup2.setVisibility(0);
         this.mWeekGroup2.getChildAt(3).setVisibility(4);
         var23 = 3;
      }

      for(int var8 = 0; var8 < 7; ++var8) {
         if (var8 >= var6) {
            this.mWeekGroup.getChildAt(var8).setVisibility(8);
         } else {
            this.mWeekByDayButtons[var7] = (ToggleButton)this.mWeekGroup.getChildAt(var8);
            this.mWeekByDayButtons[var7].setTextOff(var22[this.TIME_DAY_TO_CALENDAR_DAY[var7]]);
            this.mWeekByDayButtons[var7].setTextOn(var22[this.TIME_DAY_TO_CALENDAR_DAY[var7]]);
            this.mWeekByDayButtons[var7].setOnCheckedChangeListener(this);
            ++var7;
            if (var7 >= 7) {
               var7 = 0;
            }
         }
      }

      byte var27 = 0;
      int var26 = var7;

      for(var7 = var27; var7 < 3; ++var7) {
         if (var7 >= var23) {
            this.mWeekGroup2.getChildAt(var7).setVisibility(8);
         } else {
            this.mWeekByDayButtons[var26] = (ToggleButton)this.mWeekGroup2.getChildAt(var7);
            this.mWeekByDayButtons[var26].setTextOff(var22[this.TIME_DAY_TO_CALENDAR_DAY[var26]]);
            this.mWeekByDayButtons[var26].setTextOn(var22[this.TIME_DAY_TO_CALENDAR_DAY[var26]]);
            this.mWeekByDayButtons[var26].setOnCheckedChangeListener(this);
            ++var26;
            if (var26 >= 7) {
               var26 = 0;
            }
         }
      }

      this.mMonthGroup = (LinearLayout)this.mView.findViewById(id.monthGroup);
      RadioGroup var24 = (RadioGroup)this.mView.findViewById(id.monthGroup);
      this.mMonthRepeatByRadioGroup = var24;
      var24.setOnCheckedChangeListener(this);
      this.mRepeatMonthlyByNthDayOfWeek = (RadioButton)this.mView.findViewById(id.repeatMonthlyByNthDayOfTheWeek);
      this.mRepeatMonthlyByNthDayOfMonth = (RadioButton)this.mView.findViewById(id.repeatMonthlyByNthDayOfMonth);
      Button var25 = (Button)this.mView.findViewById(id.done_button);
      this.mDoneButton = var25;
      var25.setOnClickListener(this);
      ((Button)this.mView.findViewById(id.cancel_button)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            RecurrencePickerDialogFragment.this.dismiss();
         }
      });
      this.togglePickerOptions();
      this.updateDialog();
      if (var9) {
         this.mEndCount.requestFocus();
      }

      return this.mView;
   }

   public void onDateSet(CalendarDatePickerDialogFragment var1, int var2, int var3, int var4) {
      if (this.mModel.endDate == null) {
         this.mModel.endDate = new Time(this.mTime.timezone);
         Time var6 = this.mModel.endDate;
         Time var5 = this.mModel.endDate;
         this.mModel.endDate.second = 0;
         var5.minute = 0;
         var6.hour = 0;
      }

      this.mModel.endDate.year = var2;
      this.mModel.endDate.month = var3;
      this.mModel.endDate.monthDay = var4;
      this.mModel.endDate.normalize(false);
      this.updateDialog();
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      OnDialogDismissListener var2 = this.mDismissCallback;
      if (var2 != null) {
         var2.onDialogDismiss(var1);
      }

   }

   public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
      if (var1 == this.mFreqSpinner) {
         this.mModel.freq = var3;
      } else if (var1 == this.mEndSpinner) {
         byte var6 = 0;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  this.mModel.end = 2;
                  if (this.mModel.endCount <= 1) {
                     this.mModel.endCount = 1;
                  } else if (this.mModel.endCount > 730) {
                     this.mModel.endCount = 730;
                  }

                  this.updateEndCountText();
               }
            } else {
               this.mModel.end = 1;
            }
         } else {
            this.mModel.end = 0;
         }

         EditText var7 = this.mEndCount;
         byte var9;
         if (this.mModel.end == 2) {
            var9 = 0;
         } else {
            var9 = 8;
         }

         var7.setVisibility(var9);
         TextView var8 = this.mEndDateTextView;
         if (this.mModel.end == 1) {
            var9 = 0;
         } else {
            var9 = 8;
         }

         var8.setVisibility(var9);
         var8 = this.mPostEndCount;
         if (this.mModel.end == 2 && !this.mHidePostEndCount) {
            var9 = var6;
         } else {
            var9 = 8;
         }

         var8.setVisibility(var9);
      }

      this.updateDialog();
   }

   public void onNothingSelected(AdapterView var1) {
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putParcelable("bundle_model", this.mModel);
      if (this.mEndCount.hasFocus()) {
         var1.putBoolean("bundle_end_count_has_focus", true);
      }

   }

   public RecurrencePickerDialogFragment setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
      return this;
   }

   public void setOnRecurrenceSetListener(RecurrencePickerDialogFragment.OnRecurrenceSetListener var1) {
      this.mRecurrenceSetListener = var1;
   }

   public void updateDialog() {
      String var3 = Integer.toString(this.mModel.interval);
      if (!var3.equals(this.mInterval.getText().toString())) {
         this.mInterval.setText(var3);
      }

      this.mFreqSpinner.setSelection(this.mModel.freq);
      LinearLayout var6 = this.mWeekGroup;
      int var1 = this.mModel.freq;
      byte var2 = 8;
      byte var4;
      if (var1 == 2) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      var6.setVisibility(var4);
      var6 = this.mWeekGroup2;
      if (this.mModel.freq == 2) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      var6.setVisibility(var4);
      var6 = this.mMonthGroup;
      var4 = var2;
      if (this.mModel.freq == 3) {
         var4 = 0;
      }

      var6.setVisibility(var4);
      var1 = this.mModel.freq;
      if (var1 != 0) {
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 == 4) {
                     this.mIntervalResId = plurals.recurrence_interval_yearly;
                  }
               } else {
                  this.mIntervalResId = plurals.recurrence_interval_monthly;
                  if (this.mModel.monthlyRepeat == 0) {
                     this.mMonthRepeatByRadioGroup.check(id.repeatMonthlyByNthDayOfMonth);
                  } else if (this.mModel.monthlyRepeat == 1) {
                     this.mMonthRepeatByRadioGroup.check(id.repeatMonthlyByNthDayOfTheWeek);
                  }

                  if (this.mMonthRepeatByDayOfWeekStr == null) {
                     int var5 = this.mModel.monthlyByNthDayOfWeek;
                     var1 = 5;
                     if (var5 == 0) {
                        this.mModel.monthlyByNthDayOfWeek = (this.mTime.monthDay + 6) / 7;
                        if (this.mModel.monthlyByNthDayOfWeek >= 5) {
                           this.mModel.monthlyByNthDayOfWeek = -1;
                        }

                        this.mModel.monthlyByDayOfWeek = this.mTime.weekDay;
                     }

                     String[] var7 = this.mMonthRepeatByDayOfWeekStrs[this.mModel.monthlyByDayOfWeek];
                     if (this.mModel.monthlyByNthDayOfWeek >= 0) {
                        var1 = this.mModel.monthlyByNthDayOfWeek;
                     }

                     var3 = var7[var1 - 1];
                     this.mMonthRepeatByDayOfWeekStr = var3;
                     this.mRepeatMonthlyByNthDayOfWeek.setText(var3);
                  }
               }
            } else {
               this.mIntervalResId = plurals.recurrence_interval_weekly;

               for(var1 = 0; var1 < 7; ++var1) {
                  this.mWeekByDayButtons[var1].setChecked(this.mModel.weeklyByDayOfWeek[var1]);
               }
            }
         } else {
            this.mIntervalResId = plurals.recurrence_interval_daily;
         }
      } else {
         this.mIntervalResId = plurals.recurrence_interval_hourly;
      }

      this.updateIntervalText();
      this.updateDoneButtonState();
      this.mEndSpinner.setSelection(this.mModel.end);
      if (this.mModel.end == 1) {
         var3 = DateUtils.formatDateTime(this.getActivity(), this.mModel.endDate.toMillis(false), 131072);
         this.mEndDateTextView.setText(var3);
      } else {
         if (this.mModel.end == 2) {
            var3 = Integer.toString(this.mModel.endCount);
            if (!var3.equals(this.mEndCount.getText().toString())) {
               this.mEndCount.setText(var3);
            }
         }

      }
   }

   private class EndSpinnerAdapter extends ArrayAdapter {
      final String END_COUNT_MARKER = "%d";
      final String END_DATE_MARKER = "%s";
      private String mEndDateString;
      private LayoutInflater mInflater;
      private int mItemResourceId;
      private ArrayList mStrings;
      private int mTextResourceId;
      private boolean mUseFormStrings;

      public EndSpinnerAdapter(Context var2, ArrayList var3, int var4, int var5) {
         super(var2, var4, var3);
         this.mInflater = (LayoutInflater)var2.getSystemService("layout_inflater");
         this.mItemResourceId = var4;
         this.mTextResourceId = var5;
         this.mStrings = var3;
         String var6 = RecurrencePickerDialogFragment.this.getResources().getString(string.recurrence_end_date);
         this.mEndDateString = var6;
         if (var6.indexOf("%s") <= 0) {
            this.mUseFormStrings = true;
         } else if (RecurrencePickerDialogFragment.this.getResources().getQuantityString(plurals.recurrence_end_count, 1).indexOf("%d") <= 0) {
            this.mUseFormStrings = true;
         }

         if (this.mUseFormStrings) {
            RecurrencePickerDialogFragment.this.mEndSpinner.setLayoutParams(new LayoutParams(0, -2, 1.0F));
         }

      }

      public View getDropDownView(int var1, View var2, ViewGroup var3) {
         if (var2 == null) {
            var2 = this.mInflater.inflate(this.mItemResourceId, var3, false);
         }

         ((TextView)var2.findViewById(id.spinner_item)).setText((CharSequence)this.mStrings.get(var1));
         return var2;
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         if (var2 == null) {
            var2 = this.mInflater.inflate(this.mTextResourceId, var3, false);
         }

         TextView var6 = (TextView)var2.findViewById(id.spinner_item);
         if (var1 != 0) {
            if (var1 != 1) {
               if (var1 != 2) {
                  return null;
               }

               String var5 = RecurrencePickerDialogFragment.this.mResources.getQuantityString(plurals.recurrence_end_count, RecurrencePickerDialogFragment.this.mModel.endCount);
               int var4 = var5.indexOf("%d");
               if (var4 != -1) {
                  if (!this.mUseFormStrings && var4 != 0) {
                     var1 = "%d".length();
                     RecurrencePickerDialogFragment.this.mPostEndCount.setText(var5.substring(var1 + var4, var5.length()).trim());
                     if (RecurrencePickerDialogFragment.this.mModel.end == 2) {
                        RecurrencePickerDialogFragment.this.mPostEndCount.setVisibility(0);
                     }

                     var1 = var4;
                     if (var5.charAt(var4 - 1) == ' ') {
                        var1 = var4 - 1;
                     }

                     var6.setText(var5.substring(0, var1).trim());
                     return var2;
                  }

                  var6.setText(RecurrencePickerDialogFragment.this.mEndCountLabel);
                  RecurrencePickerDialogFragment.this.mPostEndCount.setVisibility(8);
                  RecurrencePickerDialogFragment.this.mHidePostEndCount = true;
                  return var2;
               }
            } else {
               var1 = this.mEndDateString.indexOf("%s");
               if (var1 != -1) {
                  if (!this.mUseFormStrings && var1 != 0) {
                     var6.setText(this.mEndDateString.substring(0, var1).trim());
                     return var2;
                  }

                  var6.setText(RecurrencePickerDialogFragment.this.mEndDateLabel);
                  return var2;
               }
            }
         } else {
            var6.setText((CharSequence)this.mStrings.get(0));
         }

         return var2;
      }
   }

   public interface OnRecurrenceSetListener {
      void onRecurrenceSet(String var1);
   }

   private static class RecurrenceModel implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public RecurrencePickerDialogFragment.RecurrenceModel createFromParcel(Parcel var1) {
            return new RecurrencePickerDialogFragment.RecurrenceModel(var1);
         }

         public RecurrencePickerDialogFragment.RecurrenceModel[] newArray(int var1) {
            return new RecurrencePickerDialogFragment.RecurrenceModel[var1];
         }
      };
      static final int END_BY_COUNT = 2;
      static final int END_BY_DATE = 1;
      static final int END_NEVER = 0;
      static final int FREQ_DAILY = 1;
      static final int FREQ_HOURLY = 0;
      static final int FREQ_MONTHLY = 3;
      static final int FREQ_WEEKLY = 2;
      static final int FREQ_YEARLY = 4;
      static final int MONTHLY_BY_DATE = 0;
      static final int MONTHLY_BY_NTH_DAY_OF_WEEK = 1;
      static final int STATE_NO_RECURRENCE = 0;
      static final int STATE_RECURRENCE = 1;
      int end;
      int endCount;
      Time endDate;
      boolean forceHideSwitchButton;
      int freq;
      int interval;
      int monthlyByDayOfWeek;
      int monthlyByMonthDay;
      int monthlyByNthDayOfWeek;
      int monthlyRepeat;
      int recurrenceState;
      boolean[] weeklyByDayOfWeek;

      public RecurrenceModel() {
         this.freq = 2;
         this.interval = 1;
         this.endCount = 5;
         this.weeklyByDayOfWeek = new boolean[7];
      }

      private RecurrenceModel(Parcel var1) {
         this.freq = 2;
         boolean var2 = true;
         this.interval = 1;
         this.endCount = 5;
         this.weeklyByDayOfWeek = new boolean[7];
         this.recurrenceState = var1.readInt();
         this.freq = var1.readInt();
         this.interval = var1.readInt();
         this.end = var1.readInt();
         Time var3 = new Time();
         this.endDate = var3;
         var3.year = var1.readInt();
         this.endDate.month = var1.readInt();
         this.endDate.monthDay = var1.readInt();
         this.endCount = var1.readInt();
         this.weeklyByDayOfWeek = var1.createBooleanArray();
         this.monthlyRepeat = var1.readInt();
         this.monthlyByMonthDay = var1.readInt();
         this.monthlyByDayOfWeek = var1.readInt();
         this.monthlyByNthDayOfWeek = var1.readInt();
         if (var1.readByte() == 0) {
            var2 = false;
         }

         this.forceHideSwitchButton = var2;
      }

      // $FF: synthetic method
      RecurrenceModel(Parcel var1, Object var2) {
         this(var1);
      }

      public int describeContents() {
         return 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Model [freq=");
         var1.append(this.freq);
         var1.append(", interval=");
         var1.append(this.interval);
         var1.append(", end=");
         var1.append(this.end);
         var1.append(", endDate=");
         var1.append(this.endDate);
         var1.append(", endCount=");
         var1.append(this.endCount);
         var1.append(", weeklyByDayOfWeek=");
         var1.append(Arrays.toString(this.weeklyByDayOfWeek));
         var1.append(", monthlyRepeat=");
         var1.append(this.monthlyRepeat);
         var1.append(", monthlyByMonthDay=");
         var1.append(this.monthlyByMonthDay);
         var1.append(", monthlyByDayOfWeek=");
         var1.append(this.monthlyByDayOfWeek);
         var1.append(", monthlyByNthDayOfWeek=");
         var1.append(this.monthlyByNthDayOfWeek);
         var1.append("]");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.recurrenceState);
         var1.writeInt(this.freq);
         var1.writeInt(this.interval);
         var1.writeInt(this.end);
         var1.writeInt(this.endDate.year);
         var1.writeInt(this.endDate.month);
         var1.writeInt(this.endDate.monthDay);
         var1.writeInt(this.endCount);
         var1.writeBooleanArray(this.weeklyByDayOfWeek);
         var1.writeInt(this.monthlyRepeat);
         var1.writeInt(this.monthlyByMonthDay);
         var1.writeInt(this.monthlyByDayOfWeek);
         var1.writeInt(this.monthlyByNthDayOfWeek);
         var1.writeByte((byte)this.forceHideSwitchButton);
      }
   }

   class minMaxTextWatcher implements TextWatcher {
      private int mDefault;
      private int mMax;
      private int mMin;

      public minMaxTextWatcher(int var2, int var3, int var4) {
         this.mMin = var2;
         this.mMax = var4;
         this.mDefault = var3;
      }

      public void afterTextChanged(Editable var1) {
         boolean var3 = false;

         int var2;
         try {
            var2 = Integer.parseInt(var1.toString());
         } catch (NumberFormatException var6) {
            var2 = this.mDefault;
         }

         int var4;
         if (var2 < this.mMin) {
            var4 = this.mMin;
            var3 = true;
         } else {
            var4 = var2;
            if (var2 > this.mMax) {
               var3 = true;
               var4 = this.mMax;
            }
         }

         if (var3) {
            var1.clear();
            var1.append(Integer.toString(var4));
         }

         RecurrencePickerDialogFragment.this.updateDoneButtonState();
         this.onChange(var4);
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      void onChange(int var1) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
