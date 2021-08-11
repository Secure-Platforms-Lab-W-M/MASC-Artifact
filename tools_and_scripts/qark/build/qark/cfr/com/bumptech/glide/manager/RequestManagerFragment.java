/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ActivityFragmentLifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Deprecated
public class RequestManagerFragment
extends Fragment {
    private static final String TAG = "RMFragment";
    private final Set<RequestManagerFragment> childRequestManagerFragments;
    private final ActivityFragmentLifecycle lifecycle;
    private Fragment parentFragmentHint;
    private RequestManager requestManager;
    private final RequestManagerTreeNode requestManagerTreeNode;
    private RequestManagerFragment rootRequestManagerFragment;

    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    RequestManagerFragment(ActivityFragmentLifecycle activityFragmentLifecycle) {
        this.requestManagerTreeNode = new FragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet<RequestManagerFragment>();
        this.lifecycle = activityFragmentLifecycle;
    }

    private void addChildRequestManagerFragment(RequestManagerFragment requestManagerFragment) {
        this.childRequestManagerFragments.add(requestManagerFragment);
    }

    private Fragment getParentFragmentUsingHint() {
        Fragment fragment = Build.VERSION.SDK_INT >= 17 ? this.getParentFragment() : null;
        if (fragment != null) {
            return fragment;
        }
        return this.parentFragmentHint;
    }

    private boolean isDescendant(Fragment fragment) {
        Fragment fragment2;
        Fragment fragment3 = this.getParentFragment();
        while ((fragment2 = fragment.getParentFragment()) != null) {
            if (fragment2.equals((Object)fragment3)) {
                return true;
            }
            fragment = fragment.getParentFragment();
        }
        return false;
    }

    private void registerFragmentWithRoot(Activity object) {
        this.unregisterFragmentWithRoot();
        this.rootRequestManagerFragment = object = Glide.get((Context)object).getRequestManagerRetriever().getRequestManagerFragment((Activity)object);
        if (!this.equals(object)) {
            this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    private void removeChildRequestManagerFragment(RequestManagerFragment requestManagerFragment) {
        this.childRequestManagerFragments.remove((Object)requestManagerFragment);
    }

    private void unregisterFragmentWithRoot() {
        RequestManagerFragment requestManagerFragment = this.rootRequestManagerFragment;
        if (requestManagerFragment != null) {
            requestManagerFragment.removeChildRequestManagerFragment(this);
            this.rootRequestManagerFragment = null;
        }
    }

    Set<RequestManagerFragment> getDescendantRequestManagerFragments() {
        if (this.equals((Object)this.rootRequestManagerFragment)) {
            return Collections.unmodifiableSet(this.childRequestManagerFragments);
        }
        if (this.rootRequestManagerFragment != null && Build.VERSION.SDK_INT >= 17) {
            HashSet<RequestManagerFragment> hashSet = new HashSet<RequestManagerFragment>();
            for (RequestManagerFragment requestManagerFragment : this.rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
                if (!this.isDescendant(requestManagerFragment.getParentFragment())) continue;
                hashSet.add(requestManagerFragment);
            }
            return Collections.unmodifiableSet(hashSet);
        }
        return Collections.emptySet();
    }

    ActivityFragmentLifecycle getGlideLifecycle() {
        return this.lifecycle;
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }

    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return this.requestManagerTreeNode;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.registerFragmentWithRoot(activity);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            if (Log.isLoggable((String)"RMFragment", (int)5)) {
                Log.w((String)"RMFragment", (String)"Unable to register fragment with root", (Throwable)illegalStateException);
            }
            return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
        this.unregisterFragmentWithRoot();
    }

    public void onDetach() {
        super.onDetach();
        this.unregisterFragmentWithRoot();
    }

    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }

    public void onStop() {
        super.onStop();
        this.lifecycle.onStop();
    }

    void setParentFragmentHint(Fragment fragment) {
        this.parentFragmentHint = fragment;
        if (fragment != null && fragment.getActivity() != null) {
            this.registerFragmentWithRoot(fragment.getActivity());
        }
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("{parent=");
        stringBuilder.append((Object)this.getParentFragmentUsingHint());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private class FragmentRequestManagerTreeNode
    implements RequestManagerTreeNode {
        FragmentRequestManagerTreeNode() {
        }

        @Override
        public Set<RequestManager> getDescendants() {
            Object object = RequestManagerFragment.this.getDescendantRequestManagerFragments();
            HashSet<RequestManager> hashSet = new HashSet<RequestManager>(object.size());
            object = object.iterator();
            while (object.hasNext()) {
                RequestManagerFragment requestManagerFragment = (RequestManagerFragment)((Object)object.next());
                if (requestManagerFragment.getRequestManager() == null) continue;
                hashSet.add(requestManagerFragment.getRequestManager());
            }
            return hashSet;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.toString());
            stringBuilder.append("{fragment=");
            stringBuilder.append((Object)RequestManagerFragment.this);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

