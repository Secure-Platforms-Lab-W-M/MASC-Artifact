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
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.Log
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    private ResourcesCompat() {
    }

    public static int getColor(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(n, theme);
        }
        return resources.getColor(n);
    }

    public static ColorStateList getColorStateList(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, theme);
        }
        return resources.getColorStateList(n);
    }

    public static Drawable getDrawable(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, theme);
        }
        return resources.getDrawable(n);
    }

    public static Drawable getDrawableForDensity(Resources resources, int n, int n2, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }

    public static float getFloat(Resources object, int n) {
        TypedValue typedValue = new TypedValue();
        object.getValue(n, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        object = new StringBuilder();
        object.append("Resource ID #0x");
        object.append(Integer.toHexString(n));
        object.append(" type #0x");
        object.append(Integer.toHexString(typedValue.type));
        object.append(" is not valid");
        throw new Resources.NotFoundException(object.toString());
    }

    public static Typeface getFont(Context context, int n) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, new TypedValue(), 0, null, null, false);
    }

    public static Typeface getFont(Context context, int n, TypedValue typedValue, int n2, FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }

    public static void getFont(Context context, int n, FontCallback fontCallback, Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        ResourcesCompat.loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }

    private static Typeface loadFont(Context object, int n, TypedValue typedValue, int n2, FontCallback fontCallback, Handler handler, boolean bl) {
        Resources resources = object.getResources();
        resources.getValue(n, typedValue, true);
        object = ResourcesCompat.loadFont((Context)object, resources, typedValue, n, n2, fontCallback, handler, bl);
        if (object == null) {
            if (fontCallback != null) {
                return object;
            }
            object = new StringBuilder();
            object.append("Font resource ID #0x");
            object.append(Integer.toHexString(n));
            object.append(" could not be retrieved.");
            throw new Resources.NotFoundException(object.toString());
        }
        return object;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static Typeface loadFont(Context object, Resources object2, TypedValue object3, int n, int n2, FontCallback fontCallback, Handler handler, boolean bl) {
        block25 : {
            block24 : {
                block23 : {
                    block21 : {
                        block22 : {
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
                                if (fontCallback == null) return null;
                                fontCallback.callbackFailAsync(-3, handler);
                                return null;
                            }
                            object4 = TypefaceCompat.findFromCache((Resources)object2, n, n2);
                            if (object4 != null) {
                                if (fontCallback == null) return object4;
                                fontCallback.callbackSuccessAsync((Typeface)object4, handler);
                                return object4;
                            }
                            bl2 = object3.toLowerCase().endsWith(".xml");
                            if (!bl2) break block21;
                            object4 = FontResourcesParserCompat.parse((XmlPullParser)object2.getXml(n), (Resources)object2);
                            if (object4 != null) break block22;
                            try {
                                Log.e((String)"ResourcesCompat", (String)"Failed to find font-family tag");
                                if (fontCallback == null) return null;
                                fontCallback.callbackFailAsync(-3, handler);
                                return null;
                            }
                            catch (IOException iOException) {
                                break block23;
                            }
                            catch (XmlPullParserException xmlPullParserException) {
                                break block24;
                            }
                        }
                        try {
                            return TypefaceCompat.createFromResourcesFamilyXml((Context)object, (FontResourcesParserCompat.FamilyResourceEntry)object4, (Resources)object2, n, n2, fontCallback, handler, bl);
                        }
                        catch (IOException iOException) {
                            break block23;
                        }
                        catch (XmlPullParserException xmlPullParserException) {
                            break block24;
                        }
                        catch (IOException iOException) {
                            break block23;
                        }
                        catch (XmlPullParserException xmlPullParserException) {
                            break block24;
                        }
                    }
                    object = TypefaceCompat.createFromResourcesFontFile((Context)object, (Resources)object2, n, (String)object3, n2);
                    if (fontCallback == null) return object;
                    if (object == null) ** GOTO lbl54
                    try {
                        fontCallback.callbackSuccessAsync((Typeface)object, handler);
                        return object;
lbl54: // 1 sources:
                        fontCallback.callbackFailAsync(-3, handler);
                        return object;
                    }
                    catch (IOException iOException) {
                        break block23;
                    }
                    catch (XmlPullParserException xmlPullParserException) {
                        break block24;
                    }
                    catch (IOException iOException) {
                        break block23;
                    }
                    catch (XmlPullParserException xmlPullParserException) {
                        break block24;
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                object2 = new StringBuilder();
                object2.append("Failed to read xml resource ");
                object2.append((String)object3);
                Log.e((String)"ResourcesCompat", (String)object2.toString(), (Throwable)var0_10);
                break block25;
                catch (XmlPullParserException xmlPullParserException) {
                    // empty catch block
                }
            }
            object2 = new StringBuilder();
            object2.append("Failed to parse xml resource ");
            object2.append((String)object3);
            Log.e((String)"ResourcesCompat", (String)object2.toString(), (Throwable)var0_12);
        }
        if (fontCallback == null) return null;
        fontCallback.callbackFailAsync(-3, handler);
        return null;
    }

    public static abstract class FontCallback {
        public final void callbackFailAsync(final int n, Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(){

                @Override
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(n);
                }
            });
        }

        public final void callbackSuccessAsync(final Typeface typeface, Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(){

                @Override
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        public abstract void onFontRetrievalFailed(int var1);

        public abstract void onFontRetrieved(Typeface var1);

    }

}

