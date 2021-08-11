/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.XmlResourceParser
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package com.google.android.material.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableUtils {
    private DrawableUtils() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static AttributeSet parseDrawableXml(Context object, int n, CharSequence charSequence) {
        void var0_3;
        int n2;
        object = object.getResources().getXml(n);
        while ((n2 = object.next()) != 2 && n2 != 1) {
        }
        if (n2 != 2) throw new XmlPullParserException("No start tag found");
        try {
            if (TextUtils.equals((CharSequence)object.getName(), (CharSequence)charSequence)) {
                return Xml.asAttributeSet((XmlPullParser)object);
            }
            object = new StringBuilder();
            object.append("Must have a <");
            object.append((Object)charSequence);
            object.append("> start tag");
            throw new XmlPullParserException(object.toString());
        }
        catch (IOException iOException) {
        }
        catch (XmlPullParserException xmlPullParserException) {
            // empty catch block
        }
        charSequence = new StringBuilder();
        charSequence.append("Can't load badge resource ID #0x");
        charSequence.append(Integer.toHexString(n));
        charSequence = new Resources.NotFoundException(charSequence.toString());
        charSequence.initCause((Throwable)var0_3);
        throw charSequence;
    }

    public static PorterDuffColorFilter updateTintFilter(Drawable drawable2, ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList != null && mode != null) {
            return new PorterDuffColorFilter(colorStateList.getColorForState(drawable2.getState(), 0), mode);
        }
        return null;
    }
}

