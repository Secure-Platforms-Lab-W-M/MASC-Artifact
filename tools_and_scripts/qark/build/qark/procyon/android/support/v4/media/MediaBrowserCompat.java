// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import java.util.Collections;
import android.os.Binder;
import androidx.media.MediaBrowserCompatUtils;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import android.os.Parcelable$Creator;
import java.util.Iterator;
import java.util.Map;
import android.content.ServiceConnection;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.session.IMediaSession;
import androidx.core.app.BundleCompat;
import android.os.RemoteException;
import androidx.collection.ArrayMap;
import android.os.Parcelable;
import android.os.Parcel;
import android.support.v4.os.ResultReceiver;
import android.os.BadParcelableException;
import java.util.List;
import android.os.Message;
import android.os.Messenger;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.text.TextUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public final class MediaBrowserCompat
{
    public static final String CUSTOM_ACTION_DOWNLOAD = "android.support.v4.media.action.DOWNLOAD";
    public static final String CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE = "android.support.v4.media.action.REMOVE_DOWNLOADED_FILE";
    static final boolean DEBUG;
    public static final String EXTRA_DOWNLOAD_PROGRESS = "android.media.browse.extra.DOWNLOAD_PROGRESS";
    public static final String EXTRA_MEDIA_ID = "android.media.browse.extra.MEDIA_ID";
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;
    
    static {
        DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
    }
    
    public MediaBrowserCompat(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi26(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi23(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi21(context, componentName, connectionCallback, bundle);
            return;
        }
        this.mImpl = (MediaBrowserImpl)new MediaBrowserImplBase(context, componentName, connectionCallback, bundle);
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
    
    public void getItem(final String s, final ItemCallback itemCallback) {
        this.mImpl.getItem(s, itemCallback);
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
    
    public void search(final String s, final Bundle bundle, final SearchCallback searchCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("query cannot be empty");
        }
        if (searchCallback != null) {
            this.mImpl.search(s, bundle, searchCallback);
            return;
        }
        throw new IllegalArgumentException("callback cannot be null");
    }
    
    public void sendCustomAction(final String s, final Bundle bundle, final CustomActionCallback customActionCallback) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            this.mImpl.sendCustomAction(s, bundle, customActionCallback);
            return;
        }
        throw new IllegalArgumentException("action cannot be empty");
    }
    
    public void subscribe(final String s, final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (bundle != null) {
            this.mImpl.subscribe(s, bundle, subscriptionCallback);
            return;
        }
        throw new IllegalArgumentException("options are null");
    }
    
    public void subscribe(final String s, final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback != null) {
            this.mImpl.subscribe(s, null, subscriptionCallback);
            return;
        }
        throw new IllegalArgumentException("callback is null");
    }
    
    public void unsubscribe(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            this.mImpl.unsubscribe(s, null);
            return;
        }
        throw new IllegalArgumentException("parentId is empty");
    }
    
    public void unsubscribe(final String s, final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback != null) {
            this.mImpl.unsubscribe(s, subscriptionCallback);
            return;
        }
        throw new IllegalArgumentException("callback is null");
    }
    
    private static class CallbackHandler extends Handler
    {
        private final WeakReference<MediaBrowserServiceCallbackImpl> mCallbackImplRef;
        private WeakReference<Messenger> mCallbacksMessengerRef;
        
        CallbackHandler(final MediaBrowserServiceCallbackImpl mediaBrowserServiceCallbackImpl) {
            this.mCallbackImplRef = new WeakReference<MediaBrowserServiceCallbackImpl>(mediaBrowserServiceCallbackImpl);
        }
        
        public void handleMessage(final Message message) {
            final WeakReference<Messenger> mCallbacksMessengerRef = this.mCallbacksMessengerRef;
            if (mCallbacksMessengerRef != null && mCallbacksMessengerRef.get() != null) {
                if (this.mCallbackImplRef.get() == null) {
                    return;
                }
                final Bundle data = message.getData();
                MediaSessionCompat.ensureClassLoader(data);
                final MediaBrowserServiceCallbackImpl mediaBrowserServiceCallbackImpl = this.mCallbackImplRef.get();
                final Messenger messenger = this.mCallbacksMessengerRef.get();
                try {
                    final int what = message.what;
                    if (what != 1) {
                        if (what != 2) {
                            if (what != 3) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unhandled message: ");
                                sb.append(message);
                                sb.append("\n  Client version: ");
                                sb.append(1);
                                sb.append("\n  Service version: ");
                                sb.append(message.arg1);
                                Log.w("MediaBrowserCompat", sb.toString());
                            }
                            else {
                                final Bundle bundle = data.getBundle("data_options");
                                MediaSessionCompat.ensureClassLoader(bundle);
                                final Bundle bundle2 = data.getBundle("data_notify_children_changed_options");
                                MediaSessionCompat.ensureClassLoader(bundle2);
                                mediaBrowserServiceCallbackImpl.onLoadChildren(messenger, data.getString("data_media_item_id"), data.getParcelableArrayList("data_media_item_list"), bundle, bundle2);
                            }
                        }
                        else {
                            mediaBrowserServiceCallbackImpl.onConnectionFailed(messenger);
                        }
                    }
                    else {
                        final Bundle bundle3 = data.getBundle("data_root_hints");
                        MediaSessionCompat.ensureClassLoader(bundle3);
                        mediaBrowserServiceCallbackImpl.onServiceConnected(messenger, data.getString("data_media_item_id"), (MediaSessionCompat.Token)data.getParcelable("data_media_session_token"), bundle3);
                    }
                }
                catch (BadParcelableException ex) {
                    Log.e("MediaBrowserCompat", "Could not unparcel the data.");
                    if (message.what == 1) {
                        mediaBrowserServiceCallbackImpl.onConnectionFailed(messenger);
                    }
                }
            }
        }
        
        void setCallbacksMessenger(final Messenger messenger) {
            this.mCallbacksMessengerRef = new WeakReference<Messenger>(messenger);
        }
    }
    
    public static class ConnectionCallback
    {
        ConnectionCallbackInternal mConnectionCallbackInternal;
        final Object mConnectionCallbackObj;
        
        public ConnectionCallback() {
            if (Build$VERSION.SDK_INT >= 21) {
                this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback((MediaBrowserCompatApi21.ConnectionCallback)new StubApi21());
                return;
            }
            this.mConnectionCallbackObj = null;
        }
        
        public void onConnected() {
        }
        
        public void onConnectionFailed() {
        }
        
        public void onConnectionSuspended() {
        }
        
        void setInternalConnectionCallback(final ConnectionCallbackInternal mConnectionCallbackInternal) {
            this.mConnectionCallbackInternal = mConnectionCallbackInternal;
        }
        
        interface ConnectionCallbackInternal
        {
            void onConnected();
            
            void onConnectionFailed();
            
            void onConnectionSuspended();
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback
        {
            StubApi21() {
            }
            
            @Override
            public void onConnected() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnected();
            }
            
            @Override
            public void onConnectionFailed() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
            }
            
            @Override
            public void onConnectionSuspended() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
            }
        }
    }
    
    public abstract static class CustomActionCallback
    {
        public void onError(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onProgressUpdate(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onResult(final String s, final Bundle bundle, final Bundle bundle2) {
        }
    }
    
    private static class CustomActionResultReceiver extends ResultReceiver
    {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;
        
        CustomActionResultReceiver(final String mAction, final Bundle mExtras, final CustomActionCallback mCallback, final Handler handler) {
            super(handler);
            this.mAction = mAction;
            this.mExtras = mExtras;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            if (this.mCallback == null) {
                return;
            }
            MediaSessionCompat.ensureClassLoader(bundle);
            if (n == -1) {
                this.mCallback.onError(this.mAction, this.mExtras, bundle);
                return;
            }
            if (n == 0) {
                this.mCallback.onResult(this.mAction, this.mExtras, bundle);
                return;
            }
            if (n != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown result code: ");
                sb.append(n);
                sb.append(" (extras=");
                sb.append(this.mExtras);
                sb.append(", resultData=");
                sb.append(bundle);
                sb.append(")");
                Log.w("MediaBrowserCompat", sb.toString());
                return;
            }
            this.mCallback.onProgressUpdate(this.mAction, this.mExtras, bundle);
        }
    }
    
    public abstract static class ItemCallback
    {
        final Object mItemCallbackObj;
        
        public ItemCallback() {
            if (Build$VERSION.SDK_INT >= 23) {
                this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback((MediaBrowserCompatApi23.ItemCallback)new StubApi23());
                return;
            }
            this.mItemCallbackObj = null;
        }
        
        public void onError(final String s) {
        }
        
        public void onItemLoaded(final MediaItem mediaItem) {
        }
        
        private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback
        {
            StubApi23() {
            }
            
            @Override
            public void onError(final String s) {
                MediaBrowserCompat.ItemCallback.this.onError(s);
            }
            
            @Override
            public void onItemLoaded(final Parcel parcel) {
                if (parcel == null) {
                    MediaBrowserCompat.ItemCallback.this.onItemLoaded(null);
                    return;
                }
                parcel.setDataPosition(0);
                final MediaItem mediaItem = (MediaItem)MediaItem.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                MediaBrowserCompat.ItemCallback.this.onItemLoaded(mediaItem);
            }
        }
    }
    
    private static class ItemReceiver extends ResultReceiver
    {
        private final ItemCallback mCallback;
        private final String mMediaId;
        
        ItemReceiver(final String mMediaId, final ItemCallback mCallback, final Handler handler) {
            super(handler);
            this.mMediaId = mMediaId;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (n != 0 || bundle == null || !bundle.containsKey("media_item")) {
                this.mCallback.onError(this.mMediaId);
                return;
            }
            final Parcelable parcelable = bundle.getParcelable("media_item");
            if (parcelable != null && !(parcelable instanceof MediaItem)) {
                this.mCallback.onError(this.mMediaId);
                return;
            }
            this.mCallback.onItemLoaded((MediaItem)parcelable);
        }
    }
    
    interface MediaBrowserImpl
    {
        void connect();
        
        void disconnect();
        
        Bundle getExtras();
        
        void getItem(final String p0, final ItemCallback p1);
        
        Bundle getNotifyChildrenChangedOptions();
        
        String getRoot();
        
        ComponentName getServiceComponent();
        
        MediaSessionCompat.Token getSessionToken();
        
        boolean isConnected();
        
        void search(final String p0, final Bundle p1, final SearchCallback p2);
        
        void sendCustomAction(final String p0, final Bundle p1, final CustomActionCallback p2);
        
        void subscribe(final String p0, final Bundle p1, final SubscriptionCallback p2);
        
        void unsubscribe(final String p0, final SubscriptionCallback p1);
    }
    
    static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallbackInternal
    {
        protected final Object mBrowserObj;
        protected Messenger mCallbacksMessenger;
        final Context mContext;
        protected final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        private Bundle mNotifyChildrenChangedOptions;
        protected final Bundle mRootHints;
        protected ServiceBinderWrapper mServiceBinderWrapper;
        protected int mServiceVersion;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        MediaBrowserImplApi21(final Context mContext, final ComponentName componentName, final ConnectionCallback connectionCallback, Bundle mRootHints) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            this.mContext = mContext;
            if (mRootHints != null) {
                mRootHints = new Bundle(mRootHints);
            }
            else {
                mRootHints = new Bundle();
            }
            (this.mRootHints = mRootHints).putInt("extra_client_version", 1);
            connectionCallback.setInternalConnectionCallback((ConnectionCallbackInternal)this);
            this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(mContext, componentName, connectionCallback.mConnectionCallbackObj, this.mRootHints);
        }
        
        @Override
        public void connect() {
            MediaBrowserCompatApi21.connect(this.mBrowserObj);
        }
        
        @Override
        public void disconnect() {
            final ServiceBinderWrapper mServiceBinderWrapper = this.mServiceBinderWrapper;
            if (mServiceBinderWrapper != null) {
                final Messenger mCallbacksMessenger = this.mCallbacksMessenger;
                if (mCallbacksMessenger != null) {
                    try {
                        mServiceBinderWrapper.unregisterCallbackMessenger(mCallbacksMessenger);
                    }
                    catch (RemoteException ex) {
                        Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
                    }
                }
            }
            MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
        }
        
        @Override
        public Bundle getExtras() {
            return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
        }
        
        @Override
        public void getItem(final String s, final ItemCallback itemCallback) {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                if (itemCallback != null) {
                    if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                itemCallback.onError(s);
                            }
                        });
                        return;
                    }
                    if (this.mServiceBinderWrapper == null) {
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                itemCallback.onError(s);
                            }
                        });
                        return;
                    }
                    final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
                    try {
                        this.mServiceBinderWrapper.getMediaItem(s, itemReceiver, this.mCallbacksMessenger);
                        return;
                    }
                    catch (RemoteException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Remote error getting media item: ");
                        sb.append(s);
                        Log.i("MediaBrowserCompat", sb.toString());
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                itemCallback.onError(s);
                            }
                        });
                        return;
                    }
                }
                throw new IllegalArgumentException("cb is null");
            }
            throw new IllegalArgumentException("mediaId is empty");
        }
        
        @Override
        public Bundle getNotifyChildrenChangedOptions() {
            return this.mNotifyChildrenChangedOptions;
        }
        
        @Override
        public String getRoot() {
            return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
        }
        
        @Override
        public ComponentName getServiceComponent() {
            return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
        }
        
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            if (this.mMediaSessionToken == null) {
                this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
            }
            return this.mMediaSessionToken;
        }
        
        @Override
        public boolean isConnected() {
            return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
        }
        
        @Override
        public void onConnected() {
            final Bundle extras = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
            if (extras == null) {
                return;
            }
            this.mServiceVersion = extras.getInt("extra_service_version", 0);
            final IBinder binder = BundleCompat.getBinder(extras, "extra_messenger");
            if (binder != null) {
                this.mServiceBinderWrapper = new ServiceBinderWrapper(binder, this.mRootHints);
                final Messenger messenger = new Messenger((Handler)this.mHandler);
                this.mCallbacksMessenger = messenger;
                this.mHandler.setCallbacksMessenger(messenger);
                try {
                    this.mServiceBinderWrapper.registerCallbackMessenger(this.mContext, this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
                }
            }
            final IMediaSession interface1 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(extras, "extra_session_binder"));
            if (interface1 != null) {
                this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj), interface1);
            }
        }
        
        @Override
        public void onConnectionFailed() {
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
        }
        
        @Override
        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mMediaSessionToken = null;
            this.mHandler.setCallbacksMessenger(null);
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, final Bundle bundle, final Bundle bundle2) {
            if (this.mCallbacksMessenger != messenger) {
                return;
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription == null) {
                if (MediaBrowserCompat.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("onLoadChildren for id that isn't subscribed id=");
                    sb.append(s);
                    Log.d("MediaBrowserCompat", sb.toString());
                }
                return;
            }
            final SubscriptionCallback callback = subscription.getCallback(bundle);
            if (callback != null) {
                if (bundle == null) {
                    if (list == null) {
                        callback.onError(s);
                        return;
                    }
                    this.mNotifyChildrenChangedOptions = bundle2;
                    callback.onChildrenLoaded(s, list);
                    this.mNotifyChildrenChangedOptions = null;
                }
                else {
                    if (list == null) {
                        callback.onError(s, bundle);
                        return;
                    }
                    this.mNotifyChildrenChangedOptions = bundle2;
                    callback.onChildrenLoaded(s, list, bundle);
                    this.mNotifyChildrenChangedOptions = null;
                }
            }
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger, final String s, final MediaSessionCompat.Token token, final Bundle bundle) {
        }
        
        @Override
        public void search(final String s, final Bundle bundle, final SearchCallback searchCallback) {
            if (this.isConnected()) {
                if (this.mServiceBinderWrapper == null) {
                    Log.i("MediaBrowserCompat", "The connected service doesn't support search.");
                    this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            searchCallback.onError(s, bundle);
                        }
                    });
                    return;
                }
                final SearchResultReceiver searchResultReceiver = new SearchResultReceiver(s, bundle, searchCallback, this.mHandler);
                try {
                    this.mServiceBinderWrapper.search(s, bundle, searchResultReceiver, this.mCallbacksMessenger);
                    return;
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Remote error searching items with query: ");
                    sb.append(s);
                    Log.i("MediaBrowserCompat", sb.toString(), (Throwable)ex);
                    this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            searchCallback.onError(s, bundle);
                        }
                    });
                    return;
                }
            }
            throw new IllegalStateException("search() called while not connected");
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle, final CustomActionCallback customActionCallback) {
            if (this.isConnected()) {
                if (this.mServiceBinderWrapper == null) {
                    Log.i("MediaBrowserCompat", "The connected service doesn't support sendCustomAction.");
                    if (customActionCallback != null) {
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                customActionCallback.onError(s, bundle, null);
                            }
                        });
                    }
                }
                final CustomActionResultReceiver customActionResultReceiver = new CustomActionResultReceiver(s, bundle, customActionCallback, this.mHandler);
                try {
                    this.mServiceBinderWrapper.sendCustomAction(s, bundle, customActionResultReceiver, this.mCallbacksMessenger);
                    return;
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Remote error sending a custom action: action=");
                    sb.append(s);
                    sb.append(", extras=");
                    sb.append(bundle);
                    Log.i("MediaBrowserCompat", sb.toString(), (Throwable)ex);
                    if (customActionCallback != null) {
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                customActionCallback.onError(s, bundle, null);
                            }
                        });
                    }
                    return;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot send a custom action (");
            sb2.append(s);
            sb2.append(") with ");
            sb2.append("extras ");
            sb2.append(bundle);
            sb2.append(" because the browser is not connected to the ");
            sb2.append("service.");
            throw new IllegalStateException(sb2.toString());
        }
        
        @Override
        public void subscribe(final String s, final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            subscriptionCallback.setSubscription(subscription);
            Bundle bundle2;
            if (bundle == null) {
                bundle2 = null;
            }
            else {
                bundle2 = new Bundle(bundle);
            }
            subscription.putCallback(bundle2, subscriptionCallback);
            final ServiceBinderWrapper mServiceBinderWrapper = this.mServiceBinderWrapper;
            if (mServiceBinderWrapper == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            try {
                mServiceBinderWrapper.addSubscription(s, subscriptionCallback.mToken, bundle2, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Remote error subscribing media item: ");
                sb.append(s);
                Log.i("MediaBrowserCompat", sb.toString());
            }
        }
        
        @Override
        public void unsubscribe(final String s, final SubscriptionCallback subscriptionCallback) {
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription == null) {
                return;
            }
            final ServiceBinderWrapper mServiceBinderWrapper = this.mServiceBinderWrapper;
            Label_0271: {
                if (mServiceBinderWrapper == null) {
                    if (subscriptionCallback == null) {
                        MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                    }
                    else {
                        final List<SubscriptionCallback> callbacks = subscription.getCallbacks();
                        final List<Bundle> optionsList = subscription.getOptionsList();
                        for (int i = callbacks.size() - 1; i >= 0; --i) {
                            if (callbacks.get(i) == subscriptionCallback) {
                                callbacks.remove(i);
                                optionsList.remove(i);
                            }
                        }
                        if (callbacks.size() == 0) {
                            MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                        }
                    }
                }
                else {
                    Label_0149: {
                        if (subscriptionCallback != null) {
                            break Label_0149;
                        }
                        try {
                            mServiceBinderWrapper.removeSubscription(s, null, this.mCallbacksMessenger);
                            Label_0230: {
                                break Label_0271;
                            }
                            final List<SubscriptionCallback> callbacks2 = subscription.getCallbacks();
                            final List<Bundle> optionsList2 = subscription.getOptionsList();
                            int n = callbacks2.size() - 1;
                            while (true) {
                                this.mServiceBinderWrapper.removeSubscription(s, subscriptionCallback.mToken, this.mCallbacksMessenger);
                                callbacks2.remove(n);
                                optionsList2.remove(n);
                                Label_0223:
                                --n;
                                continue;
                            }
                        }
                        // iftrue(Label_0230:, n < 0)
                        // iftrue(Label_0223:, callbacks2.get(n) != subscriptionCallback)
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("removeSubscription failed with RemoteException parentId=");
                            sb.append(s);
                            Log.d("MediaBrowserCompat", sb.toString());
                        }
                    }
                }
            }
            if (subscription.isEmpty() || subscriptionCallback == null) {
                this.mSubscriptions.remove(s);
            }
        }
    }
    
    static class MediaBrowserImplApi23 extends MediaBrowserImplApi21
    {
        MediaBrowserImplApi23(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }
        
        @Override
        public void getItem(final String s, final ItemCallback itemCallback) {
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi23.getItem(this.mBrowserObj, s, itemCallback.mItemCallbackObj);
                return;
            }
            super.getItem(s, itemCallback);
        }
    }
    
    static class MediaBrowserImplApi26 extends MediaBrowserImplApi23
    {
        MediaBrowserImplApi26(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }
        
        @Override
        public void subscribe(final String s, final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
            if (this.mServiceBinderWrapper != null && this.mServiceVersion >= 2) {
                super.subscribe(s, bundle, subscriptionCallback);
                return;
            }
            if (bundle == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            MediaBrowserCompatApi26.subscribe(this.mBrowserObj, s, bundle, subscriptionCallback.mSubscriptionCallbackObj);
        }
        
        @Override
        public void unsubscribe(final String s, final SubscriptionCallback subscriptionCallback) {
            if (this.mServiceBinderWrapper != null && this.mServiceVersion >= 2) {
                super.unsubscribe(s, subscriptionCallback);
                return;
            }
            if (subscriptionCallback == null) {
                MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                return;
            }
            MediaBrowserCompatApi26.unsubscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
        }
    }
    
    static class MediaBrowserImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl
    {
        static final int CONNECT_STATE_CONNECTED = 3;
        static final int CONNECT_STATE_CONNECTING = 2;
        static final int CONNECT_STATE_DISCONNECTED = 1;
        static final int CONNECT_STATE_DISCONNECTING = 0;
        static final int CONNECT_STATE_SUSPENDED = 4;
        final ConnectionCallback mCallback;
        Messenger mCallbacksMessenger;
        final Context mContext;
        private Bundle mExtras;
        final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        private Bundle mNotifyChildrenChangedOptions;
        final Bundle mRootHints;
        private String mRootId;
        ServiceBinderWrapper mServiceBinderWrapper;
        final ComponentName mServiceComponent;
        MediaServiceConnection mServiceConnection;
        int mState;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        public MediaBrowserImplBase(final Context mContext, final ComponentName mServiceComponent, final ConnectionCallback mCallback, final Bundle bundle) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            this.mState = 1;
            if (mContext == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (mServiceComponent == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (mCallback != null) {
                this.mContext = mContext;
                this.mServiceComponent = mServiceComponent;
                this.mCallback = mCallback;
                Bundle mRootHints;
                if (bundle == null) {
                    mRootHints = null;
                }
                else {
                    mRootHints = new Bundle(bundle);
                }
                this.mRootHints = mRootHints;
                return;
            }
            throw new IllegalArgumentException("connection callback must not be null");
        }
        
        private static String getStateLabel(final int n) {
            if (n == 0) {
                return "CONNECT_STATE_DISCONNECTING";
            }
            if (n == 1) {
                return "CONNECT_STATE_DISCONNECTED";
            }
            if (n == 2) {
                return "CONNECT_STATE_CONNECTING";
            }
            if (n == 3) {
                return "CONNECT_STATE_CONNECTED";
            }
            if (n != 4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("UNKNOWN/");
                sb.append(n);
                return sb.toString();
            }
            return "CONNECT_STATE_SUSPENDED";
        }
        
        private boolean isCurrent(final Messenger messenger, final String s) {
            if (this.mCallbacksMessenger == messenger) {
                final int mState = this.mState;
                if (mState != 0) {
                    if (mState != 1) {
                        return true;
                    }
                }
            }
            final int mState2 = this.mState;
            if (mState2 != 0 && mState2 != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(" for ");
                sb.append(this.mServiceComponent);
                sb.append(" with mCallbacksMessenger=");
                sb.append(this.mCallbacksMessenger);
                sb.append(" this=");
                sb.append(this);
                Log.i("MediaBrowserCompat", sb.toString());
            }
            return false;
        }
        
        @Override
        public void connect() {
            final int mState = this.mState;
            if (mState != 0 && mState != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("connect() called while neigther disconnecting nor disconnected (state=");
                sb.append(getStateLabel(this.mState));
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            this.mState = 2;
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (MediaBrowserImplBase.this.mState == 0) {
                        return;
                    }
                    MediaBrowserImplBase.this.mState = 2;
                    if (MediaBrowserCompat.DEBUG && MediaBrowserImplBase.this.mServiceConnection != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("mServiceConnection should be null. Instead it is ");
                        sb.append(MediaBrowserImplBase.this.mServiceConnection);
                        throw new RuntimeException(sb.toString());
                    }
                    if (MediaBrowserImplBase.this.mServiceBinderWrapper != null) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("mServiceBinderWrapper should be null. Instead it is ");
                        sb2.append(MediaBrowserImplBase.this.mServiceBinderWrapper);
                        throw new RuntimeException(sb2.toString());
                    }
                    if (MediaBrowserImplBase.this.mCallbacksMessenger == null) {
                        final Intent intent = new Intent("android.media.browse.MediaBrowserService");
                        intent.setComponent(MediaBrowserImplBase.this.mServiceComponent);
                        final MediaBrowserImplBase this$0 = MediaBrowserImplBase.this;
                        this$0.mServiceConnection = this$0.new MediaServiceConnection();
                        int bindService = 0;
                        try {
                            bindService = (MediaBrowserImplBase.this.mContext.bindService(intent, (ServiceConnection)MediaBrowserImplBase.this.mServiceConnection, 1) ? 1 : 0);
                        }
                        catch (Exception ex) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Failed binding to service ");
                            sb3.append(MediaBrowserImplBase.this.mServiceComponent);
                            Log.e("MediaBrowserCompat", sb3.toString());
                        }
                        if (bindService == 0) {
                            MediaBrowserImplBase.this.forceCloseConnection();
                            MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                        }
                        if (MediaBrowserCompat.DEBUG) {
                            Log.d("MediaBrowserCompat", "connect...");
                            MediaBrowserImplBase.this.dump();
                        }
                        return;
                    }
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("mCallbacksMessenger should be null. Instead it is ");
                    sb4.append(MediaBrowserImplBase.this.mCallbacksMessenger);
                    throw new RuntimeException(sb4.toString());
                }
            });
        }
        
        @Override
        public void disconnect() {
            this.mState = 0;
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (MediaBrowserImplBase.this.mCallbacksMessenger != null) {
                        try {
                            MediaBrowserImplBase.this.mServiceBinderWrapper.disconnect(MediaBrowserImplBase.this.mCallbacksMessenger);
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("RemoteException during connect for ");
                            sb.append(MediaBrowserImplBase.this.mServiceComponent);
                            Log.w("MediaBrowserCompat", sb.toString());
                        }
                    }
                    final int mState = MediaBrowserImplBase.this.mState;
                    MediaBrowserImplBase.this.forceCloseConnection();
                    if (mState != 0) {
                        MediaBrowserImplBase.this.mState = mState;
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
            final StringBuilder sb = new StringBuilder();
            sb.append("  mServiceComponent=");
            sb.append(this.mServiceComponent);
            Log.d("MediaBrowserCompat", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  mCallback=");
            sb2.append(this.mCallback);
            Log.d("MediaBrowserCompat", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  mRootHints=");
            sb3.append(this.mRootHints);
            Log.d("MediaBrowserCompat", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  mState=");
            sb4.append(getStateLabel(this.mState));
            Log.d("MediaBrowserCompat", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  mServiceConnection=");
            sb5.append(this.mServiceConnection);
            Log.d("MediaBrowserCompat", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("  mServiceBinderWrapper=");
            sb6.append(this.mServiceBinderWrapper);
            Log.d("MediaBrowserCompat", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("  mCallbacksMessenger=");
            sb7.append(this.mCallbacksMessenger);
            Log.d("MediaBrowserCompat", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("  mRootId=");
            sb8.append(this.mRootId);
            Log.d("MediaBrowserCompat", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("  mMediaSessionToken=");
            sb9.append(this.mMediaSessionToken);
            Log.d("MediaBrowserCompat", sb9.toString());
        }
        
        void forceCloseConnection() {
            final MediaServiceConnection mServiceConnection = this.mServiceConnection;
            if (mServiceConnection != null) {
                this.mContext.unbindService((ServiceConnection)mServiceConnection);
            }
            this.mState = 1;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }
        
        @Override
        public Bundle getExtras() {
            if (this.isConnected()) {
                return this.mExtras;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("getExtras() called while not connected (state=");
            sb.append(getStateLabel(this.mState));
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
        
        @Override
        public void getItem(final String s, final ItemCallback itemCallback) {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                if (itemCallback != null) {
                    if (!this.isConnected()) {
                        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                itemCallback.onError(s);
                            }
                        });
                        return;
                    }
                    final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
                    try {
                        this.mServiceBinderWrapper.getMediaItem(s, itemReceiver, this.mCallbacksMessenger);
                        return;
                    }
                    catch (RemoteException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Remote error getting media item: ");
                        sb.append(s);
                        Log.i("MediaBrowserCompat", sb.toString());
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                itemCallback.onError(s);
                            }
                        });
                        return;
                    }
                }
                throw new IllegalArgumentException("cb is null");
            }
            throw new IllegalArgumentException("mediaId is empty");
        }
        
        @Override
        public Bundle getNotifyChildrenChangedOptions() {
            return this.mNotifyChildrenChangedOptions;
        }
        
        @Override
        public String getRoot() {
            if (this.isConnected()) {
                return this.mRootId;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("getRoot() called while not connected(state=");
            sb.append(getStateLabel(this.mState));
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
        
        @Override
        public ComponentName getServiceComponent() {
            if (this.isConnected()) {
                return this.mServiceComponent;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("getServiceComponent() called while not connected (state=");
            sb.append(this.mState);
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
        
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            if (this.isConnected()) {
                return this.mMediaSessionToken;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("getSessionToken() called while not connected(state=");
            sb.append(this.mState);
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
        
        @Override
        public boolean isConnected() {
            return this.mState == 3;
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onConnectFailed for ");
            sb.append(this.mServiceComponent);
            Log.e("MediaBrowserCompat", sb.toString());
            if (!this.isCurrent(messenger, "onConnectFailed")) {
                return;
            }
            if (this.mState != 2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("onConnect from service while mState=");
                sb2.append(getStateLabel(this.mState));
                sb2.append("... ignoring");
                Log.w("MediaBrowserCompat", sb2.toString());
                return;
            }
            this.forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, final Bundle bundle, final Bundle bundle2) {
            if (!this.isCurrent(messenger, "onLoadChildren")) {
                return;
            }
            if (MediaBrowserCompat.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onLoadChildren for ");
                sb.append(this.mServiceComponent);
                sb.append(" id=");
                sb.append(s);
                Log.d("MediaBrowserCompat", sb.toString());
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription == null) {
                if (MediaBrowserCompat.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("onLoadChildren for id that isn't subscribed id=");
                    sb2.append(s);
                    Log.d("MediaBrowserCompat", sb2.toString());
                }
                return;
            }
            final SubscriptionCallback callback = subscription.getCallback(bundle);
            if (callback != null) {
                if (bundle == null) {
                    if (list == null) {
                        callback.onError(s);
                        return;
                    }
                    this.mNotifyChildrenChangedOptions = bundle2;
                    callback.onChildrenLoaded(s, list);
                    this.mNotifyChildrenChangedOptions = null;
                }
                else {
                    if (list == null) {
                        callback.onError(s, bundle);
                        return;
                    }
                    this.mNotifyChildrenChangedOptions = bundle2;
                    callback.onChildrenLoaded(s, list, bundle);
                    this.mNotifyChildrenChangedOptions = null;
                }
            }
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger, String mRootId, final MediaSessionCompat.Token mMediaSessionToken, final Bundle mExtras) {
            if (!this.isCurrent(messenger, "onConnect")) {
                return;
            }
            if (this.mState != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onConnect from service while mState=");
                sb.append(getStateLabel(this.mState));
                sb.append("... ignoring");
                Log.w("MediaBrowserCompat", sb.toString());
                return;
            }
            this.mRootId = mRootId;
            this.mMediaSessionToken = mMediaSessionToken;
            this.mExtras = mExtras;
            this.mState = 3;
            if (MediaBrowserCompat.DEBUG) {
                Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                this.dump();
            }
            this.mCallback.onConnected();
            try {
                for (final Map.Entry<String, Subscription> entry : this.mSubscriptions.entrySet()) {
                    mRootId = entry.getKey();
                    final Subscription subscription = entry.getValue();
                    final List<SubscriptionCallback> callbacks = subscription.getCallbacks();
                    final List<Bundle> optionsList = subscription.getOptionsList();
                    for (int i = 0; i < callbacks.size(); ++i) {
                        this.mServiceBinderWrapper.addSubscription(mRootId, callbacks.get(i).mToken, optionsList.get(i), this.mCallbacksMessenger);
                    }
                }
            }
            catch (RemoteException ex) {
                Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
            }
        }
        
        @Override
        public void search(final String s, final Bundle bundle, final SearchCallback searchCallback) {
            if (this.isConnected()) {
                final SearchResultReceiver searchResultReceiver = new SearchResultReceiver(s, bundle, searchCallback, this.mHandler);
                try {
                    this.mServiceBinderWrapper.search(s, bundle, searchResultReceiver, this.mCallbacksMessenger);
                    return;
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Remote error searching items with query: ");
                    sb.append(s);
                    Log.i("MediaBrowserCompat", sb.toString(), (Throwable)ex);
                    this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            searchCallback.onError(s, bundle);
                        }
                    });
                    return;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("search() called while not connected (state=");
            sb2.append(getStateLabel(this.mState));
            sb2.append(")");
            throw new IllegalStateException(sb2.toString());
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle, final CustomActionCallback customActionCallback) {
            if (this.isConnected()) {
                final CustomActionResultReceiver customActionResultReceiver = new CustomActionResultReceiver(s, bundle, customActionCallback, this.mHandler);
                try {
                    this.mServiceBinderWrapper.sendCustomAction(s, bundle, customActionResultReceiver, this.mCallbacksMessenger);
                    return;
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Remote error sending a custom action: action=");
                    sb.append(s);
                    sb.append(", extras=");
                    sb.append(bundle);
                    Log.i("MediaBrowserCompat", sb.toString(), (Throwable)ex);
                    if (customActionCallback != null) {
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                customActionCallback.onError(s, bundle, null);
                            }
                        });
                    }
                    return;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot send a custom action (");
            sb2.append(s);
            sb2.append(") with ");
            sb2.append("extras ");
            sb2.append(bundle);
            sb2.append(" because the browser is not connected to the ");
            sb2.append("service.");
            throw new IllegalStateException(sb2.toString());
        }
        
        @Override
        public void subscribe(final String s, final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            Bundle bundle2;
            if (bundle == null) {
                bundle2 = null;
            }
            else {
                bundle2 = new Bundle(bundle);
            }
            subscription.putCallback(bundle2, subscriptionCallback);
            if (this.isConnected()) {
                try {
                    this.mServiceBinderWrapper.addSubscription(s, subscriptionCallback.mToken, bundle2, this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("addSubscription failed with RemoteException parentId=");
                    sb.append(s);
                    Log.d("MediaBrowserCompat", sb.toString());
                }
            }
        }
        
        @Override
        public void unsubscribe(final String s, final SubscriptionCallback subscriptionCallback) {
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription == null) {
                return;
            }
        Label_0109_Outer:
            while (true) {
                if (subscriptionCallback == null) {
                    Label_0175: {
                        try {
                            if (this.isConnected()) {
                                this.mServiceBinderWrapper.removeSubscription(s, null, this.mCallbacksMessenger);
                            }
                            Label_0134: {
                                break Label_0175;
                            }
                            // iftrue(Label_0109:, !this.isConnected())
                            // iftrue(Label_0134:, n < 0)
                            // iftrue(Label_0127:, callbacks.get(n) != subscriptionCallback)
                            while (true) {
                                int n;
                                List<SubscriptionCallback> callbacks;
                                List<Bundle> optionsList;
                                Block_8:Label_0070_Outer:Label_0127_Outer:
                                while (true) {
                                    break Block_8;
                                    Block_6: {
                                        while (true) {
                                            while (true) {
                                                break Block_6;
                                                --n;
                                                continue Label_0127_Outer;
                                                callbacks = subscription.getCallbacks();
                                                optionsList = subscription.getOptionsList();
                                                n = callbacks.size() - 1;
                                                continue Label_0127_Outer;
                                            }
                                            callbacks.remove(n);
                                            optionsList.remove(n);
                                            continue Label_0109_Outer;
                                        }
                                    }
                                    continue Label_0070_Outer;
                                }
                                this.mServiceBinderWrapper.removeSubscription(s, subscriptionCallback.mToken, this.mCallbacksMessenger);
                                continue;
                            }
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("removeSubscription failed with RemoteException parentId=");
                            sb.append(s);
                            Log.d("MediaBrowserCompat", sb.toString());
                        }
                    }
                    if (subscription.isEmpty() || subscriptionCallback == null) {
                        this.mSubscriptions.remove(s);
                    }
                    return;
                }
                continue;
            }
        }
        
        private class MediaServiceConnection implements ServiceConnection
        {
            MediaServiceConnection() {
            }
            
            private void postOrRun(final Runnable runnable) {
                if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
                    runnable.run();
                    return;
                }
                MediaBrowserImplBase.this.mHandler.post(runnable);
            }
            
            boolean isCurrent(final String s) {
                if (MediaBrowserImplBase.this.mServiceConnection == this && MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
                    return true;
                }
                if (MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(" for ");
                    sb.append(MediaBrowserImplBase.this.mServiceComponent);
                    sb.append(" with mServiceConnection=");
                    sb.append(MediaBrowserImplBase.this.mServiceConnection);
                    sb.append(" this=");
                    sb.append(this);
                    Log.i("MediaBrowserCompat", sb.toString());
                }
                return false;
            }
            
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("MediaServiceConnection.onServiceConnected name=");
                            sb.append(componentName);
                            sb.append(" binder=");
                            sb.append(binder);
                            Log.d("MediaBrowserCompat", sb.toString());
                            MediaBrowserImplBase.this.dump();
                        }
                        if (!MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                            return;
                        }
                        MediaBrowserImplBase.this.mServiceBinderWrapper = new ServiceBinderWrapper(binder, MediaBrowserImplBase.this.mRootHints);
                        MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger((Handler)MediaBrowserImplBase.this.mHandler);
                        MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                        MediaBrowserImplBase.this.mState = 2;
                        try {
                            if (MediaBrowserCompat.DEBUG) {
                                Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                MediaBrowserImplBase.this.dump();
                            }
                            MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("RemoteException during connect for ");
                            sb2.append(MediaBrowserImplBase.this.mServiceComponent);
                            Log.w("MediaBrowserCompat", sb2.toString());
                            if (MediaBrowserCompat.DEBUG) {
                                Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                MediaBrowserImplBase.this.dump();
                            }
                        }
                    }
                });
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("MediaServiceConnection.onServiceDisconnected name=");
                            sb.append(componentName);
                            sb.append(" this=");
                            sb.append(this);
                            sb.append(" mServiceConnection=");
                            sb.append(MediaBrowserImplBase.this.mServiceConnection);
                            Log.d("MediaBrowserCompat", sb.toString());
                            MediaBrowserImplBase.this.dump();
                        }
                        if (!MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                            return;
                        }
                        MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                        MediaBrowserImplBase.this.mCallbacksMessenger = null;
                        MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(null);
                        MediaBrowserImplBase.this.mState = 4;
                        MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                    }
                });
            }
        }
    }
    
    interface MediaBrowserServiceCallbackImpl
    {
        void onConnectionFailed(final Messenger p0);
        
        void onLoadChildren(final Messenger p0, final String p1, final List p2, final Bundle p3, final Bundle p4);
        
        void onServiceConnected(final Messenger p0, final String p1, final MediaSessionCompat.Token p2, final Bundle p3);
    }
    
    public static class MediaItem implements Parcelable
    {
        public static final Parcelable$Creator<MediaItem> CREATOR;
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaItem>() {
                public MediaItem createFromParcel(final Parcel parcel) {
                    return new MediaItem(parcel);
                }
                
                public MediaItem[] newArray(final int n) {
                    return new MediaItem[n];
                }
            };
        }
        
        MediaItem(final Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }
        
        public MediaItem(final MediaDescriptionCompat mDescription, final int mFlags) {
            if (mDescription == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (!TextUtils.isEmpty((CharSequence)mDescription.getMediaId())) {
                this.mFlags = mFlags;
                this.mDescription = mDescription;
                return;
            }
            throw new IllegalArgumentException("description must have a non-empty media id");
        }
        
        public static MediaItem fromMediaItem(final Object o) {
            if (o != null && Build$VERSION.SDK_INT >= 21) {
                return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(o)), MediaBrowserCompatApi21.MediaItem.getFlags(o));
            }
            return null;
        }
        
        public static List<MediaItem> fromMediaItemList(final List<?> list) {
            if (list != null && Build$VERSION.SDK_INT >= 21) {
                final ArrayList<MediaItem> list2 = new ArrayList<MediaItem>(list.size());
                final Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    list2.add(fromMediaItem(iterator.next()));
                }
                return list2;
            }
            return null;
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
            return (this.mFlags & 0x1) != 0x0;
        }
        
        public boolean isPlayable() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MediaItem{");
            sb.append("mFlags=");
            sb.append(this.mFlags);
            sb.append(", mDescription=");
            sb.append(this.mDescription);
            sb.append('}');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n);
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface Flags {
        }
    }
    
    public abstract static class SearchCallback
    {
        public void onError(final String s, final Bundle bundle) {
        }
        
        public void onSearchResult(final String s, final Bundle bundle, final List<MediaItem> list) {
        }
    }
    
    private static class SearchResultReceiver extends ResultReceiver
    {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;
        
        SearchResultReceiver(final String mQuery, final Bundle mExtras, final SearchCallback mCallback, final Handler handler) {
            super(handler);
            this.mQuery = mQuery;
            this.mExtras = mExtras;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(int n, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (n == 0 && bundle != null && bundle.containsKey("search_results")) {
                final Parcelable[] parcelableArray = bundle.getParcelableArray("search_results");
                List<MediaItem> list = null;
                if (parcelableArray != null) {
                    final ArrayList<MediaItem> list2 = new ArrayList<MediaItem>();
                    final int length = parcelableArray.length;
                    n = 0;
                    while (true) {
                        list = list2;
                        if (n >= length) {
                            break;
                        }
                        list2.add((MediaItem)parcelableArray[n]);
                        ++n;
                    }
                }
                this.mCallback.onSearchResult(this.mQuery, this.mExtras, list);
                return;
            }
            this.mCallback.onError(this.mQuery, this.mExtras);
        }
    }
    
    private static class ServiceBinderWrapper
    {
        private Messenger mMessenger;
        private Bundle mRootHints;
        
        public ServiceBinderWrapper(final IBinder binder, final Bundle mRootHints) {
            this.mMessenger = new Messenger(binder);
            this.mRootHints = mRootHints;
        }
        
        private void sendRequest(final int what, final Bundle data, final Messenger replyTo) throws RemoteException {
            final Message obtain = Message.obtain();
            obtain.what = what;
            obtain.arg1 = 1;
            obtain.setData(data);
            obtain.replyTo = replyTo;
            this.mMessenger.send(obtain);
        }
        
        void addSubscription(final String s, final IBinder binder, final Bundle bundle, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", s);
            BundleCompat.putBinder(bundle2, "data_callback_token", binder);
            bundle2.putBundle("data_options", bundle);
            this.sendRequest(3, bundle2, messenger);
        }
        
        void connect(final Context context, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(1, bundle, messenger);
        }
        
        void disconnect(final Messenger messenger) throws RemoteException {
            this.sendRequest(2, null, messenger);
        }
        
        void getMediaItem(final String s, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            bundle.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(5, bundle, messenger);
        }
        
        void registerCallbackMessenger(final Context context, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(6, bundle, messenger);
        }
        
        void removeSubscription(final String s, final IBinder binder, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            BundleCompat.putBinder(bundle, "data_callback_token", binder);
            this.sendRequest(4, bundle, messenger);
        }
        
        void search(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_search_query", s);
            bundle2.putBundle("data_search_extras", bundle);
            bundle2.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(8, bundle2, messenger);
        }
        
        void sendCustomAction(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_custom_action", s);
            bundle2.putBundle("data_custom_action_extras", bundle);
            bundle2.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(9, bundle2, messenger);
        }
        
        void unregisterCallbackMessenger(final Messenger messenger) throws RemoteException {
            this.sendRequest(7, null, messenger);
        }
    }
    
    private static class Subscription
    {
        private final List<SubscriptionCallback> mCallbacks;
        private final List<Bundle> mOptionsList;
        
        public Subscription() {
            this.mCallbacks = new ArrayList<SubscriptionCallback>();
            this.mOptionsList = new ArrayList<Bundle>();
        }
        
        public SubscriptionCallback getCallback(final Bundle bundle) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    return this.mCallbacks.get(i);
                }
            }
            return null;
        }
        
        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }
        
        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }
        
        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }
        
        public void putCallback(final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    this.mCallbacks.set(i, subscriptionCallback);
                    return;
                }
            }
            this.mCallbacks.add(subscriptionCallback);
            this.mOptionsList.add(bundle);
        }
    }
    
    public abstract static class SubscriptionCallback
    {
        final Object mSubscriptionCallbackObj;
        WeakReference<Subscription> mSubscriptionRef;
        final IBinder mToken;
        
        public SubscriptionCallback() {
            this.mToken = (IBinder)new Binder();
            if (Build$VERSION.SDK_INT >= 26) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi26.createSubscriptionCallback((MediaBrowserCompatApi26.SubscriptionCallback)new StubApi26());
                return;
            }
            if (Build$VERSION.SDK_INT >= 21) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback((MediaBrowserCompatApi21.SubscriptionCallback)new StubApi21());
                return;
            }
            this.mSubscriptionCallbackObj = null;
        }
        
        public void onChildrenLoaded(final String s, final List<MediaItem> list) {
        }
        
        public void onChildrenLoaded(final String s, final List<MediaItem> list, final Bundle bundle) {
        }
        
        public void onError(final String s) {
        }
        
        public void onError(final String s, final Bundle bundle) {
        }
        
        void setSubscription(final Subscription subscription) {
            this.mSubscriptionRef = new WeakReference<Subscription>(subscription);
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback
        {
            StubApi21() {
            }
            
            List<MediaBrowserCompat.MediaItem> applyOptions(final List<MediaBrowserCompat.MediaItem> list, final Bundle bundle) {
                if (list == null) {
                    return null;
                }
                final int int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
                final int int2 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
                if (int1 == -1 && int2 == -1) {
                    return list;
                }
                final int n = int2 * int1;
                final int n2 = n + int2;
                if (int1 >= 0 && int2 >= 1 && n < list.size()) {
                    int size;
                    if ((size = n2) > list.size()) {
                        size = list.size();
                    }
                    return list.subList(n, size);
                }
                return Collections.emptyList();
            }
            
            @Override
            public void onChildrenLoaded(final String s, final List<?> list) {
                Object o;
                if (MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef == null) {
                    o = null;
                }
                else {
                    o = MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef.get();
                }
                if (o == null) {
                    MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, MediaBrowserCompat.MediaItem.fromMediaItemList(list));
                    return;
                }
                final List<MediaBrowserCompat.MediaItem> fromMediaItemList = MediaBrowserCompat.MediaItem.fromMediaItemList(list);
                final List<MediaBrowserCompat.SubscriptionCallback> callbacks = ((Subscription)o).getCallbacks();
                final List<Bundle> optionsList = ((Subscription)o).getOptionsList();
                for (int i = 0; i < callbacks.size(); ++i) {
                    final Bundle bundle = optionsList.get(i);
                    if (bundle == null) {
                        MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, fromMediaItemList);
                    }
                    else {
                        MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, this.applyOptions(fromMediaItemList, bundle), bundle);
                    }
                }
            }
            
            @Override
            public void onError(final String s) {
                MediaBrowserCompat.SubscriptionCallback.this.onError(s);
            }
        }
        
        private class StubApi26 extends StubApi21 implements MediaBrowserCompatApi26.SubscriptionCallback
        {
            StubApi26() {
            }
            
            @Override
            public void onChildrenLoaded(final String s, final List<?> list, final Bundle bundle) {
                MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, MediaBrowserCompat.MediaItem.fromMediaItemList(list), bundle);
            }
            
            @Override
            public void onError(final String s, final Bundle bundle) {
                MediaBrowserCompat.SubscriptionCallback.this.onError(s, bundle);
            }
        }
    }
}
