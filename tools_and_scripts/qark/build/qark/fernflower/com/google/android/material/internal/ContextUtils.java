package com.google.android.material.internal;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextUtils {
   public static Activity getActivity(Context var0) {
      while(var0 instanceof ContextWrapper) {
         if (var0 instanceof Activity) {
            return (Activity)var0;
         }

         var0 = ((ContextWrapper)var0).getBaseContext();
      }

      return null;
   }
}
