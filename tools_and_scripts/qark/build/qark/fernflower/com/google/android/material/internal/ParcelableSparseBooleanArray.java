package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseBooleanArray;

public class ParcelableSparseBooleanArray extends SparseBooleanArray implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ParcelableSparseBooleanArray createFromParcel(Parcel var1) {
         int var3 = var1.readInt();
         ParcelableSparseBooleanArray var4 = new ParcelableSparseBooleanArray(var3);
         int[] var5 = new int[var3];
         boolean[] var6 = new boolean[var3];
         var1.readIntArray(var5);
         var1.readBooleanArray(var6);

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.put(var5[var2], var6[var2]);
         }

         return var4;
      }

      public ParcelableSparseBooleanArray[] newArray(int var1) {
         return new ParcelableSparseBooleanArray[var1];
      }
   };

   public ParcelableSparseBooleanArray() {
   }

   public ParcelableSparseBooleanArray(int var1) {
      super(var1);
   }

   public ParcelableSparseBooleanArray(SparseBooleanArray var1) {
      super(var1.size());

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.put(var1.keyAt(var2), var1.valueAt(var2));
      }

   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      int[] var3 = new int[this.size()];
      boolean[] var4 = new boolean[this.size()];

      for(var2 = 0; var2 < this.size(); ++var2) {
         var3[var2] = this.keyAt(var2);
         var4[var2] = this.valueAt(var2);
      }

      var1.writeInt(this.size());
      var1.writeIntArray(var3);
      var1.writeBooleanArray(var4);
   }
}
