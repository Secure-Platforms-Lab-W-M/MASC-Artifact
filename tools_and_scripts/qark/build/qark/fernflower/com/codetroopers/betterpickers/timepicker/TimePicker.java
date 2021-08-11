package com.codetroopers.betterpickers.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.drawable;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.styleable;
import java.text.DateFormatSymbols;

public class TimePicker extends LinearLayout implements OnClickListener, OnLongClickListener {
   private static final int AMPM_NOT_SELECTED = 0;
   private static final int AM_SELECTED = 2;
   private static final int HOURS24_MODE = 3;
   private static final int PM_SELECTED = 1;
   private static final String TIME_PICKER_SAVED_AMPM = "timer_picker_saved_ampm";
   private static final String TIME_PICKER_SAVED_BUFFER_POINTER = "timer_picker_saved_buffer_pointer";
   private static final String TIME_PICKER_SAVED_INPUT = "timer_picker_saved_input";
   private TextView mAmPmLabel;
   private int mAmPmState;
   private String[] mAmpm;
   private int mButtonBackgroundResId;
   protected final Context mContext;
   protected ImageButton mDelete;
   private int mDeleteDrawableSrcResId;
   protected View mDivider;
   private int mDividerColor;
   protected TimerView mEnteredTime;
   protected int[] mInput;
   protected int mInputPointer;
   protected int mInputSize;
   private boolean mIs24HoursMode;
   private int mKeyBackgroundResId;
   protected Button mLeft;
   private final String mNoAmPmLabel;
   protected final Button[] mNumbers;
   protected Button mRight;
   private Button mSetButton;
   private ColorStateList mTextColor;
   private int mTheme;

   public TimePicker(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TimePicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mInputSize = 4;
      this.mNumbers = new Button[10];
      this.mInput = new int[4];
      this.mInputPointer = -1;
      this.mIs24HoursMode = false;
      this.mTheme = -1;
      this.mContext = var1;
      this.mIs24HoursMode = get24HourMode(var1);
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(this.getLayoutId(), this);
      this.mNoAmPmLabel = var1.getResources().getString(string.time_picker_ampm_label);
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.mKeyBackgroundResId = drawable.key_background_dark;
      this.mButtonBackgroundResId = drawable.button_background_dark;
      this.mDividerColor = this.getResources().getColor(color.default_divider_color_dark);
      this.mDeleteDrawableSrcResId = drawable.ic_backspace_dark;
   }

   private void addClickedNumber(int var1) {
      if (this.mInputPointer < this.mInputSize - 1) {
         for(int var2 = this.mInputPointer; var2 >= 0; --var2) {
            int[] var3 = this.mInput;
            var3[var2 + 1] = var3[var2];
         }

         ++this.mInputPointer;
         this.mInput[0] = var1;
      }

   }

   private boolean canAddDigits() {
      int var1 = this.getEnteredTime();
      boolean var2 = this.mIs24HoursMode;
      boolean var4 = false;
      boolean var3 = false;
      if (!var2) {
         var2 = var3;
         if (var1 >= 1) {
            var2 = var3;
            if (var1 <= 12) {
               var2 = true;
            }
         }

         return var2;
      } else {
         var2 = var4;
         if (var1 >= 0) {
            var2 = var4;
            if (var1 <= 23) {
               var1 = this.mInputPointer;
               var2 = var4;
               if (var1 > -1) {
                  var2 = var4;
                  if (var1 < 2) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      }
   }

   private void enableSetButton() {
      Button var4 = this.mSetButton;
      if (var4 != null) {
         int var1 = this.mInputPointer;
         boolean var2 = false;
         boolean var3 = false;
         if (var1 == -1) {
            var4.setEnabled(false);
         } else if (!this.mIs24HoursMode) {
            if (this.mAmPmState != 0) {
               var2 = true;
            }

            var4.setEnabled(var2);
         } else {
            var1 = this.getEnteredTime();
            var4 = this.mSetButton;
            var2 = var3;
            if (this.mInputPointer >= 2) {
               label24: {
                  if (var1 >= 60) {
                     var2 = var3;
                     if (var1 <= 95) {
                        break label24;
                     }
                  }

                  var2 = true;
               }
            }

            var4.setEnabled(var2);
         }
      }
   }

   public static boolean get24HourMode(Context var0) {
      return DateFormat.is24HourFormat(var0);
   }

   private int getEnteredTime() {
      int[] var1 = this.mInput;
      return var1[3] * 1000 + var1[2] * 100 + var1[1] * 10 + var1[0];
   }

   private void onLeftClicked() {
      this.getEnteredTime();
      if (!this.mIs24HoursMode) {
         if (this.canAddDigits()) {
            this.addClickedNumber(0);
            this.addClickedNumber(0);
         }

         this.mAmPmState = 2;
      } else {
         if (this.canAddDigits()) {
            this.addClickedNumber(0);
            this.addClickedNumber(0);
         }

      }
   }

   private void onRightClicked() {
      this.getEnteredTime();
      if (!this.mIs24HoursMode) {
         if (this.canAddDigits()) {
            this.addClickedNumber(0);
            this.addClickedNumber(0);
         }

         this.mAmPmState = 1;
      } else {
         if (this.canAddDigits()) {
            this.addClickedNumber(3);
            this.addClickedNumber(0);
         }

      }
   }

   private void restyleViews() {
      Button[] var3 = this.mNumbers;
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Button var4 = var3[var1];
         if (var4 != null) {
            var4.setTextColor(this.mTextColor);
            var4.setBackgroundResource(this.mKeyBackgroundResId);
         }
      }

      View var5 = this.mDivider;
      if (var5 != null) {
         var5.setBackgroundColor(this.mDividerColor);
      }

      Button var6 = this.mLeft;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mLeft.setBackgroundResource(this.mKeyBackgroundResId);
      }

      TextView var7 = this.mAmPmLabel;
      if (var7 != null) {
         var7.setTextColor(this.mTextColor);
         this.mAmPmLabel.setBackgroundResource(this.mKeyBackgroundResId);
      }

      var6 = this.mRight;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mRight.setBackgroundResource(this.mKeyBackgroundResId);
      }

      ImageButton var8 = this.mDelete;
      if (var8 != null) {
         var8.setBackgroundResource(this.mButtonBackgroundResId);
         this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
      }

      TimerView var9 = this.mEnteredTime;
      if (var9 != null) {
         var9.setTheme(this.mTheme);
      }

   }

   private void setKeyRange(int var1) {
      int var2 = 0;

      while(true) {
         Button[] var4 = this.mNumbers;
         if (var2 >= var4.length) {
            return;
         }

         Button var5 = var4[var2];
         boolean var3;
         if (var2 <= var1) {
            var3 = true;
         } else {
            var3 = false;
         }

         var5.setEnabled(var3);
         ++var2;
      }
   }

   private void showAmPm() {
      if (!this.mIs24HoursMode) {
         int var1 = this.mAmPmState;
         if (var1 != 0) {
            if (var1 != 1) {
               if (var1 == 2) {
                  this.mAmPmLabel.setText(this.mAmpm[0]);
               }
            } else {
               this.mAmPmLabel.setText(this.mAmpm[1]);
            }
         } else {
            this.mAmPmLabel.setText(this.mNoAmPmLabel);
         }
      } else {
         this.mAmPmLabel.setVisibility(4);
         this.mAmPmState = 3;
      }
   }

   private void updateKeypad() {
      this.showAmPm();
      this.updateLeftRightButtons();
      this.updateTime();
      this.updateNumericKeys();
      this.enableSetButton();
      this.updateDeleteButton();
   }

   private void updateLeftRightButtons() {
      int var1 = this.getEnteredTime();
      if (this.mIs24HoursMode) {
         boolean var2 = this.canAddDigits();
         this.mLeft.setEnabled(var2);
         this.mRight.setEnabled(var2);
      } else if ((var1 <= 12 || var1 >= 100) && var1 != 0 && this.mAmPmState == 0) {
         this.mLeft.setEnabled(true);
         this.mRight.setEnabled(true);
      } else {
         this.mLeft.setEnabled(false);
         this.mRight.setEnabled(false);
      }
   }

   private void updateNumericKeys() {
      int var1 = this.getEnteredTime();
      if (this.mIs24HoursMode) {
         int var2 = this.mInputPointer;
         if (var2 >= 3) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 == 0) {
            if (var2 != -1 && var2 != 0 && var2 != 2) {
               if (var2 == 1) {
                  this.setKeyRange(5);
                  return;
               }

               this.setKeyRange(-1);
               return;
            }

            this.setKeyRange(9);
            return;
         }

         if (var1 == 1) {
            if (var2 != 0 && var2 != 2) {
               if (var2 == 1) {
                  this.setKeyRange(5);
                  return;
               }

               this.setKeyRange(-1);
               return;
            }

            this.setKeyRange(9);
            return;
         }

         if (var1 == 2) {
            if (var2 != 2 && var2 != 1) {
               if (var2 == 0) {
                  this.setKeyRange(3);
                  return;
               }

               this.setKeyRange(-1);
               return;
            }

            this.setKeyRange(9);
            return;
         }

         if (var1 <= 5) {
            this.setKeyRange(9);
            return;
         }

         if (var1 <= 9) {
            this.setKeyRange(5);
            return;
         }

         if (var1 >= 10 && var1 <= 15) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 16 && var1 <= 19) {
            this.setKeyRange(5);
            return;
         }

         if (var1 >= 20 && var1 <= 25) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 26 && var1 <= 29) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 30 && var1 <= 35) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 36 && var1 <= 39) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 40 && var1 <= 45) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 46 && var1 <= 49) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 50 && var1 <= 55) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 56 && var1 <= 59) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 60 && var1 <= 65) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 70 && var1 <= 75) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 80 && var1 <= 85) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 90 && var1 <= 95) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 100 && var1 <= 105) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 106 && var1 <= 109) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 110 && var1 <= 115) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 116 && var1 <= 119) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 120 && var1 <= 125) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 126 && var1 <= 129) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 130 && var1 <= 135) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 136 && var1 <= 139) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 140 && var1 <= 145) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 146 && var1 <= 149) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 150 && var1 <= 155) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 156 && var1 <= 159) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 160 && var1 <= 165) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 166 && var1 <= 169) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 170 && var1 <= 175) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 176 && var1 <= 179) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 180 && var1 <= 185) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 186 && var1 <= 189) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 190 && var1 <= 195) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 196 && var1 <= 199) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 200 && var1 <= 205) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 206 && var1 <= 209) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 210 && var1 <= 215) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 216 && var1 <= 219) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 220 && var1 <= 225) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 226 && var1 <= 229) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 230 && var1 <= 235) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 236) {
            this.setKeyRange(-1);
            return;
         }
      } else {
         if (this.mAmPmState != 0) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 == 0) {
            this.setKeyRange(9);
            this.mNumbers[0].setEnabled(false);
            return;
         }

         if (var1 <= 9) {
            this.setKeyRange(5);
            return;
         }

         if (var1 <= 95) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 100 && var1 <= 105) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 106 && var1 <= 109) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 110 && var1 <= 115) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 116 && var1 <= 119) {
            this.setKeyRange(-1);
            return;
         }

         if (var1 >= 120 && var1 <= 125) {
            this.setKeyRange(9);
            return;
         }

         if (var1 >= 126) {
            this.setKeyRange(-1);
         }
      }

   }

   protected void doOnClick(View var1) {
      Integer var4 = (Integer)var1.getTag(id.numbers_key);
      if (var4 != null) {
         this.addClickedNumber(var4);
      } else if (var1 == this.mDelete) {
         if (!this.mIs24HoursMode && this.mAmPmState != 0) {
            this.mAmPmState = 0;
         } else if (this.mInputPointer >= 0) {
            int var2 = 0;

            while(true) {
               int var3 = this.mInputPointer;
               if (var2 >= var3) {
                  this.mInput[var3] = 0;
                  this.mInputPointer = var3 - 1;
                  break;
               }

               int[] var5 = this.mInput;
               var5[var2] = var5[var2 + 1];
               ++var2;
            }
         }
      } else if (var1 == this.mLeft) {
         this.onLeftClicked();
      } else if (var1 == this.mRight) {
         this.onRightClicked();
      }

      this.updateKeypad();
   }

   public int getHours() {
      int[] var4 = this.mInput;
      int var2 = var4[3] * 10 + var4[2];
      byte var1 = 0;
      if (var2 == 12) {
         int var3 = this.mAmPmState;
         if (var3 == 1) {
            return 12;
         }

         if (var3 == 2) {
            return 0;
         }

         if (var3 == 3) {
            return var2;
         }
      }

      if (this.mAmPmState == 1) {
         var1 = 12;
      }

      return var1 + var2;
   }

   protected int getLayoutId() {
      return layout.time_picker_view;
   }

   public int getMinutes() {
      int[] var1 = this.mInput;
      return var1[1] * 10 + var1[0];
   }

   public int getTime() {
      int[] var1 = this.mInput;
      return var1[4] * 3600 + var1[3] * 600 + var1[2] * 60 + var1[1] * 10 + var1[0];
   }

   public void onClick(View var1) {
      var1.performHapticFeedback(1);
      this.doOnClick(var1);
      this.updateDeleteButton();
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      View var2 = this.findViewById(id.first);
      View var3 = this.findViewById(id.second);
      View var4 = this.findViewById(id.third);
      View var5 = this.findViewById(id.fourth);
      this.mEnteredTime = (TimerView)this.findViewById(id.timer_time_text);
      ImageButton var6 = (ImageButton)this.findViewById(id.delete);
      this.mDelete = var6;
      var6.setOnClickListener(this);
      this.mDelete.setOnLongClickListener(this);
      this.mNumbers[1] = (Button)var2.findViewById(id.key_left);
      this.mNumbers[2] = (Button)var2.findViewById(id.key_middle);
      this.mNumbers[3] = (Button)var2.findViewById(id.key_right);
      this.mNumbers[4] = (Button)var3.findViewById(id.key_left);
      this.mNumbers[5] = (Button)var3.findViewById(id.key_middle);
      this.mNumbers[6] = (Button)var3.findViewById(id.key_right);
      this.mNumbers[7] = (Button)var4.findViewById(id.key_left);
      this.mNumbers[8] = (Button)var4.findViewById(id.key_middle);
      this.mNumbers[9] = (Button)var4.findViewById(id.key_right);
      this.mLeft = (Button)var5.findViewById(id.key_left);
      this.mNumbers[0] = (Button)var5.findViewById(id.key_middle);
      this.mRight = (Button)var5.findViewById(id.key_right);
      this.setLeftRightEnabled(false);

      for(int var1 = 0; var1 < 10; ++var1) {
         this.mNumbers[var1].setOnClickListener(this);
         this.mNumbers[var1].setText(String.format("%d", var1));
         this.mNumbers[var1].setTag(id.numbers_key, new Integer(var1));
      }

      this.updateTime();
      Resources var7 = this.mContext.getResources();
      String[] var8 = (new DateFormatSymbols()).getAmPmStrings();
      this.mAmpm = var8;
      if (this.mIs24HoursMode) {
         this.mLeft.setText(var7.getString(string.time_picker_00_label));
         this.mRight.setText(var7.getString(string.time_picker_30_label));
      } else {
         this.mLeft.setText(var8[0]);
         this.mRight.setText(this.mAmpm[1]);
      }

      this.mLeft.setOnClickListener(this);
      this.mRight.setOnClickListener(this);
      this.mAmPmLabel = (TextView)this.findViewById(id.ampm_label);
      this.mAmPmState = 0;
      this.mDivider = this.findViewById(id.divider);
      this.restyleViews();
      this.updateKeypad();
   }

   public boolean onLongClick(View var1) {
      var1.performHapticFeedback(0);
      ImageButton var2 = this.mDelete;
      if (var1 == var2) {
         var2.setPressed(false);
         this.mAmPmState = 0;
         this.reset();
         this.updateKeypad();
         return true;
      } else {
         return false;
      }
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof TimePicker.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         TimePicker.SavedState var3 = (TimePicker.SavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         this.mInputPointer = var3.mInputPointer;
         int[] var2 = var3.mInput;
         this.mInput = var2;
         if (var2 == null) {
            this.mInput = new int[this.mInputSize];
            this.mInputPointer = -1;
         }

         this.mAmPmState = var3.mAmPmState;
         this.updateKeypad();
      }
   }

   public Parcelable onSaveInstanceState() {
      TimePicker.SavedState var1 = new TimePicker.SavedState(super.onSaveInstanceState());
      var1.mInput = this.mInput;
      var1.mAmPmState = this.mAmPmState;
      var1.mInputPointer = this.mInputPointer;
      return var1;
   }

   public void reset() {
      for(int var1 = 0; var1 < this.mInputSize; ++var1) {
         this.mInput[var1] = 0;
      }

      this.mInputPointer = -1;
      this.updateTime();
   }

   public void restoreEntryState(Bundle var1, String var2) {
      int[] var4 = var1.getIntArray(var2);
      if (var4 != null && this.mInputSize == var4.length) {
         for(int var3 = 0; var3 < this.mInputSize; ++var3) {
            int[] var5 = this.mInput;
            var5[var3] = var4[var3];
            if (var5[var3] != 0) {
               this.mInputPointer = var3;
            }
         }

         this.updateTime();
      }

   }

   public void saveEntryState(Bundle var1, String var2) {
      var1.putIntArray(var2, this.mInput);
   }

   protected void setLeftRightEnabled(boolean var1) {
      this.mLeft.setEnabled(var1);
      this.mRight.setEnabled(var1);
      if (!var1) {
         this.mLeft.setContentDescription((CharSequence)null);
         this.mRight.setContentDescription((CharSequence)null);
      }

   }

   public void setSetButton(Button var1) {
      this.mSetButton = var1;
      this.enableSetButton();
   }

   public void setTheme(int var1) {
      this.mTheme = var1;
      if (var1 != -1) {
         TypedArray var2 = this.getContext().obtainStyledAttributes(var1, styleable.BetterPickersDialogFragment);
         this.mTextColor = var2.getColorStateList(styleable.BetterPickersDialogFragment_bpTextColor);
         this.mKeyBackgroundResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpKeyBackground, this.mKeyBackgroundResId);
         this.mButtonBackgroundResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpButtonBackground, this.mButtonBackgroundResId);
         this.mDividerColor = var2.getColor(styleable.BetterPickersDialogFragment_bpDividerColor, this.mDividerColor);
         this.mDeleteDrawableSrcResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpDeleteIcon, this.mDeleteDrawableSrcResId);
      }

      this.restyleViews();
   }

   public void updateDeleteButton() {
      boolean var1;
      if (this.mInputPointer != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      ImageButton var2 = this.mDelete;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

   }

   protected void updateTime() {
      byte var2 = -1;
      this.getEnteredTime();
      int var3 = this.mInputPointer;
      int var4 = -1;
      int var6;
      if (var3 > -1) {
         byte var1 = var2;
         if (var3 >= 0) {
            label92: {
               var3 = this.mInput[var3];
               if (!this.mIs24HoursMode || var3 < 3 || var3 > 9) {
                  var1 = var2;
                  if (this.mIs24HoursMode) {
                     break label92;
                  }

                  var1 = var2;
                  if (var3 < 2) {
                     break label92;
                  }

                  var1 = var2;
                  if (var3 > 9) {
                     break label92;
                  }
               }

               var1 = -2;
            }
         }

         var3 = this.mInputPointer;
         var2 = var1;
         if (var3 > 0) {
            var2 = var1;
            if (var3 < 3) {
               var2 = var1;
               if (var1 != -2) {
                  label93: {
                     int[] var5 = this.mInput;
                     var3 = var5[var3] * 10 + var5[var3 - 1];
                     if (!this.mIs24HoursMode || var3 < 24 || var3 > 25) {
                        var2 = var1;
                        if (this.mIs24HoursMode) {
                           break label93;
                        }

                        var2 = var1;
                        if (var3 < 13) {
                           break label93;
                        }

                        var2 = var1;
                        if (var3 > 15) {
                           break label93;
                        }
                     }

                     var2 = -2;
                  }
               }
            }
         }

         var6 = var2;
         if (this.mInputPointer == 3) {
            var6 = this.mInput[3];
         }
      } else {
         var6 = -1;
      }

      int var7;
      if (this.mInputPointer < 2) {
         var7 = -1;
      } else {
         var7 = this.mInput[2];
      }

      if (this.mInputPointer < 1) {
         var3 = -1;
      } else {
         var3 = this.mInput[1];
      }

      if (this.mInputPointer >= 0) {
         var4 = this.mInput[0];
      }

      this.mEnteredTime.setTime(var6, var7, var3, var4);
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public TimePicker.SavedState createFromParcel(Parcel var1) {
            return new TimePicker.SavedState(var1);
         }

         public TimePicker.SavedState[] newArray(int var1) {
            return new TimePicker.SavedState[var1];
         }
      };
      int mAmPmState;
      int[] mInput;
      int mInputPointer;

      private SavedState(Parcel var1) {
         super(var1);
         this.mInputPointer = var1.readInt();
         var1.readIntArray(this.mInput);
         this.mAmPmState = var1.readInt();
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
         var1.writeInt(this.mInputPointer);
         var1.writeIntArray(this.mInput);
         var1.writeInt(this.mAmPmState);
      }
   }
}
