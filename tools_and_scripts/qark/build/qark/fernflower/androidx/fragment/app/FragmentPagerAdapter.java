package androidx.fragment.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;

public abstract class FragmentPagerAdapter extends PagerAdapter {
   public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
   @Deprecated
   public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;
   private static final boolean DEBUG = false;
   private static final String TAG = "FragmentPagerAdapter";
   private final int mBehavior;
   private FragmentTransaction mCurTransaction;
   private Fragment mCurrentPrimaryItem;
   private final FragmentManager mFragmentManager;

   @Deprecated
   public FragmentPagerAdapter(FragmentManager var1) {
      this(var1, 0);
   }

   public FragmentPagerAdapter(FragmentManager var1, int var2) {
      this.mCurTransaction = null;
      this.mCurrentPrimaryItem = null;
      this.mFragmentManager = var1;
      this.mBehavior = var2;
   }

   private static String makeFragmentName(int var0, long var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append("android:switcher:");
      var3.append(var0);
      var3.append(":");
      var3.append(var1);
      return var3.toString();
   }

   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      this.mCurTransaction.detach(var4);
      if (var4.equals(this.mCurrentPrimaryItem)) {
         this.mCurrentPrimaryItem = null;
      }

   }

   public void finishUpdate(ViewGroup var1) {
      FragmentTransaction var3 = this.mCurTransaction;
      if (var3 != null) {
         try {
            var3.commitNowAllowingStateLoss();
         } catch (IllegalStateException var2) {
            this.mCurTransaction.commitAllowingStateLoss();
         }

         this.mCurTransaction = null;
      }

   }

   public abstract Fragment getItem(int var1);

   public long getItemId(int var1) {
      return (long)var1;
   }

   public Object instantiateItem(ViewGroup var1, int var2) {
      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      long var3 = this.getItemId(var2);
      String var5 = makeFragmentName(var1.getId(), var3);
      Fragment var6 = this.mFragmentManager.findFragmentByTag(var5);
      Fragment var7;
      if (var6 != null) {
         this.mCurTransaction.attach(var6);
         var7 = var6;
      } else {
         var6 = this.getItem(var2);
         this.mCurTransaction.add(var1.getId(), var6, makeFragmentName(var1.getId(), var3));
         var7 = var6;
      }

      if (var7 != this.mCurrentPrimaryItem) {
         var7.setMenuVisibility(false);
         if (this.mBehavior == 1) {
            this.mCurTransaction.setMaxLifecycle(var7, Lifecycle.State.STARTED);
            return var7;
         }

         var7.setUserVisibleHint(false);
      }

      return var7;
   }

   public boolean isViewFromObject(View var1, Object var2) {
      return ((Fragment)var2).getView() == var1;
   }

   public void restoreState(Parcelable var1, ClassLoader var2) {
   }

   public Parcelable saveState() {
      return null;
   }

   public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      Fragment var5 = this.mCurrentPrimaryItem;
      if (var4 != var5) {
         if (var5 != null) {
            var5.setMenuVisibility(false);
            if (this.mBehavior == 1) {
               if (this.mCurTransaction == null) {
                  this.mCurTransaction = this.mFragmentManager.beginTransaction();
               }

               this.mCurTransaction.setMaxLifecycle(this.mCurrentPrimaryItem, Lifecycle.State.STARTED);
            } else {
               this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
         }

         var4.setMenuVisibility(true);
         if (this.mBehavior == 1) {
            if (this.mCurTransaction == null) {
               this.mCurTransaction = this.mFragmentManager.beginTransaction();
            }

            this.mCurTransaction.setMaxLifecycle(var4, Lifecycle.State.RESUMED);
         } else {
            var4.setUserVisibleHint(true);
         }

         this.mCurrentPrimaryItem = var4;
      }

   }

   public void startUpdate(ViewGroup var1) {
      if (var1.getId() == -1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("ViewPager with adapter ");
         var2.append(this);
         var2.append(" requires a view id");
         throw new IllegalStateException(var2.toString());
      }
   }
}
