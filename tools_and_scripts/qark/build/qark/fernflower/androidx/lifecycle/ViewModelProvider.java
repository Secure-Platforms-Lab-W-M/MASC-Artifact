package androidx.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
   private static final String DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey";
   private final ViewModelProvider.Factory mFactory;
   private final ViewModelStore mViewModelStore;

   public ViewModelProvider(ViewModelStore var1, ViewModelProvider.Factory var2) {
      this.mFactory = var2;
      this.mViewModelStore = var1;
   }

   public ViewModelProvider(ViewModelStoreOwner var1) {
      ViewModelStore var2 = var1.getViewModelStore();
      Object var3;
      if (var1 instanceof HasDefaultViewModelProviderFactory) {
         var3 = ((HasDefaultViewModelProviderFactory)var1).getDefaultViewModelProviderFactory();
      } else {
         var3 = ViewModelProvider.NewInstanceFactory.getInstance();
      }

      this((ViewModelStore)var2, (ViewModelProvider.Factory)var3);
   }

   public ViewModelProvider(ViewModelStoreOwner var1, ViewModelProvider.Factory var2) {
      this(var1.getViewModelStore(), var2);
   }

   public ViewModel get(Class var1) {
      String var2 = var1.getCanonicalName();
      if (var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("androidx.lifecycle.ViewModelProvider.DefaultKey:");
         var3.append(var2);
         return this.get(var3.toString(), var1);
      } else {
         throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
      }
   }

   public ViewModel get(String var1, Class var2) {
      ViewModel var3 = this.mViewModelStore.get(var1);
      if (var2.isInstance(var3)) {
         ViewModelProvider.Factory var4 = this.mFactory;
         if (var4 instanceof ViewModelProvider.OnRequeryFactory) {
            ((ViewModelProvider.OnRequeryFactory)var4).onRequery(var3);
         }

         return var3;
      } else {
         ViewModelProvider.Factory var6 = this.mFactory;
         ViewModel var5;
         if (var6 instanceof ViewModelProvider.KeyedFactory) {
            var5 = ((ViewModelProvider.KeyedFactory)((ViewModelProvider.KeyedFactory)var6)).create(var1, var2);
         } else {
            var5 = var6.create(var2);
         }

         this.mViewModelStore.put(var1, var5);
         return var5;
      }
   }

   public static class AndroidViewModelFactory extends ViewModelProvider.NewInstanceFactory {
      private static ViewModelProvider.AndroidViewModelFactory sInstance;
      private Application mApplication;

      public AndroidViewModelFactory(Application var1) {
         this.mApplication = var1;
      }

      public static ViewModelProvider.AndroidViewModelFactory getInstance(Application var0) {
         if (sInstance == null) {
            sInstance = new ViewModelProvider.AndroidViewModelFactory(var0);
         }

         return sInstance;
      }

      public ViewModel create(Class var1) {
         if (AndroidViewModel.class.isAssignableFrom(var1)) {
            StringBuilder var3;
            try {
               ViewModel var2 = (ViewModel)var1.getConstructor(Application.class).newInstance(this.mApplication);
               return var2;
            } catch (NoSuchMethodException var4) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var4);
            } catch (IllegalAccessException var5) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var5);
            } catch (InstantiationException var6) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var6);
            } catch (InvocationTargetException var7) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var7);
            }
         } else {
            return super.create(var1);
         }
      }
   }

   public interface Factory {
      ViewModel create(Class var1);
   }

   abstract static class KeyedFactory extends ViewModelProvider.OnRequeryFactory implements ViewModelProvider.Factory {
      public ViewModel create(Class var1) {
         throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory");
      }

      public abstract ViewModel create(String var1, Class var2);
   }

   public static class NewInstanceFactory implements ViewModelProvider.Factory {
      private static ViewModelProvider.NewInstanceFactory sInstance;

      static ViewModelProvider.NewInstanceFactory getInstance() {
         if (sInstance == null) {
            sInstance = new ViewModelProvider.NewInstanceFactory();
         }

         return sInstance;
      }

      public ViewModel create(Class var1) {
         StringBuilder var3;
         try {
            ViewModel var2 = (ViewModel)var1.newInstance();
            return var2;
         } catch (InstantiationException var4) {
            var3 = new StringBuilder();
            var3.append("Cannot create an instance of ");
            var3.append(var1);
            throw new RuntimeException(var3.toString(), var4);
         } catch (IllegalAccessException var5) {
            var3 = new StringBuilder();
            var3.append("Cannot create an instance of ");
            var3.append(var1);
            throw new RuntimeException(var3.toString(), var5);
         }
      }
   }

   static class OnRequeryFactory {
      void onRequery(ViewModel var1) {
      }
   }
}
