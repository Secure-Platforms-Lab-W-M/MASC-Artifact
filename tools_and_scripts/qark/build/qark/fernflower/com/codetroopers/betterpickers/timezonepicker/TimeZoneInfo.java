package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneInfo implements Comparable {
   private static final int DST_SYMBOL_COLOR = -4210753;
   private static final int GMT_TEXT_COLOR = -7829368;
   public static int NUM_OF_TRANSITIONS = 6;
   private static final char SEPARATOR = ',';
   private static final String TAG = null;
   public static boolean is24HourFormat;
   private static Formatter mFormatter;
   private static SparseArray mGmtDisplayNameCache;
   private static long mGmtDisplayNameUpdateTime;
   private static StringBuilder mSB = new StringBuilder(50);
   private static final Factory mSpannableFactory = Factory.getInstance();
   public static long time = System.currentTimeMillis() / 1000L;
   public int groupId;
   public String mCountry;
   public String mDisplayName;
   SparseArray mLocalTimeCache = new SparseArray();
   long mLocalTimeCacheReferenceTime = 0L;
   int mRawoffset;
   public long[] mTransitions;
   TimeZone mTz;
   public String mTzId;
   private Time recycledTime = new Time();

   static {
      mFormatter = new Formatter(mSB, Locale.getDefault());
      mGmtDisplayNameCache = new SparseArray();
   }

   public TimeZoneInfo(TimeZone var1, String var2) {
      this.mTz = var1;
      this.mTzId = var1.getID();
      this.mCountry = var2;
      this.mRawoffset = var1.getRawOffset();

      try {
         this.mTransitions = getTransitions(var1, time);
      } catch (NoSuchFieldException var3) {
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
         return;
      }

   }

   public static long[] copyFromIntArray(int[] var0) {
      if (var0 == null) {
         return new long[0];
      } else {
         long[] var2 = new long[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = (long)var0[var1];
         }

         return var2;
      }
   }

   private static String formatTime(DateFormat var0, int var1) {
      return var0.format(new Date((long)var1 * 1000L));
   }

   private static long[] getTransitions(TimeZone var0, long var1) throws IllegalAccessException, NoSuchFieldException {
      Field var7 = var0.getClass().getDeclaredField("mTransitions");
      var7.setAccessible(true);
      long[] var6 = null;
      long[] var8;
      if (VERSION.SDK_INT >= 23) {
         var8 = (long[])((long[])var7.get(var0));
      } else {
         var8 = copyFromIntArray((int[])((int[])var7.get(var0)));
      }

      if (var8.length != 0) {
         long[] var9 = new long[NUM_OF_TRANSITIONS];
         int var4 = 0;
         int var3 = 0;

         while(true) {
            var6 = var9;
            if (var3 >= var8.length) {
               break;
            }

            if (var8[var3] >= var1) {
               int var5 = var4 + 1;
               var9[var4] = var8[var3];
               if (var5 == NUM_OF_TRANSITIONS) {
                  return var9;
               }

               var4 = var5;
            }

            ++var3;
         }
      }

      return var6;
   }

   public int compareTo(TimeZoneInfo var1) {
      if (this.getNowOffsetMillis() != var1.getNowOffsetMillis()) {
         return var1.getNowOffsetMillis() < this.getNowOffsetMillis() ? -1 : 1;
      } else if (this.mCountry == null && var1.mCountry != null) {
         return 1;
      } else {
         String var3 = var1.mCountry;
         if (var3 == null) {
            return -1;
         } else {
            int var2 = this.mCountry.compareTo(var3);
            if (var2 != 0) {
               return var2;
            } else {
               if (Arrays.equals(this.mTransitions, var1.mTransitions)) {
                  var3 = TAG;
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Not expected to be comparing tz with the same country, same offset, same dst, same transitions:\n");
                  var4.append(this.toString());
                  var4.append("\n");
                  var4.append(var1.toString());
                  Log.e(var3, var4.toString());
               }

               var3 = this.mDisplayName;
               if (var3 != null) {
                  String var5 = var1.mDisplayName;
                  if (var5 != null) {
                     return var3.compareTo(var5);
                  }
               }

               return this.mTz.getDisplayName(Locale.getDefault()).compareTo(var1.mTz.getDisplayName(Locale.getDefault()));
            }
         }
      }
   }

   public CharSequence getGmtDisplayName(Context var1) {
      synchronized(this){}

      Throwable var10000;
      label1158: {
         long var7;
         boolean var10001;
         try {
            var7 = System.currentTimeMillis() / 60000L;
         } catch (Throwable var145) {
            var10000 = var145;
            var10001 = false;
            break label1158;
         }

         long var9 = 60000L * var7;

         int var4;
         boolean var11;
         try {
            var4 = this.mTz.getOffset(var9);
            var11 = this.mTz.useDaylightTime();
         } catch (Throwable var144) {
            var10000 = var144;
            var10001 = false;
            break label1158;
         }

         int var2;
         if (var11) {
            var2 = (int)((long)var4 + 129600000L);
         } else {
            var2 = (int)((long)var4 - 129600000L);
         }

         CharSequence var12;
         label1143: {
            label1142: {
               try {
                  if (mGmtDisplayNameUpdateTime != var7) {
                     mGmtDisplayNameUpdateTime = var7;
                     mGmtDisplayNameCache.clear();
                     break label1142;
                  }
               } catch (Throwable var143) {
                  var10000 = var143;
                  var10001 = false;
                  break label1158;
               }

               try {
                  var12 = (CharSequence)mGmtDisplayNameCache.get(var2);
                  break label1143;
               } catch (Throwable var142) {
                  var10000 = var142;
                  var10001 = false;
                  break label1158;
               }
            }

            var12 = null;
         }

         Object var13 = var12;
         if (var12 != null) {
            return (CharSequence)var13;
         }

         try {
            mSB.setLength(0);
         } catch (Throwable var141) {
            var10000 = var141;
            var10001 = false;
            break label1158;
         }

         int var3 = 524288 | 1;

         label1126: {
            try {
               if (!is24HourFormat) {
                  break label1126;
               }
            } catch (Throwable var140) {
               var10000 = var140;
               var10001 = false;
               break label1158;
            }

            var3 |= 128;
         }

         int var5;
         int var6;
         try {
            DateUtils.formatDateRange(var1, mFormatter, var9, var9, var3, this.mTzId);
            mSB.append("  ");
            var5 = mSB.length();
            TimeZonePickerUtils.appendGmtOffset(mSB, var4);
            var6 = mSB.length();
         } catch (Throwable var139) {
            var10000 = var139;
            var10001 = false;
            break label1158;
         }

         var3 = 0;
         var4 = 0;
         if (var11) {
            try {
               mSB.append(' ');
               var3 = mSB.length();
               mSB.append(TimeZonePickerUtils.getDstSymbol());
               var4 = mSB.length();
            } catch (Throwable var138) {
               var10000 = var138;
               var10001 = false;
               break label1158;
            }
         }

         try {
            var13 = mSpannableFactory.newSpannable(mSB);
            ((Spannable)var13).setSpan(new ForegroundColorSpan(-7829368), var5, var6, 33);
         } catch (Throwable var137) {
            var10000 = var137;
            var10001 = false;
            break label1158;
         }

         if (var11) {
            try {
               ((Spannable)var13).setSpan(new ForegroundColorSpan(-4210753), var3, var4, 33);
            } catch (Throwable var136) {
               var10000 = var136;
               var10001 = false;
               break label1158;
            }
         }

         label1106:
         try {
            mGmtDisplayNameCache.put(var2, var13);
            return (CharSequence)var13;
         } catch (Throwable var135) {
            var10000 = var135;
            var10001 = false;
            break label1106;
         }
      }

      Throwable var146 = var10000;
      throw var146;
   }

   public int getLocalHr(long var1) {
      this.recycledTime.timezone = this.mTzId;
      this.recycledTime.set(var1);
      return this.recycledTime.hour;
   }

   public String getLocalTime(long var1) {
      this.recycledTime.timezone = TimeZone.getDefault().getID();
      this.recycledTime.set(var1);
      int var3 = this.recycledTime.year;
      int var4 = this.recycledTime.yearDay;
      this.recycledTime.timezone = this.mTzId;
      this.recycledTime.set(var1);
      String var6 = null;
      int var5 = this.recycledTime.hour * 60 + this.recycledTime.minute;
      if (this.mLocalTimeCacheReferenceTime != var1) {
         this.mLocalTimeCacheReferenceTime = var1;
         this.mLocalTimeCache.clear();
      } else {
         var6 = (String)this.mLocalTimeCache.get(var5);
      }

      String var7 = var6;
      if (var6 == null) {
         var6 = "%I:%M %p";
         if (var3 * 366 + var4 != this.recycledTime.year * 366 + this.recycledTime.yearDay) {
            if (is24HourFormat) {
               var6 = "%b %d %H:%M";
            } else {
               var6 = "%b %d %I:%M %p";
            }
         } else if (is24HourFormat) {
            var6 = "%H:%M";
         }

         var7 = this.recycledTime.format(var6);
         this.mLocalTimeCache.put(var5, var7);
      }

      return var7;
   }

   public int getNowOffsetMillis() {
      return this.mTz.getOffset(System.currentTimeMillis());
   }

   public boolean hasSameRules(TimeZoneInfo var1) {
      return this.mRawoffset == var1.mRawoffset && Arrays.equals(this.mTransitions, var1.mTransitions);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      String var2 = this.mCountry;
      TimeZone var3 = this.mTz;
      var1.append(this.mTzId);
      var1.append(',');
      var1.append(var3.getDisplayName(false, 1));
      var1.append(',');
      var1.append(var3.getDisplayName(false, 0));
      var1.append(',');
      if (var3.useDaylightTime()) {
         var1.append(var3.getDisplayName(true, 1));
         var1.append(',');
         var1.append(var3.getDisplayName(true, 0));
      } else {
         var1.append(',');
      }

      var1.append(',');
      var1.append((float)var3.getRawOffset() / 3600000.0F);
      var1.append(',');
      var1.append((float)var3.getDSTSavings() / 3600000.0F);
      var1.append(',');
      var1.append(var2);
      var1.append(',');
      var1.append(this.getLocalTime(1357041600000L));
      var1.append(',');
      var1.append(this.getLocalTime(1363348800000L));
      var1.append(',');
      var1.append(this.getLocalTime(1372680000000L));
      var1.append(',');
      var1.append(this.getLocalTime(1383307200000L));
      var1.append(',');
      var1.append('\n');
      return var1.toString();
   }
}
