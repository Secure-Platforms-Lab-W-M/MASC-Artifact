package androidx.viewpager2.adapter;

import android.os.Parcelable;

public interface StatefulAdapter {
   void restoreState(Parcelable var1);

   Parcelable saveState();
}
