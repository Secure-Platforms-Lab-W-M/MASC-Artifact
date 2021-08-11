/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 *  android.view.View
 */
package androidx.transition;

import android.util.SparseArray;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.transition.TransitionValues;

class TransitionValuesMaps {
    final SparseArray<View> mIdValues = new SparseArray();
    final LongSparseArray<View> mItemIdValues = new LongSparseArray();
    final ArrayMap<String, View> mNameValues = new ArrayMap();
    final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();

    TransitionValuesMaps() {
    }
}

