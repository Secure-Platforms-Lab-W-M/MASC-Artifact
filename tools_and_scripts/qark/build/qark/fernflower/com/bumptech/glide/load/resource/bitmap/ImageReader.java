package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

interface ImageReader {
   Bitmap decodeBitmap(Options var1) throws IOException;

   int getImageOrientation() throws IOException;

   ImageHeaderParser.ImageType getImageType() throws IOException;

   void stopGrowingBuffers();

   public static final class InputStreamImageReader implements ImageReader {
      private final ArrayPool byteArrayPool;
      private final InputStreamRewinder dataRewinder;
      private final List parsers;

      InputStreamImageReader(InputStream var1, List var2, ArrayPool var3) {
         this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(var3);
         this.parsers = (List)Preconditions.checkNotNull(var2);
         this.dataRewinder = new InputStreamRewinder(var1, var3);
      }

      public Bitmap decodeBitmap(Options var1) throws IOException {
         return BitmapFactory.decodeStream(this.dataRewinder.rewindAndGet(), (Rect)null, var1);
      }

      public int getImageOrientation() throws IOException {
         return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
      }

      public ImageHeaderParser.ImageType getImageType() throws IOException {
         return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
      }

      public void stopGrowingBuffers() {
         this.dataRewinder.fixMarkLimits();
      }
   }

   public static final class ParcelFileDescriptorImageReader implements ImageReader {
      private final ArrayPool byteArrayPool;
      private final ParcelFileDescriptorRewinder dataRewinder;
      private final List parsers;

      ParcelFileDescriptorImageReader(ParcelFileDescriptor var1, List var2, ArrayPool var3) {
         this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(var3);
         this.parsers = (List)Preconditions.checkNotNull(var2);
         this.dataRewinder = new ParcelFileDescriptorRewinder(var1);
      }

      public Bitmap decodeBitmap(Options var1) throws IOException {
         return BitmapFactory.decodeFileDescriptor(this.dataRewinder.rewindAndGet().getFileDescriptor(), (Rect)null, var1);
      }

      public int getImageOrientation() throws IOException {
         return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder, this.byteArrayPool);
      }

      public ImageHeaderParser.ImageType getImageType() throws IOException {
         return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder, this.byteArrayPool);
      }

      public void stopGrowingBuffers() {
      }
   }
}
