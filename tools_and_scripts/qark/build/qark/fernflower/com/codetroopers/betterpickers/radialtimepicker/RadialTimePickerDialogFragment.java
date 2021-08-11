package com.codetroopers.betterpickers.radialtimepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.codetroopers.betterpickers.HapticFeedbackController;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.style;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.numberpicker.NumberPickerErrorTextView;
import com.nineoldandroids.animation.ObjectAnimator;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class RadialTimePickerDialogFragment extends DialogFragment implements RadialPickerLayout.OnValueSelectedListener {
   // $FF: renamed from: AM int
   public static final int field_47 = 0;
   public static final int AMPM_INDEX = 2;
   public static final int ENABLE_PICKER_INDEX = 3;
   public static final int HOUR_INDEX = 0;
   private static final String KEY_CURRENT_DATE = "current_date";
   private static final String KEY_CURRENT_ITEM_SHOWING = "current_item_showing";
   private static final String KEY_FUTURE_MINUTES_LIMIT = "future_minutes_limit";
   private static final String KEY_HOUR_OF_DAY = "hour_of_day";
   private static final String KEY_IN_KB_MODE = "in_kb_mode";
   private static final String KEY_IS_24_HOUR_VIEW = "is_24_hour_view";
   private static final String KEY_MINUTE = "minute";
   private static final String KEY_PAST_MINUTES_LIMIT = "past_minutes_limit";
   private static final String KEY_PICKER_DATE = "picker_date";
   private static final String KEY_STYLE = "theme";
   private static final String KEY_TYPED_TIMES = "typed_times";
   public static final int MINUTE_INDEX = 1;
   // $FF: renamed from: PM int
   public static final int field_48 = 1;
   private static final int PULSE_ANIMATOR_DELAY = 300;
   private static final String TAG = "TimePickerDialog";
   private boolean mAllowAutoAdvance;
   private int mAmKeyCode;
   private View mAmPmHitspace;
   private TextView mAmPmTextView;
   private String mAmText;
   private RadialTimePickerDialogFragment.OnTimeSetListener mCallback;
   private String mCancelText;
   private String mDeletedKeyFormat;
   private OnDialogDismissListener mDismissCallback;
   private Button mDoneButton;
   private String mDoneText;
   private String mDoublePlaceholderText;
   private NumberPickerErrorTextView mError;
   private Integer mFutureMinutesLimit;
   private HapticFeedbackController mHapticFeedbackController;
   private String mHourPickerDescription;
   private TextView mHourSpaceView;
   private TextView mHourView;
   private boolean mInKbMode;
   private int mInitialHourOfDay;
   private int mInitialMinute;
   private Boolean mIs24HourMode;
   private RadialTimePickerDialogFragment.Node mLegalTimesTree;
   private String mMinutePickerDescription;
   private TextView mMinuteSpaceView;
   private TextView mMinuteView;
   private Integer mPastMinutesLimit;
   private Calendar mPickerDate;
   private char mPlaceholderText;
   private int mPmKeyCode;
   private String mPmText;
   private String mSelectHours;
   private String mSelectMinutes;
   private int mSelectedColor;
   private int mStyleResId;
   private RadialPickerLayout mTimePicker;
   private String mTitleText;
   private TextView mTitleTextView;
   private ArrayList mTypedTimes;
   private int mUnselectedColor;
   private Calendar mValidateDateTime;

   public RadialTimePickerDialogFragment() {
      Calendar var1 = Calendar.getInstance();
      this.mInitialMinute = var1.get(12);
      this.mInitialHourOfDay = var1.get(11);
      this.mInKbMode = false;
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_PrimaryColor;
   }

   private boolean addKeyIfLegal(int var1) {
      if ((!this.mIs24HourMode || this.mTypedTimes.size() != 4) && (this.mIs24HourMode || !this.isTypedTimeFullyLegal())) {
         this.mTypedTimes.add(var1);
         if (!this.isTypedTimeLegalSoFar()) {
            this.deleteLastTypedKey();
            return false;
         } else {
            var1 = this.getValFromKeyCode(var1);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, String.format("%d", var1));
            if (this.isTypedTimeFullyLegal()) {
               if (!this.mIs24HourMode && this.mTypedTimes.size() <= 3) {
                  ArrayList var2 = this.mTypedTimes;
                  var2.add(var2.size() - 1, 7);
                  var2 = this.mTypedTimes;
                  var2.add(var2.size() - 1, 7);
               }

               this.mDoneButton.setEnabled(true);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private int deleteLastTypedKey() {
      ArrayList var2 = this.mTypedTimes;
      int var1 = (Integer)var2.remove(var2.size() - 1);
      if (!this.isTypedTimeFullyLegal()) {
         this.mDoneButton.setEnabled(false);
      }

      return var1;
   }

   private void finishKbMode(boolean var1) {
      this.mInKbMode = false;
      if (!this.mTypedTimes.isEmpty()) {
         int[] var2 = this.getEnteredTime((Boolean[])null);
         this.mTimePicker.setTime(var2[0], var2[1]);
         if (!this.mIs24HourMode) {
            this.mTimePicker.setAmOrPm(var2[2]);
         }

         this.mTypedTimes.clear();
      }

      if (var1) {
         this.updateDisplay(false);
         this.mTimePicker.trySettingInputEnabled(true);
      }

   }

   private void generateLegalTimesTree() {
      this.mLegalTimesTree = new RadialTimePickerDialogFragment.Node(new int[0]);
      RadialTimePickerDialogFragment.Node var1;
      RadialTimePickerDialogFragment.Node var2;
      RadialTimePickerDialogFragment.Node var3;
      RadialTimePickerDialogFragment.Node var4;
      if (this.mIs24HourMode) {
         var1 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12});
         var2 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
         var1.addChild(var2);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8});
         this.mLegalTimesTree.addChild(var3);
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12});
         var3.addChild(var4);
         var4.addChild(var1);
         var4.addChild(new RadialTimePickerDialogFragment.Node(new int[]{13, 14, 15, 16}));
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{13, 14, 15, 16});
         var3.addChild(var4);
         var4.addChild(var1);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{9});
         this.mLegalTimesTree.addChild(var3);
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10});
         var3.addChild(var4);
         var4.addChild(var1);
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{11, 12});
         var3.addChild(var4);
         var4.addChild(var2);
         var2 = new RadialTimePickerDialogFragment.Node(new int[]{10, 11, 12, 13, 14, 15, 16});
         this.mLegalTimesTree.addChild(var2);
         var2.addChild(var1);
      } else {
         var1 = new RadialTimePickerDialogFragment.Node(new int[]{this.getAmOrPmKeyCode(0), this.getAmOrPmKeyCode(1)});
         var2 = new RadialTimePickerDialogFragment.Node(new int[]{8});
         this.mLegalTimesTree.addChild(var2);
         var2.addChild(var1);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9});
         var2.addChild(var3);
         var3.addChild(var1);
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12});
         var3.addChild(var4);
         var4.addChild(var1);
         RadialTimePickerDialogFragment.Node var5 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
         var4.addChild(var5);
         var5.addChild(var1);
         var4 = new RadialTimePickerDialogFragment.Node(new int[]{13, 14, 15, 16});
         var3.addChild(var4);
         var4.addChild(var1);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{10, 11, 12});
         var2.addChild(var3);
         var2 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
         var3.addChild(var2);
         var2.addChild(var1);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{9, 10, 11, 12, 13, 14, 15, 16});
         this.mLegalTimesTree.addChild(var3);
         var3.addChild(var1);
         var2 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12});
         var3.addChild(var2);
         var3 = new RadialTimePickerDialogFragment.Node(new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
         var2.addChild(var3);
         var3.addChild(var1);
      }
   }

   private int getAmOrPmKeyCode(int var1) {
      if (this.mAmKeyCode == -1 || this.mPmKeyCode == -1) {
         KeyCharacterMap var5 = KeyCharacterMap.load(-1);

         for(int var4 = 0; var4 < Math.max(this.mAmText.length(), this.mPmText.length()); ++var4) {
            char var2 = this.mAmText.toLowerCase(Locale.getDefault()).charAt(var4);
            char var3 = this.mPmText.toLowerCase(Locale.getDefault()).charAt(var4);
            if (var2 != var3) {
               KeyEvent[] var6 = var5.getEvents(new char[]{var2, var3});
               if (var6 != null && var6.length == 4) {
                  this.mAmKeyCode = var6[0].getKeyCode();
                  this.mPmKeyCode = var6[2].getKeyCode();
               } else {
                  Log.e("TimePickerDialog", "Unable to find keycodes for AM and PM.");
               }
               break;
            }
         }
      }

      if (var1 == 0) {
         return this.mAmKeyCode;
      } else {
         return var1 == 1 ? this.mPmKeyCode : -1;
      }
   }

   private int[] getEnteredTime(Boolean[] var1) {
      byte var2 = -1;
      byte var4 = 1;
      boolean var10 = this.mIs24HourMode;
      Boolean var11 = true;
      byte var5 = var2;
      byte var3 = var4;
      ArrayList var12;
      if (!var10) {
         var5 = var2;
         var3 = var4;
         if (this.isTypedTimeFullyLegal()) {
            var12 = this.mTypedTimes;
            int var14 = (Integer)var12.get(var12.size() - 1);
            if (var14 == this.getAmOrPmKeyCode(0)) {
               var2 = 0;
            } else if (var14 == this.getAmOrPmKeyCode(1)) {
               var2 = 1;
            }

            var3 = 2;
            var5 = var2;
         }
      }

      int var8 = -1;
      int var7 = -1;

      int var15;
      for(int var6 = var3; var6 <= this.mTypedTimes.size(); var7 = var15) {
         var12 = this.mTypedTimes;
         int var9 = this.getValFromKeyCode((Integer)var12.get(var12.size() - var6));
         int var13;
         if (var6 == var3) {
            var13 = var9;
            var15 = var7;
         } else if (var6 == var3 + 1) {
            var8 += var9 * 10;
            var13 = var8;
            var15 = var7;
            if (var1 != null) {
               var13 = var8;
               var15 = var7;
               if (var9 == 0) {
                  var1[1] = var11;
                  var13 = var8;
                  var15 = var7;
               }
            }
         } else if (var6 == var3 + 2) {
            var15 = var9;
            var13 = var8;
         } else {
            var13 = var8;
            var15 = var7;
            if (var6 == var3 + 3) {
               var7 += var9 * 10;
               var13 = var8;
               var15 = var7;
               if (var1 != null) {
                  var13 = var8;
                  var15 = var7;
                  if (var9 == 0) {
                     var1[0] = var11;
                     var15 = var7;
                     var13 = var8;
                  }
               }
            }
         }

         ++var6;
         var8 = var13;
      }

      return new int[]{var7, var8, var5};
   }

   private int getValFromKeyCode(int var1) {
      switch(var1) {
      case 7:
         return 0;
      case 8:
         return 1;
      case 9:
         return 2;
      case 10:
         return 3;
      case 11:
         return 4;
      case 12:
         return 5;
      case 13:
         return 6;
      case 14:
         return 7;
      case 15:
         return 8;
      case 16:
         return 9;
      default:
         return -1;
      }
   }

   private boolean hasTimeLimits() {
      return this.mFutureMinutesLimit != null || this.mPastMinutesLimit != null;
   }

   private boolean isTypedTimeFullyLegal() {
      boolean var3 = this.mIs24HourMode;
      boolean var1 = false;
      boolean var2 = false;
      if (var3) {
         int[] var4 = this.getEnteredTime((Boolean[])null);
         var1 = var2;
         if (var4[0] >= 0) {
            var1 = var2;
            if (var4[1] >= 0) {
               var1 = var2;
               if (var4[1] < 60) {
                  var1 = true;
               }
            }
         }

         return var1;
      } else {
         if (this.mTypedTimes.contains(this.getAmOrPmKeyCode(0)) || this.mTypedTimes.contains(this.getAmOrPmKeyCode(1))) {
            var1 = true;
         }

         return var1;
      }
   }

   private boolean isTypedTimeLegalSoFar() {
      RadialTimePickerDialogFragment.Node var1 = this.mLegalTimesTree;
      Iterator var2 = this.mTypedTimes.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }

         var1 = var1.canReach((Integer)var2.next());
      } while(var1 != null);

      return false;
   }

   private boolean processKeyUp(int var1) {
      if (var1 != 111 && var1 != 4) {
         if (var1 == 61) {
            if (this.mInKbMode) {
               if (this.isTypedTimeFullyLegal()) {
                  this.finishKbMode(true);
               }

               return true;
            }
         } else {
            if (var1 == 66) {
               if (this.mInKbMode) {
                  if (!this.isTypedTimeFullyLegal()) {
                     return true;
                  }

                  this.finishKbMode(false);
               }

               this.doneClickValidateAndCallback();
               return true;
            }

            if (var1 == 67) {
               if (this.mInKbMode && !this.mTypedTimes.isEmpty()) {
                  var1 = this.deleteLastTypedKey();
                  String var2;
                  if (var1 == this.getAmOrPmKeyCode(0)) {
                     var2 = this.mAmText;
                  } else if (var1 == this.getAmOrPmKeyCode(1)) {
                     var2 = this.mPmText;
                  } else {
                     var2 = String.format("%d", this.getValFromKeyCode(var1));
                  }

                  Utils.tryAccessibilityAnnounce(this.mTimePicker, String.format(this.mDeletedKeyFormat, var2));
                  this.updateDisplay(true);
                  return false;
               }
            } else if (var1 == 7 || var1 == 8 || var1 == 9 || var1 == 10 || var1 == 11 || var1 == 12 || var1 == 13 || var1 == 14 || var1 == 15 || var1 == 16 || !this.mIs24HourMode && (var1 == this.getAmOrPmKeyCode(0) || var1 == this.getAmOrPmKeyCode(1))) {
               if (!this.mInKbMode) {
                  if (this.mTimePicker == null) {
                     Log.e("TimePickerDialog", "Unable to initiate keyboard mode, TimePicker was null.");
                     return true;
                  } else {
                     this.mTypedTimes.clear();
                     this.tryStartingKbMode(var1);
                     return true;
                  }
               } else {
                  if (this.addKeyIfLegal(var1)) {
                     this.updateDisplay(false);
                  }

                  return true;
               }
            }
         }

         return false;
      } else {
         this.dismiss();
         return true;
      }
   }

   private void setCurrentItemShowing(int var1, boolean var2, boolean var3, boolean var4) {
      this.mTimePicker.setCurrentItemShowing(var1, var2);
      int var5;
      RadialPickerLayout var7;
      StringBuilder var8;
      TextView var9;
      if (var1 == 0) {
         int var6 = this.mTimePicker.getHours();
         var5 = var6;
         if (!this.mIs24HourMode) {
            var5 = var6 % 12;
         }

         var7 = this.mTimePicker;
         var8 = new StringBuilder();
         var8.append(this.mHourPickerDescription);
         var8.append(": ");
         var8.append(var5);
         var7.setContentDescription(var8.toString());
         if (var4) {
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectHours);
         }

         var9 = this.mHourView;
      } else {
         var5 = this.mTimePicker.getMinutes();
         var7 = this.mTimePicker;
         var8 = new StringBuilder();
         var8.append(this.mMinutePickerDescription);
         var8.append(": ");
         var8.append(var5);
         var7.setContentDescription(var8.toString());
         if (var4) {
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectMinutes);
         }

         var9 = this.mMinuteView;
      }

      if (var1 == 0) {
         var5 = this.mSelectedColor;
      } else {
         var5 = this.mUnselectedColor;
      }

      if (var1 == 1) {
         var1 = this.mSelectedColor;
      } else {
         var1 = this.mUnselectedColor;
      }

      this.mHourView.setTextColor(var5);
      this.mMinuteView.setTextColor(var1);
      ObjectAnimator var10 = Utils.getPulseAnimator(var9, 0.85F, 1.1F);
      if (var3) {
         var10.setStartDelay(300L);
      }

      var10.start();
   }

   private void setHour(int var1, boolean var2) {
      String var4;
      if (this.mIs24HourMode) {
         var4 = "%02d";
      } else {
         String var5 = "%d";
         int var3 = var1 % 12;
         var4 = var5;
         var1 = var3;
         if (var3 == 0) {
            var1 = 12;
            var4 = var5;
         }
      }

      var4 = String.format(var4, var1);
      this.mHourView.setText(var4);
      this.mHourSpaceView.setText(var4);
      if (var2) {
         Utils.tryAccessibilityAnnounce(this.mTimePicker, var4);
      }

   }

   private void setMinute(int var1) {
      int var2 = var1;
      if (var1 == 60) {
         var2 = 0;
      }

      String var3 = String.format(Locale.getDefault(), "%02d", var2);
      Utils.tryAccessibilityAnnounce(this.mTimePicker, var3);
      this.mMinuteView.setText(var3);
      this.mMinuteSpaceView.setText(var3);
   }

   private void tryStartingKbMode(int var1) {
      if (this.mTimePicker.trySettingInputEnabled(false) && (var1 == -1 || this.addKeyIfLegal(var1))) {
         this.mInKbMode = true;
         this.mDoneButton.setEnabled(false);
         this.updateDisplay(false);
      }

   }

   private void updateAmPmDisplay(int var1) {
      if (var1 == 0) {
         this.mAmPmTextView.setText(this.mAmText);
         Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mAmText);
         this.mAmPmHitspace.setContentDescription(this.mAmText);
      } else if (var1 == 1) {
         this.mAmPmTextView.setText(this.mPmText);
         Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mPmText);
         this.mAmPmHitspace.setContentDescription(this.mPmText);
      } else {
         this.mAmPmTextView.setText(this.mDoublePlaceholderText);
      }
   }

   private void updateDisplay(boolean var1) {
      byte var2 = 0;
      Boolean var5 = false;
      if (!var1 && this.mTypedTimes.isEmpty()) {
         int var3 = this.mTimePicker.getHours();
         int var4 = this.mTimePicker.getMinutes();
         this.setHour(var3, true);
         this.setMinute(var4);
         if (!this.mIs24HourMode) {
            if (var3 >= 12) {
               var2 = 1;
            }

            this.updateAmPmDisplay(var2);
         }

         this.setCurrentItemShowing(this.mTimePicker.getCurrentItemShowing(), true, true, true);
         this.mDoneButton.setEnabled(true);
      } else {
         Boolean[] var8 = new Boolean[]{var5, var5};
         int[] var7 = this.getEnteredTime(var8);
         var1 = var8[0];
         String var6 = "%02d";
         String var9;
         if (var1) {
            var9 = "%02d";
         } else {
            var9 = "%2d";
         }

         if (!var8[1]) {
            var6 = "%2d";
         }

         if (var7[0] == -1) {
            var9 = this.mDoublePlaceholderText;
         } else {
            var9 = String.format(var9, var7[0]).replace(' ', this.mPlaceholderText);
         }

         if (var7[1] == -1) {
            var6 = this.mDoublePlaceholderText;
         } else {
            var6 = String.format(var6, var7[1]).replace(' ', this.mPlaceholderText);
         }

         this.mHourView.setText(var9);
         this.mHourSpaceView.setText(var9);
         this.mHourView.setTextColor(this.mUnselectedColor);
         this.mMinuteView.setText(var6);
         this.mMinuteSpaceView.setText(var6);
         this.mMinuteView.setTextColor(this.mUnselectedColor);
         if (!this.mIs24HourMode) {
            this.updateAmPmDisplay(var7[2]);
         }

      }
   }

   public void doneClickValidateAndCallback() {
      NumberPickerErrorTextView var1;
      if (this.isSelectionTooFarInTheFuture()) {
         var1 = this.mError;
         if (var1 != null) {
            var1.setText(this.getString(string.max_time_error));
            this.mError.show();
            return;
         }
      } else if (this.isSelectionTooFarInPast()) {
         var1 = this.mError;
         if (var1 != null) {
            var1.setText(this.getString(string.min_time_error));
            this.mError.show();
            return;
         }
      } else {
         RadialTimePickerDialogFragment.OnTimeSetListener var2 = this.mCallback;
         if (var2 != null) {
            var2.onTimeSet(this, this.mTimePicker.getHours(), this.mTimePicker.getMinutes());
         }

         this.dismiss();
      }

   }

   public boolean isSelectionTooFarInPast() {
      Calendar var2 = this.mPickerDate;
      boolean var1 = false;
      if (var2 != null && this.mValidateDateTime != null && this.mPastMinutesLimit != null) {
         var2 = Calendar.getInstance();
         var2.setTime(this.mPickerDate.getTime());
         var2.set(11, this.mTimePicker.getHours());
         var2.set(12, this.mTimePicker.getMinutes());
         Calendar var3 = Calendar.getInstance();
         var3.setTime(this.mValidateDateTime.getTime());
         var3.add(12, -this.mPastMinutesLimit);
         if (var2.compareTo(var3) < 0) {
            var1 = true;
         }

         return var1;
      } else {
         return false;
      }
   }

   public boolean isSelectionTooFarInTheFuture() {
      Calendar var2 = this.mPickerDate;
      boolean var1 = false;
      if (var2 != null && this.mValidateDateTime != null && this.mFutureMinutesLimit != null) {
         var2 = Calendar.getInstance();
         var2.setTime(this.mPickerDate.getTime());
         var2.set(11, this.mTimePicker.getHours());
         var2.set(12, this.mTimePicker.getMinutes());
         Calendar var3 = Calendar.getInstance();
         var3.setTime(this.mValidateDateTime.getTime());
         var3.add(12, this.mFutureMinutesLimit);
         if (var2.compareTo(var3) > 0) {
            var1 = true;
         }

         return var1;
      } else {
         return false;
      }
   }

   public boolean isThemeDark() {
      return this.mStyleResId == style.BetterPickersRadialTimePickerDialog_Dark;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (var1 != null && var1.containsKey("hour_of_day") && var1.containsKey("minute") && var1.containsKey("is_24_hour_view")) {
         this.mInitialHourOfDay = var1.getInt("hour_of_day");
         this.mInitialMinute = var1.getInt("minute");
         this.mIs24HourMode = var1.getBoolean("is_24_hour_view");
         this.mInKbMode = var1.getBoolean("in_kb_mode");
         this.mStyleResId = var1.getInt("theme");
         if (var1.containsKey("future_minutes_limit")) {
            this.mFutureMinutesLimit = var1.getInt("future_minutes_limit");
         }

         if (var1.containsKey("past_minutes_limit")) {
            this.mPastMinutesLimit = var1.getInt("past_minutes_limit");
         }

         if (var1.containsKey("current_date")) {
            this.mValidateDateTime = (Calendar)var1.getSerializable("current_date");
         }

         if (var1.containsKey("picker_date")) {
            this.mPickerDate = (Calendar)var1.getSerializable("picker_date");
            return;
         }
      } else if (this.mIs24HourMode == null) {
         this.mIs24HourMode = DateFormat.is24HourFormat(this.getContext());
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      if (this.getShowsDialog()) {
         this.getDialog().getWindow().requestFeature(1);
      }

      View var14 = var1.inflate(layout.radial_time_picker_dialog, var2, false);
      RadialTimePickerDialogFragment.KeyboardListener var11 = new RadialTimePickerDialogFragment.KeyboardListener();
      var14.findViewById(id.time_picker_dialog).setOnKeyListener(var11);
      Resources var15 = this.getResources();
      TypedArray var10 = this.getActivity().obtainStyledAttributes(this.mStyleResId, styleable.BetterPickersDialogs);
      int var6 = var10.getColor(styleable.BetterPickersDialogs_bpHeaderBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpBlue));
      int var7 = var10.getColor(styleable.BetterPickersDialogs_bpBodyBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var8 = var10.getColor(styleable.BetterPickersDialogs_bpButtonsBackgroundColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      int var9 = var10.getColor(styleable.BetterPickersDialogs_bpButtonsTextColor, ContextCompat.getColor(this.getActivity(), color.bpBlue));
      this.mSelectedColor = var10.getColor(styleable.BetterPickersDialogs_bpHeaderSelectedTextColor, ContextCompat.getColor(this.getActivity(), color.bpWhite));
      this.mUnselectedColor = var10.getColor(styleable.BetterPickersDialogs_bpHeaderUnselectedTextColor, ContextCompat.getColor(this.getActivity(), color.radial_gray_light));
      this.mHourPickerDescription = var15.getString(string.hour_picker_description);
      this.mSelectHours = var15.getString(string.select_hours);
      this.mMinutePickerDescription = var15.getString(string.minute_picker_description);
      this.mSelectMinutes = var15.getString(string.select_minutes);
      TextView var12 = (TextView)var14.findViewById(id.hours);
      this.mHourView = var12;
      var12.setOnKeyListener(var11);
      this.mHourSpaceView = (TextView)var14.findViewById(id.hour_space);
      this.mMinuteSpaceView = (TextView)var14.findViewById(id.minutes_space);
      var12 = (TextView)var14.findViewById(id.minutes);
      this.mMinuteView = var12;
      var12.setOnKeyListener(var11);
      var12 = (TextView)var14.findViewById(id.ampm_label);
      this.mAmPmTextView = var12;
      var12.setOnKeyListener(var11);
      String[] var19 = (new DateFormatSymbols()).getAmPmStrings();
      this.mAmText = var19[0];
      this.mPmText = var19[1];
      this.mHapticFeedbackController = new HapticFeedbackController(this.getActivity());
      RadialPickerLayout var20 = (RadialPickerLayout)var14.findViewById(id.time_picker);
      this.mTimePicker = var20;
      var20.setOnValueSelectedListener(this);
      this.mTimePicker.setOnKeyListener(var11);
      this.mTimePicker.initialize(this.getActivity(), this.mHapticFeedbackController, this.mInitialHourOfDay, this.mInitialMinute, this.mIs24HourMode);
      byte var5 = 0;
      int var4 = var5;
      if (var3 != null) {
         var4 = var5;
         if (var3.containsKey("current_item_showing")) {
            var4 = var3.getInt("current_item_showing");
         }
      }

      this.setCurrentItemShowing(var4, false, true, true);
      this.mTimePicker.invalidate();
      this.mHourView.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            RadialTimePickerDialogFragment.this.setCurrentItemShowing(0, true, false, true);
            RadialTimePickerDialogFragment.this.tryVibrate();
         }
      });
      this.mMinuteView.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            RadialTimePickerDialogFragment.this.setCurrentItemShowing(1, true, false, true);
            RadialTimePickerDialogFragment.this.tryVibrate();
         }
      });
      var12 = (TextView)var14.findViewById(id.time_picker_header);
      this.mTitleTextView = var12;
      if (this.mTitleText != null) {
         var12.setVisibility(0);
         this.mTitleTextView.setText(this.mTitleText);
      } else {
         var12.setVisibility(8);
      }

      this.mError = (NumberPickerErrorTextView)var14.findViewById(id.error);
      if (this.hasTimeLimits()) {
         this.mError.setVisibility(4);
      } else {
         this.mError.setVisibility(8);
      }

      Button var21 = (Button)var14.findViewById(id.done_button);
      this.mDoneButton = var21;
      String var13 = this.mDoneText;
      if (var13 != null) {
         var21.setText(var13);
      }

      this.mDoneButton.setTextColor(var9);
      this.mDoneButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (RadialTimePickerDialogFragment.this.mInKbMode && RadialTimePickerDialogFragment.this.isTypedTimeFullyLegal()) {
               RadialTimePickerDialogFragment.this.finishKbMode(false);
            } else {
               RadialTimePickerDialogFragment.this.tryVibrate();
            }

            RadialTimePickerDialogFragment.this.doneClickValidateAndCallback();
         }
      });
      this.mDoneButton.setOnKeyListener(var11);
      Button var17 = (Button)var14.findViewById(id.cancel_button);
      String var22 = this.mCancelText;
      if (var22 != null) {
         var17.setText(var22);
      }

      var17.setTextColor(var9);
      var17.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            RadialTimePickerDialogFragment.this.tryVibrate();
            RadialTimePickerDialogFragment.this.dismiss();
         }
      });
      this.mAmPmHitspace = var14.findViewById(id.ampm_hitspace);
      if (this.mIs24HourMode) {
         this.mAmPmTextView.setVisibility(8);
         LayoutParams var18 = new LayoutParams(-2, -2);
         var18.addRule(13);
         ((TextView)var14.findViewById(id.separator)).setLayoutParams(var18);
      } else {
         this.mAmPmTextView.setVisibility(0);
         byte var16;
         if (this.mInitialHourOfDay < 12) {
            var16 = 0;
         } else {
            var16 = 1;
         }

         this.updateAmPmDisplay(var16);
         this.mAmPmHitspace.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               RadialTimePickerDialogFragment.this.tryVibrate();
               int var3 = RadialTimePickerDialogFragment.this.mTimePicker.getIsCurrentlyAmOrPm();
               int var2;
               if (var3 == 0) {
                  var2 = 1;
               } else {
                  var2 = var3;
                  if (var3 == 1) {
                     var2 = 0;
                  }
               }

               RadialTimePickerDialogFragment.this.updateAmPmDisplay(var2);
               RadialTimePickerDialogFragment.this.mTimePicker.setAmOrPm(var2);
            }
         });
      }

      this.mAllowAutoAdvance = true;
      this.setHour(this.mInitialHourOfDay, true);
      this.setMinute(this.mInitialMinute);
      this.mDoublePlaceholderText = var15.getString(string.time_placeholder);
      this.mDeletedKeyFormat = var15.getString(string.deleted_key);
      this.mPlaceholderText = this.mDoublePlaceholderText.charAt(0);
      this.mPmKeyCode = -1;
      this.mAmKeyCode = -1;
      this.generateLegalTimesTree();
      if (this.mInKbMode) {
         this.mTypedTimes = var3.getIntegerArrayList("typed_times");
         this.tryStartingKbMode(-1);
         this.mHourView.invalidate();
      } else if (this.mTypedTimes == null) {
         this.mTypedTimes = new ArrayList();
      }

      this.mTimePicker.setTheme(var10);
      var14.findViewById(id.time_display_background).setBackgroundColor(var6);
      var14.findViewById(id.ok_cancel_buttons_layout).setBackgroundColor(var8);
      var14.findViewById(id.time_display).setBackgroundColor(var6);
      var14.findViewById(id.time_picker_error_holder).setBackgroundColor(var6);
      ((TextView)var14.findViewById(id.separator)).setTextColor(this.mUnselectedColor);
      ((TextView)var14.findViewById(id.ampm_label)).setTextColor(this.mUnselectedColor);
      this.mTimePicker.setBackgroundColor(var7);
      return var14;
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      OnDialogDismissListener var2 = this.mDismissCallback;
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
      RadialPickerLayout var2 = this.mTimePicker;
      if (var2 != null) {
         var1.putInt("hour_of_day", var2.getHours());
         var1.putInt("minute", this.mTimePicker.getMinutes());
         var1.putBoolean("is_24_hour_view", this.mIs24HourMode);
         var1.putInt("current_item_showing", this.mTimePicker.getCurrentItemShowing());
         var1.putBoolean("in_kb_mode", this.mInKbMode);
         Integer var3 = this.mFutureMinutesLimit;
         if (var3 != null) {
            var1.putInt("future_minutes_limit", var3);
         }

         var3 = this.mPastMinutesLimit;
         if (var3 != null) {
            var1.putInt("past_minutes_limit", var3);
         }

         var1.putSerializable("current_date", this.mValidateDateTime);
         var1.putSerializable("picker_date", this.mPickerDate);
         if (this.mInKbMode) {
            var1.putIntegerArrayList("typed_times", this.mTypedTimes);
         }

         var1.putInt("theme", this.mStyleResId);
      }

   }

   public void onValueSelected(int var1, int var2, boolean var3) {
      if (this.hasTimeLimits()) {
         this.mError.hideImmediately();
      }

      StringBuilder var8;
      if (var1 != 0) {
         if (var1 == 1) {
            this.setMinute(var2);
            RadialPickerLayout var7 = this.mTimePicker;
            var8 = new StringBuilder();
            var8.append(this.mMinutePickerDescription);
            var8.append(": ");
            var8.append(var2);
            var7.setContentDescription(var8.toString());
         } else if (var1 == 2) {
            this.updateAmPmDisplay(var2);
         } else {
            if (var1 == 3) {
               if (!this.isTypedTimeFullyLegal()) {
                  this.mTypedTimes.clear();
               }

               this.finishKbMode(true);
            }

         }
      } else {
         this.setHour(var2, false);
         String var4 = String.format("%d", var2);
         if (this.mAllowAutoAdvance && var3) {
            this.setCurrentItemShowing(1, true, true, false);
            var8 = new StringBuilder();
            var8.append(var4);
            var8.append(". ");
            var8.append(this.mSelectMinutes);
            var4 = var8.toString();
         } else {
            RadialPickerLayout var5 = this.mTimePicker;
            StringBuilder var6 = new StringBuilder();
            var6.append(this.mHourPickerDescription);
            var6.append(": ");
            var6.append(var2);
            var5.setContentDescription(var6.toString());
         }

         Utils.tryAccessibilityAnnounce(this.mTimePicker, var4);
      }
   }

   @Deprecated
   public RadialTimePickerDialogFragment setAutodetectDateFormat(Context var1) {
      this.mIs24HourMode = DateFormat.is24HourFormat(var1);
      return this;
   }

   public RadialTimePickerDialogFragment setCancelText(String var1) {
      this.mCancelText = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setDoneText(String var1) {
      this.mDoneText = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setForced12hFormat() {
      this.mIs24HourMode = false;
      return this;
   }

   public RadialTimePickerDialogFragment setForced24hFormat() {
      this.mIs24HourMode = true;
      return this;
   }

   public RadialTimePickerDialogFragment setFutureMinutesLimit(Integer var1) {
      this.mFutureMinutesLimit = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setOnTimeSetListener(RadialTimePickerDialogFragment.OnTimeSetListener var1) {
      this.mCallback = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setPastMinutesLimit(Integer var1) {
      this.mPastMinutesLimit = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setPickerDate(Calendar var1) {
      this.mPickerDate = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setStartTime(int var1, int var2) {
      this.mInitialHourOfDay = var1;
      this.mInitialMinute = var2;
      this.mInKbMode = false;
      return this;
   }

   public RadialTimePickerDialogFragment setThemeCustom(int var1) {
      this.mStyleResId = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setThemeDark() {
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_Dark;
      return this;
   }

   public RadialTimePickerDialogFragment setThemeLight() {
      this.mStyleResId = style.BetterPickersRadialTimePickerDialog_Light;
      return this;
   }

   public RadialTimePickerDialogFragment setTitleText(String var1) {
      this.mTitleText = var1;
      return this;
   }

   public RadialTimePickerDialogFragment setValidateDateTime(Calendar var1) {
      this.mValidateDateTime = var1;
      return this;
   }

   public void tryVibrate() {
      this.mHapticFeedbackController.tryVibrate();
   }

   private class KeyboardListener implements OnKeyListener {
      private KeyboardListener() {
      }

      // $FF: synthetic method
      KeyboardListener(Object var2) {
         this();
      }

      public boolean onKey(View var1, int var2, KeyEvent var3) {
         return var3.getAction() == 1 && RadialTimePickerDialogFragment.this.processKeyUp(var2);
      }
   }

   private class Node {
      private ArrayList mChildren;
      private int[] mLegalKeys;

      public Node(int... var2) {
         this.mLegalKeys = var2;
         this.mChildren = new ArrayList();
      }

      public void addChild(RadialTimePickerDialogFragment.Node var1) {
         this.mChildren.add(var1);
      }

      public RadialTimePickerDialogFragment.Node canReach(int var1) {
         ArrayList var2 = this.mChildren;
         if (var2 == null) {
            return null;
         } else {
            Iterator var4 = var2.iterator();

            RadialTimePickerDialogFragment.Node var3;
            do {
               if (!var4.hasNext()) {
                  return null;
               }

               var3 = (RadialTimePickerDialogFragment.Node)var4.next();
            } while(!var3.containsKey(var1));

            return var3;
         }
      }

      public boolean containsKey(int var1) {
         int var2 = 0;

         while(true) {
            int[] var3 = this.mLegalKeys;
            if (var2 >= var3.length) {
               return false;
            }

            if (var3[var2] == var1) {
               return true;
            }

            ++var2;
         }
      }
   }

   public interface OnTimeSetListener {
      void onTimeSet(RadialTimePickerDialogFragment var1, int var2, int var3);
   }
}
