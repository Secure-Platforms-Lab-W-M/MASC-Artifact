package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@RequiresApi(14)
interface ViewGroupUtilsImpl {
   ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var1);

   void suppressLayout(@NonNull ViewGroup var1, boolean var2);
}
