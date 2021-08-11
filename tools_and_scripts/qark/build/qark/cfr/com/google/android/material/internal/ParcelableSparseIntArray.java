/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseIntArray
 */
package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

public class ParcelableSparseIntArray
extends SparseIntArray
implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseIntArray> CREATOR = new Parcelable.Creator<ParcelableSparseIntArray>(){

        public ParcelableSparseIntArray createFromParcel(Parcel parcel) {
            int n = parcel.readInt();
            ParcelableSparseIntArray parcelableSparseIntArray = new ParcelableSparseIntArray(n);
            int[] arrn = new int[n];
            int[] arrn2 = new int[n];
            parcel.readIntArray(arrn);
            parcel.readIntArray(arrn2);
            for (int i = 0; i < n; ++i) {
                parcelableSparseIntArray.put(arrn[i], arrn2[i]);
            }
            return parcelableSparseIntArray;
        }

        public ParcelableSparseIntArray[] newArray(int n) {
            return new ParcelableSparseIntArray[n];
        }
    };

    public ParcelableSparseIntArray() {
    }

    public ParcelableSparseIntArray(int n) {
        super(n);
    }

    public ParcelableSparseIntArray(SparseIntArray sparseIntArray) {
        for (int i = 0; i < sparseIntArray.size(); ++i) {
            this.put(sparseIntArray.keyAt(i), sparseIntArray.valueAt(i));
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int[] arrn = new int[this.size()];
        int[] arrn2 = new int[this.size()];
        for (n = 0; n < this.size(); ++n) {
            arrn[n] = this.keyAt(n);
            arrn2[n] = this.valueAt(n);
        }
        parcel.writeInt(this.size());
        parcel.writeIntArray(arrn);
        parcel.writeIntArray(arrn2);
    }

}

