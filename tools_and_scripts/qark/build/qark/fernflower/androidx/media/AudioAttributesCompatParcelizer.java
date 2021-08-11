package androidx.media;

import androidx.versionedparcelable.VersionedParcel;

public final class AudioAttributesCompatParcelizer {
   public static AudioAttributesCompat read(VersionedParcel var0) {
      AudioAttributesCompat var1 = new AudioAttributesCompat();
      var1.mImpl = (AudioAttributesImpl)var0.readVersionedParcelable(var1.mImpl, 1);
      return var1;
   }

   public static void write(AudioAttributesCompat var0, VersionedParcel var1) {
      var1.setSerializationFlags(false, false);
      var1.writeVersionedParcelable(var0.mImpl, 1);
   }
}
