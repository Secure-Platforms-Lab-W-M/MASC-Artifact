package com.bumptech.glide.util.pool;

import android.util.Log;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
   private static final int DEFAULT_POOL_SIZE = 20;
   private static final FactoryPools.Resetter EMPTY_RESETTER = new FactoryPools.Resetter() {
      public void reset(Object var1) {
      }
   };
   private static final String TAG = "FactoryPools";

   private FactoryPools() {
   }

   private static Pools.Pool build(Pools.Pool var0, FactoryPools.Factory var1) {
      return build(var0, var1, emptyResetter());
   }

   private static Pools.Pool build(Pools.Pool var0, FactoryPools.Factory var1, FactoryPools.Resetter var2) {
      return new FactoryPools.FactoryPool(var0, var1, var2);
   }

   private static FactoryPools.Resetter emptyResetter() {
      return EMPTY_RESETTER;
   }

   public static Pools.Pool simple(int var0, FactoryPools.Factory var1) {
      return build(new Pools.SimplePool(var0), var1);
   }

   public static Pools.Pool threadSafe(int var0, FactoryPools.Factory var1) {
      return build(new Pools.SynchronizedPool(var0), var1);
   }

   public static Pools.Pool threadSafeList() {
      return threadSafeList(20);
   }

   public static Pools.Pool threadSafeList(int var0) {
      return build(new Pools.SynchronizedPool(var0), new FactoryPools.Factory() {
         public List create() {
            return new ArrayList();
         }
      }, new FactoryPools.Resetter() {
         public void reset(List var1) {
            var1.clear();
         }
      });
   }

   public interface Factory {
      Object create();
   }

   private static final class FactoryPool implements Pools.Pool {
      private final FactoryPools.Factory factory;
      private final Pools.Pool pool;
      private final FactoryPools.Resetter resetter;

      FactoryPool(Pools.Pool var1, FactoryPools.Factory var2, FactoryPools.Resetter var3) {
         this.pool = var1;
         this.factory = var2;
         this.resetter = var3;
      }

      public Object acquire() {
         Object var2 = this.pool.acquire();
         Object var1 = var2;
         if (var2 == null) {
            var2 = this.factory.create();
            var1 = var2;
            if (Log.isLoggable("FactoryPools", 2)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Created new ");
               var3.append(var2.getClass());
               Log.v("FactoryPools", var3.toString());
               var1 = var2;
            }
         }

         if (var1 instanceof FactoryPools.Poolable) {
            ((FactoryPools.Poolable)var1).getVerifier().setRecycled(false);
         }

         return var1;
      }

      public boolean release(Object var1) {
         if (var1 instanceof FactoryPools.Poolable) {
            ((FactoryPools.Poolable)var1).getVerifier().setRecycled(true);
         }

         this.resetter.reset(var1);
         return this.pool.release(var1);
      }
   }

   public interface Poolable {
      StateVerifier getVerifier();
   }

   public interface Resetter {
      void reset(Object var1);
   }
}
