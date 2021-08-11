// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

import android.os.Parcel;

@Deprecated
public interface ParcelableCompatCreatorCallbacks<T>
{
    T createFromParcel(final Parcel p0, final ClassLoader p1);
    
    T[] newArray(final int p0);
}
