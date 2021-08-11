package androidx.databinding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DataBindingUtil {
   private static DataBindingComponent sDefaultComponent = null;
   private static DataBinderMapper sMapper = new DataBinderMapperImpl();

   private DataBindingUtil() {
   }

   public static ViewDataBinding bind(View var0) {
      return bind(var0, sDefaultComponent);
   }

   public static ViewDataBinding bind(View var0, DataBindingComponent var1) {
      ViewDataBinding var3 = getBinding(var0);
      if (var3 != null) {
         return var3;
      } else {
         Object var6 = var0.getTag();
         if (var6 instanceof String) {
            String var4 = (String)var6;
            int var2 = sMapper.getLayoutId(var4);
            if (var2 != 0) {
               return sMapper.getDataBinder(var1, var0, var2);
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("View is not a binding layout. Tag: ");
               var5.append(var6);
               throw new IllegalArgumentException(var5.toString());
            }
         } else {
            throw new IllegalArgumentException("View is not a binding layout");
         }
      }
   }

   static ViewDataBinding bind(DataBindingComponent var0, View var1, int var2) {
      return sMapper.getDataBinder(var0, var1, var2);
   }

   static ViewDataBinding bind(DataBindingComponent var0, View[] var1, int var2) {
      return sMapper.getDataBinder(var0, var1, var2);
   }

   private static ViewDataBinding bindToAddedViews(DataBindingComponent var0, ViewGroup var1, int var2, int var3) {
      int var4 = var1.getChildCount();
      int var5 = var4 - var2;
      if (var5 == 1) {
         return bind(var0, var1.getChildAt(var4 - 1), var3);
      } else {
         View[] var6 = new View[var5];

         for(var4 = 0; var4 < var5; ++var4) {
            var6[var4] = var1.getChildAt(var4 + var2);
         }

         return bind(var0, var6, var3);
      }
   }

   public static String convertBrIdToString(int var0) {
      return sMapper.convertBrIdToString(var0);
   }

   public static ViewDataBinding findBinding(View var0) {
      while(var0 != null) {
         ViewDataBinding var6 = ViewDataBinding.getBinding(var0);
         if (var6 != null) {
            return var6;
         }

         Object var8 = var0.getTag();
         if (var8 instanceof String) {
            String var9 = (String)var8;
            if (var9.startsWith("layout") && var9.endsWith("_0")) {
               char var5 = var9.charAt(6);
               int var4 = var9.indexOf(47, 7);
               boolean var2 = false;
               boolean var3 = false;
               boolean var1 = false;
               if (var5 == '/') {
                  if (var4 == -1) {
                     var1 = true;
                  }
               } else {
                  var1 = var2;
                  if (var5 == '-') {
                     var1 = var2;
                     if (var4 != -1) {
                        var1 = var3;
                        if (var9.indexOf(47, var4 + 1) == -1) {
                           var1 = true;
                        }
                     }
                  }
               }

               if (var1) {
                  return null;
               }
            }
         }

         ViewParent var7 = var0.getParent();
         if (var7 instanceof View) {
            var0 = (View)var7;
         } else {
            var0 = null;
         }
      }

      return null;
   }

   public static ViewDataBinding getBinding(View var0) {
      return ViewDataBinding.getBinding(var0);
   }

   public static DataBindingComponent getDefaultComponent() {
      return sDefaultComponent;
   }

   public static ViewDataBinding inflate(LayoutInflater var0, int var1, ViewGroup var2, boolean var3) {
      return inflate(var0, var1, var2, var3, sDefaultComponent);
   }

   public static ViewDataBinding inflate(LayoutInflater var0, int var1, ViewGroup var2, boolean var3, DataBindingComponent var4) {
      int var6 = 0;
      boolean var5;
      if (var2 != null && var3) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         var6 = var2.getChildCount();
      }

      View var7 = var0.inflate(var1, var2, var3);
      return var5 ? bindToAddedViews(var4, var2, var6, var1) : bind(var4, var7, var1);
   }

   public static ViewDataBinding setContentView(Activity var0, int var1) {
      return setContentView(var0, var1, sDefaultComponent);
   }

   public static ViewDataBinding setContentView(Activity var0, int var1, DataBindingComponent var2) {
      var0.setContentView(var1);
      return bindToAddedViews(var2, (ViewGroup)var0.getWindow().getDecorView().findViewById(16908290), 0, var1);
   }

   public static void setDefaultComponent(DataBindingComponent var0) {
      sDefaultComponent = var0;
   }
}
