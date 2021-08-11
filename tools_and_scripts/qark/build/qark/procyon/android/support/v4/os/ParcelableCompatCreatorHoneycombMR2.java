// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;

class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable$ClassLoaderCreator<T>
{
    private final ParcelableCompatCreatorCallbacks<T> mCallbacks;
    
    public ParcelableCompatCreatorHoneycombMR2(final ParcelableCompatCreatorCallbacks<T> mCallbacks) {
        this.mCallbacks = mCallbacks;
    }
    
    public T createFromParcel(final Parcel parcel) {
        return this.mCallbacks.createFromParcel(parcel, null);
    }
    
    public T createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
        return this.mCallbacks.createFromParcel(parcel, classLoader);
    }
    
    public T[] newArray(final int n) {
        return this.mCallbacks.newArray(n);
    }
}
