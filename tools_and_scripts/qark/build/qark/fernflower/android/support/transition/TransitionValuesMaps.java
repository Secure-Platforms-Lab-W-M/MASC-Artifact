package android.support.transition;

import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;

@RequiresApi(14)
class TransitionValuesMaps {
   final SparseArray mIdValues = new SparseArray();
   final LongSparseArray mItemIdValues = new LongSparseArray();
   final ArrayMap mNameValues = new ArrayMap();
   final ArrayMap mViewValues = new ArrayMap();
}
