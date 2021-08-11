// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import android.os.Looper;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;
import android.os.Handler;
import android.graphics.Typeface;
import android.content.Context;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import android.content.res.Resources$Theme;
import android.content.res.Resources;

public final class ResourcesCompat
{
    private static final String TAG = "ResourcesCompat";
    
    private ResourcesCompat() {
    }
    
    public static int getColor(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColor(n, resources$Theme);
        }
        return resources.getColor(n);
    }
    
    public static ColorStateList getColorStateList(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, resources$Theme);
        }
        return resources.getColorStateList(n);
    }
    
    public static Drawable getDrawable(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, resources$Theme);
        }
        return resources.getDrawable(n);
    }
    
    public static Drawable getDrawableForDensity(final Resources resources, final int n, final int n2, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, resources$Theme);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }
    
    public static float getFloat(final Resources resources, final int n) {
        final TypedValue typedValue = new TypedValue();
        resources.getValue(n, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Resource ID #0x");
        sb.append(Integer.toHexString(n));
        sb.append(" type #0x");
        sb.append(Integer.toHexString(typedValue.type));
        sb.append(" is not valid");
        throw new Resources$NotFoundException(sb.toString());
    }
    
    public static Typeface getFont(final Context context, final int n) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, new TypedValue(), 0, null, null, false);
    }
    
    public static Typeface getFont(final Context context, final int n, final TypedValue typedValue, final int n2, final FontCallback fontCallback) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }
    
    public static void getFont(final Context context, final int n, final FontCallback fontCallback, final Handler handler) throws Resources$NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }
    
    private static Typeface loadFont(final Context context, final int n, final TypedValue typedValue, final int n2, final FontCallback fontCallback, final Handler handler, final boolean b) {
        final Resources resources = context.getResources();
        resources.getValue(n, typedValue, true);
        final Typeface loadFont = loadFont(context, resources, typedValue, n, n2, fontCallback, handler, b);
        if (loadFont != null) {
            return loadFont;
        }
        if (fontCallback != null) {
            return loadFont;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Font resource ID #0x");
        sb.append(Integer.toHexString(n));
        sb.append(" could not be retrieved.");
        throw new Resources$NotFoundException(sb.toString());
    }
    
    private static Typeface loadFont(Context fromResourcesFontFile, final Resources resources, TypedValue string, final int n, final int n2, final FontCallback fontCallback, final Handler handler, final boolean b) {
        if (string.string == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Resource \"");
            sb.append(resources.getResourceName(n));
            sb.append("\" (");
            sb.append(Integer.toHexString(n));
            sb.append(") is not a Font: ");
            sb.append(string);
            throw new Resources$NotFoundException(sb.toString());
        }
        string = (TypedValue)string.string.toString();
        if (!((String)string).startsWith("res/")) {
            if (fontCallback != null) {
                fontCallback.callbackFailAsync(-3, handler);
            }
            return null;
        }
        final Typeface fromCache = TypefaceCompat.findFromCache(resources, n, n2);
        if (fromCache != null) {
            if (fontCallback != null) {
                fontCallback.callbackSuccessAsync(fromCache, handler);
            }
            return fromCache;
        }
        Label_0267: {
            try {
                if (((String)string).toLowerCase().endsWith(".xml")) {
                    try {
                        final FontResourcesParserCompat.FamilyResourceEntry parse = FontResourcesParserCompat.parse((XmlPullParser)resources.getXml(n), resources);
                        if (parse == null) {
                            try {
                                Log.e("ResourcesCompat", "Failed to find font-family tag");
                                if (fontCallback != null) {
                                    fontCallback.callbackFailAsync(-3, handler);
                                }
                                return null;
                            }
                            catch (IOException fromResourcesFontFile) {
                                goto Label_0231;
                            }
                            catch (XmlPullParserException fromResourcesFontFile) {
                                break Label_0267;
                            }
                        }
                        try {
                            return TypefaceCompat.createFromResourcesFamilyXml((Context)fromResourcesFontFile, parse, resources, n, n2, fontCallback, handler, b);
                        }
                        catch (IOException fromResourcesFontFile) {}
                        catch (XmlPullParserException fromResourcesFontFile) {}
                    }
                    catch (IOException fromResourcesFontFile) {
                        goto Label_0231;
                    }
                    catch (XmlPullParserException fromResourcesFontFile) {
                        break Label_0267;
                    }
                }
                try {
                    fromResourcesFontFile = (IOException)TypefaceCompat.createFromResourcesFontFile((Context)fromResourcesFontFile, resources, n, (String)string, n2);
                    if (fontCallback != null) {
                        Label_0201: {
                            if (fromResourcesFontFile == null) {
                                break Label_0201;
                            }
                            try {
                                fontCallback.callbackSuccessAsync((Typeface)fromResourcesFontFile, handler);
                                return (Typeface)fromResourcesFontFile;
                                fontCallback.callbackFailAsync(-3, handler);
                                return (Typeface)fromResourcesFontFile;
                            }
                            catch (IOException fromResourcesFontFile) {
                                goto Label_0231;
                            }
                            catch (XmlPullParserException fromResourcesFontFile) {
                                break Label_0267;
                            }
                        }
                    }
                    return (Typeface)fromResourcesFontFile;
                }
                catch (IOException ex) {}
                catch (XmlPullParserException ex2) {}
            }
            catch (IOException ex3) {}
            catch (XmlPullParserException ex4) {}
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Failed to parse xml resource ");
        sb2.append((String)string);
        Log.e("ResourcesCompat", sb2.toString(), (Throwable)fromResourcesFontFile);
        if (fontCallback != null) {
            fontCallback.callbackFailAsync(-3, handler);
        }
        return null;
    }
    
    public abstract static class FontCallback
    {
        public final void callbackFailAsync(final int n, final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(n);
                }
            });
        }
        
        public final void callbackSuccessAsync(final Typeface typeface, final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }
        
        public abstract void onFontRetrievalFailed(final int p0);
        
        public abstract void onFontRetrieved(final Typeface p0);
    }
}
