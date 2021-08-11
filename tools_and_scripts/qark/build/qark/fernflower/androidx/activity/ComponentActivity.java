package androidx.activity;

import android.app.Application;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;

public class ComponentActivity extends androidx.core.app.ComponentActivity implements LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, OnBackPressedDispatcherOwner {
   private int mContentLayoutId;
   private ViewModelProvider.Factory mDefaultFactory;
   private final LifecycleRegistry mLifecycleRegistry;
   private final OnBackPressedDispatcher mOnBackPressedDispatcher;
   private final SavedStateRegistryController mSavedStateRegistryController;
   private ViewModelStore mViewModelStore;

   public ComponentActivity() {
      this.mLifecycleRegistry = new LifecycleRegistry(this);
      this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
      this.mOnBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable() {
         public void run() {
            ComponentActivity.super.onBackPressed();
         }
      });
      if (this.getLifecycle() != null) {
         if (VERSION.SDK_INT >= 19) {
            this.getLifecycle().addObserver(new LifecycleEventObserver() {
               public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
                  if (var2 == Lifecycle.Event.ON_STOP) {
                     Window var3 = ComponentActivity.this.getWindow();
                     View var4;
                     if (var3 != null) {
                        var4 = var3.peekDecorView();
                     } else {
                        var4 = null;
                     }

                     if (var4 != null) {
                        var4.cancelPendingInputEvents();
                     }
                  }

               }
            });
         }

         this.getLifecycle().addObserver(new LifecycleEventObserver() {
            public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
               if (var2 == Lifecycle.Event.ON_DESTROY && !ComponentActivity.this.isChangingConfigurations()) {
                  ComponentActivity.this.getViewModelStore().clear();
               }

            }
         });
         if (19 <= VERSION.SDK_INT && VERSION.SDK_INT <= 23) {
            this.getLifecycle().addObserver(new ImmLeaksCleaner(this));
         }

      } else {
         throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
      }
   }

   public ComponentActivity(int var1) {
      this();
      this.mContentLayoutId = var1;
   }

   public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
      if (this.getApplication() != null) {
         if (this.mDefaultFactory == null) {
            Application var2 = this.getApplication();
            Bundle var1;
            if (this.getIntent() != null) {
               var1 = this.getIntent().getExtras();
            } else {
               var1 = null;
            }

            this.mDefaultFactory = new SavedStateViewModelFactory(var2, this, var1);
         }

         return this.mDefaultFactory;
      } else {
         throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
      }
   }

   @Deprecated
   public Object getLastCustomNonConfigurationInstance() {
      ComponentActivity.NonConfigurationInstances var1 = (ComponentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      return var1 != null ? var1.custom : null;
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
      return this.mOnBackPressedDispatcher;
   }

   public final SavedStateRegistry getSavedStateRegistry() {
      return this.mSavedStateRegistryController.getSavedStateRegistry();
   }

   public ViewModelStore getViewModelStore() {
      if (this.getApplication() != null) {
         if (this.mViewModelStore == null) {
            ComponentActivity.NonConfigurationInstances var1 = (ComponentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
            if (var1 != null) {
               this.mViewModelStore = var1.viewModelStore;
            }

            if (this.mViewModelStore == null) {
               this.mViewModelStore = new ViewModelStore();
            }
         }

         return this.mViewModelStore;
      } else {
         throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
      }
   }

   public void onBackPressed() {
      this.mOnBackPressedDispatcher.onBackPressed();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mSavedStateRegistryController.performRestore(var1);
      ReportFragment.injectIfNeededIn(this);
      int var2 = this.mContentLayoutId;
      if (var2 != 0) {
         this.setContentView(var2);
      }

   }

   @Deprecated
   public Object onRetainCustomNonConfigurationInstance() {
      return null;
   }

   public final Object onRetainNonConfigurationInstance() {
      Object var3 = this.onRetainCustomNonConfigurationInstance();
      ViewModelStore var2 = this.mViewModelStore;
      ViewModelStore var1 = var2;
      if (var2 == null) {
         ComponentActivity.NonConfigurationInstances var4 = (ComponentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
         var1 = var2;
         if (var4 != null) {
            var1 = var4.viewModelStore;
         }
      }

      if (var1 == null && var3 == null) {
         return null;
      } else {
         ComponentActivity.NonConfigurationInstances var5 = new ComponentActivity.NonConfigurationInstances();
         var5.custom = var3;
         var5.viewModelStore = var1;
         return var5;
      }
   }

   protected void onSaveInstanceState(Bundle var1) {
      Lifecycle var2 = this.getLifecycle();
      if (var2 instanceof LifecycleRegistry) {
         ((LifecycleRegistry)var2).setCurrentState(Lifecycle.State.CREATED);
      }

      super.onSaveInstanceState(var1);
      this.mSavedStateRegistryController.performSave(var1);
   }

   static final class NonConfigurationInstances {
      Object custom;
      ViewModelStore viewModelStore;
   }
}
