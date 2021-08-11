package android.support.design.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ParcelableSparseArray extends SparseArray implements Parcelable {
   public static final Creator CREATOR = new ClassLoaderCreator() {
      public ParcelableSparseArray createFromParcel(Parcel var1) {
         return new ParcelableSparseArray(var1, (ClassLoader)null);
      }

      public ParcelableSparseArray createFromParcel(Parcel var1, ClassLoader var2) {
         return new ParcelableSparseArray(var1, var2);
      }

      public ParcelableSparseArray[] newArray(int var1) {
         return new ParcelableSparseArray[var1];
      }
   };

   public ParcelableSparseArray() {
   }

   public ParcelableSparseArray(Parcel var1, ClassLoader var2) {
      int var4 = var1.readInt();
      int[] var5 = new int[var4];
      var1.readIntArray(var5);
      Parcelable[] var6 = var1.readParcelableArray(var2);

      for(int var3 = 0; var3 < var4; ++var3) {
         this.put(var5[var3], var6[var3]);
      }

   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      int var4 = this.size();
      int[] var5 = new int[var4];
      Parcelable[] var6 = new Parcelable[var4];

      for(int var3 = 0; var3 < var4; ++var3) {
         var5[var3] = this.keyAt(var3);
         var6[var3] = (Parcelable)this.valueAt(var3);
      }

      var1.writeInt(var4);
      var1.writeIntArray(var5);
      var1.writeParcelableArray(var6, var2);
   }
}
