package com.yalantis.ucrop.util;

import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ImageHeaderParser {
   private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
   private static final int EXIF_MAGIC_NUMBER = 65496;
   private static final int EXIF_SEGMENT_TYPE = 225;
   private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
   private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
   private static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\u0000\u0000".getBytes(Charset.forName("UTF-8"));
   private static final int MARKER_EOI = 217;
   private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
   private static final int ORIENTATION_TAG_TYPE = 274;
   private static final int SEGMENT_SOS = 218;
   private static final int SEGMENT_START_ID = 255;
   private static final String TAG = "ImageHeaderParser";
   public static final int UNKNOWN_ORIENTATION = -1;
   private final ImageHeaderParser.Reader reader;

   public ImageHeaderParser(InputStream var1) {
      this.reader = new ImageHeaderParser.StreamReader(var1);
   }

   private static int calcTagOffset(int var0, int var1) {
      return var0 + 2 + var1 * 12;
   }

   public static void copyExif(ExifInterface var0, int var1, int var2, String var3) {
      String[] var6 = new String[]{"FNumber", "DateTime", "DateTimeDigitized", "ExposureTime", "Flash", "FocalLength", "GPSAltitude", "GPSAltitudeRef", "GPSDateStamp", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "GPSProcessingMethod", "GPSTimeStamp", "ISOSpeedRatings", "Make", "Model", "SubSecTime", "SubSecTimeDigitized", "SubSecTimeOriginal", "WhiteBalance"};

      IOException var12;
      label48: {
         int var5;
         ExifInterface var13;
         try {
            var13 = new ExifInterface(var3);
            var5 = var6.length;
         } catch (IOException var11) {
            var12 = var11;
            break label48;
         }

         IOException var10000;
         label39: {
            boolean var10001;
            for(int var4 = 0; var4 < var5; ++var4) {
               String var7 = var6[var4];

               try {
                  String var8 = var0.getAttribute(var7);
                  if (!TextUtils.isEmpty(var8)) {
                     var13.setAttribute(var7, var8);
                  }
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label39;
               }
            }

            try {
               var13.setAttribute("ImageWidth", String.valueOf(var1));
               var13.setAttribute("ImageLength", String.valueOf(var2));
               var13.setAttribute("Orientation", "0");
               var13.saveAttributes();
               return;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
            }
         }

         var12 = var10000;
      }

      Log.d("ImageHeaderParser", var12.getMessage());
   }

   private static boolean handles(int var0) {
      return (var0 & '\uffd8') == 65496 || var0 == 19789 || var0 == 18761;
   }

   private boolean hasJpegExifPreamble(byte[] var1, int var2) {
      boolean var3;
      if (var1 != null && var2 > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var2 = 0;

         while(true) {
            byte[] var4 = JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
            if (var2 >= var4.length) {
               break;
            }

            if (var1[var2] != var4[var2]) {
               return false;
            }

            ++var2;
         }
      }

      return var3;
   }

   private int moveToExifSegmentAndGetLength() throws IOException {
      while(true) {
         short var1 = this.reader.getUInt8();
         StringBuilder var5;
         if (var1 != 255) {
            if (Log.isLoggable("ImageHeaderParser", 3)) {
               var5 = new StringBuilder();
               var5.append("Unknown segmentId=");
               var5.append(var1);
               Log.d("ImageHeaderParser", var5.toString());
            }

            return -1;
         }

         var1 = this.reader.getUInt8();
         if (var1 == 218) {
            return -1;
         }

         if (var1 == 217) {
            if (Log.isLoggable("ImageHeaderParser", 3)) {
               Log.d("ImageHeaderParser", "Found MARKER_EOI in exif segment");
            }

            return -1;
         }

         int var2 = this.reader.getUInt16() - 2;
         if (var1 != 225) {
            long var3 = this.reader.skip((long)var2);
            if (var3 == (long)var2) {
               continue;
            }

            if (Log.isLoggable("ImageHeaderParser", 3)) {
               var5 = new StringBuilder();
               var5.append("Unable to skip enough data, type: ");
               var5.append(var1);
               var5.append(", wanted to skip: ");
               var5.append(var2);
               var5.append(", but actually skipped: ");
               var5.append(var3);
               Log.d("ImageHeaderParser", var5.toString());
            }

            return -1;
         }

         return var2;
      }
   }

   private static int parseExifSegment(ImageHeaderParser.RandomAccessReader var0) {
      int var1 = "Exif\u0000\u0000".length();
      short var2 = var0.getInt16(var1);
      ByteOrder var8;
      StringBuilder var11;
      if (var2 == 19789) {
         var8 = ByteOrder.BIG_ENDIAN;
      } else if (var2 == 18761) {
         var8 = ByteOrder.LITTLE_ENDIAN;
      } else {
         if (Log.isLoggable("ImageHeaderParser", 3)) {
            var11 = new StringBuilder();
            var11.append("Unknown endianness = ");
            var11.append(var2);
            Log.d("ImageHeaderParser", var11.toString());
         }

         var8 = ByteOrder.BIG_ENDIAN;
      }

      var0.order(var8);
      int var10 = var0.getInt32(var1 + 4) + var1;
      short var3 = var0.getInt16(var10);

      for(var1 = 0; var1 < var3; ++var1) {
         int var5 = calcTagOffset(var10, var1);
         short var4 = var0.getInt16(var5);
         if (var4 == 274) {
            short var6 = var0.getInt16(var5 + 2);
            if (var6 >= 1 && var6 <= 12) {
               int var7 = var0.getInt32(var5 + 4);
               if (var7 < 0) {
                  if (Log.isLoggable("ImageHeaderParser", 3)) {
                     Log.d("ImageHeaderParser", "Negative tiff component count");
                  }
               } else {
                  if (Log.isLoggable("ImageHeaderParser", 3)) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("Got tagIndex=");
                     var9.append(var1);
                     var9.append(" tagType=");
                     var9.append(var4);
                     var9.append(" formatCode=");
                     var9.append(var6);
                     var9.append(" componentCount=");
                     var9.append(var7);
                     Log.d("ImageHeaderParser", var9.toString());
                  }

                  var7 += BYTES_PER_FORMAT[var6];
                  if (var7 > 4) {
                     if (Log.isLoggable("ImageHeaderParser", 3)) {
                        var11 = new StringBuilder();
                        var11.append("Got byte count > 4, not orientation, continuing, formatCode=");
                        var11.append(var6);
                        Log.d("ImageHeaderParser", var11.toString());
                     }
                  } else {
                     var5 += 8;
                     if (var5 >= 0 && var5 <= var0.length()) {
                        if (var7 >= 0 && var5 + var7 <= var0.length()) {
                           return var0.getInt16(var5);
                        }

                        if (Log.isLoggable("ImageHeaderParser", 3)) {
                           var11 = new StringBuilder();
                           var11.append("Illegal number of bytes for TI tag data tagType=");
                           var11.append(var4);
                           Log.d("ImageHeaderParser", var11.toString());
                        }
                     } else if (Log.isLoggable("ImageHeaderParser", 3)) {
                        var11 = new StringBuilder();
                        var11.append("Illegal tagValueOffset=");
                        var11.append(var5);
                        var11.append(" tagType=");
                        var11.append(var4);
                        Log.d("ImageHeaderParser", var11.toString());
                     }
                  }
               }
            } else if (Log.isLoggable("ImageHeaderParser", 3)) {
               var11 = new StringBuilder();
               var11.append("Got invalid format code = ");
               var11.append(var6);
               Log.d("ImageHeaderParser", var11.toString());
            }
         }
      }

      return -1;
   }

   private int parseExifSegment(byte[] var1, int var2) throws IOException {
      int var3 = this.reader.read(var1, var2);
      if (var3 != var2) {
         if (Log.isLoggable("ImageHeaderParser", 3)) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Unable to read exif segment data, length: ");
            var4.append(var2);
            var4.append(", actually read: ");
            var4.append(var3);
            Log.d("ImageHeaderParser", var4.toString());
         }

         return -1;
      } else if (this.hasJpegExifPreamble(var1, var2)) {
         return parseExifSegment(new ImageHeaderParser.RandomAccessReader(var1, var2));
      } else {
         if (Log.isLoggable("ImageHeaderParser", 3)) {
            Log.d("ImageHeaderParser", "Missing jpeg exif preamble");
         }

         return -1;
      }
   }

   public int getOrientation() throws IOException {
      int var1 = this.reader.getUInt16();
      if (!handles(var1)) {
         if (Log.isLoggable("ImageHeaderParser", 3)) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Parser doesn't handle magic number: ");
            var2.append(var1);
            Log.d("ImageHeaderParser", var2.toString());
         }

         return -1;
      } else {
         var1 = this.moveToExifSegmentAndGetLength();
         if (var1 == -1) {
            if (Log.isLoggable("ImageHeaderParser", 3)) {
               Log.d("ImageHeaderParser", "Failed to parse exif segment length, or exif segment not found");
            }

            return -1;
         } else {
            return this.parseExifSegment(new byte[var1], var1);
         }
      }
   }

   private static class RandomAccessReader {
      private final ByteBuffer data;

      public RandomAccessReader(byte[] var1, int var2) {
         this.data = (ByteBuffer)ByteBuffer.wrap(var1).order(ByteOrder.BIG_ENDIAN).limit(var2);
      }

      public short getInt16(int var1) {
         return this.data.getShort(var1);
      }

      public int getInt32(int var1) {
         return this.data.getInt(var1);
      }

      public int length() {
         return this.data.remaining();
      }

      public void order(ByteOrder var1) {
         this.data.order(var1);
      }
   }

   private interface Reader {
      int getUInt16() throws IOException;

      short getUInt8() throws IOException;

      int read(byte[] var1, int var2) throws IOException;

      long skip(long var1) throws IOException;
   }

   private static class StreamReader implements ImageHeaderParser.Reader {
      // $FF: renamed from: is java.io.InputStream
      private final InputStream field_235;

      public StreamReader(InputStream var1) {
         this.field_235 = var1;
      }

      public int getUInt16() throws IOException {
         return this.field_235.read() << 8 & '\uff00' | this.field_235.read() & 255;
      }

      public short getUInt8() throws IOException {
         return (short)(this.field_235.read() & 255);
      }

      public int read(byte[] var1, int var2) throws IOException {
         int var3;
         int var4;
         for(var3 = var2; var3 > 0; var3 -= var4) {
            var4 = this.field_235.read(var1, var2 - var3, var3);
            if (var4 == -1) {
               break;
            }
         }

         return var2 - var3;
      }

      public long skip(long var1) throws IOException {
         if (var1 < 0L) {
            return 0L;
         } else {
            long var3 = var1;

            while(var3 > 0L) {
               long var5 = this.field_235.skip(var3);
               if (var5 > 0L) {
                  var3 -= var5;
               } else {
                  if (this.field_235.read() == -1) {
                     break;
                  }

                  --var3;
               }
            }

            return var1 - var3;
         }
      }
   }
}
