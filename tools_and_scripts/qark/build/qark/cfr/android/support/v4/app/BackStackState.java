/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.SparseArray
 */
package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerImpl;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

final class BackStackState
implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>(){

        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        public BackStackState[] newArray(int n) {
            return new BackStackState[n];
        }
    };
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;

    /*
     * Enabled aggressive block sorting
     */
    public BackStackState(Parcel parcel) {
        this.mOps = parcel.createIntArray();
        this.mTransition = parcel.readInt();
        this.mTransitionStyle = parcel.readInt();
        this.mName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mBreadCrumbTitleRes = parcel.readInt();
        this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mBreadCrumbShortTitleRes = parcel.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSharedElementSourceNames = parcel.createStringArrayList();
        this.mSharedElementTargetNames = parcel.createStringArrayList();
        boolean bl = parcel.readInt() != 0;
        this.mReorderingAllowed = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BackStackState(BackStackRecord backStackRecord) {
        int n = backStackRecord.mOps.size();
        this.mOps = new int[n * 6];
        if (!backStackRecord.mAddToBackStack) {
            throw new IllegalStateException("Not on back stack");
        }
        int n2 = 0;
        int n3 = 0;
        do {
            if (n2 >= n) {
                this.mTransition = backStackRecord.mTransition;
                this.mTransitionStyle = backStackRecord.mTransitionStyle;
                this.mName = backStackRecord.mName;
                this.mIndex = backStackRecord.mIndex;
                this.mBreadCrumbTitleRes = backStackRecord.mBreadCrumbTitleRes;
                this.mBreadCrumbTitleText = backStackRecord.mBreadCrumbTitleText;
                this.mBreadCrumbShortTitleRes = backStackRecord.mBreadCrumbShortTitleRes;
                this.mBreadCrumbShortTitleText = backStackRecord.mBreadCrumbShortTitleText;
                this.mSharedElementSourceNames = backStackRecord.mSharedElementSourceNames;
                this.mSharedElementTargetNames = backStackRecord.mSharedElementTargetNames;
                this.mReorderingAllowed = backStackRecord.mReorderingAllowed;
                return;
            }
            BackStackRecord.Op op = backStackRecord.mOps.get(n2);
            int[] arrn = this.mOps;
            int n4 = n3 + 1;
            arrn[n3] = op.cmd;
            arrn = this.mOps;
            int n5 = n4 + 1;
            n3 = op.fragment != null ? op.fragment.mIndex : -1;
            arrn[n4] = n3;
            arrn = this.mOps;
            n3 = n5 + 1;
            arrn[n5] = op.enterAnim;
            arrn = this.mOps;
            n4 = n3 + 1;
            arrn[n3] = op.exitAnim;
            arrn = this.mOps;
            n5 = n4 + 1;
            arrn[n4] = op.popEnterAnim;
            arrn = this.mOps;
            n3 = n5 + 1;
            arrn[n5] = op.popExitAnim;
            ++n2;
        } while (true);
    }

    public int describeContents() {
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BackStackRecord instantiate(FragmentManagerImpl fragmentManagerImpl) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
        int n = 0;
        int n2 = 0;
        do {
            if (n >= this.mOps.length) {
                backStackRecord.mTransition = this.mTransition;
                backStackRecord.mTransitionStyle = this.mTransitionStyle;
                backStackRecord.mName = this.mName;
                backStackRecord.mIndex = this.mIndex;
                backStackRecord.mAddToBackStack = true;
                backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
                backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
                backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
                backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
                backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
                backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
                backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
                backStackRecord.bumpBackStackNesting(1);
                return backStackRecord;
            }
            BackStackRecord.Op op = new BackStackRecord.Op();
            int[] arrn = this.mOps;
            int n3 = n + 1;
            op.cmd = arrn[n];
            if (FragmentManagerImpl.DEBUG) {
                Log.v((String)"FragmentManager", (String)("Instantiate " + backStackRecord + " op #" + n2 + " base fragment #" + this.mOps[n3]));
            }
            arrn = this.mOps;
            n = n3 + 1;
            op.fragment = (n3 = arrn[n3]) >= 0 ? (Fragment)fragmentManagerImpl.mActive.get(n3) : null;
            arrn = this.mOps;
            n3 = n + 1;
            op.enterAnim = arrn[n];
            arrn = this.mOps;
            n = n3 + 1;
            op.exitAnim = arrn[n3];
            arrn = this.mOps;
            n3 = n + 1;
            op.popEnterAnim = arrn[n];
            arrn = this.mOps;
            n = n3 + 1;
            op.popExitAnim = arrn[n3];
            backStackRecord.mEnterAnim = op.enterAnim;
            backStackRecord.mExitAnim = op.exitAnim;
            backStackRecord.mPopEnterAnim = op.popEnterAnim;
            backStackRecord.mPopExitAnim = op.popExitAnim;
            backStackRecord.addOp(op);
            ++n2;
        } while (true);
    }

    public void writeToParcel(Parcel parcel, int n) {
        n = 0;
        parcel.writeIntArray(this.mOps);
        parcel.writeInt(this.mTransition);
        parcel.writeInt(this.mTransitionStyle);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbTitleText, (Parcel)parcel, (int)0);
        parcel.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbShortTitleText, (Parcel)parcel, (int)0);
        parcel.writeStringList(this.mSharedElementSourceNames);
        parcel.writeStringList(this.mSharedElementTargetNames);
        if (this.mReorderingAllowed) {
            n = 1;
        }
        parcel.writeInt(n);
    }

}

