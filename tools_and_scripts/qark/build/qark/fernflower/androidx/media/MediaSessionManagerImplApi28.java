package androidx.media;

import android.content.Context;
import android.media.session.MediaSessionManager.RemoteUserInfo;
import androidx.core.util.ObjectsCompat;

class MediaSessionManagerImplApi28 extends MediaSessionManagerImplApi21 {
   android.media.session.MediaSessionManager mObject;

   MediaSessionManagerImplApi28(Context var1) {
      super(var1);
      this.mObject = (android.media.session.MediaSessionManager)var1.getSystemService("media_session");
   }

   public boolean isTrustedForMediaControl(MediaSessionManager.RemoteUserInfoImpl var1) {
      return var1 instanceof MediaSessionManagerImplApi28.RemoteUserInfoImplApi28 ? this.mObject.isTrustedForMediaControl(((MediaSessionManagerImplApi28.RemoteUserInfoImplApi28)var1).mObject) : false;
   }

   static final class RemoteUserInfoImplApi28 implements MediaSessionManager.RemoteUserInfoImpl {
      final RemoteUserInfo mObject;

      RemoteUserInfoImplApi28(RemoteUserInfo var1) {
         this.mObject = var1;
      }

      RemoteUserInfoImplApi28(String var1, int var2, int var3) {
         this.mObject = new RemoteUserInfo(var1, var2, var3);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof MediaSessionManagerImplApi28.RemoteUserInfoImplApi28)) {
            return false;
         } else {
            MediaSessionManagerImplApi28.RemoteUserInfoImplApi28 var2 = (MediaSessionManagerImplApi28.RemoteUserInfoImplApi28)var1;
            return this.mObject.equals(var2.mObject);
         }
      }

      public String getPackageName() {
         return this.mObject.getPackageName();
      }

      public int getPid() {
         return this.mObject.getPid();
      }

      public int getUid() {
         return this.mObject.getUid();
      }

      public int hashCode() {
         return ObjectsCompat.hash(this.mObject);
      }
   }
}
