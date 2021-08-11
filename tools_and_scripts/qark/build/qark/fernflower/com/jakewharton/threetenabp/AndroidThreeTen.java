package com.jakewharton.threetenabp;

import android.app.Application;
import android.content.Context;
import java.util.concurrent.atomic.AtomicBoolean;
import org.threeten.bp.zone.ZoneRulesInitializer;

public final class AndroidThreeTen {
   private static final AtomicBoolean initialized = new AtomicBoolean();

   private AndroidThreeTen() {
      throw new AssertionError();
   }

   public static void init(Application var0) {
      init((Context)var0);
   }

   public static void init(Context var0) {
      init(var0, "org/threeten/bp/TZDB.dat");
   }

   public static void init(Context var0, String var1) {
      if (!initialized.getAndSet(true)) {
         ZoneRulesInitializer.setInitializer(new AssetsZoneRulesInitializer(var0, var1));
      }

   }
}
