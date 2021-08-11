package android.support.v4.util;

import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import java.util.Objects;

public class ObjectsCompat {
   private static final ObjectsCompat.ImplBase IMPL;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new ObjectsCompat.ImplApi19();
      } else {
         IMPL = new ObjectsCompat.ImplBase();
      }
   }

   private ObjectsCompat() {
   }

   public static boolean equals(Object var0, Object var1) {
      return IMPL.equals(var0, var1);
   }

   @RequiresApi(19)
   private static class ImplApi19 extends ObjectsCompat.ImplBase {
      private ImplApi19() {
         super(null);
      }

      // $FF: synthetic method
      ImplApi19(Object var1) {
         this();
      }

      public boolean equals(Object var1, Object var2) {
         return Objects.equals(var1, var2);
      }
   }

   private static class ImplBase {
      private ImplBase() {
      }

      // $FF: synthetic method
      ImplBase(Object var1) {
         this();
      }

      public boolean equals(Object var1, Object var2) {
         return var1 == var2 || var1 != null && var1.equals(var2);
      }
   }
}
