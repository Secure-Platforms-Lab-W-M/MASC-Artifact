package androidx.lifecycle;

import android.app.Application;
import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class SavedStateViewModelFactory extends ViewModelProvider.KeyedFactory {
   private static final Class[] ANDROID_VIEWMODEL_SIGNATURE = new Class[]{Application.class, SavedStateHandle.class};
   private static final Class[] VIEWMODEL_SIGNATURE = new Class[]{SavedStateHandle.class};
   private final Application mApplication;
   private final Bundle mDefaultArgs;
   private final ViewModelProvider.AndroidViewModelFactory mFactory;
   private final Lifecycle mLifecycle;
   private final SavedStateRegistry mSavedStateRegistry;

   public SavedStateViewModelFactory(Application var1, SavedStateRegistryOwner var2) {
      this(var1, var2, (Bundle)null);
   }

   public SavedStateViewModelFactory(Application var1, SavedStateRegistryOwner var2, Bundle var3) {
      this.mSavedStateRegistry = var2.getSavedStateRegistry();
      this.mLifecycle = var2.getLifecycle();
      this.mDefaultArgs = var3;
      this.mApplication = var1;
      this.mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(var1);
   }

   private static Constructor findMatchingConstructor(Class var0, Class[] var1) {
      Constructor[] var5 = var0.getConstructors();
      int var3 = var5.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Constructor var4 = var5[var2];
         if (Arrays.equals(var1, var4.getParameterTypes())) {
            return var4;
         }
      }

      return null;
   }

   public ViewModel create(Class var1) {
      String var2 = var1.getCanonicalName();
      if (var2 != null) {
         return this.create(var2, var1);
      } else {
         throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
      }
   }

   public ViewModel create(String var1, Class var2) {
      boolean var3 = AndroidViewModel.class.isAssignableFrom(var2);
      Constructor var4;
      if (var3) {
         var4 = findMatchingConstructor(var2, ANDROID_VIEWMODEL_SIGNATURE);
      } else {
         var4 = findMatchingConstructor(var2, VIEWMODEL_SIGNATURE);
      }

      if (var4 == null) {
         return this.mFactory.create(var2);
      } else {
         StringBuilder var19;
         IllegalAccessException var21;
         label52: {
            InstantiationException var20;
            label51: {
               InvocationTargetException var10000;
               label50: {
                  SavedStateHandleController var5 = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, var1, this.mDefaultArgs);
                  ViewModel var15;
                  boolean var10001;
                  if (var3) {
                     try {
                        var15 = (ViewModel)var4.newInstance(this.mApplication, var5.getHandle());
                     } catch (IllegalAccessException var12) {
                        var21 = var12;
                        var10001 = false;
                        break label52;
                     } catch (InstantiationException var13) {
                        var20 = var13;
                        var10001 = false;
                        break label51;
                     } catch (InvocationTargetException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label50;
                     }
                  } else {
                     try {
                        var15 = (ViewModel)var4.newInstance(var5.getHandle());
                     } catch (IllegalAccessException var9) {
                        var21 = var9;
                        var10001 = false;
                        break label52;
                     } catch (InstantiationException var10) {
                        var20 = var10;
                        var10001 = false;
                        break label51;
                     } catch (InvocationTargetException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label50;
                     }
                  }

                  try {
                     var15.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", var5);
                     return var15;
                  } catch (IllegalAccessException var6) {
                     var21 = var6;
                     var10001 = false;
                     break label52;
                  } catch (InstantiationException var7) {
                     var20 = var7;
                     var10001 = false;
                     break label51;
                  } catch (InvocationTargetException var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               InvocationTargetException var16 = var10000;
               var19 = new StringBuilder();
               var19.append("An exception happened in constructor of ");
               var19.append(var2);
               throw new RuntimeException(var19.toString(), var16.getCause());
            }

            InstantiationException var17 = var20;
            var19 = new StringBuilder();
            var19.append("A ");
            var19.append(var2);
            var19.append(" cannot be instantiated.");
            throw new RuntimeException(var19.toString(), var17);
         }

         IllegalAccessException var18 = var21;
         var19 = new StringBuilder();
         var19.append("Failed to access ");
         var19.append(var2);
         throw new RuntimeException(var19.toString(), var18);
      }
   }

   void onRequery(ViewModel var1) {
      SavedStateHandleController.attachHandleIfNeeded(var1, this.mSavedStateRegistry, this.mLifecycle);
   }
}
