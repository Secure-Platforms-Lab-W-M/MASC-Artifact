package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
   private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
   static final int EXIF_MAGIC_NUMBER = 65496;
   static final int EXIF_SEGMENT_TYPE = 225;
   private static final int GIF_HEADER = 4671814;
   private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
   private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
   static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\u0000\u0000".getBytes(Charset.forName("UTF-8"));
   private static final int MARKER_EOI = 217;
   private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
   private static final int ORIENTATION_TAG_TYPE = 274;
   private static final int PNG_HEADER = -1991225785;
   private static final int RIFF_HEADER = 1380533830;
   private static final int SEGMENT_SOS = 218;
   static final int SEGMENT_START_ID = 255;
   private static final String TAG = "DfltImageHeaderParser";
   private static final int VP8_HEADER = 1448097792;
   private static final int VP8_HEADER_MASK = -256;
   private static final int VP8_HEADER_TYPE_EXTENDED = 88;
   private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
   private static final int VP8_HEADER_TYPE_MASK = 255;
   private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
   private static final int WEBP_HEADER = 1464156752;
   private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;

   private static int calcTagOffset(int var0, int var1) {
      return var0 + 2 + var1 * 12;
   }

   private int getOrientation(DefaultImageHeaderParser.Reader var1, ArrayPool var2) throws IOException {
      boolean var10001;
      int var3;
      boolean var4;
      try {
         var3 = var1.getUInt16();
         var4 = handles(var3);
      } catch (DefaultImageHeaderParser.Reader.EndOfFileException var20) {
         var10001 = false;
         return -1;
      }

      if (!var4) {
         try {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               StringBuilder var21 = new StringBuilder();
               var21.append("Parser doesn't handle magic number: ");
               var21.append(var3);
               Log.d("DfltImageHeaderParser", var21.toString());
               return -1;
            }

            return -1;
         } catch (DefaultImageHeaderParser.Reader.EndOfFileException var15) {
            var10001 = false;
         }
      } else {
         try {
            var3 = this.moveToExifSegmentAndGetLength(var1);
         } catch (DefaultImageHeaderParser.Reader.EndOfFileException var19) {
            var10001 = false;
            return -1;
         }

         if (var3 == -1) {
            try {
               if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                  Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found");
                  return -1;
               }

               return -1;
            } catch (DefaultImageHeaderParser.Reader.EndOfFileException var16) {
               var10001 = false;
            }
         } else {
            byte[] var5;
            try {
               var5 = (byte[])var2.get(var3, byte[].class);
            } catch (DefaultImageHeaderParser.Reader.EndOfFileException var18) {
               var10001 = false;
               return -1;
            }

            try {
               var3 = this.parseExifSegment(var1, var5, var3);
            } finally {
               try {
                  var2.put(var5);
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var14) {
                  var10001 = false;
                  return -1;
               }
            }

            return var3;
         }
      }

      return -1;
   }

   private ImageHeaderParser.ImageType getType(DefaultImageHeaderParser.Reader var1) throws IOException {
      boolean var10001;
      int var2;
      try {
         var2 = var1.getUInt16();
      } catch (DefaultImageHeaderParser.Reader.EndOfFileException var16) {
         var10001 = false;
         return ImageHeaderParser.ImageType.UNKNOWN;
      }

      if (var2 == 65496) {
         try {
            return ImageHeaderParser.ImageType.JPEG;
         } catch (DefaultImageHeaderParser.Reader.EndOfFileException var4) {
            var10001 = false;
         }
      } else {
         try {
            var2 = var2 << 8 | var1.getUInt8();
         } catch (DefaultImageHeaderParser.Reader.EndOfFileException var15) {
            var10001 = false;
            return ImageHeaderParser.ImageType.UNKNOWN;
         }

         if (var2 == 4671814) {
            try {
               return ImageHeaderParser.ImageType.GIF;
            } catch (DefaultImageHeaderParser.Reader.EndOfFileException var5) {
               var10001 = false;
            }
         } else {
            try {
               var2 = var2 << 8 | var1.getUInt8();
            } catch (DefaultImageHeaderParser.Reader.EndOfFileException var14) {
               var10001 = false;
               return ImageHeaderParser.ImageType.UNKNOWN;
            }

            ImageHeaderParser.ImageType var21;
            if (var2 == -1991225785) {
               try {
                  var1.skip(21L);
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var12) {
                  var10001 = false;
                  return ImageHeaderParser.ImageType.UNKNOWN;
               }

               label109: {
                  try {
                     if (var1.getUInt8() >= 3) {
                        return ImageHeaderParser.ImageType.PNG_A;
                     }
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var17) {
                     var10001 = false;
                     break label109;
                  }

                  try {
                     var21 = ImageHeaderParser.ImageType.PNG;
                     return var21;
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var6) {
                     var10001 = false;
                  }
               }

               try {
                  return ImageHeaderParser.ImageType.PNG;
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var3) {
                  var10001 = false;
               }
            } else if (var2 != 1380533830) {
               try {
                  return ImageHeaderParser.ImageType.UNKNOWN;
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var7) {
                  var10001 = false;
               }
            } else {
               try {
                  var1.skip(4L);
                  if ((var1.getUInt16() << 16 | var1.getUInt16()) != 1464156752) {
                     return ImageHeaderParser.ImageType.UNKNOWN;
                  }
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var20) {
                  var10001 = false;
                  return ImageHeaderParser.ImageType.UNKNOWN;
               }

               try {
                  var2 = var1.getUInt16() << 16 | var1.getUInt16();
               } catch (DefaultImageHeaderParser.Reader.EndOfFileException var13) {
                  var10001 = false;
                  return ImageHeaderParser.ImageType.UNKNOWN;
               }

               if ((var2 & -256) != 1448097792) {
                  try {
                     return ImageHeaderParser.ImageType.UNKNOWN;
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var8) {
                     var10001 = false;
                  }
               } else if ((var2 & 255) == 88) {
                  try {
                     var1.skip(4L);
                     if ((var1.getUInt8() & 16) != 0) {
                        return ImageHeaderParser.ImageType.WEBP_A;
                     }
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var18) {
                     var10001 = false;
                     return ImageHeaderParser.ImageType.UNKNOWN;
                  }

                  try {
                     return ImageHeaderParser.ImageType.WEBP;
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var9) {
                     var10001 = false;
                  }
               } else if ((var2 & 255) != 76) {
                  try {
                     var21 = ImageHeaderParser.ImageType.WEBP;
                     return var21;
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var11) {
                     var10001 = false;
                  }
               } else {
                  try {
                     var1.skip(4L);
                     if ((var1.getUInt8() & 8) != 0) {
                        return ImageHeaderParser.ImageType.WEBP_A;
                     }
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var19) {
                     var10001 = false;
                     return ImageHeaderParser.ImageType.UNKNOWN;
                  }

                  try {
                     return ImageHeaderParser.ImageType.WEBP;
                  } catch (DefaultImageHeaderParser.Reader.EndOfFileException var10) {
                     var10001 = false;
                  }
               }
            }
         }
      }

      return ImageHeaderParser.ImageType.UNKNOWN;
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

   private int moveToExifSegmentAndGetLength(DefaultImageHeaderParser.Reader var1) throws IOException {
      while(true) {
         short var2 = var1.getUInt8();
         StringBuilder var6;
         if (var2 != 255) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var6 = new StringBuilder();
               var6.append("Unknown segmentId=");
               var6.append(var2);
               Log.d("DfltImageHeaderParser", var6.toString());
            }

            return -1;
         }

         var2 = var1.getUInt8();
         if (var2 == 218) {
            return -1;
         }

         if (var2 == 217) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment");
            }

            return -1;
         }

         int var3 = var1.getUInt16() - 2;
         if (var2 != 225) {
            long var4 = var1.skip((long)var3);
            if (var4 == (long)var3) {
               continue;
            }

            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var6 = new StringBuilder();
               var6.append("Unable to skip enough data, type: ");
               var6.append(var2);
               var6.append(", wanted to skip: ");
               var6.append(var3);
               var6.append(", but actually skipped: ");
               var6.append(var4);
               Log.d("DfltImageHeaderParser", var6.toString());
            }

            return -1;
         }

         return var3;
      }
   }

   private static int parseExifSegment(DefaultImageHeaderParser.RandomAccessReader var0) {
      int var1 = "Exif\u0000\u0000".length();
      short var2 = var0.getInt16(var1);
      StringBuilder var8;
      ByteOrder var11;
      if (var2 != 18761) {
         if (var2 != 19789) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var8 = new StringBuilder();
               var8.append("Unknown endianness = ");
               var8.append(var2);
               Log.d("DfltImageHeaderParser", var8.toString());
            }

            var11 = ByteOrder.BIG_ENDIAN;
         } else {
            var11 = ByteOrder.BIG_ENDIAN;
         }
      } else {
         var11 = ByteOrder.LITTLE_ENDIAN;
      }

      var0.order(var11);
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
                  if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                     Log.d("DfltImageHeaderParser", "Negative tiff component count");
                  }
               } else {
                  if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("Got tagIndex=");
                     var9.append(var1);
                     var9.append(" tagType=");
                     var9.append(var4);
                     var9.append(" formatCode=");
                     var9.append(var6);
                     var9.append(" componentCount=");
                     var9.append(var7);
                     Log.d("DfltImageHeaderParser", var9.toString());
                  }

                  var7 += BYTES_PER_FORMAT[var6];
                  if (var7 > 4) {
                     if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                        var8 = new StringBuilder();
                        var8.append("Got byte count > 4, not orientation, continuing, formatCode=");
                        var8.append(var6);
                        Log.d("DfltImageHeaderParser", var8.toString());
                     }
                  } else {
                     var5 += 8;
                     if (var5 >= 0 && var5 <= var0.length()) {
                        if (var7 >= 0 && var5 + var7 <= var0.length()) {
                           return var0.getInt16(var5);
                        }

                        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                           var8 = new StringBuilder();
                           var8.append("Illegal number of bytes for TI tag data tagType=");
                           var8.append(var4);
                           Log.d("DfltImageHeaderParser", var8.toString());
                        }
                     } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                        var8 = new StringBuilder();
                        var8.append("Illegal tagValueOffset=");
                        var8.append(var5);
                        var8.append(" tagType=");
                        var8.append(var4);
                        Log.d("DfltImageHeaderParser", var8.toString());
                     }
                  }
               }
            } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var8 = new StringBuilder();
               var8.append("Got invalid format code = ");
               var8.append(var6);
               Log.d("DfltImageHeaderParser", var8.toString());
            }
         }
      }

      return -1;
   }

   private int parseExifSegment(DefaultImageHeaderParser.Reader var1, byte[] var2, int var3) throws IOException {
      int var4 = var1.read(var2, var3);
      if (var4 != var3) {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Unable to read exif segment data, length: ");
            var5.append(var3);
            var5.append(", actually read: ");
            var5.append(var4);
            Log.d("DfltImageHeaderParser", var5.toString());
         }

         return -1;
      } else if (this.hasJpegExifPreamble(var2, var3)) {
         return parseExifSegment(new DefaultImageHeaderParser.RandomAccessReader(var2, var3));
      } else {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble");
         }

         return -1;
      }
   }

   public int getOrientation(InputStream var1, ArrayPool var2) throws IOException {
      return this.getOrientation((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.StreamReader((InputStream)Preconditions.checkNotNull(var1))), (ArrayPool)Preconditions.checkNotNull(var2));
   }

   public int getOrientation(ByteBuffer var1, ArrayPool var2) throws IOException {
      return this.getOrientation((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(var1))), (ArrayPool)Preconditions.checkNotNull(var2));
   }

   public ImageHeaderParser.ImageType getType(InputStream var1) throws IOException {
      return this.getType((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.StreamReader((InputStream)Preconditions.checkNotNull(var1))));
   }

   public ImageHeaderParser.ImageType getType(ByteBuffer var1) throws IOException {
      return this.getType((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(var1))));
   }

   private static final class ByteBufferReader implements DefaultImageHeaderParser.Reader {
      private final ByteBuffer byteBuffer;

      ByteBufferReader(ByteBuffer var1) {
         this.byteBuffer = var1;
         var1.order(ByteOrder.BIG_ENDIAN);
      }

      public int getUInt16() throws DefaultImageHeaderParser.Reader.EndOfFileException {
         return this.getUInt8() << 8 | this.getUInt8();
      }

      public short getUInt8() throws DefaultImageHeaderParser.Reader.EndOfFileException {
         if (this.byteBuffer.remaining() >= 1) {
            return (short)(this.byteBuffer.get() & 255);
         } else {
            throw new DefaultImageHeaderParser.Reader.EndOfFileException();
         }
      }

      public int read(byte[] var1, int var2) {
         var2 = Math.min(var2, this.byteBuffer.remaining());
         if (var2 == 0) {
            return -1;
         } else {
            this.byteBuffer.get(var1, 0, var2);
            return var2;
         }
      }

      public long skip(long var1) {
         int var3 = (int)Math.min((long)this.byteBuffer.remaining(), var1);
         ByteBuffer var4 = this.byteBuffer;
         var4.position(var4.position() + var3);
         return (long)var3;
      }
   }

   private static final class RandomAccessReader {
      private final ByteBuffer data;

      RandomAccessReader(byte[] var1, int var2) {
         this.data = (ByteBuffer)ByteBuffer.wrap(var1).order(ByteOrder.BIG_ENDIAN).limit(var2);
      }

      private boolean isAvailable(int var1, int var2) {
         return this.data.remaining() - var1 >= var2;
      }

      short getInt16(int var1) {
         return this.isAvailable(var1, 2) ? this.data.getShort(var1) : -1;
      }

      int getInt32(int var1) {
         return this.isAvailable(var1, 4) ? this.data.getInt(var1) : -1;
      }

      int length() {
         return this.data.remaining();
      }

      void order(ByteOrder var1) {
         this.data.order(var1);
      }
   }

   private interface Reader {
      int getUInt16() throws IOException;

      short getUInt8() throws IOException;

      int read(byte[] var1, int var2) throws IOException;

      long skip(long var1) throws IOException;

      public static final class EndOfFileException extends IOException {
         private static final long serialVersionUID = 1L;

         EndOfFileException() {
            super("Unexpectedly reached end of a file");
         }
      }
   }

   private static final class StreamReader implements DefaultImageHeaderParser.Reader {
      // $FF: renamed from: is java.io.InputStream
      private final InputStream field_237;

      StreamReader(InputStream var1) {
         this.field_237 = var1;
      }

      public int getUInt16() throws IOException {
         return this.getUInt8() << 8 | this.getUInt8();
      }

      public short getUInt8() throws IOException {
         int var1 = this.field_237.read();
         if (var1 != -1) {
            return (short)var1;
         } else {
            throw new DefaultImageHeaderParser.Reader.EndOfFileException();
         }
      }

      public int read(byte[] var1, int var2) throws IOException {
         int var4 = 0;
         int var3 = 0;

         int var5;
         while(true) {
            var5 = var3;
            if (var4 >= var2) {
               break;
            }

            int var6 = this.field_237.read(var1, var4, var2 - var4);
            var3 = var6;
            var5 = var6;
            if (var6 == -1) {
               break;
            }

            var4 += var6;
         }

         if (var4 == 0) {
            if (var5 != -1) {
               return var4;
            } else {
               throw new DefaultImageHeaderParser.Reader.EndOfFileException();
            }
         } else {
            return var4;
         }
      }

      public long skip(long var1) throws IOException {
         if (var1 < 0L) {
            return 0L;
         } else {
            long var3 = var1;

            while(var3 > 0L) {
               long var5 = this.field_237.skip(var3);
               if (var5 > 0L) {
                  var3 -= var5;
               } else {
                  if (this.field_237.read() == -1) {
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
