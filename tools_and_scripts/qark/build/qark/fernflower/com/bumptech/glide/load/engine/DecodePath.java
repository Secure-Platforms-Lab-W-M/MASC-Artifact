package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecodePath {
   private static final String TAG = "DecodePath";
   private final Class dataClass;
   private final List decoders;
   private final String failureMessage;
   private final Pools.Pool listPool;
   private final ResourceTranscoder transcoder;

   public DecodePath(Class var1, Class var2, Class var3, List var4, ResourceTranscoder var5, Pools.Pool var6) {
      this.dataClass = var1;
      this.decoders = var4;
      this.transcoder = var5;
      this.listPool = var6;
      StringBuilder var7 = new StringBuilder();
      var7.append("Failed DecodePath{");
      var7.append(var1.getSimpleName());
      var7.append("->");
      var7.append(var2.getSimpleName());
      var7.append("->");
      var7.append(var3.getSimpleName());
      var7.append("}");
      this.failureMessage = var7.toString();
   }

   private Resource decodeResource(DataRewinder var1, int var2, int var3, Options var4) throws GlideException {
      List var5 = (List)Preconditions.checkNotNull(this.listPool.acquire());

      Resource var8;
      try {
         var8 = this.decodeResourceWithList(var1, var2, var3, var4, var5);
      } finally {
         this.listPool.release(var5);
      }

      return var8;
   }

   private Resource decodeResourceWithList(DataRewinder var1, int var2, int var3, Options var4, List var5) throws GlideException {
      Resource var8 = null;
      int var6 = 0;
      int var7 = this.decoders.size();

      Resource var9;
      while(true) {
         var9 = var8;
         if (var6 >= var7) {
            break;
         }

         ResourceDecoder var10 = (ResourceDecoder)this.decoders.get(var6);
         var9 = var8;

         label42: {
            label50: {
               Object var15;
               try {
                  if (var10.handles(var1.rewindAndGet(), var4)) {
                     var9 = var10.decode(var1.rewindAndGet(), var2, var3, var4);
                  }
                  break label50;
               } catch (IOException var12) {
                  var15 = var12;
               } catch (RuntimeException var13) {
                  var15 = var13;
               } catch (OutOfMemoryError var14) {
                  var15 = var14;
               }

               if (Log.isLoggable("DecodePath", 2)) {
                  StringBuilder var11 = new StringBuilder();
                  var11.append("Failed to decode data for ");
                  var11.append(var10);
                  Log.v("DecodePath", var11.toString(), (Throwable)var15);
               }

               var5.add(var15);
               break label42;
            }

            var8 = var9;
         }

         if (var8 != null) {
            var9 = var8;
            break;
         }

         ++var6;
      }

      if (var9 != null) {
         return var9;
      } else {
         throw new GlideException(this.failureMessage, new ArrayList(var5));
      }
   }

   public Resource decode(DataRewinder var1, int var2, int var3, Options var4, DecodePath.DecodeCallback var5) throws GlideException {
      Resource var6 = var5.onResourceDecoded(this.decodeResource(var1, var2, var3, var4));
      return this.transcoder.transcode(var6, var4);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DecodePath{ dataClass=");
      var1.append(this.dataClass);
      var1.append(", decoders=");
      var1.append(this.decoders);
      var1.append(", transcoder=");
      var1.append(this.transcoder);
      var1.append('}');
      return var1.toString();
   }

   interface DecodeCallback {
      Resource onResourceDecoded(Resource var1);
   }
}
