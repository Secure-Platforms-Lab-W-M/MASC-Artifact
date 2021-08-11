package androidx.media;

import android.media.AudioAttributes;
import androidx.versionedparcelable.VersionedParcel;

public final class AudioAttributesImplApi21Parcelizer {
   public static AudioAttributesImplApi21 read(VersionedParcel var0) {
      AudioAttributesImplApi21 var1 = new AudioAttributesImplApi21();
      var1.mAudioAttributes = (AudioAttributes)var0.readParcelable(var1.mAudioAttributes, 1);
      var1.mLegacyStreamType = var0.readInt(var1.mLegacyStreamType, 2);
      return var1;
   }

   public static void write(AudioAttributesImplApi21 var0, VersionedParcel var1) {
      var1.setSerializationFlags(false, false);
      var1.writeParcelable(var0.mAudioAttributes, 1);
      var1.writeInt(var0.mLegacyStreamType, 2);
   }
}
