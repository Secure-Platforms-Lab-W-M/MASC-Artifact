package android.support.graphics.drawable;

import android.animation.TypeEvaluator;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ArgbEvaluator implements TypeEvaluator {
   private static final ArgbEvaluator sInstance = new ArgbEvaluator();

   public static ArgbEvaluator getInstance() {
      return sInstance;
   }

   public Object evaluate(float var1, Object var2, Object var3) {
      int var12 = (Integer)var2;
      float var4 = (float)(var12 >> 24 & 255) / 255.0F;
      float var7 = (float)(var12 >> 16 & 255) / 255.0F;
      float var8 = (float)(var12 >> 8 & 255) / 255.0F;
      float var9 = (float)(var12 & 255) / 255.0F;
      var12 = (Integer)var3;
      float var5 = (float)(var12 >> 24 & 255) / 255.0F;
      float var11 = (float)(var12 >> 16 & 255) / 255.0F;
      float var10 = (float)(var12 >> 8 & 255) / 255.0F;
      float var6 = (float)(var12 & 255) / 255.0F;
      var7 = (float)Math.pow((double)var7, 2.2D);
      var8 = (float)Math.pow((double)var8, 2.2D);
      var9 = (float)Math.pow((double)var9, 2.2D);
      var11 = (float)Math.pow((double)var11, 2.2D);
      var10 = (float)Math.pow((double)var10, 2.2D);
      var6 = (float)Math.pow((double)var6, 2.2D);
      var7 = (float)Math.pow((double)((var11 - var7) * var1 + var7), 0.45454545454545453D);
      var8 = (float)Math.pow((double)((var10 - var8) * var1 + var8), 0.45454545454545453D);
      var6 = (float)Math.pow((double)((var6 - var9) * var1 + var9), 0.45454545454545453D);
      return Math.round(((var5 - var4) * var1 + var4) * 255.0F) << 24 | Math.round(var7 * 255.0F) << 16 | Math.round(var8 * 255.0F) << 8 | Math.round(var6 * 255.0F);
   }
}
