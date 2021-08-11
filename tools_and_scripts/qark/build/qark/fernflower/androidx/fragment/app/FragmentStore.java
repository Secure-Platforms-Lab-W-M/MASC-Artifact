package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class FragmentStore {
   private static final String TAG = "FragmentManager";
   private final HashMap mActive = new HashMap();
   private final ArrayList mAdded = new ArrayList();

   void addFragment(Fragment param1) {
      // $FF: Couldn't be decompiled
   }

   void burpActive() {
      this.mActive.values().removeAll(Collections.singleton((Object)null));
   }

   boolean containsActiveFragment(String var1) {
      return this.mActive.containsKey(var1);
   }

   void dispatchStateChange(int var1) {
      Iterator var2 = this.mAdded.iterator();

      FragmentStateManager var4;
      while(var2.hasNext()) {
         Fragment var3 = (Fragment)var2.next();
         var4 = (FragmentStateManager)this.mActive.get(var3.mWho);
         if (var4 != null) {
            var4.setFragmentManagerState(var1);
         }
      }

      var2 = this.mActive.values().iterator();

      while(var2.hasNext()) {
         var4 = (FragmentStateManager)var2.next();
         if (var4 != null) {
            var4.setFragmentManagerState(var1);
         }
      }

   }

   void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var1);
      var7.append("    ");
      String var11 = var7.toString();
      if (!this.mActive.isEmpty()) {
         var3.print(var1);
         var3.print("Active Fragments:");
         Iterator var8 = this.mActive.values().iterator();

         while(var8.hasNext()) {
            FragmentStateManager var9 = (FragmentStateManager)var8.next();
            var3.print(var1);
            if (var9 != null) {
               Fragment var12 = var9.getFragment();
               var3.println(var12);
               var12.dump(var11, var2, var3, var4);
            } else {
               var3.println("null");
            }
         }
      }

      int var6 = this.mAdded.size();
      if (var6 > 0) {
         var3.print(var1);
         var3.println("Added Fragments:");

         for(int var5 = 0; var5 < var6; ++var5) {
            Fragment var10 = (Fragment)this.mAdded.get(var5);
            var3.print(var1);
            var3.print("  #");
            var3.print(var5);
            var3.print(": ");
            var3.println(var10.toString());
         }
      }

   }

   Fragment findActiveFragment(String var1) {
      FragmentStateManager var2 = (FragmentStateManager)this.mActive.get(var1);
      return var2 != null ? var2.getFragment() : null;
   }

   Fragment findFragmentById(int var1) {
      for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null && var3.mFragmentId == var1) {
            return var3;
         }
      }

      Iterator var5 = this.mActive.values().iterator();

      while(var5.hasNext()) {
         FragmentStateManager var4 = (FragmentStateManager)var5.next();
         if (var4 != null) {
            Fragment var6 = var4.getFragment();
            if (var6.mFragmentId == var1) {
               return var6;
            }
         }
      }

      return null;
   }

   Fragment findFragmentByTag(String var1) {
      if (var1 != null) {
         for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      if (var1 != null) {
         Iterator var5 = this.mActive.values().iterator();

         while(var5.hasNext()) {
            FragmentStateManager var4 = (FragmentStateManager)var5.next();
            if (var4 != null) {
               Fragment var6 = var4.getFragment();
               if (var1.equals(var6.mTag)) {
                  return var6;
               }
            }
         }
      }

      return null;
   }

   Fragment findFragmentByWho(String var1) {
      Iterator var2 = this.mActive.values().iterator();

      while(var2.hasNext()) {
         FragmentStateManager var3 = (FragmentStateManager)var2.next();
         if (var3 != null) {
            Fragment var4 = var3.getFragment().findFragmentByWho(var1);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   Fragment findFragmentUnder(Fragment var1) {
      ViewGroup var3 = var1.mContainer;
      View var4 = var1.mView;
      if (var3 != null) {
         if (var4 == null) {
            return null;
         } else {
            for(int var2 = this.mAdded.indexOf(var1) - 1; var2 >= 0; --var2) {
               var1 = (Fragment)this.mAdded.get(var2);
               if (var1.mContainer == var3 && var1.mView != null) {
                  return var1;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   int getActiveFragmentCount() {
      return this.mActive.size();
   }

   List getActiveFragments() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.mActive.values().iterator();

      while(var2.hasNext()) {
         FragmentStateManager var3 = (FragmentStateManager)var2.next();
         if (var3 != null) {
            var1.add(var3.getFragment());
         } else {
            var1.add((Object)null);
         }
      }

      return var1;
   }

   FragmentStateManager getFragmentStateManager(String var1) {
      return (FragmentStateManager)this.mActive.get(var1);
   }

   List getFragments() {
      // $FF: Couldn't be decompiled
   }

   void makeActive(FragmentStateManager var1) {
      this.mActive.put(var1.getFragment().mWho, var1);
   }

   void makeInactive(FragmentStateManager var1) {
      Fragment var4 = var1.getFragment();
      Iterator var2 = this.mActive.values().iterator();

      while(var2.hasNext()) {
         FragmentStateManager var3 = (FragmentStateManager)var2.next();
         if (var3 != null) {
            Fragment var5 = var3.getFragment();
            if (var4.mWho.equals(var5.mTargetWho)) {
               var5.mTarget = var4;
               var5.mTargetWho = null;
            }
         }
      }

      this.mActive.put(var4.mWho, (Object)null);
      if (var4.mTargetWho != null) {
         var4.mTarget = this.findActiveFragment(var4.mTargetWho);
      }

   }

   void removeFragment(Fragment param1) {
      // $FF: Couldn't be decompiled
   }

   void resetActiveFragments() {
      this.mActive.clear();
   }

   void restoreAddedFragments(List var1) {
      this.mAdded.clear();
      Fragment var3;
      if (var1 != null) {
         for(Iterator var2 = var1.iterator(); var2.hasNext(); this.addFragment(var3)) {
            String var5 = (String)var2.next();
            var3 = this.findActiveFragment(var5);
            if (var3 == null) {
               StringBuilder var6 = new StringBuilder();
               var6.append("No instantiated fragment for (");
               var6.append(var5);
               var6.append(")");
               throw new IllegalStateException(var6.toString());
            }

            if (FragmentManager.isLoggingEnabled(2)) {
               StringBuilder var4 = new StringBuilder();
               var4.append("restoreSaveState: added (");
               var4.append(var5);
               var4.append("): ");
               var4.append(var3);
               Log.v("FragmentManager", var4.toString());
            }
         }
      }

   }

   ArrayList saveActiveFragments() {
      ArrayList var1 = new ArrayList(this.mActive.size());
      Iterator var2 = this.mActive.values().iterator();

      while(var2.hasNext()) {
         FragmentStateManager var4 = (FragmentStateManager)var2.next();
         if (var4 != null) {
            Fragment var3 = var4.getFragment();
            FragmentState var6 = var4.saveState();
            var1.add(var6);
            if (FragmentManager.isLoggingEnabled(2)) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Saved state of ");
               var5.append(var3);
               var5.append(": ");
               var5.append(var6.mSavedFragmentState);
               Log.v("FragmentManager", var5.toString());
            }
         }
      }

      return var1;
   }

   ArrayList saveAddedFragments() {
      ArrayList var1 = this.mAdded;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label314: {
         try {
            if (this.mAdded.isEmpty()) {
               return null;
            }
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label314;
         }

         ArrayList var2;
         Iterator var3;
         try {
            var2 = new ArrayList(this.mAdded.size());
            var3 = this.mAdded.iterator();
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label314;
         }

         while(true) {
            try {
               if (!var3.hasNext()) {
                  break;
               }

               Fragment var4 = (Fragment)var3.next();
               var2.add(var4.mWho);
               if (FragmentManager.isLoggingEnabled(2)) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("saveAllState: adding fragment (");
                  var5.append(var4.mWho);
                  var5.append("): ");
                  var5.append(var4);
                  Log.v("FragmentManager", var5.toString());
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label314;
            }
         }

         label292:
         try {
            return var2;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label292;
         }
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            continue;
         }
      }
   }
}
