package android.arch.lifecycle;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
   private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
   private final ViewModelProvider.Factory mFactory;
   private final ViewModelStore mViewModelStore;

   public ViewModelProvider(@NonNull ViewModelStore var1, @NonNull ViewModelProvider.Factory var2) {
      this.mFactory = var2;
      this.mViewModelStore = var1;
   }

   public ViewModelProvider(@NonNull ViewModelStoreOwner var1, @NonNull ViewModelProvider.Factory var2) {
      this(var1.getViewModelStore(), var2);
   }

   @NonNull
   public ViewModel get(@NonNull Class var1) {
      String var2 = var1.getCanonicalName();
      if (var2 == null) {
         throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
      } else {
         return this.get("android.arch.lifecycle.ViewModelProvider.DefaultKey:" + var2, var1);
      }
   }

   @MainThread
   @NonNull
   public ViewModel get(@NonNull String var1, @NonNull Class var2) {
      ViewModel var3 = this.mViewModelStore.get(var1);
      if (var2.isInstance(var3)) {
         return var3;
      } else {
         if (var3 != null) {
         }

         ViewModel var4 = this.mFactory.create(var2);
         this.mViewModelStore.put(var1, var4);
         return var4;
      }
   }

   public static class AndroidViewModelFactory extends ViewModelProvider.NewInstanceFactory {
      private static ViewModelProvider.AndroidViewModelFactory sInstance;
      private Application mApplication;

      public AndroidViewModelFactory(@NonNull Application var1) {
         this.mApplication = var1;
      }

      public static ViewModelProvider.AndroidViewModelFactory getInstance(@NonNull Application var0) {
         if (sInstance == null) {
            sInstance = new ViewModelProvider.AndroidViewModelFactory(var0);
         }

         return sInstance;
      }

      @NonNull
      public ViewModel create(@NonNull Class var1) {
         if (AndroidViewModel.class.isAssignableFrom(var1)) {
            try {
               ViewModel var2 = (ViewModel)var1.getConstructor(Application.class).newInstance(this.mApplication);
               return var2;
            } catch (NoSuchMethodException var3) {
               throw new RuntimeException("Cannot create an instance of " + var1, var3);
            } catch (IllegalAccessException var4) {
               throw new RuntimeException("Cannot create an instance of " + var1, var4);
            } catch (InstantiationException var5) {
               throw new RuntimeException("Cannot create an instance of " + var1, var5);
            } catch (InvocationTargetException var6) {
               throw new RuntimeException("Cannot create an instance of " + var1, var6);
            }
         } else {
            return super.create(var1);
         }
      }
   }

   public interface Factory {
      @NonNull
      ViewModel create(@NonNull Class var1);
   }

   public static class NewInstanceFactory implements ViewModelProvider.Factory {
      @NonNull
      public ViewModel create(@NonNull Class var1) {
         try {
            ViewModel var2 = (ViewModel)var1.newInstance();
            return var2;
         } catch (InstantiationException var3) {
            throw new RuntimeException("Cannot create an instance of " + var1, var3);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException("Cannot create an instance of " + var1, var4);
         }
      }
   }
}
