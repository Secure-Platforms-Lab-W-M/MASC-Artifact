/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseBooleanArray
 */
package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

public class ParcelableSparseBooleanArray
extends SparseBooleanArray
implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseBooleanArray> CREATOR = new Parcelable.Creator<ParcelableSparseBooleanArray>(){

        public ParcelableSparseBooleanArray createFromParcel(Parcel parcel) {
            int n = parcel.readInt();
            ParcelableSparseBooleanArray parcelableSparseBooleanArray = new ParcelableSparseBooleanArray(n);
            int[] arrn = new int[n];
            boolean[] arrbl = new boolean[n];
            parcel.readIntArray(arrn);
            parcel.readBooleanArray(arrbl);
            for (int i = 0; i < n; ++i) {
                parcelableSparseBooleanArray.put(arrn[i], arrbl[i]);
            }
            return parcelableSparseBooleanArray;
        }

        public ParcelableSparseBooleanArray[] newArray(int n) {
            return new ParcelableSparseBooleanArray[n];
        }
    };

    public ParcelableSparseBooleanArray() {
    }

    public ParcelableSparseBooleanArray(int n) {
        super(n);
    }

    public ParcelableSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        super(sparseBooleanArray.size());
        for (int i = 0; i < sparseBooleanArray.size(); ++i) {
            this.put(sparseBooleanArray.keyAt(i), sparseBooleanArray.valueAt(i));
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int[] arrn = new int[this.size()];
        boolean[] arrbl = new boolean[this.size()];
        for (n = 0; n < this.size(); ++n) {
            arrn[n] = this.keyAt(n);
            arrbl[n] = this.valueAt(n);
        }
        parcel.writeInt(this.size());
        parcel.writeIntArray(arrn);
        parcel.writeBooleanArray(arrbl);
    }

}

