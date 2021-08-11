/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.Objects;

public class ObjectsCompat {
    private static final ImplBase IMPL = Build.VERSION.SDK_INT >= 19 ? new ImplApi19() : new ImplBase();

    private ObjectsCompat() {
    }

    public static boolean equals(Object object, Object object2) {
        return IMPL.equals(object, object2);
    }

    @RequiresApi(value=19)
    private static class ImplApi19
    extends ImplBase {
        private ImplApi19() {
            super();
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return Objects.equals(object, object2);
        }
    }

    private static class ImplBase {
        private ImplBase() {
        }

        public boolean equals(Object object, Object object2) {
            if (!(object == object2 || object != null && object.equals(object2))) {
                return false;
            }
            return true;
        }
    }

}

