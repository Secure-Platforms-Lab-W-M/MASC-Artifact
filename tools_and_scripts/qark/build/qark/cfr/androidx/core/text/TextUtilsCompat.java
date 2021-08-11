/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 */
package androidx.core.text;

import android.os.Build;
import android.text.TextUtils;
import androidx.core.text.ICUCompat;
import java.util.Locale;

public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT = new Locale("", "");

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        byte by = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        if (by != 1 && by != 2) {
            return 0;
        }
        return 1;
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale((Locale)locale);
        }
        if (locale != null && !locale.equals(ROOT)) {
            String string2 = ICUCompat.maximizeAndGetScript(locale);
            if (string2 == null) {
                return TextUtilsCompat.getLayoutDirectionFromFirstChar(locale);
            }
            if (string2.equalsIgnoreCase("Arab") || string2.equalsIgnoreCase("Hebr")) {
                return 1;
            }
        }
        return 0;
    }

    public static String htmlEncode(String string2) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode((String)string2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            if (c != '\"') {
                if (c != '<') {
                    if (c != '>') {
                        if (c != '&') {
                            if (c != '\'') {
                                stringBuilder.append(c);
                                continue;
                            }
                            stringBuilder.append("&#39;");
                            continue;
                        }
                        stringBuilder.append("&amp;");
                        continue;
                    }
                    stringBuilder.append("&gt;");
                    continue;
                }
                stringBuilder.append("&lt;");
                continue;
            }
            stringBuilder.append("&quot;");
        }
        return stringBuilder.toString();
    }
}

