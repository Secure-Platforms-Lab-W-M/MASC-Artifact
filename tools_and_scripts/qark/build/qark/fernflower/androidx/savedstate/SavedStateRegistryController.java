package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;

public final class SavedStateRegistryController {
   private final SavedStateRegistryOwner mOwner;
   private final SavedStateRegistry mRegistry;

   private SavedStateRegistryController(SavedStateRegistryOwner var1) {
      this.mOwner = var1;
      this.mRegistry = new SavedStateRegistry();
   }

   public static SavedStateRegistryController create(SavedStateRegistryOwner var0) {
      return new SavedStateRegistryController(var0);
   }

   public SavedStateRegistry getSavedStateRegistry() {
      return this.mRegistry;
   }

   public void performRestore(Bundle var1) {
      Lifecycle var2 = this.mOwner.getLifecycle();
      if (var2.getCurrentState() == Lifecycle.State.INITIALIZED) {
         var2.addObserver(new Recreator(this.mOwner));
         this.mRegistry.performRestore(var2, var1);
      } else {
         throw new IllegalStateException("Restarter must be created only during owner's initialization stage");
      }
   }

   public void performSave(Bundle var1) {
      this.mRegistry.performSave(var1);
   }
}
