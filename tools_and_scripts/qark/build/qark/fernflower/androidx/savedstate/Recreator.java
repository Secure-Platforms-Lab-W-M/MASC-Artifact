package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class Recreator implements GenericLifecycleObserver {
   static final String CLASSES_KEY = "classes_to_restore";
   static final String COMPONENT_KEY = "androidx.savedstate.Restarter";
   private final SavedStateRegistryOwner mOwner;

   Recreator(SavedStateRegistryOwner var1) {
      this.mOwner = var1;
   }

   private void reflectiveNew(String var1) {
      Class var2;
      StringBuilder var3;
      try {
         var2 = Class.forName(var1, false, Recreator.class.getClassLoader()).asSubclass(SavedStateRegistry.AutoRecreated.class);
      } catch (ClassNotFoundException var6) {
         var3 = new StringBuilder();
         var3.append("Class ");
         var3.append(var1);
         var3.append(" wasn't found");
         throw new RuntimeException(var3.toString(), var6);
      }

      Constructor var8;
      try {
         var8 = var2.getDeclaredConstructor();
      } catch (NoSuchMethodException var5) {
         var3 = new StringBuilder();
         var3.append("Class");
         var3.append(var2.getSimpleName());
         var3.append(" must have default constructor in order to be automatically recreated");
         throw new IllegalStateException(var3.toString(), var5);
      }

      var8.setAccessible(true);

      SavedStateRegistry.AutoRecreated var7;
      try {
         var7 = (SavedStateRegistry.AutoRecreated)var8.newInstance();
      } catch (Exception var4) {
         var3 = new StringBuilder();
         var3.append("Failed to instantiate ");
         var3.append(var1);
         throw new RuntimeException(var3.toString(), var4);
      }

      var7.onRecreated(this.mOwner);
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      if (var2 != Lifecycle.Event.ON_CREATE) {
         throw new AssertionError("Next event must be ON_CREATE");
      } else {
         var1.getLifecycle().removeObserver(this);
         Bundle var3 = this.mOwner.getSavedStateRegistry().consumeRestoredStateForKey("androidx.savedstate.Restarter");
         if (var3 != null) {
            ArrayList var4 = var3.getStringArrayList("classes_to_restore");
            if (var4 == null) {
               throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
            } else {
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  this.reflectiveNew((String)var5.next());
               }

            }
         }
      }
   }

   static final class SavedStateProvider implements SavedStateRegistry.SavedStateProvider {
      final Set mClasses = new HashSet();

      SavedStateProvider(SavedStateRegistry var1) {
         var1.registerSavedStateProvider("androidx.savedstate.Restarter", this);
      }

      void add(String var1) {
         this.mClasses.add(var1);
      }

      public Bundle saveState() {
         Bundle var1 = new Bundle();
         var1.putStringArrayList("classes_to_restore", new ArrayList(this.mClasses));
         return var1;
      }
   }
}
