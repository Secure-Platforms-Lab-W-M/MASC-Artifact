package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;

class ResourceRecycler {
   private final Handler handler = new Handler(Looper.getMainLooper(), new ResourceRecycler.ResourceRecyclerCallback());
   private boolean isRecycling;

   void recycle(Resource var1, boolean var2) {
      synchronized(this){}

      Throwable var10000;
      label118: {
         boolean var10001;
         label117: {
            try {
               if (this.isRecycling) {
                  break label117;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label118;
            }

            if (!var2) {
               try {
                  this.isRecycling = true;
                  var1.recycle();
                  this.isRecycling = false;
                  return;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label118;
               }
            }
         }

         label111:
         try {
            this.handler.obtainMessage(1, var1).sendToTarget();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label111;
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   private static final class ResourceRecyclerCallback implements Callback {
      static final int RECYCLE_RESOURCE = 1;

      ResourceRecyclerCallback() {
      }

      public boolean handleMessage(Message var1) {
         if (var1.what == 1) {
            ((Resource)var1.obj).recycle();
            return true;
         } else {
            return false;
         }
      }
   }
}
