package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
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
   final int mBreadCrumbShortTitleRes;
   final CharSequence mBreadCrumbShortTitleText;
   final int mBreadCrumbTitleRes;
   final CharSequence mBreadCrumbTitleText;
   final int mIndex;
   final String mName;
   final int[] mOps;
   final boolean mReorderingAllowed;
   final ArrayList mSharedElementSourceNames;
   final ArrayList mSharedElementTargetNames;
   final int mTransition;
   final int mTransitionStyle;

   public BackStackState(Parcel var1) {
      this.mOps = var1.createIntArray();
      this.mTransition = var1.readInt();
      this.mTransitionStyle = var1.readInt();
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
      this.mOps = new int[var4 * 6];
      if (var1.mAddToBackStack) {
         int var3 = 0;

         for(int var2 = 0; var2 < var4; ++var3) {
            BackStackRecord.class_20 var7 = (BackStackRecord.class_20)var1.mOps.get(var2);
            int[] var8 = this.mOps;
            int var5 = var3 + 1;
            var8[var3] = var7.cmd;
            var8 = this.mOps;
            int var6 = var5 + 1;
            if (var7.fragment != null) {
               var3 = var7.fragment.mIndex;
            } else {
               var3 = -1;
            }

            var8[var5] = var3;
            var8 = this.mOps;
            var3 = var6 + 1;
            var8[var6] = var7.enterAnim;
            var8 = this.mOps;
            var5 = var3 + 1;
            var8[var3] = var7.exitAnim;
            var8 = this.mOps;
            var3 = var5 + 1;
            var8[var5] = var7.popEnterAnim;
            this.mOps[var3] = var7.popExitAnim;
            ++var2;
         }

         this.mTransition = var1.mTransition;
         this.mTransitionStyle = var1.mTransitionStyle;
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
         IllegalStateException var9 = new IllegalStateException("Not on back stack");
         throw var9;
      }
   }

   public int describeContents() {
      return 0;
   }

   public BackStackRecord instantiate(FragmentManagerImpl var1) {
      BackStackRecord var5 = new BackStackRecord(var1);
      int var3 = 0;

      int var4;
      for(int var2 = 0; var3 < this.mOps.length; var3 = var4 + 1) {
         BackStackRecord.class_20 var6 = new BackStackRecord.class_20();
         int[] var7 = this.mOps;
         var4 = var3 + 1;
         var6.cmd = var7[var3];
         if (FragmentManagerImpl.DEBUG) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Instantiate ");
            var8.append(var5);
            var8.append(" op #");
            var8.append(var2);
            var8.append(" base fragment #");
            var8.append(this.mOps[var4]);
            Log.v("FragmentManager", var8.toString());
         }

         var7 = this.mOps;
         var3 = var4 + 1;
         var4 = var7[var4];
         if (var4 >= 0) {
            var6.fragment = (Fragment)var1.mActive.get(var4);
         } else {
            var6.fragment = null;
         }

         var7 = this.mOps;
         var4 = var3 + 1;
         var6.enterAnim = var7[var3];
         var3 = var4 + 1;
         var6.exitAnim = var7[var4];
         var4 = var3 + 1;
         var6.popEnterAnim = var7[var3];
         var6.popExitAnim = var7[var4];
         var5.mEnterAnim = var6.enterAnim;
         var5.mExitAnim = var6.exitAnim;
         var5.mPopEnterAnim = var6.popEnterAnim;
         var5.mPopExitAnim = var6.popExitAnim;
         var5.addOp(var6);
         ++var2;
      }

      var5.mTransition = this.mTransition;
      var5.mTransitionStyle = this.mTransitionStyle;
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
