// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.text;

import android.text.TextUtils;
import android.os.Build$VERSION;
import java.util.Locale;

public final class TextUtilsCompat
{
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT;
    
    static {
        ROOT = new Locale("", "");
    }
    
    private TextUtilsCompat() {
    }
    
    private static int getLayoutDirectionFromFirstChar(final Locale locale) {
        final byte directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        if (directionality != 1 && directionality != 2) {
            return 0;
        }
        return 1;
    }
    
    public static int getLayoutDirectionFromLocale(final Locale locale) {
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
    
    public static String htmlEncode(final String s) {
        if (Build$VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode(s);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\"') {
                if (char1 != '<') {
                    if (char1 != '>') {
                        if (char1 != '&') {
                            if (char1 != '\'') {
                                sb.append(char1);
                            }
                            else {
                                sb.append("&#39;");
                            }
                        }
                        else {
                            sb.append("&amp;");
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
