/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 *  android.view.View
 */
package android.support.transition;

import android.support.annotation.RequiresApi;
import android.support.transition.TransitionValues;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;

@RequiresApi(value=14)
class TransitionValuesMaps {
    final SparseArray<View> mIdValues = new SparseArray();
    final LongSparseArray<View> mItemIdValues = new LongSparseArray();
    final ArrayMap<String, View> mNameValues = new ArrayMap();
    final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();

    TransitionValuesMaps() {
    }
}

