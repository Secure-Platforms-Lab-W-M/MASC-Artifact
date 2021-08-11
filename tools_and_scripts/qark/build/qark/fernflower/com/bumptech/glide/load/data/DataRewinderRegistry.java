package com.bumptech.glide.load.data;

import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataRewinderRegistry {
   private static final DataRewinder.Factory DEFAULT_FACTORY = new DataRewinder.Factory() {
      public DataRewinder build(Object var1) {
         return new DataRewinderRegistry.DefaultRewinder(var1);
      }

      public Class getDataClass() {
         throw new UnsupportedOperationException("Not implemented");
      }
   };
   private final Map rewinders = new HashMap();

   public DataRewinder build(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label290: {
         DataRewinder.Factory var3;
         boolean var10001;
         try {
            Preconditions.checkNotNull(var1);
            var3 = (DataRewinder.Factory)this.rewinders.get(var1.getClass());
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label290;
         }

         DataRewinder.Factory var2 = var3;
         if (var3 == null) {
            Iterator var4;
            try {
               var4 = this.rewinders.values().iterator();
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label290;
            }

            while(true) {
               var2 = var3;

               try {
                  if (!var4.hasNext()) {
                     break;
                  }

                  var2 = (DataRewinder.Factory)var4.next();
                  if (var2.getDataClass().isAssignableFrom(var1.getClass())) {
                     break;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label290;
               }
            }
         }

         var3 = var2;
         if (var2 == null) {
            try {
               var3 = DEFAULT_FACTORY;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label290;
            }
         }

         label266:
         try {
            DataRewinder var36 = var3.build(var1);
            return var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label266;
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }

   public void register(DataRewinder.Factory var1) {
      synchronized(this){}

      try {
         this.rewinders.put(var1.getDataClass(), var1);
      } finally {
         ;
      }

   }

   private static final class DefaultRewinder implements DataRewinder {
      private final Object data;

      DefaultRewinder(Object var1) {
         this.data = var1;
      }

      public void cleanup() {
      }

      public Object rewindAndGet() {
         return this.data;
      }
   }
}
