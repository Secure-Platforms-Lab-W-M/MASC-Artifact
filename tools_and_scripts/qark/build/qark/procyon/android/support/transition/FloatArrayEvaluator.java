// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator<float[]>
{
    private float[] mArray;
    
    FloatArrayEvaluator(final float[] mArray) {
        this.mArray = mArray;
    }
    
    public float[] evaluate(final float n, final float[] array, final float[] array2) {
        float[] mArray = this.mArray;
        if (mArray == null) {
            mArray = new float[array.length];
        }
        for (int i = 0; i < mArray.length; ++i) {
            final float n2 = array[i];
            mArray[i] = (array2[i] - n2) * n + n2;
        }
        return mArray;
    }
}
