/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.graphics.Typeface
 *  android.text.TextUtils
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.Filter
 *  android.widget.Filter$FilterResults
 *  android.widget.Filterable
 *  android.widget.TextView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$layout
 */
package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class TimeZoneFilterTypeAdapter
extends BaseAdapter
implements Filterable,
View.OnClickListener {
    private static final boolean DEBUG = false;
    public static final int FILTER_TYPE_COUNTRY = 1;
    public static final int FILTER_TYPE_EMPTY = -1;
    public static final int FILTER_TYPE_GMT = 3;
    public static final int FILTER_TYPE_NONE = 0;
    public static final int FILTER_TYPE_STATE = 2;
    public static final String TAG = "TimeZoneFilterTypeAdapter";
    View.OnClickListener mDummyListener;
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;
    private OnSetFilterListener mListener;
    private ArrayList<FilterTypeResult> mLiveResults = new ArrayList();
    private int mLiveResultsCount = 0;
    private Typeface mSansSerifLightTypeface;
    private TimeZoneData mTimeZoneData;

    public TimeZoneFilterTypeAdapter(Context context, TimeZoneData timeZoneData, OnSetFilterListener onSetFilterListener) {
        this.mDummyListener = new View.OnClickListener(){

            public void onClick(View view) {
            }
        };
        this.mTimeZoneData = timeZoneData;
        this.mListener = onSetFilterListener;
        this.mInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mSansSerifLightTypeface = Typeface.createFromAsset((AssetManager)context.getAssets(), (String)"fonts/Roboto-Light.ttf");
    }

    public int getCount() {
        return this.mLiveResultsCount;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter();
        }
        return this.mFilter;
    }

    public FilterTypeResult getItem(int n) {
        return this.mLiveResults.get(n);
    }

    public long getItemId(int n) {
        return n;
    }

    public View getView(int n, View view, ViewGroup object) {
        Object object2;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.time_zone_filter_item, (ViewGroup)object, false);
            ViewHolder.setupViewHolder(view);
        }
        object = (ViewHolder)view.getTag();
        if (n >= this.mLiveResults.size()) {
            object2 = new StringBuilder();
            object2.append("getView: ");
            object2.append(n);
            object2.append(" of ");
            object2.append(this.mLiveResults.size());
            Log.e((String)"TimeZoneFilterTypeAdapter", (String)object2.toString());
        }
        object2 = this.mLiveResults.get(n);
        object.filterType = object2.type;
        object.str = object2.constraint;
        object.time = object2.time;
        object.strTextView.setText((CharSequence)object2.constraint);
        object.strTextView.setTypeface(this.mSansSerifLightTypeface);
        return view;
    }

    public void onClick(View object) {
        if (this.mListener != null && object != null) {
            object = (ViewHolder)object.getTag();
            this.mListener.onSetFilter(object.filterType, object.str, object.time);
        }
        this.notifyDataSetInvalidated();
    }

    private class ArrayFilter
    extends Filter {
        private ArrayFilter() {
        }

        private void handleSearchByGmt(ArrayList<FilterTypeResult> arrayList, int n, boolean bl) {
            StringBuilder stringBuilder;
            TimeZoneFilterTypeAdapter timeZoneFilterTypeAdapter;
            int n2 = n;
            if (n >= 0) {
                if (n == 1) {
                    for (n2 = 19; n2 >= 10; --n2) {
                        if (!TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(n2)) continue;
                        timeZoneFilterTypeAdapter = TimeZoneFilterTypeAdapter.this;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("GMT+");
                        stringBuilder.append(n2);
                        arrayList.add(timeZoneFilterTypeAdapter.new FilterTypeResult(3, stringBuilder.toString(), n2));
                    }
                }
                if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(n)) {
                    timeZoneFilterTypeAdapter = TimeZoneFilterTypeAdapter.this;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GMT+");
                    stringBuilder.append(n);
                    arrayList.add(timeZoneFilterTypeAdapter.new FilterTypeResult(3, stringBuilder.toString(), n));
                }
                n2 = n * -1;
            }
            if (!bl && n2 != 0) {
                if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(n2)) {
                    timeZoneFilterTypeAdapter = TimeZoneFilterTypeAdapter.this;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GMT");
                    stringBuilder.append(n2);
                    arrayList.add(timeZoneFilterTypeAdapter.new FilterTypeResult(3, stringBuilder.toString(), n2));
                }
                if (n2 == -1) {
                    for (n = -10; n >= -19; --n) {
                        if (!TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(n)) continue;
                        timeZoneFilterTypeAdapter = TimeZoneFilterTypeAdapter.this;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("GMT");
                        stringBuilder.append(n);
                        arrayList.add(timeZoneFilterTypeAdapter.new FilterTypeResult(3, stringBuilder.toString(), n));
                    }
                }
            }
        }

        private boolean isStartingInitialsFor(String string2, String string3) {
            int n = string2.length();
            int n2 = string3.length();
            int n3 = 0;
            boolean bl = true;
            for (int i = 0; i < n2; ++i) {
                int n4;
                boolean bl2;
                if (!Character.isLetter(string3.charAt(i))) {
                    bl2 = true;
                    n4 = n3;
                } else {
                    n4 = n3;
                    bl2 = bl;
                    if (bl) {
                        n4 = n3 + 1;
                        if (string2.charAt(n3) != string3.charAt(i)) {
                            return false;
                        }
                        if (n4 == n) {
                            return true;
                        }
                        bl2 = false;
                    }
                }
                n3 = n4;
                bl = bl2;
            }
            if (string2.equals("usa") && string3.equals("united states")) {
                return true;
            }
            return false;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public int parseNum(String var1_1, int var2_2) {
            var6_3 = 1;
            var4_4 = 1;
            var5_5 = var2_2 + 1;
            var3_6 = var1_1.charAt(var2_2);
            if (var3_6 == '+') ** GOTO lbl11
            if (var3_6 != '-') {
                var4_4 = var6_3;
                var2_2 = var5_5;
            } else {
                var4_4 = -1;
lbl11: // 2 sources:
                if (var5_5 >= var1_1.length()) {
                    return Integer.MIN_VALUE;
                }
                var3_6 = var1_1.charAt(var5_5);
                var2_2 = var5_5 + 1;
            }
            if (!Character.isDigit(var3_6)) {
                return Integer.MIN_VALUE;
            }
            var5_5 = var7_7 = Character.digit(var3_6, 10);
            var6_3 = var2_2;
            if (var2_2 < var1_1.length()) {
                var3_6 = var1_1.charAt(var2_2);
                if (Character.isDigit(var3_6) == false) return Integer.MIN_VALUE;
                var5_5 = var7_7 * 10 + Character.digit(var3_6, 10);
                var6_3 = var2_2 + 1;
            }
            if (var6_3 == var1_1.length()) return var4_4 * var5_5;
            return Integer.MIN_VALUE;
        }

        protected Filter.FilterResults performFiltering(CharSequence object) {
            int n;
            Filter.FilterResults filterResults = new Filter.FilterResults();
            Object object2 = null;
            if (object != null) {
                object2 = object.toString().trim().toLowerCase();
            }
            if (TextUtils.isEmpty((CharSequence)object2)) {
                filterResults.values = null;
                filterResults.count = 0;
                return filterResults;
            }
            object = new ArrayList();
            int n2 = 0;
            if (object2.charAt(0) == '+' || object2.charAt(0) == '-') {
                // empty if block
            }
            if (object2.startsWith("gmt")) {
                n2 = 3;
            }
            if ((n = this.parseNum((String)object2, n2)) != Integer.MIN_VALUE) {
                boolean bl = object2.length() > n2 && object2.charAt(n2) == '+';
                this.handleSearchByGmt((ArrayList<FilterTypeResult>)object, n, bl);
            }
            Object object3 = new ArrayList();
            Iterator<String> iterator = TimeZoneFilterTypeAdapter.access$100((TimeZoneFilterTypeAdapter)TimeZoneFilterTypeAdapter.this).mTimeZonesByCountry.keySet().iterator();
            do {
                String string2;
                n = 0;
                if (!iterator.hasNext()) break;
                string2 = iterator.next();
                if (TextUtils.isEmpty((CharSequence)string2)) continue;
                String[] arrstring = string2.toLowerCase();
                int n3 = 0;
                if (!(arrstring.startsWith((String)object2) || arrstring.charAt(0) == object2.charAt(0) && this.isStartingInitialsFor((String)object2, (String)arrstring))) {
                    n2 = n3;
                    if (arrstring.contains(" ")) {
                        arrstring = arrstring.split(" ");
                        int n4 = arrstring.length;
                        do {
                            n2 = n3;
                            if (n >= n4) break;
                            if (arrstring[n].startsWith((String)object2)) {
                                n2 = 1;
                                break;
                            }
                            ++n;
                        } while (true);
                    }
                } else {
                    n2 = 1;
                }
                if (n2 == 0) continue;
                object3.add(string2);
            } while (true);
            if (object3.size() > 0) {
                Collections.sort(object3);
                object2 = object3.iterator();
                while (object2.hasNext()) {
                    object3 = (String)object2.next();
                    object.add(new FilterTypeResult(1, (String)object3, 0));
                }
            }
            filterResults.values = object;
            filterResults.count = object.size();
            return filterResults;
        }

        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            if (filterResults.values != null && filterResults.count != 0) {
                TimeZoneFilterTypeAdapter.this.mLiveResults = (ArrayList)filterResults.values;
            } else if (TimeZoneFilterTypeAdapter.this.mListener != null) {
                int n = TextUtils.isEmpty((CharSequence)charSequence) ? 0 : -1;
                TimeZoneFilterTypeAdapter.this.mListener.onSetFilter(n, null, 0);
            }
            TimeZoneFilterTypeAdapter.this.mLiveResultsCount = filterResults.count;
            if (filterResults.count > 0) {
                TimeZoneFilterTypeAdapter.this.notifyDataSetChanged();
                return;
            }
            TimeZoneFilterTypeAdapter.this.notifyDataSetInvalidated();
        }
    }

    class FilterTypeResult {
        String constraint;
        public int time;
        int type;

        public FilterTypeResult(int n, String string2, int n2) {
            this.type = n;
            this.constraint = string2;
            this.time = n2;
        }

        public String toString() {
            return this.constraint;
        }
    }

    public static interface OnSetFilterListener {
        public void onSetFilter(int var1, String var2, int var3);
    }

    static class ViewHolder {
        int filterType;
        String str;
        TextView strTextView;
        int time;

        ViewHolder() {
        }

        static void setupViewHolder(View view) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.strTextView = (TextView)view.findViewById(R.id.value);
            view.setTag((Object)viewHolder);
        }
    }

}

