package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import com.codetroopers.betterpickers.R.array;
import com.codetroopers.betterpickers.R.string;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneData {
   private static final boolean DEBUG = false;
   private static final int OFFSET_ARRAY_OFFSET = 20;
   private static final String PALESTINE_COUNTRY_CODE = "PS";
   private static final String TAG = "TimeZoneData";
   public static boolean is24HourFormat;
   private static String[] mBackupCountryCodes;
   private static Locale mBackupCountryLocale;
   private static String[] mBackupCountryNames;
   private String mAlternateDefaultTimeZoneId;
   private Context mContext;
   private HashMap mCountryCodeToNameMap = new HashMap();
   private String mDefaultTimeZoneCountry;
   public String mDefaultTimeZoneId;
   private TimeZoneInfo mDefaultTimeZoneInfo;
   private boolean[] mHasTimeZonesInHrOffset = new boolean[40];
   private String mPalestineDisplayName;
   private long mTimeMillis;
   HashSet mTimeZoneNames = new HashSet();
   ArrayList mTimeZones;
   LinkedHashMap mTimeZonesByCountry;
   private HashMap mTimeZonesById;
   SparseArray mTimeZonesByOffsets;

   public TimeZoneData(Context var1, String var2, long var3) {
      this.mContext = var1;
      boolean var7 = DateFormat.is24HourFormat(var1);
      TimeZoneInfo.is24HourFormat = var7;
      is24HourFormat = var7;
      this.mAlternateDefaultTimeZoneId = var2;
      this.mDefaultTimeZoneId = var2;
      long var5 = System.currentTimeMillis();
      if (var3 == 0L) {
         this.mTimeMillis = var5;
      } else {
         this.mTimeMillis = var3;
      }

      this.mPalestineDisplayName = var1.getResources().getString(string.palestine_display_name);
      this.loadTzs(var1);
      StringBuilder var8 = new StringBuilder();
      var8.append("Time to load time zones (ms): ");
      var8.append(System.currentTimeMillis() - var5);
      Log.i("TimeZoneData", var8.toString());
   }

   private String getCountryNames(String var1, String var2) {
      Locale var5 = Locale.getDefault();
      if ("PS".equalsIgnoreCase(var2)) {
         var1 = this.mPalestineDisplayName;
      } else {
         var1 = (new Locale(var1, var2)).getDisplayCountry(var5);
      }

      if (!var2.equals(var1)) {
         return var1;
      } else {
         if (mBackupCountryCodes == null || !var5.equals(mBackupCountryLocale)) {
            mBackupCountryLocale = var5;
            mBackupCountryCodes = this.mContext.getResources().getStringArray(array.backup_country_codes);
            mBackupCountryNames = this.mContext.getResources().getStringArray(array.backup_country_names);
         }

         int var4 = Math.min(mBackupCountryCodes.length, mBackupCountryNames.length);

         for(int var3 = 0; var3 < var4; ++var3) {
            if (mBackupCountryCodes[var3].equals(var2)) {
               return mBackupCountryNames[var3];
            }
         }

         return var2;
      }
   }

   private int getIdenticalTimeZoneInTheCountry(TimeZoneInfo var1) {
      int var2 = 0;

      for(Iterator var3 = this.mTimeZones.iterator(); var3.hasNext(); ++var2) {
         TimeZoneInfo var4 = (TimeZoneInfo)var3.next();
         if (var4.hasSameRules(var1)) {
            if (var4.mCountry == null) {
               if (var1.mCountry == null) {
                  return var2;
               }
            } else if (var4.mCountry.equals(var1.mCountry)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private void indexByOffsets(int var1, TimeZoneInfo var2) {
      int var3 = (int)((long)var2.getNowOffsetMillis() / 3600000L) + 20;
      this.mHasTimeZonesInHrOffset[var3] = true;
      ArrayList var4 = (ArrayList)this.mTimeZonesByOffsets.get(var3);
      ArrayList var5 = var4;
      if (var4 == null) {
         var5 = new ArrayList();
         this.mTimeZonesByOffsets.put(var3, var5);
      }

      var5.add(var1);
   }

   private HashSet loadTzsInZoneTab(Context param1) {
      // $FF: Couldn't be decompiled
   }

   private void populateDisplayNameOverrides(Resources var1) {
      String[] var4 = var1.getStringArray(array.timezone_rename_ids);
      String[] var7 = var1.getStringArray(array.timezone_rename_labels);
      int var2 = var4.length;
      StringBuilder var5;
      if (var4.length != var7.length) {
         var5 = new StringBuilder();
         var5.append("timezone_rename_ids len=");
         var5.append(var4.length);
         var5.append(" timezone_rename_labels len=");
         var5.append(var7.length);
         Log.e("TimeZoneData", var5.toString());
         var2 = Math.min(var4.length, var7.length);
      }

      for(int var3 = 0; var3 < var2; ++var3) {
         TimeZoneInfo var6 = (TimeZoneInfo)this.mTimeZonesById.get(var4[var3]);
         if (var6 != null) {
            var6.mDisplayName = var7[var3];
         } else {
            var5 = new StringBuilder();
            var5.append("Could not find timezone with label: ");
            var5.append(var7[var3]);
            Log.e("TimeZoneData", var5.toString());
         }
      }

   }

   private void printTimeZones() {
      TimeZoneInfo var3 = null;
      boolean var1 = true;

      boolean var2;
      for(Iterator var5 = this.mTimeZones.iterator(); var5.hasNext(); var1 = var2) {
         TimeZoneInfo var4 = (TimeZoneInfo)var5.next();
         if (var4.mTz.getDisplayName().startsWith("GMT") && !var4.mTzId.startsWith("Etc/GMT")) {
            Log.e("GMT", var4.toString());
         }

         var2 = var1;
         if (var3 != null) {
            if (var3.compareTo(var4) == 0) {
               var2 = var1;
               if (var1) {
                  Log.e("SAME", var3.toString());
                  var2 = false;
               }

               Log.e("SAME", var4.toString());
            } else {
               var2 = true;
            }
         }

         var3 = var4;
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("Total number of tz's = ");
      var6.append(this.mTimeZones.size());
      Log.e("TimeZoneData", var6.toString());
   }

   public int findIndexByTimeZoneIdSlow(String var1) {
      int var2 = 0;

      for(Iterator var3 = this.mTimeZones.iterator(); var3.hasNext(); ++var2) {
         if (var1.equals(((TimeZoneInfo)var3.next()).mTzId)) {
            return var2;
         }
      }

      return -1;
   }

   public TimeZoneInfo get(int var1) {
      return (TimeZoneInfo)this.mTimeZones.get(var1);
   }

   public int getDefaultTimeZoneIndex() {
      return this.mTimeZones.indexOf(this.mDefaultTimeZoneInfo);
   }

   public ArrayList getTimeZonesByOffset(int var1) {
      var1 += 20;
      return var1 < this.mHasTimeZonesInHrOffset.length && var1 >= 0 ? (ArrayList)this.mTimeZonesByOffsets.get(var1) : null;
   }

   public boolean hasTimeZonesInHrOffset(int var1) {
      var1 += 20;
      boolean[] var2 = this.mHasTimeZonesInHrOffset;
      return var1 < var2.length && var1 >= 0 ? var2[var1] : false;
   }

   void loadTzs(Context var1) {
      this.mTimeZones = new ArrayList();
      HashSet var9 = this.loadTzsInZoneTab(var1);
      String[] var4 = TimeZone.getAvailableIDs();
      int var3 = var4.length;

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         String var5 = var4[var2];
         if (!var9.contains(var5) && var5.startsWith("Etc/GMT")) {
            TimeZone var6 = TimeZone.getTimeZone(var5);
            if (var6 == null) {
               StringBuilder var16 = new StringBuilder();
               var16.append("Timezone not found: ");
               var16.append(var5);
               Log.e("TimeZoneData", var16.toString());
            } else {
               TimeZoneInfo var14 = new TimeZoneInfo(var6, (String)null);
               if (this.getIdenticalTimeZoneInTheCountry(var14) == -1) {
                  this.mTimeZones.add(var14);
               }
            }
         }
      }

      Collections.sort(this.mTimeZones);
      this.mTimeZonesByCountry = new LinkedHashMap();
      this.mTimeZonesByOffsets = new SparseArray(this.mHasTimeZonesInHrOffset.length);
      this.mTimeZonesById = new HashMap(this.mTimeZones.size());
      Iterator var10 = this.mTimeZones.iterator();

      while(var10.hasNext()) {
         TimeZoneInfo var12 = (TimeZoneInfo)var10.next();
         this.mTimeZonesById.put(var12.mTzId, var12);
      }

      this.populateDisplayNameOverrides(this.mContext.getResources());
      Date var15 = new Date(this.mTimeMillis);
      Locale var17 = Locale.getDefault();
      var2 = 0;

      for(Iterator var7 = this.mTimeZones.iterator(); var7.hasNext(); ++var2) {
         TimeZoneInfo var8 = (TimeZoneInfo)var7.next();
         if (var8.mDisplayName == null) {
            var8.mDisplayName = var8.mTz.getDisplayName(var8.mTz.inDaylightTime(var15), 1, var17);
         }

         ArrayList var13 = (ArrayList)this.mTimeZonesByCountry.get(var8.mCountry);
         ArrayList var11 = var13;
         if (var13 == null) {
            var11 = new ArrayList();
            this.mTimeZonesByCountry.put(var8.mCountry, var11);
         }

         var11.add(var2);
         this.indexByOffsets(var2, var8);
         if (!var8.mDisplayName.endsWith(":00")) {
            this.mTimeZoneNames.add(var8.mDisplayName);
         }
      }

   }

   public void setTime(long var1) {
      this.mTimeMillis = var1;
   }

   public int size() {
      return this.mTimeZones.size();
   }
}
