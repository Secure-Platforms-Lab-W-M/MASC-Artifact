package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;

public final class LazyBitmapDrawableResource implements Resource, Initializable {
   private final Resource bitmapResource;
   private final Resources resources;

   private LazyBitmapDrawableResource(Resources var1, Resource var2) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
      this.bitmapResource = (Resource)Preconditions.checkNotNull(var2);
   }

   public static Resource obtain(Resources var0, Resource var1) {
      return var1 == null ? null : new LazyBitmapDrawableResource(var0, var1);
   }

   @Deprecated
   public static LazyBitmapDrawableResource obtain(Context var0, Bitmap var1) {
      return (LazyBitmapDrawableResource)obtain((Resources)var0.getResources(), (Resource)BitmapResource.obtain(var1, Glide.get(var0).getBitmapPool()));
   }

   @Deprecated
   public static LazyBitmapDrawableResource obtain(Resources var0, BitmapPool var1, Bitmap var2) {
      return (LazyBitmapDrawableResource)obtain((Resources)var0, (Resource)BitmapResource.obtain(var2, var1));
   }

   public BitmapDrawable get() {
      return new BitmapDrawable(this.resources, (Bitmap)this.bitmapResource.get());
   }

   public Class getResourceClass() {
      return BitmapDrawable.class;
   }

   public int getSize() {
      return this.bitmapResource.getSize();
   }

   public void initialize() {
      Resource var1 = this.bitmapResource;
      if (var1 instanceof Initializable) {
         ((Initializable)var1).initialize();
      }

   }

   public void recycle() {
      this.bitmapResource.recycle();
   }
}
