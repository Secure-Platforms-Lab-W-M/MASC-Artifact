package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import androidx.core.util.Pools;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.util.MultiClassKey;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class LoadPathCache {
   private static final LoadPath NO_PATHS_SIGNAL = new LoadPath(Object.class, Object.class, Object.class, Collections.singletonList(new DecodePath(Object.class, Object.class, Object.class, Collections.emptyList(), new UnitTranscoder(), (Pools.Pool)null)), (Pools.Pool)null);
   private final ArrayMap cache = new ArrayMap();
   private final AtomicReference keyRef = new AtomicReference();

   private MultiClassKey getKey(Class var1, Class var2, Class var3) {
      MultiClassKey var5 = (MultiClassKey)this.keyRef.getAndSet((Object)null);
      MultiClassKey var4 = var5;
      if (var5 == null) {
         var4 = new MultiClassKey();
      }

      var4.set(var1, var2, var3);
      return var4;
   }

   public LoadPath get(Class param1, Class param2, Class param3) {
      // $FF: Couldn't be decompiled
   }

   public boolean isEmptyLoadPath(LoadPath var1) {
      return NO_PATHS_SIGNAL.equals(var1);
   }

   public void put(Class var1, Class var2, Class var3, LoadPath var4) {
      ArrayMap var5 = this.cache;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label177: {
         ArrayMap var6;
         MultiClassKey var27;
         try {
            var6 = this.cache;
            var27 = new MultiClassKey(var1, var2, var3);
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label177;
         }

         if (var4 == null) {
            try {
               var4 = NO_PATHS_SIGNAL;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label177;
            }
         }

         label166:
         try {
            var6.put(var27, var4);
            return;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label166;
         }
      }

      while(true) {
         Throwable var28 = var10000;

         try {
            throw var28;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            continue;
         }
      }
   }
}
