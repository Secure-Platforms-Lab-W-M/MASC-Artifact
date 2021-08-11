package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import androidx.core.util.Preconditions;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;
import com.google.android.material.R.string;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class RangeDateSelector implements DateSelector {
   public static final Creator CREATOR = new Creator() {
      public RangeDateSelector createFromParcel(Parcel var1) {
         RangeDateSelector var2 = new RangeDateSelector();
         var2.selectedStartItem = (Long)var1.readValue(Long.class.getClassLoader());
         var2.selectedEndItem = (Long)var1.readValue(Long.class.getClassLoader());
         return var2;
      }

      public RangeDateSelector[] newArray(int var1) {
         return new RangeDateSelector[var1];
      }
   };
   private final String invalidRangeEndError = " ";
   private String invalidRangeStartError;
   private Long proposedTextEnd = null;
   private Long proposedTextStart = null;
   private Long selectedEndItem = null;
   private Long selectedStartItem = null;

   private void clearInvalidRange(TextInputLayout var1, TextInputLayout var2) {
      if (var1.getError() != null && this.invalidRangeStartError.contentEquals(var1.getError())) {
         var1.setError((CharSequence)null);
      }

      if (var2.getError() != null && " ".contentEquals(var2.getError())) {
         var2.setError((CharSequence)null);
      }

   }

   private boolean isValidRange(long var1, long var3) {
      return var1 <= var3;
   }

   private void setInvalidRange(TextInputLayout var1, TextInputLayout var2) {
      var1.setError(this.invalidRangeStartError);
      var2.setError(" ");
   }

   private void updateIfValidTextProposal(TextInputLayout var1, TextInputLayout var2, OnSelectionChangedListener var3) {
      Long var4 = this.proposedTextStart;
      if (var4 != null && this.proposedTextEnd != null) {
         if (this.isValidRange(var4, this.proposedTextEnd)) {
            this.selectedStartItem = this.proposedTextStart;
            this.selectedEndItem = this.proposedTextEnd;
            var3.onSelectionChanged(this.getSelection());
         } else {
            this.setInvalidRange(var1, var2);
         }
      } else {
         this.clearInvalidRange(var1, var2);
      }
   }

   public int describeContents() {
      return 0;
   }

   public int getDefaultThemeResId(Context var1) {
      Resources var3 = var1.getResources();
      DisplayMetrics var4 = var3.getDisplayMetrics();
      int var2 = var3.getDimensionPixelSize(dimen.mtrl_calendar_maximum_default_fullscreen_minor_axis);
      if (Math.min(var4.widthPixels, var4.heightPixels) > var2) {
         var2 = attr.materialCalendarTheme;
      } else {
         var2 = attr.materialCalendarFullscreenTheme;
      }

      return MaterialAttributes.resolveOrThrow(var1, var2, MaterialDatePicker.class.getCanonicalName());
   }

   public int getDefaultTitleResId() {
      return string.mtrl_picker_range_header_title;
   }

   public Collection getSelectedDays() {
      ArrayList var1 = new ArrayList();
      Long var2 = this.selectedStartItem;
      if (var2 != null) {
         var1.add(var2);
      }

      var2 = this.selectedEndItem;
      if (var2 != null) {
         var1.add(var2);
      }

      return var1;
   }

   public Collection getSelectedRanges() {
      if (this.selectedStartItem != null && this.selectedEndItem != null) {
         ArrayList var1 = new ArrayList();
         var1.add(new Pair(this.selectedStartItem, this.selectedEndItem));
         return var1;
      } else {
         return new ArrayList();
      }
   }

   public Pair getSelection() {
      return new Pair(this.selectedStartItem, this.selectedEndItem);
   }

   public String getSelectionDisplayString(Context var1) {
      Resources var4 = var1.getResources();
      if (this.selectedStartItem == null && this.selectedEndItem == null) {
         return var4.getString(string.mtrl_picker_range_header_unselected);
      } else {
         Long var2 = this.selectedEndItem;
         if (var2 == null) {
            return var4.getString(string.mtrl_picker_range_header_only_start_selected, new Object[]{DateStrings.getDateString(this.selectedStartItem)});
         } else {
            Long var3 = this.selectedStartItem;
            if (var3 == null) {
               return var4.getString(string.mtrl_picker_range_header_only_end_selected, new Object[]{DateStrings.getDateString(this.selectedEndItem)});
            } else {
               Pair var5 = DateStrings.getDateRangeString(var3, var2);
               return var4.getString(string.mtrl_picker_range_header_selected, new Object[]{var5.first, var5.second});
            }
         }
      }
   }

   public boolean isSelectionComplete() {
      Long var1 = this.selectedStartItem;
      return var1 != null && this.selectedEndItem != null && this.isValidRange(var1, this.selectedEndItem);
   }

   public View onCreateTextInputView(LayoutInflater var1, ViewGroup var2, Bundle var3, CalendarConstraints var4, final OnSelectionChangedListener var5) {
      View var10 = var1.inflate(layout.mtrl_picker_text_input_date_range, var2, false);
      final TextInputLayout var11 = (TextInputLayout)var10.findViewById(id.mtrl_picker_text_input_range_start);
      final TextInputLayout var12 = (TextInputLayout)var10.findViewById(id.mtrl_picker_text_input_range_end);
      EditText var6 = var11.getEditText();
      EditText var7 = var12.getEditText();
      if (ManufacturerUtils.isSamsungDevice()) {
         var6.setInputType(17);
         var7.setInputType(17);
      }

      this.invalidRangeStartError = var10.getResources().getString(string.mtrl_picker_invalid_range);
      SimpleDateFormat var8 = UtcDates.getTextInputFormat();
      Long var9 = this.selectedStartItem;
      if (var9 != null) {
         var6.setText(var8.format(var9));
         this.proposedTextStart = this.selectedStartItem;
      }

      var9 = this.selectedEndItem;
      if (var9 != null) {
         var7.setText(var8.format(var9));
         this.proposedTextEnd = this.selectedEndItem;
      }

      String var13 = UtcDates.getTextInputHint(var10.getResources(), var8);
      var6.addTextChangedListener(new DateFormatTextWatcher(var13, var8, var11, var4) {
         void onInvalidDate() {
            RangeDateSelector.this.proposedTextStart = null;
            RangeDateSelector.this.updateIfValidTextProposal(var11, var12, var5);
         }

         void onValidDate(Long var1) {
            RangeDateSelector.this.proposedTextStart = var1;
            RangeDateSelector.this.updateIfValidTextProposal(var11, var12, var5);
         }
      });
      var7.addTextChangedListener(new DateFormatTextWatcher(var13, var8, var12, var4) {
         void onInvalidDate() {
            RangeDateSelector.this.proposedTextEnd = null;
            RangeDateSelector.this.updateIfValidTextProposal(var11, var12, var5);
         }

         void onValidDate(Long var1) {
            RangeDateSelector.this.proposedTextEnd = var1;
            RangeDateSelector.this.updateIfValidTextProposal(var11, var12, var5);
         }
      });
      ViewUtils.requestFocusAndShowKeyboard(var6);
      return var10;
   }

   public void select(long var1) {
      Long var3 = this.selectedStartItem;
      if (var3 == null) {
         this.selectedStartItem = var1;
      } else if (this.selectedEndItem == null && this.isValidRange(var3, var1)) {
         this.selectedEndItem = var1;
      } else {
         this.selectedEndItem = null;
         this.selectedStartItem = var1;
      }
   }

   public void setSelection(Pair var1) {
      if (var1.first != null && var1.second != null) {
         Preconditions.checkArgument(this.isValidRange((Long)var1.first, (Long)var1.second));
      }

      Object var2 = var1.first;
      Object var3 = null;
      Long var5;
      if (var2 == null) {
         var5 = null;
      } else {
         var5 = UtcDates.canonicalYearMonthDay((Long)var1.first);
      }

      this.selectedStartItem = var5;
      Long var4;
      if (var1.second == null) {
         var4 = (Long)var3;
      } else {
         var4 = UtcDates.canonicalYearMonthDay((Long)var1.second);
      }

      this.selectedEndItem = var4;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeValue(this.selectedStartItem);
      var1.writeValue(this.selectedEndItem);
   }
}
