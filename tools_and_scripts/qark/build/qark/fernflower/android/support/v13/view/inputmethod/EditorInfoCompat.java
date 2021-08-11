package android.support.v13.view.inputmethod;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
   public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
   private static final EditorInfoCompat.EditorInfoCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 25) {
         IMPL = new EditorInfoCompat.EditorInfoCompatApi25Impl();
      } else {
         IMPL = new EditorInfoCompat.EditorInfoCompatBaseImpl();
      }
   }

   @NonNull
   public static String[] getContentMimeTypes(EditorInfo var0) {
      return IMPL.getContentMimeTypes(var0);
   }

   public static void setContentMimeTypes(@NonNull EditorInfo var0, @Nullable String[] var1) {
      IMPL.setContentMimeTypes(var0, var1);
   }

   @RequiresApi(25)
   private static final class EditorInfoCompatApi25Impl implements EditorInfoCompat.EditorInfoCompatImpl {
      private EditorInfoCompatApi25Impl() {
      }

      // $FF: synthetic method
      EditorInfoCompatApi25Impl(Object var1) {
         this();
      }

      @NonNull
      public String[] getContentMimeTypes(@NonNull EditorInfo var1) {
         String[] var2 = var1.contentMimeTypes;
         return var2 != null ? var2 : EditorInfoCompat.EMPTY_STRING_ARRAY;
      }

      public void setContentMimeTypes(@NonNull EditorInfo var1, @Nullable String[] var2) {
         var1.contentMimeTypes = var2;
      }
   }

   private static final class EditorInfoCompatBaseImpl implements EditorInfoCompat.EditorInfoCompatImpl {
      private static String CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";

      private EditorInfoCompatBaseImpl() {
      }

      // $FF: synthetic method
      EditorInfoCompatBaseImpl(Object var1) {
         this();
      }

      @NonNull
      public String[] getContentMimeTypes(@NonNull EditorInfo var1) {
         if (var1.extras == null) {
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
         } else {
            String[] var2 = var1.extras.getStringArray(CONTENT_MIME_TYPES_KEY);
            return var2 != null ? var2 : EditorInfoCompat.EMPTY_STRING_ARRAY;
         }
      }

      public void setContentMimeTypes(@NonNull EditorInfo var1, @Nullable String[] var2) {
         if (var1.extras == null) {
            var1.extras = new Bundle();
         }

         var1.extras.putStringArray(CONTENT_MIME_TYPES_KEY, var2);
      }
   }

   private interface EditorInfoCompatImpl {
      @NonNull
      String[] getContentMimeTypes(@NonNull EditorInfo var1);

      void setContentMimeTypes(@NonNull EditorInfo var1, @Nullable String[] var2);
   }
}
