package androidx.transition;

import android.util.SparseArray;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;

class TransitionValuesMaps {
   final SparseArray mIdValues = new SparseArray();
   final LongSparseArray mItemIdValues = new LongSparseArray();
   final ArrayMap mNameValues = new ArrayMap();
   final ArrayMap mViewValues = new ArrayMap();
}
