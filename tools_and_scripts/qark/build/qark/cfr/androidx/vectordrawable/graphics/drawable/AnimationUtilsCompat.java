/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Xml
 *  android.view.animation.AccelerateDecelerateInterpolator
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AnimationUtils
 *  android.view.animation.AnticipateInterpolator
 *  android.view.animation.AnticipateOvershootInterpolator
 *  android.view.animation.BounceInterpolator
 *  android.view.animation.CycleInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 *  android.view.animation.OvershootInterpolator
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtilsCompat {
    private AnimationUtilsCompat() {
    }

    private static Interpolator createInterpolatorFromXml(Context object, Resources object2, Resources.Theme object3, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int n;
        object2 = null;
        int n2 = xmlPullParser.getDepth();
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            object2 = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
            object3 = xmlPullParser.getName();
            if (object3.equals("linearInterpolator")) {
                object2 = new LinearInterpolator();
                continue;
            }
            if (object3.equals("accelerateInterpolator")) {
                object2 = new AccelerateInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("decelerateInterpolator")) {
                object2 = new DecelerateInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("accelerateDecelerateInterpolator")) {
                object2 = new AccelerateDecelerateInterpolator();
                continue;
            }
            if (object3.equals("cycleInterpolator")) {
                object2 = new CycleInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("anticipateInterpolator")) {
                object2 = new AnticipateInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("overshootInterpolator")) {
                object2 = new OvershootInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("anticipateOvershootInterpolator")) {
                object2 = new AnticipateOvershootInterpolator((Context)object, (AttributeSet)object2);
                continue;
            }
            if (object3.equals("bounceInterpolator")) {
                object2 = new BounceInterpolator();
                continue;
            }
            if (object3.equals("pathInterpolator")) {
                object2 = new PathInterpolatorCompat((Context)object, (AttributeSet)object2, xmlPullParser);
                continue;
            }
            object = new StringBuilder();
            object.append("Unknown interpolator name: ");
            object.append(xmlPullParser.getName());
            throw new RuntimeException(object.toString());
        }
        return object2;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static Interpolator loadInterpolator(Context var0, int var1_4) throws Resources.NotFoundException {
        block12 : {
            block13 : {
                if (Build.VERSION.SDK_INT >= 21) {
                    return AnimationUtils.loadInterpolator((Context)var0, (int)var1_4);
                }
                var3_5 = null;
                var4_6 = null;
                var2_7 = null;
                if (var1_4 == 17563663) {
                    var0 = new FastOutLinearInInterpolator();
                    if (false == false) return var0;
                    throw new NullPointerException();
                }
                if (var1_4 == 17563661) {
                    var0 = new FastOutSlowInInterpolator();
                    if (false == false) return var0;
                    throw new NullPointerException();
                }
                if (var1_4 == 17563662) {
                    var0 = new LinearOutSlowInInterpolator();
                    if (false == false) return var0;
                    throw new NullPointerException();
                }
                try {
                    var5_8 = var0.getResources().getAnimation(var1_4);
                    var2_7 = var5_8;
                    var3_5 = var5_8;
                    var4_6 = var5_8;
                    var0 = AnimationUtilsCompat.createInterpolatorFromXml((Context)var0, var0.getResources(), var0.getTheme(), (XmlPullParser)var5_8);
                    if (var5_8 == null) return var0;
                }
                catch (Throwable var0_1) {
                    break block12;
                }
                catch (IOException var0_2) {
                    break block13;
                }
                catch (XmlPullParserException var0_3) {
                    ** GOTO lbl49
                }
                var5_8.close();
                return var0;
            }
            var2_7 = var3_5;
            {
                var4_6 = new StringBuilder();
                var2_7 = var3_5;
                var4_6.append("Can't load animation resource ID #0x");
                var2_7 = var3_5;
                var4_6.append(Integer.toHexString(var1_4));
                var2_7 = var3_5;
                var4_6 = new Resources.NotFoundException(var4_6.toString());
                var2_7 = var3_5;
                var4_6.initCause((Throwable)var0_2);
                var2_7 = var3_5;
                throw var4_6;
lbl49: // 1 sources:
                var2_7 = var4_6;
                var3_5 = new StringBuilder();
                var2_7 = var4_6;
                var3_5.append("Can't load animation resource ID #0x");
                var2_7 = var4_6;
                var3_5.append(Integer.toHexString(var1_4));
                var2_7 = var4_6;
                var3_5 = new Resources.NotFoundException(var3_5.toString());
                var2_7 = var4_6;
                var3_5.initCause((Throwable)var0_3);
                var2_7 = var4_6;
                throw var3_5;
            }
        }
        if (var2_7 == null) throw var0_1;
        var2_7.close();
        throw var0_1;
    }
}

