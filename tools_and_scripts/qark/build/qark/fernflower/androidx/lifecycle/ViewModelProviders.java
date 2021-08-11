package androidx.lifecycle;

import android.app.Application;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

@Deprecated
public class ViewModelProviders {
   // $FF: renamed from: of (androidx.fragment.app.Fragment) androidx.lifecycle.ViewModelProvider
   @Deprecated
   public static ViewModelProvider method_27(Fragment var0) {
      return new ViewModelProvider(var0);
   }

   // $FF: renamed from: of (androidx.fragment.app.Fragment, androidx.lifecycle.ViewModelProvider$Factory) androidx.lifecycle.ViewModelProvider
   @Deprecated
   public static ViewModelProvider method_28(Fragment var0, ViewModelProvider.Factory var1) {
      ViewModelProvider.Factory var2 = var1;
      if (var1 == null) {
         var2 = var0.getDefaultViewModelProviderFactory();
      }

      return new ViewModelProvider(var0.getViewModelStore(), var2);
   }

   // $FF: renamed from: of (androidx.fragment.app.FragmentActivity) androidx.lifecycle.ViewModelProvider
   @Deprecated
   public static ViewModelProvider method_29(FragmentActivity var0) {
      return new ViewModelProvider(var0);
   }

   // $FF: renamed from: of (androidx.fragment.app.FragmentActivity, androidx.lifecycle.ViewModelProvider$Factory) androidx.lifecycle.ViewModelProvider
   @Deprecated
   public static ViewModelProvider method_30(FragmentActivity var0, ViewModelProvider.Factory var1) {
      ViewModelProvider.Factory var2 = var1;
      if (var1 == null) {
         var2 = var0.getDefaultViewModelProviderFactory();
      }

      return new ViewModelProvider(var0.getViewModelStore(), var2);
   }

   @Deprecated
   public static class DefaultFactory extends ViewModelProvider.AndroidViewModelFactory {
      @Deprecated
      public DefaultFactory(Application var1) {
         super(var1);
      }
   }
}
