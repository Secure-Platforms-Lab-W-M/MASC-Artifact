/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

@RequiresApi(value=9)
class TextViewCompatGingerbread {
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompatGingerbread";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;

    TextViewCompatGingerbread() {
    }

    static Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
        return textView.getCompoundDrawables();
    }

    static int getMaxLines(TextView textView) {
        if (!sMaxModeFieldFetched) {
            sMaxModeField = TextViewCompatGingerbread.retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
        }
        if (sMaxModeField != null && TextViewCompatGingerbread.retrieveIntFromField(sMaxModeField, textView) == 1) {
            if (!sMaximumFieldFetched) {
                sMaximumField = TextViewCompatGingerbread.retrieveField("mMaximum");
                sMaximumFieldFetched = true;
            }
            if (sMaximumField != null) {
                return TextViewCompatGingerbread.retrieveIntFromField(sMaximumField, textView);
            }
        }
        return -1;
    }

    static int getMinLines(TextView textView) {
        if (!sMinModeFieldFetched) {
            sMinModeField = TextViewCompatGingerbread.retrieveField("mMinMode");
            sMinModeFieldFetched = true;
        }
        if (sMinModeField != null && TextViewCompatGingerbread.retrieveIntFromField(sMinModeField, textView) == 1) {
            if (!sMinimumFieldFetched) {
                sMinimumField = TextViewCompatGingerbread.retrieveField("mMinimum");
                sMinimumFieldFetched = true;
            }
            if (sMinimumField != null) {
                return TextViewCompatGingerbread.retrieveIntFromField(sMinimumField, textView);
            }
        }
        return -1;
    }

    private static Field retrieveField(String string2) {
        Field field;
        Field field2 = null;
        try {
            field2 = field = TextView.class.getDeclaredField(string2);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"TextViewCompatGingerbread", (String)("Could not retrieve " + string2 + " field."));
            return field2;
        }
        field.setAccessible(true);
        return field;
    }

    private static int retrieveIntFromField(Field field, TextView textView) {
        try {
            int n = field.getInt((Object)textView);
            return n;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.d((String)"TextViewCompatGingerbread", (String)("Could not retrieve value of " + field.getName() + " field."));
            return -1;
        }
    }

    static void setTextAppearance(TextView textView, int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}

