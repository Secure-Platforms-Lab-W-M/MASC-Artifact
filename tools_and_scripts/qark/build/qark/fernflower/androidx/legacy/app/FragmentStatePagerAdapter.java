package androidx.legacy.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment.SavedState;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
import java.util.Iterator;

@Deprecated
public abstract class FragmentStatePagerAdapter extends PagerAdapter {
   private static final boolean DEBUG = false;
   private static final String TAG = "FragStatePagerAdapter";
   private FragmentTransaction mCurTransaction = null;
   private Fragment mCurrentPrimaryItem = null;
   private final FragmentManager mFragmentManager;
   private ArrayList mFragments = new ArrayList();
   private ArrayList mSavedState = new ArrayList();

   @Deprecated
   public FragmentStatePagerAdapter(FragmentManager var1) {
      this.mFragmentManager = var1;
   }

   @Deprecated
   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      Fragment var6 = (Fragment)var3;
      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      while(this.mSavedState.size() <= var2) {
         this.mSavedState.add((Object)null);
      }

      ArrayList var4 = this.mSavedState;
      SavedState var5;
      if (var6.isAdded()) {
         var5 = this.mFragmentManager.saveFragmentInstanceState(var6);
      } else {
         var5 = null;
      }

      var4.set(var2, var5);
      this.mFragments.set(var2, (Object)null);
      this.mCurTransaction.remove(var6);
   }

   @Deprecated
   public void finishUpdate(ViewGroup var1) {
      FragmentTransaction var2 = this.mCurTransaction;
      if (var2 != null) {
         var2.commitAllowingStateLoss();
         this.mCurTransaction = null;
         this.mFragmentManager.executePendingTransactions();
      }

   }

   @Deprecated
   public abstract Fragment getItem(int var1);

   @Deprecated
   public Object instantiateItem(ViewGroup var1, int var2) {
      Fragment var3;
      if (this.mFragments.size() > var2) {
         var3 = (Fragment)this.mFragments.get(var2);
         if (var3 != null) {
            return var3;
         }
      }

      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      var3 = this.getItem(var2);
      if (this.mSavedState.size() > var2) {
         SavedState var4 = (SavedState)this.mSavedState.get(var2);
         if (var4 != null) {
            var3.setInitialSavedState(var4);
         }
      }

      while(this.mFragments.size() <= var2) {
         this.mFragments.add((Object)null);
      }

      var3.setMenuVisibility(false);
      FragmentCompat.setUserVisibleHint(var3, false);
      this.mFragments.set(var2, var3);
      this.mCurTransaction.add(var1.getId(), var3);
      return var3;
   }

   @Deprecated
   public boolean isViewFromObject(View var1, Object var2) {
      return ((Fragment)var2).getView() == var1;
   }

   @Deprecated
   public void restoreState(Parcelable var1, ClassLoader var2) {
      if (var1 != null) {
         Bundle var6 = (Bundle)var1;
         var6.setClassLoader(var2);
         Parcelable[] var7 = var6.getParcelableArray("states");
         this.mSavedState.clear();
         this.mFragments.clear();
         int var3;
         if (var7 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               this.mSavedState.add((SavedState)var7[var3]);
            }
         }

         Iterator var8 = var6.keySet().iterator();

         while(true) {
            while(true) {
               String var4;
               do {
                  if (!var8.hasNext()) {
                     return;
                  }

                  var4 = (String)var8.next();
               } while(!var4.startsWith("f"));

               var3 = Integer.parseInt(var4.substring(1));
               Fragment var5 = this.mFragmentManager.getFragment(var6, var4);
               if (var5 != null) {
                  while(this.mFragments.size() <= var3) {
                     this.mFragments.add((Object)null);
                  }

                  FragmentCompat.setMenuVisibility(var5, false);
                  this.mFragments.set(var3, var5);
               } else {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Bad fragment at key ");
                  var9.append(var4);
                  Log.w("FragStatePagerAdapter", var9.toString());
               }
            }
         }
      }
   }

   @Deprecated
   public Parcelable saveState() {
      Bundle var3 = null;
      if (this.mSavedState.size() > 0) {
         var3 = new Bundle();
         SavedState[] var2 = new SavedState[this.mSavedState.size()];
         this.mSavedState.toArray(var2);
         var3.putParcelableArray("states", var2);
      }

      Bundle var5;
      for(int var1 = 0; var1 < this.mFragments.size(); var3 = var5) {
         Fragment var4 = (Fragment)this.mFragments.get(var1);
         var5 = var3;
         if (var4 != null) {
            var5 = var3;
            if (var4.isAdded()) {
               var5 = var3;
               if (var3 == null) {
                  var5 = new Bundle();
               }

               StringBuilder var6 = new StringBuilder();
               var6.append("f");
               var6.append(var1);
               String var7 = var6.toString();
               this.mFragmentManager.putFragment(var5, var7, var4);
            }
         }

         ++var1;
      }

      return var3;
   }

   @Deprecated
   public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      Fragment var5 = this.mCurrentPrimaryItem;
      if (var4 != var5) {
         if (var5 != null) {
            var5.setMenuVisibility(false);
            FragmentCompat.setUserVisibleHint(this.mCurrentPrimaryItem, false);
         }

         if (var4 != null) {
            var4.setMenuVisibility(true);
            FragmentCompat.setUserVisibleHint(var4, true);
         }

         this.mCurrentPrimaryItem = var4;
      }

   }

   @Deprecated
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
