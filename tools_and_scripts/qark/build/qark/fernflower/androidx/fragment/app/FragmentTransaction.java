package androidx.fragment.app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public abstract class FragmentTransaction {
   static final int OP_ADD = 1;
   static final int OP_ATTACH = 7;
   static final int OP_DETACH = 6;
   static final int OP_HIDE = 4;
   static final int OP_NULL = 0;
   static final int OP_REMOVE = 3;
   static final int OP_REPLACE = 2;
   static final int OP_SET_MAX_LIFECYCLE = 10;
   static final int OP_SET_PRIMARY_NAV = 8;
   static final int OP_SHOW = 5;
   static final int OP_UNSET_PRIMARY_NAV = 9;
   public static final int TRANSIT_ENTER_MASK = 4096;
   public static final int TRANSIT_EXIT_MASK = 8192;
   public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
   public static final int TRANSIT_FRAGMENT_FADE = 4099;
   public static final int TRANSIT_FRAGMENT_OPEN = 4097;
   public static final int TRANSIT_NONE = 0;
   public static final int TRANSIT_UNSET = -1;
   boolean mAddToBackStack;
   boolean mAllowAddToBackStack = true;
   int mBreadCrumbShortTitleRes;
   CharSequence mBreadCrumbShortTitleText;
   int mBreadCrumbTitleRes;
   CharSequence mBreadCrumbTitleText;
   private final ClassLoader mClassLoader;
   ArrayList mCommitRunnables;
   int mEnterAnim;
   int mExitAnim;
   private final FragmentFactory mFragmentFactory;
   String mName;
   ArrayList mOps = new ArrayList();
   int mPopEnterAnim;
   int mPopExitAnim;
   boolean mReorderingAllowed = false;
   ArrayList mSharedElementSourceNames;
   ArrayList mSharedElementTargetNames;
   int mTransition;

   @Deprecated
   public FragmentTransaction() {
      this.mFragmentFactory = null;
      this.mClassLoader = null;
   }

   FragmentTransaction(FragmentFactory var1, ClassLoader var2) {
      this.mFragmentFactory = var1;
      this.mClassLoader = var2;
   }

   private Fragment createFragment(Class var1, Bundle var2) {
      FragmentFactory var3 = this.mFragmentFactory;
      if (var3 != null) {
         ClassLoader var4 = this.mClassLoader;
         if (var4 != null) {
            Fragment var5 = var3.instantiate(var4, var1.getName());
            if (var2 != null) {
               var5.setArguments(var2);
            }

            return var5;
         } else {
            throw new IllegalStateException("The FragmentManager must be attached to itshost to create a Fragment");
         }
      } else {
         throw new IllegalStateException("Creating a Fragment requires that this FragmentTransaction was built with FragmentManager.beginTransaction()");
      }
   }

   public FragmentTransaction add(int var1, Fragment var2) {
      this.doAddOp(var1, var2, (String)null, 1);
      return this;
   }

   public FragmentTransaction add(int var1, Fragment var2, String var3) {
      this.doAddOp(var1, var2, var3, 1);
      return this;
   }

   public final FragmentTransaction add(int var1, Class var2, Bundle var3) {
      return this.add(var1, this.createFragment(var2, var3));
   }

   public final FragmentTransaction add(int var1, Class var2, Bundle var3, String var4) {
      return this.add(var1, this.createFragment(var2, var3), var4);
   }

   FragmentTransaction add(ViewGroup var1, Fragment var2, String var3) {
      var2.mContainer = var1;
      return this.add(var1.getId(), var2, var3);
   }

   public FragmentTransaction add(Fragment var1, String var2) {
      this.doAddOp(0, var1, var2, 1);
      return this;
   }

   public final FragmentTransaction add(Class var1, Bundle var2, String var3) {
      return this.add(this.createFragment(var1, var2), var3);
   }

   void addOp(FragmentTransaction.class_11 var1) {
      this.mOps.add(var1);
      var1.mEnterAnim = this.mEnterAnim;
      var1.mExitAnim = this.mExitAnim;
      var1.mPopEnterAnim = this.mPopEnterAnim;
      var1.mPopExitAnim = this.mPopExitAnim;
   }

   public FragmentTransaction addSharedElement(View var1, String var2) {
      if (FragmentTransition.supportsTransition()) {
         String var3 = ViewCompat.getTransitionName(var1);
         if (var3 != null) {
            if (this.mSharedElementSourceNames == null) {
               this.mSharedElementSourceNames = new ArrayList();
               this.mSharedElementTargetNames = new ArrayList();
            } else {
               if (this.mSharedElementTargetNames.contains(var2)) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("A shared element with the target name '");
                  var4.append(var2);
                  var4.append("' has already been added to the transaction.");
                  throw new IllegalArgumentException(var4.toString());
               }

               if (this.mSharedElementSourceNames.contains(var3)) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("A shared element with the source name '");
                  var5.append(var3);
                  var5.append("' has already been added to the transaction.");
                  throw new IllegalArgumentException(var5.toString());
               }
            }

            this.mSharedElementSourceNames.add(var3);
            this.mSharedElementTargetNames.add(var2);
            return this;
         } else {
            throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
         }
      } else {
         return this;
      }
   }

   public FragmentTransaction addToBackStack(String var1) {
      if (this.mAllowAddToBackStack) {
         this.mAddToBackStack = true;
         this.mName = var1;
         return this;
      } else {
         throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
      }
   }

   public FragmentTransaction attach(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(7, var1));
      return this;
   }

   public abstract int commit();

   public abstract int commitAllowingStateLoss();

   public abstract void commitNow();

   public abstract void commitNowAllowingStateLoss();

   public FragmentTransaction detach(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(6, var1));
      return this;
   }

   public FragmentTransaction disallowAddToBackStack() {
      if (!this.mAddToBackStack) {
         this.mAllowAddToBackStack = false;
         return this;
      } else {
         throw new IllegalStateException("This transaction is already being added to the back stack");
      }
   }

   void doAddOp(int var1, Fragment var2, String var3, int var4) {
      Class var6 = var2.getClass();
      int var5 = var6.getModifiers();
      if (var6.isAnonymousClass() || !Modifier.isPublic(var5) || var6.isMemberClass() && !Modifier.isStatic(var5)) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Fragment ");
         var7.append(var6.getCanonicalName());
         var7.append(" must be a public static class to be  properly recreated from instance state.");
         throw new IllegalStateException(var7.toString());
      } else {
         StringBuilder var9;
         if (var3 != null) {
            if (var2.mTag != null && !var3.equals(var2.mTag)) {
               var9 = new StringBuilder();
               var9.append("Can't change tag of fragment ");
               var9.append(var2);
               var9.append(": was ");
               var9.append(var2.mTag);
               var9.append(" now ");
               var9.append(var3);
               throw new IllegalStateException(var9.toString());
            }

            var2.mTag = var3;
         }

         if (var1 != 0) {
            if (var1 == -1) {
               var9 = new StringBuilder();
               var9.append("Can't add fragment ");
               var9.append(var2);
               var9.append(" with tag ");
               var9.append(var3);
               var9.append(" to container view with no id");
               throw new IllegalArgumentException(var9.toString());
            }

            if (var2.mFragmentId != 0 && var2.mFragmentId != var1) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Can't change container ID of fragment ");
               var8.append(var2);
               var8.append(": was ");
               var8.append(var2.mFragmentId);
               var8.append(" now ");
               var8.append(var1);
               throw new IllegalStateException(var8.toString());
            }

            var2.mFragmentId = var1;
            var2.mContainerId = var1;
         }

         this.addOp(new FragmentTransaction.class_11(var4, var2));
      }
   }

   public FragmentTransaction hide(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(4, var1));
      return this;
   }

   public boolean isAddToBackStackAllowed() {
      return this.mAllowAddToBackStack;
   }

   public boolean isEmpty() {
      return this.mOps.isEmpty();
   }

   public FragmentTransaction remove(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(3, var1));
      return this;
   }

   public FragmentTransaction replace(int var1, Fragment var2) {
      return this.replace(var1, (Fragment)var2, (String)null);
   }

   public FragmentTransaction replace(int var1, Fragment var2, String var3) {
      if (var1 != 0) {
         this.doAddOp(var1, var2, var3, 2);
         return this;
      } else {
         throw new IllegalArgumentException("Must use non-zero containerViewId");
      }
   }

   public final FragmentTransaction replace(int var1, Class var2, Bundle var3) {
      return this.replace(var1, var2, var3, (String)null);
   }

   public final FragmentTransaction replace(int var1, Class var2, Bundle var3, String var4) {
      return this.replace(var1, this.createFragment(var2, var3), var4);
   }

   public FragmentTransaction runOnCommit(Runnable var1) {
      this.disallowAddToBackStack();
      if (this.mCommitRunnables == null) {
         this.mCommitRunnables = new ArrayList();
      }

      this.mCommitRunnables.add(var1);
      return this;
   }

   @Deprecated
   public FragmentTransaction setAllowOptimization(boolean var1) {
      return this.setReorderingAllowed(var1);
   }

   @Deprecated
   public FragmentTransaction setBreadCrumbShortTitle(int var1) {
      this.mBreadCrumbShortTitleRes = var1;
      this.mBreadCrumbShortTitleText = null;
      return this;
   }

   @Deprecated
   public FragmentTransaction setBreadCrumbShortTitle(CharSequence var1) {
      this.mBreadCrumbShortTitleRes = 0;
      this.mBreadCrumbShortTitleText = var1;
      return this;
   }

   @Deprecated
   public FragmentTransaction setBreadCrumbTitle(int var1) {
      this.mBreadCrumbTitleRes = var1;
      this.mBreadCrumbTitleText = null;
      return this;
   }

   @Deprecated
   public FragmentTransaction setBreadCrumbTitle(CharSequence var1) {
      this.mBreadCrumbTitleRes = 0;
      this.mBreadCrumbTitleText = var1;
      return this;
   }

   public FragmentTransaction setCustomAnimations(int var1, int var2) {
      return this.setCustomAnimations(var1, var2, 0, 0);
   }

   public FragmentTransaction setCustomAnimations(int var1, int var2, int var3, int var4) {
      this.mEnterAnim = var1;
      this.mExitAnim = var2;
      this.mPopEnterAnim = var3;
      this.mPopExitAnim = var4;
      return this;
   }

   public FragmentTransaction setMaxLifecycle(Fragment var1, Lifecycle.State var2) {
      this.addOp(new FragmentTransaction.class_11(10, var1, var2));
      return this;
   }

   public FragmentTransaction setPrimaryNavigationFragment(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(8, var1));
      return this;
   }

   public FragmentTransaction setReorderingAllowed(boolean var1) {
      this.mReorderingAllowed = var1;
      return this;
   }

   public FragmentTransaction setTransition(int var1) {
      this.mTransition = var1;
      return this;
   }

   @Deprecated
   public FragmentTransaction setTransitionStyle(int var1) {
      return this;
   }

   public FragmentTransaction show(Fragment var1) {
      this.addOp(new FragmentTransaction.class_11(5, var1));
      return this;
   }

   static final class class_11 {
      int mCmd;
      Lifecycle.State mCurrentMaxState;
      int mEnterAnim;
      int mExitAnim;
      Fragment mFragment;
      Lifecycle.State mOldMaxState;
      int mPopEnterAnim;
      int mPopExitAnim;

      class_11() {
      }

      class_11(int var1, Fragment var2) {
         this.mCmd = var1;
         this.mFragment = var2;
         this.mOldMaxState = Lifecycle.State.RESUMED;
         this.mCurrentMaxState = Lifecycle.State.RESUMED;
      }

      class_11(int var1, Fragment var2, Lifecycle.State var3) {
         this.mCmd = var1;
         this.mFragment = var2;
         this.mOldMaxState = var2.mMaxState;
         this.mCurrentMaxState = var3;
      }
   }
}
