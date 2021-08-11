package androidx.core.view.inputmethod;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
   private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
   private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
   public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;

   public static String[] getContentMimeTypes(EditorInfo var0) {
      if (VERSION.SDK_INT >= 25) {
         String[] var3 = var0.contentMimeTypes;
         return var3 != null ? var3 : EMPTY_STRING_ARRAY;
      } else if (var0.extras == null) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] var2 = var0.extras.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
         String[] var1 = var2;
         if (var2 == null) {
            var1 = var0.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
         }

         return var1 != null ? var1 : EMPTY_STRING_ARRAY;
      }
   }

   static int getProtocol(EditorInfo var0) {
      if (VERSION.SDK_INT >= 25) {
         return 1;
      } else if (var0.extras == null) {
         return 0;
      } else {
         boolean var1 = var0.extras.containsKey("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
         boolean var2 = var0.extras.containsKey("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
         if (var1 && var2) {
            return 4;
         } else if (var1) {
            return 3;
         } else {
            return var2 ? 2 : 0;
         }
      }
   }

   public static void setContentMimeTypes(EditorInfo var0, String[] var1) {
      if (VERSION.SDK_INT >= 25) {
         var0.contentMimeTypes = var1;
      } else {
         if (var0.extras == null) {
            var0.extras = new Bundle();
         }

         var0.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", var1);
         var0.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", var1);
      }
   }
}
