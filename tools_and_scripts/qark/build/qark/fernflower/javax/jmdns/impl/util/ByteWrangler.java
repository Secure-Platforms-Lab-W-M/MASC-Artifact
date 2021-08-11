package javax.jmdns.impl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ByteWrangler {
   public static final byte[] EMPTY_TXT = new byte[]{0};
   public static final int MAX_DATA_LENGTH = 256;
   public static final int MAX_VALUE_LENGTH = 255;
   public static final byte[] NO_VALUE = new byte[0];

   public static byte[] encodeText(String var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(256);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(100);
      writeUTF(var2, var0);
      byte[] var5 = var2.toByteArray();
      if (var5.length <= 255) {
         var1.write((byte)var5.length);
         var1.write(var5, 0, var5.length);
         byte[] var3 = var1.toByteArray();
         return var3.length > 0 ? var3 : EMPTY_TXT;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Cannot have individual values larger that 255 chars. Offending value: ");
         var4.append(var0);
         throw new IOException(var4.toString());
      }
   }

   public static void readProperties(Map var0, byte[] var1) throws Exception {
      int var3;
      int var4;
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; var2 = var4 + var3) {
            var3 = var2 + 1;
            var4 = var1[var2] & 255;
            if (var4 == 0 || var3 + var4 > var1.length) {
               var0.clear();
               break;
            }

            for(var2 = 0; var2 < var4 && var1[var3 + var2] != 61; ++var2) {
            }

            String var5 = readUTF(var1, var3, var2);
            if (var5 == null) {
               var0.clear();
               return;
            }

            if (var2 == var4) {
               var0.put(var5, NO_VALUE);
            } else {
               ++var2;
               byte[] var6 = new byte[var4 - var2];
               System.arraycopy(var1, var3 + var2, var6, 0, var4 - var2);
               var0.put(var5, var6);
            }
         }
      }

   }

   public static String readUTF(byte[] var0) {
      return readUTF(var0, 0, var0.length);
   }

   public static String readUTF(byte[] var0, int var1, int var2) {
      int var4 = var1;
      StringBuilder var6 = new StringBuilder();
      var1 = var1;

      while(true) {
         int var3 = var1;
         if (var1 >= var4 + var2) {
            return var6.toString();
         }

         ++var1;
         var3 = var0[var3] & 255;
         int var5;
         switch(var3 >> 4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            break;
         case 8:
         case 9:
         case 10:
         case 11:
         default:
            if (var1 + 1 >= var2) {
               return null;
            }

            var5 = var1 + 1;
            var3 = (var3 & 63) << 4 | var0[var1] & 15;
            var1 = var5;
            break;
         case 12:
         case 13:
            if (var1 >= var2) {
               return null;
            }

            var5 = var1 + 1;
            var3 = (var3 & 31) << 6 | var0[var1] & 63;
            var1 = var5;
            break;
         case 14:
            if (var1 + 2 >= var2) {
               return null;
            }

            var5 = var1 + 1;
            var3 = (var0[var1] & 63) << 6 | (var3 & 15) << 12 | var0[var5] & 63;
            var1 = var5 + 1;
         }

         var6.append((char)var3);
      }
   }

   public static byte[] textFromProperties(Map var0) {
      IOException var10000;
      StringBuilder var19;
      label128: {
         byte[] var1 = null;
         if (var0 != null) {
            Iterator var3;
            boolean var10001;
            ByteArrayOutputStream var18;
            try {
               var18 = new ByteArrayOutputStream(256);
               var3 = var0.entrySet().iterator();
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break label128;
            }

            while(true) {
               String var2;
               ByteArrayOutputStream var4;
               Object var17;
               try {
                  if (!var3.hasNext()) {
                     break;
                  }

                  Entry var16 = (Entry)var3.next();
                  var2 = (String)var16.getKey();
                  var17 = var16.getValue();
                  var4 = new ByteArrayOutputStream(100);
                  writeUTF(var4, var2);
               } catch (IOException var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label128;
               }

               label129: {
                  if (var17 != null) {
                     label130: {
                        try {
                           if (var17 instanceof String) {
                              var4.write(61);
                              writeUTF(var4, (String)var17);
                              break label130;
                           }
                        } catch (IOException var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label128;
                        }

                        try {
                           if (!(var17 instanceof byte[])) {
                              break label129;
                           }

                           byte[] var5 = (byte[])((byte[])var17);
                           if (var5.length > 0) {
                              var4.write(61);
                              var4.write(var5, 0, var5.length);
                              break label130;
                           }
                        } catch (IOException var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label128;
                        }

                        var17 = null;
                     }
                  }

                  label124: {
                     byte[] var23;
                     try {
                        var23 = var4.toByteArray();
                        if (var23.length > 255) {
                           var19 = new StringBuilder();
                           var19.append("Cannot have individual values larger that 255 chars. Offending value: ");
                           var19.append(var2);
                           break label124;
                        }
                     } catch (IOException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label128;
                     }

                     try {
                        var18.write((byte)var23.length);
                        var18.write(var23, 0, var23.length);
                        continue;
                     } catch (IOException var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label128;
                     }
                  }

                  String var20;
                  if (var17 != null) {
                     var20 = "";
                  } else {
                     try {
                        StringBuilder var21 = new StringBuilder();
                        var21.append("=");
                        var21.append(var17);
                        var20 = var21.toString();
                     } catch (IOException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label128;
                     }
                  }

                  try {
                     var19.append(var20);
                     throw new IOException(var19.toString());
                  } catch (IOException var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label128;
                  }
               }

               try {
                  var19 = new StringBuilder();
                  var19.append("Invalid property value: ");
                  var19.append(var17);
                  throw new IllegalArgumentException(var19.toString());
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label128;
               }
            }

            try {
               var1 = var18.toByteArray();
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label128;
            }
         }

         if (var1 != null && var1.length > 0) {
            return var1;
         }

         return EMPTY_TXT;
      }

      IOException var22 = var10000;
      var19 = new StringBuilder();
      var19.append("unexpected exception: ");
      var19.append(var22);
      throw new RuntimeException(var19.toString());
   }

   public static void writeUTF(OutputStream var0, String var1) throws IOException {
      int var3 = var1.length();

      for(int var2 = 0; var2 < var3; ++var2) {
         char var4 = var1.charAt(var2);
         if (var4 >= 1 && var4 <= 127) {
            var0.write(var4);
         } else if (var4 > 2047) {
            var0.write(var4 >> 12 & 15 | 224);
            var0.write(var4 >> 6 & 63 | 128);
            var0.write(var4 >> 0 & 63 | 128);
         } else {
            var0.write(var4 >> 6 & 31 | 192);
            var0.write(var4 >> 0 & 63 | 128);
         }
      }

   }
}
