package okhttp3.internal.http2;

import java.io.IOException;
import okhttp3.internal.Util;
import okio.ByteString;

public final class Http2 {
   static final String[] BINARY = new String[256];
   static final ByteString CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
   static final String[] FLAGS = new String[64];
   static final byte FLAG_ACK = 1;
   static final byte FLAG_COMPRESSED = 32;
   static final byte FLAG_END_HEADERS = 4;
   static final byte FLAG_END_PUSH_PROMISE = 4;
   static final byte FLAG_END_STREAM = 1;
   static final byte FLAG_NONE = 0;
   static final byte FLAG_PADDED = 8;
   static final byte FLAG_PRIORITY = 32;
   private static final String[] FRAME_NAMES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
   static final int INITIAL_MAX_FRAME_SIZE = 16384;
   static final byte TYPE_CONTINUATION = 9;
   static final byte TYPE_DATA = 0;
   static final byte TYPE_GOAWAY = 7;
   static final byte TYPE_HEADERS = 1;
   static final byte TYPE_PING = 6;
   static final byte TYPE_PRIORITY = 2;
   static final byte TYPE_PUSH_PROMISE = 5;
   static final byte TYPE_RST_STREAM = 3;
   static final byte TYPE_SETTINGS = 4;
   static final byte TYPE_WINDOW_UPDATE = 8;

   static {
      int var0 = 0;

      while(true) {
         String[] var6 = BINARY;
         if (var0 >= var6.length) {
            String[] var7 = FLAGS;
            var7[0] = "";
            var7[1] = "END_STREAM";
            int[] var10 = new int[]{1};
            var7[8] = "PADDED";
            int var1 = var10.length;

            int var2;
            for(var0 = 0; var0 < var1; ++var0) {
               var2 = var10[var0];
               var7 = FLAGS;
               StringBuilder var8 = new StringBuilder();
               var8.append(FLAGS[var2]);
               var8.append("|PADDED");
               var7[var2 | 8] = var8.toString();
            }

            var7 = FLAGS;
            var7[4] = "END_HEADERS";
            var7[32] = "PRIORITY";
            var7[36] = "END_HEADERS|PRIORITY";
            int[] var11 = new int[]{4, 32, 36};
            var2 = var11.length;

            for(var0 = 0; var0 < var2; ++var0) {
               int var3 = var11[var0];
               int var4 = var10.length;

               for(var1 = 0; var1 < var4; ++var1) {
                  int var5 = var10[var1];
                  String[] var12 = FLAGS;
                  StringBuilder var9 = new StringBuilder();
                  var9.append(FLAGS[var5]);
                  var9.append('|');
                  var9.append(FLAGS[var3]);
                  var12[var5 | var3] = var9.toString();
                  var12 = FLAGS;
                  var9 = new StringBuilder();
                  var9.append(FLAGS[var5]);
                  var9.append('|');
                  var9.append(FLAGS[var3]);
                  var9.append("|PADDED");
                  var12[var5 | var3 | 8] = var9.toString();
               }
            }

            var0 = 0;

            while(true) {
               var6 = FLAGS;
               if (var0 >= var6.length) {
                  return;
               }

               if (var6[var0] == null) {
                  var6[var0] = BINARY[var0];
               }

               ++var0;
            }
         }

         var6[var0] = Util.format("%8s", Integer.toBinaryString(var0)).replace(' ', '0');
         ++var0;
      }
   }

   private Http2() {
   }

   static String formatFlags(byte var0, byte var1) {
      if (var1 == 0) {
         return "";
      } else {
         if (var0 != 2 && var0 != 3) {
            if (var0 == 4 || var0 == 6) {
               return var1 == 1 ? "ACK" : BINARY[var1];
            }

            if (var0 != 7 && var0 != 8) {
               String[] var2 = FLAGS;
               String var3;
               if (var1 < var2.length) {
                  var3 = var2[var1];
               } else {
                  var3 = BINARY[var1];
               }

               if (var0 == 5 && (var1 & 4) != 0) {
                  return var3.replace("HEADERS", "PUSH_PROMISE");
               }

               if (var0 == 0 && (var1 & 32) != 0) {
                  return var3.replace("PRIORITY", "COMPRESSED");
               }

               return var3;
            }
         }

         return BINARY[var1];
      }
   }

   static String frameLog(boolean var0, int var1, int var2, byte var3, byte var4) {
      String[] var5 = FRAME_NAMES;
      String var8;
      if (var3 < var5.length) {
         var8 = var5[var3];
      } else {
         var8 = Util.format("0x%02x", var3);
      }

      String var7 = formatFlags(var3, var4);
      String var6;
      if (var0) {
         var6 = "<<";
      } else {
         var6 = ">>";
      }

      return Util.format("%s 0x%08x %5d %-13s %s", var6, var1, var2, var8, var7);
   }

   static IllegalArgumentException illegalArgument(String var0, Object... var1) {
      throw new IllegalArgumentException(Util.format(var0, var1));
   }

   static IOException ioException(String var0, Object... var1) throws IOException {
      throw new IOException(Util.format(var0, var1));
   }
}
