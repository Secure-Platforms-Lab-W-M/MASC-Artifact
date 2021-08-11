package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;

public abstract class CustomTarget implements Target {
   private final int height;
   private Request request;
   private final int width;

   public CustomTarget() {
      this(Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public CustomTarget(int var1, int var2) {
      if (Util.isValidDimensions(var1, var2)) {
         this.width = var1;
         this.height = var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
         var3.append(var1);
         var3.append(" and height: ");
         var3.append(var2);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public final Request getRequest() {
      return this.request;
   }

   public final void getSize(SizeReadyCallback var1) {
      var1.onSizeReady(this.width, this.height);
   }

   public void onDestroy() {
   }

   public void onLoadFailed(Drawable var1) {
   }

   public void onLoadStarted(Drawable var1) {
   }

   public void onStart() {
   }

   public void onStop() {
   }

   public final void removeCallback(SizeReadyCallback var1) {
   }

   public final void setRequest(Request var1) {
      this.request = var1;
   }
}
