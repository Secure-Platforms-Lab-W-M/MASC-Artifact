/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.Arrays;

class BundleUtil {
    BundleUtil() {
    }

    public static Bundle[] getBundleArrayFromBundle(Bundle bundle, String string2) {
        Parcelable[] arrparcelable = bundle.getParcelableArray(string2);
        if (arrparcelable instanceof Bundle[] || arrparcelable == null) {
            return (Bundle[])arrparcelable;
        }
        arrparcelable = (Bundle[])Arrays.copyOf(arrparcelable, arrparcelable.length, Bundle[].class);
        bundle.putParcelableArray(string2, arrparcelable);
        return arrparcelable;
    }
}

