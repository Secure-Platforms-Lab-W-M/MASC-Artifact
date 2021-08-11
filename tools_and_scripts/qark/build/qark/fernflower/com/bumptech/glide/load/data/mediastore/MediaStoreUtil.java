package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;

public final class MediaStoreUtil {
   private static final int MINI_THUMB_HEIGHT = 384;
   private static final int MINI_THUMB_WIDTH = 512;

   private MediaStoreUtil() {
   }

   public static boolean isMediaStoreImageUri(Uri var0) {
      return isMediaStoreUri(var0) && !isVideoUri(var0);
   }

   public static boolean isMediaStoreUri(Uri var0) {
      return var0 != null && "content".equals(var0.getScheme()) && "media".equals(var0.getAuthority());
   }

   public static boolean isMediaStoreVideoUri(Uri var0) {
      return isMediaStoreUri(var0) && isVideoUri(var0);
   }

   public static boolean isThumbnailSize(int var0, int var1) {
      return var0 != Integer.MIN_VALUE && var1 != Integer.MIN_VALUE && var0 <= 512 && var1 <= 384;
   }

   private static boolean isVideoUri(Uri var0) {
      return var0.getPathSegments().contains("video");
   }
}
