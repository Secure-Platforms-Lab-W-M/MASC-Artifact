package com.codetroopers.betterpickers.timezonepicker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.codetroopers.betterpickers.OnDialogDismissListener;

public class TimeZonePickerDialogFragment extends DialogFragment implements TimeZonePickerView.OnTimeZoneSetListener {
   public static final String BUNDLE_START_TIME_MILLIS = "bundle_event_start_time";
   public static final String BUNDLE_TIME_ZONE = "bundle_event_time_zone";
   private static final String KEY_HAS_RESULTS = "has_results";
   private static final String KEY_HIDE_FILTER_SEARCH = "hide_filter_search";
   private static final String KEY_LAST_FILTER_STRING = "last_filter_string";
   private static final String KEY_LAST_FILTER_TIME = "last_filter_time";
   private static final String KEY_LAST_FILTER_TYPE = "last_filter_type";
   private OnDialogDismissListener mDismissCallback;
   private boolean mHasCachedResults = false;
   private TimeZonePickerDialogFragment.OnTimeZoneSetListener mTimeZoneSetListener;
   private TimeZonePickerView mView;

   public Dialog onCreateDialog(Bundle var1) {
      Dialog var2 = super.onCreateDialog(var1);
      var2.requestWindowFeature(1);
      var2.getWindow().setSoftInputMode(16);
      return var2;
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Bundle var7 = this.getArguments();
      long var4;
      String var8;
      if (var7 != null) {
         var4 = var7.getLong("bundle_event_start_time");
         var8 = var7.getString("bundle_event_time_zone");
      } else {
         var4 = 0L;
         var8 = null;
      }

      boolean var6;
      if (var3 != null) {
         var6 = var3.getBoolean("hide_filter_search");
      } else {
         var6 = false;
      }

      this.mView = new TimeZonePickerView(this.getActivity(), (AttributeSet)null, var8, var4, this, var6);
      if (var3 != null && var3.getBoolean("has_results", false)) {
         this.mView.showFilterResults(var3.getInt("last_filter_type"), var3.getString("last_filter_string"), var3.getInt("last_filter_time"));
      }

      return this.mView;
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      OnDialogDismissListener var2 = this.mDismissCallback;
      if (var2 != null) {
         var2.onDialogDismiss(var1);
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      TimeZonePickerView var3 = this.mView;
      boolean var2;
      if (var3 != null && var3.hasResults()) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.putBoolean("has_results", var2);
      var3 = this.mView;
      if (var3 != null) {
         var1.putInt("last_filter_type", var3.getLastFilterType());
         var1.putString("last_filter_string", this.mView.getLastFilterString());
         var1.putInt("last_filter_time", this.mView.getLastFilterTime());
         var1.putBoolean("hide_filter_search", this.mView.getHideFilterSearchOnStart());
      }

   }

   public void onTimeZoneSet(TimeZoneInfo var1) {
      TimeZonePickerDialogFragment.OnTimeZoneSetListener var2 = this.mTimeZoneSetListener;
      if (var2 != null) {
         var2.onTimeZoneSet(var1);
      }

      this.dismiss();
   }

   public TimeZonePickerDialogFragment setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
      return this;
   }

   public void setOnTimeZoneSetListener(TimeZonePickerDialogFragment.OnTimeZoneSetListener var1) {
      this.mTimeZoneSetListener = var1;
   }

   public interface OnTimeZoneSetListener {
      void onTimeZoneSet(TimeZoneInfo var1);
   }
}
