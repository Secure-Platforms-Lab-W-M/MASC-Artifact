package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public BackStackState createFromParcel(Parcel var1) {
         return new BackStackState(var1);
      }

      public BackStackState[] newArray(int var1) {
         return new BackStackState[var1];
      }
   };
   private static final String TAG = "FragmentManager";
   final int mBreadCrumbShortTitleRes;
   final CharSequence mBreadCrumbShortTitleText;
   final int mBreadCrumbTitleRes;
   final CharSequence mBreadCrumbTitleText;
   final int[] mCurrentMaxLifecycleStates;
   final ArrayList mFragmentWhos;
   final int mIndex;
   final String mName;
   final int[] mOldMaxLifecycleStates;
   final int[] mOps;
   final boolean mReorderingAllowed;
   final ArrayList mSharedElementSourceNames;
   final ArrayList mSharedElementTargetNames;
   final int mTransition;

   public BackStackState(Parcel var1) {
      this.mOps = var1.createIntArray();
      this.mFragmentWhos = var1.createStringArrayList();
      this.mOldMaxLifecycleStates = var1.createIntArray();
      this.mCurrentMaxLifecycleStates = var1.createIntArray();
      this.mTransition = var1.readInt();
      this.mName = var1.readString();
      this.mIndex = var1.readInt();
      this.mBreadCrumbTitleRes = var1.readInt();
      this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mBreadCrumbShortTitleRes = var1.readInt();
      this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mSharedElementSourceNames = var1.createStringArrayList();
      this.mSharedElementTargetNames = var1.createStringArrayList();
      boolean var2;
      if (var1.readInt() != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mReorderingAllowed = var2;
   }

   public BackStackState(BackStackRecord var1) {
      int var4 = var1.mOps.size();
      this.mOps = new int[var4 * 5];
      if (var1.mAddToBackStack) {
         this.mFragmentWhos = new ArrayList(var4);
         this.mOldMaxLifecycleStates = new int[var4];
         this.mCurrentMaxLifecycleStates = new int[var4];
         int var3 = 0;

         for(int var2 = 0; var2 < var4; ++var3) {
            FragmentTransaction.class_11 var7 = (FragmentTransaction.class_11)var1.mOps.get(var2);
            int[] var6 = this.mOps;
            int var5 = var3 + 1;
            var6[var3] = var7.mCmd;
            ArrayList var8 = this.mFragmentWhos;
            String var9;
            if (var7.mFragment != null) {
               var9 = var7.mFragment.mWho;
            } else {
               var9 = null;
            }

            var8.add(var9);
            var6 = this.mOps;
            var3 = var5 + 1;
            var6[var5] = var7.mEnterAnim;
            var6 = this.mOps;
            var5 = var3 + 1;
            var6[var3] = var7.mExitAnim;
            var6 = this.mOps;
            var3 = var5 + 1;
            var6[var5] = var7.mPopEnterAnim;
            this.mOps[var3] = var7.mPopExitAnim;
            this.mOldMaxLifecycleStates[var2] = var7.mOldMaxState.ordinal();
            this.mCurrentMaxLifecycleStates[var2] = var7.mCurrentMaxState.ordinal();
            ++var2;
         }

         this.mTransition = var1.mTransition;
         this.mName = var1.mName;
         this.mIndex = var1.mIndex;
         this.mBreadCrumbTitleRes = var1.mBreadCrumbTitleRes;
         this.mBreadCrumbTitleText = var1.mBreadCrumbTitleText;
         this.mBreadCrumbShortTitleRes = var1.mBreadCrumbShortTitleRes;
         this.mBreadCrumbShortTitleText = var1.mBreadCrumbShortTitleText;
         this.mSharedElementSourceNames = var1.mSharedElementSourceNames;
         this.mSharedElementTargetNames = var1.mSharedElementTargetNames;
         this.mReorderingAllowed = var1.mReorderingAllowed;
      } else {
         throw new IllegalStateException("Not on back stack");
      }
   }

   public int describeContents() {
      return 0;
   }

   public BackStackRecord instantiate(FragmentManager var1) {
      BackStackRecord var5 = new BackStackRecord(var1);
      int var3 = 0;

      for(int var2 = 0; var3 < this.mOps.length; ++var3) {
         FragmentTransaction.class_11 var6 = new FragmentTransaction.class_11();
         int[] var7 = this.mOps;
         int var4 = var3 + 1;
         var6.mCmd = var7[var3];
         if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Instantiate ");
            var8.append(var5);
            var8.append(" op #");
            var8.append(var2);
            var8.append(" base fragment #");
            var8.append(this.mOps[var4]);
            Log.v("FragmentManager", var8.toString());
         }

         String var9 = (String)this.mFragmentWhos.get(var2);
         if (var9 != null) {
            var6.mFragment = var1.findActiveFragment(var9);
         } else {
            var6.mFragment = null;
         }

         var6.mOldMaxState = Lifecycle.State.values()[this.mOldMaxLifecycleStates[var2]];
         var6.mCurrentMaxState = Lifecycle.State.values()[this.mCurrentMaxLifecycleStates[var2]];
         var7 = this.mOps;
         var3 = var4 + 1;
         var6.mEnterAnim = var7[var4];
         var7 = this.mOps;
         var4 = var3 + 1;
         var6.mExitAnim = var7[var3];
         var7 = this.mOps;
         var3 = var4 + 1;
         var6.mPopEnterAnim = var7[var4];
         var6.mPopExitAnim = this.mOps[var3];
         var5.mEnterAnim = var6.mEnterAnim;
         var5.mExitAnim = var6.mExitAnim;
         var5.mPopEnterAnim = var6.mPopEnterAnim;
         var5.mPopExitAnim = var6.mPopExitAnim;
         var5.addOp(var6);
         ++var2;
      }

      var5.mTransition = this.mTransition;
      var5.mName = this.mName;
      var5.mIndex = this.mIndex;
      var5.mAddToBackStack = true;
      var5.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
      var5.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
      var5.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
      var5.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
      var5.mSharedElementSourceNames = this.mSharedElementSourceNames;
      var5.mSharedElementTargetNames = this.mSharedElementTargetNames;
      var5.mReorderingAllowed = this.mReorderingAllowed;
      var5.bumpBackStackNesting(1);
      return var5;
   }

   public void writeToParcel(Parcel var1, int var2) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }
}
