package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream extends FilterInputStream {
   private static final byte[] EXIF_SEGMENT;
   private static final int ORIENTATION_POSITION;
   private static final int SEGMENT_LENGTH;
   private static final int SEGMENT_START_POSITION = 2;
   private final byte orientation;
   private int position;

   static {
      byte[] var1 = new byte[]{-1, -31, 0, 28, 69, 120, 105, 102, 0, 0, 77, 77, 0, 0, 0, 0, 0, 8, 0, 1, 1, 18, 0, 2, 0, 0, 0, 1, 0};
      EXIF_SEGMENT = var1;
      int var0 = var1.length;
      SEGMENT_LENGTH = var0;
      ORIENTATION_POSITION = var0 + 2;
   }

   public ExifOrientationStream(InputStream var1, int var2) {
      super(var1);
      if (var2 >= -1 && var2 <= 8) {
         this.orientation = (byte)var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Cannot add invalid orientation: ");
         var3.append(var2);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void mark(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      int var1;
      label20: {
         var1 = this.position;
         if (var1 >= 2) {
            int var2 = ORIENTATION_POSITION;
            if (var1 <= var2) {
               if (var1 == var2) {
                  var1 = this.orientation;
               } else {
                  var1 = EXIF_SEGMENT[var1 - 2] & 255;
               }
               break label20;
            }
         }

         var1 = super.read();
      }

      if (var1 != -1) {
         ++this.position;
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.position;
      int var5 = ORIENTATION_POSITION;
      if (var4 > var5) {
         var2 = super.read(var1, var2, var3);
      } else if (var4 == var5) {
         var1[var2] = this.orientation;
         var2 = 1;
      } else if (var4 < 2) {
         var2 = super.read(var1, var2, 2 - var4);
      } else {
         var3 = Math.min(var5 - var4, var3);
         System.arraycopy(EXIF_SEGMENT, this.position - 2, var1, var2, var3);
         var2 = var3;
      }

      if (var2 > 0) {
         this.position += var2;
      }

      return var2;
   }

   public void reset() throws IOException {
      throw new UnsupportedOperationException();
   }

   public long skip(long var1) throws IOException {
      var1 = super.skip(var1);
      if (var1 > 0L) {
         this.position = (int)((long)this.position + var1);
      }

      return var1;
   }
}
