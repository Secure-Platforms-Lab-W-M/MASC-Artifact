package com.bumptech.glide.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
   private static final String TAG = "ContentLengthStream";
   private static final int UNKNOWN = -1;
   private final long contentLength;
   private int readSoFar;

   private ContentLengthInputStream(InputStream var1, long var2) {
      super(var1);
      this.contentLength = var2;
   }

   private int checkReadSoFarOrThrow(int var1) throws IOException {
      if (var1 >= 0) {
         this.readSoFar += var1;
         return var1;
      } else if (this.contentLength - (long)this.readSoFar <= 0L) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to read all expected data, expected: ");
         var2.append(this.contentLength);
         var2.append(", but read: ");
         var2.append(this.readSoFar);
         throw new IOException(var2.toString());
      }
   }

   public static InputStream obtain(InputStream var0, long var1) {
      return new ContentLengthInputStream(var0, var1);
   }

   public static InputStream obtain(InputStream var0, String var1) {
      return obtain(var0, (long)parseContentLength(var1));
   }

   private static int parseContentLength(String var0) {
      if (!TextUtils.isEmpty(var0)) {
         try {
            int var1 = Integer.parseInt(var0);
            return var1;
         } catch (NumberFormatException var4) {
            if (Log.isLoggable("ContentLengthStream", 3)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("failed to parse content length header: ");
               var3.append(var0);
               Log.d("ContentLengthStream", var3.toString(), var4);
            }
         }
      }

      return -1;
   }

   public int available() throws IOException {
      synchronized(this){}
      boolean var6 = false;

      long var2;
      try {
         var6 = true;
         var2 = Math.max(this.contentLength - (long)this.readSoFar, (long)this.in.available());
         var6 = false;
      } finally {
         if (var6) {
            ;
         }
      }

      int var1 = (int)var2;
      return var1;
   }

   public int read() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label81: {
         boolean var10001;
         int var2;
         try {
            var2 = super.read();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label81;
         }

         byte var1;
         if (var2 >= 0) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         label72:
         try {
            this.checkReadSoFarOrThrow(var1);
            return var2;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label72;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this){}

      try {
         var2 = this.checkReadSoFarOrThrow(super.read(var1, var2, var3));
      } finally {
         ;
      }

      return var2;
   }
}
