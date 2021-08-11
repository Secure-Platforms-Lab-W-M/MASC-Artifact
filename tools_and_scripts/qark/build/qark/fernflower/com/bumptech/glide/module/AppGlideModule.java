package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;

public abstract class AppGlideModule extends LibraryGlideModule implements AppliesOptions {
   public void applyOptions(Context var1, GlideBuilder var2) {
   }

   public boolean isManifestParsingEnabled() {
      return true;
   }
}
