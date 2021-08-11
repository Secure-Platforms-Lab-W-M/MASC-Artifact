package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

final class FragmentManagerViewModel extends ViewModel {
   private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
      public ViewModel create(Class var1) {
         return new FragmentManagerViewModel(true);
      }
   };
   private static final String TAG = "FragmentManager";
   private final HashMap mChildNonConfigs = new HashMap();
   private boolean mHasBeenCleared = false;
   private boolean mHasSavedSnapshot = false;
   private final HashMap mRetainedFragments = new HashMap();
   private final boolean mStateAutomaticallySaved;
   private final HashMap mViewModelStores = new HashMap();

   FragmentManagerViewModel(boolean var1) {
      this.mStateAutomaticallySaved = var1;
   }

   static FragmentManagerViewModel getInstance(ViewModelStore var0) {
      return (FragmentManagerViewModel)(new ViewModelProvider(var0, FACTORY)).get(FragmentManagerViewModel.class);
   }

   boolean addRetainedFragment(Fragment var1) {
      if (this.mRetainedFragments.containsKey(var1.mWho)) {
         return false;
      } else {
         this.mRetainedFragments.put(var1.mWho, var1);
         return true;
      }
   }

   void clearNonConfigState(Fragment var1) {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Clearing non-config state for ");
         var2.append(var1);
         Log.d("FragmentManager", var2.toString());
      }

      FragmentManagerViewModel var3 = (FragmentManagerViewModel)this.mChildNonConfigs.get(var1.mWho);
      if (var3 != null) {
         var3.onCleared();
         this.mChildNonConfigs.remove(var1.mWho);
      }

      ViewModelStore var4 = (ViewModelStore)this.mViewModelStores.get(var1.mWho);
      if (var4 != null) {
         var4.clear();
         this.mViewModelStores.remove(var1.mWho);
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            FragmentManagerViewModel var2 = (FragmentManagerViewModel)var1;
            return this.mRetainedFragments.equals(var2.mRetainedFragments) && this.mChildNonConfigs.equals(var2.mChildNonConfigs) && this.mViewModelStores.equals(var2.mViewModelStores);
         }
      } else {
         return false;
      }
   }

   Fragment findRetainedFragmentByWho(String var1) {
      return (Fragment)this.mRetainedFragments.get(var1);
   }

   FragmentManagerViewModel getChildNonConfig(Fragment var1) {
      FragmentManagerViewModel var3 = (FragmentManagerViewModel)this.mChildNonConfigs.get(var1.mWho);
      FragmentManagerViewModel var2 = var3;
      if (var3 == null) {
         var2 = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
         this.mChildNonConfigs.put(var1.mWho, var2);
      }

      return var2;
   }

   Collection getRetainedFragments() {
      return this.mRetainedFragments.values();
   }

   @Deprecated
   FragmentManagerNonConfig getSnapshot() {
      if (this.mRetainedFragments.isEmpty() && this.mChildNonConfigs.isEmpty() && this.mViewModelStores.isEmpty()) {
         return null;
      } else {
         HashMap var1 = new HashMap();
         Iterator var2 = this.mChildNonConfigs.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            FragmentManagerNonConfig var4 = ((FragmentManagerViewModel)var3.getValue()).getSnapshot();
            if (var4 != null) {
               var1.put(var3.getKey(), var4);
            }
         }

         this.mHasSavedSnapshot = true;
         if (this.mRetainedFragments.isEmpty() && var1.isEmpty() && this.mViewModelStores.isEmpty()) {
            return null;
         } else {
            return new FragmentManagerNonConfig(new ArrayList(this.mRetainedFragments.values()), var1, new HashMap(this.mViewModelStores));
         }
      }
   }

   ViewModelStore getViewModelStore(Fragment var1) {
      ViewModelStore var3 = (ViewModelStore)this.mViewModelStores.get(var1.mWho);
      ViewModelStore var2 = var3;
      if (var3 == null) {
         var2 = new ViewModelStore();
         this.mViewModelStores.put(var1.mWho, var2);
      }

      return var2;
   }

   public int hashCode() {
      return (this.mRetainedFragments.hashCode() * 31 + this.mChildNonConfigs.hashCode()) * 31 + this.mViewModelStores.hashCode();
   }

   boolean isCleared() {
      return this.mHasBeenCleared;
   }

   protected void onCleared() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("onCleared called for ");
         var1.append(this);
         Log.d("FragmentManager", var1.toString());
      }

      this.mHasBeenCleared = true;
   }

   boolean removeRetainedFragment(Fragment var1) {
      return this.mRetainedFragments.remove(var1.mWho) != null;
   }

   @Deprecated
   void restoreFromSnapshot(FragmentManagerNonConfig var1) {
      this.mRetainedFragments.clear();
      this.mChildNonConfigs.clear();
      this.mViewModelStores.clear();
      if (var1 != null) {
         Collection var2 = var1.getFragments();
         Iterator var6;
         if (var2 != null) {
            var6 = var2.iterator();

            while(var6.hasNext()) {
               Fragment var3 = (Fragment)var6.next();
               if (var3 != null) {
                  this.mRetainedFragments.put(var3.mWho, var3);
               }
            }
         }

         Map var7 = var1.getChildNonConfigs();
         if (var7 != null) {
            var6 = var7.entrySet().iterator();

            while(var6.hasNext()) {
               Entry var8 = (Entry)var6.next();
               FragmentManagerViewModel var4 = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
               var4.restoreFromSnapshot((FragmentManagerNonConfig)var8.getValue());
               this.mChildNonConfigs.put(var8.getKey(), var4);
            }
         }

         Map var5 = var1.getViewModelStores();
         if (var5 != null) {
            this.mViewModelStores.putAll(var5);
         }
      }

      this.mHasSavedSnapshot = false;
   }

   boolean shouldDestroy(Fragment var1) {
      if (!this.mRetainedFragments.containsKey(var1.mWho)) {
         return true;
      } else {
         return this.mStateAutomaticallySaved ? this.mHasBeenCleared : this.mHasSavedSnapshot ^ true;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("FragmentManagerViewModel{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append("} Fragments (");
      Iterator var2 = this.mRetainedFragments.values().iterator();

      while(var2.hasNext()) {
         var1.append(var2.next());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append(") Child Non Config (");
      var2 = this.mChildNonConfigs.keySet().iterator();

      while(var2.hasNext()) {
         var1.append((String)var2.next());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append(") ViewModelStores (");
      var2 = this.mViewModelStores.keySet().iterator();

      while(var2.hasNext()) {
         var1.append((String)var2.next());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append(')');
      return var1.toString();
   }
}
