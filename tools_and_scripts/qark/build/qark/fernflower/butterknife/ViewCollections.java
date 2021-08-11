package butterknife;

import android.util.Property;
import android.view.View;
import java.util.List;

public final class ViewCollections {
   private ViewCollections() {
   }

   public static void run(View var0, Action var1) {
      var1.apply(var0, 0);
   }

   @SafeVarargs
   public static void run(View var0, Action... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         var1[var2].apply(var0, 0);
      }

   }

   public static void run(List var0, Action var1) {
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         var1.apply((View)var0.get(var2), var2);
      }

   }

   @SafeVarargs
   public static void run(List var0, Action... var1) {
      int var2 = 0;

      for(int var4 = var0.size(); var2 < var4; ++var2) {
         int var5 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            var1[var3].apply((View)var0.get(var2), var2);
         }
      }

   }

   public static void run(View[] var0, Action var1) {
      int var2 = 0;

      for(int var3 = var0.length; var2 < var3; ++var2) {
         var1.apply(var0[var2], var2);
      }

   }

   @SafeVarargs
   public static void run(View[] var0, Action... var1) {
      int var2 = 0;

      for(int var4 = var0.length; var2 < var4; ++var2) {
         int var5 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            var1[var3].apply(var0[var2], var2);
         }
      }

   }

   public static void set(View var0, Property var1, Object var2) {
      var1.set(var0, var2);
   }

   public static void set(View var0, Setter var1, Object var2) {
      var1.set(var0, var2, 0);
   }

   public static void set(List var0, Property var1, Object var2) {
      int var3 = 0;

      for(int var4 = var0.size(); var3 < var4; ++var3) {
         var1.set(var0.get(var3), var2);
      }

   }

   public static void set(List var0, Setter var1, Object var2) {
      int var3 = 0;

      for(int var4 = var0.size(); var3 < var4; ++var3) {
         var1.set((View)var0.get(var3), var2, var3);
      }

   }

   public static void set(View[] var0, Property var1, Object var2) {
      int var3 = 0;

      for(int var4 = var0.length; var3 < var4; ++var3) {
         var1.set(var0[var3], var2);
      }

   }

   public static void set(View[] var0, Setter var1, Object var2) {
      int var3 = 0;

      for(int var4 = var0.length; var3 < var4; ++var3) {
         var1.set(var0[var3], var2, var3);
      }

   }
}
