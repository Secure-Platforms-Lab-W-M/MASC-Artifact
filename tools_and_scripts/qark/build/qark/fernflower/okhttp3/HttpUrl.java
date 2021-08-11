package okhttp3;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;

public final class HttpUrl {
   static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
   static final String FRAGMENT_ENCODE_SET = "";
   static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
   private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
   static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
   static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
   static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
   static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
   static final String QUERY_ENCODE_SET = " \"'<>#";
   static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
   @Nullable
   private final String fragment;
   final String host;
   private final String password;
   private final List pathSegments;
   final int port;
   @Nullable
   private final List queryNamesAndValues;
   final String scheme;
   private final String url;
   private final String username;

   HttpUrl(HttpUrl.Builder var1) {
      this.scheme = var1.scheme;
      this.username = percentDecode(var1.encodedUsername, false);
      this.password = percentDecode(var1.encodedPassword, false);
      this.host = var1.host;
      this.port = var1.effectivePort();
      this.pathSegments = this.percentDecode(var1.encodedPathSegments, false);
      List var2 = var1.encodedQueryNamesAndValues;
      Object var3 = null;
      if (var2 != null) {
         var2 = this.percentDecode(var1.encodedQueryNamesAndValues, true);
      } else {
         var2 = null;
      }

      this.queryNamesAndValues = var2;
      String var4;
      if (var1.encodedFragment != null) {
         var4 = percentDecode(var1.encodedFragment, false);
      } else {
         var4 = (String)var3;
      }

      this.fragment = var4;
      this.url = var1.toString();
   }

   static String canonicalize(String var0, int var1, int var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      int var9;
      for(int var8 = var1; var8 < var2; var8 += Character.charCount(var9)) {
         var9 = var0.codePointAt(var8);
         if (var9 < 32 || var9 == 127 || var9 >= 128 && var7 || var3.indexOf(var9) != -1 || var9 == 37 && (!var4 || var5 && !percentEncoded(var0, var8, var2)) || var9 == 43 && var6) {
            Buffer var10 = new Buffer();
            var10.writeUtf8(var0, var1, var8);
            canonicalize(var10, var0, var8, var2, var3, var4, var5, var6, var7);
            return var10.readUtf8();
         }
      }

      return var0.substring(var1, var2);
   }

   static String canonicalize(String var0, String var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      return canonicalize(var0, 0, var0.length(), var1, var2, var3, var4, var5);
   }

   static void canonicalize(Buffer var0, String var1, int var2, int var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      int var9;
      for(Buffer var11 = null; var2 < var3; var2 += Character.charCount(var9)) {
         var9 = var1.codePointAt(var2);
         if (!var5 || var9 != 9 && var9 != 10 && var9 != 12 && var9 != 13) {
            if (var9 == 43 && var7) {
               String var13;
               if (var5) {
                  var13 = "+";
               } else {
                  var13 = "%2B";
               }

               var0.writeUtf8(var13);
            } else if (var9 < 32 || var9 == 127 || var9 >= 128 && var8 || var4.indexOf(var9) != -1 || var9 == 37 && (!var5 || var6 && !percentEncoded(var1, var2, var3))) {
               Buffer var12 = var11;
               if (var11 == null) {
                  var12 = new Buffer();
               }

               var12.writeUtf8CodePoint(var9);

               while(true) {
                  var11 = var12;
                  if (var12.exhausted()) {
                     break;
                  }

                  int var10 = var12.readByte() & 255;
                  var0.writeByte(37);
                  var0.writeByte(HEX_DIGITS[var10 >> 4 & 15]);
                  var0.writeByte(HEX_DIGITS[var10 & 15]);
               }
            } else {
               var0.writeUtf8CodePoint(var9);
            }
         }
      }

   }

   static int decodeHexDigit(char var0) {
      if (var0 >= '0' && var0 <= '9') {
         return var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         return var0 - 97 + 10;
      } else {
         return var0 >= 'A' && var0 <= 'F' ? var0 - 65 + 10 : -1;
      }
   }

   public static int defaultPort(String var0) {
      if (var0.equals("http")) {
         return 80;
      } else {
         return var0.equals("https") ? 443 : -1;
      }
   }

   @Nullable
   public static HttpUrl get(URI var0) {
      return parse(var0.toString());
   }

   @Nullable
   public static HttpUrl get(URL var0) {
      return parse(var0.toString());
   }

   static HttpUrl getChecked(String var0) throws MalformedURLException, UnknownHostException {
      HttpUrl.Builder var3 = new HttpUrl.Builder();
      HttpUrl.Builder.ParseResult var2 = var3.parse((HttpUrl)null, var0);
      int var1 = null.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[var2.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Invalid URL: ");
            var5.append(var2);
            var5.append(" for ");
            var5.append(var0);
            throw new MalformedURLException(var5.toString());
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid host: ");
            var4.append(var0);
            throw new UnknownHostException(var4.toString());
         }
      } else {
         return var3.build();
      }
   }

   static void namesAndValuesToQueryString(StringBuilder var0, List var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; var2 += 2) {
         String var4 = (String)var1.get(var2);
         String var5 = (String)var1.get(var2 + 1);
         if (var2 > 0) {
            var0.append('&');
         }

         var0.append(var4);
         if (var5 != null) {
            var0.append('=');
            var0.append(var5);
         }
      }

   }

   @Nullable
   public static HttpUrl parse(String var0) {
      HttpUrl.Builder var2 = new HttpUrl.Builder();
      HttpUrl var1 = null;
      if (var2.parse((HttpUrl)null, var0) == HttpUrl.Builder.ParseResult.SUCCESS) {
         var1 = var2.build();
      }

      return var1;
   }

   static void pathSegmentsToString(StringBuilder var0, List var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         var0.append('/');
         var0.append((String)var1.get(var2));
      }

   }

   static String percentDecode(String var0, int var1, int var2, boolean var3) {
      for(int var4 = var1; var4 < var2; ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == '%' || var5 == '+' && var3) {
            Buffer var6 = new Buffer();
            var6.writeUtf8(var0, var1, var4);
            percentDecode(var6, var0, var4, var2, var3);
            return var6.readUtf8();
         }
      }

      return var0.substring(var1, var2);
   }

   static String percentDecode(String var0, boolean var1) {
      return percentDecode(var0, 0, var0.length(), var1);
   }

   private List percentDecode(List var1, boolean var2) {
      int var4 = var1.size();
      ArrayList var6 = new ArrayList(var4);

      for(int var3 = 0; var3 < var4; ++var3) {
         String var5 = (String)var1.get(var3);
         if (var5 != null) {
            var5 = percentDecode(var5, var2);
         } else {
            var5 = null;
         }

         var6.add(var5);
      }

      return Collections.unmodifiableList(var6);
   }

   static void percentDecode(Buffer var0, String var1, int var2, int var3, boolean var4) {
      int var5;
      for(; var2 < var3; var2 += Character.charCount(var5)) {
         var5 = var1.codePointAt(var2);
         if (var5 == 37 && var2 + 2 < var3) {
            int var6 = decodeHexDigit(var1.charAt(var2 + 1));
            int var7 = decodeHexDigit(var1.charAt(var2 + 2));
            if (var6 != -1 && var7 != -1) {
               var0.writeByte((var6 << 4) + var7);
               var2 += 2;
               continue;
            }
         } else if (var5 == 43 && var4) {
            var0.writeByte(32);
            continue;
         }

         var0.writeUtf8CodePoint(var5);
      }

   }

   static boolean percentEncoded(String var0, int var1, int var2) {
      return var1 + 2 < var2 && var0.charAt(var1) == '%' && decodeHexDigit(var0.charAt(var1 + 1)) != -1 && decodeHexDigit(var0.charAt(var1 + 2)) != -1;
   }

   static List queryStringToNamesAndValues(String var0) {
      ArrayList var4 = new ArrayList();

      int var2;
      for(int var1 = 0; var1 <= var0.length(); var1 = var2 + 1) {
         int var3 = var0.indexOf(38, var1);
         var2 = var3;
         if (var3 == -1) {
            var2 = var0.length();
         }

         var3 = var0.indexOf(61, var1);
         if (var3 != -1 && var3 <= var2) {
            var4.add(var0.substring(var1, var3));
            var4.add(var0.substring(var3 + 1, var2));
         } else {
            var4.add(var0.substring(var1, var2));
            var4.add((Object)null);
         }
      }

      return var4;
   }

   @Nullable
   public String encodedFragment() {
      if (this.fragment == null) {
         return null;
      } else {
         int var1 = this.url.indexOf(35);
         return this.url.substring(var1 + 1);
      }
   }

   public String encodedPassword() {
      if (this.password.isEmpty()) {
         return "";
      } else {
         int var1 = this.url.indexOf(58, this.scheme.length() + 3);
         int var2 = this.url.indexOf(64);
         return this.url.substring(var1 + 1, var2);
      }
   }

   public String encodedPath() {
      int var1 = this.url.indexOf(47, this.scheme.length() + 3);
      String var3 = this.url;
      int var2 = Util.delimiterOffset(var3, var1, var3.length(), "?#");
      return this.url.substring(var1, var2);
   }

   public List encodedPathSegments() {
      int var1 = this.url.indexOf(47, this.scheme.length() + 3);
      String var4 = this.url;
      int var2 = Util.delimiterOffset(var4, var1, var4.length(), "?#");
      ArrayList var5 = new ArrayList();

      while(var1 < var2) {
         int var3 = var1 + 1;
         var1 = Util.delimiterOffset(this.url, var3, var2, '/');
         var5.add(this.url.substring(var3, var1));
      }

      return var5;
   }

   @Nullable
   public String encodedQuery() {
      if (this.queryNamesAndValues == null) {
         return null;
      } else {
         int var1 = this.url.indexOf(63) + 1;
         String var3 = this.url;
         int var2 = Util.delimiterOffset(var3, var1 + 1, var3.length(), '#');
         return this.url.substring(var1, var2);
      }
   }

   public String encodedUsername() {
      if (this.username.isEmpty()) {
         return "";
      } else {
         int var1 = this.scheme.length() + 3;
         String var3 = this.url;
         int var2 = Util.delimiterOffset(var3, var1, var3.length(), ":@");
         return this.url.substring(var1, var2);
      }
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof HttpUrl && ((HttpUrl)var1).url.equals(this.url);
   }

   @Nullable
   public String fragment() {
      return this.fragment;
   }

   public int hashCode() {
      return this.url.hashCode();
   }

   public String host() {
      return this.host;
   }

   public boolean isHttps() {
      return this.scheme.equals("https");
   }

   public HttpUrl.Builder newBuilder() {
      HttpUrl.Builder var2 = new HttpUrl.Builder();
      var2.scheme = this.scheme;
      var2.encodedUsername = this.encodedUsername();
      var2.encodedPassword = this.encodedPassword();
      var2.host = this.host;
      int var1;
      if (this.port != defaultPort(this.scheme)) {
         var1 = this.port;
      } else {
         var1 = -1;
      }

      var2.port = var1;
      var2.encodedPathSegments.clear();
      var2.encodedPathSegments.addAll(this.encodedPathSegments());
      var2.encodedQuery(this.encodedQuery());
      var2.encodedFragment = this.encodedFragment();
      return var2;
   }

   @Nullable
   public HttpUrl.Builder newBuilder(String var1) {
      HttpUrl.Builder var2 = new HttpUrl.Builder();
      return var2.parse(this, var1) == HttpUrl.Builder.ParseResult.SUCCESS ? var2 : null;
   }

   public String password() {
      return this.password;
   }

   public List pathSegments() {
      return this.pathSegments;
   }

   public int pathSize() {
      return this.pathSegments.size();
   }

   public int port() {
      return this.port;
   }

   @Nullable
   public String query() {
      if (this.queryNamesAndValues == null) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         namesAndValuesToQueryString(var1, this.queryNamesAndValues);
         return var1.toString();
      }
   }

   @Nullable
   public String queryParameter(String var1) {
      List var4 = this.queryNamesAndValues;
      if (var4 == null) {
         return null;
      } else {
         int var2 = 0;

         for(int var3 = var4.size(); var2 < var3; var2 += 2) {
            if (var1.equals(this.queryNamesAndValues.get(var2))) {
               return (String)this.queryNamesAndValues.get(var2 + 1);
            }
         }

         return null;
      }
   }

   public String queryParameterName(int var1) {
      List var2 = this.queryNamesAndValues;
      if (var2 != null) {
         return (String)var2.get(var1 * 2);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public Set queryParameterNames() {
      if (this.queryNamesAndValues == null) {
         return Collections.emptySet();
      } else {
         LinkedHashSet var3 = new LinkedHashSet();
         int var1 = 0;

         for(int var2 = this.queryNamesAndValues.size(); var1 < var2; var1 += 2) {
            var3.add(this.queryNamesAndValues.get(var1));
         }

         return Collections.unmodifiableSet(var3);
      }
   }

   public String queryParameterValue(int var1) {
      List var2 = this.queryNamesAndValues;
      if (var2 != null) {
         return (String)var2.get(var1 * 2 + 1);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public List queryParameterValues(String var1) {
      if (this.queryNamesAndValues == null) {
         return Collections.emptyList();
      } else {
         ArrayList var4 = new ArrayList();
         int var2 = 0;

         for(int var3 = this.queryNamesAndValues.size(); var2 < var3; var2 += 2) {
            if (var1.equals(this.queryNamesAndValues.get(var2))) {
               var4.add(this.queryNamesAndValues.get(var2 + 1));
            }
         }

         return Collections.unmodifiableList(var4);
      }
   }

   public int querySize() {
      List var1 = this.queryNamesAndValues;
      return var1 != null ? var1.size() / 2 : 0;
   }

   public String redact() {
      return this.newBuilder("/...").username("").password("").build().toString();
   }

   @Nullable
   public HttpUrl resolve(String var1) {
      HttpUrl.Builder var2 = this.newBuilder(var1);
      return var2 != null ? var2.build() : null;
   }

   public String scheme() {
      return this.scheme;
   }

   public String toString() {
      return this.url;
   }

   @Nullable
   public String topPrivateDomain() {
      return Util.verifyAsIpAddress(this.host) ? null : PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
   }

   public URI uri() {
      String var2 = this.newBuilder().reencodeForUri().toString();

      try {
         URI var1 = new URI(var2);
         return var1;
      } catch (URISyntaxException var4) {
         try {
            URI var5 = URI.create(var2.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
            return var5;
         } catch (Exception var3) {
            throw new RuntimeException(var4);
         }
      }
   }

   public URL url() {
      try {
         URL var1 = new URL(this.url);
         return var1;
      } catch (MalformedURLException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String username() {
      return this.username;
   }

   public static final class Builder {
      @Nullable
      String encodedFragment;
      String encodedPassword = "";
      final List encodedPathSegments;
      @Nullable
      List encodedQueryNamesAndValues;
      String encodedUsername = "";
      @Nullable
      String host;
      int port = -1;
      @Nullable
      String scheme;

      public Builder() {
         ArrayList var1 = new ArrayList();
         this.encodedPathSegments = var1;
         var1.add("");
      }

      private HttpUrl.Builder addPathSegments(String var1, boolean var2) {
         int var3 = 0;

         do {
            int var4 = Util.delimiterOffset(var1, var3, var1.length(), "/\\");
            boolean var5;
            if (var4 < var1.length()) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.push(var1, var3, var4, var5, var2);
            var3 = var4 + 1;
         } while(var3 <= var1.length());

         return this;
      }

      private static String canonicalizeHost(String var0, int var1, int var2) {
         var0 = HttpUrl.percentDecode(var0, var1, var2, false);
         if (!var0.contains(":")) {
            return Util.domainToAscii(var0);
         } else {
            InetAddress var3;
            if (var0.startsWith("[") && var0.endsWith("]")) {
               var3 = decodeIpv6(var0, 1, var0.length() - 1);
            } else {
               var3 = decodeIpv6(var0, 0, var0.length());
            }

            if (var3 == null) {
               return null;
            } else {
               byte[] var4 = var3.getAddress();
               if (var4.length == 16) {
                  return inet6AddressToAscii(var4);
               } else {
                  throw new AssertionError();
               }
            }
         }
      }

      private static boolean decodeIpv4Suffix(String var0, int var1, int var2, byte[] var3, int var4) {
         int var6 = var4;

         for(int var5 = var1; var5 < var2; ++var6) {
            if (var6 == var3.length) {
               return false;
            }

            var1 = var5;
            if (var6 != var4) {
               if (var0.charAt(var5) != '.') {
                  return false;
               }

               var1 = var5 + 1;
            }

            int var7 = 0;

            for(var5 = var1; var5 < var2; ++var5) {
               char var8 = var0.charAt(var5);
               if (var8 < '0' || var8 > '9') {
                  break;
               }

               if (var7 == 0 && var1 != var5) {
                  return false;
               }

               var7 = var7 * 10 + var8 - 48;
               if (var7 > 255) {
                  return false;
               }
            }

            if (var5 - var1 == 0) {
               return false;
            }

            var3[var6] = (byte)var7;
         }

         if (var6 != var4 + 4) {
            return false;
         } else {
            return true;
         }
      }

      @Nullable
      private static InetAddress decodeIpv6(String var0, int var1, int var2) {
         byte[] var9 = new byte[16];
         int var3 = 0;
         int var4 = -1;
         int var8 = -1;
         int var5 = var1;

         int var6;
         while(true) {
            var1 = var3;
            var6 = var4;
            if (var5 >= var2) {
               break;
            }

            if (var3 == var9.length) {
               return null;
            }

            int var7;
            if (var5 + 2 <= var2 && var0.regionMatches(var5, "::", 0, 2)) {
               if (var4 != -1) {
                  return null;
               }

               var5 += 2;
               var4 = var3 + 2;
               var7 = var4;
               var6 = var4;
               var1 = var5;
               if (var5 == var2) {
                  var1 = var4;
                  var6 = var4;
                  break;
               }
            } else {
               var7 = var3;
               var6 = var4;
               var1 = var5;
               if (var3 != 0) {
                  if (!var0.regionMatches(var5, ":", 0, 1)) {
                     if (!var0.regionMatches(var5, ".", 0, 1)) {
                        return null;
                     }

                     if (!decodeIpv4Suffix(var0, var8, var2, var9, var3 - 2)) {
                        return null;
                     }

                     var1 = var3 + 2;
                     var6 = var4;
                     break;
                  }

                  var1 = var5 + 1;
                  var7 = var3;
                  var6 = var4;
               }
            }

            var3 = 0;

            for(var5 = var1; var1 < var2; ++var1) {
               var4 = HttpUrl.decodeHexDigit(var0.charAt(var1));
               if (var4 == -1) {
                  break;
               }

               var3 = (var3 << 4) + var4;
            }

            var4 = var1 - var5;
            if (var4 == 0) {
               return null;
            }

            if (var4 > 4) {
               return null;
            }

            var8 = var7 + 1;
            var9[var7] = (byte)(var3 >>> 8 & 255);
            var4 = var8 + 1;
            var9[var8] = (byte)(var3 & 255);
            var3 = var4;
            var4 = var6;
            var8 = var5;
            var5 = var1;
         }

         if (var1 != var9.length) {
            if (var6 == -1) {
               return null;
            }

            System.arraycopy(var9, var6, var9, var9.length - (var1 - var6), var1 - var6);
            Arrays.fill(var9, var6, var9.length - var1 + var6, (byte)0);
         }

         try {
            InetAddress var11 = InetAddress.getByAddress(var9);
            return var11;
         } catch (UnknownHostException var10) {
            throw new AssertionError();
         }
      }

      private static String inet6AddressToAscii(byte[] var0) {
         int var4 = -1;
         int var3 = 0;

         int var1;
         int var2;
         for(var1 = 0; var1 < var0.length; var3 = var2) {
            var2 = var1;

            while(true) {
               int var6 = var2;
               if (var2 >= 16 || var0[var2] != 0 || var0[var2 + 1] != 0) {
                  int var7 = var2 - var1;
                  int var5 = var4;
                  var2 = var3;
                  if (var7 > var3) {
                     var5 = var4;
                     var2 = var3;
                     if (var7 >= 4) {
                        var2 = var7;
                        var5 = var1;
                     }
                  }

                  var1 = var6 + 2;
                  var4 = var5;
                  break;
               }

               var2 += 2;
            }
         }

         Buffer var8 = new Buffer();
         var1 = 0;

         while(var1 < var0.length) {
            if (var1 == var4) {
               var8.writeByte(58);
               var2 = var1 + var3;
               var1 = var2;
               if (var2 == 16) {
                  var8.writeByte(58);
                  var1 = var2;
               }
            } else {
               if (var1 > 0) {
                  var8.writeByte(58);
               }

               var8.writeHexadecimalUnsignedLong((long)((var0[var1] & 255) << 8 | var0[var1 + 1] & 255));
               var1 += 2;
            }
         }

         return var8.readUtf8();
      }

      private boolean isDot(String var1) {
         return var1.equals(".") || var1.equalsIgnoreCase("%2e");
      }

      private boolean isDotDot(String var1) {
         return var1.equals("..") || var1.equalsIgnoreCase("%2e.") || var1.equalsIgnoreCase(".%2e") || var1.equalsIgnoreCase("%2e%2e");
      }

      private static int parsePort(String var0, int var1, int var2) {
         try {
            var1 = Integer.parseInt(HttpUrl.canonicalize(var0, var1, var2, "", false, false, false, true));
         } catch (NumberFormatException var3) {
            return -1;
         }

         return var1 > 0 && var1 <= 65535 ? var1 : -1;
      }

      private void pop() {
         List var1 = this.encodedPathSegments;
         if (((String)var1.remove(var1.size() - 1)).isEmpty() && !this.encodedPathSegments.isEmpty()) {
            var1 = this.encodedPathSegments;
            var1.set(var1.size() - 1, "");
         } else {
            this.encodedPathSegments.add("");
         }
      }

      private static int portColonOffset(String var0, int var1, int var2) {
         int var3;
         label26:
         for(; var1 < var2; var1 = var3 + 1) {
            char var4 = var0.charAt(var1);
            if (var4 == ':') {
               return var1;
            }

            var3 = var1;
            if (var4 != '[') {
               var3 = var1;
            } else {
               do {
                  var1 = var3 + 1;
                  var3 = var1;
                  if (var1 >= var2) {
                     continue label26;
                  }

                  var3 = var1;
               } while(var0.charAt(var1) != ']');

               var3 = var1;
            }
         }

         return var2;
      }

      private void push(String var1, int var2, int var3, boolean var4, boolean var5) {
         var1 = HttpUrl.canonicalize(var1, var2, var3, " \"<>^`{}|/\\?#", var5, false, false, true);
         if (!this.isDot(var1)) {
            if (this.isDotDot(var1)) {
               this.pop();
            } else {
               List var6 = this.encodedPathSegments;
               if (((String)var6.get(var6.size() - 1)).isEmpty()) {
                  var6 = this.encodedPathSegments;
                  var6.set(var6.size() - 1, var1);
               } else {
                  this.encodedPathSegments.add(var1);
               }

               if (var4) {
                  this.encodedPathSegments.add("");
               }

            }
         }
      }

      private void removeAllCanonicalQueryParameters(String var1) {
         for(int var2 = this.encodedQueryNamesAndValues.size() - 2; var2 >= 0; var2 -= 2) {
            if (var1.equals(this.encodedQueryNamesAndValues.get(var2))) {
               this.encodedQueryNamesAndValues.remove(var2 + 1);
               this.encodedQueryNamesAndValues.remove(var2);
               if (this.encodedQueryNamesAndValues.isEmpty()) {
                  this.encodedQueryNamesAndValues = null;
                  return;
               }
            }
         }

      }

      private void resolvePath(String var1, int var2, int var3) {
         if (var2 != var3) {
            char var4 = var1.charAt(var2);
            if (var4 != '/' && var4 != '\\') {
               List var6 = this.encodedPathSegments;
               var6.set(var6.size() - 1, "");
            } else {
               this.encodedPathSegments.clear();
               this.encodedPathSegments.add("");
               ++var2;
            }

            while(var2 < var3) {
               int var7 = Util.delimiterOffset(var1, var2, var3, "/\\");
               boolean var5;
               if (var7 < var3) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               this.push(var1, var2, var7, var5, true);
               var2 = var7;
               if (var5) {
                  var2 = var7 + 1;
               }
            }

         }
      }

      private static int schemeDelimiterOffset(String var0, int var1, int var2) {
         if (var2 - var1 < 2) {
            return -1;
         } else {
            char var3 = var0.charAt(var1);
            if (var3 < 'a' || var3 > 'z') {
               if (var3 < 'A') {
                  return -1;
               }

               if (var3 > 'Z') {
                  return -1;
               }
            }

            ++var1;

            while(var1 < var2) {
               var3 = var0.charAt(var1);
               if ((var3 < 'a' || var3 > 'z') && (var3 < 'A' || var3 > 'Z') && (var3 < '0' || var3 > '9') && var3 != '+' && var3 != '-' && var3 != '.') {
                  if (var3 == ':') {
                     return var1;
                  }

                  return -1;
               }

               ++var1;
            }

            return -1;
         }
      }

      private static int slashCount(String var0, int var1, int var2) {
         int var3;
         for(var3 = 0; var1 < var2; ++var1) {
            char var4 = var0.charAt(var1);
            if (var4 != '\\' && var4 != '/') {
               break;
            }

            ++var3;
         }

         return var3;
      }

      public HttpUrl.Builder addEncodedPathSegment(String var1) {
         if (var1 != null) {
            this.push(var1, 0, var1.length(), false, true);
            return this;
         } else {
            throw new NullPointerException("encodedPathSegment == null");
         }
      }

      public HttpUrl.Builder addEncodedPathSegments(String var1) {
         if (var1 != null) {
            return this.addPathSegments(var1, true);
         } else {
            throw new NullPointerException("encodedPathSegments == null");
         }
      }

      public HttpUrl.Builder addEncodedQueryParameter(String var1, @Nullable String var2) {
         if (var1 != null) {
            if (this.encodedQueryNamesAndValues == null) {
               this.encodedQueryNamesAndValues = new ArrayList();
            }

            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(var1, " \"'<>#&=", true, false, true, true));
            List var3 = this.encodedQueryNamesAndValues;
            if (var2 != null) {
               var1 = HttpUrl.canonicalize(var2, " \"'<>#&=", true, false, true, true);
            } else {
               var1 = null;
            }

            var3.add(var1);
            return this;
         } else {
            throw new NullPointerException("encodedName == null");
         }
      }

      public HttpUrl.Builder addPathSegment(String var1) {
         if (var1 != null) {
            this.push(var1, 0, var1.length(), false, false);
            return this;
         } else {
            throw new NullPointerException("pathSegment == null");
         }
      }

      public HttpUrl.Builder addPathSegments(String var1) {
         if (var1 != null) {
            return this.addPathSegments(var1, false);
         } else {
            throw new NullPointerException("pathSegments == null");
         }
      }

      public HttpUrl.Builder addQueryParameter(String var1, @Nullable String var2) {
         if (var1 != null) {
            if (this.encodedQueryNamesAndValues == null) {
               this.encodedQueryNamesAndValues = new ArrayList();
            }

            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(var1, " \"'<>#&=", false, false, true, true));
            List var3 = this.encodedQueryNamesAndValues;
            if (var2 != null) {
               var1 = HttpUrl.canonicalize(var2, " \"'<>#&=", false, false, true, true);
            } else {
               var1 = null;
            }

            var3.add(var1);
            return this;
         } else {
            throw new NullPointerException("name == null");
         }
      }

      public HttpUrl build() {
         if (this.scheme != null) {
            if (this.host != null) {
               return new HttpUrl(this);
            } else {
               throw new IllegalStateException("host == null");
            }
         } else {
            throw new IllegalStateException("scheme == null");
         }
      }

      int effectivePort() {
         int var1 = this.port;
         return var1 != -1 ? var1 : HttpUrl.defaultPort(this.scheme);
      }

      public HttpUrl.Builder encodedFragment(@Nullable String var1) {
         if (var1 != null) {
            var1 = HttpUrl.canonicalize(var1, "", true, false, false, false);
         } else {
            var1 = null;
         }

         this.encodedFragment = var1;
         return this;
      }

      public HttpUrl.Builder encodedPassword(String var1) {
         if (var1 != null) {
            this.encodedPassword = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
         } else {
            throw new NullPointerException("encodedPassword == null");
         }
      }

      public HttpUrl.Builder encodedPath(String var1) {
         if (var1 != null) {
            if (var1.startsWith("/")) {
               this.resolvePath(var1, 0, var1.length());
               return this;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("unexpected encodedPath: ");
               var2.append(var1);
               throw new IllegalArgumentException(var2.toString());
            }
         } else {
            throw new NullPointerException("encodedPath == null");
         }
      }

      public HttpUrl.Builder encodedQuery(@Nullable String var1) {
         List var2;
         if (var1 != null) {
            var2 = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var1, " \"'<>#", true, false, true, true));
         } else {
            var2 = null;
         }

         this.encodedQueryNamesAndValues = var2;
         return this;
      }

      public HttpUrl.Builder encodedUsername(String var1) {
         if (var1 != null) {
            this.encodedUsername = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
         } else {
            throw new NullPointerException("encodedUsername == null");
         }
      }

      public HttpUrl.Builder fragment(@Nullable String var1) {
         if (var1 != null) {
            var1 = HttpUrl.canonicalize(var1, "", false, false, false, false);
         } else {
            var1 = null;
         }

         this.encodedFragment = var1;
         return this;
      }

      public HttpUrl.Builder host(String var1) {
         if (var1 != null) {
            String var2 = canonicalizeHost(var1, 0, var1.length());
            if (var2 != null) {
               this.host = var2;
               return this;
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append("unexpected host: ");
               var3.append(var1);
               throw new IllegalArgumentException(var3.toString());
            }
         } else {
            throw new NullPointerException("host == null");
         }
      }

      HttpUrl.Builder.ParseResult parse(@Nullable HttpUrl var1, String var2) {
         int var3 = Util.skipLeadingAsciiWhitespace(var2, 0, var2.length());
         int var8 = Util.skipTrailingAsciiWhitespace(var2, var3, var2.length());
         if (schemeDelimiterOffset(var2, var3, var8) != -1) {
            if (var2.regionMatches(true, var3, "https:", 0, 6)) {
               this.scheme = "https";
               var3 += "https:".length();
            } else {
               if (!var2.regionMatches(true, var3, "http:", 0, 5)) {
                  return HttpUrl.Builder.ParseResult.UNSUPPORTED_SCHEME;
               }

               this.scheme = "http";
               var3 += "http:".length();
            }
         } else {
            if (var1 == null) {
               return HttpUrl.Builder.ParseResult.MISSING_SCHEME;
            }

            this.scheme = var1.scheme;
         }

         int var4 = slashCount(var2, var3, var8);
         if (var4 < 2 && var1 != null && var1.scheme.equals(this.scheme)) {
            this.encodedUsername = var1.encodedUsername();
            this.encodedPassword = var1.encodedPassword();
            this.host = var1.host;
            this.port = var1.port;
            this.encodedPathSegments.clear();
            this.encodedPathSegments.addAll(var1.encodedPathSegments());
            if (var3 == var8 || var2.charAt(var3) == '#') {
               this.encodedQuery(var1.encodedQuery());
            }
         } else {
            int var6 = var3 + var4;
            boolean var13 = false;
            boolean var14 = false;

            while(true) {
               int var5 = Util.delimiterOffset(var2, var6, var8, "@/\\?#");
               int var7;
               if (var5 != var8) {
                  var7 = var2.charAt(var5);
               } else {
                  var7 = -1;
               }

               if (var7 == -1 || var7 == 35 || var7 == 47 || var7 == 92 || var7 == 63) {
                  var3 = portColonOffset(var2, var6, var5);
                  if (var3 + 1 < var5) {
                     this.host = canonicalizeHost(var2, var6, var3);
                     var3 = parsePort(var2, var3 + 1, var5);
                     this.port = var3;
                     if (var3 == -1) {
                        return HttpUrl.Builder.ParseResult.INVALID_PORT;
                     }
                  } else {
                     this.host = canonicalizeHost(var2, var6, var3);
                     this.port = HttpUrl.defaultPort(this.scheme);
                  }

                  if (this.host == null) {
                     return HttpUrl.Builder.ParseResult.INVALID_HOST;
                  }

                  var3 = var5;
                  break;
               }

               if (var7 == 64) {
                  if (!var14) {
                     int var9 = Util.delimiterOffset(var2, var6, var5, ':');
                     String var11 = HttpUrl.canonicalize(var2, var6, var9, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                     if (var13) {
                        StringBuilder var10 = new StringBuilder();
                        var10.append(this.encodedUsername);
                        var10.append("%40");
                        var10.append(var11);
                        var11 = var10.toString();
                     }

                     this.encodedUsername = var11;
                     if (var9 != var5) {
                        var14 = true;
                        this.encodedPassword = HttpUrl.canonicalize(var2, var9 + 1, var5, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                     }

                     var13 = true;
                  } else {
                     StringBuilder var12 = new StringBuilder();
                     var12.append(this.encodedPassword);
                     var12.append("%40");
                     var12.append(HttpUrl.canonicalize(var2, var6, var5, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true));
                     this.encodedPassword = var12.toString();
                  }

                  var6 = var5 + 1;
               }
            }
         }

         var4 = Util.delimiterOffset(var2, var3, var8, "?#");
         this.resolvePath(var2, var3, var4);
         var3 = var4;
         var4 = var4;
         if (var3 < var8) {
            var4 = var3;
            if (var2.charAt(var3) == '?') {
               var4 = Util.delimiterOffset(var2, var3, var8, '#');
               this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var2, var3 + 1, var4, " \"'<>#", true, false, true, true));
            }
         }

         if (var4 < var8 && var2.charAt(var4) == '#') {
            this.encodedFragment = HttpUrl.canonicalize(var2, var4 + 1, var8, "", true, false, false, false);
         }

         return HttpUrl.Builder.ParseResult.SUCCESS;
      }

      public HttpUrl.Builder password(String var1) {
         if (var1 != null) {
            this.encodedPassword = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
         } else {
            throw new NullPointerException("password == null");
         }
      }

      public HttpUrl.Builder port(int var1) {
         if (var1 > 0 && var1 <= 65535) {
            this.port = var1;
            return this;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("unexpected port: ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }
      }

      public HttpUrl.Builder query(@Nullable String var1) {
         List var2;
         if (var1 != null) {
            var2 = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var1, " \"'<>#", false, false, true, true));
         } else {
            var2 = null;
         }

         this.encodedQueryNamesAndValues = var2;
         return this;
      }

      HttpUrl.Builder reencodeForUri() {
         int var1 = 0;

         int var2;
         String var3;
         for(var2 = this.encodedPathSegments.size(); var1 < var2; ++var1) {
            var3 = (String)this.encodedPathSegments.get(var1);
            this.encodedPathSegments.set(var1, HttpUrl.canonicalize(var3, "[]", true, true, false, true));
         }

         List var4 = this.encodedQueryNamesAndValues;
         if (var4 != null) {
            var1 = 0;

            for(var2 = var4.size(); var1 < var2; ++var1) {
               var3 = (String)this.encodedQueryNamesAndValues.get(var1);
               if (var3 != null) {
                  this.encodedQueryNamesAndValues.set(var1, HttpUrl.canonicalize(var3, "\\^`{|}", true, true, true, true));
               }
            }
         }

         var3 = this.encodedFragment;
         if (var3 != null) {
            this.encodedFragment = HttpUrl.canonicalize(var3, " \"#<>\\^`{|}", true, true, false, false);
         }

         return this;
      }

      public HttpUrl.Builder removeAllEncodedQueryParameters(String var1) {
         if (var1 != null) {
            if (this.encodedQueryNamesAndValues == null) {
               return this;
            } else {
               this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(var1, " \"'<>#&=", true, false, true, true));
               return this;
            }
         } else {
            throw new NullPointerException("encodedName == null");
         }
      }

      public HttpUrl.Builder removeAllQueryParameters(String var1) {
         if (var1 != null) {
            if (this.encodedQueryNamesAndValues == null) {
               return this;
            } else {
               this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(var1, " \"'<>#&=", false, false, true, true));
               return this;
            }
         } else {
            throw new NullPointerException("name == null");
         }
      }

      public HttpUrl.Builder removePathSegment(int var1) {
         this.encodedPathSegments.remove(var1);
         if (this.encodedPathSegments.isEmpty()) {
            this.encodedPathSegments.add("");
         }

         return this;
      }

      public HttpUrl.Builder scheme(String var1) {
         if (var1 != null) {
            if (var1.equalsIgnoreCase("http")) {
               this.scheme = "http";
               return this;
            } else if (var1.equalsIgnoreCase("https")) {
               this.scheme = "https";
               return this;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("unexpected scheme: ");
               var2.append(var1);
               throw new IllegalArgumentException(var2.toString());
            }
         } else {
            throw new NullPointerException("scheme == null");
         }
      }

      public HttpUrl.Builder setEncodedPathSegment(int var1, String var2) {
         if (var2 != null) {
            String var3 = HttpUrl.canonicalize(var2, 0, var2.length(), " \"<>^`{}|/\\?#", true, false, false, true);
            this.encodedPathSegments.set(var1, var3);
            if (!this.isDot(var3) && !this.isDotDot(var3)) {
               return this;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("unexpected path segment: ");
               var4.append(var2);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            throw new NullPointerException("encodedPathSegment == null");
         }
      }

      public HttpUrl.Builder setEncodedQueryParameter(String var1, @Nullable String var2) {
         this.removeAllEncodedQueryParameters(var1);
         this.addEncodedQueryParameter(var1, var2);
         return this;
      }

      public HttpUrl.Builder setPathSegment(int var1, String var2) {
         if (var2 != null) {
            String var3 = HttpUrl.canonicalize(var2, 0, var2.length(), " \"<>^`{}|/\\?#", false, false, false, true);
            if (!this.isDot(var3) && !this.isDotDot(var3)) {
               this.encodedPathSegments.set(var1, var3);
               return this;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("unexpected path segment: ");
               var4.append(var2);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            throw new NullPointerException("pathSegment == null");
         }
      }

      public HttpUrl.Builder setQueryParameter(String var1, @Nullable String var2) {
         this.removeAllQueryParameters(var1);
         this.addQueryParameter(var1, var2);
         return this;
      }

      public String toString() {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.scheme);
         var2.append("://");
         if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
            var2.append(this.encodedUsername);
            if (!this.encodedPassword.isEmpty()) {
               var2.append(':');
               var2.append(this.encodedPassword);
            }

            var2.append('@');
         }

         if (this.host.indexOf(58) != -1) {
            var2.append('[');
            var2.append(this.host);
            var2.append(']');
         } else {
            var2.append(this.host);
         }

         int var1 = this.effectivePort();
         if (var1 != HttpUrl.defaultPort(this.scheme)) {
            var2.append(':');
            var2.append(var1);
         }

         HttpUrl.pathSegmentsToString(var2, this.encodedPathSegments);
         if (this.encodedQueryNamesAndValues != null) {
            var2.append('?');
            HttpUrl.namesAndValuesToQueryString(var2, this.encodedQueryNamesAndValues);
         }

         if (this.encodedFragment != null) {
            var2.append('#');
            var2.append(this.encodedFragment);
         }

         return var2.toString();
      }

      public HttpUrl.Builder username(String var1) {
         if (var1 != null) {
            this.encodedUsername = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
         } else {
            throw new NullPointerException("username == null");
         }
      }

      static enum ParseResult {
         INVALID_HOST,
         INVALID_PORT,
         MISSING_SCHEME,
         SUCCESS,
         UNSUPPORTED_SCHEME;

         static {
            HttpUrl.Builder.ParseResult var0 = new HttpUrl.Builder.ParseResult("INVALID_HOST", 4);
            INVALID_HOST = var0;
         }
      }
   }
}
