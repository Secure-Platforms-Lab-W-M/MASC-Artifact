package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.transition.Transition;

public interface Target extends LifecycleListener {
   int SIZE_ORIGINAL = Integer.MIN_VALUE;

   Request getRequest();

   void getSize(SizeReadyCallback var1);

   void onLoadCleared(Drawable var1);

   void onLoadFailed(Drawable var1);

   void onLoadStarted(Drawable var1);

   void onResourceReady(Object var1, Transition var2);

   void removeCallback(SizeReadyCallback var1);

   void setRequest(Request var1);
}
