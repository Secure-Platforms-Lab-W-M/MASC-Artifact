/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class MediaBrowserCompatUtils {
    public static boolean areSameOptions(Bundle bundle, Bundle bundle2) {
        if (bundle == bundle2) {
            return true;
        }
        if (bundle == null) {
            if (bundle2.getInt("android.media.browse.extra.PAGE", -1) == -1 && bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1) {
                return true;
            }
            return false;
        }
        if (bundle2 == null) {
            if (bundle.getInt("android.media.browse.extra.PAGE", -1) == -1 && bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1) {
                return true;
            }
            return false;
        }
        if (bundle.getInt("android.media.browse.extra.PAGE", -1) == bundle2.getInt("android.media.browse.extra.PAGE", -1) && bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) == bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
            return true;
        }
        return false;
    }

    public static boolean hasDuplicatedItems(Bundle bundle, Bundle bundle2) {
        int n = bundle == null ? -1 : bundle.getInt("android.media.browse.extra.PAGE", -1);
        int n2 = bundle2 == null ? -1 : bundle2.getInt("android.media.browse.extra.PAGE", -1);
        int n3 = bundle == null ? -1 : bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        int n4 = bundle2 == null ? -1 : bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if (n != -1 && n3 != -1) {
            n = n3 * n;
            n3 = n + n3 - 1;
        } else {
            n = 0;
            n3 = Integer.MAX_VALUE;
        }
        if (n2 != -1 && n4 != -1) {
            n2 = n4 * n2;
            n4 = n2 + n4 - 1;
        } else {
            n2 = 0;
            n4 = Integer.MAX_VALUE;
        }
        if (n <= n2 && n2 <= n3) {
            return true;
        }
        if (n <= n4 && n4 <= n3) {
            return true;
        }
        return false;
    }
}

