/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl
extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    boolean mCreatingLoader;
    FragmentHostCallback mHost;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders = new SparseArrayCompat();
    final SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat();
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;

    static {
        DEBUG = false;
    }

    LoaderManagerImpl(String string2, FragmentHostCallback fragmentHostCallback, boolean bl) {
        this.mWho = string2;
        this.mHost = fragmentHostCallback;
        this.mStarted = bl;
    }

    private LoaderInfo createAndInstallLoader(int n, Bundle object, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
        try {
            this.mCreatingLoader = true;
            object = this.createLoader(n, (Bundle)object, loaderCallbacks);
            this.installLoader((LoaderInfo)object);
            return object;
        }
        finally {
            this.mCreatingLoader = false;
        }
    }

    private LoaderInfo createLoader(int n, Bundle bundle, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
        LoaderInfo loaderInfo = new LoaderInfo(n, bundle, loaderCallbacks);
        loaderInfo.mLoader = loaderCallbacks.onCreateLoader(n, bundle);
        return loaderInfo;
    }

    @Override
    public void destroyLoader(int n) {
        if (!this.mCreatingLoader) {
            int n2;
            Object object;
            if (DEBUG) {
                object = new StringBuilder();
                object.append("destroyLoader in ");
                object.append(this);
                object.append(" of ");
                object.append(n);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            if ((n2 = this.mLoaders.indexOfKey(n)) >= 0) {
                object = this.mLoaders.valueAt(n2);
                this.mLoaders.removeAt(n2);
                object.destroy();
            }
            if ((n = this.mInactiveLoaders.indexOfKey(n)) >= 0) {
                object = this.mInactiveLoaders.valueAt(n);
                this.mInactiveLoaders.removeAt(n);
                object.destroy();
            }
            if (this.mHost != null && !this.hasRunningLoaders()) {
                this.mHost.mFragmentManager.startPendingDeferredFragments();
                return;
            }
            return;
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    void doDestroy() {
        StringBuilder stringBuilder;
        int n;
        if (!this.mRetaining) {
            if (DEBUG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Destroying Active in ");
                stringBuilder.append(this);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            for (n = this.mLoaders.size() - 1; n >= 0; --n) {
                this.mLoaders.valueAt(n).destroy();
            }
            this.mLoaders.clear();
        }
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Destroying Inactive in ");
            stringBuilder.append(this);
            Log.v((String)"LoaderManager", (String)stringBuilder.toString());
        }
        for (n = this.mInactiveLoaders.size() - 1; n >= 0; --n) {
            this.mInactiveLoaders.valueAt(n).destroy();
        }
        this.mInactiveLoaders.clear();
        this.mHost = null;
    }

    void doReportNextStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt((int)i).mReportNextStart = true;
        }
    }

    void doReportStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).reportStart();
        }
    }

    void doRetain() {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Retaining in ");
            stringBuilder.append(this);
            Log.v((String)"LoaderManager", (String)stringBuilder.toString());
        }
        if (!this.mStarted) {
            stringBuilder = new RuntimeException("here");
            stringBuilder.fillInStackTrace();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Called doRetain when not started: ");
            stringBuilder2.append(this);
            Log.w((String)"LoaderManager", (String)stringBuilder2.toString(), (Throwable)((Object)stringBuilder));
            return;
        }
        this.mRetaining = true;
        this.mStarted = false;
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).retain();
        }
    }

    void doStart() {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Starting in ");
            stringBuilder.append(this);
            Log.v((String)"LoaderManager", (String)stringBuilder.toString());
        }
        if (this.mStarted) {
            stringBuilder = new RuntimeException("here");
            stringBuilder.fillInStackTrace();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Called doStart when already started: ");
            stringBuilder2.append(this);
            Log.w((String)"LoaderManager", (String)stringBuilder2.toString(), (Throwable)((Object)stringBuilder));
            return;
        }
        this.mStarted = true;
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).start();
        }
    }

    void doStop() {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Stopping in ");
            stringBuilder.append(this);
            Log.v((String)"LoaderManager", (String)stringBuilder.toString());
        }
        if (!this.mStarted) {
            stringBuilder = new RuntimeException("here");
            stringBuilder.fillInStackTrace();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Called doStop when not started: ");
            stringBuilder2.append(this);
            Log.w((String)"LoaderManager", (String)stringBuilder2.toString(), (Throwable)((Object)stringBuilder));
            return;
        }
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).stop();
        }
        this.mStarted = false;
    }

    @Override
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        LoaderInfo loaderInfo;
        int n;
        if (this.mLoaders.size() > 0) {
            printWriter.print(string2);
            printWriter.println("Active Loaders:");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("    ");
            String string3 = stringBuilder.toString();
            for (n = 0; n < this.mLoaders.size(); ++n) {
                loaderInfo = this.mLoaders.valueAt(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(this.mLoaders.keyAt(n));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string3, fileDescriptor, printWriter, arrstring);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            printWriter.print(string2);
            printWriter.println("Inactive Loaders:");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("    ");
            String string4 = stringBuilder.toString();
            for (n = 0; n < this.mInactiveLoaders.size(); ++n) {
                loaderInfo = this.mInactiveLoaders.valueAt(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(this.mInactiveLoaders.keyAt(n));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string4, fileDescriptor, printWriter, arrstring);
            }
            return;
        }
    }

    void finishRetain() {
        if (this.mRetaining) {
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Finished Retaining in ");
                stringBuilder.append(this);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            this.mRetaining = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).finishRetain();
            }
            return;
        }
    }

    @Override
    public <D> Loader<D> getLoader(int n) {
        if (!this.mCreatingLoader) {
            LoaderInfo loaderInfo = this.mLoaders.get(n);
            if (loaderInfo != null) {
                if (loaderInfo.mPendingLoader != null) {
                    return loaderInfo.mPendingLoader.mLoader;
                }
                return loaderInfo.mLoader;
            }
            return null;
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    @Override
    public boolean hasRunningLoaders() {
        boolean bl = false;
        int n = this.mLoaders.size();
        for (int i = 0; i < n; ++i) {
            LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
            boolean bl2 = loaderInfo.mStarted && !loaderInfo.mDeliveredData;
            bl |= bl2;
        }
        return bl;
    }

    @Override
    public <D> Loader<D> initLoader(int n, Bundle object, LoaderManager.LoaderCallbacks<D> object2) {
        if (!this.mCreatingLoader) {
            LoaderInfo loaderInfo = this.mLoaders.get(n);
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("initLoader in ");
                stringBuilder.append(this);
                stringBuilder.append(": args=");
                stringBuilder.append(object);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            if (loaderInfo == null) {
                object = this.createAndInstallLoader(n, (Bundle)object, (LoaderManager.LoaderCallbacks<Object>)object2);
                if (DEBUG) {
                    object2 = new StringBuilder();
                    object2.append("  Created new loader ");
                    object2.append(object);
                    Log.v((String)"LoaderManager", (String)object2.toString());
                }
            } else {
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("  Re-using existing loader ");
                    object.append(loaderInfo);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                loaderInfo.mCallbacks = object2;
                object = loaderInfo;
            }
            if (object.mHaveData && this.mStarted) {
                object.callOnLoadFinished(object.mLoader, object.mData);
            }
            return object.mLoader;
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    void installLoader(LoaderInfo loaderInfo) {
        this.mLoaders.put(loaderInfo.mId, loaderInfo);
        if (this.mStarted) {
            loaderInfo.start();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public <D> Loader<D> restartLoader(int n, Bundle bundle, LoaderManager.LoaderCallbacks<D> loaderCallbacks) {
        Object object;
        if (this.mCreatingLoader) throw new IllegalStateException("Called while creating a loader");
        LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (DEBUG) {
            object = new StringBuilder();
            object.append("restartLoader in ");
            object.append(this);
            object.append(": args=");
            object.append((Object)bundle);
            Log.v((String)"LoaderManager", (String)object.toString());
        }
        if (loaderInfo == null) return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
        object = this.mInactiveLoaders.get(n);
        if (object != null) {
            if (loaderInfo.mHaveData) {
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Removing last inactive loader: ");
                    stringBuilder.append(loaderInfo);
                    Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                }
                object.mDeliveredData = false;
                object.destroy();
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(n, loaderInfo);
                return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
            }
            if (!loaderInfo.cancel()) {
                if (DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Current loader is stopped; replacing");
                }
                this.mLoaders.put(n, null);
                loaderInfo.destroy();
                return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
            }
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)"  Current loader is running; configuring pending loader");
            }
            if (loaderInfo.mPendingLoader != null) {
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("  Removing pending loader: ");
                    object.append(loaderInfo.mPendingLoader);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                loaderInfo.mPendingLoader.destroy();
                loaderInfo.mPendingLoader = null;
            }
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)"  Enqueuing as new pending loader");
            }
            loaderInfo.mPendingLoader = this.createLoader(n, bundle, loaderCallbacks);
            return loaderInfo.mPendingLoader.mLoader;
        }
        if (DEBUG) {
            object = new StringBuilder();
            object.append("  Making last loader inactive: ");
            object.append(loaderInfo);
            Log.v((String)"LoaderManager", (String)object.toString());
        }
        loaderInfo.mLoader.abandon();
        this.mInactiveLoaders.put(n, loaderInfo);
        return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("LoaderManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    void updateHostController(FragmentHostCallback fragmentHostCallback) {
        this.mHost = fragmentHostCallback;
    }

    final class LoaderInfo
    implements Loader.OnLoadCompleteListener<Object>,
    Loader.OnLoadCanceledListener<Object> {
        final Bundle mArgs;
        LoaderManager.LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;

        public LoaderInfo(int n, Bundle bundle, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
            this.mId = n;
            this.mArgs = bundle;
            this.mCallbacks = loaderCallbacks;
        }

        void callOnLoadFinished(Loader<Object> loader, Object object) {
            if (this.mCallbacks != null) {
                String string2 = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    string2 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (LoaderManagerImpl.DEBUG) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("  onLoadFinished in ");
                        stringBuilder.append(loader);
                        stringBuilder.append(": ");
                        stringBuilder.append(loader.dataToString(object));
                        Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                    }
                    this.mCallbacks.onLoadFinished(loader, object);
                    this.mDeliveredData = true;
                    return;
                }
                finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = string2;
                    }
                }
            }
        }

        boolean cancel() {
            Object object;
            if (LoaderManagerImpl.DEBUG) {
                object = new StringBuilder();
                object.append("  Canceling: ");
                object.append(this);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            if (this.mStarted && (object = this.mLoader) != null && this.mListenerRegistered) {
                boolean bl = object.cancelLoad();
                if (!bl) {
                    this.onLoadCanceled(this.mLoader);
                    return bl;
                }
                return bl;
            }
            return false;
        }

        void destroy() {
            Loader<Object> loader;
            if (LoaderManagerImpl.DEBUG) {
                loader = new StringBuilder();
                loader.append("  Destroying: ");
                loader.append((Object)this);
                Log.v((String)"LoaderManager", (String)loader.toString());
            }
            this.mDestroyed = true;
            boolean bl = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && bl) {
                if (LoaderManagerImpl.DEBUG) {
                    loader = new StringBuilder();
                    loader.append("  Resetting: ");
                    loader.append((Object)this);
                    Log.v((String)"LoaderManager", (String)loader.toString());
                }
                loader = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    loader = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                }
                finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = loader;
                    }
                }
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            loader = this.mLoader;
            if (loader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    loader.unregisterListener(this);
                    this.mLoader.unregisterOnLoadCanceledListener(this);
                }
                this.mLoader.reset();
            }
            if ((loader = this.mPendingLoader) != null) {
                loader.destroy();
                return;
            }
        }

        public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
            StringBuilder stringBuilder;
            printWriter.print(string2);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println((Object)this.mArgs);
            printWriter.print(string2);
            printWriter.print("mCallbacks=");
            printWriter.println(this.mCallbacks);
            printWriter.print(string2);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            Object object = this.mLoader;
            if (object != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append("  ");
                object.dump(stringBuilder.toString(), fileDescriptor, printWriter, arrstring);
            }
            if (this.mHaveData || this.mDeliveredData) {
                printWriter.print(string2);
                printWriter.print("mHaveData=");
                printWriter.print(this.mHaveData);
                printWriter.print("  mDeliveredData=");
                printWriter.println(this.mDeliveredData);
                printWriter.print(string2);
                printWriter.print("mData=");
                printWriter.println(this.mData);
            }
            printWriter.print(string2);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mReportNextStart=");
            printWriter.print(this.mReportNextStart);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            printWriter.print(string2);
            printWriter.print("mRetaining=");
            printWriter.print(this.mRetaining);
            printWriter.print(" mRetainingStarted=");
            printWriter.print(this.mRetainingStarted);
            printWriter.print(" mListenerRegistered=");
            printWriter.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                printWriter.print(string2);
                printWriter.println("Pending Loader ");
                printWriter.print(this.mPendingLoader);
                printWriter.println(":");
                object = this.mPendingLoader;
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append("  ");
                object.dump(stringBuilder.toString(), fileDescriptor, printWriter, arrstring);
                return;
            }
        }

        void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Finished Retaining: ");
                    stringBuilder.append(this);
                    Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                }
                this.mRetaining = false;
                boolean bl = this.mStarted;
                if (bl != this.mRetainingStarted && !bl) {
                    this.stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                this.callOnLoadFinished(this.mLoader, this.mData);
                return;
            }
        }

        @Override
        public void onLoadCanceled(Loader<Object> object) {
            if (LoaderManagerImpl.DEBUG) {
                object = new StringBuilder();
                object.append("onLoadCanceled: ");
                object.append(this);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load canceled -- destroyed");
                }
                return;
            }
            if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load canceled -- not active");
                }
                return;
            }
            object = this.mPendingLoader;
            if (object != null) {
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Switching to pending loader: ");
                    stringBuilder.append(object);
                    Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                }
                this.mPendingLoader = null;
                LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                this.destroy();
                LoaderManagerImpl.this.installLoader((LoaderInfo)object);
                return;
            }
        }

        @Override
        public void onLoadComplete(Loader<Object> object, Object object2) {
            Object object3;
            if (LoaderManagerImpl.DEBUG) {
                object3 = new StringBuilder();
                object3.append("onLoadComplete: ");
                object3.append(this);
                Log.v((String)"LoaderManager", (String)object3.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load complete -- destroyed");
                }
                return;
            }
            if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load complete -- not active");
                }
                return;
            }
            object3 = this.mPendingLoader;
            if (object3 != null) {
                if (LoaderManagerImpl.DEBUG) {
                    object = new StringBuilder();
                    object.append("  Switching to pending loader: ");
                    object.append(object3);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                this.mPendingLoader = null;
                LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                this.destroy();
                LoaderManagerImpl.this.installLoader((LoaderInfo)object3);
                return;
            }
            if (this.mData != object2 || !this.mHaveData) {
                this.mData = object2;
                this.mHaveData = true;
                if (this.mStarted) {
                    this.callOnLoadFinished((Loader<Object>)object, object2);
                }
            }
            if ((object = LoaderManagerImpl.this.mInactiveLoaders.get(this.mId)) != null && object != this) {
                object.mDeliveredData = false;
                object.destroy();
                LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
            }
            if (LoaderManagerImpl.this.mHost != null && !LoaderManagerImpl.this.hasRunningLoaders()) {
                LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
                return;
            }
        }

        void reportStart() {
            if (this.mStarted) {
                if (this.mReportNextStart) {
                    this.mReportNextStart = false;
                    if (this.mHaveData && !this.mRetaining) {
                        this.callOnLoadFinished(this.mLoader, this.mData);
                        return;
                    }
                    return;
                }
                return;
            }
        }

        void retain() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Retaining: ");
                stringBuilder.append(this);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }

        void start() {
            Object object;
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
                return;
            }
            if (this.mStarted) {
                return;
            }
            this.mStarted = true;
            if (LoaderManagerImpl.DEBUG) {
                object = new StringBuilder();
                object.append("  Starting: ");
                object.append(this);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            if (this.mLoader == null && (object = this.mCallbacks) != null) {
                this.mLoader = object.onCreateLoader(this.mId, this.mArgs);
            }
            if ((object = this.mLoader) != null) {
                if (object.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                    object = new StringBuilder();
                    object.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                    object.append(this.mLoader);
                    throw new IllegalArgumentException(object.toString());
                }
                if (!this.mListenerRegistered) {
                    this.mLoader.registerListener(this.mId, this);
                    this.mLoader.registerOnLoadCanceledListener(this);
                    this.mListenerRegistered = true;
                }
                this.mLoader.startLoading();
                return;
            }
        }

        void stop() {
            Loader<Object> loader;
            if (LoaderManagerImpl.DEBUG) {
                loader = new StringBuilder();
                loader.append("  Stopping: ");
                loader.append((Object)this);
                Log.v((String)"LoaderManager", (String)loader.toString());
            }
            this.mStarted = false;
            if (!this.mRetaining) {
                loader = this.mLoader;
                if (loader != null && this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    loader.unregisterListener(this);
                    this.mLoader.unregisterOnLoadCanceledListener(this);
                    this.mLoader.stopLoading();
                    return;
                }
                return;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(64);
            stringBuilder.append("LoaderInfo{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" #");
            stringBuilder.append(this.mId);
            stringBuilder.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, stringBuilder);
            stringBuilder.append("}}");
            return stringBuilder.toString();
        }
    }

}

