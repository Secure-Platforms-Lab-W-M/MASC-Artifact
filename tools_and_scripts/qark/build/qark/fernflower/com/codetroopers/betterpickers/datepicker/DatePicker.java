package com.codetroopers.betterpickers.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.drawable;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatePicker extends LinearLayout implements OnClickListener, OnLongClickListener {
   private static final String KEYBOARD_DATE = "date";
   private static final String KEYBOARD_MONTH = "month";
   private static final String KEYBOARD_YEAR = "year";
   private static int sDateKeyboardPosition = -1;
   private static int sMonthKeyboardPosition = -1;
   private static int sYearKeyboardPosition = -1;
   private int mButtonBackgroundResId;
   private int mCheckDrawableSrcResId;
   protected final Context mContext;
   private char[] mDateFormatOrder;
   protected int[] mDateInput;
   protected int mDateInputPointer;
   protected int mDateInputSize;
   protected Button mDateLeft;
   protected final Button[] mDateNumbers;
   protected ImageButton mDateRight;
   protected ImageButton mDelete;
   private int mDeleteDrawableSrcResId;
   protected View mDivider;
   protected DateView mEnteredDate;
   private int mKeyBackgroundResId;
   protected UnderlinePageIndicatorPicker mKeyboardIndicator;
   private int mKeyboardIndicatorColor;
   protected ViewPager mKeyboardPager;
   protected DatePicker.KeyboardPagerAdapter mKeyboardPagerAdapter;
   protected String[] mMonthAbbreviations;
   protected int mMonthInput;
   protected final Button[] mMonths;
   private Button mSetButton;
   private ColorStateList mTextColor;
   private int mTheme;
   private int mTitleDividerColor;
   protected int[] mYearInput;
   protected int mYearInputPointer;
   protected int mYearInputSize;
   protected Button mYearLeft;
   protected final Button[] mYearNumbers;
   private boolean mYearOptional;
   protected Button mYearRight;

   public DatePicker(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DatePicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mDateInputSize = 2;
      this.mYearInputSize = 4;
      this.mMonthInput = -1;
      this.mDateInput = new int[2];
      this.mYearInput = new int[4];
      this.mDateInputPointer = -1;
      this.mYearInputPointer = -1;
      this.mMonths = new Button[12];
      this.mDateNumbers = new Button[10];
      this.mYearNumbers = new Button[10];
      this.mYearOptional = false;
      this.mTheme = -1;
      this.mContext = var1;
      this.mDateFormatOrder = DateFormat.getDateFormatOrder(var1);
      this.mMonthAbbreviations = makeLocalizedMonthAbbreviations();
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(this.getLayoutId(), this);
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.mKeyBackgroundResId = drawable.key_background_dark;
      this.mButtonBackgroundResId = drawable.button_background_dark;
      this.mTitleDividerColor = this.getResources().getColor(color.default_divider_color_dark);
      this.mKeyboardIndicatorColor = this.getResources().getColor(color.default_keyboard_indicator_color_dark);
      this.mDeleteDrawableSrcResId = drawable.ic_backspace_dark;
      this.mCheckDrawableSrcResId = drawable.ic_check_dark;
   }

   private void addClickedDateNumber(int var1) {
      if (this.mDateInputPointer < this.mDateInputSize - 1) {
         for(int var2 = this.mDateInputPointer; var2 >= 0; --var2) {
            int[] var3 = this.mDateInput;
            var3[var2 + 1] = var3[var2];
         }

         ++this.mDateInputPointer;
         this.mDateInput[0] = var1;
      }

      if ((this.getDayOfMonth() >= 4 || this.getMonthOfYear() == 1 && this.getDayOfMonth() >= 3) && this.mKeyboardPager.getCurrentItem() < 2) {
         ViewPager var4 = this.mKeyboardPager;
         var4.setCurrentItem(var4.getCurrentItem() + 1, true);
      }

   }

   private void addClickedYearNumber(int var1) {
      if (this.mYearInputPointer < this.mYearInputSize - 1) {
         for(int var2 = this.mYearInputPointer; var2 >= 0; --var2) {
            int[] var3 = this.mYearInput;
            var3[var2 + 1] = var3[var2];
         }

         ++this.mYearInputPointer;
         this.mYearInput[0] = var1;
      }

      if (this.getYear() >= 1000 && this.mKeyboardPager.getCurrentItem() < 2) {
         ViewPager var4 = this.mKeyboardPager;
         var4.setCurrentItem(var4.getCurrentItem() + 1, true);
      }

   }

   private boolean canGoToYear() {
      return this.getDayOfMonth() > 0;
   }

   private void enableSetButton() {
      Button var2 = this.mSetButton;
      if (var2 != null) {
         boolean var1;
         if (this.getDayOfMonth() > 0 && (this.mYearOptional || this.getYear() > 0) && this.getMonthOfYear() >= 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         var2.setEnabled(var1);
      }
   }

   public static String[] makeLocalizedMonthAbbreviations() {
      return makeLocalizedMonthAbbreviations(Locale.getDefault());
   }

   public static String[] makeLocalizedMonthAbbreviations(Locale var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      SimpleDateFormat var2;
      if (var1) {
         var2 = new SimpleDateFormat("MMM", var0);
      } else {
         var2 = new SimpleDateFormat("MMM");
      }

      GregorianCalendar var4;
      if (var1) {
         var4 = new GregorianCalendar(var0);
      } else {
         var4 = new GregorianCalendar();
      }

      var4.set(1, 0);
      var4.set(5, 1);
      var4.set(11, 0);
      var4.set(12, 0);
      var4.set(13, 0);
      var4.set(14, 0);
      String[] var3 = new String[12];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4.set(2, var5);
         var3[var5] = var2.format(var4.getTime()).toUpperCase();
      }

      return var3;
   }

   private void onDateRightClicked() {
      if (this.mKeyboardPager.getCurrentItem() < 2) {
         ViewPager var1 = this.mKeyboardPager;
         var1.setCurrentItem(var1.getCurrentItem() + 1, true);
      }

   }

   private void restyleViews() {
      Button[] var4 = this.mMonths;
      int var3 = var4.length;
      byte var2 = 0;

      int var1;
      Button var5;
      for(var1 = 0; var1 < var3; ++var1) {
         var5 = var4[var1];
         if (var5 != null) {
            var5.setTextColor(this.mTextColor);
            var5.setBackgroundResource(this.mKeyBackgroundResId);
         }
      }

      var4 = this.mDateNumbers;
      var3 = var4.length;

      for(var1 = 0; var1 < var3; ++var1) {
         var5 = var4[var1];
         if (var5 != null) {
            var5.setTextColor(this.mTextColor);
            var5.setBackgroundResource(this.mKeyBackgroundResId);
         }
      }

      var4 = this.mYearNumbers;
      var3 = var4.length;

      for(var1 = var2; var1 < var3; ++var1) {
         var5 = var4[var1];
         if (var5 != null) {
            var5.setTextColor(this.mTextColor);
            var5.setBackgroundResource(this.mKeyBackgroundResId);
         }
      }

      UnderlinePageIndicatorPicker var6 = this.mKeyboardIndicator;
      if (var6 != null) {
         var6.setSelectedColor(this.mKeyboardIndicatorColor);
      }

      View var7 = this.mDivider;
      if (var7 != null) {
         var7.setBackgroundColor(this.mTitleDividerColor);
      }

      Button var8 = this.mDateLeft;
      if (var8 != null) {
         var8.setTextColor(this.mTextColor);
         this.mDateLeft.setBackgroundResource(this.mKeyBackgroundResId);
      }

      ImageButton var9 = this.mDateRight;
      if (var9 != null) {
         var9.setBackgroundResource(this.mKeyBackgroundResId);
         this.mDateRight.setImageDrawable(this.getResources().getDrawable(this.mCheckDrawableSrcResId));
      }

      var9 = this.mDelete;
      if (var9 != null) {
         var9.setBackgroundResource(this.mButtonBackgroundResId);
         this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
      }

      var8 = this.mYearLeft;
      if (var8 != null) {
         var8.setTextColor(this.mTextColor);
         this.mYearLeft.setBackgroundResource(this.mKeyBackgroundResId);
      }

      var8 = this.mYearRight;
      if (var8 != null) {
         var8.setTextColor(this.mTextColor);
         this.mYearRight.setBackgroundResource(this.mKeyBackgroundResId);
      }

      DateView var10 = this.mEnteredDate;
      if (var10 != null) {
         var10.setTheme(this.mTheme);
      }

   }

   private void setDateKeyRange(int var1) {
      int var2 = 0;

      while(true) {
         Button[] var4 = this.mDateNumbers;
         if (var2 >= var4.length) {
            return;
         }

         if (var4[var2] != null) {
            Button var5 = var4[var2];
            boolean var3;
            if (var2 <= var1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var5.setEnabled(var3);
         }

         ++var2;
      }
   }

   private void setDateMinKeyRange(int var1) {
      int var2 = 0;

      while(true) {
         Button[] var4 = this.mDateNumbers;
         if (var2 >= var4.length) {
            return;
         }

         if (var4[var2] != null) {
            Button var5 = var4[var2];
            boolean var3;
            if (var2 >= var1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var5.setEnabled(var3);
         }

         ++var2;
      }
   }

   private void setYearKeyRange(int var1) {
      int var2 = 0;

      while(true) {
         Button[] var4 = this.mYearNumbers;
         if (var2 >= var4.length) {
            return;
         }

         if (var4[var2] != null) {
            Button var5 = var4[var2];
            boolean var3;
            if (var2 <= var1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var5.setEnabled(var3);
         }

         ++var2;
      }
   }

   private void setYearMinKeyRange(int var1) {
      int var2 = 0;

      while(true) {
         Button[] var4 = this.mYearNumbers;
         if (var2 >= var4.length) {
            return;
         }

         if (var4[var2] != null) {
            Button var5 = var4[var2];
            boolean var3;
            if (var2 >= var1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var5.setEnabled(var3);
         }

         ++var2;
      }
   }

   private void updateDateKeys() {
      int var1 = this.getDayOfMonth();
      if (var1 >= 4) {
         this.setDateKeyRange(-1);
      } else if (var1 >= 3) {
         var1 = this.mMonthInput;
         if (var1 == 1) {
            this.setDateKeyRange(-1);
         } else if (var1 != 3 && var1 != 5 && var1 != 8 && var1 != 10) {
            this.setDateKeyRange(1);
         } else {
            this.setDateKeyRange(0);
         }
      } else if (var1 >= 2) {
         this.setDateKeyRange(9);
      } else if (var1 >= 1) {
         this.setDateKeyRange(9);
      } else {
         this.setDateMinKeyRange(1);
      }
   }

   private void updateKeypad() {
      this.updateLeftRightButtons();
      this.updateDate();
      this.enableSetButton();
      this.updateDeleteButton();
      this.updateMonthKeys();
      this.updateDateKeys();
      this.updateYearKeys();
   }

   private void updateLeftRightButtons() {
      ImageButton var1 = this.mDateRight;
      if (var1 != null) {
         var1.setEnabled(this.canGoToYear());
      }

   }

   private void updateMonthKeys() {
      int var2 = this.getDayOfMonth();
      int var1 = 0;

      while(true) {
         Button[] var3 = this.mMonths;
         if (var1 >= var3.length) {
            if (var2 > 29 && var3[1] != null) {
               var3[1].setEnabled(false);
            }

            if (var2 > 30) {
               var3 = this.mMonths;
               if (var3[3] != null) {
                  var3[3].setEnabled(false);
               }

               var3 = this.mMonths;
               if (var3[5] != null) {
                  var3[5].setEnabled(false);
               }

               var3 = this.mMonths;
               if (var3[8] != null) {
                  var3[8].setEnabled(false);
               }

               var3 = this.mMonths;
               if (var3[10] != null) {
                  var3[10].setEnabled(false);
               }
            }

            return;
         }

         if (var3[var1] != null) {
            var3[var1].setEnabled(true);
         }

         ++var1;
      }
   }

   private void updateYearKeys() {
      int var1 = this.getYear();
      if (var1 >= 1000) {
         this.setYearKeyRange(-1);
      } else if (var1 >= 1) {
         this.setYearKeyRange(9);
      } else {
         this.setYearMinKeyRange(1);
      }
   }

   protected void doOnClick(View var1) {
      ViewPager var5;
      if (var1 == this.mDelete) {
         char var2 = this.mDateFormatOrder[this.mKeyboardPager.getCurrentItem()];
         if (var2 != 'M') {
            int var3;
            int[] var4;
            int var6;
            if (var2 != 'd') {
               if (var2 == 'y') {
                  if (this.mYearInputPointer >= 0) {
                     var6 = 0;

                     while(true) {
                        var3 = this.mYearInputPointer;
                        if (var6 >= var3) {
                           this.mYearInput[var3] = 0;
                           this.mYearInputPointer = var3 - 1;
                           break;
                        }

                        var4 = this.mYearInput;
                        var4[var6] = var4[var6 + 1];
                        ++var6;
                     }
                  } else if (this.mKeyboardPager.getCurrentItem() > 0) {
                     var5 = this.mKeyboardPager;
                     var5.setCurrentItem(var5.getCurrentItem() - 1, true);
                  }
               }
            } else if (this.mDateInputPointer >= 0) {
               var6 = 0;

               while(true) {
                  var3 = this.mDateInputPointer;
                  if (var6 >= var3) {
                     this.mDateInput[var3] = 0;
                     this.mDateInputPointer = var3 - 1;
                     break;
                  }

                  var4 = this.mDateInput;
                  var4[var6] = var4[var6 + 1];
                  ++var6;
               }
            } else if (this.mKeyboardPager.getCurrentItem() > 0) {
               var5 = this.mKeyboardPager;
               var5.setCurrentItem(var5.getCurrentItem() - 1, true);
            }
         } else if (this.mMonthInput != -1) {
            this.mMonthInput = -1;
         }
      } else if (var1 == this.mDateRight) {
         this.onDateRightClicked();
      } else if (var1 == this.mEnteredDate.getDate()) {
         this.mKeyboardPager.setCurrentItem(sDateKeyboardPosition);
      } else if (var1 == this.mEnteredDate.getMonth()) {
         this.mKeyboardPager.setCurrentItem(sMonthKeyboardPosition);
      } else if (var1 == this.mEnteredDate.getYear()) {
         this.mKeyboardPager.setCurrentItem(sYearKeyboardPosition);
      } else if (var1.getTag(id.date_keyboard).equals("month")) {
         this.mMonthInput = (Integer)var1.getTag(id.date_month_int);
         if (this.mKeyboardPager.getCurrentItem() < 2) {
            var5 = this.mKeyboardPager;
            var5.setCurrentItem(var5.getCurrentItem() + 1, true);
         }
      } else if (var1.getTag(id.date_keyboard).equals("date")) {
         this.addClickedDateNumber((Integer)var1.getTag(id.numbers_key));
      } else if (var1.getTag(id.date_keyboard).equals("year")) {
         this.addClickedYearNumber((Integer)var1.getTag(id.numbers_key));
      }

      this.updateKeypad();
   }

   public int getDayOfMonth() {
      int[] var1 = this.mDateInput;
      return var1[1] * 10 + var1[0];
   }

   protected int getLayoutId() {
      return layout.date_picker_view;
   }

   public int getMonthOfYear() {
      return this.mMonthInput;
   }

   public int getYear() {
      int[] var1 = this.mYearInput;
      return var1[3] * 1000 + var1[2] * 100 + var1[1] * 10 + var1[0];
   }

   public void onClick(View var1) {
      var1.performHapticFeedback(1);
      this.doOnClick(var1);
      this.updateDeleteButton();
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mDivider = this.findViewById(id.divider);
      int var1 = 0;

      while(true) {
         int[] var2 = this.mDateInput;
         if (var1 >= var2.length) {
            var1 = 0;

            while(true) {
               var2 = this.mYearInput;
               if (var1 >= var2.length) {
                  this.mKeyboardIndicator = (UnderlinePageIndicatorPicker)this.findViewById(id.keyboard_indicator);
                  ViewPager var3 = (ViewPager)this.findViewById(id.keyboard_pager);
                  this.mKeyboardPager = var3;
                  var3.setOffscreenPageLimit(2);
                  DatePicker.KeyboardPagerAdapter var4 = new DatePicker.KeyboardPagerAdapter((LayoutInflater)this.mContext.getSystemService("layout_inflater"));
                  this.mKeyboardPagerAdapter = var4;
                  this.mKeyboardPager.setAdapter(var4);
                  this.mKeyboardIndicator.setViewPager(this.mKeyboardPager);
                  this.mKeyboardPager.setCurrentItem(0);
                  DateView var5 = (DateView)this.findViewById(id.date_text);
                  this.mEnteredDate = var5;
                  var5.setTheme(this.mTheme);
                  this.mEnteredDate.setUnderlinePage(this.mKeyboardIndicator);
                  this.mEnteredDate.setOnClick(this);
                  ImageButton var6 = (ImageButton)this.findViewById(id.delete);
                  this.mDelete = var6;
                  var6.setOnClickListener(this);
                  this.mDelete.setOnLongClickListener(this);
                  this.setLeftRightEnabled();
                  this.updateDate();
                  this.updateKeypad();
                  return;
               }

               var2[var1] = 0;
               ++var1;
            }
         }

         var2[var1] = 0;
         ++var1;
      }
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
      if (!(var1 instanceof DatePicker.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         DatePicker.SavedState var2 = (DatePicker.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mDateInputPointer = var2.mDateInputPointer;
         this.mYearInputPointer = var2.mYearInputPointer;
         this.mDateInput = var2.mDateInput;
         this.mYearInput = var2.mYearInput;
         if (this.mDateInput == null) {
            this.mDateInput = new int[this.mDateInputSize];
            this.mDateInputPointer = -1;
         }

         if (this.mYearInput == null) {
            this.mYearInput = new int[this.mYearInputSize];
            this.mYearInputPointer = -1;
         }

         this.mMonthInput = var2.mMonthInput;
         this.updateKeypad();
      }
   }

   public Parcelable onSaveInstanceState() {
      DatePicker.SavedState var1 = new DatePicker.SavedState(super.onSaveInstanceState());
      var1.mMonthInput = this.mMonthInput;
      var1.mDateInput = this.mDateInput;
      var1.mDateInputPointer = this.mDateInputPointer;
      var1.mYearInput = this.mYearInput;
      var1.mYearInputPointer = this.mYearInputPointer;
      return var1;
   }

   public void reset() {
      int var1;
      for(var1 = 0; var1 < this.mDateInputSize; ++var1) {
         this.mDateInput[var1] = 0;
      }

      for(var1 = 0; var1 < this.mYearInputSize; ++var1) {
         this.mYearInput[var1] = 0;
      }

      this.mDateInputPointer = -1;
      this.mYearInputPointer = -1;
      this.mMonthInput = -1;
      this.mKeyboardPager.setCurrentItem(0, true);
      this.updateDate();
   }

   public void setDate(int var1, int var2, int var3) {
      this.mMonthInput = var2;
      int[] var6 = this.mYearInput;
      var6[3] = var1 / 1000;
      var6[2] = var1 % 1000 / 100;
      var6[1] = var1 % 100 / 10;
      var6[0] = var1 % 10;
      if (var1 >= 1000) {
         this.mYearInputPointer = 3;
      } else if (var1 >= 100) {
         this.mYearInputPointer = 2;
      } else if (var1 >= 10) {
         this.mYearInputPointer = 1;
      } else if (var1 > 0) {
         this.mYearInputPointer = 0;
      }

      var6 = this.mDateInput;
      var6[1] = var3 / 10;
      var6[0] = var3 % 10;
      if (var3 >= 10) {
         this.mDateInputPointer = 1;
      } else if (var3 > 0) {
         this.mDateInputPointer = 0;
      }

      int var4 = 0;

      while(true) {
         char[] var7 = this.mDateFormatOrder;
         if (var4 >= var7.length) {
            break;
         }

         char var5 = var7[var4];
         if (var5 == 'M' && var2 == -1) {
            this.mKeyboardPager.setCurrentItem(var4, true);
            break;
         }

         if (var5 == 'd' && var3 <= 0) {
            this.mKeyboardPager.setCurrentItem(var4, true);
            break;
         }

         if (var5 == 'y' && var1 <= 0) {
            this.mKeyboardPager.setCurrentItem(var4, true);
            break;
         }

         ++var4;
      }

      this.updateKeypad();
   }

   protected void setLeftRightEnabled() {
      Button var1 = this.mDateLeft;
      if (var1 != null) {
         var1.setEnabled(false);
      }

      ImageButton var2 = this.mDateRight;
      if (var2 != null) {
         var2.setEnabled(this.canGoToYear());
      }

      var1 = this.mYearLeft;
      if (var1 != null) {
         var1.setEnabled(false);
      }

      var1 = this.mYearRight;
      if (var1 != null) {
         var1.setEnabled(false);
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
         this.mCheckDrawableSrcResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpCheckIcon, this.mCheckDrawableSrcResId);
         this.mTitleDividerColor = var2.getColor(styleable.BetterPickersDialogFragment_bpTitleDividerColor, this.mTitleDividerColor);
         this.mKeyboardIndicatorColor = var2.getColor(styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor, this.mKeyboardIndicatorColor);
         this.mDeleteDrawableSrcResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpDeleteIcon, this.mDeleteDrawableSrcResId);
      }

      this.restyleViews();
   }

   public void setYearOptional(boolean var1) {
      this.mYearOptional = var1;
   }

   protected void updateDate() {
      int var1 = this.mMonthInput;
      String var2;
      if (var1 < 0) {
         var2 = "";
      } else {
         var2 = this.mMonthAbbreviations[var1];
      }

      this.mEnteredDate.setDate(var2, this.getDayOfMonth(), this.getYear());
   }

   public void updateDeleteButton() {
      boolean var1;
      if (this.mMonthInput == -1 && this.mDateInputPointer == -1 && this.mYearInputPointer == -1) {
         var1 = false;
      } else {
         var1 = true;
      }

      ImageButton var2 = this.mDelete;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

   }

   private class KeyboardPagerAdapter extends PagerAdapter {
      private LayoutInflater mInflater;

      public KeyboardPagerAdapter(LayoutInflater var2) {
         this.mInflater = var2;
      }

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)var3);
      }

      public int getCount() {
         return 3;
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         Resources var4 = DatePicker.this.mContext.getResources();
         View var3;
         View var5;
         View var6;
         View var7;
         View var9;
         if (DatePicker.this.mDateFormatOrder[var2] == 'M') {
            DatePicker.sMonthKeyboardPosition = var2;
            var3 = this.mInflater.inflate(layout.keyboard_text_with_header, var1, false);
            var9 = var3.findViewById(id.first);
            var5 = var3.findViewById(id.second);
            var6 = var3.findViewById(id.third);
            var7 = var3.findViewById(id.fourth);
            ((TextView)var3.findViewById(id.header)).setText(string.month_c);
            DatePicker.this.mMonths[0] = (Button)var9.findViewById(id.key_left);
            DatePicker.this.mMonths[1] = (Button)var9.findViewById(id.key_middle);
            DatePicker.this.mMonths[2] = (Button)var9.findViewById(id.key_right);
            DatePicker.this.mMonths[3] = (Button)var5.findViewById(id.key_left);
            DatePicker.this.mMonths[4] = (Button)var5.findViewById(id.key_middle);
            DatePicker.this.mMonths[5] = (Button)var5.findViewById(id.key_right);
            DatePicker.this.mMonths[6] = (Button)var6.findViewById(id.key_left);
            DatePicker.this.mMonths[7] = (Button)var6.findViewById(id.key_middle);
            DatePicker.this.mMonths[8] = (Button)var6.findViewById(id.key_right);
            DatePicker.this.mMonths[9] = (Button)var7.findViewById(id.key_left);
            DatePicker.this.mMonths[10] = (Button)var7.findViewById(id.key_middle);
            DatePicker.this.mMonths[11] = (Button)var7.findViewById(id.key_right);

            for(var2 = 0; var2 < 12; ++var2) {
               DatePicker.this.mMonths[var2].setOnClickListener(DatePicker.this);
               DatePicker.this.mMonths[var2].setText(DatePicker.this.mMonthAbbreviations[var2]);
               DatePicker.this.mMonths[var2].setTextColor(DatePicker.this.mTextColor);
               DatePicker.this.mMonths[var2].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
               DatePicker.this.mMonths[var2].setTag(id.date_keyboard, "month");
               DatePicker.this.mMonths[var2].setTag(id.date_month_int, var2);
            }
         } else if (DatePicker.this.mDateFormatOrder[var2] == 'd') {
            DatePicker.sDateKeyboardPosition = var2;
            var3 = this.mInflater.inflate(layout.keyboard_right_drawable_with_header, var1, false);
            var5 = var3.findViewById(id.first);
            var6 = var3.findViewById(id.second);
            var7 = var3.findViewById(id.third);
            View var8 = var3.findViewById(id.fourth);
            ((TextView)var3.findViewById(id.header)).setText(string.day_c);
            DatePicker.this.mDateNumbers[1] = (Button)var5.findViewById(id.key_left);
            DatePicker.this.mDateNumbers[2] = (Button)var5.findViewById(id.key_middle);
            DatePicker.this.mDateNumbers[3] = (Button)var5.findViewById(id.key_right);
            DatePicker.this.mDateNumbers[4] = (Button)var6.findViewById(id.key_left);
            DatePicker.this.mDateNumbers[5] = (Button)var6.findViewById(id.key_middle);
            DatePicker.this.mDateNumbers[6] = (Button)var6.findViewById(id.key_right);
            DatePicker.this.mDateNumbers[7] = (Button)var7.findViewById(id.key_left);
            DatePicker.this.mDateNumbers[8] = (Button)var7.findViewById(id.key_middle);
            DatePicker.this.mDateNumbers[9] = (Button)var7.findViewById(id.key_right);
            DatePicker.this.mDateLeft = (Button)var8.findViewById(id.key_left);
            DatePicker.this.mDateLeft.setTextColor(DatePicker.this.mTextColor);
            DatePicker.this.mDateLeft.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
            DatePicker.this.mDateNumbers[0] = (Button)var8.findViewById(id.key_middle);
            DatePicker.this.mDateRight = (ImageButton)var8.findViewById(id.key_right);

            for(var2 = 0; var2 < 10; ++var2) {
               DatePicker.this.mDateNumbers[var2].setOnClickListener(DatePicker.this);
               DatePicker.this.mDateNumbers[var2].setText(String.format(Locale.getDefault(), "%d", var2));
               DatePicker.this.mDateNumbers[var2].setTextColor(DatePicker.this.mTextColor);
               DatePicker.this.mDateNumbers[var2].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
               DatePicker.this.mDateNumbers[var2].setTag(id.date_keyboard, "date");
               DatePicker.this.mDateNumbers[var2].setTag(id.numbers_key, var2);
            }

            DatePicker.this.mDateRight.setImageDrawable(var4.getDrawable(DatePicker.this.mCheckDrawableSrcResId));
            DatePicker.this.mDateRight.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
            DatePicker.this.mDateRight.setOnClickListener(DatePicker.this);
         } else if (DatePicker.this.mDateFormatOrder[var2] == 'y') {
            DatePicker.sYearKeyboardPosition = var2;
            var3 = this.mInflater.inflate(layout.keyboard_with_header, var1, false);
            var9 = var3.findViewById(id.first);
            var5 = var3.findViewById(id.second);
            var6 = var3.findViewById(id.third);
            var7 = var3.findViewById(id.fourth);
            ((TextView)var3.findViewById(id.header)).setText(string.year_c);
            DatePicker.this.mYearNumbers[1] = (Button)var9.findViewById(id.key_left);
            DatePicker.this.mYearNumbers[2] = (Button)var9.findViewById(id.key_middle);
            DatePicker.this.mYearNumbers[3] = (Button)var9.findViewById(id.key_right);
            DatePicker.this.mYearNumbers[4] = (Button)var5.findViewById(id.key_left);
            DatePicker.this.mYearNumbers[5] = (Button)var5.findViewById(id.key_middle);
            DatePicker.this.mYearNumbers[6] = (Button)var5.findViewById(id.key_right);
            DatePicker.this.mYearNumbers[7] = (Button)var6.findViewById(id.key_left);
            DatePicker.this.mYearNumbers[8] = (Button)var6.findViewById(id.key_middle);
            DatePicker.this.mYearNumbers[9] = (Button)var6.findViewById(id.key_right);
            DatePicker.this.mYearLeft = (Button)var7.findViewById(id.key_left);
            DatePicker.this.mYearLeft.setTextColor(DatePicker.this.mTextColor);
            DatePicker.this.mYearLeft.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
            DatePicker.this.mYearNumbers[0] = (Button)var7.findViewById(id.key_middle);
            DatePicker.this.mYearRight = (Button)var7.findViewById(id.key_right);
            DatePicker.this.mYearRight.setTextColor(DatePicker.this.mTextColor);
            DatePicker.this.mYearRight.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);

            for(var2 = 0; var2 < 10; ++var2) {
               DatePicker.this.mYearNumbers[var2].setOnClickListener(DatePicker.this);
               DatePicker.this.mYearNumbers[var2].setText(String.format(Locale.getDefault(), "%d", var2));
               DatePicker.this.mYearNumbers[var2].setTextColor(DatePicker.this.mTextColor);
               DatePicker.this.mYearNumbers[var2].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
               DatePicker.this.mYearNumbers[var2].setTag(id.date_keyboard, "year");
               DatePicker.this.mYearNumbers[var2].setTag(id.numbers_key, var2);
            }
         } else {
            var3 = new View(DatePicker.this.mContext);
         }

         DatePicker.this.setLeftRightEnabled();
         DatePicker.this.updateDate();
         DatePicker.this.updateKeypad();
         var1.addView(var3, 0);
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         return var1 == var2;
      }
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public DatePicker.SavedState createFromParcel(Parcel var1) {
            return new DatePicker.SavedState(var1);
         }

         public DatePicker.SavedState[] newArray(int var1) {
            return new DatePicker.SavedState[var1];
         }
      };
      int[] mDateInput;
      int mDateInputPointer;
      int mMonthInput;
      int[] mYearInput;
      int mYearInputPointer;

      private SavedState(Parcel var1) {
         super(var1);
         this.mDateInputPointer = var1.readInt();
         this.mYearInputPointer = var1.readInt();
         var1.readIntArray(this.mDateInput);
         var1.readIntArray(this.mYearInput);
         this.mMonthInput = var1.readInt();
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
         var1.writeInt(this.mDateInputPointer);
         var1.writeInt(this.mYearInputPointer);
         var1.writeIntArray(this.mDateInput);
         var1.writeIntArray(this.mYearInput);
         var1.writeInt(this.mMonthInput);
      }
   }
}
