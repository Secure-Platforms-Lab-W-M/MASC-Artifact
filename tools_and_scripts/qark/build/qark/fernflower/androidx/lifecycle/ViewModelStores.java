package androidx.lifecycle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

@Deprecated
public class ViewModelStores {
   private ViewModelStores() {
   }

   // $FF: renamed from: of (androidx.fragment.app.Fragment) androidx.lifecycle.ViewModelStore
   @Deprecated
   public static ViewModelStore method_45(Fragment var0) {
      return var0.getViewModelStore();
   }

   // $FF: renamed from: of (androidx.fragment.app.FragmentActivity) androidx.lifecycle.ViewModelStore
   @Deprecated
   public static ViewModelStore method_46(FragmentActivity var0) {
      return var0.getViewModelStore();
   }
}
