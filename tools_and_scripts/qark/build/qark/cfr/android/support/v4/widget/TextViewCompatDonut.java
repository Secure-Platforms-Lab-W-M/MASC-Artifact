/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatDonut {
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompatDonut";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;

    TextViewCompatDonut() {
    }

    static int getMaxLines(TextView textView) {
        if (!sMaxModeFieldFetched) {
            sMaxModeField = TextViewCompatDonut.retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
        }
        if (sMaxModeField != null && TextViewCompatDonut.retrieveIntFromField(sMaxModeField, textView) == 1) {
            if (!sMaximumFieldFetched) {
                sMaximumField = TextViewCompatDonut.retrieveField("mMaximum");
                sMaximumFieldFetched = true;
            }
            if (sMaximumField != null) {
                return TextViewCompatDonut.retrieveIntFromField(sMaximumField, textView);
            }
        }
        return -1;
    }

    static int getMinLines(TextView textView) {
        if (!sMinModeFieldFetched) {
            sMinModeField = TextViewCompatDonut.retrieveField("mMinMode");
            sMinModeFieldFetched = true;
        }
        if (sMinModeField != null && TextViewCompatDonut.retrieveIntFromField(sMinModeField, textView) == 1) {
            if (!sMinimumFieldFetched) {
                sMinimumField = TextViewCompatDonut.retrieveField("mMinimum");
                sMinimumFieldFetched = true;
            }
            if (sMinimumField != null) {
                return TextViewCompatDonut.retrieveIntFromField(sMinimumField, textView);
            }
        }
        return -1;
    }

    private static Field retrieveField(String string) {
        Field field;
        Field field2 = null;
        try {
            field2 = field = TextView.class.getDeclaredField(string);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not retrieve ");
            stringBuilder.append(string);
            stringBuilder.append(" field.");
            Log.e((String)"TextViewCompatDonut", (String)stringBuilder.toString());
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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not retrieve value of ");
            stringBuilder.append(field.getName());
            stringBuilder.append(" field.");
            Log.d((String)"TextViewCompatDonut", (String)stringBuilder.toString());
            return -1;
        }
    }

    static void setTextAppearance(TextView textView, int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}

