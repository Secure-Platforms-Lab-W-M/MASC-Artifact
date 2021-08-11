package androidx.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.os.IBinder.DeathRecipient;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.app.BundleCompat;
import androidx.core.util.Pair;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class MediaBrowserServiceCompat extends Service {
   static final boolean DEBUG = Log.isLoggable("MBServiceCompat", 3);
   private static final float EPSILON = 1.0E-5F;
   public static final String KEY_MEDIA_ITEM = "media_item";
   public static final String KEY_SEARCH_RESULTS = "search_results";
   public static final int RESULT_ERROR = -1;
   static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
   static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
   static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
   public static final int RESULT_OK = 0;
   public static final int RESULT_PROGRESS_UPDATE = 1;
   public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
   static final String TAG = "MBServiceCompat";
   final ArrayMap mConnections = new ArrayMap();
   MediaBrowserServiceCompat.ConnectionRecord mCurConnection;
   final MediaBrowserServiceCompat.ServiceHandler mHandler = new MediaBrowserServiceCompat.ServiceHandler();
   private MediaBrowserServiceCompat.MediaBrowserServiceImpl mImpl;
   MediaSessionCompat.Token mSession;

   void addSubscription(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, IBinder var3, Bundle var4) {
      List var6 = (List)var2.subscriptions.get(var1);
      Object var5 = var6;
      if (var6 == null) {
         var5 = new ArrayList();
      }

      Iterator var8 = ((List)var5).iterator();

      Pair var7;
      do {
         if (!var8.hasNext()) {
            ((List)var5).add(new Pair(var3, var4));
            var2.subscriptions.put(var1, var5);
            this.performLoadChildren(var1, var2, var4, (Bundle)null);
            this.mCurConnection = var2;
            this.onSubscribe(var1, var4);
            this.mCurConnection = null;
            return;
         }

         var7 = (Pair)var8.next();
      } while(var3 != var7.first || !MediaBrowserCompatUtils.areSameOptions(var4, (Bundle)var7.second));

   }

   List applyOptions(List var1, Bundle var2) {
      if (var1 == null) {
         return null;
      } else {
         int var3 = var2.getInt("android.media.browse.extra.PAGE", -1);
         int var6 = var2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
         if (var3 == -1 && var6 == -1) {
            return var1;
         } else {
            int var5 = var6 * var3;
            int var4 = var5 + var6;
            if (var3 >= 0 && var6 >= 1 && var5 < var1.size()) {
               var3 = var4;
               if (var4 > var1.size()) {
                  var3 = var1.size();
               }

               return var1.subList(var5, var3);
            } else {
               return Collections.emptyList();
            }
         }
      }
   }

   public void attachToBaseContext(Context var1) {
      this.attachBaseContext(var1);
   }

   public void dump(FileDescriptor var1, PrintWriter var2, String[] var3) {
   }

   public final Bundle getBrowserRootHints() {
      return this.mImpl.getBrowserRootHints();
   }

   public final MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
      return this.mImpl.getCurrentBrowserInfo();
   }

   public MediaSessionCompat.Token getSessionToken() {
      return this.mSession;
   }

   boolean isValidPackage(String var1, int var2) {
      if (var1 == null) {
         return false;
      } else {
         String[] var4 = this.getPackageManager().getPackagesForUid(var2);
         int var3 = var4.length;

         for(var2 = 0; var2 < var3; ++var2) {
            if (var4[var2].equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo var1, String var2, Bundle var3) {
      if (var1 != null) {
         if (var2 != null) {
            if (var3 != null) {
               this.mImpl.notifyChildrenChanged(var1, var2, var3);
            } else {
               throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
            }
         } else {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
         }
      } else {
         throw new IllegalArgumentException("remoteUserInfo cannot be null in notifyChildrenChanged");
      }
   }

   public void notifyChildrenChanged(String var1) {
      if (var1 != null) {
         this.mImpl.notifyChildrenChanged(var1, (Bundle)null);
      } else {
         throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
      }
   }

   public void notifyChildrenChanged(String var1, Bundle var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.mImpl.notifyChildrenChanged(var1, var2);
         } else {
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
         }
      } else {
         throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
      }
   }

   public IBinder onBind(Intent var1) {
      return this.mImpl.onBind(var1);
   }

   public void onCreate() {
      super.onCreate();
      if (VERSION.SDK_INT >= 28) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi28();
      } else if (VERSION.SDK_INT >= 26) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi26();
      } else if (VERSION.SDK_INT >= 23) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi23();
      } else if (VERSION.SDK_INT >= 21) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi21();
      } else {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplBase();
      }

      this.mImpl.onCreate();
   }

   public void onCustomAction(String var1, Bundle var2, MediaBrowserServiceCompat.Result var3) {
      var3.sendError((Bundle)null);
   }

   public abstract MediaBrowserServiceCompat.BrowserRoot onGetRoot(String var1, int var2, Bundle var3);

   public abstract void onLoadChildren(String var1, MediaBrowserServiceCompat.Result var2);

   public void onLoadChildren(String var1, MediaBrowserServiceCompat.Result var2, Bundle var3) {
      var2.setFlags(1);
      this.onLoadChildren(var1, var2);
   }

   public void onLoadItem(String var1, MediaBrowserServiceCompat.Result var2) {
      var2.setFlags(2);
      var2.sendResult((Object)null);
   }

   public void onSearch(String var1, Bundle var2, MediaBrowserServiceCompat.Result var3) {
      var3.setFlags(4);
      var3.sendResult((Object)null);
   }

   public void onSubscribe(String var1, Bundle var2) {
   }

   public void onUnsubscribe(String var1) {
   }

   void performCustomAction(String var1, Bundle var2, MediaBrowserServiceCompat.ConnectionRecord var3, final ResultReceiver var4) {
      MediaBrowserServiceCompat.Result var6 = new MediaBrowserServiceCompat.Result(var1) {
         void onErrorSent(Bundle var1) {
            var4.send(-1, var1);
         }

         void onProgressUpdateSent(Bundle var1) {
            var4.send(1, var1);
         }

         void onResultSent(Bundle var1) {
            var4.send(0, var1);
         }
      };
      this.mCurConnection = var3;
      this.onCustomAction(var1, var2, var6);
      this.mCurConnection = null;
      if (!var6.isDone()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("onCustomAction must call detach() or sendResult() or sendError() before returning for action=");
         var5.append(var1);
         var5.append(" extras=");
         var5.append(var2);
         throw new IllegalStateException(var5.toString());
      }
   }

   void performLoadChildren(final String var1, final MediaBrowserServiceCompat.ConnectionRecord var2, final Bundle var3, final Bundle var4) {
      MediaBrowserServiceCompat.Result var6 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1x) {
            StringBuilder var3x;
            if (MediaBrowserServiceCompat.this.mConnections.get(var2.callbacks.asBinder()) != var2) {
               if (MediaBrowserServiceCompat.DEBUG) {
                  var3x = new StringBuilder();
                  var3x.append("Not sending onLoadChildren result for connection that has been disconnected. pkg=");
                  var3x.append(var2.pkg);
                  var3x.append(" id=");
                  var3x.append(var1);
                  Log.d("MBServiceCompat", var3x.toString());
               }

            } else {
               if ((this.getFlags() & 1) != 0) {
                  var1x = MediaBrowserServiceCompat.this.applyOptions(var1x, var3);
               }

               try {
                  var2.callbacks.onLoadChildren(var1, var1x, var3, var4);
               } catch (RemoteException var2x) {
                  var3x = new StringBuilder();
                  var3x.append("Calling onLoadChildren() failed for id=");
                  var3x.append(var1);
                  var3x.append(" package=");
                  var3x.append(var2.pkg);
                  Log.w("MBServiceCompat", var3x.toString());
               }
            }
         }
      };
      this.mCurConnection = var2;
      if (var3 == null) {
         this.onLoadChildren(var1, var6);
      } else {
         this.onLoadChildren(var1, var6, var3);
      }

      this.mCurConnection = null;
      if (!var6.isDone()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("onLoadChildren must call detach() or sendResult() before returning for package=");
         var5.append(var2.pkg);
         var5.append(" id=");
         var5.append(var1);
         throw new IllegalStateException(var5.toString());
      }
   }

   void performLoadItem(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, final ResultReceiver var3) {
      MediaBrowserServiceCompat.Result var5 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(MediaBrowserCompat.MediaItem var1) {
            if ((this.getFlags() & 2) != 0) {
               var3.send(-1, (Bundle)null);
            } else {
               Bundle var2 = new Bundle();
               var2.putParcelable("media_item", var1);
               var3.send(0, var2);
            }
         }
      };
      this.mCurConnection = var2;
      this.onLoadItem(var1, var5);
      this.mCurConnection = null;
      if (!var5.isDone()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("onLoadItem must call detach() or sendResult() before returning for id=");
         var4.append(var1);
         throw new IllegalStateException(var4.toString());
      }
   }

   void performSearch(String var1, Bundle var2, MediaBrowserServiceCompat.ConnectionRecord var3, final ResultReceiver var4) {
      MediaBrowserServiceCompat.Result var6 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1) {
            if ((this.getFlags() & 4) == 0 && var1 != null) {
               Bundle var2 = new Bundle();
               var2.putParcelableArray("search_results", (Parcelable[])var1.toArray(new MediaBrowserCompat.MediaItem[0]));
               var4.send(0, var2);
            } else {
               var4.send(-1, (Bundle)null);
            }
         }
      };
      this.mCurConnection = var3;
      this.onSearch(var1, var2, var6);
      this.mCurConnection = null;
      if (!var6.isDone()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("onSearch must call detach() or sendResult() before returning for query=");
         var5.append(var1);
         throw new IllegalStateException(var5.toString());
      }
   }

   boolean removeSubscription(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, IBinder var3) {
      boolean var4;
      Throwable var10000;
      boolean var10001;
      if (var3 == null) {
         label487: {
            Object var50;
            try {
               var50 = var2.subscriptions.remove(var1);
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label487;
            }

            if (var50 != null) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.mCurConnection = var2;
            this.onUnsubscribe(var1);
            this.mCurConnection = null;
            return var4;
         }
      } else {
         label488: {
            boolean var5 = false;
            var4 = false;

            List var6;
            try {
               var6 = (List)var2.subscriptions.get(var1);
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label488;
            }

            if (var6 != null) {
               label489: {
                  Iterator var7;
                  try {
                     var7 = var6.iterator();
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label488;
                  }

                  label476:
                  while(true) {
                     try {
                        do {
                           if (!var7.hasNext()) {
                              break label476;
                           }
                        } while(var3 != ((Pair)var7.next()).first);
                     } catch (Throwable var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label488;
                     }

                     var4 = true;

                     try {
                        var7.remove();
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label488;
                     }
                  }

                  var5 = var4;

                  try {
                     if (var6.size() != 0) {
                        break label489;
                     }

                     var2.subscriptions.remove(var1);
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label488;
                  }

                  var5 = var4;
               }
            }

            this.mCurConnection = var2;
            this.onUnsubscribe(var1);
            this.mCurConnection = null;
            return var5;
         }
      }

      Throwable var51 = var10000;
      this.mCurConnection = var2;
      this.onUnsubscribe(var1);
      this.mCurConnection = null;
      throw var51;
   }

   public void setSessionToken(MediaSessionCompat.Token var1) {
      if (var1 != null) {
         if (this.mSession == null) {
            this.mSession = var1;
            this.mImpl.setSessionToken(var1);
         } else {
            throw new IllegalStateException("The session token has already been set.");
         }
      } else {
         throw new IllegalArgumentException("Session token may not be null.");
      }
   }

   public static final class BrowserRoot {
      public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
      public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
      public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
      @Deprecated
      public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
      private final Bundle mExtras;
      private final String mRootId;

      public BrowserRoot(String var1, Bundle var2) {
         if (var1 != null) {
            this.mRootId = var1;
            this.mExtras = var2;
         } else {
            throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
         }
      }

      public Bundle getExtras() {
         return this.mExtras;
      }

      public String getRootId() {
         return this.mRootId;
      }
   }

   private class ConnectionRecord implements DeathRecipient {
      public final MediaSessionManager.RemoteUserInfo browserInfo;
      public final MediaBrowserServiceCompat.ServiceCallbacks callbacks;
      public final int pid;
      public final String pkg;
      public MediaBrowserServiceCompat.BrowserRoot root;
      public final Bundle rootHints;
      public final HashMap subscriptions = new HashMap();
      public final int uid;

      ConnectionRecord(String var2, int var3, int var4, Bundle var5, MediaBrowserServiceCompat.ServiceCallbacks var6) {
         this.pkg = var2;
         this.pid = var3;
         this.uid = var4;
         this.browserInfo = new MediaSessionManager.RemoteUserInfo(var2, var3, var4);
         this.rootHints = var5;
         this.callbacks = var6;
      }

      public void binderDied() {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               MediaBrowserServiceCompat.this.mConnections.remove(ConnectionRecord.this.callbacks.asBinder());
            }
         });
      }
   }

   interface MediaBrowserServiceImpl {
      Bundle getBrowserRootHints();

      MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo();

      void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo var1, String var2, Bundle var3);

      void notifyChildrenChanged(String var1, Bundle var2);

      IBinder onBind(Intent var1);

      void onCreate();

      void setSessionToken(MediaSessionCompat.Token var1);
   }

   class MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompat.MediaBrowserServiceImpl, MediaBrowserServiceCompatApi21.ServiceCompatProxy {
      Messenger mMessenger;
      final List mRootExtrasList = new ArrayList();
      Object mServiceObj;

      public Bundle getBrowserRootHints() {
         if (this.mMessenger == null) {
            return null;
         } else if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            return MediaBrowserServiceCompat.this.mCurConnection.rootHints == null ? null : new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
         } else {
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
         }
      }

      public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            return MediaBrowserServiceCompat.this.mCurConnection.browserInfo;
         } else {
            throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
         }
      }

      public void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo var1, String var2, Bundle var3) {
         this.notifyChildrenChangedForCompat(var1, var2, var3);
      }

      public void notifyChildrenChanged(String var1, Bundle var2) {
         this.notifyChildrenChangedForFramework(var1, var2);
         this.notifyChildrenChangedForCompat(var1, var2);
      }

      void notifyChildrenChangedForCompat(final MediaSessionManager.RemoteUserInfo var1, final String var2, final Bundle var3) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               for(int var1x = 0; var1x < MediaBrowserServiceCompat.this.mConnections.size(); ++var1x) {
                  MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.valueAt(var1x);
                  if (var2x.browserInfo.equals(var1)) {
                     MediaBrowserServiceImplApi21.this.notifyChildrenChangedForCompatOnHandler(var2x, var2, var3);
                  }
               }

            }
         });
      }

      void notifyChildrenChangedForCompat(final String var1, final Bundle var2) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               Iterator var1x = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();

               while(var1x.hasNext()) {
                  IBinder var2x = (IBinder)var1x.next();
                  MediaBrowserServiceCompat.ConnectionRecord var3 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var2x);
                  MediaBrowserServiceImplApi21.this.notifyChildrenChangedForCompatOnHandler(var3, var1, var2);
               }

            }
         });
      }

      void notifyChildrenChangedForCompatOnHandler(MediaBrowserServiceCompat.ConnectionRecord var1, String var2, Bundle var3) {
         List var4 = (List)var1.subscriptions.get(var2);
         if (var4 != null) {
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
               Pair var5 = (Pair)var6.next();
               if (MediaBrowserCompatUtils.hasDuplicatedItems(var3, (Bundle)var5.second)) {
                  MediaBrowserServiceCompat.this.performLoadChildren(var2, var1, (Bundle)var5.second, var3);
               }
            }
         }

      }

      void notifyChildrenChangedForFramework(String var1, Bundle var2) {
         MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, var1);
      }

      public IBinder onBind(Intent var1) {
         return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, var1);
      }

      public void onCreate() {
         Object var1 = MediaBrowserServiceCompatApi21.createService(MediaBrowserServiceCompat.this, this);
         this.mServiceObj = var1;
         MediaBrowserServiceCompatApi21.onCreate(var1);
      }

      public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String var1, int var2, Bundle var3) {
         Bundle var5 = null;
         Bundle var4 = var5;
         if (var3 != null) {
            var4 = var5;
            if (var3.getInt("extra_client_version", 0) != 0) {
               var3.remove("extra_client_version");
               this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
               var5 = new Bundle();
               var5.putInt("extra_service_version", 2);
               BundleCompat.putBinder(var5, "extra_messenger", this.mMessenger.getBinder());
               if (MediaBrowserServiceCompat.this.mSession != null) {
                  IMediaSession var8 = MediaBrowserServiceCompat.this.mSession.getExtraBinder();
                  IBinder var9;
                  if (var8 == null) {
                     var9 = null;
                  } else {
                     var9 = var8.asBinder();
                  }

                  BundleCompat.putBinder(var5, "extra_session_binder", var9);
                  var4 = var5;
               } else {
                  this.mRootExtrasList.add(var5);
                  var4 = var5;
               }
            }
         }

         MediaBrowserServiceCompat var10 = MediaBrowserServiceCompat.this;
         var10.mCurConnection = var10.new ConnectionRecord(var1, -1, var2, var3, (MediaBrowserServiceCompat.ServiceCallbacks)null);
         MediaBrowserServiceCompat.BrowserRoot var7 = MediaBrowserServiceCompat.this.onGetRoot(var1, var2, var3);
         MediaBrowserServiceCompat.this.mCurConnection = null;
         if (var7 == null) {
            return null;
         } else {
            Bundle var6;
            if (var4 == null) {
               var6 = var7.getExtras();
            } else {
               var6 = var4;
               if (var7.getExtras() != null) {
                  var4.putAll(var7.getExtras());
                  var6 = var4;
               }
            }

            return new MediaBrowserServiceCompatApi21.BrowserRoot(var7.getRootId(), var6);
         }
      }

      public void onLoadChildren(String var1, final MediaBrowserServiceCompatApi21.ResultWrapper var2) {
         MediaBrowserServiceCompat.Result var3 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(List var1) {
               ArrayList var2x = null;
               if (var1 != null) {
                  ArrayList var3 = new ArrayList();
                  Iterator var5 = var1.iterator();

                  while(true) {
                     var2x = var3;
                     if (!var5.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var6 = (MediaBrowserCompat.MediaItem)var5.next();
                     Parcel var4 = Parcel.obtain();
                     var6.writeToParcel(var4, 0);
                     var3.add(var4);
                  }
               }

               var2.sendResult(var2x);
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var3);
      }

      public void setSessionToken(final MediaSessionCompat.Token var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               if (!MediaBrowserServiceImplApi21.this.mRootExtrasList.isEmpty()) {
                  IMediaSession var1x = var1.getExtraBinder();
                  if (var1x != null) {
                     Iterator var2 = MediaBrowserServiceImplApi21.this.mRootExtrasList.iterator();

                     while(var2.hasNext()) {
                        BundleCompat.putBinder((Bundle)var2.next(), "extra_session_binder", var1x.asBinder());
                     }
                  }

                  MediaBrowserServiceImplApi21.this.mRootExtrasList.clear();
               }

               MediaBrowserServiceCompatApi21.setSessionToken(MediaBrowserServiceImplApi21.this.mServiceObj, var1.getToken());
            }
         });
      }
   }

   class MediaBrowserServiceImplApi23 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompatApi23.ServiceCompatProxy {
      MediaBrowserServiceImplApi23() {
         super();
      }

      public void onCreate() {
         this.mServiceObj = MediaBrowserServiceCompatApi23.createService(MediaBrowserServiceCompat.this, this);
         MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
      }

      public void onLoadItem(String var1, final MediaBrowserServiceCompatApi21.ResultWrapper var2) {
         MediaBrowserServiceCompat.Result var3 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(MediaBrowserCompat.MediaItem var1) {
               if (var1 == null) {
                  var2.sendResult((Object)null);
               } else {
                  Parcel var2x = Parcel.obtain();
                  var1.writeToParcel(var2x, 0);
                  var2.sendResult(var2x);
               }
            }
         };
         MediaBrowserServiceCompat.this.onLoadItem(var1, var3);
      }
   }

   class MediaBrowserServiceImplApi26 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi26.ServiceCompatProxy {
      MediaBrowserServiceImplApi26() {
         super();
      }

      public Bundle getBrowserRootHints() {
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            return MediaBrowserServiceCompat.this.mCurConnection.rootHints == null ? null : new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
         } else {
            return MediaBrowserServiceCompatApi26.getBrowserRootHints(this.mServiceObj);
         }
      }

      void notifyChildrenChangedForFramework(String var1, Bundle var2) {
         if (var2 != null) {
            MediaBrowserServiceCompatApi26.notifyChildrenChanged(this.mServiceObj, var1, var2);
         } else {
            super.notifyChildrenChangedForFramework(var1, var2);
         }
      }

      public void onCreate() {
         this.mServiceObj = MediaBrowserServiceCompatApi26.createService(MediaBrowserServiceCompat.this, this);
         MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
      }

      public void onLoadChildren(String var1, final MediaBrowserServiceCompatApi26.ResultWrapper var2, Bundle var3) {
         MediaBrowserServiceCompat.Result var4 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(List var1) {
               ArrayList var2x = null;
               if (var1 != null) {
                  ArrayList var3 = new ArrayList();
                  Iterator var5 = var1.iterator();

                  while(true) {
                     var2x = var3;
                     if (!var5.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var6 = (MediaBrowserCompat.MediaItem)var5.next();
                     Parcel var4 = Parcel.obtain();
                     var6.writeToParcel(var4, 0);
                     var3.add(var4);
                  }
               }

               var2.sendResult(var2x, this.getFlags());
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var4, var3);
      }
   }

   class MediaBrowserServiceImplApi28 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi26 {
      MediaBrowserServiceImplApi28() {
         super();
      }

      public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
         return MediaBrowserServiceCompat.this.mCurConnection != null ? MediaBrowserServiceCompat.this.mCurConnection.browserInfo : new MediaSessionManager.RemoteUserInfo(((MediaBrowserService)this.mServiceObj).getCurrentBrowserInfo());
      }
   }

   class MediaBrowserServiceImplBase implements MediaBrowserServiceCompat.MediaBrowserServiceImpl {
      private Messenger mMessenger;

      public Bundle getBrowserRootHints() {
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            return MediaBrowserServiceCompat.this.mCurConnection.rootHints == null ? null : new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
         } else {
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
         }
      }

      public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            return MediaBrowserServiceCompat.this.mCurConnection.browserInfo;
         } else {
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
         }
      }

      public void notifyChildrenChanged(final MediaSessionManager.RemoteUserInfo var1, final String var2, final Bundle var3) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               for(int var1x = 0; var1x < MediaBrowserServiceCompat.this.mConnections.size(); ++var1x) {
                  MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.valueAt(var1x);
                  if (var2x.browserInfo.equals(var1)) {
                     MediaBrowserServiceImplBase.this.notifyChildrenChangedOnHandler(var2x, var2, var3);
                     return;
                  }
               }

            }
         });
      }

      public void notifyChildrenChanged(final String var1, final Bundle var2) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               Iterator var1x = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();

               while(var1x.hasNext()) {
                  IBinder var2x = (IBinder)var1x.next();
                  MediaBrowserServiceCompat.ConnectionRecord var3 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var2x);
                  MediaBrowserServiceImplBase.this.notifyChildrenChangedOnHandler(var3, var1, var2);
               }

            }
         });
      }

      void notifyChildrenChangedOnHandler(MediaBrowserServiceCompat.ConnectionRecord var1, String var2, Bundle var3) {
         List var4 = (List)var1.subscriptions.get(var2);
         if (var4 != null) {
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
               Pair var5 = (Pair)var6.next();
               if (MediaBrowserCompatUtils.hasDuplicatedItems(var3, (Bundle)var5.second)) {
                  MediaBrowserServiceCompat.this.performLoadChildren(var2, var1, (Bundle)var5.second, var3);
               }
            }
         }

      }

      public IBinder onBind(Intent var1) {
         return "android.media.browse.MediaBrowserService".equals(var1.getAction()) ? this.mMessenger.getBinder() : null;
      }

      public void onCreate() {
         this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
      }

      public void setSessionToken(final MediaSessionCompat.Token var1) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               Iterator var1x = MediaBrowserServiceCompat.this.mConnections.values().iterator();

               while(var1x.hasNext()) {
                  MediaBrowserServiceCompat.ConnectionRecord var2 = (MediaBrowserServiceCompat.ConnectionRecord)var1x.next();

                  try {
                     var2.callbacks.onConnect(var2.root.getRootId(), var1, var2.root.getExtras());
                  } catch (RemoteException var4) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Connection for ");
                     var3.append(var2.pkg);
                     var3.append(" is no longer valid.");
                     Log.w("MBServiceCompat", var3.toString());
                     var1x.remove();
                  }
               }

            }
         });
      }
   }

   public static class Result {
      private final Object mDebug;
      private boolean mDetachCalled;
      private int mFlags;
      private boolean mSendErrorCalled;
      private boolean mSendProgressUpdateCalled;
      private boolean mSendResultCalled;

      Result(Object var1) {
         this.mDebug = var1;
      }

      private void checkExtraFields(Bundle var1) {
         if (var1 != null) {
            if (var1.containsKey("android.media.browse.extra.DOWNLOAD_PROGRESS")) {
               float var2 = var1.getFloat("android.media.browse.extra.DOWNLOAD_PROGRESS");
               if (var2 < -1.0E-5F || var2 > 1.00001F) {
                  throw new IllegalArgumentException("The value of the EXTRA_DOWNLOAD_PROGRESS field must be a float number within [0.0, 1.0].");
               }
            }
         }
      }

      public void detach() {
         StringBuilder var1;
         if (!this.mDetachCalled) {
            if (!this.mSendResultCalled) {
               if (!this.mSendErrorCalled) {
                  this.mDetachCalled = true;
               } else {
                  var1 = new StringBuilder();
                  var1.append("detach() called when sendError() had already been called for: ");
                  var1.append(this.mDebug);
                  throw new IllegalStateException(var1.toString());
               }
            } else {
               var1 = new StringBuilder();
               var1.append("detach() called when sendResult() had already been called for: ");
               var1.append(this.mDebug);
               throw new IllegalStateException(var1.toString());
            }
         } else {
            var1 = new StringBuilder();
            var1.append("detach() called when detach() had already been called for: ");
            var1.append(this.mDebug);
            throw new IllegalStateException(var1.toString());
         }
      }

      int getFlags() {
         return this.mFlags;
      }

      boolean isDone() {
         return this.mDetachCalled || this.mSendResultCalled || this.mSendErrorCalled;
      }

      void onErrorSent(Bundle var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("It is not supported to send an error for ");
         var2.append(this.mDebug);
         throw new UnsupportedOperationException(var2.toString());
      }

      void onProgressUpdateSent(Bundle var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("It is not supported to send an interim update for ");
         var2.append(this.mDebug);
         throw new UnsupportedOperationException(var2.toString());
      }

      void onResultSent(Object var1) {
      }

      public void sendError(Bundle var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.mSendErrorCalled = true;
            this.onErrorSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendError() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      public void sendProgressUpdate(Bundle var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.checkExtraFields(var1);
            this.mSendProgressUpdateCalled = true;
            this.onProgressUpdateSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      public void sendResult(Object var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.mSendResultCalled = true;
            this.onResultSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendResult() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      void setFlags(int var1) {
         this.mFlags = var1;
      }
   }

   private class ServiceBinderImpl {
      ServiceBinderImpl() {
      }

      public void addSubscription(final String var1, final IBinder var2, final Bundle var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var4.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
               if (var2x == null) {
                  StringBuilder var3x = new StringBuilder();
                  var3x.append("addSubscription for callback that isn't registered id=");
                  var3x.append(var1);
                  Log.w("MBServiceCompat", var3x.toString());
               } else {
                  MediaBrowserServiceCompat.this.addSubscription(var1, var2x, var2, var3);
               }
            }
         });
      }

      public void connect(final String var1, final int var2, final int var3, final Bundle var4, final MediaBrowserServiceCompat.ServiceCallbacks var5) {
         if (MediaBrowserServiceCompat.this.isValidPackage(var1, var3)) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var5.asBinder();
                  MediaBrowserServiceCompat.this.mConnections.remove(var1x);
                  MediaBrowserServiceCompat.ConnectionRecord var2x = MediaBrowserServiceCompat.this.new ConnectionRecord(var1, var2, var3, var4, var5);
                  MediaBrowserServiceCompat.this.mCurConnection = var2x;
                  var2x.root = MediaBrowserServiceCompat.this.onGetRoot(var1, var3, var4);
                  MediaBrowserServiceCompat.this.mCurConnection = null;
                  if (var2x.root == null) {
                     StringBuilder var5x = new StringBuilder();
                     var5x.append("No root for client ");
                     var5x.append(var1);
                     var5x.append(" from service ");
                     var5x.append(this.getClass().getName());
                     Log.i("MBServiceCompat", var5x.toString());

                     try {
                        var5.onConnectFailed();
                     } catch (RemoteException var3x) {
                        var5x = new StringBuilder();
                        var5x.append("Calling onConnectFailed() failed. Ignoring. pkg=");
                        var5x.append(var1);
                        Log.w("MBServiceCompat", var5x.toString());
                     }

                  } else {
                     try {
                        MediaBrowserServiceCompat.this.mConnections.put(var1x, var2x);
                        var1x.linkToDeath(var2x, 0);
                        if (MediaBrowserServiceCompat.this.mSession != null) {
                           var5.onConnect(var2x.root.getRootId(), MediaBrowserServiceCompat.this.mSession, var2x.root.getExtras());
                        }

                     } catch (RemoteException var4x) {
                        StringBuilder var6 = new StringBuilder();
                        var6.append("Calling onConnect() failed. Dropping client. pkg=");
                        var6.append(var1);
                        Log.w("MBServiceCompat", var6.toString());
                        MediaBrowserServiceCompat.this.mConnections.remove(var1x);
                     }
                  }
               }
            });
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("Package/uid mismatch: uid=");
            var6.append(var3);
            var6.append(" package=");
            var6.append(var1);
            throw new IllegalArgumentException(var6.toString());
         }
      }

      public void disconnect(final MediaBrowserServiceCompat.ServiceCallbacks var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(var1x);
               if (var2 != null) {
                  var2.callbacks.asBinder().unlinkToDeath(var2, 0);
               }

            }
         });
      }

      public void getMediaItem(final String var1, final ResultReceiver var2, final MediaBrowserServiceCompat.ServiceCallbacks var3) {
         if (!TextUtils.isEmpty(var1)) {
            if (var2 != null) {
               MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                  public void run() {
                     IBinder var1x = var3.asBinder();
                     MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                     if (var2x == null) {
                        StringBuilder var3x = new StringBuilder();
                        var3x.append("getMediaItem for callback that isn't registered id=");
                        var3x.append(var1);
                        Log.w("MBServiceCompat", var3x.toString());
                     } else {
                        MediaBrowserServiceCompat.this.performLoadItem(var1, var2x, var2);
                     }
                  }
               });
            }
         }
      }

      public void registerCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks var1, final String var2, final int var3, final int var4, final Bundle var5) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               MediaBrowserServiceCompat.this.mConnections.remove(var1x);
               MediaBrowserServiceCompat.ConnectionRecord var2x = MediaBrowserServiceCompat.this.new ConnectionRecord(var2, var3, var4, var5, var1);
               MediaBrowserServiceCompat.this.mConnections.put(var1x, var2x);

               try {
                  var1x.linkToDeath(var2x, 0);
               } catch (RemoteException var3x) {
                  Log.w("MBServiceCompat", "IBinder is already dead.");
               }
            }
         });
      }

      public void removeSubscription(final String var1, final IBinder var2, final MediaBrowserServiceCompat.ServiceCallbacks var3) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var3.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
               StringBuilder var3x;
               if (var2x == null) {
                  var3x = new StringBuilder();
                  var3x.append("removeSubscription for callback that isn't registered id=");
                  var3x.append(var1);
                  Log.w("MBServiceCompat", var3x.toString());
               } else {
                  if (!MediaBrowserServiceCompat.this.removeSubscription(var1, var2x, var2)) {
                     var3x = new StringBuilder();
                     var3x.append("removeSubscription called for ");
                     var3x.append(var1);
                     var3x.append(" which is not subscribed");
                     Log.w("MBServiceCompat", var3x.toString());
                  }

               }
            }
         });
      }

      public void search(final String var1, final Bundle var2, final ResultReceiver var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!TextUtils.isEmpty(var1)) {
            if (var3 != null) {
               MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                  public void run() {
                     IBinder var1x = var4.asBinder();
                     MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                     if (var2x == null) {
                        StringBuilder var3x = new StringBuilder();
                        var3x.append("search for callback that isn't registered query=");
                        var3x.append(var1);
                        Log.w("MBServiceCompat", var3x.toString());
                     } else {
                        MediaBrowserServiceCompat.this.performSearch(var1, var2, var2x, var3);
                     }
                  }
               });
            }
         }
      }

      public void sendCustomAction(final String var1, final Bundle var2, final ResultReceiver var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!TextUtils.isEmpty(var1)) {
            if (var3 != null) {
               MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                  public void run() {
                     IBinder var1x = var4.asBinder();
                     MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                     if (var2x == null) {
                        StringBuilder var3x = new StringBuilder();
                        var3x.append("sendCustomAction for callback that isn't registered action=");
                        var3x.append(var1);
                        var3x.append(", extras=");
                        var3x.append(var2);
                        Log.w("MBServiceCompat", var3x.toString());
                     } else {
                        MediaBrowserServiceCompat.this.performCustomAction(var1, var2, var2x, var3);
                     }
                  }
               });
            }
         }
      }

      public void unregisterCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(var1x);
               if (var2 != null) {
                  var1x.unlinkToDeath(var2, 0);
               }

            }
         });
      }
   }

   private interface ServiceCallbacks {
      IBinder asBinder();

      void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException;

      void onConnectFailed() throws RemoteException;

      void onLoadChildren(String var1, List var2, Bundle var3, Bundle var4) throws RemoteException;
   }

   private static class ServiceCallbacksCompat implements MediaBrowserServiceCompat.ServiceCallbacks {
      final Messenger mCallbacks;

      ServiceCallbacksCompat(Messenger var1) {
         this.mCallbacks = var1;
      }

      private void sendRequest(int var1, Bundle var2) throws RemoteException {
         Message var3 = Message.obtain();
         var3.what = var1;
         var3.arg1 = 2;
         var3.setData(var2);
         this.mCallbacks.send(var3);
      }

      public IBinder asBinder() {
         return this.mCallbacks.getBinder();
      }

      public void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException {
         Bundle var4 = var3;
         if (var3 == null) {
            var4 = new Bundle();
         }

         var4.putInt("extra_service_version", 2);
         var3 = new Bundle();
         var3.putString("data_media_item_id", var1);
         var3.putParcelable("data_media_session_token", var2);
         var3.putBundle("data_root_hints", var4);
         this.sendRequest(1, var3);
      }

      public void onConnectFailed() throws RemoteException {
         this.sendRequest(2, (Bundle)null);
      }

      public void onLoadChildren(String var1, List var2, Bundle var3, Bundle var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_media_item_id", var1);
         var5.putBundle("data_options", var3);
         var5.putBundle("data_notify_children_changed_options", var4);
         if (var2 != null) {
            ArrayList var6;
            if (var2 instanceof ArrayList) {
               var6 = (ArrayList)var2;
            } else {
               var6 = new ArrayList(var2);
            }

            var5.putParcelableArrayList("data_media_item_list", var6);
         }

         this.sendRequest(3, var5);
      }
   }

   private final class ServiceHandler extends Handler {
      private final MediaBrowserServiceCompat.ServiceBinderImpl mServiceBinderImpl = MediaBrowserServiceCompat.this.new ServiceBinderImpl();

      ServiceHandler() {
      }

      public void handleMessage(Message var1) {
         Bundle var2 = var1.getData();
         Bundle var3;
         switch(var1.what) {
         case 1:
            var3 = var2.getBundle("data_root_hints");
            MediaSessionCompat.ensureClassLoader(var3);
            this.mServiceBinderImpl.connect(var2.getString("data_package_name"), var2.getInt("data_calling_pid"), var2.getInt("data_calling_uid"), var3, new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 2:
            this.mServiceBinderImpl.disconnect(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 3:
            var3 = var2.getBundle("data_options");
            MediaSessionCompat.ensureClassLoader(var3);
            this.mServiceBinderImpl.addSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), var3, new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 4:
            this.mServiceBinderImpl.removeSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 5:
            this.mServiceBinderImpl.getMediaItem(var2.getString("data_media_item_id"), (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 6:
            var3 = var2.getBundle("data_root_hints");
            MediaSessionCompat.ensureClassLoader(var3);
            this.mServiceBinderImpl.registerCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo), var2.getString("data_package_name"), var2.getInt("data_calling_pid"), var2.getInt("data_calling_uid"), var3);
            return;
         case 7:
            this.mServiceBinderImpl.unregisterCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 8:
            var3 = var2.getBundle("data_search_extras");
            MediaSessionCompat.ensureClassLoader(var3);
            this.mServiceBinderImpl.search(var2.getString("data_search_query"), var3, (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         case 9:
            var3 = var2.getBundle("data_custom_action_extras");
            MediaSessionCompat.ensureClassLoader(var3);
            this.mServiceBinderImpl.sendCustomAction(var2.getString("data_custom_action"), var3, (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            return;
         default:
            StringBuilder var4 = new StringBuilder();
            var4.append("Unhandled message: ");
            var4.append(var1);
            var4.append("\n  Service version: ");
            var4.append(2);
            var4.append("\n  Client version: ");
            var4.append(var1.arg1);
            Log.w("MBServiceCompat", var4.toString());
         }
      }

      public void postOrRun(Runnable var1) {
         if (Thread.currentThread() == this.getLooper().getThread()) {
            var1.run();
         } else {
            this.post(var1);
         }
      }

      public boolean sendMessageAtTime(Message var1, long var2) {
         Bundle var4 = var1.getData();
         var4.setClassLoader(MediaBrowserCompat.class.getClassLoader());
         var4.putInt("data_calling_uid", Binder.getCallingUid());
         var4.putInt("data_calling_pid", Binder.getCallingPid());
         return super.sendMessageAtTime(var1, var2);
      }
   }
}
