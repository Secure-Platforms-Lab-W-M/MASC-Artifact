/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentState;
import androidx.fragment.app.FragmentStateManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class FragmentStore {
    private static final String TAG = "FragmentManager";
    private final HashMap<String, FragmentStateManager> mActive = new HashMap();
    private final ArrayList<Fragment> mAdded = new ArrayList();

    FragmentStore() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void addFragment(Fragment fragment) {
        if (!this.mAdded.contains(fragment)) {
            ArrayList<Fragment> arrayList = this.mAdded;
            synchronized (arrayList) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(fragment);
        throw new IllegalStateException(stringBuilder.toString());
    }

    void burpActive() {
        this.mActive.values().removeAll(Collections.singleton(null));
    }

    boolean containsActiveFragment(String string2) {
        return this.mActive.containsKey(string2);
    }

    void dispatchStateChange(int n) {
        for (Fragment fragment : this.mAdded) {
            FragmentStateManager object = this.mActive.get(fragment.mWho);
            if (object == null) continue;
            object.setFragmentManagerState(n);
        }
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            fragmentStateManager.setFragmentManagerState(n);
        }
    }

    void dump(String string2, FileDescriptor object, PrintWriter printWriter, String[] arrstring) {
        int n;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("    ");
        charSequence = charSequence.toString();
        if (!this.mActive.isEmpty()) {
            printWriter.print(string2);
            printWriter.print("Active Fragments:");
            Iterator<FragmentStateManager> iterator = this.mActive.values().iterator();
            while (iterator.hasNext()) {
                Object object2 = iterator.next();
                printWriter.print(string2);
                if (object2 != null) {
                    object2 = object2.getFragment();
                    printWriter.println(object2);
                    object2.dump((String)charSequence, (FileDescriptor)object, printWriter, arrstring);
                    continue;
                }
                printWriter.println("null");
            }
        }
        if ((n = this.mAdded.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Added Fragments:");
            for (int i = 0; i < n; ++i) {
                object = this.mAdded.get(i);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(object.toString());
            }
        }
    }

    Fragment findActiveFragment(String object) {
        if ((object = this.mActive.get(object)) != null) {
            return object.getFragment();
        }
        return null;
    }

    Fragment findFragmentById(int n) {
        Object object;
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            object = this.mAdded.get(i);
            if (object == null || object.mFragmentId != n) continue;
            return object;
        }
        object = this.mActive.values().iterator();
        while (object.hasNext()) {
            Object object2 = (FragmentStateManager)object.next();
            if (object2 == null) continue;
            object2 = object2.getFragment();
            if (object2.mFragmentId != n) continue;
            return object2;
        }
        return null;
    }

    Fragment findFragmentByTag(String string2) {
        Object object;
        if (string2 != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                object = this.mAdded.get(i);
                if (object == null || !string2.equals(object.mTag)) continue;
                return object;
            }
        }
        if (string2 != null) {
            object = this.mActive.values().iterator();
            while (object.hasNext()) {
                Object object2 = object.next();
                if (object2 == null) continue;
                object2 = object2.getFragment();
                if (!string2.equals(object2.mTag)) continue;
                return object2;
            }
        }
        return null;
    }

    Fragment findFragmentByWho(String string2) {
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            Fragment fragment;
            if (fragmentStateManager == null || (fragment = fragmentStateManager.getFragment().findFragmentByWho(string2)) == null) continue;
            return fragment;
        }
        return null;
    }

    Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (viewGroup != null) {
            if (view == null) {
                return null;
            }
            for (int i = this.mAdded.indexOf((Object)fragment) - 1; i >= 0; --i) {
                fragment = this.mAdded.get(i);
                if (fragment.mContainer != viewGroup || fragment.mView == null) continue;
                return fragment;
            }
            return null;
        }
        return null;
    }

    int getActiveFragmentCount() {
        return this.mActive.size();
    }

    List<Fragment> getActiveFragments() {
        ArrayList<Fragment> arrayList = new ArrayList<Fragment>();
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager.getFragment());
                continue;
            }
            arrayList.add(null);
        }
        return arrayList;
    }

    FragmentStateManager getFragmentStateManager(String string2) {
        return this.mActive.get(string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            return new ArrayList<Fragment>(this.mAdded);
        }
    }

    void makeActive(FragmentStateManager fragmentStateManager) {
        this.mActive.put(fragmentStateManager.getFragment().mWho, fragmentStateManager);
    }

    void makeInactive(FragmentStateManager object) {
        object = object.getFragment();
        Iterator<FragmentStateManager> iterator = this.mActive.values().iterator();
        while (iterator.hasNext()) {
            Object object2 = iterator.next();
            if (object2 == null) continue;
            object2 = object2.getFragment();
            if (!object.mWho.equals(object2.mTargetWho)) continue;
            object2.mTarget = object;
            object2.mTargetWho = null;
        }
        this.mActive.put(object.mWho, null);
        if (object.mTargetWho != null) {
            object.mTarget = this.findActiveFragment(object.mTargetWho);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void removeFragment(Fragment fragment) {
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            this.mAdded.remove(fragment);
        }
        fragment.mAdded = false;
    }

    void resetActiveFragments() {
        this.mActive.clear();
    }

    void restoreAddedFragments(List<String> object) {
        this.mAdded.clear();
        if (object != null) {
            Object object2 = object.iterator();
            while (object2.hasNext()) {
                object = object2.next();
                Fragment fragment = this.findActiveFragment((String)object);
                if (fragment != null) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("restoreSaveState: added (");
                        stringBuilder.append((String)object);
                        stringBuilder.append("): ");
                        stringBuilder.append(fragment);
                        Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                    }
                    this.addFragment(fragment);
                    continue;
                }
                object2 = new StringBuilder();
                object2.append("No instantiated fragment for (");
                object2.append((String)object);
                object2.append(")");
                throw new IllegalStateException(object2.toString());
            }
        }
    }

    ArrayList<FragmentState> saveActiveFragments() {
        ArrayList<FragmentState> arrayList = new ArrayList<FragmentState>(this.mActive.size());
        Iterator<FragmentStateManager> iterator = this.mActive.values().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object == null) continue;
            Fragment fragment = object.getFragment();
            object = object.saveState();
            arrayList.add((FragmentState)object);
            if (!FragmentManager.isLoggingEnabled(2)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Saved state of ");
            stringBuilder.append(fragment);
            stringBuilder.append(": ");
            stringBuilder.append((Object)object.mSavedFragmentState);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    ArrayList<String> saveAddedFragments() {
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            if (this.mAdded.isEmpty()) {
                return null;
            }
            ArrayList<String> arrayList2 = new ArrayList<String>(this.mAdded.size());
            Iterator<Fragment> iterator = this.mAdded.iterator();
            while (iterator.hasNext()) {
                Fragment fragment = iterator.next();
                arrayList2.add(fragment.mWho);
                if (!FragmentManager.isLoggingEnabled(2)) continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("saveAllState: adding fragment (");
                stringBuilder.append(fragment.mWho);
                stringBuilder.append("): ");
                stringBuilder.append(fragment);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            return arrayList2;
        }
    }
}

