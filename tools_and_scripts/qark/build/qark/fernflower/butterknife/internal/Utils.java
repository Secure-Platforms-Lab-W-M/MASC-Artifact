package butterknife.internal;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import java.lang.reflect.Array;
import java.util.List;

public final class Utils {
   private static final TypedValue VALUE = new TypedValue();

   private Utils() {
      throw new AssertionError("No instances.");
   }

   @SafeVarargs
   public static Object[] arrayFilteringNull(Object... var0) {
      int var2 = 0;
      int var4 = var0.length;

      int var3;
      for(int var1 = 0; var1 < var4; var2 = var3) {
         Object var5 = var0[var1];
         var3 = var2;
         if (var5 != null) {
            var0[var2] = var5;
            var3 = var2 + 1;
         }

         ++var1;
      }

      if (var2 == var4) {
         return var0;
      } else {
         Object[] var6 = (Object[])Array.newInstance(var0.getClass().getComponentType(), var2);
         System.arraycopy(var0, 0, var6, 0, var2);
         return var6;
      }
   }

   public static Object castParam(Object var0, String var1, int var2, String var3, int var4, Class var5) {
      try {
         var0 = var5.cast(var0);
         return var0;
      } catch (ClassCastException var6) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Parameter #");
         var7.append(var2 + 1);
         var7.append(" of method '");
         var7.append(var1);
         var7.append("' was of the wrong type for parameter #");
         var7.append(var4 + 1);
         var7.append(" of method '");
         var7.append(var3);
         var7.append("'. See cause for more info.");
         throw new IllegalStateException(var7.toString(), var6);
      }
   }

   public static Object castView(View var0, int var1, String var2, Class var3) {
      try {
         Object var7 = var3.cast(var0);
         return var7;
      } catch (ClassCastException var5) {
         String var6 = getResourceEntryName(var0, var1);
         StringBuilder var4 = new StringBuilder();
         var4.append("View '");
         var4.append(var6);
         var4.append("' with ID ");
         var4.append(var1);
         var4.append(" for ");
         var4.append(var2);
         var4.append(" was of the wrong type. See cause for more info.");
         throw new IllegalStateException(var4.toString(), var5);
      }
   }

   public static Object findOptionalViewAsType(View var0, int var1, String var2, Class var3) {
      return castView(var0.findViewById(var1), var1, var2, var3);
   }

   public static View findRequiredView(View var0, int var1, String var2) {
      View var3 = var0.findViewById(var1);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = getResourceEntryName(var0, var1);
         StringBuilder var5 = new StringBuilder();
         var5.append("Required view '");
         var5.append(var4);
         var5.append("' with ID ");
         var5.append(var1);
         var5.append(" for ");
         var5.append(var2);
         var5.append(" was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.");
         throw new IllegalStateException(var5.toString());
      }
   }

   public static Object findRequiredViewAsType(View var0, int var1, String var2, Class var3) {
      return castView(findRequiredView(var0, var1, var2), var1, var2, var3);
   }

   public static float getFloat(Context var0, int var1) {
      TypedValue var2 = VALUE;
      var0.getResources().getValue(var1, var2, true);
      if (var2.type == 4) {
         return var2.getFloat();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Resource ID #0x");
         var3.append(Integer.toHexString(var1));
         var3.append(" type #0x");
         var3.append(Integer.toHexString(var2.type));
         var3.append(" is not valid");
         throw new NotFoundException(var3.toString());
      }
   }

   private static String getResourceEntryName(View var0, int var1) {
      return var0.isInEditMode() ? "<unavailable while editing>" : var0.getContext().getResources().getResourceEntryName(var1);
   }

   public static Drawable getTintedDrawable(Context var0, int var1, int var2) {
      if (var0.getTheme().resolveAttribute(var2, VALUE, true)) {
         Drawable var4 = DrawableCompat.wrap(ContextCompat.getDrawable(var0, var1).mutate());
         DrawableCompat.setTint(var4, ContextCompat.getColor(var0, VALUE.resourceId));
         return var4;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Required tint color attribute with name ");
         var3.append(var0.getResources().getResourceEntryName(var2));
         var3.append(" and attribute ID ");
         var3.append(var2);
         var3.append(" was not found.");
         throw new NotFoundException(var3.toString());
      }
   }

   @SafeVarargs
   public static List listFilteringNull(Object... var0) {
      return new ImmutableList(arrayFilteringNull(var0));
   }
}
