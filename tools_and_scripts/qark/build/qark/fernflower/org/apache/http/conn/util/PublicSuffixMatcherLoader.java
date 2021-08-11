package org.apache.http.conn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.http.Consts;
import org.apache.http.util.Args;

public final class PublicSuffixMatcherLoader {
   private static volatile PublicSuffixMatcher DEFAULT_INSTANCE;

   public static PublicSuffixMatcher getDefault() {
      // $FF: Couldn't be decompiled
   }

   public static PublicSuffixMatcher load(File var0) throws IOException {
      Args.notNull(var0, "File");
      FileInputStream var4 = new FileInputStream(var0);

      PublicSuffixMatcher var1;
      try {
         var1 = load((InputStream)var4);
      } finally {
         var4.close();
      }

      return var1;
   }

   private static PublicSuffixMatcher load(InputStream var0) throws IOException {
      return new PublicSuffixMatcher((new PublicSuffixListParser()).parseByType(new InputStreamReader(var0, Consts.UTF_8)));
   }

   public static PublicSuffixMatcher load(URL var0) throws IOException {
      Args.notNull(var0, "URL");
      InputStream var4 = var0.openStream();

      PublicSuffixMatcher var1;
      try {
         var1 = load(var4);
      } finally {
         var4.close();
      }

      return var1;
   }
}
