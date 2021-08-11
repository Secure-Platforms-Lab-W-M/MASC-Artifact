package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
   private final List mActionExtrasList = new ArrayList();
   private RemoteViews mBigContentView;
   private final Builder mBuilder;
   private final NotificationCompat.Builder mBuilderCompat;
   private RemoteViews mContentView;
   private final Bundle mExtras = new Bundle();
   private int mGroupAlertBehavior;
   private RemoteViews mHeadsUpContentView;

   NotificationCompatBuilder(NotificationCompat.Builder var1) {
      this.mBuilderCompat = var1;
      if (VERSION.SDK_INT >= 26) {
         this.mBuilder = new Builder(var1.mContext, var1.mChannelId);
      } else {
         this.mBuilder = new Builder(var1.mContext);
      }

      Notification var4 = var1.mNotification;
      Builder var5 = this.mBuilder.setWhen(var4.when).setSmallIcon(var4.icon, var4.iconLevel).setContent(var4.contentView).setTicker(var4.tickerText, var1.mTickerView).setVibrate(var4.vibrate).setLights(var4.ledARGB, var4.ledOnMS, var4.ledOffMS);
      boolean var3;
      if ((var4.flags & 2) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var5 = var5.setOngoing(var3);
      if ((var4.flags & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var5 = var5.setOnlyAlertOnce(var3);
      if ((var4.flags & 16) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var5 = var5.setAutoCancel(var3).setDefaults(var4.defaults).setContentTitle(var1.mContentTitle).setContentText(var1.mContentText).setContentInfo(var1.mContentInfo).setContentIntent(var1.mContentIntent).setDeleteIntent(var4.deleteIntent);
      PendingIntent var6 = var1.mFullScreenIntent;
      if ((var4.flags & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var5.setFullScreenIntent(var6, var3).setLargeIcon(var1.mLargeIcon).setNumber(var1.mNumber).setProgress(var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate);
      if (VERSION.SDK_INT < 21) {
         this.mBuilder.setSound(var4.sound, var4.audioStreamType);
      }

      if (VERSION.SDK_INT >= 16) {
         this.mBuilder.setSubText(var1.mSubText).setUsesChronometer(var1.mUseChronometer).setPriority(var1.mPriority);
         Iterator var9 = var1.mActions.iterator();

         while(var9.hasNext()) {
            this.addAction((NotificationCompat.Action)var9.next());
         }

         if (var1.mExtras != null) {
            this.mExtras.putAll(var1.mExtras);
         }

         if (VERSION.SDK_INT < 20) {
            if (var1.mLocalOnly) {
               this.mExtras.putBoolean("android.support.localOnly", true);
            }

            if (var1.mGroupKey != null) {
               this.mExtras.putString("android.support.groupKey", var1.mGroupKey);
               if (var1.mGroupSummary) {
                  this.mExtras.putBoolean("android.support.isGroupSummary", true);
               } else {
                  this.mExtras.putBoolean("android.support.useSideChannel", true);
               }
            }

            if (var1.mSortKey != null) {
               this.mExtras.putString("android.support.sortKey", var1.mSortKey);
            }
         }

         this.mContentView = var1.mContentView;
         this.mBigContentView = var1.mBigContentView;
      }

      if (VERSION.SDK_INT >= 19) {
         this.mBuilder.setShowWhen(var1.mShowWhen);
         if (VERSION.SDK_INT < 21 && var1.mPeople != null && !var1.mPeople.isEmpty()) {
            this.mExtras.putStringArray("android.people", (String[])var1.mPeople.toArray(new String[var1.mPeople.size()]));
         }
      }

      if (VERSION.SDK_INT >= 20) {
         this.mBuilder.setLocalOnly(var1.mLocalOnly).setGroup(var1.mGroupKey).setGroupSummary(var1.mGroupSummary).setSortKey(var1.mSortKey);
         this.mGroupAlertBehavior = var1.mGroupAlertBehavior;
      }

      if (VERSION.SDK_INT >= 21) {
         this.mBuilder.setCategory(var1.mCategory).setColor(var1.mColor).setVisibility(var1.mVisibility).setPublicVersion(var1.mPublicVersion).setSound(var4.sound, var4.audioAttributes);
         Iterator var7 = var1.mPeople.iterator();

         while(var7.hasNext()) {
            String var10 = (String)var7.next();
            this.mBuilder.addPerson(var10);
         }

         this.mHeadsUpContentView = var1.mHeadsUpContentView;
         if (var1.mInvisibleActions.size() > 0) {
            Bundle var11 = var1.getExtras().getBundle("android.car.EXTENSIONS");
            Bundle var8 = var11;
            if (var11 == null) {
               var8 = new Bundle();
            }

            var11 = new Bundle();

            for(int var2 = 0; var2 < var1.mInvisibleActions.size(); ++var2) {
               var11.putBundle(Integer.toString(var2), NotificationCompatJellybean.getBundleForAction((NotificationCompat.Action)var1.mInvisibleActions.get(var2)));
            }

            var8.putBundle("invisible_actions", var11);
            var1.getExtras().putBundle("android.car.EXTENSIONS", var8);
            this.mExtras.putBundle("android.car.EXTENSIONS", var8);
         }
      }

      if (VERSION.SDK_INT >= 24) {
         this.mBuilder.setExtras(var1.mExtras).setRemoteInputHistory(var1.mRemoteInputHistory);
         if (var1.mContentView != null) {
            this.mBuilder.setCustomContentView(var1.mContentView);
         }

         if (var1.mBigContentView != null) {
            this.mBuilder.setCustomBigContentView(var1.mBigContentView);
         }

         if (var1.mHeadsUpContentView != null) {
            this.mBuilder.setCustomHeadsUpContentView(var1.mHeadsUpContentView);
         }
      }

      if (VERSION.SDK_INT >= 26) {
         this.mBuilder.setBadgeIconType(var1.mBadgeIcon).setShortcutId(var1.mShortcutId).setTimeoutAfter(var1.mTimeout).setGroupAlertBehavior(var1.mGroupAlertBehavior);
         if (var1.mColorizedSet) {
            this.mBuilder.setColorized(var1.mColorized);
         }

         if (!TextUtils.isEmpty(var1.mChannelId)) {
            this.mBuilder.setSound((Uri)null).setDefaults(0).setLights(0, 0, 0).setVibrate((long[])null);
         }
      }

   }

   private void addAction(NotificationCompat.Action var1) {
      if (VERSION.SDK_INT >= 20) {
         android.app.Notification.Action.Builder var5 = new android.app.Notification.Action.Builder(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
         if (var1.getRemoteInputs() != null) {
            android.app.RemoteInput[] var4 = RemoteInput.fromCompat(var1.getRemoteInputs());
            int var3 = var4.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               var5.addRemoteInput(var4[var2]);
            }
         }

         Bundle var6;
         if (var1.getExtras() != null) {
            var6 = new Bundle(var1.getExtras());
         } else {
            var6 = new Bundle();
         }

         var6.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
         if (VERSION.SDK_INT >= 24) {
            var5.setAllowGeneratedReplies(var1.getAllowGeneratedReplies());
         }

         var6.putInt("android.support.action.semanticAction", var1.getSemanticAction());
         if (VERSION.SDK_INT >= 28) {
            var5.setSemanticAction(var1.getSemanticAction());
         }

         var6.putBoolean("android.support.action.showsUserInterface", var1.getShowsUserInterface());
         var5.addExtras(var6);
         this.mBuilder.addAction(var5.build());
      } else if (VERSION.SDK_INT >= 16) {
         this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, var1));
         return;
      }

   }

   private void removeSoundAndVibration(Notification var1) {
      var1.sound = null;
      var1.vibrate = null;
      var1.defaults &= -2;
      var1.defaults &= -3;
   }

   public Notification build() {
      NotificationCompat.Style var2 = this.mBuilderCompat.mStyle;
      if (var2 != null) {
         var2.apply(this);
      }

      RemoteViews var1;
      if (var2 != null) {
         var1 = var2.makeContentView(this);
      } else {
         var1 = null;
      }

      Notification var3 = this.buildInternal();
      if (var1 != null) {
         var3.contentView = var1;
      } else if (this.mBuilderCompat.mContentView != null) {
         var3.contentView = this.mBuilderCompat.mContentView;
      }

      if (VERSION.SDK_INT >= 16 && var2 != null) {
         var1 = var2.makeBigContentView(this);
         if (var1 != null) {
            var3.bigContentView = var1;
         }
      }

      if (VERSION.SDK_INT >= 21 && var2 != null) {
         var1 = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
         if (var1 != null) {
            var3.headsUpContentView = var1;
         }
      }

      if (VERSION.SDK_INT >= 16 && var2 != null) {
         Bundle var4 = NotificationCompat.getExtras(var3);
         if (var4 != null) {
            var2.addCompatExtras(var4);
         }
      }

      return var3;
   }

   protected Notification buildInternal() {
      if (VERSION.SDK_INT >= 26) {
         return this.mBuilder.build();
      } else {
         Notification var1;
         if (VERSION.SDK_INT >= 24) {
            var1 = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
               if (var1.getGroup() != null && (var1.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                  this.removeSoundAndVibration(var1);
               }

               if (var1.getGroup() != null && (var1.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                  this.removeSoundAndVibration(var1);
               }
            }

            return var1;
         } else {
            RemoteViews var8;
            if (VERSION.SDK_INT >= 21) {
               this.mBuilder.setExtras(this.mExtras);
               var1 = this.mBuilder.build();
               var8 = this.mContentView;
               if (var8 != null) {
                  var1.contentView = var8;
               }

               var8 = this.mBigContentView;
               if (var8 != null) {
                  var1.bigContentView = var8;
               }

               var8 = this.mHeadsUpContentView;
               if (var8 != null) {
                  var1.headsUpContentView = var8;
               }

               if (this.mGroupAlertBehavior != 0) {
                  if (var1.getGroup() != null && (var1.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                     this.removeSoundAndVibration(var1);
                  }

                  if (var1.getGroup() != null && (var1.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                     this.removeSoundAndVibration(var1);
                  }
               }

               return var1;
            } else if (VERSION.SDK_INT >= 20) {
               this.mBuilder.setExtras(this.mExtras);
               var1 = this.mBuilder.build();
               var8 = this.mContentView;
               if (var8 != null) {
                  var1.contentView = var8;
               }

               var8 = this.mBigContentView;
               if (var8 != null) {
                  var1.bigContentView = var8;
               }

               if (this.mGroupAlertBehavior != 0) {
                  if (var1.getGroup() != null && (var1.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                     this.removeSoundAndVibration(var1);
                  }

                  if (var1.getGroup() != null && (var1.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                     this.removeSoundAndVibration(var1);
                  }
               }

               return var1;
            } else if (VERSION.SDK_INT >= 19) {
               SparseArray var6 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
               if (var6 != null) {
                  this.mExtras.putSparseParcelableArray("android.support.actionExtras", var6);
               }

               this.mBuilder.setExtras(this.mExtras);
               var1 = this.mBuilder.build();
               var8 = this.mContentView;
               if (var8 != null) {
                  var1.contentView = var8;
               }

               var8 = this.mBigContentView;
               if (var8 != null) {
                  var1.bigContentView = var8;
               }

               return var1;
            } else if (VERSION.SDK_INT >= 16) {
               var1 = this.mBuilder.build();
               Bundle var2 = NotificationCompat.getExtras(var1);
               Bundle var3 = new Bundle(this.mExtras);
               Iterator var4 = this.mExtras.keySet().iterator();

               while(var4.hasNext()) {
                  String var5 = (String)var4.next();
                  if (var2.containsKey(var5)) {
                     var3.remove(var5);
                  }
               }

               var2.putAll(var3);
               SparseArray var7 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
               if (var7 != null) {
                  NotificationCompat.getExtras(var1).putSparseParcelableArray("android.support.actionExtras", var7);
               }

               var8 = this.mContentView;
               if (var8 != null) {
                  var1.contentView = var8;
               }

               var8 = this.mBigContentView;
               if (var8 != null) {
                  var1.bigContentView = var8;
               }

               return var1;
            } else {
               return this.mBuilder.getNotification();
            }
         }
      }
   }

   public Builder getBuilder() {
      return this.mBuilder;
   }
}
