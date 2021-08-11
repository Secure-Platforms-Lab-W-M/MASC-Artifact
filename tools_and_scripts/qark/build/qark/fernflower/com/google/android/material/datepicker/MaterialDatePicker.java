package com.google.android.material.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.drawable;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;
import com.google.android.material.R.string;
import com.google.android.material.R.style;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.util.Iterator;
import java.util.LinkedHashSet;

public final class MaterialDatePicker extends DialogFragment {
   private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
   static final Object CANCEL_BUTTON_TAG = "CANCEL_BUTTON_TAG";
   static final Object CONFIRM_BUTTON_TAG = "CONFIRM_BUTTON_TAG";
   private static final String DATE_SELECTOR_KEY = "DATE_SELECTOR_KEY";
   private static final String OVERRIDE_THEME_RES_ID = "OVERRIDE_THEME_RES_ID";
   private static final String TITLE_TEXT_KEY = "TITLE_TEXT_KEY";
   private static final String TITLE_TEXT_RES_ID_KEY = "TITLE_TEXT_RES_ID_KEY";
   static final Object TOGGLE_BUTTON_TAG = "TOGGLE_BUTTON_TAG";
   private MaterialShapeDrawable background;
   private MaterialCalendar calendar;
   private CalendarConstraints calendarConstraints;
   private Button confirmButton;
   private DateSelector dateSelector;
   private boolean fullscreen;
   private TextView headerSelectionText;
   private CheckableImageButton headerToggleButton;
   private final LinkedHashSet onCancelListeners = new LinkedHashSet();
   private final LinkedHashSet onDismissListeners = new LinkedHashSet();
   private final LinkedHashSet onNegativeButtonClickListeners = new LinkedHashSet();
   private final LinkedHashSet onPositiveButtonClickListeners = new LinkedHashSet();
   private int overrideThemeResId;
   private PickerFragment pickerFragment;
   private CharSequence titleText;
   private int titleTextResId;

   private static Drawable createHeaderToggleDrawable(Context var0) {
      StateListDrawable var1 = new StateListDrawable();
      Drawable var2 = AppCompatResources.getDrawable(var0, drawable.ic_calendar_black_24dp);
      var1.addState(new int[]{16842912}, var2);
      Drawable var3 = AppCompatResources.getDrawable(var0, drawable.ic_edit_black_24dp);
      var1.addState(new int[0], var3);
      return var1;
   }

   private static int getDialogPickerHeight(Context var0) {
      Resources var1 = var0.getResources();
      return var1.getDimensionPixelSize(dimen.mtrl_calendar_navigation_height) + var1.getDimensionPixelOffset(dimen.mtrl_calendar_navigation_top_padding) + var1.getDimensionPixelOffset(dimen.mtrl_calendar_navigation_bottom_padding) + var1.getDimensionPixelSize(dimen.mtrl_calendar_days_of_week_height) + MonthAdapter.MAXIMUM_WEEKS * var1.getDimensionPixelSize(dimen.mtrl_calendar_day_height) + (MonthAdapter.MAXIMUM_WEEKS - 1) * var1.getDimensionPixelOffset(dimen.mtrl_calendar_month_vertical_padding) + var1.getDimensionPixelOffset(dimen.mtrl_calendar_bottom_padding);
   }

   private static int getPaddedPickerWidth(Context var0) {
      Resources var3 = var0.getResources();
      int var1 = var3.getDimensionPixelOffset(dimen.mtrl_calendar_content_padding);
      int var2 = Month.today().daysInWeek;
      return var1 * 2 + var2 * var3.getDimensionPixelSize(dimen.mtrl_calendar_day_width) + (var2 - 1) * var3.getDimensionPixelOffset(dimen.mtrl_calendar_month_horizontal_padding);
   }

   private int getThemeResId(Context var1) {
      int var2 = this.overrideThemeResId;
      return var2 != 0 ? var2 : this.dateSelector.getDefaultThemeResId(var1);
   }

   private void initHeaderToggle(Context var1) {
      this.headerToggleButton.setTag(TOGGLE_BUTTON_TAG);
      this.headerToggleButton.setImageDrawable(createHeaderToggleDrawable(var1));
      ViewCompat.setAccessibilityDelegate(this.headerToggleButton, (AccessibilityDelegateCompat)null);
      this.updateToggleContentDescription(this.headerToggleButton);
      this.headerToggleButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            MaterialDatePicker.this.headerToggleButton.toggle();
            MaterialDatePicker var2 = MaterialDatePicker.this;
            var2.updateToggleContentDescription(var2.headerToggleButton);
            MaterialDatePicker.this.startPickerFragment();
         }
      });
   }

   static boolean isFullscreen(Context var0) {
      TypedArray var2 = var0.obtainStyledAttributes(MaterialAttributes.resolveOrThrow(var0, attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName()), new int[]{16843277});
      boolean var1 = var2.getBoolean(0, false);
      var2.recycle();
      return var1;
   }

   static MaterialDatePicker newInstance(MaterialDatePicker.Builder var0) {
      MaterialDatePicker var1 = new MaterialDatePicker();
      Bundle var2 = new Bundle();
      var2.putInt("OVERRIDE_THEME_RES_ID", var0.overrideThemeResId);
      var2.putParcelable("DATE_SELECTOR_KEY", var0.dateSelector);
      var2.putParcelable("CALENDAR_CONSTRAINTS_KEY", var0.calendarConstraints);
      var2.putInt("TITLE_TEXT_RES_ID_KEY", var0.titleTextResId);
      var2.putCharSequence("TITLE_TEXT_KEY", var0.titleText);
      var1.setArguments(var2);
      return var1;
   }

   private void startPickerFragment() {
      this.calendar = MaterialCalendar.newInstance(this.dateSelector, this.getThemeResId(this.requireContext()), this.calendarConstraints);
      Object var1;
      if (this.headerToggleButton.isChecked()) {
         var1 = MaterialTextInputPicker.newInstance(this.dateSelector, this.calendarConstraints);
      } else {
         var1 = this.calendar;
      }

      this.pickerFragment = (PickerFragment)var1;
      this.updateHeader();
      FragmentTransaction var2 = this.getChildFragmentManager().beginTransaction();
      var2.replace(id.mtrl_calendar_frame, this.pickerFragment);
      var2.commitNow();
      this.pickerFragment.addOnSelectionChangedListener(new OnSelectionChangedListener() {
         public void onSelectionChanged(Object var1) {
            MaterialDatePicker.this.updateHeader();
            if (MaterialDatePicker.this.dateSelector.isSelectionComplete()) {
               MaterialDatePicker.this.confirmButton.setEnabled(true);
            } else {
               MaterialDatePicker.this.confirmButton.setEnabled(false);
            }
         }
      });
   }

   public static long thisMonthInUtcMilliseconds() {
      return Month.today().timeInMillis;
   }

   public static long todayInUtcMilliseconds() {
      return UtcDates.getTodayCalendar().getTimeInMillis();
   }

   private void updateHeader() {
      String var1 = this.getHeaderText();
      this.headerSelectionText.setContentDescription(String.format(this.getString(string.mtrl_picker_announce_current_selection), var1));
      this.headerSelectionText.setText(var1);
   }

   private void updateToggleContentDescription(CheckableImageButton var1) {
      String var2;
      if (this.headerToggleButton.isChecked()) {
         var2 = var1.getContext().getString(string.mtrl_picker_toggle_to_calendar_input_mode);
      } else {
         var2 = var1.getContext().getString(string.mtrl_picker_toggle_to_text_input_mode);
      }

      this.headerToggleButton.setContentDescription(var2);
   }

   public boolean addOnCancelListener(OnCancelListener var1) {
      return this.onCancelListeners.add(var1);
   }

   public boolean addOnDismissListener(OnDismissListener var1) {
      return this.onDismissListeners.add(var1);
   }

   public boolean addOnNegativeButtonClickListener(OnClickListener var1) {
      return this.onNegativeButtonClickListeners.add(var1);
   }

   public boolean addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener var1) {
      return this.onPositiveButtonClickListeners.add(var1);
   }

   public void clearOnCancelListeners() {
      this.onCancelListeners.clear();
   }

   public void clearOnDismissListeners() {
      this.onDismissListeners.clear();
   }

   public void clearOnNegativeButtonClickListeners() {
      this.onNegativeButtonClickListeners.clear();
   }

   public void clearOnPositiveButtonClickListeners() {
      this.onPositiveButtonClickListeners.clear();
   }

   public String getHeaderText() {
      return this.dateSelector.getSelectionDisplayString(this.getContext());
   }

   public final Object getSelection() {
      return this.dateSelector.getSelection();
   }

   public final void onCancel(DialogInterface var1) {
      Iterator var2 = this.onCancelListeners.iterator();

      while(var2.hasNext()) {
         ((OnCancelListener)var2.next()).onCancel(var1);
      }

      super.onCancel(var1);
   }

   public final void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (var1 == null) {
         var1 = this.getArguments();
      }

      this.overrideThemeResId = var1.getInt("OVERRIDE_THEME_RES_ID");
      this.dateSelector = (DateSelector)var1.getParcelable("DATE_SELECTOR_KEY");
      this.calendarConstraints = (CalendarConstraints)var1.getParcelable("CALENDAR_CONSTRAINTS_KEY");
      this.titleTextResId = var1.getInt("TITLE_TEXT_RES_ID_KEY");
      this.titleText = var1.getCharSequence("TITLE_TEXT_KEY");
   }

   public final Dialog onCreateDialog(Bundle var1) {
      Dialog var5 = new Dialog(this.requireContext(), this.getThemeResId(this.requireContext()));
      Context var3 = var5.getContext();
      this.fullscreen = isFullscreen(var3);
      int var2 = MaterialAttributes.resolveOrThrow(var3, attr.colorSurface, MaterialDatePicker.class.getCanonicalName());
      MaterialShapeDrawable var4 = new MaterialShapeDrawable(var3, (AttributeSet)null, attr.materialCalendarStyle, style.Widget_MaterialComponents_MaterialCalendar);
      this.background = var4;
      var4.initializeElevationOverlay(var3);
      this.background.setFillColor(ColorStateList.valueOf(var2));
      this.background.setElevation(ViewCompat.getElevation(var5.getWindow().getDecorView()));
      return var5;
   }

   public final View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      int var4;
      if (this.fullscreen) {
         var4 = layout.mtrl_picker_fullscreen;
      } else {
         var4 = layout.mtrl_picker_dialog;
      }

      View var6 = var1.inflate(var4, var2);
      Context var7 = var6.getContext();
      if (this.fullscreen) {
         var6.findViewById(id.mtrl_calendar_frame).setLayoutParams(new LayoutParams(getPaddedPickerWidth(var7), -2));
      } else {
         View var9 = var6.findViewById(id.mtrl_calendar_main_pane);
         View var5 = var6.findViewById(id.mtrl_calendar_frame);
         var9.setLayoutParams(new LayoutParams(getPaddedPickerWidth(var7), -1));
         var5.setMinimumHeight(getDialogPickerHeight(this.requireContext()));
      }

      TextView var10 = (TextView)var6.findViewById(id.mtrl_picker_header_selection_text);
      this.headerSelectionText = var10;
      ViewCompat.setAccessibilityLiveRegion(var10, 1);
      this.headerToggleButton = (CheckableImageButton)var6.findViewById(id.mtrl_picker_header_toggle);
      var10 = (TextView)var6.findViewById(id.mtrl_picker_title_text);
      CharSequence var11 = this.titleText;
      if (var11 != null) {
         var10.setText(var11);
      } else {
         var10.setText(this.titleTextResId);
      }

      this.initHeaderToggle(var7);
      this.confirmButton = (Button)var6.findViewById(id.confirm_button);
      if (this.dateSelector.isSelectionComplete()) {
         this.confirmButton.setEnabled(true);
      } else {
         this.confirmButton.setEnabled(false);
      }

      this.confirmButton.setTag(CONFIRM_BUTTON_TAG);
      this.confirmButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var2 = MaterialDatePicker.this.onPositiveButtonClickListeners.iterator();

            while(var2.hasNext()) {
               ((MaterialPickerOnPositiveButtonClickListener)var2.next()).onPositiveButtonClick(MaterialDatePicker.this.getSelection());
            }

            MaterialDatePicker.this.dismiss();
         }
      });
      Button var8 = (Button)var6.findViewById(id.cancel_button);
      var8.setTag(CANCEL_BUTTON_TAG);
      var8.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var2 = MaterialDatePicker.this.onNegativeButtonClickListeners.iterator();

            while(var2.hasNext()) {
               ((OnClickListener)var2.next()).onClick(var1);
            }

            MaterialDatePicker.this.dismiss();
         }
      });
      return var6;
   }

   public final void onDismiss(DialogInterface var1) {
      Iterator var2 = this.onDismissListeners.iterator();

      while(var2.hasNext()) {
         ((OnDismissListener)var2.next()).onDismiss(var1);
      }

      ViewGroup var3 = (ViewGroup)this.getView();
      if (var3 != null) {
         var3.removeAllViews();
      }

      super.onDismiss(var1);
   }

   public final void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putInt("OVERRIDE_THEME_RES_ID", this.overrideThemeResId);
      var1.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
      CalendarConstraints.Builder var2 = new CalendarConstraints.Builder(this.calendarConstraints);
      if (this.calendar.getCurrentMonth() != null) {
         var2.setOpenAt(this.calendar.getCurrentMonth().timeInMillis);
      }

      var1.putParcelable("CALENDAR_CONSTRAINTS_KEY", var2.build());
      var1.putInt("TITLE_TEXT_RES_ID_KEY", this.titleTextResId);
      var1.putCharSequence("TITLE_TEXT_KEY", this.titleText);
   }

   public void onStart() {
      super.onStart();
      Window var2 = this.requireDialog().getWindow();
      if (this.fullscreen) {
         var2.setLayout(-1, -1);
         var2.setBackgroundDrawable(this.background);
      } else {
         var2.setLayout(-2, -2);
         int var1 = this.getResources().getDimensionPixelOffset(dimen.mtrl_calendar_dialog_background_inset);
         Rect var3 = new Rect(var1, var1, var1, var1);
         var2.setBackgroundDrawable(new InsetDrawable(this.background, var1, var1, var1, var1));
         var2.getDecorView().setOnTouchListener(new InsetDialogOnTouchListener(this.requireDialog(), var3));
      }

      this.startPickerFragment();
   }

   public void onStop() {
      this.pickerFragment.clearOnSelectionChangedListeners();
      super.onStop();
   }

   public boolean removeOnCancelListener(OnCancelListener var1) {
      return this.onCancelListeners.remove(var1);
   }

   public boolean removeOnDismissListener(OnDismissListener var1) {
      return this.onDismissListeners.remove(var1);
   }

   public boolean removeOnNegativeButtonClickListener(OnClickListener var1) {
      return this.onNegativeButtonClickListeners.remove(var1);
   }

   public boolean removeOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener var1) {
      return this.onPositiveButtonClickListeners.remove(var1);
   }

   public static final class Builder {
      CalendarConstraints calendarConstraints;
      final DateSelector dateSelector;
      int overrideThemeResId = 0;
      Object selection = null;
      CharSequence titleText = null;
      int titleTextResId = 0;

      private Builder(DateSelector var1) {
         this.dateSelector = var1;
      }

      static MaterialDatePicker.Builder customDatePicker(DateSelector var0) {
         return new MaterialDatePicker.Builder(var0);
      }

      public static MaterialDatePicker.Builder datePicker() {
         return new MaterialDatePicker.Builder(new SingleDateSelector());
      }

      public static MaterialDatePicker.Builder dateRangePicker() {
         return new MaterialDatePicker.Builder(new RangeDateSelector());
      }

      public MaterialDatePicker build() {
         if (this.calendarConstraints == null) {
            this.calendarConstraints = (new CalendarConstraints.Builder()).build();
         }

         if (this.titleTextResId == 0) {
            this.titleTextResId = this.dateSelector.getDefaultTitleResId();
         }

         Object var1 = this.selection;
         if (var1 != null) {
            this.dateSelector.setSelection(var1);
         }

         return MaterialDatePicker.newInstance(this);
      }

      public MaterialDatePicker.Builder setCalendarConstraints(CalendarConstraints var1) {
         this.calendarConstraints = var1;
         return this;
      }

      public MaterialDatePicker.Builder setSelection(Object var1) {
         this.selection = var1;
         return this;
      }

      public MaterialDatePicker.Builder setTheme(int var1) {
         this.overrideThemeResId = var1;
         return this;
      }

      public MaterialDatePicker.Builder setTitleText(int var1) {
         this.titleTextResId = var1;
         this.titleText = null;
         return this;
      }

      public MaterialDatePicker.Builder setTitleText(CharSequence var1) {
         this.titleText = var1;
         this.titleTextResId = 0;
         return this;
      }
   }
}
