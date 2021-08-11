package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public interface GifDecoder {
   int STATUS_FORMAT_ERROR = 1;
   int STATUS_OK = 0;
   int STATUS_OPEN_ERROR = 2;
   int STATUS_PARTIAL_DECODE = 3;
   int TOTAL_ITERATION_COUNT_FOREVER = 0;

   void advance();

   void clear();

   int getByteSize();

   int getCurrentFrameIndex();

   ByteBuffer getData();

   int getDelay(int var1);

   int getFrameCount();

   int getHeight();

   @Deprecated
   int getLoopCount();

   int getNetscapeLoopCount();

   int getNextDelay();

   Bitmap getNextFrame();

   int getStatus();

   int getTotalIterationCount();

   int getWidth();

   int read(InputStream var1, int var2);

   int read(byte[] var1);

   void resetFrameIndex();

   void setData(GifHeader var1, ByteBuffer var2);

   void setData(GifHeader var1, ByteBuffer var2, int var3);

   void setData(GifHeader var1, byte[] var2);

   void setDefaultBitmapConfig(Config var1);

   public interface BitmapProvider {
      Bitmap obtain(int var1, int var2, Config var3);

      byte[] obtainByteArray(int var1);

      int[] obtainIntArray(int var1);

      void release(Bitmap var1);

      void release(byte[] var1);

      void release(int[] var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface GifDecodeStatus {
   }
}
