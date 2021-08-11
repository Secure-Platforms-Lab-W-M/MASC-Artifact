package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class BytesResource implements Resource {
   private final byte[] bytes;

   public BytesResource(byte[] var1) {
      this.bytes = (byte[])Preconditions.checkNotNull(var1);
   }

   public byte[] get() {
      return this.bytes;
   }

   public Class getResourceClass() {
      return byte[].class;
   }

   public int getSize() {
      return this.bytes.length;
   }

   public void recycle() {
   }
}
