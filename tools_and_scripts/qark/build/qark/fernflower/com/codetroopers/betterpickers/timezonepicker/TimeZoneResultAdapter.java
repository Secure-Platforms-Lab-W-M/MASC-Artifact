package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TimeZoneResultAdapter extends BaseAdapter implements OnItemClickListener, TimeZoneFilterTypeAdapter.OnSetFilterListener {
   private static final boolean DEBUG = false;
   private static final int EMPTY_INDEX = -100;
   private static final String KEY_RECENT_TIMEZONES = "preferences_recent_timezones";
   private static final int MAX_RECENT_TIMEZONES = 3;
   private static final String RECENT_TIMEZONES_DELIMITER = ",";
   private static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";
   private static final String TAG = "TimeZoneResultAdapter";
   private static final int VIEW_TAG_TIME_ZONE;
   private Context mContext;
   private int[] mFilteredTimeZoneIndices;
   private int mFilteredTimeZoneLength = 0;
   private boolean mHasResults = false;
   private LayoutInflater mInflater;
   private String mLastFilterString;
   private int mLastFilterTime;
   private int mLastFilterType;
   private Typeface mSansSerifLightTypeface;
   private TimeZoneData mTimeZoneData;
   private TimeZonePickerView.OnTimeZoneSetListener mTimeZoneSetListener;

   static {
      VIEW_TAG_TIME_ZONE = id.time_zone;
   }

   public TimeZoneResultAdapter(Context var1, TimeZoneData var2, TimeZonePickerView.OnTimeZoneSetListener var3) {
      this.mContext = var1;
      this.mTimeZoneData = var2;
      this.mTimeZoneSetListener = var3;
      this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
      this.mFilteredTimeZoneIndices = new int[this.mTimeZoneData.size()];
      this.mSansSerifLightTypeface = Typeface.createFromAsset(var1.getAssets(), "fonts/Roboto-Light.ttf");
      this.onSetFilter(0, (String)null, 0);
   }

   public boolean areAllItemsEnabled() {
      return false;
   }

   public int getCount() {
      return this.mFilteredTimeZoneLength;
   }

   public Object getItem(int var1) {
      return var1 >= 0 && var1 < this.mFilteredTimeZoneLength ? this.mTimeZoneData.get(this.mFilteredTimeZoneIndices[var1]) : null;
   }

   public long getItemId(int var1) {
      return (long)this.mFilteredTimeZoneIndices[var1];
   }

   public String getLastFilterString() {
      return this.mLastFilterString;
   }

   public int getLastFilterTime() {
      return this.mLastFilterTime;
   }

   public int getLastFilterType() {
      return this.mLastFilterType;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      View var4 = var2;
      if (this.mFilteredTimeZoneIndices[var1] == -100) {
         var2 = this.mInflater.inflate(layout.empty_time_zone_item, var3, false);
         ((TextView)var2.findViewById(id.empty_item)).setTypeface(this.mSansSerifLightTypeface);
         return var2;
      } else {
         label18: {
            if (var2 != null) {
               var2 = var2;
               if (var4.findViewById(id.empty_item) == null) {
                  break label18;
               }
            }

            var2 = this.mInflater.inflate(layout.time_zone_item, var3, false);
            TimeZoneResultAdapter.ViewHolder.setupViewHolder(var2);
         }

         TimeZoneResultAdapter.ViewHolder var5 = (TimeZoneResultAdapter.ViewHolder)var2.getTag();
         TimeZoneInfo var6 = this.mTimeZoneData.get(this.mFilteredTimeZoneIndices[var1]);
         var2.setTag(VIEW_TAG_TIME_ZONE, var6);
         var5.timeZone.setTypeface(this.mSansSerifLightTypeface);
         var5.timeOffset.setTypeface(this.mSansSerifLightTypeface);
         var5.location.setTypeface(this.mSansSerifLightTypeface);
         var5.timeZone.setText(var6.mDisplayName);
         var5.timeOffset.setText(var6.getGmtDisplayName(this.mContext));
         String var7 = var6.mCountry;
         if (var7 == null) {
            var5.location.setVisibility(4);
            return var2;
         } else {
            var5.location.setText(var7);
            var5.location.setVisibility(0);
            return var2;
         }
      }
   }

   public boolean hasResults() {
      return this.mHasResults;
   }

   public boolean hasStableIds() {
      return true;
   }

   public boolean isEnabled(int var1) {
      return this.mFilteredTimeZoneIndices[var1] >= 0;
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      if (this.mTimeZoneSetListener != null) {
         TimeZoneInfo var6 = (TimeZoneInfo)var2.getTag(VIEW_TAG_TIME_ZONE);
         if (var6 != null) {
            this.mTimeZoneSetListener.onTimeZoneSet(var6);
            this.saveRecentTimezone(var6.mTzId);
         }
      }

   }

   public void onSetFilter(int var1, String var2, int var3) {
      this.mLastFilterType = var1;
      this.mLastFilterString = var2;
      this.mLastFilterTime = var3;
      boolean var5 = false;
      this.mFilteredTimeZoneLength = 0;
      int[] var10;
      if (var1 != -1) {
         if (var1 != 0) {
            Integer var6;
            int[] var7;
            ArrayList var8;
            Iterator var9;
            if (var1 != 1) {
               if (var1 != 2) {
                  if (var1 != 3) {
                     throw new IllegalArgumentException();
                  }

                  var8 = this.mTimeZoneData.getTimeZonesByOffset(var3);
                  if (var8 != null) {
                     for(var9 = var8.iterator(); var9.hasNext(); var7[var1] = var6) {
                        var6 = (Integer)var9.next();
                        var7 = this.mFilteredTimeZoneIndices;
                        var1 = this.mFilteredTimeZoneLength++;
                     }
                  }
               }
            } else {
               var8 = (ArrayList)this.mTimeZoneData.mTimeZonesByCountry.get(var2);
               if (var8 != null) {
                  for(var9 = var8.iterator(); var9.hasNext(); var7[var1] = var6) {
                     var6 = (Integer)var9.next();
                     var7 = this.mFilteredTimeZoneIndices;
                     var1 = this.mFilteredTimeZoneLength++;
                  }
               }
            }
         } else {
            var1 = this.mTimeZoneData.getDefaultTimeZoneIndex();
            if (var1 != -1) {
               var10 = this.mFilteredTimeZoneIndices;
               var3 = this.mFilteredTimeZoneLength++;
               var10[var3] = var1;
            }

            var2 = this.mContext.getSharedPreferences("com.android.calendar_preferences", 0).getString("preferences_recent_timezones", (String)null);
            if (!TextUtils.isEmpty(var2)) {
               String[] var11 = var2.split(",");

               for(var1 = var11.length - 1; var1 >= 0; --var1) {
                  if (!TextUtils.isEmpty(var11[var1]) && !var11[var1].equals(this.mTimeZoneData.mDefaultTimeZoneId)) {
                     var3 = this.mTimeZoneData.findIndexByTimeZoneIdSlow(var11[var1]);
                     if (var3 != -1) {
                        int[] var12 = this.mFilteredTimeZoneIndices;
                        int var4 = this.mFilteredTimeZoneLength++;
                        var12[var4] = var3;
                     }
                  }
               }
            }
         }
      } else {
         var10 = this.mFilteredTimeZoneIndices;
         this.mFilteredTimeZoneLength = 0 + 1;
         var10[0] = -100;
      }

      if (this.mFilteredTimeZoneLength > 0) {
         var5 = true;
      }

      this.mHasResults = var5;
      this.notifyDataSetChanged();
   }

   public void saveRecentTimezone(String var1) {
      Context var4 = this.mContext;
      int var2 = 0;
      SharedPreferences var10 = var4.getSharedPreferences("com.android.calendar_preferences", 0);
      String var6 = var10.getString("preferences_recent_timezones", (String)null);
      if (var6 != null) {
         LinkedHashSet var5 = new LinkedHashSet();
         String[] var12 = var6.split(",");

         for(int var3 = var12.length; var2 < var3; ++var2) {
            String var7 = var12[var2];
            if (!var5.contains(var7) && !var1.equals(var7)) {
               var5.add(var7);
            }
         }

         Iterator var13 = var5.iterator();

         while(var5.size() >= 3 && var13.hasNext()) {
            var13.next();
            var13.remove();
         }

         var5.add(var1);
         StringBuilder var8 = new StringBuilder();
         boolean var9 = true;

         for(Iterator var11 = var5.iterator(); var11.hasNext(); var8.append(var6)) {
            var6 = (String)var11.next();
            if (var9) {
               var9 = false;
            } else {
               var8.append(",");
            }
         }

         var1 = var8.toString();
      }

      var10.edit().putString("preferences_recent_timezones", var1).apply();
   }

   static class ViewHolder {
      TextView location;
      TextView timeOffset;
      TextView timeZone;

      static void setupViewHolder(View var0) {
         TimeZoneResultAdapter.ViewHolder var1 = new TimeZoneResultAdapter.ViewHolder();
         var1.timeZone = (TextView)var0.findViewById(id.time_zone);
         var1.timeOffset = (TextView)var0.findViewById(id.time_offset);
         var1.location = (TextView)var0.findViewById(id.location);
         var0.setTag(var1);
      }
   }
}
