package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Factory2;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
   static final LayoutInflaterCompat.LayoutInflaterCompatBaseImpl IMPL;
   private static final String TAG = "LayoutInflaterCompatHC";
   private static boolean sCheckedField;
   private static Field sLayoutInflaterFactory2Field;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new LayoutInflaterCompat.LayoutInflaterCompatApi21Impl();
      } else {
         IMPL = new LayoutInflaterCompat.LayoutInflaterCompatBaseImpl();
      }
   }

   private LayoutInflaterCompat() {
   }

   static void forceSetFactory2(LayoutInflater var0, Factory2 var1) {
      if (!sCheckedField) {
         try {
            sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
            sLayoutInflaterFactory2Field.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
            var3.append(LayoutInflater.class.getName());
            var3.append("; inflation may have unexpected results.");
            Log.e("LayoutInflaterCompatHC", var3.toString(), var4);
         }

         sCheckedField = true;
      }

      Field var2 = sLayoutInflaterFactory2Field;
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
      return IMPL.getFactory(var0);
   }

   @Deprecated
   public static void setFactory(@NonNull LayoutInflater var0, @NonNull LayoutInflaterFactory var1) {
      IMPL.setFactory(var0, var1);
   }

   public static void setFactory2(@NonNull LayoutInflater var0, @NonNull Factory2 var1) {
      IMPL.setFactory2(var0, var1);
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

   @RequiresApi(21)
   static class LayoutInflaterCompatApi21Impl extends LayoutInflaterCompat.LayoutInflaterCompatBaseImpl {
      public void setFactory(LayoutInflater var1, LayoutInflaterFactory var2) {
         LayoutInflaterCompat.Factory2Wrapper var3;
         if (var2 != null) {
            var3 = new LayoutInflaterCompat.Factory2Wrapper(var2);
         } else {
            var3 = null;
         }

         var1.setFactory2(var3);
      }

      public void setFactory2(LayoutInflater var1, Factory2 var2) {
         var1.setFactory2(var2);
      }
   }

   static class LayoutInflaterCompatBaseImpl {
      public LayoutInflaterFactory getFactory(LayoutInflater var1) {
         Factory var2 = var1.getFactory();
         return var2 instanceof LayoutInflaterCompat.Factory2Wrapper ? ((LayoutInflaterCompat.Factory2Wrapper)var2).mDelegateFactory : null;
      }

      public void setFactory(LayoutInflater var1, LayoutInflaterFactory var2) {
         LayoutInflaterCompat.Factory2Wrapper var3;
         if (var2 != null) {
            var3 = new LayoutInflaterCompat.Factory2Wrapper(var2);
         } else {
            var3 = null;
         }

         this.setFactory2(var1, var3);
      }

      public void setFactory2(LayoutInflater var1, Factory2 var2) {
         var1.setFactory2(var2);
         Factory var3 = var1.getFactory();
         if (var3 instanceof Factory2) {
            LayoutInflaterCompat.forceSetFactory2(var1, (Factory2)var3);
         } else {
            LayoutInflaterCompat.forceSetFactory2(var1, var2);
         }
      }
   }
}
