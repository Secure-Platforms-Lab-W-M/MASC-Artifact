package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

public abstract class AbstractSavedStateViewModelFactory extends ViewModelProvider.KeyedFactory {
   static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
   private final Bundle mDefaultArgs;
   private final Lifecycle mLifecycle;
   private final SavedStateRegistry mSavedStateRegistry;

   public AbstractSavedStateViewModelFactory(SavedStateRegistryOwner var1, Bundle var2) {
      this.mSavedStateRegistry = var1.getSavedStateRegistry();
      this.mLifecycle = var1.getLifecycle();
      this.mDefaultArgs = var2;
   }

   public final ViewModel create(Class var1) {
      String var2 = var1.getCanonicalName();
      if (var2 != null) {
         return this.create(var2, var1);
      } else {
         throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
      }
   }

   public final ViewModel create(String var1, Class var2) {
      SavedStateHandleController var3 = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, var1, this.mDefaultArgs);
      ViewModel var4 = this.create(var1, var2, var3.getHandle());
      var4.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", var3);
      return var4;
   }

   protected abstract ViewModel create(String var1, Class var2, SavedStateHandle var3);

   void onRequery(ViewModel var1) {
      SavedStateHandleController.attachHandleIfNeeded(var1, this.mSavedStateRegistry, this.mLifecycle);
   }
}
