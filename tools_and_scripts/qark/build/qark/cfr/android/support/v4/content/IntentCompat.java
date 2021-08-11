/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.content;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

public final class IntentCompat {
    @Deprecated
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    @Deprecated
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    @Deprecated
    public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    @Deprecated
    public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
    @Deprecated
    public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
    @Deprecated
    public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
    private static final IntentCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 15 ? new IntentCompatApi15Impl() : new IntentCompatBaseImpl();

    private IntentCompat() {
    }

    @Deprecated
    public static Intent makeMainActivity(ComponentName componentName) {
        return Intent.makeMainActivity((ComponentName)componentName);
    }

    public static Intent makeMainSelectorActivity(String string2, String string3) {
        return IMPL.makeMainSelectorActivity(string2, string3);
    }

    @Deprecated
    public static Intent makeRestartActivityTask(ComponentName componentName) {
        return Intent.makeRestartActivityTask((ComponentName)componentName);
    }

    @RequiresApi(value=15)
    static class IntentCompatApi15Impl
    extends IntentCompatBaseImpl {
        IntentCompatApi15Impl() {
        }

        @Override
        public Intent makeMainSelectorActivity(String string2, String string3) {
            return Intent.makeMainSelectorActivity((String)string2, (String)string3);
        }
    }

    static class IntentCompatBaseImpl {
        IntentCompatBaseImpl() {
        }

        public Intent makeMainSelectorActivity(String string2, String string3) {
            string2 = new Intent(string2);
            string2.addCategory(string3);
            return string2;
        }
    }

}

