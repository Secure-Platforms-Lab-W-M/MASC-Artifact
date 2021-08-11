/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.TypedValue
 *  android.widget.TextView
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    private ResourcesCompat() {
    }

    @ColorInt
    public static int getColor(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(n, theme);
        }
        return resources.getColor(n);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, theme);
        }
        return resources.getColorStateList(n);
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, theme);
        }
        return resources.getDrawable(n);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources resources, @DrawableRes int n, int n2, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }

    @Nullable
    public static Typeface getFont(@NonNull Context context, @FontRes int n) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, new TypedValue(), 0, null);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Typeface getFont(@NonNull Context context, @FontRes int n, TypedValue typedValue, int n2, @Nullable TextView textView) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, typedValue, n2, textView);
    }

    private static Typeface loadFont(@NonNull Context object, int n, TypedValue typedValue, int n2, @Nullable TextView textView) {
        Resources resources = object.getResources();
        resources.getValue(n, typedValue, true);
        object = ResourcesCompat.loadFont((Context)object, resources, typedValue, n, n2, textView);
        if (object != null) {
            return object;
        }
        object = new StringBuilder();
        object.append("Font resource ID #0x");
        object.append(Integer.toHexString(n));
        throw new Resources.NotFoundException(object.toString());
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static Typeface loadFont(@NonNull Context object, Resources object2, TypedValue object3, int n, int n2, @Nullable TextView textView) {
        void var0_6;
        block11 : {
            void var0_4;
            block10 : {
                block9 : {
                    if (object3.string == null) {
                        object = new StringBuilder();
                        object.append("Resource \"");
                        object.append(object2.getResourceName(n));
                        object.append("\" (");
                        object.append(Integer.toHexString(n));
                        object.append(") is not a Font: ");
                        object.append(object3);
                        throw new Resources.NotFoundException(object.toString());
                    }
                    object3 = object3.string.toString();
                    if (!object3.startsWith("res/")) {
                        return null;
                    }
                    Object object4 = TypefaceCompat.findFromCache((Resources)object2, n, n2);
                    if (object4 != null) {
                        return object4;
                    }
                    if (!object3.toLowerCase().endsWith(".xml")) break block9;
                    object4 = FontResourcesParserCompat.parse((XmlPullParser)object2.getXml(n), (Resources)object2);
                    if (object4 != null) return TypefaceCompat.createFromResourcesFamilyXml((Context)object, (FontResourcesParserCompat.FamilyResourceEntry)object4, (Resources)object2, n, n2, textView);
                    Log.e((String)"ResourcesCompat", (String)"Failed to find font-family tag");
                    return null;
                }
                try {
                    return TypefaceCompat.createFromResourcesFontFile((Context)object, (Resources)object2, n, (String)object3, n2);
                }
                catch (IOException iOException) {
                    break block10;
                }
                catch (XmlPullParserException xmlPullParserException) {
                    break block11;
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            object2 = new StringBuilder();
            object2.append("Failed to read xml resource ");
            object2.append((String)object3);
            Log.e((String)"ResourcesCompat", (String)object2.toString(), (Throwable)var0_4);
            return null;
            catch (XmlPullParserException xmlPullParserException) {
                // empty catch block
            }
        }
        object2 = new StringBuilder();
        object2.append("Failed to parse xml resource ");
        object2.append((String)object3);
        Log.e((String)"ResourcesCompat", (String)object2.toString(), (Throwable)var0_6);
        return null;
    }
}

