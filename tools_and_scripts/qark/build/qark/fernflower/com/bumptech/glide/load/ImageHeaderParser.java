package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageHeaderParser {
   int UNKNOWN_ORIENTATION = -1;

   int getOrientation(InputStream var1, ArrayPool var2) throws IOException;

   int getOrientation(ByteBuffer var1, ArrayPool var2) throws IOException;

   ImageHeaderParser.ImageType getType(InputStream var1) throws IOException;

   ImageHeaderParser.ImageType getType(ByteBuffer var1) throws IOException;

   public static enum ImageType {
      GIF(true),
      JPEG(false),
      PNG(false),
      PNG_A(true),
      RAW(false),
      UNKNOWN,
      WEBP(false),
      WEBP_A(true);

      private final boolean hasAlpha;

      static {
         ImageHeaderParser.ImageType var0 = new ImageHeaderParser.ImageType("UNKNOWN", 7, false);
         UNKNOWN = var0;
      }

      private ImageType(boolean var3) {
         this.hasAlpha = var3;
      }

      public boolean hasAlpha() {
         return this.hasAlpha;
      }
   }
}
