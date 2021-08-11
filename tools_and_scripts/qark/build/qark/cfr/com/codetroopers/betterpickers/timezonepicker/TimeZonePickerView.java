/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.IBinder
 *  android.text.Editable
 *  android.text.SpannableStringBuilder
 *  android.text.TextWatcher
 *  android.text.style.ImageSpan
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AutoCompleteTextView
 *  android.widget.Filter
 *  android.widget.ImageButton
 *  android.widget.LinearLayout
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$drawable
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$layout
 *  com.codetroopers.betterpickers.R$string
 */
package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneData;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneFilterTypeAdapter;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneInfo;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneResultAdapter;

public class TimeZonePickerView
extends LinearLayout
implements TextWatcher,
AdapterView.OnItemClickListener,
View.OnClickListener {
    private static final String TAG = "TimeZonePickerView";
    private AutoCompleteTextView mAutoCompleteTextView;
    private ImageButton mClearButton;
    private Context mContext;
    private TimeZoneFilterTypeAdapter mFilterAdapter;
    private boolean mFirstTime = true;
    private boolean mHideFilterSearchOnStart = false;
    TimeZoneResultAdapter mResultAdapter;
    private Typeface mSansSerifLightTypeface;

    public TimeZonePickerView(Context object, AttributeSet attributeSet, String string2, long l, OnTimeZoneSetListener onTimeZoneSetListener, boolean bl) {
        super((Context)object, attributeSet);
        this.mContext = object;
        ((LayoutInflater)object.getSystemService("layout_inflater")).inflate(R.layout.timezonepickerview, (ViewGroup)this, true);
        this.mSansSerifLightTypeface = Typeface.createFromAsset((AssetManager)object.getAssets(), (String)"fonts/Roboto-Light.ttf");
        this.mHideFilterSearchOnStart = bl;
        object = new TimeZoneData(this.mContext, string2, l);
        this.mResultAdapter = new TimeZoneResultAdapter(this.mContext, (TimeZoneData)object, onTimeZoneSetListener);
        attributeSet = (ListView)this.findViewById(R.id.timezonelist);
        attributeSet.setAdapter((ListAdapter)this.mResultAdapter);
        attributeSet.setOnItemClickListener((AdapterView.OnItemClickListener)this.mResultAdapter);
        this.mFilterAdapter = new TimeZoneFilterTypeAdapter(this.mContext, (TimeZoneData)object, this.mResultAdapter);
        this.mAutoCompleteTextView = object = (AutoCompleteTextView)this.findViewById(R.id.searchBox);
        object.setTypeface(this.mSansSerifLightTypeface);
        this.mAutoCompleteTextView.addTextChangedListener((TextWatcher)this);
        this.mAutoCompleteTextView.setOnItemClickListener((AdapterView.OnItemClickListener)this);
        this.mAutoCompleteTextView.setOnClickListener((View.OnClickListener)this);
        this.updateHint(R.string.hint_time_zone_search, R.drawable.ic_search_holo_light);
        this.mClearButton = object = (ImageButton)this.findViewById(R.id.clear_search);
        object.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                TimeZonePickerView.this.mAutoCompleteTextView.getEditableText().clear();
            }
        });
    }

    private void filterOnString(String string2) {
        if (this.mAutoCompleteTextView.getAdapter() == null) {
            this.mAutoCompleteTextView.setAdapter((ListAdapter)this.mFilterAdapter);
        }
        this.mHideFilterSearchOnStart = false;
        this.mFilterAdapter.getFilter().filter((CharSequence)string2);
    }

    private void updateHint(int n, int n2) {
        String string2 = this.getResources().getString(n);
        Drawable drawable2 = this.getResources().getDrawable(n2);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)"   ");
        spannableStringBuilder.append((CharSequence)string2);
        n = (int)((double)this.mAutoCompleteTextView.getTextSize() * 1.25);
        drawable2.setBounds(0, 0, n, n);
        spannableStringBuilder.setSpan((Object)new ImageSpan(drawable2), 1, 2, 33);
        this.mAutoCompleteTextView.setHint((CharSequence)spannableStringBuilder);
    }

    public void afterTextChanged(Editable editable) {
        ImageButton imageButton = this.mClearButton;
        if (imageButton != null) {
            int n = editable.length() > 0 ? 0 : 8;
            imageButton.setVisibility(n);
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
    }

    public boolean getHideFilterSearchOnStart() {
        return this.mHideFilterSearchOnStart;
    }

    public String getLastFilterString() {
        TimeZoneResultAdapter timeZoneResultAdapter = this.mResultAdapter;
        if (timeZoneResultAdapter != null) {
            return timeZoneResultAdapter.getLastFilterString();
        }
        return null;
    }

    public int getLastFilterTime() {
        TimeZoneResultAdapter timeZoneResultAdapter = this.mResultAdapter;
        if (timeZoneResultAdapter != null) {
            return timeZoneResultAdapter.getLastFilterType();
        }
        return -1;
    }

    public int getLastFilterType() {
        TimeZoneResultAdapter timeZoneResultAdapter = this.mResultAdapter;
        if (timeZoneResultAdapter != null) {
            return timeZoneResultAdapter.getLastFilterType();
        }
        return -1;
    }

    public boolean hasResults() {
        TimeZoneResultAdapter timeZoneResultAdapter = this.mResultAdapter;
        if (timeZoneResultAdapter != null && timeZoneResultAdapter.hasResults()) {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        view = this.mAutoCompleteTextView;
        if (view != null && !view.isPopupShowing()) {
            this.filterOnString(this.mAutoCompleteTextView.getText().toString());
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
        ((InputMethodManager)this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mAutoCompleteTextView.getWindowToken(), 0);
        this.mHideFilterSearchOnStart = true;
        this.mFilterAdapter.onClick(view);
    }

    public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
        if (this.mFirstTime && this.mHideFilterSearchOnStart) {
            this.mFirstTime = false;
            return;
        }
        this.filterOnString(charSequence.toString());
    }

    public void showFilterResults(int n, String string2, int n2) {
        TimeZoneResultAdapter timeZoneResultAdapter = this.mResultAdapter;
        if (timeZoneResultAdapter != null) {
            timeZoneResultAdapter.onSetFilter(n, string2, n2);
        }
    }

    public static interface OnTimeZoneSetListener {
        public void onTimeZoneSet(TimeZoneInfo var1);
    }

}

