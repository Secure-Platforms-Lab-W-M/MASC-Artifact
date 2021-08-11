package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class NumberPicker extends LinearLayout implements OnClickListener, OnLongClickListener {
   private static final int CLICKED_DECIMAL = 10;
   public static final int SIGN_NEGATIVE = 1;
   public static final int SIGN_POSITIVE = 0;
   private int mButtonBackgroundResId;
   protected final Context mContext;
   protected ImageButton mDelete;
   private int mDeleteDrawableSrcResId;
   protected View mDivider;
   private int mDividerColor;
   protected NumberView mEnteredNumber;
   private NumberPickerErrorTextView mError;
   protected int[] mInput;
   protected int mInputPointer;
   protected int mInputSize;
   private int mKeyBackgroundResId;
   private TextView mLabel;
   private String mLabelText;
   protected Button mLeft;
   private BigDecimal mMaxNumber;
   private BigDecimal mMinNumber;
   protected final Button[] mNumbers;
   protected Button mRight;
   private Button mSetButton;
   private int mSign;
   private ColorStateList mTextColor;
   private int mTheme;

   public NumberPicker(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NumberPicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mInputSize = 20;
      this.mNumbers = new Button[10];
      this.mInput = new int[20];
      this.mInputPointer = -1;
      this.mLabelText = "";
      this.mTheme = -1;
      this.mMinNumber = null;
      this.mMaxNumber = null;
      this.mContext = var1;
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(this.getLayoutId(), this);
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.mKeyBackgroundResId = drawable.key_background_dark;
      this.mButtonBackgroundResId = drawable.button_background_dark;
      this.mDeleteDrawableSrcResId = drawable.ic_backspace_dark;
      this.mDividerColor = this.getResources().getColor(color.default_divider_color_dark);
   }

   private void addClickedNumber(int var1) {
      if (this.mInputPointer < this.mInputSize - 1) {
         int[] var3 = this.mInput;
         if (var3[0] == 0 && var3[1] == -1 && !this.containsDecimal() && var1 != 10) {
            this.mInput[0] = var1;
            return;
         }

         for(int var2 = this.mInputPointer; var2 >= 0; --var2) {
            var3 = this.mInput;
            var3[var2 + 1] = var3[var2];
         }

         ++this.mInputPointer;
         this.mInput[0] = var1;
      }

   }

   private boolean canAddDecimal() {
      return this.containsDecimal() ^ true;
   }

   private boolean containsDecimal() {
      boolean var3 = false;
      int[] var4 = this.mInput;
      int var2 = var4.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         if (var4[var1] == 10) {
            var3 = true;
         }
      }

      return var3;
   }

   private String doubleToString(double var1) {
      DecimalFormat var3 = new DecimalFormat("0.0");
      var3.setMaximumFractionDigits(Integer.MAX_VALUE);
      return var3.format(var1);
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

   private String getEnteredNumberString() {
      String var2 = "";

      for(int var1 = this.mInputPointer; var1 >= 0; --var1) {
         int[] var3 = this.mInput;
         if (var3[var1] != -1) {
            StringBuilder var4;
            if (var3[var1] == 10) {
               var4 = new StringBuilder();
               var4.append(var2);
               var4.append(".");
               var2 = var4.toString();
            } else {
               var4 = new StringBuilder();
               var4.append(var2);
               var4.append(this.mInput[var1]);
               var2 = var4.toString();
            }
         }
      }

      return var2;
   }

   private void onLeftClicked() {
      if (this.mSign == 0) {
         this.mSign = 1;
      } else {
         this.mSign = 0;
      }
   }

   private void onRightClicked() {
      if (this.canAddDecimal()) {
         this.addClickedNumber(10);
      }

   }

   private void readAndRightDigits(String var1) {
      for(int var2 = var1.length() - 1; var2 >= 0; --var2) {
         int var3 = this.mInputPointer + 1;
         this.mInputPointer = var3;
         this.mInput[var3] = var1.charAt(var2) - 48;
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

      var6 = this.mRight;
      if (var6 != null) {
         var6.setTextColor(this.mTextColor);
         this.mRight.setBackgroundResource(this.mKeyBackgroundResId);
      }

      ImageButton var7 = this.mDelete;
      if (var7 != null) {
         var7.setBackgroundResource(this.mButtonBackgroundResId);
         this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
      }

      NumberView var8 = this.mEnteredNumber;
      if (var8 != null) {
         var8.setTheme(this.mTheme);
      }

      TextView var9 = this.mLabel;
      if (var9 != null) {
         var9.setTextColor(this.mTextColor);
      }

   }

   private void showLabel() {
      TextView var1 = this.mLabel;
      if (var1 != null) {
         var1.setText(this.mLabelText);
      }

   }

   private void updateKeypad() {
      this.updateLeftRightButtons();
      this.updateNumber();
      this.enableSetButton();
      this.updateDeleteButton();
   }

   private void updateLeftRightButtons() {
      this.mRight.setEnabled(this.canAddDecimal());
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
                  this.mInput[var3] = -1;
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

   public double getDecimal() {
      return this.getEnteredNumber().remainder(BigDecimal.ONE).doubleValue();
   }

   public BigDecimal getEnteredNumber() {
      String var2 = "0";

      StringBuilder var4;
      for(int var1 = this.mInputPointer; var1 >= 0; --var1) {
         int[] var3 = this.mInput;
         if (var3[var1] == -1) {
            break;
         }

         if (var3[var1] == 10) {
            var4 = new StringBuilder();
            var4.append(var2);
            var4.append(".");
            var2 = var4.toString();
         } else {
            var4 = new StringBuilder();
            var4.append(var2);
            var4.append(this.mInput[var1]);
            var2 = var4.toString();
         }
      }

      String var5 = var2;
      if (this.mSign == 1) {
         var4 = new StringBuilder();
         var4.append("-");
         var4.append(var2);
         var5 = var4.toString();
      }

      return new BigDecimal(var5);
   }

   public NumberPickerErrorTextView getErrorView() {
      return this.mError;
   }

   public boolean getIsNegative() {
      return this.mSign == 1;
   }

   protected int getLayoutId() {
      return layout.number_picker_view;
   }

   public BigInteger getNumber() {
      return this.getEnteredNumber().setScale(0, 3).toBigIntegerExact();
   }

   public void onClick(View var1) {
      var1.performHapticFeedback(1);
      this.mError.hideImmediately();
      this.doOnClick(var1);
      this.updateDeleteButton();
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mDivider = this.findViewById(id.divider);
      this.mError = (NumberPickerErrorTextView)this.findViewById(id.error);
      int var1 = 0;

      while(true) {
         int[] var2 = this.mInput;
         if (var1 >= var2.length) {
            View var7 = this.findViewById(id.first);
            View var3 = this.findViewById(id.second);
            View var4 = this.findViewById(id.third);
            View var5 = this.findViewById(id.fourth);
            this.mEnteredNumber = (NumberView)this.findViewById(id.number_text);
            ImageButton var6 = (ImageButton)this.findViewById(id.delete);
            this.mDelete = var6;
            var6.setOnClickListener(this);
            this.mDelete.setOnLongClickListener(this);
            this.mNumbers[1] = (Button)var7.findViewById(id.key_left);
            this.mNumbers[2] = (Button)var7.findViewById(id.key_middle);
            this.mNumbers[3] = (Button)var7.findViewById(id.key_right);
            this.mNumbers[4] = (Button)var3.findViewById(id.key_left);
            this.mNumbers[5] = (Button)var3.findViewById(id.key_middle);
            this.mNumbers[6] = (Button)var3.findViewById(id.key_right);
            this.mNumbers[7] = (Button)var4.findViewById(id.key_left);
            this.mNumbers[8] = (Button)var4.findViewById(id.key_middle);
            this.mNumbers[9] = (Button)var4.findViewById(id.key_right);
            this.mLeft = (Button)var5.findViewById(id.key_left);
            this.mNumbers[0] = (Button)var5.findViewById(id.key_middle);
            this.mRight = (Button)var5.findViewById(id.key_right);
            this.setLeftRightEnabled();

            for(var1 = 0; var1 < 10; ++var1) {
               this.mNumbers[var1].setOnClickListener(this);
               this.mNumbers[var1].setText(String.format("%d", var1));
               this.mNumbers[var1].setTag(id.numbers_key, new Integer(var1));
            }

            this.updateNumber();
            Resources var8 = this.mContext.getResources();
            this.mLeft.setText(var8.getString(string.number_picker_plus_minus));
            this.mRight.setText(var8.getString(string.number_picker_seperator));
            this.mLeft.setOnClickListener(this);
            this.mRight.setOnClickListener(this);
            this.mLabel = (TextView)this.findViewById(id.label);
            this.mSign = 0;
            this.showLabel();
            this.restyleViews();
            this.updateKeypad();
            return;
         }

         var2[var1] = -1;
         ++var1;
      }
   }

   public boolean onLongClick(View var1) {
      var1.performHapticFeedback(0);
      this.mError.hideImmediately();
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
      if (!(var1 instanceof NumberPicker.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NumberPicker.SavedState var3 = (NumberPicker.SavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         this.mInputPointer = var3.mInputPointer;
         int[] var2 = var3.mInput;
         this.mInput = var2;
         if (var2 == null) {
            this.mInput = new int[this.mInputSize];
            this.mInputPointer = -1;
         }

         this.mSign = var3.mSign;
         this.updateKeypad();
      }
   }

   public Parcelable onSaveInstanceState() {
      NumberPicker.SavedState var1 = new NumberPicker.SavedState(super.onSaveInstanceState());
      var1.mInput = this.mInput;
      var1.mSign = this.mSign;
      var1.mInputPointer = this.mInputPointer;
      return var1;
   }

   public void reset() {
      for(int var1 = 0; var1 < this.mInputSize; ++var1) {
         this.mInput[var1] = -1;
      }

      this.mInputPointer = -1;
      this.updateNumber();
   }

   public void setDecimalVisibility(int var1) {
      Button var2 = this.mRight;
      if (var2 != null) {
         var2.setVisibility(var1);
      }

   }

   public void setLabelText(String var1) {
      this.mLabelText = var1;
      this.showLabel();
   }

   protected void setLeftRightEnabled() {
      this.mLeft.setEnabled(true);
      this.mRight.setEnabled(this.canAddDecimal());
      if (!this.canAddDecimal()) {
         this.mRight.setContentDescription((CharSequence)null);
      }

   }

   public void setMax(BigDecimal var1) {
      this.mMaxNumber = var1;
   }

   public void setMin(BigDecimal var1) {
      this.mMinNumber = var1;
   }

   public void setNumber(Integer var1, Double var2, Integer var3) {
      if (var3 != null) {
         this.mSign = var3;
      } else {
         this.mSign = 0;
      }

      if (var2 != null) {
         String var5 = this.doubleToString(var2);
         this.readAndRightDigits(TextUtils.substring(var5, 2, var5.length()));
         int var4 = this.mInputPointer + 1;
         this.mInputPointer = var4;
         this.mInput[var4] = 10;
      }

      if (var1 != null) {
         this.readAndRightDigits(String.valueOf(var1));
      }

      this.updateKeypad();
   }

   public void setPlusMinusVisibility(int var1) {
      Button var2 = this.mLeft;
      if (var2 != null) {
         var2.setVisibility(var1);
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

   protected void updateNumber() {
      String var7 = this.getEnteredNumberString().replaceAll("\\-", "");
      String[] var6 = var7.split("\\.");
      int var1 = var6.length;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var2 = false;
      String var10;
      NumberView var11;
      if (var1 >= 2) {
         if (var6[0].equals("")) {
            var11 = this.mEnteredNumber;
            var10 = var6[1];
            var3 = this.containsDecimal();
            if (this.mSign == 1) {
               var2 = true;
            }

            var11.setNumber("0", var10, var3, var2);
         } else {
            var11 = this.mEnteredNumber;
            String var8 = var6[0];
            var10 = var6[1];
            var4 = this.containsDecimal();
            var2 = var3;
            if (this.mSign == 1) {
               var2 = true;
            }

            var11.setNumber(var8, var10, var4, var2);
         }
      } else if (var6.length == 1) {
         var11 = this.mEnteredNumber;
         var10 = var6[0];
         var3 = this.containsDecimal();
         var2 = var4;
         if (this.mSign == 1) {
            var2 = true;
         }

         var11.setNumber(var10, "", var3, var2);
      } else {
         if (var7.equals(".")) {
            NumberView var9 = this.mEnteredNumber;
            var2 = var5;
            if (this.mSign == 1) {
               var2 = true;
            }

            var9.setNumber("0", "", true, var2);
         }

      }
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public NumberPicker.SavedState createFromParcel(Parcel var1) {
            return new NumberPicker.SavedState(var1);
         }

         public NumberPicker.SavedState[] newArray(int var1) {
            return new NumberPicker.SavedState[var1];
         }
      };
      int[] mInput;
      int mInputPointer;
      int mSign;

      private SavedState(Parcel var1) {
         super(var1);
         this.mInputPointer = var1.readInt();
         int var2 = var1.readInt();
         if (var2 > 0) {
            int[] var3 = new int[var2];
            this.mInput = var3;
            var1.readIntArray(var3);
         }

         this.mSign = var1.readInt();
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
         int[] var3 = this.mInput;
         if (var3 != null) {
            var1.writeInt(var3.length);
            var1.writeIntArray(this.mInput);
         } else {
            var1.writeInt(0);
         }

         var1.writeInt(this.mSign);
      }
   }
}
