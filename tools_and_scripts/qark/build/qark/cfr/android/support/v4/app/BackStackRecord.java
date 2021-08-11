/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.View
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord
extends FragmentTransaction
implements FragmentManager.BackStackEntry,
FragmentManagerImpl.OpGenerator {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SET_PRIMARY_NAV = 8;
    static final int OP_SHOW = 5;
    static final int OP_UNSET_PRIMARY_NAV = 9;
    static final boolean SUPPORTS_TRANSITIONS;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    int mIndex = -1;
    final FragmentManagerImpl mManager;
    String mName;
    ArrayList<Op> mOps = new ArrayList();
    int mPopEnterAnim;
    int mPopExitAnim;
    boolean mReorderingAllowed = false;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        SUPPORTS_TRANSITIONS = bl;
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        this.mManager = fragmentManagerImpl;
    }

    private void doAddOp(int n, Fragment object, String charSequence, int n2) {
        Class class_ = object.getClass();
        int n3 = class_.getModifiers();
        if (!class_.isAnonymousClass() && Modifier.isPublic(n3) && (!class_.isMemberClass() || Modifier.isStatic(n3))) {
            object.mFragmentManager = this.mManager;
            if (charSequence != null) {
                if (object.mTag != null && !charSequence.equals(object.mTag)) {
                    class_ = new StringBuilder();
                    class_.append("Can't change tag of fragment ");
                    class_.append(object);
                    class_.append(": was ");
                    class_.append(object.mTag);
                    class_.append(" now ");
                    class_.append((String)charSequence);
                    throw new IllegalStateException(class_.toString());
                }
                object.mTag = charSequence;
            }
            if (n != 0) {
                if (n != -1) {
                    if (object.mFragmentId != 0 && object.mFragmentId != n) {
                        charSequence = new StringBuilder();
                        charSequence.append("Can't change container ID of fragment ");
                        charSequence.append(object);
                        charSequence.append(": was ");
                        charSequence.append(object.mFragmentId);
                        charSequence.append(" now ");
                        charSequence.append(n);
                        throw new IllegalStateException(charSequence.toString());
                    }
                    object.mFragmentId = n;
                    object.mContainerId = n;
                } else {
                    class_ = new StringBuilder();
                    class_.append("Can't add fragment ");
                    class_.append(object);
                    class_.append(" with tag ");
                    class_.append((String)charSequence);
                    class_.append(" to container view with no id");
                    throw new IllegalArgumentException(class_.toString());
                }
            }
            this.addOp(new Op(n2, (Fragment)object));
            return;
        }
        object = new StringBuilder();
        object.append("Fragment ");
        object.append(class_.getCanonicalName());
        object.append(" must be a public static class to be  properly recreated from");
        object.append(" instance state.");
        throw new IllegalStateException(object.toString());
    }

    private static boolean isFragmentPostponed(Op object) {
        object = object.fragment;
        if (object != null && object.mAdded && object.mView != null && !object.mDetached && !object.mHidden && object.isPostponed()) {
            return true;
        }
        return false;
    }

    @Override
    public FragmentTransaction add(int n, Fragment fragment) {
        this.doAddOp(n, fragment, null, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(int n, Fragment fragment, String string2) {
        this.doAddOp(n, fragment, string2, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(Fragment fragment, String string2) {
        this.doAddOp(0, fragment, string2, 1);
        return this;
    }

    void addOp(Op op) {
        this.mOps.add(op);
        op.enterAnim = this.mEnterAnim;
        op.exitAnim = this.mExitAnim;
        op.popEnterAnim = this.mPopEnterAnim;
        op.popExitAnim = this.mPopExitAnim;
    }

    @Override
    public FragmentTransaction addSharedElement(View object, String charSequence) {
        block2 : {
            block3 : {
                block6 : {
                    block7 : {
                        block5 : {
                            block4 : {
                                if (!SUPPORTS_TRANSITIONS) break block2;
                                if ((object = ViewCompat.getTransitionName((View)object)) == null) break block3;
                                if (this.mSharedElementSourceNames != null) break block4;
                                this.mSharedElementSourceNames = new ArrayList();
                                this.mSharedElementTargetNames = new ArrayList();
                                break block5;
                            }
                            if (this.mSharedElementTargetNames.contains(charSequence)) break block6;
                            if (this.mSharedElementSourceNames.contains(object)) break block7;
                        }
                        this.mSharedElementSourceNames.add((String)object);
                        this.mSharedElementTargetNames.add((String)charSequence);
                        return this;
                    }
                    charSequence = new StringBuilder();
                    charSequence.append("A shared element with the source name '");
                    charSequence.append((String)object);
                    charSequence.append(" has already been added to the transaction.");
                    throw new IllegalArgumentException(charSequence.toString());
                }
                object = new StringBuilder();
                object.append("A shared element with the target name '");
                object.append((String)charSequence);
                object.append("' has already been added to the transaction.");
                throw new IllegalArgumentException(object.toString());
            }
            throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
        }
        return this;
    }

    @Override
    public FragmentTransaction addToBackStack(String string2) {
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = string2;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }

    @Override
    public FragmentTransaction attach(Fragment fragment) {
        this.addOp(new Op(7, fragment));
        return this;
    }

    void bumpBackStackNesting(int n) {
        Object object;
        if (!this.mAddToBackStack) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            object = new StringBuilder();
            object.append("Bump nesting in ");
            object.append(this);
            object.append(" by ");
            object.append(n);
            Log.v((String)"FragmentManager", (String)object.toString());
        }
        int n2 = this.mOps.size();
        for (int i = 0; i < n2; ++i) {
            object = this.mOps.get(i);
            if (object.fragment == null) continue;
            Object object2 = object.fragment;
            object2.mBackStackNesting += n;
            if (!FragmentManagerImpl.DEBUG) continue;
            object2 = new StringBuilder();
            object2.append("Bump nesting of ");
            object2.append(object.fragment);
            object2.append(" to ");
            object2.append(object.fragment.mBackStackNesting);
            Log.v((String)"FragmentManager", (String)object2.toString());
        }
    }

    @Override
    public int commit() {
        return this.commitInternal(false);
    }

    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }

    int commitInternal(boolean bl) {
        if (!this.mCommitted) {
            if (FragmentManagerImpl.DEBUG) {
                Appendable appendable = new StringBuilder();
                appendable.append("Commit: ");
                appendable.append(this);
                Log.v((String)"FragmentManager", (String)appendable.toString());
                appendable = new PrintWriter(new LogWriter("FragmentManager"));
                this.dump("  ", null, (PrintWriter)appendable, null);
                appendable.close();
            }
            this.mCommitted = true;
            this.mIndex = this.mAddToBackStack ? this.mManager.allocBackStackIndex(this) : -1;
            this.mManager.enqueueAction(this, bl);
            return this.mIndex;
        }
        throw new IllegalStateException("commit already called");
    }

    @Override
    public void commitNow() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    @Override
    public void commitNowAllowingStateLoss() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        this.addOp(new Op(6, fragment));
        return this;
    }

    @Override
    public FragmentTransaction disallowAddToBackStack() {
        if (!this.mAddToBackStack) {
            this.mAllowAddToBackStack = false;
            return this;
        }
        throw new IllegalStateException("This transaction is already being added to the back stack");
    }

    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        this.dump(string2, printWriter, true);
    }

    public void dump(String string2, PrintWriter printWriter, boolean bl) {
        if (bl) {
            printWriter.print(string2);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(string2);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(string2);
            printWriter.println("Operations:");
            CharSequence charSequence = new StringBuilder();
            charSequence.append(string2);
            charSequence.append("    ");
            charSequence.toString();
            int n = this.mOps.size();
            for (int i = 0; i < n; ++i) {
                Op op = this.mOps.get(i);
                switch (op.cmd) {
                    default: {
                        charSequence = new StringBuilder();
                        charSequence.append("cmd=");
                        charSequence.append(op.cmd);
                        charSequence = charSequence.toString();
                        break;
                    }
                    case 9: {
                        charSequence = "UNSET_PRIMARY_NAV";
                        break;
                    }
                    case 8: {
                        charSequence = "SET_PRIMARY_NAV";
                        break;
                    }
                    case 7: {
                        charSequence = "ATTACH";
                        break;
                    }
                    case 6: {
                        charSequence = "DETACH";
                        break;
                    }
                    case 5: {
                        charSequence = "SHOW";
                        break;
                    }
                    case 4: {
                        charSequence = "HIDE";
                        break;
                    }
                    case 3: {
                        charSequence = "REMOVE";
                        break;
                    }
                    case 2: {
                        charSequence = "REPLACE";
                        break;
                    }
                    case 1: {
                        charSequence = "ADD";
                        break;
                    }
                    case 0: {
                        charSequence = "NULL";
                    }
                }
                printWriter.print(string2);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print((String)charSequence);
                printWriter.print(" ");
                printWriter.println(op.fragment);
                if (!bl) continue;
                if (op.enterAnim != 0 || op.exitAnim != 0) {
                    printWriter.print(string2);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(op.enterAnim));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(op.exitAnim));
                }
                if (op.popEnterAnim == 0 && op.popExitAnim == 0) continue;
                printWriter.print(string2);
                printWriter.print("popEnterAnim=#");
                printWriter.print(Integer.toHexString(op.popEnterAnim));
                printWriter.print(" popExitAnim=#");
                printWriter.println(Integer.toHexString(op.popExitAnim));
            }
            return;
        }
    }

    void executeOps() {
        Object object;
        int n = this.mOps.size();
        for (int i = 0; i < n; ++i) {
            int n2;
            object = this.mOps.get(i);
            Object object2 = object.fragment;
            if (object2 != null) {
                object2.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            if ((n2 = object.cmd) != 1) {
                switch (n2) {
                    default: {
                        object2 = new StringBuilder();
                        object2.append("Unknown cmd: ");
                        object2.append(object.cmd);
                        throw new IllegalArgumentException(object2.toString());
                    }
                    case 9: {
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    }
                    case 8: {
                        this.mManager.setPrimaryNavigationFragment((Fragment)object2);
                        break;
                    }
                    case 7: {
                        object2.setNextAnim(object.enterAnim);
                        this.mManager.attachFragment((Fragment)object2);
                        break;
                    }
                    case 6: {
                        object2.setNextAnim(object.exitAnim);
                        this.mManager.detachFragment((Fragment)object2);
                        break;
                    }
                    case 5: {
                        object2.setNextAnim(object.enterAnim);
                        this.mManager.showFragment((Fragment)object2);
                        break;
                    }
                    case 4: {
                        object2.setNextAnim(object.exitAnim);
                        this.mManager.hideFragment((Fragment)object2);
                        break;
                    }
                    case 3: {
                        object2.setNextAnim(object.exitAnim);
                        this.mManager.removeFragment((Fragment)object2);
                        break;
                    }
                }
            } else {
                object2.setNextAnim(object.enterAnim);
                this.mManager.addFragment((Fragment)object2, false);
            }
            if (this.mReorderingAllowed || object.cmd == 1 || object2 == null) continue;
            this.mManager.moveFragmentToExpectedState((Fragment)object2);
        }
        if (!this.mReorderingAllowed) {
            object = this.mManager;
            object.moveToState(object.mCurState, true);
            return;
        }
    }

    void executePopOps(boolean bl) {
        Object object;
        for (int i = this.mOps.size() - 1; i >= 0; --i) {
            int n;
            object = this.mOps.get(i);
            Object object2 = object.fragment;
            if (object2 != null) {
                object2.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            if ((n = object.cmd) != 1) {
                switch (n) {
                    default: {
                        object2 = new StringBuilder();
                        object2.append("Unknown cmd: ");
                        object2.append(object.cmd);
                        throw new IllegalArgumentException(object2.toString());
                    }
                    case 9: {
                        this.mManager.setPrimaryNavigationFragment((Fragment)object2);
                        break;
                    }
                    case 8: {
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    }
                    case 7: {
                        object2.setNextAnim(object.popExitAnim);
                        this.mManager.detachFragment((Fragment)object2);
                        break;
                    }
                    case 6: {
                        object2.setNextAnim(object.popEnterAnim);
                        this.mManager.attachFragment((Fragment)object2);
                        break;
                    }
                    case 5: {
                        object2.setNextAnim(object.popExitAnim);
                        this.mManager.hideFragment((Fragment)object2);
                        break;
                    }
                    case 4: {
                        object2.setNextAnim(object.popEnterAnim);
                        this.mManager.showFragment((Fragment)object2);
                        break;
                    }
                    case 3: {
                        object2.setNextAnim(object.popEnterAnim);
                        this.mManager.addFragment((Fragment)object2, false);
                        break;
                    }
                }
            } else {
                object2.setNextAnim(object.popExitAnim);
                this.mManager.removeFragment((Fragment)object2);
            }
            if (this.mReorderingAllowed || object.cmd == 3 || object2 == null) continue;
            this.mManager.moveFragmentToExpectedState((Fragment)object2);
        }
        if (!this.mReorderingAllowed && bl) {
            object = this.mManager;
            object.moveToState(object.mCurState, true);
            return;
        }
    }

    Fragment expandOps(ArrayList<Fragment> arrayList, Fragment fragment) {
        block6 : for (int i = 0; i < this.mOps.size(); ++i) {
            Op op = this.mOps.get(i);
            switch (op.cmd) {
                default: {
                    continue block6;
                }
                case 8: {
                    this.mOps.add(i, new Op(9, fragment));
                    ++i;
                    fragment = op.fragment;
                    continue block6;
                }
                case 3: 
                case 6: {
                    arrayList.remove(op.fragment);
                    if (op.fragment != fragment) continue block6;
                    this.mOps.add(i, new Op(9, op.fragment));
                    ++i;
                    fragment = null;
                    continue block6;
                }
                case 2: {
                    Fragment fragment2 = op.fragment;
                    int n = fragment2.mContainerId;
                    boolean bl = false;
                    for (int j = arrayList.size() - 1; j >= 0; --j) {
                        Fragment fragment3 = arrayList.get(j);
                        if (fragment3.mContainerId != n) continue;
                        if (fragment3 == fragment2) {
                            bl = true;
                            continue;
                        }
                        if (fragment3 == fragment) {
                            this.mOps.add(i, new Op(9, fragment3));
                            ++i;
                            fragment = null;
                        }
                        Op op2 = new Op(3, fragment3);
                        op2.enterAnim = op.enterAnim;
                        op2.popEnterAnim = op.popEnterAnim;
                        op2.exitAnim = op.exitAnim;
                        op2.popExitAnim = op.popExitAnim;
                        this.mOps.add(i, op2);
                        arrayList.remove(fragment3);
                        ++i;
                    }
                    if (bl) {
                        this.mOps.remove(i);
                        --i;
                        continue block6;
                    }
                    op.cmd = 1;
                    arrayList.add(fragment2);
                    continue block6;
                }
                case 1: 
                case 7: {
                    arrayList.add(op.fragment);
                }
            }
        }
        return fragment;
    }

    @Override
    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManagerImpl.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Run: ");
            stringBuilder.append(this);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        arrayList.add(this);
        arrayList2.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }

    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    @Override
    public int getId() {
        return this.mIndex;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    @Override
    public FragmentTransaction hide(Fragment fragment) {
        this.addOp(new Op(4, fragment));
        return this;
    }

    boolean interactsWith(int n) {
        int n2 = this.mOps.size();
        int n3 = 0;
        do {
            int n4 = 0;
            if (n3 >= n2) break;
            Op op = this.mOps.get(n3);
            if (op.fragment != null) {
                n4 = op.fragment.mContainerId;
            }
            if (n4 != 0 && n4 == n) {
                return true;
            }
            ++n3;
        } while (true);
        return false;
    }

    boolean interactsWith(ArrayList<BackStackRecord> arrayList, int n, int n2) {
        if (n2 == n) {
            return false;
        }
        int n3 = this.mOps.size();
        int n4 = -1;
        for (int i = 0; i < n3; ++i) {
            Object object = this.mOps.get(i);
            int n5 = object.fragment != null ? object.fragment.mContainerId : 0;
            if (n5 == 0 || n5 == n4) continue;
            n4 = n5;
            for (int j = n; j < n2; ++j) {
                object = arrayList.get(j);
                int n6 = object.mOps.size();
                for (int k = 0; k < n6; ++k) {
                    Op op = object.mOps.get(k);
                    int n7 = op.fragment != null ? op.fragment.mContainerId : 0;
                    if (n7 != n5) continue;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }

    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); ++i) {
            if (!BackStackRecord.isFragmentPostponed(this.mOps.get(i))) continue;
            return true;
        }
        return false;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        this.addOp(new Op(3, fragment));
        return this;
    }

    @Override
    public FragmentTransaction replace(int n, Fragment fragment) {
        return this.replace(n, fragment, null);
    }

    @Override
    public FragmentTransaction replace(int n, Fragment fragment, String string2) {
        if (n != 0) {
            this.doAddOp(n, fragment, string2, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }

    @Override
    public FragmentTransaction runOnCommit(Runnable runnable) {
        if (runnable != null) {
            this.disallowAddToBackStack();
            if (this.mCommitRunnables == null) {
                this.mCommitRunnables = new ArrayList();
            }
            this.mCommitRunnables.add(runnable);
            return this;
        }
        throw new IllegalArgumentException("runnable cannot be null");
    }

    public void runOnCommitRunnables() {
        ArrayList<Runnable> arrayList = this.mCommitRunnables;
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
            return;
        }
    }

    @Override
    public FragmentTransaction setAllowOptimization(boolean bl) {
        return this.setReorderingAllowed(bl);
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(int n) {
        this.mBreadCrumbShortTitleRes = n;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = charSequence;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(int n) {
        this.mBreadCrumbTitleRes = n;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = charSequence;
        return this;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int n, int n2) {
        return this.setCustomAnimations(n, n2, 0, 0);
    }

    @Override
    public FragmentTransaction setCustomAnimations(int n, int n2, int n3, int n4) {
        this.mEnterAnim = n;
        this.mExitAnim = n2;
        this.mPopEnterAnim = n3;
        this.mPopExitAnim = n4;
        return this;
    }

    void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            Op op = this.mOps.get(i);
            if (!BackStackRecord.isFragmentPostponed(op)) continue;
            op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
        }
    }

    @Override
    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        this.addOp(new Op(8, fragment));
        return this;
    }

    @Override
    public FragmentTransaction setReorderingAllowed(boolean bl) {
        this.mReorderingAllowed = bl;
        return this;
    }

    @Override
    public FragmentTransaction setTransition(int n) {
        this.mTransition = n;
        return this;
    }

    @Override
    public FragmentTransaction setTransitionStyle(int n) {
        this.mTransitionStyle = n;
        return this;
    }

    @Override
    public FragmentTransaction show(Fragment fragment) {
        this.addOp(new Op(5, fragment));
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("BackStackEntry{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            stringBuilder.append(" #");
            stringBuilder.append(this.mIndex);
        }
        if (this.mName != null) {
            stringBuilder.append(" ");
            stringBuilder.append(this.mName);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    Fragment trackAddedFragmentsInPop(ArrayList<Fragment> var1_1, Fragment var2_2) {
        var3_3 = 0;
        while (var3_3 < this.mOps.size()) {
            block7 : {
                var5_5 = this.mOps.get(var3_3);
                var4_4 = var5_5.cmd;
                if (var4_4 == 1) break block7;
                if (var4_4 == 3) ** GOTO lbl-1000
                switch (var4_4) {
                    default: {
                        ** break;
                    }
                    case 9: {
                        var2_2 = var5_5.fragment;
                        ** break;
                    }
                    case 8: {
                        var2_2 = null;
                        ** break;
                    }
                    case 6: lbl-1000: // 2 sources:
                    {
                        var1_1.add(var5_5.fragment);
                        ** break;
                    }
                    case 7: 
                }
            }
            var1_1.remove(var5_5.fragment);
lbl22: // 5 sources:
            ++var3_3;
        }
        return var2_2;
    }

    static final class Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;

        Op() {
        }

        Op(int n, Fragment fragment) {
            this.cmd = n;
            this.fragment = fragment;
        }
    }

}

