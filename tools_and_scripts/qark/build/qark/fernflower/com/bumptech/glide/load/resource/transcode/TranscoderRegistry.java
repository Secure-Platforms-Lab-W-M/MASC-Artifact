package com.bumptech.glide.load.resource.transcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TranscoderRegistry {
   private final List transcoders = new ArrayList();

   public ResourceTranscoder get(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label258: {
         boolean var10001;
         ResourceTranscoder var25;
         label248: {
            try {
               if (!var2.isAssignableFrom(var1)) {
                  break label248;
               }

               var25 = UnitTranscoder.get();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label258;
            }

            return var25;
         }

         Iterator var3;
         try {
            var3 = this.transcoders.iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label258;
         }

         while(true) {
            try {
               if (!var3.hasNext()) {
                  break;
               }

               TranscoderRegistry.Entry var4 = (TranscoderRegistry.Entry)var3.next();
               if (var4.handles(var1, var2)) {
                  var25 = var4.transcoder;
                  return var25;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label258;
            }
         }

         label227:
         try {
            StringBuilder var27 = new StringBuilder();
            var27.append("No transcoder registered to transcode from ");
            var27.append(var1);
            var27.append(" to ");
            var27.append(var2);
            throw new IllegalArgumentException(var27.toString());
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label227;
         }
      }

      Throwable var26 = var10000;
      throw var26;
   }

   public List getTranscodeClasses(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label187: {
         boolean var10001;
         ArrayList var3;
         try {
            var3 = new ArrayList();
            if (var2.isAssignableFrom(var1)) {
               var3.add(var2);
               return var3;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label187;
         }

         Iterator var4;
         try {
            var4 = this.transcoders.iterator();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label187;
         }

         while(true) {
            try {
               if (var4.hasNext()) {
                  if (((TranscoderRegistry.Entry)var4.next()).handles(var1, var2)) {
                     var3.add(var2);
                  }
                  continue;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            return var3;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }

   public void register(Class var1, Class var2, ResourceTranscoder var3) {
      synchronized(this){}

      try {
         this.transcoders.add(new TranscoderRegistry.Entry(var1, var2, var3));
      } finally {
         ;
      }

   }

   private static final class Entry {
      private final Class fromClass;
      private final Class toClass;
      final ResourceTranscoder transcoder;

      Entry(Class var1, Class var2, ResourceTranscoder var3) {
         this.fromClass = var1;
         this.toClass = var2;
         this.transcoder = var3;
      }

      public boolean handles(Class var1, Class var2) {
         return this.fromClass.isAssignableFrom(var1) && var2.isAssignableFrom(this.toClass);
      }
   }
}
