package androidx.core.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Factory2;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
   private static final String TAG = "LayoutInflaterCompatHC";
   private static boolean sCheckedField;
   private static Field sLayoutInflaterFactory2Field;

   private LayoutInflaterCompat() {
   }

   private static void forceSetFactory2(LayoutInflater var0, Factory2 var1) {
      Field var2;
      if (!sCheckedField) {
         try {
            var2 = LayoutInflater.class.getDeclaredField("mFactory2");
            sLayoutInflaterFactory2Field = var2;
            var2.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
            var3.append(LayoutInflater.class.getName());
            var3.append("; inflation may have unexpected results.");
            Log.e("LayoutInflaterCompatHC", var3.toString(), var4);
         }

         sCheckedField = true;
      }

      var2 = sLayoutInflaterFactory2Field;
      if (var2 != null) {
         try {
            var2.set(var0, var1);
            return;
         } catch (IllegalAccessException var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
            var6.append(var0);
            var6.append("; inflation may have unexpected results.");
            Log.e("LayoutInflaterCompatHC", var6.toString(), var5);
         }
      }

   }

   @Deprecated
   public static LayoutInflaterFactory getFactory(LayoutInflater var0) {
      Factory var1 = var0.getFactory();
      return var1 instanceof LayoutInflaterCompat.Factory2Wrapper ? ((LayoutInflaterCompat.Factory2Wrapper)var1).mDelegateFactory : null;
   }

   @Deprecated
   public static void setFactory(LayoutInflater var0, LayoutInflaterFactory var1) {
      int var2 = VERSION.SDK_INT;
      Object var4 = null;
      LayoutInflaterCompat.Factory2Wrapper var3 = null;
      if (var2 >= 21) {
         if (var1 != null) {
            var3 = new LayoutInflaterCompat.Factory2Wrapper(var1);
         }

         var0.setFactory2(var3);
      } else {
         var3 = (LayoutInflaterCompat.Factory2Wrapper)var4;
         if (var1 != null) {
            var3 = new LayoutInflaterCompat.Factory2Wrapper(var1);
         }

         var0.setFactory2(var3);
         Factory var5 = var0.getFactory();
         if (var5 instanceof Factory2) {
            forceSetFactory2(var0, (Factory2)var5);
         } else {
            forceSetFactory2(var0, var3);
         }
      }
   }

   public static void setFactory2(LayoutInflater var0, Factory2 var1) {
      var0.setFactory2(var1);
      if (VERSION.SDK_INT < 21) {
         Factory var2 = var0.getFactory();
         if (var2 instanceof Factory2) {
            forceSetFactory2(var0, (Factory2)var2);
            return;
         }

         forceSetFactory2(var0, var1);
      }

   }

   static class Factory2Wrapper implements Factory2 {
      final LayoutInflaterFactory mDelegateFactory;

      Factory2Wrapper(LayoutInflaterFactory var1) {
         this.mDelegateFactory = var1;
      }

      public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
         return this.mDelegateFactory.onCreateView(var1, var2, var3, var4);
      }

      public View onCreateView(String var1, Context var2, AttributeSet var3) {
         return this.mDelegateFactory.onCreateView((View)null, var1, var2, var3);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.getClass().getName());
         var1.append("{");
         var1.append(this.mDelegateFactory);
         var1.append("}");
         return var1.toString();
      }
   }
}
