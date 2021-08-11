package androidx.databinding.adapters;

import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import androidx.databinding.InverseBindingListener;

public class TabHostBindingAdapter {
   public static int getCurrentTab(TabHost var0) {
      return var0.getCurrentTab();
   }

   public static String getCurrentTabTag(TabHost var0) {
      return var0.getCurrentTabTag();
   }

   public static void setCurrentTab(TabHost var0, int var1) {
      if (var0.getCurrentTab() != var1) {
         var0.setCurrentTab(var1);
      }

   }

   public static void setCurrentTabTag(TabHost var0, String var1) {
      if (var0.getCurrentTabTag() != var1) {
         var0.setCurrentTabByTag(var1);
      }

   }

   public static void setListeners(TabHost var0, final OnTabChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnTabChangedListener(var1);
      } else {
         var0.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String var1x) {
               OnTabChangeListener var2x = var1;
               if (var2x != null) {
                  var2x.onTabChanged(var1x);
               }

               var2.onChange();
            }
         });
      }
   }
}
