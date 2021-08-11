package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.versionedparcelable.VersionedParcel;

public class RemoteActionCompatParcelizer {
   public static RemoteActionCompat read(VersionedParcel var0) {
      RemoteActionCompat var1 = new RemoteActionCompat();
      var1.mIcon = (IconCompat)var0.readVersionedParcelable(var1.mIcon, 1);
      var1.mTitle = var0.readCharSequence(var1.mTitle, 2);
      var1.mContentDescription = var0.readCharSequence(var1.mContentDescription, 3);
      var1.mActionIntent = (PendingIntent)var0.readParcelable(var1.mActionIntent, 4);
      var1.mEnabled = var0.readBoolean(var1.mEnabled, 5);
      var1.mShouldShowIcon = var0.readBoolean(var1.mShouldShowIcon, 6);
      return var1;
   }

   public static void write(RemoteActionCompat var0, VersionedParcel var1) {
      var1.setSerializationFlags(false, false);
      var1.writeVersionedParcelable(var0.mIcon, 1);
      var1.writeCharSequence(var0.mTitle, 2);
      var1.writeCharSequence(var0.mContentDescription, 3);
      var1.writeParcelable(var0.mActionIntent, 4);
      var1.writeBoolean(var0.mEnabled, 5);
      var1.writeBoolean(var0.mShouldShowIcon, 6);
   }
}
