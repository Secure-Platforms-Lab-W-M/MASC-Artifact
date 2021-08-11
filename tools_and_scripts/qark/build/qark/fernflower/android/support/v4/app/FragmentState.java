package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public FragmentState createFromParcel(Parcel var1) {
         return new FragmentState(var1);
      }

      public FragmentState[] newArray(int var1) {
         return new FragmentState[var1];
      }
   };
   final Bundle mArguments;
   final String mClassName;
   final int mContainerId;
   final boolean mDetached;
   final int mFragmentId;
   final boolean mFromLayout;
   final boolean mHidden;
   final int mIndex;
   Fragment mInstance;
   final boolean mRetainInstance;
   Bundle mSavedFragmentState;
   final String mTag;

   public FragmentState(Parcel var1) {
      this.mClassName = var1.readString();
      this.mIndex = var1.readInt();
      int var2 = var1.readInt();
      boolean var4 = true;
      boolean var3;
      if (var2 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mFromLayout = var3;
      this.mFragmentId = var1.readInt();
      this.mContainerId = var1.readInt();
      this.mTag = var1.readString();
      if (var1.readInt() != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mRetainInstance = var3;
      if (var1.readInt() != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mDetached = var3;
      this.mArguments = var1.readBundle();
      if (var1.readInt() != 0) {
         var3 = var4;
      } else {
         var3 = false;
      }

      this.mHidden = var3;
      this.mSavedFragmentState = var1.readBundle();
   }

   public FragmentState(Fragment var1) {
      this.mClassName = var1.getClass().getName();
      this.mIndex = var1.mIndex;
      this.mFromLayout = var1.mFromLayout;
      this.mFragmentId = var1.mFragmentId;
      this.mContainerId = var1.mContainerId;
      this.mTag = var1.mTag;
      this.mRetainInstance = var1.mRetainInstance;
      this.mDetached = var1.mDetached;
      this.mArguments = var1.mArguments;
      this.mHidden = var1.mHidden;
   }

   public int describeContents() {
      return 0;
   }

   public Fragment instantiate(FragmentHostCallback var1, FragmentContainer var2, Fragment var3, FragmentManagerNonConfig var4) {
      if (this.mInstance == null) {
         Context var5 = var1.getContext();
         Bundle var6 = this.mArguments;
         if (var6 != null) {
            var6.setClassLoader(var5.getClassLoader());
         }

         if (var2 != null) {
            this.mInstance = var2.instantiate(var5, this.mClassName, this.mArguments);
         } else {
            this.mInstance = Fragment.instantiate(var5, this.mClassName, this.mArguments);
         }

         Bundle var9 = this.mSavedFragmentState;
         if (var9 != null) {
            var9.setClassLoader(var5.getClassLoader());
            this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
         }

         this.mInstance.setIndex(this.mIndex, var3);
         Fragment var10 = this.mInstance;
         var10.mFromLayout = this.mFromLayout;
         var10.mRestored = true;
         var10.mFragmentId = this.mFragmentId;
         var10.mContainerId = this.mContainerId;
         var10.mTag = this.mTag;
         var10.mRetainInstance = this.mRetainInstance;
         var10.mDetached = this.mDetached;
         var10.mHidden = this.mHidden;
         var10.mFragmentManager = var1.mFragmentManager;
         if (FragmentManagerImpl.DEBUG) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Instantiated fragment ");
            var7.append(this.mInstance);
            Log.v("FragmentManager", var7.toString());
         }
      }

      Fragment var8 = this.mInstance;
      var8.mChildNonConfig = var4;
      return var8;
   }

   public void writeToParcel(Parcel var1, int var2) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }
}
