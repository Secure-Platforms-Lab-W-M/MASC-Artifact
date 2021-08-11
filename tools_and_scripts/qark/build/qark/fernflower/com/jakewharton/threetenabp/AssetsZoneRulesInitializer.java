package com.jakewharton.threetenabp;

import android.content.Context;
import org.threeten.bp.zone.ZoneRulesInitializer;

final class AssetsZoneRulesInitializer extends ZoneRulesInitializer {
   private final String assetPath;
   private final Context context;

   AssetsZoneRulesInitializer(Context var1, String var2) {
      this.context = var1;
      this.assetPath = var2;
   }

   protected void initializeProviders() {
      // $FF: Couldn't be decompiled
   }
}
