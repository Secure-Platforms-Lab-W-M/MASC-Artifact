package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.core.content.ContextCompat;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.dimen;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.styleable;
import java.util.ArrayList;
import java.util.List;

public class YearPickerView extends ListView implements OnItemClickListener, CalendarDatePickerDialogFragment.OnDateChangedListener {
   private static final String TAG = "YearPickerView";
   private YearPickerView.YearAdapter mAdapter;
   private int mBackgroundColor;
   private int mChildSize;
   private int mCircleColor;
   private final CalendarDatePickerController mController;
   private TextViewWithCircularIndicator mSelectedView;
   private int mTextColor;
   private int mViewSize;

   public YearPickerView(Context var1, CalendarDatePickerController var2) {
      super(var1);
      this.mController = var2;
      var2.registerOnDateChangedListener(this);
      this.setLayoutParams(new LayoutParams(-1, -2));
      Resources var3 = var1.getResources();
      this.mViewSize = var3.getDimensionPixelOffset(dimen.date_picker_view_animator_height);
      this.mChildSize = var3.getDimensionPixelOffset(dimen.year_label_height);
      this.setVerticalFadingEdgeEnabled(true);
      this.setFadingEdgeLength(this.mChildSize / 3);
      this.init(var1);
      this.setOnItemClickListener(this);
      this.setSelector(new StateListDrawable());
      this.setDividerHeight(0);
      this.onDateChanged();
      this.mBackgroundColor = color.circle_background;
      this.mCircleColor = color.bpBlue;
      this.mTextColor = color.ampm_text_color;
   }

   private int getYearFromTextView(TextView var1) {
      return Integer.valueOf(var1.getText().toString());
   }

   private void init(Context var1) {
      ArrayList var3 = new ArrayList();

      for(int var2 = this.mController.getMinDate().year; var2 <= this.mController.getMaxDate().year; ++var2) {
         var3.add(String.format("%d", var2));
      }

      YearPickerView.YearAdapter var4 = new YearPickerView.YearAdapter(var1, layout.calendar_year_label_text_view, var3);
      this.mAdapter = var4;
      this.setAdapter(var4);
   }

   public int getFirstPositionOffset() {
      View var1 = this.getChildAt(0);
      return var1 == null ? 0 : var1.getTop();
   }

   public void onDateChanged() {
      this.mAdapter.notifyDataSetChanged();
      this.postSetSelectionCentered(this.mController.getSelectedDay().year - this.mController.getMinDate().year);
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (var1.getEventType() == 4096) {
         var1.setFromIndex(0);
         var1.setToIndex(0);
      }

   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      this.mController.tryVibrate();
      TextViewWithCircularIndicator var6 = (TextViewWithCircularIndicator)var2;
      if (var6 != null) {
         TextViewWithCircularIndicator var7 = this.mSelectedView;
         if (var6 != var7) {
            if (var7 != null) {
               var7.drawIndicator(false);
               this.mSelectedView.requestLayout();
            }

            var6.drawIndicator(true);
            var6.requestLayout();
            this.mSelectedView = var6;
         }

         this.mController.onYearSelected(this.getYearFromTextView(var6));
         this.mAdapter.notifyDataSetChanged();
      }

   }

   public void postSetSelectionCentered(int var1) {
      this.postSetSelectionFromTop(var1, this.mViewSize / 2 - this.mChildSize / 2);
   }

   public void postSetSelectionFromTop(final int var1, final int var2) {
      this.post(new Runnable() {
         public void run() {
            YearPickerView.this.setSelectionFromTop(var1, var2);
            YearPickerView.this.requestLayout();
         }
      });
   }

   public void setTheme(TypedArray var1) {
      this.mCircleColor = var1.getColor(styleable.BetterPickersDialogs_bpRadialPointerColor, ContextCompat.getColor(this.getContext(), color.bpBlue));
      this.mTextColor = var1.getColor(styleable.BetterPickersDialogs_bpBodyUnselectedTextColor, ContextCompat.getColor(this.getContext(), color.ampm_text_color));
   }

   private class YearAdapter extends ArrayAdapter {
      public YearAdapter(Context var2, int var3, List var4) {
         super(var2, var3, var4);
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         TextViewWithCircularIndicator var5 = (TextViewWithCircularIndicator)super.getView(var1, var2, var3);
         var5.requestLayout();
         var1 = YearPickerView.this.getYearFromTextView(var5);
         var5.setCircleColor(YearPickerView.this.mCircleColor);
         var5.setTextColor(YearPickerView.this.mTextColor);
         boolean var4;
         if (YearPickerView.this.mController.getSelectedDay().year == var1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.drawIndicator(var4);
         if (var4) {
            YearPickerView.this.mSelectedView = var5;
         }

         return var5;
      }
   }
}
