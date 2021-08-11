// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatDonut
{
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
    
    static int getMaxLines(final TextView textView) {
        if (!TextViewCompatDonut.sMaxModeFieldFetched) {
            TextViewCompatDonut.sMaxModeField = retrieveField("mMaxMode");
            TextViewCompatDonut.sMaxModeFieldFetched = true;
        }
        if (TextViewCompatDonut.sMaxModeField != null && retrieveIntFromField(TextViewCompatDonut.sMaxModeField, textView) == 1) {
            if (!TextViewCompatDonut.sMaximumFieldFetched) {
                TextViewCompatDonut.sMaximumField = retrieveField("mMaximum");
                TextViewCompatDonut.sMaximumFieldFetched = true;
            }
            if (TextViewCompatDonut.sMaximumField != null) {
                return retrieveIntFromField(TextViewCompatDonut.sMaximumField, textView);
            }
        }
        return -1;
    }
    
    static int getMinLines(final TextView textView) {
        if (!TextViewCompatDonut.sMinModeFieldFetched) {
            TextViewCompatDonut.sMinModeField = retrieveField("mMinMode");
            TextViewCompatDonut.sMinModeFieldFetched = true;
        }
        if (TextViewCompatDonut.sMinModeField != null && retrieveIntFromField(TextViewCompatDonut.sMinModeField, textView) == 1) {
            if (!TextViewCompatDonut.sMinimumFieldFetched) {
                TextViewCompatDonut.sMinimumField = retrieveField("mMinimum");
                TextViewCompatDonut.sMinimumFieldFetched = true;
            }
            if (TextViewCompatDonut.sMinimumField != null) {
                return retrieveIntFromField(TextViewCompatDonut.sMinimumField, textView);
            }
        }
        return -1;
    }
    
    private static Field retrieveField(final String s) {
        Field declaredField = null;
        try {
            final Field field = declaredField = TextView.class.getDeclaredField(s);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException ex) {
            Log.e("TextViewCompatDonut", "Could not retrieve " + s + " field.");
            return declaredField;
        }
    }
    
    private static int retrieveIntFromField(final Field field, final TextView textView) {
        try {
            return field.getInt(textView);
        }
        catch (IllegalAccessException ex) {
            Log.d("TextViewCompatDonut", "Could not retrieve value of " + field.getName() + " field.");
            return -1;
        }
    }
    
    static void setTextAppearance(final TextView textView, final int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}
