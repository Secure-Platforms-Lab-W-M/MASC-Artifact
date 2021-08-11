package com.yalantis.ucrop.callback;

import android.graphics.Bitmap;
import com.yalantis.ucrop.model.ExifInfo;

public interface BitmapLoadCallback {
   void onBitmapLoaded(Bitmap var1, ExifInfo var2, String var3, String var4);

   void onFailure(Exception var1);
}
