package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(14)
interface ViewGroupOverlayImpl extends ViewOverlayImpl {
   void add(@NonNull View var1);

   void remove(@NonNull View var1);
}
