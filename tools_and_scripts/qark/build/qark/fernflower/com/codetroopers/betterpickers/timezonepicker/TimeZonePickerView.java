package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.codetroopers.betterpickers.R.drawable;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.string;

public class TimeZonePickerView extends LinearLayout implements TextWatcher, OnItemClickListener, OnClickListener {
   private static final String TAG = "TimeZonePickerView";
   private AutoCompleteTextView mAutoCompleteTextView;
   private ImageButton mClearButton;
   private Context mContext;
   private TimeZoneFilterTypeAdapter mFilterAdapter;
   private boolean mFirstTime = true;
   private boolean mHideFilterSearchOnStart = false;
   TimeZoneResultAdapter mResultAdapter;
   private Typeface mSansSerifLightTypeface;

   public TimeZonePickerView(Context var1, AttributeSet var2, String var3, long var4, TimeZonePickerView.OnTimeZoneSetListener var6, boolean var7) {
      super(var1, var2);
      this.mContext = var1;
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(layout.timezonepickerview, this, true);
      this.mSansSerifLightTypeface = Typeface.createFromAsset(var1.getAssets(), "fonts/Roboto-Light.ttf");
      this.mHideFilterSearchOnStart = var7;
      TimeZoneData var8 = new TimeZoneData(this.mContext, var3, var4);
      this.mResultAdapter = new TimeZoneResultAdapter(this.mContext, var8, var6);
      ListView var11 = (ListView)this.findViewById(id.timezonelist);
      var11.setAdapter(this.mResultAdapter);
      var11.setOnItemClickListener(this.mResultAdapter);
      this.mFilterAdapter = new TimeZoneFilterTypeAdapter(this.mContext, var8, this.mResultAdapter);
      AutoCompleteTextView var9 = (AutoCompleteTextView)this.findViewById(id.searchBox);
      this.mAutoCompleteTextView = var9;
      var9.setTypeface(this.mSansSerifLightTypeface);
      this.mAutoCompleteTextView.addTextChangedListener(this);
      this.mAutoCompleteTextView.setOnItemClickListener(this);
      this.mAutoCompleteTextView.setOnClickListener(this);
      this.updateHint(string.hint_time_zone_search, drawable.ic_search_holo_light);
      ImageButton var10 = (ImageButton)this.findViewById(id.clear_search);
      this.mClearButton = var10;
      var10.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            TimeZonePickerView.this.mAutoCompleteTextView.getEditableText().clear();
         }
      });
   }

   private void filterOnString(String var1) {
      if (this.mAutoCompleteTextView.getAdapter() == null) {
         this.mAutoCompleteTextView.setAdapter(this.mFilterAdapter);
      }

      this.mHideFilterSearchOnStart = false;
      this.mFilterAdapter.getFilter().filter(var1);
   }

   private void updateHint(int var1, int var2) {
      String var3 = this.getResources().getString(var1);
      Drawable var4 = this.getResources().getDrawable(var2);
      SpannableStringBuilder var5 = new SpannableStringBuilder("   ");
      var5.append(var3);
      var1 = (int)((double)this.mAutoCompleteTextView.getTextSize() * 1.25D);
      var4.setBounds(0, 0, var1, var1);
      var5.setSpan(new ImageSpan(var4), 1, 2, 33);
      this.mAutoCompleteTextView.setHint(var5);
   }

   public void afterTextChanged(Editable var1) {
      ImageButton var3 = this.mClearButton;
      if (var3 != null) {
         byte var2;
         if (var1.length() > 0) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var3.setVisibility(var2);
      }

   }

   public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
   }

   public boolean getHideFilterSearchOnStart() {
      return this.mHideFilterSearchOnStart;
   }

   public String getLastFilterString() {
      TimeZoneResultAdapter var1 = this.mResultAdapter;
      return var1 != null ? var1.getLastFilterString() : null;
   }

   public int getLastFilterTime() {
      TimeZoneResultAdapter var1 = this.mResultAdapter;
      return var1 != null ? var1.getLastFilterType() : -1;
   }

   public int getLastFilterType() {
      TimeZoneResultAdapter var1 = this.mResultAdapter;
      return var1 != null ? var1.getLastFilterType() : -1;
   }

   public boolean hasResults() {
      TimeZoneResultAdapter var1 = this.mResultAdapter;
      return var1 != null && var1.hasResults();
   }

   public void onClick(View var1) {
      AutoCompleteTextView var2 = this.mAutoCompleteTextView;
      if (var2 != null && !var2.isPopupShowing()) {
         this.filterOnString(this.mAutoCompleteTextView.getText().toString());
      }

   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      ((InputMethodManager)this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mAutoCompleteTextView.getWindowToken(), 0);
      this.mHideFilterSearchOnStart = true;
      this.mFilterAdapter.onClick(var2);
   }

   public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      if (this.mFirstTime && this.mHideFilterSearchOnStart) {
         this.mFirstTime = false;
      } else {
         this.filterOnString(var1.toString());
      }
   }

   public void showFilterResults(int var1, String var2, int var3) {
      TimeZoneResultAdapter var4 = this.mResultAdapter;
      if (var4 != null) {
         var4.onSetFilter(var1, var2, var3);
      }

   }

   public interface OnTimeZoneSetListener {
      void onTimeZoneSet(TimeZoneInfo var1);
   }
}
