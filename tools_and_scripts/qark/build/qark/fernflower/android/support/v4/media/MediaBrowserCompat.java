package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BadParcelableException;
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
import android.os.Parcelable.Creator;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.app.BundleCompat;
import androidx.media.MediaBrowserCompatUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class MediaBrowserCompat {
   public static final String CUSTOM_ACTION_DOWNLOAD = "android.support.v4.media.action.DOWNLOAD";
   public static final String CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE = "android.support.v4.media.action.REMOVE_DOWNLOADED_FILE";
   static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
   public static final String EXTRA_DOWNLOAD_PROGRESS = "android.media.browse.extra.DOWNLOAD_PROGRESS";
   public static final String EXTRA_MEDIA_ID = "android.media.browse.extra.MEDIA_ID";
   public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
   public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
   static final String TAG = "MediaBrowserCompat";
   private final MediaBrowserCompat.MediaBrowserImpl mImpl;

   public MediaBrowserCompat(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
      if (VERSION.SDK_INT >= 26) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi26(var1, var2, var3, var4);
      } else if (VERSION.SDK_INT >= 23) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi23(var1, var2, var3, var4);
      } else if (VERSION.SDK_INT >= 21) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi21(var1, var2, var3, var4);
      } else {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplBase(var1, var2, var3, var4);
      }
   }

   public void connect() {
      this.mImpl.connect();
   }

   public void disconnect() {
      this.mImpl.disconnect();
   }

   public Bundle getExtras() {
      return this.mImpl.getExtras();
   }

   public void getItem(String var1, MediaBrowserCompat.ItemCallback var2) {
      this.mImpl.getItem(var1, var2);
   }

   public Bundle getNotifyChildrenChangedOptions() {
      return this.mImpl.getNotifyChildrenChangedOptions();
   }

   public String getRoot() {
      return this.mImpl.getRoot();
   }

   public ComponentName getServiceComponent() {
      return this.mImpl.getServiceComponent();
   }

   public MediaSessionCompat.Token getSessionToken() {
      return this.mImpl.getSessionToken();
   }

   public boolean isConnected() {
      return this.mImpl.isConnected();
   }

   public void search(String var1, Bundle var2, MediaBrowserCompat.SearchCallback var3) {
      if (!TextUtils.isEmpty(var1)) {
         if (var3 != null) {
            this.mImpl.search(var1, var2, var3);
         } else {
            throw new IllegalArgumentException("callback cannot be null");
         }
      } else {
         throw new IllegalArgumentException("query cannot be empty");
      }
   }

   public void sendCustomAction(String var1, Bundle var2, MediaBrowserCompat.CustomActionCallback var3) {
      if (!TextUtils.isEmpty(var1)) {
         this.mImpl.sendCustomAction(var1, var2, var3);
      } else {
         throw new IllegalArgumentException("action cannot be empty");
      }
   }

   public void subscribe(String var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3) {
      if (!TextUtils.isEmpty(var1)) {
         if (var3 != null) {
            if (var2 != null) {
               this.mImpl.subscribe(var1, var2, var3);
            } else {
               throw new IllegalArgumentException("options are null");
            }
         } else {
            throw new IllegalArgumentException("callback is null");
         }
      } else {
         throw new IllegalArgumentException("parentId is empty");
      }
   }

   public void subscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2) {
      if (!TextUtils.isEmpty(var1)) {
         if (var2 != null) {
            this.mImpl.subscribe(var1, (Bundle)null, var2);
         } else {
            throw new IllegalArgumentException("callback is null");
         }
      } else {
         throw new IllegalArgumentException("parentId is empty");
      }
   }

   public void unsubscribe(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.mImpl.unsubscribe(var1, (MediaBrowserCompat.SubscriptionCallback)null);
      } else {
         throw new IllegalArgumentException("parentId is empty");
      }
   }

   public void unsubscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2) {
      if (!TextUtils.isEmpty(var1)) {
         if (var2 != null) {
            this.mImpl.unsubscribe(var1, var2);
         } else {
            throw new IllegalArgumentException("callback is null");
         }
      } else {
         throw new IllegalArgumentException("parentId is empty");
      }
   }

   private static class CallbackHandler extends Handler {
      private final WeakReference mCallbackImplRef;
      private WeakReference mCallbacksMessengerRef;

      CallbackHandler(MediaBrowserCompat.MediaBrowserServiceCallbackImpl var1) {
         this.mCallbackImplRef = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         WeakReference var3 = this.mCallbacksMessengerRef;
         if (var3 != null && var3.get() != null) {
            if (this.mCallbackImplRef.get() != null) {
               Bundle var5 = var1.getData();
               MediaSessionCompat.ensureClassLoader(var5);
               MediaBrowserCompat.MediaBrowserServiceCallbackImpl var14 = (MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get();
               Messenger var4 = (Messenger)this.mCallbacksMessengerRef.get();

               label62: {
                  boolean var10001;
                  int var2;
                  try {
                     var2 = var1.what;
                  } catch (BadParcelableException var13) {
                     var10001 = false;
                     break label62;
                  }

                  Bundle var6;
                  if (var2 != 1) {
                     if (var2 != 2) {
                        if (var2 != 3) {
                           try {
                              StringBuilder var15 = new StringBuilder();
                              var15.append("Unhandled message: ");
                              var15.append(var1);
                              var15.append("\n  Client version: ");
                              var15.append(1);
                              var15.append("\n  Service version: ");
                              var15.append(var1.arg1);
                              Log.w("MediaBrowserCompat", var15.toString());
                           } catch (BadParcelableException var12) {
                              var10001 = false;
                              break label62;
                           }
                        } else {
                           try {
                              var6 = var5.getBundle("data_options");
                              MediaSessionCompat.ensureClassLoader(var6);
                              Bundle var7 = var5.getBundle("data_notify_children_changed_options");
                              MediaSessionCompat.ensureClassLoader(var7);
                              var14.onLoadChildren(var4, var5.getString("data_media_item_id"), var5.getParcelableArrayList("data_media_item_list"), var6, var7);
                           } catch (BadParcelableException var11) {
                              var10001 = false;
                              break label62;
                           }
                        }
                     } else {
                        try {
                           var14.onConnectionFailed(var4);
                        } catch (BadParcelableException var10) {
                           var10001 = false;
                           break label62;
                        }
                     }
                  } else {
                     try {
                        var6 = var5.getBundle("data_root_hints");
                        MediaSessionCompat.ensureClassLoader(var6);
                        var14.onServiceConnected(var4, var5.getString("data_media_item_id"), (MediaSessionCompat.Token)var5.getParcelable("data_media_session_token"), var6);
                     } catch (BadParcelableException var9) {
                        var10001 = false;
                        break label62;
                     }
                  }

                  try {
                     return;
                  } catch (BadParcelableException var8) {
                     var10001 = false;
                  }
               }

               Log.e("MediaBrowserCompat", "Could not unparcel the data.");
               if (var1.what == 1) {
                  var14.onConnectionFailed(var4);
               }

            }
         }
      }

      void setCallbacksMessenger(Messenger var1) {
         this.mCallbacksMessengerRef = new WeakReference(var1);
      }
   }

   public static class ConnectionCallback {
      MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal mConnectionCallbackInternal;
      final Object mConnectionCallbackObj;

      public ConnectionCallback() {
         if (VERSION.SDK_INT >= 21) {
            this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new MediaBrowserCompat.ConnectionCallback.StubApi21());
         } else {
            this.mConnectionCallbackObj = null;
         }
      }

      public void onConnected() {
      }

      public void onConnectionFailed() {
      }

      public void onConnectionSuspended() {
      }

      void setInternalConnectionCallback(MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal var1) {
         this.mConnectionCallbackInternal = var1;
      }

      interface ConnectionCallbackInternal {
         void onConnected();

         void onConnectionFailed();

         void onConnectionSuspended();
      }

      private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback {
         StubApi21() {
         }

         public void onConnected() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
            }

            ConnectionCallback.this.onConnected();
         }

         public void onConnectionFailed() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
            }

            ConnectionCallback.this.onConnectionFailed();
         }

         public void onConnectionSuspended() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
            }

            ConnectionCallback.this.onConnectionSuspended();
         }
      }
   }

   public abstract static class CustomActionCallback {
      public void onError(String var1, Bundle var2, Bundle var3) {
      }

      public void onProgressUpdate(String var1, Bundle var2, Bundle var3) {
      }

      public void onResult(String var1, Bundle var2, Bundle var3) {
      }
   }

   private static class CustomActionResultReceiver extends ResultReceiver {
      private final String mAction;
      private final MediaBrowserCompat.CustomActionCallback mCallback;
      private final Bundle mExtras;

      CustomActionResultReceiver(String var1, Bundle var2, MediaBrowserCompat.CustomActionCallback var3, Handler var4) {
         super(var4);
         this.mAction = var1;
         this.mExtras = var2;
         this.mCallback = var3;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         if (this.mCallback != null) {
            MediaSessionCompat.ensureClassLoader(var2);
            if (var1 != -1) {
               if (var1 != 0) {
                  if (var1 != 1) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Unknown result code: ");
                     var3.append(var1);
                     var3.append(" (extras=");
                     var3.append(this.mExtras);
                     var3.append(", resultData=");
                     var3.append(var2);
                     var3.append(")");
                     Log.w("MediaBrowserCompat", var3.toString());
                  } else {
                     this.mCallback.onProgressUpdate(this.mAction, this.mExtras, var2);
                  }
               } else {
                  this.mCallback.onResult(this.mAction, this.mExtras, var2);
               }
            } else {
               this.mCallback.onError(this.mAction, this.mExtras, var2);
            }
         }
      }
   }

   public abstract static class ItemCallback {
      final Object mItemCallbackObj;

      public ItemCallback() {
         if (VERSION.SDK_INT >= 23) {
            this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new MediaBrowserCompat.ItemCallback.StubApi23());
         } else {
            this.mItemCallbackObj = null;
         }
      }

      public void onError(String var1) {
      }

      public void onItemLoaded(MediaBrowserCompat.MediaItem var1) {
      }

      private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback {
         StubApi23() {
         }

         public void onError(String var1) {
            ItemCallback.this.onError(var1);
         }

         public void onItemLoaded(Parcel var1) {
            if (var1 == null) {
               ItemCallback.this.onItemLoaded((MediaBrowserCompat.MediaItem)null);
            } else {
               var1.setDataPosition(0);
               MediaBrowserCompat.MediaItem var2 = (MediaBrowserCompat.MediaItem)MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(var1);
               var1.recycle();
               ItemCallback.this.onItemLoaded(var2);
            }
         }
      }
   }

   private static class ItemReceiver extends ResultReceiver {
      private final MediaBrowserCompat.ItemCallback mCallback;
      private final String mMediaId;

      ItemReceiver(String var1, MediaBrowserCompat.ItemCallback var2, Handler var3) {
         super(var3);
         this.mMediaId = var1;
         this.mCallback = var2;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         if (var1 == 0 && var2 != null && var2.containsKey("media_item")) {
            Parcelable var3 = var2.getParcelable("media_item");
            if (var3 != null && !(var3 instanceof MediaBrowserCompat.MediaItem)) {
               this.mCallback.onError(this.mMediaId);
            } else {
               this.mCallback.onItemLoaded((MediaBrowserCompat.MediaItem)var3);
            }
         } else {
            this.mCallback.onError(this.mMediaId);
         }
      }
   }

   interface MediaBrowserImpl {
      void connect();

      void disconnect();

      Bundle getExtras();

      void getItem(String var1, MediaBrowserCompat.ItemCallback var2);

      Bundle getNotifyChildrenChangedOptions();

      String getRoot();

      ComponentName getServiceComponent();

      MediaSessionCompat.Token getSessionToken();

      boolean isConnected();

      void search(String var1, Bundle var2, MediaBrowserCompat.SearchCallback var3);

      void sendCustomAction(String var1, Bundle var2, MediaBrowserCompat.CustomActionCallback var3);

      void subscribe(String var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3);

      void unsubscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2);
   }

   static class MediaBrowserImplApi21 implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl, MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal {
      protected final Object mBrowserObj;
      protected Messenger mCallbacksMessenger;
      final Context mContext;
      protected final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
      private MediaSessionCompat.Token mMediaSessionToken;
      private Bundle mNotifyChildrenChangedOptions;
      protected final Bundle mRootHints;
      protected MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
      protected int mServiceVersion;
      private final ArrayMap mSubscriptions = new ArrayMap();

      MediaBrowserImplApi21(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         this.mContext = var1;
         if (var4 != null) {
            var4 = new Bundle(var4);
         } else {
            var4 = new Bundle();
         }

         this.mRootHints = var4;
         var4.putInt("extra_client_version", 1);
         var3.setInternalConnectionCallback(this);
         this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(var1, var2, var3.mConnectionCallbackObj, this.mRootHints);
      }

      public void connect() {
         MediaBrowserCompatApi21.connect(this.mBrowserObj);
      }

      public void disconnect() {
         MediaBrowserCompat.ServiceBinderWrapper var1 = this.mServiceBinderWrapper;
         if (var1 != null) {
            Messenger var2 = this.mCallbacksMessenger;
            if (var2 != null) {
               try {
                  var1.unregisterCallbackMessenger(var2);
               } catch (RemoteException var3) {
                  Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
               }
            }
         }

         MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
      }

      public Bundle getExtras() {
         return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
      }

      public void getItem(final String var1, final MediaBrowserCompat.ItemCallback var2) {
         if (!TextUtils.isEmpty(var1)) {
            if (var2 != null) {
               if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                  Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var2.onError(var1);
                     }
                  });
               } else if (this.mServiceBinderWrapper == null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var2.onError(var1);
                     }
                  });
               } else {
                  MediaBrowserCompat.ItemReceiver var3 = new MediaBrowserCompat.ItemReceiver(var1, var2, this.mHandler);

                  try {
                     this.mServiceBinderWrapper.getMediaItem(var1, var3, this.mCallbacksMessenger);
                  } catch (RemoteException var4) {
                     StringBuilder var5 = new StringBuilder();
                     var5.append("Remote error getting media item: ");
                     var5.append(var1);
                     Log.i("MediaBrowserCompat", var5.toString());
                     this.mHandler.post(new Runnable() {
                        public void run() {
                           var2.onError(var1);
                        }
                     });
                  }
               }
            } else {
               throw new IllegalArgumentException("cb is null");
            }
         } else {
            throw new IllegalArgumentException("mediaId is empty");
         }
      }

      public Bundle getNotifyChildrenChangedOptions() {
         return this.mNotifyChildrenChangedOptions;
      }

      public String getRoot() {
         return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
      }

      public ComponentName getServiceComponent() {
         return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
      }

      public MediaSessionCompat.Token getSessionToken() {
         if (this.mMediaSessionToken == null) {
            this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
         }

         return this.mMediaSessionToken;
      }

      public boolean isConnected() {
         return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
      }

      public void onConnected() {
         Bundle var1 = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
         if (var1 != null) {
            this.mServiceVersion = var1.getInt("extra_service_version", 0);
            IBinder var2 = BundleCompat.getBinder(var1, "extra_messenger");
            if (var2 != null) {
               this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var2, this.mRootHints);
               Messenger var5 = new Messenger(this.mHandler);
               this.mCallbacksMessenger = var5;
               this.mHandler.setCallbacksMessenger(var5);

               try {
                  this.mServiceBinderWrapper.registerCallbackMessenger(this.mContext, this.mCallbacksMessenger);
               } catch (RemoteException var3) {
                  Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
               }
            }

            IMediaSession var4 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(var1, "extra_session_binder"));
            if (var4 != null) {
               this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj), var4);
            }

         }
      }

      public void onConnectionFailed() {
      }

      public void onConnectionFailed(Messenger var1) {
      }

      public void onConnectionSuspended() {
         this.mServiceBinderWrapper = null;
         this.mCallbacksMessenger = null;
         this.mMediaSessionToken = null;
         this.mHandler.setCallbacksMessenger((Messenger)null);
      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4, Bundle var5) {
         if (this.mCallbacksMessenger == var1) {
            MediaBrowserCompat.Subscription var6 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var6 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("onLoadChildren for id that isn't subscribed id=");
                  var8.append(var2);
                  Log.d("MediaBrowserCompat", var8.toString());
               }

            } else {
               MediaBrowserCompat.SubscriptionCallback var7 = var6.getCallback(var4);
               if (var7 != null) {
                  if (var4 == null) {
                     if (var3 == null) {
                        var7.onError(var2);
                        return;
                     }

                     this.mNotifyChildrenChangedOptions = var5;
                     var7.onChildrenLoaded(var2, var3);
                     this.mNotifyChildrenChangedOptions = null;
                     return;
                  }

                  if (var3 == null) {
                     var7.onError(var2, var4);
                     return;
                  }

                  this.mNotifyChildrenChangedOptions = var5;
                  var7.onChildrenLoaded(var2, var3, var4);
                  this.mNotifyChildrenChangedOptions = null;
               }

            }
         }
      }

      public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4) {
      }

      public void search(final String var1, final Bundle var2, final MediaBrowserCompat.SearchCallback var3) {
         if (this.isConnected()) {
            if (this.mServiceBinderWrapper == null) {
               Log.i("MediaBrowserCompat", "The connected service doesn't support search.");
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            } else {
               MediaBrowserCompat.SearchResultReceiver var4 = new MediaBrowserCompat.SearchResultReceiver(var1, var2, var3, this.mHandler);

               try {
                  this.mServiceBinderWrapper.search(var1, var2, var4, this.mCallbacksMessenger);
               } catch (RemoteException var6) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Remote error searching items with query: ");
                  var5.append(var1);
                  Log.i("MediaBrowserCompat", var5.toString(), var6);
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2);
                     }
                  });
               }
            }
         } else {
            throw new IllegalStateException("search() called while not connected");
         }
      }

      public void sendCustomAction(final String var1, final Bundle var2, final MediaBrowserCompat.CustomActionCallback var3) {
         if (!this.isConnected()) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot send a custom action (");
            var7.append(var1);
            var7.append(") with ");
            var7.append("extras ");
            var7.append(var2);
            var7.append(" because the browser is not connected to the ");
            var7.append("service.");
            throw new IllegalStateException(var7.toString());
         } else {
            if (this.mServiceBinderWrapper == null) {
               Log.i("MediaBrowserCompat", "The connected service doesn't support sendCustomAction.");
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }
            }

            MediaBrowserCompat.CustomActionResultReceiver var4 = new MediaBrowserCompat.CustomActionResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.sendCustomAction(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error sending a custom action: action=");
               var5.append(var1);
               var5.append(", extras=");
               var5.append(var2);
               Log.i("MediaBrowserCompat", var5.toString(), var6);
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }

            }
         }
      }

      public void subscribe(String var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3) {
         MediaBrowserCompat.Subscription var5 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         MediaBrowserCompat.Subscription var4 = var5;
         if (var5 == null) {
            var4 = new MediaBrowserCompat.Subscription();
            this.mSubscriptions.put(var1, var4);
         }

         var3.setSubscription(var4);
         if (var2 == null) {
            var2 = null;
         } else {
            var2 = new Bundle(var2);
         }

         var4.putCallback(var2, var3);
         MediaBrowserCompat.ServiceBinderWrapper var7 = this.mServiceBinderWrapper;
         if (var7 == null) {
            MediaBrowserCompatApi21.subscribe(this.mBrowserObj, var1, var3.mSubscriptionCallbackObj);
         } else {
            try {
               var7.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Remote error subscribing media item: ");
               var8.append(var1);
               Log.i("MediaBrowserCompat", var8.toString());
            }
         }
      }

      public void unsubscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         MediaBrowserCompat.Subscription var4 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         if (var4 != null) {
            MediaBrowserCompat.ServiceBinderWrapper var5 = this.mServiceBinderWrapper;
            int var3;
            List var6;
            List var10;
            if (var5 == null) {
               if (var2 == null) {
                  MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
               } else {
                  var10 = var4.getCallbacks();
                  var6 = var4.getOptionsList();

                  for(var3 = var10.size() - 1; var3 >= 0; --var3) {
                     if (var10.get(var3) == var2) {
                        var10.remove(var3);
                        var6.remove(var3);
                     }
                  }

                  if (var10.size() == 0) {
                     MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
                  }
               }
            } else {
               label70: {
                  boolean var10001;
                  if (var2 == null) {
                     try {
                        var5.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                        break label70;
                     } catch (RemoteException var7) {
                        var10001 = false;
                     }
                  } else {
                     label68: {
                        try {
                           var10 = var4.getCallbacks();
                           var6 = var4.getOptionsList();
                           var3 = var10.size() - 1;
                        } catch (RemoteException var9) {
                           var10001 = false;
                           break label68;
                        }

                        while(true) {
                           if (var3 < 0) {
                              break label70;
                           }

                           try {
                              if (var10.get(var3) == var2) {
                                 this.mServiceBinderWrapper.removeSubscription(var1, var2.mToken, this.mCallbacksMessenger);
                                 var10.remove(var3);
                                 var6.remove(var3);
                              }
                           } catch (RemoteException var8) {
                              var10001 = false;
                              break;
                           }

                           --var3;
                        }
                     }
                  }

                  StringBuilder var11 = new StringBuilder();
                  var11.append("removeSubscription failed with RemoteException parentId=");
                  var11.append(var1);
                  Log.d("MediaBrowserCompat", var11.toString());
               }
            }

            if (var4.isEmpty() || var2 == null) {
               this.mSubscriptions.remove(var1);
            }

         }
      }
   }

   static class MediaBrowserImplApi23 extends MediaBrowserCompat.MediaBrowserImplApi21 {
      MediaBrowserImplApi23(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         super(var1, var2, var3, var4);
      }

      public void getItem(String var1, MediaBrowserCompat.ItemCallback var2) {
         if (this.mServiceBinderWrapper == null) {
            MediaBrowserCompatApi23.getItem(this.mBrowserObj, var1, var2.mItemCallbackObj);
         } else {
            super.getItem(var1, var2);
         }
      }
   }

   static class MediaBrowserImplApi26 extends MediaBrowserCompat.MediaBrowserImplApi23 {
      MediaBrowserImplApi26(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         super(var1, var2, var3, var4);
      }

      public void subscribe(String var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3) {
         if (this.mServiceBinderWrapper != null && this.mServiceVersion >= 2) {
            super.subscribe(var1, var2, var3);
         } else if (var2 == null) {
            MediaBrowserCompatApi21.subscribe(this.mBrowserObj, var1, var3.mSubscriptionCallbackObj);
         } else {
            MediaBrowserCompatApi26.subscribe(this.mBrowserObj, var1, var2, var3.mSubscriptionCallbackObj);
         }
      }

      public void unsubscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         if (this.mServiceBinderWrapper != null && this.mServiceVersion >= 2) {
            super.unsubscribe(var1, var2);
         } else if (var2 == null) {
            MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
         } else {
            MediaBrowserCompatApi26.unsubscribe(this.mBrowserObj, var1, var2.mSubscriptionCallbackObj);
         }
      }
   }

   static class MediaBrowserImplBase implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl {
      static final int CONNECT_STATE_CONNECTED = 3;
      static final int CONNECT_STATE_CONNECTING = 2;
      static final int CONNECT_STATE_DISCONNECTED = 1;
      static final int CONNECT_STATE_DISCONNECTING = 0;
      static final int CONNECT_STATE_SUSPENDED = 4;
      final MediaBrowserCompat.ConnectionCallback mCallback;
      Messenger mCallbacksMessenger;
      final Context mContext;
      private Bundle mExtras;
      final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
      private MediaSessionCompat.Token mMediaSessionToken;
      private Bundle mNotifyChildrenChangedOptions;
      final Bundle mRootHints;
      private String mRootId;
      MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
      final ComponentName mServiceComponent;
      MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection mServiceConnection;
      int mState = 1;
      private final ArrayMap mSubscriptions = new ArrayMap();

      public MediaBrowserImplBase(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         if (var1 != null) {
            if (var2 != null) {
               if (var3 != null) {
                  this.mContext = var1;
                  this.mServiceComponent = var2;
                  this.mCallback = var3;
                  Bundle var5;
                  if (var4 == null) {
                     var5 = null;
                  } else {
                     var5 = new Bundle(var4);
                  }

                  this.mRootHints = var5;
               } else {
                  throw new IllegalArgumentException("connection callback must not be null");
               }
            } else {
               throw new IllegalArgumentException("service component must not be null");
            }
         } else {
            throw new IllegalArgumentException("context must not be null");
         }
      }

      private static String getStateLabel(int var0) {
         if (var0 != 0) {
            if (var0 != 1) {
               if (var0 != 2) {
                  if (var0 != 3) {
                     if (var0 != 4) {
                        StringBuilder var1 = new StringBuilder();
                        var1.append("UNKNOWN/");
                        var1.append(var0);
                        return var1.toString();
                     } else {
                        return "CONNECT_STATE_SUSPENDED";
                     }
                  } else {
                     return "CONNECT_STATE_CONNECTED";
                  }
               } else {
                  return "CONNECT_STATE_CONNECTING";
               }
            } else {
               return "CONNECT_STATE_DISCONNECTED";
            }
         } else {
            return "CONNECT_STATE_DISCONNECTING";
         }
      }

      private boolean isCurrent(Messenger var1, String var2) {
         int var3;
         if (this.mCallbacksMessenger == var1) {
            var3 = this.mState;
            if (var3 != 0 && var3 != 1) {
               return true;
            }
         }

         var3 = this.mState;
         if (var3 != 0 && var3 != 1) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var2);
            var4.append(" for ");
            var4.append(this.mServiceComponent);
            var4.append(" with mCallbacksMessenger=");
            var4.append(this.mCallbacksMessenger);
            var4.append(" this=");
            var4.append(this);
            Log.i("MediaBrowserCompat", var4.toString());
         }

         return false;
      }

      public void connect() {
         int var1 = this.mState;
         if (var1 != 0 && var1 != 1) {
            StringBuilder var2 = new StringBuilder();
            var2.append("connect() called while neigther disconnecting nor disconnected (state=");
            var2.append(getStateLabel(this.mState));
            var2.append(")");
            throw new IllegalStateException(var2.toString());
         } else {
            this.mState = 2;
            this.mHandler.post(new Runnable() {
               public void run() {
                  if (MediaBrowserImplBase.this.mState != 0) {
                     MediaBrowserImplBase.this.mState = 2;
                     StringBuilder var3;
                     if (MediaBrowserCompat.DEBUG && MediaBrowserImplBase.this.mServiceConnection != null) {
                        var3 = new StringBuilder();
                        var3.append("mServiceConnection should be null. Instead it is ");
                        var3.append(MediaBrowserImplBase.this.mServiceConnection);
                        throw new RuntimeException(var3.toString());
                     } else if (MediaBrowserImplBase.this.mServiceBinderWrapper != null) {
                        var3 = new StringBuilder();
                        var3.append("mServiceBinderWrapper should be null. Instead it is ");
                        var3.append(MediaBrowserImplBase.this.mServiceBinderWrapper);
                        throw new RuntimeException(var3.toString());
                     } else if (MediaBrowserImplBase.this.mCallbacksMessenger == null) {
                        Intent var6 = new Intent("android.media.browse.MediaBrowserService");
                        var6.setComponent(MediaBrowserImplBase.this.mServiceComponent);
                        MediaBrowserCompat.MediaBrowserImplBase var4 = MediaBrowserImplBase.this;
                        var4.mServiceConnection = var4.new MediaServiceConnection();
                        boolean var1 = false;

                        label34: {
                           boolean var2;
                           try {
                              var2 = MediaBrowserImplBase.this.mContext.bindService(var6, MediaBrowserImplBase.this.mServiceConnection, 1);
                           } catch (Exception var5) {
                              var3 = new StringBuilder();
                              var3.append("Failed binding to service ");
                              var3.append(MediaBrowserImplBase.this.mServiceComponent);
                              Log.e("MediaBrowserCompat", var3.toString());
                              break label34;
                           }

                           var1 = var2;
                        }

                        if (!var1) {
                           MediaBrowserImplBase.this.forceCloseConnection();
                           MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                        }

                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "connect...");
                           MediaBrowserImplBase.this.dump();
                        }

                     } else {
                        var3 = new StringBuilder();
                        var3.append("mCallbacksMessenger should be null. Instead it is ");
                        var3.append(MediaBrowserImplBase.this.mCallbacksMessenger);
                        throw new RuntimeException(var3.toString());
                     }
                  }
               }
            });
         }
      }

      public void disconnect() {
         this.mState = 0;
         this.mHandler.post(new Runnable() {
            public void run() {
               if (MediaBrowserImplBase.this.mCallbacksMessenger != null) {
                  try {
                     MediaBrowserImplBase.this.mServiceBinderWrapper.disconnect(MediaBrowserImplBase.this.mCallbacksMessenger);
                  } catch (RemoteException var3) {
                     StringBuilder var2 = new StringBuilder();
                     var2.append("RemoteException during connect for ");
                     var2.append(MediaBrowserImplBase.this.mServiceComponent);
                     Log.w("MediaBrowserCompat", var2.toString());
                  }
               }

               int var1 = MediaBrowserImplBase.this.mState;
               MediaBrowserImplBase.this.forceCloseConnection();
               if (var1 != 0) {
                  MediaBrowserImplBase.this.mState = var1;
               }

               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "disconnect...");
                  MediaBrowserImplBase.this.dump();
               }

            }
         });
      }

      void dump() {
         Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
         StringBuilder var1 = new StringBuilder();
         var1.append("  mServiceComponent=");
         var1.append(this.mServiceComponent);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mCallback=");
         var1.append(this.mCallback);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mRootHints=");
         var1.append(this.mRootHints);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mState=");
         var1.append(getStateLabel(this.mState));
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mServiceConnection=");
         var1.append(this.mServiceConnection);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mServiceBinderWrapper=");
         var1.append(this.mServiceBinderWrapper);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mCallbacksMessenger=");
         var1.append(this.mCallbacksMessenger);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mRootId=");
         var1.append(this.mRootId);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mMediaSessionToken=");
         var1.append(this.mMediaSessionToken);
         Log.d("MediaBrowserCompat", var1.toString());
      }

      void forceCloseConnection() {
         MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection var1 = this.mServiceConnection;
         if (var1 != null) {
            this.mContext.unbindService(var1);
         }

         this.mState = 1;
         this.mServiceConnection = null;
         this.mServiceBinderWrapper = null;
         this.mCallbacksMessenger = null;
         this.mHandler.setCallbacksMessenger((Messenger)null);
         this.mRootId = null;
         this.mMediaSessionToken = null;
      }

      public Bundle getExtras() {
         if (this.isConnected()) {
            return this.mExtras;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("getExtras() called while not connected (state=");
            var1.append(getStateLabel(this.mState));
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         }
      }

      public void getItem(final String var1, final MediaBrowserCompat.ItemCallback var2) {
         if (!TextUtils.isEmpty(var1)) {
            if (var2 != null) {
               if (!this.isConnected()) {
                  Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var2.onError(var1);
                     }
                  });
               } else {
                  MediaBrowserCompat.ItemReceiver var3 = new MediaBrowserCompat.ItemReceiver(var1, var2, this.mHandler);

                  try {
                     this.mServiceBinderWrapper.getMediaItem(var1, var3, this.mCallbacksMessenger);
                  } catch (RemoteException var4) {
                     StringBuilder var5 = new StringBuilder();
                     var5.append("Remote error getting media item: ");
                     var5.append(var1);
                     Log.i("MediaBrowserCompat", var5.toString());
                     this.mHandler.post(new Runnable() {
                        public void run() {
                           var2.onError(var1);
                        }
                     });
                  }
               }
            } else {
               throw new IllegalArgumentException("cb is null");
            }
         } else {
            throw new IllegalArgumentException("mediaId is empty");
         }
      }

      public Bundle getNotifyChildrenChangedOptions() {
         return this.mNotifyChildrenChangedOptions;
      }

      public String getRoot() {
         if (this.isConnected()) {
            return this.mRootId;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("getRoot() called while not connected(state=");
            var1.append(getStateLabel(this.mState));
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         }
      }

      public ComponentName getServiceComponent() {
         if (this.isConnected()) {
            return this.mServiceComponent;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("getServiceComponent() called while not connected (state=");
            var1.append(this.mState);
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         }
      }

      public MediaSessionCompat.Token getSessionToken() {
         if (this.isConnected()) {
            return this.mMediaSessionToken;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("getSessionToken() called while not connected(state=");
            var1.append(this.mState);
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         }
      }

      public boolean isConnected() {
         return this.mState == 3;
      }

      public void onConnectionFailed(Messenger var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("onConnectFailed for ");
         var2.append(this.mServiceComponent);
         Log.e("MediaBrowserCompat", var2.toString());
         if (this.isCurrent(var1, "onConnectFailed")) {
            if (this.mState != 2) {
               StringBuilder var3 = new StringBuilder();
               var3.append("onConnect from service while mState=");
               var3.append(getStateLabel(this.mState));
               var3.append("... ignoring");
               Log.w("MediaBrowserCompat", var3.toString());
            } else {
               this.forceCloseConnection();
               this.mCallback.onConnectionFailed();
            }
         }
      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4, Bundle var5) {
         if (this.isCurrent(var1, "onLoadChildren")) {
            StringBuilder var6;
            if (MediaBrowserCompat.DEBUG) {
               var6 = new StringBuilder();
               var6.append("onLoadChildren for ");
               var6.append(this.mServiceComponent);
               var6.append(" id=");
               var6.append(var2);
               Log.d("MediaBrowserCompat", var6.toString());
            }

            MediaBrowserCompat.Subscription var7 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var7 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  var6 = new StringBuilder();
                  var6.append("onLoadChildren for id that isn't subscribed id=");
                  var6.append(var2);
                  Log.d("MediaBrowserCompat", var6.toString());
               }

            } else {
               MediaBrowserCompat.SubscriptionCallback var8 = var7.getCallback(var4);
               if (var8 != null) {
                  if (var4 == null) {
                     if (var3 == null) {
                        var8.onError(var2);
                        return;
                     }

                     this.mNotifyChildrenChangedOptions = var5;
                     var8.onChildrenLoaded(var2, var3);
                     this.mNotifyChildrenChangedOptions = null;
                     return;
                  }

                  if (var3 == null) {
                     var8.onError(var2, var4);
                     return;
                  }

                  this.mNotifyChildrenChangedOptions = var5;
                  var8.onChildrenLoaded(var2, var3, var4);
                  this.mNotifyChildrenChangedOptions = null;
               }

            }
         }
      }

      public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4) {
         if (this.isCurrent(var1, "onConnect")) {
            if (this.mState != 2) {
               StringBuilder var10 = new StringBuilder();
               var10.append("onConnect from service while mState=");
               var10.append(getStateLabel(this.mState));
               var10.append("... ignoring");
               Log.w("MediaBrowserCompat", var10.toString());
            } else {
               this.mRootId = var2;
               this.mMediaSessionToken = var3;
               this.mExtras = var4;
               this.mState = 3;
               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                  this.dump();
               }

               this.mCallback.onConnected();

               label47: {
                  Iterator var9;
                  boolean var10001;
                  try {
                     var9 = this.mSubscriptions.entrySet().iterator();
                  } catch (RemoteException var8) {
                     var10001 = false;
                     break label47;
                  }

                  label44:
                  while(true) {
                     List var12;
                     List var14;
                     try {
                        if (!var9.hasNext()) {
                           return;
                        }

                        Entry var11 = (Entry)var9.next();
                        var2 = (String)var11.getKey();
                        MediaBrowserCompat.Subscription var13 = (MediaBrowserCompat.Subscription)var11.getValue();
                        var12 = var13.getCallbacks();
                        var14 = var13.getOptionsList();
                     } catch (RemoteException var7) {
                        var10001 = false;
                        break;
                     }

                     int var5 = 0;

                     while(true) {
                        try {
                           if (var5 >= var12.size()) {
                              break;
                           }

                           this.mServiceBinderWrapper.addSubscription(var2, ((MediaBrowserCompat.SubscriptionCallback)var12.get(var5)).mToken, (Bundle)var14.get(var5), this.mCallbacksMessenger);
                        } catch (RemoteException var6) {
                           var10001 = false;
                           break label44;
                        }

                        ++var5;
                     }
                  }
               }

               Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
            }
         }
      }

      public void search(final String var1, final Bundle var2, final MediaBrowserCompat.SearchCallback var3) {
         if (this.isConnected()) {
            MediaBrowserCompat.SearchResultReceiver var4 = new MediaBrowserCompat.SearchResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.search(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error searching items with query: ");
               var5.append(var1);
               Log.i("MediaBrowserCompat", var5.toString(), var6);
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            }
         } else {
            StringBuilder var7 = new StringBuilder();
            var7.append("search() called while not connected (state=");
            var7.append(getStateLabel(this.mState));
            var7.append(")");
            throw new IllegalStateException(var7.toString());
         }
      }

      public void sendCustomAction(final String var1, final Bundle var2, final MediaBrowserCompat.CustomActionCallback var3) {
         if (this.isConnected()) {
            MediaBrowserCompat.CustomActionResultReceiver var4 = new MediaBrowserCompat.CustomActionResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.sendCustomAction(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error sending a custom action: action=");
               var5.append(var1);
               var5.append(", extras=");
               var5.append(var2);
               Log.i("MediaBrowserCompat", var5.toString(), var6);
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }

            }
         } else {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot send a custom action (");
            var7.append(var1);
            var7.append(") with ");
            var7.append("extras ");
            var7.append(var2);
            var7.append(" because the browser is not connected to the ");
            var7.append("service.");
            throw new IllegalStateException(var7.toString());
         }
      }

      public void subscribe(String var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3) {
         MediaBrowserCompat.Subscription var5 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         MediaBrowserCompat.Subscription var4 = var5;
         if (var5 == null) {
            var4 = new MediaBrowserCompat.Subscription();
            this.mSubscriptions.put(var1, var4);
         }

         if (var2 == null) {
            var2 = null;
         } else {
            var2 = new Bundle(var2);
         }

         var4.putCallback(var2, var3);
         if (this.isConnected()) {
            try {
               this.mServiceBinderWrapper.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
               return;
            } catch (RemoteException var6) {
               StringBuilder var7 = new StringBuilder();
               var7.append("addSubscription failed with RemoteException parentId=");
               var7.append(var1);
               Log.d("MediaBrowserCompat", var7.toString());
            }
         }

      }

      public void unsubscribe(String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         MediaBrowserCompat.Subscription var4 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         if (var4 != null) {
            label60: {
               boolean var10001;
               if (var2 == null) {
                  try {
                     if (this.isConnected()) {
                        this.mServiceBinderWrapper.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                     }
                     break label60;
                  } catch (RemoteException var7) {
                     var10001 = false;
                  }
               } else {
                  label58: {
                     int var3;
                     List var5;
                     List var6;
                     try {
                        var5 = var4.getCallbacks();
                        var6 = var4.getOptionsList();
                        var3 = var5.size() - 1;
                     } catch (RemoteException var10) {
                        var10001 = false;
                        break label58;
                     }

                     while(true) {
                        if (var3 < 0) {
                           break label60;
                        }

                        label52: {
                           try {
                              if (var5.get(var3) != var2) {
                                 break label52;
                              }

                              if (this.isConnected()) {
                                 this.mServiceBinderWrapper.removeSubscription(var1, var2.mToken, this.mCallbacksMessenger);
                              }
                           } catch (RemoteException var9) {
                              var10001 = false;
                              break;
                           }

                           try {
                              var5.remove(var3);
                              var6.remove(var3);
                           } catch (RemoteException var8) {
                              var10001 = false;
                              break;
                           }
                        }

                        --var3;
                     }
                  }
               }

               StringBuilder var11 = new StringBuilder();
               var11.append("removeSubscription failed with RemoteException parentId=");
               var11.append(var1);
               Log.d("MediaBrowserCompat", var11.toString());
            }

            if (var4.isEmpty() || var2 == null) {
               this.mSubscriptions.remove(var1);
            }

         }
      }

      private class MediaServiceConnection implements ServiceConnection {
         MediaServiceConnection() {
         }

         private void postOrRun(Runnable var1) {
            if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
               var1.run();
            } else {
               MediaBrowserImplBase.this.mHandler.post(var1);
            }
         }

         boolean isCurrent(String var1) {
            if (MediaBrowserImplBase.this.mServiceConnection == this && MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
               return true;
            } else {
               if (MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
                  StringBuilder var2 = new StringBuilder();
                  var2.append(var1);
                  var2.append(" for ");
                  var2.append(MediaBrowserImplBase.this.mServiceComponent);
                  var2.append(" with mServiceConnection=");
                  var2.append(MediaBrowserImplBase.this.mServiceConnection);
                  var2.append(" this=");
                  var2.append(this);
                  Log.i("MediaBrowserCompat", var2.toString());
               }

               return false;
            }
         }

         public void onServiceConnected(final ComponentName var1, final IBinder var2) {
            this.postOrRun(new Runnable() {
               public void run() {
                  StringBuilder var1x;
                  if (MediaBrowserCompat.DEBUG) {
                     var1x = new StringBuilder();
                     var1x.append("MediaServiceConnection.onServiceConnected name=");
                     var1x.append(var1);
                     var1x.append(" binder=");
                     var1x.append(var2);
                     Log.d("MediaBrowserCompat", var1x.toString());
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var2, MediaBrowserImplBase.this.mRootHints);
                     MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserImplBase.this.mHandler);
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                     MediaBrowserImplBase.this.mState = 2;

                     try {
                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                           MediaBrowserImplBase.this.dump();
                        }

                        MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                     } catch (RemoteException var2x) {
                        var1x = new StringBuilder();
                        var1x.append("RemoteException during connect for ");
                        var1x.append(MediaBrowserImplBase.this.mServiceComponent);
                        Log.w("MediaBrowserCompat", var1x.toString());
                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                           MediaBrowserImplBase.this.dump();
                        }

                     }
                  }
               }
            });
         }

         public void onServiceDisconnected(final ComponentName var1) {
            this.postOrRun(new Runnable() {
               public void run() {
                  if (MediaBrowserCompat.DEBUG) {
                     StringBuilder var1x = new StringBuilder();
                     var1x.append("MediaServiceConnection.onServiceDisconnected name=");
                     var1x.append(var1);
                     var1x.append(" this=");
                     var1x.append(this);
                     var1x.append(" mServiceConnection=");
                     var1x.append(MediaBrowserImplBase.this.mServiceConnection);
                     Log.d("MediaBrowserCompat", var1x.toString());
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                     MediaBrowserImplBase.this.mCallbacksMessenger = null;
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger((Messenger)null);
                     MediaBrowserImplBase.this.mState = 4;
                     MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                  }
               }
            });
         }
      }
   }

   interface MediaBrowserServiceCallbackImpl {
      void onConnectionFailed(Messenger var1);

      void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4, Bundle var5);

      void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4);
   }

   public static class MediaItem implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaBrowserCompat.MediaItem createFromParcel(Parcel var1) {
            return new MediaBrowserCompat.MediaItem(var1);
         }

         public MediaBrowserCompat.MediaItem[] newArray(int var1) {
            return new MediaBrowserCompat.MediaItem[var1];
         }
      };
      public static final int FLAG_BROWSABLE = 1;
      public static final int FLAG_PLAYABLE = 2;
      private final MediaDescriptionCompat mDescription;
      private final int mFlags;

      MediaItem(Parcel var1) {
         this.mFlags = var1.readInt();
         this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var1);
      }

      public MediaItem(MediaDescriptionCompat var1, int var2) {
         if (var1 != null) {
            if (!TextUtils.isEmpty(var1.getMediaId())) {
               this.mFlags = var2;
               this.mDescription = var1;
            } else {
               throw new IllegalArgumentException("description must have a non-empty media id");
            }
         } else {
            throw new IllegalArgumentException("description cannot be null");
         }
      }

      public static MediaBrowserCompat.MediaItem fromMediaItem(Object var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            int var1 = MediaBrowserCompatApi21.MediaItem.getFlags(var0);
            return new MediaBrowserCompat.MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(var0)), var1);
         } else {
            return null;
         }
      }

      public static List fromMediaItemList(List var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            ArrayList var1 = new ArrayList(var0.size());
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               var1.add(fromMediaItem(var2.next()));
            }

            return var1;
         } else {
            return null;
         }
      }

      public int describeContents() {
         return 0;
      }

      public MediaDescriptionCompat getDescription() {
         return this.mDescription;
      }

      public int getFlags() {
         return this.mFlags;
      }

      public String getMediaId() {
         return this.mDescription.getMediaId();
      }

      public boolean isBrowsable() {
         return (this.mFlags & 1) != 0;
      }

      public boolean isPlayable() {
         return (this.mFlags & 2) != 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("MediaItem{");
         var1.append("mFlags=");
         var1.append(this.mFlags);
         var1.append(", mDescription=");
         var1.append(this.mDescription);
         var1.append('}');
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.mFlags);
         this.mDescription.writeToParcel(var1, var2);
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface Flags {
      }
   }

   public abstract static class SearchCallback {
      public void onError(String var1, Bundle var2) {
      }

      public void onSearchResult(String var1, Bundle var2, List var3) {
      }
   }

   private static class SearchResultReceiver extends ResultReceiver {
      private final MediaBrowserCompat.SearchCallback mCallback;
      private final Bundle mExtras;
      private final String mQuery;

      SearchResultReceiver(String var1, Bundle var2, MediaBrowserCompat.SearchCallback var3, Handler var4) {
         super(var4);
         this.mQuery = var1;
         this.mExtras = var2;
         this.mCallback = var3;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         if (var1 == 0 && var2 != null && var2.containsKey("search_results")) {
            Parcelable[] var5 = var2.getParcelableArray("search_results");
            ArrayList var6 = null;
            if (var5 != null) {
               ArrayList var4 = new ArrayList();
               int var3 = var5.length;
               var1 = 0;

               while(true) {
                  var6 = var4;
                  if (var1 >= var3) {
                     break;
                  }

                  var4.add((MediaBrowserCompat.MediaItem)var5[var1]);
                  ++var1;
               }
            }

            this.mCallback.onSearchResult(this.mQuery, this.mExtras, var6);
         } else {
            this.mCallback.onError(this.mQuery, this.mExtras);
         }
      }
   }

   private static class ServiceBinderWrapper {
      private Messenger mMessenger;
      private Bundle mRootHints;

      public ServiceBinderWrapper(IBinder var1, Bundle var2) {
         this.mMessenger = new Messenger(var1);
         this.mRootHints = var2;
      }

      private void sendRequest(int var1, Bundle var2, Messenger var3) throws RemoteException {
         Message var4 = Message.obtain();
         var4.what = var1;
         var4.arg1 = 1;
         var4.setData(var2);
         var4.replyTo = var3;
         this.mMessenger.send(var4);
      }

      void addSubscription(String var1, IBinder var2, Bundle var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_media_item_id", var1);
         BundleCompat.putBinder(var5, "data_callback_token", var2);
         var5.putBundle("data_options", var3);
         this.sendRequest(3, var5, var4);
      }

      void connect(Context var1, Messenger var2) throws RemoteException {
         Bundle var3 = new Bundle();
         var3.putString("data_package_name", var1.getPackageName());
         var3.putBundle("data_root_hints", this.mRootHints);
         this.sendRequest(1, var3, var2);
      }

      void disconnect(Messenger var1) throws RemoteException {
         this.sendRequest(2, (Bundle)null, var1);
      }

      void getMediaItem(String var1, ResultReceiver var2, Messenger var3) throws RemoteException {
         Bundle var4 = new Bundle();
         var4.putString("data_media_item_id", var1);
         var4.putParcelable("data_result_receiver", var2);
         this.sendRequest(5, var4, var3);
      }

      void registerCallbackMessenger(Context var1, Messenger var2) throws RemoteException {
         Bundle var3 = new Bundle();
         var3.putString("data_package_name", var1.getPackageName());
         var3.putBundle("data_root_hints", this.mRootHints);
         this.sendRequest(6, var3, var2);
      }

      void removeSubscription(String var1, IBinder var2, Messenger var3) throws RemoteException {
         Bundle var4 = new Bundle();
         var4.putString("data_media_item_id", var1);
         BundleCompat.putBinder(var4, "data_callback_token", var2);
         this.sendRequest(4, var4, var3);
      }

      void search(String var1, Bundle var2, ResultReceiver var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_search_query", var1);
         var5.putBundle("data_search_extras", var2);
         var5.putParcelable("data_result_receiver", var3);
         this.sendRequest(8, var5, var4);
      }

      void sendCustomAction(String var1, Bundle var2, ResultReceiver var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_custom_action", var1);
         var5.putBundle("data_custom_action_extras", var2);
         var5.putParcelable("data_result_receiver", var3);
         this.sendRequest(9, var5, var4);
      }

      void unregisterCallbackMessenger(Messenger var1) throws RemoteException {
         this.sendRequest(7, (Bundle)null, var1);
      }
   }

   private static class Subscription {
      private final List mCallbacks = new ArrayList();
      private final List mOptionsList = new ArrayList();

      public Subscription() {
      }

      public MediaBrowserCompat.SubscriptionCallback getCallback(Bundle var1) {
         for(int var2 = 0; var2 < this.mOptionsList.size(); ++var2) {
            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var2), var1)) {
               return (MediaBrowserCompat.SubscriptionCallback)this.mCallbacks.get(var2);
            }
         }

         return null;
      }

      public List getCallbacks() {
         return this.mCallbacks;
      }

      public List getOptionsList() {
         return this.mOptionsList;
      }

      public boolean isEmpty() {
         return this.mCallbacks.isEmpty();
      }

      public void putCallback(Bundle var1, MediaBrowserCompat.SubscriptionCallback var2) {
         for(int var3 = 0; var3 < this.mOptionsList.size(); ++var3) {
            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var3), var1)) {
               this.mCallbacks.set(var3, var2);
               return;
            }
         }

         this.mCallbacks.add(var2);
         this.mOptionsList.add(var1);
      }
   }

   public abstract static class SubscriptionCallback {
      final Object mSubscriptionCallbackObj;
      WeakReference mSubscriptionRef;
      final IBinder mToken = new Binder();

      public SubscriptionCallback() {
         if (VERSION.SDK_INT >= 26) {
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi26.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi26());
         } else if (VERSION.SDK_INT >= 21) {
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi21());
         } else {
            this.mSubscriptionCallbackObj = null;
         }
      }

      public void onChildrenLoaded(String var1, List var2) {
      }

      public void onChildrenLoaded(String var1, List var2, Bundle var3) {
      }

      public void onError(String var1) {
      }

      public void onError(String var1, Bundle var2) {
      }

      void setSubscription(MediaBrowserCompat.Subscription var1) {
         this.mSubscriptionRef = new WeakReference(var1);
      }

      private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback {
         StubApi21() {
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

         public void onChildrenLoaded(String var1, List var2) {
            MediaBrowserCompat.Subscription var4;
            if (SubscriptionCallback.this.mSubscriptionRef == null) {
               var4 = null;
            } else {
               var4 = (MediaBrowserCompat.Subscription)SubscriptionCallback.this.mSubscriptionRef.get();
            }

            if (var4 == null) {
               SubscriptionCallback.this.onChildrenLoaded(var1, MediaBrowserCompat.MediaItem.fromMediaItemList(var2));
            } else {
               var2 = MediaBrowserCompat.MediaItem.fromMediaItemList(var2);
               List var5 = var4.getCallbacks();
               List var7 = var4.getOptionsList();

               for(int var3 = 0; var3 < var5.size(); ++var3) {
                  Bundle var6 = (Bundle)var7.get(var3);
                  if (var6 == null) {
                     SubscriptionCallback.this.onChildrenLoaded(var1, var2);
                  } else {
                     SubscriptionCallback.this.onChildrenLoaded(var1, this.applyOptions(var2, var6), var6);
                  }
               }

            }
         }

         public void onError(String var1) {
            SubscriptionCallback.this.onError(var1);
         }
      }

      private class StubApi26 extends MediaBrowserCompat.SubscriptionCallback.StubApi21 implements MediaBrowserCompatApi26.SubscriptionCallback {
         StubApi26() {
            super();
         }

         public void onChildrenLoaded(String var1, List var2, Bundle var3) {
            SubscriptionCallback.this.onChildrenLoaded(var1, MediaBrowserCompat.MediaItem.fromMediaItemList(var2), var3);
         }

         public void onError(String var1, Bundle var2) {
            SubscriptionCallback.this.onError(var1, var2);
         }
      }
   }
}
