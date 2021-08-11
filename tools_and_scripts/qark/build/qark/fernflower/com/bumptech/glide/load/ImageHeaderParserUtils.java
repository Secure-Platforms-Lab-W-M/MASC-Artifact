package com.bumptech.glide.load;

import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
   private static final int MARK_READ_LIMIT = 5242880;

   private ImageHeaderParserUtils() {
   }

   public static int getOrientation(List var0, final ParcelFileDescriptorRewinder var1, final ArrayPool var2) throws IOException {
      return getOrientationInternal(var0, new ImageHeaderParserUtils.OrientationReader() {
         public int getOrientation(ImageHeaderParser var1x) throws IOException {
            RecyclableBufferedInputStream var3 = null;

            int var2x;
            RecyclableBufferedInputStream var4;
            label108: {
               Throwable var10000;
               label109: {
                  boolean var10001;
                  try {
                     var4 = new RecyclableBufferedInputStream(new FileInputStream(var1.rewindAndGet().getFileDescriptor()), var2);
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label109;
                  }

                  var3 = var4;

                  label99:
                  try {
                     var2x = var1x.getOrientation((InputStream)var4, var2);
                     break label108;
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label99;
                  }
               }

               Throwable var17 = var10000;
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (IOException var13) {
                  }
               }

               var1.rewindAndGet();
               throw var17;
            }

            try {
               var4.close();
            } catch (IOException var14) {
            }

            var1.rewindAndGet();
            return var2x;
         }
      });
   }

   public static int getOrientation(List var0, InputStream var1, final ArrayPool var2) throws IOException {
      if (var1 == null) {
         return -1;
      } else {
         final Object var3 = var1;
         if (!var1.markSupported()) {
            var3 = new RecyclableBufferedInputStream(var1, var2);
         }

         ((InputStream)var3).mark(5242880);
         return getOrientationInternal(var0, new ImageHeaderParserUtils.OrientationReader() {
            public int getOrientation(ImageHeaderParser var1) throws IOException {
               int var2x;
               try {
                  var2x = var1.getOrientation((InputStream)var3, var2);
               } finally {
                  ((InputStream)var3).reset();
               }

               return var2x;
            }
         });
      }
   }

   private static int getOrientationInternal(List var0, ImageHeaderParserUtils.OrientationReader var1) throws IOException {
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         int var4 = var1.getOrientation((ImageHeaderParser)var0.get(var2));
         if (var4 != -1) {
            return var4;
         }
      }

      return -1;
   }

   public static ImageHeaderParser.ImageType getType(List var0, final ParcelFileDescriptorRewinder var1, final ArrayPool var2) throws IOException {
      return getTypeInternal(var0, new ImageHeaderParserUtils.TypeReader() {
         public ImageHeaderParser.ImageType getType(ImageHeaderParser var1x) throws IOException {
            RecyclableBufferedInputStream var2x = null;

            ImageHeaderParser.ImageType var17;
            RecyclableBufferedInputStream var3;
            label108: {
               Throwable var10000;
               label109: {
                  boolean var10001;
                  try {
                     var3 = new RecyclableBufferedInputStream(new FileInputStream(var1.rewindAndGet().getFileDescriptor()), var2);
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label109;
                  }

                  var2x = var3;

                  label99:
                  try {
                     var17 = var1x.getType((InputStream)var3);
                     break label108;
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label99;
                  }
               }

               Throwable var16 = var10000;
               if (var2x != null) {
                  try {
                     var2x.close();
                  } catch (IOException var12) {
                  }
               }

               var1.rewindAndGet();
               throw var16;
            }

            try {
               var3.close();
            } catch (IOException var13) {
            }

            var1.rewindAndGet();
            return var17;
         }
      });
   }

   public static ImageHeaderParser.ImageType getType(List var0, InputStream var1, ArrayPool var2) throws IOException {
      if (var1 == null) {
         return ImageHeaderParser.ImageType.UNKNOWN;
      } else {
         final Object var3 = var1;
         if (!var1.markSupported()) {
            var3 = new RecyclableBufferedInputStream(var1, var2);
         }

         ((InputStream)var3).mark(5242880);
         return getTypeInternal(var0, new ImageHeaderParserUtils.TypeReader() {
            public ImageHeaderParser.ImageType getType(ImageHeaderParser var1) throws IOException {
               ImageHeaderParser.ImageType var4;
               try {
                  var4 = var1.getType((InputStream)var3);
               } finally {
                  ((InputStream)var3).reset();
               }

               return var4;
            }
         });
      }
   }

   public static ImageHeaderParser.ImageType getType(List var0, final ByteBuffer var1) throws IOException {
      return var1 == null ? ImageHeaderParser.ImageType.UNKNOWN : getTypeInternal(var0, new ImageHeaderParserUtils.TypeReader() {
         public ImageHeaderParser.ImageType getType(ImageHeaderParser var1x) throws IOException {
            return var1x.getType(var1);
         }
      });
   }

   private static ImageHeaderParser.ImageType getTypeInternal(List var0, ImageHeaderParserUtils.TypeReader var1) throws IOException {
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         ImageHeaderParser.ImageType var4 = var1.getType((ImageHeaderParser)var0.get(var2));
         if (var4 != ImageHeaderParser.ImageType.UNKNOWN) {
            return var4;
         }
      }

      return ImageHeaderParser.ImageType.UNKNOWN;
   }

   private interface OrientationReader {
      int getOrientation(ImageHeaderParser var1) throws IOException;
   }

   private interface TypeReader {
      ImageHeaderParser.ImageType getType(ImageHeaderParser var1) throws IOException;
   }
}
