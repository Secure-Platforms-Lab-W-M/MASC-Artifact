package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ComplexColorCompat {
   private static final String LOG_TAG = "ComplexColorCompat";
   private int mColor;
   private final ColorStateList mColorStateList;
   private final Shader mShader;

   private ComplexColorCompat(Shader var1, ColorStateList var2, int var3) {
      this.mShader = var1;
      this.mColorStateList = var2;
      this.mColor = var3;
   }

   private static ComplexColorCompat createFromXml(Resources var0, int var1, Theme var2) throws IOException, XmlPullParserException {
      XmlResourceParser var4 = var0.getXml(var1);
      AttributeSet var6 = Xml.asAttributeSet(var4);

      do {
         var1 = var4.next();
      } while(var1 != 2 && var1 != 1);

      if (var1 == 2) {
         String var5 = var4.getName();
         byte var8 = -1;
         int var3 = var5.hashCode();
         if (var3 != 89650992) {
            if (var3 == 1191572447 && var5.equals("selector")) {
               var8 = 0;
            }
         } else if (var5.equals("gradient")) {
            var8 = 1;
         }

         if (var8 != 0) {
            if (var8 == 1) {
               return from(GradientColorInflaterCompat.createFromXmlInner(var0, var4, var6, var2));
            } else {
               StringBuilder var7 = new StringBuilder();
               var7.append(var4.getPositionDescription());
               var7.append(": unsupported complex color tag ");
               var7.append(var5);
               throw new XmlPullParserException(var7.toString());
            }
         } else {
            return from(ColorStateListInflaterCompat.createFromXmlInner(var0, var4, var6, var2));
         }
      } else {
         throw new XmlPullParserException("No start tag found");
      }
   }

   static ComplexColorCompat from(int var0) {
      return new ComplexColorCompat((Shader)null, (ColorStateList)null, var0);
   }

   static ComplexColorCompat from(ColorStateList var0) {
      return new ComplexColorCompat((Shader)null, var0, var0.getDefaultColor());
   }

   static ComplexColorCompat from(Shader var0) {
      return new ComplexColorCompat(var0, (ColorStateList)null, 0);
   }

   public static ComplexColorCompat inflate(Resources var0, int var1, Theme var2) {
      try {
         ComplexColorCompat var4 = createFromXml(var0, var1, var2);
         return var4;
      } catch (Exception var3) {
         Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", var3);
         return null;
      }
   }

   public int getColor() {
      return this.mColor;
   }

   public Shader getShader() {
      return this.mShader;
   }

   public boolean isGradient() {
      return this.mShader != null;
   }

   public boolean isStateful() {
      if (this.mShader == null) {
         ColorStateList var1 = this.mColorStateList;
         if (var1 != null && var1.isStateful()) {
            return true;
         }
      }

      return false;
   }

   public boolean onStateChanged(int[] var1) {
      boolean var4 = false;
      boolean var3 = var4;
      if (this.isStateful()) {
         ColorStateList var5 = this.mColorStateList;
         int var2 = var5.getColorForState(var1, var5.getDefaultColor());
         var3 = var4;
         if (var2 != this.mColor) {
            var3 = true;
            this.mColor = var2;
         }
      }

      return var3;
   }

   public void setColor(int var1) {
      this.mColor = var1;
   }

   public boolean willDraw() {
      return this.isGradient() || this.mColor != 0;
   }
}
