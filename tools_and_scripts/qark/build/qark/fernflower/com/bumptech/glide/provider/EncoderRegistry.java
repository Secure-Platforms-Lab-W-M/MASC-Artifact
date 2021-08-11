package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EncoderRegistry {
   private final List encoders = new ArrayList();

   public void append(Class var1, Encoder var2) {
      synchronized(this){}

      try {
         this.encoders.add(new EncoderRegistry.Entry(var1, var2));
      } finally {
         ;
      }

   }

   public Encoder getEncoder(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label113: {
         boolean var10001;
         Iterator var2;
         try {
            var2 = this.encoders.iterator();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label113;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  EncoderRegistry.Entry var3 = (EncoderRegistry.Entry)var2.next();
                  if (!var3.handles(var1)) {
                     continue;
                  }

                  Encoder var11 = var3.encoder;
                  return var11;
               }
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            return null;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public void prepend(Class var1, Encoder var2) {
      synchronized(this){}

      try {
         this.encoders.add(0, new EncoderRegistry.Entry(var1, var2));
      } finally {
         ;
      }

   }

   private static final class Entry {
      private final Class dataClass;
      final Encoder encoder;

      Entry(Class var1, Encoder var2) {
         this.dataClass = var1;
         this.encoder = var2;
      }

      boolean handles(Class var1) {
         return this.dataClass.isAssignableFrom(var1);
      }
   }
}
