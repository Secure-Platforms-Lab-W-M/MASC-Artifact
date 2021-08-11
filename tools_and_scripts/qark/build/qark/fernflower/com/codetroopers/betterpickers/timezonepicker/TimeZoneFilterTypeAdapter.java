package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter.FilterResults;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TimeZoneFilterTypeAdapter extends BaseAdapter implements Filterable, OnClickListener {
   private static final boolean DEBUG = false;
   public static final int FILTER_TYPE_COUNTRY = 1;
   public static final int FILTER_TYPE_EMPTY = -1;
   public static final int FILTER_TYPE_GMT = 3;
   public static final int FILTER_TYPE_NONE = 0;
   public static final int FILTER_TYPE_STATE = 2;
   public static final String TAG = "TimeZoneFilterTypeAdapter";
   OnClickListener mDummyListener = new OnClickListener() {
      public void onClick(View var1) {
      }
   };
   private TimeZoneFilterTypeAdapter.ArrayFilter mFilter;
   private LayoutInflater mInflater;
   private TimeZoneFilterTypeAdapter.OnSetFilterListener mListener;
   private ArrayList mLiveResults = new ArrayList();
   private int mLiveResultsCount = 0;
   private Typeface mSansSerifLightTypeface;
   private TimeZoneData mTimeZoneData;

   public TimeZoneFilterTypeAdapter(Context var1, TimeZoneData var2, TimeZoneFilterTypeAdapter.OnSetFilterListener var3) {
      this.mTimeZoneData = var2;
      this.mListener = var3;
      this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
      this.mSansSerifLightTypeface = Typeface.createFromAsset(var1.getAssets(), "fonts/Roboto-Light.ttf");
   }

   public int getCount() {
      return this.mLiveResultsCount;
   }

   public Filter getFilter() {
      if (this.mFilter == null) {
         this.mFilter = new TimeZoneFilterTypeAdapter.ArrayFilter();
      }

      return this.mFilter;
   }

   public TimeZoneFilterTypeAdapter.FilterTypeResult getItem(int var1) {
      return (TimeZoneFilterTypeAdapter.FilterTypeResult)this.mLiveResults.get(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (var2 == null) {
         var2 = this.mInflater.inflate(layout.time_zone_filter_item, var3, false);
         TimeZoneFilterTypeAdapter.ViewHolder.setupViewHolder(var2);
      }

      TimeZoneFilterTypeAdapter.ViewHolder var5 = (TimeZoneFilterTypeAdapter.ViewHolder)var2.getTag();
      if (var1 >= this.mLiveResults.size()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("getView: ");
         var4.append(var1);
         var4.append(" of ");
         var4.append(this.mLiveResults.size());
         Log.e("TimeZoneFilterTypeAdapter", var4.toString());
      }

      TimeZoneFilterTypeAdapter.FilterTypeResult var6 = (TimeZoneFilterTypeAdapter.FilterTypeResult)this.mLiveResults.get(var1);
      var5.filterType = var6.type;
      var5.str = var6.constraint;
      var5.time = var6.time;
      var5.strTextView.setText(var6.constraint);
      var5.strTextView.setTypeface(this.mSansSerifLightTypeface);
      return var2;
   }

   public void onClick(View var1) {
      if (this.mListener != null && var1 != null) {
         TimeZoneFilterTypeAdapter.ViewHolder var2 = (TimeZoneFilterTypeAdapter.ViewHolder)var1.getTag();
         this.mListener.onSetFilter(var2.filterType, var2.str, var2.time);
      }

      this.notifyDataSetInvalidated();
   }

   private class ArrayFilter extends Filter {
      private ArrayFilter() {
      }

      // $FF: synthetic method
      ArrayFilter(Object var2) {
         this();
      }

      private void handleSearchByGmt(ArrayList var1, int var2, boolean var3) {
         int var4 = var2;
         TimeZoneFilterTypeAdapter var5;
         StringBuilder var6;
         if (var2 >= 0) {
            if (var2 == 1) {
               for(var4 = 19; var4 >= 10; --var4) {
                  if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(var4)) {
                     var5 = TimeZoneFilterTypeAdapter.this;
                     var6 = new StringBuilder();
                     var6.append("GMT+");
                     var6.append(var4);
                     var1.add(var5.new FilterTypeResult(3, var6.toString(), var4));
                  }
               }
            }

            if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(var2)) {
               var5 = TimeZoneFilterTypeAdapter.this;
               var6 = new StringBuilder();
               var6.append("GMT+");
               var6.append(var2);
               var1.add(var5.new FilterTypeResult(3, var6.toString(), var2));
            }

            var4 = var2 * -1;
         }

         if (!var3 && var4 != 0) {
            if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(var4)) {
               var5 = TimeZoneFilterTypeAdapter.this;
               var6 = new StringBuilder();
               var6.append("GMT");
               var6.append(var4);
               var1.add(var5.new FilterTypeResult(3, var6.toString(), var4));
            }

            if (var4 == -1) {
               for(var2 = -10; var2 >= -19; --var2) {
                  if (TimeZoneFilterTypeAdapter.this.mTimeZoneData.hasTimeZonesInHrOffset(var2)) {
                     var5 = TimeZoneFilterTypeAdapter.this;
                     var6 = new StringBuilder();
                     var6.append("GMT");
                     var6.append(var2);
                     var1.add(var5.new FilterTypeResult(3, var6.toString(), var2));
                  }
               }
            }
         }

      }

      private boolean isStartingInitialsFor(String var1, String var2) {
         int var8 = var1.length();
         int var9 = var2.length();
         int var5 = 0;
         boolean var6 = true;

         boolean var3;
         for(int var4 = 0; var4 < var9; var6 = var3) {
            int var7;
            if (!Character.isLetter(var2.charAt(var4))) {
               var3 = true;
               var7 = var5;
            } else {
               var7 = var5;
               var3 = var6;
               if (var6) {
                  var7 = var5 + 1;
                  if (var1.charAt(var5) != var2.charAt(var4)) {
                     return false;
                  }

                  if (var7 == var8) {
                     return true;
                  }

                  var3 = false;
               }
            }

            ++var4;
            var5 = var7;
         }

         if (var1.equals("usa") && var2.equals("united states")) {
            return true;
         } else {
            return false;
         }
      }

      public int parseNum(String var1, int var2) {
         char var3;
         byte var4;
         int var5;
         label37: {
            byte var6 = 1;
            var4 = 1;
            var5 = var2 + 1;
            var3 = var1.charAt(var2);
            if (var3 != '+') {
               if (var3 != '-') {
                  var4 = var6;
                  var2 = var5;
                  break label37;
               }

               var4 = -1;
            }

            if (var5 >= var1.length()) {
               return Integer.MIN_VALUE;
            }

            var3 = var1.charAt(var5);
            var2 = var5 + 1;
         }

         if (!Character.isDigit(var3)) {
            return Integer.MIN_VALUE;
         } else {
            int var7 = Character.digit(var3, 10);
            var5 = var7;
            int var8 = var2;
            if (var2 < var1.length()) {
               var3 = var1.charAt(var2);
               if (!Character.isDigit(var3)) {
                  return Integer.MIN_VALUE;
               }

               var5 = var7 * 10 + Character.digit(var3, 10);
               var8 = var2 + 1;
            }

            return var8 != var1.length() ? Integer.MIN_VALUE : var4 * var5;
         }
      }

      protected FilterResults performFiltering(CharSequence var1) {
         FilterResults var8 = new FilterResults();
         String var7 = null;
         if (var1 != null) {
            var7 = var1.toString().trim().toLowerCase();
         }

         if (TextUtils.isEmpty(var7)) {
            var8.values = null;
            var8.count = 0;
            return var8;
         } else {
            ArrayList var13 = new ArrayList();
            byte var2 = 0;
            if (var7.charAt(0) != '+' && var7.charAt(0) == '-') {
            }

            if (var7.startsWith("gmt")) {
               var2 = 3;
            }

            int var3 = this.parseNum(var7, var2);
            if (var3 != Integer.MIN_VALUE) {
               boolean var6;
               if (var7.length() > var2 && var7.charAt(var2) == '+') {
                  var6 = true;
               } else {
                  var6 = false;
               }

               this.handleSearchByGmt(var13, var3, var6);
            }

            ArrayList var9 = new ArrayList();
            Iterator var10 = TimeZoneFilterTypeAdapter.this.mTimeZoneData.mTimeZonesByCountry.keySet().iterator();

            while(true) {
               String var11;
               do {
                  var3 = 0;
                  if (!var10.hasNext()) {
                     if (var9.size() > 0) {
                        Collections.sort(var9);
                        Iterator var15 = var9.iterator();

                        while(var15.hasNext()) {
                           String var16 = (String)var15.next();
                           var13.add(TimeZoneFilterTypeAdapter.this.new FilterTypeResult(1, var16, 0));
                        }
                     }

                     var8.values = var13;
                     var8.count = var13.size();
                     return var8;
                  }

                  var11 = (String)var10.next();
               } while(TextUtils.isEmpty(var11));

               String var12 = var11.toLowerCase();
               boolean var4 = false;
               boolean var14;
               if (var12.startsWith(var7) || var12.charAt(0) == var7.charAt(0) && this.isStartingInitialsFor(var7, var12)) {
                  var14 = true;
               } else {
                  var14 = var4;
                  if (var12.contains(" ")) {
                     String[] var17 = var12.split(" ");
                     int var5 = var17.length;

                     while(true) {
                        var14 = var4;
                        if (var3 >= var5) {
                           break;
                        }

                        if (var17[var3].startsWith(var7)) {
                           var14 = true;
                           break;
                        }

                        ++var3;
                     }
                  }
               }

               if (var14) {
                  var9.add(var11);
               }
            }
         }
      }

      protected void publishResults(CharSequence var1, FilterResults var2) {
         if (var2.values != null && var2.count != 0) {
            TimeZoneFilterTypeAdapter.this.mLiveResults = (ArrayList)var2.values;
         } else if (TimeZoneFilterTypeAdapter.this.mListener != null) {
            byte var3;
            if (TextUtils.isEmpty(var1)) {
               var3 = 0;
            } else {
               var3 = -1;
            }

            TimeZoneFilterTypeAdapter.this.mListener.onSetFilter(var3, (String)null, 0);
         }

         TimeZoneFilterTypeAdapter.this.mLiveResultsCount = var2.count;
         if (var2.count > 0) {
            TimeZoneFilterTypeAdapter.this.notifyDataSetChanged();
         } else {
            TimeZoneFilterTypeAdapter.this.notifyDataSetInvalidated();
         }
      }
   }

   class FilterTypeResult {
      String constraint;
      public int time;
      int type;

      public FilterTypeResult(int var2, String var3, int var4) {
         this.type = var2;
         this.constraint = var3;
         this.time = var4;
      }

      public String toString() {
         return this.constraint;
      }
   }

   public interface OnSetFilterListener {
      void onSetFilter(int var1, String var2, int var3);
   }

   static class ViewHolder {
      int filterType;
      String str;
      TextView strTextView;
      int time;

      static void setupViewHolder(View var0) {
         TimeZoneFilterTypeAdapter.ViewHolder var1 = new TimeZoneFilterTypeAdapter.ViewHolder();
         var1.strTextView = (TextView)var0.findViewById(id.value);
         var0.setTag(var1);
      }
   }
}
