package org.apache.http.impl.client;

import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.IgnoreSpecProvider;
import org.apache.http.impl.cookie.NetscapeDraftSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;

public final class CookieSpecRegistries {
   private CookieSpecRegistries() {
   }

   public static Lookup createDefault() {
      return createDefault(PublicSuffixMatcherLoader.getDefault());
   }

   public static Lookup createDefault(PublicSuffixMatcher var0) {
      return createDefaultBuilder(var0).build();
   }

   public static RegistryBuilder createDefaultBuilder() {
      return createDefaultBuilder(PublicSuffixMatcherLoader.getDefault());
   }

   public static RegistryBuilder createDefaultBuilder(PublicSuffixMatcher var0) {
      DefaultCookieSpecProvider var1 = new DefaultCookieSpecProvider(var0);
      RFC6265CookieSpecProvider var2 = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, var0);
      RFC6265CookieSpecProvider var3 = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.STRICT, var0);
      return RegistryBuilder.create().register("default", var1).register("best-match", var1).register("compatibility", var1).register("standard", var2).register("standard-strict", var3).register("netscape", new NetscapeDraftSpecProvider()).register("ignoreCookies", new IgnoreSpecProvider());
   }
}
