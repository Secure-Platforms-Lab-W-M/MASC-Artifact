/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Set;

public abstract class FragmentStatePagerAdapter
extends PagerAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapt";
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments = new ArrayList();
    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList();

    public FragmentStatePagerAdapter(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void destroyItem(ViewGroup object, int n, Object object2) {
        object2 = (Fragment)object2;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= n) {
            this.mSavedState.add(null);
        }
        ArrayList<Fragment.SavedState> arrayList = this.mSavedState;
        object = object2.isAdded() ? this.mFragmentManager.saveFragmentInstanceState((Fragment)object2) : null;
        arrayList.set(n, (Fragment.SavedState)object);
        this.mFragments.set(n, null);
        this.mCurTransaction.remove((Fragment)object2);
    }

    @Override
    public void finishUpdate(ViewGroup object) {
        object = this.mCurTransaction;
        if (object != null) {
            object.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
            return;
        }
    }

    public abstract Fragment getItem(int var1);

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int n) {
        Fragment.SavedState savedState;
        Fragment fragment;
        if (this.mFragments.size() > n && (fragment = this.mFragments.get(n)) != null) {
            return fragment;
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        fragment = this.getItem(n);
        if (this.mSavedState.size() > n && (savedState = this.mSavedState.get(n)) != null) {
            fragment.setInitialSavedState(savedState);
        }
        while (this.mFragments.size() <= n) {
            this.mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        this.mFragments.set(n, fragment);
        this.mCurTransaction.add(viewGroup.getId(), fragment);
        return fragment;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (((Fragment)object).getView() == view) {
            return true;
        }
        return false;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader object) {
        if (parcelable != null) {
            int n;
            parcelable = (Bundle)parcelable;
            parcelable.setClassLoader((ClassLoader)object);
            object = parcelable.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (object != null) {
                for (n = 0; n < object.length; ++n) {
                    this.mSavedState.add((Fragment.SavedState)object[n]);
                }
            }
            for (String string2 : parcelable.keySet()) {
                if (!string2.startsWith("f")) continue;
                n = Integer.parseInt(string2.substring(1));
                Object object2 = this.mFragmentManager.getFragment((Bundle)parcelable, string2);
                if (object2 != null) {
                    while (this.mFragments.size() <= n) {
                        this.mFragments.add(null);
                    }
                    object2.setMenuVisibility(false);
                    this.mFragments.set(n, (Fragment)object2);
                    continue;
                }
                object2 = new StringBuilder();
                object2.append("Bad fragment at key ");
                object2.append(string2);
                Log.w((String)"FragmentStatePagerAdapt", (String)object2.toString());
            }
            return;
        }
    }

    @Override
    public Parcelable saveState() {
        Object object;
        Bundle bundle = null;
        if (this.mSavedState.size() > 0) {
            bundle = new Bundle();
            object = new Fragment.SavedState[this.mSavedState.size()];
            this.mSavedState.toArray((T[])object);
            bundle.putParcelableArray("states", (Parcelable[])object);
        }
        for (int i = 0; i < this.mFragments.size(); ++i) {
            object = this.mFragments.get(i);
            if (object == null || !object.isAdded()) continue;
            if (bundle == null) {
                bundle = new Bundle();
            }
            CharSequence charSequence = new StringBuilder();
            charSequence.append("f");
            charSequence.append(i);
            charSequence = charSequence.toString();
            this.mFragmentManager.putFragment(bundle, (String)charSequence, (Fragment)object);
        }
        return bundle;
    }

    @Override
    public void setPrimaryItem(ViewGroup object, int n, Object object2) {
        object = (Fragment)object2;
        object2 = this.mCurrentPrimaryItem;
        if (object != object2) {
            if (object2 != null) {
                object2.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (object != null) {
                object.setMenuVisibility(true);
                object.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = object;
            return;
        }
    }

    @Override
    public void startUpdate(ViewGroup object) {
        if (object.getId() != -1) {
            return;
        }
        object = new StringBuilder();
        object.append("ViewPager with adapter ");
        object.append(this);
        object.append(" requires a view id");
        throw new IllegalStateException(object.toString());
    }
}

