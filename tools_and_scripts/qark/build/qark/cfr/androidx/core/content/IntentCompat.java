/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package androidx.core.content;

import android.content.Intent;
import android.os.Build;

public final class IntentCompat {
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";

    private IntentCompat() {
    }

    public static Intent makeMainSelectorActivity(String string2, String string3) {
        if (Build.VERSION.SDK_INT >= 15) {
            return Intent.makeMainSelectorActivity((String)string2, (String)string3);
        }
        string2 = new Intent(string2);
        string2.addCategory(string3);
        return string2;
    }
}

