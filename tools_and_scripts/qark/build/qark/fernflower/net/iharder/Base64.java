package net.iharder;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

public class Base64 {
   public static final int DECODE = 0;
   public static final int DONT_BREAK_LINES = 8;
   public static final int ENCODE = 1;
   private static final byte EQUALS_SIGN = 61;
   private static final byte EQUALS_SIGN_ENC = -1;
   public static final int GZIP = 2;
   private static final int MAX_LINE_LENGTH = 76;
   private static final byte NEW_LINE = 10;
   public static final int NO_OPTIONS = 0;
   public static final int ORDERED = 32;
   private static final String PREFERRED_ENCODING = "UTF-8";
   public static final int URL_SAFE = 16;
   private static final byte WHITE_SPACE_ENC = -5;
   private static final byte[] _ORDERED_ALPHABET = new byte[]{45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
   private static final byte[] _ORDERED_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9};
   private static final byte[] _STANDARD_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] _STANDARD_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
   private static final byte[] _URL_SAFE_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
   private static final byte[] _URL_SAFE_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};

   private Base64() {
   }

   public static byte[] decode(String var0) {
      return decode(var0, 0);
   }

   public static byte[] decode(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static byte[] decode(byte[] var0, int var1, int var2, int var3) {
      byte[] var12 = getDecodabet(var3);
      byte[] var11 = new byte[var2 * 3 / 4];
      int var5 = 0;
      byte[] var13 = new byte[4];
      int var8 = 0;
      int var7 = var1;

      int var6;
      while(true) {
         var6 = var5;
         if (var7 >= var1 + var2) {
            break;
         }

         byte var4 = (byte)(var0[var7] & 127);
         byte var10 = var12[var4];
         if (var10 < -5) {
            PrintStream var14 = System.err;
            StringBuilder var15 = new StringBuilder();
            var15.append("Bad Base64 input character at ");
            var15.append(var7);
            var15.append(": ");
            var15.append(var0[var7]);
            var15.append("(decimal)");
            var14.println(var15.toString());
            return null;
         }

         int var9 = var5;
         var6 = var8;
         if (var10 >= -1) {
            var6 = var8 + 1;
            var13[var8] = var4;
            if (var6 > 3) {
               var5 += decode4to3(var13, 0, var11, var5, var3);
               var6 = 0;
               var9 = var5;
               if (var4 == 61) {
                  var6 = var5;
                  break;
               }
            } else {
               var9 = var5;
            }
         }

         ++var7;
         var5 = var9;
         var8 = var6;
      }

      var0 = new byte[var6];
      System.arraycopy(var11, 0, var0, 0, var6);
      return var0;
   }

   private static int decode4to3(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      byte[] var5 = getDecodabet(var4);
      if (var0[var1 + 2] == 61) {
         var2[var3] = (byte)(((var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12) >>> 16);
         return 1;
      } else if (var0[var1 + 3] == 61) {
         var1 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6;
         var2[var3] = (byte)(var1 >>> 16);
         var2[var3 + 1] = (byte)(var1 >>> 8);
         return 2;
      } else {
         var1 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6 | var5[var0[var1 + 3]] & 255;
         var2[var3] = (byte)(var1 >> 16);
         var2[var3 + 1] = (byte)(var1 >> 8);
         var2[var3 + 2] = (byte)var1;
         return 3;
      }
   }

   public static boolean decodeFileToFile(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static byte[] decodeFromFile(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean decodeToFile(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static Object decodeToObject(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static byte[] encode3to4(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      byte[] var8 = getAlphabet(var5);
      int var7 = 0;
      if (var2 > 0) {
         var5 = var0[var1] << 24 >>> 8;
      } else {
         var5 = 0;
      }

      int var6;
      if (var2 > 1) {
         var6 = var0[var1 + 1] << 24 >>> 16;
      } else {
         var6 = 0;
      }

      if (var2 > 2) {
         var7 = var0[var1 + 2] << 24 >>> 24;
      }

      var1 = var7 | var5 | var6;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3) {
               return var3;
            } else {
               var3[var4] = var8[var1 >>> 18];
               var3[var4 + 1] = var8[var1 >>> 12 & 63];
               var3[var4 + 2] = var8[var1 >>> 6 & 63];
               var3[var4 + 3] = var8[var1 & 63];
               return var3;
            }
         } else {
            var3[var4] = var8[var1 >>> 18];
            var3[var4 + 1] = var8[var1 >>> 12 & 63];
            var3[var4 + 2] = var8[var1 >>> 6 & 63];
            var3[var4 + 3] = 61;
            return var3;
         }
      } else {
         var3[var4] = var8[var1 >>> 18];
         var3[var4 + 1] = var8[var1 >>> 12 & 63];
         var3[var4 + 2] = 61;
         var3[var4 + 3] = 61;
         return var3;
      }
   }

   private static byte[] encode3to4(byte[] var0, byte[] var1, int var2, int var3) {
      encode3to4(var1, 0, var2, var0, 0, var3);
      return var0;
   }

   public static String encodeBytes(byte[] var0) {
      return encodeBytes(var0, 0, var0.length, 0);
   }

   public static String encodeBytes(byte[] var0, int var1) {
      return encodeBytes(var0, 0, var0.length, var1);
   }

   public static String encodeBytes(byte[] var0, int var1, int var2) {
      return encodeBytes(var0, var1, var2, 0);
   }

   public static String encodeBytes(byte[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static boolean encodeFileToFile(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static String encodeFromFile(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static String encodeObject(Serializable var0) {
      return encodeObject(var0, 0);
   }

   public static String encodeObject(Serializable param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean encodeToFile(byte[] param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   private static final byte[] getAlphabet(int var0) {
      if ((var0 & 16) == 16) {
         return _URL_SAFE_ALPHABET;
      } else {
         return (var0 & 32) == 32 ? _ORDERED_ALPHABET : _STANDARD_ALPHABET;
      }
   }

   private static final byte[] getDecodabet(int var0) {
      if ((var0 & 16) == 16) {
         return _URL_SAFE_DECODABET;
      } else {
         return (var0 & 32) == 32 ? _ORDERED_DECODABET : _STANDARD_DECODABET;
      }
   }

   public static final void main(String[] var0) {
      if (var0.length < 3) {
         usage("Not enough arguments.");
      } else {
         String var1 = var0[0];
         String var2 = var0[1];
         String var3 = var0[2];
         if (var1.equals("-e")) {
            encodeFileToFile(var2, var3);
         } else if (var1.equals("-d")) {
            decodeFileToFile(var2, var3);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Unknown flag: ");
            var4.append(var1);
            usage(var4.toString());
         }
      }
   }

   private static final void usage(String var0) {
      System.err.println(var0);
      System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
   }

   public static class InputStream extends FilterInputStream {
      private boolean breakLines;
      private byte[] buffer;
      private int bufferLength;
      private byte[] decodabet;
      private boolean encode;
      private int lineLength;
      private int numSigBytes;
      private int options;
      private int position;

      public InputStream(java.io.InputStream var1) {
         this(var1, 0);
      }

      public InputStream(java.io.InputStream var1, int var2) {
         super(var1);
         boolean var5 = true;
         boolean var4;
         if ((var2 & 8) != 8) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.breakLines = var4;
         if ((var2 & 1) == 1) {
            var4 = var5;
         } else {
            var4 = false;
         }

         this.encode = var4;
         byte var3;
         if (var4) {
            var3 = 4;
         } else {
            var3 = 3;
         }

         this.bufferLength = var3;
         this.buffer = new byte[var3];
         this.position = -1;
         this.lineLength = 0;
         this.options = var2;
         this.decodabet = Base64.getDecodabet(var2);
      }

      public int read() throws IOException {
         int var1;
         int var2;
         byte[] var5;
         if (this.position < 0) {
            if (!this.encode) {
               var5 = new byte[4];

               for(var1 = 0; var1 < 4; ++var1) {
                  do {
                     var2 = this.in.read();
                  } while(var2 >= 0 && this.decodabet[var2 & 127] <= -5);

                  if (var2 < 0) {
                     break;
                  }

                  var5[var1] = (byte)var2;
               }

               if (var1 != 4) {
                  if (var1 == 0) {
                     return -1;
                  }

                  throw new IOException("Improperly padded Base64 input.");
               }

               this.numSigBytes = Base64.decode4to3(var5, 0, this.buffer, 0, this.options);
               this.position = 0;
            } else {
               var5 = new byte[3];
               int var3 = 0;
               var1 = 0;

               while(true) {
                  if (var3 >= 3) {
                     if (var1 <= 0) {
                        return -1;
                     }

                     Base64.encode3to4(var5, 0, var1, this.buffer, 0, this.options);
                     this.position = 0;
                     this.numSigBytes = 4;
                     break;
                  }

                  label97: {
                     int var4;
                     try {
                        var4 = this.in.read();
                     } catch (IOException var7) {
                        if (var3 != 0) {
                           break label97;
                        }

                        throw var7;
                     }

                     var2 = var1;
                     if (var4 >= 0) {
                        var5[var3] = (byte)var4;
                        var2 = var1 + 1;
                     }

                     var1 = var2;
                  }

                  ++var3;
               }
            }
         }

         var1 = this.position;
         if (var1 >= 0) {
            if (var1 >= this.numSigBytes) {
               return -1;
            } else if (this.encode && this.breakLines && this.lineLength >= 76) {
               this.lineLength = 0;
               return 10;
            } else {
               ++this.lineLength;
               var5 = this.buffer;
               var2 = this.position;
               var1 = var2 + 1;
               this.position = var1;
               byte var8 = var5[var2];
               if (var1 >= this.bufferLength) {
                  this.position = -1;
               }

               return var8 & 255;
            }
         } else {
            throw new IOException("Error in Base64 code reading stream.");
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            int var5 = this.read();
            if (var5 < 0) {
               if (var4 == 0) {
                  return -1;
               }
               break;
            }

            var1[var2 + var4] = (byte)var5;
         }

         return var4;
      }
   }

   public static class OutputStream extends FilterOutputStream {
      // $FF: renamed from: b4 byte[]
      private byte[] field_122;
      private boolean breakLines;
      private byte[] buffer;
      private int bufferLength;
      private byte[] decodabet;
      private boolean encode;
      private int lineLength;
      private int options;
      private int position;
      private boolean suspendEncoding;

      public OutputStream(java.io.OutputStream var1) {
         this(var1, 1);
      }

      public OutputStream(java.io.OutputStream var1, int var2) {
         super(var1);
         boolean var5 = true;
         boolean var4;
         if ((var2 & 8) != 8) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.breakLines = var4;
         if ((var2 & 1) == 1) {
            var4 = var5;
         } else {
            var4 = false;
         }

         this.encode = var4;
         byte var3;
         if (var4) {
            var3 = 3;
         } else {
            var3 = 4;
         }

         this.bufferLength = var3;
         this.buffer = new byte[var3];
         this.position = 0;
         this.lineLength = 0;
         this.suspendEncoding = false;
         this.field_122 = new byte[4];
         this.options = var2;
         this.decodabet = Base64.getDecodabet(var2);
      }

      public void close() throws IOException {
         this.flushBase64();
         super.close();
         this.buffer = null;
         this.out = null;
      }

      public void flushBase64() throws IOException {
         if (this.position > 0) {
            if (this.encode) {
               this.out.write(Base64.encode3to4(this.field_122, this.buffer, this.position, this.options));
               this.position = 0;
            } else {
               throw new IOException("Base64 input not properly padded.");
            }
         }
      }

      public void resumeEncoding() {
         this.suspendEncoding = false;
      }

      public void suspendEncoding() throws IOException {
         this.flushBase64();
         this.suspendEncoding = true;
      }

      public void write(int var1) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(var1);
         } else {
            int var2;
            int var3;
            byte[] var4;
            if (this.encode) {
               var4 = this.buffer;
               var2 = this.position;
               var3 = var2 + 1;
               this.position = var3;
               var4[var2] = (byte)var1;
               if (var3 >= this.bufferLength) {
                  this.out.write(Base64.encode3to4(this.field_122, this.buffer, this.bufferLength, this.options));
                  var1 = this.lineLength + 4;
                  this.lineLength = var1;
                  if (this.breakLines && var1 >= 76) {
                     this.out.write(10);
                     this.lineLength = 0;
                  }

                  this.position = 0;
                  return;
               }
            } else {
               var4 = this.decodabet;
               if (var4[var1 & 127] > -5) {
                  var4 = this.buffer;
                  var2 = this.position;
                  var3 = var2 + 1;
                  this.position = var3;
                  var4[var2] = (byte)var1;
                  if (var3 >= this.bufferLength) {
                     var1 = Base64.decode4to3(var4, 0, this.field_122, 0, this.options);
                     this.out.write(this.field_122, 0, var1);
                     this.position = 0;
                     return;
                  }
               } else if (var4[var1 & 127] != -5) {
                  throw new IOException("Invalid character in Base64 data.");
               }
            }

         }
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(var1, var2, var3);
         } else {
            for(int var4 = 0; var4 < var3; ++var4) {
               this.write(var1[var2 + var4]);
            }

         }
      }
   }
}
