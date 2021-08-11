package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@TargetApi(12)
@RequiresApi(12)
class BitmapCompatHoneycombMr1 {
   static int getAllocationByteCount(Bitmap var0) {
      return var0.getByteCount();
   }
}
