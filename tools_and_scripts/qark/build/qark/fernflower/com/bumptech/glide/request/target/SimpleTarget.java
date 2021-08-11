package com.bumptech.glide.request.target;

import com.bumptech.glide.util.Util;

@Deprecated
public abstract class SimpleTarget extends BaseTarget {
   private final int height;
   private final int width;

   public SimpleTarget() {
      this(Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public SimpleTarget(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public final void getSize(SizeReadyCallback var1) {
      if (Util.isValidDimensions(this.width, this.height)) {
         var1.onSizeReady(this.width, this.height);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
         var2.append(this.width);
         var2.append(" and height: ");
         var2.append(this.height);
         var2.append(", either provide dimensions in the constructor or call override()");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void removeCallback(SizeReadyCallback var1) {
   }
}
