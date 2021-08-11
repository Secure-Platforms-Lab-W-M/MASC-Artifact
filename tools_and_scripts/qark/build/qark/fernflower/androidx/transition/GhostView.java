package androidx.transition;

import android.view.View;
import android.view.ViewGroup;

interface GhostView {
   void reserveEndViewTransition(ViewGroup var1, View var2);

   void setVisibility(int var1);
}
