package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import androidx.versionedparcelable.VersionedParcel;

public class IconCompatParcelizer {
   public static IconCompat read(VersionedParcel var0) {
      IconCompat var1 = new IconCompat();
      var1.mType = var0.readInt(var1.mType, 1);
      var1.mData = var0.readByteArray(var1.mData, 2);
      var1.mParcelable = var0.readParcelable(var1.mParcelable, 3);
      var1.mInt1 = var0.readInt(var1.mInt1, 4);
      var1.mInt2 = var0.readInt(var1.mInt2, 5);
      var1.mTintList = (ColorStateList)var0.readParcelable(var1.mTintList, 6);
      var1.mTintModeStr = var0.readString(var1.mTintModeStr, 7);
      var1.onPostParceling();
      return var1;
   }

   public static void write(IconCompat var0, VersionedParcel var1) {
      var1.setSerializationFlags(true, true);
      var0.onPreParceling(var1.isStream());
      if (-1 != var0.mType) {
         var1.writeInt(var0.mType, 1);
      }

      if (var0.mData != null) {
         var1.writeByteArray(var0.mData, 2);
      }

      if (var0.mParcelable != null) {
         var1.writeParcelable(var0.mParcelable, 3);
      }

      if (var0.mInt1 != 0) {
         var1.writeInt(var0.mInt1, 4);
      }

      if (var0.mInt2 != 0) {
         var1.writeInt(var0.mInt2, 5);
      }

      if (var0.mTintList != null) {
         var1.writeParcelable(var0.mTintList, 6);
      }

      if (var0.mTintModeStr != null) {
         var1.writeString(var0.mTintModeStr, 7);
      }

   }
}
