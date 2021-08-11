package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.core.R.attr;
import androidx.core.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ColorStateListInflaterCompat {
   private ColorStateListInflaterCompat() {
   }

   public static ColorStateList createFromXml(Resources var0, XmlPullParser var1, Theme var2) throws XmlPullParserException, IOException {
      AttributeSet var4 = Xml.asAttributeSet(var1);

      int var3;
      do {
         var3 = var1.next();
      } while(var3 != 2 && var3 != 1);

      if (var3 == 2) {
         return createFromXmlInner(var0, var1, var4, var2);
      } else {
         throw new XmlPullParserException("No start tag found");
      }
   }

   public static ColorStateList createFromXmlInner(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws XmlPullParserException, IOException {
      String var4 = var1.getName();
      if (var4.equals("selector")) {
         return inflate(var0, var1, var2, var3);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1.getPositionDescription());
         var5.append(": invalid color state list tag ");
         var5.append(var4);
         throw new XmlPullParserException(var5.toString());
      }
   }

   public static ColorStateList inflate(Resources var0, int var1, Theme var2) {
      try {
         ColorStateList var4 = createFromXml(var0, var0.getXml(var1), var2);
         return var4;
      } catch (Exception var3) {
         Log.e("CSLCompat", "Failed to inflate ColorStateList.", var3);
         return null;
      }
   }

   private static ColorStateList inflate(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws XmlPullParserException, IOException {
      int var5 = var1.getDepth() + 1;
      int[][] var13 = new int[20][];
      int[] var14 = new int[var13.length];
      int var6 = 0;

      while(true) {
         int var7 = var1.next();
         if (var7 == 1) {
            break;
         }

         int var8 = var1.getDepth();
         if (var8 < var5 && var7 == 3) {
            break;
         }

         if (var7 == 2 && var8 <= var5 && var1.getName().equals("item")) {
            TypedArray var15 = obtainAttributes(var0, var3, var2, styleable.ColorStateListItem);
            int var11 = var15.getColor(styleable.ColorStateListItem_android_color, -65281);
            float var4 = 1.0F;
            if (var15.hasValue(styleable.ColorStateListItem_android_alpha)) {
               var4 = var15.getFloat(styleable.ColorStateListItem_android_alpha, 1.0F);
            } else if (var15.hasValue(styleable.ColorStateListItem_alpha)) {
               var4 = var15.getFloat(styleable.ColorStateListItem_alpha, 1.0F);
            }

            var15.recycle();
            var8 = 0;
            int var12 = var2.getAttributeCount();
            int[] var16 = new int[var12];

            int var9;
            for(var7 = 0; var7 < var12; var8 = var9) {
               int var10 = var2.getAttributeNameResource(var7);
               var9 = var8;
               if (var10 != 16843173) {
                  var9 = var8;
                  if (var10 != 16843551) {
                     var9 = var8;
                     if (var10 != attr.alpha) {
                        if (var2.getAttributeBooleanValue(var7, false)) {
                           var9 = var10;
                        } else {
                           var9 = -var10;
                        }

                        var16[var8] = var9;
                        var9 = var8 + 1;
                     }
                  }
               }

               ++var7;
            }

            int[] var19 = StateSet.trimStateSet(var16, var8);
            var14 = GrowingArrayUtils.append(var14, var6, modulateColorAlpha(var11, var4));
            var13 = (int[][])GrowingArrayUtils.append(var13, var6, var19);
            ++var6;
         }
      }

      int[] var17 = new int[var6];
      int[][] var18 = new int[var6][];
      System.arraycopy(var14, 0, var17, 0, var6);
      System.arraycopy(var13, 0, var18, 0, var6);
      return new ColorStateList(var18, var17);
   }

   private static int modulateColorAlpha(int var0, float var1) {
      return 16777215 & var0 | Math.round((float)Color.alpha(var0) * var1) << 24;
   }

   private static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      return var1 == null ? var0.obtainAttributes(var2, var3) : var1.obtainStyledAttributes(var2, var3, 0, 0);
   }
}
