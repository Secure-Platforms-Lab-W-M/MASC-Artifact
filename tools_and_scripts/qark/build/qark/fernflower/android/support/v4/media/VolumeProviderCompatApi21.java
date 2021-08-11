package android.support.v4.media;

import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class VolumeProviderCompatApi21 {
   public static Object createVolumeProvider(int var0, int var1, int var2, final VolumeProviderCompatApi21.Delegate var3) {
      return new VolumeProvider(var0, var1, var2) {
         public void onAdjustVolume(int var1) {
            var3.onAdjustVolume(var1);
         }

         public void onSetVolumeTo(int var1) {
            var3.onSetVolumeTo(var1);
         }
      };
   }

   public static void setCurrentVolume(Object var0, int var1) {
      ((VolumeProvider)var0).setCurrentVolume(var1);
   }

   public interface Delegate {
      void onAdjustVolume(int var1);

      void onSetVolumeTo(int var1);
   }
}
