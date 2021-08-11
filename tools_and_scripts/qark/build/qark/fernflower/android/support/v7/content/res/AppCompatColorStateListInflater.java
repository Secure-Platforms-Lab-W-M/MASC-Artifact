package android.support.v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class AppCompatColorStateListInflater {
   private static final int DEFAULT_COLOR = -65536;

   private AppCompatColorStateListInflater() {
   }

   @NonNull
   public static ColorStateList createFromXml(@NonNull Resources var0, @NonNull XmlPullParser var1, @Nullable Theme var2) throws XmlPullParserException, IOException {
      AttributeSet var4 = Xml.asAttributeSet(var1);

      int var3;
      do {
         var3 = var1.next();
      } while(var3 != 2 && var3 != 1);

      if (var3 == 2) {
         return createFromXmlInner(var0, var1, var4, var2);
      } else {
         XmlPullParserException var5 = new XmlPullParserException("No start tag found");
         throw var5;
      }
   }

   @NonNull
   private static ColorStateList createFromXmlInner(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
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

   private static ColorStateList inflate(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
      int var6 = var1.getDepth() + 1;
      int var5 = -65536;
      int[][] var14 = new int[20][];
      int[] var15 = new int[var14.length];
      int var7 = 0;

      while(true) {
         int var8 = var1.next();
         if (var8 == 1) {
            break;
         }

         int var9 = var1.getDepth();
         if (var9 < var6 && var8 == 3) {
            break;
         }

         if (var8 == 2 && var9 <= var6 && var1.getName().equals("item")) {
            TypedArray var16 = obtainAttributes(var0, var3, var2, R$styleable.ColorStateListItem);
            int var13 = var16.getColor(R$styleable.ColorStateListItem_android_color, -65281);
            float var4 = 1.0F;
            if (var16.hasValue(R$styleable.ColorStateListItem_android_alpha)) {
               var4 = var16.getFloat(R$styleable.ColorStateListItem_android_alpha, 1.0F);
            } else if (var16.hasValue(R$styleable.ColorStateListItem_alpha)) {
               var4 = var16.getFloat(R$styleable.ColorStateListItem_alpha, 1.0F);
            }

            var16.recycle();
            var9 = var2.getAttributeCount();
            int[] var19 = new int[var9];
            var8 = 0;

            int var11;
            for(int var10 = 0; var10 < var9; var8 = var11) {
               int var12 = var2.getAttributeNameResource(var10);
               var11 = var8;
               if (var12 != 16843173) {
                  var11 = var8;
                  if (var12 != 16843551) {
                     var11 = var8;
                     if (var12 != R$attr.alpha) {
                        if (var2.getAttributeBooleanValue(var10, false)) {
                           var11 = var12;
                        } else {
                           var11 = -var12;
                        }

                        var19[var8] = var11;
                        var11 = var8 + 1;
                     }
                  }
               }

               ++var10;
            }

            var19 = StateSet.trimStateSet(var19, var8);
            var8 = modulateColorAlpha(var13, var4);
            if (var7 == 0 || var19.length == 0) {
               ;
            }

            var15 = GrowingArrayUtils.append(var15, var7, var8);
            var14 = (int[][])GrowingArrayUtils.append(var14, var7, var19);
            ++var7;
         }
      }

      int[] var17 = new int[var7];
      int[][] var18 = new int[var7][];
      System.arraycopy(var15, 0, var17, 0, var7);
      System.arraycopy(var14, 0, var18, 0, var7);
      return new ColorStateList(var18, var17);
   }

   private static int modulateColorAlpha(int var0, float var1) {
      return ColorUtils.setAlphaComponent(var0, Math.round((float)Color.alpha(var0) * var1));
   }

   private static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      return var1 == null ? var0.obtainAttributes(var2, var3) : var1.obtainStyledAttributes(var2, var3, 0, 0);
   }
}
