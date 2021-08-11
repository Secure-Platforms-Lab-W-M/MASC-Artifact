/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.os.Binder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Messenger
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompatUtils;
import android.support.v4.media.MediaBrowserServiceCompatApi21;
import android.support.v4.media.MediaBrowserServiceCompatApi23;
import android.support.v4.media.MediaBrowserServiceCompatApi24;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class MediaBrowserServiceCompat
extends Service {
    static final boolean DEBUG = Log.isLoggable((String)"MBServiceCompat", (int)3);
    private static final float EPSILON = 1.0E-5f;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final String KEY_MEDIA_ITEM = "media_item";
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final String KEY_SEARCH_RESULTS = "search_results";
    static final int RESULT_ERROR = -1;
    static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
    static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
    static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
    static final int RESULT_OK = 0;
    static final int RESULT_PROGRESS_UPDATE = 1;
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    static final String TAG = "MBServiceCompat";
    final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
    ConnectionRecord mCurConnection;
    final ServiceHandler mHandler;
    private MediaBrowserServiceImpl mImpl;
    MediaSessionCompat.Token mSession;

    public MediaBrowserServiceCompat() {
        this.mHandler = new ServiceHandler();
    }

    void addSubscription(String string2, ConnectionRecord connectionRecord, IBinder iBinder, Bundle bundle) {
        List<Pair<IBinder, Bundle>> list = connectionRecord.subscriptions.get(string2);
        if (list == null) {
            list = new ArrayList<Pair<IBinder, Bundle>>();
        }
        for (Pair<IBinder, Bundle> pair : list) {
            if (iBinder != pair.first || !MediaBrowserCompatUtils.areSameOptions(bundle, (Bundle)pair.second)) continue;
            return;
        }
        list.add(new Pair<IBinder, Bundle>(iBinder, bundle));
        connectionRecord.subscriptions.put(string2, list);
        this.performLoadChildren(string2, connectionRecord, bundle);
    }

    List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> list, Bundle bundle) {
        if (list == null) {
            return null;
        }
        int n = bundle.getInt("android.media.browse.extra.PAGE", -1);
        int n2 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if (n == -1 && n2 == -1) {
            return list;
        }
        int n3 = n2 * n;
        int n4 = n3 + n2;
        if (n >= 0 && n2 >= 1 && n3 < list.size()) {
            if (n4 > list.size()) {
                n4 = list.size();
            }
            return list.subList(n3, n4);
        }
        return Collections.EMPTY_LIST;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
    }

    public final Bundle getBrowserRootHints() {
        return this.mImpl.getBrowserRootHints();
    }

    @Nullable
    public MediaSessionCompat.Token getSessionToken() {
        return this.mSession;
    }

    boolean isValidPackage(String string2, int n) {
        if (string2 == null) {
            return false;
        }
        String[] arrstring = this.getPackageManager().getPackagesForUid(n);
        int n2 = arrstring.length;
        for (n = 0; n < n2; ++n) {
            if (!arrstring[n].equals(string2)) continue;
            return true;
        }
        return false;
    }

    public void notifyChildrenChanged(@NonNull String string2) {
        if (string2 != null) {
            this.mImpl.notifyChildrenChanged(string2, null);
            return;
        }
        throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    }

    public void notifyChildrenChanged(@NonNull String string2, @NonNull Bundle bundle) {
        if (string2 != null) {
            if (bundle != null) {
                this.mImpl.notifyChildrenChanged(string2, bundle);
                return;
            }
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    }

    public IBinder onBind(Intent intent) {
        return this.mImpl.onBind(intent);
    }

    public void onCreate() {
        super.onCreate();
        this.mImpl = Build.VERSION.SDK_INT >= 26 ? new MediaBrowserServiceImplApi24() : (Build.VERSION.SDK_INT >= 23 ? new MediaBrowserServiceImplApi23() : (Build.VERSION.SDK_INT >= 21 ? new MediaBrowserServiceImplApi21() : new MediaBrowserServiceImplBase()));
        this.mImpl.onCreate();
    }

    public void onCustomAction(@NonNull String string2, Bundle bundle, @NonNull Result<Bundle> result) {
        result.sendError(null);
    }

    @Nullable
    public abstract BrowserRoot onGetRoot(@NonNull String var1, int var2, @Nullable Bundle var3);

    public abstract void onLoadChildren(@NonNull String var1, @NonNull Result<List<MediaBrowserCompat.MediaItem>> var2);

    public void onLoadChildren(@NonNull String string2, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result, @NonNull Bundle bundle) {
        result.setFlags(1);
        this.onLoadChildren(string2, result);
    }

    public void onLoadItem(String string2, @NonNull Result<MediaBrowserCompat.MediaItem> result) {
        result.setFlags(2);
        result.sendResult(null);
    }

    public void onSearch(@NonNull String string2, Bundle bundle, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.setFlags(4);
        result.sendResult(null);
    }

    void performCustomAction(String string2, Bundle bundle, ConnectionRecord object, ResultReceiver object2) {
        object2 = new Result<Bundle>((Object)string2, (ResultReceiver)object2){
            final /* synthetic */ ResultReceiver val$receiver;
            {
                this.val$receiver = resultReceiver;
                super(object);
            }

            @Override
            void onErrorSent(Bundle bundle) {
                this.val$receiver.send(-1, bundle);
            }

            @Override
            void onProgressUpdateSent(Bundle bundle) {
                this.val$receiver.send(1, bundle);
            }

            @Override
            void onResultSent(Bundle bundle) {
                this.val$receiver.send(0, bundle);
            }
        };
        this.mCurConnection = object;
        this.onCustomAction(string2, bundle, (Result<Bundle>)object2);
        this.mCurConnection = null;
        if (object2.isDone()) {
            return;
        }
        object = new StringBuilder();
        object.append("onCustomAction must call detach() or sendResult() or sendError() before returning for action=");
        object.append(string2);
        object.append(" extras=");
        object.append((Object)bundle);
        throw new IllegalStateException(object.toString());
    }

    void performLoadChildren(final String string2, final ConnectionRecord connectionRecord, Bundle object) {
        Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>((Object)string2, (Bundle)object){
            final /* synthetic */ Bundle val$options;
            {
                this.val$options = bundle;
                super(object);
            }

            @Override
            void onResultSent(List<MediaBrowserCompat.MediaItem> list) {
                if (MediaBrowserServiceCompat.this.mConnections.get((Object)connectionRecord.callbacks.asBinder()) != connectionRecord) {
                    if (MediaBrowserServiceCompat.DEBUG) {
                        list = new StringBuilder();
                        list.append("Not sending onLoadChildren result for connection that has been disconnected. pkg=");
                        list.append(connectionRecord.pkg);
                        list.append(" id=");
                        list.append(string2);
                        Log.d((String)"MBServiceCompat", (String)list.toString());
                        return;
                    }
                    return;
                }
                if ((this.getFlags() & 1) != 0) {
                    list = MediaBrowserServiceCompat.this.applyOptions(list, this.val$options);
                }
                try {
                    connectionRecord.callbacks.onLoadChildren(string2, list, this.val$options);
                    return;
                }
                catch (RemoteException remoteException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Calling onLoadChildren() failed for id=");
                    stringBuilder.append(string2);
                    stringBuilder.append(" package=");
                    stringBuilder.append(connectionRecord.pkg);
                    Log.w((String)"MBServiceCompat", (String)stringBuilder.toString());
                    return;
                }
            }
        };
        this.mCurConnection = connectionRecord;
        if (object == null) {
            this.onLoadChildren(string2, result);
        } else {
            this.onLoadChildren(string2, result, (Bundle)object);
        }
        this.mCurConnection = null;
        if (result.isDone()) {
            return;
        }
        object = new StringBuilder();
        object.append("onLoadChildren must call detach() or sendResult() before returning for package=");
        object.append(connectionRecord.pkg);
        object.append(" id=");
        object.append(string2);
        throw new IllegalStateException(object.toString());
    }

    void performLoadItem(String string2, ConnectionRecord object, ResultReceiver object2) {
        object2 = new Result<MediaBrowserCompat.MediaItem>((Object)string2, (ResultReceiver)object2){
            final /* synthetic */ ResultReceiver val$receiver;
            {
                this.val$receiver = resultReceiver;
                super(object);
            }

            @Override
            void onResultSent(MediaBrowserCompat.MediaItem mediaItem) {
                if ((this.getFlags() & 2) != 0) {
                    this.val$receiver.send(-1, null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("media_item", (Parcelable)mediaItem);
                this.val$receiver.send(0, bundle);
            }
        };
        this.mCurConnection = object;
        this.onLoadItem(string2, (Result<MediaBrowserCompat.MediaItem>)object2);
        this.mCurConnection = null;
        if (object2.isDone()) {
            return;
        }
        object = new StringBuilder();
        object.append("onLoadItem must call detach() or sendResult() before returning for id=");
        object.append(string2);
        throw new IllegalStateException(object.toString());
    }

    void performSearch(String string2, Bundle object, ConnectionRecord connectionRecord, ResultReceiver object2) {
        object2 = new Result<List<MediaBrowserCompat.MediaItem>>((Object)string2, (ResultReceiver)object2){
            final /* synthetic */ ResultReceiver val$receiver;
            {
                this.val$receiver = resultReceiver;
                super(object);
            }

            @Override
            void onResultSent(List<MediaBrowserCompat.MediaItem> list) {
                if ((this.getFlags() & 4) == 0 && list != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArray("search_results", (Parcelable[])list.toArray(new MediaBrowserCompat.MediaItem[0]));
                    this.val$receiver.send(0, bundle);
                    return;
                }
                this.val$receiver.send(-1, null);
            }
        };
        this.mCurConnection = connectionRecord;
        this.onSearch(string2, (Bundle)object, (Result<List<MediaBrowserCompat.MediaItem>>)object2);
        this.mCurConnection = null;
        if (object2.isDone()) {
            return;
        }
        object = new StringBuilder();
        object.append("onSearch must call detach() or sendResult() before returning for query=");
        object.append(string2);
        throw new IllegalStateException(object.toString());
    }

    boolean removeSubscription(String string2, ConnectionRecord connectionRecord, IBinder iBinder) {
        if (iBinder == null) {
            if (connectionRecord.subscriptions.remove(string2) != null) {
                return true;
            }
            return false;
        }
        boolean bl = false;
        List<Pair<IBinder, Bundle>> list = connectionRecord.subscriptions.get(string2);
        if (list != null) {
            Iterator<Pair<IBinder, Bundle>> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iBinder != iterator.next().first) continue;
                bl = true;
                iterator.remove();
            }
            if (list.size() == 0) {
                connectionRecord.subscriptions.remove(string2);
                return bl;
            }
            return bl;
        }
        return false;
    }

    public void setSessionToken(MediaSessionCompat.Token token) {
        if (token != null) {
            if (this.mSession == null) {
                this.mSession = token;
                this.mImpl.setSessionToken(token);
                return;
            }
            throw new IllegalStateException("The session token has already been set.");
        }
        throw new IllegalArgumentException("Session token may not be null.");
    }

    public static final class BrowserRoot {
        public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
        @Deprecated
        public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
        private final Bundle mExtras;
        private final String mRootId;

        public BrowserRoot(@NonNull String string2, @Nullable Bundle bundle) {
            if (string2 != null) {
                this.mRootId = string2;
                this.mExtras = bundle;
                return;
            }
            throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public String getRootId() {
            return this.mRootId;
        }
    }

    private static class ConnectionRecord {
        ServiceCallbacks callbacks;
        String pkg;
        BrowserRoot root;
        Bundle rootHints;
        HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions = new HashMap();

        ConnectionRecord() {
        }
    }

    static interface MediaBrowserServiceImpl {
        public Bundle getBrowserRootHints();

        public void notifyChildrenChanged(String var1, Bundle var2);

        public IBinder onBind(Intent var1);

        public void onCreate();

        public void setSessionToken(MediaSessionCompat.Token var1);
    }

    @RequiresApi(value=21)
    class MediaBrowserServiceImplApi21
    implements MediaBrowserServiceImpl,
    MediaBrowserServiceCompatApi21.ServiceCompatProxy {
        Messenger mMessenger;
        final List<Bundle> mRootExtrasList;
        Object mServiceObj;

        MediaBrowserServiceImplApi21() {
            this.mRootExtrasList = new ArrayList<Bundle>();
        }

        @Override
        public Bundle getBrowserRootHints() {
            if (this.mMessenger == null) {
                return null;
            }
            if (MediaBrowserServiceCompat.this.mCurConnection != null) {
                if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
                    return null;
                }
                return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
        }

        @Override
        public void notifyChildrenChanged(final String string2, final Bundle bundle) {
            if (this.mMessenger == null) {
                MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, string2);
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Iterator<IBinder> iterator = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        Object object = iterator.next();
                        object = MediaBrowserServiceCompat.this.mConnections.get(object);
                        List<Pair<IBinder, Bundle>> list = object.subscriptions.get(string2);
                        if (list == null) continue;
                        for (Pair pair : list) {
                            if (!MediaBrowserCompatUtils.hasDuplicatedItems(bundle, (Bundle)pair.second)) continue;
                            MediaBrowserServiceCompat.this.performLoadChildren(string2, (ConnectionRecord)object, (Bundle)pair.second);
                        }
                    }
                }
            });
        }

        @Override
        public IBinder onBind(Intent intent) {
            return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, intent);
        }

        @Override
        public void onCreate() {
            this.mServiceObj = MediaBrowserServiceCompatApi21.createService((Context)MediaBrowserServiceCompat.this, this);
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
        }

        @Override
        public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String object, int n, Bundle bundle) {
            Object object2 = null;
            if (bundle != null && bundle.getInt("extra_client_version", 0) != 0) {
                bundle.remove("extra_client_version");
                this.mMessenger = new Messenger((Handler)MediaBrowserServiceCompat.this.mHandler);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("extra_service_version", 1);
                BundleCompat.putBinder(bundle2, "extra_messenger", this.mMessenger.getBinder());
                if (MediaBrowserServiceCompat.this.mSession != null) {
                    object2 = MediaBrowserServiceCompat.this.mSession.getExtraBinder();
                    object2 = object2 == null ? null : object2.asBinder();
                    BundleCompat.putBinder(bundle2, "extra_session_binder", (IBinder)object2);
                    object2 = bundle2;
                } else {
                    this.mRootExtrasList.add(bundle2);
                    object2 = bundle2;
                }
            }
            if ((object = MediaBrowserServiceCompat.this.onGetRoot((String)object, n, bundle)) == null) {
                return null;
            }
            if (object2 == null) {
                object2 = object.getExtras();
            } else if (object.getExtras() != null) {
                object2.putAll(object.getExtras());
            }
            return new MediaBrowserServiceCompatApi21.BrowserRoot(object.getRootId(), (Bundle)object2);
        }

        @Override
        public void onLoadChildren(String string2, final MediaBrowserServiceCompatApi21.ResultWrapper<List<Parcel>> object) {
            object = new Result<List<MediaBrowserCompat.MediaItem>>((Object)string2){

                @Override
                public void detach() {
                    object.detach();
                }

                @Override
                void onResultSent(List<MediaBrowserCompat.MediaItem> arrayList) {
                    ArrayList<Parcel> arrayList2 = null;
                    if (arrayList != null) {
                        arrayList2 = new ArrayList<Parcel>();
                        for (MediaBrowserCompat.MediaItem mediaItem : arrayList) {
                            Parcel parcel = Parcel.obtain();
                            mediaItem.writeToParcel(parcel, 0);
                            arrayList2.add(parcel);
                        }
                        arrayList = arrayList2;
                    } else {
                        arrayList = arrayList2;
                    }
                    object.sendResult(arrayList);
                }
            };
            MediaBrowserServiceCompat.this.onLoadChildren(string2, (Result<List<MediaBrowserCompat.MediaItem>>)object);
        }

        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    if (!MediaBrowserServiceImplApi21.this.mRootExtrasList.isEmpty()) {
                        IMediaSession iMediaSession = token.getExtraBinder();
                        if (iMediaSession != null) {
                            Iterator<Bundle> iterator = MediaBrowserServiceImplApi21.this.mRootExtrasList.iterator();
                            while (iterator.hasNext()) {
                                BundleCompat.putBinder(iterator.next(), "extra_session_binder", iMediaSession.asBinder());
                            }
                        }
                        MediaBrowserServiceImplApi21.this.mRootExtrasList.clear();
                    }
                    MediaBrowserServiceCompatApi21.setSessionToken(MediaBrowserServiceImplApi21.this.mServiceObj, token.getToken());
                }
            });
        }

    }

    @RequiresApi(value=23)
    class MediaBrowserServiceImplApi23
    extends MediaBrowserServiceImplApi21
    implements MediaBrowserServiceCompatApi23.ServiceCompatProxy {
        MediaBrowserServiceImplApi23() {
        }

        @Override
        public void onCreate() {
            this.mServiceObj = MediaBrowserServiceCompatApi23.createService((Context)MediaBrowserServiceCompat.this, this);
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
        }

        @Override
        public void onLoadItem(String string2, final MediaBrowserServiceCompatApi21.ResultWrapper<Parcel> object) {
            object = new Result<MediaBrowserCompat.MediaItem>((Object)string2){

                @Override
                public void detach() {
                    object.detach();
                }

                @Override
                void onResultSent(MediaBrowserCompat.MediaItem mediaItem) {
                    if (mediaItem == null) {
                        object.sendResult(null);
                        return;
                    }
                    Parcel parcel = Parcel.obtain();
                    mediaItem.writeToParcel(parcel, 0);
                    object.sendResult(parcel);
                }
            };
            MediaBrowserServiceCompat.this.onLoadItem(string2, (Result<MediaBrowserCompat.MediaItem>)object);
        }

    }

    @RequiresApi(value=26)
    class MediaBrowserServiceImplApi24
    extends MediaBrowserServiceImplApi23
    implements MediaBrowserServiceCompatApi24.ServiceCompatProxy {
        MediaBrowserServiceImplApi24() {
        }

        @Override
        public Bundle getBrowserRootHints() {
            if (MediaBrowserServiceCompat.this.mCurConnection != null) {
                if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
                    return null;
                }
                return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }
            return MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
        }

        @Override
        public void notifyChildrenChanged(String string2, Bundle bundle) {
            if (bundle == null) {
                MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, string2);
                return;
            }
            MediaBrowserServiceCompatApi24.notifyChildrenChanged(this.mServiceObj, string2, bundle);
        }

        @Override
        public void onCreate() {
            this.mServiceObj = MediaBrowserServiceCompatApi24.createService((Context)MediaBrowserServiceCompat.this, this);
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
        }

        @Override
        public void onLoadChildren(String string2, MediaBrowserServiceCompatApi24.ResultWrapper object, Bundle bundle) {
            object = new Result<List<MediaBrowserCompat.MediaItem>>((Object)string2, (MediaBrowserServiceCompatApi24.ResultWrapper)object){
                final /* synthetic */ MediaBrowserServiceCompatApi24.ResultWrapper val$resultWrapper;
                {
                    this.val$resultWrapper = resultWrapper;
                    super(object);
                }

                @Override
                public void detach() {
                    this.val$resultWrapper.detach();
                }

                @Override
                void onResultSent(List<MediaBrowserCompat.MediaItem> arrayList) {
                    ArrayList<Parcel> arrayList2 = null;
                    if (arrayList != null) {
                        arrayList2 = new ArrayList<Parcel>();
                        for (MediaBrowserCompat.MediaItem mediaItem : arrayList) {
                            Parcel parcel = Parcel.obtain();
                            mediaItem.writeToParcel(parcel, 0);
                            arrayList2.add(parcel);
                        }
                        arrayList = arrayList2;
                    } else {
                        arrayList = arrayList2;
                    }
                    this.val$resultWrapper.sendResult(arrayList, this.getFlags());
                }
            };
            MediaBrowserServiceCompat.this.onLoadChildren(string2, (Result<List<MediaBrowserCompat.MediaItem>>)object, bundle);
        }

    }

    class MediaBrowserServiceImplBase
    implements MediaBrowserServiceImpl {
        private Messenger mMessenger;

        MediaBrowserServiceImplBase() {
        }

        @Override
        public Bundle getBrowserRootHints() {
            if (MediaBrowserServiceCompat.this.mCurConnection != null) {
                if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
                    return null;
                }
                return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
        }

        @Override
        public void notifyChildrenChanged(final @NonNull String string2, final Bundle bundle) {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Iterator<IBinder> iterator = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        Object object = iterator.next();
                        object = MediaBrowserServiceCompat.this.mConnections.get(object);
                        List<Pair<IBinder, Bundle>> list = object.subscriptions.get(string2);
                        if (list == null) continue;
                        for (Pair pair : list) {
                            if (!MediaBrowserCompatUtils.hasDuplicatedItems(bundle, (Bundle)pair.second)) continue;
                            MediaBrowserServiceCompat.this.performLoadChildren(string2, (ConnectionRecord)object, (Bundle)pair.second);
                        }
                    }
                }
            });
        }

        @Override
        public IBinder onBind(Intent intent) {
            if ("android.media.browse.MediaBrowserService".equals(intent.getAction())) {
                return this.mMessenger.getBinder();
            }
            return null;
        }

        @Override
        public void onCreate() {
            this.mMessenger = new Messenger((Handler)MediaBrowserServiceCompat.this.mHandler);
        }

        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Iterator<ConnectionRecord> iterator = MediaBrowserServiceCompat.this.mConnections.values().iterator();
                    while (iterator.hasNext()) {
                        ConnectionRecord connectionRecord = iterator.next();
                        try {
                            connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
                        }
                        catch (RemoteException remoteException) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Connection for ");
                            stringBuilder.append(connectionRecord.pkg);
                            stringBuilder.append(" is no longer valid.");
                            Log.w((String)"MBServiceCompat", (String)stringBuilder.toString());
                            iterator.remove();
                        }
                    }
                }
            });
        }

    }

    public static class Result<T> {
        private final Object mDebug;
        private boolean mDetachCalled;
        private int mFlags;
        private boolean mSendErrorCalled;
        private boolean mSendProgressUpdateCalled;
        private boolean mSendResultCalled;

        Result(Object object) {
            this.mDebug = object;
        }

        private void checkExtraFields(Bundle bundle) {
            if (bundle == null) {
                return;
            }
            if (bundle.containsKey("android.media.browse.extra.DOWNLOAD_PROGRESS")) {
                float f = bundle.getFloat("android.media.browse.extra.DOWNLOAD_PROGRESS");
                if (f >= -1.0E-5f && f <= 1.00001f) {
                    return;
                }
                throw new IllegalArgumentException("The value of the EXTRA_DOWNLOAD_PROGRESS field must be a float number within [0.0, 1.0].");
            }
        }

        public void detach() {
            if (!this.mDetachCalled) {
                if (!this.mSendResultCalled) {
                    if (!this.mSendErrorCalled) {
                        this.mDetachCalled = true;
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("detach() called when sendError() had already been called for: ");
                    stringBuilder.append(this.mDebug);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("detach() called when sendResult() had already been called for: ");
                stringBuilder.append(this.mDebug);
                throw new IllegalStateException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("detach() called when detach() had already been called for: ");
            stringBuilder.append(this.mDebug);
            throw new IllegalStateException(stringBuilder.toString());
        }

        int getFlags() {
            return this.mFlags;
        }

        boolean isDone() {
            if (!(this.mDetachCalled || this.mSendResultCalled || this.mSendErrorCalled)) {
                return false;
            }
            return true;
        }

        void onErrorSent(Bundle object) {
            object = new StringBuilder();
            object.append("It is not supported to send an error for ");
            object.append(this.mDebug);
            throw new UnsupportedOperationException(object.toString());
        }

        void onProgressUpdateSent(Bundle object) {
            object = new StringBuilder();
            object.append("It is not supported to send an interim update for ");
            object.append(this.mDebug);
            throw new UnsupportedOperationException(object.toString());
        }

        void onResultSent(T t) {
        }

        public void sendError(Bundle object) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.mSendErrorCalled = true;
                this.onErrorSent((Bundle)object);
                return;
            }
            object = new StringBuilder();
            object.append("sendError() called when either sendResult() or sendError() had already been called for: ");
            object.append(this.mDebug);
            throw new IllegalStateException(object.toString());
        }

        public void sendProgressUpdate(Bundle object) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.checkExtraFields((Bundle)object);
                this.mSendProgressUpdateCalled = true;
                this.onProgressUpdateSent((Bundle)object);
                return;
            }
            object = new StringBuilder();
            object.append("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: ");
            object.append(this.mDebug);
            throw new IllegalStateException(object.toString());
        }

        public void sendResult(T object) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.mSendResultCalled = true;
                this.onResultSent(object);
                return;
            }
            object = new StringBuilder();
            object.append("sendResult() called when either sendResult() or sendError() had already been called for: ");
            object.append(this.mDebug);
            throw new IllegalStateException(object.toString());
        }

        void setFlags(int n) {
            this.mFlags = n;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    private static @interface ResultFlags {
    }

    private class ServiceBinderImpl {
        ServiceBinderImpl() {
        }

        public void addSubscription(final String string2, final IBinder iBinder, final Bundle bundle, final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    Object object = serviceCallbacks.asBinder();
                    if ((object = MediaBrowserServiceCompat.this.mConnections.get(object)) == null) {
                        object = new StringBuilder();
                        object.append("addSubscription for callback that isn't registered id=");
                        object.append(string2);
                        Log.w((String)"MBServiceCompat", (String)object.toString());
                        return;
                    }
                    MediaBrowserServiceCompat.this.addSubscription(string2, (ConnectionRecord)object, iBinder, bundle);
                }
            });
        }

        public void connect(final String string2, int n, Bundle object, final ServiceCallbacks serviceCallbacks) {
            if (MediaBrowserServiceCompat.this.isValidPackage(string2, n)) {
                MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable((Bundle)object, n){
                    final /* synthetic */ Bundle val$rootHints;
                    final /* synthetic */ int val$uid;
                    {
                        this.val$rootHints = bundle;
                        this.val$uid = n;
                    }

                    @Override
                    public void run() {
                        Object object = serviceCallbacks.asBinder();
                        MediaBrowserServiceCompat.this.mConnections.remove(object);
                        ConnectionRecord connectionRecord = new ConnectionRecord();
                        connectionRecord.pkg = string2;
                        connectionRecord.rootHints = this.val$rootHints;
                        connectionRecord.callbacks = serviceCallbacks;
                        connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(string2, this.val$uid, this.val$rootHints);
                        if (connectionRecord.root == null) {
                            object = new StringBuilder();
                            object.append("No root for client ");
                            object.append(string2);
                            object.append(" from service ");
                            object.append(this.getClass().getName());
                            Log.i((String)"MBServiceCompat", (String)object.toString());
                            try {
                                serviceCallbacks.onConnectFailed();
                            }
                            catch (RemoteException remoteException) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Calling onConnectFailed() failed. Ignoring. pkg=");
                                stringBuilder.append(string2);
                                Log.w((String)"MBServiceCompat", (String)stringBuilder.toString());
                            }
                            return;
                        }
                        try {
                            MediaBrowserServiceCompat.this.mConnections.put((IBinder)object, connectionRecord);
                            if (MediaBrowserServiceCompat.this.mSession != null) {
                                serviceCallbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras());
                            }
                            return;
                        }
                        catch (RemoteException remoteException) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Calling onConnect() failed. Dropping client. pkg=");
                            stringBuilder.append(string2);
                            Log.w((String)"MBServiceCompat", (String)stringBuilder.toString());
                            MediaBrowserServiceCompat.this.mConnections.remove(object);
                            return;
                        }
                    }
                });
                return;
            }
            object = new StringBuilder();
            object.append("Package/uid mismatch: uid=");
            object.append(n);
            object.append(" package=");
            object.append(string2);
            throw new IllegalArgumentException(object.toString());
        }

        public void disconnect(final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    IBinder iBinder = serviceCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                }
            });
        }

        public void getMediaItem(final String string2, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                if (resultReceiver == null) {
                    return;
                }
                MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                    @Override
                    public void run() {
                        Object object = serviceCallbacks.asBinder();
                        if ((object = MediaBrowserServiceCompat.this.mConnections.get(object)) == null) {
                            object = new StringBuilder();
                            object.append("getMediaItem for callback that isn't registered id=");
                            object.append(string2);
                            Log.w((String)"MBServiceCompat", (String)object.toString());
                            return;
                        }
                        MediaBrowserServiceCompat.this.performLoadItem(string2, (ConnectionRecord)object, resultReceiver);
                    }
                });
                return;
            }
        }

        public void registerCallbacks(final ServiceCallbacks serviceCallbacks, final Bundle bundle) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    IBinder iBinder = serviceCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                    ConnectionRecord connectionRecord = new ConnectionRecord();
                    connectionRecord.callbacks = serviceCallbacks;
                    connectionRecord.rootHints = bundle;
                    MediaBrowserServiceCompat.this.mConnections.put(iBinder, connectionRecord);
                }
            });
        }

        public void removeSubscription(final String string2, final IBinder iBinder, final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    Object object = serviceCallbacks.asBinder();
                    if ((object = MediaBrowserServiceCompat.this.mConnections.get(object)) == null) {
                        object = new StringBuilder();
                        object.append("removeSubscription for callback that isn't registered id=");
                        object.append(string2);
                        Log.w((String)"MBServiceCompat", (String)object.toString());
                        return;
                    }
                    if (!MediaBrowserServiceCompat.this.removeSubscription(string2, (ConnectionRecord)object, iBinder)) {
                        object = new StringBuilder();
                        object.append("removeSubscription called for ");
                        object.append(string2);
                        object.append(" which is not subscribed");
                        Log.w((String)"MBServiceCompat", (String)object.toString());
                        return;
                    }
                }
            });
        }

        public void search(final String string2, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                if (resultReceiver == null) {
                    return;
                }
                MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                    @Override
                    public void run() {
                        Object object = serviceCallbacks.asBinder();
                        if ((object = MediaBrowserServiceCompat.this.mConnections.get(object)) == null) {
                            object = new StringBuilder();
                            object.append("search for callback that isn't registered query=");
                            object.append(string2);
                            Log.w((String)"MBServiceCompat", (String)object.toString());
                            return;
                        }
                        MediaBrowserServiceCompat.this.performSearch(string2, bundle, (ConnectionRecord)object, resultReceiver);
                    }
                });
                return;
            }
        }

        public void sendCustomAction(final String string2, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                if (resultReceiver == null) {
                    return;
                }
                MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                    @Override
                    public void run() {
                        Object object = serviceCallbacks.asBinder();
                        if ((object = MediaBrowserServiceCompat.this.mConnections.get(object)) == null) {
                            object = new StringBuilder();
                            object.append("sendCustomAction for callback that isn't registered action=");
                            object.append(string2);
                            object.append(", extras=");
                            object.append((Object)bundle);
                            Log.w((String)"MBServiceCompat", (String)object.toString());
                            return;
                        }
                        MediaBrowserServiceCompat.this.performCustomAction(string2, bundle, (ConnectionRecord)object, resultReceiver);
                    }
                });
                return;
            }
        }

        public void unregisterCallbacks(final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(){

                @Override
                public void run() {
                    IBinder iBinder = serviceCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                }
            });
        }

    }

    private static interface ServiceCallbacks {
        public IBinder asBinder();

        public void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException;

        public void onConnectFailed() throws RemoteException;

        public void onLoadChildren(String var1, List<MediaBrowserCompat.MediaItem> var2, Bundle var3) throws RemoteException;
    }

    private static class ServiceCallbacksCompat
    implements ServiceCallbacks {
        final Messenger mCallbacks;

        ServiceCallbacksCompat(Messenger messenger) {
            this.mCallbacks = messenger;
        }

        private void sendRequest(int n, Bundle bundle) throws RemoteException {
            Message message = Message.obtain();
            message.what = n;
            message.arg1 = 1;
            message.setData(bundle);
            this.mCallbacks.send(message);
        }

        @Override
        public IBinder asBinder() {
            return this.mCallbacks.getBinder();
        }

        @Override
        public void onConnect(String string2, MediaSessionCompat.Token token, Bundle bundle) throws RemoteException {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putInt("extra_service_version", 1);
            Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", string2);
            bundle2.putParcelable("data_media_session_token", (Parcelable)token);
            bundle2.putBundle("data_root_hints", bundle);
            this.sendRequest(1, bundle2);
        }

        @Override
        public void onConnectFailed() throws RemoteException {
            this.sendRequest(2, null);
        }

        @Override
        public void onLoadChildren(String arrayList, List<MediaBrowserCompat.MediaItem> list, Bundle bundle) throws RemoteException {
            Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", (String)((Object)arrayList));
            bundle2.putBundle("data_options", bundle);
            if (list != null) {
                arrayList = list instanceof ArrayList ? (ArrayList)list : new ArrayList(list);
                bundle2.putParcelableArrayList("data_media_item_list", arrayList);
            }
            this.sendRequest(3, bundle2);
        }
    }

    private final class ServiceHandler
    extends Handler {
        private final ServiceBinderImpl mServiceBinderImpl;

        ServiceHandler() {
            this.mServiceBinderImpl = new ServiceBinderImpl();
        }

        public void handleMessage(Message message) {
            Object object = message.getData();
            switch (message.what) {
                default: {
                    object = new StringBuilder();
                    object.append("Unhandled message: ");
                    object.append((Object)message);
                    object.append("\n  Service version: ");
                    object.append(1);
                    object.append("\n  Client version: ");
                    object.append(message.arg1);
                    Log.w((String)"MBServiceCompat", (String)object.toString());
                    return;
                }
                case 9: {
                    this.mServiceBinderImpl.sendCustomAction(object.getString("data_custom_action"), object.getBundle("data_custom_action_extras"), (ResultReceiver)object.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 8: {
                    this.mServiceBinderImpl.search(object.getString("data_search_query"), object.getBundle("data_search_extras"), (ResultReceiver)object.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 7: {
                    this.mServiceBinderImpl.unregisterCallbacks(new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 6: {
                    this.mServiceBinderImpl.registerCallbacks(new ServiceCallbacksCompat(message.replyTo), object.getBundle("data_root_hints"));
                    return;
                }
                case 5: {
                    this.mServiceBinderImpl.getMediaItem(object.getString("data_media_item_id"), (ResultReceiver)object.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 4: {
                    this.mServiceBinderImpl.removeSubscription(object.getString("data_media_item_id"), BundleCompat.getBinder((Bundle)object, "data_callback_token"), new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 3: {
                    this.mServiceBinderImpl.addSubscription(object.getString("data_media_item_id"), BundleCompat.getBinder((Bundle)object, "data_callback_token"), object.getBundle("data_options"), new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 2: {
                    this.mServiceBinderImpl.disconnect(new ServiceCallbacksCompat(message.replyTo));
                    return;
                }
                case 1: 
            }
            this.mServiceBinderImpl.connect(object.getString("data_package_name"), object.getInt("data_calling_uid"), object.getBundle("data_root_hints"), new ServiceCallbacksCompat(message.replyTo));
        }

        public void postOrRun(Runnable runnable) {
            if (Thread.currentThread() == this.getLooper().getThread()) {
                runnable.run();
                return;
            }
            this.post(runnable);
        }

        public boolean sendMessageAtTime(Message message, long l) {
            Bundle bundle = message.getData();
            bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            bundle.putInt("data_calling_uid", Binder.getCallingUid());
            return super.sendMessageAtTime(message, l);
        }
    }

}

