package android.support.v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NotificationCompatBase {
   public abstract static class Action {
      public abstract PendingIntent getActionIntent();

      public abstract boolean getAllowGeneratedReplies();

      public abstract RemoteInputCompatBase.RemoteInput[] getDataOnlyRemoteInputs();

      public abstract Bundle getExtras();

      public abstract int getIcon();

      public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

      public abstract CharSequence getTitle();

      public interface Factory {
         NotificationCompatBase.Action build(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInputCompatBase.RemoteInput[] var5, RemoteInputCompatBase.RemoteInput[] var6, boolean var7);

         NotificationCompatBase.Action[] newArray(int var1);
      }
   }

   public abstract static class UnreadConversation {
      abstract long getLatestTimestamp();

      abstract String[] getMessages();

      abstract String getParticipant();

      abstract String[] getParticipants();

      abstract PendingIntent getReadPendingIntent();

      abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

      abstract PendingIntent getReplyPendingIntent();

      public interface Factory {
         NotificationCompatBase.UnreadConversation build(String[] var1, RemoteInputCompatBase.RemoteInput var2, PendingIntent var3, PendingIntent var4, String[] var5, long var6);
      }
   }
}
