package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtilsCompat {
   private AnimationUtilsCompat() {
   }

   private static Interpolator createInterpolatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3) throws XmlPullParserException, IOException {
      Object var7 = null;
      int var4 = var3.getDepth();

      while(true) {
         int var5 = var3.next();
         if (var5 == 3 && var3.getDepth() <= var4 || var5 == 1) {
            return (Interpolator)var7;
         }

         if (var5 == 2) {
            AttributeSet var8 = Xml.asAttributeSet(var3);
            String var9 = var3.getName();
            if (var9.equals("linearInterpolator")) {
               var7 = new LinearInterpolator();
            } else if (var9.equals("accelerateInterpolator")) {
               var7 = new AccelerateInterpolator(var0, var8);
            } else if (var9.equals("decelerateInterpolator")) {
               var7 = new DecelerateInterpolator(var0, var8);
            } else if (var9.equals("accelerateDecelerateInterpolator")) {
               var7 = new AccelerateDecelerateInterpolator();
            } else if (var9.equals("cycleInterpolator")) {
               var7 = new CycleInterpolator(var0, var8);
            } else if (var9.equals("anticipateInterpolator")) {
               var7 = new AnticipateInterpolator(var0, var8);
            } else if (var9.equals("overshootInterpolator")) {
               var7 = new OvershootInterpolator(var0, var8);
            } else if (var9.equals("anticipateOvershootInterpolator")) {
               var7 = new AnticipateOvershootInterpolator(var0, var8);
            } else if (var9.equals("bounceInterpolator")) {
               var7 = new BounceInterpolator();
            } else {
               if (!var9.equals("pathInterpolator")) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Unknown interpolator name: ");
                  var6.append(var3.getName());
                  throw new RuntimeException(var6.toString());
               }

               var7 = new PathInterpolatorCompat(var0, var8, var3);
            }
         }
      }
   }

   public static Interpolator loadInterpolator(Context param0, int param1) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }
}
