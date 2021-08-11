package androidx.media;

import androidx.versionedparcelable.VersionedParcel;

public final class AudioAttributesImplBaseParcelizer {
   public static AudioAttributesImplBase read(VersionedParcel var0) {
      AudioAttributesImplBase var1 = new AudioAttributesImplBase();
      var1.mUsage = var0.readInt(var1.mUsage, 1);
      var1.mContentType = var0.readInt(var1.mContentType, 2);
      var1.mFlags = var0.readInt(var1.mFlags, 3);
      var1.mLegacyStream = var0.readInt(var1.mLegacyStream, 4);
      return var1;
   }

   public static void write(AudioAttributesImplBase var0, VersionedParcel var1) {
      var1.setSerializationFlags(false, false);
      var1.writeInt(var0.mUsage, 1);
      var1.writeInt(var0.mContentType, 2);
      var1.writeInt(var0.mFlags, 3);
      var1.writeInt(var0.mLegacyStream, 4);
   }
}
