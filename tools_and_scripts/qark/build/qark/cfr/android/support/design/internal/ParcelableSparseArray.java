/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package android.support.design.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class ParcelableSparseArray
extends SparseArray<Parcelable>
implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseArray> CREATOR = new Parcelable.ClassLoaderCreator<ParcelableSparseArray>(){

        public ParcelableSparseArray createFromParcel(Parcel parcel) {
            return new ParcelableSparseArray(parcel, null);
        }

        public ParcelableSparseArray createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ParcelableSparseArray(parcel, classLoader);
        }

        public ParcelableSparseArray[] newArray(int n) {
            return new ParcelableSparseArray[n];
        }
    };

    public ParcelableSparseArray() {
    }

    public ParcelableSparseArray(Parcel arrparcelable, ClassLoader classLoader) {
        int n = arrparcelable.readInt();
        int[] arrn = new int[n];
        arrparcelable.readIntArray(arrn);
        arrparcelable = arrparcelable.readParcelableArray(classLoader);
        for (int i = 0; i < n; ++i) {
            this.put(arrn[i], (Object)arrparcelable[i]);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int n2 = this.size();
        int[] arrn = new int[n2];
        Parcelable[] arrparcelable = new Parcelable[n2];
        for (int i = 0; i < n2; ++i) {
            arrn[i] = this.keyAt(i);
            arrparcelable[i] = (Parcelable)this.valueAt(i);
        }
        parcel.writeInt(n2);
        parcel.writeIntArray(arrn);
        parcel.writeParcelableArray(arrparcelable, n);
    }

}

