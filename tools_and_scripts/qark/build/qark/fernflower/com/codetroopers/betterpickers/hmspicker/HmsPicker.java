package com.codetroopers.betterpickers.hmspicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
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

public class HmsPicker extends LinearLayout implements OnClickListener, OnLongClickListener {
   public static final int SIGN_NEGATIVE = 1;
   public static final int SIGN_POSITIVE = 0;
   private int mButtonBackgroundResId;
   protected final Context mContext;
   protected ImageButton mDelete;
   private int mDeleteDrawableSrcResId;
   protected View mDivider;
   private int mDividerColor;
   protected HmsView mEnteredHms;
   private TextView mHoursLabel;
   protected int[] mInput;
   protected int mInputPointer;
   protected int mInputSize;
   private int mKeyBackgroundResId;
   protected Button mLeft;
   private TextView mMinutesLabel;
   protected final Button[] mNumbers;
   protected Button mRight;
   private TextView mSecondsLabel;
   private Button mSetButton;
   private int mSign;
   private ColorStateList mTextColor;
   private int mTheme;

   public HmsPicker(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public HmsPicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mInputSize = 5;
      this.mNumbers = new Button[10];
      this.mInput = new int[5];
      this.mInputPointer = -1;
      this.mTheme = -1;
      this.mContext = var1;
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(this.getLayoutId(), this);
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.mKeyBackgroundResId = drawable.key_background_dark;
      this.mButtonBackgroundResId = drawable.button_background_dark;
      this.mDividerColor = this.getResources().getColor(color.default_divider_color_dark);
      this.mDeleteDrawableSrcResId = drawable.ic_backspace_dark;
   }

   private void addClickedNumber(int var1) {
      int var2 = this.mInputPointer;
      if (var2 < this.mInputSize - 1 && (var2 != -1 || var1 != 0)) {
         for(var2 = this.mInputPointer; var2 >= 0; --var2) {
            int[] var3 = this.mInput;
            var3[var2 + 1] = var3[var2];
         }

         ++this.mInputPointer;
         this.mInput[0] = var1;
      }

   }

   private void enableSetButton() {
      Button var3 = this.mSetButton;
      if (var3 != null) {
         int var1 = this.mInputPointer;
         boolean var2 = false;
         if (var1 == -1) {
            var3.setEnabled(false);
         } else {
            if (var1 >= 0) {
               var2 = true;
            }

            var3.setEnabled(var2);
         }
      }
   }

   private void onLeftClicked() {
      if (this.isNegative()) {
         this.mSign = 0;
      } else {
         this.mSign = 1;
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

      TextView var6 = this.mHoursLabel;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mHoursLabel.setBackgroundResource(this.mKeyBackgroundResId);
      }

      var6 = this.mMinutesLabel;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mMinutesLabel.setBackgroundResource(this.mKeyBackgroundResId);
      }

      var6 = this.mSecondsLabel;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mSecondsLabel.setBackgroundResource(this.mKeyBackgroundResId);
      }

      ImageButton var7 = this.mDelete;
      if (var7 != null) {
         var7.setBackgroundResource(this.mButtonBackgroundResId);
         this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
      }

      HmsView var8 = this.mEnteredHms;
      if (var8 != null) {
         var8.setTheme(this.mTheme);
      }

      Button var9 = this.mLeft;
      if (var9 != null) {
         var9.setTextColor(this.mTextColor);
         this.mLeft.setBackgroundResource(this.mKeyBackgroundResId);
      }

   }

   private void updateKeypad() {
      this.updateHms();
      this.enableSetButton();
      this.updateDeleteButton();
   }

   protected void doOnClick(View var1) {
      Integer var4 = (Integer)var1.getTag(id.numbers_key);
      if (var4 != null) {
         this.addClickedNumber(var4);
      } else if (var1 == this.mDelete) {
         if (this.mInputPointer >= 0) {
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
      }

      this.updateKeypad();
   }

   public int getHours() {
      return this.mInput[4];
   }

   protected int getLayoutId() {
      return layout.hms_picker_view;
   }

   public int getMinutes() {
      int[] var1 = this.mInput;
      return var1[3] * 10 + var1[2];
   }

   public int getSeconds() {
      int[] var1 = this.mInput;
      return var1[1] * 10 + var1[0];
   }

   public int getTime() {
      int[] var1 = this.mInput;
      return var1[4] * 3600 + var1[3] * 600 + var1[2] * 60 + var1[1] * 10 + var1[0];
   }

   public boolean isNegative() {
      return this.mSign == 1;
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
      this.mEnteredHms = (HmsView)this.findViewById(id.hms_text);
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
      this.setRightEnabled(false);

      for(int var1 = 0; var1 < 10; ++var1) {
         this.mNumbers[var1].setOnClickListener(this);
         this.mNumbers[var1].setText(String.format("%d", var1));
         this.mNumbers[var1].setTag(id.numbers_key, new Integer(var1));
      }

      this.updateHms();
      Resources var7 = this.mContext.getResources();
      this.mLeft.setText(var7.getString(string.number_picker_plus_minus));
      this.mLeft.setOnClickListener(this);
      this.mHoursLabel = (TextView)this.findViewById(id.hours_label);
      this.mMinutesLabel = (TextView)this.findViewById(id.minutes_label);
      this.mSecondsLabel = (TextView)this.findViewById(id.seconds_label);
      this.mDivider = this.findViewById(id.divider);
      this.restyleViews();
      this.updateKeypad();
   }

   public boolean onLongClick(View var1) {
      var1.performHapticFeedback(0);
      ImageButton var2 = this.mDelete;
      if (var1 == var2) {
         var2.setPressed(false);
         this.reset();
         this.updateKeypad();
         return true;
      } else {
         return false;
      }
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof HmsPicker.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         HmsPicker.SavedState var2 = (HmsPicker.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mInputPointer = var2.mInputPointer;
         int[] var3 = var2.mInput;
         this.mInput = var3;
         if (var3 == null) {
            this.mInput = new int[this.mInputSize];
            this.mInputPointer = -1;
         }

         this.updateKeypad();
      }
   }

   public Parcelable onSaveInstanceState() {
      HmsPicker.SavedState var1 = new HmsPicker.SavedState(super.onSaveInstanceState());
      var1.mInput = this.mInput;
      var1.mInputPointer = this.mInputPointer;
      return var1;
   }

   public void reset() {
      for(int var1 = 0; var1 < this.mInputSize; ++var1) {
         this.mInput[var1] = 0;
      }

      this.mInputPointer = -1;
      this.updateHms();
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

         this.updateHms();
      }

   }

   public void saveEntryState(Bundle var1, String var2) {
      var1.putIntArray(var2, this.mInput);
   }

   public void setPlusMinusVisibility(int var1) {
      Button var2 = this.mLeft;
      if (var2 != null) {
         var2.setVisibility(var1);
      }

   }

   protected void setRightEnabled(boolean var1) {
      this.mRight.setEnabled(var1);
      if (!var1) {
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

   public void setTime(int var1, int var2, int var3) {
      int[] var4 = this.mInput;
      var4[4] = var1;
      var4[3] = var2 / 10;
      var4[2] = var2 % 10;
      var4[1] = var3 / 10;
      var4[0] = var3 % 10;

      for(var1 = 4; var1 >= 0; --var1) {
         if (this.mInput[var1] > 0) {
            this.mInputPointer = var1;
            break;
         }
      }

      this.updateKeypad();
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

   protected void updateHms() {
      HmsView var2 = this.mEnteredHms;
      boolean var1 = this.isNegative();
      int[] var3 = this.mInput;
      var2.setTime(var1, var3[4], var3[3], var3[2], var3[1], var3[0]);
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public HmsPicker.SavedState createFromParcel(Parcel var1) {
            return new HmsPicker.SavedState(var1);
         }

         public HmsPicker.SavedState[] newArray(int var1) {
            return new HmsPicker.SavedState[var1];
         }
      };
      int mAmPmState;
      int[] mInput;
      int mInputPointer;

      private SavedState(Parcel var1) {
         super(var1);
         this.mInputPointer = var1.readInt();
         this.mInput = var1.createIntArray();
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
