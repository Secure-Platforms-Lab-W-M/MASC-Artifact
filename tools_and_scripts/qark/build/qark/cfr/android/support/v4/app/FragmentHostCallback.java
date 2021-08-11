/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.os.Bundle
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManagerImpl;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentHostCallback<E>
extends FragmentContainer {
    private final Activity mActivity;
    private SimpleArrayMap<String, LoaderManager> mAllLoaderManagers;
    private boolean mCheckedForLoaderManager;
    final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private LoaderManagerImpl mLoaderManager;
    private boolean mLoadersStarted;
    private boolean mRetainLoaders;
    final int mWindowAnimations;

    FragmentHostCallback(Activity activity, Context context, Handler handler, int n) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = activity;
        this.mContext = context;
        this.mHandler = handler;
        this.mWindowAnimations = n;
    }

    public FragmentHostCallback(Context context, Handler handler, int n) {
        Activity activity = context instanceof Activity ? (Activity)context : null;
        this(activity, context, handler, n);
    }

    FragmentHostCallback(FragmentActivity fragmentActivity) {
        this(fragmentActivity, (Context)fragmentActivity, fragmentActivity.mHandler, 0);
    }

    void doLoaderDestroy() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl == null) {
            return;
        }
        loaderManagerImpl.doDestroy();
    }

    void doLoaderRetain() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl == null) {
            return;
        }
        loaderManagerImpl.doRetain();
    }

    void doLoaderStart() {
        if (this.mLoadersStarted) {
            return;
        }
        this.mLoadersStarted = true;
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            loaderManagerImpl.doStart();
        } else if (!this.mCheckedForLoaderManager && (loaderManagerImpl = (this.mLoaderManager = this.getLoaderManager("(root)", this.mLoadersStarted, false))) != null && !loaderManagerImpl.mStarted) {
            this.mLoaderManager.doStart();
        }
        this.mCheckedForLoaderManager = true;
    }

    void doLoaderStop(boolean bl) {
        this.mRetainLoaders = bl;
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl == null) {
            return;
        }
        if (!this.mLoadersStarted) {
            return;
        }
        this.mLoadersStarted = false;
        if (bl) {
            loaderManagerImpl.doRetain();
            return;
        }
        loaderManagerImpl.doStop();
    }

    void dumpLoaders(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        printWriter.print(string2);
        printWriter.print("mLoadersStarted=");
        printWriter.println(this.mLoadersStarted);
        if (this.mLoaderManager != null) {
            printWriter.print(string2);
            printWriter.print("Loader Manager ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            printWriter.println(":");
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("  ");
            loaderManagerImpl.dump(stringBuilder.toString(), fileDescriptor, printWriter, arrstring);
            return;
        }
    }

    Activity getActivity() {
        return this.mActivity;
    }

    Context getContext() {
        return this.mContext;
    }

    FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    LoaderManagerImpl getLoaderManager(String string2, boolean bl, boolean bl2) {
        LoaderManagerImpl loaderManagerImpl;
        if (this.mAllLoaderManagers == null) {
            this.mAllLoaderManagers = new SimpleArrayMap();
        }
        if ((loaderManagerImpl = (LoaderManagerImpl)this.mAllLoaderManagers.get(string2)) == null && bl2) {
            loaderManagerImpl = new LoaderManagerImpl(string2, this, bl);
            this.mAllLoaderManagers.put(string2, loaderManagerImpl);
            return loaderManagerImpl;
        }
        if (bl && loaderManagerImpl != null && !loaderManagerImpl.mStarted) {
            loaderManagerImpl.doStart();
            return loaderManagerImpl;
        }
        return loaderManagerImpl;
    }

    LoaderManagerImpl getLoaderManagerImpl() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            return loaderManagerImpl;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = this.getLoaderManager("(root)", this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    boolean getRetainLoaders() {
        return this.mRetainLoaders;
    }

    void inactivateFragment(String string2) {
        SimpleArrayMap<String, LoaderManager> simpleArrayMap = this.mAllLoaderManagers;
        if (simpleArrayMap != null) {
            if ((simpleArrayMap = (LoaderManagerImpl)simpleArrayMap.get(string2)) != null && !simpleArrayMap.mRetaining) {
                simpleArrayMap.doDestroy();
                this.mAllLoaderManagers.remove(string2);
                return;
            }
            return;
        }
    }

    void onAttachFragment(Fragment fragment) {
    }

    public void onDump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
    }

    @Nullable
    @Override
    public View onFindViewById(int n) {
        return null;
    }

    @Nullable
    public abstract E onGetHost();

    public LayoutInflater onGetLayoutInflater() {
        return (LayoutInflater)this.mContext.getSystemService("layout_inflater");
    }

    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }

    @Override
    public boolean onHasView() {
        return true;
    }

    public boolean onHasWindowAnimations() {
        return true;
    }

    public void onRequestPermissionsFromFragment(@NonNull Fragment fragment, @NonNull String[] arrstring, int n) {
    }

    public boolean onShouldSaveFragmentState(Fragment fragment) {
        return true;
    }

    public boolean onShouldShowRequestPermissionRationale(@NonNull String string2) {
        return false;
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int n) {
        this.onStartActivityFromFragment(fragment, intent, n, null);
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int n, @Nullable Bundle bundle) {
        if (n == -1) {
            this.mContext.startActivity(intent);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }

    public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intentSender, int n, @Nullable Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        if (n == -1) {
            ActivityCompat.startIntentSenderForResult(this.mActivity, intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
    }

    public void onSupportInvalidateOptionsMenu() {
    }

    void reportLoaderStart() {
        LoaderManagerImpl[] arrloaderManagerImpl = this.mAllLoaderManagers;
        if (arrloaderManagerImpl != null) {
            int n;
            int n2 = arrloaderManagerImpl.size();
            arrloaderManagerImpl = new LoaderManagerImpl[n2];
            for (n = n2 - 1; n >= 0; --n) {
                arrloaderManagerImpl[n] = (LoaderManagerImpl)this.mAllLoaderManagers.valueAt(n);
            }
            for (n = 0; n < n2; ++n) {
                LoaderManagerImpl loaderManagerImpl = arrloaderManagerImpl[n];
                loaderManagerImpl.finishRetain();
                loaderManagerImpl.doReportStart();
            }
            return;
        }
    }

    void restoreLoaderNonConfig(SimpleArrayMap<String, LoaderManager> simpleArrayMap) {
        if (simpleArrayMap != null) {
            int n = simpleArrayMap.size();
            for (int i = 0; i < n; ++i) {
                ((LoaderManagerImpl)simpleArrayMap.valueAt(i)).updateHostController(this);
            }
        }
        this.mAllLoaderManagers = simpleArrayMap;
    }

    SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        int n = 0;
        int n2 = 0;
        LoaderManagerImpl[] arrloaderManagerImpl = this.mAllLoaderManagers;
        if (arrloaderManagerImpl != null) {
            int n3 = arrloaderManagerImpl.size();
            arrloaderManagerImpl = new LoaderManagerImpl[n3];
            for (n = n3 - 1; n >= 0; --n) {
                arrloaderManagerImpl[n] = (LoaderManagerImpl)this.mAllLoaderManagers.valueAt(n);
            }
            boolean bl = this.getRetainLoaders();
            n = n2;
            for (int i = 0; i < n3; ++i) {
                LoaderManagerImpl loaderManagerImpl = arrloaderManagerImpl[i];
                if (!loaderManagerImpl.mRetaining && bl) {
                    if (!loaderManagerImpl.mStarted) {
                        loaderManagerImpl.doStart();
                    }
                    loaderManagerImpl.doRetain();
                }
                if (loaderManagerImpl.mRetaining) {
                    n = 1;
                    continue;
                }
                loaderManagerImpl.doDestroy();
                this.mAllLoaderManagers.remove(loaderManagerImpl.mWho);
            }
        }
        if (n != 0) {
            return this.mAllLoaderManagers;
        }
        return null;
    }
}

