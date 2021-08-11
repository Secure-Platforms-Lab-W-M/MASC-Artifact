// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.util.Log;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class TextViewCompatGingerbread
{
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
    
    static Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
        return textView.getCompoundDrawables();
    }
    
    static int getMaxLines(final TextView textView) {
        if (!TextViewCompatGingerbread.sMaxModeFieldFetched) {
            TextViewCompatGingerbread.sMaxModeField = retrieveField("mMaxMode");
            TextViewCompatGingerbread.sMaxModeFieldFetched = true;
        }
        if (TextViewCompatGingerbread.sMaxModeField != null && retrieveIntFromField(TextViewCompatGingerbread.sMaxModeField, textView) == 1) {
            if (!TextViewCompatGingerbread.sMaximumFieldFetched) {
                TextViewCompatGingerbread.sMaximumField = retrieveField("mMaximum");
                TextViewCompatGingerbread.sMaximumFieldFetched = true;
            }
            if (TextViewCompatGingerbread.sMaximumField != null) {
                return retrieveIntFromField(TextViewCompatGingerbread.sMaximumField, textView);
            }
        }
        return -1;
    }
    
    static int getMinLines(final TextView textView) {
        if (!TextViewCompatGingerbread.sMinModeFieldFetched) {
            TextViewCompatGingerbread.sMinModeField = retrieveField("mMinMode");
            TextViewCompatGingerbread.sMinModeFieldFetched = true;
        }
        if (TextViewCompatGingerbread.sMinModeField != null && retrieveIntFromField(TextViewCompatGingerbread.sMinModeField, textView) == 1) {
            if (!TextViewCompatGingerbread.sMinimumFieldFetched) {
                TextViewCompatGingerbread.sMinimumField = retrieveField("mMinimum");
                TextViewCompatGingerbread.sMinimumFieldFetched = true;
            }
            if (TextViewCompatGingerbread.sMinimumField != null) {
                return retrieveIntFromField(TextViewCompatGingerbread.sMinimumField, textView);
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
            Log.e("TextViewCompatGingerbread", "Could not retrieve " + s + " field.");
            return declaredField;
        }
    }
    
    private static int retrieveIntFromField(final Field field, final TextView textView) {
        try {
            return field.getInt(textView);
        }
        catch (IllegalAccessException ex) {
            Log.d("TextViewCompatGingerbread", "Could not retrieve value of " + field.getName() + " field.");
            return -1;
        }
    }
    
    static void setTextAppearance(final TextView textView, final int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}
