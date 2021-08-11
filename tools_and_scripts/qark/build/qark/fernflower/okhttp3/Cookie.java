package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;

public final class Cookie {
   private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
   private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
   private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
   private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
   private final String domain;
   private final long expiresAt;
   private final boolean hostOnly;
   private final boolean httpOnly;
   private final String name;
   private final String path;
   private final boolean persistent;
   private final boolean secure;
   private final String value;

   private Cookie(String var1, String var2, long var3, String var5, String var6, boolean var7, boolean var8, boolean var9, boolean var10) {
      this.name = var1;
      this.value = var2;
      this.expiresAt = var3;
      this.domain = var5;
      this.path = var6;
      this.secure = var7;
      this.httpOnly = var8;
      this.hostOnly = var9;
      this.persistent = var10;
   }

   Cookie(Cookie.Builder var1) {
      if (var1.name != null) {
         if (var1.value != null) {
            if (var1.domain != null) {
               this.name = var1.name;
               this.value = var1.value;
               this.expiresAt = var1.expiresAt;
               this.domain = var1.domain;
               this.path = var1.path;
               this.secure = var1.secure;
               this.httpOnly = var1.httpOnly;
               this.persistent = var1.persistent;
               this.hostOnly = var1.hostOnly;
            } else {
               throw new NullPointerException("builder.domain == null");
            }
         } else {
            throw new NullPointerException("builder.value == null");
         }
      } else {
         throw new NullPointerException("builder.name == null");
      }
   }

   private static int dateCharacterOffset(String var0, int var1, int var2, boolean var3) {
      while(var1 < var2) {
         char var4 = var0.charAt(var1);
         boolean var5;
         if ((var4 >= ' ' || var4 == '\t') && var4 < 127 && (var4 < '0' || var4 > '9') && (var4 < 'a' || var4 > 'z') && (var4 < 'A' || var4 > 'Z') && var4 != ':') {
            var5 = false;
         } else {
            var5 = true;
         }

         if (var5 == (var3 ^ true)) {
            return var1;
         }

         ++var1;
      }

      return var2;
   }

   private static boolean domainMatch(String var0, String var1) {
      if (var0.equals(var1)) {
         return true;
      } else {
         return var0.endsWith(var1) && var0.charAt(var0.length() - var1.length() - 1) == '.' && !Util.verifyAsIpAddress(var0);
      }
   }

   @Nullable
   static Cookie parse(long var0, HttpUrl var2, String var3) {
      int var5 = var3.length();
      int var4 = Util.delimiterOffset(var3, 0, var5, ';');
      int var6 = Util.delimiterOffset(var3, 0, var4, '=');
      if (var6 == var4) {
         return null;
      } else {
         String var27 = Util.trimSubstring(var3, 0, var6);
         if (var27.isEmpty()) {
            return null;
         } else if (Util.indexOfControlOrNonAscii(var27) != -1) {
            return null;
         } else {
            String var28 = Util.trimSubstring(var3, var6 + 1, var4);
            if (Util.indexOfControlOrNonAscii(var28) != -1) {
               return null;
            } else {
               long var8 = 253402300799999L;
               String var24 = null;
               String var23 = null;
               long var10 = -1L;
               boolean var19 = false;
               boolean var20 = false;
               boolean var17 = true;
               boolean var18 = false;
               ++var4;

               String var25;
               while(var4 < var5) {
                  var6 = Util.delimiterOffset(var3, var4, var5, ';');
                  int var7 = Util.delimiterOffset(var3, var4, var6, '=');
                  String var29 = Util.trimSubstring(var3, var4, var7);
                  if (var7 < var6) {
                     var25 = Util.trimSubstring(var3, var7 + 1, var6);
                  } else {
                     var25 = "";
                  }

                  long var12;
                  long var14;
                  boolean var16;
                  boolean var21;
                  boolean var22;
                  String var26;
                  if (var29.equalsIgnoreCase("expires")) {
                     label114: {
                        try {
                           var12 = parseExpires(var25, 0, var25.length());
                        } catch (IllegalArgumentException var32) {
                           var12 = var8;
                           var25 = var24;
                           var26 = var23;
                           var14 = var10;
                           var21 = var19;
                           var22 = var17;
                           var16 = var18;
                           break label114;
                        }

                        var16 = true;
                        var25 = var24;
                        var26 = var23;
                        var14 = var10;
                        var21 = var19;
                        var22 = var17;
                     }
                  } else if (var29.equalsIgnoreCase("max-age")) {
                     label110: {
                        try {
                           var14 = parseMaxAge(var25);
                        } catch (NumberFormatException var31) {
                           var12 = var8;
                           var25 = var24;
                           var26 = var23;
                           var14 = var10;
                           var21 = var19;
                           var22 = var17;
                           var16 = var18;
                           break label110;
                        }

                        var16 = true;
                        var12 = var8;
                        var25 = var24;
                        var26 = var23;
                        var21 = var19;
                        var22 = var17;
                     }
                  } else if (var29.equalsIgnoreCase("domain")) {
                     label106: {
                        try {
                           var25 = parseDomain(var25);
                        } catch (IllegalArgumentException var30) {
                           var12 = var8;
                           var25 = var24;
                           var26 = var23;
                           var14 = var10;
                           var21 = var19;
                           var22 = var17;
                           var16 = var18;
                           break label106;
                        }

                        var22 = false;
                        var12 = var8;
                        var26 = var23;
                        var14 = var10;
                        var21 = var19;
                        var16 = var18;
                     }
                  } else if (var29.equalsIgnoreCase("path")) {
                     var26 = var25;
                     var12 = var8;
                     var25 = var24;
                     var14 = var10;
                     var21 = var19;
                     var22 = var17;
                     var16 = var18;
                  } else if (var29.equalsIgnoreCase("secure")) {
                     var21 = true;
                     var12 = var8;
                     var25 = var24;
                     var26 = var23;
                     var14 = var10;
                     var22 = var17;
                     var16 = var18;
                  } else {
                     var12 = var8;
                     var25 = var24;
                     var26 = var23;
                     var14 = var10;
                     var21 = var19;
                     var22 = var17;
                     var16 = var18;
                     if (var29.equalsIgnoreCase("httponly")) {
                        var20 = true;
                        var16 = var18;
                        var22 = var17;
                        var21 = var19;
                        var14 = var10;
                        var26 = var23;
                        var25 = var24;
                        var12 = var8;
                     }
                  }

                  var4 = var6 + 1;
                  var8 = var12;
                  var24 = var25;
                  var23 = var26;
                  var10 = var14;
                  var19 = var21;
                  var17 = var22;
                  var18 = var16;
               }

               if (var10 == Long.MIN_VALUE) {
                  var0 = Long.MIN_VALUE;
               } else if (var10 != -1L) {
                  if (var10 <= 9223372036854775L) {
                     var8 = 1000L * var10;
                  } else {
                     var8 = Long.MAX_VALUE;
                  }

                  var8 += var0;
                  if (var8 >= var0 && var8 <= 253402300799999L) {
                     var0 = var8;
                  } else {
                     var0 = 253402300799999L;
                  }
               } else {
                  var0 = var8;
               }

               var25 = var2.host();
               if (var24 == null) {
                  var3 = var25;
               } else {
                  if (!domainMatch(var25, var24)) {
                     return null;
                  }

                  var3 = var24;
               }

               if (var25.length() != var3.length() && PublicSuffixDatabase.get().getEffectiveTldPlusOne(var3) == null) {
                  return null;
               } else {
                  var24 = "/";
                  String var33;
                  if (var23 != null && var23.startsWith("/")) {
                     var33 = var23;
                  } else {
                     var23 = var2.encodedPath();
                     var4 = var23.lastIndexOf(47);
                     var33 = var24;
                     if (var4 != 0) {
                        var33 = var23.substring(0, var4);
                     }
                  }

                  return new Cookie(var27, var28, var0, var3, var33, var19, var20, var17, var18);
               }
            }
         }
      }
   }

   @Nullable
   public static Cookie parse(HttpUrl var0, String var1) {
      return parse(System.currentTimeMillis(), var0, var1);
   }

   public static List parseAll(HttpUrl var0, Headers var1) {
      List var5 = var1.values("Set-Cookie");
      ArrayList var7 = null;
      int var2 = 0;

      for(int var3 = var5.size(); var2 < var3; ++var2) {
         Cookie var6 = parse(var0, (String)var5.get(var2));
         if (var6 != null) {
            ArrayList var4 = var7;
            if (var7 == null) {
               var4 = new ArrayList();
            }

            var4.add(var6);
            var7 = var4;
         }
      }

      if (var7 != null) {
         return Collections.unmodifiableList(var7);
      } else {
         return Collections.emptyList();
      }
   }

   private static String parseDomain(String var0) {
      if (!var0.endsWith(".")) {
         String var1 = var0;
         if (var0.startsWith(".")) {
            var1 = var0.substring(1);
         }

         var0 = Util.domainToAscii(var1);
         if (var0 != null) {
            return var0;
         } else {
            throw new IllegalArgumentException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static long parseExpires(String var0, int var1, int var2) {
      int var8 = dateCharacterOffset(var0, var1, var2, false);
      int var7 = -1;
      int var6 = -1;
      int var5 = -1;
      int var4 = -1;
      int var3 = -1;
      var1 = -1;

      int var14;
      for(Matcher var15 = TIME_PATTERN.matcher(var0); var8 < var2; var8 = var14) {
         var14 = dateCharacterOffset(var0, var8 + 1, var2, true);
         var15.region(var8, var14);
         int var9;
         int var10;
         int var11;
         int var12;
         int var13;
         if (var7 == -1 && var15.usePattern(TIME_PATTERN).matches()) {
            var8 = Integer.parseInt(var15.group(1));
            var9 = Integer.parseInt(var15.group(2));
            var10 = Integer.parseInt(var15.group(3));
            var11 = var4;
            var12 = var3;
            var13 = var1;
         } else if (var4 == -1 && var15.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
            var11 = Integer.parseInt(var15.group(1));
            var8 = var7;
            var9 = var6;
            var10 = var5;
            var12 = var3;
            var13 = var1;
         } else if (var3 == -1 && var15.usePattern(MONTH_PATTERN).matches()) {
            String var16 = var15.group(1).toLowerCase(Locale.US);
            var12 = MONTH_PATTERN.pattern().indexOf(var16) / 4;
            var8 = var7;
            var9 = var6;
            var10 = var5;
            var11 = var4;
            var13 = var1;
         } else {
            var8 = var7;
            var9 = var6;
            var10 = var5;
            var11 = var4;
            var12 = var3;
            var13 = var1;
            if (var1 == -1) {
               var8 = var7;
               var9 = var6;
               var10 = var5;
               var11 = var4;
               var12 = var3;
               var13 = var1;
               if (var15.usePattern(YEAR_PATTERN).matches()) {
                  var13 = Integer.parseInt(var15.group(1));
                  var12 = var3;
                  var11 = var4;
                  var10 = var5;
                  var9 = var6;
                  var8 = var7;
               }
            }
         }

         var14 = dateCharacterOffset(var0, var14 + 1, var2, false);
         var7 = var8;
         var6 = var9;
         var5 = var10;
         var4 = var11;
         var3 = var12;
         var1 = var13;
      }

      var2 = var1;
      if (var1 >= 70) {
         var2 = var1;
         if (var1 <= 99) {
            var2 = var1 + 1900;
         }
      }

      var1 = var2;
      if (var2 >= 0) {
         var1 = var2;
         if (var2 <= 69) {
            var1 = var2 + 2000;
         }
      }

      if (var1 >= 1601) {
         if (var3 != -1) {
            if (var4 >= 1 && var4 <= 31) {
               if (var7 >= 0 && var7 <= 23) {
                  if (var6 >= 0 && var6 <= 59) {
                     if (var5 >= 0 && var5 <= 59) {
                        GregorianCalendar var17 = new GregorianCalendar(Util.UTC);
                        var17.setLenient(false);
                        var17.set(1, var1);
                        var17.set(2, var3 - 1);
                        var17.set(5, var4);
                        var17.set(11, var7);
                        var17.set(12, var6);
                        var17.set(13, var5);
                        var17.set(14, 0);
                        return var17.getTimeInMillis();
                     } else {
                        throw new IllegalArgumentException();
                     }
                  } else {
                     throw new IllegalArgumentException();
                  }
               } else {
                  throw new IllegalArgumentException();
               }
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            throw new IllegalArgumentException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static long parseMaxAge(String var0) {
      long var1;
      try {
         var1 = Long.parseLong(var0);
      } catch (NumberFormatException var4) {
         if (var0.matches("-?\\d+")) {
            if (var0.startsWith("-")) {
               return Long.MIN_VALUE;
            }

            return Long.MAX_VALUE;
         }

         throw var4;
      }

      return var1 <= 0L ? Long.MIN_VALUE : var1;
   }

   private static boolean pathMatch(HttpUrl var0, String var1) {
      String var2 = var0.encodedPath();
      if (var2.equals(var1)) {
         return true;
      } else {
         if (var2.startsWith(var1)) {
            if (var1.endsWith("/")) {
               return true;
            }

            if (var2.charAt(var1.length()) == '/') {
               return true;
            }
         }

         return false;
      }
   }

   public String domain() {
      return this.domain;
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof Cookie)) {
         return false;
      } else {
         Cookie var2 = (Cookie)var1;
         return var2.name.equals(this.name) && var2.value.equals(this.value) && var2.domain.equals(this.domain) && var2.path.equals(this.path) && var2.expiresAt == this.expiresAt && var2.secure == this.secure && var2.httpOnly == this.httpOnly && var2.persistent == this.persistent && var2.hostOnly == this.hostOnly;
      }
   }

   public long expiresAt() {
      return this.expiresAt;
   }

   public int hashCode() {
      int var1 = this.name.hashCode();
      int var2 = this.value.hashCode();
      int var3 = this.domain.hashCode();
      int var4 = this.path.hashCode();
      long var5 = this.expiresAt;
      return ((((((((17 * 31 + var1) * 31 + var2) * 31 + var3) * 31 + var4) * 31 + (int)(var5 ^ var5 >>> 32)) * 31 + (this.secure ^ 1)) * 31 + (this.httpOnly ^ 1)) * 31 + (this.persistent ^ 1)) * 31 + (this.hostOnly ^ 1);
   }

   public boolean hostOnly() {
      return this.hostOnly;
   }

   public boolean httpOnly() {
      return this.httpOnly;
   }

   public boolean matches(HttpUrl var1) {
      boolean var2;
      if (this.hostOnly) {
         var2 = var1.host().equals(this.domain);
      } else {
         var2 = domainMatch(var1.host(), this.domain);
      }

      if (!var2) {
         return false;
      } else if (!pathMatch(var1, this.path)) {
         return false;
      } else {
         return !this.secure || var1.isHttps();
      }
   }

   public String name() {
      return this.name;
   }

   public String path() {
      return this.path;
   }

   public boolean persistent() {
      return this.persistent;
   }

   public boolean secure() {
      return this.secure;
   }

   public String toString() {
      return this.toString(false);
   }

   String toString(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.name);
      var2.append('=');
      var2.append(this.value);
      if (this.persistent) {
         if (this.expiresAt == Long.MIN_VALUE) {
            var2.append("; max-age=0");
         } else {
            var2.append("; expires=");
            var2.append(HttpDate.format(new Date(this.expiresAt)));
         }
      }

      if (!this.hostOnly) {
         var2.append("; domain=");
         if (var1) {
            var2.append(".");
         }

         var2.append(this.domain);
      }

      var2.append("; path=");
      var2.append(this.path);
      if (this.secure) {
         var2.append("; secure");
      }

      if (this.httpOnly) {
         var2.append("; httponly");
      }

      return var2.toString();
   }

   public String value() {
      return this.value;
   }

   public static final class Builder {
      String domain;
      long expiresAt = 253402300799999L;
      boolean hostOnly;
      boolean httpOnly;
      String name;
      String path = "/";
      boolean persistent;
      boolean secure;
      String value;

      private Cookie.Builder domain(String var1, boolean var2) {
         if (var1 != null) {
            String var3 = Util.domainToAscii(var1);
            if (var3 != null) {
               this.domain = var3;
               this.hostOnly = var2;
               return this;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("unexpected domain: ");
               var4.append(var1);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            throw new NullPointerException("domain == null");
         }
      }

      public Cookie build() {
         return new Cookie(this);
      }

      public Cookie.Builder domain(String var1) {
         return this.domain(var1, false);
      }

      public Cookie.Builder expiresAt(long var1) {
         long var3 = var1;
         if (var1 <= 0L) {
            var3 = Long.MIN_VALUE;
         }

         var1 = var3;
         if (var3 > 253402300799999L) {
            var1 = 253402300799999L;
         }

         this.expiresAt = var1;
         this.persistent = true;
         return this;
      }

      public Cookie.Builder hostOnlyDomain(String var1) {
         return this.domain(var1, true);
      }

      public Cookie.Builder httpOnly() {
         this.httpOnly = true;
         return this;
      }

      public Cookie.Builder name(String var1) {
         if (var1 != null) {
            if (var1.trim().equals(var1)) {
               this.name = var1;
               return this;
            } else {
               throw new IllegalArgumentException("name is not trimmed");
            }
         } else {
            throw new NullPointerException("name == null");
         }
      }

      public Cookie.Builder path(String var1) {
         if (var1.startsWith("/")) {
            this.path = var1;
            return this;
         } else {
            throw new IllegalArgumentException("path must start with '/'");
         }
      }

      public Cookie.Builder secure() {
         this.secure = true;
         return this;
      }

      public Cookie.Builder value(String var1) {
         if (var1 != null) {
            if (var1.trim().equals(var1)) {
               this.value = var1;
               return this;
            } else {
               throw new IllegalArgumentException("value is not trimmed");
            }
         } else {
            throw new NullPointerException("value == null");
         }
      }
   }
}
