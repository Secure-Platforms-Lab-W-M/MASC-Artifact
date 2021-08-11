package com.bumptech.glide.load.resource;

import android.content.Context;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;

public final class UnitTransformation implements Transformation {
   private static final Transformation TRANSFORMATION = new UnitTransformation();

   private UnitTransformation() {
   }

   public static UnitTransformation get() {
      return (UnitTransformation)TRANSFORMATION;
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      return var2;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
   }
}
