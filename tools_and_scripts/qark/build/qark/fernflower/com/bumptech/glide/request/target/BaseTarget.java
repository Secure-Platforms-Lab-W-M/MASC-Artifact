package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;

@Deprecated
public abstract class BaseTarget implements Target {
   private Request request;

   public Request getRequest() {
      return this.request;
   }

   public void onDestroy() {
   }

   public void onLoadCleared(Drawable var1) {
   }

   public void onLoadFailed(Drawable var1) {
   }

   public void onLoadStarted(Drawable var1) {
   }

   public void onStart() {
   }

   public void onStop() {
   }

   public void setRequest(Request var1) {
      this.request = var1;
   }
}
