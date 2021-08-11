package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class SimpleResource implements Resource {
   protected final Object data;

   public SimpleResource(Object var1) {
      this.data = Preconditions.checkNotNull(var1);
   }

   public final Object get() {
      return this.data;
   }

   public Class getResourceClass() {
      return this.data.getClass();
   }

   public final int getSize() {
      return 1;
   }

   public void recycle() {
   }
}
