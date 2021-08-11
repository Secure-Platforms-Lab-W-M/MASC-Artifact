/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Fragment
 *  android.app.FragmentManager
 *  android.app.FragmentTransaction
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewParent
 */
package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ActivityFragmentLifecycle;
import com.bumptech.glide.manager.ApplicationLifecycle;
import com.bumptech.glide.manager.EmptyRequestManagerTreeNode;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerFragment;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestManagerRetriever
implements Handler.Callback {
    private static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory(){

        @Override
        public RequestManager build(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, Context context) {
            return new RequestManager(glide, lifecycle, requestManagerTreeNode, context);
        }
    };
    private static final String FRAGMENT_INDEX_KEY = "key";
    static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final String TAG = "RMRetriever";
    private volatile RequestManager applicationManager;
    private final RequestManagerFactory factory;
    private final Handler handler;
    final Map<android.app.FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap<android.app.FragmentManager, RequestManagerFragment>();
    final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap<FragmentManager, SupportRequestManagerFragment>();
    private final Bundle tempBundle = new Bundle();
    private final ArrayMap<View, Fragment> tempViewToFragment = new ArrayMap();
    private final ArrayMap<View, androidx.fragment.app.Fragment> tempViewToSupportFragment = new ArrayMap();

    public RequestManagerRetriever(RequestManagerFactory requestManagerFactory) {
        if (requestManagerFactory == null) {
            requestManagerFactory = DEFAULT_FACTORY;
        }
        this.factory = requestManagerFactory;
        this.handler = new Handler(Looper.getMainLooper(), (Handler.Callback)this);
    }

    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17) {
            if (!activity.isDestroyed()) {
                return;
            }
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    private static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity)context;
        }
        if (context instanceof ContextWrapper) {
            return RequestManagerRetriever.findActivity(((ContextWrapper)context).getBaseContext());
        }
        return null;
    }

    @Deprecated
    private void findAllFragmentsWithViews(android.app.FragmentManager object, ArrayMap<View, Fragment> arrayMap) {
        if (Build.VERSION.SDK_INT >= 26) {
            for (Fragment fragment : object.getFragments()) {
                if (fragment.getView() == null) continue;
                arrayMap.put(fragment.getView(), fragment);
                this.findAllFragmentsWithViews(fragment.getChildFragmentManager(), arrayMap);
            }
            return;
        }
        this.findAllFragmentsWithViewsPreO((android.app.FragmentManager)object, arrayMap);
    }

    @Deprecated
    private void findAllFragmentsWithViewsPreO(android.app.FragmentManager fragmentManager, ArrayMap<View, Fragment> arrayMap) {
        int n = 0;
        do {
            this.tempBundle.putInt("key", n);
            Fragment fragment = null;
            try {
                Fragment fragment2;
                fragment = fragment2 = fragmentManager.getFragment(this.tempBundle, "key");
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (fragment == null) {
                return;
            }
            if (fragment.getView() != null) {
                arrayMap.put(fragment.getView(), fragment);
                if (Build.VERSION.SDK_INT >= 17) {
                    this.findAllFragmentsWithViews(fragment.getChildFragmentManager(), arrayMap);
                }
            }
            ++n;
        } while (true);
    }

    private static void findAllSupportFragmentsWithViews(Collection<androidx.fragment.app.Fragment> object, Map<View, androidx.fragment.app.Fragment> map) {
        if (object == null) {
            return;
        }
        object = object.iterator();
        while (object.hasNext()) {
            androidx.fragment.app.Fragment fragment = (androidx.fragment.app.Fragment)object.next();
            if (fragment == null || fragment.getView() == null) continue;
            map.put(fragment.getView(), fragment);
            RequestManagerRetriever.findAllSupportFragmentsWithViews(fragment.getChildFragmentManager().getFragments(), map);
        }
    }

    @Deprecated
    private Fragment findFragment(View view, Activity activity) {
        this.tempViewToFragment.clear();
        this.findAllFragmentsWithViews(activity.getFragmentManager(), this.tempViewToFragment);
        View view2 = null;
        View view3 = activity.findViewById(16908290);
        activity = view;
        view = view2;
        do {
            view2 = view;
            if (activity.equals((Object)view3)) break;
            view = this.tempViewToFragment.get((Object)activity);
            if (view != null) {
                view2 = view;
                break;
            }
            view2 = view;
            if (!(activity.getParent() instanceof View)) break;
            activity = (View)activity.getParent();
        } while (true);
        this.tempViewToFragment.clear();
        return view2;
    }

    private androidx.fragment.app.Fragment findSupportFragment(View object, FragmentActivity fragmentActivity) {
        this.tempViewToSupportFragment.clear();
        RequestManagerRetriever.findAllSupportFragmentsWithViews(fragmentActivity.getSupportFragmentManager().getFragments(), this.tempViewToSupportFragment);
        Object object2 = null;
        View view = fragmentActivity.findViewById(16908290);
        fragmentActivity = object;
        object = object2;
        do {
            object2 = object;
            if (fragmentActivity.equals((Object)view)) break;
            object = this.tempViewToSupportFragment.get(fragmentActivity);
            if (object != null) {
                object2 = object;
                break;
            }
            object2 = object;
            if (!(fragmentActivity.getParent() instanceof View)) break;
            fragmentActivity = (View)fragmentActivity.getParent();
        } while (true);
        this.tempViewToSupportFragment.clear();
        return object2;
    }

    @Deprecated
    private RequestManager fragmentGet(Context context, android.app.FragmentManager object, Fragment object2, boolean bl) {
        RequestManagerFragment requestManagerFragment = this.getRequestManagerFragment((android.app.FragmentManager)object, (Fragment)object2, bl);
        object = object2 = requestManagerFragment.getRequestManager();
        if (object2 == null) {
            object = Glide.get(context);
            object = this.factory.build((Glide)object, requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode(), context);
            requestManagerFragment.setRequestManager((RequestManager)object);
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RequestManager getApplicationManager(Context context) {
        if (this.applicationManager == null) {
            synchronized (this) {
                if (this.applicationManager == null) {
                    Glide glide = Glide.get(context.getApplicationContext());
                    this.applicationManager = this.factory.build(glide, new ApplicationLifecycle(), new EmptyRequestManagerTreeNode(), context.getApplicationContext());
                }
            }
        }
        return this.applicationManager;
    }

    private RequestManagerFragment getRequestManagerFragment(android.app.FragmentManager fragmentManager, Fragment fragment, boolean bl) {
        RequestManagerFragment requestManagerFragment;
        RequestManagerFragment requestManagerFragment2 = requestManagerFragment = (RequestManagerFragment)fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (requestManagerFragment == null) {
            requestManagerFragment2 = requestManagerFragment = this.pendingRequestManagerFragments.get((Object)fragmentManager);
            if (requestManagerFragment == null) {
                requestManagerFragment2 = new RequestManagerFragment();
                requestManagerFragment2.setParentFragmentHint(fragment);
                if (bl) {
                    requestManagerFragment2.getGlideLifecycle().onStart();
                }
                this.pendingRequestManagerFragments.put(fragmentManager, requestManagerFragment2);
                fragmentManager.beginTransaction().add((Fragment)requestManagerFragment2, "com.bumptech.glide.manager").commitAllowingStateLoss();
                this.handler.obtainMessage(1, (Object)fragmentManager).sendToTarget();
            }
        }
        return requestManagerFragment2;
    }

    private SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager fragmentManager, androidx.fragment.app.Fragment fragment, boolean bl) {
        SupportRequestManagerFragment supportRequestManagerFragment;
        SupportRequestManagerFragment supportRequestManagerFragment2 = supportRequestManagerFragment = (SupportRequestManagerFragment)fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (supportRequestManagerFragment == null) {
            supportRequestManagerFragment2 = supportRequestManagerFragment = this.pendingSupportRequestManagerFragments.get(fragmentManager);
            if (supportRequestManagerFragment == null) {
                supportRequestManagerFragment2 = new SupportRequestManagerFragment();
                supportRequestManagerFragment2.setParentFragmentHint(fragment);
                if (bl) {
                    supportRequestManagerFragment2.getGlideLifecycle().onStart();
                }
                this.pendingSupportRequestManagerFragments.put(fragmentManager, supportRequestManagerFragment2);
                fragmentManager.beginTransaction().add(supportRequestManagerFragment2, "com.bumptech.glide.manager").commitAllowingStateLoss();
                this.handler.obtainMessage(2, (Object)fragmentManager).sendToTarget();
            }
        }
        return supportRequestManagerFragment2;
    }

    private static boolean isActivityVisible(Context context) {
        if ((context = RequestManagerRetriever.findActivity(context)) != null && context.isFinishing()) {
            return false;
        }
        return true;
    }

    private RequestManager supportFragmentGet(Context context, FragmentManager object, androidx.fragment.app.Fragment object2, boolean bl) {
        SupportRequestManagerFragment supportRequestManagerFragment = this.getSupportRequestManagerFragment((FragmentManager)object, (androidx.fragment.app.Fragment)object2, bl);
        object = object2 = supportRequestManagerFragment.getRequestManager();
        if (object2 == null) {
            object = Glide.get(context);
            object = this.factory.build((Glide)object, supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode(), context);
            supportRequestManagerFragment.setRequestManager((RequestManager)object);
        }
        return object;
    }

    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(activity.getApplicationContext());
        }
        RequestManagerRetriever.assertNotDestroyed(activity);
        return this.fragmentGet((Context)activity, activity.getFragmentManager(), null, RequestManagerRetriever.isActivityVisible((Context)activity));
    }

    @Deprecated
    public RequestManager get(Fragment fragment) {
        if (fragment.getActivity() != null) {
            if (!Util.isOnBackgroundThread() && Build.VERSION.SDK_INT >= 17) {
                android.app.FragmentManager fragmentManager = fragment.getChildFragmentManager();
                return this.fragmentGet((Context)fragment.getActivity(), fragmentManager, fragment, fragment.isVisible());
            }
            return this.get(fragment.getActivity().getApplicationContext());
        }
        throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
    }

    public RequestManager get(Context context) {
        if (context != null) {
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity) {
                    return this.get((FragmentActivity)context);
                }
                if (context instanceof Activity) {
                    return this.get((Activity)context);
                }
                if (context instanceof ContextWrapper && ((ContextWrapper)context).getBaseContext().getApplicationContext() != null) {
                    return this.get(((ContextWrapper)context).getBaseContext());
                }
            }
            return this.getApplicationManager(context);
        }
        throw new IllegalArgumentException("You cannot start a load on a null Context");
    }

    public RequestManager get(View object) {
        if (Util.isOnBackgroundThread()) {
            return this.get(object.getContext().getApplicationContext());
        }
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(object.getContext(), "Unable to obtain a request manager for a view without a Context");
        Activity activity = RequestManagerRetriever.findActivity(object.getContext());
        if (activity == null) {
            return this.get(object.getContext().getApplicationContext());
        }
        if (activity instanceof FragmentActivity) {
            if ((object = this.findSupportFragment((View)object, (FragmentActivity)activity)) != null) {
                return this.get((androidx.fragment.app.Fragment)object);
            }
            return this.get((FragmentActivity)activity);
        }
        if ((object = this.findFragment((View)object, activity)) == null) {
            return this.get(activity);
        }
        return this.get((Fragment)object);
    }

    public RequestManager get(androidx.fragment.app.Fragment fragment) {
        Preconditions.checkNotNull(fragment.getContext(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
        if (Util.isOnBackgroundThread()) {
            return this.get(fragment.getContext().getApplicationContext());
        }
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        return this.supportFragmentGet(fragment.getContext(), fragmentManager, fragment, fragment.isVisible());
    }

    public RequestManager get(FragmentActivity fragmentActivity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(fragmentActivity.getApplicationContext());
        }
        RequestManagerRetriever.assertNotDestroyed(fragmentActivity);
        return this.supportFragmentGet((Context)fragmentActivity, fragmentActivity.getSupportFragmentManager(), null, RequestManagerRetriever.isActivityVisible((Context)fragmentActivity));
    }

    @Deprecated
    RequestManagerFragment getRequestManagerFragment(Activity activity) {
        return this.getRequestManagerFragment(activity.getFragmentManager(), null, RequestManagerRetriever.isActivityVisible((Context)activity));
    }

    SupportRequestManagerFragment getSupportRequestManagerFragment(Context context, FragmentManager fragmentManager) {
        return this.getSupportRequestManagerFragment(fragmentManager, null, RequestManagerRetriever.isActivityVisible(context));
    }

    public boolean handleMessage(Message object) {
        boolean bl = true;
        Object var5_3 = null;
        Object object2 = null;
        int n = object.what;
        if (n != 1) {
            if (n != 2) {
                bl = false;
                object = var5_3;
            } else {
                object2 = object = (FragmentManager)object.obj;
                object = this.pendingSupportRequestManagerFragments.remove(object);
            }
        } else {
            object2 = object = (android.app.FragmentManager)object.obj;
            object = this.pendingRequestManagerFragments.remove(object);
        }
        if (bl && object == null && Log.isLoggable((String)"RMRetriever", (int)5)) {
            object = new StringBuilder();
            object.append("Failed to remove expected request manager fragment, manager: ");
            object.append(object2);
            Log.w((String)"RMRetriever", (String)object.toString());
        }
        return bl;
    }

    public static interface RequestManagerFactory {
        public RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, Context var4);
    }

}

