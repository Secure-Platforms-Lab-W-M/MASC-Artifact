package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import com.codetroopers.betterpickers.R.array;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZonePickerUtils {
   public static final int DST_SYMBOL_COLOR = -4210753;
   public static final int GMT_TEXT_COLOR = -7829368;
   private static final String TAG = "TimeZonePickerUtils";
   private static final Factory mSpannableFactory = Factory.getInstance();
   private Locale mDefaultLocale;
   private String[] mOverrideIds;
   private String[] mOverrideLabels;

   public TimeZonePickerUtils(Context var1) {
      this.cacheOverrides(var1);
   }

   public static void appendGmtOffset(StringBuilder var0, int var1) {
      var0.append("GMT");
      if (var1 < 0) {
         var0.append('-');
      } else {
         var0.append('+');
      }

      var1 = Math.abs(var1);
      var0.append((long)var1 / 3600000L);
      var1 = var1 / '\uea60' % 60;
      if (var1 != 0) {
         var0.append(':');
         if (var1 < 10) {
            var0.append('0');
         }

         var0.append(var1);
      }

   }

   private CharSequence buildGmtDisplayName(TimeZone var1, long var2, boolean var4) {
      Time var10 = new Time(var1.getID());
      var10.set(var2);
      StringBuilder var11 = new StringBuilder();
      boolean var9;
      if (var10.isDst != 0) {
         var9 = true;
      } else {
         var9 = false;
      }

      var11.append(this.getDisplayName(var1, var9));
      var11.append("  ");
      int var5 = var1.getOffset(var2);
      int var7 = var11.length();
      appendGmtOffset(var11, var5);
      int var8 = var11.length();
      var5 = 0;
      int var6 = 0;
      if (var1.useDaylightTime()) {
         var11.append(" ");
         var5 = var11.length();
         var11.append(getDstSymbol());
         var6 = var11.length();
      }

      Spannable var12 = mSpannableFactory.newSpannable(var11);
      if (var4) {
         var12.setSpan(new ForegroundColorSpan(-7829368), var7, var8, 33);
      }

      if (var1.useDaylightTime()) {
         var12.setSpan(new ForegroundColorSpan(-4210753), var5, var6, 33);
      }

      return var12;
   }

   private void cacheOverrides(Context var1) {
      Resources var2 = var1.getResources();
      this.mOverrideIds = var2.getStringArray(array.timezone_rename_ids);
      this.mOverrideLabels = var2.getStringArray(array.timezone_rename_labels);
   }

   private String getDisplayName(TimeZone var1, boolean var2) {
      if (this.mOverrideIds != null && this.mOverrideLabels != null) {
         for(int var3 = 0; var3 < this.mOverrideIds.length; ++var3) {
            if (var1.getID().equals(this.mOverrideIds[var3])) {
               String[] var4 = this.mOverrideLabels;
               if (var4.length > var3) {
                  return var4[var3];
               }

               StringBuilder var5 = new StringBuilder();
               var5.append("timezone_rename_ids len=");
               var5.append(this.mOverrideIds.length);
               var5.append(" timezone_rename_labels len=");
               var5.append(this.mOverrideLabels.length);
               Log.e("TimeZonePickerUtils", var5.toString());
               break;
            }
         }

         return var1.getDisplayName(var2, 1, Locale.getDefault());
      } else {
         return var1.getDisplayName(var2, 1, Locale.getDefault());
      }
   }

   public static char getDstSymbol() {
      return (char)(VERSION.SDK_INT >= 16 ? '☀' : '*');
   }

   public CharSequence getGmtDisplayName(Context var1, String var2, long var3, boolean var5) {
      TimeZone var7 = TimeZone.getTimeZone(var2);
      if (var7 == null) {
         return null;
      } else {
         Locale var6 = Locale.getDefault();
         if (!var6.equals(this.mDefaultLocale)) {
            this.mDefaultLocale = var6;
            this.cacheOverrides(var1);
         }

         return this.buildGmtDisplayName(var7, var3, var5);
      }
   }
}
