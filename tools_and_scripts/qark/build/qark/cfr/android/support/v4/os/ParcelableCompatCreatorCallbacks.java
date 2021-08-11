/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package android.support.v4.os;

import android.os.Parcel;

@Deprecated
public interface ParcelableCompatCreatorCallbacks<T> {
    public T createFromParcel(Parcel var1, ClassLoader var2);

    public T[] newArray(int var1);
}

