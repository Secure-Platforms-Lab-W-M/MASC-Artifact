package android.support.v4.os;

import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;

@Deprecated
public final class ParcelableCompat {
   private ParcelableCompat() {
   }

   @Deprecated
   public static Creator newCreator(ParcelableCompatCreatorCallbacks var0) {
      return new ParcelableCompat.ParcelableCompatCreatorHoneycombMR2(var0);
   }

   static class ParcelableCompatCreatorHoneycombMR2 implements ClassLoaderCreator {
      private final ParcelableCompatCreatorCallbacks mCallbacks;

      ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks var1) {
         this.mCallbacks = var1;
      }

      public Object createFromParcel(Parcel var1) {
         return this.mCallbacks.createFromParcel(var1, (ClassLoader)null);
      }

      public Object createFromParcel(Parcel var1, ClassLoader var2) {
         return this.mCallbacks.createFromParcel(var1, var2);
      }

      public Object[] newArray(int var1) {
         return this.mCallbacks.newArray(var1);
      }
   }
}
