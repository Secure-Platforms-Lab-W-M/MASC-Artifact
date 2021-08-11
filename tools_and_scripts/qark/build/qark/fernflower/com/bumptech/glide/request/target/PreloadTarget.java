package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.transition.Transition;

public final class PreloadTarget extends CustomTarget {
   private static final Handler HANDLER = new Handler(Looper.getMainLooper(), new Callback() {
      public boolean handleMessage(Message var1) {
         if (var1.what == 1) {
            ((PreloadTarget)var1.obj).clear();
            return true;
         } else {
            return false;
         }
      }
   });
   private static final int MESSAGE_CLEAR = 1;
   private final RequestManager requestManager;

   private PreloadTarget(RequestManager var1, int var2, int var3) {
      super(var2, var3);
      this.requestManager = var1;
   }

   public static PreloadTarget obtain(RequestManager var0, int var1, int var2) {
      return new PreloadTarget(var0, var1, var2);
   }

   void clear() {
      this.requestManager.clear((Target)this);
   }

   public void onLoadCleared(Drawable var1) {
   }

   public void onResourceReady(Object var1, Transition var2) {
      HANDLER.obtainMessage(1, this).sendToTarget();
   }
}
