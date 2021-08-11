package okhttp3;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.http.HttpHeaders;

public final class CacheControl {
   public static final CacheControl FORCE_CACHE;
   public static final CacheControl FORCE_NETWORK = (new CacheControl.Builder()).noCache().build();
   @Nullable
   String headerValue;
   private final boolean immutable;
   private final boolean isPrivate;
   private final boolean isPublic;
   private final int maxAgeSeconds;
   private final int maxStaleSeconds;
   private final int minFreshSeconds;
   private final boolean mustRevalidate;
   private final boolean noCache;
   private final boolean noStore;
   private final boolean noTransform;
   private final boolean onlyIfCached;
   private final int sMaxAgeSeconds;

   static {
      FORCE_CACHE = (new CacheControl.Builder()).onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
   }

   CacheControl(CacheControl.Builder var1) {
      this.noCache = var1.noCache;
      this.noStore = var1.noStore;
      this.maxAgeSeconds = var1.maxAgeSeconds;
      this.sMaxAgeSeconds = -1;
      this.isPrivate = false;
      this.isPublic = false;
      this.mustRevalidate = false;
      this.maxStaleSeconds = var1.maxStaleSeconds;
      this.minFreshSeconds = var1.minFreshSeconds;
      this.onlyIfCached = var1.onlyIfCached;
      this.noTransform = var1.noTransform;
      this.immutable = var1.immutable;
   }

   private CacheControl(boolean var1, boolean var2, int var3, int var4, boolean var5, boolean var6, boolean var7, int var8, int var9, boolean var10, boolean var11, boolean var12, @Nullable String var13) {
      this.noCache = var1;
      this.noStore = var2;
      this.maxAgeSeconds = var3;
      this.sMaxAgeSeconds = var4;
      this.isPrivate = var5;
      this.isPublic = var6;
      this.mustRevalidate = var7;
      this.maxStaleSeconds = var8;
      this.minFreshSeconds = var9;
      this.onlyIfCached = var10;
      this.noTransform = var11;
      this.immutable = var12;
      this.headerValue = var13;
   }

   private String headerValue() {
      StringBuilder var1 = new StringBuilder();
      if (this.noCache) {
         var1.append("no-cache, ");
      }

      if (this.noStore) {
         var1.append("no-store, ");
      }

      if (this.maxAgeSeconds != -1) {
         var1.append("max-age=");
         var1.append(this.maxAgeSeconds);
         var1.append(", ");
      }

      if (this.sMaxAgeSeconds != -1) {
         var1.append("s-maxage=");
         var1.append(this.sMaxAgeSeconds);
         var1.append(", ");
      }

      if (this.isPrivate) {
         var1.append("private, ");
      }

      if (this.isPublic) {
         var1.append("public, ");
      }

      if (this.mustRevalidate) {
         var1.append("must-revalidate, ");
      }

      if (this.maxStaleSeconds != -1) {
         var1.append("max-stale=");
         var1.append(this.maxStaleSeconds);
         var1.append(", ");
      }

      if (this.minFreshSeconds != -1) {
         var1.append("min-fresh=");
         var1.append(this.minFreshSeconds);
         var1.append(", ");
      }

      if (this.onlyIfCached) {
         var1.append("only-if-cached, ");
      }

      if (this.noTransform) {
         var1.append("no-transform, ");
      }

      if (this.immutable) {
         var1.append("immutable, ");
      }

      if (var1.length() == 0) {
         return "";
      } else {
         var1.delete(var1.length() - 2, var1.length());
         return var1.toString();
      }
   }

   public static CacheControl parse(Headers var0) {
      boolean var10 = false;
      int var1 = -1;
      int var6 = -1;
      boolean var17 = false;
      boolean var16 = false;
      boolean var14 = false;
      int var5 = -1;
      int var4 = -1;
      boolean var13 = false;
      boolean var12 = false;
      boolean var15 = false;
      boolean var2 = true;
      String var18 = null;
      int var7 = 0;
      boolean var11 = false;

      for(int var8 = var0.size(); var7 < var8; ++var7) {
         String var20 = var0.name(var7);
         String var21 = var0.value(var7);
         if (var20.equalsIgnoreCase("Cache-Control")) {
            if (var18 != null) {
               var2 = false;
            } else {
               var18 = var21;
            }
         } else {
            if (!var20.equalsIgnoreCase("Pragma")) {
               continue;
            }

            var2 = false;
         }

         int var3 = 0;

         while(var3 < var21.length()) {
            int var9 = HttpHeaders.skipUntil(var21, var3, "=,;");
            String var22 = var21.substring(var3, var9).trim();
            String var19;
            if (var9 != var21.length() && var21.charAt(var9) != ',' && var21.charAt(var9) != ';') {
               var9 = HttpHeaders.skipWhitespace(var21, var9 + 1);
               if (var9 < var21.length() && var21.charAt(var9) == '"') {
                  var3 = var9 + 1;
                  var9 = HttpHeaders.skipUntil(var21, var3, "\"");
                  var19 = var21.substring(var3, var9);
                  var3 = var9 + 1;
               } else {
                  var3 = HttpHeaders.skipUntil(var21, var9, ",;");
                  var19 = var21.substring(var9, var3).trim();
               }
            } else {
               var3 = var9 + 1;
               var19 = null;
            }

            if ("no-cache".equalsIgnoreCase(var22)) {
               var11 = true;
            } else if ("no-store".equalsIgnoreCase(var22)) {
               var10 = true;
            } else if ("max-age".equalsIgnoreCase(var22)) {
               var1 = HttpHeaders.parseSeconds(var19, -1);
            } else if ("s-maxage".equalsIgnoreCase(var22)) {
               var6 = HttpHeaders.parseSeconds(var19, -1);
            } else if ("private".equalsIgnoreCase(var22)) {
               var17 = true;
            } else if ("public".equalsIgnoreCase(var22)) {
               var16 = true;
            } else if ("must-revalidate".equalsIgnoreCase(var22)) {
               var14 = true;
            } else if ("max-stale".equalsIgnoreCase(var22)) {
               var5 = HttpHeaders.parseSeconds(var19, Integer.MAX_VALUE);
            } else if ("min-fresh".equalsIgnoreCase(var22)) {
               var4 = HttpHeaders.parseSeconds(var19, -1);
            } else if ("only-if-cached".equalsIgnoreCase(var22)) {
               var13 = true;
            } else if ("no-transform".equalsIgnoreCase(var22)) {
               var12 = true;
            } else if ("immutable".equalsIgnoreCase(var22)) {
               var15 = true;
            }
         }
      }

      if (!var2) {
         var18 = null;
      }

      return new CacheControl(var11, var10, var1, var6, var17, var16, var14, var5, var4, var13, var12, var15, var18);
   }

   public boolean immutable() {
      return this.immutable;
   }

   public boolean isPrivate() {
      return this.isPrivate;
   }

   public boolean isPublic() {
      return this.isPublic;
   }

   public int maxAgeSeconds() {
      return this.maxAgeSeconds;
   }

   public int maxStaleSeconds() {
      return this.maxStaleSeconds;
   }

   public int minFreshSeconds() {
      return this.minFreshSeconds;
   }

   public boolean mustRevalidate() {
      return this.mustRevalidate;
   }

   public boolean noCache() {
      return this.noCache;
   }

   public boolean noStore() {
      return this.noStore;
   }

   public boolean noTransform() {
      return this.noTransform;
   }

   public boolean onlyIfCached() {
      return this.onlyIfCached;
   }

   public int sMaxAgeSeconds() {
      return this.sMaxAgeSeconds;
   }

   public String toString() {
      String var1 = this.headerValue;
      if (var1 != null) {
         return var1;
      } else {
         var1 = this.headerValue();
         this.headerValue = var1;
         return var1;
      }
   }

   public static final class Builder {
      boolean immutable;
      int maxAgeSeconds = -1;
      int maxStaleSeconds = -1;
      int minFreshSeconds = -1;
      boolean noCache;
      boolean noStore;
      boolean noTransform;
      boolean onlyIfCached;

      public CacheControl build() {
         return new CacheControl(this);
      }

      public CacheControl.Builder immutable() {
         this.immutable = true;
         return this;
      }

      public CacheControl.Builder maxAge(int var1, TimeUnit var2) {
         if (var1 >= 0) {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.maxAgeSeconds = var1;
            return this;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("maxAge < 0: ");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         }
      }

      public CacheControl.Builder maxStale(int var1, TimeUnit var2) {
         if (var1 >= 0) {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.maxStaleSeconds = var1;
            return this;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("maxStale < 0: ");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         }
      }

      public CacheControl.Builder minFresh(int var1, TimeUnit var2) {
         if (var1 >= 0) {
            long var3 = var2.toSeconds((long)var1);
            if (var3 > 2147483647L) {
               var1 = Integer.MAX_VALUE;
            } else {
               var1 = (int)var3;
            }

            this.minFreshSeconds = var1;
            return this;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("minFresh < 0: ");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         }
      }

      public CacheControl.Builder noCache() {
         this.noCache = true;
         return this;
      }

      public CacheControl.Builder noStore() {
         this.noStore = true;
         return this;
      }

      public CacheControl.Builder noTransform() {
         this.noTransform = true;
         return this;
      }

      public CacheControl.Builder onlyIfCached() {
         this.onlyIfCached = true;
         return this;
      }
   }
}
