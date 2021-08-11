package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
   private final List encoders = new ArrayList();

   public void append(Class var1, ResourceEncoder var2) {
      synchronized(this){}

      try {
         this.encoders.add(new ResourceEncoderRegistry.Entry(var1, var2));
      } finally {
         ;
      }

   }

   public ResourceEncoder get(Class var1) {
      synchronized(this){}
      int var2 = 0;

      Throwable var10000;
      label97: {
         boolean var10001;
         int var3;
         try {
            var3 = this.encoders.size();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label97;
         }

         while(true) {
            if (var2 >= var3) {
               return null;
            }

            try {
               ResourceEncoderRegistry.Entry var4 = (ResourceEncoderRegistry.Entry)this.encoders.get(var2);
               if (var4.handles(var1)) {
                  ResourceEncoder var12 = var4.encoder;
                  return var12;
               }
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      Throwable var11 = var10000;
      throw var11;
   }

   public void prepend(Class var1, ResourceEncoder var2) {
      synchronized(this){}

      try {
         this.encoders.add(0, new ResourceEncoderRegistry.Entry(var1, var2));
      } finally {
         ;
      }

   }

   private static final class Entry {
      final ResourceEncoder encoder;
      private final Class resourceClass;

      Entry(Class var1, ResourceEncoder var2) {
         this.resourceClass = var1;
         this.encoder = var2;
      }

      boolean handles(Class var1) {
         return this.resourceClass.isAssignableFrom(var1);
      }
   }
}
