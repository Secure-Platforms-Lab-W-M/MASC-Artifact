// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.text;

import android.text.TextUtils;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.util.Locale;

public final class TextUtilsCompat
{
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    @Deprecated
    public static final Locale ROOT;
    
    static {
        ROOT = new Locale("", "");
    }
    
    private TextUtilsCompat() {
    }
    
    private static int getLayoutDirectionFromFirstChar(@NonNull final Locale locale) {
        switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
            default: {
                return 0;
            }
            case 1:
            case 2: {
                return 1;
            }
        }
    }
    
    public static int getLayoutDirectionFromLocale(@Nullable final Locale locale) {
        if (Build$VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
        if (locale != null && !locale.equals(TextUtilsCompat.ROOT)) {
            final String maximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
            if (maximizeAndGetScript == null) {
                return getLayoutDirectionFromFirstChar(locale);
            }
            if (maximizeAndGetScript.equalsIgnoreCase("Arab") || maximizeAndGetScript.equalsIgnoreCase("Hebr")) {
                return 1;
            }
        }
        return 0;
    }
    
    @NonNull
    public static String htmlEncode(@NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode(s);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\"') {
                if (char1 != '<') {
                    if (char1 != '>') {
                        switch (char1) {
                            default: {
                                sb.append(char1);
                                break;
                            }
                            case 39: {
                                sb.append("&#39;");
                                break;
                            }
                            case 38: {
                                sb.append("&amp;");
                                break;
                            }
                        }
                    }
                    else {
                        sb.append("&gt;");
                    }
                }
                else {
                    sb.append("&lt;");
                }
            }
            else {
                sb.append("&quot;");
            }
        }
        return sb.toString();
    }
}
