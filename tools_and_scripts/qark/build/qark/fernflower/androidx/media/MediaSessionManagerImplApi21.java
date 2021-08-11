package androidx.media;

import android.content.Context;

class MediaSessionManagerImplApi21 extends MediaSessionManagerImplBase {
   MediaSessionManagerImplApi21(Context var1) {
      super(var1);
      this.mContext = var1;
   }

   private boolean hasMediaControlPermission(MediaSessionManager.RemoteUserInfoImpl var1) {
      return this.getContext().checkPermission("android.permission.MEDIA_CONTENT_CONTROL", var1.getPid(), var1.getUid()) == 0;
   }

   public boolean isTrustedForMediaControl(MediaSessionManager.RemoteUserInfoImpl var1) {
      return this.hasMediaControlPermission(var1) || super.isTrustedForMediaControl(var1);
   }
}
