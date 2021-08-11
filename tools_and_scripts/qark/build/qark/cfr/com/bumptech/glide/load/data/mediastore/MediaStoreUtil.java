/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 */
package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;
import java.util.List;

public final class MediaStoreUtil {
    private static final int MINI_THUMB_HEIGHT = 384;
    private static final int MINI_THUMB_WIDTH = 512;

    private MediaStoreUtil() {
    }

    public static boolean isMediaStoreImageUri(Uri uri) {
        if (MediaStoreUtil.isMediaStoreUri(uri) && !MediaStoreUtil.isVideoUri(uri)) {
            return true;
        }
        return false;
    }

    public static boolean isMediaStoreUri(Uri uri) {
        if (uri != null && "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority())) {
            return true;
        }
        return false;
    }

    public static boolean isMediaStoreVideoUri(Uri uri) {
        if (MediaStoreUtil.isMediaStoreUri(uri) && MediaStoreUtil.isVideoUri(uri)) {
            return true;
        }
        return false;
    }

    public static boolean isThumbnailSize(int n, int n2) {
        if (n != Integer.MIN_VALUE && n2 != Integer.MIN_VALUE && n <= 512 && n2 <= 384) {
            return true;
        }
        return false;
    }

    private static boolean isVideoUri(Uri uri) {
        return uri.getPathSegments().contains("video");
    }
}

