package androidx.core.widget;

import android.os.Build.VERSION;

public interface AutoSizeableTextView {
   boolean PLATFORM_SUPPORTS_AUTOSIZE;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 27) {
         var0 = true;
      } else {
         var0 = false;
      }

      PLATFORM_SUPPORTS_AUTOSIZE = var0;
   }

   int getAutoSizeMaxTextSize();

   int getAutoSizeMinTextSize();

   int getAutoSizeStepGranularity();

   int[] getAutoSizeTextAvailableSizes();

   int getAutoSizeTextType();

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException;

   void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException;

   void setAutoSizeTextTypeWithDefaults(int var1);
}
