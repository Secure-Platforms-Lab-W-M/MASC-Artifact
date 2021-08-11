package androidx.databinding.adapters;

import android.util.SparseBooleanArray;
import android.widget.TableLayout;
import java.util.regex.Pattern;

public class TableLayoutBindingAdapter {
   private static final int MAX_COLUMNS = 20;
   private static Pattern sColumnPattern = Pattern.compile("\\s*,\\s*");

   private static SparseBooleanArray parseColumns(CharSequence var0) {
      SparseBooleanArray var4 = new SparseBooleanArray();
      if (var0 == null) {
         return var4;
      } else {
         String[] var8 = sColumnPattern.split(var0);
         int var2 = var8.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            String var5 = var8[var1];

            boolean var10001;
            int var3;
            try {
               var3 = Integer.parseInt(var5);
            } catch (NumberFormatException var7) {
               var10001 = false;
               continue;
            }

            if (var3 >= 0) {
               try {
                  var4.put(var3, true);
               } catch (NumberFormatException var6) {
                  var10001 = false;
               }
            }
         }

         return var4;
      }
   }

   public static void setCollapseColumns(TableLayout var0, CharSequence var1) {
      SparseBooleanArray var4 = parseColumns(var1);

      for(int var2 = 0; var2 < 20; ++var2) {
         boolean var3 = var4.get(var2, false);
         if (var3 != var0.isColumnCollapsed(var2)) {
            var0.setColumnCollapsed(var2, var3);
         }
      }

   }

   public static void setShrinkColumns(TableLayout var0, CharSequence var1) {
      if (var1 != null && var1.length() > 0 && var1.charAt(0) == '*') {
         var0.setShrinkAllColumns(true);
      } else {
         var0.setShrinkAllColumns(false);
         SparseBooleanArray var6 = parseColumns(var1);
         int var3 = var6.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            int var4 = var6.keyAt(var2);
            boolean var5 = var6.valueAt(var2);
            if (var5) {
               var0.setColumnShrinkable(var4, var5);
            }
         }

      }
   }

   public static void setStretchColumns(TableLayout var0, CharSequence var1) {
      if (var1 != null && var1.length() > 0 && var1.charAt(0) == '*') {
         var0.setStretchAllColumns(true);
      } else {
         var0.setStretchAllColumns(false);
         SparseBooleanArray var6 = parseColumns(var1);
         int var3 = var6.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            int var4 = var6.keyAt(var2);
            boolean var5 = var6.valueAt(var2);
            if (var5) {
               var0.setColumnStretchable(var4, var5);
            }
         }

      }
   }
}
