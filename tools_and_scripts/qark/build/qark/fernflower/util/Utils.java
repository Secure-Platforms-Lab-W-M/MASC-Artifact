package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
   public static int byteArrayToInt(byte[] var0) {
      return var0[3] & 255 | (var0[2] & 255) << 8 | (var0[1] & 255) << 16 | (var0[0] & 255) << 24;
   }

   public static long byteArrayToLong(byte[] var0, int var1) {
      return (long)(var0[7 + var1] & 255) | (long)(var0[6 + var1] & 255) << 8 | (long)(var0[5 + var1] & 255) << 16 | (long)(var0[4 + var1] & 255) << 24 | (long)(var0[3 + var1] & 255) << 32 | (long)(var0[2 + var1] & 255) << 40 | (long)(var0[1 + var1] & 255) << 48 | (long)(var0[0 + var1] & 255) << 56;
   }

   public static void closeSocket(Socket var0) {
      try {
         var0.shutdownOutput();
      } catch (IOException var4) {
      }

      try {
         var0.shutdownInput();
      } catch (IOException var3) {
      }

      try {
         var0.close();
      } catch (IOException var2) {
      }
   }

   public static void copyFile(File var0, File var1) throws IOException {
      File var2 = var1.getParentFile();
      if (var2 != null) {
         var2.mkdirs();
      }

      copyFully(new FileInputStream(var0), new FileOutputStream(var1), true);
   }

   public static void copyFully(InputStream var0, OutputStream var1, boolean var2) throws IOException {
      byte[] var4 = new byte[1024];

      while(true) {
         int var3 = var0.read(var4);
         if (var3 == -1) {
            var1.flush();
            if (var2) {
               var1.close();
               var0.close();
            }

            return;
         }

         var1.write(var4, 0, var3);
      }
   }

   public static void deleteFolder(String var0) {
      File var3 = new File(var0);
      if (var3.exists() && var3.isDirectory()) {
         File[] var2 = var3.listFiles();

         for(int var1 = 0; var1 < var2.length; ++var1) {
            if (var2[var1].isDirectory()) {
               deleteFolder(var2[var1].getAbsolutePath());
            } else {
               var2[var1].delete();
            }
         }

         var3.delete();
      }

   }

   public static Object deserializeObject(byte[] var0) throws IOException {
      ObjectInputStream var2 = new ObjectInputStream(new ByteArrayInputStream(var0));

      try {
         Object var3 = var2.readObject();
         return var3;
      } catch (ClassNotFoundException var1) {
         throw new IOException(var1);
      }
   }

   public static long getLongStringHash(String var0) {
      int var3 = 0;
      int var2 = 0;
      int var4 = var0.length();
      byte[] var5 = var0.getBytes();

      for(int var1 = 0; var1 < var4; ++var1) {
         var3 = 31 * var3 + (var5[var1] & 255);
         var2 = 31 * var2 + (var5[var4 - var1 - 1] & 255);
      }

      return (long)var3 << 32 | (long)var2 & 4294967295L;
   }

   public static String getServerTime() {
      Calendar var0 = Calendar.getInstance();
      SimpleDateFormat var1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
      var1.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var1.format(var0.getTime());
   }

   public static byte[] intToByteArray(int var0) {
      return new byte[]{(byte)(var0 >> 24 & 255), (byte)(var0 >> 16 & 255), (byte)(var0 >> 8 & 255), (byte)(var0 & 255)};
   }

   public static byte[] longToByteArray(long var0) {
      return new byte[]{(byte)((int)(var0 >> 56)), (byte)((int)(var0 >> 48)), (byte)((int)(var0 >> 40)), (byte)((int)(var0 >> 32)), (byte)((int)(var0 >> 24)), (byte)((int)(var0 >> 16)), (byte)((int)(var0 >> 8)), (byte)((int)var0)};
   }

   public static String[] parseURI(String var0) throws IOException {
      String var4;
      label36: {
         Exception var10000;
         label44: {
            int var2;
            String var3;
            boolean var10001;
            try {
               var3 = var0.substring(7);
               var2 = var3.indexOf(47);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label44;
            }

            int var1 = var2;
            if (var2 == -1) {
               try {
                  var1 = var3.length();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label44;
               }
            }

            try {
               var4 = var3.substring(0, var1);
               if (var1 == var3.length()) {
                  break label36;
               }
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label44;
            }

            try {
               var3 = var3.substring(var1);
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label44;
            }

            var0 = var3;
            return new String[]{var4, var0};
         }

         Exception var9 = var10000;
         StringBuilder var10 = new StringBuilder();
         var10.append("Cannot parse URI '");
         var10.append(var0);
         var10.append("'! - ");
         var10.append(var9.toString());
         throw new IOException(var10.toString());
      }

      var0 = "/";
      return new String[]{var4, var0};
   }

   public static byte[] readFully(InputStream var0, int var1) throws IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      byte[] var4 = new byte[var1];

      while(true) {
         int var2 = var0.read(var4, 0, var1);
         if (var2 == -1) {
            return var3.toByteArray();
         }

         var3.write(var4, 0, var2);
      }
   }

   public static int readLineBytesFromStream(InputStream var0, byte[] var1, boolean var2, boolean var3) throws IOException {
      int var4 = var0.read();

      int var5;
      while(var3 && var4 == 35) {
         var5 = skipLine(var0);
         var4 = var5;
         if (var5 != -1) {
            var4 = var0.read();
         }
      }

      if (var4 == -1) {
         return -1;
      } else if (var1.length == 0) {
         throw new IOException("Buffer Overflow!");
      } else {
         var1[0] = (byte)var4;
         var5 = 1;
         int var7 = var4;

         while(var7 != -1 && var7 != 10) {
            int var6 = var5;
            var4 = var7;

            while(true) {
               var7 = var4;
               var5 = var6;
               if (var4 == -1) {
                  break;
               }

               var7 = var4;
               var5 = var6;
               if (var4 == 10) {
                  break;
               }

               var5 = var0.read();
               var4 = var5;
               if (var5 != -1) {
                  if (var6 == var1.length) {
                     throw new IOException("Buffer Overflow!");
                  }

                  if (var2 && var5 < 32 && var5 < 9 && var5 > 13) {
                     StringBuilder var8 = new StringBuilder();
                     var8.append("Non Printable character: ");
                     var8.append(var5);
                     var8.append("(");
                     var8.append((char)var5);
                     var8.append(")");
                     throw new IOException(var8.toString());
                  }

                  var1[var6] = (byte)var5;
                  ++var6;
                  var4 = var5;
               }
            }
         }

         return var5;
      }
   }

   public static String readLineFromStream(InputStream var0) throws IOException {
      return readLineFromStream(var0, false);
   }

   public static String readLineFromStream(InputStream var0, boolean var1) throws IOException {
      StringBuffer var8 = new StringBuffer();
      boolean var5 = false;
      int var6 = -1;
      int var2 = 0;

      byte var4;
      int var9;
      for(var4 = 0; !var5; var2 = var9) {
         var6 = var0.read();
         byte var7 = (byte)var6;
         boolean var3;
         if (var6 == -1 || var7 == 10 && (!var1 || var4 == 13)) {
            var3 = true;
         } else {
            var3 = false;
         }

         var5 = var3;
         var9 = var2;
         if (!var5) {
            var8.append((char)var7);
            var9 = var2 + 1;
            var4 = var7;
         }
      }

      if (var6 == -1 && var2 == 0) {
         throw new EOFException("Stream is closed!");
      } else {
         var9 = var2;
         if (var2 > 0) {
            var9 = var2;
            if (var4 == 13) {
               var9 = var2 - 1;
            }
         }

         return var8.substring(0, var9);
      }
   }

   public static String readLineFromStreamRN(InputStream var0) throws IOException {
      return readLineFromStream(var0, true);
   }

   public static byte[] serializeObject(Object var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(var0);
      var2.flush();
      var2.close();
      return var1.toByteArray();
   }

   public static int skipLine(InputStream var0) throws IOException {
      int var1;
      for(var1 = 0; var1 != -1 && var1 != 10; var1 = var0.read()) {
      }

      return var1;
   }

   public static int skipWhitespace(InputStream var0, int var1) throws IOException {
      while(var1 != -1 && var1 != 10 && (var1 == 9 || var1 == 32 || var1 == 13)) {
         var1 = var0.read();
      }

      return var1;
   }

   public static void writeLongToByteArray(long var0, byte[] var2, int var3) {
      var2[var3 + 0] = (byte)((int)(var0 >> 56));
      var2[var3 + 1] = (byte)((int)(var0 >> 48));
      var2[var3 + 2] = (byte)((int)(var0 >> 40));
      var2[var3 + 3] = (byte)((int)(var0 >> 32));
      var2[var3 + 4] = (byte)((int)(var0 >> 24));
      var2[var3 + 5] = (byte)((int)(var0 >> 16));
      var2[var3 + 6] = (byte)((int)(var0 >> 8));
      var2[var3 + 7] = (byte)((int)var0);
   }
}
