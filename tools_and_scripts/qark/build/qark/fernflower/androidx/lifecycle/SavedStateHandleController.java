package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.Iterator;

final class SavedStateHandleController implements LifecycleEventObserver {
   static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
   private final SavedStateHandle mHandle;
   private boolean mIsAttached = false;
   private final String mKey;

   SavedStateHandleController(String var1, SavedStateHandle var2) {
      this.mKey = var1;
      this.mHandle = var2;
   }

   static void attachHandleIfNeeded(ViewModel var0, SavedStateRegistry var1, Lifecycle var2) {
      SavedStateHandleController var3 = (SavedStateHandleController)var0.getTag("androidx.lifecycle.savedstate.vm.tag");
      if (var3 != null && !var3.isAttached()) {
         var3.attachToLifecycle(var1, var2);
         tryToAddRecreator(var1, var2);
      }

   }

   static SavedStateHandleController create(SavedStateRegistry var0, Lifecycle var1, String var2, Bundle var3) {
      SavedStateHandleController var4 = new SavedStateHandleController(var2, SavedStateHandle.createHandle(var0.consumeRestoredStateForKey(var2), var3));
      var4.attachToLifecycle(var0, var1);
      tryToAddRecreator(var0, var1);
      return var4;
   }

   private static void tryToAddRecreator(final SavedStateRegistry var0, final Lifecycle var1) {
      Lifecycle.State var2 = var1.getCurrentState();
      if (var2 != Lifecycle.State.INITIALIZED && !var2.isAtLeast(Lifecycle.State.STARTED)) {
         var1.addObserver(new LifecycleEventObserver() {
            public void onStateChanged(LifecycleOwner var1x, Lifecycle.Event var2) {
               if (var2 == Lifecycle.Event.ON_START) {
                  var1.removeObserver(this);
                  var0.runOnNextRecreation(SavedStateHandleController.OnRecreation.class);
               }

            }
         });
      } else {
         var0.runOnNextRecreation(SavedStateHandleController.OnRecreation.class);
      }
   }

   void attachToLifecycle(SavedStateRegistry var1, Lifecycle var2) {
      if (!this.mIsAttached) {
         this.mIsAttached = true;
         var2.addObserver(this);
         var1.registerSavedStateProvider(this.mKey, this.mHandle.savedStateProvider());
      } else {
         throw new IllegalStateException("Already attached to lifecycleOwner");
      }
   }

   SavedStateHandle getHandle() {
      return this.mHandle;
   }

   boolean isAttached() {
      return this.mIsAttached;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      if (var2 == Lifecycle.Event.ON_DESTROY) {
         this.mIsAttached = false;
         var1.getLifecycle().removeObserver(this);
      }

   }

   static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
      public void onRecreated(SavedStateRegistryOwner var1) {
         if (!(var1 instanceof ViewModelStoreOwner)) {
            throw new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
         } else {
            ViewModelStore var2 = ((ViewModelStoreOwner)var1).getViewModelStore();
            SavedStateRegistry var3 = var1.getSavedStateRegistry();
            Iterator var4 = var2.keys().iterator();

            while(var4.hasNext()) {
               SavedStateHandleController.attachHandleIfNeeded(var2.get((String)var4.next()), var3, var1.getLifecycle());
            }

            if (!var2.keys().isEmpty()) {
               var3.runOnNextRecreation(SavedStateHandleController.OnRecreation.class);
            }

         }
      }
   }
}
