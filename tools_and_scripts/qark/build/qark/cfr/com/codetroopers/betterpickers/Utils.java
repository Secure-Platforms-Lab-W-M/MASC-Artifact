/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 */
package com.codetroopers.betterpickers;

import android.os.Build;
import android.view.View;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class Utils {
    public static final int FULL_ALPHA = 255;
    public static final int MONDAY_BEFORE_JULIAN_EPOCH = 2440585;
    public static final int PULSE_ANIMATOR_DURATION = 544;
    static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";

    public static int formatDisabledDayForKey(int n, int n2, int n3) {
        return n * 10000 + n2 * 100 + n3;
    }

    public static int getDaysInMonth(int n, int n2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("Invalid Month");
            }
            case 3: 
            case 5: 
            case 8: 
            case 10: {
                return 30;
            }
            case 1: {
                if (n2 % 4 == 0 && n2 % 100 != 0 || n2 % 400 == 0) {
                    return 29;
                }
                return 28;
            }
            case 0: 
            case 2: 
            case 4: 
            case 6: 
            case 7: 
            case 9: 
            case 11: 
        }
        return 31;
    }

    public static int getJulianMondayFromWeeksSinceEpoch(int n) {
        return n * 7 + 2440585;
    }

    public static ObjectAnimator getPulseAnimator(View object, float f, float f2) {
        Cloneable cloneable = Keyframe.ofFloat(0.0f, 1.0f);
        Keyframe keyframe = Keyframe.ofFloat(0.275f, f);
        Keyframe keyframe2 = Keyframe.ofFloat(0.69f, f2);
        Keyframe keyframe3 = Keyframe.ofFloat(1.0f, 1.0f);
        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("scaleX", new Keyframe[]{cloneable, keyframe, keyframe2, keyframe3});
        cloneable = PropertyValuesHolder.ofKeyframe("scaleY", new Keyframe[]{cloneable, keyframe, keyframe2, keyframe3});
        if (AnimatorProxy.NEEDS_PROXY) {
            object = AnimatorProxy.wrap((View)object);
        }
        object = ObjectAnimator.ofPropertyValuesHolder(object, new PropertyValuesHolder[]{propertyValuesHolder, cloneable});
        object.setDuration(544L);
        return object;
    }

    public static int getWeeksSinceEpochFromJulianDay(int n, int n2) {
        int n3;
        n2 = n3 = 4 - n2;
        if (n3 < 0) {
            n2 = n3 + 7;
        }
        return (n - (2440588 - n2)) / 7;
    }

    public static boolean isJellybeanOrLater() {
        if (Build.VERSION.SDK_INT >= 16) {
            return true;
        }
        return false;
    }

    public static void tryAccessibilityAnnounce(View view, CharSequence charSequence) {
        if (Utils.isJellybeanOrLater() && view != null && charSequence != null) {
            view.announceForAccessibility(charSequence);
        }
    }
}

