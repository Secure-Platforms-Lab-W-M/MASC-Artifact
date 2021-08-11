package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public interface IMediaSession extends IInterface {
   void addQueueItem(MediaDescriptionCompat var1) throws RemoteException;

   void addQueueItemAt(MediaDescriptionCompat var1, int var2) throws RemoteException;

   void adjustVolume(int var1, int var2, String var3) throws RemoteException;

   void fastForward() throws RemoteException;

   Bundle getExtras() throws RemoteException;

   long getFlags() throws RemoteException;

   PendingIntent getLaunchPendingIntent() throws RemoteException;

   MediaMetadataCompat getMetadata() throws RemoteException;

   String getPackageName() throws RemoteException;

   PlaybackStateCompat getPlaybackState() throws RemoteException;

   List getQueue() throws RemoteException;

   CharSequence getQueueTitle() throws RemoteException;

   int getRatingType() throws RemoteException;

   int getRepeatMode() throws RemoteException;

   int getShuffleMode() throws RemoteException;

   String getTag() throws RemoteException;

   ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

   boolean isCaptioningEnabled() throws RemoteException;

   boolean isShuffleModeEnabledRemoved() throws RemoteException;

   boolean isTransportControlEnabled() throws RemoteException;

   void next() throws RemoteException;

   void pause() throws RemoteException;

   void play() throws RemoteException;

   void playFromMediaId(String var1, Bundle var2) throws RemoteException;

   void playFromSearch(String var1, Bundle var2) throws RemoteException;

   void playFromUri(Uri var1, Bundle var2) throws RemoteException;

   void prepare() throws RemoteException;

   void prepareFromMediaId(String var1, Bundle var2) throws RemoteException;

   void prepareFromSearch(String var1, Bundle var2) throws RemoteException;

   void prepareFromUri(Uri var1, Bundle var2) throws RemoteException;

   void previous() throws RemoteException;

   void rate(RatingCompat var1) throws RemoteException;

   void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException;

   void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   void removeQueueItem(MediaDescriptionCompat var1) throws RemoteException;

   void removeQueueItemAt(int var1) throws RemoteException;

   void rewind() throws RemoteException;

   void seekTo(long var1) throws RemoteException;

   void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException;

   void sendCustomAction(String var1, Bundle var2) throws RemoteException;

   boolean sendMediaButton(KeyEvent var1) throws RemoteException;

   void setCaptioningEnabled(boolean var1) throws RemoteException;

   void setRepeatMode(int var1) throws RemoteException;

   void setShuffleMode(int var1) throws RemoteException;

   void setShuffleModeEnabledRemoved(boolean var1) throws RemoteException;

   void setVolumeTo(int var1, int var2, String var3) throws RemoteException;

   void skipToQueueItem(long var1) throws RemoteException;

   void stop() throws RemoteException;

   void unregisterCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaSession {
      private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";
      static final int TRANSACTION_addQueueItem = 41;
      static final int TRANSACTION_addQueueItemAt = 42;
      static final int TRANSACTION_adjustVolume = 11;
      static final int TRANSACTION_fastForward = 22;
      static final int TRANSACTION_getExtras = 31;
      static final int TRANSACTION_getFlags = 9;
      static final int TRANSACTION_getLaunchPendingIntent = 8;
      static final int TRANSACTION_getMetadata = 27;
      static final int TRANSACTION_getPackageName = 6;
      static final int TRANSACTION_getPlaybackState = 28;
      static final int TRANSACTION_getQueue = 29;
      static final int TRANSACTION_getQueueTitle = 30;
      static final int TRANSACTION_getRatingType = 32;
      static final int TRANSACTION_getRepeatMode = 37;
      static final int TRANSACTION_getShuffleMode = 47;
      static final int TRANSACTION_getTag = 7;
      static final int TRANSACTION_getVolumeAttributes = 10;
      static final int TRANSACTION_isCaptioningEnabled = 45;
      static final int TRANSACTION_isShuffleModeEnabledRemoved = 38;
      static final int TRANSACTION_isTransportControlEnabled = 5;
      static final int TRANSACTION_next = 20;
      static final int TRANSACTION_pause = 18;
      static final int TRANSACTION_play = 13;
      static final int TRANSACTION_playFromMediaId = 14;
      static final int TRANSACTION_playFromSearch = 15;
      static final int TRANSACTION_playFromUri = 16;
      static final int TRANSACTION_prepare = 33;
      static final int TRANSACTION_prepareFromMediaId = 34;
      static final int TRANSACTION_prepareFromSearch = 35;
      static final int TRANSACTION_prepareFromUri = 36;
      static final int TRANSACTION_previous = 21;
      static final int TRANSACTION_rate = 25;
      static final int TRANSACTION_rateWithExtras = 51;
      static final int TRANSACTION_registerCallbackListener = 3;
      static final int TRANSACTION_removeQueueItem = 43;
      static final int TRANSACTION_removeQueueItemAt = 44;
      static final int TRANSACTION_rewind = 23;
      static final int TRANSACTION_seekTo = 24;
      static final int TRANSACTION_sendCommand = 1;
      static final int TRANSACTION_sendCustomAction = 26;
      static final int TRANSACTION_sendMediaButton = 2;
      static final int TRANSACTION_setCaptioningEnabled = 46;
      static final int TRANSACTION_setRepeatMode = 39;
      static final int TRANSACTION_setShuffleMode = 48;
      static final int TRANSACTION_setShuffleModeEnabledRemoved = 40;
      static final int TRANSACTION_setVolumeTo = 12;
      static final int TRANSACTION_skipToQueueItem = 17;
      static final int TRANSACTION_stop = 19;
      static final int TRANSACTION_unregisterCallbackListener = 4;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaSession");
      }

      public static IMediaSession asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaSession");
            return (IMediaSession)(var1 != null && var1 instanceof IMediaSession ? (IMediaSession)var1 : new IMediaSession.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }

      private static class Proxy implements IMediaSession {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public void addQueueItem(MediaDescriptionCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(41, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void addQueueItemAt(MediaDescriptionCompat var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(42, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void adjustVolume(int var1, int var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeString(var3);
               this.mRemote.transact(11, var4, var5, 0);
               var5.readException();
            } finally {
               var5.recycle();
               var4.recycle();
            }

         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void fastForward() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(22, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public Bundle getExtras() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            Bundle var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(31, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (Bundle)Bundle.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public long getFlags() throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            long var1;
            try {
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(9, var3, var4, 0);
               var4.readException();
               var1 = var4.readLong();
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var1;
         }

         public String getInterfaceDescriptor() {
            return "android.support.v4.media.session.IMediaSession";
         }

         public PendingIntent getLaunchPendingIntent() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            PendingIntent var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(8, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public MediaMetadataCompat getMetadata() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            MediaMetadataCompat var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(27, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public String getPackageName() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(6, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public PlaybackStateCompat getPlaybackState() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            PlaybackStateCompat var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(28, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public List getQueue() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            ArrayList var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(29, var1, var2, 0);
               var2.readException();
               var3 = var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public CharSequence getQueueTitle() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            CharSequence var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(30, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public int getRatingType() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(32, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public int getRepeatMode() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(37, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public int getShuffleMode() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(47, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public String getTag() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(7, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            ParcelableVolumeInfo var1;
            label37: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(10, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public boolean isCaptioningEnabled() throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var2 = false;
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(45, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isShuffleModeEnabledRemoved() throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var2 = false;
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(38, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isTransportControlEnabled() throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var2 = false;
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(5, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public void next() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(20, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void pause() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(18, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void play() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(13, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void playFromMediaId(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(14, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void playFromSearch(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(15, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void playFromUri(Uri var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(16, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepare() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(33, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void prepareFromMediaId(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(34, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepareFromSearch(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(35, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepareFromUri(Uri var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(36, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void previous() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(21, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void rate(RatingCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(25, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(51, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label124: {
               Throwable var10000;
               label128: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label128;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label128;
                     }
                  } else {
                     var16 = null;
                  }

                  label115:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(3, var2, var3, 0);
                     var3.readException();
                     break label124;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label115;
                  }
               }

               Throwable var17 = var10000;
               var3.recycle();
               var2.recycle();
               throw var17;
            }

            var3.recycle();
            var2.recycle();
         }

         public void removeQueueItem(MediaDescriptionCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(43, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void removeQueueItemAt(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(44, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void rewind() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(23, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void seekTo(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var3.writeLong(var1);
               this.mRemote.transact(24, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var4.writeString(var1);
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label309;
                  }

                  if (var2 != null) {
                     try {
                        var4.writeInt(1);
                        var2.writeToParcel(var4, 0);
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var3 != null) {
                     try {
                        var4.writeInt(1);
                        var3.writeToParcel(var4, 0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(1, var4, var5, 0);
                     var5.readException();
                     break label305;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var48 = var10000;
               var5.recycle();
               var4.recycle();
               throw var48;
            }

            var5.recycle();
            var4.recycle();
         }

         public void sendCustomAction(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(26, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public boolean sendMediaButton(KeyEvent var1) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var3 = false;

            int var2;
            label186: {
               Throwable var10000;
               label190: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label190;
                  }

                  if (var1 != null) {
                     try {
                        var4.writeInt(1);
                        var1.writeToParcel(var4, 0);
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label190;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label190;
                     }
                  }

                  label176:
                  try {
                     this.mRemote.transact(2, var4, var5, 0);
                     var5.readException();
                     var2 = var5.readInt();
                     break label186;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label176;
                  }
               }

               Throwable var26 = var10000;
               var5.recycle();
               var4.recycle();
               throw var26;
            }

            if (var2 != 0) {
               var3 = true;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public void setCaptioningEnabled(boolean var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label77: {
               Throwable var10000;
               label81: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label81;
                  }

                  byte var2;
                  if (var1) {
                     var2 = 1;
                  } else {
                     var2 = 0;
                  }

                  label72:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(46, var3, var4, 0);
                     var4.readException();
                     break label77;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label72;
                  }
               }

               Throwable var5 = var10000;
               var4.recycle();
               var3.recycle();
               throw var5;
            }

            var4.recycle();
            var3.recycle();
         }

         public void setRepeatMode(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(39, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setShuffleMode(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(48, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setShuffleModeEnabledRemoved(boolean var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label77: {
               Throwable var10000;
               label81: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label81;
                  }

                  byte var2;
                  if (var1) {
                     var2 = 1;
                  } else {
                     var2 = 0;
                  }

                  label72:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(40, var3, var4, 0);
                     var4.readException();
                     break label77;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label72;
                  }
               }

               Throwable var5 = var10000;
               var4.recycle();
               var3.recycle();
               throw var5;
            }

            var4.recycle();
            var3.recycle();
         }

         public void setVolumeTo(int var1, int var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeString(var3);
               this.mRemote.transact(12, var4, var5, 0);
               var5.readException();
            } finally {
               var5.recycle();
               var4.recycle();
            }

         }

         public void skipToQueueItem(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var3.writeLong(var1);
               this.mRemote.transact(17, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public void stop() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(19, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void unregisterCallbackListener(IMediaControllerCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label124: {
               Throwable var10000;
               label128: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label128;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label128;
                     }
                  } else {
                     var16 = null;
                  }

                  label115:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(4, var2, var3, 0);
                     var3.readException();
                     break label124;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label115;
                  }
               }

               Throwable var17 = var10000;
               var3.recycle();
               var2.recycle();
               throw var17;
            }

            var3.recycle();
            var2.recycle();
         }
      }
   }
}
