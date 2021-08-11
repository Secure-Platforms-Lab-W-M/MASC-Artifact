package android.support.transition;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
interface ViewOverlayImpl {
   void add(@NonNull Drawable var1);

   void clear();

   void remove(@NonNull Drawable var1);
}
