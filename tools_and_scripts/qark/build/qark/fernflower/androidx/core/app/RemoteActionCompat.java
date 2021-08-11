package androidx.core.app;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.os.Build.VERSION;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import androidx.versionedparcelable.VersionedParcelable;

public final class RemoteActionCompat implements VersionedParcelable {
   public PendingIntent mActionIntent;
   public CharSequence mContentDescription;
   public boolean mEnabled;
   public IconCompat mIcon;
   public boolean mShouldShowIcon;
   public CharSequence mTitle;

   public RemoteActionCompat() {
   }

   public RemoteActionCompat(RemoteActionCompat var1) {
      Preconditions.checkNotNull(var1);
      this.mIcon = var1.mIcon;
      this.mTitle = var1.mTitle;
      this.mContentDescription = var1.mContentDescription;
      this.mActionIntent = var1.mActionIntent;
      this.mEnabled = var1.mEnabled;
      this.mShouldShowIcon = var1.mShouldShowIcon;
   }

   public RemoteActionCompat(IconCompat var1, CharSequence var2, CharSequence var3, PendingIntent var4) {
      this.mIcon = (IconCompat)Preconditions.checkNotNull(var1);
      this.mTitle = (CharSequence)Preconditions.checkNotNull(var2);
      this.mContentDescription = (CharSequence)Preconditions.checkNotNull(var3);
      this.mActionIntent = (PendingIntent)Preconditions.checkNotNull(var4);
      this.mEnabled = true;
      this.mShouldShowIcon = true;
   }

   public static RemoteActionCompat createFromRemoteAction(RemoteAction var0) {
      Preconditions.checkNotNull(var0);
      RemoteActionCompat var1 = new RemoteActionCompat(IconCompat.createFromIcon(var0.getIcon()), var0.getTitle(), var0.getContentDescription(), var0.getActionIntent());
      var1.setEnabled(var0.isEnabled());
      if (VERSION.SDK_INT >= 28) {
         var1.setShouldShowIcon(var0.shouldShowIcon());
      }

      return var1;
   }

   public PendingIntent getActionIntent() {
      return this.mActionIntent;
   }

   public CharSequence getContentDescription() {
      return this.mContentDescription;
   }

   public IconCompat getIcon() {
      return this.mIcon;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public boolean isEnabled() {
      return this.mEnabled;
   }

   public void setEnabled(boolean var1) {
      this.mEnabled = var1;
   }

   public void setShouldShowIcon(boolean var1) {
      this.mShouldShowIcon = var1;
   }

   public boolean shouldShowIcon() {
      return this.mShouldShowIcon;
   }

   public RemoteAction toRemoteAction() {
      RemoteAction var1 = new RemoteAction(this.mIcon.toIcon(), this.mTitle, this.mContentDescription, this.mActionIntent);
      var1.setEnabled(this.isEnabled());
      if (VERSION.SDK_INT >= 28) {
         var1.setShouldShowIcon(this.shouldShowIcon());
      }

      return var1;
   }
}
