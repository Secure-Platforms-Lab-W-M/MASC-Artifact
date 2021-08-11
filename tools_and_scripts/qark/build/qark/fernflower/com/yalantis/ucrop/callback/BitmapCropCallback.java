package com.yalantis.ucrop.callback;

import android.net.Uri;

public interface BitmapCropCallback {
   void onBitmapCropped(Uri var1, int var2, int var3, int var4, int var5);

   void onCropFailure(Throwable var1);
}
