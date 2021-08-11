/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.Rating
 */
package android.support.v4.media;

import android.media.Rating;

class RatingCompatApi21 {
    RatingCompatApi21() {
    }

    public static float getPercentRating(Object object) {
        return ((Rating)object).getPercentRating();
    }

    public static int getRatingStyle(Object object) {
        return ((Rating)object).getRatingStyle();
    }

    public static float getStarRating(Object object) {
        return ((Rating)object).getStarRating();
    }

    public static boolean hasHeart(Object object) {
        return ((Rating)object).hasHeart();
    }

    public static boolean isRated(Object object) {
        return ((Rating)object).isRated();
    }

    public static boolean isThumbUp(Object object) {
        return ((Rating)object).isThumbUp();
    }

    public static Object newHeartRating(boolean bl) {
        return Rating.newHeartRating((boolean)bl);
    }

    public static Object newPercentageRating(float f) {
        return Rating.newPercentageRating((float)f);
    }

    public static Object newStarRating(int n, float f) {
        return Rating.newStarRating((int)n, (float)f);
    }

    public static Object newThumbRating(boolean bl) {
        return Rating.newThumbRating((boolean)bl);
    }

    public static Object newUnratedRating(int n) {
        return Rating.newUnratedRating((int)n);
    }
}

