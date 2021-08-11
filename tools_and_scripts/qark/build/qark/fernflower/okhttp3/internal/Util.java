package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;

public final class Util {
   public static final byte[] EMPTY_BYTE_ARRAY;
   public static final RequestBody EMPTY_REQUEST;
   public static final ResponseBody EMPTY_RESPONSE;
   public static final String[] EMPTY_STRING_ARRAY;
   public static final Comparator NATURAL_ORDER;
   public static final TimeZone UTC;
   private static final Charset UTF_16_BE;
   private static final ByteString UTF_16_BE_BOM;
   private static final Charset UTF_16_LE;
   private static final ByteString UTF_16_LE_BOM;
   private static final Charset UTF_32_BE;
   private static final ByteString UTF_32_BE_BOM;
   private static final Charset UTF_32_LE;
   private static final ByteString UTF_32_LE_BOM;
   public static final Charset UTF_8;
   private static final ByteString UTF_8_BOM;
   private static final Pattern VERIFY_AS_IP_ADDRESS;

   static {
      byte[] var0 = new byte[0];
      EMPTY_BYTE_ARRAY = var0;
      EMPTY_STRING_ARRAY = new String[0];
      EMPTY_RESPONSE = ResponseBody.create((MediaType)null, (byte[])var0);
      EMPTY_REQUEST = RequestBody.create((MediaType)null, (byte[])EMPTY_BYTE_ARRAY);
      UTF_8_BOM = ByteString.decodeHex("efbbbf");
      UTF_16_BE_BOM = ByteString.decodeHex("feff");
      UTF_16_LE_BOM = ByteString.decodeHex("fffe");
      UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
      UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
      UTF_8 = Charset.forName("UTF-8");
      UTF_16_BE = Charset.forName("UTF-16BE");
      UTF_16_LE = Charset.forName("UTF-16LE");
      UTF_32_BE = Charset.forName("UTF-32BE");
      UTF_32_LE = Charset.forName("UTF-32LE");
      UTC = TimeZone.getTimeZone("GMT");
      NATURAL_ORDER = new Comparator() {
         public int compare(String var1, String var2) {
            return var1.compareTo(var2);
         }
      };
      VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
   }

   private Util() {
   }

   public static Charset bomAwareCharset(BufferedSource var0, Charset var1) throws IOException {
      if (var0.rangeEquals(0L, UTF_8_BOM)) {
         var0.skip((long)UTF_8_BOM.size());
         return UTF_8;
      } else if (var0.rangeEquals(0L, UTF_16_BE_BOM)) {
         var0.skip((long)UTF_16_BE_BOM.size());
         return UTF_16_BE;
      } else if (var0.rangeEquals(0L, UTF_16_LE_BOM)) {
         var0.skip((long)UTF_16_LE_BOM.size());
         return UTF_16_LE;
      } else if (var0.rangeEquals(0L, UTF_32_BE_BOM)) {
         var0.skip((long)UTF_32_BE_BOM.size());
         return UTF_32_BE;
      } else if (var0.rangeEquals(0L, UTF_32_LE_BOM)) {
         var0.skip((long)UTF_32_LE_BOM.size());
         return UTF_32_LE;
      } else {
         return var1;
      }
   }

   public static void checkOffsetAndCount(long var0, long var2, long var4) {
      if ((var2 | var4) < 0L || var2 > var0 || var0 - var2 < var4) {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }
   }

   public static void closeQuietly(ServerSocket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }
   }

   public static void closeQuietly(Socket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (AssertionError var1) {
            if (!isAndroidGetsocknameError(var1)) {
               throw var1;
            }
         } catch (RuntimeException var2) {
            throw var2;
         } catch (Exception var3) {
         }
      }
   }

   public static String[] concat(String[] var0, String var1) {
      String[] var2 = new String[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
   }

   private static boolean containsInvalidHostnameAsciiCodes(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 <= 31) {
            return true;
         }

         if (var2 >= 127) {
            return true;
         }

         if (" #%/:?@[\\]".indexOf(var2) != -1) {
            return true;
         }
      }

      return false;
   }

   public static int delimiterOffset(String var0, int var1, int var2, char var3) {
      while(var1 < var2) {
         if (var0.charAt(var1) == var3) {
            return var1;
         }

         ++var1;
      }

      return var2;
   }

   public static int delimiterOffset(String var0, int var1, int var2, String var3) {
      while(var1 < var2) {
         if (var3.indexOf(var0.charAt(var1)) != -1) {
            return var1;
         }

         ++var1;
      }

      return var2;
   }

   public static boolean discard(Source var0, int var1, TimeUnit var2) {
      try {
         boolean var3 = skipAll(var0, var1, var2);
         return var3;
      } catch (IOException var4) {
         return false;
      }
   }

   public static String domainToAscii(String var0) {
      boolean var1;
      try {
         var0 = IDN.toASCII(var0).toLowerCase(Locale.US);
         if (var0.isEmpty()) {
            return null;
         }

         var1 = containsInvalidHostnameAsciiCodes(var0);
      } catch (IllegalArgumentException var2) {
         return null;
      }

      return var1 ? null : var0;
   }

   public static boolean equal(Object var0, Object var1) {
      return var0 == var1 || var0 != null && var0.equals(var1);
   }

   public static String format(String var0, Object... var1) {
      return String.format(Locale.US, var0, var1);
   }

   public static String hostHeader(HttpUrl var0, boolean var1) {
      String var4;
      if (var0.host().contains(":")) {
         StringBuilder var2 = new StringBuilder();
         var2.append("[");
         var2.append(var0.host());
         var2.append("]");
         var4 = var2.toString();
      } else {
         var4 = var0.host();
      }

      if (!var1 && var0.port() == HttpUrl.defaultPort(var0.scheme())) {
         return var4;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var4);
         var3.append(":");
         var3.append(var0.port());
         return var3.toString();
      }
   }

   public static List immutableList(List var0) {
      return Collections.unmodifiableList(new ArrayList(var0));
   }

   public static List immutableList(Object... var0) {
      return Collections.unmodifiableList(Arrays.asList((Object[])var0.clone()));
   }

   public static int indexOf(Comparator var0, String[] var1, String var2) {
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         if (var0.compare(var1[var3], var2) == 0) {
            return var3;
         }
      }

      return -1;
   }

   public static int indexOfControlOrNonAscii(String var0) {
      int var1 = 0;

      for(int var2 = var0.length(); var1 < var2; ++var1) {
         char var3 = var0.charAt(var1);
         if (var3 <= 31) {
            return var1;
         }

         if (var3 >= 127) {
            return var1;
         }
      }

      return -1;
   }

   public static String[] intersect(Comparator var0, String[] var1, String[] var2) {
      ArrayList var7 = new ArrayList();
      int var5 = var1.length;

      for(int var3 = 0; var3 < var5; ++var3) {
         String var8 = var1[var3];
         int var6 = var2.length;

         for(int var4 = 0; var4 < var6; ++var4) {
            if (var0.compare(var8, var2[var4]) == 0) {
               var7.add(var8);
               break;
            }
         }
      }

      return (String[])var7.toArray(new String[var7.size()]);
   }

   public static boolean isAndroidGetsocknameError(AssertionError var0) {
      return var0.getCause() != null && var0.getMessage() != null && var0.getMessage().contains("getsockname failed");
   }

   public static boolean nonEmptyIntersection(Comparator var0, String[] var1, String[] var2) {
      if (var1 != null && var2 != null && var1.length != 0) {
         if (var2.length == 0) {
            return false;
         } else {
            int var5 = var1.length;

            for(int var3 = 0; var3 < var5; ++var3) {
               String var7 = var1[var3];
               int var6 = var2.length;

               for(int var4 = 0; var4 < var6; ++var4) {
                  if (var0.compare(var7, var2[var4]) == 0) {
                     return true;
                  }
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean skipAll(Source param0, int param1, TimeUnit param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static int skipLeadingAsciiWhitespace(String var0, int var1, int var2) {
      while(var1 < var2) {
         char var3 = var0.charAt(var1);
         if (var3 != '\t' && var3 != '\n' && var3 != '\f' && var3 != '\r' && var3 != ' ') {
            return var1;
         }

         ++var1;
      }

      return var2;
   }

   public static int skipTrailingAsciiWhitespace(String var0, int var1, int var2) {
      --var2;

      while(var2 >= var1) {
         char var3 = var0.charAt(var2);
         if (var3 != '\t' && var3 != '\n' && var3 != '\f' && var3 != '\r' && var3 != ' ') {
            return var2 + 1;
         }

         --var2;
      }

      return var1;
   }

   public static ThreadFactory threadFactory(final String var0, final boolean var1) {
      return new ThreadFactory() {
         public Thread newThread(Runnable var1x) {
            Thread var2 = new Thread(var1x, var0);
            var2.setDaemon(var1);
            return var2;
         }
      };
   }

   public static String toHumanReadableAscii(String var0) {
      int var1 = 0;

      int var2;
      for(int var4 = var0.length(); var1 < var4; var1 += Character.charCount(var2)) {
         var2 = var0.codePointAt(var1);
         if (var2 <= 31 || var2 >= 127) {
            Buffer var5 = new Buffer();
            var5.writeUtf8(var0, 0, var1);

            while(var1 < var4) {
               int var3 = var0.codePointAt(var1);
               if (var3 > 31 && var3 < 127) {
                  var2 = var3;
               } else {
                  var2 = 63;
               }

               var5.writeUtf8CodePoint(var2);
               var1 += Character.charCount(var3);
            }

            return var5.readUtf8();
         }
      }

      return var0;
   }

   public static String trimSubstring(String var0, int var1, int var2) {
      var1 = skipLeadingAsciiWhitespace(var0, var1, var2);
      return var0.substring(var1, skipTrailingAsciiWhitespace(var0, var1, var2));
   }

   public static boolean verifyAsIpAddress(String var0) {
      return VERIFY_AS_IP_ADDRESS.matcher(var0).matches();
   }
}
