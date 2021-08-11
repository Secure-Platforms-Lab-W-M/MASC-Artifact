package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

@TargetApi(9)
@RequiresApi(9)
class TextViewCompatGingerbread {
   private static final int LINES = 1;
   private static final String LOG_TAG = "TextViewCompatGingerbread";
   private static Field sMaxModeField;
   private static boolean sMaxModeFieldFetched;
   private static Field sMaximumField;
   private static boolean sMaximumFieldFetched;
   private static Field sMinModeField;
   private static boolean sMinModeFieldFetched;
   private static Field sMinimumField;
   private static boolean sMinimumFieldFetched;

   static Drawable[] getCompoundDrawablesRelative(@NonNull TextView var0) {
      return var0.getCompoundDrawables();
   }

   static int getMaxLines(TextView var0) {
      if (!sMaxModeFieldFetched) {
         sMaxModeField = retrieveField("mMaxMode");
         sMaxModeFieldFetched = true;
      }

      if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, var0) == 1) {
         if (!sMaximumFieldFetched) {
            sMaximumField = retrieveField("mMaximum");
            sMaximumFieldFetched = true;
         }

         if (sMaximumField != null) {
            return retrieveIntFromField(sMaximumField, var0);
         }
      }

      return -1;
   }

   static int getMinLines(TextView var0) {
      if (!sMinModeFieldFetched) {
         sMinModeField = retrieveField("mMinMode");
         sMinModeFieldFetched = true;
      }

      if (sMinModeField != null && retrieveIntFromField(sMinModeField, var0) == 1) {
         if (!sMinimumFieldFetched) {
            sMinimumField = retrieveField("mMinimum");
            sMinimumFieldFetched = true;
         }

         if (sMinimumField != null) {
            return retrieveIntFromField(sMinimumField, var0);
         }
      }

      return -1;
   }

   private static Field retrieveField(String var0) {
      Field var1 = null;

      label25: {
         boolean var10001;
         Field var2;
         try {
            var2 = TextView.class.getDeclaredField(var0);
         } catch (NoSuchFieldException var4) {
            var10001 = false;
            break label25;
         }

         var1 = var2;

         try {
            var2.setAccessible(true);
            return var2;
         } catch (NoSuchFieldException var3) {
            var10001 = false;
         }
      }

      Log.e("TextViewCompatGingerbread", "Could not retrieve " + var0 + " field.");
      return var1;
   }

   private static int retrieveIntFromField(Field var0, TextView var1) {
      try {
         int var2 = var0.getInt(var1);
         return var2;
      } catch (IllegalAccessException var3) {
         Log.d("TextViewCompatGingerbread", "Could not retrieve value of " + var0.getName() + " field.");
         return -1;
      }
   }

   static void setTextAppearance(TextView var0, int var1) {
      var0.setTextAppearance(var0.getContext(), var1);
   }
}
